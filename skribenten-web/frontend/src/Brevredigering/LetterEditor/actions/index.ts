import { addNewLine } from "./addNewLine";
import { create } from "./common";
import { cursorPosition, updateFocus } from "./cursorPosition";
import { deleteSelection } from "./deleteSelection";
import { merge } from "./merge";
import { keepMissingFromTemplateBlock, removeMissingFromTemplateBlock } from "./missingFromTemplate";
import { paste, pasteReplacingSelection } from "./paste";
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
import { toggleBulletList } from "./toggleBulletList";
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
  keepMissingFromTemplateBlock,
  merge,
  moveTableRow,
  paste,
  pasteReplacingSelection,
  promoteRowToHeader,
  updateFocus,
  removeMissingFromTemplateBlock,
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
