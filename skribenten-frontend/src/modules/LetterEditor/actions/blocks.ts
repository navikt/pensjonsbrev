import {Action} from "../../../lib/actions"
import {AnyBlock, LITERAL, PARAGRAPH, ParagraphBlock, TITLE1, Title1Block, VARIABLE} from "../model/api"
import produce from "immer"
import {cleanseText, MergeTarget} from "./common"
import {getMergeIds, isEmptyContent, mergeContentArrays} from "../model/utils"

const updateBlock: Action<AnyBlock[], [blockId: number, block: AnyBlock]> =
    produce((draft, blockId, block) => {
        draft[blockId] = block
    })

const splitBlock: Action<AnyBlock[], [blockId: number, contentId: number, offset: number]> =
    produce((draft, blockId, contentId, offset) => {
        const block = draft[blockId]
        const prevBlock = draft[blockId - 1]

        if (!isEmptyBlock(block) && (prevBlock == null || !isEmptyBlock(prevBlock))) {
            if (block.type === TITLE1) {
                const content = block.content[contentId]
                const firstText = content.text.substring(0, offset)
                const secondText = content.text.substring(offset)

                const newBlock: Title1Block = {
                    ...block,
                    content: [...block.content.slice(0, contentId), {...content, text: cleanseText(firstText)}],
                }

                block.content.splice(0, contentId)

                content.text = cleanseText(secondText)
                if (content.text.length === 0) {
                    content.id = -1
                }

                draft.splice(blockId, 0, newBlock)
            } else if (block.type === PARAGRAPH) {
                const content = block.content[contentId]
                if (content.type === LITERAL || content.type === VARIABLE) {
                    const firstText = cleanseText(content.text.substring(0, offset))
                    const secondText = cleanseText(content.text.substring(offset))

                    const newBlock: ParagraphBlock = {
                        ...block,
                        id: contentId === 0 && firstText.length < 2 ? -1 : block.id,
                        content: [...block.content.slice(0, contentId), {...content, id: firstText.length < 2 ? -1 : content.id , text: firstText}],
                    }

                    block.content.splice(0, contentId)

                    content.text = secondText
                    if (block.content.length === 1 && content.text.length < 2) {
                        block.id = -1
                        content.id = -1
                    }

                    draft.splice(blockId, 0, newBlock)
                } else {
                    console.warn("Don't know how to split an ItemList content.")
                }
            }
        }
    })


function isEmptyBlock(block: AnyBlock): boolean {
    switch (block.type) {
        case TITLE1:
            return block.content.length === 1 && block.content[0].text.length === 0
        case PARAGRAPH:
            return block.content.length === 1 && isEmptyContent(block.content[0])
    }
}

const mergeWith: Action<AnyBlock[], [blockId: number, target: MergeTarget]> =
    produce((draft, blockId, target) => {
        const [firstId, secondId] = getMergeIds(blockId, target)
        const first = draft[firstId]
        const second = draft[secondId]

        if (first != null && second != null) {
            if (isEmptyBlock(first)) {
                draft.splice(firstId, 1)
            } else if (isEmptyBlock(second)) {
                draft.splice(secondId, 1)
            } else {
                first.content = mergeContentArrays(first.content, second.content)
                draft.splice(secondId, 1)
            }
        }
    })

export const BlocksAction = {
    updateBlock, splitBlock, mergeWith,
}