import { newColSpec, newRow } from "~/Brevredigering/LetterEditor/actions/common";
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
    colSpec: newColSpec(cols),
  },
  rows: Array.from({ length: rows }, () => newRow(cols)),
  deletedRows: [],
});
