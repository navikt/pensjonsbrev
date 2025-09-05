import type { BrevInfo } from "~/types/brev";
import type { Content, EditedLetter } from "~/types/brevbakerTypes";

export type ContentGroup = { content: Content[] };

export type BlockContentIndex = { blockIndex: number; contentIndex: number };
export type ItemContentIndex = BlockContentIndex & {
  itemIndex: number;
  itemContentIndex: number;
};

export type TableCellIndex = BlockContentIndex & {
  rowIndex: number;
  cellIndex: number;
  cellContentIndex: number;
};

export type LiteralIndex = BlockContentIndex | ItemContentIndex | TableCellIndex;

export type Focus = LiteralIndex & { cursorPosition?: number };

export type LetterEditorState = {
  info: BrevInfo;
  redigertBrev: EditedLetter;
  redigertBrevHash: string;
  saveStatus: "DIRTY" | "SAVE_PENDING" | "SAVED";
  focus: Focus;
};
