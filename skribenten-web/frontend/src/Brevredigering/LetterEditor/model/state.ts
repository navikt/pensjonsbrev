import type { BrevInfo } from "~/types/brev";
import type { Content, EditedLetter } from "~/types/brevbakerTypes";

import type { HistoryEntry } from "../history";

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
  isDirty: boolean;
  focus: Focus;
  history: HistoryEntry[];
  historyPointer: number;
};
