import { type KeyboardEvent, type RefObject } from "react";

import {
  areAnyContentEditableSiblingsPlacedHigher,
  areAnyContentEditableSiblingsPlacedLower,
  ensureAdjacentLineVisible,
  ensureLineVisibleInScrollContainer,
  getCaretRect,
  gotoCoordinates,
} from "~/Brevredigering/LetterEditor/services/caretUtils";

import { caretAtDomSelection, containerSelector, placeCaret, visibleLength } from "../../caret/domCaret";
import { type Vertical, verticalTarget } from "../../caret/navigation";
import { addNewLine } from "../../commands/addNewLine";
import { merge } from "../../commands/merge";
import { split } from "../../commands/split";
import { moveTableRow, removeTable, removeTableRow } from "../../commands/table";
import { tab } from "../../commands/tableTab";
import { moveFocus } from "../../commands/updateText";
import { type Caret, samePath, tableAt } from "../../model/paths";
import { isEmptyContent } from "../../model/text";
import { useEditorV2 } from "../EditorContextV2";

const LINE_MARGIN = 10;

export function isTextMutatingKey(event: KeyboardEvent): boolean {
  return (
    !event.ctrlKey &&
    !event.altKey &&
    !event.metaKey &&
    (event.key.length === 1 || event.key === "Backspace" || event.key === "Delete" || event.key === "Enter")
  );
}

function editableSpans(): HTMLElement[] {
  return [...document.querySelectorAll<HTMLElement>("[data-path][contenteditable]")];
}

function isCollapsed(): boolean {
  return globalThis.getSelection()?.isCollapsed ?? true;
}

export function useLiteralKeyboard(element: RefObject<HTMLSpanElement | null>, at: Omit<Caret, "offset">) {
  const { state, dispatch } = useEditorV2();
  const doc = state.redigertBrev;

  const caret = (): Caret => {
    const dom = caretAtDomSelection();
    return dom && samePath(dom.path, at.path) && dom.content === at.content ? dom : { ...at, offset: 0 };
  };

  const horizontal = (event: KeyboardEvent, direction: "left" | "right") => {
    const self = element.current;
    if (!self || event.shiftKey || !isCollapsed()) return;
    const offset = caret().offset;
    if (direction === "left" ? offset > 0 : offset < visibleLength(self)) return;
    const spans = editableSpans();
    const target = spans[spans.indexOf(self) + (direction === "left" ? -1 : 1)];
    if (!target) return;
    event.preventDefault();
    const sameContainer = target.parentElement === self.parentElement;
    const length = visibleLength(target);
    const step = sameContainer ? 1 : 0;
    placeCaret(target, Math.max(0, direction === "left" ? length - step : step));
  };

  const vertical = (event: KeyboardEvent, direction: Vertical) => {
    const self = element.current;
    const caretRect = getCaretRect();
    if (!self || !caretRect) return;
    const onEdgeLine =
      direction === "up"
        ? !areAnyContentEditableSiblingsPlacedHigher(self)
        : !areAnyContentEditableSiblingsPlacedLower(self);
    if (!onEdgeLine) {
      ensureAdjacentLineVisible(self, direction);
      return;
    }
    const target = verticalTarget(doc, at.path, direction);
    const targetElement = target && document.querySelector<HTMLElement>(containerSelector(target));
    if (!targetElement) return;
    event.preventDefault();
    ensureLineVisibleInScrollContainer(targetElement, direction === "up" ? "bottom" : "top");
    const rect = targetElement.getBoundingClientRect();
    gotoCoordinates({
      x: Math.min(Math.max(caretRect.x, rect.left + 1), rect.right - 1),
      y: direction === "up" ? rect.bottom - LINE_MARGIN : rect.top + LINE_MARGIN,
    });
  };

  const deleteInTable = (event: KeyboardEvent) => {
    if (at.path.kind !== "cell") return;
    const table = tableAt(doc, at.path.block);
    if (!table) return;
    event.preventDefault();
    const onlyEmptyRow = table.rows.length === 1 && table.rows[0].cells.every((cell) => isEmptyContent(cell.content));
    dispatch(onlyEmptyRow ? removeTable : removeTableRow, caret());
  };

  const deleteKey = (event: KeyboardEvent<HTMLSpanElement>) => {
    if (!isCollapsed()) return;
    event.stopPropagation();
    if (event.shiftKey && at.path.kind === "cell") {
      deleteInTable(event);
      return;
    }
    const current = caret();
    const backward = event.key === "Backspace";
    const atBoundary = backward ? current.offset === 0 : current.offset >= visibleLength(event.currentTarget);
    if (!atBoundary) return;
    event.preventDefault();
    dispatch(merge, current, backward ? "backward" : "forward");
  };

  const rememberCaretBeforeEdit = () => {
    if (!isCollapsed()) return;
    const current = caret();
    if (
      !samePath(state.focus.path, current.path) ||
      state.focus.content !== current.content ||
      state.focus.offset !== current.offset
    ) {
      dispatch(moveFocus, current);
    }
  };

  return (event: KeyboardEvent<HTMLSpanElement>) => {
    if (isTextMutatingKey(event)) rememberCaretBeforeEdit();
    const verticalKey = event.key === "ArrowUp" ? "up" : event.key === "ArrowDown" ? "down" : undefined;

    if (verticalKey && event.altKey && event.shiftKey && at.path.kind === "cell") {
      event.preventDefault();
      event.stopPropagation();
      dispatch(moveTableRow, caret(), verticalKey);
    } else if (verticalKey && !event.shiftKey) {
      vertical(event, verticalKey);
    } else if (event.key === "Enter") {
      event.preventDefault();
      event.stopPropagation();
      dispatch(event.shiftKey ? addNewLine : split, caret());
    } else if (event.key === "Backspace" || event.key === "Delete") {
      deleteKey(event);
    } else if (event.key === "Tab" && (at.path.kind === "cell" || at.path.kind === "header")) {
      event.preventDefault();
      dispatch(tab, caret(), event.shiftKey);
    } else if (event.key === "ArrowLeft" || event.key === "ArrowRight") {
      horizontal(event, event.key === "ArrowLeft" ? "left" : "right");
    }
  };
}
