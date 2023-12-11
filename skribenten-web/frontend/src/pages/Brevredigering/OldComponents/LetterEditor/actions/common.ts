import type { Content, RenderedLetter } from "../lib/model/skribenten";
import { ITEM_LIST, VARIABLE } from "../lib/model/skribenten";
import type { LetterEditorState } from "../model/state";

export function cleanseText(text: string): string {
  return text.replaceAll("<br>", "").replaceAll("&nbsp;", " ");
}

export function isEditableContent(content: Content | undefined | null): boolean {
  return content != null && (content.type === VARIABLE || content.type === ITEM_LIST);
}

export function create(letter: RenderedLetter): LetterEditorState {
  return {
    editedLetter: {
      letter,
      deletedBlocks: [],
    },
    stealFocus: {},
  };
}
