import { addNewLine } from "./addNewLine";
import { create } from "./common";
import { cursorPosition, updateFocus, updateSelection } from "./cursorPosition";
import { deleteSelection, replaceSelectionWithBreak } from "./deleteSelection";
import { merge } from "./merge";
import { keepMissingFromTemplateBlock, removeMissingFromTemplateBlock } from "./missingFromTemplate";
import { insertText, paste, pasteReplacingSelection, replaceSelectionWithText } from "./paste";
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
  insertText,
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
  removeMissingFromTemplateBlock,
  removeTable,
  removeTableColumn,
  removeTableRow,
  replaceSelectionWithBreak,
  replaceSelectionWithText,
  split,
  switchFontType,
  switchTypography,
  toggleBulletList,
  toggleNumberList,
  updateContentText,
  updateFocus,
  updateSelection,
  updateSignatur,
};
export default Actions;
