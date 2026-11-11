import { describe, expect, test } from "vitest";

import {
  item,
  list,
  literal,
  missingFromTemplate,
  newLine,
  newText,
  numberedList,
  paragraph,
  stateV2,
  texts,
  variable,
} from "~test/support/editorV2TestUtils";

import {
  appendContent,
  breakOutOfList,
  countMissingFromTemplate,
  deleteRange,
  insertSibling,
  joinAdjacentLists,
  removeContainer,
  splitContent,
} from "./structure";

const doc = (...blocks: Parameters<typeof stateV2>) => stateV2(...blocks).redigertBrev;

describe("splitContent", () => {
  test("splits a literal and moves the rest into the tail", () => {
    const block = paragraph(literal("abcd"), variable("v"), literal("e"));
    const [, moved, last] = block.content;
    const tail = splitContent(block, { path: { kind: "block", block: 0 }, content: 0, offset: 2 });
    expect(texts(block.content)).toEqual(["ab"]);
    expect(texts(tail)).toEqual(["cd", "v", "e"]);
    expect(block.deletedContent).toEqual([moved.id, last.id]);
  });

  test("splitting before a variable starts the tail with an empty literal", () => {
    const block = paragraph(literal("ab"), variable("v"));
    const first = block.content[0];
    const tail = splitContent(block, { path: { kind: "block", block: 0 }, content: 1, offset: 0 });
    expect(block.content).toEqual([first]);
    expect(texts(tail)).toEqual(["", "v", ""]);
  });
});

describe("appendContent", () => {
  test("moves content and merges new literals", () => {
    const target = paragraph(literal("a"));
    const source = paragraph(newText("b"), variable("v"));
    appendContent(target, source);
    expect(texts(target.content)).toEqual(["ab", "v", ""]);
    expect(source.content).toEqual([]);
  });
});

describe("lists", () => {
  test("joinAdjacentLists joins lists of the same type", () => {
    const first = list(item(literal("a")));
    const second = list(item(literal("b")));
    const d = doc(first, second);
    joinAdjacentLists(d, 1);
    expect(d.blocks).toEqual([first]);
    expect(first.items).toHaveLength(2);
    expect(d.deletedBlocks).toEqual([second.id]);
    expect(second.deletedItems).toHaveLength(1);
  });

  test("joinAdjacentLists keeps lists of different types", () => {
    const d = doc(list(item(literal("a"))), numberedList(item(literal("b"))));
    joinAdjacentLists(d, 1);
    expect(d.blocks).toHaveLength(2);
  });

  test("removing the last item removes the list", () => {
    const d = doc(paragraph(literal("p")), list(item(literal("a"))));
    removeContainer(d, { kind: "item", block: 1, item: 0 });
    expect(d.blocks).toHaveLength(1);
  });

  test("removing a block between lists joins them", () => {
    const d = doc(list(item(literal("a"))), paragraph(literal("p")), list(item(literal("b"))));
    removeContainer(d, { kind: "block", block: 1 });
    expect(d.blocks).toHaveLength(1);
  });

  test("insertSibling adds new items and paragraphs", () => {
    const l = list(item(literal("a")));
    const d = doc(l, paragraph(literal("p")));
    insertSibling(d, { kind: "item", block: 0, item: 0 }, "after", [newText("b")]);
    insertSibling(d, { kind: "block", block: 1 }, "before");
    expect(l.items.map((i) => texts(i.content))).toEqual([["a"], ["b"]]);
    expect(d.blocks.map((b) => b.type)).toEqual(["ITEM_LIST", "PARAGRAPH", "PARAGRAPH"]);
    expect(insertSibling(d, { kind: "title" }, "after")).toBeUndefined();
  });
});

describe("breakOutOfList", () => {
  const three = () => list(item(literal("a")), item(literal("b")), item(literal("c")));

  test("first item becomes a paragraph before the list", () => {
    const l = three();
    const d = doc(l);
    breakOutOfList(d, { kind: "item", block: 0, item: 0 });
    expect(d.blocks.map((b) => b.type)).toEqual(["PARAGRAPH", "ITEM_LIST"]);
    expect(d.blocks[1]).toBe(l);
    expect(l.items).toHaveLength(2);
  });

  test("middle item splits the list", () => {
    const l = three();
    const d = doc(l);
    const paragraph = breakOutOfList(d, { kind: "item", block: 0, item: 1 });
    expect(d.blocks.map((b) => b.type)).toEqual(["ITEM_LIST", "PARAGRAPH", "ITEM_LIST"]);
    expect(texts(paragraph?.content ?? [])).toEqual(["b"]);
    expect(d.blocks[2]).toMatchObject({ id: null, deletedItems: [] });
    expect(l.deletedItems).toHaveLength(2);
  });

  test("last item becomes a paragraph after the list", () => {
    const d = doc(three());
    breakOutOfList(d, { kind: "item", block: 0, item: 2 });
    expect(d.blocks.map((b) => b.type)).toEqual(["ITEM_LIST", "PARAGRAPH"]);
  });

  test("only item replaces the list", () => {
    const l = list(item(literal("a")));
    const d = doc(l);
    breakOutOfList(d, { kind: "item", block: 0, item: 0 });
    expect(d.blocks.map((b) => b.type)).toEqual(["PARAGRAPH"]);
    expect(d.deletedBlocks).toEqual([l.id]);
  });
});

describe("deleteRange", () => {
  test("trims literals inside the range", () => {
    const block = paragraph(literal("abcdef"));
    deleteRange(block, 1, 4);
    expect(texts(block.content)).toEqual(["aef"]);
  });

  test("removes fully covered elements and records template ids", () => {
    const v = variable("vv");
    const n = newLine();
    const block = paragraph(literal("ab"), v, n, literal("cd"));
    deleteRange(block, 1, 6);
    expect(texts(block.content)).toEqual(["a", "d"]);
    expect(block.deletedContent).toEqual([v.id, n.id]);
  });

  test("an empty range at a new line keeps it", () => {
    const block = paragraph(literal("ab"), newLine(), literal("cd"));
    deleteRange(block, 2, 2);
    expect(block.content).toHaveLength(3);
  });
});

describe("countMissingFromTemplate", () => {
  test("counts blocks missing from the template", () => {
    const d = doc(missingFromTemplate(paragraph(literal("a"))), paragraph(), missingFromTemplate(list(item())));
    expect(countMissingFromTemplate(d)).toBe(2);
  });
});
