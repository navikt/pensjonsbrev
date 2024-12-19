import { cursorPosition } from "~/Brevredigering/LetterEditor/actions/cursorPosition";
import { paste } from "~/Brevredigering/LetterEditor/actions/paste";

import { create } from "./common";
import { merge } from "./merge";
import { split } from "./split";
import { switchTypography } from "./switchTypography";
import { toggleBulletList } from "./toggleBulletList";
import { updateContentText } from "./updateContentText";

const Actions = {
  create,
  cursorPosition,
  merge,
  paste,
  split,
  switchTypography,
  updateContentText,
  toggleBulletList,
};
export default Actions;
