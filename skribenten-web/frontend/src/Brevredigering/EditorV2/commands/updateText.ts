import { type Caret, resolveText } from "../model/paths";
import { isLiteral, setText } from "../model/text";
import { type Command } from "../state/editorState";

export const updateText: Command<[at: Caret, text: string]> = (draft, at, text) => {
  const literal = resolveText(draft.redigertBrev, at);
  if (!isLiteral(literal)) return;
  setText(literal, text);
  draft.focus = at;
};

export const moveFocus: Command<[at: Caret]> = (draft, at) => {
  draft.focus = at;
};

export const refocus: Command<[]> = (draft) => {
  draft.focus = { ...draft.focus };
};
