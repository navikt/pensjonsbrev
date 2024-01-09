import { describe, expect, test } from "vitest";

import Actions from "~/pages/Brevredigering/LetterEditor/actions";
import type { Content, ItemList, ParagraphBlock, TextContent } from "~/types/brevbakerTypes";
import { LITERAL } from "~/types/brevbakerTypes";

import { item, itemList, letter, literal, paragraph, select, variable } from "../utils";

describe("LetterEditorActions.split", () => {
  describe("at literal", () => {
    test("specified block is split at contentId and offset", () => {
      const state = letter(paragraph(variable("var1"), literal("lit1"), variable("var2")));
      const splitId = { blockId: 0, contentId: 1 };
      const result = Actions.split(state, splitId, 2);
      const resultBlocks = result.editedLetter.letter.blocks;

      expect(resultBlocks).toHaveLength(2);

      expect(resultBlocks[splitId.blockId].content).toHaveLength(2);
      expect(select<TextContent>(result, splitId).text).toEqual("li");

      expect(resultBlocks[splitId.blockId + 1].content).toHaveLength(2);
      expect(select<TextContent>(result, { blockId: 1, contentId: 0 }).text).toEqual("t1");
      expect(select<Content>(result, { blockId: 1, contentId: 1 })).toStrictEqual(
        select<Content>(state, { blockId: 0, contentId: 2 }),
      );

      // focus is stolen
      expect(result.nextFocus).toEqual({ blockIndex: splitId.blockId + 1, contentIndex: 0, cursorPosition: 0 });
    });

    test("result is not the original array", () => {
      const state = letter(paragraph(literal("lit1")));
      const result = Actions.split(state, { blockId: 0, contentId: 0 }, 2);

      expect(result.editedLetter.letter.blocks).not.toBe(state.editedLetter.letter.blocks);
    });

    test("when the offset is at the end of the current content, the new block will have one content element with an empty string", () => {
      const state = letter(paragraph(literal("lit1")));
      const splitId = { blockId: 0, contentId: 0 };
      const offset = select<TextContent>(state, splitId).text.length;

      const result = Actions.split(state, splitId, offset);
      const resultBlocks = result.editedLetter.letter.blocks;

      expect(resultBlocks.length).toBe(state.editedLetter.letter.blocks.length + 1);
      expect(resultBlocks[splitId.blockId + 1].content.length).toBe(1);
      expect(select<TextContent>(result, { blockId: splitId.blockId + 1, contentId: 0 }).text).toBe("");

      // focus is stolen
      expect(result.nextFocus).toEqual({ blockIndex: splitId.blockId + 1, contentIndex: 0, cursorPosition: 0 });
    });

    test("does not split an emptyBlock", () => {
      const state = letter(paragraph(literal("")));
      const result = Actions.split(state, { blockId: 0, contentId: 0 }, 0);
      expect(result).toBe(state);
    });

    test("does not split at the very beginning of block when previous is empty", () => {
      const state = letter(paragraph(literal("")), paragraph(literal("lit2")));
      const result = Actions.split(state, { blockId: 1, contentId: 0 }, 0);
      expect(result).toBe(state);
    });

    test("does not leave an empty literal after an itemList", () => {
      const state = letter(paragraph(itemList(item(literal("item 1 literal"))), literal("paragraph literal")));
      const result = Actions.split(state, { blockId: 0, contentId: 1 }, 0);

      expect(result.editedLetter.letter.blocks).toHaveLength(2);
      expect(select<ParagraphBlock>(result, { blockId: 0 }).content).toHaveLength(1);
    });
  });

  describe("at itemList", () => {
    test("specified item is split at contentId and offset", () => {
      const state = letter(paragraph(itemList(item(variable("var1"), literal("lit1"), variable("var2")))));
      const splitId = { blockId: 0, contentId: 0, itemId: 0, itemContentId: 1 };
      const offset = 2;
      const original = select<ItemList>(state, { blockId: 0, contentId: 0 });

      const result = Actions.split(state, splitId, offset);
      const resultItems = select<ItemList>(result, { blockId: 0, contentId: 0 }).items;

      expect(resultItems).toHaveLength(original.items.length + 1);

      // split item
      const splitItem = resultItems[splitId.itemId];
      expect(splitItem.content).toHaveLength(splitId.itemContentId + 1);
      expect(splitItem.content[splitId.itemContentId].text).toEqual(
        original.items[splitId.itemId].content[splitId.itemContentId].text.slice(0, Math.max(0, offset)),
      );

      // new item
      const newItem = resultItems[splitId.itemId + 1];
      expect(newItem.content).toHaveLength(original.items[splitId.itemId].content.length - splitId.itemContentId);
      expect(newItem.content[0].text).toEqual(
        original.items[splitId.itemId].content[splitId.itemContentId].text.slice(Math.max(0, offset)),
      );

      // focus is stolen to beginning of new item
      // TODO: reimplement itemlist logic
      // expect(result.stealFocus).toEqual({
      //   [splitId.blockId]: {
      //     contentId: splitId.contentId,
      //     startOffset: 0,
      //     item: { id: splitId.itemId + 1, contentId: 0 },
      //   },
      // });
    });

    test("when the offset is at the end of the item content, the new item will have one content element with an empty string", () => {
      const state = letter(paragraph(itemList(item(variable("var1"), literal("lit1")))));
      const splitId = { blockId: 0, contentId: 0, itemId: 0, itemContentId: 1 };
      const original = select<ItemList>(state, { blockId: 0, contentId: 0 });

      const result = Actions.split(state, splitId, select<TextContent>(state, splitId).text.length);
      const resultItems = select<ItemList>(result, { blockId: 0, contentId: 0 });

      expect(resultItems.items).toHaveLength(original.items.length + 1);
      const newItem = resultItems.items[splitId.itemId + 1];
      expect(newItem.content).toHaveLength(1);
      expect(newItem.content[0].text).toEqual("");
    });

    test("does not split so that we get multiple empty items", () => {
      const state = letter(paragraph(itemList(item(literal("item1")), item(literal("")), item(literal("item3")))));

      // The following assertions also assert that we don't steal focus
      // Split at the empty item
      expect(Actions.split(state, { blockId: 0, contentId: 0, itemId: 1, itemContentId: 0 }, 0)).toBe(state);
      // Repeat split before the empty item
      expect(Actions.split(state, { blockId: 0, contentId: 0, itemId: 0, itemContentId: 0 }, "item1".length)).toBe(
        state,
      );
      // Split at beginning of item after empty item
      expect(Actions.split(state, { blockId: 0, contentId: 0, itemId: 2, itemContentId: 0 }, 0)).toBe(state);
    });

    test("splits the last empty item as a Literal after the list in the parent block", () => {
      const state = letter(paragraph(itemList(item(literal("lit1")), item(literal("")))));

      // Split at the empty item
      const withLiteralAfterList = Actions.split(state, { blockId: 0, contentId: 0, itemId: 1, itemContentId: 0 }, 0);
      expect(select<ItemList>(withLiteralAfterList, { blockId: 0, contentId: 0 }).items).toHaveLength(1);
      expect(select<ParagraphBlock>(withLiteralAfterList, { blockId: 0 }).content).toHaveLength(2);
      expect(select<Content>(withLiteralAfterList, { blockId: 0, contentId: 1 }).type).toEqual(LITERAL);

      // TODO: reimplement
      // expect(withLiteralAfterList.nextFocus).toEqual({ blockIndex: 0, contentIndex: 1, startOffset: 0 });
    });

    test("when splitting the last empty item and parent block already has a subsequent literal then no new literal will be added", () => {
      const state = letter(paragraph(itemList(item(literal("lit1")), item(literal(""))), literal("")));

      const result = Actions.split(state, { blockId: 0, contentId: 0, itemId: 1, itemContentId: 0 }, 0);

      expect(select<ItemList>(result, { blockId: 0, contentId: 0 }).items).toHaveLength(1);
      expect(select<ParagraphBlock>(result, { blockId: 0 }).content).toHaveLength(2);
    });
  });
});
