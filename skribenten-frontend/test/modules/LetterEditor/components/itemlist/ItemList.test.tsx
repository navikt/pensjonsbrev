import * as Model from "../../../../../src/modules/LetterEditor/model/api"
import {LiteralValue} from "../../../../../src/modules/LetterEditor/model/api"
import userEvent from "@testing-library/user-event"
import ItemList from "../../../../../src/modules/LetterEditor/components/itemlist/ItemList"
import {afterEach, beforeEach, describe, expect, test, vi} from "vitest"
import {cleanup, render, screen} from "@testing-library/react"
import {ItemListAction} from "../../../../../src/modules/LetterEditor/actions/itemlist"
import {TextContentAction} from "../../../../../src/modules/LetterEditor/actions/textcontent"
import {MergeTarget} from "../../../../../src/modules/LetterEditor/actions/common"
import {isEmptyItem} from "../../../../../src/modules/LetterEditor/model/utils"


const itemList: Model.ItemList = {
    id: 1,
    type: Model.ITEM_LIST,
    items: [
        {content: [{type: Model.LITERAL, id: 1, text: "item 1, text 1"}, {type: Model.LITERAL, id: 2, text: "item 1, text 2"}]},
        {content: [{type: Model.LITERAL, id: 3, text: "item 2, text 1"}, {type: Model.LITERAL, id: 4, text: "item 2, text 2"}]},
    ]
}

const user = userEvent.setup()

const updateList = vi.fn<[Model.ItemList]>()

const component = <ItemList
    itemList={itemList}
    editable={true}
    updateList={updateList}
/>

beforeEach(() => {
    render(component)
})

afterEach(() => {
    cleanup()
    vi.restoreAllMocks()
})

describe('updateList', () => {
    test('text changes are propagated', async () => {
        const item = itemList.items[1]
        const originalText = item.content[0].text
        const addedText = ': with a change'
        const span = screen.getByText(originalText)

        await user.click(span)
        await user.keyboard(addedText)

        const expected = updateItemContent(itemList, 1, 0, originalText + addedText)
        expect(expected.items[1].content[0].text).toEqual(originalText + addedText)
        expect(updateList).toHaveBeenLastCalledWith(expected)
    })
})

describe('backspaceHandler', () => {
    test('backspace at the beginning of the first item does not trigger merging in itemList', async () => {
        const item = itemList.items[0]
        const span = screen.getByText(item.content[0].text)

        await user.click(span)
        await user.keyboard("{Home}{Backspace}")

        expect(updateList).toHaveBeenCalledWith(itemList)
    })
    test('backspace at the beginning of the second item triggers merging with the first', async () => {
        const second = itemList.items[1]
        const span = screen.getByText(second.content[0].text)

        await user.click(span)
        await user.keyboard("{Home}{Backspace}")

        expect(updateList).toHaveBeenCalledWith(ItemListAction.updateItems(itemList, ItemListAction.mergeWith(itemList.items, 1, MergeTarget.PREVIOUS)))
    })
    test('backspace not at the beginning of the second item does not trigger merging in itemList', async () => {
        const item = itemList.items[1]
        const text = item.content[0].text
        const span = screen.getByText(text)

        await user.click(span)
        await user.keyboard("{End}{Backspace}")

        expect(updateList).toHaveBeenCalled()
        expect(updateList.mock.lastCall![0].items.length).toEqual(2)
        expect(updateList).toHaveBeenCalledWith(updateItemContent(itemList, 1, 0, text.substring(0, text.length - 1)))
    })
})

