import {Action} from "../../../lib/actions"
import {AnyBlock, Content} from "../model/api"
import produce from "immer"

const updateBlockContent: Action<AnyBlock, [contentId: number, content: Content]> =
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
