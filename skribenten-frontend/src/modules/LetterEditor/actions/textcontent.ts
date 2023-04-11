import {Action} from "../../../lib/actions"
import {LiteralValue} from "../model/api"
import produce from "immer"
import {cleanseText} from "./common"

const updateText: Action<LiteralValue, [content: string]> =
    produce((draft, content) => {
        draft.text = cleanseText(content)
    })

export const TextContentAction = {
    updateText,
}

