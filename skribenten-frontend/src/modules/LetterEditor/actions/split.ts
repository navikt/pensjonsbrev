import {Action} from "../../../lib/actions"
import produce from "immer"
import {Item, ITEM_LIST, LITERAL, PARAGRAPH, ParagraphBlock} from "../../../lib/model/skribenten"
import {isEmptyBlock, isEmptyContent, isEmptyItem} from "../model/utils"
import {cleanseText, isEditableContent} from "./common"
import {ContentId} from "./model"
import {LetterEditorState} from "../model/state"

function getId(id: number, isNew: boolean): number {
    return isNew ? -1 : id
}

export const split: Action<LetterEditorState, [splitId: ContentId, offset: number]> =
    produce((draft, splitId, offset) => {
        const letter = draft.editedLetter.letter
        const block = letter.blocks[splitId.blockId]
        const prevBlock = letter.blocks[splitId.blockId - 1]
        const content = block.content[splitId.contentId]

        if (content.type === LITERAL) {
            if (!isEmptyBlock(block) && (prevBlock == null || offset > 0 || !isEmptyBlock(prevBlock))) {
                // Create next block
                const nextText = cleanseText(content.text.substring(offset))
                const isNew = nextText.length < 2 && block.content.length - splitId.contentId <= 1

                const nextBlock: ParagraphBlock = {
                    type: PARAGRAPH,
                    id: -1, //getId(block.id, isNew),
                    editable: true,
                    content: [
                        {...content, id: getId(content.id, isNew), text: cleanseText(content.text.substring(offset))},
                        ...block.content.slice(splitId.contentId + 1).map(c => ({...c, id: getId(c.id, isNew)})),
                    ],
                }
                letter.blocks.splice(splitId.blockId + 1, 0, nextBlock)
                draft.stealFocus[splitId.blockId + 1] = {contentId: 0, startOffset: 0}

                // Update existing
                content.text = cleanseText(content.text.substring(0, offset))
                block.content.splice(splitId.contentId + 1, block.content.length - splitId.contentId + 1)
                if (isEmptyContent(content) && isEditableContent(block.content[splitId.contentId - 1])) {
                    // We don't want to leave an empty dangling literal if the previous content is editable
                    block.content.splice(splitId.contentId, 1)
                }
            }
        } else if (content.type === ITEM_LIST) {
            if ("itemId" in splitId) {
                const item = content.items[splitId.itemId]
                const prevItem = content.items[splitId.itemId - 1]
                const nextItem = content.items[splitId.itemId + 1]

                if (splitId.itemId === content.items.length - 1 && isEmptyItem(item)) {
                    // We're at the last item, and it's empty, so the split should result in converting it to content in the same block after the ItemList (or steal focus at ít).
                    content.items.splice(splitId.itemId, 1)
                    if (splitId.contentId >= block.content.length - 1) {
                        block.content.push({type: LITERAL, id: -1, text: ""})
                    }
                    draft.stealFocus[splitId.blockId] = {contentId: splitId.contentId + 1, startOffset: 0}
                } else {
                    if (isEmptyItem(item)) {
                        // An empty item would result in two consecutive empty items
                        return
                    }

                    const itemContent = item.content[splitId.itemContentId]

                    const firstText = cleanseText(itemContent.text.substring(0, offset))
                    const secondText = cleanseText(itemContent.text.substring(offset))

                    // create new item
                    const newItem: Item = {...item, content: [{...itemContent, text: secondText}, ...item.content.slice(splitId.itemContentId + 1)]}

                    // new and next item are both empty
                    if (isEmptyItem(newItem) && nextItem != null && isEmptyItem(nextItem)) {
                        return
                    }
                    // previous item and current would both be empty
                    if (prevItem != null && isEmptyItem(prevItem) && splitId.itemContentId === 0 && offset === 0) {
                        return
                    }

                    // insert new item after specified item
                    content.items.splice(splitId.itemId + 1, 0, newItem)

                    // split specified content
                    item.content.splice(splitId.itemContentId + 1, item.content.length)
                    itemContent.text = firstText

                    // steal focus
                    draft.stealFocus[splitId.blockId] = {
                        contentId: splitId.contentId,
                        startOffset: 0,
                        item: {
                            id: splitId.itemId + 1,
                            contentId: 0,
                        },
                    }
                }
            } else {
                console.warn("Can't split an ItemList without itemId")
            }
        }
    })