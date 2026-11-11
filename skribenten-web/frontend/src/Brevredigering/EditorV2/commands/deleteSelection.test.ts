import { describe, expect, it } from "vitest";

import { type CellV2, type ColumnSpecV2, type ItemV2 } from "~/types/brevV2";
import {
  cell,
  column,
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
  texts,
  variable,
} from "~test/support/editorV2TestUtils";

import { type ContainerPath, caret, TITLE } from "../model/paths";
import { type LetterStateV2, run } from "../state/editorState";
import { deleteSelection } from "./deleteSelection";

const block = (index: number): ContainerPath => ({ kind: "block", block: index });
const itemPath = (index: number, itemIndex: number): ContainerPath => ({ kind: "item", block: index, item: itemIndex });
const cellPath = (index: number, rowIndex: number, cellIndex: number): ContainerPath => ({
  kind: "cell",
  block: index,
  row: rowIndex,
  cell: cellIndex,
});
const header = (index: number, columnIndex: number): ContainerPath => ({
  kind: "header",
  block: index,
  column: columnIndex,
});

function del(
  state: LetterStateV2,
  start: [ContainerPath, number, number],
  end: [ContainerPath, number, number],
): LetterStateV2 {
  return run(state, deleteSelection, { anchor: caret(...start), focus: caret(...end) });
}

function expectEmpty(container: CellV2 | ColumnSpecV2 | ItemV2) {
  expect(container.content).toHaveLength(1);
  expect(container.content[0].type).toBe("LITERAL");
  expect(texts(container.content)).toEqual([""]);
}

