import { inRange, minBy } from "lodash";

import { isBlockContentIndex, isItemContentIndex } from "~/Brevredigering/LetterEditor/actions/common";
import { type LiteralIndex, type SelectionIndex as SelectionFocus } from "~/Brevredigering/LetterEditor/model/state";
import { isTableCellIndex, ZERO_WIDTH_SPACE } from "~/Brevredigering/LetterEditor/model/utils";
import { restoreEditables, unifyEditables } from "~/hooks/useDragSelectUnifier";

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
    if (range?.collapsed) {
      return range.startContainer.textContent === ZERO_WIDTH_SPACE ? 0 : range.startOffset;
    } else {
      return -1;
    }
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
    if (range?.collapsed) {
      return range.startContainer.textContent === ZERO_WIDTH_SPACE ? 0 : range.startOffset;
    } else {
      return range;
    }
  }
  return undefined;
};

/**
 * Moves DOM focus back to the editable element holding the current selection/caret (e.g. after a
 * toolbar button took focus), without changing the selection.
 */
export function refocusEditableAtSelection() {
  const node = globalThis.getSelection()?.anchorNode;
  const element = (node instanceof HTMLElement ? node : node?.parentElement)?.closest<HTMLElement>(
    "[contenteditable='true']",
  );
  if (element && document.activeElement !== element) {
    element.focus({ preventScroll: true });
  }
}

/**
 * True when the range touches a diff-decorated literal. These also show deleted text, so DOM offsets in them
 * do not match the letter model, and the range must not be mapped to model positions.
 */
export function rangeTouchesDiffDecoration(root: HTMLElement, range: Range): boolean {
  return [...root.querySelectorAll("[data-diff-version]")].some((element) => rangeIntersectsNodeSafe(range, element));
}

/** True when the current caret or selection touches a diff-decorated literal in `root`. */
export function selectionTouchesDiffDecoration(root: HTMLElement | undefined): boolean {
  const selection = globalThis.getSelection();
  if (!root || !selection || selection.rangeCount === 0) return false;
  return rangeTouchesDiffDecoration(root, selection.getRangeAt(0));
}

function findLiteralElement(root: HTMLElement, index: LiteralIndex): HTMLElement | undefined {
  const key = JSON.stringify(literalIndexKey(index));
  for (const el of root.querySelectorAll<HTMLElement>("[data-literal-index]")) {
    const parsed = parseLiteralIndex(el);
    if (parsed && JSON.stringify(literalIndexKey(parsed)) === key) return el;
  }
  return undefined;
}

function literalIndexKey(index: LiteralIndex) {
  if (isTableCellIndex(index)) {
    return [index.blockIndex, index.contentIndex, index.rowIndex, index.cellIndex, index.cellContentIndex];
  }
  if (isItemContentIndex(index)) {
    return [index.blockIndex, index.contentIndex, index.itemIndex, index.itemContentIndex];
  }
  return [index.blockIndex, index.contentIndex];
}

function textPosition(el: HTMLElement, offset: number): { node: Node; offset: number } {
  const node = el.firstChild ?? el;
  const content = node.textContent ?? "";
  const zwspOffset = content.startsWith(ZERO_WIDTH_SPACE) ? 1 : 0;
  return { node, offset: Math.min(offset + zwspOffset, content.length) };
}

/**
 * Selects the given model range in the DOM (a caret when start equals end), and focuses the editable
 * element at its start. The range may span several literals. Returns false if the literals are not rendered.
 */
export function selectSelectionIndex(root: HTMLElement, selection: SelectionFocus): boolean {
  const startEl = findLiteralElement(root, selection.start);
  const endEl = findLiteralElement(root, selection.end);
  if (!startEl || !endEl) return false;

  const start = textPosition(startEl, selection.start.cursorPosition);
  const end = textPosition(endEl, selection.end.cursorPosition);
  if (startEl !== endEl) {
    // The browser only keeps a selection across several editables while they are unified (see useDragSelectUnifier).
    // Restore first, so editables rendered since the last unification are included.
    restoreEditables(root);
    unifyEditables(root);
  }
  try {
    if (document.activeElement !== startEl) startEl.focus({ preventScroll: true });
    globalThis.getSelection()?.setBaseAndExtent(start.node, start.offset, end.node, end.offset);
    return true;
  } catch (error) {
    console.warn("Could not restore selection", selection, error);
    return false;
  }
}

