import type * as generated from "./skribenten-api";

export type LetterModelSpecification = generated.TemplateModelSpecification;
export type ObjectTypeSpecifications = LetterModelSpecification["types"];
export type ObjectTypeSpecification = ObjectTypeSpecifications[keyof ObjectTypeSpecifications];

export type FieldType = generated.TemplateModelSpecificationFieldType;
export type TScalar = generated.TemplateModelSpecificationFieldTypeScalar;
export type TEnum = generated.TemplateModelSpecificationFieldTypeEnum;
export type TEnumEntry = generated.TemplateModelSpecificationFieldTypeEnumEntry;
export type TArray = generated.TemplateModelSpecificationFieldTypeArray;
export type TObject = generated.TemplateModelSpecificationFieldTypeObject;
export type ScalarKind = generated.TemplateModelSpecificationFieldTypeScalarKind;

export type LanguageCode = generated.LanguageCode;

export type Identifiable = {
  readonly id?: number | null;
  readonly parentId?: number | null;
};

export enum ElementTags {
  FRITEKST = "FRITEKST",
}

export const LITERAL = "LITERAL";
export type LiteralValue = Identifiable & {
  readonly type: typeof LITERAL;
  readonly text: string;
  readonly editedText: string | null;
  readonly fontType: FontType;
  readonly editedFontType: Nullable<FontType>;
  readonly tags: ElementTags[];
};
export const VARIABLE = "VARIABLE";
export type VariableValue = Identifiable & {
  readonly type: typeof VARIABLE;
  readonly name?: string;
  readonly text: string;
  readonly fontType: FontType;
};
export const NEW_LINE = "NEW_LINE";
export type NewLine = Identifiable & {
  readonly type: typeof NEW_LINE;
  readonly text: string;
};

export enum FontType {
  PLAIN = "PLAIN",
  BOLD = "BOLD",
  ITALIC = "ITALIC",
}

export enum ListType {
  PUNKTLISTE = "PUNKTLISTE",
  NUMMERERT_LISTE = "NUMMERERT_LISTE",
}

export const ITEM_LIST = "ITEM_LIST";
export type ItemList = Identifiable & {
  readonly type: typeof ITEM_LIST;
  readonly listType: ListType;
  readonly editedListType: ListType | null;
  readonly items: Item[];
  readonly deletedItems: number[];
};
export type Item = Identifiable & {
  readonly content: TextContent[];
  readonly deletedContent: number[];
};

export const TABLE = "TABLE";
export type Table = Identifiable & {
  readonly type: typeof TABLE;
  readonly rows: Row[];
  readonly header: Header;
  readonly deletedRows: number[];
};
export type Row = Identifiable & {
  readonly cells: Cell[];
};
export type Cell = Identifiable & {
  readonly text: TextContent[];
};
export type Header = Identifiable & {
  readonly colSpec: ColumnSpec[];
};
export type ColumnSpec = Identifiable & {
  readonly headerContent: Cell;
  readonly alignment: ColumnAlignment;
  readonly span: number;
};
export type ColumnAlignment = "LEFT" | "RIGHT";

export type TextContent = LiteralValue | VariableValue | NewLine;
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
export type Title1Block = Block & {
  readonly type: typeof TITLE1;
  readonly content: TextContent[];
};

export const TITLE2 = "TITLE2";
export type Title2Block = Block & {
  readonly type: typeof TITLE2;
  readonly content: TextContent[];
};

export const TITLE3 = "TITLE3";
export type Title3Block = Block & {
  readonly type: typeof TITLE3;
  readonly content: TextContent[];
};

export type EditedLetter = generated.EditLetter;
export type Sakspart = generated.LetterMarkupSakspart;
export type Signatur = generated.LetterMarkupSignatur;
export const TITLE_INDEX = -1;
export type Title = generated.EditTitle;

export type AnyBlock = Title1Block | Title2Block | Title3Block | ParagraphBlock;
export type ParagraphBlock = generated.EditBlockParagraph;
export type Title1Block = generated.EditBlockTitle1;
export type Title2Block = generated.EditBlockTitle2;
export type Title3Block = generated.EditBlockTitle3;

export type TextContent = generated.EditParagraphContentText;
export type Content = generated.EditParagraphContent;

export type LiteralValue = generated.EditParagraphContentTextLiteral;
export type VariableValue = generated.EditParagraphContentTextVariable;
export type NewLine = generated.EditParagraphContentTextNewLine;
export type ItemList = generated.EditParagraphContentItemList;
export type Table = generated.EditParagraphContentTable;

export type ElementTags = generated.ElementTags;
export const ElementTags: Record<ElementTags, ElementTags> = {
  FRITEKST: "FRITEKST",
  REDIGERBAR_DATA: "REDIGERBAR_DATA",
};

export type FontType = generated.EditParagraphContentTextFontType;
export const FontType: Record<FontType, FontType> = {
  PLAIN: "PLAIN",
  BOLD: "BOLD",
  ITALIC: "ITALIC",
};

export type Item = generated.EditParagraphContentItemListItem;

export type Row = generated.EditParagraphContentTableRow;
export type Cell = generated.EditParagraphContentTableCell;
export type Header = generated.EditParagraphContentTableHeader;
export type ColumnSpec = generated.EditParagraphContentTableColumnSpec;
export type ColumnAlignment = generated.EditParagraphContentTableColumnAlignment;

export interface PropertyUsage {
  readonly typeName: string;
  readonly propertyName: string;
}
