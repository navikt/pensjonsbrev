import type { Draft } from "immer";
import { produce } from "immer";

import type { LiteralValue } from "~/types/brevbakerTypes";

import type { Action } from "../lib/actions";
import type { LetterEditorState, LiteralIndex } from "../model/state";
import { isItemList, isLiteral } from "../model/utils";
import { cleanseText, isTable, isTableContentIndex } from "./common";

export const updateContentText: Action<LetterEditorState, [literalIndex: LiteralIndex, text: string]> = produce(
  (draft, literalIndex, text) => {
    const focus = literalIndex;
    const block = draft.redigertBrev.blocks[focus.blockIndex];
    const paraContent = block.content[focus.contentIndex];

    if (isTable(paraContent) && isTableContentIndex(focus)) {
      // Here itmeIndex === -1 means the table header row.
      if (focus.itemIndex === -1) {
        const colSpec = paraContent.header.colSpec[focus.itemContentIndex];
        const literal = colSpec?.headerContent.text.find((txt) => isLiteral(txt));
        if (literal) {
          updateLiteralText(literal, text);
          draft.isDirty = true;
        }
        return;
      }
      // the table body.
      const row = paraContent.rows[focus.itemIndex];
      const cell = row?.cells[focus.itemContentIndex];
      const literal = cell?.text[0];
      if (isLiteral(literal)) {
        updateLiteralText(literal, text);
        draft.isDirty = true;
      }
      return;
    } else if (isLiteral(paraContent)) {
      updateLiteralText(paraContent, text);
      draft.isDirty = true;
    } else if (isItemList(paraContent)) {
      if ("itemIndex" in literalIndex) {
        const itemContent = paraContent.items[literalIndex.itemIndex].content[literalIndex.itemContentIndex];
        if (isLiteral(itemContent)) {
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
