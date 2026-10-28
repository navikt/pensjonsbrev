// Typer for TemplateDocumentationRenderer (backend: pensjon/brevbaker,
// no.nav.pensjon.brev.template.render.TemplateDocumentation).
import { type LetterModelSpecification } from "~/api/brevbakerTypes";

export type TemplateDocumentation = {
  title: ContentOrControlStructure<Text>[];
  outline: ContentOrControlStructure<OutlineContent>[];
  attachments: Attachment[];
  templateModelSpecification: LetterModelSpecification;
};

export type Attachment = {
  title: ContentOrControlStructure<Text>[];
  outline: ContentOrControlStructure<OutlineContent>[];
  include: Expr;
  attachmentData: Expr;
};

export enum ElementType {
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

export type Element = OutlineContent | ParagraphContent;
export type OutlineContent = Title1 | Title2 | Title3 | Paragraph;

export type Title1 = {
  elementType: ElementType.TITLE1;
  index: number;
  text: ContentOrControlStructure<Text>[];
};
export type Title2 = {
  elementType: ElementType.TITLE2;
  index: number;
  text: ContentOrControlStructure<Text>[];
};
export type Title3 = {
  elementType: ElementType.TITLE3;
  index: number;
  text: ContentOrControlStructure<Text>[];
};
export type Paragraph = {
  elementType: ElementType.PARAGRAPH;
  index: number;
  paragraph: ContentOrControlStructure<ParagraphContent>[];
};

export type ParagraphContent = Text | ItemList | Item | Table | Row;
export type Text = TextLiteral | TextExpression;
export type TextLiteral = { text: string; elementType: ElementType.PARAGRAPH_TEXT_LITERAL };
export type TextExpression = { expression: Expr; elementType: ElementType.PARAGRAPH_TEXT_EXPRESSION };
/** Speiler backends `no.nav.brev.Listetype`. */
export type Listetype = "PUNKTLISTE" | "NUMMERERT_LISTE";
export type ItemList = {
  elementType: ElementType.PARAGRAPH_ITEMLIST;
  items: ContentOrControlStructure<Item>[];
  type: Listetype;
};
export type Item = {
  elementType: ElementType.PARAGRAPH_ITEMLIST_ITEM;
  text: ContentOrControlStructure<Text>[];
};
export type Table = {
  header: Row;
  rows: ContentOrControlStructure<Row>[];
  elementType: ElementType.PARAGRAPH_TABLE;
};
export type Row = {
  cells: Cell[];
  elementType: ElementType.PARAGRAPH_TABLE_ROW;
};
export type Cell = {
  text: ContentOrControlStructure<Text>[];
};

export enum ContentOrControlStructureType {
  CONTENT = "CONTENT",
  CONDITIONAL = "CONDITIONAL",
  FOR_EACH = "FOR_EACH",
}

export type ContentOrControlStructure<E> = Content<E> | Conditional<E> | ForEach<E>;

export type Content<E> = {
  content: E;
  controlStructureType: ContentOrControlStructureType.CONTENT;
};
export type Conditional<E> = {
  controlStructureType: ContentOrControlStructureType.CONDITIONAL;
  predicate: Expr;
  showIf: ContentOrControlStructure<E>[];
  elseIf: ElseIf<E>[];
  showElse: ContentOrControlStructure<E>[];
};
export type ElseIf<E> = {
  predicate: Expr;
  showIf: ContentOrControlStructure<E>[];
};
export type ForEach<E> = {
  controlStructureType: ContentOrControlStructureType.FOR_EACH;
  items: Expr;
  body: ContentOrControlStructure<E>[];
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

export type ScalarKind = "NUMBER" | "DOUBLE" | "STRING" | "BOOLEAN" | "DATE";

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
  kind: ScalarKind | null;
};

export type ExprFieldPath = {
  exprType: ExprType.FIELD_PATH;
  source: DataSource;
  segments: string[];
  leafType: string | null;
  leafOwnerType?: string | null;
  exampleText?: string | null;
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
  /**
   * Syntetisk, men 100% korrekt formattert eksempel (produsert av den ekte
   * `LocalizedFormatter`), eller `null` hvis formattereren er ukjent eller
   * formatteringen feilet for eksempelverdien.
   */
  exampleText: string | null;
};

/** Betinget uttrykk med predikat og verdier for sant/usant. */
export type ExprConditional = {
  exprType: ExprType.CONDITIONAL_EXPR;
  predicate: Expr;
  ifTrue: Expr;
  ifElse: Expr;
};

/** `?:` / `IfNull` — vist eksplisitt i dokumentasjonen. */
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
