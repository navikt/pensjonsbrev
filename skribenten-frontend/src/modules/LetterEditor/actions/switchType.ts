import {Action} from "../../../lib/actions"
import {PARAGRAPH, TITLE1} from "../model/api"
import produce from "immer"
import {isTextContent} from "../model/utils"
import {LetterEditorState} from "../model/state"

export const switchType: Action<LetterEditorState, [blockId: number, toType: typeof PARAGRAPH | typeof TITLE1]> =
    produce((draft, blockId, toType) => {
        const block = draft.editedLetter.letter.blocks[blockId]
        switch (toType) {
            case PARAGRAPH:
                block.type = toType
                break;

            case TITLE1:
                if(block.content.every(isTextContent)) {
                    block.type = toType
                } else {
                    console.warn("Cannot switch type of block to title1: contains non text content")
                }
        }
    })