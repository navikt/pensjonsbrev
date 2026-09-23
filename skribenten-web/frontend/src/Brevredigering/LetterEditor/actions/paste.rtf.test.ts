import { describe, expect, test } from "vitest";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { newLiteral, newParagraph, newTitle, text } from "~/Brevredigering/LetterEditor/actions/common";
import { type LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { type LiteralValue } from "~/types/brevbakerTypes";
import { letter, literal, paragraph, select } from "~test/support/letterEditorTestUtils";

function clipboardOf(data: Record<string, string>): DataTransfer {
  return {
    types: Object.keys(data),
    getData: (format: string) => data[format] ?? "",
  } as unknown as DataTransfer;
}

function initialState(): LetterEditorState {
  return letter(paragraph({ id: 1, content: [literal({ id: 11, text: "Teksten min" })] }));
}

function pasteAtStart(clipboard: DataTransfer) {
  return Actions.paste(initialState(), { blockIndex: 0, contentIndex: 0 }, 0, clipboard);
}

// Pasting RTF should produce exactly the same letter as pasting the
// equivalent HTML, since both paths feed the same insertion logic.
function expectRtfEquivalentToHtml(rtf: string, html: string) {
  const fromRtf = pasteAtStart(clipboardOf({ "text/rtf": rtf }));
  const fromHtml = pasteAtStart(clipboardOf({ "text/html": html }));
  expect(fromRtf.redigertBrev).toEqual(fromHtml.redigertBrev);
  expect(fromRtf.focus).toEqual(fromHtml.focus);
}

const WORD_HEADER =
  "{\\rtf1\\ansi\\ansicpg1252\\deff0{\\fonttbl{\\f0\\fswiss Calibri;}}{\\colortbl;\\red0\\green0\\blue0;}" +
  "{\\stylesheet{\\s0 Normal;}{\\s1\\outlinelevel0 heading 1;}{\\s2\\outlinelevel1 heading 2;}{\\s3 Overskrift 3;}}" +
  "{\\*\\generator Microsoft Word;}";

describe("LetterEditorActions.paste - format: text/rtf", () => {
  describe("native Word RTF", () => {
    test("headings and paragraph", () => {
      const rtf = `${WORD_HEADER}\\pard\\s1 T1\\par\\pard\\s2 T2\\par\\pard\\s3 T3\\par\\pard\\s0 P1\\par}`;
      const result = pasteAtStart(clipboardOf({ "text/rtf": rtf }));

      expect(result.redigertBrev.blocks).toMatchObject([
        newTitle({ type: "TITLE1", content: [newLiteral({ editedText: "T1" })] }),
        newTitle({ type: "TITLE2", content: [newLiteral({ editedText: "T2" })] }),
        newTitle({ type: "TITLE3", content: [newLiteral({ editedText: "T3" })] }),
        newParagraph({ content: [newLiteral({ editedText: "P1" })] }),
        initialState().redigertBrev.blocks[0],
      ]);
    });

    test("bold and italic", () => {
      expectRtfEquivalentToHtml(
        `${WORD_HEADER}\\pard Vanlig {\\b fet} og {\\i kursiv}\\par}`,
        "<p>Vanlig <b>fet</b> og <i>kursiv</i></p>",
      );
    });

    test("inline formatted text without paragraph break merges into current literal", () => {
      expectRtfEquivalentToHtml(`${WORD_HEADER}{\\b Fet }}`, "<b>Fet </b>");
    });

    test("bullet list", () => {
      expectRtfEquivalentToHtml(
        WORD_HEADER +
          "\\pard\\ls1\\ilvl0{\\listtext\\'b7\\tab}Ett\\par" +
          "\\pard\\ls1\\ilvl0{\\listtext\\'b7\\tab}To\\par}",
        "<ul><li>Ett</li><li>To</li></ul>",
      );
    });

    test("numbered list", () => {
      expectRtfEquivalentToHtml(
        WORD_HEADER +
          "\\pard\\ls2\\ilvl0{\\listtext 1.\\tab}Ett\\par" +
          "\\pard\\ls2\\ilvl0{\\listtext 2.\\tab}To\\par}",
        "<ol><li>Ett</li><li>To</li></ol>",
      );
    });

    test("table", () => {
      expectRtfEquivalentToHtml(
        WORD_HEADER +
          "\\trowd\\cellx3000\\cellx6000\\pard\\intbl Navn\\cell\\pard\\intbl Bel\\'f8p\\cell\\row" +
          "\\trowd\\cellx3000\\cellx6000\\pard\\intbl Ola\\cell\\pard\\intbl 100\\cell\\row" +
          "\\pard\\par}",
        "<table><tr><td>Navn</td><td>Beløp</td></tr><tr><td>Ola</td><td>100</td></tr></table>",
      );
    });

    test("Norwegian characters via hex and unicode escapes", () => {
      const result = pasteAtStart(clipboardOf({ "text/rtf": `${WORD_HEADER}Bl\\'e5b\\u230?rsyltet\\u248?y}` }));
      expect(text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }))).toEqual(
        "BlåbærsyltetøyTeksten min",
      );
    });
  });

  describe("Outlook RTF-encapsulated content", () => {
    test("\\fromhtml1 is unwrapped and pasted as html", () => {
      const rtf =
        "{\\rtf1\\ansi\\ansicpg1252\\fromhtml1\\deff0{\\fonttbl{\\f0\\fswiss Arial;}}" +
        "{\\*\\htmltag19 <html>}{\\*\\htmltag34 <body>}" +
        "{\\*\\htmltag64 <h1>}Tittel{\\*\\htmltag72 </h1>}\\htmlrtf\\par\\htmlrtf0" +
        "{\\*\\htmltag64 <p>}Hei {\\*\\htmltag84 <b>}verden{\\*\\htmltag92 </b>}{\\*\\htmltag72 </p>}" +
        "{\\*\\htmltag42 </body>}{\\*\\htmltag27 </html>}}";
      expectRtfEquivalentToHtml(rtf, "<h1>Tittel</h1><p>Hei <b>verden</b></p>");
    });

    test("\\fromtext1 is unwrapped and pasted as plain text", () => {
      const rtf = "{\\rtf1\\ansi\\fromtext1 {\\fonttbl{\\f0 Courier;}}Hei p\\'e5 deg }";
      const result = pasteAtStart(clipboardOf({ "text/rtf": rtf }));
      expect(text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }))).toEqual("Hei på deg Teksten min");
    });
  });

  describe("clipboard type priority and fallback", () => {
    test("text/html wins over text/rtf when both are present", () => {
      const result = pasteAtStart(
        clipboardOf({ "text/html": "<span>fra html </span>", "text/rtf": "{\\rtf1 fra rtf }" }),
      );
      expect(text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }))).toEqual("fra html Teksten min");
    });

    test("text/rtf wins over text/plain", () => {
      const fromRtf = pasteAtStart(clipboardOf({ "text/rtf": "{\\rtf1 {\\b fet }}", "text/plain": "vanlig " }));
      const fromHtml = pasteAtStart(clipboardOf({ "text/html": "<b>fet </b>" }));
      expect(fromRtf.redigertBrev).toEqual(fromHtml.redigertBrev);
    });

    test("falls back to text/plain when RTF yields no content", () => {
      const result = pasteAtStart(
        clipboardOf({ "text/rtf": "{\\rtf1{\\fonttbl{\\f0 Arial;}}}", "text/plain": "reserve " }),
      );
      expect(text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }))).toEqual("reserve Teksten min");
    });

    test("images and other unsupported destinations are dropped, surrounding text kept", () => {
      const rtf = `${WORD_HEADER}\\pard F\\'f8r {\\*\\shppict{\\pict\\pngblip 89504e47}}etter\\par}`;
      expectRtfEquivalentToHtml(rtf, "<p>Før etter</p>");
    });
  });

  test("pasteReplacingSelection supports text/rtf", () => {
    const state = initialState();
    const selection = {
      start: { blockIndex: 0, contentIndex: 0, cursorPosition: 0 },
      end: { blockIndex: 0, contentIndex: 0, cursorPosition: 7 },
    };
    const result = Actions.pasteReplacingSelection(state, selection, clipboardOf({ "text/rtf": "{\\rtf1 Min}" }));
    expect(text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }))).toEqual("Min min");
  });
});
