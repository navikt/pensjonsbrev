import { describe, expect, test, vi } from "vitest";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { fontTypeOf, newLiteral, newParagraph, newTitle, text } from "~/Brevredigering/LetterEditor/actions/common";
import { logPastedClipboard } from "~/Brevredigering/LetterEditor/actions/paste";
import { type Focus, type LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { FontType, type ItemList, ListType, type LiteralValue, type ParagraphBlock } from "~/types/brevbakerTypes";
import outlook365 from "~test/fixtures/rtf/synthetic/outlook365-fromhtml.rtf?raw";
import word365 from "~test/fixtures/rtf/synthetic/word365-nb.rtf?raw";
import {
  cell,
  item,
  itemList,
  letter,
  literal,
  paragraph,
  row,
  select,
  table,
} from "~test/support/letterEditorTestUtils";
import { MockDataTransfer, projectLetter } from "~test/support/pasteTestUtils";

describe("LetterEditorActions.paste - RTF", () => {
  const HEADER = "{\\rtf1\\ansi\\ansicpg1252\\deflang1033";

  test("inserts a paragraph, replacing the matched text", () => {
    const index = { blockIndex: 0, contentIndex: 0 };
    const state = letter(paragraph({ id: 1, content: [literal({ text: "Teksten min" })] }));
    const clipboard = new MockDataTransfer({ "text/rtf": `${HEADER} Ny tekst\\par}` });

    const result = Actions.paste(state, index, 0, clipboard);

    expect(text(select<LiteralValue>(result, index))).toEqual("Ny tekst");
    expect(text(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }))).toEqual("Teksten min");
  });

  test("preserves bold and italic runs", () => {
    const index = { blockIndex: 0, contentIndex: 0 };
    const state = letter(paragraph({ id: 1, content: [literal({ text: "tekst" })] }));
    const clipboard = new MockDataTransfer({
      "text/rtf": `${HEADER} Vanlig \\b fet\\b0  \\i kursiv\\i0 \\par}`,
    });

    const result = Actions.paste(state, index, 0, clipboard);

    const paragraphBlock = select<ParagraphBlock>(result, { blockIndex: 0 });
    const contents = paragraphBlock.content as LiteralValue[];
    expect(contents.map((c) => ({ text: text(c), font: fontTypeOf(c) }))).toEqual([
      { text: "Vanlig ", font: FontType.PLAIN },
      { text: "fet", font: FontType.BOLD },
      { text: " ", font: FontType.PLAIN },
      { text: "kursiv", font: FontType.ITALIC },
    ]);
  });

  test("inserts a bullet list", () => {
    const index = { blockIndex: 0, contentIndex: 0 };
    const state = letter(paragraph({ id: 1, content: [literal({ text: "tekst" })] }));
    const clipboard = new MockDataTransfer({
      "text/rtf": `${HEADER} {\\pntext\\'B7\\tab}Punkt en\\par{\\pntext\\'B7\\tab}Punkt to\\par}`,
    });

    const result = Actions.paste(state, index, 0, clipboard);

    const list = select<ItemList>(result, { blockIndex: 0, contentIndex: 0 });
    expect(list.listType).toBe(ListType.PUNKTLISTE);
    expect(
      text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 })),
    ).toBe("Punkt en");
    expect(
      text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 1, itemContentIndex: 0 })),
    ).toBe("Punkt to");
  });

  test("HTML on the clipboard takes priority over RTF when both are present", () => {
    const index = { blockIndex: 0, contentIndex: 0 };
    const state = letter(paragraph({ id: 1, content: [literal({ text: "tekst" })] }));
    const clipboard = new MockDataTransfer({
      "text/html": "<p>Fra HTML</p>",
      "text/rtf": `${HEADER} Fra RTF\\par}`,
    });

    const result = Actions.paste(state, index, 0, clipboard);

    expect(text(select<LiteralValue>(result, index))).toEqual("Fra HTML");
  });

  test("falls back from text/rtf to application/rtf", () => {
    const index = { blockIndex: 0, contentIndex: 0 };
    const state = letter(paragraph({ id: 1, content: [literal({ text: "tekst" })] }));
    const clipboard = new MockDataTransfer({ "application/rtf": `${HEADER} Fra application/rtf\\par}` });

    const result = Actions.paste(state, index, 0, clipboard);

    expect(text(select<LiteralValue>(result, index))).toEqual("Fra application/rtf");
  });

  test("keeps headings, lists, the table and formatting from a Word 365 paste", () => {
    const state = letter(paragraph({ id: 1, content: [literal({ text: "" })] }));
    const clipboard = new MockDataTransfer({ "text/rtf": word365, "text/plain": "ren tekst" });

    const result = Actions.paste(state, { blockIndex: 0, contentIndex: 0 }, 0, clipboard);

    expect(projectLetter(result)).toEqual([
      "H1: Vedtak om alderspensjon",
      "P: Vi har **innvilget** søknaden din om _alderspensjon_ fra 1. mai 2026.",
      "H2: Dette er grunnlaget for vedtaket",
      "• Du har fylt 67 år.",
      "• Du har bodd i Norge i minst fem år.",
      "1. Pensjonen blir utbetalt den 20. hver måned.",
      "1. Utbetaling i utlandet skjer i euro (€).",
      "P: Du skrev “jeg vil ha pensjon” i søknaden – det har vi tatt hensyn til. Ny linje i samme avsnitt.",
      "TABLE",
      "  th: **Periode** | **Beløp per måned**",
      "  tr: Fra 1. mai 2026 | kr 25 000",
      "  tr: Fra 1. mai 2027 | kr 25 500",
      "P: Med vennlig hilsen",
    ]);
  });

  test("unwraps HTML encapsulated in Outlook RTF (\\fromhtml1)", () => {
    const state = letter(paragraph({ id: 1, content: [literal({ text: "" })] }));
    const clipboard = new MockDataTransfer({ "text/rtf": outlook365, "text/plain": "ren tekst" });

    const result = Actions.paste(state, { blockIndex: 0, contentIndex: 0 }, 0, clipboard);

    expect(projectLetter(result)).toEqual([
      "H1: Vedtak om alderspensjon",
      "P: Vi har **innvilget** søknaden din om _alderspensjon_ fra 1. mai 2026.",
      "• Du har fylt 67 år.",
      "• Du har bodd i Norge i minst fem år.",
      "1. Pensjonen blir utbetalt den 20. hver måned.",
      "1. Utbetaling i utlandet skjer i euro (€).",
      "TABLE",
      "  th: **Periode** | **Beløp per måned**",
      "  tr: Fra 1. mai 2026 | kr 25 000",
      "P: Les mer på nav.no.",
    ]);
  });

  test("inserts plain text encapsulated in Outlook RTF (\\fromtext) as text", () => {
    const index = { blockIndex: 0, contentIndex: 0 };
    const state = letter(paragraph({ id: 1, content: [literal({ text: "" })] }));
    const clipboard = new MockDataTransfer({
      "text/rtf": "{\\rtf1\\ansi\\fromtext {\\*\\htmltag <b>}Hei\\htmlrtf \\b\\htmlrtf0  fra Outlook}",
    });

    const result = Actions.paste(state, index, 0, clipboard);

    expect(text(select<LiteralValue>(result, index))).toEqual("Hei fra Outlook");
  });

  test.each([
    ["empty RTF", ""],
    ["RTF without content", `${HEADER}{\\fonttbl{\\f0 Calibri;}}}`],
  ])("falls back to text/plain for %s", (_, rtf) => {
    const index = { blockIndex: 0, contentIndex: 0 };
    const state = letter(paragraph({ id: 1, content: [literal({ text: "" })] }));
    const clipboard = new MockDataTransfer({ "text/rtf": rtf, "text/plain": "ren tekst" });

    const result = Actions.paste(state, index, 0, clipboard);

    expect(text(select<LiteralValue>(result, index))).toEqual("ren tekst");
  });

  test("inserts a partial selection without a paragraph mark inline", () => {
    const state = letter(paragraph({ id: 1, content: [literal({ text: "Her har vi noe" })] }));
    const clipboard = new MockDataTransfer({ "text/rtf": `${HEADER}\\b  ikke}` });

    const result = Actions.paste(state, { blockIndex: 0, contentIndex: 0 }, 10, clipboard);

    expect(result.redigertBrev.blocks).toHaveLength(1);
    expect(projectLetter(result)).toEqual(["P: Her har vi** ikke** noe"]);
  });
});

