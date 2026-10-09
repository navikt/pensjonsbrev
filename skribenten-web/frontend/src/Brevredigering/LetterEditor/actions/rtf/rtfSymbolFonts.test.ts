import { describe, expect, test } from "vitest";

import { createByteDecoder } from "~/Brevredigering/LetterEditor/actions/rtf/rtfDecoding";
import {
  mapSymbolBytes,
  mapSymbolText,
  parseSymbolFonts,
} from "~/Brevredigering/LetterEditor/actions/rtf/rtfSymbolFonts";
import { tokenizeRtf } from "~/Brevredigering/LetterEditor/actions/rtf/tokenizeRtf";

const fontsOf = (rtf: string) => parseSymbolFonts(tokenizeRtf(rtf));
const cp1252 = createByteDecoder([]);

describe("parseSymbolFonts", () => {
  test("finds symbol fonts by name in a Word font table", () => {
    const fonts = fontsOf(
      "{\\rtf1\\ansi\\deff0{\\fonttbl{\\f0\\froman\\fcharset0 Times New Roman;}{\\f3\\froman\\fcharset2\\fprq2{\\*\\panose 05050102010706020507}Symbol;}" +
        "{\\f10\\fnil\\fcharset2\\fprq2{\\*\\panose 05000000000000000000}Wingdings;}}\\pard Tekst\\par}",
    );

    expect(fonts.defaultFont).toBe(0);
    expect([...fonts.tables.keys()]).toEqual([3, 10]);
  });

  test("reads font tables without groups around the entries", () => {
    const fonts = fontsOf("{\\rtf1\\ansi{\\fonttbl\\f0\\fswiss Helvetica;\\f1\\fnil Zapf Dingbats;}Tekst}");

    expect([...fonts.tables.keys()]).toEqual([1]);
  });

  test("leaves the alternative font name (\\falt) out of the name", () => {
    const fonts = fontsOf("{\\rtf1{\\fonttbl{\\f18\\fnil\\fcharset2 Zapf Dingbats{\\*\\falt Monotype Sorts};}}}");

    expect([...fonts.tables.keys()]).toEqual([18]);
  });

  test.each(["Symbol", "WINGDINGS", "Wingdings 2", "Wingdings 3", "ZapfDingbats", "ITC Zapf Dingbats"])(
    "recognises %s",
    (name) => {
      expect(fontsOf(`{\\rtf1{\\fonttbl{\\f5 ${name};}}}`).tables.has(5)).toBe(true);
    },
  );

  test.each(["Webdings", "Marlett", "Symbolic", "Calibri"])("has no table for %s, even with \\fcharset2", (name) => {
    expect(fontsOf(`{\\rtf1{\\fonttbl{\\f5\\fcharset2 ${name};}}}`).tables.size).toBe(0);
  });

  test("has no default font or tables without a font table", () => {
    expect(fontsOf("{\\rtf1\\ansi Tekst}")).toEqual({ defaultFont: undefined, tables: new Map() });
  });
});

describe("symbol tables", () => {
  const tableOf = (name: string) => fontsOf(`{\\rtf1{\\fonttbl{\\f1 ${name};}}}`).tables.get(1)!;
  const bytes = (name: string, ...values: number[]) => mapSymbolBytes(tableOf(name), values, cp1252);

  test.each([
    [0x61, "α"],
    [0x57, "Ω"],
    [0xb7, "•"],
    [0xae, "→"],
    [0xb3, "≥"],
    [0xa0, "€"],
    [0xd6, "√"],
  ])("Symbol byte %i is %s", (byte, expected) => {
    expect(bytes("Symbol", byte)).toBe(expected);
  });

  test.each([
    [0xa7, "▪"],
    [0x6c, "●"],
    [0x6e, "■"],
    [0x76, "❖"],
    [0xd8, "⮚"],
    [0xfc, "✔"],
  ])("Wingdings byte %i is %s, used by Word for list bullets", (byte, expected) => {
    expect(bytes("Wingdings", byte)).toBe(expected);
  });

  test.each([
    ["Wingdings 2", 0x50, "✓"],
    ["Wingdings 2", 0x53, "☒"],
    ["Wingdings 3", 0x75, "►"],
    ["Zapf Dingbats", 0x41, "✡"],
    ["Zapf Dingbats", 0x48, "★"],
    ["Zapf Dingbats", 0x6c, "●"],
    ["Zapf Dingbats", 0xd4, "➔"],
    ["Zapf Dingbats", 0xac, "①"],
  ])("%s byte %i is %s", (name, byte, expected) => {
    expect(bytes(name, byte)).toBe(expected);
  });

  test("decodes bytes without a mapping in the document's code page", () => {
    expect(bytes("Wingdings", 0x41, 0xe6)).toBe("Aæ");
    expect(bytes("Symbol", 0xf0)).toBe("ð");
  });

  test("maps text and Word for Mac's private use characters (U+F020–U+F0FF) by byte value", () => {
    expect(mapSymbolText(tableOf("Symbol"), "ab\uF061\uF0B7", (byte) => cp1252([byte]))).toBe("αβα•");
  });

  test("keeps characters above 0xFF that are not in the private use range", () => {
    expect(mapSymbolText(tableOf("Symbol"), "€\u2022", (byte) => cp1252([byte]))).toBe("€\u2022");
  });
});
