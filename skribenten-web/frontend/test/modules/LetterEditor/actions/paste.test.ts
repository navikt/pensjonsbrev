import { expect } from "vitest";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { text } from "~/Brevredigering/LetterEditor/actions/common";
import type { LiteralValue } from "~/types/brevbakerTypes";

import { item, itemList, letter, literal, paragraph, select, title1 } from "../utils";

describe("LetterEditorActions.paste", () => {
  describe("format: text/plain", () => {
    test("start of a literal", () => {
      const state = letter(paragraph(literal({ text: "Her har vi noe" })));
      const clipboard = new MockDataTransfer({ "text/plain": "ikke " });
      const result = Actions.paste(state, { blockIndex: 0, contentIndex: 0 }, 0, clipboard);

      expect(text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }))).toEqual("ikke Her har vi noe");
    });
    test("inside of a literal", () => {
      const state = letter(paragraph(literal({ text: "Her har vi noe" })));
      const clipboard = new MockDataTransfer({ "text/plain": " ikke" });
      const result = Actions.paste(state, { blockIndex: 0, contentIndex: 0 }, 10, clipboard);

      expect(text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }))).toEqual("Her har vi ikke noe");
    });

    test("end of a literal", () => {
      const state = letter(paragraph(literal({ text: "Her har vi noe" })));
      const clipboard = new MockDataTransfer({ "text/plain": " ikke" });
      const result = Actions.paste(state, { blockIndex: 0, contentIndex: 0 }, 14, clipboard);

      expect(text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }))).toEqual("Her har vi noe ikke");
    });

    test("in an item", () => {
      const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
      const state = letter(paragraph(itemList({ items: [item(literal({ text: "Her har vi noe" }))] })));
      const clipboard = new MockDataTransfer({ "text/plain": " ikke" });
      const result = Actions.paste(state, index, 10, clipboard);

      expect(text(select<LiteralValue>(result, index))).toEqual("Her har vi ikke noe");
    });
    test("should update cursorPosition", () => {
      const state = letter(paragraph(literal({ text: "Hei" })));
      const clipboard = new MockDataTransfer({ "text/plain": " Hade" });
      const result = Actions.paste(state, { blockIndex: 0, contentIndex: 0 }, 10, clipboard);

      expect(text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }))).toEqual("Hei Hade");
      expect(result.focus.cursorPosition).toEqual(8);
    });
  });

  describe("format: text/html", () => {
    test("single span should be inserted", () => {
      const index = { blockIndex: 0, contentIndex: 0 };
      const state = letter(paragraph(literal({ text: "Her har vi noe" })));
      const clipboard = new MockDataTransfer({ "text/html": "<span> ikke</span>" });
      const result = Actions.paste(state, index, 10, clipboard);

      expect(text(select<LiteralValue>(result, index))).toEqual("Her har vi ikke noe");
    });
    test("multiple span should be inserted in order", () => {
      const index = { blockIndex: 0, contentIndex: 0 };
      const state = letter(paragraph(literal({ text: "Her har vi noe" })));
      const clipboard = new MockDataTransfer({ "text/html": "<span> da</span><span> ikke</span>" });
      const result = Actions.paste(state, index, 10, clipboard);

      expect(text(select<LiteralValue>(result, index))).toEqual("Her har vi da ikke noe");
    });
    test("multiple p elements should append first to existing", () => {
      const index = { blockIndex: 0, contentIndex: 0 };
      const state = letter(paragraph(literal({ text: "Her har vi noe" })));
      const clipboard = new MockDataTransfer({ "text/html": "<p> da</p><p> ikke</p>" });
      const result = Actions.paste(state, index, 10, clipboard);

      expect(text(select<LiteralValue>(result, index))).toEqual("Her har vi da");
      expect(text(select<LiteralValue>(result, { ...index, blockIndex: 1 }))).toEqual(" ikke noe");
    });

    test("multiple p elements should append first to existing list item", () => {
      const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
      const state = letter(paragraph(itemList({ items: [item(literal({ text: "Her har vi noe" }))] })));
      const clipboard = new MockDataTransfer({ "text/html": "<p> da</p><p> ikke</p>" });
      const result = Actions.paste(state, index, 10, clipboard);

      expect(text(select<LiteralValue>(result, index))).toEqual("Her har vi da");
      expect(text(select<LiteralValue>(result, { ...index, itemIndex: 1 }))).toEqual(" ikke noe");
    });

    test("ul list should append to existing itemList", () => {
      const index = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
      const state = letter(paragraph(itemList({ items: [item(literal({ text: "Her har vi noe " }))] })));
      const clipboard = new MockDataTransfer({ "text/html": "<ul><li>annet</li><li>og enda mer</li></ul>" });
      const result = Actions.paste(state, index, 15, clipboard);

      expect(text(select<LiteralValue>(result, index))).toEqual("Her har vi noe annet");
      expect(text(select<LiteralValue>(result, { ...index, itemIndex: 1 }))).toEqual("og enda mer");
    });

    test("ul list should append first li to current literal and create a list for the remaining", () => {
      const index = { blockIndex: 0, contentIndex: 0 };
      const state = letter(paragraph(literal({ text: "Her har vi noe " })));
      const clipboard = new MockDataTransfer({ "text/html": "<ul><li>annet</li><li>mer</li><li>enda mer</li></ul>" });
      const result = Actions.paste(state, index, 15, clipboard);

      expect(text(select<LiteralValue>(result, index))).toEqual("Her har vi noe annet");
      const itemIndex = { ...index, contentIndex: index.contentIndex + 1, itemIndex: 0, itemContentIndex: 0 };
      expect(text(select<LiteralValue>(result, itemIndex))).toEqual("mer");
      expect(text(select<LiteralValue>(result, { ...itemIndex, itemIndex: 1 }))).toEqual("enda mer");
    });

    test("when at title1 ul list should append first li to current literal and create a new block and list for the remaining", () => {
      const index = { blockIndex: 0, contentIndex: 0 };
      const state = letter(title1(literal({ text: "Her har vi noe " })));
      const clipboard = new MockDataTransfer({ "text/html": "<ul><li>annet</li><li>mer</li><li>enda mer</li></ul>" });
      const result = Actions.paste(state, index, 15, clipboard);

      expect(text(select<LiteralValue>(result, index))).toEqual("Her har vi noe annet");
      const itemIndex = {
        blockIndex: index.blockIndex + 1,
        contentIndex: 0,
        itemIndex: 0,
        itemContentIndex: 0,
      };
      expect(text(select<LiteralValue>(result, itemIndex))).toEqual("mer");
      expect(text(select<LiteralValue>(result, { ...itemIndex, itemIndex: 1 }))).toEqual("enda mer");
    });

    test("should update cursorPosition", () => {
      const state = letter(paragraph(literal({ text: "Hei" })));
      const clipboard = new MockDataTransfer({ "text/html": "<p> html</p>" });
      const result = Actions.paste(state, { blockIndex: 0, contentIndex: 0 }, 10, clipboard);

      expect(text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }))).toEqual("Hei html");
      console.log(result.redigertBrev.blocks[0].content);
      console.log(result.focus);
      expect(result.focus.cursorPosition).toEqual(8);
    });
  });
});

class MockDataTransfer implements DataTransfer {
  private readonly data: { [format: string]: string } = {};

  get types() {
    return Object.keys(this.data);
  }
  getData(format: string): string {
    return this.data[format];
  }
  setData(format: string, data: string): void {
    this.data[format] = data;
  }

  constructor(data: { [format: string]: string }) {
    this.data = { ...data };
  }

  dropEffect: "none" | "copy" | "link" | "move" = "none";
  effectAllowed: "none" | "copy" | "link" | "move" | "all" | "copyLink" | "copyMove" | "linkMove" | "uninitialized" =
    "uninitialized";
  files = [] as unknown as FileList;
  items = [] as unknown as DataTransferItemList;

  clearData(): void {
    throw new Error("Method not implemented.");
  }
  setDragImage(): void {
    throw new Error("Method not implemented.");
  }
}
