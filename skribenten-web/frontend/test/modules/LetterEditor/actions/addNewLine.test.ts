import { expect, test } from "vitest";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { text } from "~/Brevredigering/LetterEditor/actions/common";
import type { LiteralValue, ParagraphBlock, TextContent } from "~/types/brevbakerTypes";
import { NEW_LINE } from "~/types/brevbakerTypes";

import { letter, literal, paragraph, select, title1, title2 } from "../utils";

describe("Actions.addNewLine", () => {
  test("does not add new line to title1", () => {
    const state = letter(title1(literal({ text: "l1" })));
    const result = Actions.addNewLine(state, { blockIndex: 0, contentIndex: 0, cursorPosition: 2 });
    expect(result).toBe(state);
    expect(result).toEqual(state);
  });

  test("does not add new line to title2", () => {
    const state = letter(title2(literal({ text: "l1" })));
    const result = Actions.addNewLine(state, { blockIndex: 0, contentIndex: 0, cursorPosition: 2 });
    expect(result).toBe(state);
    expect(result).toEqual(state);
  });

  test("adds new line to paragraph after literal", () => {
    const state = letter(paragraph(literal({ text: "l1" }), literal({ text: "l2" })));
    const result = Actions.addNewLine(state, { blockIndex: 0, contentIndex: 0, cursorPosition: 2 });
    const block = select<ParagraphBlock>(result, { blockIndex: 0 });

    expect(block.content).toHaveLength(3);
    expect(block.content[0]).toEqual(state.redigertBrev.blocks[0].content[0]);
    expect(block.content[1].type).toEqual(NEW_LINE);
  });

  test("adds new line to paragraph before literal", () => {
    const state = letter(paragraph(literal({ text: "l1" })));
    const result = Actions.addNewLine(state, { blockIndex: 0, contentIndex: 0, cursorPosition: 0 });
    const block = select<ParagraphBlock>(result, { blockIndex: 0 });

    expect(block.content).toHaveLength(2);
    expect(block.content[0].type).toEqual(NEW_LINE);
    expect(block.content[1]).toEqual(state.redigertBrev.blocks[0].content[0]);
  });

  test("splits literal and adds newline in between", () => {
    const state = letter(paragraph(literal({ text: "l1l2" })));
    const result = Actions.addNewLine(state, { blockIndex: 0, contentIndex: 0, cursorPosition: 2 });
    const block = select<ParagraphBlock>(result, { blockIndex: 0 });

    expect(block.content).toHaveLength(3);
    expect(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }).editedText).toEqual("l1");
    expect(select<TextContent>(result, { blockIndex: 0, contentIndex: 1 }).type).toEqual(NEW_LINE);
    expect(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 2 }).editedText).toEqual("l2");
  });

  test("when adding newline at end of last literal a new literal is also added", () => {
    const state = letter(paragraph(literal({ text: "l1" })));
    const result = Actions.addNewLine(state, { blockIndex: 0, contentIndex: 0, cursorPosition: 2 });
    const block = select<ParagraphBlock>(result, { blockIndex: 0 });

    expect(block.content).toHaveLength(3);
    expect(block.content[0]).toEqual(state.redigertBrev.blocks[0].content[0]);
    expect(select<TextContent>(result, { blockIndex: 0, contentIndex: 1 }).type).toEqual(NEW_LINE);
    expect(text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 2 }))).toEqual("");
  });
});
