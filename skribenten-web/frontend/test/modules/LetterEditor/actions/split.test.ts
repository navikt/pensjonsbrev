import { describe, expect, test } from "vitest";

import Actions from "~/pages/Brevredigering/LetterEditor/actions";
import type { Content, ItemList, ParagraphBlock, TextContent } from "~/types/brevbakerTypes";
import { LITERAL } from "~/types/brevbakerTypes";

import { item, itemList, letter, literal, paragraph, select, variable } from "../utils";

describe("LetterEditorActions.split", () => {
  describe("at literal", () => {
    test("specified block is split at contentIndex and offset", () => {
      const state = letter(paragraph(variable("var1"), literal("lit1"), variable("var2")));
      const splitId = { blockIndex: 0, contentIndex: 1 };
      const result = Actions.split(state, splitId, 2);
      const resultBlocks = result.editedLetter.letter.blocks;

      expect(resultBlocks).toHaveLength(2);

      expect(resultBlocks[splitId.blockIndex].content).toHaveLength(2);
      expect(select<TextContent>(result, splitId).text).toEqual("li");

      expect(resultBlocks[splitId.blockIndex + 1].content).toHaveLength(2);
      expect(select<TextContent>(result, { blockIndex: 1, contentIndex: 0 }).text).toEqual("t1");
      expect(select<Content>(result, { blockIndex: 1, contentIndex: 1 })).toStrictEqual(
        select<Content>(state, { blockIndex: 0, contentIndex: 2 }),
      );

      // focus is stolen
      expect(result.nextFocus).toEqual({ blockIndex: splitId.blockIndex + 1, contentIndex: 0, cursorPosition: 0 });
    });

    test("result is not the original array", () => {
      const state = letter(paragraph(literal("lit1")));
      const result = Actions.split(state, { blockIndex: 0, contentIndex: 0 }, 2);

      expect(result.editedLetter.letter.blocks).not.toBe(state.editedLetter.letter.blocks);
    });

    test("when the offset is at the end of the current content, the new block will have one content element with an empty string", () => {
      const state = letter(paragraph(literal("lit1")));
      const splitId = { blockIndex: 0, contentIndex: 0 };
      const offset = select<TextContent>(state, splitId).text.length;

      const result = Actions.split(state, splitId, offset);
      const resultBlocks = result.editedLetter.letter.blocks;

      expect(resultBlocks.length).toBe(state.editedLetter.letter.blocks.length + 1);
      expect(resultBlocks[splitId.blockIndex + 1].content.length).toBe(1);
      expect(select<TextContent>(result, { blockIndex: splitId.blockIndex + 1, contentIndex: 0 }).text).toBe("");

      // focus is stolen
      expect(result.nextFocus).toEqual({ blockIndex: splitId.blockIndex + 1, contentIndex: 0, cursorPosition: 0 });
    });

    test("does not split an emptyBlock", () => {
      const state = letter(paragraph(literal("")));
      const result = Actions.split(state, { blockIndex: 0, contentIndex: 0 }, 0);
      expect(result).toBe(state);
    });

    test("does not split at the very beginning of block when previous is empty", () => {
      const state = letter(paragraph(literal("")), paragraph(literal("lit2")));
      const result = Actions.split(state, { blockIndex: 1, contentIndex: 0 }, 0);
      expect(result).toBe(state);
    });

    test("does not leave an empty literal after an itemList", () => {
      const state = letter(paragraph(itemList(item(literal("item 1 literal"))), literal("paragraph literal")));
      const result = Actions.split(state, { blockIndex: 0, contentIndex: 1 }, 0);

      expect(result.editedLetter.letter.blocks).toHaveLength(2);
      expect(select<ParagraphBlock>(result, { blockIndex: 0 }).content).toHaveLength(1);
    });
  });

  describe("at itemList", () => {
    test("specified item is split at contentIndex and offset", () => {
      const state = letter(paragraph(itemList(item(variable("var1"), literal("lit1"), variable("var2")))));
      const splitId = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 1 };
      const offset = 2;
      const original = select<ItemList>(state, { blockIndex: 0, contentIndex: 0 });

      const result = Actions.split(state, splitId, offset);
      const resultItems = select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).items;

      expect(resultItems).toHaveLength(original.items.length + 1);

      // split item
      const splitItem = resultItems[splitId.itemIndex];
      expect(splitItem.content).toHaveLength(splitId.itemContentIndex + 1);
      expect(splitItem.content[splitId.itemContentIndex].text).toEqual(
        original.items[splitId.itemIndex].content[splitId.itemContentIndex].text.slice(0, Math.max(0, offset)),
      );

      // new item
      const newItem = resultItems[splitId.itemIndex + 1];
      expect(newItem.content).toHaveLength(original.items[splitId.itemIndex].content.length - splitId.itemContentIndex);
      expect(newItem.content[0].text).toEqual(
        original.items[splitId.itemIndex].content[splitId.itemContentIndex].text.slice(Math.max(0, offset)),
      );

      // focus is stolen to beginning of new item
      // TODO: reimplement itemlist logic
      // expect(result.stealFocus).toEqual({
      //   [splitId.blockIndex]: {
      //     contentIndex: splitId.contentIndex,
      //     startOffset: 0,
      //     item: { id: splitId.itemIndex + 1, contentIndex: 0 },
      //   },
      // });
    });

    test("when the offset is at the end of the item content, the new item will have one content element with an empty string", () => {
      const state = letter(paragraph(itemList(item(variable("var1"), literal("lit1")))));
      const splitId = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 1 };
      const original = select<ItemList>(state, { blockIndex: 0, contentIndex: 0 });

      const result = Actions.split(state, splitId, select<TextContent>(state, splitId).text.length);
      const resultItems = select<ItemList>(result, { blockIndex: 0, contentIndex: 0 });

      expect(resultItems.items).toHaveLength(original.items.length + 1);
      const newItem = resultItems.items[splitId.itemIndex + 1];
      expect(newItem.content).toHaveLength(1);
      expect(newItem.content[0].text).toEqual("");
    });

    test("does not split so that we get multiple empty items", () => {
      const state = letter(paragraph(itemList(item(literal("item1")), item(literal("")), item(literal("item3")))));

      // The following assertions also assert that we don't steal focus
      // Split at the empty item
      expect(Actions.split(state, { blockIndex: 0, contentIndex: 0, itemIndex: 1, itemContentIndex: 0 }, 0)).toBe(state);
      // Repeat split before the empty item
      expect(
        Actions.split(state, { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 }, "item1".length),
      ).toBe(state);
      // Split at beginning of item after empty item
      expect(Actions.split(state, { blockIndex: 0, contentIndex: 0, itemIndex: 2, itemContentIndex: 0 }, 0)).toBe(state);
    });

    test("splits the last empty item as a Literal after the list in the parent block", () => {
      const state = letter(paragraph(itemList(item(literal("lit1")), item(literal("")))));

      // Split at the empty item
      const withLiteralAfterList = Actions.split(
        state,
        { blockIndex: 0, contentIndex: 0, itemIndex: 1, itemContentIndex: 0 },
        0,
      );
      expect(select<ItemList>(withLiteralAfterList, { blockIndex: 0, contentIndex: 0 }).items).toHaveLength(1);
      expect(select<ParagraphBlock>(withLiteralAfterList, { blockIndex: 0 }).content).toHaveLength(2);
      expect(select<Content>(withLiteralAfterList, { blockIndex: 0, contentIndex: 1 }).type).toEqual(LITERAL);

      // TODO: reimplement
      // expect(withLiteralAfterList.nextFocus).toEqual({ blockIndex: 0, contentIndex: 1, startOffset: 0 });
    });

    test("when splitting the last empty item and parent block already has a subsequent literal then no new literal will be added", () => {
      const state = letter(paragraph(itemList(item(literal("lit1")), item(literal(""))), literal("")));

      const result = Actions.split(state, { blockIndex: 0, contentIndex: 0, itemIndex: 1, itemContentIndex: 0 }, 0);

      expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).items).toHaveLength(1);
      expect(select<ParagraphBlock>(result, { blockIndex: 0 }).content).toHaveLength(2);
    });
  });
});
