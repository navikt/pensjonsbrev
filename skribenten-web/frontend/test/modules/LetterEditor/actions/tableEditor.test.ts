import type React from "react";
import { describe, expect, it, vi } from "vitest";

import { isItemContentIndex } from "~/Brevredigering/LetterEditor/actions/common";
import { switchFontType } from "~/Brevredigering/LetterEditor/actions/switchFontType";
import type { Focus, LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { addRow, nextTableFocus } from "~/Brevredigering/LetterEditor/services/tableCaretUtils";
import { FontType } from "~/types/brevbakerTypes";

import { cell, letter, literal, paragraph, row, table } from "../utils";

function createEditorState(tableContent: ReturnType<typeof table>, initialFocus: Focus): LetterEditorState {
  const editorState = letter(paragraph({ content: [tableContent] }));
  editorState.focus = initialFocus;
  return editorState;
}
function isFocusValid(focusResult: ReturnType<typeof nextTableFocus>): focusResult is Focus {
  return typeof focusResult !== "string";
}

const simpleTable = table(
  [cell(literal({ text: "" })), cell(literal({ text: "" }))],
  [row(cell(literal({ text: "r1c1" })), cell(literal({ text: "r1c2" })))],
);

describe("table caret navigation (nextTableFocus)", () => {
  it("Tab moves right and exits at the end", () => {
    const startFocus: Focus = {
      blockIndex: 0,
      contentIndex: 0,
      itemIndex: 0,
      itemContentIndex: 0,
      cursorPosition: 0,
    };
    const initialState = createEditorState(simpleTable, startFocus);

    const nextFocus = nextTableFocus(initialState, "forward");

    if (isFocusValid(nextFocus) && isItemContentIndex(nextFocus)) {
      expect(nextFocus.itemContentIndex).toBe(1);
    } else {
      throw new Error("Unexpected EXIT while moving forward");
    }
    const updatedState = { ...initialState, focus: nextFocus as Focus };
    const exitSignal = nextTableFocus(updatedState, "forward");

    expect(exitSignal).toBe("EXIT_FORWARD");
  });

  it("Shift+Tab moves left and exits backwards", () => {
    const startFocus: Focus = {
      blockIndex: 0,
      contentIndex: 0,
      itemIndex: 0,
      itemContentIndex: 1,
      cursorPosition: 0,
    };
    const initialState = createEditorState(simpleTable, startFocus);

    const previousFocus = nextTableFocus(initialState, "backward");
    if (isFocusValid(previousFocus) && isItemContentIndex(previousFocus)) {
      expect(previousFocus.itemContentIndex).toBe(0);
    } else {
      throw new Error("Unexpected EXIT while moving backward");
    }

    const updatedState = { ...initialState, focus: previousFocus as Focus };
    const exit = nextTableFocus(updatedState, "backward");

    expect(exit).toBe("EXIT_BACKWARD");
  });
});

describe("addRow utility", () => {
  it("adds a blank row when Enter is pressed in the last row", () => {
    const startFocus: Focus = {
      blockIndex: 0,
      contentIndex: 0,
      itemIndex: 0,
      itemContentIndex: 0,
      cursorPosition: 0,
    };

    let editorState = createEditorState(simpleTable, startFocus);

    const keyboardEvent = { preventDefault: vi.fn(), shiftKey: false } as unknown as React.KeyboardEvent;

    const wasRowAdded = addRow(
      editorState,
      (updateState) => {
        editorState = updateState(editorState);
      },
      keyboardEvent,
    );

    expect(wasRowAdded).toBe(true);

    const updatedTable = editorState.redigertBrev.blocks[0].content[0] as ReturnType<typeof table>;
    expect(updatedTable.rows.length).toBe(2);
  });
});

describe("font toggling inside a table cell", () => {
  it("bold button sets editedFontType on the targeted cell literal", () => {
    const focus: Focus = {
      blockIndex: 0,
      contentIndex: 0,
      itemIndex: 0,
      itemContentIndex: 0,
      cursorPosition: 0,
    };
    const initialState = createEditorState(simpleTable, focus);

    const updatedState: LetterEditorState = switchFontType(initialState, focus, FontType.BOLD);

    const literalContent = (updatedState.redigertBrev.blocks[0].content[0] as ReturnType<typeof table>).rows[0].cells[0]
      .text[0] as {
      editedFontType: FontType;
    };

    expect(literalContent.editedFontType).toBe(FontType.BOLD);
  });
});