/**
 * Maps the current DOM caret or selection to a model SelectionIndex (start equals end for a caret).
 */
export function getSelectionIndex(root?: HTMLElement): SelectionFocus | undefined {
  const selection = globalThis.getSelection();
  if (!selection || selection.rangeCount === 0) return undefined;

  const range = selection.getRangeAt(0);
  if (!range.collapsed) {
    const selectionFocus = getSelectionFocus(root);
    // Undefined also when the whole selection is deleted text in the attestant diff: then it collapses to its start.
    if (selectionFocus || !closestLiteralEl(range.startContainer)) return selectionFocus;
  }

  const literalEl = closestLiteralEl(range.startContainer);
  const index = literalEl ? parseLiteralIndex(literalEl) : undefined;
  if (!literalEl || !index) return undefined;

  const position = {
    ...index,
    cursorPosition: charOffsetWithinLiteral(literalEl, range.startContainer, range.startOffset),
  };
  return { start: position, end: position };
}

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
  // but will keep caretRangeFromPoint as backup for wider support
  // https://developer.mozilla.org/en-US/docs/Web/API/Document/caretPositionFromPoint
  // https://developer.mozilla.org/en-US/docs/Web/API/Document/caretRangeFromPoint
  if (document.caretPositionFromPoint) {
    const position = document.caretPositionFromPoint(x, y);
    if (position === null) {
      console.warn("Could not get caret for position:", x, y);
      return;
    }
    range = document.createRange();
    range.setStart(position.offsetNode, position.offset);
    range.collapse(true);
  } else if (document.caretRangeFromPoint) {
    range = document.caretRangeFromPoint(x, y);
  }
  if (range === null) {
    console.warn("Could not get caret for position:", x, y);
    return;
  }

  // caretRangeFromPoint may return a range pointing to an Element node (nodeType 1)
  // instead of a Text node (nodeType 3) — observed on Linux Chromium when the target
  // coordinates land at the exact edge of an inline element. In that case, the offset
  // is a child-node index, not a text character offset. Resolve to the actual text node
  // so the selection is placed at a meaningful text position.
  if (range.startContainer.nodeType === Node.ELEMENT_NODE) {
    const container = range.startContainer as Element;
    const childIndex = range.startOffset;

    // childIndex points *before* the child at that index; if it equals childCount,
    // it means "after the last child" → place caret at the end of the last text node.
    if (childIndex >= container.childNodes.length) {
      // After last child → end of last text node
      const lastChild = container.lastChild;
      if (lastChild?.nodeType === Node.TEXT_NODE) {
        range.setStart(lastChild, lastChild.textContent?.length ?? 0);
        range.collapse(true);
      }
    } else {
      const targetChild = container.childNodes[childIndex];
      if (targetChild.nodeType === Node.TEXT_NODE) {
        // Before this text node → start of it
        range.setStart(targetChild, 0);
        range.collapse(true);
      }
    }
  }

  const selection = globalThis.getSelection();
  selection?.removeAllRanges();
  selection?.addRange(range);
}

