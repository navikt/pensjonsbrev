import { cursor } from "~/pages/Brevredigering/LetterEditor/actions/cursor";

import { create } from "./common";
import { merge } from "./merge";
import { split } from "./split";
import { switchTypography } from "./switchTypography";
import { updateContentText } from "./updateContentText";
import { updateLetter } from "./updateLetter";

const Actions = {
  create,
  cursor,
  merge,
  split,
  switchTypography,
  updateContentText,
  updateLetter,
};
export default Actions;
