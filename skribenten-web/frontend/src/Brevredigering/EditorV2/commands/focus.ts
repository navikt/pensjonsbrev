import { type Draft } from "immer";

import { type TextContainer } from "../model/paths";
import { caretIn, endOf } from "../model/structure";
import { type EditorStateV2 } from "../state/editorState";

export function focusIn(draft: Draft<EditorStateV2>, container: TextContainer | undefined, linear = 0): void {
  const focus = container && caretIn(draft.redigertBrev, container, linear);
  if (focus) draft.focus = focus;
}

export function focusEnd(draft: Draft<EditorStateV2>, container: TextContainer | undefined): void {
  if (container) focusIn(draft, container, endOf(container));
}
