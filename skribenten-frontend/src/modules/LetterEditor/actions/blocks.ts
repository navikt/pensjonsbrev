import {Action} from "../../../lib/actions"
import {AnyBlock, LITERAL} from "../model"
import produce from "immer"
import {cleanseText} from "./content"

const updateBlock: Action<AnyBlock[], [blockId: number, block: AnyBlock]> =
    produce((draft, blockId, block) => {
        draft[blockId] = block
    })

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


function getMergeIds(srcId: number, target: MERGE_TARGET): [number, number] {
    switch (target) {
        case MERGE_TARGET.PREVIOUS:
            return [srcId - 1, srcId]
        case MERGE_TARGET.NEXT:
            return [srcId, srcId + 1]
    }
}
function isEmpty(block: AnyBlock): boolean {
    return block.content.length === 1 && block.content[0].text.length === 0
}

export enum MERGE_TARGET { PREVIOUS = "PREVIOUS", NEXT = "NEXT" }
const mergeWith: Action<AnyBlock[], [blockId: number, target: MERGE_TARGET]> =
    produce((draft, blockId, target) => {
        const [firstId, secondId] = getMergeIds(blockId, target)
        const first = draft[firstId]
        const second = draft[secondId]

        if (first != null && second != null) {
            if (isEmpty(first)) {
                draft.splice(firstId, 1)
            } else if(isEmpty(second)) {
                draft.splice(secondId, 1)
            } else {
                const lastContentOfFirst = first.content[first.content.length - 1]
                const firstContentOfSecond = second.content[0]

                // merge adjoining literals
                if (lastContentOfFirst.type === LITERAL && firstContentOfSecond.type === LITERAL) {
                    lastContentOfFirst.text += firstContentOfSecond.text
                    second.content.splice(0, 1)
                }
                first.content.splice(first.content.length, 0, ...second.content)
                draft.splice(secondId, 1)
            }
        }
    })

export const BlocksAction = {
    updateBlock, splitBlock, mergeWith,
}