function initialState(): LetterEditorState {
  return letter(paragraph({ id: 1, content: [literal({ id: 11, text: "Teksten min" })] }));
}

function pasteAtStart(clipboard: DataTransfer) {
  return Actions.paste(initialState(), { blockIndex: 0, contentIndex: 0 }, 0, clipboard);
}

// Pasting RTF should produce exactly the same letter as pasting the
// equivalent HTML, since both paths feed the same insertion logic.
function expectRtfEquivalentToHtml(rtf: string, html: string) {
  const fromRtf = pasteAtStart(new MockDataTransfer({ "text/rtf": rtf }));
  const fromHtml = pasteAtStart(new MockDataTransfer({ "text/html": html }));
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
      const result = pasteAtStart(new MockDataTransfer({ "text/rtf": rtf }));

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

    test("overlapping bold and italic keep the emphasis turned on first", () => {
      expectRtfEquivalentToHtml(
        `${WORD_HEADER}\\pard {\\i kursiv {\\b begge}} og {\\b fet {\\i begge}}\\par}`,
        "<p><i>kursiv <b>begge</b></i> og <b>fet <i>begge</i></b></p>",
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

    // Word marks sub-items with \ilvl; the list table gives each level its own number format.
    test("nested list items are flattened into the outer list, like nested HTML lists", () => {
      const listTables =
        "{\\*\\listtable{\\list{\\listlevel\\levelnfc23}{\\listlevel\\levelnfc0}\\listid5}}" +
        "{\\*\\listoverridetable{\\listoverride\\listid5\\listoverridecount0\\ls1}}";
      expectRtfEquivalentToHtml(
        WORD_HEADER +
          listTables +
          "\\pard\\ls1\\ilvl0{\\listtext\\'b7\\tab}Ett\\par" +
          "\\pard\\ls1\\ilvl1{\\listtext 1.\\tab}Under\\par" +
          "\\pard\\ls1\\ilvl0{\\listtext\\'b7\\tab}To\\par}",
        "<ul><li>Ett<ol><li>Under</li></ol></li><li>To</li></ul>",
      );
    });

    // Unlike HTML, a trailing blank RTF paragraph is trimmed, so the trailing `\pard\par` / `<p></p>` is left out.
    test("table", () => {
      expectRtfEquivalentToHtml(
        WORD_HEADER +
          "\\trowd\\cellx3000\\cellx6000\\pard\\intbl Navn\\cell\\pard\\intbl Bel\\'f8p\\cell\\row" +
          "\\trowd\\cellx3000\\cellx6000\\pard\\intbl Ola\\cell\\pard\\intbl 100\\cell\\row}",
        "<table><tr><td>Navn</td><td>Beløp</td></tr><tr><td>Ola</td><td>100</td></tr></table>",
      );
    });

    test("a header row narrower than the body is padded to the widest row", () => {
      expectRtfEquivalentToHtml(
        WORD_HEADER +
          "\\trowd\\trhdr\\cellx3000\\cellx6000\\pard\\intbl Navn\\cell\\pard\\intbl Bel\\'f8p\\cell\\row" +
          "\\trowd\\cellx2000\\cellx4000\\cellx6000\\pard\\intbl Ola\\cell\\pard\\intbl 100\\cell\\pard\\intbl kr\\cell\\row}",
        "<table><tr><th>Navn</th><th>Beløp</th></tr><tr><td>Ola</td><td>100</td><td>kr</td></tr></table>",
      );
      const result = pasteAtStart(
        new MockDataTransfer({
          "text/html":
            "<table><tr><th>Navn</th><th>Beløp</th></tr><tr><td>Ola</td><td>100</td><td>kr</td></tr></table>",
        }),
      );
      expect(projectLetter(result)).toEqual([
        "TABLE",
        "  th: Navn | Beløp | ",
        "  tr: Ola | 100 | kr",
        "P: Teksten min",
      ]);
    });

    test("a horizontally merged cell is one cell, like colspan in HTML", () => {
      expectRtfEquivalentToHtml(
        WORD_HEADER +
          "\\trowd\\clmgf\\cellx3000\\clmrg\\cellx6000\\cellx9000\\pard\\intbl Navn\\cell\\pard\\intbl \\cell\\pard\\intbl Sum\\cell\\row" +
          "\\trowd\\cellx3000\\cellx6000\\cellx9000\\pard\\intbl Ola\\cell\\pard\\intbl 100\\cell\\pard\\intbl kr\\cell\\row}",
        '<table><tr><td colspan="2">Navn</td><td>Sum</td></tr><tr><td>Ola</td><td>100</td><td>kr</td></tr></table>',
      );
    });

    test("a trailing paragraph without \\par is inserted inline, like trailing HTML text", () => {
      expectRtfEquivalentToHtml("{\\rtf1\\ansi\\pard First\\par\\pard No trailing par}", "<p>First</p>No trailing par");
    });

    test("an empty list item in the middle of a list keeps the list together", () => {
      expectRtfEquivalentToHtml(
        WORD_HEADER +
          "\\pard\\ls1{\\listtext\\'b7\\tab}a\\par" +
          "\\pard\\ls1{\\listtext\\'b7\\tab}\\par" +
          "\\pard\\ls1{\\listtext\\'b7\\tab}b\\par}",
        "<ul><li>a</li><li></li><li>b</li></ul>",
      );
    });

    test("a trailing empty list item is trimmed", () => {
      expectRtfEquivalentToHtml(
        `${WORD_HEADER}\\pard\\ls1{\\listtext\\'b7\\tab}a\\par\\pard\\ls1{\\listtext\\'b7\\tab}\\par}`,
        "<ul><li>a</li></ul>",
      );
    });

    test("empty paragraphs are kept as line breaks", () => {
      expectRtfEquivalentToHtml(
        `${WORD_HEADER}\\pard Første\\par\\pard\\par\\pard Andre\\par}`,
        "<p>Første</p><p></p><p>Andre</p>",
      );
    });

    test("a paragraph after a list goes before a list that followed it in the same block", () => {
      const state = letter(
        paragraph({
          id: 1,
          content: [
            itemList({ id: 10, items: [item(literal({ text: "a" }))] }),
            itemList({ id: 20, listType: ListType.NUMMERERT_LISTE, items: [item(literal({ text: "z" }))] }),
          ],
        }),
      );
      const rtf = `${WORD_HEADER}\\pard\\ls1{\\listtext\\'b7\\tab}x\\par\\pard y\\par}`;

      const result = Actions.paste(
        state,
        { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 },
        1,
        new MockDataTransfer({ "text/rtf": rtf }),
      );

      expect(projectLetter(result)).toEqual(["• ax", "P: y", "1. z"]);
    });

    test("Norwegian characters via hex and unicode escapes", () => {
      const result = pasteAtStart(
        new MockDataTransfer({ "text/rtf": `${WORD_HEADER}Bl\\'e5b\\u230?rsyltet\\u248?y}` }),
      );
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
      const result = pasteAtStart(new MockDataTransfer({ "text/rtf": rtf }));
      expect(text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }))).toEqual("Hei på deg Teksten min");
    });
  });

  describe("clipboard type priority and fallback", () => {
    test("text/html wins over text/rtf when both are present", () => {
      const result = pasteAtStart(
        new MockDataTransfer({ "text/html": "<span>fra html </span>", "text/rtf": "{\\rtf1 fra rtf }" }),
      );
      expect(text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }))).toEqual("fra html Teksten min");
    });

    test("text/rtf wins over text/plain", () => {
      const fromRtf = pasteAtStart(
        new MockDataTransfer({ "text/rtf": "{\\rtf1 {\\b fet }}", "text/plain": "vanlig " }),
      );
      const fromHtml = pasteAtStart(new MockDataTransfer({ "text/html": "<b>fet </b>" }));
      expect(fromRtf.redigertBrev).toEqual(fromHtml.redigertBrev);
    });

    test("falls back to text/plain when RTF yields no content", () => {
      const result = pasteAtStart(
        new MockDataTransfer({ "text/rtf": "{\\rtf1{\\fonttbl{\\f0 Arial;}}}", "text/plain": "reserve " }),
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
    const result = Actions.pasteReplacingSelection(
      state,
      selection,
      new MockDataTransfer({ "text/rtf": "{\\rtf1 Min}" }),
    );
    expect(text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }))).toEqual("Min min");
  });
});

