import {describe, expect, test} from "vitest"
import {AnyBlock, LITERAL, VARIABLE} from "../../../../src/modules/LetterEditor/model"
import {StealFocusAction} from "../../../../src/modules/LetterEditor/actions/stealfocus"
import {MergeTarget} from "../../../../src/modules/LetterEditor/actions/blocks"

describe('StealFocusAction', () => {
    const blocks: AnyBlock[] = [
        {
            type: "PARAGRAPH",
            id: 1,
            location: [],
            editable: true,
            content: [
                {id: 1, location: [], type: VARIABLE, text: "Ole"},
                {id: 2, location: [], type: LITERAL, text: " på mandag vi diskutere planen videre."},
            ]
        }, {
            type: "PARAGRAPH",
            id: 1,
            location: [],
            editable: true,
            content: [
                {id: 1, location: [], type: LITERAL, text: "Hyggelig å møte deg "},
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
                expect(result[0]?.startOffset).toBe(blocks[0].content[blocks[0].content.length - 1].text.length)
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
            test('will set steal focus startOffset so that the we keep the position to the end of the content-element', () => {
                const result = StealFocusAction.onMerge({}, blocks, 0, MergeTarget.NEXT)
                expect(result[0]?.startOffset).toBe(blocks[0].content[blocks[0].content.length - 1].text.length)
            })
        })
    })

    describe('onSplit', () => {
        test('will set steal focus to the new block', () => {
            const result = StealFocusAction.onSplit({}, 0)
            expect(result[1]).toBeDefined()
            expect(result[1]).not.toBeNull()
        })
        test('will set steal focus to the first content of the new block', () => {
            const result = StealFocusAction.onSplit({}, 0)
            expect(result[1]?.contentId).toBe(0)
        })
        test('will set steal focus startOffset to the beginning of the content', () => {
            const result = StealFocusAction.onSplit({}, 0)
            expect(result[1]?.startOffset).toBe(0)
        })
    })

    describe('focusStolen', () => {
        test('will remove the current steal focus for a block', () => {
            const result = StealFocusAction.focusStolen({9: {contentId: 3, startOffset: 1}}, 9)
            expect(result[9]).toBeUndefined()
        })
    })
})