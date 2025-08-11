import type { Draft } from "immer";
import { produce } from "immer";

import type { LiteralValue, Table } from "~/types/brevbakerTypes";
import { LITERAL, PARAGRAPH } from "~/types/brevbakerTypes";

import type { Action } from "../lib/actions";
import type { Focus, LetterEditorState } from "../model/state";
import { newTable } from "../model/tableHelpers";
import { isTableCellIndex } from "../model/utils";
import { addElements, isTable, newColSpec, newRow, removeElements, text } from "./common";
import { updateLiteralText } from "./updateContentText";

/**
 * Re‑number default header labels (“Kolonne N”) so they match the current
 * column order.
 *
 * Scenario: User inserts a column between “Kolonne 1” and “Kolonne 2”.
 * This helper updates the auto‑generated labels to
 * “Kolonne 1  Kolonne 2  Kolonne 3”.
 *
 * • Only overwrites cells that are blank or still have the default pattern
 *   /^Kolonne \d+$/ – customised header text is preserved.
 */

const updateDefaultHeaderLabels = (table: Draft<Table>) => {
  table.header.colSpec.forEach((col, idx) => {
    const litIdx = col.headerContent.text.findIndex((txt) => txt.type === LITERAL);
    if (litIdx === -1) return;

    const literal = col.headerContent.text[litIdx] as LiteralValue;
    const shown = text(literal).trim();

    const isDefaultHeaderText = shown === "" || /^Kolonne \d+$/.test(shown);
    if (!isDefaultHeaderText) return;

    updateLiteralText(literal, `Kolonne ${idx + 1}`);
  });
};

export const insertTable: Action<LetterEditorState, [focus: Focus, rows: number, cols: number]> = produce(
  (draft, focus, rows, cols) => {
    const block = draft.redigertBrev.blocks[focus.blockIndex];
    if (block.type !== PARAGRAPH) return;

    // If content was emptied (e.g. a table was just deleted) ensure we insert at 0
    const safeContentIndex = Math.min(focus.contentIndex, block.content.length - 1);
    const insertAt = block.content.length === 0 ? 0 : safeContentIndex + 1;

    addElements([newTable(rows, cols)], insertAt, block.content, block.deletedContent);
    draft.focus = { blockIndex: focus.blockIndex, contentIndex: insertAt };
    draft.isDirty = true;
  },
);

export const removeTableRow = produce<LetterEditorState>((draft) => {
  if (!isTableCellIndex(draft.focus)) return;
  const { blockIndex, contentIndex, rowIndex } = draft.focus;
  if (rowIndex < 0) return;

  const table = draft.redigertBrev.blocks[blockIndex].content[contentIndex];
  if (!isTable(table)) return;
  removeElements(rowIndex, 1, { content: table.rows, deletedContent: table.deletedRows, id: table.id });

  draft.isDirty = true;
});

export const removeTableColumn = produce<LetterEditorState>((draft) => {
  if (!isTableCellIndex(draft.focus)) return;
  const { blockIndex, contentIndex, cellIndex: col } = draft.focus;

  const table = draft.redigertBrev.blocks[blockIndex].content[contentIndex];
  if (!isTable(table)) return;

  table.header.colSpec.splice(col, 1);
  table.rows.forEach((row) => row.cells.splice(col, 1));
  updateDefaultHeaderLabels(table);
  draft.isDirty = true;
});

export const removeTable = produce<LetterEditorState>((draft) => {
  if (!isTableCellIndex(draft.focus)) return;
  const { blockIndex, contentIndex } = draft.focus;

  const parentBlock = draft.redigertBrev.blocks[blockIndex];
  removeElements(contentIndex, 1, parentBlock);

  // Adjust focus to a valid position
  const newContentIndex = Math.max(0, Math.min(contentIndex - 1, parentBlock.content.length - 1));
  draft.focus = { blockIndex, contentIndex: newContentIndex, cursorPosition: 0 };

  draft.isDirty = true;
});

export const insertTableColumnLeft: Action<LetterEditorState, []> = produce((draft) => {
  if (!isTableCellIndex(draft.focus)) return;
  const { blockIndex, contentIndex, cellIndex: at } = draft.focus;

  const table = draft.redigertBrev.blocks[blockIndex].content[contentIndex];
  if (!isTable(table)) return;
  //TODO: Once Header and Row have their own deleted* arrays
  // (e.g. header.deletedColSpecs, row.deletedCells),
  // replace these direct splices with addElements(...)
  table.header.colSpec.splice(at, 0, ...newColSpec(1));
  table.rows.forEach((row) => row.cells.splice(at, 0, newRow(1).cells[0]));
  updateDefaultHeaderLabels(table);
  draft.isDirty = true;
});

export const insertTableColumnRight: Action<LetterEditorState, []> = produce((draft) => {
  if (!isTableCellIndex(draft.focus)) return;
  const { blockIndex, contentIndex, cellIndex } = draft.focus;

  const table = draft.redigertBrev.blocks[blockIndex].content[contentIndex];
  if (!isTable(table)) return;
  const at = cellIndex + 1;
  //TODO: Once Header and Row have their own deleted* arrays
  // (e.g. header.deletedColSpecs, row.deletedCells),
  // replace these direct splices with addElements(...)
  table.header.colSpec.splice(at, 0, ...newColSpec(1));
  table.rows.forEach((row) => row.cells.splice(at, 0, newRow(1).cells[0]));
  updateDefaultHeaderLabels(table);
  draft.isDirty = true;
});

export const insertTableRowAbove: Action<LetterEditorState, []> = produce((draft) => {
  if (!isTableCellIndex(draft.focus)) return;
  const { blockIndex, contentIndex, rowIndex } = draft.focus;

  if (rowIndex < 0) return;
  const table = draft.redigertBrev.blocks[blockIndex].content[contentIndex];
  if (!isTable(table)) return;
  addElements([newRow(table.header.colSpec.length)], rowIndex, table.rows, table.deletedRows);
  draft.isDirty = true;
});

export const insertTableRowBelow: Action<LetterEditorState, []> = produce((draft) => {
  if (!isTableCellIndex(draft.focus)) return;
  const { blockIndex, contentIndex, rowIndex } = draft.focus;

  if (rowIndex < 0) return;
  const table = draft.redigertBrev.blocks[blockIndex].content[contentIndex];
  if (!isTable(table)) return;
  addElements([newRow(table.header.colSpec.length)], rowIndex + 1, table.rows, table.deletedRows);
  draft.isDirty = true;
});
