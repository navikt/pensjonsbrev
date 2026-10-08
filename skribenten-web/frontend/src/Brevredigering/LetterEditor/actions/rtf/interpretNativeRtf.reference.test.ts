/**
 * Parser tests against the test files of other open source RTF parsers (test/fixtures/rtf/<source>/).
 * Files are verbatim copies unless noted. The expectations are Skribenten's supported subset, projected to text
 * (`projectElements`); the HTML the other parsers produce is not used as an oracle.
 *
 * rtf2xml/ — paulhtremblay/rtf2xml at commit fc3f36ba081e642926f89f7535ec48f3490d40be, licensed MIT:
 * https://github.com/paulhtremblay/rtf2xml/blob/fc3f36ba081e642926f89f7535ec48f3490d40be/LICENSE
 * Each <name>.rtf is a copy of
 * https://github.com/paulhtremblay/rtf2xml/blob/fc3f36ba081e642926f89f7535ec48f3490d40be/test/test_files/good/<name>.rtf
 * - bullet_list, char_upper_ranges, char_upper_ranges_hex, complex_list_bullet, complex_list_diff_styles,
 *   escaped_text, fields_simple_format_switch, heading_with_section, headings_mixed, hyperlink,
 *   inline_over_para, italics_plain, list_in_table, list_with_indented_items, lists_with_breaks,
 *   nested_lists_indents, sections_next_page, simple_page_break1, simple_page_break2, symbol,
 *   table_different_cell_widths, table_empty_row, table_simple, table_with_header, zapf_dingbats (Word)
 * - open_office_hello_world, open_office_special_char (OpenOffice)
 * - os_x_text_edit_faces_mixed, os_x_text_edit_italics_mixed (TextEdit on macOS)
 *
 * rtfpipe/ — erdomke/RtfPipe at commit 9851ab97d693037073d567d74346184ae2f3d4b3, licensed MIT:
 * https://github.com/erdomke/RtfPipe/blob/9851ab97d693037073d567d74346184ae2f3d4b3/LICENSE
 * - Headings, List, ListGoogle, ListWord, merged_cells: copies of
 *   https://github.com/erdomke/RtfPipe/blob/9851ab97d693037073d567d74346184ae2f3d4b3/RtfPipe.Tests/Files/<name>.rtf
 * - nested_lists_indents_word, outline_list: copies of
 *   https://github.com/erdomke/RtfPipe/blob/9851ab97d693037073d567d74346184ae2f3d4b3/RtfPipe.Tests/Files/rtf2xml/<name>.rtf
 *
 * rtfjs/ — tbluemel/rtf.js at commit 85fddf55b2f262bfd450769120c18c9ccef1f21c, licensed MIT:
 * https://github.com/tbluemel/rtf.js/blob/85fddf55b2f262bfd450769120c18c9ccef1f21c/LICENSE
 * - hyperlink, implicit-par, pile-of-poo: <name>.rtf is a copy of
 *   https://github.com/tbluemel/rtf.js/blob/85fddf55b2f262bfd450769120c18c9ccef1f21c/test/rtf-test-files/<name>/source.rtf
 * - sample, simple5 and wmf-and-emf-excerpt (an excerpt, not verbatim): see interpretNativeRtf.fixtures.test.ts
 *
 * rtfstreamparser/ — mazira/rtf-stream-parser: see extractEncapsulation.test.ts
 *
 * The smoke test also covers synthetic/, described in interpretNativeRtf.fixtures.test.ts and extractEncapsulation.test.ts.
 */
import { describe, expect, test } from "vitest";

import { parseRtfClipboard } from "~/Brevredigering/LetterEditor/actions/rtf/parseRtfClipboard";
import { projectElements } from "~test/support/pasteTestUtils";

const fixtures = import.meta.glob<string>("/test/fixtures/rtf/**/*.rtf", {
  query: "?raw",
  import: "default",
  eager: true,
});

