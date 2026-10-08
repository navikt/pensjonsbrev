import { describe, expect, test } from "vitest";

import { interpretNativeRtf } from "~/Brevredigering/LetterEditor/actions/rtf/interpretNativeRtf";
import { createByteDecoder } from "~/Brevredigering/LetterEditor/actions/rtf/rtfDecoding";
import { tokenizeRtf } from "~/Brevredigering/LetterEditor/actions/rtf/tokenizeRtf";
import { FontType, ListType } from "~/types/brevbakerTypes";

// Real Word/TextEdit exports have a much larger preamble; see interpretNativeRtf.fixtures.test.ts.
const HEADER = "{\\rtf1\\ansi\\ansicpg1252\\deflang1033";

const interpret = (rtf: string) => {
  const tokens = tokenizeRtf(rtf);
  return interpretNativeRtf(tokens, createByteDecoder(tokens));
};

const plain = (text: string) => ({ type: "TEXT", font: FontType.PLAIN, text });
const paragraph = (text: string) => ({ type: "P", content: [plain(text)] });
const EMPTY_PARAGRAPH = paragraph("");

describe("interpretNativeRtf", () => {
  test("parses plain paragraph text", () => {
    const rtf = `${HEADER} Hello world\\par}`;

    expect(interpret(rtf)).toEqual([
      { type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "Hello world" }] },
    ]);
  });

  test("parses multiple paragraphs", () => {
    const rtf = `${HEADER} First\\par Second\\par}`;

    expect(interpret(rtf)).toEqual([
      { type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "First" }] },
      { type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "Second" }] },
    ]);
  });

  test("parses bold text", () => {
    const rtf = `${HEADER} Before \\b bold\\b0  after\\par}`;

    expect(interpret(rtf)).toEqual([
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

    expect(interpret(rtf)).toEqual([
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

    expect(interpret(rtf)).toEqual([{ type: "P", content: [{ type: "TEXT", font: FontType.BOLD, text: "both" }] }]);
  });

  test("detects headings via English Word stylesheet names", () => {
    const rtf =
      `${HEADER}` +
      "{\\stylesheet{\\s1 heading 1;}{\\s2 heading 2;}{\\s3 heading 3;}}" +
      "\\s1 Title\\par" +
      "\\s2 Subtitle\\par" +
      "\\s3 Sub-subtitle\\par" +
      "\\pard Body text\\par}";

    expect(interpret(rtf)).toEqual([
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

    expect(interpret(rtf)).toEqual([
      { type: "H1", content: [{ type: "TEXT", font: FontType.PLAIN, text: "Tittel" }] },
      { type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "Brødtekst" }] },
    ]);
  });

  test("does not treat unrecognized style names as headings", () => {
    const rtf = `${HEADER}{\\stylesheet{\\s1 Custom Style;}}\\s1 Not a heading\\par}`;

    expect(interpret(rtf)).toEqual([
      { type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "Not a heading" }] },
    ]);
  });

  test("parses a bullet list using the old-style \\pntext marker", () => {
    const rtf = `${HEADER}{\\pntext\\'B7\\tab}First item\\par{\\pntext\\'B7\\tab}Second item\\par}`;

    expect(interpret(rtf)).toEqual([
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
    // intentionally don't resolve (see interpretNativeRtf.ts). \ls alone is treated as "a list item,
    // default to bullet" - the far more common case for pasted letter content.
    const rtf =
      `${HEADER}` +
      "{\\*\\listtable{\\list\\ls1{\\listlevel{\\*\\pn\\pnlvlblt{\\pntxtb\\'B7}}}}}" +
      "\\pard\\ls1 First item\\par" +
      "\\pard\\ls1 Second item\\par}";

    expect(interpret(rtf)).toEqual([
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

    expect(interpret(rtf)).toEqual([
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

    expect(interpret(rtf)).toEqual([
      { type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "Plain paragraph" }] },
    ]);
  });

  test("parses a simple table", () => {
    const rtf =
      `${HEADER}` +
      "\\trowd\\cellx1440\\cellx2880\\pard\\intbl A1\\cell\\pard\\intbl B1\\cell\\row" +
      "\\trowd\\cellx1440\\cellx2880\\pard\\intbl A2\\cell\\pard\\intbl B2\\cell\\row}";

    expect(interpret(rtf)).toEqual([
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

    expect(interpret(rtf)).toEqual([
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

    expect(interpret(rtf)).toEqual([{ type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "æøå" }] }]);
  });

  test("decodes Windows-1257 (Baltic) hex-escaped bytes", () => {
    // 0xE4 = ä, 0xF6 = ö, 0xF5 = õ in cp1257.
    const rtf = "{\\rtf1\\ansi\\ansicpg1257\\deflang1044 \\'e4\\'f6\\'f5\\par}";

    expect(interpret(rtf)).toEqual([{ type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "äöõ" }] }]);
  });

  test("decodes double-byte code pages (Shift JIS) with the byte pairs kept together", () => {
    const rtf = "{\\rtf1\\ansi\\ansicpg932 \\pard \\'93\\'fa\\'96\\'7b\\par}";

    expect(interpret(rtf)).toEqual([{ type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "日本" }] }]);
  });

  test("skips double-byte fallback bytes after \\uN with \\uc2", () => {
    // 日 as a Shift JIS byte pair, then 本 (U+672C) as \u with its Shift JIS bytes as a two-unit fallback.
    const rtf = "{\\rtf1\\ansi\\ansicpg932 \\pard \\'93\\'fa\\uc2\\u26412\\'96\\'7b\\par}";

    expect(interpret(rtf)).toEqual([{ type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "日本" }] }]);
  });

  test("decodes ISO 8859 code pages", () => {
    // 0xB1 = ą, 0xBE = ž in ISO-8859-2.
    const rtf = "{\\rtf1\\ansi\\ansicpg28592 \\pard \\'b1\\'be\\par}";

    expect(interpret(rtf)).toEqual([{ type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "ąž" }] }]);
  });

  test("only reads \\ansicpg from the header, not from escaped body text", () => {
    const rtf = "{\\rtf1\\ansi Tekst \\\\ansicpg1251 \\'e6\\par}";

    expect(interpret(rtf)).toEqual([
      { type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "Tekst \\ansicpg1251 æ" }] },
    ]);
  });

  test("decodes \\uN unicode escapes and skips the default single fallback character", () => {
    // \u8364 is the euro sign (U+20AC); the "?" following it is the ANSI fallback character that
    // must be skipped rather than emitted.
    const rtf = `${HEADER} \\u8364?\\par}`;

    expect(interpret(rtf)).toEqual([{ type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "€" }] }]);
  });

  test("respects \\uc to skip multiple fallback characters", () => {
    const rtf = `${HEADER} \\uc2\\u8364??\\par}`;

    expect(interpret(rtf)).toEqual([{ type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "€" }] }]);
  });

  test("ignores content inside ignorable destinations (font table, generator, etc)", () => {
    const rtf =
      "{\\rtf1\\ansi\\ansicpg1252\\deflang1033" +
      "{\\fonttbl{\\f0 Times New Roman;}}" +
      "{\\*\\generator Microsoft Word;}" +
      "{\\info{\\author Someone}}" +
      " Real content\\par}";

    expect(interpret(rtf)).toEqual([
      { type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "Real content" }] },
    ]);
  });

  test("ignores unsupported destinations wrapped generically with \\*", () => {
    const rtf = `${HEADER}{\\*\\unknownfutureDestination should not appear} Visible text\\par}`;

    expect(interpret(rtf)).toEqual([
      { type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "Visible text" }] },
    ]);
  });

  test("returns an empty array for an empty document", () => {
    expect(interpret(`${HEADER}}`)).toEqual([]);
  });

  test("returns an unterminated plain paragraph as inline text", () => {
    expect(interpret(`${HEADER} a \\b partial\\b0  selection}`)).toEqual([
      plain("a "),
      { type: "TEXT", font: FontType.BOLD, text: "partial" },
      plain(" selection"),
    ]);
  });

  test("keeps the edge spaces of inline text", () => {
    expect(interpret(`${HEADER}\\b  ikke }`)).toEqual([{ type: "TEXT", font: FontType.BOLD, text: " ikke " }]);
  });

  test("collapses runs of spaces and tabs into one space", () => {
    expect(interpret(`${HEADER}\\pard Hei  \\tab  der\\par}`)).toEqual([paragraph("Hei der")]);
  });

  test("reads a document truncated inside an open group", () => {
    expect(interpret(`${HEADER}\\pard Hei {\\b der`)).toEqual([
      plain("Hei "),
      { type: "TEXT", font: FontType.BOLD, text: "der" },
    ]);
  });

  test("reads input that is not RTF as plain text without throwing", () => {
    expect(interpret("hello {world")).toEqual([plain("hello world")]);
  });
});

