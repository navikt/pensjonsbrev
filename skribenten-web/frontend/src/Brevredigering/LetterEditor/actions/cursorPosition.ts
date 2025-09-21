import { produce } from "immer";

import { type Action, withPatches } from "~/Brevredigering/LetterEditor/lib/actions";
import type { Focus, LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";

export const cursorPosition: Action<LetterEditorState, [cursorPosition: number]> = produce((draft, cursorPosition) => {
  draft.focus.cursorPosition = cursorPosition;
});

export const updateCursorPosition: Action<LetterEditorState, [focus: Focus]> = withPatches((draft, focus) => {
  draft.focus = focus;
});

export const updateCursorPositionNoHistory: Action<LetterEditorState, [focus: Focus]> = produce((draft, focus) => {
  draft.focus = focus;
});
