import { fireEvent, render } from "@testing-library/react";
import React from "react";
import { beforeEach, describe, expect, test, vi } from "vitest";

import { EditableText } from "~/Brevredigering/LetterEditor/components/ContentGroup";
import { EditorStateContext } from "~/Brevredigering/LetterEditor/LetterEditor";
import type { CallbackReceiver } from "~/Brevredigering/LetterEditor/lib/actions";
import type { LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import type { LiteralValue } from "~/types/brevbakerTypes";

import { letter, literal, paragraph } from "../../utils";

const literalIndex = { blockIndex: 0, contentIndex: 0 };

const createEditorState = (): LetterEditorState => {
  const state = letter(paragraph([literal({ text: "Test tekst" })]));
  return {
    ...state,
    focus: { ...literalIndex, cursorPosition: undefined },
  };
};

const renderEditable = (setEditorState: CallbackReceiver<LetterEditorState>) => {
  const editorState = createEditorState();
  const content = editorState.redigertBrev.blocks[0].content[0] as LiteralValue;

  return render(
    <EditorStateContext.Provider
      value={{
        freeze: false,
        error: false,
        editorState,
        setEditorState,
        undo: vi.fn(),
        redo: vi.fn(),
      }}
    >
      <EditableText content={content} literalIndex={literalIndex} />
    </EditorStateContext.Provider>,
  );
};

const selectText = (node: HTMLElement, start: number, end: number, collapsed = false) => {
  const textNode = node.firstChild as Text;
  const range = document.createRange();
  range.setStart(textNode, start);
  range.setEnd(textNode, end);

  const selection = globalThis.getSelection();
  selection?.removeAllRanges();
  selection?.addRange(range);

  if (collapsed) {
    selection?.collapseToStart();
  }
};

describe("EditableText - updateFocus respects user selection", () => {
  let setEditorStateMock: ReturnType<typeof vi.fn>;
  let setEditorState: CallbackReceiver<LetterEditorState>;

  beforeEach(() => {
    setEditorStateMock = vi.fn();
    setEditorState = setEditorStateMock as unknown as CallbackReceiver<LetterEditorState>;
  });

  test("keeps mouse selection and skips updateFocus when a range is selected", () => {
    const { getByText } = renderEditable(setEditorState);
    const span = getByText("Test tekst");

    selectText(span, 0, 4);

    const callsBeforeKeyDown = setEditorStateMock.mock.calls.length;

    fireEvent.keyDown(span, { key: "a" });

    const selection = globalThis.getSelection();
    expect(selection?.isCollapsed).toBe(false);
    expect(selection?.getRangeAt(0).startOffset).toBe(0);
    expect(selection?.getRangeAt(0).endOffset).toBe(4);
    expect(setEditorStateMock.mock.calls.length).toBe(callsBeforeKeyDown);
  });

  test("calls updateFocus when only a caret is present", () => {
    const { getByText } = renderEditable(setEditorState);
    const span = getByText("Test tekst");

    selectText(span, 4, 4, true);

    fireEvent.keyDown(span, { key: "a" });

    const selection = globalThis.getSelection();
    expect(selection?.isCollapsed).toBe(true);
    expect(selection?.getRangeAt(0).startOffset).toBe(4);
    expect(setEditorStateMock).toHaveBeenCalled();
  });
});