describe("interpretNativeRtf - paragraph properties", () => {
  test("heading style does not leak into the next paragraph after \\pard", () => {
    const rtf = `${HEADER}{\\stylesheet{\\s1 heading 1;}}\\pard\\plain\\s1 {Heading\\par}\\pard\\plain {Body\\par}}`;

    expect(interpret(rtf)).toEqual([{ type: "H1", content: [plain("Heading")] }, paragraph("Body")]);
  });

  test("detects headings via \\outlinelevel on the paragraph, regardless of style name", () => {
    const rtf = `${HEADER}{\\stylesheet{\\s5 Min stil;}}\\pard\\s5\\outlinelevel1 Kapittel\\par\\pard Tekst\\par}`;

    expect(interpret(rtf)).toEqual([{ type: "H2", content: [plain("Kapittel")] }, paragraph("Tekst")]);
  });

  test("detects headings via \\outlinelevel in the stylesheet entry", () => {
    const rtf = `${HEADER}{\\stylesheet{\\s1\\outlinelevel0 \\sbasedon0 Egendefinert;}}\\pard\\s1 Tittel\\par}`;

    expect(interpret(rtf)).toEqual([{ type: "H1", content: [plain("Tittel")] }]);
  });

  test("outline levels below 3 are plain paragraphs", () => {
    expect(interpret(`${HEADER}\\pard\\outlinelevel3 Level four\\par}`)).toEqual([paragraph("Level four")]);
  });

  test("ignores character styles in the stylesheet", () => {
    const rtf = `${HEADER}{\\stylesheet{\\*\\cs1 heading 1;}}\\pard\\s1 Not a heading\\par}`;

    expect(interpret(rtf)).toEqual([paragraph("Not a heading")]);
  });

  test("\\ls persists across \\par until the next \\pard", () => {
    const rtf = `${HEADER}\\pard\\ls1 {One\\par Two\\par}\\pard After\\par}`;

    expect(interpret(rtf)).toEqual([
      { type: "ITEM", content: [plain("One")], listType: ListType.PUNKTLISTE },
      { type: "ITEM", content: [plain("Two")], listType: ListType.PUNKTLISTE },
      paragraph("After"),
    ]);
  });

  test("keeps blank lines as empty paragraphs, but trims them at the edges", () => {
    const rtf = `${HEADER}\\pard\\par First\\par\\par Second\\par\\par}`;

    expect(interpret(rtf)).toEqual([paragraph("First"), EMPTY_PARAGRAPH, paragraph("Second")]);
  });

  test.each(["sect", "page"])("\\%s ends the paragraph", (word) => {
    expect(interpret(`${HEADER}\\pard En\\${word} To\\par}`)).toEqual([paragraph("En"), paragraph("To")]);
  });

  test.each([
    ["\\par\\sect", "par then sect"],
    ["\\par\\page", "par then page"],
    ["\\page\\par", "page then par"],
    ["\\sect\\par", "sect then par"],
    ["\\page\\sect\\par", "page, sect and par"],
  ])("%s is a single paragraph break (%s)", (breaks) => {
    expect(interpret(`${HEADER}\\pard En${breaks} To\\par}`)).toEqual([paragraph("En"), paragraph("To")]);
  });

  test("a blank line before a page break is kept", () => {
    expect(interpret(`${HEADER}\\pard En\\par\\par\\page To\\par}`)).toEqual([
      paragraph("En"),
      EMPTY_PARAGRAPH,
      paragraph("To"),
    ]);
  });

  test("\\sect ends a list item, and the next item keeps its marker", () => {
    const rtf = `${HEADER}{\\listtext 1.\\tab}\\pard\\ls1 {Ett\\sect }\\sectd {\\listtext 2.\\tab}\\pard\\ls1 To\\par}`;

    expect(interpret(rtf)).toEqual([
      { type: "ITEM", content: [plain("Ett")], listType: ListType.NUMMERERT_LISTE },
      { type: "ITEM", content: [plain("To")], listType: ListType.NUMMERERT_LISTE },
    ]);
  });

  test("\\page inside a table cell is a space", () => {
    const rtf = `${HEADER}\\trowd\\cellx4000\\pard\\intbl En\\page To\\cell\\row\\pard Etter\\par}`;

    expect(interpret(rtf)).toEqual([
      { type: "TABLE", rows: [{ cells: [{ content: [plain("En To")] }] }] },
      paragraph("Etter"),
    ]);
  });
});

