import { useEffect } from "react";
import { flushSync } from "react-dom";

import { type SelectionIndex } from "~/Brevredigering/LetterEditor/model/state";
import { getSelectionFocus } from "~/Brevredigering/LetterEditor/services/caretUtils";

const isDeleteKey = (event: KeyboardEvent) => event.key === "Backspace" || event.key === "Delete";
// Single characters, and Enter which splits the text at the caret
const isTypingKey = (event: KeyboardEvent) => event.key.length === 1 || event.key === "Enter";

const literalOf = (node: Node) =>
  (node instanceof Element ? node : node.parentElement)?.closest("[data-literal-index]") ?? null;

/** Diff-decorated literals also show deleted text, so DOM offsets in them do not match the letter model. */
const touchesDiffDecoration = (root: HTMLElement, range: Range) =>
  [...root.querySelectorAll("[data-diff-version]")].some((element) => range.intersectsNode(element));

/**
 * Deletes a selection with Backspace/Delete. Typing over a selection that spans several literals deletes it first,
 * so the key replaces the whole selection (the browser alone would only replace the part in the first literal).
 *
 * If the selection touches diff decorations, the key only calls `onDiffEditIntent`, like the first edit in a
 * decorated literal: diff mode is turned off without changing the letter.
 */
export function useSelectionDeleteHotkey(
  rootEl: HTMLElement | null,
  onDelete: (focus: SelectionIndex) => void,
  enabled = true,
  onDiffEditIntent?: () => void,
) {
  useEffect(() => {
    if (!enabled) return;

    const handler = (event: KeyboardEvent) => {
      const deleting = isDeleteKey(event);
      if (!deleting && !isTypingKey(event)) return;

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
      // Only keys pressed in the editor; e.g. Space on a focused toolbar button must not delete the selection
      if (!(event.target instanceof Node && root.contains(event.target))) return;

      const selection = globalThis.getSelection?.();
      if (!selection || selection.rangeCount === 0 || selection.isCollapsed) return;

      const range = selection.getRangeAt(0);
      if (!(root.contains(range.startContainer) || root.contains(range.endContainer))) return;
      if (!deleting && literalOf(range.startContainer) === literalOf(range.endContainer)) return;

      if (touchesDiffDecoration(root, range)) {
        event.preventDefault();
        event.stopPropagation();
        onDiffEditIntent?.();
        return;
      }

      const focus = getSelectionFocus(root);
      if (!focus) return;

      if (!deleting) {
        // Render the deletion and its caret synchronously, then let the browser type the key there.
        flushSync(() => onDelete(focus));
        return;
      }

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
  }, [rootEl, onDelete, enabled, onDiffEditIntent]);
}
