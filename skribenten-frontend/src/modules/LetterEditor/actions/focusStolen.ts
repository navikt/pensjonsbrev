import {Action} from "../../../lib/actions"
import produce from "immer"

import {LetterEditorState} from "../model/state"

export const focusStolen: Action<LetterEditorState, [blockId: number]> =
    produce((draft, blockId) => {
        delete draft.stealFocus[blockId]
    })