describe("interpretNativeRtf - lists", () => {
  test("{\\listtext} marks a list item and its formatting does not leak into the item", () => {
    const rtf =
      `${HEADER}{\\stylesheet{\\s1 heading 1;}}` +
      "{\\listtext\\pard\\plain\\s1\\b\\f3 \\'b7\\tab}\\pard\\ls1 Item\\par}";

    expect(interpret(rtf)).toEqual([{ type: "ITEM", content: [plain("Item")], listType: ListType.PUNKTLISTE }]);
  });

  test.each([
    ["1.", ListType.NUMMERERT_LISTE],
    ["12)", ListType.NUMMERERT_LISTE],
    ["1.2.", ListType.NUMMERERT_LISTE],
    ["a.", ListType.NUMMERERT_LISTE],
    ["(iv)", ListType.NUMMERERT_LISTE],
    ["IV.", ListType.NUMMERERT_LISTE],
    ["\\'b7", ListType.PUNKTLISTE],
    ["o", ListType.PUNKTLISTE],
    ["\\'a7", ListType.PUNKTLISTE],
    ["\\endash", ListType.PUNKTLISTE],
  ])("classifies list marker %s", (marker, listType) => {
    const rtf = `${HEADER}{\\listtext ${marker}\\tab}\\pard\\ls1 Item\\par}`;

    expect(interpret(rtf)).toEqual([{ type: "ITEM", content: [plain("Item")], listType }]);
  });

  test("maps text in symbol fonts, in list markers and in the item", () => {
    const rtf =
      `${HEADER}{\\fonttbl{\\f0 Calibri;}{\\f3\\fcharset2 Symbol;}}` +
      "{\\listtext\\pard\\plain\\f3 \\'b7\\tab}\\pard\\ls1 Alder {\\f3 \\'b3} 67\\par}";

    expect(interpret(rtf)).toEqual([{ type: "ITEM", content: [plain("Alder ≥ 67")], listType: ListType.PUNKTLISTE }]);
  });

  test("reads the list type from {\\*\\pn} when there is no marker text", () => {
    const rtf = `${HEADER}\\pard{\\*\\pn\\pnlvlbody\\pndec{\\pntxta .}} Item\\par}`;

    expect(interpret(rtf)).toEqual([{ type: "ITEM", content: [plain("Item")], listType: ListType.NUMMERERT_LISTE }]);
  });

  test("ignores list definitions in {\\*\\pnseclvl}", () => {
    const rtf = `${HEADER}{\\*\\pnseclvl1\\pnucrm\\pnstart1{\\pntxta .}}{\\*\\pnseclvl2\\pnlvlblt}\\pard Tekst\\par}`;

    expect(interpret(rtf)).toEqual([paragraph("Tekst")]);
  });

  test("keeps the number of a numbered heading as text", () => {
    const rtf = `${HEADER}{\\listtext 3\\tab}\\pard\\ls2\\outlinelevel0 Innledning\\par}`;

    expect(interpret(rtf)).toEqual([{ type: "H1", content: [plain("3 Innledning")] }]);
  });
});

