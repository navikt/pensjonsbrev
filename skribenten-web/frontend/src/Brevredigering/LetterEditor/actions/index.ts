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
} from "~/Brevredigering/LetterEditor/actions/table";

import { addNewLine } from "./addNewLine";
import { create } from "./common";
import { cursorPosition, updateFocus } from "./cursorPosition";
import { merge } from "./merge";
import { paste } from "./paste";
import { split } from "./split";
import { switchFontType } from "./switchFontType";
import { switchTypography } from "./switchTypography";
import { toggleBulletList } from "./toggleBulletList";
import { updateContentText } from "./updateContentText";
import { updateSignatur } from "./updateSignatur";
import { updateTitle } from "./updateTitle";

const Actions = {
  create,
  cursorPosition,
  merge,
  addNewLine,
  paste,
  split,
  switchTypography,
  updateContentText,
  updateSignatur,
  updateTitle,
  toggleBulletList,
  switchFontType,
  insertTable,

  removeTableRow,
  removeTableColumn,
  removeTable,
  insertTableColumnLeft,
  insertTableColumnRight,
  insertTableRowAbove,
  insertTableRowBelow,
  promoteRowToHeader,
  demoteHeaderToRow,
  updateFocus,
};
export default Actions;
