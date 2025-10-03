import { describe, expect, test } from "vitest";

import Actions from "~/Brevredigering/LetterEditor/actions";
import type { LiteralValue, TextContent } from "~/types/brevbakerTypes";

import { item, itemList, letter, literal, paragraph, select } from "../utils";

const state = letter(
  paragraph([literal({ text: "heisann" }), itemList({ items: [item(literal({ text: "punkt 1" }))] })]),
);
const contentIndex = { blockIndex: 0, contentIndex: 0 };
describe("updateContentText", () => {
  test("text is updated", () => {
    const resultText = "Heisann joda";
    const result = Actions.updateContentText(state, contentIndex, resultText, resultText.length);
    expect(select<TextContent>(result, contentIndex).text).toEqual("heisann");
    expect(select<LiteralValue>(result, contentIndex).editedText).toEqual(resultText);
  });

  test("line break elements are removed", () => {
    const rawText = "Hallo, <br>Håper alt er bra";
    const expectedText = "Hallo, Håper alt er bra";
    const result = Actions.updateContentText(state, contentIndex, rawText, expectedText.length);
    expect(select<LiteralValue>(result, contentIndex).editedText).toEqual(expectedText);
  });

  test("non-breaking space entities are replaced", () => {
    const rawText = "Hallo&nbsp;hvordan går det";
    const expectedText = "Hallo hvordan går det";
    const result = Actions.updateContentText(state, contentIndex, rawText, expectedText.length);
    expect(select<LiteralValue>(result, contentIndex).editedText).toEqual(expectedText);
  });

  test("changing back to original text sets editedText to null", () => {
    const editedState = letter(paragraph([literal({ text: "heisann" }), literal({ text: "hello" })]));
    const original = "heisann";
    const result = Actions.updateContentText(
      editedState,
      { blockIndex: 0, contentIndex: 0 },
      original,
      original.length,
    );
    expect(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }).editedText).toBeNull();
  });
});