describe("interpretNativeRtf - list tables", () => {
  const LIST_TABLES =
    "{\\*\\listtable" +
    "{\\list{\\listlevel\\levelnfc0}{\\listlevel\\levelnfc23}\\listid100}" +
    "{\\list{\\listlevel\\levelnfc23}{\\listlevel\\levelnfc4}\\listid200}" +
    "{\\list{\\listlevel\\levelnfc255}\\listid300}}" +
    "{\\*\\listoverridetable" +
    "{\\listoverride\\listid100\\listoverridecount0\\ls1}" +
    "{\\listoverride\\listid200\\listoverridecount0\\ls2}" +
    "{\\listoverride\\listid300\\listoverridecount0\\ls3}}";
  const withLists = (body: string) => `${HEADER}${LIST_TABLES}${body}}`;
  const item = (text: string, listType: ListType, nested?: boolean) => ({
    type: "ITEM",
    content: [plain(text)],
    listType,
    ...(nested ? { nested } : {}),
  });

  test("reads the list type from the list table when there is no marker text", () => {
    expect(interpret(withLists("\\pard\\ls1 En\\par\\pard\\ls2 To\\par"))).toEqual([
      item("En", ListType.NUMMERERT_LISTE),
      item("To", ListType.PUNKTLISTE),
    ]);
  });

  test("reads the list type of the paragraph's \\ilvl", () => {
    expect(interpret(withLists("\\pard\\ls1\\ilvl1 En\\par\\pard\\ls2\\ilvl1 To\\par"))).toEqual([
      item("En", ListType.PUNKTLISTE, true),
      item("To", ListType.NUMMERERT_LISTE, true),
    ]);
  });

  test("the list table wins over marker text and \\pn", () => {
    const rtf = withLists("{\\listtext 1.\\tab}\\pard\\ls2 En\\par{\\*\\pn\\pnlvlblt}\\pard\\ls1 To\\par");

    expect(interpret(rtf)).toEqual([item("En", ListType.PUNKTLISTE), item("To", ListType.NUMMERERT_LISTE)]);
  });

  test("a level without number (\\levelnfc255) is a plain paragraph", () => {
    expect(interpret(withLists("{\\listtext\\tab}\\pard\\ls3 Innrykket\\par"))).toEqual([paragraph("Innrykket")]);
  });

  // Modelled on Apache Tika's testRTFListOverride: the type comes from the list that the override points to.
  test("follows \\ls through the override table to the list", () => {
    const rtf =
      `${HEADER}{\\*\\listtable{\\list{\\listlevel\\levelnfc23}\\listid7}{\\list{\\listlevel\\levelnfc0}\\listid8}}` +
      "{\\*\\listoverridetable{\\listoverride\\listid8\\ls1}{\\listoverride\\listid7\\ls2}}" +
      "\\pard\\ls1 En\\par\\pard\\ls2 To\\par}";

    expect(interpret(rtf)).toEqual([item("En", ListType.NUMMERERT_LISTE), item("To", ListType.PUNKTLISTE)]);
  });

  // Modelled on Apache Tika's testRTFCorruptListOverride: a broken list table falls back to the marker text.
  test.each([
    ["an override to a missing list", "{\\*\\listoverridetable{\\listoverride\\listid999\\ls1}}"],
    ["a missing override", "{\\*\\listtable{\\list{\\listlevel\\levelnfc23}\\listid1}}"],
    [
      "a level that is not defined",
      "{\\*\\listtable{\\list\\listid1}}{\\*\\listoverridetable{\\listoverride\\listid1\\ls1}}",
    ],
  ])("falls back to the marker text with %s", (_, tables) => {
    const rtf = `${HEADER}${tables}{\\listtext 1.\\tab}\\pard\\ls1 En\\par}`;

    expect(interpret(rtf)).toEqual([item("En", ListType.NUMMERERT_LISTE)]);
  });

  test("\\pard resets \\ilvl", () => {
    expect(interpret(withLists("\\pard\\ls1\\ilvl1 En\\par\\pard\\ls1 To\\par"))).toEqual([
      item("En", ListType.PUNKTLISTE, true),
      item("To", ListType.NUMMERERT_LISTE),
    ]);
  });

  test("marks items below the top level as nested, also without list tables", () => {
    const rtf = `${HEADER}{\\listtext\\'b7\\tab}\\pard\\ls1 En\\par{\\listtext o\\tab}\\pard\\ls1\\ilvl1 Under\\par}`;

    expect(interpret(rtf)).toEqual([item("En", ListType.PUNKTLISTE), item("Under", ListType.PUNKTLISTE, true)]);
  });

  test.each([
    ["\\pnlvlbody\\pndec", false],
    ["\\pnlvl1\\pndec", false],
    ["\\pnlvl2\\pndec", true],
  ])("Word 95 list with {\\*\\pn%s} is nested: %s", (pn, nested) => {
    const rtf = `${HEADER}\\pard{\\*\\pn${pn}{\\pntxta .}} Item\\par}`;

    expect(interpret(rtf)).toEqual([item("Item", ListType.NUMMERERT_LISTE, nested)]);
  });
});

