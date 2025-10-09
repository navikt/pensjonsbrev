import { expect } from "vitest";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { text } from "~/Brevredigering/LetterEditor/actions/common";
import type { Cell, ItemList, LiteralValue, Row, Table } from "~/types/brevbakerTypes";
import { LITERAL } from "~/types/brevbakerTypes";

import { cell, item, itemList, letter, literal, paragraph, row, select, table, title1, variable } from "../utils";

describe("Actions.deleteSelection", () => {
  describe("start and end in TextContent", () => {
    const state = letter(
      title1(literal("Første tittel for "), variable("2025"), literal(" brev")),
      paragraph([literal("Første setning i "), variable("første"), literal(" avsnitt.")]),
      paragraph([literal("Første setning i "), variable("andre"), literal(" avsnitt.")]),
    );

    it("start and end in same content does nothing", () => {
      const result = Actions.deleteSelection(state, {
        start: { blockIndex: 0, contentIndex: 0, cursorPosition: 5 },
        end: { blockIndex: 0, contentIndex: 0, cursorPosition: 8 },
      });
      expect(result).toBe(state);
      expect(result).toEqual(state);
    });

    it("start and end in different content deletes everything between", () => {
      const result = Actions.deleteSelection(state, {
        start: { blockIndex: 0, contentIndex: 0, cursorPosition: 5 },
        end: { blockIndex: 0, contentIndex: 2, cursorPosition: 2 },
      });
      const block = result.redigertBrev.blocks[0];
      expect(block.content).toHaveLength(2);
      expect(block.deletedContent).toContain(state.redigertBrev.blocks[0].content[1].id);
      expect(block.content[0]).toMatchObject({ editedText: "Først" });
      expect(block.content[1]).toMatchObject({ editedText: "rev" });
    });

    it("starts in variable and ends in literal, deletes variable and text in literal", () => {
      const result = Actions.deleteSelection(state, {
        start: { blockIndex: 0, contentIndex: 1, cursorPosition: 3 },
        end: { blockIndex: 0, contentIndex: 2, cursorPosition: 2 },
      });
      const block = result.redigertBrev.blocks[0];
      expect(block.content).toHaveLength(2);
      expect(block.deletedContent).toContain(state.redigertBrev.blocks[0].content[1].id);
      expect(block.content[1]).toMatchObject({ editedText: "rev" });
    });

    it("starts in literal and ends in variable, deletes variable and text in literal", () => {
      const result = Actions.deleteSelection(state, {
        start: { blockIndex: 0, contentIndex: 0, cursorPosition: 8 },
        end: { blockIndex: 0, contentIndex: 1, cursorPosition: 2 },
      });
      const block = result.redigertBrev.blocks[0];
      expect(block.content).toHaveLength(2);
      expect(block.deletedContent).toContain(state.redigertBrev.blocks[0].content[1].id);
      expect(block.content[0]).toMatchObject({ editedText: "Første t" });
    });

    it("starts at offset 0 in literal and ends in variable, deletes variable and literal", () => {
      const result = Actions.deleteSelection(state, {
        start: { blockIndex: 0, contentIndex: 0, cursorPosition: 0 },
        end: { blockIndex: 0, contentIndex: 1, cursorPosition: 2 },
      });

      const block = result.redigertBrev.blocks[0];
      expect(block.content).toHaveLength(1);
      expect(block.deletedContent).toContain(state.redigertBrev.blocks[0].content[0].id);
      expect(block.deletedContent).toContain(state.redigertBrev.blocks[0].content[1].id);
      expect(block.content[0]).toBe(state.redigertBrev.blocks[0].content[2]);
    });

    it("starts in literal and ends at end of literal, deletes literal", () => {
      const result = Actions.deleteSelection(state, {
        start: { blockIndex: 0, contentIndex: 0, cursorPosition: 5 },
        end: { blockIndex: 0, contentIndex: 2, cursorPosition: 5 },
      });
      const block = result.redigertBrev.blocks[0];
      expect(block.content).toHaveLength(1);
      expect(block.deletedContent).toContain(state.redigertBrev.blocks[0].content[1].id);
      expect(block.deletedContent).toContain(state.redigertBrev.blocks[0].content[2].id);
      expect(block.content[0]).toMatchObject({ editedText: "Først" });
    });

    it("ends in next block", () => {
      const result = Actions.deleteSelection(state, {
        start: { blockIndex: 0, contentIndex: 0, cursorPosition: 5 },
        end: { blockIndex: 1, contentIndex: 2, cursorPosition: 3 },
      });
      const block1 = result.redigertBrev.blocks[0];
      expect(block1.content).toHaveLength(2);
      expect(block1.content[0]).toMatchObject({ editedText: "Først" });
      expect(block1.deletedContent).toContain(state.redigertBrev.blocks[0].content[1].id);
      expect(block1.deletedContent).toContain(state.redigertBrev.blocks[0].content[2].id);

      expect(result.redigertBrev.deletedBlocks).toContain(state.redigertBrev.blocks[1].id);
      expect(block1.content[1]).toMatchObject({ editedText: "snitt." });
    });

    it("deletes blocks in between start and end", () => {
      const result = Actions.deleteSelection(state, {
        start: { blockIndex: 0, contentIndex: 0, cursorPosition: 5 },
        end: { blockIndex: 2, contentIndex: 0, cursorPosition: 3 },
      });
      const block1 = result.redigertBrev.blocks[0];
      expect(result.redigertBrev.blocks).toHaveLength(1);
      expect(result.redigertBrev.deletedBlocks).toContain(state.redigertBrev.blocks[1].id);
      expect(result.redigertBrev.deletedBlocks).toContain(state.redigertBrev.blocks[2].id);

      expect(block1.content).toHaveLength(4);
      expect(block1.content[0]).toMatchObject({ editedText: "Først" });
      expect(block1.deletedContent).toContain(state.redigertBrev.blocks[0].content[1].id);
      expect(block1.deletedContent).toContain(state.redigertBrev.blocks[0].content[2].id);
      expect(block1.content[1]).toMatchObject({ editedText: "ste setning i " });
    });

    it("selects all text in letter, deletes everything but leaves an empty paragraph", () => {
      const result = Actions.deleteSelection(state, {
        start: { blockIndex: 0, contentIndex: 0, cursorPosition: 0 },
        end: { blockIndex: 2, contentIndex: 2, cursorPosition: 9 },
      });
      expect(result.redigertBrev.blocks).toHaveLength(1);
      const block = result.redigertBrev.blocks[0];
      expect(block.type).toBe("PARAGRAPH");
      expect(block.content).toHaveLength(1);
      expect(block.content[0].type).toBe(LITERAL);
      expect(text(block.content[0] as LiteralValue)).toBe("");
    });

    it("selects all text in block that starts with variable, ends up with empty literal", () => {
      const state = letter(paragraph([variable("hei"), literal(" på deg")]));
      const result = Actions.deleteSelection(state, {
        start: { blockIndex: 0, contentIndex: 0, cursorPosition: 0 },
        end: { blockIndex: 0, contentIndex: 1, cursorPosition: 7 },
      });
      expect(result.redigertBrev.blocks).toHaveLength(1);
      const block = result.redigertBrev.blocks[0];
      expect(block.type).toBe("PARAGRAPH");
      expect(block.content).toHaveLength(1);
      expect(block.content[0].type).toBe(LITERAL);
      expect(text(block.content[0] as LiteralValue)).toBe("");
    });
  });

  describe("selection in itemList", () => {
    const itemList1 = itemList({
      items: [
        item(literal("første punkt i "), variable("første"), literal(" punktliste")),
        item(literal("andre punkt i "), variable("første"), literal(" punktliste")),
        item(literal("tredje punkt i "), variable("første"), literal(" punktliste")),
      ],
    });
    const itemList2 = itemList({
      items: [
        item(literal("første punkt i "), variable("andre"), literal(" punktliste")),
        item(literal("andre punkt i "), variable("andre"), literal(" punktliste")),
        item(literal("tredje punkt i "), variable("andre"), literal(" punktliste")),
      ],
    });
    const state = letter(
      paragraph([
        literal("Første setning i "),
        variable("første"),
        literal(" avsnitt."),
        itemList1,
        literal("etter første punktliste"),
        literal("også etter første punktliste."),
      ]),
      paragraph([literal("Første setning i "), variable("andre"), literal(" avsnitt."), itemList2]),
    );

    it("start and end in same item, different content", () => {
      const result = Actions.deleteSelection(state, {
        start: { blockIndex: 0, contentIndex: 3, itemIndex: 0, itemContentIndex: 0, cursorPosition: 7 },
        end: { blockIndex: 0, contentIndex: 3, itemIndex: 0, itemContentIndex: 2, cursorPosition: 3 },
      });
      const list = select<ItemList>(result, { blockIndex: 0, contentIndex: 3 });
      const item = list.items[0];

      expect(list.items).toHaveLength(3);
      expect(item.content).toHaveLength(2);
      expect(item.content[0]).toMatchObject({ editedText: "første " });
      expect(item.deletedContent).toContain(itemList1.items[0].content[1].id);
      expect(item.content[1]).toMatchObject({ editedText: "nktliste" });
    });

    it("end in next item", () => {
      const result = Actions.deleteSelection(state, {
        start: { blockIndex: 0, contentIndex: 3, itemIndex: 0, itemContentIndex: 0, cursorPosition: 7 },
        end: { blockIndex: 0, contentIndex: 3, itemIndex: 1, itemContentIndex: 1, cursorPosition: 2 },
      });
      const list = select<ItemList>(result, { blockIndex: 0, contentIndex: 3 });
      expect(list.items).toHaveLength(2);
      expect(list.deletedItems).toContain(itemList1.items[1].id);

      const item = list.items[0];
      expect(item.content).toHaveLength(2);
      expect(item.content[0]).toMatchObject({ editedText: "første " });
      expect(item.deletedContent).toContain(itemList1.items[0].content[1].id);
      expect(item.deletedContent).toContain(itemList1.items[0].content[2].id);
      expect(item.content[1]).toMatchObject({ text: " punktliste" });
    });

    it("items in between start and end are deleted", () => {
      const result = Actions.deleteSelection(state, {
        start: { blockIndex: 0, contentIndex: 3, itemIndex: 0, itemContentIndex: 0, cursorPosition: 7 },
        end: { blockIndex: 0, contentIndex: 3, itemIndex: 2, itemContentIndex: 0, cursorPosition: 2 },
      });
      const list = select<ItemList>(result, { blockIndex: 0, contentIndex: 3 });
      expect(list.items).toHaveLength(1);
      expect(list.deletedItems).toContain(itemList1.items[1].id);
      expect(list.deletedItems).toContain(itemList1.items[2].id);

      const item = list.items[0];
      expect(item.content).toHaveLength(4);
      expect(item.content[0]).toMatchObject({ editedText: "første " });
      expect(item.deletedContent).toContain(itemList1.items[0].content[1].id);
      expect(item.deletedContent).toContain(itemList1.items[0].content[2].id);
      expect(item.content[1]).toMatchObject({ editedText: "edje punkt i " });
    });

    it("starts and ends in same list and includes entire start item", () => {
      const selection = {
        start: { blockIndex: 0, contentIndex: 3, itemIndex: 0, itemContentIndex: 0, cursorPosition: 0 },
        end: { blockIndex: 0, contentIndex: 3, itemIndex: 2, itemContentIndex: 0, cursorPosition: 3 },
      };
      const result = Actions.deleteSelection(state, selection);
      const list = select<ItemList>(result, { blockIndex: 0, contentIndex: 3 });
      expect(list.items).toHaveLength(1);
      expect(list.deletedItems).toContain(itemList1.items[0].id);
      expect(list.deletedItems).toContain(itemList1.items[1].id);

      const item = list.items[0];
      expect(item.content).toHaveLength(3);
      expect(item.content[0]).toMatchObject({ editedText: "dje punkt i " });
      expect(item.content.slice(1)).toEqual(itemList1.items[2].content.slice(1));
    });

    it("starts in itemList and ends in last content in block", () => {
      const selection = {
        start: { blockIndex: 0, contentIndex: 3, itemIndex: 1, itemContentIndex: 0, cursorPosition: 7 },
        end: { blockIndex: 0, contentIndex: 5, cursorPosition: 2 },
      };
      const result = Actions.deleteSelection(state, selection);
      const list = select<ItemList>(result, { blockIndex: 0, contentIndex: 3 });
      const block = result.redigertBrev.blocks[0];

      // intermediate items in itemList are removed
      expect(list.items).toHaveLength(2);
      expect(list.deletedItems).toContain(itemList1.items[2].id);

      // content is deleted in item
      const item = list.items[1];
      // TODO: should be 2 (is 1 because of how mergeRecipe currently works)
      expect(item.content).toHaveLength(1);
      expect(item.content[0]).toMatchObject({ editedText: "andre p" });
      expect(item.deletedContent).toContain(itemList1.items[1].content[1].id);
      expect(item.deletedContent).toContain(itemList1.items[1].content[2].id);

      // content between list and selection end is deleted
      expect(block.content).toHaveLength(5);
      expect(block.deletedContent).toContain(state.redigertBrev.blocks[0].content[4].id);

      // text content after itemlist is merged into itemList
      // TODO: Replace with comment once mergeRecipe is updated to properly move content into itemList
      expect(block.content.at(-1)).toMatchObject({ editedText: "så etter første punktliste." });
      // expect(item.content[1]).toMatchObject({ editedText: "så etter første punktliste." });
      // expect(block.deletedContent).toContain(select<Content>(state, selection.end).id);
    });

    it("starts in literal and ends in itemList in same block", () => {
      const selection = {
        start: { blockIndex: 0, contentIndex: 2, cursorPosition: 3 },
        end: { blockIndex: 0, contentIndex: 3, itemIndex: 1, itemContentIndex: 0, cursorPosition: 2 },
      };
      const result = Actions.deleteSelection(state, selection);
      const block = result.redigertBrev.blocks[0];
      expect(block.content).toHaveLength(6);

      expect(block.content[2]).toMatchObject({ editedText: " av" });

      const list = select<ItemList>(result, { blockIndex: 0, contentIndex: 3 });
      expect(list.items).toHaveLength(2);
      expect(list.deletedItems).toContain(itemList1.items[0].id);
      expect(list.items[0].content).toHaveLength(3);
      expect(list.items[0].content[0]).toMatchObject({ editedText: "dre punkt i " });

      // TODO: When mergeRecipe is updated to properly move content out of itemList, this test should be updated with the following expectations:
      // expect(block.content).toHaveLength(9);
      //
      // const list = select<ItemList>(result, { blockIndex: 0, contentIndex: 3 });
      // expect(list.items).toHaveLength(1);
      // expect(list.deletedItems).toContain(itemList1.items[0].id);
      // expect(list.deletedItems).toContain(itemList1.items[1].id);
      //
      // expect(block.content.slice(3, 6)).toEqual(itemList1.items[1].content);
    });

    it("all the text in an item is selected, text is deleted but item still has at least one literal", () => {
      const selection = {
        start: { blockIndex: 0, contentIndex: 3, itemIndex: 1, itemContentIndex: 0, cursorPosition: 0 },
        end: { blockIndex: 0, contentIndex: 3, itemIndex: 1, itemContentIndex: 2, cursorPosition: 11 },
      };
      const result = Actions.deleteSelection(state, selection);
      const list = select<ItemList>(result, { blockIndex: 0, contentIndex: 3 });
      expect(list.items).toHaveLength(3);
      const item = list.items[1];
      expect(item.content).toHaveLength(1);
      expect(item.content[0].type).toEqual(LITERAL);
      expect(text(item.content[0])).toEqual("");
    });

    it("starts and ends in itemlists in different blocks, deletes everything in between", () => {
      const selection = {
        start: { blockIndex: 0, contentIndex: 3, itemIndex: 1, itemContentIndex: 0, cursorPosition: 7 },
        end: { blockIndex: 1, contentIndex: 3, itemIndex: 1, itemContentIndex: 2, cursorPosition: 3 },
      };
      const result = Actions.deleteSelection(state, selection);
      const block1 = result.redigertBrev.blocks[0];
      expect(block1.content).toHaveLength(4);
      expect(block1.deletedContent).toContain(state.redigertBrev.blocks[0].content[4].id);
      expect(block1.deletedContent).toContain(state.redigertBrev.blocks[0].content[5].id);

      const list1 = select<ItemList>(result, { blockIndex: 0, contentIndex: 3 });
      expect(list1.items).toHaveLength(2);
      expect(list1.deletedItems).toContain(itemList1.items[2].id);
      expect(list1.items[1].content).toHaveLength(1);
      expect(list1.items[1].content[0]).toMatchObject({ editedText: "andre p" });
      expect(list1.items[1].deletedContent).toContain(itemList1.items[1].content[1].id);
      expect(list1.items[1].deletedContent).toContain(itemList1.items[1].content[2].id);
      expect(list1.deletedItems).toContain(itemList1.items[2].id);

      const list2 = select<ItemList>(result, { blockIndex: 1, contentIndex: 0 });
      expect(list2.items).toHaveLength(2);
      expect(list2.deletedItems).toContain(itemList2.items[0].id);
      expect(list2.items[0].content).toHaveLength(1);
      expect(list2.items[0].content[0]).toMatchObject({ editedText: "nktliste" });
    });

    it("starts at beginning of itemList and ends after itemList, deletes entire itemList", () => {
      const selection = {
        start: { blockIndex: 0, contentIndex: 3, itemIndex: 0, itemContentIndex: 0, cursorPosition: 0 },
        end: { blockIndex: 0, contentIndex: 5, cursorPosition: 2 },
      };
      const result = Actions.deleteSelection(state, selection);
      const block = result.redigertBrev.blocks[0];
      expect(block.content).toHaveLength(4);
      expect(block.deletedContent).toContain(itemList1.id);
      expect(block.deletedContent).toContain(state.redigertBrev.blocks[0].content[4].id);
      expect(block.content[3]).toMatchObject({ editedText: "så etter første punktliste." });
    });

    it("starts and ends in same list and includes entire list at end of block, deletes entire list", () => {
      const state = letter(paragraph([literal("første avsnitt"), itemList2]), paragraph([literal("hei")]));
      const selection = {
        start: { blockIndex: 0, contentIndex: 1, itemIndex: 0, itemContentIndex: 0, cursorPosition: 0 },
        end: { blockIndex: 0, contentIndex: 1, itemIndex: 2, itemContentIndex: 2, cursorPosition: 11 },
      };
      const result = Actions.deleteSelection(state, selection);
      expect(result.redigertBrev.blocks[0].content).toHaveLength(state.redigertBrev.blocks[0].content.length - 1);
    });

    it("includes entire list and it is the only content in block, deletes entire list but leaves a block with an empty literal ", () => {
      const state = letter(paragraph([itemList2]), paragraph([literal("hei")]));
      const selection = {
        start: { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0, cursorPosition: 0 },
        end: { blockIndex: 0, contentIndex: 0, itemIndex: 2, itemContentIndex: 2, cursorPosition: 11 },
      };
      const result = Actions.deleteSelection(state, selection);
      expect(result.redigertBrev.blocks[0].content).toHaveLength(1);
      expect(result.redigertBrev.blocks[0].content[0]).toMatchObject({ text: "", type: LITERAL });
    });

    it("includes entire list that is the only content in block and ends in next block, deletes list and merges blocks", () => {
      const state = letter(paragraph([itemList2]), paragraph([literal("hei")]));
      const selection = {
        start: { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0, cursorPosition: 0 },
        end: { blockIndex: 1, contentIndex: 0, cursorPosition: 1 },
      };
      const result = Actions.deleteSelection(state, selection);
      expect(result.redigertBrev.blocks).toHaveLength(1);
      expect(result.redigertBrev.blocks[0].content).toHaveLength(1);
      expect(result.redigertBrev.blocks[0].content[0]).toMatchObject({ editedText: "ei", type: LITERAL });
    });
  });

  describe("selection in table", () => {
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
        cell(literal("rad "), variable("2"), literal(" celle 1")),
        cell(literal("rad "), variable("2"), literal(" celle 2")),
        cell(literal("rad "), variable("2"), literal(" celle 3")),
      ),
    ];
    const table1 = table(
      [
        cell(literal("kolonne "), variable("1"), literal(" i table 1")),
        cell(literal("kolonne "), variable("2"), literal(" i table 1")),
        cell(literal("kolonne "), variable("3"), literal(" i table 1")),
      ],
      rows(),
    );
    const table2 = table(
      [
        cell(literal("kolonne "), variable("1"), literal(" i table 2")),
        cell(literal("kolonne "), variable("2"), literal(" i table 2")),
        cell(literal("kolonne "), variable("3"), literal(" i table 2")),
      ],
      rows(),
    );
    const state = letter(
      paragraph([
        literal("Første setning i "),
        variable("første"),
        literal(" avsnitt."),
        table1,
        literal(" etter table."),
      ]),
      paragraph([literal("Første setning i "), variable("andre"), literal(" avsnitt."), table2]),
    );

    it("start and end in same cell, different content", () => {
      const selection = {
        start: { blockIndex: 0, contentIndex: 3, rowIndex: 0, cellIndex: 0, cellContentIndex: 0, cursorPosition: 2 },
        end: { blockIndex: 0, contentIndex: 3, rowIndex: 0, cellIndex: 0, cellContentIndex: 2, cursorPosition: 3 },
      };
      const result = Actions.deleteSelection(state, selection);
      const cell = select<Cell>(result, { ...selection.start, cellContentIndex: undefined });
      expect(cell.text).toHaveLength(2);
      expect(cell.text[0]).toMatchObject({ editedText: "ra" });
      expect(cell.text[1]).toMatchObject({ editedText: "lle 1" });
    });

    it("start and end in different cells in same row", () => {
      const selection = {
        start: { blockIndex: 0, contentIndex: 3, rowIndex: 0, cellIndex: 0, cellContentIndex: 0, cursorPosition: 2 },
        end: { blockIndex: 0, contentIndex: 3, rowIndex: 0, cellIndex: 2, cellContentIndex: 2, cursorPosition: 3 },
      };
      const result = Actions.deleteSelection(state, selection);
      const row1 = select<Row>(result, { blockIndex: 0, contentIndex: 3, rowIndex: 0 });
      expect(row1.cells).toHaveLength(3);

      const cell0 = row1.cells[0];
      expect(cell0.text).toHaveLength(1);
      expect(cell0.text[0]).toMatchObject({ editedText: "ra" });

      expectEmptyCell(row1.cells[1]);

      const cell2 = row1.cells[2];
      expect(cell2.text).toHaveLength(1);
      expect(cell2.text[0]).toMatchObject({ editedText: "lle 3" });
    });

    it("starts in header and ends in last row, deletes everything in between", () => {
      const selection = {
        start: { blockIndex: 0, contentIndex: 3, rowIndex: -1, cellIndex: 0, cellContentIndex: 0, cursorPosition: 3 },
        end: { blockIndex: 0, contentIndex: 3, rowIndex: 2, cellIndex: 1, cellContentIndex: 2, cursorPosition: 3 },
      };
      const result = Actions.deleteSelection(state, selection);
      const table = select<Table>(result, { blockIndex: 0, contentIndex: 3 });

      const header1 = table.header.colSpec[0].headerContent;
      expect(header1.text).toHaveLength(1);
      expect(header1.text[0]).toMatchObject({ editedText: "kol" });
      table.header.colSpec.slice(1).forEach((col) => expectEmptyCell(col.headerContent));

      table.rows.slice(0, 2).forEach((row) => row.cells.forEach(expectEmptyCell));
      expectEmptyCell(table.rows[2].cells[0]);

      const cell21 = table.rows[2].cells[1];
      expect(cell21.text).toHaveLength(1);
      expect(cell21.text[0]).toMatchObject({ editedText: "lle 2" });

      expect(table.rows[2].cells[2]).toEqual(table1.rows[2].cells[2]);
    });

    it("starts at beginning of table and ends outside of table, deletes entire table", () => {
      const selection = {
        start: { blockIndex: 0, contentIndex: 3, rowIndex: -1, cellIndex: 0, cellContentIndex: 0, cursorPosition: 0 },
        end: { blockIndex: 0, contentIndex: 4, cursorPosition: 2 },
      };
      const result = Actions.deleteSelection(state, selection);
      const block = result.redigertBrev.blocks[0];
      expect(block.content).toHaveLength(4);
      expect(block.deletedContent).toContain(table1.id);
      expect(block.content[3]).toMatchObject({ editedText: "tter table." });
    });

    it("starts at beginning of table and ends at end of table, deletes entire table", () => {
      const selection = {
        start: { blockIndex: 0, contentIndex: 3, rowIndex: -1, cellIndex: 0, cellContentIndex: 0, cursorPosition: 0 },
        end: { blockIndex: 0, contentIndex: 3, rowIndex: 2, cellIndex: 2, cellContentIndex: 2, cursorPosition: 8 },
      };
      const result = Actions.deleteSelection(state, selection);
      const block = result.redigertBrev.blocks[0];
      expect(block.content).toHaveLength(4);
      expect(block.deletedContent).toContain(table1.id);
    });

    it("starts before table and ends at end of table, deletes entire table and text before", () => {
      const selection = {
        start: { blockIndex: 0, contentIndex: 2, cursorPosition: 5 },
        end: { blockIndex: 0, contentIndex: 3, rowIndex: 2, cellIndex: 2, cellContentIndex: 2, cursorPosition: 8 },
      };
      const result = Actions.deleteSelection(state, selection);
      const block = result.redigertBrev.blocks[0];
      expect(block.content).toHaveLength(4);
      expect(block.deletedContent).toContain(table1.id);
      expect(block.content[2]).toMatchObject({ editedText: " avsn" });
      expect(block.content[3]).toMatchObject({ text: " etter table." });
    });
  });
});

function expectEmptyCell(cell: Cell) {
  expect(cell.text).toHaveLength(1);
  expect(cell.text[0].type).toEqual(LITERAL);
  expect(text(cell.text[0])).toEqual("");
}
