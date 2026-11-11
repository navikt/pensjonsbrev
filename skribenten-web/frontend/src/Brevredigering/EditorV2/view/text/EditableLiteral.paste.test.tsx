import { fireEvent, screen } from "@testing-library/react";
import { StrictMode } from "react";
import { afterEach, describe, expect, test, vi } from "vitest";

import { trackEvent } from "~/utils/umami";
import { cell, column, literal, paragraph, row, stateV2, table } from "~test/support/editorV2TestUtils";
import { block, renderLive, selectText } from "~test/support/editorV2ViewTestUtils";
import { MockDataTransfer } from "~test/support/pasteTestUtils";

import { caret } from "../../model/paths";

vi.mock("~/utils/umami", () => ({ trackEvent: vi.fn() }));

function paste(element: HTMLElement, clipboard: Record<string, string>) {
  const event = new Event("paste", { bubbles: true, cancelable: true });
  Object.defineProperty(event, "clipboardData", { value: new MockDataTransfer(clipboard) });
  fireEvent(element, event);
}

afterEach(() => {
  globalThis.getSelection()?.removeAllRanges();
  vi.clearAllMocks();
});

describe("paste replacing a browser selection", () => {
  test.each([
    ["empty plain text", { "text/plain": "" }],
    ["a cell-less table", { "text/html": "<table></table>" }],
    ["empty HTML", { "text/html": "" }],
    ["an unsupported format", { "application/example": "data" }],
  ])("%s leaves the selection available for the next paste", (_, clipboard) => {
    renderLive(stateV2(paragraph(literal("Hello World"))), (editor) => <StrictMode>{editor}</StrictMode>);
    const span = screen.getByText("Hello World");
    selectText(span, 0, 5);

    paste(span, clipboard);

    expect(globalThis.getSelection()?.toString()).toBe("Hello");
    expect(span.textContent).toBe("Hello World");

    paste(span, { "text/plain": "Replacement" });

    expect(span.textContent).toBe("Replacement World");
    expect(globalThis.getSelection()?.isCollapsed).toBe(true);
    expect(globalThis.getSelection()?.getRangeAt(0).startOffset).toBe(11);
  });

  test("a table rejected inside a cell preserves its browser selection", () => {
    renderLive(stateV2(table([column(literal("header"))], [row(cell(literal("Hello World")))])));
    const span = screen.getByText("Hello World");
    selectText(span, 0, 5);

    paste(span, { "text/html": "<table><tr><td>x</td></tr></table>" });

    expect(globalThis.getSelection()?.toString()).toBe("Hello");
    expect(span.textContent).toBe("Hello World");
  });

  test("a successful replacement collapses the selection even if text and caret offset are unchanged", () => {
    const state = stateV2(paragraph(literal({ text: "Hello World", editedText: "Hello World" })));
    renderLive({ ...state, focus: caret(block(0), 0, 5) });
    const span = screen.getByText("Hello World");
    selectText(span, 0, 5);

    paste(span, { "text/plain": "Hello" });

    expect(span.textContent).toBe("Hello World");
    expect(globalThis.getSelection()?.isCollapsed).toBe(true);
    expect(globalThis.getSelection()?.getRangeAt(0).startOffset).toBe(5);
  });
});

describe("paste tracking", () => {
  test("tracks a keyboard shortcut paste at the caret", () => {
    renderLive(stateV2(paragraph(literal("Hello World"))));
    const span = screen.getByText("Hello World");
    selectText(span, 5);

    fireEvent.keyDown(span, { key: "v", ctrlKey: true });
    paste(span, { "text/plain": "abc" });

    expect(trackEvent).toHaveBeenCalledWith(
      "tekst limt inn",
      expect.objectContaining({ antallTegn: 3, limInnMetode: "tastatursnarvei", innholdsformat: "ren tekst" }),
    );
    expect(span.textContent).toBe("Helloabc World");
  });

  test("tracks a context menu paste replacing a selection", () => {
    renderLive(stateV2(paragraph(literal("Hello World"))));
    const span = screen.getByText("Hello World");
    selectText(span, 0, 5);

    fireEvent.contextMenu(span);
    paste(span, { "text/plain": "Hi" });

    expect(trackEvent).toHaveBeenCalledWith(
      "tekst erstattet",
      expect.objectContaining({ antallTegn: 2, limInnMetode: "kontekstmeny" }),
    );
  });

  test("does not track an empty paste", () => {
    renderLive(stateV2(paragraph(literal("Hello World"))));
    const span = screen.getByText("Hello World");
    selectText(span, 5);

    paste(span, { "text/plain": "" });

    expect(trackEvent).not.toHaveBeenCalled();
  });
});
