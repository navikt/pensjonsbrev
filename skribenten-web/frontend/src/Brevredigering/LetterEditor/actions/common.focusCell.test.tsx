import { fireEvent, render, screen } from "@testing-library/react";
import { describe, expect, test } from "vitest";

import { focusEmptyCellOnClick } from "~/Brevredigering/LetterEditor/actions/common";
import { renderDiffSegments } from "~/Brevredigering/LetterEditor/diff/DiffSegments";
import { ZERO_WIDTH_SPACE } from "~/Brevredigering/LetterEditor/model/utils";

function renderCell(cellContent: React.ReactNode) {
  render(
    // biome-ignore lint/a11y/useKeyWithClickEvents: Tests the mouse-only cell-whitespace handler.
    <table onClick={focusEmptyCellOnClick}>
      <tbody>
        <tr>
          <td data-testid="cell">{cellContent}</td>
        </tr>
      </tbody>
    </table>,
  );
  const selection = document.getSelection()!;
  selection.collapse(document.body, 0);
  return { cell: screen.getByTestId("cell"), selection };
}

const literal = (text: string) => (
  <span contentEditable data-literal-index="{}" data-testid="literal" suppressContentEditableWarning tabIndex={-1}>
    {text}
  </span>
);

describe("focusEmptyCellOnClick", () => {
  test("focuses the literal of an empty cell", () => {
    const { cell, selection } = renderCell(literal(ZERO_WIDTH_SPACE));
    const span = screen.getByTestId("literal");

    fireEvent.click(cell);

    expect(document.activeElement).toBe(span);
    expect(selection.isCollapsed).toBe(true);
    expect(selection.anchorNode).toBe(span.firstChild);
    expect(selection.anchorOffset).toBe(ZERO_WIDTH_SPACE.length);
  });

  test("focuses the first literal of a cell with several empty literals and a line break", () => {
    const { cell } = renderCell(
      <>
        {literal(ZERO_WIDTH_SPACE)}
        <br />
        {literal(ZERO_WIDTH_SPACE)}
      </>,
    );

    fireEvent.click(cell);

    expect(document.activeElement).toBe(screen.getAllByTestId("literal")[0]);
  });

  test.each([
    ["with text", literal("Tekst")],
    [
      "with a variable",
      <>
        {literal(ZERO_WIDTH_SPACE)}
        <span data-literal-index="{}">variabel</span>
      </>,
    ],
  ])("leaves a cell %s to the browser", (_, cellContent) => {
    const { cell } = renderCell(cellContent);

    fireEvent.click(cell);

    expect(document.activeElement).toBe(document.body);
  });

  test("does nothing for a cell showing deleted diff text", () => {
    const { cell } = renderCell(literal(ZERO_WIDTH_SPACE));
    renderDiffSegments(screen.getByTestId("literal"), [{ type: "deleted", text: "Slettet" }], "test");

    expect(() => fireEvent.click(cell)).not.toThrow();
    expect(document.activeElement).toBe(document.body);
  });

  test("ignores clicks on the literal itself and keeps selections", () => {
    const { selection } = renderCell(literal(ZERO_WIDTH_SPACE));
    const span = screen.getByTestId("literal");

    fireEvent.click(span);
    expect(document.activeElement).toBe(document.body);

    selection.selectAllChildren(document.body);
    fireEvent.click(screen.getByTestId("cell"));
    expect(document.activeElement).toBe(document.body);
  });
});