describe("interpretNativeRtf - character formatting", () => {
  test("\\plain resets bold and italic", () => {
    expect(interpret(`${HEADER}\\pard\\b\\i Fet \\plain vanlig\\par}`)).toEqual([
      {
        type: "P",
        content: [{ type: "TEXT", font: FontType.BOLD, text: "Fet " }, plain("vanlig")],
      },
    ]);
  });

  test("drops hidden (\\v) and tracked-deleted (\\deleted) text", () => {
    const rtf = `${HEADER}\\pard Synlig{\\v  skjult}{\\deleted  slettet} tekst\\par}`;

    expect(interpret(rtf)).toEqual([paragraph("Synlig tekst")]);
  });

  test.each([
    ["\\lquote a\\rquote", "\u2018a\u2019"],
    ["\\ldblquote a\\rdblquote", "\u201Ca\u201D"],
    ["a\\endash b\\emdash c", "a\u2013b\u2014c"],
    ["a\\line b", "a b"],
    ["a\\tab b", "a b"],
    ["\\bullet  a", "\u2022 a"],
    ["a\\~b", "a b"],
    ["opt\\-ional", "optional"],
    ["non\\_breaking", "non-breaking"],
  ])("converts %s", (source, expected) => {
    expect(interpret(`${HEADER}\\pard ${source}\\par}`)).toEqual([paragraph(expected)]);
  });

  test("skips footnotes, annotations, field instructions and \\nonesttables", () => {
    const rtf =
      `${HEADER}\\pard Tekst{\\footnote\\pard fotnote\\par}` +
      "{\\*\\atnid NN}{\\*\\annotation kommentar}" +
      '{\\field{\\*\\fldinst HYPERLINK "https://nav.no"}{\\fldrslt  lenke}}' +
      "{\\nonesttables duplikat}\\par}";

    expect(interpret(rtf)).toEqual([paragraph("Tekst lenke")]);
  });

  test("the \\u fallback skip counts \\'hh and control words, and stops at a group boundary", () => {
    expect(interpret(`${HEADER}\\pard\\uc2 \\u8364\\'80\\'80 a\\u8364{}b\\par}`)).toEqual([paragraph("€ a€b")]);
  });

  test("decodes \\uN escapes with negative parameters", () => {
    expect(interpret(`${HEADER}\\pard \\u-4064?\\par}`)).toEqual([paragraph("\uF020")]);
  });

  test("falls back to Windows-1252 for unknown codepages", () => {
    expect(interpret("{\\rtf1\\ansi\\ansicpg99999 \\'e6\\'80\\par}")).toEqual([paragraph("æ€")]);
  });

  test("skips \\bin data", () => {
    expect(interpret(`${HEADER}\\pard a\\bin3 x}yb\\par}`)).toEqual([paragraph("ab")]);
  });
});

