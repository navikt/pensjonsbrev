import { describe, expect, test } from "vitest";

import {
  cell,
  column,
  item,
  itemWith,
  list,
  listAtIndex,
  literal,
  newLine,
  newText,
  numberedList,
  paragraph,
  paragraphWith,
  row,
  stateV2,
  table,
  textBlockAt,
  texts,
  variable,
} from "~test/support/editorV2TestUtils";

import { type ContainerPath, caret, resolve } from "../model/paths";
import { caretIn, endOf } from "../model/structure";
import { type LetterStateV2, run } from "../state/editorState";
import { type Direction, merge } from "./merge";
import { split } from "./split";

const block = (index: number): ContainerPath => ({ kind: "block", block: index });
const itemPath = (index: number, itemIndex: number): ContainerPath => ({ kind: "item", block: index, item: itemIndex });

function mergeAt(state: LetterStateV2, path: ContainerPath, content: number, offset: number, direction: Direction) {
  return run(state, merge, caret(path, content, offset), direction);
}

const backward = (state: LetterStateV2, path: ContainerPath, content = 0) =>
  mergeAt(state, path, content, 0, "backward");

function forwardFromEnd(state: LetterStateV2, path: ContainerPath) {
  const container = resolve(state.redigertBrev, path);
  const at = container && caretIn(state.redigertBrev, container, endOf(container));
  if (!at) throw new Error("No container at path");
  return run(state, merge, at, "forward");
}

