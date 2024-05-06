import type { Content, RenderLetterResponse } from "~/types/brevbakerTypes";
import { ITEM_LIST, VARIABLE } from "~/types/brevbakerTypes";

import type { LetterEditorState } from "../model/state";

export function cleanseText(text: string): string {
  return text.replaceAll("<br>", "").replaceAll("&nbsp;", " ");
}

export function isEditableContent(content: Content | undefined | null): boolean {
  return content != null && (content.type === VARIABLE || content.type === ITEM_LIST);
}

export function create(renderedLetter: RenderLetterResponse): LetterEditorState {
  return {
    renderedLetter,
    focus: { blockIndex: 0, contentIndex: 0 },
  };
}
