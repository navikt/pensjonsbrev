import { type Draft, produce } from "immer";
import { describe, expect, test } from "vitest";

import {
  absorbListIntoList,
  buildMergedItemList,
  coalesceAdjacentSameTypeLists,
  findAdjoiningContent,
  removeElements,
  text,
} from "~/Brevredigering/LetterEditor/actions/common";
import { isTextContent } from "~/Brevredigering/LetterEditor/model/utils";
import {
  type Content,
  type Identifiable,
  type ItemList,
  ListType,
  type LiteralValue,
  type TextContent,
} from "~/types/brevbakerTypes";

import { item, itemList, letter, literal, paragraph } from "../utils";

describe("findAdjoiningContent", () => {
  describe("entire array matches", () => {
    const arr: TextContent[] = [
      literal({ text: "a" }),
      literal({ text: "b" }),
      literal({ text: "c" }),
      literal({ text: "d" }),
    ];
    const expectedSentence = { startIndex: 0, endIndex: 3, count: 4 };

    test("atIndex is first", () => {
      expect(findAdjoiningContent(0, arr, isTextContent)).toEqual(expectedSentence);
    });
    test("atIndex is last", () => {
      expect(findAdjoiningContent(3, arr, isTextContent)).toEqual(expectedSentence);
    });
    test("in the middle", () => {
      expect(findAdjoiningContent(2, arr, isTextContent)).toEqual(expectedSentence);
    });
  });

  describe("content before does not match", () => {
    const arr: Content[] = [
      itemList({ items: [] }),
      literal({ text: "a" }),
      literal({ text: "b" }),
      literal({ text: "c" }),
      literal({ text: "d" }),
    ];
    const expectedSentence = { startIndex: 1, endIndex: 4, count: 4 };

    test("atIndex is first", () => {
      expect(findAdjoiningContent(1, arr, isTextContent)).toEqual(expectedSentence);
    });
    test("atIndex is last", () => {
      expect(findAdjoiningContent(4, arr, isTextContent)).toEqual(expectedSentence);
    });
    test("in the middle", () => {
      expect(findAdjoiningContent(2, arr, isTextContent)).toEqual(expectedSentence);
    });
  });

  describe("content after does not match", () => {
    const arr: Content[] = [
      literal({ text: "a" }),
      literal({ text: "b" }),
      literal({ text: "c" }),
      literal({ text: "d" }),
      itemList({ items: [] }),
    ];
    const expectedSentence = { startIndex: 0, endIndex: 3, count: 4 };

    test("atIndex is first", () => {
      expect(findAdjoiningContent(0, arr, isTextContent)).toEqual(expectedSentence);
    });
    test("atIndex is last", () => {
      expect(findAdjoiningContent(3, arr, isTextContent)).toEqual(expectedSentence);
    });
    test("in the middle", () => {
      expect(findAdjoiningContent(2, arr, isTextContent)).toEqual(expectedSentence);
    });
  });

  describe("content before and after does not match", () => {
    const arr: Content[] = [
      itemList({ items: [] }),
      literal({ text: "a" }),
      literal({ text: "b" }),
      literal({ text: "c" }),
      literal({ text: "d" }),
      itemList({ items: [] }),
    ];
    const expectedSentence = { startIndex: 1, endIndex: 4, count: 4 };

    test("atIndex is first", () => {
      expect(findAdjoiningContent(1, arr, isTextContent)).toEqual(expectedSentence);
    });
    test("atIndex is last", () => {
      expect(findAdjoiningContent(4, arr, isTextContent)).toEqual(expectedSentence);
    });
    test("in the middle", () => {
      expect(findAdjoiningContent(2, arr, isTextContent)).toEqual(expectedSentence);
    });
  });

  test("only one element", () => {
    const arr: Content[] = [literal({ text: "a" })];
    expect(findAdjoiningContent(0, arr, isTextContent)).toEqual({ startIndex: 0, endIndex: 0, count: 1 });
  });
  test("empty array", () => {
    expect(findAdjoiningContent(0, [] as Content[], isTextContent)).toEqual({ startIndex: 0, endIndex: 0, count: 0 });
  });
});

const id = (c: Identifiable) => c.id;

