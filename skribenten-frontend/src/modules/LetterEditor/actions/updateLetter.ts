import {Action} from "../../../lib/actions"
import {RenderedLetter} from "../../../lib/model/skribenten"
import produce from "immer"
import {LetterEditorState} from "../model/state"

export const updateLetter: Action<LetterEditorState, [letter: RenderedLetter]> = produce((draft, letter) => {
    draft.editedLetter.letter = letter
})