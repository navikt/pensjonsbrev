import {Item, ItemList, TextContent} from "../model/api"
import {Action} from "../../../lib/actions"
import produce from "immer"
import {getMergeIds, isEmptyItem, mergeContentArrays, MergeTarget} from "./common"
import {cleanseText} from "./textcontent"

const updateItem: Action<ItemList, [itemId: number, item: Item]> =
    produce((draft, itemId, item) => {
        draft.items[itemId] = item
    })

const updateItems: Action<ItemList, [items: Item[]]> =
    produce((draft, items) => {
        draft.items = items
    })

const updateItemContent: Action<Item, [contentId: number, content: TextContent]> =
    produce((draft, contentId, content) => {
        draft.content[contentId] = content
    })

const mergeWith: Action<Item[], [itemId: number, target: MergeTarget]> =
    produce((draft, itemId, target) => {
        const [firstId, secondId] = getMergeIds(itemId, target)
        const first = draft[firstId]
        const second = draft[secondId]

        if (first != null && second != null) {
            if (isEmptyItem(first)) {
                draft.splice(firstId, 1)
            } else if (isEmptyItem(second)) {
                draft.splice(secondId, 1)
            } else {
                first.content = mergeContentArrays(first.content, second.content)
                draft.splice(secondId, 1)
            }
        }
    })

const splitItem: Action<Item[], [itemId: number, contentId: number, offset: number]> =
    produce((draft, itemId, contentId, offset) => {
        const item = draft[itemId]
        const prevItem = draft[itemId - 1]
        if (!isEmptyItem(item) && (prevItem == null || !isEmptyItem(prevItem))) {
            const content = item.content[contentId]

            const firstText = cleanseText(content.text.substring(0, offset))
            const secondText = cleanseText(content.text.substring(offset))

            // create new item an insert it after specified item (must copy to new item before modifying specified item)
            const newItem: Item = {...item, content: [{...content, text: secondText}, ...item.content.slice(contentId + 1)]}
            draft.splice(itemId + 1, 0, newItem)

            // split specified content
            item.content.splice(contentId + 1, item.content.length)
            content.text = firstText
        }
    })

export const ItemListAction = {
    updateItems, updateItem, updateItemContent, mergeWith, splitItem,
}