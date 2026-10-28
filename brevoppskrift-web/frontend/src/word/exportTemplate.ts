import {
  CommentRangeEnd,
  CommentRangeStart,
  CommentReference,
  Document,
  HeadingLevel,
  type ICommentOptions,
  type INumberingOptions,
  LevelFormat,
  Packer,
  Paragraph,
  type ParagraphChild,
  Table,
  TableCell,
  TableRow,
  TextRun,
  WidthType,
} from "docx";

import {
  type ContentOrControlStructure,
  ContentOrControlStructureType,
  ElementType,
  type Expr,
  ExprType,
  type ParagraphContent,
  type Row,
  type TemplateDocumentation,
  type Text,
} from "~/api/templateDocumentationTypes";

import { expressionText } from "./expressions";

type Block = Paragraph | Table;
type Part = ParagraphChild | Block;
type Context = readonly string[];

function textRuns(text: string): TextRun[] {
  return text.split(/\r\n|\r|\n/).map((line, index) => new TextRun({ text: line, break: index > 0 ? 1 : 0 }));
}

export type TemplateExport = {
  documentation: TemplateDocumentation;
  templateId: string;
  language: string;
};

// Each branch carries its effective condition, including the preceding branches that must not match.
function expand<E, R>(
  items: ContentOrControlStructure<E>[],
  context: Context,
  render: (content: E, context: Context) => R[],
  joinBranches: (branches: R[][]) => R[] = (branches) => branches.flat(),
): R[] {
  return items.flatMap((item) => {
    switch (item.controlStructureType) {
      case ContentOrControlStructureType.CONTENT:
        return render(item.content, context);
      case ContentOrControlStructureType.FOR_EACH:
        return expand(
          item.body,
          [...context, `Gjentas for hvert element i: ${expressionText(item.items)}`],
          render,
          joinBranches,
        );
      case ContentOrControlStructureType.CONDITIONAL: {
        const excluded: string[] = [];
        const branches: R[][] = [];
        for (const branch of [{ predicate: item.predicate, showIf: item.showIf }, ...item.elseIf]) {
          const predicate = expressionText(branch.predicate);
          branches.push(
            expand(
              branch.showIf,
              [...context, `Betingelse: ${[...excluded, `(${predicate})`].join(" og ")}`],
              render,
              joinBranches,
            ),
          );
          excluded.push(`ikke (${predicate})`);
        }
        if (item.showElse.length > 0) {
          branches.push(
            expand(item.showElse, [...context, `Betingelse: ${excluded.join(" og ")}`], render, joinBranches),
          );
        }
        return joinBranches(branches);
      }
    }
    throw new Error("Ukjent kontrollstruktur i brevoppskriften");
  });
}

class TemplateWordRenderer {
  readonly comments: ICommentOptions[] = [];
  readonly numbering: INumberingOptions["config"][number][] = [];

  annotate(children: ParagraphChild[], annotations: Context): ParagraphChild[] {
    if (annotations.length === 0 || children.length === 0) return children;
    const id = this.comments.length;
    this.comments.push({
      id,
      author: "Brevoppskrift",
      initials: "BO",
      children: annotations.map((text) => new Paragraph({ text })),
    });
    return [
      new CommentRangeStart(id),
      ...children,
      new CommentRangeEnd(id),
      new TextRun({ children: [new CommentReference(id)] }),
    ];
  }

  alternatives<T extends Part>(branches: T[][]): (T | TextRun)[] {
    return [
      new TextRun("["),
      ...branches.flatMap((branch, index) => [...(index > 0 ? [new TextRun(" | ")] : []), ...branch]),
      new TextRun("]"),
    ];
  }

  expression(expr: Expr, context: Context): ParagraphChild[] {
    switch (expr.exprType) {
      case ExprType.LITERAL:
        return this.annotate(textRuns(expr.value), context);
      case ExprType.ASSOCIATIVE_OP:
        if (expr.op === "CONCAT") {
          return expr.operands.flatMap((operand) => this.expression(operand, context));
        }
        break;
      case ExprType.CONDITIONAL_EXPR: {
        const predicate = expressionText(expr.predicate);
        return this.alternatives([
          this.expression(expr.ifTrue, [...context, `Betingelse: ${predicate}`]),
          this.expression(expr.ifElse, [...context, `Betingelse: ikke (${predicate})`]),
        ]);
      }
      case ExprType.NULL_COALESCE: {
        const value = expressionText(expr.value);
        return this.alternatives([
          this.expression(expr.value, [...context, `Betingelse: ${value} != null`]),
          this.expression(expr.fallback, [...context, `Betingelse: ${value} = null`]),
        ]);
      }
      case ExprType.EDITABLE_FIELD:
        return this.annotate(
          [
            new TextRun(`[${expr.kind}]`),
            ...(expr.value ? this.expression(expr.value, []) : []),
            ...(expr.fallback
              ? [new TextRun(" [alternativ: "), ...this.expression(expr.fallback, []), new TextRun("]")]
              : []),
          ],
          [...context, `Redigerbart felt: ${expressionText(expr)}`],
        );
      case ExprType.FIELD_PATH:
      case ExprType.COMPARISON:
      case ExprType.NOT:
      case ExprType.FUNCTION_CALL:
      case ExprType.FORMAT:
        break;
    }
    const label =
      expr.exprType === ExprType.FIELD_PATH
        ? expr.segments.join(".") || expressionText(expr)
        : expr.exprType === ExprType.FORMAT
          ? expressionText(expr.value)
          : expressionText(expr);
    return this.annotate(
      [new TextRun({ text: `[${label}]`, shading: { fill: "EEEEEE" } })],
      [...context, `Variabeluttrykk: ${expressionText(expr)}`],
    );
  }

