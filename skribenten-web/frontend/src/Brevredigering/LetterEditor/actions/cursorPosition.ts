import { produce } from "immer";

import { type Action } from "~/Brevredigering/LetterEditor/lib/actions";
import { type Focus, type LetterEditorState, type SelectionIndex } from "~/Brevredigering/LetterEditor/model/state";

export const cursorPosition: Action<LetterEditorState, [cursorPosition: number]> = produce((draft, cursorPosition) => {
  if (cursorPosition >= 0) {
    draft.focus.cursorPosition = cursorPosition;
  } else {
    draft.focus = { ...draft.focus };
  }
  delete draft.selection;
});

export const updateFocus: Action<LetterEditorState, [focus: Focus]> = produce((draft, focus) => {
  draft.focus = focus;
  delete draft.selection;
});

export const updateSelection: Action<LetterEditorState, [selection: SelectionIndex | undefined, focus?: Focus]> =
  produce((draft, selection, focus) => {
    if (focus) {
      draft.focus = focus;
    }
    if (selection) {
      draft.selection = selection;
    } else {
      delete draft.selection;
    }
  });
