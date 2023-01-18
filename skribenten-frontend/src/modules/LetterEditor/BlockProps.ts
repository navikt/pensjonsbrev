import {AnyBlock, TextContent} from "./model"
import {BoundAction} from "../../lib/actions"
import {MERGE_TARGET} from "./actions/blocks"
import {CursorPosition} from "./components/content/Content"

export type Unlock = BoundAction<[]>
export type UpdateContent = BoundAction<[contentId: number, content: TextContent]>
export type SplitBlockAtContent = BoundAction<[contentId: number, currentText: string, nextText: string]>

export interface BlockProps<T extends AnyBlock> {
    block: T
    doUnlock: Unlock
    updateContent: UpdateContent
    mergeWith: BoundAction<[target: MERGE_TARGET]>
    splitBlockAtContent: SplitBlockAtContent
    blockStealFocus: CursorPosition | undefined
    blockFocusStolen: BoundAction<[]>
}