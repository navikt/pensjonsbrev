import { type Action, withPatches } from "../lib/actions";
import { type LetterEditorState, type SelectionIndex } from "../model/state";
import { isTableCellIndex } from "../model/utils";
import { addNewLineRecipe } from "./addNewLine";
import { isIndexAfter, isValidIndex } from "./common";
import { deleteSelectionRecipe } from "./deleteSelection";
import { splitRecipe } from "./split";

export const deleteSelectionAndEnter: Action<LetterEditorState, [selection: SelectionIndex, softBreak: boolean]> =
  withPatches((draft, selection, softBreak) => {
    if (
      !isValidIndex(draft.redigertBrev, selection.start) ||
      !isValidIndex(draft.redigertBrev, selection.end) ||
      !isIndexAfter(selection.start, selection.end)
    ) {
      return;
    }

    deleteSelectionRecipe(draft, selection);
    // Enter does not insert breaks inside table cells.
    if (isTableCellIndex(draft.focus)) return;

    if (softBreak) {
      addNewLineRecipe(draft, draft.focus);
    } else {
      splitRecipe(draft, draft.focus, draft.focus.cursorPosition ?? 0);
    }
  });
