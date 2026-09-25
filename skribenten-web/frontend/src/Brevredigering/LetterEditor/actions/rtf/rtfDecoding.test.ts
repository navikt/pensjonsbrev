import { describe, expect, test } from "vitest";

import {
  createByteDecoder,
  decodeUnicodeParam,
  skipUnicodeFallback,
} from "~/Brevredigering/LetterEditor/actions/rtf/rtfDecoding";

describe("decodeUnicodeParam", () => {
  test("decodes a positive parameter", () => {
    expect(decodeUnicodeParam(8226)).toBe("•");
  });

  test("decodes a negative parameter as a signed 16-bit value", () => {
    expect(decodeUnicodeParam(-3913)).toBe("\uF0B7");
  });
});

describe("skipUnicodeFallback", () => {
  test("skips characters of a text token and keeps the rest", () => {
    expect(skipUnicodeFallback(2, { type: "text", value: "??rest" })).toEqual({
      remaining: 0,
      rest: { type: "text", value: "rest" },
    });
  });

  test("keeps skipping into the next token when the text is shorter than the skip count", () => {
    expect(skipUnicodeFallback(3, { type: "text", value: "?" })).toEqual({ remaining: 2, rest: undefined });
  });

  test("counts a hex byte as one unit", () => {
    expect(skipUnicodeFallback(2, { type: "hexByte", byte: 0xe5 })).toEqual({ remaining: 1 });
  });

  test("counts a control word as one unit", () => {
    expect(skipUnicodeFallback(1, { type: "control", word: "tab", hasParam: false, param: 0 })).toEqual({
      remaining: 0,
    });
  });

  test("a group boundary ends the skip", () => {
    expect(skipUnicodeFallback(2, { type: "groupEnd" })).toEqual({ remaining: 0, rest: { type: "groupEnd" } });
    expect(skipUnicodeFallback(2, { type: "groupStart" })).toEqual({ remaining: 0, rest: { type: "groupStart" } });
  });
});

describe("createByteDecoder", () => {
  test("defaults to windows-1252", () => {
    expect(createByteDecoder("{\\rtf1\\ansi}")([0xe6, 0xf8, 0xe5])).toBe("æøå");
  });

  test("uses the document's \\ansicpg", () => {
    expect(createByteDecoder("{\\rtf1\\ansi\\ansicpg1257}")([0xe0, 0xfe])).toBe("ąž");
  });

  test("falls back to windows-1252 for an unknown code page", () => {
    expect(createByteDecoder("{\\rtf1\\ansi\\ansicpg99999}")([0xe6])).toBe("æ");
  });
});
