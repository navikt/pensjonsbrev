import { act, render } from "@testing-library/react";
import { useState } from "react";
import { describe, expect, test, vi } from "vitest";

import { EditableText } from "~/Brevredigering/LetterEditor/components/ContentGroup";
import { EditableSurface } from "~/Brevredigering/LetterEditor/components/EditableSurface";
import { EditorStateContext } from "~/Brevredigering/LetterEditor/LetterEditor";
import { type CallbackReceiver } from "~/Brevredigering/LetterEditor/lib/actions";
import { type LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { type LiteralValue } from "~/types/brevbakerTypes";
import { letter, literal, paragraph } from "~test/support/letterEditorTestUtils";

const literalIndex = { blockIndex: 0, contentIndex: 0 };

function StatefulSurface({
  onUpdate,
  stateRef,
}: {
  onUpdate: CallbackReceiver<LetterEditorState>;
  stateRef: { current: LetterEditorState };
}) {
  const [editorState, setEditorState] = useState<LetterEditorState>(() => ({
    ...letter(paragraph([literal({ text: "Test tekst" })])),
    focus: { ...literalIndex, cursorPosition: undefined },
  }));
  stateRef.current = editorState;
  const wrapped: CallbackReceiver<LetterEditorState> = (updater) => {
    onUpdate(updater);
    setEditorState((prev) => updater(prev));
  };
  const content = editorState.redigertBrev.blocks[0].content[0] as LiteralValue;

  return (
    <EditorStateContext.Provider
      value={{
        freeze: false,
        error: false,
        editorState,
        setEditorState: wrapped,
        undo: vi.fn(),
        redo: vi.fn(),
      }}
    >
      <EditableSurface data-testid="surface">
        <EditableText content={content} literalIndex={literalIndex} />
      </EditableSurface>
    </EditorStateContext.Provider>
  );
}

const selectText = (surface: HTMLElement, node: HTMLElement, start: number, end: number, collapsed = false) => {
  surface.focus();
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
  document.dispatchEvent(new Event("selectionchange"));
};

describe("EditableSurface - selectionchange syncs DOM selection to model", () => {
  test("maps non-collapsed mouse selection into editorState.selection without collapsing DOM range", () => {
    const spy = vi.fn();
    const stateRef = { current: {} as LetterEditorState };
    const { getByText, getByTestId } = render(
      <StatefulSurface onUpdate={spy as unknown as CallbackReceiver<LetterEditorState>} stateRef={stateRef} />,
    );
    const surface = getByTestId("surface");
    const span = getByText("Test tekst");

    act(() => {
      selectText(surface, span, 0, 4);
    });

    const selection = globalThis.getSelection();
    expect(selection?.isCollapsed).toBe(false);
    expect(selection?.getRangeAt(0).startOffset).toBe(0);
    expect(selection?.getRangeAt(0).endOffset).toBe(4);
    expect(stateRef.current.selection).toEqual({
      start: { ...literalIndex, cursorPosition: 0 },
      end: { ...literalIndex, cursorPosition: 4 },
    });
  });

  test("maps collapsed caret into editorState.focus and clears editorState.selection", () => {
    const spy = vi.fn();
    const stateRef = { current: {} as LetterEditorState };
    const { getByText, getByTestId } = render(
      <StatefulSurface onUpdate={spy as unknown as CallbackReceiver<LetterEditorState>} stateRef={stateRef} />,
    );
    const surface = getByTestId("surface");
    const span = getByText("Test tekst");

    act(() => {
      selectText(surface, span, 4, 4, true);
    });

    const selection = globalThis.getSelection();
    expect(selection?.isCollapsed).toBe(true);
    expect(selection?.getRangeAt(0).startOffset).toBe(4);
    expect(stateRef.current.focus).toEqual({ ...literalIndex, cursorPosition: 4 });
    expect(stateRef.current.selection).toBeUndefined();
  });
});
