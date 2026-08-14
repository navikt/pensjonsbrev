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

/**
 * The minimal editable document the letter editor engine operates on. Both EditedLetter
 * (the brev) and EditAttachment (a redigerbart vedlegg) satisfy this shape structurally, so the
 * block/title editing actions can be reused for both without either pretending to be the other.
 * Brev-only metadata (sakspart, signatur) is intentionally excluded — it is a render-time concern.
 */
export type EditedDocument = {
  title: Title;
  blocks: AnyBlock[];
  deletedBlocks: number[];
};

/**
 * Narrows an EditedDocument to a full EditedLetter (which additionally has sakspart/signatur).
 * The brev layer uses this at the points that legitimately need the letter-only fields (sakspart
 * rendering, signatur, saving). A redigerbart vedlegg (EditAttachment) is never a letter, so the
 * brev-specific UI that calls this is simply not rendered for vedlegg.
 */
export const isLetterDocument = (doc: EditedDocument): doc is EditedLetter => "sakspart" in doc && "signatur" in doc;

/**
 * Returns the document as a full EditedLetter. The brev editing/saving layer always holds a letter
 * (built from BrevResponse), so this is a safe narrowing at those letter-only boundaries — it throws
 * if ever called on a vedlegg, which would indicate a wiring bug (a vedlegg has no sakspart/signatur
 * and is saved through its own endpoint, not the brev one).
 */
export const asLetterDocument = (doc: EditedDocument): EditedLetter => {
  if (!isLetterDocument(doc)) {
    throw new Error("Expected a letter document (with sakspart/signatur), but got a vedlegg");
  }
  return doc;
};

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

export type PropertyUsage = generated.LetterMarkupWithDataUsageProperty;
