import { expect } from "vitest";

import { findAdjoiningContent } from "~/Brevredigering/LetterEditor/actions/common";
import { isTextContent } from "~/Brevredigering/LetterEditor/model/utils";
import type { Content, TextContent } from "~/types/brevbakerTypes";

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
