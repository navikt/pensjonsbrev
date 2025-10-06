import type { Dispatch, SetStateAction } from "react";
import { useCallback } from "react";

import { getSelectionFocus } from "~/Brevredigering/LetterEditor/services/caretUtils";

import Actions from "./actions";
import { applyAction } from "./lib/actions";
import type { LetterEditorState } from "./model/state";

export enum Typography {
  PARAGRAPH = "PARAGRAPH",
  TITLE1 = "TITLE1",
  TITLE2 = "TITLE2",
}

export const TypographyToText = {
  [Typography.TITLE1]: "Overskrift (alt/option+1)",
  [Typography.TITLE2]: "Underoverskrift (alt/option+2)",
  [Typography.PARAGRAPH]: "Normal (alt/option+3)",
} as const;

export const useEditorKeyboardShortcuts = (setEditorState: Dispatch<SetStateAction<LetterEditorState>>) => {
  return useCallback(
    (event: React.KeyboardEvent<HTMLDivElement>) => {
      if (event.altKey && event.code === "Digit1") {
        event.preventDefault();
        applyAction(Actions.switchTypography, setEditorState, Typography.PARAGRAPH);
      } else if (event.altKey && event.code === "Digit2") {
        event.preventDefault();
        applyAction(Actions.switchTypography, setEditorState, Typography.TITLE1);
      } else if (event.altKey && event.code === "Digit3") {
        event.preventDefault();
        applyAction(Actions.switchTypography, setEditorState, Typography.TITLE2);
      } else if (event.key === "Backspace" || event.key === "Delete") {
        const rootDiv = event.currentTarget as HTMLElement;
        const selectionFocus = getSelectionFocus(rootDiv);
        if (selectionFocus) {
          event.preventDefault();
          applyAction(Actions.deleteSelection, setEditorState, selectionFocus);
          globalThis.getSelection()?.collapse(null);
        }
      }
    },
    [setEditorState],
  );
};
