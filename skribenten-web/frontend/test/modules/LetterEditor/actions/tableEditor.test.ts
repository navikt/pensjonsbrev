import type React from "react";
import type { Mock } from "vitest";
import { describe, expect, it, vi } from "vitest";

import { isItemContentIndex } from "~/Brevredigering/LetterEditor/actions/common";
import { switchFontType } from "~/Brevredigering/LetterEditor/actions/switchFontType";
import type { Focus, LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import {
  addRow,
  handleBackspaceInTableCell,
  nextTableFocus,
} from "~/Brevredigering/LetterEditor/services/tableCaretUtils";
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
  it("adds a blank row when Tab is pressed in the last cell of the last row", () => {
    const startFocus: Focus = {
      blockIndex: 0,
      contentIndex: 0,
      itemIndex: simpleTable.rows.length - 1,
      itemContentIndex: simpleTable.rows[0].cells.length - 1,
      cursorPosition: 0,
    };

    let editorState = createEditorState(simpleTable, startFocus);

    const keyboardEvent = {
      preventDefault: vi.fn(),
      shiftKey: false,
    } as unknown as React.KeyboardEvent;

    const wasRowAdded = addRow(
      editorState,
      (updateState) => {
        editorState = updateState(editorState);
      },
      keyboardEvent,
    );

    expect(wasRowAdded).toBe(true);

    const updatedTable = editorState.redigertBrev.blocks[0].content[0] as ReturnType<typeof table>;
    expect(updatedTable.rows.length).toBe(simpleTable.rows.length + 1);
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

function fakeEvt(): React.KeyboardEvent & { preventDefault: Mock } {
  return {
    key: "Backspace",
    preventDefault: vi.fn(),
  } as unknown as React.KeyboardEvent & { preventDefault: Mock };
}

// 2 × 2 table, second row completely blank
const tblBlankRow = table(
  [],
  [
    row(cell(literal({ text: "A1" })), cell(literal({ text: "A2" }))),
    row(cell(literal({ text: "" })), cell(literal({ text: "" }))),
  ],
);

// 1 × 1 table, single filled cell
const tblFilled = table([], [row(cell(literal({ text: "X" })))]);

describe("handleBackspaceInTableCell()", () => {
  it("jumps left when caret is in an empty non-first column (offset 0)", () => {
    const initialFocus = {
      blockIndex: 0,
      contentIndex: 0,
      itemIndex: 1,
      itemContentIndex: 1,
      cursorPosition: 0,
    };
    let editorState = createEditorState(tblBlankRow, initialFocus);

    const wasBackspaceHandled = handleBackspaceInTableCell(fakeEvt(), editorState, (fn) => {
      editorState = fn(editorState);
    });

    expect(wasBackspaceHandled).toBe(true);
    expect(isItemContentIndex(editorState.focus) && editorState.focus.itemContentIndex).toBe(0);
  });

  it("jumps left when caret is after the ZWSP (offset 1)", () => {
    const initialFocus = {
      blockIndex: 0,
      contentIndex: 0,
      itemIndex: 1,
      itemContentIndex: 1,
      cursorPosition: 1,
    };
    let editorState = createEditorState(tblBlankRow, initialFocus);

    const wasBackspaceHandled = handleBackspaceInTableCell(fakeEvt(), editorState, (fn) => {
      editorState = fn(editorState);
    });

    expect(wasBackspaceHandled).toBe(true);
    expect(isItemContentIndex(editorState.focus) && editorState.focus.itemContentIndex).toBe(0);
  });

  it("deletes the row when the first column is empty and entire row blank", () => {
    const initialFocus = {
      blockIndex: 0,
      contentIndex: 0,
      itemIndex: 1,
      itemContentIndex: 0,
      cursorPosition: 0,
    };
    let editorState = createEditorState(tblBlankRow, initialFocus);

    const wasBackspaceHandled = handleBackspaceInTableCell(fakeEvt(), editorState, (fn) => {
      editorState = fn(editorState);
    });

    const tblAfter = editorState.redigertBrev.blocks[0].content[0];
    expect(wasBackspaceHandled).toBe(true);
    expect((tblAfter as ReturnType<typeof table>).rows.length).toBe(1);
  });

  it("falls through when cell is not empty (char-delete path)", () => {
    const initialFocus = {
      blockIndex: 0,
      contentIndex: 0,
      itemIndex: 0,
      itemContentIndex: 0,
      cursorPosition: 0,
    };
    let editorState = createEditorState(tblFilled, initialFocus);

    const evt = fakeEvt();
    const wasBackspaceHandled = handleBackspaceInTableCell(evt, editorState, (fn) => {
      editorState = fn(editorState);
    });

    expect(wasBackspaceHandled).toBe(false);
    expect(evt.preventDefault).not.toHaveBeenCalled();
  });
});
