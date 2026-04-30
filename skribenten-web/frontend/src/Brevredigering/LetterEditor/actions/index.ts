import { addNewLine } from "./addNewLine";
import { create } from "./common";
import { cursorPosition, updateFocus } from "./cursorPosition";
import { deleteSelection } from "./deleteSelection";
import { merge } from "./merge";
import { paste } from "./paste";
import { split } from "./split";
import { switchFontType } from "./switchFontType";
import { switchTypography } from "./switchTypography";
import {
  demoteHeaderToRow,
  insertTable,
  insertTableColumnLeft,
  insertTableColumnRight,
  insertTableRowAbove,
  insertTableRowBelow,
  moveTableRow,
  promoteRowToHeader,
  removeTable,
  removeTableColumn,
  removeTableRow,
} from "./table";
import { toggleBulletList, toggleNumberList } from "./toggleListType";
import { updateContentText } from "./updateContentText";
import { updateSignatur } from "./updateSignatur";

const Actions = {
  addNewLine,
  create,
  cursorPosition,
  deleteSelection,
  demoteHeaderToRow,
  insertTable,
  insertTableColumnLeft,
  insertTableColumnRight,
  insertTableRowAbove,
  insertTableRowBelow,
  merge,
  moveTableRow,
  paste,
  promoteRowToHeader,
  updateFocus,
  removeTable,
  removeTableColumn,
  removeTableRow,
  split,
  switchFontType,
  switchTypography,
  toggleBulletList,
  toggleNumberList,
  updateContentText,
  updateSignatur,
};
export default Actions;
