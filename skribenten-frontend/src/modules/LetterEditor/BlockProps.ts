import {AnyBlock, Content} from "./model/api"
import {BoundAction} from "../../lib/actions"
import {MergeTarget} from "./actions/common"
import {CursorPosition} from "./model/state"

export type Unlock = BoundAction<[]>
export type UpdateContent<T extends Content> = BoundAction<[contentId: number, content: T]>
export type SplitAtContent = BoundAction<[contentId: number, offset: number]>

export interface BlockProps<T extends AnyBlock> {
    block: T
    updateContent: UpdateContent<Content>
    mergeWith: BoundAction<[target: MergeTarget]>
    splitBlockAtContent: SplitAtContent
    blockStealFocus: CursorPosition | undefined
    blockFocusStolen: BoundAction<[]>
    onFocus: BoundAction<[]>
}