describe("removeElements", () => {
  type RemoveElementsState<T extends Identifiable> = { content: T[]; deletedContent: number[]; id: number | null };
  type RemoveElementsProducer<T extends Identifiable> = (
    state: RemoveElementsState<T>,
    startIndex: number,
    count: number,
  ) => RemoveElementsState<T>;

  const removeElementsProducer: RemoveElementsProducer<LiteralValue> = produce((draft, startIndex, count) => {
    removeElements(startIndex, count, draft);
  });

  const content = [literal({ text: "l1" }), literal({ text: "l2" }), literal({ text: "l3" }), literal({ text: "l4" })];

  test("removed elements at beginning are marked as deleted", () => {
    const result = removeElementsProducer({ content, id: null, deletedContent: [] }, 0, 2);

    expect(result.deletedContent).toEqual(content.slice(0, 2).map(id));
    expect(result.content).toEqual(content.slice(2));
  });

  test("removed elements in the middle are marked as deleted", () => {
    const result = removeElementsProducer({ content, id: null, deletedContent: [] }, 1, 2);

    expect(result.deletedContent).toEqual(content.slice(1, 3).map(id));
    expect(result.content).toEqual([...content.slice(0, 1), ...content.slice(3)]);
  });

  test("removed elements at the end are marked as deleted", () => {
    const result = removeElementsProducer({ content, id: null, deletedContent: [] }, 3, 2);

    expect(result.deletedContent).toEqual(content.slice(3).map(id));
    expect(result.content).toEqual(content.slice(0, 3));
  });

  test("remove all elements and they are marked as deleted", () => {
    const result = removeElementsProducer({ content, id: null, deletedContent: [] }, 0, 4);

    expect(result.deletedContent).toEqual(content.map(id));
    expect(result.content).toEqual([]);
  });

  test("removing elements retains previously deleted", () => {
    const result = removeElementsProducer({ content, id: null, deletedContent: [-1, -2] }, 0, 4);

    expect(result.deletedContent).toContain(-1);
    expect(result.deletedContent).toContain(-2);
    for (const c of content) expect(result.deletedContent).toContain(c.id);
    expect(result.content).toEqual([]);
  });

  test("removed elements that originally has another parent are not marked as deleted", () => {
    const otherParent = [
      literal({ parentId: -1, text: "l1" }),
      literal({ parentId: 1, text: "l2" }),
      literal({ parentId: 1, text: "l3" }),
      literal({ parentId: 1, text: "l4" }),
    ];
    const result = removeElementsProducer({ content: otherParent, deletedContent: [], id: 1 }, 0, 3);
    expect(result.deletedContent).toEqual(otherParent.slice(1, 3).map(id));
  });
});

const literalText = (list: ItemList, itemIndex: number) => text(list.items[itemIndex].content[0]);

describe("buildMergedItemList", () => {
  test("flattens items from all lists in order", () => {
    const merged = buildMergedItemList(
      [itemList({ items: [item(literal("a")), item(literal("b"))] }), itemList({ items: [item(literal("c"))] })],
      ListType.PUNKTLISTE,
    );
    expect(merged.items.map((_, i) => literalText(merged, i))).toEqual(["a", "b", "c"]);
  });

  test("preserves the id of the first list with a non-null id", () => {
    const nullIdList: ItemList = { ...itemList({ items: [item(literal("a"))] }), id: null };
    const merged = buildMergedItemList(
      [nullIdList, itemList({ id: 42, items: [item(literal("b"))] })],
      ListType.PUNKTLISTE,
    );
    expect(merged.id).toBe(42);
  });

  test("falls back to the last list's id when all ids are null", () => {
    const first: ItemList = { ...itemList({ items: [item(literal("a"))] }), id: null };
    const last: ItemList = { ...itemList({ items: [item(literal("b"))] }), id: null };
    const merged = buildMergedItemList([first, last], ListType.PUNKTLISTE);
    expect(merged.id).toBeNull();
  });

  test("collects deletedItems from all lists", () => {
    const merged = buildMergedItemList(
      [
        itemList({ items: [item(literal("a"))], deletedItems: [1, 2] }),
        itemList({ items: [item(literal("b"))], deletedItems: [3] }),
      ],
      ListType.PUNKTLISTE,
    );
    expect(merged.deletedItems).toEqual([1, 2, 3]);
  });

  test("sets editedListType when target differs from the surviving list's listType", () => {
    const merged = buildMergedItemList(
      [itemList({ id: 7, listType: ListType.PUNKTLISTE, items: [item(literal("a"))] })],
      ListType.NUMMERERT_LISTE,
    );
    expect(merged.editedListType).toBe(ListType.NUMMERERT_LISTE);
  });

  test("leaves editedListType null when target equals the surviving list's listType", () => {
    const merged = buildMergedItemList(
      [itemList({ id: 7, listType: ListType.PUNKTLISTE, editedListType: null, items: [item(literal("a"))] })],
      ListType.PUNKTLISTE,
    );
    expect(merged.editedListType).toBeNull();
  });
});

