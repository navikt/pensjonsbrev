import type { Content, EditedLetter } from "~/types/brevbakerTypes";

export type CursorPosition = {
  contentId: number;
  startOffset: number;
  item?: {
    id: number;
    contentId: number;
  };
};
export type StealFocus = {
  [blockId: number]: CursorPosition | undefined;
};
export type ContentGroup = { content: Content[] };

export type LetterEditorState = {
  readonly editedLetter: EditedLetter;
  readonly stealFocus: StealFocus;
};
