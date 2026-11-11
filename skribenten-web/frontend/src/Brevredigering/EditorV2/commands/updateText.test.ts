import { describe, expect, test } from "vitest";

import { literal, paragraph, stateV2, textBlockAt, variable } from "~test/support/editorV2TestUtils";

import { type ContainerPath, caret } from "../model/paths";
import { run } from "../state/editorState";
import { moveFocus, updateText } from "./updateText";

const block: ContainerPath = { kind: "block", block: 0 };
const state = stateV2(paragraph(literal("heisann"), variable("var"), literal("hello")));

describe("updateText", () => {
  test("text is updated and focus set", () => {
    const at = caret(block, 0, 12);
    const result = run(state, updateText, at, "Heisann joda");
    expect(textBlockAt(result, 0).content[0]).toMatchObject({ text: "heisann", editedText: "Heisann joda" });
    expect(result.focus).toEqual(at);
  });

  test("line break elements are removed", () => {
    const result = run(state, updateText, caret(block), "Hallo, <br>Håper alt er bra");
    expect(textBlockAt(result, 0).content[0]).toMatchObject({ editedText: "Hallo, Håper alt er bra" });
  });

  test("non-breaking space entities are replaced", () => {
    const result = run(state, updateText, caret(block), "Hallo&nbsp;hvordan går det");
    expect(textBlockAt(result, 0).content[0]).toMatchObject({ editedText: "Hallo hvordan går det" });
  });

  test("changing back to the original text sets editedText to null", () => {
    const edited = run(state, updateText, caret(block), "endret");
    const result = run(edited, updateText, caret(block), "heisann");
    expect(textBlockAt(result, 0).content[0]).toMatchObject({ editedText: null });
  });

  test("ignores variables", () => {
    expect(run(state, updateText, caret(block, 1), "x")).toBe(state);
  });
});

describe("moveFocus", () => {
  test("sets focus without changing the letter", () => {
    const at = caret(block, 2, 3);
    const result = run(state, moveFocus, at);
    expect(result.focus).toEqual(at);
    expect(result.redigertBrev).toBe(state.redigertBrev);
  });
});
