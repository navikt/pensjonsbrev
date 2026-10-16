import { fireEvent, render, screen } from "@testing-library/react";
import { afterEach, describe, expect, test, vi } from "vitest";

import DebugPanel from "~/Brevredigering/LetterEditor/components/DebugPanel";
import { EditorStateContext } from "~/Brevredigering/LetterEditor/LetterEditor";
import { type CallbackReceiver } from "~/Brevredigering/LetterEditor/lib/actions";
import { type LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { TITLE_INDEX } from "~/types/brevbakerTypes";
import { item, itemList, letter, literal, paragraph } from "~test/support/letterEditorTestUtils";

const initialHeight = Math.round(globalThis.innerHeight * 0.3);
const minimumHeight = 40;
const maximumHeight = Math.max(minimumHeight, Math.floor(globalThis.innerHeight * 0.8));

const createEditorState = () => letter(paragraph([literal({ text: "Test text" }), literal({ text: "More text" })]));

function debugPanelTree(editorState: LetterEditorState) {
  return (
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
    </EditorStateContext.Provider>
  );
}

function renderDebugPanel(editorState = createEditorState()) {
  const result = render(debugPanelTree(editorState));
  return {
    ...result,
    rerenderWithEditorState: (nextState: LetterEditorState) => result.rerender(debugPanelTree(nextState)),
  };
}

afterEach(() => {
  vi.restoreAllMocks();
});

describe("DebugPanel resize handle", () => {
  test("starts at 30vh and supports keyboard resizing within its bounds", () => {
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

describe("DebugPanel focused entry", () => {
  test("scrolls the focused content entry into view", () => {
    const editorState = createEditorState();
    const { container, rerenderWithEditorState } = renderDebugPanel({
      ...editorState,
      focus: { ...editorState.focus, blockIndex: 0, contentIndex: 0 },
    });
    const panel = container.querySelector<HTMLElement>(".debug-panel")!;
    const tree = container.querySelector<HTMLElement>(".debug-panel-tree")!;

    Object.defineProperty(tree, "clientHeight", { configurable: true, value: 100 });
    vi.spyOn(HTMLElement.prototype, "getBoundingClientRect").mockImplementation(function (this: HTMLElement) {
      if (this === tree) return new DOMRect(0, 0, 200, 100);
      if (this.hasAttribute("data-debug-focus-target")) return new DOMRect(0, 120, 200, 20);
      return new DOMRect(0, 0, 200, 20);
    });

    rerenderWithEditorState({
      ...editorState,
      focus: { ...editorState.focus, blockIndex: 0, contentIndex: 1 },
    });

    expect(tree.scrollTop).toBe(40);
    expect(panel.scrollTop).toBe(0);
    expect(tree.querySelectorAll("[data-debug-focus-target]")).toHaveLength(1);
    expect(tree.contains(container.querySelector("[data-debug-panel-stats]"))).toBe(false);
    expect(tree.parentElement).toBe(panel);
  });

  test("targets the focused literal inside the selected list item", () => {
    const editorState = letter(
      paragraph([
        itemList({
          items: [
            item(literal({ text: "First item" }), literal({ text: "First item continues" })),
            item(literal({ text: "Second item" }), literal({ text: "Second item continues" })),
          ],
        }),
        literal({ text: "After the list" }),
      ]),
    );

    const { container } = renderDebugPanel({
      ...editorState,
      focus: {
        ...editorState.focus,
        blockIndex: 0,
        contentIndex: 0,
        itemIndex: 1,
        itemContentIndex: 0,
      },
    });

    const targets = container.querySelectorAll("[data-debug-focus-target]");
    expect(targets).toHaveLength(1);
    expect(targets[0].textContent).toContain("Second item");
  });

  test("targets only the focused list when a block contains several lists", () => {
    const editorState = letter(
      paragraph([
        itemList({
          items: [
            item(literal({ text: "List A first" }), literal({ text: "List A first continues" })),
            item(literal({ text: "List A second" }), literal({ text: "List A second continues" })),
          ],
        }),
        itemList({
          items: [
            item(literal({ text: "List B first" }), literal({ text: "List B first continues" })),
            item(literal({ text: "List B second" }), literal({ text: "List B second continues" })),
          ],
        }),
      ]),
    );

    const { container } = renderDebugPanel({
      ...editorState,
      focus: {
        ...editorState.focus,
        blockIndex: 0,
        contentIndex: 1,
        itemIndex: 0,
        itemContentIndex: 0,
      },
    });

    const targets = container.querySelectorAll("[data-debug-focus-target]");
    expect(targets).toHaveLength(1);
    expect(targets[0].textContent).toContain("List B first");
  });

  test("shows and scrolls to a focused title literal", () => {
    const editorState = createEditorState();
    const { container } = renderDebugPanel({
      ...editorState,
      focus: { ...editorState.focus, blockIndex: TITLE_INDEX, contentIndex: 0 },
    });

    const target = container.querySelector("[data-debug-focus-target]");
    expect(target?.textContent).toContain(editorState.redigertBrev.title.text[0].text);
  });
});
