import { addNewLine } from "./addNewLine";
import { create } from "./common";
import { cursorPosition } from "./cursorPosition";
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
  promoteRowToHeader,
  removeTable,
  removeTableColumn,
  removeTableRow,
} from "./table";
import { toggleBulletList } from "./toggleBulletList";
import { updateContentText } from "./updateContentText";
import { updateSignatur } from "./updateSignatur";

const Actions = {
  addNewLine,
  create,
  cursorPosition,
  demoteHeaderToRow,
  insertTable,
  insertTableColumnLeft,
  insertTableColumnRight,
  insertTableRowAbove,
  insertTableRowBelow,
  merge,
  paste,
  promoteRowToHeader,
  removeTable,
  removeTableColumn,
  removeTableRow,
  split,
  switchFontType,
  switchTypography,
  toggleBulletList,
  updateContentText,
  updateSignatur,
};
export default Actions;
