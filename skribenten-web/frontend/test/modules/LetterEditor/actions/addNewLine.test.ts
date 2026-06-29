import { describe, expect, test } from "vitest";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { text } from "~/Brevredigering/LetterEditor/actions/common";
import { ListType, type LiteralValue, NEW_LINE, type ParagraphBlock, type TextContent } from "~/types/brevbakerTypes";

import { item, itemList, letter, literal, newLine, paragraph, select, title1, title2 } from "../utils";

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
    const state = letter(paragraph([literal({ text: "l1" }), literal({ text: "l2" })]));
    const result = Actions.addNewLine(state, { blockIndex: 0, contentIndex: 0, cursorPosition: 2 });
    const block = select<ParagraphBlock>(result, { blockIndex: 0 });

    expect(block.content).toHaveLength(3);
    expect(block.content[0]).toEqual(state.redigertBrev.blocks[0].content[0]);
    expect(block.content[1].type).toEqual("NEW_LINE");
  });

  test("adds new line to paragraph before literal", () => {
    const state = letter(paragraph([literal({ text: "l1" })]));
    const result = Actions.addNewLine(state, { blockIndex: 0, contentIndex: 0, cursorPosition: 0 });
    const block = select<ParagraphBlock>(result, { blockIndex: 0 });

    expect(block.content).toHaveLength(3);
    expect(block.content[0]).toEqual({
      id: null,
      parentId: null,
      type: "LITERAL",
      text: "",
      editedText: null,
      fontType: "PLAIN",
      tags: [],
    });
    expect(block.content[1].type).toEqual("NEW_LINE");
    expect(block.content[2]).toEqual(state.redigertBrev.blocks[0].content[0]);
  });

  test("splits literal and adds newline in between", () => {
    const state = letter(paragraph([literal({ text: "l1l2" })]));
    const result = Actions.addNewLine(state, { blockIndex: 0, contentIndex: 0, cursorPosition: 2 });
    const block = select<ParagraphBlock>(result, { blockIndex: 0 });

    expect(block.content).toHaveLength(3);
    expect(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }).editedText).toEqual("l1");
    expect(select<TextContent>(result, { blockIndex: 0, contentIndex: 1 }).type).toEqual("NEW_LINE");
    expect(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 2 }).editedText).toEqual("l2");
  });

  test("when adding newline at end of last literal a new literal is also added", () => {
    const state = letter(paragraph([literal({ text: "l1" })]));
    const result = Actions.addNewLine(state, { blockIndex: 0, contentIndex: 0, cursorPosition: 2 });
    const block = select<ParagraphBlock>(result, { blockIndex: 0 });

    expect(block.content).toHaveLength(3);
    expect(block.content[0]).toEqual(state.redigertBrev.blocks[0].content[0]);
    expect(select<TextContent>(result, { blockIndex: 0, contentIndex: 1 }).type).toEqual("NEW_LINE");
    expect(text(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 2 }))).toEqual("");
  });
});

describe("Actions.addNewLine inside list item", () => {
  test("adds new line after literal (mid-item, end position)", () => {
    const it = item(literal({ text: "hello" }));
    const state = letter(paragraph([itemList({ items: [it] })]));
    const result = Actions.addNewLine(state, {
      blockIndex: 0,
      contentIndex: 0,
      itemIndex: 0,
      itemContentIndex: 0,
      cursorPosition: 5,
    });

    const resultItem = select<typeof it>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0 });
    expect(resultItem.content).toHaveLength(3);
    expect(resultItem.content[0]).toEqual(it.content[0]);
    expect(resultItem.content[1].type).toEqual(NEW_LINE);
    expect(text(resultItem.content[2] as LiteralValue)).toEqual("");
    expect(result.focus).toMatchObject({
      blockIndex: 0,
      contentIndex: 0,
      itemIndex: 0,
      itemContentIndex: 2,
      cursorPosition: 0,
    });
  });

  test("adds new line before literal (start position)", () => {
    const it = item(literal({ text: "hello" }));
    const state = letter(paragraph([itemList({ items: [it] })]));
    const result = Actions.addNewLine(state, {
      blockIndex: 0,
      contentIndex: 0,
      itemIndex: 0,
      itemContentIndex: 0,
      cursorPosition: 0,
    });

    const resultItem = select<typeof it>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0 });
    expect(resultItem.content).toHaveLength(3);
    expect(resultItem.content[0]).toMatchObject({ type: "LITERAL", text: "" });
    expect(resultItem.content[1].type).toEqual(NEW_LINE);
    expect(resultItem.content[2]).toEqual(it.content[0]);
    expect(result.focus).toMatchObject({
      blockIndex: 0,
      contentIndex: 0,
      itemIndex: 0,
      itemContentIndex: 2,
      cursorPosition: 0,
    });
  });

  test("splits literal and inserts new line in middle", () => {
    const it = item(literal({ text: "abcdef" }));
    const state = letter(paragraph([itemList({ items: [it] })]));
    const result = Actions.addNewLine(state, {
      blockIndex: 0,
      contentIndex: 0,
      itemIndex: 0,
      itemContentIndex: 0,
      cursorPosition: 3,
    });

    const resultItem = select<typeof it>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0 });
    expect(resultItem.content).toHaveLength(3);
    expect(text(resultItem.content[0] as LiteralValue)).toEqual("abc");
    expect(resultItem.content[1].type).toEqual(NEW_LINE);
    expect(text(resultItem.content[2] as LiteralValue)).toEqual("def");
    expect(result.focus).toMatchObject({ itemContentIndex: 2, cursorPosition: 0 });
  });

  test("does not add consecutive new lines at end", () => {
    const it = item({ content: [literal({ text: "hi" }), newLine()] });
    const state = letter(paragraph([itemList({ items: [it] })]));
    const result = Actions.addNewLine(state, {
      blockIndex: 0,
      contentIndex: 0,
      itemIndex: 0,
      itemContentIndex: 0,
      cursorPosition: 2,
    });
    expect(result).toBe(state);
  });

  test("works identically for NUMMERERT_LISTE items", () => {
    const it = item(literal({ text: "punkt" }));
    const state = letter(paragraph([itemList({ items: [it], listType: ListType.NUMMERERT_LISTE })]));
    const result = Actions.addNewLine(state, {
      blockIndex: 0,
      contentIndex: 0,
      itemIndex: 0,
      itemContentIndex: 0,
      cursorPosition: 5,
    });

    const resultItem = select<typeof it>(result, { blockIndex: 0, contentIndex: 0, itemIndex: 0 });
    expect(resultItem.content).toHaveLength(3);
    expect(resultItem.content[1].type).toEqual(NEW_LINE);
  });
});
