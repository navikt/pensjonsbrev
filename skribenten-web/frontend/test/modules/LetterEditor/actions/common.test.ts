import { produce } from "immer";
import { expect } from "vitest";

import { findAdjoiningContent, removeElements } from "~/Brevredigering/LetterEditor/actions/common";
import { isTextContent } from "~/Brevredigering/LetterEditor/model/utils";
import type { Content, Identifiable, LiteralValue, TextContent } from "~/types/brevbakerTypes";

import { itemList, literal } from "../utils";

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
