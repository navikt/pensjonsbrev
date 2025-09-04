import { produceWithPatches } from "immer";

import { tableRecipe } from "./tableRecipe";

export const patchGeneratingTableReducer = produceWithPatches(tableRecipe);
