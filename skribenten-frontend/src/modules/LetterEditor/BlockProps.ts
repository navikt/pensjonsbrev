import {AnyBlock, TextContent} from "./model"
import {BoundAction} from "../../lib/actions"

export type Unlock = BoundAction<[]>
export type UpdateContent = BoundAction<[contentId: number, content: TextContent]>
export type SplitBlockAtContent = BoundAction<[contentId: number, currentText: string, nextText: string]>

export interface BlockProps<T extends AnyBlock> {
    block: T
    doUnlock: Unlock
    updateContent: UpdateContent
    mergeWithPrevious: BoundAction<[]>
    splitBlockAtContent: SplitBlockAtContent
    blockStealFocus: boolean
    blockFocusStolen: BoundAction<[]>
}