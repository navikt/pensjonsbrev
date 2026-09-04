// @vitest-environment node
import { renderToStaticMarkup } from "react-dom/server";
import { describe, expect, it } from "vitest";

import {
  ContentOrControlStructureTypeV2,
  type ContentOrControlStructureV2,
  ElementTypeV2,
  type Expr,
  ExprType,
  type OutlineContentV2,
} from "~/api/brevbakerTypesV2";
import { DocumentV2, ExprToText } from "~/components/TemplateDocumentationV2View";

function render(expr: Expr): string {
  return renderToStaticMarkup(<ExprToText expr={expr} />);
}

/** Fjerner HTML-tagger, slik at assertions kan sammenlignes mot den rene teksten som vises. */
function text(html: string): string {
  return html.replace(/<[^>]+>/g, "");
}

describe("ExprToText FieldPath med Computed-base", () => {
  it("rendrer getOrNull som array-access og segmentene som en tydelig avgrenset postfix-kjede", () => {
    // Eksakt strukturen fra legacy-selectoren
    // vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_vilkar_unguforresultat: getOrNull(...)
    // etterfulgt av .vilkar.unguforresultat, pakket i en NullCoalesce med tom streng-fallback.
    const expr: Expr = {
      exprType: ExprType.NULL_COALESCE,
      value: {
        exprType: ExprType.FIELD_PATH,
        source: {
          dataSourceType: "COMPUTED",
          expr: {
            exprType: ExprType.FUNCTION_CALL,
            name: "getOrNull",
            args: [
              {
                exprType: ExprType.FIELD_PATH,
                source: { dataSourceType: "SCOPE", name: "argument" },
                segments: ["pesysData", "pe", "vedtaksbrev", "vedtaksdata", "vilkarsvedtaklist", "vilkarsvedtak"],
                leafType: null,
              },
              { exprType: ExprType.LITERAL, value: "0", kind: "NUMBER" },
            ],
          },
        },
        segments: ["vilkar", "unguforresultat"],
        leafType: null,
      },
      fallback: { exprType: ExprType.LITERAL, value: "", kind: "STRING" },
    };

    const html = render(expr);
    const rendered = text(html);

    // Listen som indekseres skal være tydelig avgrenset til getOrNull sitt første argument
    // (`...vilkarsvedtak[0]`) - de påfølgende `.vilkar.unguforresultat`-segmentene skal IKKE
    // se ut til å høre til selve listeuttrykket. "argument."-prefikset skal være skjult, siden
    // praktisk talt alle uttrykk starter der og det bare er støy.
    expect(rendered).toContain(
      "pesysData.pe.vedtaksbrev.vedtaksdata.vilkarsvedtaklist.vilkarsvedtak[0].vilkar.unguforresultat",
    );
    expect(rendered).not.toContain("argument.");
    // Ingen prosafrase ("element nr. ... i ...") som ikke tydeliggjør grensen mellom
    // funksjonskallet og de påfølgende segmentene.
    expect(rendered).not.toContain("element nr.");
    // Tom streng-fallback skal fortsatt være synlig (kvotert).
    expect(rendered).toContain("&quot;&quot;");
  });
});

describe("ExprToText FieldPath argument-scope", () => {
  it("skjuler 'argument.'-prefikset siden praktisk talt alle uttrykk starter der", () => {
    const expr: Expr = {
      exprType: ExprType.FIELD_PATH,
      source: { dataSourceType: "SCOPE", name: "argument" },
      segments: ["person", "navn"],
      leafType: null,
    };

    expect(text(render(expr))).toBe("person.navn");
  });

  it("beholder navnet til andre scopes enn 'argument' (f.eks. 'felles')", () => {
    const expr: Expr = {
      exprType: ExprType.FIELD_PATH,
      source: { dataSourceType: "SCOPE", name: "felles" },
      segments: ["dato"],
      leafType: null,
    };

    expect(text(render(expr))).toBe("felles.dato");
  });

  it("faller tilbake til 'argument' når det ikke er noen segmenter å vise (hele scopet brukt direkte)", () => {
    const expr: Expr = {
      exprType: ExprType.FIELD_PATH,
      source: { dataSourceType: "SCOPE", name: "argument" },
      segments: [],
      leafType: null,
    };

    expect(text(render(expr))).toBe("argument");
  });
});

function fieldPath(name: string): Expr {
  return {
    exprType: ExprType.FIELD_PATH,
    source: { dataSourceType: "SCOPE", name: "argument" },
    segments: [name],
    leafType: null,
  };
}

