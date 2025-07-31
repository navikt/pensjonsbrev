import { addNewLine } from "~/Brevredigering/LetterEditor/actions/addNewLine";
import { cursorPosition } from "~/Brevredigering/LetterEditor/actions/cursorPosition";
import { paste } from "~/Brevredigering/LetterEditor/actions/paste";
import { updateSaksbehandlerSignatur } from "~/Brevredigering/LetterEditor/actions/signatur";

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
  updateSaksbehandlerSignatur,
  toggleBulletList,
  switchFontType,
};
export default Actions;
