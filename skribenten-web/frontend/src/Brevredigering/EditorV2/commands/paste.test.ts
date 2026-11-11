import { describe, expect, test } from "vitest";

import { type TraversedElement } from "~/Brevredigering/LetterEditor/actions/traversedElement";
import { type BlockV2, type TextV2 } from "~/types/brevV2";
import {
  cell,
  column,
  fritekst,
  item,
  letterV2,
  list,
  listAtIndex,
  literal,
  numberedList,
  paragraph,
  row,
  stateOf,
  stateV2,
  table,
  tableAtIndex,
  textBlockAt,
  title2,
  variable,
} from "~test/support/editorV2TestUtils";
import { MockDataTransfer } from "~test/support/pasteTestUtils";

import { type ContainerPath, caret, TITLE } from "../model/paths";
import { type LetterStateV2, run } from "../state/editorState";
import { insertElements, paste } from "./paste";

const block = (index: number): ContainerPath => ({ kind: "block", block: index });
const itemPath = (index: number, itemIndex: number): ContainerPath => ({ kind: "item", block: index, item: itemIndex });
const cellPath = (index: number, rowIndex: number, cellIndex: number): ContainerPath => ({
  kind: "cell",
  block: index,
  row: rowIndex,
  cell: cellIndex,
});
const headerPath = (index: number, column: number): ContainerPath => ({ kind: "header", block: index, column });

function runs(content: TextV2[]): string {
  return content
    .map((text) => {
      const value = text.type === "LITERAL" ? (text.editedText ?? text.text) : text.text;
      const font = text.type === "LITERAL" ? (text.editedFontType ?? text.fontType) : text.fontType;
      if (value.length === 0 || font === "PLAIN") return value;
      return font === "BOLD" ? `**${value}**` : `_${value}_`;
    })
    .join("");
}

function projectBlock(block: BlockV2): string[] {
  switch (block.type) {
    case "ITEM_LIST":
      return block.items.map((i) => `• ${runs(i.content)}`);
    case "NUMBERED_LIST":
      return block.items.map((i) => `1. ${runs(i.content)}`);
    case "TABLE":
      return [
        "TABLE",
        `  th: ${block.header.colSpec.map((c) => runs(c.content)).join(" | ")}`,
        ...block.rows.map((r) => `  tr: ${r.cells.map((c) => runs(c.content)).join(" | ")}`),
      ];
    case "PARAGRAPH":
      return [`P: ${runs(block.content)}`];
    default:
      return [`${block.type}: ${runs(block.content)}`];
  }
}

const project = (state: LetterStateV2) => state.redigertBrev.blocks.flatMap(projectBlock);

function pasteAt(
  state: LetterStateV2,
  path: ContainerPath,
  content: number,
  offset: number,
  data: Record<string, string>,
) {
  return run(state, paste, caret(path, content, offset), new MockDataTransfer(data));
}

const html = (state: LetterStateV2, path: ContainerPath, content: number, offset: number, markup: string) =>
  pasteAt(state, path, content, offset, { "text/html": markup });

const plain = (state: LetterStateV2, path: ContainerPath, content: number, offset: number, text: string) =>
  pasteAt(state, path, content, offset, { "text/plain": text });

const textState = () => stateV2(paragraph(literal({ id: 11, text: "Teksten min" })));
const listState = () =>
  stateV2(list(item(literal("en")), item(literal({ id: 21, text: "Teksten min" })), item(literal("tre"))));

