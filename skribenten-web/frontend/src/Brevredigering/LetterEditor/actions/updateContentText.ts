import type { Draft } from "immer";

import type { LiteralValue } from "~/types/brevbakerTypes";

import { type Action, withPatches } from "../lib/actions";
import type { LetterEditorState, LiteralIndex } from "../model/state";
import { isItemList, isLiteral, isTableCellIndex } from "../model/utils";
import { cleanseText, isTable } from "./common";

export const updateContentText: Action<LetterEditorState, [literalIndex: LiteralIndex, text: string]> = withPatches(
  (draft: Draft<LetterEditorState>, literalIndex: LiteralIndex, text: string) => {
    const focus = literalIndex;
    const block = draft.redigertBrev.blocks[focus.blockIndex];
    const paraContent = block.content[focus.contentIndex];

    if (isTable(paraContent) && isTableCellIndex(focus)) {
      // Here rowIndex === -1 means the table header row.
      if (focus.rowIndex === -1) {
        const colSpec = paraContent.header.colSpec[focus.cellIndex];
        const literal = colSpec?.headerContent.text[focus.cellContentIndex];
        if (isLiteral(literal)) {
          updateLiteralText(literal, text);
          draft.isDirty = true;
        }
        return;
      }
      // the table body.
      const row = paraContent.rows[focus.rowIndex];
      const cell = row?.cells[focus.cellIndex];
      const literal = cell?.text[focus.cellContentIndex];
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
