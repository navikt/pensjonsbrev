import {Action} from "../../../lib/actions"
import {AnyBlock, VARIABLE, VariableDecl} from "../model"
import produce from "immer"

const setVariable: Action<AnyBlock[], [decl: VariableDecl]> =
    produce((draft, decl) => {
        draft.forEach(
            block => block.content.forEach(
                content => {
                    if (content.type === VARIABLE && content.name === decl.spec.name) {
                        content.text = decl.value
                    }
                }
            )
        )
    })

const updateBlock: Action<AnyBlock[], [blockId: number, block: AnyBlock]> =
    produce((draft, blockId, block) => {
        draft[blockId] = block
    })

const insertBlock: Action<AnyBlock[], [at: number, block: AnyBlock]> =
    produce((draft, at, block) => {
        draft.splice(at, 0, block)
    })

const splitBlock: Action<AnyBlock[], [blockId: number, block: AnyBlock, contentId: number, firstText: string, secondText: string]> =
    produce((draft, blockId, block, contentId, firstText, secondText) => {
        const newBlock = {
            ...block,
            content: [...block.content.slice(0, contentId), {...(block.content[contentId]), text: firstText}],
        }
        draft[blockId].content.splice(0, contentId)
        draft[blockId].content[0].text = secondText
        draft.splice(blockId, 0,  newBlock)
    })

export const BlocksAction = {
    setVariable, updateBlock, insertBlock, splitBlock,
}