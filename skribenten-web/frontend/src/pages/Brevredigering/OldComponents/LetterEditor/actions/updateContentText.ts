import produce from "immer";

import type { Action } from "../lib/actions";
import { ITEM_LIST, LITERAL } from "../lib/model/skribenten";
import type { LetterEditorState } from "../model/state";
import { cleanseText } from "./common";
import type { ContentId } from "./model";

export const updateContentText: Action<LetterEditorState, [id: ContentId, text: string]> = produce(
  (draft, id, text) => {
    const content = draft.editedLetter.letter.blocks[id.blockId].content[id.contentId];
    if (content.type === LITERAL) {
      content.text = cleanseText(text);
    } else if (content.type === ITEM_LIST) {
      if ("itemId" in id) {
        const itemContent = content.items[id.itemId].content[id.itemContentId];
        if (itemContent.type === LITERAL) {
          itemContent.text = cleanseText(text);
        } else {
          console.warn("Cannot update text of:", itemContent.type);
        }
      } else {
        console.warn("Cannot update text of ItemList, itemId and itemContentId is missing.");
      }
    } else {
      console.warn("Cannot update text of non-literal content");
    }
  },
);
