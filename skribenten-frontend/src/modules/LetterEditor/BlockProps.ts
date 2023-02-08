import {AnyBlock, CursorPosition, TextContent} from "./model"
import {BoundAction} from "../../lib/actions"
import {MergeTarget} from "./actions/blocks"

export type Unlock = BoundAction<[]>
export type UpdateContent = BoundAction<[contentId: number, content: TextContent]>
export type SplitBlockAtContent = BoundAction<[contentId: number, currentText: string, nextText: string]>

export interface BlockProps<T extends AnyBlock> {
    block: T
    doUnlock: Unlock
    updateContent: UpdateContent
    mergeWith: BoundAction<[target: MergeTarget]>
    splitBlockAtContent: SplitBlockAtContent
    blockStealFocus: CursorPosition | undefined
    blockFocusStolen: BoundAction<[]>
    onFocus: BoundAction<[]>
}