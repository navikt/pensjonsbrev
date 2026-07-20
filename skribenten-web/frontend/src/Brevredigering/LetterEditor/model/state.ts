import { type BrevInfo, type SaksbehandlerValg } from "~/types/brev";
import { type Content, type EditedLetter } from "~/types/brevbakerTypes";

import { type History } from "../history";

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

export type Focus = LiteralIndex & {
  cursorPosition?: number;
  /**
   * When true, and the focused literal is an untouched fritekst, ContentGroup.tsx selects its full
   * text instead of placing a caret — replicating the click behavior, for focus changes that don't
   * originate from a real DOM focus/click event (e.g. jumping to the first unedited fritekst felt).
   * Cleared automatically once handleOnFocus rebuilds `focus` from `literalIndex` on the resulting
   * native focus event.
   */
  selectAll?: boolean;
};

export type LetterEditorState = {
  info: BrevInfo;
  redigertBrev: EditedLetter;
  redigertBrevHash: string;
  saksbehandlerValg: SaksbehandlerValg;
  saveStatus: "DIRTY" | "SAVE_PENDING" | "SAVED";
  focus: Focus;
  history: History;
};

export type SelectionIndex = {
  start: Focus & { cursorPosition: number };
  end: Focus & { cursorPosition: number };
};
