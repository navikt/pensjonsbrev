import produce from "immer";

import type { Action } from "../actions";
import type { LetterEditorState } from "../model/state";

export const focusStolen: Action<LetterEditorState, [blockId: number]> = produce((draft, blockId) => {
  delete draft.stealFocus[blockId];
});
