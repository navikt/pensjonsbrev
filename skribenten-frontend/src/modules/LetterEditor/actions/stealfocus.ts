import {AnyBlock, StealFocus} from "../model"
import {Action} from "../../../lib/actions"
import produce from "immer"
import {MergeTarget} from "./blocks"

const onMerge: Action<StealFocus, [blocks: AnyBlock[], blockId: number, target: MergeTarget]> =
    produce((draft, blocks, blockId, target) => {
        switch (target) {
            case MergeTarget.PREVIOUS:
                const prev = blocks[blockId - 1]
                if (prev) {
                    const lastContentId = prev.content.length - 1
                    draft[blockId - 1] = { contentId: lastContentId, startOffset: prev.content[lastContentId].text.length }
                }
                break
            case MergeTarget.NEXT:
                const current = blocks[blockId]
                if (current) {
                    const lastContentId = current.content.length - 1
                    draft[blockId] = { contentId: lastContentId, startOffset: current.content[lastContentId].text.length }
                }
                break
        }
    })

const onSplit: Action<StealFocus, [blockId: number]> =
    produce((draft, blockId) => {
        draft[blockId + 1]= { contentId: 0, startOffset: 0 }
    })

const focusStolen: Action<StealFocus, [blockId: number]> =
    produce((draft, blockId) => {
        delete draft[blockId]
    })

export const StealFocusAction = {
    onMerge, onSplit, focusStolen,
}