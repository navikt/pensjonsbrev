import { describe, expect, test } from "vitest";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { isEmptyContent } from "~/Brevredigering/LetterEditor/model/utils";
import type { Content, Item, ItemList, LiteralValue, ParagraphBlock, TextContent } from "~/types/brevbakerTypes";
import { LITERAL } from "~/types/brevbakerTypes";

import { asNew, item, itemList, letter, literal, paragraph, select, variable } from "../utils";

describe("LetterEditorActions.split", () => {
  describe("at literal", () => {
    test("specified block is split at contentIndex and offset", () => {
      const state = letter(paragraph(variable("var1"), literal({ text: "lit1" }), variable("var2")));
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
      const state = letter(paragraph(literal({ text: "lit1" })));
      const result = Actions.split(state, { blockIndex: 0, contentIndex: 0 }, 2);

      expect(result.redigertBrev.blocks).not.toBe(state.redigertBrev.blocks);
    });

    test("when the offset is at the end of the current content, the new block will have one content element with an empty string", () => {
      const state = letter(paragraph(literal({ text: "lit1" })));
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
      const state = letter(paragraph(literal({ text: "" })));
      const result = Actions.split(state, { blockIndex: 0, contentIndex: 0 }, 0);
      expect(result.redigertBrev).toBe(state.redigertBrev);
    });

    test("does not split at the very beginning of block when previous is empty", () => {
      const state = letter(paragraph(literal({ text: "" })), paragraph(literal({ text: "lit2" })));
      const result = Actions.split(state, { blockIndex: 1, contentIndex: 0 }, 0);
      expect(result.redigertBrev).toBe(state.redigertBrev);
    });

    test("when splitting at the very end of a block the new block and content gets no ID (new)", () => {
      const state = letter(paragraph(variable("var1"), variable("var2"), literal({ text: "lit1" })));
      const result = Actions.split(state, { blockIndex: 0, contentIndex: 2 }, 4);

      expect(result.redigertBrev.blocks).toHaveLength(2);
      const newContent = select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 });
      expect(select<ParagraphBlock>(result, { blockIndex: 1 }).id).toBeNull();
      expect(newContent.id).toBeNull();
      expect(newContent.text).toEqual("");
    });

    test("when splitting in the last content of a block the new block and content gets no ID (new)", () => {
      const state = letter(paragraph(variable("var1"), variable("var2"), literal({ text: "lit1" })));
      const result = Actions.split(state, { blockIndex: 0, contentIndex: 2 }, 2);

      expect(result.redigertBrev.blocks).toHaveLength(2);
      expect(select<ParagraphBlock>(result, { blockIndex: 1 }).id).toBeNull();
      expect(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }).id).toBeNull();
    });

    test("when splitting at the very beginning of the last content of a block the content retains id in new block", () => {
      const state = letter(paragraph(variable("var1"), variable("var2"), literal({ text: "lit1" })));
      const result = Actions.split(state, { blockIndex: 0, contentIndex: 2 }, 0);

      expect(result.redigertBrev.blocks).toHaveLength(2);
      const newContent = select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 });
      expect(select<ParagraphBlock>(result, { blockIndex: 1 }).id).toBeNull();
      expect(newContent.id).toEqual(select<TextContent>(state, { blockIndex: 0, contentIndex: 2 }).id);
      expect(select<ParagraphBlock>(result, { blockIndex: 0 }).content).toHaveLength(3);
    });

    test("when splitting at the end of a literal there are no changes to editedText", () => {
      const state = letter(paragraph(literal({ text: "lit1" })));
      const result = Actions.split(state, { blockIndex: 0, contentIndex: 0 }, "lit1".length);

      expect(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }).editedText).toBeNull();
    });

    test("splitting uses editedText if present", () => {
      const state = letter(paragraph(literal({ text: "lit1", editedText: "new text" })));
      const result = Actions.split(state, { blockIndex: 0, contentIndex: 0 }, 4);

      expect(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }).editedText).toEqual("new ");
      expect(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 }).editedText).toEqual("text");
    });

    test("splitting at the end of a content not last in block will not result in a block with an empty content as first element", () => {
      const state = letter(
        paragraph(literal({ text: "heisann" }), itemList({ items: [item(literal({ text: "item 1" }))] })),
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
      const state = letter(paragraph(literal({ text: "before split" }), literal({ text: "after split" })));

      const result = Actions.split(state, { blockIndex: 0, contentIndex: 0 }, "before split".length);

      expect(result.redigertBrev.blocks).toHaveLength(2);
      expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toEqual([
        select<LiteralValue>(state, { blockIndex: 0, contentIndex: 1 }).id,
      ]);
    });

    test("can split a block at offset 0 when not at the first content", () => {
      const state = letter(
        paragraph(literal({ text: "" })),
        paragraph(variable("before split"), literal({ text: "after split" })),
      );

      const result = Actions.split(state, { blockIndex: 1, contentIndex: 1 }, 0);

      expect(result.redigertBrev.blocks).toHaveLength(3);
    });

    test("when splitting at the very beginning of a block then an new block should be inserted at cursor", () => {
      const state = letter(paragraph(literal({ text: "begynnelse av block" }), variable("slutten")));

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
        paragraph(literal({ text: "ikke tom" }), itemList({ items: [item(literal({ text: "ikke tom" }))] })),
        paragraph(literal({ text: "" })),
      );

      const result = Actions.split(state, { blockIndex: 1, contentIndex: 0 }, 0);

      expect(result.redigertBrev.blocks).toHaveLength(2);
    });

    test("when splitting from beginning of block then focus shifts with the block", () => {
      const state = letter(paragraph(literal({ text: "ikke tom" })));

      const result = Actions.split(state, { blockIndex: 0, contentIndex: 0 }, 0);

      expect(result.redigertBrev.blocks).toHaveLength(2);
      expect(result.focus).toStrictEqual({ blockIndex: 1, contentIndex: 0, cursorPosition: 0 });
    });
  });

  describe("at itemList", () => {
    test("specified item is split at contentIndex and offset", () => {
      const state = letter(
        paragraph(itemList({ items: [item(variable("var1"), literal({ text: "lit1" }), variable("var2"))] })),
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
      const state = letter(paragraph(itemList({ items: [item(variable("var1"), literal({ text: "lit1" }))] })));
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
        paragraph(
          itemList({
            items: [
              item(literal({ text: "p1" })),
              asNew(item(asNew(literal({ text: "" })), literal({ text: "p2" }))),
              item(literal({ text: "p3" })),
            ],
          }),
        ),
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
        paragraph(
          itemList({
            items: [item(literal({ text: "item1" })), item(literal({ text: "" })), item(literal({ text: "item3" }))],
          }),
        ),
      );

      // The following assertions also assert that we don't move focus
      // Split at the empty item
      expect(
        Actions.split(state, { blockIndex: 0, contentIndex: 0, itemIndex: 1, itemContentIndex: 0 }, 0).redigertBrev,
      ).toStrictEqual(state.redigertBrev);
      // Split before the empty item
      expect(
        Actions.split(state, { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 }, "item1".length)
          .redigertBrev,
      ).toStrictEqual(state.redigertBrev);
      // Split at beginning of item after empty item
      expect(
        Actions.split(state, { blockIndex: 0, contentIndex: 0, itemIndex: 2, itemContentIndex: 0 }, 0).redigertBrev,
      ).toStrictEqual(state.redigertBrev);
    });

    test("splits the last empty item as a Literal after the list in the parent block", () => {
      const state = letter(
        paragraph(itemList({ items: [item(literal({ text: "lit1" })), item(literal({ text: "" }))] })),
      );

      // Split at the empty item
      const withLiteralAfterList = Actions.split(
        state,
        { blockIndex: 0, contentIndex: 0, itemIndex: 1, itemContentIndex: 0 },
        0,
      );
      expect(select<ItemList>(withLiteralAfterList, { blockIndex: 0, contentIndex: 0 }).items).toHaveLength(1);
      expect(select<ParagraphBlock>(withLiteralAfterList, { blockIndex: 0 }).content).toHaveLength(2);
      expect(select<Content>(withLiteralAfterList, { blockIndex: 0, contentIndex: 1 }).type).toEqual(LITERAL);
      expect(select<Content>(withLiteralAfterList, { blockIndex: 0, contentIndex: 1 }).id).toBeNull();

      expect(withLiteralAfterList.focus).toEqual({ blockIndex: 0, contentIndex: 1, cursorPosition: 0 });
    });

    test("when splitting the last empty item and parent block already has a subsequent literal then no new literal will be added", () => {
      const state = letter(
        paragraph(
          itemList({ items: [item(literal({ text: "lit1" })), item(literal({ text: "" }))] }),
          literal({ text: "" }),
        ),
      );

      const result = Actions.split(state, { blockIndex: 0, contentIndex: 0, itemIndex: 1, itemContentIndex: 0 }, 0);

      expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).items).toHaveLength(1);
      expect(select<ParagraphBlock>(result, { blockIndex: 0 }).content).toHaveLength(2);
    });

    test("splitting from the last (empty) item in itemlist results in content in block and another split results in new block", () => {
      const state = letter(
        paragraph(itemList({ items: [item(literal({ text: "item1" })), item(literal({ text: "" }))] })),
      );

      const splitFromEmptyItem = Actions.split(
        state,
        { blockIndex: 0, contentIndex: 0, itemIndex: 1, itemContentIndex: 0 },
        0,
      );
      expect(splitFromEmptyItem.redigertBrev.blocks).toHaveLength(1);
      expect(select<ParagraphBlock>(splitFromEmptyItem, { blockIndex: 0 }).content).toHaveLength(2);
      expect(select<ItemList>(splitFromEmptyItem, { blockIndex: 0, contentIndex: 0 }).items).toHaveLength(1);
      expect(select<LiteralValue>(splitFromEmptyItem, { blockIndex: 0, contentIndex: 1 })).toStrictEqual({
        id: null,
        parentId: null,
        type: LITERAL,
        text: "",
        editedText: "",
        tags: [],
      });

      const splitFromEmptyLastContent = Actions.split(splitFromEmptyItem, { blockIndex: 0, contentIndex: 1 }, 0);
      expect(splitFromEmptyLastContent.redigertBrev.blocks).toHaveLength(2);
      expect(select<ParagraphBlock>(splitFromEmptyLastContent, { blockIndex: 0 }).content).toHaveLength(1);
      expect(select<ParagraphBlock>(splitFromEmptyLastContent, { blockIndex: 1 }).content).toHaveLength(1);
      expect(select<LiteralValue>(splitFromEmptyLastContent, { blockIndex: 1, contentIndex: 0 })).toStrictEqual({
        id: null,
        parentId: null,
        type: LITERAL,
        text: "",
        editedText: "",
        tags: [],
      });
    });

    test("splitting from last new empty item in itemlist results in in content after itemlist", () => {
      const state = letter(
        paragraph(
          itemList({
            items: [
              item(literal({ text: "item1" })),
              asNew(item(literal({ text: "aa" }))),
              asNew(item(literal({ text: "​" }))),
            ],
          }),
        ),
      );

      const result = Actions.split(state, { blockIndex: 0, contentIndex: 0, itemIndex: 2, itemContentIndex: 0 }, 0);
      expect(select<ParagraphBlock>(result, { blockIndex: 0 }).content).toHaveLength(2);
    });

    test("splitting at the beginning an item keeps id", () => {
      const item1 = item(literal({ text: "hei" }), variable("joda"));
      const state = letter(paragraph(itemList({ items: [item1] })));

      const result = Actions.split(state, { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 }, 0);
      expect(select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 }).id).toBeNull();
      expect(select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 1 }).id).toStrictEqual(item1.id);
    });

    test("merging");
  });
});
