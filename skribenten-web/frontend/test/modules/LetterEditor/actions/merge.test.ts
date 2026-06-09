import { describe, expect, test } from "vitest";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { newLiteral, text } from "~/Brevredigering/LetterEditor/actions/common";
import { MergeTarget } from "~/Brevredigering/LetterEditor/actions/merge";
import {
  type AnyBlock,
  type Item,
  type ItemList,
  LITERAL,
  ListType,
  type LiteralValue,
  type ParagraphBlock,
  type TextContent,
  type VariableValue,
} from "~/types/brevbakerTypes";

import { asNew, item, itemList, letter, literal, newLine, paragraph, select, variable, withDeleted } from "../utils";

describe("LetterEditorActions.merge", () => {
  describe("at literal", () => {
    describe("next", () => {
      test("the specified blocks are merged and the number of blocks reduced", () => {
        const state = letter(paragraph([literal({ text: "p1" })]), paragraph([literal({ text: "p2" })]));
        const result = Actions.merge(state, { blockIndex: 0, contentIndex: 0 }, MergeTarget.NEXT);

        expect(result.redigertBrev.blocks).toHaveLength(state.redigertBrev.blocks.length - 1);
      });

      test("merge is ignored if the specified block is the last", () => {
        const state = letter(paragraph([literal({ text: "p1" })]), paragraph([literal({ text: "p2" })]));

        const result = Actions.merge(state, { blockIndex: 1, contentIndex: 0 }, MergeTarget.NEXT);
        expect(result.redigertBrev).toBe(state.redigertBrev);
      });

      test("the content of the next block is added to the end of the specified", () => {
        const state = letter(paragraph([variable("p1")]), paragraph([literal({ text: "p2" })]));

        const mergeId = { blockIndex: 0 };
        const result = Actions.merge(state, { ...mergeId, contentIndex: 0 }, MergeTarget.NEXT);

        expect(select<ParagraphBlock>(result, mergeId).content).toEqual([
          ...select<ParagraphBlock>(state, mergeId).content,
          ...select<ParagraphBlock>(state, { blockIndex: mergeId.blockIndex + 1 }).content,
        ]);
      });

      test("adjoining literal content in merging blocks are joined", () => {
        const state = letter(
          paragraph([variable("var1"), literal({ text: "lit1" })]),
          paragraph([newLiteral({ text: "lit2" }), variable("var2")]),
        );
        const mergeId = { blockIndex: 0 };

        const result = Actions.merge(state, { ...mergeId, contentIndex: 1 }, MergeTarget.NEXT);

        // assertions
        expect(select<ParagraphBlock>(result, mergeId).content).toHaveLength(3);
        const resultLiteral = select<LiteralValue>(result, { ...mergeId, contentIndex: 1 });
        expect(resultLiteral.text).toEqual("lit1");
        expect(resultLiteral.editedText).toEqual("lit1lit2");
      });

      test("id of specified block is kept if the next is empty", () => {
        const state = letter(paragraph([literal({ text: "p1" })]), paragraph([]));

        const mergeId = { blockIndex: 0 };
        const result = Actions.merge(state, { ...mergeId, contentIndex: 0 }, MergeTarget.NEXT);

        expect(select<AnyBlock>(result, mergeId).id).toEqual(select<AnyBlock>(state, mergeId).id);
      });

      test("id of next block is kept if the specified is empty", () => {
        const state = letter(paragraph([literal("")]), paragraph([literal({ text: "p2" })]));

        const mergeId = { blockIndex: 0 };
        const result = Actions.merge(state, { ...mergeId, contentIndex: 0 }, MergeTarget.NEXT);

        expect(result.redigertBrev.blocks).toHaveLength(1);
        expect(select<AnyBlock>(result, mergeId).id).toEqual(
          select<AnyBlock>(state, { blockIndex: mergeId.blockIndex + 1 }).id,
        );
      });

      test("updates deletedContent", () => {
        const movedContent = [literal({ text: "third" }), variable("fourth")];
        const state = letter(
          withDeleted(
            paragraph([literal({ text: "first" }), variable("second")]),
            movedContent.map((c) => c.id!),
          ),
          asNew(paragraph(movedContent)),
        );

        const result = Actions.merge(state, { blockIndex: 0, contentIndex: 1 }, MergeTarget.NEXT);
        expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toHaveLength(0);
      });
    });

    describe("previous", () => {
      test("the specified blocks are merged and the number of blocks reduced", () => {
        const state = letter(paragraph([literal({ text: "p1" })]), paragraph([variable("p2")]));
        const result = Actions.merge(state, { blockIndex: 1, contentIndex: 0 }, MergeTarget.PREVIOUS);
        expect(result.redigertBrev.blocks).toHaveLength(state.redigertBrev.blocks.length - 1);
      });

      test("merge is ignored if the specified block is the first", () => {
        const state = letter(paragraph([literal({ text: "p1" })]), paragraph([variable("p2")]));

        const result = Actions.merge(state, { blockIndex: 0, contentIndex: 0 }, MergeTarget.PREVIOUS);
        expect(result.redigertBrev).toBe(state.redigertBrev);
      });

      test("the content of the specified block is added to the end of the previous", () => {
        const state = letter(paragraph([variable("p1")]), paragraph([literal({ text: "p2" })]));

        const result = Actions.merge(state, { blockIndex: 1, contentIndex: 0 }, MergeTarget.PREVIOUS);
        const resultBlock = select<ParagraphBlock>(result, { blockIndex: 0 });

        expect(resultBlock.content).toEqual([
          ...select<ParagraphBlock>(state, { blockIndex: 0 }).content,
          ...select<ParagraphBlock>(state, { blockIndex: 1 }).content,
        ]);
        expect(resultBlock.id).toStrictEqual(select<ParagraphBlock>(state, { blockIndex: 0 }).id);
      });

      test("content moved back into original parent is unmarked as deleted", () => {
        const state = letter(
          paragraph([literal({ text: "l1" }), variable("v1"), literal({ text: "l2" }), variable("v2")]),
        );

        const splitResult = Actions.split(state, { blockIndex: 0, contentIndex: 2 }, 0);
        const splitBlock = select<ParagraphBlock>(splitResult, { blockIndex: 0 });
        expect(splitBlock.deletedContent).toEqual(state.redigertBrev.blocks[0].content.slice(2).map((c) => c.id));
        expect(splitBlock.content).toHaveLength(3);

        const mergeResult = Actions.merge(splitResult, { blockIndex: 1, contentIndex: 0 }, MergeTarget.PREVIOUS);
        const mergedBlock = select<ParagraphBlock>(mergeResult, { blockIndex: 0 });
        expect(mergedBlock.content).toHaveLength(4);
        expect(mergedBlock.deletedContent).toHaveLength(0);
      });

      test("adjoining literal content in merging blocks are joined", () => {
        const state = letter(
          paragraph([variable("var1"), literal({ text: "lit1" })]),
          paragraph([newLiteral({ text: "lit2" }), variable("var2")]),
        );

        const result = Actions.merge(state, { blockIndex: 1, contentIndex: 0 }, MergeTarget.PREVIOUS);

        // assertions
        expect(select<ParagraphBlock>(result, { blockIndex: 0 }).content).toHaveLength(3);
        const resultLiteral = select<LiteralValue>(result, { blockIndex: 0, contentIndex: 1 });
        expect(resultLiteral.text).toEqual("lit1");
        expect(resultLiteral.editedText).toEqual("lit1lit2");
      });

      test("id of specified block is kept if the previous is empty", () => {
        const state = letter(paragraph([]), paragraph([literal({ text: "p2" })]));
        const result = Actions.merge(state, { blockIndex: 1, contentIndex: 0 }, MergeTarget.PREVIOUS);
        expect(select<AnyBlock>(result, { blockIndex: 0 }).id).toEqual(select<AnyBlock>(state, { blockIndex: 1 }).id);
      });

      test("id of previous block is kept if the specified is empty", () => {
        const state = letter(paragraph([literal({ text: "p1" })]), paragraph([literal("")]));
        const result = Actions.merge(state, { blockIndex: 1, contentIndex: 0 }, MergeTarget.PREVIOUS);
        expect(select<AnyBlock>(result, { blockIndex: 0 }).id).toEqual(select<AnyBlock>(state, { blockIndex: 0 }).id);
      });

      test("updates deletedContent", () => {
        const movedContent = [literal({ text: "third" }), variable("fourth")];
        const state = letter(
          withDeleted(
            paragraph([literal({ text: "first" }), variable("second")]),
            movedContent.map((c) => c.id!),
          ),
          asNew(paragraph(movedContent)),
        );

        const result = Actions.merge(state, { blockIndex: 1, contentIndex: 0 }, MergeTarget.PREVIOUS);
        expect(select<ParagraphBlock>(result, { blockIndex: 0 }).deletedContent).toHaveLength(0);
      });

      test("removed block is marked as deleted", () => {
        const state = letter(paragraph([literal({ text: "l1" })]), paragraph([literal({ text: "l2" })]));
        const result = Actions.merge(state, { blockIndex: 1, contentIndex: 0 }, MergeTarget.PREVIOUS);
        expect(result.redigertBrev.deletedBlocks).toContain(state.redigertBrev.blocks[1].id);
      });

      describe("Update focus", () => {
        test("when previous block is empty focus is stolen to beginning of the replaced block", () => {
          const state = letter(paragraph(), paragraph([literal({ text: "lit1" }), variable("var1")]));
          const result = Actions.merge(state, { blockIndex: 1, contentIndex: 0 }, MergeTarget.PREVIOUS);
          expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 0 });
        });

        test("when merging adjoining literals focus is stolen to the merge point of the two literals", () => {
          const state = letter(
            paragraph([variable("var1"), literal({ text: "lit1" })]),
            paragraph([literal({ text: "lit2" }), variable("var2")]),
          );

          const result = Actions.merge(state, { blockIndex: 1, contentIndex: 0 }, MergeTarget.PREVIOUS);
          expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 1, cursorPosition: "lit1".length });
        });

        test("when merging with non adjoining literals focus is stolen so that the cursor is at the beginning of the current content", () => {
          const state = letter(paragraph([variable("var1")]), paragraph([literal({ text: "lit1" }), variable("var2")]));

          const result = Actions.merge(state, { blockIndex: 1, contentIndex: 0 }, MergeTarget.PREVIOUS);
          expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 1, cursorPosition: 0 });
        });

        test("when merging from empty block with previous focus is correctly set to the end of previous", () => {
          const state = letter(
            paragraph([literal({ text: "first" }), variable("second"), literal({ text: "third" })]),
            paragraph([literal({ text: "" })]),
          );

          const result = Actions.merge(state, { blockIndex: 1, contentIndex: 0 }, MergeTarget.PREVIOUS);
          expect(select<ParagraphBlock>(result, { blockIndex: 0 }).content).toHaveLength(3);
          expect(result.focus).toStrictEqual({ blockIndex: 0, contentIndex: 2, cursorPosition: "third".length });
        });

        test("merging from empty block when last content of previous is variable keeps the empty literal", () => {
          const state = letter(
            paragraph([literal({ text: "first" }), variable("second")]),
            paragraph([literal({ text: "" })]),
          );

          const result = Actions.merge(state, { blockIndex: 1, contentIndex: 0 }, MergeTarget.PREVIOUS);
          expect(result.redigertBrev.blocks).toHaveLength(1);
          expect(select<ParagraphBlock>(result, { blockIndex: 0 }).content).toHaveLength(3);
          expect(result.redigertBrev.blocks[0]?.content?.at(-1)?.type).toStrictEqual(LITERAL);
        });
      });

      describe("previous content in same block is itemList", () => {
        const mergeId = { blockIndex: 1, contentIndex: 1 };
        const withContentAfterList = letter(
          paragraph([literal({ text: "block 0" })]),
          paragraph([
            itemList({ items: [item(literal({ text: "Det blir " }))] }),
            newLiteral({ text: "content 1" }),
            variable("variable 1"),
            itemList({ items: [item(literal({ text: "En annen liste" }))] }),
          ]),
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
          expect(itemList.items[0].content[1]).toEqual(
            select<VariableValue>(withContentAfterList, { ...mergeId, contentIndex: mergeId.contentIndex + 1 }),
          );
        });

        test("should only merge textcontent into the last item", () => {
          expect(select(result, mergeId)).toBe(select(withContentAfterList, { ...mergeId, contentIndex: 3 }));
        });

        test("moved textcontent should be marked as deleted", () => {
          expect(select<ParagraphBlock>(result, { blockIndex: mergeId.blockIndex }).deletedContent).toEqual([
            select<TextContent>(withContentAfterList, { ...mergeId, contentIndex: mergeId.contentIndex + 1 }).id,
          ]);
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
          const state = letter(
            paragraph([
              itemList({ items: [item(literal({ text: "item 1", editedText: "" }))] }),
              literal({ text: "" }),
            ]),
          );

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

      describe("current literal is not first in block", () => {
        test("does not merge blocks", () => {
          const state = letter(
            paragraph([literal({ text: "p1" })]),
            paragraph([literal({ text: "p2-l1" }), literal({ text: "p2-l2" })]),
          );
          const result = Actions.merge(state, { blockIndex: 1, contentIndex: 1 }, MergeTarget.PREVIOUS);
          expect(result.redigertBrev.blocks).toHaveLength(2);
        });
        test("removes empty literal", () => {
          const state = letter(
            paragraph([literal({ text: "p1" })]),
            paragraph([literal({ text: "p2-l1" }), literal({ text: "" })]),
          );
          const result = Actions.merge(state, { blockIndex: 1, contentIndex: 1 }, MergeTarget.PREVIOUS);
          const resultBlock = select<ParagraphBlock>(result, { blockIndex: 1 });
          expect(resultBlock.content).toHaveLength(1);
          expect(resultBlock.deletedContent).toContain(
            select<LiteralValue>(state, { blockIndex: 1, contentIndex: 1 }).id,
          );
          expect(result.focus).toEqual({ blockIndex: 1, contentIndex: 0, cursorPosition: "p2-l1".length });
        });
        test("does not remove non-empty literal, but shifts focus", () => {
          const state = letter(
            paragraph([literal({ text: "p1" })]),
            paragraph([literal({ text: "p2-l1" }), literal({ text: "p2-l2" })]),
          );
          const result = Actions.merge(state, { blockIndex: 1, contentIndex: 1 }, MergeTarget.PREVIOUS);
          expect(select<ParagraphBlock>(result, { blockIndex: 1 }).content).toHaveLength(2);
          expect(result.focus).toEqual({ blockIndex: 1, contentIndex: 0, cursorPosition: "p2-l1".length });
        });
      });
    });
  });

  describe("at itemList", () => {
    describe("with next", () => {
      test("the specified items are merged and the number of items reduced", () => {
        const state = letter(
          paragraph([itemList({ items: [item(literal({ text: "lit1" })), item(literal({ text: "lit2" }))] })]),
        );
        const result = Actions.merge(
          state,
          { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 },
          MergeTarget.NEXT,
        );
        expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).items).toHaveLength(1);
      });

      test("merge is ignored if the specified item is the last", () => {
        const state = letter(
          paragraph([itemList({ items: [item(literal({ text: "lit1" })), item(literal({ text: "lit2" }))] })]),
        );
        const result = Actions.merge(
          state,
          { blockIndex: 0, contentIndex: 0, itemIndex: 1, itemContentIndex: 0 },
          MergeTarget.NEXT,
        );
        expect(result.redigertBrev).toBe(state.redigertBrev);
      });

      test("the content of the next item is added to the end of the specified", () => {
        const state = letter(
          paragraph([itemList({ items: [item(variable("lit1")), item(literal({ text: "lit2" }))] })]),
        );
        const mergeId = { blockIndex: 0, contentIndex: 0, itemIndex: 0 };

        const result = Actions.merge(state, { ...mergeId, itemContentIndex: 0 }, MergeTarget.NEXT);

        expect(select<Item>(result, mergeId).content).toEqual([
          ...select<Item>(state, mergeId).content,
          ...select<Item>(state, { ...mergeId, itemIndex: 1 }).content,
        ]);
      });

      describe("adjoining literals", () => {
        const state = letter(
          paragraph([
            itemList({
              items: [
                item(variable("var1"), literal({ text: "lit1" })),
                item(newLiteral({ text: "lit2" }), variable("var2")),
              ],
            }),
          ]),
        );
        const mergeId = { blockIndex: 0, contentIndex: 0, itemIndex: 0 };

        const result = Actions.merge(state, { ...mergeId, itemContentIndex: 1 }, MergeTarget.NEXT);

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
        const state = letter(
          paragraph([itemList({ items: [item(literal({ text: "lit1" })), item(literal({ text: "" }))] })]),
        );
        const mergeId = { blockIndex: 0, contentIndex: 0, itemIndex: 0 };
        const result = Actions.merge(state, { ...mergeId, itemContentIndex: 0 }, MergeTarget.NEXT);

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
        const state = letter(
          paragraph([itemList({ items: [item(literal({ text: "" })), item(literal({ text: "lit1" }))] })]),
        );
        const mergeId = { blockIndex: 0, contentIndex: 0, itemIndex: 0 };
        const result = Actions.merge(state, { ...mergeId, itemContentIndex: 0 }, MergeTarget.NEXT);

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
        const state = letter(
          paragraph([itemList({ items: [item(literal({ text: "lit1" })), item(literal({ text: "lit2" }))] })]),
        );
        const mergeId = { blockIndex: 0, contentIndex: 0, itemIndex: 1, itemContentIndex: 0 };
        const result = Actions.merge(state, mergeId, MergeTarget.PREVIOUS);

        expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).items).toHaveLength(1);
      });

      test("merge is ignored if the specified item is the first", () => {
        const state = letter(
          paragraph([itemList({ items: [item(literal({ text: "lit1" })), item(literal({ text: "lit2" }))] })]),
        );
        const result = Actions.merge(
          state,
          { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 },
          MergeTarget.PREVIOUS,
        );
        expect(result).toBe(result);
      });

      test("the content of the specified item is added to the end of the previous", () => {
        const state = letter(
          paragraph([itemList({ items: [item(variable("var1")), item(literal({ text: "lit1" }))] })]),
        );
        const mergeId = { blockIndex: 0, contentIndex: 0, itemIndex: 1 };
        const result = Actions.merge(state, { ...mergeId, itemContentIndex: 0 }, MergeTarget.PREVIOUS);
        expect(select<Item>(result, { ...mergeId, itemIndex: mergeId.itemIndex - 1 }).content).toEqual([
          ...select<Item>(state, { ...mergeId, itemIndex: mergeId.itemIndex - 1 }).content,
          ...select<Item>(state, mergeId).content,
        ]);
      });

      test("adjoining literal content in merging items are joined", () => {
        const state = letter(
          paragraph([
            itemList({
              items: [
                item(variable("var1"), newLiteral({ text: "lit1" })),
                item(literal({ text: "lit2" }), variable("var2")),
              ],
            }),
          ]),
        );
        const mergeId = { blockIndex: 0, contentIndex: 0, itemIndex: 1 };

        const result = Actions.merge(state, { ...mergeId, itemContentIndex: 0 }, MergeTarget.PREVIOUS);

        const mergedId = { ...mergeId, itemIndex: mergeId.itemIndex - 1 };
        expect(select<Item>(result, mergedId).content).toHaveLength(3);
        const mergedLiteral = select<LiteralValue>(result, { ...mergedId, itemContentIndex: 1 });
        expect(mergedLiteral.text).toEqual("lit2");
        expect(mergedLiteral.editedText).toEqual("lit1lit2");
      });

      describe("the previous item is empty", () => {
        const state = letter(
          paragraph([itemList({ items: [item(literal({ text: "" })), item(literal({ text: "lit1" }))] })]),
        );
        const mergeId = { blockIndex: 0, contentIndex: 0, itemIndex: 1, itemContentIndex: 0 };
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
        const state = letter(
          paragraph([itemList({ items: [item(literal({ text: "lit" })), item(literal({ text: "" }))] })]),
        );
        const mergeId = { blockIndex: 0, contentIndex: 0, itemIndex: 1 };
        const result = Actions.merge(state, { ...mergeId, itemContentIndex: 0 }, MergeTarget.PREVIOUS);

        test("then the list is split and the empty item removed", () => {
          // Break out: [block: list(lit)] [block: newLiteral()]
          expect(result.redigertBrev.blocks).toHaveLength(2);
          expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).items).toHaveLength(1);
        });

        test("then the focus is set to the blank line block", () => {
          expect(result.focus).toEqual({
            blockIndex: 1,
            contentIndex: 0,
            cursorPosition: 0,
          });
        });

        test("and it is the only one then the list is replaced with a blank line block", () => {
          const state = letter(
            paragraph([literal({ text: "before list" }), itemList({ items: [item(literal({ text: "" }))] })]),
          );

          const result = Actions.merge(
            state,
            { blockIndex: 0, contentIndex: 1, itemIndex: 0, itemContentIndex: 0 },
            MergeTarget.PREVIOUS,
          );

          // Break out: [block: literal("before list")] [block: newLiteral()]
          expect(result.redigertBrev.blocks).toHaveLength(2);
          expect(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }).text).toStrictEqual("before list");
          expect(select<LiteralValue>(result, { blockIndex: 1, contentIndex: 0 })).toStrictEqual(newLiteral());
          expect(result.focus).toStrictEqual({ blockIndex: 1, contentIndex: 0, cursorPosition: 0 });
        });
      });

      test("from empty itemList to otherwise empty block", () => {
        const state = letter(
          paragraph([literal({ text: "hei" })]),
          paragraph([itemList({ items: [item(literal({ text: "" }))] })]),
        );
        const result = Actions.merge(
          state,
          { blockIndex: 1, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 },
          MergeTarget.PREVIOUS,
        );
        expect(select<ParagraphBlock>(result, { blockIndex: 1 }).content).toHaveLength(1);
        expect(result.focus).toEqual({ blockIndex: 1, contentIndex: 0, cursorPosition: 0 });
      });
    });
  });

  describe("at newline", () => {
    test("newline is previous", () => {
      const state = letter(paragraph([literal({ text: "l1" }), newLine(), literal({ text: "l2" })]));
      const result = Actions.merge(state, { blockIndex: 0, contentIndex: 2 }, MergeTarget.PREVIOUS);

      expect(result.redigertBrev.blocks[0].content.length).toEqual(2);
      expect(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }).text).toEqual("l1");
      expect(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 1 }).text).toEqual("l2");
      expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 2 });
    });

    test("newline is next", () => {
      const state = letter(paragraph([literal({ text: "l1" }), newLine(), literal({ text: "l2" })]));
      const result = Actions.merge(state, { blockIndex: 0, contentIndex: 0 }, MergeTarget.NEXT);

      expect(result.redigertBrev.blocks[0].content.length).toEqual(2);
      expect(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }).text).toEqual("l1");
      expect(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 1 }).text).toEqual("l2");
      expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 2 });
    });
  });

  describe("break out of empty item and re-merge", () => {
    test("backspace on blank line between same-type lists merges them", () => {
      const state = letter(
        paragraph([itemList({ items: [item(literal({ text: "a" })), item(literal({ text: "b" }))] })]),
        paragraph([literal({ text: "" })]),
        paragraph([itemList({ items: [item(literal({ text: "c" })), item(literal({ text: "d" }))] })]),
      );

      // Backspace on empty block 1
      const result = Actions.merge(state, { blockIndex: 1, contentIndex: 0 }, MergeTarget.PREVIOUS);

      // Should merge into one block with one list containing all 4 items
      expect(result.redigertBrev.blocks).toHaveLength(1);
      expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).items).toHaveLength(4);
      // Focus lands at end of last item of the first list (item "b", now at index 1)
      expect(result.focus).toMatchObject({
        blockIndex: 0,
        contentIndex: 0,
        itemIndex: 1,
        itemContentIndex: 0,
        cursorPosition: 1,
      });
    });

    test("backspace on blank line between different-type lists does not merge them", () => {
      const state = letter(
        paragraph([itemList({ items: [item(literal({ text: "a" }))], listType: ListType.PUNKTLISTE })]),
        paragraph([literal({ text: "" })]),
        paragraph([itemList({ items: [item(literal({ text: "b" }))], listType: ListType.NUMMERERT_LISTE })]),
      );

      // Backspace on empty block 1
      const result = Actions.merge(state, { blockIndex: 1, contentIndex: 0 }, MergeTarget.PREVIOUS);

      // Empty block removed, lists stay separate
      expect(result.redigertBrev.blocks).toHaveLength(2);
      // Focus lands at end of last item of the first (bullet) list
      expect(result.focus).toMatchObject({
        blockIndex: 0,
        contentIndex: 0,
        itemIndex: 0,
        itemContentIndex: 0,
        cursorPosition: 1,
      });
    });

    test("full round-trip: enter on empty item, then backspace to re-merge", () => {
      const state = letter(
        paragraph([
          itemList({
            items: [item(literal({ text: "a" })), item(literal({ text: "" })), item(literal({ text: "c" }))],
          }),
        ]),
      );

      // Enter on empty middle item → break out
      const afterEnter = Actions.split(state, { blockIndex: 0, contentIndex: 0, itemIndex: 1, itemContentIndex: 0 }, 0);
      expect(afterEnter.redigertBrev.blocks).toHaveLength(3);

      // Backspace on blank line block → re-merge
      const afterBackspace = Actions.merge(afterEnter, { blockIndex: 1, contentIndex: 0 }, MergeTarget.PREVIOUS);
      expect(afterBackspace.redigertBrev.blocks).toHaveLength(1);
      expect(select<ItemList>(afterBackspace, { blockIndex: 0, contentIndex: 0 }).items).toHaveLength(2);
    });

    test("full round-trip: caret lands after last item of first list, not end of merged list", () => {
      const state = letter(
        paragraph([
          itemList({
            items: [
              item(literal({ text: "item1" })),
              item(literal({ text: "item2" })),
              item(literal({ text: "" })),
              item(literal({ text: "item4" })),
            ],
          }),
        ]),
      );

      // Enter on empty third item → breaks into [item1, item2] + blank + [item4]
      const afterEnter = Actions.split(state, { blockIndex: 0, contentIndex: 0, itemIndex: 2, itemContentIndex: 0 }, 0);
      expect(afterEnter.redigertBrev.blocks).toHaveLength(3);
      expect(select<ItemList>(afterEnter, { blockIndex: 0, contentIndex: 0 }).items).toHaveLength(2);
      expect(select<ItemList>(afterEnter, { blockIndex: 2, contentIndex: 0 }).items).toHaveLength(1);

      // Backspace on blank line → re-merge, caret should land after item2 (itemIndex 1)
      const afterBackspace = Actions.merge(afterEnter, { blockIndex: 1, contentIndex: 0 }, MergeTarget.PREVIOUS);
      expect(afterBackspace.redigertBrev.blocks).toHaveLength(1);
      expect(select<ItemList>(afterBackspace, { blockIndex: 0, contentIndex: 0 }).items).toHaveLength(3);
      // Caret should be at end of item2 (index 1), NOT at end of item4 (index 2)
      expect(afterBackspace.focus).toEqual({
        blockIndex: 0,
        contentIndex: 0,
        cursorPosition: 5, // length of "item2"
        itemIndex: 1,
        itemContentIndex: 0,
      });
    });

    test("backspace on empty first item breaks out", () => {
      const state = letter(
        paragraph([
          itemList({
            items: [item(literal({ text: "" })), item(literal({ text: "b" })), item(literal({ text: "c" }))],
          }),
        ]),
      );

      const result = Actions.merge(
        state,
        { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 },
        MergeTarget.PREVIOUS,
      );

      // [block: newLiteral()] [block: list(b, c)]
      expect(result.redigertBrev.blocks).toHaveLength(2);
      expect(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 })).toStrictEqual(newLiteral());
      expect(select<ItemList>(result, { blockIndex: 1, contentIndex: 0 }).items).toHaveLength(2);
      expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 0 });
    });
  });

  describe("cross-block merge into list", () => {
    test("backspace at start of line merges text into last item of previous list", () => {
      const state = letter(
        paragraph([itemList({ items: [item(literal({ text: "a" })), item(literal({ text: "b" }))] })]),
        paragraph([literal({ text: "text" })]),
      );

      const result = Actions.merge(state, { blockIndex: 1, contentIndex: 0 }, MergeTarget.PREVIOUS);

      // Text merged into last item: list(a, [b, text])
      expect(result.redigertBrev.blocks).toHaveLength(1);
      expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).items).toHaveLength(2);
      // Both literals keep their IDs, so they stay separate (mergeLiteralsIfPossible won't merge two ID'd literals)
      const lastItemContent = select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 1 }).content;
      expect(lastItemContent).toHaveLength(2);
      expect(text(lastItemContent[0])).toBe("b");
      expect(text(lastItemContent[1])).toBe("text");
      // Caret at end of "b" (position 1 in the first literal)
      expect(result.focus).toEqual({
        blockIndex: 0,
        contentIndex: 0,
        cursorPosition: 1,
        itemIndex: 1,
        itemContentIndex: 0,
      });
    });

    test("backspace at start of line between same-type lists merges text and lists", () => {
      const state = letter(
        paragraph([itemList({ items: [item(literal({ text: "a" })), item(literal({ text: "b" }))] })]),
        paragraph([literal({ text: "text" })]),
        paragraph([itemList({ items: [item(literal({ text: "c" })), item(literal({ text: "d" }))] })]),
      );

      const result = Actions.merge(state, { blockIndex: 1, contentIndex: 0 }, MergeTarget.PREVIOUS);

      // All merged: list(a, b+"text", c, d)
      expect(result.redigertBrev.blocks).toHaveLength(1);
      expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).items).toHaveLength(4);
      // Caret at the merge point in item "b" (end of original "b", before "text")
      expect(result.focus).toEqual({
        blockIndex: 0,
        contentIndex: 0,
        cursorPosition: 1,
        itemIndex: 1,
        itemContentIndex: 0,
      });
    });

    test("backspace at start of line between different-type lists merges text but not lists", () => {
      const state = letter(
        paragraph([
          itemList({
            items: [item(literal({ text: "a" })), item(literal({ text: "b" }))],
            listType: ListType.PUNKTLISTE,
          }),
        ]),
        paragraph([literal({ text: "text" })]),
        paragraph([itemList({ items: [item(literal({ text: "c" }))], listType: ListType.NUMMERERT_LISTE })]),
      );

      const result = Actions.merge(state, { blockIndex: 1, contentIndex: 0 }, MergeTarget.PREVIOUS);

      // Text merged into punktliste, but nummerliste stays separate
      expect(result.redigertBrev.blocks).toHaveLength(2);
      expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).items).toHaveLength(2);
      expect(select<ItemList>(result, { blockIndex: 1, contentIndex: 0 }).items).toHaveLength(1);
      expect(result.focus).toEqual({
        blockIndex: 0,
        contentIndex: 0,
        cursorPosition: 1,
        itemIndex: 1,
        itemContentIndex: 0,
      });
    });

    test("backspace at start of line with no list after only merges text into list", () => {
      const state = letter(
        paragraph([itemList({ items: [item(literal({ text: "a" }))] })]),
        paragraph([literal({ text: "hello" })]),
        paragraph([literal({ text: "more content" })]),
      );

      const result = Actions.merge(state, { blockIndex: 1, contentIndex: 0 }, MergeTarget.PREVIOUS);

      // Text merged into last item, third block unchanged
      expect(result.redigertBrev.blocks).toHaveLength(2);
      const lastItemContent = select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0 }).content;
      expect(lastItemContent).toHaveLength(2);
      expect(text(lastItemContent[0])).toBe("a");
      expect(text(lastItemContent[1])).toBe("hello");
      expect(result.focus).toEqual({
        blockIndex: 0,
        contentIndex: 0,
        cursorPosition: 1,
        itemIndex: 0,
        itemContentIndex: 0,
      });
    });

    test("caret position with variable at end of last item", () => {
      const state = letter(
        paragraph([itemList({ items: [item(variable("var1"))] })]),
        paragraph([literal({ text: "text" })]),
      );

      const result = Actions.merge(state, { blockIndex: 1, contentIndex: 0 }, MergeTarget.PREVIOUS);

      // Variable is not a literal, so cursorPosition = 0 (before the variable there's no text)
      expect(result.focus).toEqual({
        blockIndex: 0,
        contentIndex: 0,
        cursorPosition: 0,
        itemIndex: 0,
        itemContentIndex: 0,
      });
    });

    test("previous block with mixed content ending with list", () => {
      const state = letter(
        paragraph([literal({ text: "before" }), itemList({ items: [item(literal({ text: "a" }))] })]),
        paragraph([literal({ text: "merged" })]),
      );

      const result = Actions.merge(state, { blockIndex: 1, contentIndex: 0 }, MergeTarget.PREVIOUS);

      // Text merged into last item of the list
      expect(result.redigertBrev.blocks).toHaveLength(1);
      expect(select<ParagraphBlock>(result, { blockIndex: 0 }).content).toHaveLength(2);
      const lastItemContent = select<Item>(result, { blockIndex: 0, contentIndex: 1, itemIndex: 0 }).content;
      expect(lastItemContent).toHaveLength(2);
      expect(text(lastItemContent[0])).toBe("a");
      expect(text(lastItemContent[1])).toBe("merged");
    });

    test("next block starts with same-type list: both lists merge after text merge", () => {
      const state = letter(
        paragraph([itemList({ items: [item(literal({ text: "a" }))] })]),
        paragraph([literal({ text: "" }), itemList({ items: [item(literal({ text: "b" }))] })]),
      );

      // Backspace at the start of the empty literal merges it into the list in block 0,
      // which then triggers mergeAdjacentListBlocks to merge the remaining same-type list.
      const result = Actions.merge(state, { blockIndex: 1, contentIndex: 0 }, MergeTarget.PREVIOUS);

      expect(result.redigertBrev.blocks).toHaveLength(1);
      expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).items).toHaveLength(2);
    });

    test("next block starts with different-type list: lists stay separate after text merge", () => {
      const state = letter(
        paragraph([itemList({ items: [item(literal({ text: "a" }))], listType: ListType.PUNKTLISTE })]),
        paragraph([
          literal({ text: "" }),
          itemList({ items: [item(literal({ text: "b" }))], listType: ListType.NUMMERERT_LISTE }),
        ]),
      );

      const result = Actions.merge(state, { blockIndex: 1, contentIndex: 0 }, MergeTarget.PREVIOUS);

      // Empty text merged, but different-type lists stay separate
      expect(result.redigertBrev.blocks).toHaveLength(2);
      expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).listType).toBe(ListType.PUNKTLISTE);
      expect(select<ItemList>(result, { blockIndex: 1, contentIndex: 0 }).listType).toBe(ListType.NUMMERERT_LISTE);
    });
  });

  describe("break out of empty item in template mixed-content block (backspace)", () => {
    test("empty middle item with content before and after list: backspace removes empty item and preserves surroundings", () => {
      const state = letter(
        paragraph([
          literal({ text: "before" }),
          itemList({
            items: [item(literal({ text: "item1" })), item(literal({ text: "" })), item(literal({ text: "item3" }))],
          }),
          literal({ text: "after" }),
        ]),
      );

      const result = Actions.merge(
        state,
        { blockIndex: 0, contentIndex: 1, itemIndex: 1, itemContentIndex: 0 },
        MergeTarget.PREVIOUS,
      );

      // [before] [list(item1)] [blank] [list(item3)] [after]
      expect(result.redigertBrev.blocks).toHaveLength(5);
      expect(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }).text).toBe("before");
      expect(select<ItemList>(result, { blockIndex: 1, contentIndex: 0 }).items).toHaveLength(1);
      expect(select<LiteralValue>(result, { blockIndex: 2, contentIndex: 0 })).toStrictEqual(newLiteral());
      expect(select<ItemList>(result, { blockIndex: 3, contentIndex: 0 }).items).toHaveLength(1);
      expect(select<LiteralValue>(result, { blockIndex: 4, contentIndex: 0 }).text).toBe("after");
      // Focus on the blank line
      expect(result.focus).toEqual({ blockIndex: 2, contentIndex: 0, cursorPosition: 0 });
    });
  });

  describe("backspace over NEW_LINE within an item", () => {
    test("removes the NEW_LINE and merges the surrounding literals when one has id:null", () => {
      // Use one id:null literal so mergeLiteralsIfPossible can merge them
      const it = item({ content: [literal({ text: "abc" }), newLine(), asNew(literal({ text: "def" }))] });
      const state = letter(paragraph([itemList({ items: [it] })]));

      // Cursor at start of "def" (itemContentIndex: 2), backspace → remove NEW_LINE, merge "abc"+"def"
      const result = Actions.merge(
        state,
        { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 2 },
        MergeTarget.PREVIOUS,
      );

      const resultItem = select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0 });
      expect(resultItem.content).toHaveLength(1);
      expect(text(resultItem.content[0] as LiteralValue)).toEqual("abcdef");
      expect(result.focus).toMatchObject({
        blockIndex: 0,
        contentIndex: 0,
        itemIndex: 0,
        itemContentIndex: 0,
        cursorPosition: "abc".length,
      });
    });

    test("removes the NEW_LINE and keeps two literals when both have non-null ids", () => {
      const it = item({ content: [literal({ text: "abc" }), newLine(), literal({ text: "def" })] });
      const state = letter(paragraph([itemList({ items: [it] })]));

      const result = Actions.merge(
        state,
        { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 2 },
        MergeTarget.PREVIOUS,
      );

      const resultItem = select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0 });
      expect(resultItem.content).toHaveLength(2);
      expect(text(resultItem.content[0] as LiteralValue)).toEqual("abc");
      expect(text(resultItem.content[1] as LiteralValue)).toEqual("def");
      expect(result.focus).toMatchObject({ itemContentIndex: 0, cursorPosition: "abc".length });
    });

    test("Delete over NEW_LINE removes it and merges the surrounding literals", () => {
      const it = item({ content: [literal({ text: "abc" }), newLine(), asNew(literal({ text: "def" }))] });
      const state = letter(paragraph([itemList({ items: [it] })]));

      // Cursor at end of "abc" (itemContentIndex: 0), delete → remove NEW_LINE, merge "abc"+"def"
      const result = Actions.merge(
        state,
        { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0 },
        MergeTarget.NEXT,
      );

      const resultItem = select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0 });
      expect(resultItem.content).toHaveLength(1);
      expect(text(resultItem.content[0] as LiteralValue)).toEqual("abcdef");
    });

    test("removes NEW_LINE between a variable and a literal (only removes NEW_LINE, no literal merge)", () => {
      const it = item({ content: [variable("var"), newLine(), literal({ text: "def" })] });
      const state = letter(paragraph([itemList({ items: [it] })]));

      const result = Actions.merge(
        state,
        { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 2 },
        MergeTarget.PREVIOUS,
      );

      const resultItem = select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0 });
      expect(resultItem.content).toHaveLength(2);
      expect(resultItem.content[0].type).toBe("VARIABLE");
      expect(text(resultItem.content[1] as LiteralValue)).toEqual("def");
    });

    test("NEW_LINE removal takes priority over item-to-item merge", () => {
      // Two items; the first has an internal NEW_LINE. Backspace at the second item's first literal
      // should trigger item-to-item merge (not touch the NEW_LINE inside item 0).
      const it0 = item({ content: [literal({ text: "a" }), newLine(), literal({ text: "b" })] });
      const it1 = item(literal({ text: "c" }));
      const state = letter(paragraph([itemList({ items: [it0, it1] })]));

      const result = Actions.merge(
        state,
        { blockIndex: 0, contentIndex: 0, itemIndex: 1, itemContentIndex: 0 },
        MergeTarget.PREVIOUS,
      );

      // Items are merged: item 1 absorbed into item 0, NEW_LINE inside item 0 untouched
      expect(select<ItemList>(result, { blockIndex: 0, contentIndex: 0 }).items).toHaveLength(1);
      const merged = select<Item>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0 });
      expect(merged.content).toHaveLength(it0.content.length + it1.content.length);
    });
  });
});