describe("merge", () => {
  describe("text blocks forward", () => {
    test("merges with next block", () => {
      const state = stateV2(paragraph(literal("p1")), paragraph(literal("p2")));
      expect(forwardFromEnd(state, block(0)).redigertBrev.blocks).toHaveLength(1);
    });

    test("is ignored at the last block", () => {
      const state = stateV2(paragraph(literal("p1")), paragraph(literal("p2")));
      expect(forwardFromEnd(state, block(1))).toBe(state);
    });

    test("appends content of the next block", () => {
      const state = stateV2(paragraph(variable("p1")), paragraph(literal("p2")));
      const result = forwardFromEnd(state, block(0));
      expect(textBlockAt(result, 0).content).toEqual([
        ...textBlockAt(state, 0).content,
        ...textBlockAt(state, 1).content,
      ]);
    });

    test("joins adjoining literals when one is new", () => {
      const state = stateV2(paragraph(variable("var1"), literal("lit1")), paragraph(newText("lit2"), variable("var2")));
      const result = forwardFromEnd(state, block(0));
      const content = textBlockAt(result, 0).content;
      expect(texts(content)).toEqual(["var1", "lit1lit2", "var2", ""]);
      expect(content[1]).toMatchObject({ text: "lit1", editedText: "lit1lit2" });
      expect(result.focus).toEqual(caret(block(0), 1, 4));
    });

    test("keeps current block when next is empty", () => {
      const state = stateV2(paragraph(literal("p1")), paragraph(literal("")));
      const result = forwardFromEnd(state, block(0));
      expect(textBlockAt(result, 0).id).toBe(textBlockAt(state, 0).id);
      expect(result.redigertBrev.blocks).toHaveLength(1);
    });

    test("keeps next block when current is empty", () => {
      const state = stateV2(paragraph(literal("")), paragraph(literal("p2")));
      const result = forwardFromEnd(state, block(0));
      expect(result.redigertBrev.blocks).toHaveLength(1);
      expect(textBlockAt(result, 0).id).toBe(textBlockAt(state, 1).id);
    });

    test("moving content back to its original parent unmarks it as deleted", () => {
      const state = stateV2(
        paragraphWith({ id: 1, content: [literal("first"), literal({ text: "x" })], deletedContent: [11, 12] }),
        paragraphWith({
          id: null,
          content: [literal({ id: 11, parentId: 1, text: "third" }), literal({ id: 12, parentId: 1, text: "fourth" })],
        }),
      );
      const result = forwardFromEnd(state, block(0));
      expect(textBlockAt(result, 0).deletedContent).toEqual([]);
    });

    test("removes an empty block before a list", () => {
      const state = stateV2(paragraph(literal("")), list(item(literal("a"))));
      const result = forwardFromEnd(state, block(0));
      expect(result.redigertBrev.blocks).toHaveLength(1);
      expect(result.focus).toEqual(caret(itemPath(0, 0)));
    });
  });

  describe("text blocks backward", () => {
    test("merges with previous block and records the removed block", () => {
      const state = stateV2(paragraph(literal("p1")), paragraph(variable("p2")));
      const result = backward(state, block(1));
      expect(result.redigertBrev.blocks).toHaveLength(1);
      expect(result.redigertBrev.deletedBlocks).toEqual([textBlockAt(state, 1).id]);
    });

    test("is ignored at the first block", () => {
      const state = stateV2(paragraph(literal("p1")), paragraph(variable("p2")));
      expect(backward(state, block(0))).toBe(state);
    });

    test("appends content to the previous block and keeps its id", () => {
      const state = stateV2(paragraph(variable("p1")), paragraph(literal("p2")));
      const result = backward(state, block(1));
      expect(textBlockAt(result, 0).content).toEqual([
        ...textBlockAt(state, 0).content,
        ...textBlockAt(state, 1).content,
      ]);
      expect(textBlockAt(result, 0).id).toBe(textBlockAt(state, 0).id);
    });

    test("split followed by merge restores content and deletedContent", () => {
      const state = stateV2(paragraph(literal("l1"), variable("v1"), literal("l2"), variable("v2")));
      const splitResult = run(state, split, caret(block(0), 2, 0));
      expect(textBlockAt(splitResult, 0).deletedContent).toEqual(
        textBlockAt(state, 0)
          .content.slice(2)
          .map((c) => c.id),
      );

      const result = backward(splitResult, block(1));
      expect(texts(textBlockAt(result, 0).content)).toEqual(["l1", "v1", "l2", "v2", ""]);
      expect(textBlockAt(result, 0).deletedContent).toEqual([]);
    });

    test("joins adjoining literals when one is new", () => {
      const state = stateV2(paragraph(variable("var1"), literal("lit1")), paragraph(newText("lit2"), variable("var2")));
      const result = backward(state, block(1));
      expect(textBlockAt(result, 0).content[1]).toMatchObject({ text: "lit1", editedText: "lit1lit2" });
      expect(result.focus).toEqual(caret(block(0), 1, 4));
    });

    test("does not join two template literals but focuses the merge point", () => {
      const state = stateV2(paragraph(variable("var1"), literal("lit1")), paragraph(literal("lit2"), variable("var2")));
      const result = backward(state, block(1));
      expect(texts(textBlockAt(result, 0).content)).toEqual(["var1", "lit1", "lit2", "var2", ""]);
      expect(result.focus).toEqual(caret(block(0), 1, 4));
    });

    test("keeps current block when previous is empty and focuses its start", () => {
      const state = stateV2(paragraph(literal("")), paragraph(literal("lit1"), variable("var1")));
      const result = backward(state, block(1));
      expect(textBlockAt(result, 0).id).toBe(textBlockAt(state, 1).id);
      expect(result.focus).toEqual(caret(block(0)));
    });

    test("keeps previous block when current is empty and focuses its end", () => {
      const state = stateV2(paragraph(literal("first"), variable("second"), literal("third")), paragraph(literal("")));
      const result = backward(state, block(1));
      expect(textBlockAt(result, 0).id).toBe(textBlockAt(state, 0).id);
      expect(textBlockAt(result, 0).content).toHaveLength(3);
      expect(result.focus).toEqual(caret(block(0), 2, 5));
    });

    test("merging an empty block into one ending with a variable keeps a trailing literal", () => {
      const state = stateV2(paragraph(literal("first"), variable("second")), paragraph(literal("")));
      const result = backward(state, block(1));
      const content = textBlockAt(result, 0).content;
      expect(content).toHaveLength(3);
      expect(content.at(-1)?.type).toBe("LITERAL");
    });

    test("non-first literal: removes an empty literal and focuses the end of the previous", () => {
      const state = stateV2(paragraph(literal("p1")), paragraph(literal("p2-l1"), literal("")));
      const result = backward(state, block(1), 1);
      expect(result.redigertBrev.blocks).toHaveLength(2);
      expect(textBlockAt(result, 1).content).toHaveLength(1);
      expect(textBlockAt(result, 1).deletedContent).toEqual([textBlockAt(state, 1).content[1].id]);
      expect(result.focus).toEqual(caret(block(1), 0, 5));
    });

    test("non-first literal: keeps a non-empty literal and focuses the end of the previous", () => {
      const state = stateV2(paragraph(literal("p1")), paragraph(literal("p2-l1"), literal("p2-l2")));
      const result = backward(state, block(1), 1);
      expect(textBlockAt(result, 1).content).toHaveLength(2);
      expect(result.focus).toEqual(caret(block(1), 0, 5));
    });

    test("removes an empty block after a table and focuses the end of the table", () => {
      const state = stateV2(table([column(literal("h"))], [row(cell(literal("c1")))]), paragraph(literal("")));
      const result = backward(state, block(1));
      expect(result.redigertBrev.blocks).toHaveLength(1);
      expect(result.focus).toEqual(caret({ kind: "cell", block: 0, row: 0, cell: 0 }, 0, 2));
    });

    test("does not merge a non-empty block into a table", () => {
      const state = stateV2(table([column(literal("h"))], [row(cell(literal("c1")))]), paragraph(literal("x")));
      expect(backward(state, block(1))).toBe(state);
    });
  });

  describe("items", () => {
    test("forward merges with next item", () => {
      const state = stateV2(list(item(literal("lit1")), item(literal("lit2"))));
      expect(listAtIndex(forwardFromEnd(state, itemPath(0, 0)), 0).items).toHaveLength(1);
    });

    test("forward is ignored at the last item", () => {
      const state = stateV2(list(item(literal("lit1")), item(literal("lit2"))));
      expect(forwardFromEnd(state, itemPath(0, 1))).toBe(state);
    });

    test("forward appends content of the next item", () => {
      const state = stateV2(list(item(variable("lit1")), item(literal("lit2"))));
      const items = listAtIndex(forwardFromEnd(state, itemPath(0, 0)), 0).items;
      expect(items[0].content).toEqual([
        ...listAtIndex(state, 0).items[0].content,
        ...listAtIndex(state, 0).items[1].content,
      ]);
    });

    test("forward joins adjoining literals and focuses the merge point", () => {
      const state = stateV2(list(item(variable("var1"), literal("lit1")), item(newText("lit2"), variable("var2"))));
      const result = forwardFromEnd(state, itemPath(0, 0));
      const content = listAtIndex(result, 0).items[0].content;
      expect(content[1]).toMatchObject({ text: "lit1", editedText: "lit1lit2" });
      expect(result.focus).toEqual(caret(itemPath(0, 0), 1, 4));
    });

    test("forward with empty next item removes it and focuses the end", () => {
      const state = stateV2(list(item(literal("lit1")), item(literal(""))));
      const result = forwardFromEnd(state, itemPath(0, 0));
      expect(listAtIndex(result, 0).items).toEqual([listAtIndex(state, 0).items[0]]);
      expect(result.focus).toEqual(caret(itemPath(0, 0), 0, 4));
    });

    test("forward from empty item replaces it with the next", () => {
      const state = stateV2(list(item(literal("")), item(literal("lit1"))));
      const result = forwardFromEnd(state, itemPath(0, 0));
      expect(listAtIndex(result, 0).items).toEqual([listAtIndex(state, 0).items[1]]);
      expect(result.focus).toEqual(caret(itemPath(0, 0)));
    });

    test("backward merges with previous item", () => {
      const state = stateV2(list(item(literal("lit1")), item(literal("lit2"))));
      const result = backward(state, itemPath(0, 1));
      expect(listAtIndex(result, 0).items).toHaveLength(1);
      expect(result.focus).toEqual(caret(itemPath(0, 0), 0, 4));
    });

    test("backward on first non-empty item is ignored", () => {
      const state = stateV2(list(item(literal("lit1")), item(literal("lit2"))));
      expect(backward(state, itemPath(0, 0))).toBe(state);
    });

    test("backward appends content to the previous item", () => {
      const state = stateV2(list(item(variable("var1")), item(literal("lit1"))));
      const items = listAtIndex(backward(state, itemPath(0, 1)), 0).items;
      expect(items[0].content).toEqual([
        ...listAtIndex(state, 0).items[0].content,
        ...listAtIndex(state, 0).items[1].content,
      ]);
    });

    test("backward joins adjoining literals into the template literal", () => {
      const state = stateV2(list(item(variable("var1"), newText("lit1")), item(literal("lit2"), variable("var2"))));
      const content = listAtIndex(backward(state, itemPath(0, 1)), 0).items[0].content;
      expect(content).toHaveLength(4);
      expect(content[1]).toMatchObject({ text: "lit2", editedText: "lit1lit2" });
    });

    test("backward with empty previous item removes it", () => {
      const state = stateV2(list(item(literal("")), item(literal("lit1"))));
      const result = backward(state, itemPath(0, 1));
      expect(listAtIndex(result, 0).items).toEqual([listAtIndex(state, 0).items[1]]);
      expect(result.focus).toEqual(caret(itemPath(0, 0)));
    });

    test("backward on empty last item breaks out of the list", () => {
      const state = stateV2(list(item(literal("lit")), item(literal(""))));
      const result = backward(state, itemPath(0, 1));
      expect(result.redigertBrev.blocks).toHaveLength(2);
      expect(listAtIndex(result, 0).items).toHaveLength(1);
      expect(texts(textBlockAt(result, 1).content)).toEqual([""]);
      expect(result.focus).toEqual(caret(block(1)));
    });

    test("backward on the only empty item replaces the list with a blank block", () => {
      const state = stateV2(paragraph(literal("hei")), list(item(literal(""))));
      const result = backward(state, itemPath(1, 0));
      expect(result.redigertBrev.blocks).toHaveLength(2);
      expect(texts(textBlockAt(result, 0).content)).toEqual(["hei"]);
      expect(texts(textBlockAt(result, 1).content)).toEqual([""]);
      expect(result.focus).toEqual(caret(block(1)));
    });

    test("backward on empty first item breaks out before the list", () => {
      const state = stateV2(list(item(literal("")), item(literal("b")), item(literal("c"))));
      const result = backward(state, itemPath(0, 0));
      expect(result.redigertBrev.blocks).toHaveLength(2);
      expect(texts(textBlockAt(result, 0).content)).toEqual([""]);
      expect(listAtIndex(result, 1).items).toHaveLength(2);
      expect(result.focus).toEqual(caret(block(0)));
    });

    test("backward on empty middle item splits the list", () => {
      const state = stateV2(list(item(literal("item1")), item(literal("")), item(literal("item3"))));
      const result = backward(state, itemPath(0, 1));
      expect(result.redigertBrev.blocks).toHaveLength(3);
      expect(result.focus).toEqual(caret(block(1)));
    });
  });

  describe("new lines", () => {
    test("backward removes previous new line", () => {
      const state = stateV2(paragraph(literal("l1"), newLine(), literal("l2")));
      const result = backward(state, block(0), 2);
      expect(texts(textBlockAt(result, 0).content)).toEqual(["l1", "l2"]);
      expect(result.focus).toEqual(caret(block(0), 0, 2));
    });

    test("forward removes next new line", () => {
      const state = stateV2(paragraph(literal("l1"), newLine(), literal("l2")));
      const result = mergeAt(state, block(0), 0, 2, "forward");
      expect(texts(textBlockAt(result, 0).content)).toEqual(["l1", "l2"]);
      expect(result.focus).toEqual(caret(block(0), 0, 2));
    });

    test("backward in item merges surrounding literals when one is new", () => {
      const state = stateV2(list(item(literal("abc"), newLine(), newText("def"))));
      const result = backward(state, itemPath(0, 0), 2);
      expect(texts(listAtIndex(result, 0).items[0].content)).toEqual(["abcdef"]);
      expect(result.focus).toEqual(caret(itemPath(0, 0), 0, 3));
    });

    test("backward in item keeps two template literals", () => {
      const state = stateV2(list(item(literal("abc"), newLine(), literal("def"))));
      const result = backward(state, itemPath(0, 0), 2);
      expect(texts(listAtIndex(result, 0).items[0].content)).toEqual(["abc", "def"]);
      expect(result.focus).toEqual(caret(itemPath(0, 0), 0, 3));
    });

    test("forward in item removes the new line and merges", () => {
      const state = stateV2(list(item(literal("abc"), newLine(), newText("def"))));
      const result = mergeAt(state, itemPath(0, 0), 0, 3, "forward");
      expect(texts(listAtIndex(result, 0).items[0].content)).toEqual(["abcdef"]);
    });

    test("removes new line between variable and literal", () => {
      const state = stateV2(list(item(variable("var"), newLine(), literal("def"))));
      const content = listAtIndex(backward(state, itemPath(0, 0), 2), 0).items[0].content;
      expect(content.map((c) => c.type)).toEqual(["VARIABLE", "LITERAL"]);
    });

    test("item merge leaves new lines inside the previous item untouched", () => {
      const state = stateV2(list(itemWith({ content: [literal("a"), newLine(), literal("b")] }), item(literal("c"))));
      const result = backward(state, itemPath(0, 1));
      expect(listAtIndex(result, 0).items).toHaveLength(1);
      expect(listAtIndex(result, 0).items[0].content).toHaveLength(4);
    });
  });

  describe("blank line between lists", () => {
    test("backspace joins same-type lists and focuses the end of the first", () => {
      const state = stateV2(
        list(item(literal("a")), item(literal("b"))),
        paragraph(literal("")),
        list(item(literal("c")), item(literal("d"))),
      );
      const result = backward(state, block(1));
      expect(result.redigertBrev.blocks).toHaveLength(1);
      expect(listAtIndex(result, 0).items).toHaveLength(4);
      expect(result.focus).toEqual(caret(itemPath(0, 1), 0, 1));
    });

    test("backspace keeps different-type lists apart", () => {
      const state = stateV2(list(item(literal("a"))), paragraph(literal("")), numberedList(item(literal("b"))));
      const result = backward(state, block(1));
      expect(result.redigertBrev.blocks).toHaveLength(2);
      expect(result.focus).toEqual(caret(itemPath(0, 0), 0, 1));
    });

    test("Enter then backspace on an empty item round-trips", () => {
      const state = stateV2(
        list(item(literal("item1")), item(literal("item2")), item(literal("")), item(literal("item4"))),
      );
      const afterEnter = run(state, split, caret(itemPath(0, 2)));
      expect(afterEnter.redigertBrev.blocks).toHaveLength(3);
      const afterBackspace = backward(afterEnter, block(1));
      expect(afterBackspace.redigertBrev.blocks).toHaveLength(1);
      expect(listAtIndex(afterBackspace, 0).items).toHaveLength(3);
      expect(afterBackspace.focus).toEqual(caret(itemPath(0, 1), 0, 5));
    });

    test("backspace on an empty paragraph after an item with empty editedText removes the paragraph", () => {
      const state = stateV2(list(item(literal({ text: "item 1", editedText: "" }))), paragraph(literal("")));
      const result = backward(state, block(1));
      expect(result.redigertBrev.blocks).toHaveLength(1);
      expect(result.focus).toEqual(caret(itemPath(0, 0)));
    });
  });

  describe("text into list", () => {
    test("backspace at start of a paragraph merges it into the last item", () => {
      const state = stateV2(list(item(literal("a")), item(literal("b"))), paragraph(literal("text")));
      const result = backward(state, block(1));
      expect(result.redigertBrev.blocks).toHaveLength(1);
      expect(texts(listAtIndex(result, 0).items[1].content)).toEqual(["b", "text"]);
      expect(result.focus).toEqual(caret(itemPath(0, 1), 0, 1));
    });

    test("joins same-type lists around the merged paragraph", () => {
      const state = stateV2(
        list(item(literal("a")), item(literal("b"))),
        paragraph(literal("text")),
        list(item(literal("c")), item(literal("d"))),
      );
      const result = backward(state, block(1));
      expect(result.redigertBrev.blocks).toHaveLength(1);
      expect(listAtIndex(result, 0).items).toHaveLength(4);
      expect(result.focus).toEqual(caret(itemPath(0, 1), 0, 1));
    });

    test("keeps different-type lists apart", () => {
      const state = stateV2(
        list(item(literal("a")), item(literal("b"))),
        paragraph(literal("text")),
        numberedList(item(literal("c"))),
      );
      const result = backward(state, block(1));
      expect(result.redigertBrev.blocks).toHaveLength(2);
      expect(listAtIndex(result, 0).items).toHaveLength(2);
      expect(listAtIndex(result, 1).items).toHaveLength(1);
    });

    test("leaves following paragraphs untouched", () => {
      const state = stateV2(list(item(literal("a"))), paragraph(literal("hello")), paragraph(literal("more content")));
      const result = backward(state, block(1));
      expect(result.redigertBrev.blocks).toHaveLength(2);
      expect(texts(listAtIndex(result, 0).items[0].content)).toEqual(["a", "hello"]);
    });

    test("caret lands after a trailing variable", () => {
      const state = stateV2(list(item(variable("var1"))), paragraph(literal("text")));
      const result = backward(state, block(1));
      expect(result.focus).toEqual(caret(itemPath(0, 0), 1, 0));
    });
  });
});
