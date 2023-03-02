import {Action} from "../../../lib/actions"
import {LiteralValue} from "../model/api"
import produce from "immer"

export function cleanseText(text: string): string {
    return text.replaceAll("<br>", "").replaceAll("&nbsp;", " ")
}

const updateText: Action<LiteralValue, [content: string]> =
    produce((draft, content) => {
        draft.text = cleanseText(content)
    })

export const TextContentAction = {
    updateText,
}

