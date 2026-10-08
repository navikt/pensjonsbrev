import { describe, expect, test } from "vitest";

import { createByteDecoder } from "~/Brevredigering/LetterEditor/actions/rtf/rtfDecoding";
import {
  ENCAPSULATION_DESTINATIONS,
  NATIVE_DESTINATIONS,
} from "~/Brevredigering/LetterEditor/actions/rtf/rtfDestinations";
import { tokenizeRtf } from "~/Brevredigering/LetterEditor/actions/rtf/tokenizeRtf";
import { type RtfEvent, type WalkRtfOptions, walkRtf } from "~/Brevredigering/LetterEditor/actions/rtf/walkRtf";

const NATIVE: WalkRtfOptions = {
  destinations: NATIVE_DESTINATIONS,
  decodeBytes: createByteDecoder([]),
};

const ENCAPSULATION: WalkRtfOptions = {
  destinations: ENCAPSULATION_DESTINATIONS,
  decodeBytes: createByteDecoder([]),
};

const walk = (rtf: string, options = NATIVE): RtfEvent[] => [...walkRtf(tokenizeRtf(rtf), options)];

const textOf = (events: RtfEvent[]) =>
  events
    .filter((event) => event.kind === "text")
    .map((event) => event.value)
    .join("");

const control = (word: string, param?: number) => ({
  kind: "control",
  token: { type: "control", word, hasParam: param !== undefined, param: param ?? 0 },
  destination: "body",
});

describe("walkRtf", () => {
  test("emits plain text and group boundaries", () => {
    expect(walk("{\\rtf1{\\b Hello}}")).toEqual([
      { kind: "groupStart", destination: "body" },
      control("rtf", 1),
      { kind: "groupStart", destination: "body" },
      control("b"),
      { kind: "text", value: "Hello", destination: "body" },
      { kind: "groupEnd", destination: "body" },
      { kind: "documentEnd" },
    ]);
  });

  test("swallows known skip destinations entirely, including nested groups", () => {
    expect(walk("{\\rtf1{\\fonttbl{\\f0 Arial;}}Hello}")).toEqual([
      { kind: "groupStart", destination: "body" },
      control("rtf", 1),
      { kind: "groupStart", destination: "body" },
      { kind: "groupEnd", destination: "skip" },
      { kind: "text", value: "Hello", destination: "body" },
      { kind: "documentEnd" },
    ]);
  });

  test("swallows unrecognized ignorable (\\*) destinations", () => {
    expect(walk("{\\rtf1{\\*\\unknownstuff Nope}Hello}")).toEqual([
      { kind: "groupStart", destination: "body" },
      control("rtf", 1),
      { kind: "groupStart", destination: "body" },
      { kind: "groupEnd", destination: "skip" },
      { kind: "text", value: "Hello", destination: "body" },
      { kind: "documentEnd" },
    ]);
  });

  test("swallows a \\* group that starts with text", () => {
    expect(textOf(walk("{\\rtf1{\\* hidden}shown}"))).toBe("shown");
  });

  test("passes through htmltag content in the htmltag destination", () => {
    expect(walk("{\\rtf1{\\*\\htmltag <p>}Hello}", ENCAPSULATION)).toEqual([
      { kind: "groupStart", destination: "body" },
      control("rtf", 1),
      { kind: "groupStart", destination: "body" },
      { kind: "text", value: "<p>", destination: "htmltag" },
      { kind: "groupEnd", destination: "htmltag" },
      { kind: "text", value: "Hello", destination: "body" },
      { kind: "documentEnd" },
    ]);
  });

  test("captures listtext content in the listMarker destination, not as body text", () => {
    expect(walk("{\\rtf1{\\listtext\\'b7\\tab}Hello}")).toEqual([
      { kind: "groupStart", destination: "body" },
      control("rtf", 1),
      { kind: "groupStart", destination: "body" },
      { kind: "text", value: "\u00b7", destination: "listMarker" },
      { kind: "text", value: "\t", destination: "listMarker" },
      { kind: "groupEnd", destination: "listMarker" },
      { kind: "text", value: "Hello", destination: "body" },
      { kind: "documentEnd" },
    ]);
  });

  test("captures \\*\\listtext content in the listMarker destination", () => {
    const events = walk("{\\rtf1{\\*\\listtext\\'b7\\tab}Hello}");
    expect(events).toContainEqual({ kind: "text", value: "\u00b7", destination: "listMarker" });
  });

  test("still skips skip-listed destinations after \\*", () => {
    expect(
      textOf(walk("{\\rtf1{\\*\\bkmkstart b}{\\*\\fldinst HYPERLINK}{\\*\\pnseclvl1\\pnucrm{\\pntxta .}}Hei}")),
    ).toBe("Hei");
  });

  test("decodes hex escapes via windows-1252", () => {
    expect(textOf(walk("\\'e6\\'f8\\'e5"))).toBe("æøå");
  });

  test("decodes a run of hex escapes as one text event", () => {
    expect(walk("{\\rtf1 \\'e6\\'f8\\'e5}")).toEqual([
      { kind: "groupStart", destination: "body" },
      control("rtf", 1),
      { kind: "text", value: "æøå", destination: "body" },
      { kind: "documentEnd" },
    ]);
  });

  test("decodes unicode escapes and skips the default 1-character fallback", () => {
    // \u8226 is the bullet character (•), followed by a "?" fallback for old readers.
    expect(walk("\\u8226?")).toEqual([{ kind: "text", value: "•", destination: "body" }, { kind: "documentEnd" }]);
  });

  test("respects \\uc to skip a different number of fallback characters", () => {
    expect(textOf(walk("\\uc2\\u8226??rest"))).toBe("•rest");
  });

  test("skips hex escapes and control words as fallback units", () => {
    expect(textOf(walk("\\uc2\\u229\\'e5\\'e5 ok"))).toBe("å ok");
  });

  test("\\uc is scoped to its group", () => {
    expect(textOf(walk("{\\rtf1{\\uc2\\u8226??}\\u8226?x}"))).toBe("••x");
  });

  test("a group boundary ends the fallback skip", () => {
    expect(textOf(walk("{\\rtf1{\\u8226}x}"))).toBe("•x");
  });

  test("negative unicode parameter is interpreted as a two's-complement codepoint", () => {
    // -10 -> 65536 - 10 = 65526, which is within the Private Use Area used by symbol fonts.
    expect(walk("\\u-10 X")[0]).toEqual({ kind: "text", value: String.fromCharCode(65_526), destination: "body" });
  });

  test("maps control symbols to their literal characters", () => {
    expect(textOf(walk("a\\~b\\_c\\\\d\\{e\\}f"))).toBe("a\u00A0b-c\\d{e}f");
  });

  test("maps typographic control words to their characters", () => {
    expect(textOf(walk("don\\rquote t \\ldblquote x\\rdblquote  a\\endash b\\emdash c \\bullet"))).toBe(
      "don’t “x” a–b—c •",
    );
  });

  test("drops the optional-hyphen control symbol", () => {
    expect(textOf(walk("a\\-b"))).toBe("ab");
  });

  test("leaves \\line to the consumer as a control event", () => {
    expect(walk("{\\rtf1 a\\line b}")).toEqual([
      { kind: "groupStart", destination: "body" },
      control("rtf", 1),
      { kind: "text", value: "a", destination: "body" },
      control("line"),
      { kind: "text", value: "b", destination: "body" },
      { kind: "documentEnd" },
    ]);
  });

  test("emits balanced group events only for groups opened outside skip destinations", () => {
    const events = walk("{\\rtf1{\\fonttbl{\\f0{\\x y}}}{\\b{\\i x}}{\\*\\generator{\\a}}}");
    const starts = events.filter((event) => event.kind === "groupStart").length;
    const ends = events.filter((event) => event.kind === "groupEnd").length;
    // The root group ends with documentEnd instead of groupEnd.
    expect(starts).toBe(5);
    expect(ends).toBe(4);
  });

  test("stops at the end of the root group", () => {
    expect(textOf(walk("{\\rtf1 A}B"))).toBe("A");
    expect(walk("{\\rtf1 A}B").at(-1)).toEqual({ kind: "documentEnd" });
  });

  test("resets a nested \\rtf document to the body destination", () => {
    const events = walk("{\\rtf1\\fromhtml1{\\*\\htmltag <p>{\\rtf1 Signatur}}}", ENCAPSULATION);
    expect(events).toContainEqual({ kind: "text", value: "<p>", destination: "htmltag" });
    expect(events).toContainEqual({ kind: "text", value: "Signatur", destination: "body" });
  });

  test("survives unbalanced closing braces without throwing", () => {
    expect(walk("Hello}}}")).toEqual([{ kind: "text", value: "Hello", destination: "body" }, { kind: "documentEnd" }]);
  });
});

