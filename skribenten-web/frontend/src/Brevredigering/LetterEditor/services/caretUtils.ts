import { inRange, minBy } from "lodash";

import { isBlockContentIndex, isItemContentIndex } from "~/Brevredigering/LetterEditor/actions/common";
import {
  type Focus,
  type LiteralIndex,
  type SelectionIndex as SelectionFocus,
} from "~/Brevredigering/LetterEditor/model/state";
import { isTableCellIndex, ZERO_WIDTH_SPACE } from "~/Brevredigering/LetterEditor/model/utils";

type Coordinates = {
  x: number;
  y: number;
};

const EDITABLE_LITERAL_SELECTOR = '[data-literal],[data-literal-index][contenteditable="true"]';

export function isEditableLiteralEl(el: Element | null | undefined): el is HTMLElement {
  return !!el && el.matches(EDITABLE_LITERAL_SELECTOR);
}

export function closestEditableLiteralEl(node: Node | null | undefined): HTMLElement | null {
  const el = node ? (node.nodeType === Node.ELEMENT_NODE ? (node as Element) : node.parentElement) : null;
  return el ? el.closest<HTMLElement>(EDITABLE_LITERAL_SELECTOR) : null;
}

export function getCleanTextLength(literalEl: HTMLElement): number {
  const raw = literalEl.textContent ?? "";
  if (raw === ZERO_WIDTH_SPACE || literalEl.hasAttribute("data-empty")) {
    return raw.replaceAll(ZERO_WIDTH_SPACE, "").length;
  }
  return raw.length;
}

export function getCleanOffsetInLiteral(literalEl: HTMLElement, container: Node, offset: number): number {
  if (!literalEl.contains(container)) return 0;
  const range = document.createRange();
  range.selectNodeContents(literalEl);
  range.setEnd(container, offset);
  const slice = range.toString();
  const isPlaceholderEmpty = literalEl.textContent === ZERO_WIDTH_SPACE || literalEl.hasAttribute("data-empty");
  const raw = isPlaceholderEmpty ? slice.replaceAll(ZERO_WIDTH_SPACE, "").length : slice.length;
  return Math.min(Math.max(raw, 0), getCleanTextLength(literalEl));
}

/**
 * Returns the current offset of the cursor, or -1 if not able to get selection or range.
 *
 */
export function getCursorOffset() {
  const selection = globalThis.getSelection();
  if ((selection?.rangeCount ?? 0) > 0) {
    const range = selection?.getRangeAt(0);
    if (range?.collapsed) {
      const literalEl = closestEditableLiteralEl(range.startContainer);
      if (literalEl) {
        return getCleanOffsetInLiteral(literalEl, range.startContainer, range.startOffset);
      }
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
      const literalEl = closestEditableLiteralEl(range.startContainer);
      if (literalEl) {
        return getCleanOffsetInLiteral(literalEl, range.startContainer, range.startOffset);
      }
      return range.startContainer.textContent === ZERO_WIDTH_SPACE ? 0 : range.startOffset;
    } else {
      return range;
    }
  }
  return undefined;
};

let programmaticFocusDepth = 0;

export function isProgrammaticFocus(): boolean {
  return programmaticFocusDepth > 0;
}

export function focusEditingHost(target: HTMLElement | null | undefined) {
  if (!target || document.activeElement === target) return;
  if (document.activeElement?.closest('[role="menu"], [aria-haspopup="menu"], [role="dialog"]')) return;
  programmaticFocusDepth += 1;
  try {
    target.focus({ preventScroll: true });
  } finally {
    programmaticFocusDepth -= 1;
  }
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
      const rawLength = node.textContent?.length ?? 0;
      const clampedOffset = node.textContent === ZERO_WIDTH_SPACE && offset === 0 ? 0 : Math.min(offset, rawLength);
      const range = document.createRange();
      range.setStart(node, clampedOffset);
      range.collapse();
      const host = node.parentElement?.closest<HTMLElement>('[contenteditable="true"]') ?? node.parentElement;
      focusEditingHost(host);

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
    ? [...element.parentElement.querySelectorAll<HTMLElement>(`:scope > ${EDITABLE_LITERAL_SELECTOR}`)].pop()
    : undefined;
  const caretCoordinates = getCaretRect();

  if (lastContentEditable === undefined || caretCoordinates === undefined) return false;

  return lastContentEditable.getBoundingClientRect().bottom > caretCoordinates.bottom;
}

