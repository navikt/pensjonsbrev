import { expect } from "vitest";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { newParagraph } from "~/Brevredigering/LetterEditor/actions/common";
import type { Item, ItemList, LiteralValue, ParagraphBlock } from "~/types/brevbakerTypes";

import { item, itemList, letter, literal, paragraph, select, variable } from "../utils";

describe("LetterEditorActions.toggleBulletList", () => {
  describe("has adjoining itemList", () => {
    test("should not merge with itemList in previous block if not first in current block", () => {
      const state = letter(
        paragraph(itemList({ items: [item(literal({ text: "b0-c1" }))] })),
        paragraph(
          literal({ text: "b1-c0" }),
          itemList({ items: [item(literal({ text: "b1-c1-i0-ic0" }))] }),
          literal({ text: "b1-c2" }),
        ),
      );
      const result = Actions.toggleBulletList(state, { blockIndex: 1, contentIndex: 2 });
      expect(result.redigertBrev.blocks).toHaveLength(2);
      expect(result.redigertBrev.blocks[0]).toEqual(state.redigertBrev.blocks[0]);

      const toggledContent = select<LiteralValue>(state, { blockIndex: 1, contentIndex: 2 });

      const toggledInBlock = select<ParagraphBlock>(result, { blockIndex: 1 });
      expect(toggledInBlock.content).toHaveLength(2);
      expect(toggledInBlock.deletedContent).toEqual(toggledContent.id ? [toggledContent.id] : []);
      expect(toggledInBlock?.content[0]).toEqual(state.redigertBrev.blocks[1].content[0]);

      expect(select<Item>(result, { blockIndex: 1, contentIndex: 1, itemIndex: 1 }).content).toEqual([toggledContent]);
    });
    test("should not merge with itemList in next block if not last in current block", () => {
      const state = letter(
        paragraph(
          literal({ text: "b1-l1" }),
          itemList({ items: [item(literal({ text: "b1-p1" }))] }),
          literal({ text: "b1-l2" }),
        ),
        paragraph(itemList({ items: [item(literal({ text: "b2-p1" }))] })),
      );
      const result = Actions.toggleBulletList(state, { blockIndex: 0, contentIndex: 0 });
      expect(result.redigertBrev.blocks).toHaveLength(2);
      expect(result.redigertBrev.blocks[1]).toEqual(state.redigertBrev.blocks[1]);

      const toggledContent = select<LiteralValue>(state, { blockIndex: 0, contentIndex: 0 });

      const toggledInBlock = select<ParagraphBlock>(result, { blockIndex: 0 });
      expect(toggledInBlock.content).toHaveLength(2);
      expect(toggledInBlock.deletedContent).toEqual(toggledContent.id ? [toggledContent.id] : []);
      expect(toggledInBlock?.content.at(-1)).toEqual(state.redigertBrev.blocks[0].content.at(-1));

      expect(select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).content).toEqual([toggledContent]);
    });
    test("should not merge with itemList in previous and next block if not first and last in block", () => {
      const state = letter(
        paragraph(itemList({ id: 1, items: [item(literal({ text: "b1-p1" }))] })),
        paragraph(
          literal({ text: "b2-l1" }),
          itemList({ id: 2, items: [item(literal({ text: "b2-ul1-p1" }))] }),
          literal({ id: 22, text: "b2-l2" }),
          itemList({ id: 3, items: [item(literal({ text: "b2-ul2-p1" }))] }),
          literal({ text: "b2-l3" }),
        ),
        paragraph(itemList({ id: 4, items: [item(literal({ text: "b3-p1" }))] })),
      );

      const result = Actions.toggleBulletList(state, { blockIndex: 1, contentIndex: 2 });

      // blocks and content that should be untouched
      expect(result.redigertBrev.blocks).toHaveLength(3);
      expect(result.redigertBrev.blocks[0]).toEqual(state.redigertBrev.blocks[0]);
      expect(result.redigertBrev.blocks[2]).toEqual(state.redigertBrev.blocks[2]);
      expect(result.redigertBrev.blocks[1].content[0]).toEqual(state.redigertBrev.blocks[1].content[0]);
      expect(result.redigertBrev.blocks[1].content.at(-1)).toEqual(state.redigertBrev.blocks[1].content.at(-1));

      const toggledInBlock = select<ParagraphBlock>(result, { blockIndex: 1 });
      expect(toggledInBlock.content).toHaveLength(3);

      // deletedContent should contain the literal that was toggled and the itemList that we merged into the one we kept
      expect(toggledInBlock.deletedContent).toHaveLength(2);
      expect(toggledInBlock.deletedContent).toEqual([22, 3]);
      const shouldBeDeleted = state.redigertBrev.blocks[1].content
        .map((c) => c.id)
        .filter((id) => toggledInBlock.content.findIndex((keptContent) => keptContent.id === id) === -1);
      expect(toggledInBlock.deletedContent).toEqual(shouldBeDeleted);

      // content that we merged in to the itemList
      expect(select<Item>(result, { blockIndex: 1, contentIndex: 1, itemIndex: 1 }).content).toEqual([
        select<LiteralValue>(state, { blockIndex: 1, contentIndex: 2 }),
      ]);
      const keptItemList = select<ItemList>(result, { blockIndex: 1, contentIndex: 1 });
      const mergedItemList = state.redigertBrev.blocks[1].content.find(
        (c) => c.type === "ITEM_LIST" && c.id !== keptItemList.id,
      ) as ItemList;
      expect(keptItemList.items).toContain(mergedItemList.items[0]);
    });
    test("does not merge other itemLists than the one we toggled", () => {
      const state = letter(
        newParagraph({ content: [itemList({ items: [item(literal({ text: "blokk før" }))] })] }),
        newParagraph({
          content: [
            itemList({ items: [item(literal({ text: "punktliste1" }))] }),
            literal({ text: "l1" }),
            itemList({ items: [item(literal({ text: "punktliste2" }))] }),
            literal({ text: "l2" }),
          ],
        }),
      );
      const result = Actions.toggleBulletList(state, { blockIndex: 1, contentIndex: 3 });
      expect(result.redigertBrev.blocks).toHaveLength(2);
      expect(result.redigertBrev.blocks[1]?.content).toHaveLength(3);
      expect(
        select<LiteralValue>(result, { blockIndex: 1, contentIndex: 2, itemIndex: 1, itemContentIndex: 0 })?.text,
      ).toEqual("l2");
      expect(result.redigertBrev.blocks[1].deletedContent).toContain(
        select<LiteralValue>(state, { blockIndex: 1, contentIndex: 3 }).id,
      );
    });
  });

  describe("retains deletedContent", () => {
    test("previous deletedContent is kept for block", () => {
      const state = letter(
        newParagraph({ id: 1, content: [literal({ id: 11, parentId: 1, text: "l1" })], deletedContent: [-1] }),
      );
      const result = Actions.toggleBulletList(state, { blockIndex: 0, contentIndex: 0 });
      const deletedContent = result.redigertBrev.blocks[0].deletedContent;

      expect(deletedContent).toContain(11);
      expect(deletedContent).toContain(-1);
    });
    test("previous deletedItems is kept for merged itemLists", () => {
      const state = letter(
        paragraph(
          itemList({ items: [item(literal({ text: "l1" }))], deletedItems: [-1] }),
          literal({ text: "l2" }),
          itemList({ items: [item(literal({ text: "l3" }))], deletedItems: [-2] }),
        ),
      );
      const result = Actions.toggleBulletList(state, { blockIndex: 0, contentIndex: 1 });

      const keptItemList = select<ItemList>(result, { blockIndex: 0, contentIndex: 0 });
      const originalDeletedItems = (
        state.redigertBrev.blocks[0].content.find((c) => c.id === keptItemList.id) as ItemList
      ).deletedItems;
      expect(originalDeletedItems).toHaveLength(1);

      const deletedItems = select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).deletedItems;
      expect(deletedItems).toContain(originalDeletedItems[0]);
    });

    test("toggling off item in middle of list keeps deletedItems for the part that keeps ID", () => {
      const state = letter(
        paragraph(
          itemList({
            id: -1,
            items: [
              item(literal({ text: "b0-c0-i0-ic0" })),
              item(literal({ text: "b0-c0-i1-ic0" })),
              item(literal({ text: "b0-c0-i2-ic0" })),
            ],
            deletedItems: [-2],
          }),
        ),
      );
      const originalItemList = select<ItemList>(state, {
        blockIndex: 0,
        contentIndex: 0,
      });
      const result = Actions.toggleBulletList(state, {
        blockIndex: 0,
        contentIndex: 0,
        itemIndex: 1,
        itemContentIndex: 0,
      });

      const block = select<ParagraphBlock>(result, { blockIndex: 0 });
      expect(block.content).toHaveLength(3);
      expect(block.content.filter((c) => c.type === "ITEM_LIST")).toHaveLength(2);

      const keptList = block.content.find((il) => il.id === -1 && il.type === "ITEM_LIST") as ItemList;
      expect(keptList).not.toBeUndefined();
      expect(keptList.deletedItems).toEqual([-2, ...originalItemList.items.slice(1).map((c) => c.id)]);
    });
  });

  describe("updates deleted", () => {
    test("when toggling off first item it is deleted", () => {
      const state = letter(
        paragraph(itemList({ items: [item(literal({ text: "p1" })), item(literal({ text: "p2" }))] })),
      );
      const result = Actions.toggleBulletList(state, { blockIndex: 0, contentIndex: 0, itemIndex: 0 });

      expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 1 }).deletedItems).toContain(
        select<Item>(state, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).id,
      );
    });

    test("when toggling off only item, then itemList is deleted", () => {
      const state = letter(paragraph(itemList({ items: [item(literal({ text: "p1" }))] })));
      const toggleIndex = { blockIndex: 0, contentIndex: 0, itemIndex: 0 };
      const originalItem = select<Item>(state, toggleIndex);
      const result = Actions.toggleBulletList(state, toggleIndex);

      expect(result.redigertBrev.blocks[0].content).toHaveLength(1);
      expect(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 })).toEqual(originalItem.content[0]);
      expect(result.redigertBrev.blocks[0].deletedContent).toContain(
        select<ItemList>(state, { blockIndex: 0, contentIndex: 0 }).id,
      );
    });

    test("when toggling off last item it is deleted", () => {
      const state = letter(
        paragraph(itemList({ items: [item(literal({ text: "p1" })), item(literal({ text: "p2" }))] })),
      );
      const result = Actions.toggleBulletList(state, { blockIndex: 0, contentIndex: 0, itemIndex: 1 });

      expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).deletedItems).toContain(
        select<Item>(state, { blockIndex: 0, contentIndex: 0, itemIndex: 1 }).id,
      );
    });
  });

  describe("focus is moved", () => {
    test("when toggling off only item focus is moved", () => {
      const state = letter(paragraph(itemList({ items: [item(literal({ text: "p1" }), literal({ text: "p1-l2" }))] })));
      const toggleIndex = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 1 };
      const result = Actions.toggleBulletList(state, toggleIndex);

      expect(result.redigertBrev.blocks).toHaveLength(1);
      expect(result.redigertBrev.blocks[0].content).toHaveLength(2);
      expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 1 });
    });

    test("when toggling off first item focus should be moved", () => {
      const state = letter(
        paragraph(
          literal({ text: "b0-c0" }),
          itemList({
            items: [item(literal({ text: "b0-c1-i0-ic0" })), item(literal({ text: "b0-c1-i1-ic0" }))],
          }),
        ),
      );
      const result = Actions.toggleBulletList(state, {
        blockIndex: 0,
        contentIndex: 1,
        itemIndex: 0,
        itemContentIndex: 0,
      });
      expect(result.redigertBrev.blocks).toHaveLength(1);
      expect(result.redigertBrev.blocks[0].content).toHaveLength(3);
      expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 1, cursorPosition: state.focus.cursorPosition });
    });

    test("when toggling off first item with multiple literals, focus should be moved", () => {
      const state = letter(
        paragraph(
          literal({ text: "b0-c0" }),
          itemList({
            items: [
              item(literal({ text: "b0-c1-i0-ic0" }), literal({ text: "b0-c1-i0-ic1" })),
              item(literal({ text: "b0-c1-i1-ic0" })),
            ],
          }),
        ),
      );
      const result = Actions.toggleBulletList(state, {
        blockIndex: 0,
        contentIndex: 1,
        itemIndex: 0,
        itemContentIndex: 1,
      });
      expect(result.redigertBrev.blocks).toHaveLength(1);
      expect(result.redigertBrev.blocks[0].content).toHaveLength(4);
      expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 2, cursorPosition: state.focus.cursorPosition });
    });

    test("when toggling off last item, focus should be moved", () => {
      const state = letter(
        paragraph(
          literal({ text: "b0-c0" }),
          itemList({
            items: [
              item(literal({ text: "b0-c1-i0-ic0" }), literal({ text: "b0-c1-i0-ic1" })),
              item(literal({ text: "b0-c1-i1-ic0" })),
            ],
          }),
        ),
      );
      const result = Actions.toggleBulletList(state, {
        blockIndex: 0,
        contentIndex: 1,
        itemIndex: 1,
        itemContentIndex: 0,
      });

      expect(result.redigertBrev.blocks).toHaveLength(1);
      expect(result.redigertBrev.blocks[0].content).toHaveLength(3);
      expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 2, cursorPosition: state.focus.cursorPosition });
    });

    test("when toggling off last item with multiple literals, focus should be moved", () => {
      const state = letter(
        paragraph(
          literal({ text: "b0-c0" }),
          itemList({
            items: [
              item(literal({ text: "b0-c1-i0-ic0" }), literal({ text: "b0-c1-i0-ic1" })),
              item(literal({ text: "b0-c1-i1-ic0" }), literal({ text: "b0-c1-i1-ic1" })),
            ],
          }),
        ),
      );
      const result = Actions.toggleBulletList(state, {
        blockIndex: 0,
        contentIndex: 1,
        itemIndex: 1,
        itemContentIndex: 1,
      });

      expect(result.redigertBrev.blocks).toHaveLength(1);
      expect(result.redigertBrev.blocks[0].content).toHaveLength(4);
      expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 3, cursorPosition: state.focus.cursorPosition });
    });

    test("when toggling off item in the middle, focus should be moved", () => {
      const state = letter(
        paragraph(
          variable("v1"),
          itemList({
            items: [
              item(literal({ text: "p1-l1" }), literal({ text: "p1-l2" })),
              item(literal({ text: "p2-l1" }), literal({ text: "p2-l2" })),
              item(literal({ text: "p3" })),
            ],
          }),
          variable("v2"),
        ),
      );
      const result = Actions.toggleBulletList(state, {
        blockIndex: 0,
        contentIndex: 1,
        itemContentIndex: 1,
        itemIndex: 1,
      });

      expect(result.redigertBrev.blocks).toHaveLength(1);
      expect(result.redigertBrev.blocks[0].content).toHaveLength(6);
      expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 3, cursorPosition: state.focus.cursorPosition });
    });
  });

  describe("toggle off", () => {
    test("first of two items, then content is inserted after list", () => {
      const state = letter(
        paragraph(
          variable("v1"),
          itemList({
            items: [
              item(literal({ text: "p1-l1" }), literal({ text: "p1-l2" })),
              item(literal({ text: "p2-l1" }), literal({ text: "p2-l2" })),
            ],
          }),
          variable("v2"),
        ),
      );
      const result = Actions.toggleBulletList(state, {
        blockIndex: 0,
        contentIndex: 1,
        itemIndex: 0,
        itemContentIndex: 1,
      }).redigertBrev.blocks[0];

      expect(result.content).toHaveLength(5);
      expect(result.content.slice(1, 3)).toEqual(
        select<Item>(state, { blockIndex: 0, contentIndex: 1, itemIndex: 0 }).content,
      );
    });
    test("last of two items, then content is inserted after list", () => {
      const state = letter(
        paragraph(
          variable("v1"),
          itemList({
            items: [
              item(literal({ text: "p1-l1" }), literal({ text: "p1-l2" })),
              item(literal({ text: "p2-l1" }), literal({ text: "p2-l2" })),
            ],
          }),
          variable("v2"),
        ),
      );
      const result = Actions.toggleBulletList(state, {
        blockIndex: 0,
        contentIndex: 1,
        itemIndex: 1,
        itemContentIndex: 1,
      }).redigertBrev.blocks[0];

      expect(result.content).toHaveLength(5);
      expect(result.content.slice(2, 4)).toEqual(
        select<Item>(state, { blockIndex: 0, contentIndex: 1, itemIndex: 1 }).content,
      );
    });
    test("only item, then content replaces list", () => {
      const state = letter(
        paragraph(
          variable("v1"),
          itemList({
            items: [item(literal({ text: "p2-l1" }), literal({ text: "p2-l2" }))],
          }),
          variable("v2"),
        ),
      );
      const result = Actions.toggleBulletList(state, {
        blockIndex: 0,
        contentIndex: 1,
        itemIndex: 0,
        itemContentIndex: 1,
      }).redigertBrev.blocks[0];

      expect(result.content).toHaveLength(4);
      expect(result.content.slice(1, 3)).toEqual(
        select<Item>(state, { blockIndex: 0, contentIndex: 1, itemIndex: 0 }).content,
      );
    });

    test("middle, then list is split and itemContent is moved", () => {
      const state = letter(
        paragraph(
          variable("v1"),
          itemList({
            items: [
              item(literal({ text: "p1-l1" }), literal({ text: "p1-l2" })),
              item(literal({ text: "p2-l1" }), literal({ text: "p2-l2" })),
              item(literal({ text: "p3-l1" }), literal({ text: "p3-l2" })),
            ],
          }),
          variable("v2"),
        ),
      );
      const result = Actions.toggleBulletList(state, {
        blockIndex: 0,
        contentIndex: 1,
        itemIndex: 1,
        itemContentIndex: 1,
      }).redigertBrev.blocks[0];

      expect(result.content).toHaveLength(6);
      expect(result.content.slice(2, 4)).toEqual(
        select<Item>(state, { blockIndex: 0, contentIndex: 1, itemIndex: 1 }).content,
      );
    });
  });
});
