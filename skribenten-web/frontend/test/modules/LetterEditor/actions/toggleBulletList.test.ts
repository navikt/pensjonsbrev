import { expect } from "vitest";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { newParagraph } from "~/Brevredigering/LetterEditor/actions/common";
import type { ItemList, LiteralValue, ParagraphBlock } from "~/types/brevbakerTypes";

import { item, itemList, letter, literal, paragraph, select } from "../utils";

describe("LetterEditorActions.toggleBulletList", () => {
  describe("has adjoining itemList", () => {
    test("should not merge with itemList in previous block if not first in current block", () => {
      const state = letter(
        paragraph(itemList({ items: [item(literal({ text: "b1-p1" }))] })),
        paragraph(
          literal({ text: "b2-l1" }),
          itemList({ items: [item(literal({ text: "b2-p1" }))] }),
          literal({ text: "b2-l2" }),
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

      expect(
        select<LiteralValue>(result, { blockIndex: 1, contentIndex: 1, itemIndex: 1, itemContentIndex: 0 }),
      ).toEqual(toggledContent);
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

      expect(
        select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 }),
      ).toEqual(toggledContent);
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
      expect(
        select<LiteralValue>(result, { blockIndex: 1, contentIndex: 1, itemIndex: 1, itemContentIndex: 0 }),
      ).toEqual(select<LiteralValue>(state, { blockIndex: 1, contentIndex: 2 }));
      const keptItemList = select<ItemList>(result, { blockIndex: 1, contentIndex: 1 });
      const mergedItemList = state.redigertBrev.blocks[1].content.find(
        (c) => c.type === "ITEM_LIST" && c.id !== keptItemList.id,
      ) as ItemList;
      expect(keptItemList.items).toContain(mergedItemList.items[0]);
    });
  });

  describe("retains deletedContent", () => {
    test("previous deletedContent is kept for block", () => {
      const state = letter(newParagraph({ id: 1, content: [literal({ text: "l1" })], deletedContent: [-1] }));
      const result = Actions.toggleBulletList(state, { blockIndex: 0, contentIndex: 0 });
      const deletedContent = result.redigertBrev.blocks[0].deletedContent;

      expect(deletedContent).toContain(select<LiteralValue>(state, { blockIndex: 0, contentIndex: 0 }).id);
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
  });
});