describe("walkRtf - symbol fonts", () => {
  const FONTS = "{\\fonttbl{\\f0 Calibri;}{\\f3\\fcharset2 Symbol;}{\\f4\\fcharset2 Wingdings;}}";
  const textIn = (body: string, deff = "\\deff0") => textOf(walk(`{\\rtf1\\ansi${deff}${FONTS}${body}}`));

  test("maps bytes and text in a symbol font", () => {
    expect(textIn("{\\f3 \\'b7 a}")).toBe("• α");
  });

  test("restores the outer font at the end of the group", () => {
    expect(textIn("{\\f3 a}a\\'b7")).toBe("αa·");
  });

  test("decodes bytes before a font change with the font they were written in", () => {
    expect(textIn("\\f3\\'b7\\f0\\'b7\\f4\\'a7")).toBe("•·▪");
  });

  test("\\plain returns to the default font", () => {
    expect(textIn("\\f3 a\\plain a")).toBe("αa");
    expect(textIn("\\f0 a\\plain a", "\\deff3")).toBe("aα");
  });

  test("uses \\deff before any \\f", () => {
    expect(textIn("a", "\\deff3")).toBe("α");
  });

  test("maps Word for Mac's \\uN in the private use area, and skips the fallback", () => {
    expect(textIn("{\\f3 \\u-3913\\'b7}")).toBe("•");
  });

  test("maps list markers in a symbol font", () => {
    const events = walk(`{\\rtf1\\ansi${FONTS}{\\listtext\\f3 \\'b7\\tab}Punkt}`);

    expect(events).toContainEqual({ kind: "text", value: "•", destination: "listMarker" });
  });

  test("never maps encapsulated HTML markup", () => {
    const events = walk(`{\\rtf1\\ansi\\fromhtml1${FONTS}\\f3{\\*\\htmltag <p>}a}`, ENCAPSULATION);

    expect(events).toContainEqual({ kind: "text", value: "<p>", destination: "htmltag" });
    expect(textOf(events)).toBe("<p>α");
  });
});
