import { produce } from "immer";

import type { Action } from "~/Brevredigering/LetterEditor/lib/actions";
import type { LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";

export const cursorPosition: Action<LetterEditorState, [cursorPosition: number]> = produce((draft, cursorPosition) => {
  draft.focus.cursorPosition = cursorPosition;
});
