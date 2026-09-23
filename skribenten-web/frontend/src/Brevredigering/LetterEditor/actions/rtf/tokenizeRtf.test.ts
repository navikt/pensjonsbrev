import { describe, expect, test } from "vitest";

import { tokenizeRtf } from "~/Brevredigering/LetterEditor/actions/rtf/tokenizeRtf";

describe("tokenizeRtf", () => {
  test("tokenizes groups", () => {
    expect(tokenizeRtf("{}")).toEqual([{ type: "groupStart" }, { type: "groupEnd" }]);
  });

  test("tokenizes plain text", () => {
    expect(tokenizeRtf("hello world")).toEqual([{ type: "text", text: "hello world" }]);
  });

  test("tokenizes a control word without parameter", () => {
    expect(tokenizeRtf("\\par")).toEqual([{ type: "controlWord", name: "par", param: undefined }]);
  });

  test("tokenizes a control word with a positive parameter", () => {
    expect(tokenizeRtf("\\b1")).toEqual([{ type: "controlWord", name: "b", param: 1 }]);
  });

  test("tokenizes a control word with a negative parameter", () => {
    expect(tokenizeRtf("\\li-360")).toEqual([{ type: "controlWord", name: "li", param: -360 }]);
  });

  test("consumes a single trailing space delimiter after a control word", () => {
    expect(tokenizeRtf("\\par Hello")).toEqual([
      { type: "controlWord", name: "par", param: undefined },
      { type: "text", text: "Hello" },
    ]);
  });

  test("does not consume more than one trailing space delimiter", () => {
    expect(tokenizeRtf("\\par  Hello")).toEqual([
      { type: "controlWord", name: "par", param: undefined },
      { type: "text", text: " Hello" },
    ]);
  });

  test("tokenizes control symbols", () => {
    expect(tokenizeRtf("\\~\\_\\-\\\\\\{\\}")).toEqual([
      { type: "controlSymbol", symbol: "~" },
      { type: "controlSymbol", symbol: "_" },
      { type: "controlSymbol", symbol: "-" },
      { type: "controlSymbol", symbol: "\\" },
      { type: "controlSymbol", symbol: "{" },
      { type: "controlSymbol", symbol: "}" },
    ]);
  });

  test("tokenizes hex-escaped bytes", () => {
    expect(tokenizeRtf("\\'e6\\'f8\\'e5")).toEqual([
      { type: "hexEscape", byte: 0xe6 },
      { type: "hexEscape", byte: 0xf8 },
      { type: "hexEscape", byte: 0xe5 },
    ]);
  });

  test("ignores raw CR/LF in the source", () => {
    expect(tokenizeRtf("a\r\nb")).toEqual([{ type: "text", text: "ab" }]);
  });

  test("skips binary payload for \\binN without tokenizing it", () => {
    // The 5-byte binary payload contains RTF-special characters ({, }, \) that
    // must NOT be interpreted as tokens - they should be skipped as raw bytes.
    const result = tokenizeRtf("\\bin5{}\\\\xAfter");
    expect(result).toEqual([{ type: "text", text: "After" }]);
  });

  test("realistic snippet: bold paragraph followed by plain paragraph", () => {
    const result = tokenizeRtf("{\\rtf1{\\b Hello}\\par World}");
    expect(result).toEqual([
      { type: "groupStart" },
      { type: "controlWord", name: "rtf", param: 1 },
      { type: "groupStart" },
      { type: "controlWord", name: "b", param: undefined },
      { type: "text", text: "Hello" },
      { type: "groupEnd" },
      { type: "controlWord", name: "par", param: undefined },
      { type: "text", text: "World" },
      { type: "groupEnd" },
    ]);
  });
});