export function areAnyContentEditableSiblingsPlacedHigher(element: HTMLSpanElement) {
  const firstContentEditable = element.parentElement
    ? [...element.parentElement.querySelectorAll<HTMLElement>(`:scope > ${EDITABLE_LITERAL_SELECTOR}`)][0]
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

    if (isEditableLiteralEl(container)) {
      if (childIndex >= container.childNodes.length) {
        const lastChild = container.lastChild;
        if (lastChild?.nodeType === Node.TEXT_NODE) {
          range.setStart(lastChild, lastChild.textContent?.length ?? 0);
          range.collapse(true);
        }
      } else {
        const targetChild = container.childNodes[childIndex];
        if (targetChild?.nodeType === Node.TEXT_NODE) {
          range.setStart(targetChild, 0);
          range.collapse(true);
        }
      }
    } else {
      const resolved = resolveElementBoundaryToLiteral(container, childIndex);
      if (resolved?.literalEl.firstChild?.nodeType === Node.TEXT_NODE) {
        const textNode = resolved.literalEl.firstChild;
        const rawOffset =
          textNode.textContent === ZERO_WIDTH_SPACE && resolved.cursorPosition === 0
            ? 0
            : Math.min(resolved.cursorPosition, textNode.textContent?.length ?? 0);
        range.setStart(textNode, rawOffset);
        range.collapse(true);
      }
    }
  }

  const host =
    (range.startContainer.nodeType === Node.ELEMENT_NODE
      ? (range.startContainer as Element)
      : range.startContainer.parentElement
    )?.closest<HTMLElement>('[contenteditable="true"]') ?? null;
  focusEditingHost(host);

  const selection = globalThis.getSelection();
  selection?.removeAllRanges();
  selection?.addRange(range);
}

