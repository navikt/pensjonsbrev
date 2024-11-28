import { cursorPosition } from "~/Brevredigering/LetterEditor/actions/cursorPosition";
import { paste } from "~/Brevredigering/LetterEditor/actions/paste";

import { create } from "./common";
import { merge } from "./merge";
import { split } from "./split";
import { switchFontType } from "./switchFontType";
import { switchTypography } from "./switchTypography";
import { updateContentText } from "./updateContentText";

const Actions = {
  create,
  cursorPosition,
  merge,
  paste,
  split,
  switchTypography,
  updateContentText,
  switchFontType,
};
export default Actions;
