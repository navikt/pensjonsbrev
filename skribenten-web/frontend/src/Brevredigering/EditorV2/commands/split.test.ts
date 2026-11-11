import { describe, expect, test } from "vitest";

import {
  item,
  itemWith,
  list,
  listAtIndex,
  listOf,
  literal,
  newText,
  paragraph,
  stateV2,
  textBlockAt,
  texts,
  title2,
  variable,
} from "~test/support/editorV2TestUtils";

import { type ContainerPath, caret, TITLE } from "../model/paths";
import { isEmptyContent } from "../model/text";
import { type LetterStateV2, run } from "../state/editorState";
import { split } from "./split";

const block = (index: number): ContainerPath => ({ kind: "block", block: index });
const itemPath = (index: number, itemIndex: number): ContainerPath => ({ kind: "item", block: index, item: itemIndex });

function splitAt(state: LetterStateV2, path: ContainerPath, content: number, offset: number) {
  return run(state, split, caret(path, content, offset));
}

describe("split", () => {
  describe("text block", () => {
    test("splits at content and offset", () => {
      const state = stateV2(paragraph(variable("var1"), literal("lit1"), variable("var2")));
      const result = splitAt(state, block(0), 1, 2);

      expect(result.redigertBrev.blocks).toHaveLength(2);
      const first = textBlockAt(result, 0);
      expect(first.content).toHaveLength(2);
      expect(first.content[1]).toMatchObject({ text: "lit1", editedText: "li" });

      const second = textBlockAt(result, 1);
      expect(texts(second.content)).toEqual(["t1", "var2", ""]);
      expect(second.content[0]).toMatchObject({ id: null, text: "", editedText: "t1" });
      expect(second.content[1]).toEqual(textBlockAt(state, 0).content[2]);
      expect(result.focus).toEqual(caret(block(1)));
    });

    test("result is not the original array", () => {
      const state = stateV2(paragraph(literal("lit1")));
      expect(splitAt(state, block(0), 0, 2).redigertBrev.blocks).not.toBe(state.redigertBrev.blocks);
    });

    test("at end of content the new block has one empty literal", () => {
      const state = stateV2(paragraph(literal("lit1")));
      const result = splitAt(state, block(0), 0, 4);
      expect(result.redigertBrev.blocks).toHaveLength(2);
      expect(textBlockAt(result, 1).content).toHaveLength(1);
      expect(texts(textBlockAt(result, 1).content)).toEqual([""]);
      expect(result.focus).toEqual(caret(block(1)));
    });

    test("does not split an empty block", () => {
      const state = stateV2(paragraph(literal("")));
      expect(splitAt(state, block(0), 0, 0)).toBe(state);
    });

    test("does not split at the beginning of a block when the previous is empty", () => {
      const state = stateV2(paragraph(literal("")), paragraph(literal("lit2")));
      expect(splitAt(state, block(1), 0, 0)).toBe(state);
    });

    test("splitting at the very end gives a new block and content without id", () => {
      const state = stateV2(paragraph(variable("var1"), variable("var2"), literal("lit1")));
      const result = splitAt(state, block(0), 2, 4);
      expect(result.redigertBrev.blocks).toHaveLength(2);
      expect(textBlockAt(result, 1).id).toBeNull();
      expect(textBlockAt(result, 1).content[0]).toMatchObject({ id: null });
      expect(texts(textBlockAt(result, 1).content)).toEqual([""]);
    });

    test("splitting inside the last content gives a new block and content without id", () => {
      const state = stateV2(paragraph(variable("var1"), variable("var2"), literal("lit1")));
      const result = splitAt(state, block(0), 2, 2);
      expect(textBlockAt(result, 1).id).toBeNull();
      expect(textBlockAt(result, 1).content[0].id).toBeNull();
    });

    test("splitting at the start of the last content moves it with its id", () => {
      const state = stateV2(paragraph(variable("var1"), variable("var2"), literal("lit1")));
      const result = splitAt(state, block(0), 2, 0);
      expect(textBlockAt(result, 1).id).toBeNull();
      expect(textBlockAt(result, 1).content[0].id).toBe(textBlockAt(state, 0).content[2].id);
      expect(texts(textBlockAt(result, 0).content)).toEqual(["var1", "var2", ""]);
    });

    test("splitting at the end of a literal does not change editedText", () => {
      const result = splitAt(stateV2(paragraph(literal("lit1"))), block(0), 0, 4);
      expect(textBlockAt(result, 0).content[0]).toMatchObject({ editedText: null });
    });

    test("uses editedText if present", () => {
      const result = splitAt(stateV2(paragraph(literal({ text: "lit1", editedText: "new text" }))), block(0), 0, 4);
      expect(texts(textBlockAt(result, 0).content)).toEqual(["new "]);
      expect(texts(textBlockAt(result, 1).content)).toEqual(["text"]);
    });

    test("content moved to the next block is marked as deleted", () => {
      const state = stateV2(paragraph(literal("before split"), literal("after split")));
      const result = splitAt(state, block(0), 0, 12);
      expect(textBlockAt(result, 0).deletedContent).toEqual([textBlockAt(state, 0).content[1].id]);
      expect(textBlockAt(result, 1).content).toHaveLength(1);
      expect(isEmptyContent(textBlockAt(result, 1).content)).toBe(false);
    });

    test("can split at offset 0 when not at the first content", () => {
      const state = stateV2(paragraph(literal("")), paragraph(variable("before split"), literal("after split")));
      expect(splitAt(state, block(1), 1, 0).redigertBrev.blocks).toHaveLength(3);
    });

    test("splitting at the very beginning inserts a new block before and focus follows the original", () => {
      const state = stateV2(paragraph(literal("begynnelse av block"), variable("slutten")));
      const result = splitAt(state, block(0), 0, 0);
      expect(result.redigertBrev.blocks).toHaveLength(2);
      expect(textBlockAt(result, 0).id).toBeNull();
      expect(textBlockAt(result, 0).deletedContent).toEqual([]);
      expect(textBlockAt(result, 1).id).toBe(textBlockAt(state, 0).id);
      expect(textBlockAt(result, 1).deletedContent).toEqual([]);
      expect(result.focus).toEqual(caret(block(1)));
    });

    test("cannot split an empty block after a list", () => {
      const state = stateV2(list(item(literal("ikke tom"))), paragraph(literal("")));
      expect(splitAt(state, block(1), 0, 0)).toBe(state);
    });

    test("splits a heading into a heading and a paragraph", () => {
      const result = splitAt(stateV2(title2(literal("overskrift"))), block(0), 0, 4);
      expect(result.redigertBrev.blocks.map((b) => b.type)).toEqual(["TITLE2", "PARAGRAPH"]);
    });

    test("does not split the title", () => {
      const state = stateV2(paragraph(literal("x")));
      expect(splitAt(state, TITLE, 0, 2)).toBe(state);
    });
  });

  describe("list", () => {
    test("splits item at content and offset", () => {
      const state = stateV2(list(item(variable("var1"), literal("lit1"), variable("var2"))));
      const result = splitAt(state, itemPath(0, 0), 1, 2);
      const items = listAtIndex(result, 0).items;

      expect(items).toHaveLength(2);
      expect(items[0].content).toHaveLength(2);
      expect(items[0].content[1]).toMatchObject({ text: "lit1", editedText: "li" });
      expect(items[1].id).toBeNull();
      expect(texts(items[1].content)).toEqual(["t1", "var2", ""]);
      expect(items[1].content[0]).toMatchObject({ text: "", editedText: "t1" });
      expect(result.focus).toEqual(caret(itemPath(0, 1)));
    });

    test("at end of item the new item has one empty literal", () => {
      const state = stateV2(list(item(variable("var1"), literal("lit1"))));
      const items = listAtIndex(splitAt(state, itemPath(0, 0), 1, 4), 0).items;
      expect(items).toHaveLength(2);
      expect(texts(items[1].content)).toEqual([""]);
    });

    test("can create new items around a non-empty new item", () => {
      const state = stateV2(
        list(item(literal("p1")), itemWith({ id: null, content: [newText(""), literal("p2")] }), item(literal("p3"))),
      );
      expect(listAtIndex(splitAt(state, itemPath(0, 0), 0, 2), 0).items).toHaveLength(4);
      expect(listAtIndex(splitAt(state, itemPath(0, 1), 1, 1), 0).items).toHaveLength(4);
      expect(listAtIndex(splitAt(state, itemPath(0, 2), 0, 0), 0).items).toHaveLength(4);
    });

    test("does not create consecutive empty items", () => {
      const state = stateV2(list(item(literal("item1")), item(literal("")), item(literal("item3"))));
      expect(splitAt(state, itemPath(0, 0), 0, 5)).toBe(state);
      expect(splitAt(state, itemPath(0, 2), 0, 0)).toBe(state);
    });

    test("splitting at the beginning of an item keeps its id", () => {
      const state = stateV2(list(item(literal("hei"), variable("joda"))));
      const items = listAtIndex(splitAt(state, itemPath(0, 0), 0, 0), 0).items;
      expect(items[0].id).toBeNull();
      expect(items[1].id).toBe(listAtIndex(state, 0).items[0].id);
      expect(items[1].content).toEqual(listAtIndex(state, 0).items[0].content);
    });
  });

  describe("break out of empty item", () => {
    const blankAt = (state: LetterStateV2, index: number) => {
      const paragraph = textBlockAt(state, index);
      expect(paragraph.type).toBe("PARAGRAPH");
      expect(isEmptyContent(paragraph.content)).toBe(true);
    };

    test("empty first item creates a blank block before the list", () => {
      const state = stateV2(list(item(literal("")), item(literal("item2")), item(literal("item3"))));
      const result = splitAt(state, itemPath(0, 0), 0, 0);
      expect(result.redigertBrev.blocks).toHaveLength(2);
      blankAt(result, 0);
      expect(listAtIndex(result, 1).items).toHaveLength(2);
      expect(listAtIndex(result, 1).id).toBe(listAtIndex(state, 0).id);
      expect(listAtIndex(result, 1).deletedItems).toEqual([listAtIndex(state, 0).items[0].id]);
      expect(result.focus).toEqual(caret(block(0)));
    });

    test("empty middle item splits the list with a blank block between", () => {
      const state = stateV2(list(item(literal("item1")), item(literal("")), item(literal("item3"))));
      const result = splitAt(state, itemPath(0, 1), 0, 0);
      expect(result.redigertBrev.blocks).toHaveLength(3);
      expect(listAtIndex(result, 0).items).toHaveLength(1);
      expect(listAtIndex(result, 0).id).toBe(listAtIndex(state, 0).id);
      expect(listAtIndex(result, 0).deletedItems).toEqual(
        expect.arrayContaining([listAtIndex(state, 0).items[1].id, listAtIndex(state, 0).items[2].id]),
      );
      blankAt(result, 1);
      expect(listAtIndex(result, 2).items.map((i) => i.id)).toEqual([listAtIndex(state, 0).items[2].id]);
      expect(result.focus).toEqual(caret(block(1)));
    });

    test("empty last item creates a blank block after the list", () => {
      const state = stateV2(list(item(literal("item1")), item(literal("item2")), item(literal(""))));
      const result = splitAt(state, itemPath(0, 2), 0, 0);
      expect(result.redigertBrev.blocks).toHaveLength(2);
      expect(listAtIndex(result, 0).items).toHaveLength(2);
      blankAt(result, 1);
      expect(result.focus).toEqual(caret(block(1)));
    });

    test("empty last item with following block", () => {
      const state = stateV2(list(item(literal("lit1")), item(literal(""))), paragraph(literal("after")));
      const result = splitAt(state, itemPath(0, 1), 0, 0);
      expect(result.redigertBrev.blocks).toHaveLength(3);
      blankAt(result, 1);
      expect(texts(textBlockAt(result, 2).content)).toEqual(["after"]);
    });

    test("only empty item replaces the list with a blank block", () => {
      const state = stateV2(list(item(literal(""))));
      const result = splitAt(state, itemPath(0, 0), 0, 0);
      expect(result.redigertBrev.blocks).toHaveLength(1);
      blankAt(result, 0);
      expect(result.redigertBrev.deletedBlocks).toEqual([listAtIndex(state, 0).id]);
      expect(result.focus).toEqual(caret(block(0)));
    });

    test("new empty last item breaks out", () => {
      const state = stateV2(
        list(
          item(literal("item1")),
          itemWith({ id: null, content: [newText("aa")] }),
          itemWith({ id: null, content: [newText("")] }),
        ),
      );
      const result = splitAt(state, itemPath(0, 2), 0, 0);
      expect(result.redigertBrev.blocks).toHaveLength(2);
      expect(listAtIndex(result, 0).items).toHaveLength(2);
      expect(listAtIndex(result, 0).deletedItems).toEqual([]);
    });

    test("preserves list type on both halves", () => {
      const state = stateV2(listOf("NUMBERED_LIST", [item(literal("a")), item(literal("")), item(literal("c"))]));
      const result = splitAt(state, itemPath(0, 1), 0, 0);
      expect(listAtIndex(result, 0).type).toBe("NUMBERED_LIST");
      expect(listAtIndex(result, 2).type).toBe("NUMBERED_LIST");
    });

    test("splitting again on the blank block is a no-op", () => {
      const state = stateV2(list(item(literal("item1")), item(literal(""))));
      const once = splitAt(state, itemPath(0, 1), 0, 0);
      expect(splitAt(once, block(1), 0, 0)).toBe(once);
    });

    test("Enter on a blank block between lists is a no-op", () => {
      const state = stateV2(list(item(literal("a"))), paragraph(literal("")), list(item(literal("b"))));
      expect(splitAt(state, block(1), 0, 0)).toBe(state);
    });
  });
});
