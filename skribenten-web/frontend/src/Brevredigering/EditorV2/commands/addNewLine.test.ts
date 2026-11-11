import { describe, expect, test } from "vitest";

import {
  item,
  itemWith,
  list,
  listAtIndex,
  literal,
  newLine,
  numberedList,
  paragraph,
  stateV2,
  textBlockAt,
  texts,
  title2,
  variable,
} from "~test/support/editorV2TestUtils";

import { type ContainerPath, caret, TITLE } from "../model/paths";
import { newLiteral } from "../model/text";
import { type LetterStateV2, run } from "../state/editorState";
import { addNewLine } from "./addNewLine";

const block: ContainerPath = { kind: "block", block: 0 };
const firstItem: ContainerPath = { kind: "item", block: 0, item: 0 };

function addAt(state: LetterStateV2, path: ContainerPath, content: number, offset: number) {
  return run(state, addNewLine, caret(path, content, offset));
}

const types = (state: LetterStateV2) => textBlockAt(state, 0).content.map((c) => c.type);

describe("addNewLine", () => {
  test("does not add new line to the title", () => {
    const state = stateV2(paragraph(literal("x")));
    expect(addAt(state, TITLE, 0, 2)).toBe(state);
  });

  test("does not add new line to a heading", () => {
    const state = stateV2(title2(literal("l1")));
    expect(addAt(state, block, 0, 2)).toBe(state);
  });

  test("adds new line after a literal", () => {
    const state = stateV2(paragraph(literal("l1"), literal("l2")));
    const result = addAt(state, block, 0, 2);
    expect(types(result)).toEqual(["LITERAL", "NEW_LINE", "LITERAL"]);
    expect(textBlockAt(result, 0).content[0]).toEqual(textBlockAt(state, 0).content[0]);
    expect(result.focus).toEqual(caret(block, 2, 0));
  });

  test("adds new line before a literal", () => {
    const state = stateV2(paragraph(literal("l1")));
    const result = addAt(state, block, 0, 0);
    const content = textBlockAt(result, 0).content;
    expect(content).toHaveLength(3);
    expect(content[0]).toEqual(newLiteral());
    expect(content[1].type).toBe("NEW_LINE");
    expect(content[2]).toEqual(textBlockAt(state, 0).content[0]);
    expect(result.focus).toEqual(caret(block, 2, 0));
  });

  test("splits a literal and adds new line in between", () => {
    const result = addAt(stateV2(paragraph(literal("l1l2"))), block, 0, 2);
    expect(types(result)).toEqual(["LITERAL", "NEW_LINE", "LITERAL"]);
    expect(texts(textBlockAt(result, 0).content)).toEqual(["l1", "", "l2"]);
    expect(textBlockAt(result, 0).content[0]).toMatchObject({ editedText: "l1" });
    expect(result.focus).toEqual(caret(block, 2, 0));
  });

  test("adds an empty literal after a new line at the end", () => {
    const state = stateV2(paragraph(literal("l1")));
    const result = addAt(state, block, 0, 2);
    expect(textBlockAt(result, 0).content[0]).toEqual(textBlockAt(state, 0).content[0]);
    expect(types(result)).toEqual(["LITERAL", "NEW_LINE", "LITERAL"]);
    expect(texts(textBlockAt(result, 0).content)[2]).toBe("");
  });

  test("adds literals around the new line next to variables", () => {
    const result = addAt(stateV2(paragraph(variable("v1"), literal(""), variable("v2"))), block, 1, 0);
    expect(types(result)).toEqual(["VARIABLE", "LITERAL", "NEW_LINE", "LITERAL", "VARIABLE"]);
  });

  test("does not add consecutive new lines", () => {
    const state = stateV2(paragraph(literal("hi"), newLine(), literal("")));
    expect(addAt(state, block, 0, 2)).toBe(state);
    expect(addAt(state, block, 2, 0)).toBe(state);
  });

  describe("in list item", () => {
    test("adds new line at the end", () => {
      const state = stateV2(list(item(literal("hello"))));
      const result = addAt(state, firstItem, 0, 5);
      const content = listAtIndex(result, 0).items[0].content;
      expect(content[0]).toEqual(listAtIndex(state, 0).items[0].content[0]);
      expect(content.map((c) => c.type)).toEqual(["LITERAL", "NEW_LINE", "LITERAL"]);
      expect(result.focus).toEqual(caret(firstItem, 2, 0));
    });

    test("adds new line at the start", () => {
      const state = stateV2(list(item(literal("hello"))));
      const result = addAt(state, firstItem, 0, 0);
      const content = listAtIndex(result, 0).items[0].content;
      expect(texts(content)).toEqual(["", "", "hello"]);
      expect(content[2]).toEqual(listAtIndex(state, 0).items[0].content[0]);
      expect(result.focus).toEqual(caret(firstItem, 2, 0));
    });

    test("splits a literal in the middle", () => {
      const result = addAt(stateV2(list(item(literal("abcdef")))), firstItem, 0, 3);
      expect(texts(listAtIndex(result, 0).items[0].content)).toEqual(["abc", "", "def"]);
      expect(result.focus).toEqual(caret(firstItem, 2, 0));
    });

    test("does not add consecutive new lines at end", () => {
      const state = stateV2(list(itemWith({ content: [literal("hi"), newLine()] })));
      expect(addAt(state, firstItem, 0, 2)).toBe(state);
    });

    test("works for numbered lists", () => {
      const result = addAt(stateV2(numberedList(item(literal("punkt")))), firstItem, 0, 5);
      expect(listAtIndex(result, 0).items[0].content[1].type).toBe("NEW_LINE");
    });
  });
});
