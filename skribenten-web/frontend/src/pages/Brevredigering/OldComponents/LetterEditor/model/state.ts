import type { Content, EditedLetter } from "../lib/model/skribenten";

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

export interface LetterEditorState {
  readonly editedLetter: EditedLetter;
  readonly stealFocus: StealFocus;
}
