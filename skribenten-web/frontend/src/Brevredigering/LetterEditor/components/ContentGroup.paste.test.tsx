import { fireEvent, render, screen } from "@testing-library/react";
import { StrictMode, useState } from "react";
import { afterEach, beforeEach, describe, expect, test, vi } from "vitest";

import { ContentGroup } from "~/Brevredigering/LetterEditor/components/ContentGroup";
import { EditorStateContext } from "~/Brevredigering/LetterEditor/LetterEditor";
import { type LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { getSelectionFocus } from "~/Brevredigering/LetterEditor/services/caretUtils";
import { cell, letter, literal, paragraph, row, table } from "~test/support/letterEditorTestUtils";
import { MockDataTransfer } from "~test/support/pasteTestUtils";

function Editor({ initialState }: { initialState: LetterEditorState }) {
  const [editorState, setEditorState] = useState(initialState);
  return (
    <EditorStateContext.Provider
      value={{ freeze: false, error: false, editorState, setEditorState, undo: vi.fn(), redo: vi.fn() }}
    >
      <div data-editor-root>
        <ContentGroup literalIndex={{ blockIndex: 0, contentIndex: 0 }} />
      </div>
    </EditorStateContext.Provider>
  );
}

function selectText(element: HTMLElement, start: number, end: number) {
  const textNode = element.firstChild;
  if (!textNode) throw new Error("Expected text node");
  const range = document.createRange();
  range.setStart(textNode, start);
  range.setEnd(textNode, end);
  const selection = globalThis.getSelection();
  selection?.removeAllRanges();
  selection?.addRange(range);
}

function paste(element: HTMLElement, clipboard: Record<string, string>) {
  const event = new Event("paste", { bubbles: true, cancelable: true });
  Object.defineProperty(event, "clipboardData", { value: new MockDataTransfer(clipboard) });
  fireEvent(element, event);
}

beforeEach(() => {
  vi.spyOn(console, "info").mockImplementation(() => {});
});

afterEach(() => {
  globalThis.getSelection()?.removeAllRanges();
  vi.restoreAllMocks();
});

describe("paste replacing a browser selection", () => {
  test.each([
    ["empty plain text", { "text/plain": "" }],
    ["a cell-less table", { "text/html": "<table></table>" }],
    ["empty HTML", { "text/html": "" }],
    ["an unsupported format", { "application/example": "data" }],
  ])("%s leaves the selection available for the next paste", (_, clipboard) => {
    render(
      <StrictMode>
        <Editor initialState={letter(paragraph([literal("Hello World")]))} />
      </StrictMode>,
    );
    const span = screen.getByText("Hello World");
    selectText(span, 0, 5);
    expect(getSelectionFocus()).toEqual({
      start: { blockIndex: 0, contentIndex: 0, cursorPosition: 0 },
      end: { blockIndex: 0, contentIndex: 0, cursorPosition: 5 },
    });

    paste(span, clipboard);

    expect(globalThis.getSelection()?.toString()).toBe("Hello");
    expect(span.textContent).toBe("Hello World");

    paste(span, { "text/plain": "Replacement" });

    expect(span.textContent).toBe("Replacement World");
    expect(globalThis.getSelection()?.isCollapsed).toBe(true);
    expect(globalThis.getSelection()?.getRangeAt(0).startOffset).toBe(11);
  });

  test("a table rejected inside a cell preserves its browser selection", () => {
    const state = letter(
      paragraph([literal(""), table([cell(literal("header"))], [row(cell(literal("Hello World")))]), literal("")]),
    );
    render(<Editor initialState={state} />);
    const span = screen.getByText("Hello World");
    selectText(span, 0, 5);
    expect(globalThis.getSelection()?.toString()).toBe("Hello");
    const selection = getSelectionFocus();
    expect(selection).toEqual({
      start: { blockIndex: 0, contentIndex: 1, rowIndex: 0, cellIndex: 0, cellContentIndex: 0, cursorPosition: 0 },
      end: { blockIndex: 0, contentIndex: 1, rowIndex: 0, cellIndex: 0, cellContentIndex: 0, cursorPosition: 5 },
    });

    paste(span, { "text/html": "<table><tr><td>x</td></tr></table>" });

    expect(globalThis.getSelection()?.toString()).toBe("Hello");
    expect(span.textContent).toBe("Hello World");
  });

  test("a successful replacement collapses the selection even if text and cursor offset are unchanged", () => {
    const state = letter(paragraph([literal({ text: "Hello World", editedText: "Hello World" })]));
    state.focus = { blockIndex: 0, contentIndex: 0, cursorPosition: 5 };
    render(<Editor initialState={state} />);
    const span = screen.getByText("Hello World");
    selectText(span, 0, 5);

    paste(span, { "text/plain": "Hello" });

    expect(span.textContent).toBe("Hello World");
    expect(globalThis.getSelection()?.isCollapsed).toBe(true);
    expect(globalThis.getSelection()?.getRangeAt(0).startOffset).toBe(5);
  });
});
