import { inRange, minBy } from "lodash";

import { isBlockContentIndex, isItemContentIndex } from "~/Brevredigering/LetterEditor/actions/common";
import type { LiteralIndex, SelectionIndex as SelectionFocus } from "~/Brevredigering/LetterEditor/model/state";
import { isTableCellIndex } from "~/Brevredigering/LetterEditor/model/utils";

type Coordinates = {
  x: number;
  y: number;
};

/**
 * Returns the current offset of the cursor, or -1 if not able to get selection or range.
 *
 */
export function getCursorOffset() {
  const selection = globalThis.getSelection();
  if ((selection?.rangeCount ?? 0) > 0) {
    const range = selection?.getRangeAt(0);
    return range?.collapsed ? range.startOffset : -1;
  }
  return -1;
}

/**
 *
 * @returns the current offset of the cursor, or the range if the cursor is not collapsed.
 */
export const getCursorOffsetOrRange = () => {
  const selection = globalThis.getSelection();
  if ((selection?.rangeCount ?? 0) > 0) {
    const range = selection?.getRangeAt(0);
    return range?.collapsed ? range.startOffset : range;
  }
  return -1;
};

/**
 * Focus cursor at the given offset in the node.
 *
 * @param node the node to focus cursor to
 * @param offset the offset in the node
 */
export function focusAtOffset(node: ChildNode, offset: number) {
  if (node && offset >= 0) {
    try {
      const range = document.createRange();
      range.setStart(node, offset);
      range.collapse();
      node.parentElement?.focus();

      const selection = globalThis.getSelection();
      selection?.removeAllRanges();
      selection?.addRange(range);
    } catch (error) {
      // eslint-disable-next-line no-console
      console.warn("Could not set cursor position for node", node, error);
    }
  }
}

export function areAnyContentEditableSiblingsPlacedLower(element: HTMLSpanElement) {
  const lastContentEditable = element.parentElement
    ? [...element.parentElement.querySelectorAll(":scope > [contenteditable]")].pop()
    : undefined;
  const caretCoordinates = getCaretRect();

  if (lastContentEditable === undefined || caretCoordinates === undefined) return false;

  return lastContentEditable.getBoundingClientRect().bottom > caretCoordinates.bottom;
}

export function areAnyContentEditableSiblingsPlacedHigher(element: HTMLSpanElement) {
  const firstContentEditable = element.parentElement
    ? [...element.parentElement.querySelectorAll(":scope > [contenteditable]")][0]
    : undefined;
  const caretCoordinates = getCaretRect();

  if (firstContentEditable === undefined || caretCoordinates === undefined) return false;
  return caretCoordinates.top > firstContentEditable.getBoundingClientRect().top;
}

export function gotoCoordinates(coordinates: Coordinates) {
  const { x, y } = fineAdjustCoordinates(coordinates);
  let range: Range | null = null;

  // caretPositionFromPoint() (supported by all except WebKit/Safari per 2025)
  // caretRangeFromPoint() (supported by all except Gecko/Firefox per 2025)
  // We should prioritize the non-deprecated caretPositionFromPoint,
  // but will keep caretRangeFromPoint first until we resolve a component test issue
  // https://developer.mozilla.org/en-US/docs/Web/API/Document/caretPositionFromPoint
  // https://developer.mozilla.org/en-US/docs/Web/API/Document/caretRangeFromPoint
  if (document.caretRangeFromPoint) {
    range = document.caretRangeFromPoint(x, y);
  } else if (document.caretPositionFromPoint) {
    const position = document.caretPositionFromPoint(x, y);
    if (position === null) {
      // eslint-disable-next-line no-console
      console.warn("Could not get caret for position:", x, y);
      return;
    }
    range = document.createRange();
    range.setStart(position.offsetNode, position.offset);
    range.collapse(true);
  }
  if (range === null) {
    // eslint-disable-next-line no-console
    console.warn("Could not get caret for position:", x, y);
    return;
  } else {
    const selection = globalThis.getSelection();
    selection?.removeAllRanges();
    selection?.addRange(range);
  }
}