function fixture(name: string): string {
  const rtf = fixtures[`/test/fixtures/rtf/${name}`];
  if (rtf === undefined) throw new Error(`Missing fixture ${name}`);
  return rtf;
}

function project(name: string): string[] {
  const parsed = parseRtfClipboard(fixture(name));
  if (parsed.mode !== "elements") throw new Error(`Expected elements from ${name}, got ${parsed.mode}`);
  return projectElements(parsed.elements);
}

describe("all RTF fixtures", () => {
  test.each(Object.keys(fixtures))("%s parses to something insertable", (path) => {
    const parsed = parseRtfClipboard(fixtures[path]);

    expect(parsed.mode).toMatch(/^(elements|html|text)$/);
    if (parsed.mode === "elements") expect(projectElements(parsed.elements).join("").trim()).not.toEqual("");
  });
});

describe("headings", () => {
  test("RtfPipe Headings: built-in heading styles", () => {
    expect(project("rtfpipe/Headings.rtf")).toEqual([
      "H1: Heading 1",
      "H2: Heading 2",
      "H3: Heading 3",
      "P: Paragraph",
    ]);
  });

  test("rtf2xml headings_mixed: heading 1–3 by \\outlinelevel, deeper levels become paragraphs", () => {
    expect(project("rtf2xml/headings_mixed.rtf")).toEqual([
      "P: Text before headers.",
      "P: ",
      "H1: **Heading one**",
      "P: ",
      "P: Text.",
      "P: ",
      "H2: **Heading two.**",
      "P: ",
      "P: Text",
      "P: ",
      "H3: **Heading three**",
      "P: Text",
      "P: ",
      "P: **Heading five**",
      "P: Text",
      "P: ",
      "H2: **Heading two**",
      "P: Text",
      "H3: **Heading three**",
      "P: Text",
    ]);
  });

  test("rtf2xml heading_with_section: a section break between headings is a paragraph break", () => {
    expect(project("rtf2xml/heading_with_section.rtf")).toEqual([
      "P: Text before headers.",
      "P: ",
      "H1: **Heading one**",
      "P: ",
      "P: Text before section break.",
      "P: ",
      "H2: **Heading two**",
      "P: Text",
    ]);
  });
});