export function fineAdjustCoordinates({ x, y }: Coordinates) {
  // Get the most specific clicked element.
  const [clickedElement] = document.elementsFromPoint(x, y);
  if (!clickedElement) return { x, y };

  // Unwrap a possible parent to its editable literal child
  const editableChildElement = clickedElement.querySelector<HTMLElement>(`:scope > ${EDITABLE_LITERAL_SELECTOR}`);

  const targetElement = editableChildElement || clickedElement;

  const targetElementIsContenteditable =
    isEditableLiteralEl(targetElement) || !!targetElement.closest(EDITABLE_LITERAL_SELECTOR);

  // If the targetElement is editable we use the existing coordinates.
  if (targetElementIsContenteditable) {
    return { x, y };
  }

  // The clicked element is likely a variable, or another non-editable element.
  // Find its most adjacent sibling that is editable.
  const editableSiblings = targetElement.parentElement
    ? [...targetElement.parentElement.querySelectorAll<HTMLElement>(`:scope ${EDITABLE_LITERAL_SELECTOR}`)]
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

  const allEditables = [...document.querySelectorAll(EDITABLE_LITERAL_SELECTOR)];
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

  const allEditables = [...document.querySelectorAll(EDITABLE_LITERAL_SELECTOR)];
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

function firstLiteralIn(node: Node | undefined | null): HTMLElement | null {
  if (!node || node.nodeType !== Node.ELEMENT_NODE) return null;
  const el = node as Element;
  if (isEditableLiteralEl(el)) return el;
  return el.querySelector<HTMLElement>(EDITABLE_LITERAL_SELECTOR);
}

function lastLiteralIn(node: Node | undefined | null): HTMLElement | null {
  if (!node || node.nodeType !== Node.ELEMENT_NODE) return null;
  const el = node as Element;
  if (isEditableLiteralEl(el)) return el;
  const all = el.querySelectorAll<HTMLElement>(EDITABLE_LITERAL_SELECTOR);
  return all.length > 0 ? all[all.length - 1] : null;
}

const INLINE_FLOW_CONTAINER_SELECTOR = "td, th, li, .PARAGRAPH, .TITLE1, .TITLE2, .TITLE3, .letter-title";

export function resolveElementBoundaryToLiteral(
  container: Element,
  childIndex: number,
  root?: HTMLElement,
): { literalEl: HTMLElement; cursorPosition: number } | undefined {
  if (isEditableLiteralEl(container)) {
    const cursorPosition = childIndex <= 0 ? 0 : getCleanTextLength(container);
    return { literalEl: container, cursorPosition };
  }

  const isInlineFlow = container.matches(INLINE_FLOW_CONTAINER_SELECTOR);

  if (isInlineFlow) {
    if (childIndex > 0) {
      const prevNode = container.childNodes[childIndex - 1];
      if (prevNode?.nodeType === Node.ELEMENT_NODE && isEditableLiteralEl(prevNode as Element)) {
        const prevEl = prevNode as HTMLElement;
        return { literalEl: prevEl, cursorPosition: getCleanTextLength(prevEl) };
      }
    }
    for (let i = childIndex; i < container.childNodes.length; i++) {
      const found = firstLiteralIn(container.childNodes[i]);
      if (found) return { literalEl: found, cursorPosition: 0 };
    }
    for (let i = Math.min(childIndex - 1, container.childNodes.length - 1); i >= 0; i--) {
      const found = lastLiteralIn(container.childNodes[i]);
      if (found) return { literalEl: found, cursorPosition: getCleanTextLength(found) };
    }
  } else {
    for (let i = childIndex; i < container.childNodes.length; i++) {
      const found = firstLiteralIn(container.childNodes[i]);
      if (found) return { literalEl: found, cursorPosition: 0 };
    }
    for (let i = Math.min(childIndex - 1, container.childNodes.length - 1); i >= 0; i--) {
      const found = lastLiteralIn(container.childNodes[i]);
      if (found) return { literalEl: found, cursorPosition: getCleanTextLength(found) };
    }
  }

  if (root && root !== container && root.contains(container)) {
    const parent = container.parentElement;
    if (parent) {
      const idx = Array.from(parent.childNodes).indexOf(container as ChildNode);
      return resolveElementBoundaryToLiteral(parent, idx + (childIndex > 0 ? 1 : 0), root);
    }
  }

  return undefined;
}

function isEffectivelyCollapsedRangeInRoot(range: Range, root: HTMLElement): boolean {
  if (!root.contains(range.startContainer)) return false;
  if (range.collapsed) return true;
  const startLit = closestEditableLiteralEl(range.startContainer);
  const endLit = closestEditableLiteralEl(range.endContainer);
  if (
    startLit &&
    (startLit === endLit || startLit.hasAttribute("data-empty")) &&
    range.toString().replaceAll(ZERO_WIDTH_SPACE, "") === ""
  ) {
    return true;
  }
  return false;
}

export function resolveCollapsedRangeInRoot(
  range: Range,
  root: HTMLElement,
): { literalEl: HTMLElement; literalIndex: LiteralIndex; cursorPosition: number } | undefined {
  if (!isEffectivelyCollapsedRangeInRoot(range, root)) return undefined;

  const directLiteral = closestEditableLiteralEl(range.startContainer);
  if (directLiteral && root.contains(directLiteral)) {
    const literalIndex = parseLiteralIndex(directLiteral);
    if (!literalIndex) return undefined;
    return {
      literalEl: directLiteral,
      literalIndex,
      cursorPosition: getCleanOffsetInLiteral(directLiteral, range.startContainer, range.startOffset),
    };
  }

  const containerEl =
    range.startContainer.nodeType === Node.ELEMENT_NODE
      ? (range.startContainer as Element)
      : range.startContainer.parentElement;
  if (!containerEl) return undefined;

  const offsetInEl =
    range.startContainer.nodeType === Node.ELEMENT_NODE
      ? range.startOffset
      : Array.from(containerEl.childNodes).indexOf(range.startContainer as ChildNode);

  const resolved = resolveElementBoundaryToLiteral(containerEl, Math.max(0, offsetInEl), root);
  if (!resolved) return undefined;
  const literalIndex = parseLiteralIndex(resolved.literalEl);
  if (!literalIndex) return undefined;
  return { literalEl: resolved.literalEl, literalIndex, cursorPosition: resolved.cursorPosition };
}

export function resolveCaretInRoot(
  root: HTMLElement,
): { literalEl: HTMLElement; literalIndex: LiteralIndex; cursorPosition: number } | undefined {
  const selection = globalThis.getSelection();
  if (!selection || selection.rangeCount === 0) return undefined;
  return resolveCollapsedRangeInRoot(selection.getRangeAt(0), root);
}

function closestLiteralEl(n: Node | null): HTMLElement | null {
  const el = (n && (n.nodeType === Node.ELEMENT_NODE ? (n as Element) : n.parentElement)) || null;
  return el ? el.closest<HTMLElement>("[data-literal-index]") : null;
}

function getTextLengthExcludingZWSP(el: HTMLElement): number {
  const text = el.textContent ?? "";
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

export function serializeLiteralIndex(index: LiteralIndex): string {
  if (isTableCellIndex(index)) {
    return JSON.stringify({
      blockIndex: index.blockIndex,
      contentIndex: index.contentIndex,
      rowIndex: index.rowIndex,
      cellIndex: index.cellIndex,
      cellContentIndex: index.cellContentIndex,
    });
  }
  if (isItemContentIndex(index)) {
    return JSON.stringify({
      blockIndex: index.blockIndex,
      contentIndex: index.contentIndex,
      itemIndex: index.itemIndex,
      itemContentIndex: index.itemContentIndex,
    });
  }
  return JSON.stringify({
    blockIndex: index.blockIndex,
    contentIndex: index.contentIndex,
  });
}

export function findIndexElementInRoot(root: ParentNode, index: LiteralIndex): HTMLElement | null {
  const serialized = serializeLiteralIndex(index);
  for (const el of root.querySelectorAll<HTMLElement>("[data-literal-index]")) {
    if (el.getAttribute("data-literal-index") === serialized) return el;
  }
  return null;
}

export function charOffsetWithinLiteral(literalEl: HTMLElement, container: Node, offset: number): number {
  const range = document.createRange();
  range.setStart(literalEl, 0);
  range.setEnd(container, offset);
  let len = range.toString().length;
  const txt = literalEl.textContent ?? "";
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

export function getSelectionFocusFromRange(range: Range, root?: HTMLElement): SelectionFocus | undefined {
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

/* Selection => Model mapping (handles ELEMENT/TEXT nodes, nested tags, ZWSP)
 * Map current DOM selection to model literalIndexes, with root-aware clamping.
 */
export function getSelectionFocus(root?: HTMLElement): SelectionFocus | undefined {
  const selection = globalThis.getSelection();
  if (!selection || selection.rangeCount === 0) return;
  return getSelectionFocusFromRange(selection.getRangeAt(0), root);
}

export function rangeToModelSelection(
  root: HTMLElement,
  range: Range,
): { focus: Focus & { cursorPosition: number }; selection?: SelectionFocus } | undefined {
  if (!root.contains(range.startContainer) || !root.contains(range.endContainer)) return undefined;

  if (isEffectivelyCollapsedRangeInRoot(range, root)) {
    const resolved = resolveCollapsedRangeInRoot(range, root);
    if (!resolved) return undefined;
    return {
      focus: { ...resolved.literalIndex, cursorPosition: resolved.cursorPosition },
      selection: undefined,
    };
  }

  const selFocus = getSelectionFocusFromRange(range, root);
  if (selFocus) {
    return {
      focus: { ...selFocus.start },
      selection: selFocus,
    };
  }

  return undefined;
}

export function resolveDomSelectionToModel(
  root: HTMLElement,
): { focus: Focus & { cursorPosition: number }; selection?: SelectionFocus } | undefined {
  const selection = globalThis.getSelection();
  if (!selection || selection.rangeCount === 0) return undefined;
  return rangeToModelSelection(root, selection.getRangeAt(0));
}

export function staticRangeToModelSelection(
  root: HTMLElement,
  staticRange: StaticRange,
): { focus: Focus & { cursorPosition: number }; selection?: SelectionFocus } | undefined {
  try {
    const liveRange = document.createRange();
    liveRange.setStart(staticRange.startContainer, staticRange.startOffset);
    liveRange.setEnd(staticRange.endContainer, staticRange.endOffset);
    return rangeToModelSelection(root, liveRange);
  } catch {
    return undefined;
  }
}

export type DomSelectionSnapshot = {
  rangeCount: number;
  anchorNode: Node | null;
  anchorOffset: number;
  focusNode: Node | null;
  focusOffset: number;
};

export function snapshotDomSelection(selection: Selection | null | undefined): DomSelectionSnapshot {
  if (!selection || selection.rangeCount === 0) {
    return { rangeCount: 0, anchorNode: null, anchorOffset: 0, focusNode: null, focusOffset: 0 };
  }
  return {
    rangeCount: selection.rangeCount,
    anchorNode: selection.anchorNode,
    anchorOffset: selection.anchorOffset,
    focusNode: selection.focusNode,
    focusOffset: selection.focusOffset,
  };
}

export function matchesDomSelectionSnapshot(
  selection: Selection | null | undefined,
  snapshot: DomSelectionSnapshot | null,
): boolean {
  if (!snapshot) return false;
  const current = snapshotDomSelection(selection);
  return (
    current.rangeCount === snapshot.rangeCount &&
    current.anchorNode === snapshot.anchorNode &&
    current.anchorOffset === snapshot.anchorOffset &&
    current.focusNode === snapshot.focusNode &&
    current.focusOffset === snapshot.focusOffset
  );
}

function resolveDomEndpointForElement(
  el: Element,
  cursorPosition: number,
  side: "start" | "end",
): { node: Node; offset: number } | null {
  if (isEditableLiteralEl(el)) {
    const textNode = el.firstChild;
    if (textNode?.nodeType !== Node.TEXT_NODE) return null;
    const domOffset =
      textNode.textContent === ZERO_WIDTH_SPACE && cursorPosition === 0
        ? 0
        : Math.min(Math.max(0, cursorPosition), textNode.textContent?.length ?? 0);
    return { node: textNode, offset: domOffset };
  }
  const parent = el.parentNode;
  if (!parent) return null;
  const childIdx = Array.from(parent.childNodes).indexOf(el as ChildNode);
  if (childIdx < 0) return null;
  return {
    node: parent,
    offset: side === "end" && cursorPosition > 0 ? childIdx + 1 : childIdx,
  };
}

export function applyModelSelectionToDom(
  root: HTMLElement,
  focus: Focus,
  selection: SelectionFocus | undefined,
  shouldFocusHost: boolean,
): DomSelectionSnapshot {
  const domSelection = globalThis.getSelection();
  if (!domSelection) return snapshotDomSelection(null);

  if (selection) {
    const startEl = findIndexElementInRoot(root, selection.start);
    const endEl = findIndexElementInRoot(root, selection.end);
    if (!startEl || !endEl || startEl.dataset.diffVersion !== undefined || endEl.dataset.diffVersion !== undefined) {
      return snapshotDomSelection(domSelection);
    }

    const startPoint = resolveDomEndpointForElement(startEl, selection.start.cursorPosition, "start");
    const endPoint = resolveDomEndpointForElement(endEl, selection.end.cursorPosition, "end");
    if (!startPoint || !endPoint) return snapshotDomSelection(domSelection);

    if (shouldFocusHost) {
      focusEditingHost(root);
    }

    if (domSelection.rangeCount > 0) {
      const existing = domSelection.getRangeAt(0);
      if (
        existing.startContainer === startPoint.node &&
        existing.startOffset === startPoint.offset &&
        existing.endContainer === endPoint.node &&
        existing.endOffset === endPoint.offset
      ) {
        return snapshotDomSelection(domSelection);
      }
    }

    try {
      const range = document.createRange();
      range.setStart(startPoint.node, startPoint.offset);
      range.setEnd(endPoint.node, endPoint.offset);
      domSelection.removeAllRanges();
      domSelection.addRange(range);
    } catch (error) {
      console.warn("Could not set DOM range selection from model", error);
    }
    return snapshotDomSelection(domSelection);
  }

  const literalEl = findIndexElementInRoot(root, focus);
  if (!literalEl || !isEditableLiteralEl(literalEl) || literalEl.dataset.diffVersion !== undefined) {
    return snapshotDomSelection(domSelection);
  }

  const logicalLen = getCleanTextLength(literalEl);
  const targetPos =
    focus.cursorPosition === undefined || focus.cursorPosition < 0
      ? logicalLen
      : Math.min(focus.cursorPosition, logicalLen);
  const endpoint = resolveDomEndpointForElement(literalEl, targetPos, "start");
  if (!endpoint) return snapshotDomSelection(domSelection);

  if (shouldFocusHost) {
    focusEditingHost(root);
  }

  const alreadyAtPoint =
    domSelection.rangeCount > 0 &&
    domSelection.isCollapsed &&
    domSelection.anchorNode === endpoint.node &&
    domSelection.anchorOffset === endpoint.offset;

  if (!alreadyAtPoint) {
    try {
      const range = document.createRange();
      range.setStart(endpoint.node, endpoint.offset);
      range.collapse(true);
      domSelection.removeAllRanges();
      domSelection.addRange(range);
    } catch (error) {
      console.warn("Could not set DOM caret from model", error);
    }
  }

  return snapshotDomSelection(domSelection);
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
      return preCaretRange.toString().replaceAll(ZERO_WIDTH_SPACE, "").length;
    }
  }
  return 0;
};
