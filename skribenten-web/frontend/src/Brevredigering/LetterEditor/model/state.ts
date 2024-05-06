import type { LiteralIndex } from "~/Brevredigering/LetterEditor/actions/model";
import type { Content, RenderLetterResponse } from "~/types/brevbakerTypes";

export type ContentGroup = { content: Content[] };

export type Focus = LiteralIndex & { cursorPosition?: number };
export type LetterEditorState = {
  renderedLetter: RenderLetterResponse;
  focus: Focus;
};
