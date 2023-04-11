import {afterEach, beforeEach, describe, expect, test, vi} from "vitest"
import {cleanup, render, screen} from "@testing-library/react"
import ContentGroup from "../../../../../src/modules/LetterEditor/components/contentgroup/ContentGroup"
import {LITERAL, LiteralValue} from "../../../../../src/modules/LetterEditor/model/api"
import {boundActionStub} from "../../../../testUtils"
import userEvent from "@testing-library/user-event"
import {TextContentAction} from "../../../../../src/modules/LetterEditor/actions/textcontent"
import {MergeTarget} from "../../../../../src/modules/LetterEditor/actions/common"

const content: LiteralValue[] = [
    {type: LITERAL, id: 1, text: "Heisann"},
    {type: LITERAL, id: 2, text: "Velkommen"},
]

const user = userEvent.setup()
const mergeAction = vi.fn()
const updateContentAction = vi.fn()
const splitAction = vi.fn<[contentId: number, offset: number]>()

const contentGroup =
    <ContentGroup
        content={content}
        editable={true}
        stealFocus={undefined}
        onFocus={boundActionStub}
        focusStolen={boundActionStub}
        splitAtContent={splitAction}
        updateContent={updateContentAction}
        mergeWith={mergeAction}
    />

beforeEach(() => {
    render(contentGroup)
})

afterEach(() => {
    cleanup()
    vi.restoreAllMocks()
})

describe('updateContent', () => {
    test('text changes are propagated', async () => {
        const firstSpan = screen.getByText(content[0].text)
        await user.click(firstSpan)
        await user.keyboard(' person')

        expect(updateContentAction).toHaveBeenCalled()
        expect(updateContentAction).toHaveBeenLastCalledWith(0, TextContentAction.updateText(content[0], content[0].text + ' person'))
    })
    test('enter is not propagated as br-element', async () => {
        const firstSpan = screen.getByText(content[0].text)

        await user.click(firstSpan)
        await user.keyboard("{Enter}asd")

        // Enter does cause a splitAction-event but we're only rendering the original block, so the focus is still in the span we clicked.
        expect(updateContentAction).toHaveBeenLastCalledWith(0, TextContentAction.updateText(content[0], content[0].text + "asd"))
    })
    test('space is not propagated as nbsp-entity', async () => {
        const firstSpan = screen.getByText(content[0].text)

        await user.click(firstSpan)
        await user.keyboard("  asd")

        expect(updateContentAction).toHaveBeenLastCalledWith(0, TextContentAction.updateText(content[0], content[0].text + "  asd"))
    })
})

describe('deleteHandler', () => {
    test('delete at end of group, and after last character, triggers merge with next', async () => {
        const lastSpan = screen.getByText(content[1].text)
        await user.click(lastSpan)
        await user.keyboard("joda{Delete}")

        expect(mergeAction).toHaveBeenCalledOnce()
        expect(mergeAction).toBeCalledWith(MergeTarget.NEXT)
    })
    test("delete at end of group, but not after last character, does not trigger merge", async () => {
        const lastSpan = screen.getByText(content[1].text)
        await user.click(lastSpan)
        await user.keyboard("{ArrowLeft}{ArrowLeft}{Delete}")

        expect(mergeAction).not.toHaveBeenCalled()
    })
    test('delete not at end of group, but after last character, does not trigger merge', async () => {
        const firstSpan = screen.getByText(content[0].text)
        await user.click(firstSpan)
        await user.keyboard("{End}{Delete}")

        expect(mergeAction).not.toHaveBeenCalled()
    })
})

describe('backspaceHandler', () => {
    test('backspace at beginning of group triggers merge with previous', async () => {
        const firstSpan = screen.getByText(content[0].text)
        await user.click(firstSpan)
        await user.keyboard("{Home}{Backspace}")

        expect(mergeAction).toHaveBeenCalledOnce()
        expect(mergeAction).toBeCalledWith(MergeTarget.PREVIOUS)
    })
    test("backspace at beginning of group, but not before first character of TextContent, does not trigger merge", async () => {
        const firstSpan = screen.getByText(content[0].text)
        await user.click(firstSpan)
        await user.keyboard("{End}{ArrowLeft}{ArrowLeft}{Backspace}")

        expect(mergeAction).not.toHaveBeenCalled()
    })
    test('backspace not at beginning of group, but before first character of a TextContent, does not trigger merge', async () => {
        const firstSpan = screen.getByText(content[1].text)
        await user.click(firstSpan)
        await user.keyboard("{Home}{Backspace}")

        expect(mergeAction).not.toHaveBeenCalled()
    })
})

describe('enterHandler', () => {
    test('enter at the very end of group triggers split with empty text for new group', async () => {
        const span = screen.getByText(content[1].text)

        await user.click(span)
        await user.keyboard("{End}{Enter}")

        expect(splitAction).toHaveBeenCalledWith(1, content[1].text.length)
    })
    test('enter not at the end of a content in group triggers split at cursor (text after cursor for new group)', async () => {
        const text = content[1].text
        const span = screen.getByText(text)

        await user.click(span)
        await user.keyboard("{End}{ArrowLeft}{ArrowLeft}{ArrowLeft}{Enter}")

        expect(splitAction).toHaveBeenCalledWith(1, text.length - 3)
    })
    test('enter at the end of an element that is not the last of group triggers split at current element', async () => {
        const span = screen.getByText(content[0].text)

        await user.click(span)
        await user.keyboard("{End}{Enter}")

        expect(splitAction).toHaveBeenLastCalledWith(0, content[0].text.length)
    })
})