export function fineAdjustCoordinates({ x, y }: Coordinates) {
  // Get the most specific clicked element.
  const [clickedElement] = document.elementsFromPoint(x, y);

  // The point might have been outside the editable element and the element we have is a parent.
  const editableChildElement = clickedElement.querySelector(":scope > [contenteditable]");

  const seekedElement = editableChildElement || clickedElement;

  const seekedElementIsContenteditable = seekedElement.hasAttribute("contenteditable");

  // If the seekedElement is editable we use the existing coordinates.
  if (seekedElementIsContenteditable) {
    return { x, y };
  }

  // The element we clicked is likely a variable, or another not editable element. Now we attempt to find its most adjacent sibling that is editable.
  const editableSiblings = seekedElement.parentElement
    ? [...seekedElement.parentElement.querySelectorAll(":scope [contenteditable]")]
    : [];

  const currentFocusRect = seekedElement.getBoundingClientRect();

  // ASSUMPTION: variable blocks are taller than editable elements.
  // Attempt to find editableSiblings that are on the same line. The caveat is that a variable block have different top/bottom that normal editable lines.
  // Therefore, we check if the editable element lies within the height of the seekedElement.
  const rectsOnTheSameLine = editableSiblings
    .map((s) => s.getBoundingClientRect())
    .filter((b) => inRange(b.bottom, currentFocusRect.top, currentFocusRect.bottom));

  const closestRect = minBy(rectsOnTheSameLine, (b) => {
    const distanceFromTheLeft = Math.abs(b.left - x);
    const distanceFromTheRight = Math.abs(b.right - x);

    return Math.min(distanceFromTheLeft, distanceFromTheRight);
  });

  // If we found no closestRect we "failed" the caret will likely be lost and the user must click manually
  if (closestRect === undefined) {
    // eslint-disable-next-line no-console
    console.warn("Found no editable element on the same line");
    return { x, y };
  }

  const distanceFromTheLeft = Math.abs(closestRect.left - x);
  const distanceFromTheRight = Math.abs(closestRect.right - x);

  const closestX = distanceFromTheLeft < distanceFromTheRight ? closestRect.left : closestRect.right;

  return { x: closestX, y };
}

export function getCaretRect() {
  return getRange()?.getBoundingClientRect();
}

export function getRange() {
  const selection = globalThis.getSelection();
  return (selection?.rangeCount ?? 0) > 0 ? selection?.getRangeAt(0) : undefined;
}

export function findOnLineBelow(element: Element) {
  const currentRect = element.getBoundingClientRect();

  const allEditables = [...document.querySelectorAll("[contenteditable]")];
  const currentIndex = allEditables.indexOf(element);
  const next = allEditables[currentIndex + 1];

  if (next === undefined) {
    return undefined;
  }

  const nextRect = next.getBoundingClientRect();

  if (currentRect.bottom !== nextRect.bottom) {
    return nextRect;
  }

  return findOnLineBelow(next);
}

export function findOnLineAbove(element: Element) {
  const currentRect = element.getBoundingClientRect();

  const allEditables = [...document.querySelectorAll("[contenteditable]")];
  const currentIndex = allEditables.indexOf(element);
  const previous = allEditables[currentIndex - 1];

  if (previous === undefined) {
    return undefined;
  }

  const previousRect = previous.getBoundingClientRect();

  if (currentRect.bottom !== previousRect.bottom) {
    return previousRect;
  }

  return findOnLineAbove(previous);
}

const ZWSP = "\u200B";

function closestLiteralEl(n: Node | null): HTMLElement | null {
  const el = (n && (n.nodeType === Node.ELEMENT_NODE ? (n as Element) : n.parentElement)) || null;
  return el ? el.closest<HTMLElement>("[data-literal-index]") : null;
}

function getTextLengthExcludingZWSP(el: HTMLElement): number {
  const text = el.textContent ?? "";
  return text.startsWith(ZWSP) ? Math.max(0, text.length - 1) : text.length;
}

