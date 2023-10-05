import {Action} from "../../../lib/actions"
import produce from "immer"
import {ITEM_LIST, LITERAL} from "../../../lib/model/skribenten"
import {ContentId} from "./model"
import {cleanseText} from "./common"
import {LetterEditorState} from "../model/state"

export const updateContentText: Action<LetterEditorState, [id: ContentId, text: string]> =
    produce((draft, id, text) => {
        const content = draft.editedLetter.letter.blocks[id.blockId].content[id.contentId]
        if (content.type === LITERAL) {
            content.text = cleanseText(text)
        } else if (content.type === ITEM_LIST) {
            if ("itemId" in id) {
                const itemContent = content.items[id.itemId].content[id.itemContentId]
                if (itemContent.type === LITERAL) {
                    itemContent.text = cleanseText(text)
                } else {
                    console.warn("Cannot update text of: ", itemContent.type)
                }
            } else {
                console.warn("Cannot update text of ItemList, itemId and itemContentId is missing.")
            }
        } else {
            console.warn("Cannot update text of non-literal content")
        }
    })