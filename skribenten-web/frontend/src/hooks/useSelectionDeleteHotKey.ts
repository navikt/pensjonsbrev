import { useEffect } from "react";

import type { SelectionIndex } from "~/Brevredigering/LetterEditor/model/state";
import { getSelectionFocus } from "~/Brevredigering/LetterEditor/services/caretUtils";

export function useSelectionDeleteHotkey(
  rootEl: HTMLElement | null,
  onDelete: (focus: SelectionIndex) => void,
  enabled = true,
) {
  useEffect(() => {
    if (!enabled) return;

    const handler = (event: KeyboardEvent) => {
      if (event.key !== "Backspace" && event.key !== "Delete") return;

      if (
        event.repeat ||
        event.ctrlKey ||
        event.metaKey ||
        event.altKey ||
        ("isComposing" in event && (event as KeyboardEvent & { isComposing?: boolean }).isComposing)
      )
        return;

      const root = rootEl;
      if (!root) return;

      const selection = globalThis.getSelection?.();
      if (!selection || selection.rangeCount === 0 || selection.isCollapsed) return;

      const range = selection.getRangeAt(0);
      if (!(root.contains(range.startContainer) || root.contains(range.endContainer))) return;

      const focus = getSelectionFocus(root);
      if (!focus) return;

      event.preventDefault();
      event.stopPropagation();
      if (typeof (event as KeyboardEvent).stopImmediatePropagation === "function") {
        (event as KeyboardEvent).stopImmediatePropagation();
      }

      onDelete(focus);
      selection.removeAllRanges();
    };

    document.addEventListener("keydown", handler, true);
    return () => document.removeEventListener("keydown", handler, true);
  }, [rootEl, onDelete, enabled]);
}
