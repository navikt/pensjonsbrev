import type { Draft } from "immer";

import type { Table } from "~/types/brevbakerTypes";
import { PARAGRAPH } from "~/types/brevbakerTypes";

import type { Focus, LetterEditorState } from "../model/state";
import { newTable } from "../model/tableHelpers";
import { isEmptyTableHeader, isTableCellIndex } from "../model/utils";
import {
  addElements,
  cleanseText,
  isTable,
  newCell,
  newColSpec,
  newLiteral,
  newRow,
  removeElements,
  safeIndex,
  text,
} from "./common";

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
  const isDefault = (s: string) => /^Kolonne\s+\d+$/i.test(s);
  const stripZWSP = (s: string) => s.replace(/\u200B/g, "");

  table.header.colSpec.forEach((col, idx) => {
    const headerCellText = stripZWSP(cleanseText(col.headerContent.text.map((txt) => text(txt) ?? "").join(""))).trim();

    if (headerCellText === "" || isDefault(headerCellText)) {
      col.headerContent.text.splice(0, col.headerContent.text.length, newLiteral({ editedText: `Kolonne ${idx + 1}` }));
    }
  });
};

export const insertTable = (draft: Draft<LetterEditorState>, focus: Focus, rows: number, cols: number): void => {
  const block = draft.redigertBrev.blocks[focus.blockIndex];
  if (block.type !== PARAGRAPH) return;

  const table = newTable(rows, cols);

  const safeContentIndex = safeIndex(focus.contentIndex, block.content);
  const insertAt = block.content.length === 0 ? 0 : safeContentIndex + 1;
  addElements([table], insertAt, block.content, block.deletedContent);

  draft.focus = { blockIndex: focus.blockIndex, contentIndex: insertAt };
  draft.isDirty = true;
};

export const removeTableRow = (draft: Draft<LetterEditorState>): void => {
  if (!isTableCellIndex(draft.focus)) return;
  const { blockIndex, contentIndex, rowIndex } = draft.focus;

  const table = draft.redigertBrev.blocks[blockIndex].content[contentIndex];
  if (!isTable(table)) return;

  if (rowIndex < 0) {
    for (const col of table.header.colSpec) {
      col.headerContent.text = [newLiteral({ editedText: "" })];
    }

    const nextRow = table.rows.length > 0 ? 0 : -1;
    draft.focus = {
      blockIndex,
      contentIndex,
      rowIndex: nextRow,
      cellIndex: 0,
      cellContentIndex: 0,
      cursorPosition: 0,
    };
    draft.isDirty = true;
    return;
  }

  removeElements(rowIndex, 1, { content: table.rows, deletedContent: table.deletedRows, id: table.id });
  const clampedRow = Math.min(rowIndex, Math.max(0, table.rows.length - 1));
  draft.focus = {
    blockIndex,
    contentIndex,
    rowIndex: clampedRow,
    cellIndex: 0,
    cellContentIndex: 0,
    cursorPosition: 0,
  };
  draft.isDirty = true;
};

export const removeTableColumn = (draft: Draft<LetterEditorState>): void => {
  if (!isTableCellIndex(draft.focus)) return;
  const { blockIndex, contentIndex, cellIndex: col } = draft.focus;

  const table = draft.redigertBrev.blocks[blockIndex].content[contentIndex];
  if (!isTable(table)) return;

  table.header.colSpec.splice(col, 1);
  table.rows.forEach((row) => row.cells.splice(col, 1));
  updateDefaultHeaderLabels(table);
  draft.isDirty = true;
};

export const removeTable = (draft: Draft<LetterEditorState>): void => {
  if (!isTableCellIndex(draft.focus)) return;
  const { blockIndex, contentIndex } = draft.focus;

  const parentBlock = draft.redigertBrev.blocks[blockIndex];
  removeElements(contentIndex, 1, parentBlock);

  // Adjust focus to a valid position
  const newContentIndex = safeIndex(contentIndex - 1, parentBlock.content);
  draft.focus = { blockIndex, contentIndex: newContentIndex, cursorPosition: 0 };

  draft.isDirty = true;
};

