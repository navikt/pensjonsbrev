import type { Dispatch, SetStateAction } from "react";
import { useCallback } from "react";

import { FontType } from "~/types/brevbakerTypes";

import Actions from "./actions";
import { applyAction } from "./lib/actions";
import type { LetterEditorState } from "./model/state";

export enum Typography {
  PARAGRAPH = "PARAGRAPH",
  TITLE1 = "TITLE1",
  TITLE2 = "TITLE2",
  TITLE3 = "TITLE3",
}

export const isMac = !globalThis.Cypress ? /Mac|iPod|iPad/.test(navigator.userAgent) : false;

export const TypographyToText = isMac
  ? ({
      [Typography.TITLE1]: "Overskrift 1 (⌥+1)",
      [Typography.TITLE2]: "Overskrift 2 (⌥+2)",
      [Typography.TITLE3]: "Overskrift 2 (⌥+3)",
      [Typography.PARAGRAPH]: "Normal (⌥+4)",
    } as const)
  : ({
      [Typography.TITLE1]: "Overskrift 1 (Alt+1)",
      [Typography.TITLE2]: "Overskrift 2 (Alt+2)",
      [Typography.TITLE3]: "Overskrift 2 (Alt+3)",
      [Typography.PARAGRAPH]: "Normal (Alt+4)",
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
        applyAction(Actions.switchTypography, setEditorState, Typography.TITLE3);
      } else if (event.altKey && event.code === "Digit4") {
        event.preventDefault();
        applyAction(Actions.switchTypography, setEditorState, Typography.PARAGRAPH);
      }

      // Word-hurtigtaster (norsk)
      // fet/bold        cmd-b (macos) | ctrl-f (windows) | ctrl-f (web)
      // kursiv/italic   cmd-i (macos) | ctrl-i (windows) | ctrl-i (web)
      // finn/søk/find   cmd-f (macos) | ctrl-b (windows) | ctrl-b (web)
      // søk i Chrome    cmd-f (macos) | ctrl-f / F3 (windows)
      // ref. Microsoft Support 2025-10-15
      // https://support.microsoft.com/nb-no/office/hurtigtaster-i-word-95ef89dd-7142-4b50-afb2-f762f663ceb2
      if (!isMac) {
        if (event.ctrlKey) {
          if (event.key === "f") {
            event.preventDefault();
            applyAction(Actions.switchFontType, setEditorState, FontType.BOLD);
          } else if (event.key === "i") {
            event.preventDefault();
            applyAction(Actions.switchFontType, setEditorState, FontType.ITALIC);
          } else if (event.key === "u") {
            // block ctrl-u from applying underline
            event.preventDefault();
          }
        }
      } else {
        if (event.metaKey) {
          if (event.key === "b") {
            event.preventDefault();
            applyAction(Actions.switchFontType, setEditorState, FontType.BOLD);
          } else if (event.key === "i") {
            event.preventDefault();
            applyAction(Actions.switchFontType, setEditorState, FontType.ITALIC);
          } else if (event.key === "u") {
            // block cmd-u from applying underline
            event.preventDefault();
          }
        }
      }
    },
    [setEditorState],
  );
};
