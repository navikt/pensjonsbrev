import { describe, expect, test } from "vitest";

import { type ListBlockTypeV2 } from "~/types/brevV2";
import {
  item,
  itemWith,
  list,
  listAtIndex,
  listOf,
  literal,
  numberedList,
  paragraph,
  paragraphWith,
  stateV2,
  textBlockAt,
  texts,
  title2,
  variable,
} from "~test/support/editorV2TestUtils";

import { type Caret, caret } from "../model/paths";
import { type LetterStateV2, run } from "../state/editorState";
import { toggleList } from "./toggleList";

function toggleAt(state: LetterStateV2, focus: Caret, type: ListBlockTypeV2 = "ITEM_LIST"): LetterStateV2 {
  return run({ ...state, focus }, toggleList, type);
}

const inBlock = (block: number, content = 0, offset = 0) => caret({ kind: "block", block }, content, offset);
const inItem = (block: number, itemIndex: number, content = 0, offset = 0) =>
  caret({ kind: "item", block, item: itemIndex }, content, offset);

describe("toggleList on a paragraph", () => {
  test("converts a paragraph to a numbered list", () => {
    const state = stateV2(paragraph(literal("tekst")));
    const result = toggleAt(state, inBlock(0), "NUMBERED_LIST");

    const resultList = listAtIndex(result, 0);
    expect(resultList.type).toBe("NUMBERED_LIST");
    expect(resultList.id).toBeNull();
    expect(resultList.items).toHaveLength(1);
    expect(resultList.items[0].content).toEqual(textBlockAt(state, 0).content);
    expect(result.redigertBrev.deletedBlocks).toEqual([textBlockAt(state, 0).id]);
    expect(result.saveStatus).toBe("DIRTY");
  });

  test("keeps the caret position in the new item", () => {
    const state = stateV2(paragraph(variable("v"), literal("tekst")));
    const result = toggleAt(state, inBlock(0, 1, 3));

    expect(result.focus).toEqual(inItem(0, 0, 1, 3));
  });

  test("toggling again on the same item removes the list", () => {
    const state = stateV2(paragraph(literal("tekst")));
    const result = toggleAt(toggleAt(state, inBlock(0, 0, 2), "NUMBERED_LIST"), inItem(0, 0, 0, 2), "NUMBERED_LIST");

    expect(result.redigertBrev.blocks).toHaveLength(1);
    expect(texts(textBlockAt(result, 0).content)).toEqual(["tekst"]);
    expect(result.focus).toEqual(inBlock(0, 0, 2));
  });

  test("joins with a same-type list before and after", () => {
    const before = list(item(literal("før")));
    const after = list(item(literal("etter")));
    const state = stateV2(before, paragraph(literal("midt")), after);
    const result = toggleAt(state, inBlock(1, 0, 2));

    expect(result.redigertBrev.blocks).toHaveLength(1);
    const joined = listAtIndex(result, 0);
    expect(joined.id).toBe(before.id);
    expect(joined.items.map((joinedItem) => texts(joinedItem.content))).toEqual([["før"], ["midt"], ["etter"]]);
    expect(result.focus).toEqual(inItem(0, 1, 0, 2));
  });

  test("does not join with lists of another type", () => {
    const state = stateV2(
      numberedList(item(literal("før"))),
      paragraph(literal("midt")),
      numberedList(item(literal("etter"))),
    );
    const result = toggleAt(state, inBlock(1));

    expect(result.redigertBrev.blocks.map((block) => block.type)).toEqual([
      "NUMBERED_LIST",
      "ITEM_LIST",
      "NUMBERED_LIST",
    ]);
  });

  test("does not join other lists than the neighbours of the toggled paragraph", () => {
    const state = stateV2(
      list(item(literal("punktliste1"))),
      paragraph(literal("l1")),
      list(item(literal("punktliste2"))),
      paragraph(literal("l2")),
    );
    const result = toggleAt(state, inBlock(3));

    expect(result.redigertBrev.blocks).toHaveLength(3);
    expect(listAtIndex(result, 0)).toEqual(state.redigertBrev.blocks[0]);
    expect(listAtIndex(result, 2).items.map((joinedItem) => texts(joinedItem.content))).toEqual([
      ["punktliste2"],
      ["l2"],
    ]);
  });

  test("keeps previous deletedItems of the list it joins", () => {
    const state = stateV2(listOf("ITEM_LIST", [item(literal("l1"))], { deletedItems: [-1] }), paragraph(literal("l2")));
    const result = toggleAt(state, inBlock(1));

    expect(listAtIndex(result, 0).deletedItems).toEqual([-1]);
  });

  test("keeps previously deleted blocks", () => {
    const state = stateV2(paragraphWith({ id: 1, content: [literal({ id: 11, parentId: 1, text: "l1" })] }));
    const result = toggleAt({ ...state, redigertBrev: { ...state.redigertBrev, deletedBlocks: [-1] } }, inBlock(0));

    expect(result.redigertBrev.deletedBlocks).toEqual([-1, 1]);
  });

  test("does nothing in titles", () => {
    const state = stateV2(title2(literal("tittel")));

    expect(toggleAt(state, inBlock(0)).redigertBrev).toBe(state.redigertBrev);
  });
});

