import { describe, expect, test } from "vitest";

import Actions from "~/Brevredigering/LetterEditor/actions";
import type { LiteralValue, TextContent } from "~/types/brevbakerTypes";

import { item, itemList, letter, literal, paragraph, select } from "../utils";

const state = letter(
  paragraph(literal({ text: "heisann" }), itemList({ items: [item(literal({ text: "punkt 1" }))] })),
);
const contentIndex = { blockIndex: 0, contentIndex: 0 };
describe("updateContentText", () => {
  test("text is updated", () => {
    const result = Actions.updateContentText(state, contentIndex, "Heisann joda");
    expect(select<TextContent>(result, contentIndex).text).toEqual("heisann");
    expect(select<LiteralValue>(result, contentIndex).editedText).toEqual("Heisann joda");
  });

  test("line break elements are removed", () => {
    const result = Actions.updateContentText(state, contentIndex, "Hallo, <br>Håper alt er bra");
    expect(select<LiteralValue>(result, contentIndex).editedText).toEqual("Hallo, Håper alt er bra");
  });

  test("non-breaking space entities are replaced", () => {
    const result = Actions.updateContentText(state, contentIndex, "Hallo&nbsp;hvordan går det");
    expect(select<LiteralValue>(result, contentIndex).editedText).toEqual("Hallo hvordan går det");
  });

  test("changing back to original text sets editedText to null", () => {
    const editedState = letter(paragraph(literal({ text: "heisann" }), literal({ text: "hello" })));
    const result = Actions.updateContentText(editedState, { blockIndex: 0, contentIndex: 0 }, "heisann");
    expect(select<LiteralValue>(result, { blockIndex: 0, contentIndex: 0 }).editedText).toBeNull();
  });
});
