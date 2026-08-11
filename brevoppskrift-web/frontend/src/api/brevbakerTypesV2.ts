// Typer for TemplateDocumentationRendererV2 (backend: pensjon/brevbaker,
// no.nav.pensjon.brev.template.render.TemplateDocumentationV2). Holdes som en selvstendig,
// duplisert typefil ved siden av brevbakerTypes.ts (v1) fremfor å dele typer generisk over
// uttrykkstypen, slik at v1 og v2 kan divergere fritt slik de gjør i backend.
import { type LetterModelSpecification } from "~/api/brevbakerTypes";

export type TemplateDocumentationV2 = {
  title: ContentOrControlStructureV2<TextV2>[];
  outline: ContentOrControlStructureV2<OutlineContentV2>[];
  attachments: AttachmentV2[];
  templateModelSpecification: LetterModelSpecification;
};

export type AttachmentV2 = {
  title: ContentOrControlStructureV2<TextV2>[];
  outline: ContentOrControlStructureV2<OutlineContentV2>[];
  include: Expr;
  attachmentData: Expr;
};

export enum ElementTypeV2 {
  TITLE1 = "TITLE1",
  TITLE2 = "TITLE2",
  TITLE3 = "TITLE3",
  PARAGRAPH = "PARAGRAPH",
  PARAGRAPH_TEXT_LITERAL = "PARAGRAPH_TEXT_LITERAL",
  PARAGRAPH_TEXT_EXPRESSION = "PARAGRAPH_TEXT_EXPRESSION",
  PARAGRAPH_ITEMLIST = "PARAGRAPH_ITEMLIST",
  PARAGRAPH_ITEMLIST_ITEM = "PARAGRAPH_ITEMLIST_ITEM",
  PARAGRAPH_TABLE = "PARAGRAPH_TABLE",
  PARAGRAPH_TABLE_ROW = "PARAGRAPH_TABLE_ROW",
}

export type ElementV2 = OutlineContentV2 | ParagraphContentV2;
export type OutlineContentV2 = Title1V2 | Title2V2 | Title3V2 | ParagraphV2;

export type Title1V2 = {
  elementType: ElementTypeV2.TITLE1;
  text: ContentOrControlStructureV2<TextV2>[];
};
export type Title2V2 = {
  elementType: ElementTypeV2.TITLE2;
  text: ContentOrControlStructureV2<TextV2>[];
};
export type Title3V2 = {
  elementType: ElementTypeV2.TITLE3;
  text: ContentOrControlStructureV2<TextV2>[];
};
export type ParagraphV2 = {
  elementType: ElementTypeV2.PARAGRAPH;
  paragraph: ContentOrControlStructureV2<ParagraphContentV2>[];
};

export type ParagraphContentV2 = TextV2 | ItemListV2 | ItemV2 | TableV2 | RowV2;
export type TextV2 = TextLiteralV2 | TextExpressionV2;
export type TextLiteralV2 = { text: string; elementType: ElementTypeV2.PARAGRAPH_TEXT_LITERAL };
export type TextExpressionV2 = { expression: Expr; elementType: ElementTypeV2.PARAGRAPH_TEXT_EXPRESSION };
export type ItemListV2 = {
  elementType: ElementTypeV2.PARAGRAPH_ITEMLIST;
  items: ContentOrControlStructureV2<ItemV2>[];
};
export type ItemV2 = {
  elementType: ElementTypeV2.PARAGRAPH_ITEMLIST_ITEM;
  text: ContentOrControlStructureV2<TextV2>[];
};
export type TableV2 = {
  header: RowV2;
  rows: ContentOrControlStructureV2<RowV2>[];
  elementType: ElementTypeV2.PARAGRAPH_TABLE;
};
export type RowV2 = {
  cells: CellV2[];
  elementType: ElementTypeV2.PARAGRAPH_TABLE_ROW;
};
export type CellV2 = {
  text: ContentOrControlStructureV2<TextV2>[];
};

export enum ContentOrControlStructureTypeV2 {
  CONTENT = "CONTENT",
  CONDITIONAL = "CONDITIONAL",
  FOR_EACH = "FOR_EACH",
}

export type ContentOrControlStructureV2<E> = ContentV2<E> | ConditionalV2<E> | ForEachV2<E>;

export type ContentV2<E> = {
  content: E;
  controlStructureType: ContentOrControlStructureTypeV2.CONTENT;
};
export type ConditionalV2<E> = {
  controlStructureType: ContentOrControlStructureTypeV2.CONDITIONAL;
  predicate: Expr;
  showIf: ContentOrControlStructureV2<E>[];
  elseIf: ElseIfV2<E>[];
  showElse: ContentOrControlStructureV2<E>[];
};
export type ElseIfV2<E> = {
  predicate: Expr;
  showIf: ContentOrControlStructureV2<E>[];
};
export type ForEachV2<E> = {
  controlStructureType: ContentOrControlStructureTypeV2.FOR_EACH;
  items: Expr;
  body: ContentOrControlStructureV2<E>[];
};

