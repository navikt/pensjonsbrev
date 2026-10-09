import { describe, expect, test } from "vitest";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { text } from "~/Brevredigering/LetterEditor/actions/common";
import { type SelectionIndex } from "~/Brevredigering/LetterEditor/model/state";
import { type ItemList, type LiteralValue, type Table } from "~/types/brevbakerTypes";
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
  variable,
} from "~test/support/letterEditorTestUtils";
import { MockDataTransfer } from "~test/support/pasteTestUtils";

describe("Actions.pasteReplacingSelection", () => {
  describe("paste plain text replacing selection in a single literal", () => {
    test("replaces selected text in the middle of a literal", () => {
      const state = letter(paragraph([literal({ text: "Hello World" })]));
      const clipboard = new MockDataTransfer({ "text/plain": "Beautiful" });
      const selection: SelectionIndex = {
        start: { blockIndex: 0, contentIndex: 0, cursorPosition: 6 },
        end: { blockIndex: 0, contentIndex: 0, cursorPosition: 11 },
      };

      const result = Actions.pasteReplacingSelection(state, selection, clipboard);

      expect(text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }))).toEqual("Hello Beautiful");
      expect(result.focus.cursorPosition).toEqual(15);
    });

    test("replaces entire literal content", () => {
      const state = letter(paragraph([literal({ text: "old text" })]));
      const clipboard = new MockDataTransfer({ "text/plain": "new text" });
      const selection: SelectionIndex = {
        start: { blockIndex: 0, contentIndex: 0, cursorPosition: 0 },
        end: { blockIndex: 0, contentIndex: 0, cursorPosition: 8 },
      };

      const result = Actions.pasteReplacingSelection(state, selection, clipboard);

      expect(text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }))).toEqual("new text");
      expect(result.focus.cursorPosition).toEqual(8);
    });

    test("replaces selection spanning start of literal", () => {
      const state = letter(paragraph([literal({ text: "Remove this keep" })]));
      const clipboard = new MockDataTransfer({ "text/plain": "Replaced " });
      const selection: SelectionIndex = {
        start: { blockIndex: 0, contentIndex: 0, cursorPosition: 0 },
        end: { blockIndex: 0, contentIndex: 0, cursorPosition: 12 },
      };

      const result = Actions.pasteReplacingSelection(state, selection, clipboard);

      expect(text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }))).toEqual("Replaced keep");
    });
  });

  describe("paste HTML replacing selection spanning multiple content elements", () => {
    test("replaces selection across two literals with bold text", () => {
      const state = letter(
        paragraph({
          id: 1,
          content: [literal({ text: "first part " }), literal({ text: "second part" })],
        }),
      );
      const clipboard = new MockDataTransfer({ "text/html": "<strong>BOLD</strong>" });
      const selection: SelectionIndex = {
        start: { blockIndex: 0, contentIndex: 0, cursorPosition: 6 },
        end: { blockIndex: 0, contentIndex: 1, cursorPosition: 6 },
      };

      const result = Actions.pasteReplacingSelection(state, selection, clipboard);

      const block = result.redigertBrev.blocks[0];
      const texts = block.content.map((c) => text(c as LiteralValue));
      const combined = texts.join("");
      expect(combined).toContain("first");
      expect(combined).toContain("BOLD");
      expect(combined).toContain("part");
      expect(combined).not.toContain("part s");
    });

    test("replaces selection across literal and variable with plain text", () => {
      const state = letter(
        paragraph({
          id: 1,
          content: [literal({ text: "before " }), variable("VAR"), literal({ text: " after" })],
        }),
      );
      const clipboard = new MockDataTransfer({ "text/plain": "replaced" });
      const selection: SelectionIndex = {
        start: { blockIndex: 0, contentIndex: 0, cursorPosition: 4 },
        end: { blockIndex: 0, contentIndex: 2, cursorPosition: 3 },
      };

      const result = Actions.pasteReplacingSelection(state, selection, clipboard);

      const block = result.redigertBrev.blocks[0];
      const allText = block.content.map((c) => text(c as LiteralValue)).join("");
      expect(allText).toContain("befo");
      expect(allText).toContain("replaced");
      expect(allText).toContain("ter");
      expect(allText).not.toContain("VAR");
    });
  });

  describe("paste replacing selection across blocks", () => {
    test("replaces selection spanning two blocks with plain text", () => {
      const state = letter(
        paragraph({ id: 1, content: [literal({ text: "First block content" })] }),
        paragraph({ id: 2, content: [literal({ text: "Second block content" })] }),
      );
      const clipboard = new MockDataTransfer({ "text/plain": "PASTED" });
      const selection: SelectionIndex = {
        start: { blockIndex: 0, contentIndex: 0, cursorPosition: 6 },
        end: { blockIndex: 1, contentIndex: 0, cursorPosition: 7 },
      };

      const result = Actions.pasteReplacingSelection(state, selection, clipboard);

      const allTexts = result.redigertBrev.blocks.flatMap((b) => b.content.map((c) => text(c as LiteralValue)));
      const combined = allTexts.join("");
      expect(combined).toContain("First");
      expect(combined).toContain("PASTED");
      expect(combined).toContain("block content");
      expect(combined).not.toContain("Second");
    });

    test("replaces selection spanning three blocks with HTML paragraph", () => {
      const state = letter(
        paragraph({ id: 1, content: [literal({ text: "Block one" })] }),
        paragraph({ id: 2, content: [literal({ text: "Block two" })] }),
        paragraph({ id: 3, content: [literal({ text: "Block three" })] }),
      );
      const clipboard = new MockDataTransfer({ "text/html": "<p>Pasted paragraph</p>" });
      const selection: SelectionIndex = {
        start: { blockIndex: 0, contentIndex: 0, cursorPosition: 5 },
        end: { blockIndex: 2, contentIndex: 0, cursorPosition: 5 },
      };

      const result = Actions.pasteReplacingSelection(state, selection, clipboard);

      const allTexts = result.redigertBrev.blocks.flatMap((b) => b.content.map((c) => text(c as LiteralValue)));
      const combined = allTexts.join("");
      expect(combined).toContain("Block");
      expect(combined).toContain("Pasted paragraph");
      expect(combined).toContain("three");
      expect(combined).not.toContain("Block two");
    });
  });

  describe("paste replacing selection in table cell", () => {
    test("replaces selected text in a table cell", () => {
      const state = letter(
        paragraph([
          literal({ text: "" }),
          table(
            [cell(literal({ text: "H1" })), cell(literal({ text: "H2" }))],
            [row(cell(literal({ text: "cell content" })), cell(literal({ text: "other" })))],
          ),
          literal({ text: "" }),
        ]),
      );
      const clipboard = new MockDataTransfer({ "text/plain": "REPLACED" });
      const selection: SelectionIndex = {
        start: { blockIndex: 0, contentIndex: 1, rowIndex: 0, cellIndex: 0, cellContentIndex: 0, cursorPosition: 5 },
        end: { blockIndex: 0, contentIndex: 1, rowIndex: 0, cellIndex: 0, cellContentIndex: 0, cursorPosition: 12 },
      };

      const result = Actions.pasteReplacingSelection(state, selection, clipboard);

      const tableContent = select<Table>(result, { blockIndex: 0, contentIndex: 1 });
      const cellText = text(tableContent.rows[0].cells[0].text[0] as LiteralValue);
      expect(cellText).toEqual("cell REPLACED");
    });
  });

  describe("paste replacing selection in item list", () => {
    test("replaces selected text within an item", () => {
      const state = letter(
        paragraph([
          itemList({
            items: [item(literal({ text: "first item content" })), item(literal({ text: "second item" }))],
          }),
        ]),
      );
      const clipboard = new MockDataTransfer({ "text/plain": "REPLACED" });
      const selection: SelectionIndex = {
        start: { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0, cursorPosition: 6 },
        end: { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0, cursorPosition: 10 },
      };

      const result = Actions.pasteReplacingSelection(state, selection, clipboard);

      const list = select<ItemList>(result, { blockIndex: 0, contentIndex: 0 });
      const itemText = text(list.items[0].content[0] as LiteralValue);
      expect(itemText).toEqual("first REPLACED content");
    });

    test("replaces selection spanning two items with plain text", () => {
      const state = letter(
        paragraph([
          itemList({
            items: [item(literal({ text: "item one text" })), item(literal({ text: "item two text" }))],
          }),
        ]),
      );
      const clipboard = new MockDataTransfer({ "text/plain": "MERGED" });
      const selection: SelectionIndex = {
        start: { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0, cursorPosition: 4 },
        end: { blockIndex: 0, contentIndex: 0, itemIndex: 1, itemContentIndex: 0, cursorPosition: 8 },
      };

      const result = Actions.pasteReplacingSelection(state, selection, clipboard);

      const list = select<ItemList>(result, { blockIndex: 0, contentIndex: 0 });
      const allItemTexts = list.items.flatMap((i) => i.content.map((c) => text(c as LiteralValue))).join("");
      expect(allItemTexts).toContain("item");
      expect(allItemTexts).toContain("MERGED");
      expect(allItemTexts).toContain("text");
    });
  });

  describe("keeps the selection when the clipboard has nothing to insert", () => {
    const selectHello: SelectionIndex = {
      start: { blockIndex: 0, contentIndex: 0, cursorPosition: 0 },
      end: { blockIndex: 0, contentIndex: 0, cursorPosition: 5 },
    };

    test("empty RTF without text/plain", () => {
      const state = letter(paragraph([literal({ text: "Hello World" })]));
      const clipboard = new MockDataTransfer({ "text/rtf": "{\\rtf1\\ansi {\\fonttbl{\\f0 Arial;}}\\par}" });

      const result = Actions.pasteReplacingSelection(state, selectHello, clipboard);

      expect(result.redigertBrev).toEqual(state.redigertBrev);
      expect(result.saveStatus).toEqual(state.saveStatus);
    });

    test("empty text/plain", () => {
      const state = letter(paragraph([literal({ text: "Hello World" })]));

      const result = Actions.pasteReplacingSelection(state, selectHello, new MockDataTransfer({ "text/plain": "" }));

      expect(result.redigertBrev).toEqual(state.redigertBrev);
    });

    const tableState = () =>
      letter(
        paragraph([
          literal({ text: "" }),
          table(
            [cell(literal({ text: "H1" })), cell(literal({ text: "H2" }))],
            [row(cell(literal({ text: "cell content" })), cell(literal({ text: "other" })))],
          ),
          literal({ text: "" }),
        ]),
      );
    const selectInCell: SelectionIndex = {
      start: { blockIndex: 0, contentIndex: 1, rowIndex: 0, cellIndex: 0, cellContentIndex: 0, cursorPosition: 5 },
      end: { blockIndex: 0, contentIndex: 1, rowIndex: 0, cellIndex: 0, cellContentIndex: 0, cursorPosition: 12 },
    };
    const htmlTable = "<table><tr><td>A</td><td>B</td></tr></table>";

    test("a table while the selection is inside a table cell", () => {
      const state = tableState();

      const result = Actions.pasteReplacingSelection(
        state,
        selectInCell,
        new MockDataTransfer({ "text/html": htmlTable }),
      );

      expect(result.redigertBrev).toEqual(state.redigertBrev);
    });

    test("a table inside a table cell falls back to text/plain", () => {
      const state = tableState();

      const result = Actions.pasteReplacingSelection(
        state,
        selectInCell,
        new MockDataTransfer({ "text/html": htmlTable, "text/plain": "A\tB" }),
      );

      const cellText = select<Table>(result, { blockIndex: 0, contentIndex: 1 }).rows[0].cells[0].text;
      expect(cellText.map((content) => text(content as LiteralValue)).join("")).toEqual("cell A\tB");
    });

    test("an RTF table inside a table cell falls back to text/plain", () => {
      const state = tableState();
      const rtfTable = "{\\rtf1\\ansi \\trowd\\cellx1000\\cellx2000 A\\cell B\\cell\\row}";

      const result = Actions.pasteReplacingSelection(
        state,
        selectInCell,
        new MockDataTransfer({ "text/rtf": rtfTable, "text/plain": "A\tB" }),
      );

      const cellText = select<Table>(result, { blockIndex: 0, contentIndex: 1 }).rows[0].cells[0].text;
      expect(cellText.map((content) => text(content as LiteralValue)).join("")).toEqual("cell A\tB");
    });
  });

  describe("undo atomicity", () => {
    test("single undo reverts the entire paste-replace operation", () => {
      const state = letter(paragraph([literal({ text: "Hello World" })]));
      const clipboard = new MockDataTransfer({ "text/plain": "Universe" });
      const selection: SelectionIndex = {
        start: { blockIndex: 0, contentIndex: 0, cursorPosition: 6 },
        end: { blockIndex: 0, contentIndex: 0, cursorPosition: 11 },
      };

      const result = Actions.pasteReplacingSelection(state, selection, clipboard);
      expect(text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }))).toEqual("Hello Universe");

      // The operation should have created exactly one history entry
      expect(result.history.entries).toHaveLength(1);
      expect(result.history.entryPointer).toEqual(0);
    });
  });
});