describe("lists", () => {
  test.each([
    ["rtfpipe/ListWord.rtf", "List 1", "List 2"],
    ["rtfpipe/List.rtf", "List 1", "List 2"],
    ["rtfpipe/ListGoogle.rtf", "Item 1", "Item 2"],
  ])("%s: bullet and numbered lists between paragraphs", (name, first, second) => {
    expect(project(name)).toEqual([
      "P: Paragraph",
      "• Bullet",
      "• Bullet 2",
      "P: Paragraph 2",
      `1. ${first}`,
      `1. ${second}`,
    ]);
  });

  test("rtf2xml bullet_list", () => {
    expect(project("rtf2xml/bullet_list.rtf")).toEqual([
      "P: Before list",
      "• First item",
      "• Second item",
      "• Third item",
      "P: After list",
    ]);
  });

  test("RtfPipe outline_list: \\ilvl > 0 marks nested items", () => {
    expect(project("rtfpipe/outline_list.rtf")).toEqual([
      "1. Level 1",
      "1. Level 1",
      "1. (n) Level 2",
      "1. (n) Level 2",
      "1. Level 1",
      "1. (n) Level 2",
      "1. (n) Level 3",
      "1. (n) Level 3",
      "1. Level 1",
      "1. (n) Level 2",
      "1. (n) Level 3",
      "1. (n) Level 3",
      "1. (n) Level 2",
    ]);
  });

  test("rtf2xml complex_list_bullet: list type per level from the list table", () => {
    expect(project("rtf2xml/complex_list_bullet.rtf")).toEqual([
      "P: Text before list",
      "1. item one level one",
      "1. item two level one",
      "• item one bullet",
      "• item two bullet",
      "1. (n) item one level two",
      "1. (n) item two level two",
      "1. (n) item one level three",
      "1. (n) item two level three",
      "1. (n) item one level four",
      "1. (n) item three level two",
    ]);
  });

  test("rtf2xml complex_list_diff_styles: nested levels with their own paragraph styles", () => {
    expect(project("rtf2xml/complex_list_diff_styles.rtf")).toEqual([
      "P: Text before list",
      "1. item one level one",
      "1. (n) item two level one",
      "1. (n) item one level two",
      "1. (n) item two level two",
      "1. (n) item one level three",
      "1. (n) item two level three",
      "1. (n) item one level four",
      "1. (n) item three level two",
    ]);
  });

  test.each(["rtf2xml/nested_lists_indents.rtf", "rtfpipe/nested_lists_indents_word.rtf"])(
    "%s: indented paragraphs inside a list stay paragraphs",
    (name) => {
      expect(project(name)).toEqual([
        "P: Text before list",
        "1. this is a list",
        "P: this should belong to the list above",
        "P: This should also belong to the list above",
        "P: ",
        "1. This is a continuation (so should still be in same list).",
        "1. Third point",
        "1. This list should start new, but should be part of big list",
        "1. like wise for this",
        "• bulleted list should belong to above",
        "• like wise with this bullet",
        "P: Stop numbering here",
      ]);
    },
  );

  test("rtf2xml list_with_indented_items", () => {
    expect(project("rtf2xml/list_with_indented_items.rtf")).toEqual([
      "P: Text before list",
      "1. this is a list",
      "P: this should belong to the list above",
      "P: This should also belong to the list above",
      "P: ",
      "1. This is a continuation (so should still be in same list).",
      "1. Third point",
    ]);
  });

  test("rtf2xml lists_with_breaks: \\page and \\sect inside a list end the item", () => {
    expect(project("rtf2xml/lists_with_breaks.rtf")).toEqual([
      "P: Text before list",
      "1. start list",
      "1. item 2",
      "1. item 3",
      "1. item 4",
      "1. item 5",
      "TABLE",
      "  tr: cell one | cell two",
      "  tr: cell 3 | cell 4",
      "1. after table. Should be its own list",
    ]);
  });

  test("rtf2xml list_in_table: lists in cells become cell text", () => {
    expect(project("rtf2xml/list_in_table.rtf")).toEqual([
      "P: Text before list",
      "TABLE",
      "  tr: item one item two | Should start new list likewise",
      "  tr: item 1 item 2 | ",
    ]);
  });
});

describe("tables", () => {
  test("rtf2xml table_simple", () => {
    expect(project("rtf2xml/table_simple.rtf")).toEqual([
      "P: 2 X 2 Table",
      "TABLE",
      "  tr: Cell one | Cell two",
      "  tr: Cell three | Cell four",
    ]);
  });

  test("rtf2xml table_different_cell_widths", () => {
    expect(project("rtf2xml/table_different_cell_widths.rtf")).toEqual([
      "P: 3 X 3 table, different cell widths",
      "P: ",
      "TABLE",
      "  tr: Cell one | Cell two | Cell three",
      "  tr: Cell four | Cell five | Cell six",
      "  tr: Cell seven | Cell eight | Cell nine",
    ]);
  });

  test("rtf2xml table_with_header: \\trhdr makes the header row, empty rows are kept", () => {
    const projected = project("rtf2xml/table_with_header.rtf");

    expect(projected.slice(0, 5)).toEqual([
      "P: 2 X 2 table. First row is header row. Followed by many empty rows.",
      "P: ",
      "TABLE",
      "  th: Header row | Header row",
      "  tr: Cell one | ",
    ]);
    expect(projected.slice(5).length).toBeGreaterThan(10);
    expect(new Set(projected.slice(5))).toEqual(new Set(["  tr:  | "]));
  });

  test("rtf2xml table_empty_row: horizontally merged cells become one cell", () => {
    expect(project("rtf2xml/table_empty_row.rtf").slice(-4)).toEqual([
      "TABLE",
      "  tr: Cell one | Cel two",
      "  tr: ",
      "  tr: Cell five | Cell three",
    ]);
  });

  test("RtfPipe merged_cells: vertically merged cells stay as empty cells", () => {
    expect(project("rtfpipe/merged_cells.rtf")).toEqual([
      "TABLE",
      "  tr: Vertical merged cells. |  | Horizontal merged cells",
      "  tr:  |  |  |  | ",
      "  tr:  |  | Horizontal and vertical merged cells",
      "  tr:  |  | ",
      "  tr:  |  | ",
    ]);
  });
});

