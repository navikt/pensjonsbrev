import {Action} from "../../../lib/actions"
import {AnyBlock, RenderedLetter} from "../model/api"
import produce from "immer"

const updateBlocks: Action<RenderedLetter, [blocks: AnyBlock[]]> = produce((draft, blocks) => {
    draft.blocks = blocks
})

export const RenderedLetterAction = {
    updateBlocks,
}