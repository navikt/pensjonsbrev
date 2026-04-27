import { describe, expect, it } from "vitest";

import { isTable } from "~/Brevredigering/LetterEditor/actions/common";
import { insertTable } from "~/Brevredigering/LetterEditor/actions/table";
import { isLiteral } from "~/Brevredigering/LetterEditor/model/utils";

import { letter, literal, paragraph } from "../utils";

function createLetterWithOneParagraph() {
  const para = paragraph([literal({ text: "hello" })]);
  const state = letter(para);
  state.focus = { blockIndex: 0, contentIndex: 0, cursorPosition: 5 };
  return state;
}

function createLetterWithTwoParagraphs() {
  const para1 = paragraph([literal({ text: "first" })]);
  const para2 = paragraph([literal({ text: "second" })]);
  const state = letter(para1, para2);
  state.focus = { blockIndex: 0, contentIndex: 0, cursorPosition: 0 };
  return state;
}

describe("insertTable", () => {
  it("appends a trailing empty literal when the table ends up last in the last block", () => {
    const state = createLetterWithOneParagraph();
    const result = insertTable(state, state.focus, 2, 2);

    const lastBlock = result.redigertBrev.blocks[result.redigertBrev.blocks.length - 1];
    const lastContent = lastBlock.content.at(-1);

    expect(isLiteral(lastContent)).toBe(true);
    expect(isTable(lastBlock.content.at(-2))).toBe(true);
  });

  it("does not append a trailing literal when the table is not in the last block", () => {
    const state = createLetterWithTwoParagraphs();
    // Insert into the first block (not the last)
    state.focus = { blockIndex: 0, contentIndex: 0, cursorPosition: 0 };
    const result = insertTable(state, state.focus, 2, 2);

    const firstBlock = result.redigertBrev.blocks[0];
    const lastContentInFirstBlock = firstBlock.content.at(-1);

    // The trailing literal should NOT have been added to the non-last block
    expect(isTable(lastContentInFirstBlock)).toBe(true);
  });

  it("focuses the newly inserted table header after insertion", () => {
    const state = createLetterWithOneParagraph();
    const result = insertTable(state, state.focus, 2, 2);

    expect(result.focus).toMatchObject({
      blockIndex: 0,
      rowIndex: -1,
      cellIndex: 0,
      cellContentIndex: 0,
      cursorPosition: 0,
    });
  });
});