describe("paste", () => {
  describe("plain text", () => {
    test.each([
      [0, "abcTeksten min", 3],
      [7, "Tekstenabc min", 10],
      [11, "Teksten minabc", 14],
    ])("into paragraph at offset %i", (offset, expected, focusOffset) => {
      const result = plain(textState(), block(0), 0, offset, "abc");

      expect(project(result)).toEqual([`P: ${expected}`]);
      expect(textBlockAt(result, 0).content[0]).toMatchObject({ id: 11, editedText: expected });
      expect(result.focus).toEqual(caret(block(0), 0, focusOffset));
    });

    test("into list item", () => {
      const result = plain(listState(), itemPath(0, 1), 0, 7, "abc");

      expect(project(result)).toEqual(["• en", "• Tekstenabc min", "• tre"]);
      expect(result.focus).toEqual(caret(itemPath(0, 1), 0, 10));
    });

    test("line breaks become spaces", () => {
      const result = plain(textState(), block(0), 0, 0, "a\r\nb ");

      expect(project(result)).toEqual(["P: a b Teksten min"]);
    });

    test("empty text is ignored", () => {
      const state = textState();

      expect(plain(state, block(0), 0, 0, "")).toBe(state);
    });

    test("next to a variable", () => {
      const state = stateV2(paragraph(literal("før "), variable("var"), literal(" etter")));
      const result = plain(state, block(0), 0, 4, "abc");

      expect(project(result)).toEqual(["P: før abcvar etter"]);
      expect(result.focus).toEqual(caret(block(0), 0, 7));
    });

    test("with the caret on a variable is ignored", () => {
      const state = stateV2(paragraph(literal("før "), variable("var")));

      expect(project(plain(state, block(0), 1, 0, "abc"))).toEqual(["P: før var"]);
    });

    test("into fritekst replaces the placeholder font but keeps fritekst", () => {
      const state = stateV2(paragraph(literal("før "), fritekst("fyll inn"), literal(" etter")));
      const result = plain(state, block(0), 1, 4, "abc");

      expect(project(result)).toEqual(["P: før fyllabc inn etter"]);
      expect(textBlockAt(result, 0).content[1]).toMatchObject({ editBehaviour: "FRITEKST" });
    });
  });

  describe("inline html", () => {
    test.each([
      [0, "1Teksten min"],
      [7, "Teksten1 min"],
      [11, "Teksten min1"],
    ])("span at offset %i extends the literal", (offset, expected) => {
      const result = html(textState(), block(0), 0, offset, "<span>1</span>");

      expect(project(result)).toEqual([`P: ${expected}`]);
      expect(textBlockAt(result, 0).content).toHaveLength(1);
      expect(result.focus).toEqual(caret(block(0), 0, offset + 1));
    });

    test("bold at the start becomes a new literal before", () => {
      const result = html(textState(), block(0), 0, 0, "<b>fet</b>");

      expect(project(result)).toEqual(["P: **fet**Teksten min"]);
      expect(textBlockAt(result, 0).content[1]).toMatchObject({ id: 11 });
      expect(result.focus).toEqual(caret(block(0), 0, 3));
    });

    test("italic in the middle splits the literal", () => {
      const result = html(textState(), block(0), 0, 7, "<i>kursiv</i>");

      expect(project(result)).toEqual(["P: Teksten_kursiv_ min"]);
      expect(textBlockAt(result, 0).content.map((c) => c.id)).toEqual([11, null, null]);
      expect(result.focus).toEqual(caret(block(0), 1, 6));
    });

    test("bold at the end becomes a new literal after", () => {
      const result = html(textState(), block(0), 0, 11, "<strong>fet</strong>");

      expect(project(result)).toEqual(["P: Teksten min**fet**"]);
      expect(result.focus).toEqual(caret(block(0), 1, 3));
    });

    test("mixed fonts are inserted in order", () => {
      const result = html(textState(), block(0), 0, 7, "<span>a</span><b>b</b><span>c</span>");

      expect(project(result)).toEqual(["P: Teksten" + "a**b**c" + " min"]);
      expect(result.focus).toEqual(caret(block(0), 2, 1));
    });
  });

  describe("paragraphs into a paragraph", () => {
    test("single paragraph at the start", () => {
      const state = stateV2({ ...paragraph(literal({ id: 10, text: "Teksten min" })), id: 1 });
      const result = html(state, block(0), 0, 0, "<p>1</p>");

      expect(project(result)).toEqual(["P: 1", "P: Teksten min"]);
      expect(result.redigertBrev.blocks.map((b) => b.id)).toEqual([null, 1]);
      expect(textBlockAt(result, 1).content[0]).toMatchObject({ id: 10 });
      expect(result.focus).toEqual(caret(block(1), 0, 0));
      expect(result.redigertBrev.deletedBlocks).toEqual([]);
    });

    test("single paragraph in the middle", () => {
      const result = html(textState(), block(0), 0, 7, "<p>1</p>");

      expect(project(result)).toEqual(["P: Teksten1", "P:  min"]);
      expect(result.focus).toEqual(caret(block(1), 0, 0));
    });

    test("single paragraph at the end", () => {
      const result = html(textState(), block(0), 0, 11, "<p>1</p>");

      expect(project(result)).toEqual(["P: Teksten min1", "P: "]);
      expect(result.focus).toEqual(caret(block(1), 0, 0));
    });

    test("multiple paragraphs in the middle", () => {
      const result = html(textState(), block(0), 0, 7, "<p>1</p><p>2</p><p>3</p>");

      expect(project(result)).toEqual(["P: Teksten1", "P: 2", "P: 3", "P:  min"]);
      expect(result.focus).toEqual(caret(block(3), 0, 0));
    });

    test("empty paragraphs are kept as empty blocks", () => {
      const result = html(textState(), block(0), 0, 0, "<p>a</p><p></p><p>b</p>");

      expect(project(result)).toEqual(["P: a", "P: ", "P: b", "P: Teksten min"]);
    });

    test("text before and after a paragraph", () => {
      const result = html(textState(), block(0), 0, 7, "tekst<p>p1</p>etter");

      expect(project(result)).toEqual(["P: Teksten" + "tekstp1", "P: etter min"]);
    });

    test("paragraph with variables keeps the variables in the tail", () => {
      const state = stateV2(paragraph(literal("før "), variable("var"), literal(" etter")));
      const result = html(state, block(0), 0, 2, "<p>x</p><p>y</p>");

      expect(project(result)).toEqual(["P: føx", "P: y", "P: r var etter"]);
    });
  });

  describe("headings", () => {
    test.each([
      ["h1", "TITLE2"],
      ["h2", "TITLE3"],
      ["h3", "TITLE4"],
    ])("%s at the start of a paragraph becomes %s", (tag, type) => {
      const result = html(textState(), block(0), 0, 0, `<${tag}>Tittel</${tag}>`);

      expect(project(result)).toEqual([`${type}: Tittel`, "P: Teksten min"]);
      expect(result.focus).toEqual(caret(block(1), 0, 0));
    });

    test("into an empty paragraph changes its type", () => {
      const state = stateV2({ ...paragraph(literal({ id: 21, text: "" })), id: 2 });
      const result = html(state, block(0), 0, 0, "<h1>Tittel</h1>");

      expect(project(result)).toEqual(["TITLE2: Tittel", "P: "]);
      expect(result.redigertBrev.blocks[0].id).toBe(2);
    });

    test("in the middle of a paragraph splits it", () => {
      const result = html(textState(), block(0), 0, 7, "<h2>Tittel</h2><p>tekst</p>");

      expect(project(result)).toEqual(["P: TekstenTittel", "P: tekst", "P:  min"]);
    });
  });

  describe("lists into a paragraph", () => {
    test.each([
      ["ul", "•"],
      ["ol", "1."],
    ])("%s at the start", (tag, marker) => {
      const result = html(textState(), block(0), 0, 0, `<${tag}><li>1</li><li>2</li></${tag}>`);

      expect(project(result)).toEqual([`${marker} 1`, `${marker} 2`, `${marker} `, "P: Teksten min"]);
      expect(textBlockAt(result, 1).content[0]).toMatchObject({ id: 11 });
      expect(result.focus).toEqual(caret(itemPath(0, 2), 0, 0));
    });

    test("in the middle", () => {
      const result = html(textState(), block(0), 0, 7, "<ul><li>1</li><li>2</li></ul>");

      expect(project(result)).toEqual(["• Teksten1", "• 2", "• ", "P:  min"]);
      expect(result.focus).toEqual(caret(itemPath(0, 2), 0, 0));
    });

    test("paragraph followed by list, Google Docs style", () => {
      const state = stateV2({ ...paragraph(literal("Hei")), id: 1 });
      const result = html(
        state,
        block(0),
        0,
        0,
        "<div><p><span><span>Punktliste</span></span><span> </span></p></div><div><ul><li><p><span><span>Første punkt</span></span><span> </span></p></li></ul></div><div><ul><li><p><span><span>Andre punkt</span></span><span> </span></p></li></ul></div>",
      );

      expect(project(result)).toEqual(["P: Punktliste ", "• Første punkt ", "• Andre punkt ", "• ", "P: Hei"]);
      expect(result.redigertBrev.blocks[2].id).toBe(1);
      expect(result.focus).toEqual(caret(itemPath(1, 2), 0, 0));
    });

    test("items continue a preceding list of the same type", () => {
      const state = stateV2(list(item(literal("a"))), paragraph(literal("")));
      const result = html(state, block(1), 0, 0, "<ul><li>b</li></ul>");

      expect(project(result)).toEqual(["• a", "• b", "• ", "P: "]);
    });

    test("nested lists are flattened into the outer list", () => {
      const result = html(textState(), block(0), 0, 0, "<ul><li>A<ol><li>B</li></ol></li><li>C</li></ul>");

      expect(project(result)).toEqual(["• A", "• B", "• C", "• ", "P: Teksten min"]);
    });

    test("paragraph after a list leaves the list", () => {
      const result = html(textState(), block(0), 0, 0, "<ul><li>1</li></ul><p>etter</p>");

      expect(project(result)).toEqual(["• 1", "P: etter", "P: Teksten min"]);
    });
  });

  describe("into a list item", () => {
    test("text keeps the item", () => {
      const result = html(listState(), itemPath(0, 1), 0, 0, "<span>x</span>");

      expect(project(result)).toEqual(["• en", "• xTeksten min", "• tre"]);
      expect(listAtIndex(result, 0).items[1].content[0]).toMatchObject({ id: 21 });
    });

    test("paragraphs become items", () => {
      const result = html(listState(), itemPath(0, 1), 0, 7, "<p>1</p><p>2</p>");

      expect(project(result)).toEqual(["• en", "• Teksten1", "• 2", "•  min", "• tre"]);
      expect(result.focus).toEqual(caret(itemPath(0, 3), 0, 0));
    });

    test("items at the start are inserted before", () => {
      const result = html(listState(), itemPath(0, 1), 0, 0, "<ul><li>1</li><li>2</li></ul>");

      expect(project(result)).toEqual(["• en", "• 1", "• 2", "• Teksten min", "• tre"]);
      expect(result.focus).toEqual(caret(itemPath(0, 3), 0, 0));
    });

    test("items in the middle split the item", () => {
      const result = html(listState(), itemPath(0, 1), 0, 7, "<ul><li>1</li><li>2</li></ul>");

      expect(project(result)).toEqual(["• en", "• Teksten1", "• 2", "•  min", "• tre"]);
    });

    test("items at the end", () => {
      const result = html(listState(), itemPath(0, 1), 0, 11, "<ul><li>1</li><li>2</li></ul>");

      expect(project(result)).toEqual(["• en", "• Teksten min1", "• 2", "• ", "• tre"]);
    });

    test("the list type of the existing list is kept", () => {
      const state = stateV2(numberedList(item(literal("a"))));
      const result = html(state, itemPath(0, 0), 0, 1, "<ul><li>b</li></ul>");

      expect(project(result)).toEqual(["1. ab", "1. "]);
    });

    test("headings become items", () => {
      const result = html(listState(), itemPath(0, 1), 0, 0, "<h1>Tittel</h1>");

      expect(project(result)).toEqual(["• en", "• Tittel", "• Teksten min", "• tre"]);
    });
  });

  describe("tables", () => {
    const tableHtml = "<table><tr><th>A</th><th>B</th></tr><tr><td>1</td><td>2</td></tr></table>";

    test("at the start of a paragraph is inserted before it", () => {
      const result = html(textState(), block(0), 0, 0, tableHtml);

      expect(project(result)).toEqual(["TABLE", "  th: A | B", "  tr: 1 | 2", "P: Teksten min"]);
      expect(result.focus).toEqual(caret(cellPath(0, 0, 0), 0, 0));
    });

    test("in the middle of a paragraph splits it", () => {
      const result = html(textState(), block(0), 0, 7, tableHtml);

      expect(project(result)).toEqual(["P: Teksten", "TABLE", "  th: A | B", "  tr: 1 | 2", "P:  min"]);
      expect(result.focus).toEqual(caret(cellPath(1, 0, 0), 0, 0));
    });

    test("at the end of a paragraph is inserted after it", () => {
      const result = html(textState(), block(0), 0, 11, tableHtml);

      expect(project(result)).toEqual(["P: Teksten min", "TABLE", "  th: A | B", "  tr: 1 | 2"]);
    });

    test("in an empty paragraph", () => {
      const result = html(stateV2(paragraph(literal(""))), block(0), 0, 0, tableHtml);

      expect(project(result)).toEqual(["TABLE", "  th: A | B", "  tr: 1 | 2", "P: "]);
    });

    test("without header uses the first row as header", () => {
      const result = html(textState(), block(0), 0, 0, "<table><tr><td>A</td></tr><tr><td>1</td></tr></table>");

      expect(project(result).slice(0, 3)).toEqual(["TABLE", "  th: A", "  tr: 1"]);
    });

    test("ragged rows are padded", () => {
      const result = html(
        textState(),
        block(0),
        0,
        0,
        "<table><tr><th>A</th><th>B</th></tr><tr><td>1</td></tr></table>",
      );

      expect(project(result).slice(0, 3)).toEqual(["TABLE", "  th: A | B", "  tr: 1 | "]);
      expect(tableAtIndex(result, 0).rows[0].cells).toHaveLength(2);
    });

    test("with only a header focuses the header", () => {
      const result = html(textState(), block(0), 0, 0, "<table><tr><th>A</th></tr></table>");

      expect(result.focus).toEqual(caret(headerPath(0, 0), 0, 0));
    });

    test("without cells falls back to text/plain", () => {
      const result = pasteAt(textState(), block(0), 0, 0, { "text/html": "<table></table>", "text/plain": "x" });

      expect(project(result)).toEqual(["P: xTeksten min"]);
    });

    test("followed by a paragraph", () => {
      const result = html(textState(), block(0), 0, 0, `${tableHtml}<p>etter</p>`);

      expect(project(result)).toEqual(["TABLE", "  th: A | B", "  tr: 1 | 2", "P: etter", "P: Teksten min"]);
    });

    test("in a list item splits the list", () => {
      const result = html(listState(), itemPath(0, 1), 0, 7, tableHtml);

      expect(project(result)).toEqual(["• en", "• Teksten", "TABLE", "  th: A | B", "  tr: 1 | 2", "•  min", "• tre"]);
      expect(result.focus).toEqual(caret(cellPath(1, 0, 0), 0, 0));
    });

    test("at the start of the first item replaces nothing", () => {
      const state = stateV2(list(item(literal("en")), item(literal("to"))));
      const result = html(state, itemPath(0, 0), 0, 0, tableHtml);

      expect(project(result)).toEqual(["TABLE", "  th: A | B", "  tr: 1 | 2", "• en", "• to"]);
    });

    test("in a title is rejected", () => {
      const state = stateV2(title2(literal("Tittel")));
      const result = pasteAt(state, block(0), 0, 6, { "text/html": tableHtml, "text/plain": "x" });

      expect(project(result)).toEqual(["TITLE2: Tittelx"]);
    });
  });

  describe("into a table cell", () => {
    const cellState = () => stateV2(table([column(literal("H"))], [row(cell(literal("celle")))]));

    test("text is inserted", () => {
      const result = html(cellState(), cellPath(0, 0, 0), 0, 2, "<b>x</b>");

      expect(project(result)).toEqual(["TABLE", "  th: H", "  tr: ce**x**lle"]);
    });

    test("paragraphs are flattened to text", () => {
      const result = html(cellState(), cellPath(0, 0, 0), 0, 5, "<p>a</p><p>b</p>");

      expect(project(result)).toEqual(["TABLE", "  th: H", "  tr: cellea b"]);
    });

    test("lists in the header fall back to text/plain", () => {
      const result = pasteAt(cellState(), headerPath(0, 0), 0, 1, {
        "text/html": "<ul><li>a</li></ul>",
        "text/plain": "x",
      });

      expect(project(result)).toEqual(["TABLE", "  th: Hx", "  tr: celle"]);
    });
  });

  describe("into the title", () => {
    test("paragraphs are flattened to text", () => {
      const state = stateOf(letterV2({ title: [literal("Tittel")], blocks: [paragraph(literal("x"))] }));
      const result = run(state, paste, caret(TITLE, 0, 6), new MockDataTransfer({ "text/html": "<p>a</p><p>b</p>" }));

      expect(runs(result.redigertBrev.title.text)).toEqual("Tittela b");
      expect(result.focus).toEqual(caret(TITLE, 0, 9));
    });
  });

  describe("clipboard formats", () => {
    test("text/html wins over rtf and text", () => {
      const result = pasteAt(textState(), block(0), 0, 0, {
        "text/html": "<span>html</span>",
        "text/rtf": "{\\rtf1 rtf}",
        "text/plain": "text",
      });

      expect(project(result)).toEqual(["P: htmlTeksten min"]);
    });

    test("text/rtf is used before text/plain", () => {
      const result = pasteAt(textState(), block(0), 0, 0, { "text/rtf": "{\\rtf1 rtf}", "text/plain": "text" });

      expect(project(result)).toEqual(["P: rtfTeksten min"]);
    });

    test("application/rtf is supported", () => {
      const result = pasteAt(textState(), block(0), 0, 0, { "application/rtf": "{\\rtf1 {\\b fet}}" });

      expect(project(result)).toEqual(["P: **fet**Teksten min"]);
    });

    test("nothing usable leaves the state unchanged", () => {
      const state = textState();

      expect(pasteAt(state, block(0), 0, 0, {})).toBe(state);
    });
  });

  describe("insertElements", () => {
    test("inserts parsed elements without a clipboard", () => {
      const elements: TraversedElement[] = [
        { type: "P", content: [{ type: "TEXT", font: "PLAIN", text: "a" }] },
        { type: "ITEM", content: [{ type: "TEXT", font: "PLAIN", text: "b" }], listType: "NUMMERERT_LISTE" },
      ];
      const result = run(textState(), insertElements, caret(block(0), 0, 0), elements);

      expect(project(result)).toEqual(["P: a", "1. b", "1. ", "P: Teksten min"]);
    });
  });
});
