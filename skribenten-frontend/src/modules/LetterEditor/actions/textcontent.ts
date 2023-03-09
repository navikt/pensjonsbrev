import {Action} from "../../../lib/actions"
import {LiteralValue} from "../model/api"
import produce from "immer"

const updateText: Action<LiteralValue, [content: string]> =
    produce((draft, content) => {
        draft.text = content
    })

export const TextContentAction = {
    updateText,
}