describe("interpretNativeRtf - tables", () => {
  const cell = (text: string) => ({ content: text ? [plain(text)] : [] });

  test("drops the continuation cells of a horizontal merge (\\clmgf, \\clmrg)", () => {
    const rtf =
      `${HEADER}\\trowd\\clmgf\\cellx2000\\clmrg\\cellx4000\\clmrg\\cellx6000\\cellx8000` +
      "\\pard\\intbl Bred\\cell\\cell\\cell Smal\\cell\\row" +
      "\\trowd\\cellx2000\\cellx4000\\cellx6000\\cellx8000\\pard\\intbl A\\cell B\\cell C\\cell D\\cell\\row\\pard\\par}";

    expect(interpret(rtf)).toEqual([
      {
        type: "TABLE",
        rows: [{ cells: [cell("Bred"), cell("Smal")] }, { cells: [cell("A"), cell("B"), cell("C"), cell("D")] }],
      },
    ]);
  });

  test("keeps the cells of a vertical merge (\\clvmgf, \\clvmrg) as empty cells", () => {
    const rtf =
      `${HEADER}\\trowd\\clvmgf\\cellx4000\\cellx8000\\pard\\intbl Høy\\cell A\\cell\\row` +
      "\\trowd\\clvmrg\\cellx4000\\cellx8000\\pard\\intbl \\cell B\\cell\\row\\pard\\par}";

    expect(interpret(rtf)).toEqual([
      { type: "TABLE", rows: [{ cells: [cell("Høy"), cell("A")] }, { cells: [cell(""), cell("B")] }] },
    ]);
  });

  test("uses the cell definitions Word repeats after the cells", () => {
    const definition = "\\trowd\\clmgf\\cellx4000\\clmrg\\cellx8000";
    const rtf = `${HEADER}${definition}\\pard\\intbl {Bred\\cell}{\\cell}\\pard\\intbl {${definition}\\row}\\pard\\par}`;

    expect(interpret(rtf)).toEqual([{ type: "TABLE", rows: [{ cells: [cell("Bred")] }] }]);
  });

  test("merges in the header row are dropped too", () => {
    const rtf =
      `${HEADER}\\trowd\\trhdr\\clmgf\\cellx4000\\clmrg\\cellx8000\\pard\\intbl Overskrift\\cell\\cell\\row` +
      "\\trowd\\cellx4000\\cellx8000\\pard\\intbl A\\cell B\\cell\\row\\pard\\par}";

    expect(interpret(rtf)).toEqual([
      { type: "TABLE", headerCells: [cell("Overskrift")], rows: [{ cells: [cell("A"), cell("B")] }] },
    ]);
  });
  test("keeps cells when Word repeats \\trowd before \\row", () => {
    const rowDefinition = "\\trowd\\irow0\\trgaph108\\cellx4000\\cellx8000";
    const rtf =
      `${HEADER}${rowDefinition}` +
      "\\pard\\intbl {A1\\cell}\\pard\\intbl {B1\\cell}" +
      `\\pard\\intbl {${rowDefinition}\\row}` +
      "\\pard After\\par}";

    expect(interpret(rtf)).toEqual([
      { type: "TABLE", rows: [{ cells: [{ content: [plain("A1")] }, { content: [plain("B1")] }] }] },
      paragraph("After"),
    ]);
  });

  test("uses a first row marked \\trhdr as header cells", () => {
    const rtf =
      `${HEADER}\\trowd\\trhdr\\cellx4000\\pard\\intbl {\\b Periode\\cell}{\\trowd\\trhdr\\cellx4000\\row}` +
      "\\trowd\\cellx4000\\pard\\intbl {2026\\cell}{\\trowd\\cellx4000\\row}\\pard\\par}";

    expect(interpret(rtf)).toEqual([
      {
        type: "TABLE",
        headerCells: [{ content: [{ type: "TEXT", font: FontType.BOLD, text: "Periode" }] }],
        rows: [{ cells: [{ content: [plain("2026")] }] }],
      },
    ]);
  });

  test("joins paragraphs in a cell with a space and ignores list markers there", () => {
    const rtf = `${HEADER}\\trowd\\cellx4000\\pard\\intbl {\\listtext 1.\\tab}First\\par Second\\cell\\row}`;

    expect(interpret(rtf)).toEqual([{ type: "TABLE", rows: [{ cells: [{ content: [plain("First Second")] }] }] }]);
  });

  test("flattens nested tables into the parent cell", () => {
    const rtf =
      `${HEADER}\\trowd\\cellx8000\\pard\\intbl Outer ` +
      "\\pard\\intbl\\itap2 inner1\\nestcell inner2\\nestcell{\\*\\nesttableprops\\trowd\\nestrow}" +
      "{\\nonesttables inner1 inner2}\\pard\\intbl\\itap1 \\cell\\row}";

    expect(interpret(rtf)).toEqual([
      { type: "TABLE", rows: [{ cells: [{ content: [plain("Outer inner1 inner2")] }] }] },
    ]);
  });

  test("starts a new table after a paragraph between two tables", () => {
    const table = (text: string) => `\\trowd\\cellx4000\\pard\\intbl ${text}\\cell\\row`;
    const rtf = `${HEADER}${table("A")}\\pard Mellom\\par${table("B")}}`;

    expect(interpret(rtf)).toEqual([
      { type: "TABLE", rows: [{ cells: [{ content: [plain("A")] }] }] },
      paragraph("Mellom"),
      { type: "TABLE", rows: [{ cells: [{ content: [plain("B")] }] }] },
    ]);
  });

  test("keeps an unterminated last row", () => {
    expect(interpret(`${HEADER}\\trowd\\cellx4000\\pard\\intbl A\\cell\\pard\\intbl B}`)).toEqual([
      { type: "TABLE", rows: [{ cells: [{ content: [plain("A")] }, { content: [plain("B")] }] }] },
    ]);
  });

  test("keeps rows without \\intbl in one table, also across whitespace between rows", () => {
    const row = (a: string, b: string) => `\\trowd\\cellx2000\\cellx4000 ${a}\\cell ${b}\\cell\\row`;
    const rtf = `${HEADER}${row("A", "B")}\r\n  ${row("C", "D")}\\pard Etter\\par}`;

    expect(interpret(rtf)).toEqual([
      {
        type: "TABLE",
        rows: [
          { cells: [{ content: [plain("A")] }, { content: [plain("B")] }] },
          { cells: [{ content: [plain("C")] }, { content: [plain("D")] }] },
        ],
      },
      paragraph("Etter"),
    ]);
  });

  test("keeps an unterminated last row without \\intbl", () => {
    expect(interpret(`${HEADER}\\trowd\\cellx2000\\cellx4000 A\\cell B}`)).toEqual([
      { type: "TABLE", rows: [{ cells: [{ content: [plain("A")] }, { content: [plain("B")] }] }] },
    ]);
  });
});

