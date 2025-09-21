import type { Dispatch, SetStateAction } from "react";

import Actions from "./actions";
import { applyAction } from "./lib/actions";
import type { LetterEditorState } from "./model/state";

export enum Typography {
  PARAGRAPH = "PARAGRAPH",
  TITLE1 = "TITLE1",
  TITLE2 = "TITLE2",
}

export const TypographyToText = {
  [Typography.PARAGRAPH]: "Normal (alt+1)",
  [Typography.TITLE1]: "Overskrift (alt+2)",
  [Typography.TITLE2]: "Underoverskrift (alt+3)",
} as const;

export const useEditorKeyboardShortcuts = (
  editorState: LetterEditorState,
  setEditorState: Dispatch<SetStateAction<LetterEditorState>>,
) => {
  return (event: React.KeyboardEvent<HTMLDivElement>) => {
    if (event.altKey && event.code === "Digit1") {
      event.preventDefault();
      applyAction(Actions.switchTypography, setEditorState, editorState.focus, Typography.PARAGRAPH);
    } else if (event.altKey && event.code === "Digit2") {
      event.preventDefault();
      applyAction(Actions.switchTypography, setEditorState, editorState.focus, Typography.TITLE1);
    } else if (event.altKey && event.code === "Digit3") {
      event.preventDefault();
      applyAction(Actions.switchTypography, setEditorState, editorState.focus, Typography.TITLE2);
    }
  };
};
