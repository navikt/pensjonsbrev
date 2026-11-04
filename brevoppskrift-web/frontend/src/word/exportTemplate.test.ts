import JSZip from "jszip";
import { describe, expect, it } from "vitest";

import {
  type ContentOrControlStructure,
  ContentOrControlStructureType,
  ElementType,
  type Expr,
  ExprType,
  type OutlineContent,
  type ParagraphContent,
  type Row,
  type TemplateDocumentation,
  type Text,
} from "~/api/templateDocumentationTypes";

import { createTemplateWordDocument } from "./exportTemplate";

const W = "http://schemas.openxmlformats.org/wordprocessingml/2006/main";

function content<E>(value: E): ContentOrControlStructure<E> {
  return { controlStructureType: ContentOrControlStructureType.CONTENT, content: value };
}

function literal(text: string): ContentOrControlStructure<Text> {
  return content({ elementType: ElementType.PARAGRAPH_TEXT_LITERAL, text });
}

function field(name: string): Expr {
  return {
    exprType: ExprType.FIELD_PATH,
    source: { dataSourceType: "SCOPE", name: "argument" },
    segments: [name],
    leafType: null,
  };
}

function variable(expression: Expr): ContentOrControlStructure<Text> {
  return content({ elementType: ElementType.PARAGRAPH_TEXT_EXPRESSION, expression });
}

function paragraph(...items: ContentOrControlStructure<ParagraphContent>[]): ContentOrControlStructure<OutlineContent> {
  return content({ elementType: ElementType.PARAGRAPH, index: 0, paragraph: items });
}

function documentation(outline: ContentOrControlStructure<OutlineContent>[]): TemplateDocumentation {
  return {
    title: [literal("Et brev")],
    outline,
    attachments: [],
    templateModelSpecification: { types: {}, letterModelTypeName: "BrevDto" },
  };
}

async function exportXml(doc: TemplateDocumentation) {
  const blob = await createTemplateWordDocument({ documentation: doc, templateId: "TEST", language: "NYNORSK" });
  const buffer = await new Promise<ArrayBuffer>((resolve, reject) => {
    const reader = new FileReader();
    reader.onload = () => resolve(reader.result as ArrayBuffer);
    reader.onerror = () => reject(reader.error);
    reader.readAsArrayBuffer(blob);
  });
  const archive = await JSZip.loadAsync(buffer);
  async function xml(path: string) {
    const source = await archive.file(path)!.async("string");
    const parsed = new DOMParser().parseFromString(source, "application/xml");
    expect(parsed.querySelector("parsererror")).toBeNull();
    return parsed;
  }
  const document = await xml("word/document.xml");
  const comments = archive.file("word/comments.xml")
    ? await xml("word/comments.xml")
    : document.implementation.createDocument(W, "comments");
  const commentById = new Map(
    Array.from(comments.getElementsByTagNameNS(W, "comment"), (comment) => [
      comment.getAttributeNS(W, "id"),
      comment.textContent,
    ]),
  );
  const ranges = new Map<string, string>();
  const active = new Set<string>();
  const ended: string[] = [];
  const referenced: string[] = [];
  for (const element of document.getElementsByTagName("*")) {
    const id = element.getAttributeNS(W, "id");
    if (element.localName === "commentRangeStart") {
      expect(ranges.has(id!)).toBe(false);
      expect(commentById.has(id)).toBe(true);
      ranges.set(id!, "");
      active.add(id!);
    } else if (element.localName === "commentRangeEnd") {
      expect(active.delete(id!)).toBe(true);
      ended.push(id!);
    } else if (element.localName === "commentReference") {
      referenced.push(id!);
    } else if (element.localName === "t") {
      for (const activeId of active) {
        ranges.set(activeId, ranges.get(activeId)! + element.textContent);
      }
    }
  }
  expect(active.size).toBe(0);
  expect([...ranges.keys()].sort()).toEqual([...commentById.keys()].sort());
  expect([...ranges.keys()].sort()).toEqual(ended.sort());
  expect([...ranges.keys()].sort()).toEqual(referenced.sort());
  return {
    document,
    xml,
    text: Array.from(document.getElementsByTagNameNS(W, "t"), (node) => node.textContent).join(""),
    annotationsFor: (text: string) =>
      [...ranges].filter(([, range]) => range.includes(text)).map(([id]) => commentById.get(id)!),
  };
}

