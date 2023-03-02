import {describe, expect, test} from "vitest"
import {AnyBlock, ITEM_LIST, LITERAL, PARAGRAPH, TextContent, VARIABLE} from "../../../../src/modules/LetterEditor/model/api"
import {StealFocusAction} from "../../../../src/modules/LetterEditor/actions/stealfocus"
import {MergeTarget} from "../../../../src/modules/LetterEditor/actions/common"

describe('StealFocusAction', () => {
    const blocks: AnyBlock[] = [
        {
            type: PARAGRAPH,
            id: 1,
            location: [],
            editable: true,
            content: [
                {id: 1, type: VARIABLE, text: "Ole"},
                {id: 2, type: LITERAL, text: " på mandag diskutere vi planen videre."},
            ]
        }, {
            type: PARAGRAPH,
            id: 2,
            location: [],
            editable: true,
            content: [
                {id: 1, type: LITERAL, text: "Hyggelig å møte deg "},
                {
                    id: 2, type: ITEM_LIST, items: [{
                        content: [{id: 1, type: LITERAL, text: "item 1"}, {id: 2, type: LITERAL, text: "item 2"}]
                    }]
                },
            ]
        }, {
            type: PARAGRAPH,
            id: 3,
            location: [],
            editable: true,
            content: [
                {id: 1, type: LITERAL, text: "last"}
            ]
        }
    ]

    describe('onMerge', () => {
        describe('with previous', () => {
            test('will set steal focus for the previous block', () => {
                const result = StealFocusAction.onMerge({}, blocks, 1, MergeTarget.PREVIOUS)
                expect(result[0]).toBeDefined()
                expect(result[0]).not.toBeNull()
            })
            test('will set steal focus contentId to the last content-element', () => {
                const result = StealFocusAction.onMerge({}, blocks, 1, MergeTarget.PREVIOUS)
                expect(result[0]?.contentId).toBe(blocks[0].content.length - 1)
            })
            test('will set steal focus startOffset to the end of the text of the last content-element', () => {
                const result = StealFocusAction.onMerge({}, blocks, 1, MergeTarget.PREVIOUS)
                const lastContent = blocks[0].content[blocks[0].content.length - 1] as TextContent
                expect(result[0]?.startOffset).toBe(lastContent.text.length)
            })
            test('will not steal when there is no previous', () => {
                const result = StealFocusAction.onMerge({}, blocks, 0, MergeTarget.PREVIOUS)
                expect(result).toEqual({})
            })
            test('will set steal focus to the content we are merging from when the last content of previous is an itemList', () => {
                const blockId = 2
                const result = StealFocusAction.onMerge({}, blocks, blockId, MergeTarget.PREVIOUS)
                expect(result).toEqual({[blockId - 1]: {contentId: blocks[blockId - 1].content.length, startOffset: 0}})
            })
        })
        describe('with next', () => {
            test('will set steal focus so that the current block keeps focus', () => {
                const result = StealFocusAction.onMerge({}, blocks, 0, MergeTarget.NEXT)
                expect(result[0]).toBeDefined()
                expect(result[0]).not.toBeNull()
            })
            test('will set steal focus contentId so that the current content-element keeps focus', () => {
                const result = StealFocusAction.onMerge({}, blocks, 0, MergeTarget.NEXT)
                expect(result[0]?.contentId).toBe(blocks[0].content.length - 1)
            })
            test('will set steal focus startOffset so that the we keep the position at the end of the current content-element', () => {
                const result = StealFocusAction.onMerge({}, blocks, 0, MergeTarget.NEXT)
                const currentContent = blocks[0].content[blocks[0].content.length - 1] as TextContent
                expect(result[0]?.startOffset).toBe(currentContent.text.length)
            })
            test('will not steal when there is no next', () => {
                const result = StealFocusAction.onMerge({}, blocks, blocks.length - 1, MergeTarget.NEXT)
                expect(result).toEqual({})
            })
            test('will not fail when the last content of current is an item list', () => {
                StealFocusAction.onMerge({}, blocks, 1, MergeTarget.NEXT)
            })
        })
    })

    describe('onSplit', () => {
        test('will set steal focus to the new block', () => {
            const result = StealFocusAction.onSplit({}, blocks, 0)
            expect(result[1]).toBeDefined()
            expect(result[1]).not.toBeNull()
        })
        test('will set steal focus to the first content of the new block', () => {
            const result = StealFocusAction.onSplit({}, blocks, 0)
            expect(result[1]?.contentId).toBe(0)
        })
        test('will set steal focus startOffset to the beginning of the content', () => {
            const result = StealFocusAction.onSplit({}, blocks, 0)
            expect(result[1]?.startOffset).toBe(0)
        })
        test('will not set steal focus if current is empty', () => {
            const blocksWithEmptyPrevious: AnyBlock[] = [
                {
                    type: PARAGRAPH,
                    id: 1,
                    location: [],
                    editable: true,
                    content: [{type: LITERAL, id: 1, text: ""}]
                }, {
                    type: PARAGRAPH,
                    id: 1,
                    location: [],
                    editable: true,
                    content: [{type: LITERAL, id: 1, text: "some text"}]
                },
            ]
            const result = StealFocusAction.onSplit({}, blocksWithEmptyPrevious, 0)
            expect(result).toEqual({})
        })
    })

    describe('focusStolen', () => {
        test('will remove the current steal focus for a block', () => {
            const result = StealFocusAction.focusStolen({9: {contentId: 3, startOffset: 1}}, 9)
            expect(result[9]).toBeUndefined()
        })
    })
})