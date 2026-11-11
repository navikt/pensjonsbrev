import { type components } from "./skribenten-api";

type Schemas = components["schemas"];

export type LetterV2 = Schemas["EditV2Letter"];
export type AttachmentV2 = Schemas["EditV2Attachment"];
export type DocumentV2 = Pick<LetterV2, "title" | "blocks" | "deletedBlocks">;

export type TitleV2 = Schemas["EditV2Title"];
export type SaksinformasjonV2 = Schemas["EditV2Saksinformasjon"];
export type SignaturV2 = Schemas["EditV2Signatur"];

export type BlockV2 = Schemas["EditV2Block"];
export type BlockTypeV2 = Schemas["EditV2BlockType"];
export type ParagraphV2 = Schemas["EditV2BlockParagraph"];
export type Title2V2 = Schemas["EditV2BlockTitle2"];
export type Title3V2 = Schemas["EditV2BlockTitle3"];
export type Title4V2 = Schemas["EditV2BlockTitle4"];
export type TextBlockV2 = ParagraphV2 | Title2V2 | Title3V2 | Title4V2;
export type TextBlockTypeV2 = TextBlockV2["type"];
export type ItemListV2 = Schemas["EditV2BlockItemList"];
export type NumberedListV2 = Schemas["EditV2BlockNumberedList"];
export type ListBlockV2 = ItemListV2 | NumberedListV2;
export type ListBlockTypeV2 = ListBlockV2["type"];
export type ItemV2 = Schemas["EditV2Item"];
export type TableV2 = Schemas["EditV2BlockTable"];
export type TableHeaderV2 = Schemas["EditV2BlockTableHeader"];
export type ColumnSpecV2 = Schemas["EditV2BlockTableColumnSpec"];
export type ColumnAlignmentV2 = Schemas["EditV2BlockTableColumnAlignment"];
export type RowV2 = Schemas["EditV2BlockTableRow"];
export type CellV2 = Schemas["EditV2BlockTableCell"];

export type TextV2 = Schemas["EditV2Text"];
export type LiteralV2 = Schemas["EditV2TextLiteral"];
export type VariableV2 = Schemas["EditV2TextVariable"];
export type NewLineV2 = Schemas["EditV2TextNewLine"];
export type FontTypeV2 = Schemas["EditV2FontType"];
export type EditBehaviourV2 = Schemas["EditV2EditBehaviour"];

export type BrevResponseV2 = Schemas["ApiV2BrevResponse"];
export type OppdaterBrevRequestV2 = Pick<BrevResponseV2, "saksbehandlerValg" | "redigertBrev">;
export type OppdaterAttesteringRequestV2 = Pick<BrevResponseV2, "redigertBrev">;
export type RedigertVedleggRequestV2 = { redigertVedlegg: AttachmentV2 };
export type MarkupVersjon = Schemas["MarkupVersjon"];

export type ContentIndexV2 = Schemas["ContentIndexV2"];
export type DiffSegmentV2 = Schemas["DiffSegmentV2"];
export type DiffResponseV2 = Schemas["DiffBrevHandlerResponseV2"];
export type SplitDiffV2 = Schemas["DiffBrevHandlerResponseV2Split"];
export type UnifiedDiffV2 = Schemas["DiffBrevHandlerResponseV2Unified"];
export type BlockEditV2 = Schemas["UnifiedDiffV2BlockEdit"];
export type TextBlockEditV2 = Schemas["UnifiedDiffV2BlockEditTextBlockEdit"];
export type ListBlockEditV2 = Schemas["UnifiedDiffV2BlockEditListBlockEdit"];
export type TableEditV2 = Schemas["UnifiedDiffV2BlockEditTableEdit"];
export type RowEditV2 = Schemas["UnifiedDiffV2RowEdit"];
export type TextOnlyEditV2 = Schemas["UnifiedDiffV2TextOnlyEdit"];
export type TextEditV2 = Schemas["UnifiedDiffV2TextEdit"];

export type IdentifiableV2 = { id?: number | null; parentId?: number | null };
