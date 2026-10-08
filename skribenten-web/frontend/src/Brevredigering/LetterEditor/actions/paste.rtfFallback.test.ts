import { afterEach, beforeEach, expect, test, vi } from "vitest";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { parseRtfClipboard } from "~/Brevredigering/LetterEditor/actions/rtf/parseRtfClipboard";
import { type LiteralValue } from "~/types/brevbakerTypes";
import { letter, literal, paragraph } from "~test/support/letterEditorTestUtils";
import { MockDataTransfer } from "~test/support/pasteTestUtils";

vi.mock("~/Brevredigering/LetterEditor/actions/rtf/parseRtfClipboard", () => ({ parseRtfClipboard: vi.fn() }));

const LOG_LINE = "Skribenten:pasteHandler: unable to interpret rtf clipboard content";

let info: ReturnType<typeof vi.spyOn>;

beforeEach(() => {
  info = vi.spyOn(console, "info").mockImplementation(() => {});
});

afterEach(() => {
  vi.restoreAllMocks();
});

function pasteRtfWithPlainText(): string {
  const state = letter(paragraph({ id: 1, content: [literal({ id: 11, text: "Teksten min" })] }));
  const result = Actions.paste(
    state,
    { blockIndex: 0, contentIndex: 0 },
    0,
    new MockDataTransfer({ "text/rtf": "{\\rtf1 Hei\\par}", "text/plain": "Ren tekst " }),
  );
  const content = result.redigertBrev.blocks[0].content as LiteralValue[];
  return content.map((item) => item.editedText ?? item.text).join("");
}

test("falls back to text/plain and logs when the RTF can't be interpreted", () => {
  const error = new Error("boom");
  vi.mocked(parseRtfClipboard).mockReturnValue({ mode: "unsupported", error });

  expect(pasteRtfWithPlainText()).toBe("Ren tekst Teksten min");
  expect(info).toHaveBeenCalledWith(LOG_LINE, error);
});

test("falls back to text/plain silently when the RTF has nothing to insert", () => {
  vi.mocked(parseRtfClipboard).mockReturnValue({ mode: "empty" });

  expect(pasteRtfWithPlainText()).toBe("Ren tekst Teksten min");
  expect(info).not.toHaveBeenCalledWith(LOG_LINE, expect.anything());
});

test("keeps the selection when the RTF can't be interpreted and there is no text/plain", () => {
  vi.mocked(parseRtfClipboard).mockReturnValue({ mode: "unsupported", error: new Error("boom") });
  const state = letter(paragraph({ id: 1, content: [literal({ id: 11, text: "Teksten min" })] }));

  const result = Actions.pasteReplacingSelection(
    state,
    {
      start: { blockIndex: 0, contentIndex: 0, cursorPosition: 0 },
      end: { blockIndex: 0, contentIndex: 0, cursorPosition: 7 },
    },
    new MockDataTransfer({ "text/rtf": "{\\rtf1 Hei\\par}" }),
  );

  expect(result.redigertBrev).toEqual(state.redigertBrev);
  expect(info).toHaveBeenCalledWith(LOG_LINE, expect.any(Error));
});
