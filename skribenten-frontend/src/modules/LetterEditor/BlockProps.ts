import {AnyBlock} from "./model/api"
import {BoundAction, CallbackReceiver} from "../../lib/actions"
import {CursorPosition, LetterEditorState} from "./model/state"

export interface BlockProps<T extends AnyBlock> {
    block: T
    blockId: number
    updateLetter: CallbackReceiver<LetterEditorState>
    blockStealFocus: CursorPosition | undefined
    blockFocusStolen: BoundAction<[]>
    onFocus: BoundAction<[]>
}