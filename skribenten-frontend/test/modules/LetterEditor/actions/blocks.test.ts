import {describe, expect, test} from "vitest"
import {AnyBlock, LITERAL, ParagraphBlock, TextContent, VARIABLE} from "../../../../src/modules/LetterEditor/model/api"
import {BlocksAction} from "../../../../src/modules/LetterEditor/actions/blocks"
import {MergeTarget} from "../../../../src/modules/LetterEditor/actions/common"

describe("BlocksAction", () => {
    const blocks: AnyBlock[] = [
        {
            id: 1,
            location: [],
            type: "PARAGRAPH",
            content: [
                {id: 1, type: LITERAL, text: "Hyggelig å møte deg "},
                {id: 2, type: VARIABLE, text: "Ole"},
                {id: 3, type: LITERAL, text: ". Sees på "},
                {id: 4, type: VARIABLE, text: "fredag"},
            ],
        },
        {
            id: 2,
            location: [],
            type: "PARAGRAPH",
            content: [
                {id: 1, type: LITERAL, text: "Da kan vi diskutere planen videre."},
            ],
        },
        {
            id: 3,
            location: [],
            type: "PARAGRAPH",
            content: [
                {id: 1, type: LITERAL, text: "Det blir "},
                {id: 2, type: VARIABLE, text: "spennende"},
            ]
        },
    ]

    const blocksFirstEmpty: AnyBlock[] = [
        {
            id: 1,
            location: [],
            type: "PARAGRAPH",
            content: [
                {id: 1, type: LITERAL, text: ""},
            ],
        },
        {
            id: 2,
            location: [],
            type: "PARAGRAPH",
            content: [
                {id: 1, type: LITERAL, text: "ABC"},
            ],
        },
    ]

    const blocksSecondEmpty: AnyBlock[] = [
        {
            id: 1,
            location: [],
            type: "PARAGRAPH",
            content: [
                {id: 1, type: LITERAL, text: "ABC"},
            ],
        },
        {
            id: 2,
            location: [],
            type: "PARAGRAPH",
            content: [
                {id: 1, type: LITERAL, text: ""},
            ],
        },
    ]

    describe("updateBlock", () => {
        const updatedBlock: ParagraphBlock = {
            id: 1,
            location: [],
            type: "PARAGRAPH",
            content: [{id: 1, type: "LITERAL", text: "hei"}]
        }
        test("replaces block at index", () => {
            const result = BlocksAction.updateBlock(blocks, 0, updatedBlock)

            expect(result[0]).toEqual(updatedBlock)
        })
        test("result is not the original array", () => {
            const result = BlocksAction.updateBlock(blocks, 0, updatedBlock)

            expect(result).not.toBe(blocks)
        })
        test("only specified block is updated", () => {
            const result = BlocksAction.updateBlock(blocks, 0, updatedBlock)

            expect(result[1]).toBe(blocks[1])
        })
    })

    describe("splitBlock", () => {
        test("specified block is split at contentId and offset", () => {
            const result = BlocksAction.splitBlock(blocks, 0, 2, 2)

            expect(result).toHaveLength(blocks.length + 1)

            expect(result[0].content).toHaveLength(3)
            expect((result[0].content[2] as TextContent).text).toEqual(". ")

            expect(result[1].content).toHaveLength(2)
            expect((result[1].content[0] as TextContent).text).toEqual("Sees på ")
            expect(result[1].content[1]).toBe(blocks[0].content[3])

            expect(result[2]).toBe(blocks[1])
        })

        test("result is not the original array", () => {
            const result = BlocksAction.splitBlock(blocks, 0, 2, 2)

            expect(result).not.toBe(blocks)
        })

        test('when the offset is at the end of the current content, the new block will have one content element with an empty string', () => {
            const result = BlocksAction.splitBlock(blocks, 1, 0, (blocks[1].content[0] as TextContent).text.length)

            expect(result.length).toBe(blocks.length + 1)
            expect(result[2].content.length).toBe(1)
            expect((result[2].content[0] as TextContent).text).toBe("")
        })

        test('does not split an emptyBlock', () => {
            let result = BlocksAction.splitBlock(blocksFirstEmpty, 0, 0, 0)
            expect(result).toBe(blocksFirstEmpty)

            result = BlocksAction.splitBlock(blocksSecondEmpty, 1, 0, 0)
            expect(result).toBe(blocksSecondEmpty)
        })

        test('does not split when previous is empty', () => {
            const result = BlocksAction.splitBlock(blocksFirstEmpty, 1, 0, 0)
            expect(result).toBe(blocksFirstEmpty)
        })

    })

    describe("mergeWith", () => {
        describe("next", () => {
            test("the specified blocks are merged and the number of blocks reduced", () => {
                const result = BlocksAction.mergeWith(blocks, 1, MergeTarget.NEXT)
                expect(result).toHaveLength(blocks.length - 1)
            })

            test("merge is ignored if the specified block is the last", () => {
                const result = BlocksAction.mergeWith(blocks, blocks.length - 1, MergeTarget.NEXT)
                expect(result).toEqual(blocks)
            })

            test("the content of the next block is added to the end of the specified", () => {
                const result = BlocksAction.mergeWith(blocks, 0, MergeTarget.NEXT)
                expect(result[0].content).toEqual(blocks[0].content.concat(blocks[1].content as TextContent[]))
            })

            test("adjoining literal content in merging blocks are joined", () => {
                const result = BlocksAction.mergeWith(blocks, 1, MergeTarget.NEXT)
                expect(result[1].content).toHaveLength(2)
                expect((result[1].content[0] as TextContent).text).toEqual((blocks[1].content[0] as TextContent).text + (blocks[2].content[0] as TextContent).text)
            })

            test("id of specified block is kept if the next is empty", () => {
                const result = BlocksAction.mergeWith(blocksSecondEmpty, 0, MergeTarget.NEXT)

                expect(result[0].id).toEqual(blocks[0].id)
            })

            test("id of next block is kept if the specified is empty", () => {
                const result = BlocksAction.mergeWith(blocksFirstEmpty, 0, MergeTarget.NEXT)
                expect(result[0].id).toEqual(blocks[1].id)
            })
        })
        describe("previous", () => {
            test("the specified blocks are merged and the number of blocks reduced", () => {
                const result = BlocksAction.mergeWith(blocks, 1, MergeTarget.PREVIOUS)
                expect(result).toHaveLength(blocks.length - 1)
            })

            test("merge is ignored if the specified block is the first", () => {
                const result = BlocksAction.mergeWith(blocks, 0, MergeTarget.PREVIOUS)
                expect(result).toEqual(blocks)
            })

            test("the content of the specified block is added to the end of the previous", () => {
                const result = BlocksAction.mergeWith(blocks, 1, MergeTarget.PREVIOUS)
                expect(result[0].content).toEqual(blocks[0].content.concat(blocks[1].content as TextContent[]))
            })

            test("adjoining literal content in merging blocks are joined", () => {
                const result = BlocksAction.mergeWith(blocks, 2, MergeTarget.PREVIOUS)
                expect(result[1].content).toHaveLength(2)
                expect((result[1].content[0] as TextContent).text).toEqual((blocks[1].content[0] as TextContent).text + (blocks[2].content[0] as TextContent).text)
            })

            test("id of specified block is kept if the previous is empty", () => {
                const result = BlocksAction.mergeWith(blocksFirstEmpty, 1, MergeTarget.PREVIOUS)
                expect(result[0].id).toEqual(blocks[1].id)
            })

            test("id of previous block is kept if the specified is empty", () => {
                const result = BlocksAction.mergeWith(blocksSecondEmpty, 1, MergeTarget.PREVIOUS)
                expect(result[0].id).toEqual(blocks[0].id)
            })
        })
    })
})
