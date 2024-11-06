import { inRange, minBy } from "lodash";

type Coordinates = {
  x: number;
  y: number;
};

/**
 * Returns the current offset of the cursor, or -1 if not able to get selection or range.
 *
 */
export function getCursorOffset() {
  //console.log("getCursorOffset");
  const selection = window.getSelection();

  if ((selection?.rangeCount ?? 0) > 0) {
    //console.log("selection:", selection);
    const range = selection?.getRangeAt(0);
    //console.log("range:", range);
    //console.log("range.collapesed:", range?.collapsed, "range.startOffset", range?.endOffset);
    return range?.collapsed ? range.startOffset : -1;
  }

  return -1;
}

/**
 *
 * @returns the current offset of the cursor, or the range if the cursor is not collapsed.
 */
export const getCursorOffsetOrRange = () => {
  const selection = window.getSelection();
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
  //console.log("length:", node.length);
  //console.log("trying to set offset", offset, "for node", node, "with length", node.length);

  if (offset >= 0) {
    const range = document.createRange();
    range.setStart(node, offset);
    range.collapse();
    node.parentElement?.focus();

    const selection = window.getSelection();
    selection?.removeAllRanges();
    selection?.addRange(range);
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

  // This is a non-standard browser function, but it is the only fix I found. It is not implemented in Firefox
  // https://developer.mozilla.org/en-US/docs/Web/API/Document/caretRangeFromPoint
  const range = document.caretRangeFromPoint(x, y);
  if (range === null) {
    // eslint-disable-next-line no-console
    console.warn("Could not get caret for position:", x, y);
    return;
  }
  const selection = window.getSelection();
  selection?.removeAllRanges();
  selection?.addRange(range);
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
  const selection = window.getSelection();
  return selection?.getRangeAt(0);
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