export function fineAdjustCoordinates({ x, y }: Coordinates) {
  // Get the most specific clicked element.
  const [clickedElement] = document.elementsFromPoint(x, y);

  // Unwrap a possible parent to its contenteditable child
  const editableChildElement = clickedElement.querySelector(":scope > [contenteditable]");

  const targetElement = editableChildElement || clickedElement;

  const targetElementIsContenteditable = targetElement.hasAttribute("contenteditable");

  // If the targetElement is editable we use the existing coordinates.
  if (targetElementIsContenteditable) {
    return { x, y };
  }

  // The clicked element is likely a variable, or another non-editable element.
  // Find its most adjacent sibling that is editable.
  const editableSiblings = targetElement.parentElement
    ? [...targetElement.parentElement.querySelectorAll(":scope [contenteditable]")]
    : [];

  const targetRect = targetElement.getBoundingClientRect();

  // Attempt to find editableSiblings at least partially on the same line:
  // ASSUMPTION: variable blocks are not as tall as editable elements
  // ASSUMPTION: variable blocks do not span multiple lines
  // CAVEAT: getBoundingClientRect on block elements may span multiple lines
  // getClientRects on inline elements returns a list of line-by-line rects
  // https://developer.mozilla.org/en-US/docs/Web/API/Element/getBoundingClientRect
  // https://developer.mozilla.org/en-US/docs/Web/API/Element/getClientRects
  const rectsOnTheSameLine = editableSiblings
    .flatMap((sibling) => Array.from(sibling.getClientRects()))
    .filter((siblingRect) => inRange(siblingRect.top + targetRect.height / 2, targetRect.top, targetRect.bottom));

  // Get the rect that has an edge closes to x
  const closestRect = minBy(rectsOnTheSameLine, (b) => {
    const distanceFromTheLeft = Math.abs(b.left - x);
    const distanceFromTheRight = Math.abs(b.right - x);

    return Math.min(distanceFromTheLeft, distanceFromTheRight);
  });

  // If we found no closestRect we "failed" the caret will likely be lost and the user must click manually
  if (closestRect === undefined) {
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

// The rect of the caret endpoint that leads the way when moving in the given direction.
// getCaretRect() returns the bounding box of the whole range, which spans every selected line
// when the selection is not collapsed. Collapsing a copy of the range onto the endpoint the caret
// moves away from gives a box that is always exactly one line tall.
function getLeadingCaretRect(direction: "up" | "down") {
  const range = getRange();
  if (range === undefined) return undefined;

  const leadingEdge = range.cloneRange();
  leadingEdge.collapse(direction === "up");
  return leadingEdge.getBoundingClientRect();
}

export function getRange() {
  const selection = globalThis.getSelection();
  return (selection?.rangeCount ?? 0) > 0 ? selection?.getRangeAt(0) : undefined;
}

// Finds the nearest parent element configured for vertical scrolling.
// Returns undefined if no such parent exists.
function getVerticalScrollContainer(element: Element): HTMLElement | undefined {
  let parent = element.parentElement;

  while (parent) {
    const { overflowY } = getComputedStyle(parent);
    const isVerticallyScrollable = overflowY === "auto" || overflowY === "scroll";

    if (isVerticallyScrollable) {
      return parent;
    }

    parent = parent.parentElement;
  }

  return undefined;
}

// Scrolls the nearest vertical scroll container just enough to show the outermost line of the
// element on the given edge: first for "top", last for "bottom". Only the line the caret is about
// to enter matters. Revealing the whole element would scroll a tall, multi-line element entirely
// into view. Does nothing if the line is already visible or no scroll container exists.
export function ensureLineVisibleInScrollContainer(element: Element, edge: "top" | "bottom") {
  const scrollContainer = getVerticalScrollContainer(element);
  if (!scrollContainer) return;

  const elementRect = element.getBoundingClientRect();
  const containerRect = scrollContainer.getBoundingClientRect();
  const lineHeight = Math.min(
    Number.parseFloat(getComputedStyle(element).lineHeight) || elementRect.height,
    elementRect.height,
  );

  const lineTop = edge === "top" ? elementRect.top : elementRect.bottom - lineHeight;
  const lineBottom = edge === "top" ? elementRect.top + lineHeight : elementRect.bottom;

  const distanceAbove = containerRect.top - lineTop;
  const distanceBelow = lineBottom - containerRect.bottom;

  if (distanceAbove > 0) {
    scrollContainer.scrollTop -= distanceAbove;
  } else if (distanceBelow > 0) {
    scrollContainer.scrollTop += distanceBelow;
  }
}

// When the browser handles the caret move itself, and is about to move the caret outside the
// visible area, Chrome reveals it with "center if needed" alignment. That recentring looks like a
// half-page jump. By first scrolling into view the line the caret is about to enter, the browser
// has nothing to reveal, so the caret keeps following the edge one line at a time.
export function ensureAdjacentLineVisible(element: Element, direction: "up" | "down") {
  const scrollContainer = getVerticalScrollContainer(element);
  const caretRect = getLeadingCaretRect(direction);
  if (!scrollContainer || caretRect === undefined) return;

  const containerRect = scrollContainer.getBoundingClientRect();
  const lineHeight = caretRect.height || Number.parseFloat(getComputedStyle(element).lineHeight) || 0;

  if (direction === "down") {
    const distanceBelow = caretRect.bottom + lineHeight - containerRect.bottom;
    if (distanceBelow > 0) {
      scrollContainer.scrollTop += distanceBelow;
    }
  } else {
    const distanceAbove = containerRect.top - (caretRect.top - lineHeight);
    if (distanceAbove > 0) {
      scrollContainer.scrollTop -= distanceAbove;
    }
  }
}

export function findOnLineBelow(element: Element) {
  const currentRect = element.getBoundingClientRect();

  const allEditables = [...document.querySelectorAll("[contenteditable]")];
  const currentIndex = allEditables.indexOf(element);
  const next = allEditables[currentIndex + 1];

  if (next === undefined) {
    return undefined;
  }

  if (currentRect.bottom !== next.getBoundingClientRect().bottom) {
    return next;
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

  if (currentRect.bottom !== previous.getBoundingClientRect().bottom) {
    return previous;
  }

  return findOnLineAbove(previous);
}

function closestLiteralEl(n: Node | null): HTMLElement | null {
  const el = (n && (n.nodeType === Node.ELEMENT_NODE ? (n as Element) : n.parentElement)) || null;
  return el ? el.closest<HTMLElement>("[data-literal-index]") : null;
}

const DIFF_DELETION = "[data-diff-deletion]";

/** Text of the letter model in `el`: deleted text shown by the attestant diff is not part of the letter. */
function modelText(el: Node): string {
  if (!(el instanceof Element) || !el.querySelector(DIFF_DELETION)) return el.textContent ?? "";
  const copy = el.cloneNode(true) as Element;
  for (const deletion of copy.querySelectorAll(DIFF_DELETION)) deletion.remove();
  return copy.textContent ?? "";
}

function getTextLengthExcludingZWSP(el: HTMLElement): number {
  const text = modelText(el);
  return text.startsWith(ZERO_WIDTH_SPACE) ? Math.max(0, text.length - 1) : text.length;
}

export function parseLiteralIndex(el: HTMLElement): LiteralIndex | undefined {
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

/**
 * Offset in the letter model of a DOM point in the literal. Deleted text shown by the attestant diff is not
 * counted, so a point inside a deleted segment maps to where that segment is shown: a selection starting there
 * starts at the following text, and one ending there ends at the preceding text.
 */
export function charOffsetWithinLiteral(literalEl: HTMLElement, container: Node, offset: number): number {
  const range = document.createRange();
  range.setStart(literalEl, 0);
  range.setEnd(container, offset);
  const before = document.createElement("div");
  before.append(range.cloneContents());
  let len = modelText(before).length;
  const txt = modelText(literalEl);
  if (txt.startsWith(ZERO_WIDTH_SPACE)) len = Math.max(0, len - 1);
  const max = txt.startsWith(ZERO_WIDTH_SPACE) ? Math.max(0, txt.length - 1) : txt.length;
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

  const startIndex = parseLiteralIndex(startEl);
  const endIndex = parseLiteralIndex(endEl);
  if (!startIndex || !endIndex) return;

  const sIsDirect = startEl === closestLiteralEl(range.startContainer);
  const eIsDirect = endEl === closestLiteralEl(range.endContainer);

  const start = {
    ...startIndex,
    cursorPosition: sIsDirect ? charOffsetWithinLiteral(startEl, range.startContainer, range.startOffset) : 0,
  };
  const end = {
    ...endIndex,
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
