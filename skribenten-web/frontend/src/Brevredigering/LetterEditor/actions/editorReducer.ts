import { produceWithPatches } from "immer";

import { editorRecipeReducer } from "./editorRecipe";

export const patchGeneratingEditorReducer = produceWithPatches(editorRecipeReducer);
