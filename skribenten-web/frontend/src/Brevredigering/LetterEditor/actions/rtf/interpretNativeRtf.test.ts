import { describe, expect, test } from "vitest";

import { interpretNativeRtf } from "~/Brevredigering/LetterEditor/actions/rtf/interpretNativeRtf";
import { tokenizeRtf } from "~/Brevredigering/LetterEditor/actions/rtf/tokenizeRtf";
import { FontType, ListType } from "~/types/brevbakerTypes";

function interpret(rtf: string) {
  return interpretNativeRtf(tokenizeRtf(rtf));
}

describe("interpretNativeRtf", () => {
  test("plain paragraph", () => {
    const rtf = "{\\rtf1\\ansi\\deff0{\\fonttbl{\\f0 Calibri;}}\\pard Hello world\\par}";
    expect(interpret(rtf)).toEqual([
      { type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "Hello world" }] },
    ]);
  });

  test("bold and italic runs within a paragraph", () => {
    const rtf = "{\\rtf1\\ansi\\pard Plain {\\b bold} and {\\i italic} text\\par}";
    expect(interpret(rtf)).toEqual([
      {
        type: "P",
        content: [
          { type: "TEXT", font: FontType.PLAIN, text: "Plain " },
          { type: "TEXT", font: FontType.BOLD, text: "bold" },
          { type: "TEXT", font: FontType.PLAIN, text: " and " },
          { type: "TEXT", font: FontType.ITALIC, text: "italic" },
          { type: "TEXT", font: FontType.PLAIN, text: " text" },
        ],
      },
    ]);
  });

  test("\\b0/\\i0 turn formatting back off within the same group", () => {
    const rtf = "{\\rtf1\\ansi\\pard \\b Bold\\b0  not bold\\par}";
    expect(interpret(rtf)).toEqual([
      {
        type: "P",
        content: [
          { type: "TEXT", font: FontType.BOLD, text: "Bold" },
          { type: "TEXT", font: FontType.PLAIN, text: " not bold" },
        ],
      },
    ]);
  });

  test("multiple paragraphs separated by \\par", () => {
    const rtf = "{\\rtf1\\ansi\\pard First\\par\\pard Second\\par}";
    expect(interpret(rtf)).toEqual([
      { type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "First" }] },
      { type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "Second" }] },
    ]);
  });

  test("heading via direct \\outlinelevel", () => {
    const rtf = "{\\rtf1\\ansi\\pard\\outlinelevel0 Title 1\\par\\pard\\outlinelevel1 Title 2\\par\\pard Body\\par}";
    expect(interpret(rtf)).toEqual([
      { type: "H1", content: [{ type: "TEXT", font: FontType.PLAIN, text: "Title 1" }] },
      { type: "H2", content: [{ type: "TEXT", font: FontType.PLAIN, text: "Title 2" }] },
      { type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "Body" }] },
    ]);
  });

  test("heading resolved via stylesheet style name when no direct \\outlinelevel is present", () => {
    const rtf =
      "{\\rtf1\\ansi" +
      "{\\stylesheet{\\s1\\outlinelevel0 heading 1;}{\\s2\\outlinelevel1 heading 2;}}" +
      "\\pard\\s1 Overskrift\\par" +
      "\\pard\\s2 Underoverskrift\\par" +
      "\\pard Vanlig avsnitt\\par}";
    expect(interpret(rtf)).toEqual([
      { type: "H1", content: [{ type: "TEXT", font: FontType.PLAIN, text: "Overskrift" }] },
      { type: "H2", content: [{ type: "TEXT", font: FontType.PLAIN, text: "Underoverskrift" }] },
      { type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "Vanlig avsnitt" }] },
    ]);
  });

  test("bulleted list item classified from a bullet-character listtext marker", () => {
    const rtf = "{\\rtf1\\ansi\\pard\\ls1{\\*\\listtext\\'b7\\tab}Item one\\par\\pard Not a list item\\par}";
    expect(interpret(rtf)).toEqual([
      {
        type: "ITEM",
        content: [{ type: "TEXT", font: FontType.PLAIN, text: "Item one" }],
        listType: ListType.PUNKTLISTE,
      },
      { type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "Not a list item" }] },
    ]);
  });

  test("numbered list item classified from a numeric listtext marker", () => {
    const rtf =
      "{\\rtf1\\ansi\\pard\\ls2{\\*\\listtext 1.\\tab}First\\par\\pard\\ls2{\\*\\listtext 2.\\tab}Second\\par}";
    expect(interpret(rtf)).toEqual([
      {
        type: "ITEM",
        content: [{ type: "TEXT", font: FontType.PLAIN, text: "First" }],
        listType: ListType.NUMMERERT_LISTE,
      },
      {
        type: "ITEM",
        content: [{ type: "TEXT", font: FontType.PLAIN, text: "Second" }],
        listType: ListType.NUMMERERT_LISTE,
      },
    ]);
  });

  test("simple table with two rows", () => {
    const rtf =
      "{\\rtf1\\ansi" +
      "\\trowd\\cellx2000\\cellx4000 A\\cell B\\cell\\row" +
      "\\trowd\\cellx2000\\cellx4000 C\\cell D\\cell\\row" +
      "}";
    expect(interpret(rtf)).toEqual([
      {
        type: "TABLE",
        rows: [
          {
            cells: [
              { content: [{ type: "TEXT", font: FontType.PLAIN, text: "A" }] },
              { content: [{ type: "TEXT", font: FontType.PLAIN, text: "B" }] },
            ],
          },
          {
            cells: [
              { content: [{ type: "TEXT", font: FontType.PLAIN, text: "C" }] },
              { content: [{ type: "TEXT", font: FontType.PLAIN, text: "D" }] },
            ],
          },
        ],
      },
    ]);
  });

  test("table followed by a trailing paragraph", () => {
    const rtf = "{\\rtf1\\ansi\\trowd\\cellx2000 A\\cell\\row\\pard Etter tabellen\\par}";
    expect(interpret(rtf)).toEqual([
      { type: "TABLE", rows: [{ cells: [{ content: [{ type: "TEXT", font: FontType.PLAIN, text: "A" }] }] }] },
      { type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "Etter tabellen" }] },
    ]);
  });

  test("decodes non-ASCII text via the ansi codepage within a paragraph", () => {
    const rtf = "{\\rtf1\\ansi\\pard Bj\\'f8rn og \\'e5se\\par}";
    expect(interpret(rtf)).toEqual([
      { type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "Bjørn og åse" }] },
    ]);
  });

  test("ignores font table and stylesheet content, not leaking into body text", () => {
    const rtf = "{\\rtf1\\ansi{\\fonttbl{\\f0 Calibri;}}{\\stylesheet{\\s0 Normal;}}\\pard Actual content\\par}";
    expect(interpret(rtf)).toEqual([
      { type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "Actual content" }] },
    ]);
  });

  test("produces no elements for an effectively empty document", () => {
    const rtf = "{\\rtf1\\ansi\\deff0{\\fonttbl{\\f0 Calibri;}}}";
    expect(interpret(rtf)).toEqual([]);
  });

  test("flushes a trailing paragraph even without a final \\par", () => {
    const rtf = "{\\rtf1\\ansi\\pard First\\par\\pard No trailing par}";
    expect(interpret(rtf)).toEqual([
      { type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "First" }] },
      { type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "No trailing par" }] },
    ]);
  });

  test("a lone unterminated plain paragraph is returned as loose text (inline fragment)", () => {
    const rtf = "{\\rtf1\\ansi\\pard Fragment {\\b fet}}";
    expect(interpret(rtf)).toEqual([
      { type: "TEXT", font: FontType.PLAIN, text: "Fragment " },
      { type: "TEXT", font: FontType.BOLD, text: "fet" },
    ]);
  });

  test("a lone unterminated heading is still returned as a heading", () => {
    const rtf = "{\\rtf1\\ansi\\pard\\outlinelevel0 Tittel}";
    expect(interpret(rtf)).toEqual([{ type: "H1", content: [{ type: "TEXT", font: FontType.PLAIN, text: "Tittel" }] }]);
  });
});
