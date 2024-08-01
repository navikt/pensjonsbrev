import { describe, expect, test } from "vitest";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { MergeTarget } from "~/Brevredigering/LetterEditor/actions/merge";
import type { AnyBlock, Item, ItemList, LiteralValue, ParagraphBlock } from "~/types/brevbakerTypes";
import { LITERAL } from "~/types/brevbakerTypes";

import { asNew, item, itemList, letter, literal, paragraph, select, variable, withDeleted } from "../utils";

describe("LetterEditorActions.merge", () => {
  describe("at literal", () => {
    describe("next", () => {
      test("the specified blocks are merged and the number of blocks reduced", () => {
        const state = letter(paragraph(literal("p1")), paragraph(literal("p2")));
        const result = Actions.merge(state, { blockIndex: 0, contentIndex: 0 }, MergeTarget.NEXT);

        expect(result.redigertBrev.blocks).toHaveLength(state.redigertBrev.blocks.length - 1);
      });

      test("merge is ignored if the specified block is the last", () => {
        const state = letter(paragraph(literal("p1")), paragraph(literal("p2")));

        const result = Actions.merge(state, { blockIndex: 1, contentIndex: 0 }, MergeTarget.NEXT);
        expect(result.redigertBrev).toBe(state.redigertBrev);
      });

      test("the content of the next block is added to the end of the specified", () => {
        const state = letter(paragraph(variable("p1")), paragraph(literal("p2")));

        const mergeId = { blockIndex: 0 };
        const result = Actions.merge(state, { ...mergeId, contentIndex: 0 }, MergeTarget.NEXT);

        expect(select<ParagraphBlock>(result, mergeId).content).toEqual([
          ...select<ParagraphBlock>(state, mergeId).content,
          ...select<ParagraphBlock>(state, { blockIndex: mergeId.blockIndex + 1 }).content,
        ]);
      });

      test("adjoining literal content in merging blocks are joined", () => {
        const state = letter(
          paragraph(variable("var1"), literal("lit1")),
          paragraph(literal("lit2"), variable("var2")),
        );
        const mergeId = { blockIndex: 0 };

        const result = Actions.merge(state, { ...mergeId, contentIndex: 0 }, MergeTarget.NEXT);

        // assertions
        expect(select<ParagraphBlock>(result, mergeId).content).toHaveLength(3);
        const resultLiteral = select<LiteralValue>(result, { ...mergeId, contentIndex: 1 });
        expect(resultLiteral.text).toEqual("lit1");
        expect(resultLiteral.editedText).toEqual("lit1lit2");
      });

      test("id of specified block is kept if the next is empty", () => {
        const state = letter(paragraph(literal("p1")), paragraph());

        const mergeId = { blockIndex: 0 };
        const result = Actions.merge(state, { ...mergeId, contentIndex: 0 }, MergeTarget.NEXT);

        expect(select<AnyBlock>(result, mergeId).id).toEqual(select<AnyBlock>(state, mergeId).id);
      });

      test("id of next block is kept if the specified is empty", () => {
        const state = letter(paragraph(), paragraph(literal("p2")));

        const mergeId = { blockIndex: 0 };
        const result = Actions.merge(state, { ...mergeId, contentIndex: 0 }, MergeTarget.NEXT);

        expect(select<AnyBlock>(result, mergeId).id).toEqual(
          select<AnyBlock>(state, { blockIndex: mergeId.blockIndex + 1 }).id,
        );
      });

      test("updates deletedContent", () => {
        const movedContent = [literal("third"), variable("fourth")];
        const state = letter(
          withDeleted(
            paragraph(literal("first"), variable("second")),
            movedContent.map((c) => c.id!),
          ),
          asNew(paragraph(...movedContent)),
        );

        const result = Actions.merge(state, { blockIndex: 0, contentIndex: 1 }, MergeTarget.NEXT);
        expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toHaveLength(0);
      });
    });

    describe("previous", () => {
      test("the specified blocks are merged and the number of blocks reduced", () => {
        const state = letter(paragraph(literal("p1")), paragraph(variable("p2")));
        const result = Actions.merge(state, { blockIndex: 1, contentIndex: 0 }, MergeTarget.PREVIOUS);
        expect(result.redigertBrev.blocks).toHaveLength(state.redigertBrev.blocks.length - 1);
      });

      test("merge is ignored if the specified block is the first", () => {
        const state = letter(paragraph(literal("p1")), paragraph(variable("p2")));

        const result = Actions.merge(state, { blockIndex: 0, contentIndex: 0 }, MergeTarget.PREVIOUS);
        expect(result.redigertBrev).toBe(state.redigertBrev);
      });

      test("the content of the specified block is added to the end of the previous", () => {
        const state = letter(paragraph(variable("p1")), paragraph(literal("p2")));

        const result = Actions.merge(state, { blockIndex: 1, contentIndex: 0 }, MergeTarget.PREVIOUS);
        const resultBlock = select<ParagraphBlock>(result, { blockIndex: 0 });

        expect(resultBlock.content).toEqual([
          ...select<ParagraphBlock>(state, { blockIndex: 0 }).content,
          ...select<ParagraphBlock>(state, { blockIndex: 1 }).content,
        ]);
        expect(resultBlock.id).toStrictEqual(select<ParagraphBlock>(state, { blockIndex: 0 }).id);
      });

      test("adjoining literal content in merging blocks are joined", () => {
        const state = letter(
          paragraph(variable("var1"), literal("lit1")),
          paragraph(literal("lit2"), variable("var2")),
        );

        const result = Actions.merge(state, { blockIndex: 1, contentIndex: 0 }, MergeTarget.PREVIOUS);

        // assertions
        expect(select<ParagraphBlock>(result, { blockIndex: 0 }).content).toHaveLength(3);
        const resultLiteral = select<LiteralValue>(result, { blockIndex: 0, contentIndex: 1 });
        expect(resultLiteral.text).toEqual("lit1");
        expect(resultLiteral.editedText).toEqual("lit1lit2");
      });

      test("id of specified block is kept if the previous is empty", () => {
        const state = letter(paragraph(), paragraph(literal("p2")));
        const result = Actions.merge(state, { blockIndex: 1, contentIndex: 0 }, MergeTarget.PREVIOUS);
        expect(select<AnyBlock>(result, { blockIndex: 0 }).id).toEqual(select<AnyBlock>(state, { blockIndex: 1 }).id);
      });

      test("id of previous block is kept if the specified is empty", () => {
        const state = letter(paragraph(literal("p1")), paragraph());
        const result = Actions.merge(state, { blockIndex: 1, contentIndex: 0 }, MergeTarget.PREVIOUS);
        expect(select<AnyBlock>(result, { blockIndex: 0 }).id).toEqual(select<AnyBlock>(state, { blockIndex: 0 }).id);
      });

      test("updates deletedContent", () => {
        const movedContent = [literal("third"), variable("fourth")];
        const state = letter(
          withDeleted(
            paragraph(literal("first"), variable("second")),
            movedContent.map((c) => c.id!),
          ),
          asNew(paragraph(...movedContent)),
        );

        const result = Actions.merge(state, { blockIndex: 1, contentIndex: 0 }, MergeTarget.PREVIOUS);
        expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toHaveLength(0);
      });

      describe("Update focus", () => {
        test("when previous block is empty focus is stolen to beginning of the replaced block", () => {
          const state = letter(paragraph(), paragraph(literal("lit1"), variable("var1")));
          const result = Actions.merge(state, { blockIndex: 1, contentIndex: 0 }, MergeTarget.PREVIOUS);
          expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 0 });
        });

        test("when merging adjoining literals focus is stolen to the merge point of the two literals", () => {
          const state = letter(
            paragraph(variable("var1"), literal("lit1")),
            paragraph(literal("lit2"), variable("var2")),
          );

          const result = Actions.merge(state, { blockIndex: 1, contentIndex: 0 }, MergeTarget.PREVIOUS);
          expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 1, cursorPosition: "lit1".length });
        });

        test("when merging with non adjoining literals focus is stolen so that the cursor is at the beginning of the current content", () => {
          const state = letter(paragraph(variable("var1")), paragraph(literal("lit1"), variable("var2")));

          const result = Actions.merge(state, { blockIndex: 1, contentIndex: 0 }, MergeTarget.PREVIOUS);
          expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 1, cursorPosition: 0 });
        });

        test("when merging from empty block with previous focus is correctly set to the end of previous", () => {
          const state = letter(
            paragraph(literal("first"), variable("second"), literal("third")),
            paragraph(literal("")),
          );

          const result = Actions.merge(state, { blockIndex: 1, contentIndex: 0 }, MergeTarget.PREVIOUS);
          expect(select<ParagraphBlock>(result, { blockIndex: 0 }).content).toHaveLength(3);
          expect(result.focus).toStrictEqual({ blockIndex: 0, contentIndex: 2, cursorPosition: "third".length });
        });

        test("merging from empty block when last content of previous is variable keeps the empty literal", () => {
          const state = letter(paragraph(literal("first"), variable("second")), paragraph(literal("")));

          const result = Actions.merge(state, { blockIndex: 1, contentIndex: 0 }, MergeTarget.PREVIOUS);
          expect(result.redigertBrev.blocks).toHaveLength(1);
          expect(select<ParagraphBlock>(result, { blockIndex: 0 }).content).toHaveLength(3);
          expect(result.redigertBrev.blocks[0]?.content?.at(-1)?.type).toStrictEqual(LITERAL);
        });
      });

      describe("previous content in same block is itemList", () => {
        const mergeId = { blockIndex: 1, contentIndex: 1 };
        const withContentAfterList = letter(
          paragraph(literal("block 0")),
          paragraph(
            itemList(item(literal("Det blir "))),
            literal("content 1"),
            variable("variable 1"),
            itemList(item(literal("En annen liste"))),
          ),
        );
        const result = Actions.merge(withContentAfterList, mergeId, MergeTarget.PREVIOUS);

        test("does not merge with previous block", () => {
          expect(result.redigertBrev.blocks).toHaveLength(withContentAfterList.redigertBrev.blocks.length);
          expect(result.redigertBrev.blocks[mergeId.blockIndex - 1]).toBe(
            withContentAfterList.redigertBrev.blocks[mergeId.blockIndex - 1],
          );
        });

        test("textcontent should be added to the last item", () => {
          const itemList = select<ItemList>(result, {
            blockIndex: mergeId.blockIndex,
            contentIndex: mergeId.contentIndex - 1,
          });
          const mergedLiteral = itemList.items[0].content[0] as LiteralValue;
          expect(itemList.items).toHaveLength(1);
          expect(mergedLiteral.text).toEqual("Det blir ");
          expect(mergedLiteral.editedText).toEqual("Det blir content 1");
          expect(itemList.items[0].content[1]).toBe(
            select(withContentAfterList, { ...mergeId, contentIndex: mergeId.contentIndex + 1 }),
          );
        });

        test("should only merge textcontent into the last item", () => {
          expect(select(result, mergeId)).toBe(select(withContentAfterList, { ...mergeId, contentIndex: 3 }));
        });

        test("focus should be stolen to the end of the last item", () => {
          expect(result.focus).toEqual({
            blockIndex: mergeId.blockIndex,
            contentIndex: mergeId.contentIndex - 1,
            cursorPosition: "Det blir ".length,
            itemIndex: 0,
            itemContentIndex: 0,
          });
        });

        test("uses edited text to calculate offset", () => {
          const state = letter(paragraph(itemList(item(literal("item 1", ""))), literal("")));

          const result = Actions.merge(state, { blockIndex: 0, contentIndex: 1 }, MergeTarget.PREVIOUS);
          expect(result.focus).toStrictEqual({
            blockIndex: 0,
            contentIndex: 0,
            itemIndex: 0,
            itemContentIndex: 0,
            cursorPosition: 0,
          });
        });
      });
    });
  });

  describe("at itemList", () => {
    describe("with next", () => {
      test("the specified items are merged and the number of items reduced", () => {
        const state = letter(paragraph(itemList(item(literal("lit1")), item(literal("lit2")))));
        const result = Actions.merge(
          state,
          { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 },
          MergeTarget.NEXT,
        );
        expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).items).toHaveLength(1);
      });

      test("merge is ignored if the specified item is the last", () => {
        const state = letter(paragraph(itemList(item(literal("lit1")), item(literal("lit2")))));
        const result = Actions.merge(
          state,
          { blockIndex: 0, contentIndex: 0, itemIndex: 1, itemContentIndex: 0 },
          MergeTarget.NEXT,
        );
        expect(result.redigertBrev).toBe(state.redigertBrev);
      });

      test("the content of the next item is added to the end of the specified", () => {
        const state = letter(paragraph(itemList(item(variable("lit1")), item(literal("lit2")))));
        const mergeId = { blockIndex: 0, contentIndex: 0, itemIndex: 0 };

        const result = Actions.merge(state, mergeId, MergeTarget.NEXT);

        expect(select<Item>(result, mergeId).content).toEqual([
          ...select<Item>(state, mergeId).content,
          ...select<Item>(state, { ...mergeId, itemIndex: 1 }).content,
        ]);
      });

      describe("adjoining literals", () => {
        const state = letter(
          paragraph(itemList(item(variable("var1"), literal("lit1")), item(literal("lit2"), variable("var2")))),
        );
        const mergeId = { blockIndex: 0, contentIndex: 0, itemIndex: 0 };

        const result = Actions.merge(state, mergeId, MergeTarget.NEXT);

        test("the adjoining literals are merged and the rest of the content is concatenated", () => {
          expect(select<Item>(result, mergeId).content).toHaveLength(3);
          const mergedLiteral = select<LiteralValue>(result, { ...mergeId, itemContentIndex: 1 });
          expect(mergedLiteral.text).toEqual("lit1");
          expect(mergedLiteral.editedText).toEqual("lit1lit2");
        });

        test("focus is stolen to center of the merged literals", () => {
          expect(result.focus).toEqual({
            blockIndex: mergeId.blockIndex,
            contentIndex: mergeId.contentIndex,
            cursorPosition: "lit1".length,
            itemIndex: mergeId.itemIndex,
            itemContentIndex: 1,
          });
        });
      });

      describe("the next item is empty", () => {
        const state = letter(paragraph(itemList(item(literal("lit1")), item(literal("")))));
        const mergeId = { blockIndex: 0, contentIndex: 0, itemIndex: 0 };
        const result = Actions.merge(state, mergeId, MergeTarget.NEXT);

        test("then the item stays as is and the next is removed", () => {
          expect(select<Item>(result, mergeId)).toBe(select<Item>(state, mergeId));
          expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).items).toHaveLength(1);
        });

        test("then the focus is stolen to the end of currrent", () => {
          const item = select<Item>(state, mergeId);

          expect(result.focus).toEqual({
            blockIndex: mergeId.blockIndex,
            contentIndex: mergeId.contentIndex,
            cursorPosition: item.content.at(-1)?.text.length,
            itemIndex: mergeId.itemIndex,
            itemContentIndex: item.content.length - 1,
          });
        });
      });

      describe("the current item is empty", () => {
        const state = letter(paragraph(itemList(item(literal("")), item(literal("lit1")))));
        const mergeId = { blockIndex: 0, contentIndex: 0, itemIndex: 0 };
        const result = Actions.merge(state, mergeId, MergeTarget.NEXT);

        test("then the current item is replaced with the next", () => {
          expect(select<Item>(result, mergeId)).toBe(
            select<Item>(state, { ...mergeId, itemIndex: mergeId.itemIndex + 1 }),
          );
          expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).items).toHaveLength(1);
        });

        test("then the focus is stolen to the beginning", () => {
          expect(result.focus).toEqual({
            blockIndex: mergeId.blockIndex,
            contentIndex: mergeId.contentIndex,
            cursorPosition: 0,
            itemIndex: mergeId.itemIndex,
            itemContentIndex: 0,
          });
        });
      });
    });

    describe("with previous", () => {
      test("the specified items are merged, the number of items is reduced and focus is stolen", () => {
        const state = letter(paragraph(itemList(item(literal("lit1")), item(literal("lit2")))));
        const mergeId = { blockIndex: 0, contentIndex: 0, itemIndex: 1 };
        const result = Actions.merge(state, mergeId, MergeTarget.PREVIOUS);

        expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).items).toHaveLength(1);
      });

      test("merge is ignored if the specified item is the first", () => {
        const state = letter(paragraph(itemList(item(literal("lit1")), item(literal("lit2")))));
        const result = Actions.merge(state, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }, MergeTarget.PREVIOUS);
        expect(result).toBe(result);
      });

      test("the content of the specified item is added to the end of the previous", () => {
        const state = letter(paragraph(itemList(item(variable("var1")), item(literal("lit1")))));
        const mergeId = { blockIndex: 0, contentIndex: 0, itemIndex: 1 };
        const result = Actions.merge(state, mergeId, MergeTarget.PREVIOUS);
        expect(select<Item>(result, { ...mergeId, itemIndex: mergeId.itemIndex - 1 }).content).toEqual([
          ...select<Item>(state, { ...mergeId, itemIndex: mergeId.itemIndex - 1 }).content,
          ...select<Item>(state, mergeId).content,
        ]);
      });

      test("adjoining literal content in merging items are joined", () => {
        const state = letter(
          paragraph(itemList(item(variable("var1"), literal("lit1")), item(literal("lit2"), variable("var2")))),
        );
        const mergeId = { blockIndex: 0, contentIndex: 0, itemIndex: 1 };

        const result = Actions.merge(state, mergeId, MergeTarget.PREVIOUS);

        const mergedId = { ...mergeId, itemIndex: mergeId.itemIndex - 1 };
        expect(select<Item>(result, mergedId).content).toHaveLength(3);
        const mergedLiteral = select<LiteralValue>(result, { ...mergedId, itemContentIndex: 1 });
        expect(mergedLiteral.text).toEqual("lit1");
        expect(mergedLiteral.editedText).toEqual("lit1lit2");
      });

      describe("the previous item is empty", () => {
        const state = letter(paragraph(itemList(item(literal("")), item(literal("lit1")))));
        const mergeId = { blockIndex: 0, contentIndex: 0, itemIndex: 1 };
        const result = Actions.merge(state, mergeId, MergeTarget.PREVIOUS);

        test("then the previous is removed", () => {
          expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).items).toHaveLength(1);
          expect(select<Item>(result, { ...mergeId, itemIndex: mergeId.itemIndex - 1 })).toBe(
            select<Item>(state, mergeId),
          );
        });

        test("then the focus is stolen to the beginning", () => {
          expect(result.focus).toEqual({
            blockIndex: mergeId.blockIndex,
            contentIndex: mergeId.contentIndex,
            cursorPosition: 0,
            itemIndex: mergeId.itemIndex - 1,
            itemContentIndex: 0,
          });
        });
      });

      describe("the current item is empty", () => {
        const state = letter(paragraph(itemList(item(literal("lit")), item(literal("")))));
        const mergeId = { blockIndex: 0, contentIndex: 0, itemIndex: 1 };
        const result = Actions.merge(state, mergeId, MergeTarget.PREVIOUS);

        test("then the current is removed", () => {
          expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).items).toHaveLength(1);
          expect(select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0 })).toBe(
            select<Item>(state, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }),
          );
          expect(select<Item>(result, mergeId)).toBeUndefined();
        });

        test("then the focus is stolen to the end", () => {
          const previousItem = select<Item>(state, { ...mergeId, itemIndex: mergeId.itemIndex - 1 });
          expect(result.focus).toEqual({
            blockIndex: mergeId.blockIndex,
            contentIndex: mergeId.contentIndex,
            cursorPosition: previousItem.content.at(-1)?.text.length,
            itemIndex: mergeId.itemIndex - 1,
            itemContentIndex: previousItem.content.length - 1,
          });
        });

        test("and it is the only one then the itemlist should be deleted", () => {
          const state = letter(paragraph(literal("before list"), itemList(item(literal("")))));

          const result = Actions.merge(
            state,
            { blockIndex: 0, contentIndex: 1, itemIndex: 0, itemContentIndex: 0 },
            MergeTarget.PREVIOUS,
          );

          expect(select<ParagraphBlock>(result, { blockIndex: 0 }).content).toHaveLength(1);
          expect(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }).text).toStrictEqual("before list");
          expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toContain(
            select<ItemList>(state, { blockIndex: 0, contentIndex: 1 }).id,
          );
          expect(result.focus).toStrictEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: "before list".length });
        });
      });
    });
  });
});