describe("deleteSelection", () => {
  describe("text blocks", () => {
    const state = stateV2(
      paragraph(literal("Første tittel for "), variable("2025"), literal(" brev")),
      paragraph(literal("Første setning i "), variable("første"), literal(" avsnitt.")),
      paragraph(literal("Første setning i "), variable("andre"), literal(" avsnitt.")),
    );
    const original = (index: number) => textBlockAt(state, index);

    it("deletes within one literal", () => {
      const result = del(state, [block(0), 0, 5], [block(0), 0, 8]);
      expect(texts(textBlockAt(result, 0).content)).toEqual(["Førstittel for ", "2025", " brev"]);
      expect(result.focus).toEqual(caret(block(0), 0, 5));
    });

    it("deletes everything between start and end content", () => {
      const result = del(state, [block(0), 0, 5], [block(0), 2, 2]);
      const paragraph = textBlockAt(result, 0);
      expect(texts(paragraph.content)).toEqual(["Først", "rev"]);
      expect(paragraph.deletedContent).toEqual([original(0).content[1].id]);
    });

    it("works with anchor after focus", () => {
      const result = run(state, deleteSelection, { anchor: caret(block(0), 2, 2), focus: caret(block(0), 0, 5) });
      expect(texts(textBlockAt(result, 0).content)).toEqual(["Først", "rev"]);
    });

    it("starts in variable and ends in literal, deletes variable and text in literal", () => {
      const result = del(state, [block(0), 1, 3], [block(0), 2, 2]);
      const paragraph = textBlockAt(result, 0);
      expect(texts(paragraph.content)).toEqual(["Første tittel for ", "rev"]);
      expect(paragraph.deletedContent).toEqual([original(0).content[1].id]);
    });

    it("starts in literal and ends in variable, deletes variable and text in literal", () => {
      const result = del(state, [block(0), 0, 8], [block(0), 1, 2]);
      const paragraph = textBlockAt(result, 0);
      expect(texts(paragraph.content)).toEqual(["Første t", " brev"]);
      expect(paragraph.deletedContent).toEqual([original(0).content[1].id]);
    });

    it("starts at offset 0 and ends in variable, deletes literal and variable", () => {
      const result = del(state, [block(0), 0, 0], [block(0), 1, 2]);
      const paragraph = textBlockAt(result, 0);
      expect(paragraph.content).toEqual([original(0).content[2]]);
      expect(paragraph.deletedContent).toEqual([original(0).content[0].id, original(0).content[1].id]);
    });

    it("ends at end of literal, deletes the literal", () => {
      const result = del(state, [block(0), 0, 5], [block(0), 2, 5]);
      const paragraph = textBlockAt(result, 0);
      expect(texts(paragraph.content)).toEqual(["Først"]);
      expect(paragraph.deletedContent).toEqual([original(0).content[1].id, original(0).content[2].id]);
    });

    it("ends in next block, joins the blocks", () => {
      const result = del(state, [block(0), 0, 5], [block(1), 2, 3]);
      const paragraph = textBlockAt(result, 0);
      expect(result.redigertBrev.blocks).toHaveLength(2);
      expect(texts(paragraph.content)).toEqual(["Først", "snitt."]);
      expect(paragraph.deletedContent).toEqual(
        expect.arrayContaining([original(0).content[1].id, original(0).content[2].id]),
      );
      expect(result.redigertBrev.deletedBlocks).toEqual([original(1).id]);
      expect(result.focus).toEqual(caret(block(0), 0, 5));
    });

    it("deletes blocks between start and end", () => {
      const result = del(state, [block(0), 0, 5], [block(2), 0, 3]);
      expect(result.redigertBrev.blocks).toHaveLength(1);
      expect(result.redigertBrev.deletedBlocks).toEqual(expect.arrayContaining([original(1).id, original(2).id]));
      expect(texts(textBlockAt(result, 0).content)).toEqual(["Først", "ste setning i ", "andre", " avsnitt."]);
    });

    it("selecting everything leaves one empty paragraph", () => {
      const result = del(state, [block(0), 0, 0], [block(2), 2, 9]);
      expect(result.redigertBrev.blocks).toHaveLength(1);
      const paragraph = textBlockAt(result, 0);
      expect(paragraph.type).toBe("PARAGRAPH");
      expect(paragraph.content).toHaveLength(1);
      expect(texts(paragraph.content)).toEqual([""]);
    });

    it("selecting all text in a block starting with a variable leaves an empty literal", () => {
      const result = del(stateV2(paragraph(variable("hei"), literal(" på deg"))), [block(0), 0, 0], [block(0), 1, 7]);
      const content = textBlockAt(result, 0).content;
      expect(content).toHaveLength(1);
      expect(content[0].type).toBe("LITERAL");
      expect(texts(content)).toEqual([""]);
    });

    it("collapsed selection is a no-op", () => {
      expect(del(state, [block(0), 0, 3], [block(0), 0, 3])).toBe(state);
    });

    it("selection from title into a block is ignored", () => {
      const result = del(stateOf(letterV2({ blocks: [paragraph(literal("abc"))] })), [TITLE, 0, 1], [block(0), 0, 1]);
      expect(texts(result.redigertBrev.title.text)).toEqual(["Tittel"]);
      expect(texts(textBlockAt(result, 0).content)).toEqual(["abc"]);
    });
  });

  describe("lists", () => {
    const items = (name: string) => [
      item(literal("første punkt i "), variable(name), literal(" punktliste")),
      item(literal("andre punkt i "), variable(name), literal(" punktliste")),
      item(literal("tredje punkt i "), variable(name), literal(" punktliste")),
    ];
    const state = stateV2(
      paragraph(literal("Første setning i "), variable("første"), literal(" avsnitt.")),
      list(...items("første")),
      paragraph(literal("etter første punktliste"), literal("også etter første punktliste.")),
      paragraph(literal("Første setning i "), variable("andre"), literal(" avsnitt.")),
      numberedList(...items("andre")),
    );
    const list1 = listAtIndex(state, 1);
    const list2 = listAtIndex(state, 4);

    it("start and end in same item", () => {
      const result = del(state, [itemPath(1, 0), 0, 7], [itemPath(1, 0), 2, 3]);
      const resultList = listAtIndex(result, 1);
      expect(resultList.items).toHaveLength(3);
      expect(texts(resultList.items[0].content)).toEqual(["første ", "nktliste"]);
      expect(resultList.items[0].deletedContent).toEqual([list1.items[0].content[1].id]);
    });

    it("end in next item joins the items", () => {
      const result = del(state, [itemPath(1, 0), 0, 7], [itemPath(1, 1), 1, 2]);
      const resultList = listAtIndex(result, 1);
      expect(resultList.items).toHaveLength(2);
      expect(resultList.deletedItems).toEqual([list1.items[1].id]);
      expect(texts(resultList.items[0].content)).toEqual(["første ", " punktliste"]);
      expect(resultList.items[0].deletedContent).toEqual(
        expect.arrayContaining([list1.items[0].content[1].id, list1.items[0].content[2].id]),
      );
    });

    it("deletes items between start and end", () => {
      const result = del(state, [itemPath(1, 0), 0, 7], [itemPath(1, 2), 0, 2]);
      const resultList = listAtIndex(result, 1);
      expect(resultList.items).toHaveLength(1);
      expect(resultList.deletedItems).toEqual(expect.arrayContaining([list1.items[1].id, list1.items[2].id]));
      expect(texts(resultList.items[0].content)).toEqual(["første ", "edje punkt i ", "første", " punktliste"]);
    });

    it("including the entire start item keeps the end item", () => {
      const result = del(state, [itemPath(1, 0), 0, 0], [itemPath(1, 2), 0, 3]);
      const resultList = listAtIndex(result, 1);
      expect(resultList.items).toHaveLength(1);
      expect(resultList.items[0].id).toBe(list1.items[2].id);
      expect(resultList.deletedItems).toEqual(expect.arrayContaining([list1.items[0].id, list1.items[1].id]));
      expect(texts(resultList.items[0].content)).toEqual(["dje punkt i ", "første", " punktliste"]);
    });

    it("from item into following paragraph joins the paragraph into the item", () => {
      const result = del(state, [itemPath(1, 1), 0, 7], [block(2), 1, 2]);
      const resultList = listAtIndex(result, 1);
      expect(resultList.items).toHaveLength(2);
      expect(resultList.deletedItems).toEqual([list1.items[2].id]);
      expect(texts(resultList.items[1].content)).toEqual(["andre p", "så etter første punktliste."]);
      expect(textBlockAt(result, 2).content).toEqual(textBlockAt(state, 3).content);
    });

    it("from paragraph into item joins the item into the paragraph", () => {
      const result = del(state, [block(0), 2, 3], [itemPath(1, 1), 0, 2]);
      expect(texts(textBlockAt(result, 0).content)).toEqual([
        "Første setning i ",
        "første",
        " av",
        "dre punkt i ",
        "første",
        " punktliste",
      ]);
      const resultList = listAtIndex(result, 1);
      expect(resultList.items).toHaveLength(1);
      expect(resultList.deletedItems).toEqual(expect.arrayContaining([list1.items[0].id, list1.items[1].id]));
    });

    it("selecting all text in an item keeps an empty item", () => {
      const result = del(state, [itemPath(1, 1), 0, 0], [itemPath(1, 1), 2, 11]);
      const resultList = listAtIndex(result, 1);
      expect(resultList.items).toHaveLength(3);
      expectEmpty(resultList.items[1]);
    });

    it("between lists in different blocks deletes everything in between and joins the items", () => {
      const result = del(state, [itemPath(1, 1), 0, 7], [itemPath(4, 1), 2, 3]);
      expect(result.redigertBrev.blocks).toHaveLength(3);
      const first = listAtIndex(result, 1);
      expect(first.items).toHaveLength(2);
      expect(first.deletedItems).toEqual([list1.items[2].id]);
      expect(texts(first.items[1].content)).toEqual(["andre p", "nktliste"]);
      const second = listAtIndex(result, 2);
      expect(second.items.map((i) => i.id)).toEqual([list2.items[2].id]);
      expect(second.deletedItems).toEqual(expect.arrayContaining([list2.items[0].id, list2.items[1].id]));
    });

    it("from start of list to after list deletes the entire list", () => {
      const result = del(state, [itemPath(1, 0), 0, 0], [block(2), 1, 2]);
      expect(result.redigertBrev.blocks).toHaveLength(4);
      expect(result.redigertBrev.deletedBlocks).toEqual([list1.id]);
      expect(texts(textBlockAt(result, 1).content)).toEqual(["så etter første punktliste."]);
      expect(result.focus).toEqual(caret(block(1), 0, 0));
    });

    it("selecting an entire list deletes it", () => {
      const result = del(state, [itemPath(1, 0), 0, 0], [itemPath(1, 2), 2, 11]);
      expect(result.redigertBrev.blocks).toHaveLength(4);
      expect(result.redigertBrev.deletedBlocks).toEqual([list1.id]);
      expect(result.focus).toEqual(caret(block(1), 0, 0));
    });

    it("selecting an entire list that is the only block leaves an empty paragraph", () => {
      const only = stateV2(list(...items("x")));
      const result = del(only, [itemPath(0, 0), 0, 0], [itemPath(0, 2), 2, 11]);
      expect(result.redigertBrev.blocks).toHaveLength(1);
      expect(texts(textBlockAt(result, 0).content)).toEqual([""]);
      expect(result.focus).toEqual(caret(block(0), 0, 0));
    });

    it("from start of a list into next paragraph deletes the list and keeps the rest of the paragraph", () => {
      const start = stateV2(list(...items("x")), paragraph(literal("hei")));
      const result = del(start, [itemPath(0, 0), 0, 0], [block(1), 0, 1]);
      expect(result.redigertBrev.blocks).toHaveLength(1);
      expect(texts(textBlockAt(result, 0).content)).toEqual(["ei"]);
    });

    it("from start of a paragraph to the end of the following list", () => {
      const result = del(state, [block(3), 0, 0], [itemPath(4, 2), 2, 11]);
      expect(result.redigertBrev.blocks).toHaveLength(4);
      expect(textBlockAt(result, 3).content).toHaveLength(1);
      expect(texts(textBlockAt(result, 3).content)).toEqual([""]);
    });

    it("joins same-type lists when the paragraph between them is removed", () => {
      const start = stateV2(
        list(item(literal("a1")), item(literal("a2"))),
        paragraph(literal("midt")),
        paragraph(literal("slutt")),
        list(item(literal("b1")), item(literal("b2"))),
      );
      const result = del(start, [block(1), 0, 0], [block(2), 0, 5]);
      expect(result.redigertBrev.blocks).toHaveLength(3);
      expect(texts(textBlockAt(result, 1).content)).toEqual([""]);
    });
  });

  describe("tables", () => {
    const rows = () => [
      row(
        cell(literal("rad "), variable("1"), literal(" celle 1")),
        cell(literal("rad "), variable("1"), literal(" celle 2")),
        cell(literal("rad "), variable("1"), literal(" celle 3")),
      ),
      row(
        cell(literal("rad "), variable("2"), literal(" celle 1")),
        cell(literal("rad "), variable("2"), literal(" celle 2")),
        cell(literal("rad "), variable("2"), literal(" celle 3")),
      ),
      row(
        cell(literal("rad "), variable("3"), literal(" celle 1")),
        cell(literal("rad "), variable("3"), literal(" celle 2")),
        cell(literal("rad "), variable("3"), literal(" celle 3")),
      ),
    ];
    const headers = () => [
      column(literal("kolonne "), variable("1"), literal(" i tabell")),
      column(literal("kolonne "), variable("2"), literal(" i tabell")),
      column(literal("kolonne "), variable("3"), literal(" i tabell")),
    ];
    const state = stateV2(
      paragraph(literal("Første setning i "), variable("første"), literal(" avsnitt.")),
      table(headers(), rows()),
      paragraph(literal(" etter tabell.")),
    );
    const table1 = tableAtIndex(state, 1);

    it("start and end in same cell", () => {
      const result = del(state, [cellPath(1, 0, 0), 0, 2], [cellPath(1, 0, 0), 2, 3]);
      expect(texts(tableAtIndex(result, 1).rows[0].cells[0].content)).toEqual(["ra", "lle 1"]);
    });

    it("start and end in different cells in same row clears the cells between", () => {
      const result = del(state, [cellPath(1, 0, 0), 0, 2], [cellPath(1, 0, 2), 2, 3]);
      const [first, middle, last] = tableAtIndex(result, 1).rows[0].cells;
      expect(texts(first.content)).toEqual(["ra"]);
      expectEmpty(middle);
      expect(texts(last.content)).toEqual(["lle 3"]);
      expect(result.focus).toEqual(caret(cellPath(1, 0, 0), 0, 2));
    });

    it("from header to last row clears everything between", () => {
      const result = del(state, [header(1, 0), 0, 3], [cellPath(1, 2, 1), 2, 3]);
      const resultTable = tableAtIndex(result, 1);
      expect(texts(resultTable.header.colSpec[0].content)).toEqual(["kol"]);
      for (const c of resultTable.header.colSpec.slice(1)) expectEmpty(c);
      for (const c of resultTable.rows.slice(0, 2).flatMap((r) => r.cells)) expectEmpty(c);
      expect(texts(resultTable.rows[2].cells[1].content)).toEqual(["lle 2"]);
      expect(resultTable.rows[2].cells[2]).toEqual(table1.rows[2].cells[2]);
    });

    it("from start of table to after it deletes the table", () => {
      const result = del(state, [header(1, 0), 0, 0], [block(2), 0, 2]);
      expect(result.redigertBrev.blocks).toHaveLength(2);
      expect(result.redigertBrev.deletedBlocks).toEqual([table1.id]);
      expect(texts(textBlockAt(result, 1).content)).toEqual(["tter tabell."]);
    });

    it("from start to end of table deletes the table", () => {
      const result = del(state, [header(1, 0), 0, 0], [cellPath(1, 2, 2), 2, 8]);
      expect(result.redigertBrev.blocks).toHaveLength(2);
      expect(result.redigertBrev.deletedBlocks).toEqual([table1.id]);
    });

    it("from before table to end of table deletes the table and text before", () => {
      const result = del(state, [block(0), 2, 5], [cellPath(1, 2, 2), 2, 8]);
      expect(result.redigertBrev.blocks).toHaveLength(2);
      expect(result.redigertBrev.deletedBlocks).toEqual([table1.id]);
      expect(texts(textBlockAt(result, 0).content)).toEqual(["Første setning i ", "første", " avsn"]);
      expect(texts(textBlockAt(result, 1).content)).toEqual([" etter tabell."]);
    });

    it("from inside table into next block clears the rest of the table", () => {
      const result = del(state, [cellPath(1, 0, 0), 0, 1], [block(2), 0, 6]);
      const resultTable = tableAtIndex(result, 1);
      expect(texts(resultTable.header.colSpec[0].content)).toEqual(texts(table1.header.colSpec[0].content));
      expect(texts(resultTable.rows[0].cells[0].content)).toEqual(["r"]);
      for (const c of resultTable.rows[0].cells.slice(1)) expectEmpty(c);
      for (const c of resultTable.rows.slice(1).flatMap((r) => r.cells)) expectEmpty(c);
      expect(texts(textBlockAt(result, 2).content)).toEqual([" tabell."]);
      expect(result.focus).toEqual(caret(cellPath(1, 0, 0), 0, 1));
    });
  });
});
