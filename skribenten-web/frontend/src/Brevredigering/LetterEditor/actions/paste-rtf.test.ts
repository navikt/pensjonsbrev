import { describe, expect, test } from "vitest";

import {
  detectRtfDocumentLanguage,
  parseRtfToTraversedElements,
} from "~/Brevredigering/LetterEditor/actions/paste-rtf";
import { FontType, ListType } from "~/types/brevbakerTypes";

// Minimal RTF document header shared by most fixtures below. Real Word/TextEdit exports include a
// much larger \fonttbl/\colortbl/\generator preamble, but our parser only reads \ansicpg,
// \deflang/\lang and \stylesheet from the header - everything else is ignorable destination noise.
const HEADER = "{\\rtf1\\ansi\\ansicpg1252\\deflang1033";

describe("parseRtfToTraversedElements", () => {
  test("parses plain paragraph text", () => {
    const rtf = `${HEADER} Hello world\\par}`;

    expect(parseRtfToTraversedElements(rtf)).toEqual([
      { type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "Hello world" }] },
    ]);
  });

  test("parses multiple paragraphs", () => {
    const rtf = `${HEADER} First\\par Second\\par}`;

    expect(parseRtfToTraversedElements(rtf)).toEqual([
      { type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "First" }] },
      { type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "Second" }] },
    ]);
  });

  test("parses bold text", () => {
    const rtf = `${HEADER} Before \\b bold\\b0  after\\par}`;

    expect(parseRtfToTraversedElements(rtf)).toEqual([
      {
        type: "P",
        content: [
          // The spaces separating differently-formatted words are meaningful and must survive -
          // otherwise "Before bold after" would render as "Beforeboldafter".
          { type: "TEXT", font: FontType.PLAIN, text: "Before " },
          { type: "TEXT", font: FontType.BOLD, text: "bold" },
          { type: "TEXT", font: FontType.PLAIN, text: " after" },
        ],
      },
    ]);
  });

  test("parses italic text", () => {
    const rtf = `${HEADER} Before \\i italic\\i0  after\\par}`;

    expect(parseRtfToTraversedElements(rtf)).toEqual([
      {
        type: "P",
        content: [
          { type: "TEXT", font: FontType.PLAIN, text: "Before " },
          { type: "TEXT", font: FontType.ITALIC, text: "italic" },
          { type: "TEXT", font: FontType.PLAIN, text: " after" },
        ],
      },
    ]);
  });

  test("bold takes precedence when bold and italic overlap", () => {
    const rtf = `${HEADER} \\b\\i both\\i0\\b0 \\par}`;

    expect(parseRtfToTraversedElements(rtf)).toEqual([
      { type: "P", content: [{ type: "TEXT", font: FontType.BOLD, text: "both" }] },
    ]);
  });

  test("detects headings via English Word stylesheet names", () => {
    const rtf =
      `${HEADER}` +
      "{\\stylesheet{\\s1 heading 1;}{\\s2 heading 2;}{\\s3 heading 3;}}" +
      "\\s1 Title\\par" +
      "\\s2 Subtitle\\par" +
      "\\s3 Sub-subtitle\\par" +
      "\\pard Body text\\par}";

    expect(parseRtfToTraversedElements(rtf)).toEqual([
      { type: "H1", content: [{ type: "TEXT", font: FontType.PLAIN, text: "Title" }] },
      { type: "H2", content: [{ type: "TEXT", font: FontType.PLAIN, text: "Subtitle" }] },
      { type: "H3", content: [{ type: "TEXT", font: FontType.PLAIN, text: "Sub-subtitle" }] },
      { type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "Body text" }] },
    ]);
  });

  test("detects headings via Norwegian bokmål stylesheet names", () => {
    const rtf =
      `${HEADER}` +
      "{\\stylesheet{\\s1 overskrift 1;}{\\s2 overskrift 2;}}" +
      "\\s1 Tittel\\par" +
      "\\pard Brødtekst\\par}";

    expect(parseRtfToTraversedElements(rtf)).toEqual([
      { type: "H1", content: [{ type: "TEXT", font: FontType.PLAIN, text: "Tittel" }] },
      { type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "Brødtekst" }] },
    ]);
  });

  test("does not treat unrecognized style names as headings", () => {
    const rtf = `${HEADER}{\\stylesheet{\\s1 Custom Style;}}\\s1 Not a heading\\par}`;

    expect(parseRtfToTraversedElements(rtf)).toEqual([
      { type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "Not a heading" }] },
    ]);
  });

  test("parses a bullet list using the old-style \\pntext marker", () => {
    const rtf = `${HEADER}{\\pntext\\'B7\\tab}First item\\par{\\pntext\\'B7\\tab}Second item\\par}`;

    expect(parseRtfToTraversedElements(rtf)).toEqual([
      {
        type: "ITEM",
        content: [{ type: "TEXT", font: FontType.PLAIN, text: "First item" }],
        listType: ListType.PUNKTLISTE,
      },
      {
        type: "ITEM",
        content: [{ type: "TEXT", font: FontType.PLAIN, text: "Second item" }],
        listType: ListType.PUNKTLISTE,
      },
    ]);
  });

  test("parses a bullet list referenced via the modern \\ls paragraph property (defaults to bullet)", () => {
    // Modern Word list export only puts \lsN directly on each paragraph; the actual bullet-vs-
    // numbered distinction lives in \listoverridetable -> \listtable -> \levelnfc, which we
    // intentionally don't resolve (see paste-rtf.ts). \ls alone is treated as "a list item,
    // default to bullet" - the far more common case for pasted letter content.
    const rtf =
      `${HEADER}` +
      "{\\*\\listtable{\\list\\ls1{\\listlevel{\\*\\pn\\pnlvlblt{\\pntxtb\\'B7}}}}}" +
      "\\pard\\ls1 First item\\par" +
      "\\pard\\ls1 Second item\\par}";

    expect(parseRtfToTraversedElements(rtf)).toEqual([
      {
        type: "ITEM",
        content: [{ type: "TEXT", font: FontType.PLAIN, text: "First item" }],
        listType: ListType.PUNKTLISTE,
      },
      {
        type: "ITEM",
        content: [{ type: "TEXT", font: FontType.PLAIN, text: "Second item" }],
        listType: ListType.PUNKTLISTE,
      },
    ]);
  });

  test("parses a numbered list using the old-style \\pntext + \\pndec marker", () => {
    const rtf = `${HEADER}{\\pntext\\pndec 1.\\tab}First item\\par{\\pntext\\pndec 2.\\tab}Second item\\par}`;

    expect(parseRtfToTraversedElements(rtf)).toEqual([
      {
        type: "ITEM",
        content: [{ type: "TEXT", font: FontType.PLAIN, text: "First item" }],
        listType: ListType.NUMMERERT_LISTE,
      },
      {
        type: "ITEM",
        content: [{ type: "TEXT", font: FontType.PLAIN, text: "Second item" }],
        listType: ListType.NUMMERERT_LISTE,
      },
    ]);
  });

  test("does not misread \\pndec occurring only inside the ignorable list-definition table as a list item", () => {
    const rtf = `${HEADER}{\\*\\listtable{\\list\\ls1{\\listlevel{\\*\\pn\\pndec}}}}\\pard Plain paragraph\\par}`;

    expect(parseRtfToTraversedElements(rtf)).toEqual([
      { type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "Plain paragraph" }] },
    ]);
  });

  test("parses a simple table", () => {
    const rtf =
      `${HEADER}` +
      "\\trowd\\cellx1440\\cellx2880\\pard\\intbl A1\\cell\\pard\\intbl B1\\cell\\row" +
      "\\trowd\\cellx1440\\cellx2880\\pard\\intbl A2\\cell\\pard\\intbl B2\\cell\\row}";

    expect(parseRtfToTraversedElements(rtf)).toEqual([
      {
        type: "TABLE",
        rows: [
          {
            cells: [
              { content: [{ type: "TEXT", font: FontType.PLAIN, text: "A1" }] },
              { content: [{ type: "TEXT", font: FontType.PLAIN, text: "B1" }] },
            ],
          },
          {
            cells: [
              { content: [{ type: "TEXT", font: FontType.PLAIN, text: "A2" }] },
              { content: [{ type: "TEXT", font: FontType.PLAIN, text: "B2" }] },
            ],
          },
        ],
      },
    ]);
  });

  test("a real paragraph following a table is not swallowed into the table", () => {
    const rtf = `${HEADER}\\trowd\\cellx1440\\pard\\intbl A1\\cell\\row\\pard Body after the table\\par}`;

    expect(parseRtfToTraversedElements(rtf)).toEqual([
      {
        type: "TABLE",
        rows: [{ cells: [{ content: [{ type: "TEXT", font: FontType.PLAIN, text: "A1" }] }] }],
      },
      { type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "Body after the table" }] },
    ]);
  });

  test("decodes Windows-1252 hex-escaped bytes", () => {
    // 0xE6 0xF8 0xE5 = æøå in cp1252 (same code points as Latin-1 for these bytes).
    const rtf = `${HEADER} \\'e6\\'f8\\'e5\\par}`;

    expect(parseRtfToTraversedElements(rtf)).toEqual([
      { type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "æøå" }] },
    ]);
  });

  test("decodes Windows-1257 (Baltic) hex-escaped bytes", () => {
    // 0xE4 = ä, 0xF6 = ö, 0xF5 = õ in cp1257.
    const rtf = "{\\rtf1\\ansi\\ansicpg1257\\deflang1044 \\'e4\\'f6\\'f5\\par}";

    expect(parseRtfToTraversedElements(rtf)).toEqual([
      { type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "äöõ" }] },
    ]);
  });

  test("decodes \\uN unicode escapes and skips the default single fallback character", () => {
    // \u8364 is the euro sign (U+20AC); the "?" following it is the ANSI fallback character that
    // must be skipped rather than emitted.
    const rtf = `${HEADER} \\u8364?\\par}`;

    expect(parseRtfToTraversedElements(rtf)).toEqual([
      { type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "€" }] },
    ]);
  });

  test("respects \\uc to skip multiple fallback characters", () => {
    const rtf = `${HEADER} \\uc2\\u8364??\\par}`;

    expect(parseRtfToTraversedElements(rtf)).toEqual([
      { type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "€" }] },
    ]);
  });

  test("ignores content inside ignorable destinations (font table, generator, etc)", () => {
    const rtf =
      "{\\rtf1\\ansi\\ansicpg1252\\deflang1033" +
      "{\\fonttbl{\\f0 Times New Roman;}}" +
      "{\\*\\generator Microsoft Word;}" +
      "{\\info{\\author Someone}}" +
      " Real content\\par}";

    expect(parseRtfToTraversedElements(rtf)).toEqual([
      { type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "Real content" }] },
    ]);
  });

  test("ignores unsupported destinations wrapped generically with \\*", () => {
    const rtf = `${HEADER}{\\*\\unknownfutureDestination should not appear} Visible text\\par}`;

    expect(parseRtfToTraversedElements(rtf)).toEqual([
      { type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "Visible text" }] },
    ]);
  });

  test("returns an empty array for an empty document", () => {
    expect(parseRtfToTraversedElements(`${HEADER}}`)).toEqual([]);
  });
});

describe("detectRtfDocumentLanguage", () => {
  test("reads \\deflang and maps known LCIDs to a friendly label", () => {
    expect(detectRtfDocumentLanguage(`${HEADER} text\\par}`)).toBe("en-US");
  });

  test("maps Norwegian bokmål LCID", () => {
    expect(detectRtfDocumentLanguage("{\\rtf1\\ansi\\deflang1044 text}")).toBe("nb-NO");
  });

  test("maps Norwegian nynorsk LCID", () => {
    expect(detectRtfDocumentLanguage("{\\rtf1\\ansi\\deflang2068 text}")).toBe("nn-NO");
  });

  test("falls back to \\lang when \\deflang is absent", () => {
    expect(detectRtfDocumentLanguage("{\\rtf1\\ansi\\lang1033 text}")).toBe("en-US");
  });

  test("labels unknown LCIDs rather than silently dropping them", () => {
    expect(detectRtfDocumentLanguage("{\\rtf1\\ansi\\deflang9999 text}")).toBe("unknown (9999)");
  });

  test("returns undefined when no language control word is present", () => {
    expect(detectRtfDocumentLanguage("{\\rtf1\\ansi text}")).toBeUndefined();
  });
});