describe("formatting", () => {
  test("rtf2xml inline_over_para: formatting carries across paragraphs", () => {
    expect(project("rtf2xml/inline_over_para.rtf").filter((line) => line !== "P: ")).toEqual([
      "P: Normal",
      "P: Normal _italics italics_",
      "P: _Italics italics_ normal",
      "P: Normal _italics italics_",
      "P: _Italics italics _**bold-italics bold-italics**",
      "P: **Bold bold** normal",
      "P: _All italics_",
      "P: _All italics_",
      "P: **All italics-bold**",
      "P: **All italics-bold**",
    ]);
  });

  test("rtf2xml italics_plain: \\plain ends italics", () => {
    expect(project("rtf2xml/italics_plain.rtf")).toEqual([
      "P: This is a sample photo cutline.",
      "P: **Joe Collins THE LOCAL GAZETTE**",
      "P: **Lorems**",
      "P: As an excuse for italics, _The Local Gazette_ will appear in this sentence.",
      "P: This text.",
      "P: Nineteen catfish downtown.",
    ]);
  });

  test("rtf2xml os_x_text_edit_italics_mixed: TextEdit turns italic on before bold", () => {
    expect(project("rtf2xml/os_x_text_edit_italics_mixed.rtf")).toEqual([
      "P: Normal text",
      "P: ",
      "P: **bold** normal",
      "P: ",
      "P: **bold** _bold&italics_ bold normal",
      "P: ",
      "TEXT: _bold&underlined&italics bold-off underlined-off bold&underlined&italics_",
    ]);
  });

  test("rtf2xml os_x_text_edit_faces_mixed: colours, underline and fonts are dropped", () => {
    expect(project("rtf2xml/os_x_text_edit_faces_mixed.rtf")).toEqual([
      "P: Normal text",
      "P: ",
      "P: added color no color",
      "P: **bold** normal",
      "P: ",
      "P: **bold** _bold&italics_ bold normal",
      "P: ",
      "P: _bold&underlined&italics bold-off _ _underlined-off bold&underlined&italics _font-geneva",
      "P: ",
      "TEXT: **font-geneva**",
    ]);
  });

  test("rtf.js hyperlink: link text is kept, the link is not", () => {
    expect(project("rtfjs/hyperlink.rtf")).toEqual([
      "P: This is a document with a hyperlink to this **pro**ject**.**",
      "P: **And **an unsafe link link in a paragraph that should be centered.",
      "P: And this last paragraph should be left-aligned again.",
    ]);
  });
});

describe("paragraphs and breaks", () => {
  test.each([
    ["rtf2xml/sections_next_page.rtf", ["P: Section one", "P: Section two", "P: Section three"]],
    ["rtf2xml/simple_page_break1.rtf", ["P: Hello world", "P: ", "P: second page"]],
    ["rtf2xml/simple_page_break2.rtf", ["P: Hellow world", "P: Now we are in another paragraph"]],
    ["rtfjs/implicit-par.rtf", ["P: LINE1 - APPLE", "P: LINE2 - BANANA", "P: LINE3 - CLEAN"]],
    ["rtf2xml/open_office_hello_world.rtf", ["P: Hello world."]],
  ])("%s", (name, expected) => {
    expect(project(name)).toEqual(expected);
  });

  test("rtf2xml escaped_text: escaped braces and backslashes are literal text", () => {
    expect(project("rtf2xml/escaped_text.rtf").slice(0, 5)).toEqual([
      "P: ***BRACES AND BACKSLASHES***",
      "P: :{this text is between brackets}:",
      "P: :{\\this text emulates a rtf grouping. It starts with a open bracket, then a back slash, and ends with a closed bracket.}:",
      "P: :\\text has a backslash before it:",
      "P: :\\\\text has two backslashes before it:",
    ]);
  });

  test("rtf2xml hyperlink: field results are kept, field instructions are not", () => {
    const projected = project("rtf2xml/hyperlink.rtf");

    expect(projected).toContain("P: Include www link http://www.cnn.com/");
    expect(projected).toContain("P: Hyperlink with email mailto:phthenry@earthlink.net?subject=subject line");
    expect(projected.join("\n")).not.toContain("HYPERLINK");
  });

  test("rtf2xml fields_simple_format_switch: field results are kept", () => {
    expect(project("rtf2xml/fields_simple_format_switch.rtf")[0]).toEqual(
      "P: Text:Cynthia Johnson(author, no switch):text",
    );
  });
});

