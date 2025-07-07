import { makeBlankRow, makeDefaultColSpec } from "~/Brevredigering/LetterEditor/actions/common";
import type { Table } from "~/types/brevbakerTypes";

/**
 * Create a new table with the specified number of rows and columns.
 * Defaults to 2 rows and 2 columns if not specified.
 */
export const newTable = (rows = 2, cols = 2): Table => ({
  id: null,
  parentId: null,
  type: "TABLE",
  header: {
    id: null,
    parentId: null,
    colSpec: makeDefaultColSpec(cols),
  },
  rows: Array.from({ length: rows }, () => makeBlankRow(cols)),
  deletedRows: [],
});

/** Add an empty row to the bottom of the table */
export const pushRow = (table: Table): void => {
  table.rows.push(makeBlankRow(table.header.colSpec.length));
};

/** Add one column to the right side of the table */
export const pushCol = (table: Table): void => {
  table.header.colSpec.push(...makeDefaultColSpec(1));
  const blankCell = makeBlankRow(1).cells[0];
  table.rows.forEach((row) => row.cells.push({ ...blankCell }));
};
