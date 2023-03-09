import {describe, expect, test} from "vitest"
import {LITERAL, Content} from "../../../../src/modules/LetterEditor/model/api"
import {TextContentAction} from "../../../../src/modules/LetterEditor/actions/textcontent"

describe("TextContentAction", () => {
    const content: Content = {
        type: LITERAL,
        id: 1,
        text: "heisann",
    }

    describe('updateText', () => {
        test("text is updated", () => {
            const result = TextContentAction.updateText(content, "Heisann joda")
            expect(result.text).toEqual("Heisann joda")
        })
        test("nothing else is updated", () => {
            const result = TextContentAction.updateText(content, "hallo")
            expect({...result, text: content.text}).toEqual(content)
        })
        test("line break elements are removed", () => {
            const result = TextContentAction.updateText(content, "Hallo, <br>Håper alt er bra")
            expect(result.text).toEqual("Hallo, Håper alt er bra")
        })
        test("non-breaking space entities are replaced", () => {
            const result = TextContentAction.updateText(content, "Hallo&nbsp;hvordan går det")
            expect(result.text).toEqual("Hallo hvordan går det")
        })
    })
})