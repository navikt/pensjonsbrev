import { fireEvent, render, screen } from "@testing-library/react";
import { describe, expect, test, vi } from "vitest";

import DebugPanel from "~/Brevredigering/LetterEditor/components/DebugPanel";
import { EditorStateContext } from "~/Brevredigering/LetterEditor/LetterEditor";
import { type CallbackReceiver } from "~/Brevredigering/LetterEditor/lib/actions";
import { type LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { letter, literal, paragraph } from "~test/support/letterEditorTestUtils";

const initialHeight = Math.round(globalThis.innerHeight * 0.4);
const minimumHeight = 120;
const maximumHeight = Math.max(minimumHeight, Math.floor(globalThis.innerHeight * 0.8));

function renderDebugPanel() {
  const editorState = letter(paragraph([literal({ text: "Test text" }), literal({ text: "More text" })]));

  return render(
    <EditorStateContext.Provider
      value={{
        freeze: false,
        error: false,
        editorState,
        setEditorState: vi.fn() as CallbackReceiver<LetterEditorState>,
        undo: vi.fn(),
        redo: vi.fn(),
      }}
    >
      <DebugPanel />
    </EditorStateContext.Provider>,
  );
}

describe("DebugPanel resize handle", () => {
  test("starts at 40vh and supports keyboard resizing within its bounds", () => {
    renderDebugPanel();
    const handle = screen.getByRole("separator", { name: "Endre høyden på debugpanelet" });

    expect(handle.getAttribute("aria-valuenow")).toBe(String(initialHeight));

    fireEvent.keyDown(handle, { key: "ArrowUp" });
    expect(handle.getAttribute("aria-valuenow")).toBe(String(initialHeight + 16));

    fireEvent.keyDown(handle, { key: "Home" });
    expect(handle.getAttribute("aria-valuenow")).toBe(String(minimumHeight));

    fireEvent.keyDown(handle, { key: "End" });
    expect(handle.getAttribute("aria-valuenow")).toBe(String(maximumHeight));
  });

  test("clamps pointer resizing at the minimum and maximum heights", () => {
    renderDebugPanel();
    const handle = screen.getByRole("separator", { name: "Endre høyden på debugpanelet" });

    fireEvent.pointerDown(handle, { pointerId: 1, clientY: 200 });
    fireEvent.pointerMove(document.defaultView!, { pointerId: 1, clientY: 1000 });
    expect(handle.getAttribute("aria-valuenow")).toBe(String(minimumHeight));
    fireEvent.pointerUp(document.defaultView!, { pointerId: 1 });

    fireEvent.pointerDown(handle, { pointerId: 1, clientY: 200 });
    fireEvent.pointerMove(document.defaultView!, { pointerId: 1, clientY: -1000 });
    expect(handle.getAttribute("aria-valuenow")).toBe(String(maximumHeight));
  });
});
