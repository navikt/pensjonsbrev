import { expect } from "vitest";

import Actions from "~/Brevredigering/LetterEditor/actions";
import type { LiteralValue, ParagraphBlock } from "~/types/brevbakerTypes";

import { item, itemList, letter, literal, paragraph, select } from "../utils";

describe("LetterEditorActions.toggleBulletList", () => {
  describe("has adjoining itemList", () => {
    test("should not merge with itemList in previous block if not first in current block", () => {
      const state = letter(
        paragraph(itemList(item(literal("b1-p1")))),
        paragraph(literal("b2-l1"), itemList(item(literal("b2-p1"))), literal("b2-l2")),
      );
      const result = Actions.toggleBulletList(state, { blockIndex: 1, contentIndex: 2 });
      expect(result.redigertBrev.blocks).toHaveLength(2);
      expect(result.redigertBrev.blocks[0]).toEqual(state.redigertBrev.blocks[0]);

      const toggledContent = select<LiteralValue>(state, { blockIndex: 1, contentIndex: 2 });

      const toggledInBlock = select<ParagraphBlock>(result, { blockIndex: 1 });
      expect(toggledInBlock.content).toHaveLength(2);
      expect(toggledInBlock.deletedContent).toEqual([toggledContent.id]);
      expect(toggledInBlock?.content[0]).toEqual(state.redigertBrev.blocks[1].content[0]);

      expect(
        select<LiteralValue>(result, { blockIndex: 1, contentIndex: 1, itemIndex: 1, itemContentIndex: 0 }),
      ).toEqual(toggledContent);
    });
  });
});