  text(items: ContentOrControlStructure<Text>[], context: Context): ParagraphChild[] {
    return expand(
      items,
      context,
      (content, conditions) =>
        content.elementType === ElementType.PARAGRAPH_TEXT_LITERAL
          ? this.annotate(textRuns(content.text), conditions)
          : this.expression(content.expression, conditions),
      (branches) => this.alternatives(branches),
    );
  }

  paragraph(parts: Part[]): Block[] {
    const blocks: Block[] = [];
    let runs: ParagraphChild[] = [];
    const flush = () => {
      if (runs.length > 0) {
        blocks.push(new Paragraph({ children: runs }));
        runs = [];
      }
    };
    for (const part of parts) {
      if (part instanceof Paragraph || part instanceof Table) {
        flush();
        blocks.push(part);
      } else {
        runs.push(part);
      }
    }
    flush();
    return blocks;
  }

  row(row: Row, context: Context, header = false): TableRow {
    return new TableRow({
      tableHeader: header,
      children: row.cells.map(
        (cell) =>
          new TableCell({
            children: [new Paragraph({ children: this.text(cell.text, context) })],
          }),
      ),
    });
  }

  content(content: ParagraphContent, context: Context): Part[] {
    switch (content.elementType) {
      case ElementType.PARAGRAPH_TEXT_LITERAL:
        return this.annotate(textRuns(content.text), context);
      case ElementType.PARAGRAPH_TEXT_EXPRESSION:
        return this.expression(content.expression, context);
      case ElementType.PARAGRAPH_ITEMLIST: {
        const reference = `list-${this.numbering.length}`;
        this.numbering.push({
          reference,
          levels: [
            {
              level: 0,
              format: content.type === "NUMMERERT_LISTE" ? LevelFormat.DECIMAL : LevelFormat.BULLET,
              text: content.type === "NUMMERERT_LISTE" ? "%1." : "•",
              style: { paragraph: { indent: { left: 720, hanging: 360 } } },
            },
          ],
        });
        return expand(content.items, context, (item, conditions) => [
          new Paragraph({
            children: this.text(item.text, conditions),
            numbering: { reference, level: 0 },
          }),
        ]);
      }
      case ElementType.PARAGRAPH_ITEMLIST_ITEM:
        return [new Paragraph({ children: this.text(content.text, context) })];
      case ElementType.PARAGRAPH_TABLE:
        return [
          new Table({
            width: { size: 100, type: WidthType.PERCENTAGE },
            rows: [
              this.row(content.header, context, true),
              ...expand(content.rows, context, (row, conditions) => [this.row(row, conditions)]),
            ],
          }),
        ];
      case ElementType.PARAGRAPH_TABLE_ROW:
        return content.cells.flatMap((cell) => [new Paragraph({ children: this.text(cell.text, context) })]);
    }
  }

  document(documentation: Pick<TemplateDocumentation, "title" | "outline">): Block[] {
    return [
      new Paragraph({ heading: HeadingLevel.HEADING_1, children: this.text(documentation.title, []) }),
      ...expand(documentation.outline, [], (content, context) => {
        if (content.elementType === ElementType.PARAGRAPH) {
          const parts = expand(
            content.paragraph,
            context,
            (element, conditions) => this.content(element, conditions),
            (branches) => {
              if (branches.flat().some((part) => part instanceof Paragraph || part instanceof Table)) {
                return branches.flat();
              }
              return this.alternatives(branches);
            },
          );
          return this.paragraph(parts);
        }
        const heading = {
          [ElementType.TITLE1]: HeadingLevel.HEADING_2,
          [ElementType.TITLE2]: HeadingLevel.HEADING_3,
          [ElementType.TITLE3]: HeadingLevel.HEADING_4,
        }[content.elementType];
        return [new Paragraph({ heading, children: this.text(content.text, context) })];
      }),
    ];
  }
}

export async function createTemplateWordDocument({
  documentation,
  templateId,
  language,
}: TemplateExport): Promise<Blob> {
  const renderer = new TemplateWordRenderer();
  const children: Block[] = [
    new Paragraph({ text: `Brevoppskrift: ${templateId}`, heading: HeadingLevel.TITLE }),
    new Paragraph({ text: `Språk: ${language}` }),
    new Paragraph({
      text: "Alle tekstalternativer er tatt med, også betinget innhold og vedlegg. Variabler er markert med hakeparenteser, og tekstalternativer i løpende tekst med [alternativ | alternativ]. Word-kommentarene viser uttrykk, betingelser og løkker. Løkkens innhold vises én gang.",
    }),
    ...renderer.document(documentation),
  ];
  for (const [index, attachment] of documentation.attachments.entries()) {
    children.push(
      new Paragraph({
        heading: HeadingLevel.HEADING_1,
        pageBreakBefore: true,
        children: renderer.annotate(
          [new TextRun(`Vedlegg ${index + 1}`)],
          [
            `Vedlegget tas med når: ${expressionText(attachment.include)}`,
            `Vedleggsdata: ${expressionText(attachment.attachmentData)}`,
          ],
        ),
      }),
      ...renderer.document(attachment),
    );
  }
  return Packer.toBlob(
    new Document({
      title: `Brevoppskrift: ${templateId}`,
      creator: "Brevoppskrift",
      styles: {
        default: {
          document: { run: { font: "Arial", size: 22 }, paragraph: { spacing: { after: 160 } } },
        },
      },
      comments: { children: renderer.comments },
      numbering: { config: renderer.numbering },
      sections: [{ children }],
    }),
  );
}
