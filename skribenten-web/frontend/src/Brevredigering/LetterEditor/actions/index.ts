import { addNewLine } from "~/Brevredigering/LetterEditor/actions/addNewLine";
import { cursorPosition } from "~/Brevredigering/LetterEditor/actions/cursorPosition";
import { paste } from "~/Brevredigering/LetterEditor/actions/paste";
import { updateSignatur } from "~/Brevredigering/LetterEditor/actions/signatur";
import {
  insertTable,
  insertTableColumnLeft,
  insertTableColumnRight,
  insertTableRowAbove,
  insertTableRowBelow,
  removeTable,
  removeTableColumn,
  removeTableRow,
} from "~/Brevredigering/LetterEditor/actions/table";

import { create } from "./common";
import { merge } from "./merge";
import { split } from "./split";
import { switchFontType } from "./switchFontType";
import { switchTypography } from "./switchTypography";
import { toggleBulletList } from "./toggleBulletList";
import { updateContentText } from "./updateContentText";

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
};
export default Actions;