describe("ExprToText linjeskift og innrykk for AND/OR-kjeder", () => {
  it("rendrer en flat AND-kjede med flere ledd som en blokk med én rad per ledd, uten nøstet border/innrykk", () => {
    const expr: Expr = {
      exprType: ExprType.ASSOCIATIVE_OP,
      op: "AND",
      operands: [fieldPath("a"), fieldPath("b"), fieldPath("c")],
    };

    const html = render(expr);
    // Kun én blokk (ingen nøsting) siden alle tre ledd er "flate" FieldPath-er.
    expect((html.match(/class="expr-block"/g) ?? []).length).toBe(1);
    expect((html.match(/class="expr-block-row"/g) ?? []).length).toBe(3);
    expect(text(html)).toBe("aog bog c");
  });

  it("rendrer en nøstet OR inni en AND som sin egen innrykkede blokk, uten eksplisitte parenteser", () => {
    const expr: Expr = {
      exprType: ExprType.ASSOCIATIVE_OP,
      op: "AND",
      operands: [
        fieldPath("a"),
        { exprType: ExprType.ASSOCIATIVE_OP, op: "OR", operands: [fieldPath("b"), fieldPath("c")] },
      ],
    };

    const html = render(expr);
    // To nøstede blokker: den ytre AND-blokken og den indre OR-blokken.
    expect((html.match(/class="expr-block"/g) ?? []).length).toBe(2);
    // Ingen parenteser rundt den nøstede OR-en - innrykket/border kommuniserer grupperingen.
    expect(text(html)).not.toContain("(");
    expect(text(html)).toContain("eller");
  });

  it("holder CONCAT/PLUS-kjeder inline (ingen linjeskift/blokk-formattering)", () => {
    const expr: Expr = {
      exprType: ExprType.ASSOCIATIVE_OP,
      op: "CONCAT",
      operands: [{ exprType: ExprType.LITERAL, value: "Hei ", kind: "STRING" }, fieldPath("navn")],
    };

    const html = render(expr);
    expect(html).not.toContain("expr-block");
  });
});

describe("ExprToText linjeskift og innrykk for Conditional (if/then/else)", () => {
  it("rendrer if/then/else som tre separate rader i en blokk (når grenene IKKE begge er literaler)", () => {
    const expr: Expr = {
      exprType: ExprType.CONDITIONAL_EXPR,
      predicate: fieldPath("harBarn"),
      ifTrue: fieldPath("navn"),
      ifElse: { exprType: ExprType.LITERAL, value: "nei", kind: "STRING" },
    };

    const html = render(expr);
    expect((html.match(/class="expr-block-row"/g) ?? []).length).toBe(3);
    const rendered = text(html);
    expect(rendered).toContain("if");
    expect(rendered).toContain("then");
    expect(rendered).toContain("else");
  });
});

/** Innholdet i selve `<button>`-markøren, uten den (skjulte) popover-innholdet ved siden av. */
function buttonText(html: string): string {
  const match = /<button[^>]*>(.*?)<\/button>/s.exec(html);
  return match ? text(match[1]) : "";
}

describe("ExprToText kompakt Conditional-visning for rene literal-grener", () => {
  it("rendrer [ifTrue] når ifElse er tom streng, uten if/then/else-blokk", () => {
    const expr: Expr = {
      exprType: ExprType.CONDITIONAL_EXPR,
      predicate: fieldPath("harBarn"),
      ifTrue: { exprType: ExprType.LITERAL, value: "med barnetillegg", kind: "STRING" },
      ifElse: { exprType: ExprType.LITERAL, value: "", kind: "STRING" },
    };

    const html = render(expr);
    expect(html).not.toContain("expr-block");
    expect(html).toContain("expr-popover-trigger");
    expect(buttonText(html)).toBe("[med barnetillegg]");
    // Predikatet skal IKKE vises direkte i markøren (kun tilgjengelig via popover).
    expect(buttonText(html)).not.toContain("harBarn");
    // Popoveren finnes i DOM-en (for tilgjengelighet), men er skjult som standard.
    expect(html).toContain("aksel-popover--hidden");
    expect(html).toContain("Hvis");
    expect(html).toContain("harBarn");
  });

  it("rendrer ifTrue|ifElse når begge grener har innhold, uten if/then/else-blokk", () => {
    const expr: Expr = {
      exprType: ExprType.CONDITIONAL_EXPR,
      predicate: fieldPath("erGift"),
      ifTrue: { exprType: ExprType.LITERAL, value: "ja", kind: "STRING" },
      ifElse: { exprType: ExprType.LITERAL, value: "nei", kind: "STRING" },
    };

    const html = render(expr);
    expect(html).not.toContain("expr-block");
    expect(buttonText(html)).toBe("ja|nei");
    expect(buttonText(html)).not.toContain("erGift");
    expect(html).toContain("aksel-popover--hidden");
  });
});