export const insertTableColumnLeft = (draft: Draft<LetterEditorState>): void => {
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
};

export const insertTableColumnRight = (draft: Draft<LetterEditorState>): void => {
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
};

export const insertTableRowAbove = (draft: Draft<LetterEditorState>): void => {
  if (!isTableCellIndex(draft.focus)) return;
  const { blockIndex, contentIndex, rowIndex } = draft.focus;

  if (rowIndex < 0) return;
  const table = draft.redigertBrev.blocks[blockIndex].content[contentIndex];
  if (!isTable(table)) return;
  addElements([newRow(table.header.colSpec.length)], rowIndex, table.rows, table.deletedRows);
  draft.isDirty = true;
};

export const insertTableRowBelow = (draft: Draft<LetterEditorState>): void => {
  if (!isTableCellIndex(draft.focus)) return;
  const { blockIndex, contentIndex, rowIndex } = draft.focus;

  const table = draft.redigertBrev.blocks[blockIndex].content[contentIndex];
  if (!isTable(table)) return;

  // If header is selected (rowIndex === -1), insert as first body row
  const at = rowIndex < 0 ? 0 : rowIndex + 1;
  addElements([newRow(table.header.colSpec.length)], at, table.rows, table.deletedRows);

  draft.focus = { blockIndex, contentIndex, rowIndex: at, cellIndex: 0, cellContentIndex: 0, cursorPosition: 0 };
  draft.isDirty = true;
};

/**
 * Promote a body row to header:
 * - Moves the entire body cells into header.
 * - Clears any remaining header cells (no default “Kolonne N” left behind).
 * - Removes the body row via `removeElements`
 */
export const promoteRowToHeader = (
  draft: Draft<LetterEditorState>,
  blockIndex: number,
  contentIndex: number,
  rowIndex: number,
): void => {
  const table = draft.redigertBrev.blocks[blockIndex].content[contentIndex];
  if (!isTable(table)) return;
  if (rowIndex < 0 || rowIndex >= table.rows.length) return;

  // if header already has meaningful content, do nothing
  if (!isEmptyTableHeader(table.header)) return;

  const row = table.rows[rowIndex];

  const colCount = table.header.colSpec.length;
  if (row.cells.length !== colCount) return;

  // Move body cells into header (transfer ownership)
  for (let c = 0; c < colCount; c++) {
    table.header.colSpec[c].headerContent = row.cells[c];
  }

  // If header is still empty (e.g., promoted an empty body row to header), set default labels so header renders
  if (isEmptyTableHeader(table.header)) {
    updateDefaultHeaderLabels(table);
  }
  // Remove promoted row from body
  removeElements(rowIndex, 1, { content: table.rows, deletedContent: table.deletedRows, id: table.id });

  draft.focus = { blockIndex, contentIndex, rowIndex: -1, cellIndex: 0, cellContentIndex: 0, cursorPosition: 0 };
  draft.isDirty = true;
};

export const demoteHeaderToRow = (draft: Draft<LetterEditorState>, blockIndex: number, contentIndex: number): void => {
  const table = draft.redigertBrev.blocks[blockIndex].content[contentIndex];
  if (!isTable(table)) return;
  if (isEmptyTableHeader(table.header)) return;

  const movedCells = table.header.colSpec.map((h) => h.headerContent);
  const row = newRow(table.header.colSpec.length);
  row.cells.splice(0, row.cells.length, ...movedCells);
  addElements([row], 0, table.rows, table.deletedRows);

  // Reset header cells with fresh empty cells.
  for (let c = 0; c < table.header.colSpec.length; c++) {
    table.header.colSpec[c].headerContent = newCell();
  }

  draft.focus = { blockIndex, contentIndex, rowIndex: 0, cellIndex: 0, cellContentIndex: 0, cursorPosition: 0 };
  draft.isDirty = true;
};
