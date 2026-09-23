import { describe, expect, test } from "vitest";

import {
  detectEncapsulationMode,
  extractEncapsulatedHtml,
  extractEncapsulatedText,
} from "~/Brevredigering/LetterEditor/actions/rtf/extractEncapsulation";
import { tokenizeRtf } from "~/Brevredigering/LetterEditor/actions/rtf/tokenizeRtf";

describe("detectEncapsulationMode", () => {
  test("detects \\fromhtml1", () => {
    expect(detectEncapsulationMode(tokenizeRtf("{\\rtf1\\ansi\\fromhtml1}"))).toBe("html");
  });

  test("detects \\fromtext1", () => {
    expect(detectEncapsulationMode(tokenizeRtf("{\\rtf1\\ansi\\fromtext1}"))).toBe("text");
  });

  test("treats \\fromhtml0 as not encapsulated", () => {
    expect(detectEncapsulationMode(tokenizeRtf("{\\rtf1\\ansi\\fromhtml0}"))).toBeUndefined();
  });

  test("returns undefined for genuine/native RTF with no encapsulation marker", () => {
    expect(detectEncapsulationMode(tokenizeRtf("{\\rtf1\\ansi\\b Bold text\\b0}"))).toBeUndefined();
  });
});

describe("extractEncapsulatedHtml", () => {
  test("recovers a simple Outlook-style encapsulated HTML body", () => {
    // A simplified but representative fragment of what Outlook produces for
    // \fromhtml1: htmltag destinations hold literal tag markup, ordinary
    // RTF text runs hold the visible page content, formatted with regular
    // RTF font/paragraph properties that we simply ignore in this mode.
    const rtf =
      "{\\rtf1\\ansi\\fromhtml1" +
      "{\\*\\htmltag64 <html>}" +
      "{\\*\\htmltag <body>}" +
      "{\\*\\htmltag <p>}" +
      "Hello " +
      "{\\*\\htmltag <b>}" +
      "world" +
      "{\\*\\htmltag </b>}" +
      "{\\*\\htmltag </p>}" +
      "{\\*\\htmltag </body>}" +
      "{\\*\\htmltag </html>}" +
      "}";

    expect(extractEncapsulatedHtml(tokenizeRtf(rtf))).toBe("<html><body><p>Hello <b>world</b></p></body></html>");
  });

  test("escapes stray angle brackets appearing in visible (non-tag) text", () => {
    const rtf = "{\\rtf1\\ansi\\fromhtml1{\\*\\htmltag <p>}a < b{\\*\\htmltag </p>}}";
    expect(extractEncapsulatedHtml(tokenizeRtf(rtf))).toBe("<p>a &lt; b</p>");
  });

  test("decodes non-ASCII visible text via the ansi codepage", () => {
    const rtf = "{\\rtf1\\ansi\\fromhtml1{\\*\\htmltag <p>}Bj\\'f8rn og \\'e5se{\\*\\htmltag </p>}}";
    expect(extractEncapsulatedHtml(tokenizeRtf(rtf))).toBe("<p>Bjørn og åse</p>");
  });

  test("ignores font table content that may appear alongside the encapsulated body", () => {
    const rtf = "{\\rtf1\\ansi\\fromhtml1{\\fonttbl{\\f0 Arial;}}{\\*\\htmltag <p>}Hi{\\*\\htmltag </p>}}";
    expect(extractEncapsulatedHtml(tokenizeRtf(rtf))).toBe("<p>Hi</p>");
  });
});

describe("extractEncapsulatedText", () => {
  test("recovers plain text with paragraph breaks converted to newlines", () => {
    const rtf = "{\\rtf1\\ansi\\fromtext1 Line one\\par Line two}";
    expect(extractEncapsulatedText(tokenizeRtf(rtf))).toBe("Line one\nLine two");
  });

  test("decodes non-ASCII text via the ansi codepage", () => {
    const rtf = "{\\rtf1\\ansi\\fromtext1 Bj\\'f8rn og \\'e5se}";
    expect(extractEncapsulatedText(tokenizeRtf(rtf))).toBe("Bjørn og åse");
  });
});
