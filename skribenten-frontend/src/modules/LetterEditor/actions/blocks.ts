import {Action} from "../../../lib/actions"
import {AnyBlock, LITERAL} from "../model"
import produce from "immer"

const updateBlock: Action<AnyBlock[], [blockId: number, block: AnyBlock]> =
    produce((draft, blockId, block) => {
        draft[blockId] = block
    })

function cleanseText(text: string): string {
    return text.replaceAll("<br>", "")
}

const splitBlock: Action<AnyBlock[], [blockId: number, block: AnyBlock, contentId: number, firstText: string, secondText: string]> =
    produce((draft, blockId, block, contentId, firstText, secondText) => {
        //TODO: Må også sette id til -1 om blokk med tom tekst er over.
        const newBlock = {
            ...block,
            content: [...block.content.slice(0, contentId), {...(block.content[contentId]), text: cleanseText(firstText)}],
        }

        draft[blockId].content.splice(0, contentId)
        const content = draft[blockId].content[0]
        content.text = cleanseText(secondText)
        if (content.text.length === 0) {
            content.id = -1
        }

        draft.splice(blockId, 0, newBlock)
    })

const mergeWithPreviousBlock: Action<AnyBlock[], [blockId: number]> =
    produce((draft, blockId) => {
        const block = draft[blockId]
        const prev = draft[blockId - 1]

        if (block != null && prev != null) {
            if (prev.content.length === 1 && prev.content[0].text === "") {
                draft.splice(blockId - 1, 1)
            } else {
                const lastContent = prev.content[prev.content.length - 1]
                const firstContent = block.content[0]

                // merge adjoining literals
                if (lastContent.type === LITERAL && firstContent.type === LITERAL) {
                    lastContent.text += firstContent.text
                    block.content.splice(0, 1)
                }
                prev.content.splice(prev.content.length, 0, ...block.content)
                draft.splice(blockId, 1)
            }
        }
    })

export const BlocksAction = {
    updateBlock, splitBlock, mergeWithPreviousBlock,
}