describe("interpretNativeRtf - structure and formatting", () => {
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

  test("Word table: cell paragraphs marked with \\intbl and row properties repeated before \\row", () => {
    const rowProperties = "\\trowd\\trgaph108\\cellx2000\\cellx4000";
    const rtf =
      "{\\rtf1\\ansi" +
      `${rowProperties}\\pard\\intbl A\\cell\\pard\\intbl B\\cell{${rowProperties}\\row}` +
      `\\pard\\intbl C\\cell\\pard\\intbl D\\cell{${rowProperties}\\row}` +
      "\\pard Etter\\par}";
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
      { type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "Etter" }] },
    ]);
  });

  test("joins the paragraphs of a multi-paragraph table cell with a space", () => {
    const rtf = "{\\rtf1\\ansi\\pard\\intbl Linje 1\\par Linje 2\\cell\\row}";
    expect(interpret(rtf)).toEqual([
      {
        type: "TABLE",
        rows: [{ cells: [{ content: [{ type: "TEXT", font: FontType.PLAIN, text: "Linje 1 Linje 2" }] }] }],
      },
    ]);
  });

  test("tabs and line breaks become spaces", () => {
    const rtf = "{\\rtf1\\ansi\\pard a\\tab b\\line c\\par}";
    expect(interpret(rtf)).toEqual([{ type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "a b c" }] }]);
  });

  test("an empty paragraph or list item at the end is trimmed", () => {
    const rtf = "{\\rtf1\\ansi\\pard First\\par\\pard\\par\\pard\\ls1{\\listtext\\'b7\\tab}\\par}";
    expect(interpret(rtf)).toEqual([{ type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "First" }] }]);
  });

  test("an empty list item in the middle of a list is kept as an empty item", () => {
    const rtf =
      "{\\rtf1\\ansi\\pard\\ls1{\\listtext\\'b7\\tab}a\\par" +
      "\\pard\\ls1{\\listtext\\'b7\\tab}\\par" +
      "\\pard\\ls1{\\listtext\\'b7\\tab}b\\par}";
    expect(interpret(rtf)).toEqual([
      { type: "ITEM", content: [{ type: "TEXT", font: FontType.PLAIN, text: "a" }], listType: ListType.PUNKTLISTE },
      { type: "ITEM", content: [{ type: "TEXT", font: FontType.PLAIN, text: "" }], listType: ListType.PUNKTLISTE },
      { type: "ITEM", content: [{ type: "TEXT", font: FontType.PLAIN, text: "b" }], listType: ListType.PUNKTLISTE },
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

  // Like the HTML path and Word: text after the last paragraph mark is inline, not a paragraph of its own.
  test("returns a trailing paragraph without a final \\par as inline text", () => {
    const rtf = "{\\rtf1\\ansi\\pard First\\par\\pard No trailing par}";
    expect(interpret(rtf)).toEqual([
      { type: "P", content: [{ type: "TEXT", font: FontType.PLAIN, text: "First" }] },
      { type: "TEXT", font: FontType.PLAIN, text: "No trailing par" },
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
