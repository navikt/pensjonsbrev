import { describe, expect, test } from "vitest";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { newParagraph } from "~/Brevredigering/LetterEditor/actions/common";
import { type Item, type ItemList, ListType, type LiteralValue, type ParagraphBlock } from "~/types/brevbakerTypes";

import { item, itemList, letter, literal, paragraph, select, variable } from "../utils";

describe("LetterEditorActions.toggleBulletList", () => {
  describe("has adjoining itemList", () => {
    test("should not merge with itemList in previous block if not first in current block", () => {
      const state = letter(
        paragraph([itemList({ items: [item(literal({ text: "b0-c1" }))] })]),
        paragraph([
          literal({ text: "b1-c0" }),
          itemList({ items: [item(literal({ text: "b1-c1-i0-ic0" }))] }),
          literal({ text: "b1-c2" }),
        ]),
      );
      const result = Actions.toggleBulletList(state, { blockIndex: 1, contentIndex: 2 });

      // With new rule: template list (non-null id) is tolerated in a mixed block — block1 is not split.
      // block0 (in the previous block) must not be touched.
      expect(result.redigertBrev.blocks).toHaveLength(2);
      expect(result.redigertBrev.blocks[0]).toEqual(state.redigertBrev.blocks[0]);

      const toggledContent = select<LiteralValue>(state, { blockIndex: 1, contentIndex: 2 });

      // block1 keeps the leading literal AND the merged list (not split out)
      const block1After = select<ParagraphBlock>(result, { blockIndex: 1 });
      expect(block1After.content).toHaveLength(2);
      expect(block1After.content[0]).toEqual(state.redigertBrev.blocks[1].content[0]);
      expect(block1After.deletedContent).toContain(toggledContent.id);

      // The merged list (at contentIndex 1) has both items; the toggled literal is item[1]
      expect(select<Item>(result, { blockIndex: 1, contentIndex: 1, itemIndex: 1 }).content).toEqual([toggledContent]);
    });
    test("should not merge with itemList in next block if not last in current block", () => {
      const state = letter(
        paragraph([
          literal({ text: "b1-l1" }),
          itemList({ items: [item(literal({ text: "b1-p1" }))] }),
          literal({ text: "b1-l2" }),
        ]),
        paragraph([itemList({ items: [item(literal({ text: "b2-p1" }))] })]),
      );
      const result = Actions.toggleBulletList(state, { blockIndex: 0, contentIndex: 0 });

      // With new rule: template list (non-null id) is tolerated in a mixed block — block0 is not split.
      // Original block1 (the next block) must not be touched.
      expect(result.redigertBrev.blocks).toHaveLength(2);
      expect(result.redigertBrev.blocks[1]).toEqual(state.redigertBrev.blocks[1]);

      const toggledContent = select<LiteralValue>(state, { blockIndex: 0, contentIndex: 0 });

      // block0 keeps the merged list AND the trailing literal (not split out)
      const block0After = select<ParagraphBlock>(result, { blockIndex: 0 });
      expect(block0After.content).toHaveLength(2);
      expect(block0After.deletedContent).toContain(toggledContent.id);
      expect(block0After.content[1]).toEqual(state.redigertBrev.blocks[0].content.at(-1));

      // The toggled literal is now item[0] in the merged list
      expect(select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).content).toEqual([toggledContent]);
    });
    test("should not merge with itemList in previous and next block if not first and last in block", () => {
      const state = letter(
        paragraph([itemList({ id: 1, items: [item(literal({ text: "b1-p1" }))] })]),
        paragraph([
          literal({ text: "b2-l1" }),
          itemList({ id: 2, items: [item(literal({ text: "b2-ul1-p1" }))] }),
          literal({ id: 22, text: "b2-l2" }),
          itemList({ id: 3, items: [item(literal({ text: "b2-ul2-p1" }))] }),
          literal({ text: "b2-l3" }),
        ]),
        paragraph([itemList({ id: 4, items: [item(literal({ text: "b3-p1" }))] })]),
      );

      const result = Actions.toggleBulletList(state, { blockIndex: 1, contentIndex: 2 });

      // With new rule: template lists (id=2, id=3) are tolerated in a mixed block — block1 is not split.
      // block0 and block2 must be untouched.
      expect(result.redigertBrev.blocks).toHaveLength(3);
      expect(result.redigertBrev.blocks[0]).toEqual(state.redigertBrev.blocks[0]);
      expect(result.redigertBrev.blocks[2]).toEqual(state.redigertBrev.blocks[2]);

      // block1 keeps leading literal, merged list, and trailing literal (3 elements, not split)
      const block1After = select<ParagraphBlock>(result, { blockIndex: 1 });
      expect(block1After.content).toHaveLength(3);
      expect(block1After.content[0]).toEqual(state.redigertBrev.blocks[1].content[0]);
      expect(block1After.content[2]).toEqual(state.redigertBrev.blocks[1].content.at(-1));
      // deletedContent: lit(22) and id=3 removed by toggleListOn; id=2 un-deleted by addElements.
      // lit("b2-l3") is not removed — it stays in block1 under the new rule.
      expect(block1After.deletedContent).toContain(22);
      expect(block1After.deletedContent).toContain(3);
      expect(block1After.deletedContent).not.toContain(
        select<LiteralValue>(state, { blockIndex: 1, contentIndex: 4 }).id,
      );

      // The merged list (id=2) is at contentIndex=1 and has all 3 items
      const keptItemList = select<ItemList>(result, { blockIndex: 1, contentIndex: 1 });
      expect(keptItemList.id).toBe(2);
      expect(keptItemList.items).toHaveLength(3);

      // The toggled content (b2-l2, id=22) is item[1] in the merged list
      expect(select<Item>(result, { blockIndex: 1, contentIndex: 1, itemIndex: 1 }).content).toEqual([
        select<LiteralValue>(state, { blockIndex: 1, contentIndex: 2 }),
      ]);
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

      // With new rule: template lists (non-null ids) tolerated in a mixed block — block1 is not split.
      // block0 (blokk før) must not be touched.
      expect(result.redigertBrev.blocks).toHaveLength(2);
      expect(result.redigertBrev.blocks[0]).toEqual(state.redigertBrev.blocks[0]);

      // block1 keeps all 3 positions: list1, "l1", merged-list
      const block1After = select<ParagraphBlock>(result, { blockIndex: 1 });
      expect(block1After.content).toHaveLength(3);

      // "punktliste1" is preserved unchanged at contentIndex 0
      expect(select<ItemList>(result, { blockIndex: 1, contentIndex: 0 }).items).toEqual(
        select<ItemList>(state, { blockIndex: 1, contentIndex: 0 }).items,
      );

      // The merged list (punktliste2 + l2) is at contentIndex 2 with 2 items
      expect(
        select<LiteralValue>(result, { blockIndex: 1, contentIndex: 2, itemIndex: 1, itemContentIndex: 0 })?.text,
      ).toEqual("l2");
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
        paragraph([
          itemList({ items: [item(literal({ text: "l1" }))], deletedItems: [-1] }),
          literal({ text: "l2" }),
          itemList({ items: [item(literal({ text: "l3" }))], deletedItems: [-2] }),
        ]),
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
        paragraph([
          itemList({
            id: -1,
            items: [
              item(literal({ text: "b0-c0-i0-ic0" })),
              item(literal({ text: "b0-c0-i1-ic0" })),
              item(literal({ text: "b0-c0-i2-ic0" })),
            ],
            deletedItems: [-2],
          }),
        ]),
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

      // toggleListOff (middle) produces 3 blocks: first-half list, text, second-half list
      expect(result.redigertBrev.blocks).toHaveLength(3);

      const keptList = select<ItemList>(result, { blockIndex: 0, contentIndex: 0 });
      expect(keptList).not.toBeUndefined();
      expect(keptList.id).toBe(-1);
      expect(keptList.items).toHaveLength(1);
      // deletedItems preserves the original -2 plus the ids of the two removed items
      expect(keptList.deletedItems).toEqual([-2, ...originalItemList.items.slice(1).map((c) => c.id)]);
    });
  });

  describe("updates deleted", () => {
    test("when toggling off first item it is deleted", () => {
      const state = letter(
        paragraph([itemList({ items: [item(literal({ text: "p1" })), item(literal({ text: "p2" }))] })]),
      );
      const result = Actions.toggleBulletList(state, {
        blockIndex: 0,
        contentIndex: 0,
        itemIndex: 0,
        itemContentIndex: 0,
      });

      // The remaining list moves to a new block1; item0's id is in that list's deletedItems
      expect(select<ItemList>(result, { blockIndex: 1, contentIndex: 0 }).deletedItems).toContain(
        select<Item>(state, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).id,
      );
    });

    test("when toggling off only item, then itemList is deleted", () => {
      const state = letter(paragraph([itemList({ items: [item(literal({ text: "p1" }))] })]));
      const toggleIndex = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
      const originalItem = select<Item>(state, { blockIndex: 0, contentIndex: 0, itemIndex: 0 });
      const result = Actions.toggleBulletList(state, toggleIndex);

      expect(result.redigertBrev.blocks[0].content).toHaveLength(1);
      expect(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 })).toEqual(originalItem.content[0]);
      expect(result.redigertBrev.blocks[0].deletedContent).toContain(
        select<ItemList>(state, { blockIndex: 0, contentIndex: 0 }).id,
      );
    });

    test("when toggling off last item it is deleted", () => {
      const state = letter(
        paragraph([itemList({ items: [item(literal({ text: "p1" })), item(literal({ text: "p2" }))] })]),
      );
      const result = Actions.toggleBulletList(state, {
        blockIndex: 0,
        contentIndex: 0,
        itemIndex: 1,
        itemContentIndex: 0,
      });

      expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).deletedItems).toContain(
        select<Item>(state, { blockIndex: 0, contentIndex: 0, itemIndex: 1 }).id,
      );
    });
  });

  describe("focus is moved", () => {
    test("when toggling off only item focus is moved", () => {
      const state = letter(
        paragraph([itemList({ items: [item(literal({ text: "p1" }), literal({ text: "p1-l2" }))] })]),
      );
      const toggleIndex = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 1 };
      const result = Actions.toggleBulletList(state, toggleIndex);

      expect(result.redigertBrev.blocks).toHaveLength(1);
      expect(result.redigertBrev.blocks[0].content).toHaveLength(2);
      expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 1 });
    });

    test("when toggling off first item focus should be moved", () => {
      const state = letter(
        paragraph([
          literal({ text: "b0-c0" }),
          itemList({
            items: [item(literal({ text: "b0-c1-i0-ic0" })), item(literal({ text: "b0-c1-i1-ic0" }))],
          }),
        ]),
      );
      const result = Actions.toggleBulletList(state, {
        blockIndex: 0,
        contentIndex: 1,
        itemIndex: 0,
        itemContentIndex: 0,
      });
      // Original block (block0) becomes [leading-literal, extracted-item-text].
      // Remaining list moves to block1.
      expect(result.redigertBrev.blocks).toHaveLength(2);
      expect(result.redigertBrev.blocks[0].content).toHaveLength(2);
      expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 1, cursorPosition: state.focus.cursorPosition });
    });

    test("when toggling off first item with multiple literals, focus should be moved", () => {
      const state = letter(
        paragraph([
          literal({ text: "b0-c0" }),
          itemList({
            items: [
              item(literal({ text: "b0-c1-i0-ic0" }), literal({ text: "b0-c1-i0-ic1" })),
              item(literal({ text: "b0-c1-i1-ic0" })),
            ],
          }),
        ]),
      );
      const result = Actions.toggleBulletList(state, {
        blockIndex: 0,
        contentIndex: 1,
        itemIndex: 0,
        itemContentIndex: 1,
      });
      // block0 becomes [leading-literal, lit0, lit1]. Remaining list in block1.
      expect(result.redigertBrev.blocks).toHaveLength(2);
      expect(result.redigertBrev.blocks[0].content).toHaveLength(3);
      expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 2, cursorPosition: state.focus.cursorPosition });
    });

    test("when toggling off last item, focus should be moved", () => {
      const state = letter(
        paragraph([
          literal({ text: "b0-c0" }),
          itemList({
            items: [
              item(literal({ text: "b0-c1-i0-ic0" }), literal({ text: "b0-c1-i0-ic1" })),
              item(literal({ text: "b0-c1-i1-ic0" })),
            ],
          }),
        ]),
      );
      const result = Actions.toggleBulletList(state, {
        blockIndex: 0,
        contentIndex: 1,
        itemIndex: 1,
        itemContentIndex: 0,
      });

      // block0 keeps [leading-literal, remaining-list]. Extracted item goes to block1.
      expect(result.redigertBrev.blocks).toHaveLength(2);
      expect(result.redigertBrev.blocks[0].content).toHaveLength(2);
      expect(result.focus).toEqual({ blockIndex: 1, contentIndex: 0, cursorPosition: state.focus.cursorPosition });
    });

    test("when toggling off last item with multiple literals, focus should be moved", () => {
      const state = letter(
        paragraph([
          literal({ text: "b0-c0" }),
          itemList({
            items: [
              item(literal({ text: "b0-c1-i0-ic0" }), literal({ text: "b0-c1-i0-ic1" })),
              item(literal({ text: "b0-c1-i1-ic0" }), literal({ text: "b0-c1-i1-ic1" })),
            ],
          }),
        ]),
      );
      const result = Actions.toggleBulletList(state, {
        blockIndex: 0,
        contentIndex: 1,
        itemIndex: 1,
        itemContentIndex: 1,
      });

      // block0 keeps [leading-literal, remaining-list]. Extracted item (2 literals) in block1.
      expect(result.redigertBrev.blocks).toHaveLength(2);
      expect(result.redigertBrev.blocks[0].content).toHaveLength(2);
      expect(result.focus).toEqual({ blockIndex: 1, contentIndex: 1, cursorPosition: state.focus.cursorPosition });
    });

    test("when toggling off item in the middle, focus should be moved", () => {
      const state = letter(
        paragraph([
          variable("v1"),
          itemList({
            items: [
              item(literal({ text: "p1-l1" }), literal({ text: "p1-l2" })),
              item(literal({ text: "p2-l1" }), literal({ text: "p2-l2" })),
              item(literal({ text: "p3" })),
            ],
          }),
          variable("v2"),
        ]),
      );
      const result = Actions.toggleBulletList(state, {
        blockIndex: 0,
        contentIndex: 1,
        itemContentIndex: 1,
        itemIndex: 1,
      });

      // block0 keeps [v1, first-half-list, v2]. Text block at block1, second-half list at block2.
      expect(result.redigertBrev.blocks).toHaveLength(3);
      expect(result.redigertBrev.blocks[0].content).toHaveLength(3);
      expect(result.focus).toEqual({ blockIndex: 1, contentIndex: 1, cursorPosition: state.focus.cursorPosition });
    });
  });

  describe("toggle off", () => {
    test("first of two items: extracted text stays in original block, remaining list moves to new block", () => {
      const state = letter(
        paragraph([
          variable("v1"),
          itemList({
            items: [
              item(literal({ text: "p1-l1" }), literal({ text: "p1-l2" })),
              item(literal({ text: "p2-l1" }), literal({ text: "p2-l2" })),
            ],
          }),
          variable("v2"),
        ]),
      );
      const result = Actions.toggleBulletList(state, {
        blockIndex: 0,
        contentIndex: 1,
        itemIndex: 0,
        itemContentIndex: 1,
      });

      // Original block becomes [v1, item0-lit1, item0-lit2, v2]; remaining list in block1
      expect(result.redigertBrev.blocks).toHaveLength(2);
      const block0 = result.redigertBrev.blocks[0];
      expect(block0.content).toHaveLength(4);
      expect(block0.content.slice(1, 3)).toEqual(
        select<Item>(state, { blockIndex: 0, contentIndex: 1, itemIndex: 0 }).content,
      );
    });
    test("last of two items: original block keeps list, extracted text goes to new block", () => {
      const state = letter(
        paragraph([
          variable("v1"),
          itemList({
            items: [
              item(literal({ text: "p1-l1" }), literal({ text: "p1-l2" })),
              item(literal({ text: "p2-l1" }), literal({ text: "p2-l2" })),
            ],
          }),
          variable("v2"),
        ]),
      );
      const result = Actions.toggleBulletList(state, {
        blockIndex: 0,
        contentIndex: 1,
        itemIndex: 1,
        itemContentIndex: 1,
      });

      // Original block keeps [v1, list([item0]), v2]; extracted item1 content in block1
      expect(result.redigertBrev.blocks).toHaveLength(2);
      expect(result.redigertBrev.blocks[0].content).toHaveLength(3);
      expect(result.redigertBrev.blocks[1].content).toEqual(
        select<Item>(state, { blockIndex: 0, contentIndex: 1, itemIndex: 1 }).content,
      );
    });
    test("only item, then content replaces list", () => {
      const state = letter(
        paragraph([
          variable("v1"),
          itemList({
            items: [item(literal({ text: "p2-l1" }), literal({ text: "p2-l2" }))],
          }),
          variable("v2"),
        ]),
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

    test("middle: original block keeps first-half list, text and second-half list go to new blocks", () => {
      const state = letter(
        paragraph([
          variable("v1"),
          itemList({
            items: [
              item(literal({ text: "p1-l1" }), literal({ text: "p1-l2" })),
              item(literal({ text: "p2-l1" }), literal({ text: "p2-l2" })),
              item(literal({ text: "p3-l1" }), literal({ text: "p3-l2" })),
            ],
          }),
          variable("v2"),
        ]),
      );
      const result = Actions.toggleBulletList(state, {
        blockIndex: 0,
        contentIndex: 1,
        itemIndex: 1,
        itemContentIndex: 1,
      });

      // block0=[v1, list([item0]), v2], block1=[item1 content], block2=[list([item2])]
      expect(result.redigertBrev.blocks).toHaveLength(3);
      expect(result.redigertBrev.blocks[0].content).toHaveLength(3);
      expect(result.redigertBrev.blocks[1].content).toEqual(
        select<Item>(state, { blockIndex: 0, contentIndex: 1, itemIndex: 1 }).content,
      );
    });
  });
});

describe("LetterEditorActions.toggleNumberList", () => {
  test("converts a literal to a numbered list", () => {
    const state = letter(paragraph([literal({ text: "tekst" })]));
    const result = Actions.toggleNumberList(state, { blockIndex: 0, contentIndex: 0 });

    const list = select<ItemList>(result, { blockIndex: 0, contentIndex: 0 });
    expect(list.listType).toBe(ListType.NUMMERERT_LISTE);
    expect(list.items).toHaveLength(1);
  });

  test("toggling again on the same item removes the list", () => {
    const state = letter(paragraph([literal({ text: "tekst" })]));
    const afterOn = Actions.toggleNumberList(state, { blockIndex: 0, contentIndex: 0 });
    const afterOff = Actions.toggleNumberList(afterOn, {
      blockIndex: 0,
      contentIndex: 0,
      itemIndex: 0,
      itemContentIndex: 0,
    });

    expect(afterOff.redigertBrev.blocks[0].content[0].type).toBe("LITERAL");
  });
});

describe("switchListType (via toggleBulletList / toggleNumberList on an existing list)", () => {
  test("switching a bullet list to numbered sets editedListType", () => {
    const state = letter(
      paragraph([itemList({ listType: ListType.PUNKTLISTE, items: [item(literal({ text: "a" }))] })]),
    );

    const result = Actions.toggleNumberList(state, {
      blockIndex: 0,
      contentIndex: 0,
      itemIndex: 0,
      itemContentIndex: 0,
    });

    const list = select<ItemList>(result, { blockIndex: 0, contentIndex: 0 });
    expect(list.listType).toBe(ListType.PUNKTLISTE);
    expect(list.editedListType).toBe(ListType.NUMMERERT_LISTE);
  });

  test("switching a numbered list to bullet sets editedListType", () => {
    const state = letter(
      paragraph([itemList({ listType: ListType.NUMMERERT_LISTE, items: [item(literal({ text: "a" }))] })]),
    );

    const result = Actions.toggleBulletList(state, {
      blockIndex: 0,
      contentIndex: 0,
      itemIndex: 0,
      itemContentIndex: 0,
    });

    const list = select<ItemList>(result, { blockIndex: 0, contentIndex: 0 });
    expect(list.listType).toBe(ListType.NUMMERERT_LISTE);
    expect(list.editedListType).toBe(ListType.PUNKTLISTE);
  });

  test("switching back to original type clears editedListType", () => {
    const state = letter(
      paragraph([itemList({ listType: ListType.PUNKTLISTE, items: [item(literal({ text: "a" }))] })]),
    );
    const afterSwitch = Actions.toggleNumberList(state, {
      blockIndex: 0,
      contentIndex: 0,
      itemIndex: 0,
      itemContentIndex: 0,
    });
    const afterRevert = Actions.toggleBulletList(afterSwitch, {
      blockIndex: 0,
      contentIndex: 0,
      itemIndex: 0,
      itemContentIndex: 0,
    });

    const list = select<ItemList>(afterRevert, { blockIndex: 0, contentIndex: 0 });
    expect(list.editedListType).toBeNull();
  });

  test("after switching type, adjacent block with the new type is merged", () => {
    const state = letter(
      paragraph([itemList({ listType: ListType.PUNKTLISTE, items: [item(literal({ text: "a" }))] })]),
      paragraph([itemList({ listType: ListType.NUMMERERT_LISTE, items: [item(literal({ text: "b" }))] })]),
    );

    // Switch block0 from bullet to numbered → should merge with block1
    const result = Actions.toggleNumberList(state, {
      blockIndex: 0,
      contentIndex: 0,
      itemIndex: 0,
      itemContentIndex: 0,
    });

    expect(result.redigertBrev.blocks).toHaveLength(1);
    expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).items).toHaveLength(2);
  });
});
