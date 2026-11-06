import { fireEvent, render, screen } from "@testing-library/react";
import { expect, test } from "vitest";

import { focusCellWhenClickedOutsideText } from "~/Brevredigering/LetterEditor/actions/common";
import { renderDiffSegments } from "~/Brevredigering/LetterEditor/diff/DiffSegments";
import { type DiffSegment } from "~/Brevredigering/LetterEditor/diff/diffModel";

const diffSegments: Record<string, DiffSegment[]> = {
  diff: [
    { type: "inserted", text: "Inserted text" },
    { type: "deleted", text: "Deleted text" },
    { type: "unchanged", text: "Trailing text" },
  ],
  "diff ending in a deletion": [
    { type: "unchanged", text: "Kept text" },
    { type: "deleted", text: "Deleted text" },
  ],
};

test.each(["plain", "empty", "diff", "diff ending in a deletion"] as const)(
  "focuses the end of the last editable text in a %s literal on cell-whitespace clicks",
  (mode) => {
    render(
      // biome-ignore lint/a11y/useKeyWithClickEvents: Tests the mouse-only cell-whitespace handler.
      <table onClick={focusCellWhenClickedOutsideText}>
        <tbody>
          <tr>
            <td data-testid="cell">
              <span contentEditable data-literal-index="{}" data-testid="literal" tabIndex={-1} />
            </td>
          </tr>
        </tbody>
      </table>,
    );
    const cell = screen.getByTestId("cell");
    const literal = screen.getByTestId("literal");
    const segments = diffSegments[mode];
    if (segments) {
      renderDiffSegments(literal, segments, "test");
    } else {
      literal.textContent = mode === "empty" ? "\u200B" : "Plain text";
    }
    // Offsets are read from the text node, so the caret must not be anchored on the literal element itself.
    const expectedTextNode = [...literal.querySelectorAll(":scope > span:not([data-diff-deletion])")]
      .map((segment) => segment.firstChild)
      .concat(segments ? [] : [literal.firstChild])
      .at(-1);
    const selection = document.getSelection()!;
    selection.collapse(cell, 0);

    fireEvent.click(cell);

    expect(document.activeElement).toBe(literal);
    expect(selection.isCollapsed).toBe(true);
    expect(selection.anchorNode).toBe(expectedTextNode);
    expect(selection.anchorOffset).toBe(expectedTextNode?.textContent?.length);
  },
);
