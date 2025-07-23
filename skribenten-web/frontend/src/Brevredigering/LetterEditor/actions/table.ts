import type { Draft } from "immer";
import { produce } from "immer";

import type { Content, LiteralValue, Table } from "~/types/brevbakerTypes";
import { LITERAL, PARAGRAPH, TABLE } from "~/types/brevbakerTypes";

import type { Action } from "../lib/actions";
import type { Focus, LetterEditorState } from "../model/state";
import { newTable, pushCol, pushRow } from "../model/tableHelpers";
import { addElements, newColSpec, newRow, text } from "./common";
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

    addElements(
      [newTable(rows, cols)],
      focus.contentIndex + 1,
      block.content as Draft<Content[]>,
      block.deletedContent,
    );
    draft.focus = { blockIndex: focus.blockIndex, contentIndex: focus.contentIndex + 1 };
    draft.isDirty = true;
  },
);

export const addTableRow: Action<LetterEditorState, [blockIdx: number, contentIdx: number]> = produce(
  (draft, blockIdx, contentIdx) => {
    const content = draft.redigertBrev.blocks[blockIdx].content[contentIdx];
    if (content?.type !== TABLE) return;

    pushRow(content);
    draft.isDirty = true;
  },
);

export const addTableColumn: Action<LetterEditorState, [blockIdx: number, contentIdx: number]> = produce(
  (draft, blockIdx, contentIdx) => {
    const content = draft.redigertBrev.blocks[blockIdx].content[contentIdx];
    if (content?.type !== TABLE) return;

    pushCol(content);
    updateDefaultHeaderLabels(content);
    draft.isDirty = true;
  },
);

export const removeTableRow = produce<LetterEditorState>((draft) => {
  const selection = draft.tableSelection ?? draft.contextMenuCell;
  if (!selection || selection.rowIndex === undefined || selection.rowIndex < 0) return;

  const table = draft.redigertBrev.blocks[selection.blockIndex].content[selection.contentIndex] as Table;
  table.rows.splice(selection.rowIndex, 1);

  draft.isDirty = true;
});

export const removeTableColumn = produce<LetterEditorState>((draft) => {
  const selection = draft.tableSelection ?? draft.contextMenuCell;
  if (!selection || selection.colIndex === undefined) return;

  const col = selection.colIndex!;
  const table = draft.redigertBrev.blocks[selection.blockIndex].content[selection.contentIndex] as Table;

  table.header.colSpec.splice(col, 1);
  table.rows.forEach((row) => row.cells.splice(col, 1));
  updateDefaultHeaderLabels(table);
  draft.isDirty = true;
});

export const removeTable = produce<LetterEditorState>((draft) => {
  const selection = draft.tableSelection ?? draft.contextMenuCell;
  if (!selection) return;

  draft.redigertBrev.blocks[selection.blockIndex].content.splice(selection.contentIndex, 1);
  draft.isDirty = true;
});

export const insertTableColumnLeft: Action<LetterEditorState, []> = produce((draft) => {
  const selection = draft.tableSelection ?? draft.contextMenuCell;
  if (!selection || selection.colIndex === undefined) return;

  const table = draft.redigertBrev.blocks[selection.blockIndex].content[selection.contentIndex] as Table;
  const at = selection.colIndex;

  table.header.colSpec.splice(at, 0, ...newColSpec(1));
  table.rows.forEach((row) => row.cells.splice(at, 0, newRow(1).cells[0]));
  updateDefaultHeaderLabels(table);
  draft.isDirty = true;
});

export const insertTableColumnRight: Action<LetterEditorState, []> = produce((draft) => {
  const selection = draft.tableSelection ?? draft.contextMenuCell;
  if (!selection || selection.colIndex === undefined) return;

  const table = draft.redigertBrev.blocks[selection.blockIndex].content[selection.contentIndex] as Table;
  const at = selection.colIndex + 1;

  table.header.colSpec.splice(at, 0, ...newColSpec(1));
  table.rows.forEach((row) => row.cells.splice(at, 0, newRow(1).cells[0]));
  updateDefaultHeaderLabels(table);
  draft.isDirty = true;
});

export const insertTableRowAbove: Action<LetterEditorState, []> = produce((draft) => {
  const selection = draft.tableSelection ?? draft.contextMenuCell;
  if (!selection || selection.rowIndex === undefined) return;

  const table = draft.redigertBrev.blocks[selection.blockIndex].content[selection.contentIndex] as Table;
  table.rows.splice(selection.rowIndex, 0, newRow(table.header.colSpec.length));

  draft.isDirty = true;
});

export const insertTableRowBelow: Action<LetterEditorState, []> = produce((draft) => {
  const selection = draft.tableSelection ?? draft.contextMenuCell;
  if (!selection || selection.rowIndex === undefined) return;

  const table = draft.redigertBrev.blocks[selection.blockIndex].content[selection.contentIndex] as Table;
  table.rows.splice(selection.rowIndex + 1, 0, newRow(table.header.colSpec.length));

  draft.isDirty = true;
});