describe("Word-eksport av brevoppskrift", () => {
  it("eksporterer alle grener og kommenterer effektive, nøstede betingelser og variabler på riktig tekst", async () => {
    const doc = documentation([
      {
        controlStructureType: ContentOrControlStructureType.CONDITIONAL,
        predicate: field("første"),
        showIf: [paragraph(literal("Første tekst"))],
        elseIf: [
          {
            predicate: field("andre"),
            showIf: [
              {
                controlStructureType: ContentOrControlStructureType.CONDITIONAL,
                predicate: field("indre"),
                showIf: [paragraph(literal("Hei "), variable(field("navn")))],
                elseIf: [],
                showElse: [],
              },
            ],
          },
        ],
        showElse: [paragraph(literal("Siste tekst"))],
      },
    ]);
    const result = await exportXml(doc);

    expect(result.text).toContain("Språk: NYNORSK");
    expect(result.text).toContain("Første tekst");
    expect(result.text).toContain("Hei [navn]");
    expect(result.text).toContain("Siste tekst");
    expect(result.annotationsFor("[navn]")).toEqual([
      expect.stringContaining("ikke (argument.første) og (argument.andre)"),
    ]);
    expect(result.annotationsFor("[navn]")[0]).toContain("Betingelse: (argument.indre)");
    expect(result.annotationsFor("[navn]")[0]).toContain("Variabeluttrykk: argument.navn");
    expect(result.annotationsFor("Siste tekst")[0]).toContain("ikke (argument.første) og ikke (argument.andre)");
    expect(result.annotationsFor("Første tekst")[0]).not.toContain("argument.andre");
  });

  it("bevarer inlinealternativer, konkatenering, formattering, fallback og redigerbare felt", async () => {
    const result = await exportXml(
      documentation([
        paragraph(
          literal("Du får "),
          {
            controlStructureType: ContentOrControlStructureType.CONDITIONAL,
            predicate: field("harBarn"),
            showIf: [literal("barnetillegg")],
            elseIf: [],
            showElse: [literal("ordinær ytelse")],
          },
          variable({
            exprType: ExprType.ASSOCIATIVE_OP,
            op: "CONCAT",
            operands: [
              { exprType: ExprType.LITERAL, kind: "STRING", value: " fra " },
              {
                exprType: ExprType.FORMAT,
                value: field("dato"),
                formatterName: "formatDate",
                exampleText: "01.01.2026",
              },
            ],
          }),
          variable({
            exprType: ExprType.CONDITIONAL_EXPR,
            predicate: field("erGift"),
            ifTrue: { exprType: ExprType.LITERAL, kind: "STRING", value: "gift" },
            ifElse: { exprType: ExprType.LITERAL, kind: "STRING", value: "ugift" },
          }),
          variable({
            exprType: ExprType.NULL_COALESCE,
            value: field("land"),
            fallback: { exprType: ExprType.LITERAL, kind: "STRING", value: "ukjent land" },
          }),
          variable({
            exprType: ExprType.EDITABLE_FIELD,
            kind: "BREVDATA_ELLER_FRITEKST",
            value: field("begrunnelse"),
            fallback: { exprType: ExprType.LITERAL, kind: "STRING", value: "Skriv her" },
          }),
        ),
      ]),
    );

    expect(result.text).toContain("Du får [barnetillegg | ordinær ytelse] fra [argument.dato]");
    expect(result.annotationsFor("barnetillegg")[0]).toContain("argument.harBarn");
    expect(result.annotationsFor("ordinær ytelse")[0]).toContain("ikke (argument.harBarn)");
    expect(result.annotationsFor("[argument.dato]")[0]).toContain("formatDate(argument.dato)");
    expect(result.text).toContain("[gift | ugift]");
    expect(result.annotationsFor("ugift")[0]).toContain("ikke (argument.erGift)");
    expect(result.text).toContain("[[land] | ukjent land]");
    expect(result.annotationsFor("ukjent land")[0]).toContain("argument.land = null");
    expect(result.text).toContain("[BREVDATA_ELLER_FRITEKST][begrunnelse] [alternativ: Skriv her]");
    expect(result.annotationsFor("Skriv her")[0]).toContain("Redigerbart felt:");
  });

  it("bevarer overskrifter, lister, betingede tabellrader, løkker og vedlegg", async () => {
    const row = (text: string): Row => ({
      elementType: ElementType.PARAGRAPH_TABLE_ROW,
      cells: [{ text: [literal(text)] }],
    });
    const doc = documentation([
      content({ elementType: ElementType.TITLE1, index: 0, text: [literal("En overskrift")] }),
      paragraph(
        literal("Før listen"),
        content({
          elementType: ElementType.PARAGRAPH_ITEMLIST,
          type: "NUMMERERT_LISTE",
          items: [
            {
              controlStructureType: ContentOrControlStructureType.FOR_EACH,
              items: field("barn"),
              body: [content({ elementType: ElementType.PARAGRAPH_ITEMLIST_ITEM, text: [literal("Et barn")] })],
            },
          ],
        }),
        literal("Etter listen"),
        content({
          elementType: ElementType.PARAGRAPH_TABLE,
          header: row("Kolonne"),
          rows: [
            {
              controlStructureType: ContentOrControlStructureType.CONDITIONAL,
              predicate: field("visRad"),
              showIf: [content(row("Betinget rad"))],
              elseIf: [],
              showElse: [content(row("Alternativ rad"))],
            },
            {
              controlStructureType: ContentOrControlStructureType.FOR_EACH,
              items: field("rader"),
              body: [content(row("Gjentatt rad"))],
            },
          ],
        }),
      ),
    ]);
    doc.attachments.push({
      title: [literal("Vedleggstittel")],
      outline: [paragraph(literal("Vedleggstekst <&>"))],
      include: field("harVedlegg"),
      attachmentData: field("vedleggsdata"),
    });
    const result = await exportXml(doc);

    expect(result.text).toContain("Før listenEt barnEtter listen");
    expect(result.text).toContain("KolonneBetinget radAlternativ radGjentatt rad");
    expect(result.document.getElementsByTagNameNS(W, "tbl")).toHaveLength(1);
    expect(result.document.getElementsByTagNameNS(W, "tr")).toHaveLength(4);
    expect(result.document.getElementsByTagNameNS(W, "numPr")).toHaveLength(1);
    expect(result.annotationsFor("Et barn")[0]).toContain("Gjentas for hvert element i: argument.barn");
    expect(result.annotationsFor("Betinget rad")[0]).toContain("Betingelse: (argument.visRad)");
    expect(result.annotationsFor("Alternativ rad")[0]).toContain("ikke (argument.visRad)");
    expect(result.annotationsFor("Gjentatt rad")[0]).toContain("Gjentas for hvert element i: argument.rader");
    expect(result.text).toContain("Vedleggstekst <&>");
    expect(result.annotationsFor("Vedlegg 1")[0]).toContain("Vedlegget tas med når: argument.harVedlegg");
    expect(result.annotationsFor("Vedlegg 1")[0]).toContain("Vedleggsdata: argument.vedleggsdata");
    expect(result.document.getElementsByTagNameNS(W, "pageBreakBefore")).toHaveLength(1);
    expect((await result.xml("word/numbering.xml")).getElementsByTagNameNS(W, "numFmt").length).toBeGreaterThan(0);
  });

  it("kan eksportere brev uten variabler eller betingelser", async () => {
    const result = await exportXml(documentation([paragraph(literal("En vanlig tekst\nmed linjeskift"))]));
    expect(result.text).toContain("En vanlig tekst");
    expect(result.annotationsFor("En vanlig tekst")).toEqual([]);
    expect(result.document.getElementsByTagNameNS(W, "br")).toHaveLength(1);
  });
});
