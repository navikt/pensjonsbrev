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

export type Identifiable = {
  readonly id?: number | null;
  readonly parentId?: number | null;
};

export type EditedLetter = generated.Letter;
export type Sakspart = generated.Sakspart;
export type Signatur = generated.Signatur;
export const TITLE_INDEX = -1;
export type Title = generated.Title;

export type AnyBlock = Title1Block | Title2Block | Title3Block | ParagraphBlock;
export type ParagraphBlock = generated.Paragraph;
export type Title1Block = generated.Title1;
export type Title2Block = generated.Title2;
export type Title3Block = generated.Title3;

export type TextContent = generated.Text;
export type Content = generated.ParagraphContent;

export type LiteralValue = generated.Literal;
export type VariableValue = generated.Variable;
export type NewLine = generated.NewLine;
export type ItemList = generated.ItemList;
export type Table = generated.Table;

export type ElementTags = generated.ElementTags;
export const ElementTags: Record<ElementTags, ElementTags> = {
  FRITEKST: "FRITEKST",
  REDIGERBAR_DATA: "REDIGERBAR_DATA",
};

export type FontType = generated.FontType;
export const FontType: Record<FontType, FontType> = {
  PLAIN: "PLAIN",
  BOLD: "BOLD",
  ITALIC: "ITALIC",
};

export type Item = generated.Item;

export type Row = generated.Row;
export type Cell = generated.Cell;
export type Header = generated.Header;
export type ColumnSpec = generated.ColumnSpec;
export type ColumnAlignment = generated.ColumnAlignment;

export interface PropertyUsage {
  readonly typeName: string;
  readonly propertyName: string;
}