describe('deleteHandler', () => {
    test('delete at the end of the last item does not trigger merging in itemList', async () => {
        const item = itemList.items[1]
        const span = screen.getByText(item.content[1].text)

        await user.click(span)
        await user.keyboard("{End}{Delete}")

        expect(updateList).toHaveBeenCalledWith(itemList)
    })
    test('delete at the end of the first item triggers merging with the second', async () => {
        const first = itemList.items[0]
        const span = screen.getByText(first.content[1].text)

        await user.click(span)
        await user.keyboard("{End}{Delete}")

        expect(updateList).toHaveBeenCalledWith(ItemListAction.updateItems(itemList, ItemListAction.mergeWith(itemList.items, 0, MergeTarget.NEXT)))
    })
    test('delete not at the end of the first item does not trigger merging in itemList', async () => {
        const item = itemList.items[0]
        const text = item.content[1].text
        const span = screen.getByText(text)

        await user.click(span)
        await user.keyboard("{Home}{Delete}")

        expect(updateList).toHaveBeenCalled()
        expect(updateList.mock.lastCall![0].items.length).toEqual(2)
        expect(updateList).toHaveBeenCalledWith(updateItemContent(itemList, 0, 1, text.substring(1, text.length)))
    })
})

describe('enterHandler', () => {
    test('enter at the very end of first item triggers split with empty text for new item', async () => {
        const itemId = 0
        const contentId = 1
        const text = itemList.items[itemId].content[contentId].text
        const span = screen.getByText(text)

        await user.click(span)
        await user.keyboard("{End}{Enter}")

        expect(updateList).toHaveBeenCalled()
        expect(updateList.mock.lastCall![0].items.length).toEqual(itemList.items.length + 1)
        expect(updateList.mock.lastCall![0].items[itemId + 1]).toSatisfy(isEmptyItem)
        expect(updateList).toHaveBeenLastCalledWith(ItemListAction.updateItems(itemList, ItemListAction.splitItem(itemList.items, itemId, contentId, text.length)))
    })
    test('enter not at the end of an item triggers split at cursor with content after cursor in the new item', async () => {
        const itemId = 0
        const contentId = 0
        const offset = 4
        const text = itemList.items[itemId].content[contentId].text
        const span = screen.getByText(text)

        await user.click(span)
        await user.keyboard("{Home}{ArrowRight}{ArrowRight}{ArrowRight}{ArrowRight}{Enter}")

        expect(updateList).toHaveBeenCalled()
        expect(updateList.mock.lastCall![0].items.length).toEqual(itemList.items.length + 1)

        // the split item
        const splitItem = updateList.mock.lastCall![0].items[itemId]
        expect(splitItem.content.length).toBe(contentId + 1)
        expect(splitItem.content[contentId].text).toEqual(itemList.items[itemId].content[contentId].text.substring(0, offset))

        // the new item
        const newItem = updateList.mock.lastCall![0].items[itemId + 1]
        expect(newItem.content.length).toBe(itemList.items[itemId].content.length - contentId)
        expect(newItem.content[0].text).toEqual(itemList.items[itemId].content[contentId].text.substring(offset))

        expect(updateList).toHaveBeenLastCalledWith(ItemListAction.updateItems(itemList, ItemListAction.splitItem(itemList.items, itemId, contentId, offset)))
    })
    test('enter at item when the previous item is empty does not split', async () => {
        const itemListWithEmptyItem: Model.ItemList = {
            ...itemList,
            items: [
                itemList.items[0],
                {content: [{id: -1, type: "LITERAL", text: ""}]},
                itemList.items[1]
            ]
        }
        cleanup()
        render(<ItemList itemList={itemListWithEmptyItem} editable={true} updateList={updateList}/>)

        const span = screen.getByText(itemListWithEmptyItem.items[2].content[0].text)

        await user.click(span)
        await user.keyboard("{Home}{Enter}")

        expect(updateList).toHaveBeenCalledWith(itemListWithEmptyItem)
    })
})

function updateItemContent(itemList: Model.ItemList, itemId: number, content: number, text: string): Model.ItemList {
    const item = itemList.items[itemId]
    return ItemListAction.updateItem(itemList, itemId, ItemListAction.updateItemContent(item, content, TextContentAction.updateText(item.content[content] as LiteralValue, text)))
}