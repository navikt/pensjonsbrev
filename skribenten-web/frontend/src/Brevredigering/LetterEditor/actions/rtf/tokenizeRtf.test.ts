import { describe, expect, test } from "vitest";

import { tokenizeRtf } from "~/Brevredigering/LetterEditor/actions/rtf/tokenizeRtf";

const control = (word: string, param?: number) => ({
  type: "control",
  word,
  hasParam: param !== undefined,
  param: param ?? 0,
});
const text = (value: string) => ({ type: "text", value });

describe("tokenizeRtf", () => {
  test("tokenizes groups", () => {
    expect(tokenizeRtf("{}")).toEqual([{ type: "groupStart" }, { type: "groupEnd" }]);
  });

  test("tokenizes plain text", () => {
    expect(tokenizeRtf("hello world")).toEqual([text("hello world")]);
  });

  test("tokenizes a control word without parameter", () => {
    expect(tokenizeRtf("\\par")).toEqual([control("par")]);
  });

  test("tokenizes a control word with a positive parameter", () => {
    expect(tokenizeRtf("\\b1")).toEqual([control("b", 1)]);
  });

  test("tokenizes a control word with a negative parameter", () => {
    expect(tokenizeRtf("\\li-360")).toEqual([control("li", -360)]);
  });

  test("consumes a single trailing space delimiter after a control word", () => {
    expect(tokenizeRtf("\\par Hello")).toEqual([control("par"), text("Hello")]);
  });

  test("does not consume more than one trailing space delimiter", () => {
    expect(tokenizeRtf("\\par  Hello")).toEqual([control("par"), text(" Hello")]);
  });

  test("tokenizes control symbols", () => {
    expect(tokenizeRtf("\\~\\_\\-\\\\\\{\\}\\*")).toEqual([
      text("\u00A0"),
      text("-"),
      text("\\"),
      text("{"),
      text("}"),
      control("*"),
    ]);
  });

  test("tokenizes an escaped line break as a paragraph break", () => {
    expect(tokenizeRtf("a\\\r\nb")).toEqual([text("a"), control("par"), text("b")]);
  });

  test("tokenizes hex-escaped bytes", () => {
    expect(tokenizeRtf("\\'e6\\'f8\\'e5")).toEqual([
      { type: "hexByte", byte: 0xe6 },
      { type: "hexByte", byte: 0xf8 },
      { type: "hexByte", byte: 0xe5 },
    ]);
  });

  test("ignores raw CR/LF in the source", () => {
    expect(tokenizeRtf("a\r\nb")).toEqual([text("a"), text("b")]);
  });

  test("skips binary payload for \\binN without tokenizing it", () => {
    // The 5-byte payload contains RTF-special characters ({, }, \) that must not become tokens.
    expect(tokenizeRtf("\\bin5{}\\\\xAfter")).toEqual([text("After")]);
  });

  test("stops at \\bin data that runs past the end of the input", () => {
    expect(tokenizeRtf("{Før\\bin99 {}\\par}")).toEqual([{ type: "groupStart" }, text("Før")]);
  });

  test.each([
    ["\\'4}", [{ type: "hexByte", byte: 0x3f }, { type: "groupEnd" }]],
    ["\\'4", [{ type: "hexByte", byte: 0x3f }]],
    ["\\'", [{ type: "hexByte", byte: 0x3f }]],
    ["\\'zz", [{ type: "hexByte", byte: 0x3f }, text("zz")]],
    ["\\'4g", [{ type: "hexByte", byte: 0x3f }, text("g")]],
  ])("a malformed hex escape %s becomes '?' and consumes only valid hex digits", (rtf, expected) => {
    expect(tokenizeRtf(rtf)).toEqual(expected);
  });

  test.each([
    ["\\ls2147483647", control("ls", 2_147_483_647)],
    ["\\ls-2147483648", control("ls", -2_147_483_648)],
    ["\\ls9999999999", control("ls", 2_147_483_647)],
    ["\\ls-9999999999", control("ls", -2_147_483_648)],
    ["\\ls12345678901234567890", control("ls")],
  ])("clamps %s to a signed 32-bit parameter, or drops it if longer than 10 digits", (rtf, expected) => {
    expect(tokenizeRtf(`${rtf} x`)).toEqual([expected, text("x")]);
  });

  test("realistic snippet: bold paragraph followed by plain paragraph", () => {
    expect(tokenizeRtf("{\\rtf1{\\b Hello}\\par World}")).toEqual([
      { type: "groupStart" },
      control("rtf", 1),
      { type: "groupStart" },
      control("b"),
      text("Hello"),
      { type: "groupEnd" },
      control("par"),
      text("World"),
      { type: "groupEnd" },
    ]);
  });
});
