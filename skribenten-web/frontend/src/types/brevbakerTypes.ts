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

export type ListType = generated.Listetype;
export const ListType: Record<ListType, ListType> = {
  PUNKTLISTE: "PUNKTLISTE",
  NUMMERERT_LISTE: "NUMMERERT_LISTE",
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