function parseLiteralIndex(el: HTMLElement): LiteralIndex | undefined {
  const dataLiteralIndex = el.getAttribute("data-literal-index");
  if (!dataLiteralIndex) return undefined;
  try {
    const parsedLiteralIndex = JSON.parse(dataLiteralIndex);
    return isBlockContentIndex(parsedLiteralIndex) ||
      isItemContentIndex(parsedLiteralIndex) ||
      isTableCellIndex(parsedLiteralIndex)
      ? parsedLiteralIndex
      : undefined;
  } catch {
    return undefined;
  }
}

function charOffsetWithinLiteral(literalEl: HTMLElement, container: Node, offset: number): number {
  const range = document.createRange();
  range.setStart(literalEl, 0);
  range.setEnd(container, offset);
  let len = range.toString().length;
  const txt = literalEl.textContent ?? "";
  if (txt.startsWith(ZWSP)) len = Math.max(0, len - 1);
  const max = txt.startsWith(ZWSP) ? Math.max(0, txt.length - 1) : txt.length;
  return Math.min(Math.max(len, 0), max);
}

function rangeIntersectsNodeSafe(range: Range, node: Node): boolean {
  try {
    return range.intersectsNode(node);
  } catch {
    return false;
  }
}

/* Selection => Model mapping (handles ELEMENT/TEXT nodes, nested tags, ZWSP)
 * Map current DOM selection to model literalIndexes, with root-aware clamping.
 */
export function getSelectionFocus(root?: HTMLElement): SelectionFocus | undefined {
  const selection = globalThis.getSelection();
  if (!selection || selection.rangeCount === 0) return;

  const range = selection.getRangeAt(0);
  if (range.collapsed) return;

  const literalEls = root ? Array.from(root.querySelectorAll<HTMLElement>("[data-literal-index]")) : [];

  let startEl = closestLiteralEl(range.startContainer);
  let endEl = closestLiteralEl(range.endContainer);

  if (!startEl && literalEls.length > 0) {
    for (const el of literalEls) {
      if (rangeIntersectsNodeSafe(range, el)) {
        startEl = el;
        break;
      }
    }
  }
  if (!endEl && literalEls.length > 0) {
    for (let i = literalEls.length - 1; i >= 0; i -= 1) {
      const el = literalEls[i];
      if (rangeIntersectsNodeSafe(range, el)) {
        endEl = el;
        break;
      }
    }
  }
  if (!startEl || !endEl) return;

  const startIdx = parseLiteralIndex(startEl);
  const endIdx = parseLiteralIndex(endEl);
  if (!startIdx || !endIdx) return;

  const sIsDirect = startEl === closestLiteralEl(range.startContainer);
  const eIsDirect = endEl === closestLiteralEl(range.endContainer);

  const start = {
    ...startIdx,
    cursorPosition: sIsDirect ? charOffsetWithinLiteral(startEl, range.startContainer, range.startOffset) : 0,
  };
  const end = {
    ...endIdx,
    cursorPosition: eIsDirect
      ? charOffsetWithinLiteral(endEl, range.endContainer, range.endOffset)
      : getTextLengthExcludingZWSP(endEl),
  };

  if (startEl === endEl && start.cursorPosition === end.cursorPosition) return;
  return { start, end };
}
/**
 * Returns the start offset (in number of characters) of the current caret or selection inside the element.
 * Used to capture/persist cursor position before key handling and after input (for undo/redo).
 */
export const getCharacterOffset = (element: Node): number => {
  const selection = globalThis.getSelection();

  if (selection && selection.rangeCount > 0) {
    const range = selection.getRangeAt(0);

    if (range.startContainer && element.contains(range.startContainer)) {
      const preCaretRange = range.cloneRange();
      preCaretRange.selectNodeContents(element);
      preCaretRange.setEnd(range.startContainer, range.startOffset);
      return preCaretRange.toString().length;
    }
  }
  return 0;
};