// --- Expr ------------------------------------------------------------------

export enum ExprType {
  LITERAL = "LITERAL",
  FIELD_PATH = "FIELD_PATH",
  ASSOCIATIVE_OP = "ASSOCIATIVE_OP",
  COMPARISON = "COMPARISON",
  NOT = "NOT",
  FUNCTION_CALL = "FUNCTION_CALL",
  FORMAT = "FORMAT",
  CONDITIONAL_EXPR = "CONDITIONAL_EXPR",
  NULL_COALESCE = "NULL_COALESCE",
  EDITABLE_FIELD = "EDITABLE_FIELD",
}

export type ScalarKindV2 = "NUMBER" | "DOUBLE" | "STRING" | "BOOLEAN" | "DATE";

export type Expr =
  | ExprLiteral
  | ExprFieldPath
  | ExprAssociativeOp
  | ExprComparison
  | ExprNot
  | ExprFunctionCall
  | ExprFormat
  | ExprConditional
  | ExprNullCoalesce
  | ExprEditableField;

export type ExprLiteral = {
  exprType: ExprType.LITERAL;
  value: string;
  kind: ScalarKindV2 | null;
};

export type ExprFieldPath = {
  exprType: ExprType.FIELD_PATH;
  source: DataSource;
  segments: string[];
  leafType: string | null;
  leafOwnerType?: string | null;
};

export type AssocOp = "AND" | "OR" | "CONCAT" | "PLUS";

export type ExprAssociativeOp = {
  exprType: ExprType.ASSOCIATIVE_OP;
  op: AssocOp;
  operands: Expr[];
};

export type CompareOp =
  | "EQUAL"
  | "NOT_EQUAL"
  | "GREATER_THAN"
  | "GREATER_THAN_OR_EQUAL"
  | "LESS_THAN"
  | "LESS_THAN_OR_EQUAL";

export type ExprComparison = {
  exprType: ExprType.COMPARISON;
  left: Expr;
  op: CompareOp;
  right: Expr;
};

export type ExprNot = {
  exprType: ExprType.NOT;
  term: Expr;
};

export type ExprFunctionCall = {
  exprType: ExprType.FUNCTION_CALL;
  name: string;
  args: Expr[];
};

export type ExprFormat = {
  exprType: ExprType.FORMAT;
  value: Expr;
  formatterName: string;
};

/** Erstatter v1s `Invoke(IfElse, first, Tuple(ifTrue, ifElse))`-innpakking. */
export type ExprConditional = {
  exprType: ExprType.CONDITIONAL_EXPR;
  predicate: Expr;
  ifTrue: Expr;
  ifElse: Expr;
};

/** `?:` / `IfNull` — vist eksplisitt fremfor skjult slik v1 gjorde for `?: false`. */
export type ExprNullCoalesce = {
  exprType: ExprType.NULL_COALESCE;
  value: Expr;
  fallback: Expr;
};

export type EditableKind = "FRITEKST" | "REDIGERBAR_DATA" | "BREVDATA_ELLER_FRITEKST";

export type ExprEditableField = {
  exprType: ExprType.EDITABLE_FIELD;
  kind: EditableKind;
  value?: Expr;
  fallback?: Expr;
};

/**
 * `DataSource` er nå diskriminert i backend via `@JsonTypeInfo(property = "dataSourceType")`
 * (samme mønster som `Expr`): `SCOPE` for scope-baserte kilder (argument/felles/language),
 * `FOR_EACH_VAR` for løkkevariabler, og `COMPUTED` for feltaksess på et vilkårlig beregnet
 * uttrykk (f.eks. `getOrNull(...).felt` eller `(a ?: b).felt`) — sistnevnte lar en `FieldPath`
 * bygges videre på toppen av et hvilket som helst `Expr`, ikke bare Scope/ForEachVar.
 */
export type DataSource = DataSourceScope | DataSourceForEachVar | DataSourceComputed;
export type DataSourceScope = { dataSourceType: "SCOPE"; name: string };
export type DataSourceForEachVar = { dataSourceType: "FOR_EACH_VAR"; label: string; depth: number };
export type DataSourceComputed = { dataSourceType: "COMPUTED"; expr: Expr };

export function isDataSourceScope(source: DataSource): source is DataSourceScope {
  return source.dataSourceType === "SCOPE";
}
