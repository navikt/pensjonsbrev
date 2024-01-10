import type { Content, EditedLetter } from "~/types/brevbakerTypes";

export type CursorPosition = {
  contentIndex: number;
  startOffset: number;
  item?: {
    id: number;
    contentIndex: number;
  };
};

export type ContentGroup = { content: Content[] };

export type NextFocus = {
  blockIndex: number;
  contentIndex: number;
  cursorPosition: number;
};

export type LetterEditorState = {
  readonly editedLetter: EditedLetter;
  readonly nextFocus?: NextFocus;
  readonly currentBlock: number;
};
