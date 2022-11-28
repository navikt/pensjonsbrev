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

export const BlockAction = {
    updateBlockContent, unlock,
}