describe("coalesceAdjacentSameTypeLists", () => {
  test("merges adjacent same-type lists in a block into one", () => {
    const state = letter(
      paragraph([itemList({ items: [item(literal("a"))] }), itemList({ items: [item(literal("b"))] })]),
    );
    let ret: { newContentIndex: number; itemIndexOffset: number } | undefined;
    const result = produce(state, (draft) => {
      ret = coalesceAdjacentSameTypeLists(draft, 0, 0, ListType.PUNKTLISTE);
    });
    const block = result.redigertBrev.blocks[0];
    expect(block.content).toHaveLength(1);
    const merged = block.content[0] as ItemList;
    expect(merged.items.map((_, i) => literalText(merged, i))).toEqual(["a", "b"]);
    expect(ret).toEqual({ newContentIndex: 0, itemIndexOffset: 0 });
  });

  test("returns itemIndexOffset counting items in lists before the anchor", () => {
    const state = letter(
      paragraph([
        itemList({ items: [item(literal("a")), item(literal("b"))] }),
        itemList({ items: [item(literal("c"))] }),
      ]),
    );
    let ret: { newContentIndex: number; itemIndexOffset: number } | undefined;
    produce(state, (draft) => {
      ret = coalesceAdjacentSameTypeLists(draft, 0, 1, ListType.PUNKTLISTE);
    });
    expect(ret).toEqual({ newContentIndex: 0, itemIndexOffset: 2 });
  });

  test("is a no-op when only one same-type list adjoins the anchor", () => {
    const state = letter(paragraph([itemList({ items: [item(literal("a"))] })]));
    let ret: { newContentIndex: number; itemIndexOffset: number } | undefined;
    const result = produce(state, (draft) => {
      ret = coalesceAdjacentSameTypeLists(draft, 0, 0, ListType.PUNKTLISTE);
    });
    expect(result.redigertBrev.blocks[0].content).toHaveLength(1);
    expect(ret).toEqual({ newContentIndex: 0, itemIndexOffset: 0 });
  });

  test("does not merge lists of different types", () => {
    const state = letter(
      paragraph([
        itemList({ listType: ListType.PUNKTLISTE, items: [item(literal("a"))] }),
        itemList({ listType: ListType.NUMMERERT_LISTE, items: [item(literal("b"))] }),
      ]),
    );
    const result = produce(state, (draft) => {
      coalesceAdjacentSameTypeLists(draft, 0, 0, ListType.PUNKTLISTE);
    });
    expect(result.redigertBrev.blocks[0].content).toHaveLength(2);
  });
});

describe("absorbListIntoList", () => {
  test("back: appends source items to target and removes the emptied source block", () => {
    const state = letter(
      paragraph([itemList({ items: [item(literal("a"))] })]),
      paragraph([itemList({ items: [item(literal("b")), item(literal("c"))] })]),
    );
    let ret: { movedItemCount: number; sourceBlockRemoved: boolean } | undefined;
    const result = produce(state, (draft) => {
      const target = draft.redigertBrev.blocks[0].content[0] as Draft<ItemList>;
      ret = absorbListIntoList(draft, target, 1, 0, "back");
    });
    expect(result.redigertBrev.blocks).toHaveLength(1);
    const merged = result.redigertBrev.blocks[0].content[0] as ItemList;
    expect(merged.items.map((_, i) => literalText(merged, i))).toEqual(["a", "b", "c"]);
    expect(ret).toEqual({ movedItemCount: 2, sourceBlockRemoved: true });
  });

  test("front: prepends source items to target", () => {
    const state = letter(
      paragraph([itemList({ items: [item(literal("x"))] })]),
      paragraph([itemList({ items: [item(literal("y"))] })]),
    );
    let ret: { movedItemCount: number; sourceBlockRemoved: boolean } | undefined;
    const result = produce(state, (draft) => {
      const target = draft.redigertBrev.blocks[1].content[0] as Draft<ItemList>;
      ret = absorbListIntoList(draft, target, 0, 0, "front");
    });
    expect(result.redigertBrev.blocks).toHaveLength(1);
    const merged = result.redigertBrev.blocks[0].content[0] as ItemList;
    expect(merged.items.map((_, i) => literalText(merged, i))).toEqual(["x", "y"]);
    expect(ret).toEqual({ movedItemCount: 1, sourceBlockRemoved: true });
  });

  test("carries the source list's deletedItems into the target", () => {
    const state = letter(
      paragraph([itemList({ items: [item(literal("a"))], deletedItems: [10] })]),
      paragraph([itemList({ items: [item(literal("b"))], deletedItems: [20] })]),
    );
    const result = produce(state, (draft) => {
      const target = draft.redigertBrev.blocks[0].content[0] as Draft<ItemList>;
      absorbListIntoList(draft, target, 1, 0, "back");
    });
    expect((result.redigertBrev.blocks[0].content[0] as ItemList).deletedItems).toEqual([10, 20]);
  });

  test("keeps the source block when it still has other content", () => {
    const state = letter(
      paragraph([itemList({ items: [item(literal("a"))] })]),
      paragraph([itemList({ items: [item(literal("b"))] }), literal("tail")]),
    );
    let ret: { movedItemCount: number; sourceBlockRemoved: boolean } | undefined;
    const result = produce(state, (draft) => {
      const target = draft.redigertBrev.blocks[0].content[0] as Draft<ItemList>;
      ret = absorbListIntoList(draft, target, 1, 0, "back");
    });
    expect(result.redigertBrev.blocks).toHaveLength(2);
    expect(ret?.sourceBlockRemoved).toBe(false);
    const tail = result.redigertBrev.blocks[1].content;
    expect(tail).toHaveLength(1);
    expect(text(tail[0] as LiteralValue)).toBe("tail");
  });
});
