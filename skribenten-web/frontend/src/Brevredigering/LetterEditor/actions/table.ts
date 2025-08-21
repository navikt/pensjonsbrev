import type { Draft } from "immer";
import { produce } from "immer";

import type { LiteralValue, Table, TextContent } from "~/types/brevbakerTypes";
import { LITERAL, NEW_LINE, PARAGRAPH, VARIABLE } from "~/types/brevbakerTypes";

import type { Action } from "../lib/actions";
import type { Focus, LetterEditorState } from "../model/state";
import { newTable } from "../model/tableHelpers";
import { isTableCellIndex } from "../model/utils";
import {
  addElements,
  createNewLine,
  hasHeaderContentCols,
  isTable,
  newColSpec,
  newLiteral,
  newRow,
  newVariable,
  removeElements,
  safeIndex,
  text,
} from "./common";
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
  const isDefault = (s: string) => /^Kolonne\s+\d+$/i.test(s);

  table.header.colSpec.forEach((col, idx) => {
    let litIdx = col.headerContent.text.findIndex((txt) => txt.type === LITERAL);
    if (litIdx === -1) {
      col.headerContent.text.splice(0, col.headerContent.text.length, newLiteral({ editedText: "" }));
      litIdx = 0;
    }
    const literal = col.headerContent.text[litIdx] as LiteralValue;
    const current = (text(literal) ?? "").trim();

    if (current === "" || isDefault(current)) {
      updateLiteralText(literal, `Kolonne ${idx + 1}`);
    }
  });
};

export const insertTable: Action<LetterEditorState, [focus: Focus, rows: number, cols: number]> = produce(
  (draft, focus, rows, cols) => {
    const block = draft.redigertBrev.blocks[focus.blockIndex];
    if (block.type !== PARAGRAPH) return;

    const table = newTable(rows, cols);

    const safeContentIndex = safeIndex(focus.contentIndex, block.content);
    const insertAt = block.content.length === 0 ? 0 : safeContentIndex + 1;
    addElements([table], insertAt, block.content, block.deletedContent);

    draft.focus = { blockIndex: focus.blockIndex, contentIndex: insertAt };
    draft.isDirty = true;
  },
);

export const removeTableRow = produce<LetterEditorState>((draft) => {
  if (!isTableCellIndex(draft.focus)) return;
  const { blockIndex, contentIndex, rowIndex } = draft.focus;

  const table = draft.redigertBrev.blocks[blockIndex].content[contentIndex];
  if (!isTable(table)) return;

  if (rowIndex < 0) {
    for (const col of table.header.colSpec) {
      col.headerContent.text = [newLiteral({ editedText: "" })];
    }

    const nextRow = table.rows.length > 0 ? 0 : 0;
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
  const newContentIndex = safeIndex(contentIndex - 1, parentBlock.content);
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

  const table = draft.redigertBrev.blocks[blockIndex].content[contentIndex];
  if (!isTable(table)) return;

  // If header is selected (rowIndex === -1), insert as first body row
  const at = rowIndex < 0 ? 0 : rowIndex + 1;
  addElements([newRow(table.header.colSpec.length)], at, table.rows, table.deletedRows);

  draft.focus = { blockIndex, contentIndex, rowIndex: at, cellIndex: 0, cellContentIndex: 0, cursorPosition: 0 };
  draft.isDirty = true;
});

// Deep-clone TextContent[] into fresh, “new” values (ids reset)
function cloneTexts(source: Draft<TextContent[]>): TextContent[] {
  return source.map((t) => {
    switch (t.type) {
      case LITERAL:
        return newLiteral({
          editedText: text(t) ?? "",
          fontType: t.editedFontType ?? t.fontType,
          editedFontType: t.editedFontType ?? null,
          tags: t.tags,
        });
      case VARIABLE:
        return newVariable({ text: t.text, fontType: t.fontType });
      case NEW_LINE:
        return createNewLine();
    }
  });
}

/**
 * Promote a body row to header:
 * - Copies cell text into existing header colSpec (keeps alignment/span/ids).
 * - Clears any remaining header cells (no default “Kolonne N” left behind).
 * - Removes the body row via `removeElements`
 */
export const promoteRowToHeader: Action<
  LetterEditorState,
  [blockIndex: number, contentIndex: number, rowIndex: number]
> = produce((draft, blockIndex, contentIndex, rowIndex) => {
  const table = draft.redigertBrev.blocks[blockIndex].content[contentIndex];
  if (!isTable(table)) return;
  if (rowIndex < 0 || rowIndex >= table.rows.length) return;

  // if header already has meaningful content, do nothing
  if (hasHeaderContentCols(table.header?.colSpec)) return;

  const row = table.rows[rowIndex];

  const colCount = table.header.colSpec.length;
  if (row.cells.length !== colCount) return;

  // Copy body row text into header cells
  for (let c = 0; c < colCount; c++) {
    const sourceTexts = row.cells[c].text;
    table.header.colSpec[c].headerContent.text =
      sourceTexts.length > 0 ? cloneTexts(sourceTexts) : [newLiteral({ editedText: "" })];
  }
  // If header is still empty (e.g., promoted an empty body row to header), set default labels so header renders
  if (!hasHeaderContentCols(table.header?.colSpec)) {
    updateDefaultHeaderLabels(table);
  }
  // Remove promoted row from body
  removeElements(rowIndex, 1, { content: table.rows, deletedContent: table.deletedRows, id: table.id });

  draft.focus = { blockIndex, contentIndex, rowIndex: -1, cellIndex: 0, cellContentIndex: 0, cursorPosition: 0 };
  draft.isDirty = true;
});

/**
 * Demote header to a regular first row:
 * - Copies header colSpec text into a new first body row.
 * - Clears header cell text so <thead> stops rendering.
 */
export const demoteHeaderToRow: Action<LetterEditorState, [blockIndex: number, contentIndex: number]> = produce(
  (draft, blockIndex, contentIndex) => {
    const table = draft.redigertBrev.blocks[blockIndex].content[contentIndex];
    if (!isTable(table)) return;

    if (!hasHeaderContentCols(table.header?.colSpec)) return;

    const colCount = table.header.colSpec.length;

    // Build a new Row from header cells
    const newBodyRow = newRow(colCount);
    for (let c = 0; c < colCount; c++) {
      const headerTexts = table.header.colSpec[c].headerContent.text as Draft<TextContent[]>;
      const cloned = cloneTexts(headerTexts);
      newBodyRow.cells[c].text.splice(0, newBodyRow.cells[c].text.length, ...cloned);
    }

    // Insert as first body row
    addElements([newBodyRow], 0, table.rows, table.deletedRows);

    // Clear header cell text to hide <thead>
    for (let c = 0; c < colCount; c++) {
      table.header.colSpec[c].headerContent.text = [newLiteral({ editedText: "" })];
    }

    draft.focus = { blockIndex, contentIndex, rowIndex: 0, cellIndex: 0, cellContentIndex: 0, cursorPosition: 0 };
    draft.isDirty = true;
  },
);
