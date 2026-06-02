import { describe, expect, test } from "vitest";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { newLiteral } from "~/Brevredigering/LetterEditor/actions/common";
import { isEmptyContent, ZERO_WIDTH_SPACE } from "~/Brevredigering/LetterEditor/model/utils";
import {
  type Content,
  type Item,
  type ItemList,
  ListType,
  type LiteralValue,
  type ParagraphBlock,
  type TextContent,
} from "~/types/brevbakerTypes";

import { asNew, item, itemList, letter, literal, paragraph, select, variable } from "../utils";

describe("LetterEditorActions.split", () => {
  describe("at literal", () => {
    test("specified block is split at contentIndex and offset", () => {
      const state = letter(paragraph([variable("var1"), literal({ text: "lit1" }), variable("var2")]));
      const splitId = { blockIndex: 0, contentIndex: 1 };
      const result = Actions.split(state, splitId, 2);
      const resultBlocks = result.redigertBrev.blocks;

      expect(resultBlocks).toHaveLength(2);

      expect(resultBlocks[splitId.blockIndex].content).toHaveLength(2);
      expect(select<LiteralValue>(result, splitId).text).toEqual("lit1");
      expect(select<LiteralValue>(result, splitId).editedText).toEqual("li");

      expect(resultBlocks[splitId.blockIndex + 1].content).toHaveLength(2);
      const splitContent = select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 });
      expect(splitContent.text).toEqual("");
      expect(splitContent.editedText).toEqual("t1");
      expect(select<Content>(result, { blockIndex: 1, contentIndex: 1 })).toStrictEqual(
        select<Content>(state, { blockIndex: 0, contentIndex: 2 }),
      );

      expect(result.focus).toEqual({ blockIndex: splitId.blockIndex + 1, contentIndex: 0, cursorPosition: 0 });
    });

    test("result is not the original array", () => {
      const state = letter(paragraph([literal({ text: "lit1" })]));
      const result = Actions.split(state, { blockIndex: 0, contentIndex: 0 }, 2);

      expect(result.redigertBrev.blocks).not.toBe(state.redigertBrev.blocks);
    });

    test("when the offset is at the end of the current content, the new block will have one content element with an empty string", () => {
      const state = letter(paragraph([literal({ text: "lit1" })]));
      const splitId = { blockIndex: 0, contentIndex: 0 };
      const offset = select<TextContent>(state, splitId).text.length;

      const result = Actions.split(state, splitId, offset);
      const resultBlocks = result.redigertBrev.blocks;

      expect(resultBlocks.length).toBe(state.redigertBrev.blocks.length + 1);
      expect(resultBlocks[splitId.blockIndex + 1].content.length).toBe(1);
      expect(select<TextContent>(result, { blockIndex: splitId.blockIndex + 1, contentIndex: 0 }).text).toBe("");

      expect(result.focus).toEqual({ blockIndex: splitId.blockIndex + 1, contentIndex: 0, cursorPosition: 0 });
    });

    test("does not split an emptyBlock", () => {
      const state = letter(paragraph([literal({ text: "" })]));
      const result = Actions.split(state, { blockIndex: 0, contentIndex: 0 }, 0);
      expect(result.redigertBrev).toBe(state.redigertBrev);
    });

    test("does not split at the very beginning of block when previous is empty", () => {
      const state = letter(paragraph([literal({ text: "" })]), paragraph([literal({ text: "lit2" })]));
      const result = Actions.split(state, { blockIndex: 1, contentIndex: 0 }, 0);
      expect(result.redigertBrev).toBe(state.redigertBrev);
    });

    test("when splitting at the very end of a block the new block and content gets no ID (new)", () => {
      const state = letter(paragraph([variable("var1"), variable("var2"), literal({ text: "lit1" })]));
      const result = Actions.split(state, { blockIndex: 0, contentIndex: 2 }, 4);

      expect(result.redigertBrev.blocks).toHaveLength(2);
      const newContent = select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 });
      expect(select<ParagraphBlock>(result, { blockIndex: 1 }).id).toBeNull();
      expect(newContent.id).toBeNull();
      expect(newContent.text).toEqual("");
    });

    test("when splitting in the last content of a block the new block and content gets no ID (new)", () => {
      const state = letter(paragraph([variable("var1"), variable("var2"), literal({ text: "lit1" })]));
      const result = Actions.split(state, { blockIndex: 0, contentIndex: 2 }, 2);

      expect(result.redigertBrev.blocks).toHaveLength(2);
      expect(select<ParagraphBlock>(result, { blockIndex: 1 }).id).toBeNull();
      expect(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }).id).toBeNull();
    });

    test("when splitting at the very beginning of the last content of a block the content retains id in new block", () => {
      const state = letter(paragraph([variable("var1"), variable("var2"), literal({ text: "lit1" })]));
      const result = Actions.split(state, { blockIndex: 0, contentIndex: 2 }, 0);

      expect(result.redigertBrev.blocks).toHaveLength(2);
      const newContent = select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 });
      expect(select<ParagraphBlock>(result, { blockIndex: 1 }).id).toBeNull();
      expect(newContent.id).toEqual(select<TextContent>(state, { blockIndex: 0, contentIndex: 2 }).id);
      expect(select<ParagraphBlock>(result, { blockIndex: 0 }).content).toHaveLength(3);
    });

    test("when splitting at the end of a literal there are no changes to editedText", () => {
      const state = letter(paragraph([literal({ text: "lit1" })]));
      const result = Actions.split(state, { blockIndex: 0, contentIndex: 0 }, "lit1".length);

      expect(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }).editedText).toBeNull();
    });

    test("splitting uses editedText if present", () => {
      const state = letter(paragraph([literal({ text: "lit1", editedText: "new text" })]));
      const result = Actions.split(state, { blockIndex: 0, contentIndex: 0 }, 4);

      expect(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }).editedText).toEqual("new ");
      expect(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }).editedText).toEqual("text");
    });

    test("splitting at the end of a content not last in block will not result in a block with an empty content as first element", () => {
      const state = letter(
        paragraph([literal({ text: "heisann" }), itemList({ items: [item(literal({ text: "item 1" }))] })]),
      );

      const result = Actions.split(state, { blockIndex: 0, contentIndex: 0 }, "heisann".length);

      expect(result.redigertBrev.blocks).toHaveLength(2);
      expect(select<ParagraphBlock>(result, { blockIndex: 0 }).content).toHaveLength(1);
      expect(select<ParagraphBlock>(result, { blockIndex: 1 }).content).toHaveLength(1);
      expect(select<ParagraphBlock>(result, { blockIndex: 1, contentIndex: 0 })).toSatisfy(
        (c: LiteralValue) => !isEmptyContent(c),
      );
    });

    test("when splitting a block the content moved to the next block is marked as deleted", () => {
      const state = letter(paragraph([literal({ text: "before split" }), literal({ text: "after split" })]));

      const result = Actions.split(state, { blockIndex: 0, contentIndex: 0 }, "before split".length);

      expect(result.redigertBrev.blocks).toHaveLength(2);
      expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([
        select<LiteralValue>(state, { blockIndex: 0, contentIndex: 1 }).id,
      ]);
    });

    test("can split a block at offset 0 when not at the first content", () => {
      const state = letter(
        paragraph([literal({ text: "" })]),
        paragraph([variable("before split"), literal({ text: "after split" })]),
      );

      const result = Actions.split(state, { blockIndex: 1, contentIndex: 1 }, 0);

      expect(result.redigertBrev.blocks).toHaveLength(3);
    });

    test("when splitting at the very beginning of a block then an new block should be inserted at cursor", () => {
      const state = letter(paragraph([literal({ text: "begynnelse av block" }), variable("slutten")]));

      const result = Actions.split(state, { blockIndex: 0, contentIndex: 0 }, 0);

      expect(result.redigertBrev.blocks).toHaveLength(2);

      const newBlock = select<ParagraphBlock>(result, { blockIndex: 0 });
      expect(newBlock.id).toBeNull();
      expect(newBlock.deletedContent).toHaveLength(0);

      const movedExistingBlock = select<ParagraphBlock>(result, { blockIndex: 1 });
      expect(movedExistingBlock.id).toStrictEqual(select<ParagraphBlock>(state, { blockIndex: 0 }).id);
      expect(movedExistingBlock.deletedContent).toHaveLength(0);
    });

    test("cannot split from an empty block", () => {
      const state = letter(
        paragraph([literal({ text: "ikke tom" }), itemList({ items: [item(literal({ text: "ikke tom" }))] })]),
        paragraph([literal({ text: "" })]),
      );

      const result = Actions.split(state, { blockIndex: 1, contentIndex: 0 }, 0);

      expect(result.redigertBrev.blocks).toHaveLength(2);
    });

    test("when splitting from beginning of block then focus shifts with the block", () => {
      const state = letter(paragraph([literal({ text: "ikke tom" })]));

      const result = Actions.split(state, { blockIndex: 0, contentIndex: 0 }, 0);

      expect(result.redigertBrev.blocks).toHaveLength(2);
      expect(result.focus).toStrictEqual({ blockIndex: 1, contentIndex: 0, cursorPosition: 0 });
    });
  });

  describe("at itemList", () => {
    test("specified item is split at contentIndex and offset", () => {
      const state = letter(
        paragraph([itemList({ items: [item(variable("var1"), literal({ text: "lit1" }), variable("var2"))] })]),
      );
      const splitId = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 1 };
      const offset = 2;
      const original = select<ItemList>(state, { blockIndex: 0, contentIndex: 0 });

      const result = Actions.split(state, splitId, offset);
      const resultItems = select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).items;

      expect(resultItems).toHaveLength(original.items.length + 1);

      // split item
      const splitItem = resultItems[splitId.itemIndex];
      expect(splitItem.content).toHaveLength(splitId.itemContentIndex + 1);
      expect(splitItem.content[splitId.itemContentIndex].text).toEqual(select<LiteralValue>(state, splitId).text);
      expect(select<LiteralValue>(result, splitId).editedText).toEqual(
        select<LiteralValue>(state, splitId).text.slice(0, Math.max(0, offset)),
      );

      const newItem = resultItems[splitId.itemIndex + 1];
      expect(newItem.id).toBeNull();
      expect(newItem.content).toHaveLength(original.items[splitId.itemIndex].content.length - splitId.itemContentIndex);
      expect(newItem.content[0].text).toEqual("");
      expect((newItem.content[0] as LiteralValue).editedText).toEqual(
        original.items[splitId.itemIndex].content[splitId.itemContentIndex].text.slice(Math.max(0, offset)),
      );

      // focus is moved to beginning of new item
      expect(result.focus).toEqual({
        blockIndex: splitId.blockIndex,
        contentIndex: splitId.contentIndex,
        cursorPosition: 0,
        itemIndex: splitId.itemIndex + 1,
        itemContentIndex: 0,
      });
    });

    test("when the offset is at the end of the item content, the new item will have one content element with an empty string", () => {
      const state = letter(paragraph([itemList({ items: [item(variable("var1"), literal({ text: "lit1" }))] })]));
      const splitId = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 1 };
      const original = select<ItemList>(state, { blockIndex: 0, contentIndex: 0 });

      const result = Actions.split(state, splitId, select<TextContent>(state, splitId).text.length);
      const resultItems = select<ItemList>(result, { blockIndex: 0, contentIndex: 0 });

      expect(resultItems.items).toHaveLength(original.items.length + 1);
      const newItem = resultItems.items[splitId.itemIndex + 1];
      expect(newItem.content).toHaveLength(1);
      expect(newItem.content[0].text).toEqual("");
    });

    test("can create new item around non-empty new item", () => {
      const state = letter(
        paragraph([
          itemList({
            items: [
              item(literal({ text: "p1" })),
              asNew(item(asNew(literal({ text: "" })), literal({ text: "p2" }))),
              item(literal({ text: "p3" })),
            ],
          }),
        ]),
      );
      const splitBefore = { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 };
      const splitAt = { blockIndex: 0, contentIndex: 0, itemIndex: 1, itemContentIndex: 0 };
      const splitAfter = { blockIndex: 0, contentIndex: 0, itemIndex: 2, itemContentIndex: 0 };

      expect(
        select<ItemList>(Actions.split(state, splitBefore, 2), { blockIndex: 0, contentIndex: 0 }).items,
      ).toHaveLength(4);
      expect(select<ItemList>(Actions.split(state, splitAt, 2), { blockIndex: 0, contentIndex: 0 }).items).toHaveLength(
        4,
      );
      expect(
        select<ItemList>(Actions.split(state, splitAfter, 0), { blockIndex: 0, contentIndex: 0 }).items,
      ).toHaveLength(4);
    });

    test("does not split so that we get multiple empty items", () => {
      const state = letter(
        paragraph([
          itemList({
            items: [item(literal({ text: "item1" })), item(literal({ text: "" })), item(literal({ text: "item3" }))],
          }),
        ]),
      );

      // Split at the empty item → breaks out to separate blocks
      const resultFromEmpty = Actions.split(
        state,
        { blockIndex: 0, contentIndex: 0, itemIndex: 1, itemContentIndex: 0 },
        0,
      );
      // Should now be 3 blocks: [list: item1] [blank literal] [list: item3]
      expect(resultFromEmpty.redigertBrev.blocks).toHaveLength(3);
      expect(select<ItemList>(resultFromEmpty, { blockIndex: 0, contentIndex: 0 }).items).toHaveLength(1);
      expect(select<ItemList>(resultFromEmpty, { blockIndex: 2, contentIndex: 0 }).items).toHaveLength(1);

      // Split before the empty item (at end of item1) should still be blocked to prevent consecutive empty items
      expect(
        Actions.split(state, { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 }, "item1".length)
          .redigertBrev,
      ).toStrictEqual(state.redigertBrev);
      // Split at beginning of item after empty item should still be blocked
      expect(
        Actions.split(state, { blockIndex: 0, contentIndex: 0, itemIndex: 2, itemContentIndex: 0 }, 0).redigertBrev,
      ).toStrictEqual(state.redigertBrev);
    });

    test("splits the last empty item into a new block with blank literal", () => {
      const state = letter(
        paragraph([itemList({ items: [item(literal({ text: "lit1" })), item(literal({ text: "" }))] })]),
      );

      const result = Actions.split(state, { blockIndex: 0, contentIndex: 0, itemIndex: 1, itemContentIndex: 0 }, 0);
      // Should be 2 blocks: [list: lit1] [blank literal]
      expect(result.redigertBrev.blocks).toHaveLength(2);
      expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).items).toHaveLength(1);
      expect(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 })).toStrictEqual(newLiteral());
      expect(result.focus).toEqual({ blockIndex: 1, contentIndex: 0, cursorPosition: 0 });
    });

    test("splitting the last empty item when parent block has subsequent content creates separate blocks", () => {
      const state = letter(
        paragraph([
          itemList({ items: [item(literal({ text: "lit1" })), item(literal({ text: "" }))] }),
          literal({ text: "after" }),
        ]),
      );

      const result = Actions.split(state, { blockIndex: 0, contentIndex: 0, itemIndex: 1, itemContentIndex: 0 }, 0);

      // Should be 3 blocks: [list: lit1] [blank literal] [literal: after]
      expect(result.redigertBrev.blocks).toHaveLength(3);
      expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).items).toHaveLength(1);
      expect(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 })).toStrictEqual(newLiteral());
    });

    test("splitting from the last (empty) item creates a blank block, and another split on the blank block is a no-op", () => {
      const state = letter(
        paragraph([itemList({ items: [item(literal({ text: "item1" })), item(literal({ text: "" }))] })]),
      );

      const splitFromEmptyItem = Actions.split(
        state,
        { blockIndex: 0, contentIndex: 0, itemIndex: 1, itemContentIndex: 0 },
        0,
      );
      // Should be 2 blocks: [list: item1] [blank literal]
      expect(splitFromEmptyItem.redigertBrev.blocks).toHaveLength(2);
      expect(select<ItemList>(splitFromEmptyItem, { blockIndex: 0, contentIndex: 0 }).items).toHaveLength(1);
      expect(select<LiteralValue>(splitFromEmptyItem, { blockIndex: 1, contentIndex: 0 })).toStrictEqual(newLiteral());

      // Splitting on the blank block (empty) should be a no-op
      const splitFromEmptyLastContent = Actions.split(splitFromEmptyItem, { blockIndex: 1, contentIndex: 0 }, 0);
      expect(splitFromEmptyLastContent.redigertBrev.blocks).toHaveLength(2);
    });

    test("splitting from last new empty item in itemlist breaks out to separate blocks", () => {
      const state = letter(
        paragraph([
          itemList({
            items: [
              item(literal({ text: "item1" })),
              asNew(item(literal({ text: "aa" }))),
              asNew(item(literal({ text: ZERO_WIDTH_SPACE }))),
            ],
          }),
        ]),
      );

      const result = Actions.split(state, { blockIndex: 0, contentIndex: 0, itemIndex: 2, itemContentIndex: 0 }, 0);
      // Should be 2 blocks: [list: item1, aa] [blank literal]
      expect(result.redigertBrev.blocks).toHaveLength(2);
      expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).items).toHaveLength(2);
    });

    test("splitting at the beginning an item keeps id", () => {
      const item1 = item(literal({ text: "hei" }), variable("joda"));
      const state = letter(paragraph([itemList({ items: [item1] })]));

      const result = Actions.split(state, { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 }, 0);
      expect(select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 }).id).toBeNull();
      expect(select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 1 }).id).toStrictEqual(item1.id);
    });

    describe("break out of empty item", () => {
      test("empty first item creates blank block before remaining list", () => {
        const state = letter(
          paragraph([
            itemList({
              items: [item(literal({ text: "" })), item(literal({ text: "item2" })), item(literal({ text: "item3" }))],
            }),
          ]),
        );

        const result = Actions.split(state, { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 }, 0);

        // [block: newLiteral()] [block: list(item2, item3)]
        expect(result.redigertBrev.blocks).toHaveLength(2);
        expect(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 })).toStrictEqual(newLiteral());
        expect(select<ItemList>(result, { blockIndex: 1, contentIndex: 0 }).items).toHaveLength(2);
        expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 0 });
      });

      test("empty middle item splits list into two blocks with blank between", () => {
        const state = letter(
          paragraph([
            itemList({
              items: [item(literal({ text: "item1" })), item(literal({ text: "" })), item(literal({ text: "item3" }))],
            }),
          ]),
        );

        const result = Actions.split(state, { blockIndex: 0, contentIndex: 0, itemIndex: 1, itemContentIndex: 0 }, 0);

        // [block: list(item1)] [block: newLiteral()] [block: list(item3)]
        expect(result.redigertBrev.blocks).toHaveLength(3);
        expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).items).toHaveLength(1);
        expect(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 })).toStrictEqual(newLiteral());
        expect(select<ItemList>(result, { blockIndex: 2, contentIndex: 0 }).items).toHaveLength(1);
        expect(result.focus).toEqual({ blockIndex: 1, contentIndex: 0, cursorPosition: 0 });
      });

      test("empty last item creates blank block after remaining list", () => {
        const state = letter(
          paragraph([
            itemList({
              items: [item(literal({ text: "item1" })), item(literal({ text: "item2" })), item(literal({ text: "" }))],
            }),
          ]),
        );

        const result = Actions.split(state, { blockIndex: 0, contentIndex: 0, itemIndex: 2, itemContentIndex: 0 }, 0);

        // [block: list(item1, item2)] [block: newLiteral()]
        expect(result.redigertBrev.blocks).toHaveLength(2);
        expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).items).toHaveLength(2);
        expect(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 })).toStrictEqual(newLiteral());
        expect(result.focus).toEqual({ blockIndex: 1, contentIndex: 0, cursorPosition: 0 });
      });

      test("only empty item removes list and creates blank block", () => {
        const state = letter(paragraph([itemList({ items: [item(literal({ text: "" }))] })]));

        const result = Actions.split(state, { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 }, 0);

        // [block: newLiteral()]
        expect(result.redigertBrev.blocks).toHaveLength(1);
        expect(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 })).toStrictEqual(newLiteral());
        expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 0 });
      });

      test("preserves listType and editedListType on split halves", () => {
        const state = letter(
          paragraph([
            itemList({
              items: [item(literal({ text: "a" })), item(literal({ text: "" })), item(literal({ text: "c" }))],
              listType: ListType.NUMMERERT_LISTE,
              editedListType: ListType.PUNKTLISTE,
            }),
          ]),
        );

        const result = Actions.split(state, { blockIndex: 0, contentIndex: 0, itemIndex: 1, itemContentIndex: 0 }, 0);

        const firstList = select<ItemList>(result, { blockIndex: 0, contentIndex: 0 });
        const secondList = select<ItemList>(result, { blockIndex: 2, contentIndex: 0 });
        expect(firstList.listType).toBe(ListType.NUMMERERT_LISTE);
        expect(firstList.editedListType).toBe(ListType.PUNKTLISTE);
        expect(secondList.listType).toBe(ListType.NUMMERERT_LISTE);
        expect(secondList.editedListType).toBe(ListType.PUNKTLISTE);
      });

      test("mixed block: content before list goes to its own block", () => {
        const state = letter(
          paragraph([
            literal({ text: "before" }),
            itemList({
              items: [item(literal({ text: "a" })), item(literal({ text: "" })), item(literal({ text: "c" }))],
            }),
          ]),
        );

        const result = Actions.split(state, { blockIndex: 0, contentIndex: 1, itemIndex: 1, itemContentIndex: 0 }, 0);

        // [block: "before"] [block: list(a)] [block: newLiteral()] [block: list(c)]
        expect(result.redigertBrev.blocks).toHaveLength(4);
        expect(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }).text).toBe("before");
        expect(select<ItemList>(result, { blockIndex: 1, contentIndex: 0 }).items).toHaveLength(1);
        expect(select<LiteralValue>(result, { blockIndex: 2, contentIndex: 0 })).toStrictEqual(newLiteral());
        expect(select<ItemList>(result, { blockIndex: 3, contentIndex: 0 }).items).toHaveLength(1);
      });

      test("another Enter on the blank line block is a no-op", () => {
        const state = letter(
          paragraph([itemList({ items: [item(literal({ text: "a" }))] })]),
          paragraph([literal({ text: "" })]),
          paragraph([itemList({ items: [item(literal({ text: "b" }))] })]),
        );

        // Enter on the empty block between the two lists
        const result = Actions.split(state, { blockIndex: 1, contentIndex: 0 }, 0);
        expect(result.redigertBrev.blocks).toHaveLength(3);
      });

      test("pre-existing deletedItems are propagated to both the before-list and the after-list", () => {
        const deletedId = 9001;
        const state = letter(
          paragraph([
            itemList({
              items: [item(literal({ text: "item1" })), item(literal({ text: "" })), item(literal({ text: "item3" }))],
              deletedItems: [deletedId],
            }),
          ]),
        );

        const result = Actions.split(state, { blockIndex: 0, contentIndex: 0, itemIndex: 1, itemContentIndex: 0 }, 0);

        // [block: list(item1)] [block: newLiteral()] [block: list(item3)]
        expect(result.redigertBrev.blocks).toHaveLength(3);
        const beforeList = select<ItemList>(result, { blockIndex: 0, contentIndex: 0 });
        const afterList = select<ItemList>(result, { blockIndex: 2, contentIndex: 0 });
        expect(beforeList.deletedItems).toContain(deletedId);
        expect(afterList.deletedItems).toContain(deletedId);
      });
    });
  });
});
