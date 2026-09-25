import { describe, expect, test } from "vitest";

import {
  detectEncapsulationMode,
  extractEncapsulatedContent,
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

describe("extractEncapsulatedContent as html", () => {
  test("recovers a simple Outlook-style encapsulated HTML body", () => {
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

    expect(extractEncapsulatedContent(tokenizeRtf(rtf), "html")).toBe(
      "<html><body><p>Hello <b>world</b></p></body></html>",
    );
  });

  test("escapes markup characters in document text", () => {
    const rtf = "{\\rtf1\\ansi\\fromhtml1{\\*\\htmltag <p>}a < b & c{\\*\\htmltag </p>}}";
    expect(extractEncapsulatedContent(tokenizeRtf(rtf), "html")).toBe("<p>a &lt; b &amp; c</p>");
  });

  test("decodes non-ASCII visible text via the ansi codepage", () => {
    const rtf = "{\\rtf1\\ansi\\fromhtml1{\\*\\htmltag <p>}Bj\\'f8rn og \\'e5se{\\*\\htmltag </p>}}";
    expect(extractEncapsulatedContent(tokenizeRtf(rtf), "html")).toBe("<p>Bjørn og åse</p>");
  });

  test("ignores font table content that may appear alongside the encapsulated body", () => {
    const rtf = "{\\rtf1\\ansi\\fromhtml1{\\fonttbl{\\f0 Arial;}}{\\*\\htmltag <p>}Hi{\\*\\htmltag </p>}}";
    expect(extractEncapsulatedContent(tokenizeRtf(rtf), "html")).toBe("<p>Hi</p>");
  });

  test("drops content between \\htmlrtf and \\htmlrtf0, which is only for RTF readers", () => {
    const rtf =
      "{\\rtf1\\ansi\\fromhtml1{\\*\\htmltag <p>}\\htmlrtf {\\b RTF only}\\htmlrtf0 Hi{\\*\\htmltag <br>\\htmlrtf \\line\\htmlrtf0}}";
    expect(extractEncapsulatedContent(tokenizeRtf(rtf), "html")).toBe("<p>Hi<br>");
  });

  test("\\htmlrtf is scoped to its group", () => {
    const rtf = "{\\rtf1\\ansi\\fromhtml1{\\htmlrtf hidden}shown}";
    expect(extractEncapsulatedContent(tokenizeRtf(rtf), "html")).toBe("shown");
  });
});

describe("extractEncapsulatedContent as text", () => {
  test("recovers plain text with paragraph breaks converted to newlines", () => {
    const rtf = "{\\rtf1\\ansi\\fromtext1 Line one\\par Line two}";
    expect(extractEncapsulatedContent(tokenizeRtf(rtf), "text")).toBe("Line one\nLine two");
  });

  test("decodes non-ASCII text via the ansi codepage", () => {
    const rtf = "{\\rtf1\\ansi\\fromtext1 Bj\\'f8rn og \\'e5se}";
    expect(extractEncapsulatedContent(tokenizeRtf(rtf), "text")).toBe("Bjørn og åse");
  });
});
