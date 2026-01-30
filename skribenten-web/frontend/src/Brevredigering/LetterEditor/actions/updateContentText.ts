import type { Draft } from "immer";

import { type LiteralValue, TITLE_INDEX } from "~/types/brevbakerTypes";

import { type Action, withPatches } from "../lib/actions";
import type { LetterEditorState, LiteralIndex } from "../model/state";
import { isItemList, isLiteral, isTableCellIndex } from "../model/utils";
import { cleanseText, isTable } from "./common";

export const updateContentText: Action<
  LetterEditorState,
  [literalIndex: LiteralIndex, text: string, cursorPosition: number]
> = withPatches((draft: Draft<LetterEditorState>, literalIndex: LiteralIndex, text: string, cursorPosition: number) => {
  const focus = literalIndex;

  const paraContent =
    literalIndex.blockIndex === TITLE_INDEX
      ? draft.redigertBrev.title.text[focus.contentIndex]
      : draft.redigertBrev.blocks[focus.blockIndex].content[focus.contentIndex];

  let textWasUpdated = false;

  if (isTable(paraContent) && isTableCellIndex(focus)) {
    // Here rowIndex === -1 means the table header row.
    if (focus.rowIndex === -1) {
      const colSpec = paraContent.header.colSpec[focus.cellIndex];
      const literal = colSpec?.headerContent.text[focus.cellContentIndex];
      if (isLiteral(literal)) {
        updateLiteralText(literal, text);
        textWasUpdated = true;
      }
    } else {
      // the table body.
      const row = paraContent.rows[focus.rowIndex];
      const cell = row?.cells[focus.cellIndex];
      const literal = cell?.text[focus.cellContentIndex];
      if (isLiteral(literal)) {
        updateLiteralText(literal, text);
        textWasUpdated = true;
      }
    }
  } else if (isLiteral(paraContent)) {
    updateLiteralText(paraContent, text);
    textWasUpdated = true;
  } else if (isItemList(paraContent)) {
    if ("itemIndex" in literalIndex) {
      const itemContent = paraContent.items[literalIndex.itemIndex].content[literalIndex.itemContentIndex];
      if (isLiteral(itemContent)) {
        updateLiteralText(itemContent, text);
        textWasUpdated = true;
      } else {
        console.warn("Cannot update text of:", itemContent.type);
      }
    } else {
      console.warn("Cannot update text of ItemList, itemIndex and itemContentIndex is missing.");
    }
  } else {
    console.warn("Cannot update text of non-literal content");
  }
  if (textWasUpdated) {
    draft.saveStatus = "DIRTY";
    draft.focus = { ...literalIndex, cursorPosition };
  }
});

export function updateLiteralText(literal: Draft<LiteralValue>, text: string) {
  literal.editedText = cleanseText(text);
  if (literal.editedText === literal.text) {
    literal.editedText = null;
  }
}