// Both paths feed the same insertion logic, so RTF and the equivalent HTML must give the same letter and focus
// wherever the caret is. Each case pastes into the same state, so generated ids are identical.
describe("LetterEditorActions.paste - text/rtf matches text/html at every position", () => {
  const RTF_HEADER = "{\\rtf1\\ansi\\ansicpg1252{\\stylesheet{\\s0 Normal;}{\\s1\\outlinelevel0 heading 1;}}";

  const contents: [string, string, string][] = [
    ["inline text", `${RTF_HEADER}Vanlig {\\b fet}}`, "Vanlig <b>fet</b>"],
    [
      "formatted paragraphs",
      `${RTF_HEADER}\\pard Vanlig {\\b fet}\\par\\pard {\\i kursiv}\\par}`,
      "<p>Vanlig <b>fet</b></p><p><i>kursiv</i></p>",
    ],
    [
      "a heading and a paragraph",
      `${RTF_HEADER}\\pard\\s1 Tittel\\par\\pard Tekst\\par}`,
      "<h1>Tittel</h1><p>Tekst</p>",
    ],
    [
      "a list with a nested item",
      `${RTF_HEADER}\\pard\\ls1{\\listtext\\'b7\\tab}En\\par\\pard\\ls1\\ilvl1{\\listtext o\\tab}Under\\par` +
        "\\pard\\ls1{\\listtext\\'b7\\tab}To\\par}",
      "<ul><li>En<ul><li>Under</li></ul></li><li>To</li></ul>",
    ],
    [
      "a table",
      `${RTF_HEADER}\\trowd\\cellx1000\\cellx2000\\pard\\intbl A\\cell B\\cell\\row` +
        "\\trowd\\cellx1000\\cellx2000\\pard\\intbl C\\cell D\\cell\\row}",
      "<table><tr><td>A</td><td>B</td></tr><tr><td>C</td><td>D</td></tr></table>",
    ],
  ];

  const inParagraph = () => letter(paragraph({ id: 1, content: [literal({ id: 11, text: "Teksten min" })] }));
  const inList = () =>
    letter(
      paragraph({
        id: 1,
        content: [itemList({ id: 2, items: [item({ id: 3, content: [literal({ id: 31, text: "punkt en" })] })] })],
      }),
    );
  const inTable = () =>
    letter(
      paragraph([
        literal({ text: "" }),
        table(
          [cell(literal({ text: "H1" })), cell(literal({ text: "H2" }))],
          [row(cell(literal({ text: "celle" })), cell(literal({ text: "annen" })))],
        ),
        literal({ text: "" }),
      ]),
    );

  const positions: [string, () => LetterEditorState, Focus][] = [
    ["at the start of a paragraph", inParagraph, { blockIndex: 0, contentIndex: 0, cursorPosition: 0 }],
    ["in the middle of a paragraph", inParagraph, { blockIndex: 0, contentIndex: 0, cursorPosition: 7 }],
    ["at the end of a paragraph", inParagraph, { blockIndex: 0, contentIndex: 0, cursorPosition: 11 }],
    [
      "inside a list item",
      inList,
      { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0, cursorPosition: 5 },
    ],
    [
      "inside a table cell",
      inTable,
      { blockIndex: 0, contentIndex: 1, rowIndex: 0, cellIndex: 0, cellContentIndex: 0, cursorPosition: 2 },
    ],
  ];

  describe.each(positions)("%s", (_, createState, focus) => {
    test.each(contents)("%s", (name, rtf, html) => {
      const state = createState();
      const { cursorPosition, ...index } = focus;

      const fromRtf = Actions.paste(state, index, cursorPosition ?? 0, new MockDataTransfer({ "text/rtf": rtf }));
      const fromHtml = Actions.paste(state, index, cursorPosition ?? 0, new MockDataTransfer({ "text/html": html }));

      expect(fromRtf.redigertBrev).toEqual(fromHtml.redigertBrev);
      expect(fromRtf.focus).toEqual(fromHtml.focus);
      // Tables can only be pasted directly in a paragraph; everything else must change the letter.
      const insertable = name !== "a table" || !("cellIndex" in index || "itemIndex" in index);
      expect(JSON.stringify(fromRtf.redigertBrev) !== JSON.stringify(state.redigertBrev)).toBe(insertable);
    });
  });

  test.each(contents)("over a selection: %s", (_, rtf, html) => {
    const state = inParagraph();
    const selection = {
      start: { blockIndex: 0, contentIndex: 0, cursorPosition: 3 },
      end: { blockIndex: 0, contentIndex: 0, cursorPosition: 8 },
    };

    const fromRtf = Actions.pasteReplacingSelection(state, selection, new MockDataTransfer({ "text/rtf": rtf }));
    const fromHtml = Actions.pasteReplacingSelection(state, selection, new MockDataTransfer({ "text/html": html }));

    expect(fromRtf.redigertBrev).toEqual(fromHtml.redigertBrev);
    expect(fromRtf.focus).toEqual(fromHtml.focus);
    expect(projectLetter(fromRtf).join("\n")).not.toContain("Teksten min");
  });
});

describe("logPastedClipboard", () => {
  test("logs formats, lengths and metadata but never the pasted content", () => {
    const info = vi.spyOn(console, "info").mockImplementation(() => {});
    const rtf = "{\\rtf1\\ansi\\ansicpg1252{\\*\\generator Riched20 10.0.22621}Fødselsnummer 12345678901\\par}";

    logPastedClipboard(
      new MockDataTransfer({
        "text/html": "<p>Fødselsnummer 12345678901</p>",
        "text/rtf": rtf,
        "text/plain": "Fødselsnummer 12345678901",
      }),
    );

    const logged = JSON.stringify(info.mock.calls);
    expect(logged).not.toContain("12345678901");
    expect(info.mock.calls).toEqual([
      [
        "Skribenten:pasteHandler: pasted clipboard - ",
        expect.objectContaining({
          lengths: { "text/html": 32, "text/rtf": rtf.length, "text/plain": 25 },
          innholdsformat: "HTML",
          htmlTagger: "p",
          rtfKilde: "Riched20 10.0.22621",
          rtfKodetabell: "1252",
        }),
      ],
    ]);
    info.mockRestore();
  });
});