describe("ExprToText Format med eksempeltekst", () => {
  it("viser exampleText kompakt med full uttrykk+formatterer tilgjengelig via popover", () => {
    const expr: Expr = {
      exprType: ExprType.FORMAT,
      value: fieldPath("fodselsdato"),
      formatterName: "DateFormat",
      exampleText: "17. mars 2024",
    };

    const html = render(expr);
    expect(html).toContain("expr-popover-trigger");
    expect(buttonText(html)).toBe("17. mars 2024");
    // Den abstrakte "formatert som"-frasen skal IKKE vises i markøren, kun i popoveren.
    expect(buttonText(html)).not.toContain("fodselsdato");
    expect(html).toContain("aksel-popover--hidden");
    expect(html).toContain("fodselsdato");
  });

  it("faller tilbake til <uttrykk> formatert som <formatterer> når exampleText er null", () => {
    const expr: Expr = {
      exprType: ExprType.FORMAT,
      value: fieldPath("ukjentFelt"),
      formatterName: "UkjentFormatter",
      exampleText: null,
    };

    const html = render(expr);
    expect(html).not.toContain("expr-popover-trigger");
    expect(html).not.toContain("aksel-popover--hidden");
    expect(text(html)).toContain("ukjentFelt");
  });
});

function textLiteralParagraph(text: string): ContentOrControlStructureV2<OutlineContentV2> {
  return {
    controlStructureType: ContentOrControlStructureTypeV2.CONTENT,
    content: {
      elementType: ElementTypeV2.PARAGRAPH,
      paragraph: [
        {
          controlStructureType: ContentOrControlStructureTypeV2.CONTENT,
          content: { elementType: ElementTypeV2.PARAGRAPH_TEXT_LITERAL, text },
        },
      ],
    },
  };
}

describe("DocumentV2 block-level Conditional (If/Else If)", () => {
  it("skjuler predikatet bak en lukket <details>/<summary>-disclosure, men viser selve den betingede teksten direkte", () => {
    const html = renderToStaticMarkup(
      <DocumentV2
        templateDocumentation={{
          title: [],
          outline: [
            {
              controlStructureType: ContentOrControlStructureTypeV2.CONDITIONAL,
              predicate: fieldPath("harBarn"),
              showIf: [textLiteralParagraph("Du har barnetillegg.")],
              elseIf: [],
              showElse: [],
            },
          ],
          include: fieldPath("dummy"),
          attachmentData: fieldPath("dummy"),
        }}
      />,
    );

    // <details> uten `open`-attributt er kollapset som standard.
    expect(html).toContain("<details");
    expect(html).not.toContain("<details open");
    expect(html).toContain("<summary");
    // Selve den betingede TEKSTEN (utfallet leseren faktisk vil se i brevet) er alltid synlig,
    // uavhengig av om disclosure-en er åpen - den er ikke skjult bak popover/details.
    expect(text(html)).toContain("Du har barnetillegg.");
    // Predikatet finnes fortsatt i DOM-en (for tilgjengelighet/søk), men bare inni <details>.
    const detailsMatch = /<details[^>]*>(.*?)<\/details>/s.exec(html);
    expect(detailsMatch).not.toBeNull();
    expect(text(detailsMatch?.[1] ?? "")).toContain("harBarn");
  });

  it("rendrer 'Else If' med samme kollapsede disclosure-mønster", () => {
    const html = renderToStaticMarkup(
      <DocumentV2
        templateDocumentation={{
          title: [],
          outline: [
            {
              controlStructureType: ContentOrControlStructureTypeV2.CONDITIONAL,
              predicate: fieldPath("harBarn"),
              showIf: [textLiteralParagraph("Med barnetillegg.")],
              elseIf: [
                { predicate: fieldPath("harEktefelle"), showIf: [textLiteralParagraph("Med ektefelletillegg.")] },
              ],
              showElse: [],
            },
          ],
          include: fieldPath("dummy"),
          attachmentData: fieldPath("dummy"),
        }}
      />,
    );

    expect((html.match(/<summary/g) ?? []).length).toBe(2);
    expect(text(html)).toContain("Else If");
    expect(text(html)).toContain("Med ektefelletillegg.");
  });
});
