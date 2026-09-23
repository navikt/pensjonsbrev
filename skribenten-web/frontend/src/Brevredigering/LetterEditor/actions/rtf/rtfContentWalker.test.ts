import { describe, expect, test } from "vitest";

import { walkRtfContent } from "~/Brevredigering/LetterEditor/actions/rtf/rtfContentWalker";
import { tokenizeRtf } from "~/Brevredigering/LetterEditor/actions/rtf/tokenizeRtf";

function walk(rtf: string, options?: Parameters<typeof walkRtfContent>[1]) {
  return walkRtfContent(tokenizeRtf(rtf), options);
}

describe("walkRtfContent", () => {
  test("emits plain text and group boundaries", () => {
    expect(walk("{\\rtf1 Hello}")).toEqual([
      { kind: "groupStart" },
      { kind: "control", name: "rtf", param: 1 },
      { kind: "text", value: "Hello", raw: false },
      { kind: "groupEnd" },
    ]);
  });

  test("swallows known skip destinations entirely, including nested groups", () => {
    const result = walk("{\\rtf1{\\fonttbl{\\f0 Arial;}}Hello}");
    expect(result).toEqual([
      { kind: "groupStart" },
      { kind: "control", name: "rtf", param: 1 },
      { kind: "text", value: "Hello", raw: false },
      { kind: "groupEnd" },
    ]);
  });

  test("swallows unrecognized ignorable (\\*) destinations", () => {
    const result = walk("{\\rtf1{\\*\\unknownstuff Nope}Hello}");
    expect(result).toEqual([
      { kind: "groupStart" },
      { kind: "control", name: "rtf", param: 1 },
      { kind: "text", value: "Hello", raw: false },
      { kind: "groupEnd" },
    ]);
  });

  test("passes through a designated destination's content as raw text", () => {
    const result = walk("{\\rtf1{\\*\\htmltag <p>}Hello}", { passThroughDestinations: new Set(["htmltag"]) });
    expect(result).toEqual([
      { kind: "groupStart" },
      { kind: "control", name: "rtf", param: 1 },
      { kind: "groupStart" },
      { kind: "control", name: "htmltag", param: undefined },
      { kind: "text", value: "<p>", raw: true },
      { kind: "groupEnd" },
      { kind: "text", value: "Hello", raw: false },
      { kind: "groupEnd" },
    ]);
  });

  test("captures listtext destination content as a single listMarkerText event, not as text", () => {
    const result = walk("{\\rtf1{\\*\\listtext\\'b7\\tab}Hello}");
    expect(result).toEqual([
      { kind: "groupStart" },
      { kind: "control", name: "rtf", param: 1 },
      { kind: "listMarkerText", value: "\u00b7" },
      { kind: "text", value: "Hello", raw: false },
      { kind: "groupEnd" },
    ]);
  });

  test("decodes hex escapes via windows-1252", () => {
    const result = walk("\\'e6\\'f8\\'e5");
    expect(result.map((e) => (e.kind === "text" ? e.value : null)).join("")).toBe("æøå");
  });

  test("decodes unicode escapes and skips the default 1-character fallback", () => {
    // \u8226 is the bullet character (•), followed by a "?" fallback for old readers.
    expect(walk("\\u8226?")).toEqual([{ kind: "text", value: "•", raw: false }]);
  });

  test("respects \\uc to skip a different number of fallback characters", () => {
    expect(walk("\\uc2\\u8226??rest")).toEqual([
      { kind: "text", value: "•", raw: false },
      { kind: "text", value: "rest", raw: false },
    ]);
  });

  test("negative unicode parameter is interpreted as a two's-complement codepoint", () => {
    // -10 -> 65536 - 10 = 65526, which is within the Private Use Area used by symbol fonts.
    const result = walk("\\u-10 X");
    expect(result[0]).toEqual({ kind: "text", value: String.fromCharCode(65_526), raw: false });
  });

  test("maps control symbols to their literal characters", () => {
    const result = walk("a\\~b\\_c\\\\d\\{e\\}f");
    expect(result.map((e) => (e.kind === "text" ? e.value : null)).join("")).toBe("a\u00A0b-c\\d{e}f");
  });

  test("drops the optional-hyphen control symbol", () => {
    const result = walk("a\\-b");
    expect(result.map((e) => (e.kind === "text" ? e.value : null)).join("")).toBe("ab");
  });

  test("survives unbalanced closing braces without throwing", () => {
    expect(() => walk("Hello}}}")).not.toThrow();
  });
});
