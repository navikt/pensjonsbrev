import { produce } from "immer";

import { ITEM_LIST, LITERAL } from "~/types/brevbakerTypes";

import type { Action } from "../lib/actions";
import type { LetterEditorState } from "../model/state";
import { cleanseText } from "./common";
import type { ContentIndex } from "./model";

export const updateContentText: Action<LetterEditorState, [contentIndex: ContentIndex, text: string]> = produce(
  (draft, contentIndex, text) => {
    const content = draft.editedLetter.letter.blocks[contentIndex.blockIndex].content[contentIndex.contentIndex];
    if (content.type === LITERAL) {
      content.text = cleanseText(text);
    } else if (content.type === ITEM_LIST) {
      if ("itemIndex" in contentIndex) {
        const itemContent = content.items[contentIndex.itemIndex].content[contentIndex.itemContentIndex];
        if (itemContent.type === LITERAL) {
          itemContent.text = cleanseText(text);
        } else {
          // eslint-disable-next-line no-console
          console.warn("Cannot update text of:", itemContent.type);
        }
      } else {
        // eslint-disable-next-line no-console
        console.warn("Cannot update text of ItemList, itemIndex and itemContentId is missing.");
      }
    } else {
      // eslint-disable-next-line no-console
      console.warn("Cannot update text of non-literal content");
    }
  },
);
