import type { ContentIndex } from "~/pages/Brevredigering/LetterEditor/actions/model";
import type { Content, EditedLetter } from "~/types/brevbakerTypes";

export type ContentGroup = { content: Content[] };

export type Focus = ContentIndex & { cursorPosition?: number };
export type LetterEditorState = {
  readonly editedLetter: EditedLetter;
  readonly focus: Focus;
};
