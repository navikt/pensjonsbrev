import { describe, expect, test, vi } from "vitest";

import { interpretNativeRtf } from "~/Brevredigering/LetterEditor/actions/rtf/interpretNativeRtf";
import {
  getRtfClipboardData,
  MAX_RTF_LENGTH,
  parseRtfClipboard,
} from "~/Brevredigering/LetterEditor/actions/rtf/parseRtfClipboard";
import { FontType, ListType } from "~/types/brevbakerTypes";
import { MockDataTransfer } from "~test/support/pasteTestUtils";

vi.mock("~/Brevredigering/LetterEditor/actions/rtf/interpretNativeRtf", async (importOriginal) => {
  const actual = await importOriginal<typeof import("~/Brevredigering/LetterEditor/actions/rtf/interpretNativeRtf")>();
  return { interpretNativeRtf: vi.fn(actual.interpretNativeRtf) };
});

describe("parseRtfClipboard", () => {
  test("returns elements for native RTF", () => {
    expect(parseRtfClipboard("{\\rtf1\\ansi\\pard\\ls1{\\listtext\\'b7\\tab}Punkt\\par}")).toEqual({
      mode: "elements",
      elements: [
        {
          type: "ITEM",
          content: [{ type: "TEXT", font: FontType.PLAIN, text: "Punkt" }],
          listType: ListType.PUNKTLISTE,
        },
      ],
    });
  });

  test("returns the encapsulated HTML for \\fromhtml1", () => {
    expect(parseRtfClipboard("{\\rtf1\\ansi\\fromhtml1{\\*\\htmltag <p>}Hei{\\*\\htmltag </p>}}")).toEqual({
      mode: "html",
      html: "<p>Hei</p>",
    });
  });

  test.each(["", "   ", "\\tab\\par  ", "{\\*\\htmltag    }"])(
    "returns empty for blank encapsulated HTML: %s",
    (body) => {
      expect(parseRtfClipboard(`{\\rtf1\\ansi\\fromhtml1 ${body}}`)).toEqual({ mode: "empty" });
    },
  );

  test("preserves nonblank HTML without trimming or interpreting its markup", () => {
    expect(parseRtfClipboard("{\\rtf1\\ansi\\fromhtml1{\\*\\htmltag  <p></p> }}")).toEqual({
      mode: "html",
      html: " <p></p> ",
    });
  });

  test("returns the encapsulated text for \\fromtext", () => {
    expect(parseRtfClipboard("{\\rtf1\\ansi\\fromtext Linje 1\\par Linje 2}")).toEqual({
      mode: "text",
      text: "Linje 1\r\nLinje 2",
    });
  });

  test("returns empty for an empty string", () => {
    expect(parseRtfClipboard("")).toEqual({ mode: "empty" });
  });

  test("returns empty for a document with only a header", () => {
    expect(parseRtfClipboard("{\\rtf1\\ansi\\ansicpg1252\\deff0{\\fonttbl{\\f0 Calibri;}}}")).toEqual({
      mode: "empty",
    });
  });

  test("returns empty for \\fromtext with blank text", () => {
    expect(parseRtfClipboard("{\\rtf1\\ansi\\fromtext \\par  }")).toEqual({ mode: "empty" });
  });

  test("returns unsupported instead of throwing when interpreting fails", () => {
    const error = new Error("boom");
    vi.mocked(interpretNativeRtf).mockImplementationOnce(() => {
      throw error;
    });

    expect(parseRtfClipboard("{\\rtf1\\ansi Hei\\par}")).toEqual({ mode: "unsupported", error });
  });

  test("returns unsupported without parsing when the RTF is too large", () => {
    vi.mocked(interpretNativeRtf).mockClear();
    const rtf = `{\\rtf1\\ansi Hei${" ".repeat(MAX_RTF_LENGTH)}\\par}`;

    expect(parseRtfClipboard(rtf)).toEqual({ mode: "unsupported", error: expect.any(Error) });
    expect(interpretNativeRtf).not.toHaveBeenCalled();
  });

  test("returns what came before \\bin data that runs past the end of the clipboard", () => {
    expect(parseRtfClipboard("{\\rtf1\\ansi\\pard Hei\\par{\\pict\\bin999 abc}")).toEqual({
      mode: "elements",
      elements: [{ type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "Hei" }] }],
    });
  });

  test("counts \\binN and its data as one fallback character after \\uN", () => {
    expect(parseRtfClipboard("{\\rtf1\\ansi\\uc1\\u945\\bin1?tail}")).toEqual({
      mode: "elements",
      elements: [{ type: "TEXT", font: FontType.PLAIN, text: "αtail" }],
    });
  });
});

describe("getRtfClipboardData", () => {
  test("prefers text/rtf over application/rtf", () => {
    const clipboard = new MockDataTransfer({ "application/rtf": "{\\rtf1 b}", "text/rtf": "{\\rtf1 a}" });

    expect(getRtfClipboardData(clipboard)).toBe("{\\rtf1 a}");
  });

  test("uses application/rtf when text/rtf is empty", () => {
    const clipboard = new MockDataTransfer({ "text/rtf": "", "application/rtf": "{\\rtf1 b}" });

    expect(getRtfClipboardData(clipboard)).toBe("{\\rtf1 b}");
  });

  test("returns undefined without RTF, or when the RTF is empty", () => {
    expect(getRtfClipboardData(new MockDataTransfer({ "text/plain": "Hei" }))).toBeUndefined();
    expect(getRtfClipboardData(new MockDataTransfer({ "text/rtf": "" }))).toBeUndefined();
  });
});
