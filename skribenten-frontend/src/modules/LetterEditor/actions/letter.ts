import {Action} from "../../../lib/actions"
import {AnyBlock, EditedLetter, RenderedLetter} from "../model/api"
import produce from "immer"

const updateBlocks: Action<EditedLetter, [blocks: AnyBlock[]]> = produce((draft, blocks) => {
    draft.letter.blocks = blocks
})

const updateLetter: Action<EditedLetter, [letter: RenderedLetter]> = produce((draft, letter) => {
    draft.letter = letter
})

function create(letter: RenderedLetter): EditedLetter {
    return {letter, deletedBlocks: []}
}

export const EditedLetterAction = {
    create, updateBlocks, updateLetter,
}