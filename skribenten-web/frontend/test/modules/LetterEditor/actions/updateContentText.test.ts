import { describe, expect, test } from "vitest";

import Actions from "~/Brevredigering/LetterEditor/actions";
import type { Content, TextContent } from "~/types/brevbakerTypes";
import { LITERAL, PARAGRAPH } from "~/types/brevbakerTypes";

import { letter, select } from "../utils";

const content: Content = {
  type: LITERAL,
  id: 1,
  text: "heisann",
};

const state = letter({ id: 1, type: PARAGRAPH, editable: true, content: [content] });
const contentIndex = { blockIndex: 0, contentIndex: 0 };

describe("updateContentText", () => {
  test("text is updated", () => {
    const result = Actions.updateContentText(state, contentIndex, "Heisann joda");
    expect(select<TextContent>(result, contentIndex).text).toEqual("Heisann joda");
  });
  test("line break elements are removed", () => {
    const result = Actions.updateContentText(state, contentIndex, "Hallo, <br>Håper alt er bra");
    expect(select<TextContent>(result, contentIndex).text).toEqual("Hallo, Håper alt er bra");
  });
  test("non-breaking space entities are replaced", () => {
    const result = Actions.updateContentText(state, contentIndex, "Hallo&nbsp;hvordan går det");
    expect(select<TextContent>(result, contentIndex).text).toEqual("Hallo hvordan går det");
  });
});
