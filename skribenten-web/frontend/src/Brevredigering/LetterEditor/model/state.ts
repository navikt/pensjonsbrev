import type { LiteralIndex } from "~/Brevredigering/LetterEditor/actions/model";
import type { BrevInfo } from "~/types/brev";
import type { Content, EditedLetter } from "~/types/brevbakerTypes";

export type ContentGroup = { content: Content[] };

export type Focus = LiteralIndex & { cursorPosition?: number };
export type LetterEditorState = {
  info: BrevInfo;
  redigertBrev: EditedLetter;
  redigertBrevHash: string;
  isDirty: boolean;
  focus: Focus;
};
