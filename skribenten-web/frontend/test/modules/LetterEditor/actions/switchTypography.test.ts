import { describe, expect, test } from "vitest";

import Actions from "~/Brevredigering/LetterEditor/actions";
import type { ParagraphBlock, Title1Block, Title2Block } from "~/types/brevbakerTypes";
import { PARAGRAPH, TITLE1, TITLE2 } from "~/types/brevbakerTypes";

import { asNew, item, itemList, letter, literal, paragraph, select, title1, title2, variable } from "../utils";

describe("switchTypography", () => {
  test("can switch from paragraph to title1", () => {
    const state = letter(paragraph(literal({ text: "en literal" })));
    const result = Actions.switchTypography(state, { blockIndex: 0, contentIndex: 0 }, TITLE1);

    const block = select<Title1Block>(result, { blockIndex: 0 });
    expect(block.type).toEqual(TITLE1);
    expect(block.originalType).toEqual(PARAGRAPH);
  });

  test("can switch from paragraph to title2", () => {
    const state = letter(paragraph(literal({ text: "en literal" })));
    const result = Actions.switchTypography(state, { blockIndex: 0, contentIndex: 0 }, TITLE2);

    const block = select<Title2Block>(result, { blockIndex: 0 });
    expect(block.type).toEqual(TITLE2);
    expect(block.originalType).toEqual(PARAGRAPH);
  });

  test("can switch from title1 to paragraph", () => {
    const state = letter(title1(literal({ text: "en literal" })));
    const result = Actions.switchTypography(state, { blockIndex: 0, contentIndex: 0 }, PARAGRAPH);

    const block = select<ParagraphBlock>(result, { blockIndex: 0 });
    expect(block.type).toEqual(PARAGRAPH);
    expect(block.originalType).toEqual(TITLE1);
  });

  test("can switch from title1 to title2", () => {
    const state = letter(title1(literal({ text: "en literal" })));
    const result = Actions.switchTypography(state, { blockIndex: 0, contentIndex: 0 }, TITLE2);

    const block = select<Title2Block>(result, { blockIndex: 0 });
    expect(block.type).toEqual(TITLE2);
    expect(block.originalType).toEqual(TITLE1);
  });

  test("can switch from title2 to title1", () => {
    const state = letter(title2(literal({ text: "en literal" })));
    const result = Actions.switchTypography(state, { blockIndex: 0, contentIndex: 0 }, TITLE1);

    const block = select<Title1Block>(result, { blockIndex: 0 });
    expect(block.type).toEqual(TITLE1);
    expect(block.originalType).toEqual(TITLE2);
  });

  test("can switch from title2 to paragraph", () => {
    const state = letter(title2(literal({ text: "en literal" })));
    const result = Actions.switchTypography(state, { blockIndex: 0, contentIndex: 0 }, PARAGRAPH);

    const block = select<ParagraphBlock>(result, { blockIndex: 0 });
    expect(block.type).toEqual(PARAGRAPH);
    expect(block.originalType).toEqual(TITLE2);
  });

  test("originalType is not replaced when switching a second time", () => {
    const state = letter(title2(literal({ text: "en literal" })));
    const result = Actions.switchTypography(
      Actions.switchTypography(state, { blockIndex: 0, contentIndex: 0 }, PARAGRAPH),
      { blockIndex: 0, contentIndex: 0 },
      TITLE1,
    );

    const block = select<Title1Block>(result, { blockIndex: 0 });
    expect(block.originalType).toEqual(TITLE2);
  });

  test("originalType is cleared when switching back", () => {
    const state = letter(title2(literal({ text: "en literal" })));
    const result = Actions.switchTypography(
      Actions.switchTypography(state, { blockIndex: 0, contentIndex: 0 }, PARAGRAPH),
      { blockIndex: 0, contentIndex: 0 },
      TITLE2,
    );

    const block = select<Title2Block>(result, { blockIndex: 0 });
    expect(block.originalType).toBeUndefined();
  });

  test("can switch to title1 from paragraph when there is non text content at the end", () => {
    const state = letter(
      paragraph(
        variable("en variabel"),
        literal({ text: "en literal" }),
        variable("to variabel"),
        itemList({ items: [item(literal({ text: "en literal" }))] }),
      ),
    );
    const result = Actions.switchTypography(state, { blockIndex: 0, contentIndex: 1 }, TITLE1);
    expect(result.redigertBrev.blocks).toHaveLength(2);

    const title1block = select<Title1Block>(result, { blockIndex: 0 });
    expect(title1block.type).toEqual(TITLE1);
    expect(title1block.content).toHaveLength(3);
    expect(title1block.originalType).toBeUndefined();

    const paragraphBlock = select<ParagraphBlock>(result, { blockIndex: 1 });
    expect(paragraphBlock.type).toEqual(PARAGRAPH);
    expect(paragraphBlock.content).toHaveLength(1);
    expect(paragraphBlock.deletedContent).toEqual(
      select<ParagraphBlock>(state, { blockIndex: 0 })
        .content.slice(0, 3)
        .map((c) => c.id),
    );
    expect(paragraphBlock.originalType).toBeUndefined();
  });

  test("can switch to title2 from paragraph when there is non text content at the beginning", () => {
    const state = letter(
      paragraph(
        itemList({ items: [item(literal({ text: "en literal" }))] }),
        variable("en variabel"),
        literal({ text: "en literal" }),
        variable("to variabel"),
      ),
    );
    const result = Actions.switchTypography(state, { blockIndex: 0, contentIndex: 2 }, TITLE2);
    expect(result.redigertBrev.blocks).toHaveLength(2);

    const title2block = select<Title2Block>(result, { blockIndex: 1 });
    expect(title2block.type).toEqual(TITLE2);
    expect(title2block.content).toHaveLength(3);
    expect(title2block.originalType).toBeUndefined();

    const paragraphBlock = select<ParagraphBlock>(result, { blockIndex: 0 });
    expect(paragraphBlock.type).toEqual(PARAGRAPH);
    expect(paragraphBlock.content).toHaveLength(1);
    expect(paragraphBlock.deletedContent).toEqual(
      select<ParagraphBlock>(state, { blockIndex: 0 })
        .content.slice(1, 4)
        .map((c) => c.id),
    );
    expect(paragraphBlock.originalType).toBeUndefined();
  });

  test("can switch to title1 from paragraph when there is non text content in the middle", () => {
    const state = letter(
      paragraph(
        variable("en variabel"),
        itemList({ items: [item(literal({ text: "en literal" }))] }),
        literal({ text: "en literal" }),
        variable("to variabel"),
      ),
    );
    const result = Actions.switchTypography(state, { blockIndex: 0, contentIndex: 2 }, TITLE1);
    expect(result.redigertBrev.blocks).toHaveLength(2);

    const title1block = select<Title1Block>(result, { blockIndex: 1 });
    expect(title1block.type).toEqual(TITLE1);
    expect(title1block.content).toHaveLength(2);
    expect(title1block.originalType).toBeUndefined();

    const paragraphBlock = select<ParagraphBlock>(result, { blockIndex: 0 });
    expect(paragraphBlock.type).toEqual(PARAGRAPH);
    expect(paragraphBlock.content).toHaveLength(2);
    expect(paragraphBlock.deletedContent).toEqual(
      select<ParagraphBlock>(state, { blockIndex: 0 })
        .content.slice(2, 4)
        .map((c) => c.id),
    );
    expect(paragraphBlock.originalType).toBeUndefined();
  });

  test("can switch to title2 from paragraph when focus is in between non text content", () => {
    const state = letter(
      paragraph(
        variable("en variabel"),
        itemList({ items: [item(literal({ text: "punktliste 1" }))] }),
        literal({ text: "en literal" }),
        variable("to variabel"),
        itemList({ items: [item(literal({ text: "punktliste 2" }))] }),
      ),
    );
    const result = Actions.switchTypography(state, { blockIndex: 0, contentIndex: 2 }, TITLE2);
    expect(result.redigertBrev.blocks).toHaveLength(3);

    const title2block = select<Title1Block>(result, { blockIndex: 1 });
    expect(title2block.type).toEqual(TITLE2);
    expect(title2block.content).toHaveLength(2);
    expect(title2block.originalType).toBeUndefined();

    const paragraphBlock1 = select<ParagraphBlock>(result, { blockIndex: 0 });
    expect(paragraphBlock1.type).toEqual(PARAGRAPH);
    expect(paragraphBlock1.content).toHaveLength(2);
    expect(paragraphBlock1.deletedContent).toEqual(
      select<ParagraphBlock>(state, { blockIndex: 0 })
        .content.slice(2, 5)
        .map((c) => c.id),
    );
    expect(paragraphBlock1.originalType).toBeUndefined();

    const paragraphBlock2 = select<ParagraphBlock>(result, { blockIndex: 2 });
    expect(paragraphBlock2.type).toEqual(PARAGRAPH);
    expect(paragraphBlock2.content).toHaveLength(1);
    expect(paragraphBlock1.originalType).toBeUndefined();
  });

  test("not possitble to switch to title1 when focus is at non-text content", () => {
    const state = letter(
      paragraph(
        literal({ text: "noe tekst" }),
        itemList({ items: [item(literal({ text: "punktliste" }))] }),
        literal({ text: "noe mer tekst" }),
      ),
    );
    const result = Actions.switchTypography(state, { blockIndex: 0, contentIndex: 1 }, TITLE1);

    expect(result).toBe(state);
    expect(result).toEqual(state);
  });

  test("does not set originalType for new blocks", () => {
    const state = letter(asNew(paragraph(literal({ text: "noe tekst" }))));
    const result = Actions.switchTypography(state, { blockIndex: 0, contentIndex: 0 }, TITLE2);
    expect(select<ParagraphBlock>(result, { blockIndex: 0 }).originalType).toBeUndefined();
  });
});
