import produce from "immer";

import type { Action } from "../lib/actions";
import type { RenderedLetter } from "../lib/model/skribenten";
import type { LetterEditorState } from "../model/state";

export const updateLetter: Action<LetterEditorState, [letter: RenderedLetter]> = produce((draft, letter) => {
  draft.editedLetter.letter = letter;
});
