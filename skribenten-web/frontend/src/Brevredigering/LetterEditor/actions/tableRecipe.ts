import type { Patch } from "immer";
import { applyPatches } from "immer";

import type { Focus, LetterEditorState } from "../model/state";
import {
  demoteHeaderToRow,
  insertTable,
  insertTableColumnLeft,
  insertTableColumnRight,
  insertTableRowAbove,
  insertTableRowBelow,
  promoteRowToHeader,
  removeTable,
  removeTableColumn,
  removeTableRow,
} from "./table";

export type TableAction =
  | { type: "TABLE_INSERT"; focus: Focus; rows: number; cols: number }
  | { type: "TABLE_REMOVE_ROW" }
  | { type: "TABLE_REMOVE_COLUMN" }
  | { type: "TABLE_REMOVE" }
  | { type: "TABLE_INSERT_COL_LEFT" }
  | { type: "TABLE_INSERT_COL_RIGHT" }
  | { type: "TABLE_INSERT_ROW_ABOVE" }
  | { type: "TABLE_INSERT_ROW_BELOW" }
  | { type: "TABLE_PROMOTE_ROW_TO_HEADER"; blockIndex: number; contentIndex: number; rowIndex: number }
  | { type: "TABLE_DEMOTE_HEADER_TO_ROW"; blockIndex: number; contentIndex: number }
  | { type: "APPLY_TABLE_PATCHES"; patches: Patch[] };

export const tableRecipe = (draft: LetterEditorState, action: TableAction) => {
  switch (action.type) {
    case "TABLE_INSERT":
      insertTable(draft, action.focus, action.rows, action.cols);
      break;
    case "TABLE_REMOVE_ROW":
      removeTableRow(draft);
      break;
    case "TABLE_REMOVE_COLUMN":
      removeTableColumn(draft);
      break;
    case "TABLE_REMOVE":
      removeTable(draft);
      break;
    case "TABLE_INSERT_COL_LEFT":
      insertTableColumnLeft(draft);
      break;
    case "TABLE_INSERT_COL_RIGHT":
      insertTableColumnRight(draft);
      break;
    case "TABLE_INSERT_ROW_ABOVE":
      insertTableRowAbove(draft);
      break;
    case "TABLE_INSERT_ROW_BELOW":
      insertTableRowBelow(draft);
      break;
    case "TABLE_PROMOTE_ROW_TO_HEADER":
      promoteRowToHeader(draft, action.blockIndex, action.contentIndex, action.rowIndex);
      break;
    case "TABLE_DEMOTE_HEADER_TO_ROW":
      demoteHeaderToRow(draft, action.blockIndex, action.contentIndex);
      break;
    case "APPLY_TABLE_PATCHES":
      return applyPatches(draft, action.patches);
  }
};
