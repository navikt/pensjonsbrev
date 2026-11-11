import { describe, expect, test } from "vitest";

import { type TextBlockTypeV2 } from "~/types/brevV2";
import {
  item,
  list,
  literal,
  paragraph,
  paragraphWith,
  stateV2,
  textBlockAt,
  title2,
  title3,
} from "~test/support/editorV2TestUtils";

import { type ContainerPath, caret } from "../model/paths";
import { type LetterStateV2, run } from "../state/editorState";
import { switchTypography } from "./switchTypography";

const first: ContainerPath = { kind: "block", block: 0 };

function switchAt(state: LetterStateV2, type: TextBlockTypeV2, path = first): LetterStateV2 {
  return run({ ...state, focus: caret(path) }, switchTypography, type);
}

describe("switchTypography", () => {
  test.each<[string, () => LetterStateV2, TextBlockTypeV2, string]>([
    ["paragraph to title2", () => stateV2(paragraph(literal("tekst"))), "TITLE2", "PARAGRAPH"],
    ["paragraph to title3", () => stateV2(paragraph(literal("tekst"))), "TITLE3", "PARAGRAPH"],
    ["title2 to paragraph", () => stateV2(title2(literal("tekst"))), "PARAGRAPH", "TITLE2"],
    ["title2 to title3", () => stateV2(title2(literal("tekst"))), "TITLE3", "TITLE2"],
    ["title3 to title2", () => stateV2(title3(literal("tekst"))), "TITLE2", "TITLE3"],
    ["title3 to paragraph", () => stateV2(title3(literal("tekst"))), "PARAGRAPH", "TITLE3"],
  ])("can switch from %s", (_, create, type, originalType) => {
    const result = switchAt(create(), type);

    expect(textBlockAt(result, 0).type).toBe(type);
    expect(textBlockAt(result, 0).originalType).toBe(originalType);
    expect(result.saveStatus).toBe("DIRTY");
  });

  test("originalType is not replaced when switching a second time", () => {
    const result = switchAt(switchAt(stateV2(title3(literal("tekst"))), "PARAGRAPH"), "TITLE2");

    expect(textBlockAt(result, 0).originalType).toBe("TITLE3");
  });

  test("originalType is cleared when switching back", () => {
    const result = switchAt(switchAt(stateV2(title3(literal("tekst"))), "PARAGRAPH"), "TITLE3");

    expect(textBlockAt(result, 0).type).toBe("TITLE3");
    expect(textBlockAt(result, 0).originalType).toBeNull();
  });

  test("does not set originalType for new blocks", () => {
    const result = switchAt(stateV2(paragraphWith({ id: null, content: [literal("tekst")] })), "TITLE2");

    expect(textBlockAt(result, 0).type).toBe("TITLE2");
    expect(textBlockAt(result, 0).originalType).toBeUndefined();
  });

  test("switching to the current type does nothing", () => {
    const state = stateV2(paragraph(literal("tekst")));

    expect(switchAt(state, "PARAGRAPH").redigertBrev).toBe(state.redigertBrev);
  });

  test("does nothing when focus is in a list item", () => {
    const state = stateV2(list(item(literal("punkt"))));

    expect(switchAt(state, "TITLE2", { kind: "item", block: 0, item: 0 }).redigertBrev).toBe(state.redigertBrev);
  });
});
