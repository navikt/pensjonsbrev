import { afterEach, describe, expect, test } from "vitest";

import { ensureAdjacentLineVisible } from "~/Brevredigering/LetterEditor/services/caretUtils";

const CONTAINER_HEIGHT = 410;
const LINE_HEIGHT = 24;
const SELECTION_HEIGHT = 264;

function rect(top: number, height: number) {
  return { top, bottom: top + height, height, left: 0, right: 0, width: 0, x: 0, y: top } as DOMRect;
}

const originalRangeRect = Range.prototype.getBoundingClientRect;

/**
 * jsdom does no layout, so every rect the helper reads has to be supplied here. The caret sits on
 * the last visible line, and the selection reaches from well above it down to that same line.
 */
function renderSelection({ collapsed }: { collapsed: boolean }) {
  const container = document.createElement("div");
  container.style.overflowY = "auto";
  container.getBoundingClientRect = () => rect(0, CONTAINER_HEIGHT);

  const editable = document.createElement("span");
  editable.contentEditable = "true";
  editable.append(document.createTextNode("En tekst som går over flere linjer"));
  container.append(editable);
  document.body.append(container);

  const textNode = editable.firstChild as Text;
  const range = document.createRange();
  range.setStart(textNode, collapsed ? 10 : 0);
  range.setEnd(textNode, 10);

  const caretRect = rect(CONTAINER_HEIGHT - LINE_HEIGHT, LINE_HEIGHT);
  Range.prototype.getBoundingClientRect = function (this: Range) {
    return this.collapsed ? caretRect : rect(CONTAINER_HEIGHT - SELECTION_HEIGHT, SELECTION_HEIGHT);
  };

  const selection = globalThis.getSelection();
  selection?.removeAllRanges();
  selection?.addRange(range);

  return { container, editable };
}

afterEach(() => {
  Range.prototype.getBoundingClientRect = originalRangeRect;
  document.body.replaceChildren();
});

describe("ensureAdjacentLineVisible", () => {
  test("scrolls one line ahead of a collapsed caret", () => {
    const { container, editable } = renderSelection({ collapsed: true });

    ensureAdjacentLineVisible(editable, "down");

    expect(container.scrollTop).toBe(LINE_HEIGHT);
  });

  // A selection stays uncollapsed when the user presses an arrow key without holding Shift.
  // Measuring the whole selection instead of the caret line would scroll most of a page.
  test("scrolls one line ahead of an uncollapsed selection, not its full height", () => {
    const { container, editable } = renderSelection({ collapsed: false });

    ensureAdjacentLineVisible(editable, "down");

    expect(container.scrollTop).toBe(LINE_HEIGHT);
    expect(container.scrollTop).toBeLessThan(SELECTION_HEIGHT);
  });
});
