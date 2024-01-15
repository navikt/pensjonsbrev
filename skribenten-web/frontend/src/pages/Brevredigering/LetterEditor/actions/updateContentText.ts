import { produce } from "immer";

import { ITEM_LIST, LITERAL } from "~/types/brevbakerTypes";

import type { Action } from "../lib/actions";
import type { LetterEditorState } from "../model/state";
import { cleanseText } from "./common";
import type { LiteralIndex } from "./model";

export const updateContentText: Action<LetterEditorState, [literalIndex: LiteralIndex, text: string]> = produce(
  (draft, literalIndex, text) => {
    const content = draft.editedLetter.letter.blocks[literalIndex.blockIndex].content[literalIndex.contentIndex];
    if (content.type === LITERAL) {
      content.text = cleanseText(text);
    } else if (content.type === ITEM_LIST) {
      if ("itemIndex" in literalIndex) {
        const itemContent = content.items[literalIndex.itemIndex].content[literalIndex.itemContentIndex];
        if (itemContent.type === LITERAL) {
          itemContent.text = cleanseText(text);
        } else {
          // eslint-disable-next-line no-console
          console.warn("Cannot update text of:", itemContent.type);
        }
      } else {
        // eslint-disable-next-line no-console
        console.warn("Cannot update text of ItemList, itemIndex and itemContentIndex is missing.");
      }
    } else {
      // eslint-disable-next-line no-console
      console.warn("Cannot update text of non-literal content");
    }
  },
);
