import {ITEM_LIST, LITERAL, PARAGRAPH, ParagraphBlock} from "../../../../../src/modules/LetterEditor/model/api"
import {afterEach, beforeEach, describe, expect, test, vi} from "vitest"
import {cleanup, render, screen} from "@testing-library/react"
import Paragraph from "../../../../../src/modules/LetterEditor/components/paragraph/Paragraph"
import {boundActionStub} from "../../../../testUtils"
import * as Model from "../../../../../src/modules/LetterEditor/model/api"
import userEvent from "@testing-library/user-event"

const itemList: Model.ItemList = {
    type: ITEM_LIST,
    id: 1,
    items: [
        {
            content: [
                {type: LITERAL, id: 1, text: "Item 1"}
            ]
        }
    ]
}
const paragraph: ParagraphBlock = {
    type: PARAGRAPH,
    id: 1,
    location: [],
    editable: true,
    content: [
        itemList
    ]
}

const user = userEvent.setup()
const updateContent = vi.fn<[contentId: number, content: Model.Content]>()

const component =
    <Paragraph block={paragraph}
               updateContent={updateContent}
               mergeWith={boundActionStub}
               splitBlockAtContent={boundActionStub}
               blockStealFocus={undefined}
               blockFocusStolen={boundActionStub}
               onFocus={boundActionStub}
    />

beforeEach(() => {
    render(component)
})

afterEach(() => {
    cleanup()
    vi.restoreAllMocks()
})

describe('updateContent', () => {
    test('enter in the last item when it is empty spawns a new empty Literal after the ItemList in the containing paragraph', async () => {
        const span = screen.getByText(itemList.items[0].content[0].text)

        await user.click(span)
        await user.keyboard("{End}{Enter}{Enter}")

        console.log(updateContent.mock.lastCall!)
        expect(updateContent).toHaveBeenLastCalledWith({...paragraph, content: [...paragraph.content, {id: -1, type: LITERAL, text: ""}]})
    })
})