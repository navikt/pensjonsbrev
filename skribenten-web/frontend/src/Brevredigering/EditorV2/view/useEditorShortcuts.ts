import { type KeyboardEvent, useCallback } from "react";

import { isMac } from "~/Brevredigering/LetterEditor/utils";
import { type FontTypeV2 } from "~/types/brevV2";

import { domSelection } from "../caret/domCaret";
import { deleteSelection } from "../commands/deleteSelection";
import { switchFontType } from "../commands/switchFontType";
import { switchTypography } from "../commands/switchTypography";
import { toggleList } from "../commands/toggleList";
import { useEditorV2 } from "./EditorContextV2";
import { TYPOGRAPHY } from "./menu/typography";

function fontShortcut(event: KeyboardEvent): FontTypeV2 | "BLOCKED" | undefined {
  const modifier = isMac ? event.metaKey : event.ctrlKey;
  if (!modifier) return undefined;
  if (event.key === "b" || (!isMac && event.key === "f")) return "BOLD";
  if (event.key === "i") return "ITALIC";
  if (event.key === "u") return "BLOCKED";
  return undefined;
}

function historyShortcut(event: KeyboardEvent): "undo" | "redo" | undefined {
  if (!(isMac ? event.metaKey : event.ctrlKey)) return undefined;
  if (event.key === "z") return event.shiftKey ? "redo" : "undo";
  if (event.key === "y") return "redo";
  return undefined;
}

export function useEditorShortcuts() {
  const { dispatch, undo, redo, freeze } = useEditorV2();

  return useCallback(
    (event: KeyboardEvent<HTMLElement>) => {
      if (event.defaultPrevented || freeze) return;

      const history = historyShortcut(event);
      if (history) {
        event.preventDefault();
        if (history === "undo") undo();
        else redo();
        return;
      }

      if (event.key === "Backspace" || event.key === "Delete") {
        if (event.repeat || event.ctrlKey || event.metaKey || event.altKey || event.nativeEvent.isComposing) return;
        const selection = domSelection(event.currentTarget);
        if (selection) {
          event.preventDefault();
          dispatch(deleteSelection, selection);
        }
        return;
      }

      if (event.repeat) return;

      if (event.altKey) {
        const typography = TYPOGRAPHY.find((entry) => entry.key === event.code);
        if (typography) {
          event.preventDefault();
          dispatch(switchTypography, typography.type);
        } else if (event.code === "Digit5" || event.code === "Digit6") {
          event.preventDefault();
          dispatch(toggleList, event.code === "Digit5" ? "ITEM_LIST" : "NUMBERED_LIST");
        }
        return;
      }

      const font = fontShortcut(event);
      if (font) {
        event.preventDefault();
        if (font !== "BLOCKED") dispatch(switchFontType, font);
      }
    },
    [dispatch, undo, redo, freeze],
  );
}
