import type { Draft } from "immer";
import { produce } from "immer";

import type { LiteralValue } from "~/types/brevbakerTypes";
import { ITEM_LIST, LITERAL } from "~/types/brevbakerTypes";

import type { Action } from "../lib/actions";
import type { LetterEditorState, LiteralIndex } from "../model/state";
import { cleanseText } from "./common";

export const updateContentText: Action<LetterEditorState, [literalIndex: LiteralIndex, text: string]> = produce(
  (draft, literalIndex, text) => {
    const content = draft.redigertBrev.blocks[literalIndex.blockIndex].content[literalIndex.contentIndex];

    if (content.type === LITERAL) {
      updateLiteralText(content, text);
      draft.isDirty = true;
    } else if (content.type === ITEM_LIST) {
      if ("itemIndex" in literalIndex) {
        const itemContent = content.items[literalIndex.itemIndex].content[literalIndex.itemContentIndex];
        if (itemContent.type === LITERAL) {
          updateLiteralText(itemContent, text);
          draft.isDirty = true;
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

export function updateLiteralText(literal: Draft<LiteralValue>, text: string) {
  literal.editedText = cleanseText(text);
  if (literal.editedText === literal.text) {
    literal.editedText = null;
  }
}
