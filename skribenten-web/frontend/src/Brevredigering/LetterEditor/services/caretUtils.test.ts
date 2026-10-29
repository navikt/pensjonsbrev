import { afterEach, describe, expect, test } from "vitest";

import { ensureAdjacentLineVisible, getSelectionIndex } from "~/Brevredigering/LetterEditor/services/caretUtils";

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

describe("getSelectionIndex med attestant-diff", () => {
  // Modellteksten er "Ny tekst her" (12 tegn); "gammel " (7 tegn) er slettet tekst som bare vises.
  function renderDecoratedLiteral() {
    const root = document.createElement("div");
    const literal = document.createElement("span");
    literal.contentEditable = "true";
    literal.dataset.literalIndex = JSON.stringify({ blockIndex: 0, contentIndex: 0 });
    literal.dataset.diffVersion = "1";
    const segment = (text: string, type?: "insertion" | "deletion") => {
      const span = document.createElement("span");
      span.textContent = text;
      if (type === "insertion") span.dataset.diffInsertion = "";
      if (type === "deletion") {
        span.dataset.diffDeletion = "";
        span.contentEditable = "false";
      }
      literal.append(span);
      return span.firstChild as Text;
    };
    const nodes = {
      ny: segment("Ny ", "insertion"),
      gammel: segment("gammel ", "deletion"),
      rest: segment("tekst her"),
    };
    root.append(literal);
    document.body.append(root);
    return { root, ...nodes };
  }

  function select(start: [Node, number], end: [Node, number] = start) {
    globalThis.getSelection()?.setBaseAndExtent(start[0], start[1], end[0], end[1]);
  }

  const at = (cursorPosition: number) => ({ blockIndex: 0, contentIndex: 0, cursorPosition });

  afterEach(() => {
    globalThis.getSelection()?.removeAllRanges();
    document.body.innerHTML = "";
  });

  test("slettet tekst inne i markeringen telles ikke med", () => {
    const { root, ny, rest } = renderDecoratedLiteral();
    select([ny, 0], [rest, 5]);
    expect(getSelectionIndex(root)).toEqual({ start: at(0), end: at(8) });
  });

  test("start i slettet tekst flyttes til teksten etter", () => {
    const { root, gammel, rest } = renderDecoratedLiteral();
    select([gammel, 2], [rest, 5]);
    expect(getSelectionIndex(root)).toEqual({ start: at(3), end: at(8) });
  });

  test("slutt i slettet tekst flyttes til teksten før", () => {
    const { root, ny, gammel } = renderDecoratedLiteral();
    select([ny, 1], [gammel, 4]);
    expect(getSelectionIndex(root)).toEqual({ start: at(1), end: at(3) });
  });

  test("markering bare i slettet tekst kollapser der den slettede teksten vises", () => {
    const { root, gammel } = renderDecoratedLiteral();
    select([gammel, 1], [gammel, 5]);
    expect(getSelectionIndex(root)).toEqual({ start: at(3), end: at(3) });
  });

  test("markør etter slettet tekst", () => {
    const { root, rest } = renderDecoratedLiteral();
    select([rest, 2]);
    expect(getSelectionIndex(root)).toEqual({ start: at(5), end: at(5) });
  });
});