describe("toggleList on an item of the same list type", () => {
  const threeItems = () =>
    stateV2(
      listOf(
        "ITEM_LIST",
        [item(literal("p1")), item(literal("p2-l1"), variable("v"), literal("p2-l2")), item(literal("p3"))],
        {
          id: -1,
          deletedItems: [-2],
        },
      ),
    );

  test("middle item: list keeps first half, paragraph and second half follow", () => {
    const state = threeItems();
    const original = listAtIndex(state, 0);
    const result = toggleAt(state, inItem(0, 1, 2, 1));

    expect(result.redigertBrev.blocks.map((block) => block.type)).toEqual(["ITEM_LIST", "PARAGRAPH", "ITEM_LIST"]);
    const kept = listAtIndex(result, 0);
    expect(kept.id).toBe(-1);
    expect(kept.items).toHaveLength(1);
    expect(kept.deletedItems).toEqual([-2, ...original.items.slice(1).map((removed) => removed.id)]);
    expect(textBlockAt(result, 1).content).toEqual(original.items[1].content);
    expect(listAtIndex(result, 2).items).toEqual([original.items[2]]);
    expect(result.focus).toEqual(inBlock(1, 2, 1));
  });

  test("last item: item is deleted from the list and placed in a paragraph after it", () => {
    const state = stateV2(list(item(literal("p1")), item(literal("p2"))));
    const result = toggleAt(state, inItem(0, 1, 0, 1));

    expect(listAtIndex(result, 0).deletedItems).toEqual([listAtIndex(state, 0).items[1].id]);
    expect(texts(textBlockAt(result, 1).content)).toEqual(["p2"]);
    expect(result.focus).toEqual(inBlock(1, 0, 1));
  });

  test("first item: paragraph is placed before the remaining list", () => {
    const state = stateV2(list(item(literal("p1")), item(literal("p2"))));
    const result = toggleAt(state, inItem(0, 0));

    expect(texts(textBlockAt(result, 0).content)).toEqual(["p1"]);
    expect(listAtIndex(result, 1).items.map((remaining) => texts(remaining.content))).toEqual([["p2"]]);
    expect(result.focus).toEqual(inBlock(0));
  });

  test("only item: the list is deleted and replaced by a paragraph", () => {
    const state = stateV2(list(itemWith({ content: [literal("p1"), variable("v"), literal("p1-l2")] })));
    const result = toggleAt(state, inItem(0, 0, 2, 3));

    expect(result.redigertBrev.blocks).toHaveLength(1);
    expect(textBlockAt(result, 0).content).toEqual(listAtIndex(state, 0).items[0].content);
    expect(result.redigertBrev.deletedBlocks).toEqual([listAtIndex(state, 0).id]);
    expect(result.focus).toEqual(inBlock(0, 2, 3));
  });
});

describe("toggleList on an item of another list type", () => {
  test("switching a bullet list to numbered sets originalType", () => {
    const state = stateV2(list(item(literal("a"))));
    const result = toggleAt(state, inItem(0, 0, 0, 1), "NUMBERED_LIST");

    const switched = listAtIndex(result, 0);
    expect(switched.type).toBe("NUMBERED_LIST");
    expect(switched.originalType).toBe("ITEM_LIST");
    expect(switched.id).toBe(listAtIndex(state, 0).id);
    expect(result.focus).toEqual(inItem(0, 0, 0, 1));
  });

  test("switching back to the original type clears originalType", () => {
    const state = stateV2(numberedList(item(literal("a"))));
    const result = toggleAt(toggleAt(state, inItem(0, 0), "ITEM_LIST"), inItem(0, 0), "NUMBERED_LIST");

    expect(listAtIndex(result, 0).type).toBe("NUMBERED_LIST");
    expect(listAtIndex(result, 0).originalType).toBeNull();
  });

  test("after switching type, an adjacent list with the new type is joined", () => {
    const state = stateV2(list(item(literal("a"))), numberedList(item(literal("b"))));
    const result = toggleAt(state, inItem(0, 0), "NUMBERED_LIST");

    expect(result.redigertBrev.blocks).toHaveLength(1);
    expect(listAtIndex(result, 0).items.map((joined) => texts(joined.content))).toEqual([["a"], ["b"]]);
    expect(result.focus).toEqual(inItem(0, 0));
  });

  test("focus follows the item when it is joined into a previous list", () => {
    const state = stateV2(numberedList(item(literal("a"))), list(item(literal("b"))));
    const result = toggleAt(state, inItem(1, 0, 0, 1), "NUMBERED_LIST");

    expect(result.redigertBrev.blocks).toHaveLength(1);
    expect(result.focus).toEqual(inItem(0, 1, 0, 1));
  });
});
