import { produce } from "immer";

import type { RenderedLetter } from "~/types/brevbakerTypes";

import type { Action } from "../lib/actions";
import type { LetterEditorState } from "../model/state";

export const updateLetter: Action<LetterEditorState, [letter: RenderedLetter]> = produce((draft, letter) => {
  draft.editedLetter.letter = letter;
});