describe("characters", () => {
  test.each([
    ["rtfjs/pile-of-poo.rtf", "P: Iñtërnâtiônàlizætiøn☃💩"],
    ["rtf2xml/open_office_special_char.rtf", "P: úӤ"],
  ])("%s: \\u escapes, including surrogate pairs", (name, expected) => {
    expect(project(name)).toEqual([expected]);
  });

  // Each line is "<character>:…:<labelled code point>". The fixtures' own labels have typos; these lines were
  // checked against Adobe's Symbol and the Unicode ZDINGBAT mapping tables instead.
  const LABEL_TYPOS: Record<string, string[]> = {
    "rtf2xml/char_upper_ranges.rtf": ["˘:0D28", "˙:0D29", "˚:0D2A"],
    "rtf2xml/char_upper_ranges_hex.rtf": ["˘:0D28", "˙:0D29", "˚:0D2A"],
    "rtf2xml/symbol.rtf": [
      "ς:V:GREEK LETTER STIGMA:03DA",
      "φ:f:GREEK SMALL LETTER PHI SYMBOL:03D5",
      "ϕ:j:GREEK SMALL LETTER PHI:03C6",
      "ςv:GREEK SMALL LETTER ZETA:03B6",
      "ζ:z:GREEK SMALL LETTER XI:03BE",
      "!:!:EXCLAMATION MARK:0033",
      "#:#:NUMBER SIGN?:0035",
      "%:%:PERCE NTAGE SIGN:0037",
      "(:(:LEFT PARENTHESIS:0040",
      "):):RIGHT PARENTHESIS:0041",
      "+:+:PLUS SIGN:0043",
      "⏐:\u2126:DIVIDES:2223",
      "⎜:ç::UNDEFINDED:0000",
      "″:≤:DOUBLE ACUTE ACCENT:02DD",
      "∑:å:N-ARY SUMMATION:2122",
      "⎦:˚:UNDEFINED:0000",
      "•:∑:BULLET:00B7",
      "⎠:ø:UNDEFINED:0000",
    ],
    "rtf2xml/zapf_dingbats.rtf": [
      "★:H:BLACK STAR:2705",
      "❊:j:TEARDROP-SPOKED ASTERISK:273B",
      "❋:k:HEAVY TEARDROP-SPOKED ASTERISK:273D",
      "◗:w:RIGHT HALF BLACK CIRCLE:2507",
      "✈:(:AIRPLANE:2708r ✉:):ENVELOPE:2709",
    ],
  };

  test.each(Object.entries(LABEL_TYPOS))("%s: every character matches its labelled code point", (name, typos) => {
    const mismatches = project(name)
      .map((line) => line.replace(/^P: /, "").split(":"))
      .filter((fields) => fields.length >= 2 && /^[0-9A-F]{4}$/.test(fields.at(-1)!))
      .filter(([character, ...rest]) => character.codePointAt(0) !== Number.parseInt(rest.at(-1)!, 16))
      .map((fields) => fields.join(":"));

    expect(mismatches).toEqual(typos);
  });
});
