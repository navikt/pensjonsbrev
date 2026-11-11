import { describe, expect, test } from "vitest";

import { fritekst, literal, newLine, newText, paragraph, texts, variable } from "~test/support/editorV2TestUtils";

import {
  cleanseText,
  ensureEditable,
  linearLength,
  linearOffset,
  mergeAdjacentLiterals,
  newLiteral,
  positionAt,
  setText,
  splitLiteral,
} from "./text";

describe("setText", () => {
  test("stores changed text as editedText", () => {
    const l = literal("abc");
    setText(l, "abcd");
    expect(l).toMatchObject({ text: "abc", editedText: "abcd" });
  });

  test("resets editedText when equal to the original", () => {
    const l = literal({ text: "abc", editedText: "x" });
    setText(l, "abc");
    expect(l.editedText).toBeNull();
  });

  test("cleanses html artifacts and line breaks", () => {
    expect(cleanseText("a<br>b&nbsp;c\nd\r")).toBe("ab c d");
  });
});

describe("splitLiteral", () => {
  test("keeps the head and returns the tail as a new literal", () => {
    const l = literal({ text: "abcdef", fontType: "BOLD" });
    const tail = splitLiteral(l, 2);
    expect(l.editedText).toBe("ab");
    expect(tail).toMatchObject({ id: null, editedText: "cdef", fontType: "BOLD" });
  });

  test("uses edited text and font", () => {
    const l = literal({ text: "abc", editedText: "xyz", editedFontType: "ITALIC" });
    const tail = splitLiteral(l, 1);
    expect(l.editedText).toBe("x");
    expect(tail).toMatchObject({ editedText: "yz", fontType: "ITALIC" });
  });

  test("splitting at the end gives an empty tail", () => {
    const l = literal("abc");
    expect(texts([splitLiteral(l, 3)])).toEqual([""]);
    expect(l.editedText).toBeNull();
  });
});

describe("mergeAdjacentLiterals", () => {
  test("merges a new literal into a template literal", () => {
    const first = literal("a");
    const block = paragraph(first, newText("b"));
    mergeAdjacentLiterals(block);
    expect(block.content).toHaveLength(1);
    expect(block.content[0].id).toBe(first.id);
    expect(texts(block.content)).toEqual(["ab"]);
    expect(block.deletedContent).toEqual([]);
  });

  test("a new first literal is merged into the second", () => {
    const second = literal("b");
    const block = paragraph(newText("a"), second);
    mergeAdjacentLiterals(block);
    expect(block.content.map((c) => c.id)).toEqual([second.id]);
    expect(texts(block.content)).toEqual(["ab"]);
  });

  test("does not merge two template literals", () => {
    const block = paragraph(literal("a"), literal("b"));
    mergeAdjacentLiterals(block);
    expect(texts(block.content)).toEqual(["a", "b"]);
  });

  test("does not merge different fonts", () => {
    const block = paragraph(newText("a"), newText("b", "BOLD"));
    mergeAdjacentLiterals(block);
    expect(block.content).toHaveLength(2);
  });

  test("does not merge fritekst", () => {
    const block = paragraph(fritekst("a"), newText("b"));
    mergeAdjacentLiterals(block);
    expect(block.content).toHaveLength(2);
  });

  test("merges chains of new literals", () => {
    const block = paragraph(newText("a"), newText("b"), newText("c"), variable("v"), newText("d"));
    mergeAdjacentLiterals(block);
    expect(texts(block.content)).toEqual(["abc", "v", "d"]);
  });
});

describe("ensureEditable", () => {
  test("adds a trailing literal after a variable", () => {
    const block = paragraph(variable("v"));
    ensureEditable(block);
    expect(block.content[1]).toEqual(newLiteral());
  });

  test("adds a literal to empty content", () => {
    const block = paragraph();
    ensureEditable(block);
    expect(block.content).toEqual([newLiteral()]);
  });

  test("leaves content ending with a literal unchanged", () => {
    const block = paragraph(literal("a"));
    ensureEditable(block);
    expect(block.content).toHaveLength(1);
  });
});

describe("linear offsets", () => {
  const content = [literal("ab"), variable("vv"), newLine(), literal("cd")];

  test("new lines count as one", () => {
    expect(linearLength(content)).toBe(7);
    expect(linearOffset(content, { content: 3, offset: 1 })).toBe(6);
    expect(linearOffset(content, { content: 2, offset: 0 })).toBe(4);
  });

  test("positionAt prefers literals at boundaries", () => {
    expect(positionAt(content, 2)).toEqual({ content: 0, offset: 2 });
    expect(positionAt(content, 5)).toEqual({ content: 3, offset: 0 });
    expect(positionAt(content, 7)).toEqual({ content: 3, offset: 2 });
  });

  test("positionAt falls back to non-literals", () => {
    expect(positionAt(content, 3)).toEqual({ content: 1, offset: 1 });
    expect(positionAt([variable("v")], 1)).toEqual({ content: 0, offset: 1 });
  });

  test("positionAt and linearOffset roundtrip", () => {
    for (let linear = 0; linear <= 7; linear++) {
      expect(linearOffset(content, positionAt(content, linear))).toBe(linear);
    }
  });

  test("positionAt clamps beyond the end", () => {
    expect(positionAt(content, 99)).toEqual({ content: 3, offset: 2 });
  });
});
