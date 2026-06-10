import { type Nullable } from "./Nullable";
import type * as generated from "./skribenten-api";

export type LetterModelSpecification = {
  readonly types: ObjectTypeSpecifications;
  readonly letterModelTypeName: string;
};

export type ObjectTypeSpecifications = {
  readonly [name: string]: ObjectTypeSpecification;
};

export type ObjectTypeSpecification = {
  readonly [field: string]: FieldType;
};

export type FieldType = TScalar | TEnum | TArray | TObject;

export type TScalar = {
  readonly type: "scalar";
  readonly nullable: boolean;
  readonly kind: ScalarKind;
  readonly displayText: Nullable<string>;
};
export type TEnum = {
  readonly type: "enum";
  readonly nullable: boolean;
  readonly values: TEnumEntry[];
  readonly displayText: Nullable<string>;
};
export type TEnumEntry = {
  readonly value: string;
  readonly displayText: Nullable<string>;
};
export type TArray = {
  readonly type: "array";
  readonly nullable: boolean;
  readonly items: FieldType;
  readonly displayText: Nullable<string>;
};
export type TObject = {
  readonly type: "object";
  readonly nullable: boolean;
  readonly typeName: string;
  readonly displayText: Nullable<string>;
};

export type ScalarKind = "NUMBER" | "DOUBLE" | "STRING" | "BOOLEAN" | "DATE" | "YEAR";

export type LanguageCode = "BOKMAL" | "NYNORSK" | "ENGLISH";

export type Sakspart = {
  readonly gjelderNavn: string;
  readonly gjelderFoedselsnummer: string;
  readonly annenMottakerNavn?: string;
  readonly saksnummer: string;
  // Formatert som 'yyyy-MM-dd'
  readonly dokumentDato: string;
};

export type Signatur = {
  readonly hilsenTekst: string;
  readonly saksbehandlerRolleTekst: string;
  readonly saksbehandlerNavn?: string;
  readonly attesterendeSaksbehandlerNavn?: string;
  readonly navAvsenderEnhet: string;
};

export type AnyBlock = Title1Block | Title2Block | Title3Block | ParagraphBlock;

export type Identifiable = {
  readonly id?: number | null;
  readonly parentId?: number | null;
};

export type ElementTags = generated.ElementTags;
export const ElementTags: Record<ElementTags, ElementTags> = {
  FRITEKST: "FRITEKST",
  REDIGERBAR_DATA: "REDIGERBAR_DATA",
};

export const LITERAL = "LITERAL";
export type LiteralValue = generated.Literal;
export const VARIABLE = "VARIABLE";
export type VariableValue = generated.Variable;
export const NEW_LINE = "NEW_LINE";
export type NewLine = generated.NewLine;

export type FontType = generated.FontType;
export const FontType: Record<FontType, FontType> = {
  PLAIN: "PLAIN",
  BOLD: "BOLD",
  ITALIC: "ITALIC",
};

export const ITEM_LIST = "ITEM_LIST";
export type ItemList = generated.ItemList;
export type Item = generated.Item;

export const TABLE = "TABLE";
export type Table = generated.Table;
export type Row = generated.Row;
export type Cell = generated.Cell;
export type Header = generated.Header;
export type ColumnSpec = generated.ColumnSpec;
export type ColumnAlignment = generated.ColumnAlignment;

export type TextContent = generated.Text;
export type Content = ItemList | TextContent | Table;

export type Block = Identifiable & {
  readonly type: string;
  readonly locked?: boolean;
  readonly editable?: boolean;
  readonly deletedContent: number[];
  readonly originalType?: typeof PARAGRAPH | typeof TITLE1 | typeof TITLE2 | typeof TITLE3;
};

export const PARAGRAPH = "PARAGRAPH";
export type ParagraphBlock = Block & {
  readonly type: typeof PARAGRAPH;
  readonly content: Content[];
};

export const TITLE1 = "TITLE1";
export type Title1Block = generated.Title1;

export const TITLE2 = "TITLE2";
export type Title2Block = generated.Title2;

export const TITLE3 = "TITLE3";
export type Title3Block = generated.Title3;

export const TITLE_INDEX = -1;
export type Title = generated.Title;

export interface EditedLetter {
  readonly title: Title;
  readonly sakspart: Sakspart;
  readonly blocks: AnyBlock[];
  readonly signatur: Signatur;
  readonly deletedBlocks: number[];
}

export interface PropertyUsage {
  readonly typeName: string;
  readonly propertyName: string;
}
