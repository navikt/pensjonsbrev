import {describe, expect, test} from "vitest"
import {MergeTarget} from "../../../../src/modules/LetterEditor/actions/common"
import {ITEM_LIST, ItemList, LITERAL, VARIABLE} from "../../../../src/modules/LetterEditor/model/api"
import {ItemListAction} from "../../../../src/modules/LetterEditor/actions/itemlist"

const itemList: ItemList = {
    id: 1,
    type: ITEM_LIST,
    items: [{
        content: [
            {id: 1, type: LITERAL, text: "Hyggelig å møte deg "},
            {id: 2, type: VARIABLE, text: "Ole"},
            {id: 3, type: LITERAL, text: ". Sees på "},
            {id: 4, type: VARIABLE, text: "fredag"},
        ]
    }, {
        content: [
            {id: 1, type: LITERAL, text: "Da kan vi diskutere planen videre."},
        ]
    }, {
        content: [
            {id: 1, type: LITERAL, text: "Det blir "},
            {id: 2, type: VARIABLE, text: "spennende"},
        ]
    }]
}
const itemListSecondEmpty: ItemList = {
    id: 2,
    type: ITEM_LIST,
    items: [
        {
            content: [
                {id: 1, type: LITERAL, text: "ABC"},
            ],
        }, {
            content: [
                {id: 1, type: LITERAL, text: ""},
            ],
        }, {
            content: [
                {id: 1, type: LITERAL, text: "DEF"},
            ],
        },
    ]
}

describe("mergeWith", () => {
    describe("next", () => {
        test("the specified items are merged and the number of items reduced", () => {
            const result = ItemListAction.mergeWith(itemList.items, 1, MergeTarget.NEXT)
            expect(result).toHaveLength(itemList.items.length - 1)
        })

        test("merge is ignored if the specified item is the last", () => {
            const result = ItemListAction.mergeWith(itemList.items, itemList.items.length - 1, MergeTarget.NEXT)
            expect(result).toEqual(itemList.items)
        })

        test("the content of the next item is added to the end of the specified", () => {
            const result = ItemListAction.mergeWith(itemList.items, 0, MergeTarget.NEXT)
            expect(result[0].content).toEqual(itemList.items[0].content.concat(itemList.items[1].content))
        })

        test("adjoining literal content in merging items are joined", () => {
            const result = ItemListAction.mergeWith(itemList.items, 1, MergeTarget.NEXT)
            expect(result[1].content).toHaveLength(2)
            expect(result[1].content[0].text).toEqual(itemList.items[1].content[0].text + itemList.items[2].content[0].text)
        })
    })

    describe("previous", () => {
        test("the specified items are merged and the number of items reduced", () => {
            const result = ItemListAction.mergeWith(itemList.items, 1, MergeTarget.PREVIOUS)
            expect(result).toHaveLength(itemList.items.length - 1)
        })

        test("merge is ignored if the specified item is the first", () => {
            const result = ItemListAction.mergeWith(itemList.items, 0, MergeTarget.PREVIOUS)
            expect(result).toEqual(itemList.items)
        })

        test("the content of the specified item is added to the end of the previous", () => {
            const result = ItemListAction.mergeWith(itemList.items, 1, MergeTarget.PREVIOUS)
            expect(result[0].content).toEqual(itemList.items[0].content.concat(itemList.items[1].content))
        })

        test("adjoining literal content in merging items are joined", () => {
            const result = ItemListAction.mergeWith(itemList.items, 2, MergeTarget.PREVIOUS)
            expect(result[1].content).toHaveLength(2)
            expect(result[1].content[0].text).toEqual(itemList.items[1].content[0].text + itemList.items[2].content[0].text)
        })
    })
})

describe('splitItem', () => {
    test('specified item is split at contentId and offset', () => {
        const itemId = 0
        const contentId = 2
        const offset = 2

        const result = ItemListAction.splitItem(itemList.items, itemId, contentId, offset)
        expect(result.length).toBe(itemList.items.length + 1)

        // split item
        const splitItem = result[itemId]
        expect(splitItem.content.length).toBe(contentId + 1)
        expect(splitItem.content[contentId].text).toEqual(itemList.items[itemId].content[contentId].text.substring(0, offset))

        // new item
        const newItem = result[itemId + 1]
        expect(newItem.content.length).toBe(itemList.items[itemId].content.length - contentId)
        expect(newItem.content[0].text).toEqual(itemList.items[itemId].content[contentId].text.substring(offset))
    })

    test('when the offset is at the end of the item content, the new item will have one content element with an empty string', () => {
        const itemId = 1
        const contentId = 0
        const offset = itemList.items[itemId].content[contentId].text.length
        const result = ItemListAction.splitItem(itemList.items, itemId, contentId, offset)

        expect(result.length).toBe(itemList.items.length + 1)
        const newItem = result[itemId + 1]
        expect(newItem.content.length).toBe(1)
        expect(newItem.content[0].text).toEqual("")
    })

    test('does not split an empty item', () => {
        const itemId = 1
        const contentId = 0
        const offset = 0

        const result = ItemListAction.splitItem(itemListSecondEmpty.items, itemId, contentId, offset)
        expect(result.length).toBe(itemListSecondEmpty.items.length)
        expect(result).toBe(itemListSecondEmpty.items)
    })

    test('does not split when previous is empty', () => {
        const itemId = 2
        const contentId = 0
        const offset = 0

        const result = ItemListAction.splitItem(itemListSecondEmpty.items, itemId, contentId, offset)
        expect(result.length).toBe(itemListSecondEmpty.items.length)
        expect(result).toBe(itemListSecondEmpty.items)
    })

})