import type { Draft } from "immer";
import { produce } from "immer";

import type { LiteralValue } from "~/types/brevbakerTypes";
import { ITEM_LIST, LITERAL, TABLE } from "~/types/brevbakerTypes";

import type { Action } from "../lib/actions";
import type { LetterEditorState, LiteralIndex } from "../model/state";
import { cleanseText, isItemContentIndex } from "./common";

export const updateContentText: Action<LetterEditorState, [literalIndex: LiteralIndex, text: string]> = produce(
  (draft, literalIndex, text) => {
    const focus = literalIndex;
    const paragraph = draft.redigertBrev.blocks[focus.blockIndex];

    const paraContent = paragraph.content[focus.contentIndex];

    if (paraContent?.type === TABLE && isItemContentIndex(focus)) {
      const row = paraContent.rows[focus.itemIndex];
      const cell = row?.cells[focus.itemContentIndex];
      const literal = cell?.text[0];
      if (literal?.type === LITERAL) {
        updateLiteralText(literal, text);
        draft.isDirty = true;
      }
      return;
    }

    if (paraContent.type === LITERAL) {
      updateLiteralText(paraContent, text);
      draft.isDirty = true;
    } else if (paraContent.type === ITEM_LIST) {
      if ("itemIndex" in literalIndex) {
        const itemContent = paraContent.items[literalIndex.itemIndex].content[literalIndex.itemContentIndex];
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
