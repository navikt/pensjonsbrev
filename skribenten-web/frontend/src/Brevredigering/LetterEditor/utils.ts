import type { Dispatch, SetStateAction } from "react";
import { useCallback } from "react";

import Actions from "./actions";
import { applyAction } from "./lib/actions";
import type { LetterEditorState } from "./model/state";

export enum Typography {
  PARAGRAPH = "PARAGRAPH",
  TITLE1 = "TITLE1",
  TITLE2 = "TITLE2",
}

const isMacOS = navigator.userAgent.includes("Mac OS X");

export const TypographyToText = isMacOS
  ? ({
      [Typography.TITLE1]: "Overskrift 1 (⌥+1)",
      [Typography.TITLE2]: "Overskrift 2 (⌥+2)",
      [Typography.PARAGRAPH]: "Normal (⌥+3)",
    } as const)
  : ({
      [Typography.TITLE1]: "Overskrift 1 (Alt+1)",
      [Typography.TITLE2]: "Overskrift 2 (Alt+2)",
      [Typography.PARAGRAPH]: "Normal (Alt+3)",
    } as const);

export const useEditorKeyboardShortcuts = (setEditorState: Dispatch<SetStateAction<LetterEditorState>>) => {
  return useCallback(
    (event: React.KeyboardEvent<HTMLDivElement>) => {
      if (event.defaultPrevented || event.repeat) return;
      if (event.altKey && event.code === "Digit1") {
        event.preventDefault();
        applyAction(Actions.switchTypography, setEditorState, Typography.TITLE1);
      } else if (event.altKey && event.code === "Digit2") {
        event.preventDefault();
        applyAction(Actions.switchTypography, setEditorState, Typography.TITLE2);
      } else if (event.altKey && event.code === "Digit3") {
        event.preventDefault();
        applyAction(Actions.switchTypography, setEditorState, Typography.PARAGRAPH);
      }
    },
    [setEditorState],
  );
};
