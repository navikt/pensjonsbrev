import { cursorPosition } from "~/Brevredigering/LetterEditor/actions/cursorPosition";

import { create } from "./common";
import { merge } from "./merge";
import { split } from "./split";
import { switchTypography } from "./switchTypography";
import { updateContentText } from "./updateContentText";

const Actions = {
  create,
  cursorPosition,
  merge,
  split,
  switchTypography,
  updateContentText,
};
export default Actions;
