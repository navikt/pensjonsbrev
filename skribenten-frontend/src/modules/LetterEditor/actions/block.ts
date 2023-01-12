import {Action} from "../../../lib/actions"
import {AnyBlock, TextContent} from "../model"
import produce from "immer"

const updateBlockContent: Action<AnyBlock, [contentId: number, content: TextContent]> =
    produce((draft, contentId, content) => {
        draft.content[contentId] = content
    })

const unlock: Action<AnyBlock, []> =
    produce((draft) => {
        draft.locked = false
    })

const switchType: Action<AnyBlock, [type: "PARAGRAPH" | "TITLE1"]> =
    produce((draft, type) => {
        draft.type = type
    })

export const BlockAction = {
    updateBlockContent, unlock, switchType,
}
