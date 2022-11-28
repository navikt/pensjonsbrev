import {Action} from "../../../lib/actions"
import {TextContent} from "../model"
import produce from "immer"

const updateText: Action<TextContent, [content: string]> =
    produce((draft, content) => {
        draft.text = content
    })

export const TextContentAction = {
    updateText,
}