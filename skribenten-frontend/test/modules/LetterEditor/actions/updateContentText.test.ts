import {describe, expect, test} from "vitest"
import {Content, LITERAL, PARAGRAPH, TextContent} from "../../../../src/lib/model/api"
import {letter, select} from "../utils"
import Actions from "../../../../src/modules/LetterEditor/actions"

const content: Content = {
    type: LITERAL,
    id: 1,
    text: "heisann",
}

const state = letter({id: 1, type: PARAGRAPH, editable: true, content: [content]})
const contentId = {blockId: 0, contentId: 0}

describe("updateContentText", () => {
    test("text is updated", () => {
        const result = Actions.updateContentText(state, contentId,"Heisann joda")
        expect(select<TextContent>(result, contentId).text).toEqual("Heisann joda")
    })
    test("line break elements are removed", () => {
        const result = Actions.updateContentText(state, contentId, "Hallo, <br>Håper alt er bra")
        expect(select<TextContent>(result, contentId).text).toEqual("Hallo, Håper alt er bra")
    })
    test("non-breaking space entities are replaced", () => {
        const result = Actions.updateContentText(state, contentId, "Hallo&nbsp;hvordan går det")
        expect(select<TextContent>(result, contentId).text).toEqual("Hallo hvordan går det")
    })
})
