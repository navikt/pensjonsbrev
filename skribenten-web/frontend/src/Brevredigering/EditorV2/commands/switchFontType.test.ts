import { describe, expect, test } from "vitest";

import { type FontTypeV2, type LiteralV2, type TextV2 } from "~/types/brevV2";
import {
  cell,
  column,
  item,
  list,
  literal,
  newText,
  paragraph,
  row,
  stateV2,
  table,
  tableAtIndex,
  textBlockAt,
  texts,
  title2,
  variable,
} from "~test/support/editorV2TestUtils";

import { type ContainerPath, caret, resolve } from "../model/paths";
import { fontTypeOf } from "../model/text";
import { type LetterStateV2, run } from "../state/editorState";
import { switchFontType } from "./switchFontType";

const block: ContainerPath = { kind: "block", block: 0 };

function switchAt(state: LetterStateV2, focus: ReturnType<typeof caret>, fontType: FontTypeV2): LetterStateV2 {
  return run({ ...state, focus }, switchFontType, fontType);
}

function fonts(content: TextV2[]): FontTypeV2[] {
  return content.map(fontTypeOf);
}

function literalAt(content: TextV2[], index: number): LiteralV2 {
  const text = content[index];
  if (text?.type !== "LITERAL") throw new Error(`Content ${index} is not a literal`);
  return text;
}

describe("switchFontType", () => {
  test("switches font of the whole literal when it is a single word", () => {
    const state = stateV2(paragraph(literal("ord")));
    const result = switchAt(state, caret(block, 0, 1), "BOLD");

    const content = textBlockAt(result, 0).content;
    expect(texts(content)).toEqual(["ord"]);
    expect(literalAt(content, 0).fontType).toBe("PLAIN");
    expect(literalAt(content, 0).editedFontType).toBe("BOLD");
    expect(literalAt(content, 0).id).toBe(literalAt(textBlockAt(state, 0).content, 0).id);
    expect(textBlockAt(result, 0).deletedContent).toEqual([]);
    expect(result.focus).toEqual(caret(block, 0, 1));
  });

  test("toggles back to plain when the literal already has the font", () => {
    const state = stateV2(paragraph(literal("ord")));
    const result = switchAt(switchAt(state, caret(block, 0, 1), "BOLD"), caret(block, 0, 1), "BOLD");

    expect(literalAt(textBlockAt(result, 0).content, 0).editedFontType).toBeNull();
  });

  test("switches font of the word in the middle of a literal", () => {
    const state = stateV2(paragraph(literal("en fin dag")));
    const result = switchAt(state, caret(block, 0, 4), "ITALIC");

    const content = textBlockAt(result, 0).content;
    expect(texts(content)).toEqual(["en ", "fin", " dag"]);
    expect(fonts(content)).toEqual(["PLAIN", "ITALIC", "PLAIN"]);
    expect(content[0].id).toBe(textBlockAt(state, 0).content[0].id);
    expect(content.slice(1).map((text) => text.id)).toEqual([null, null]);
    expect(result.focus).toEqual(caret(block, 1, 1));
  });

  test("switches font of the first word and keeps the original literal for it", () => {
    const state = stateV2(paragraph(literal("første ord")));
    const result = switchAt(state, caret(block, 0, 2), "BOLD");

    const content = textBlockAt(result, 0).content;
    expect(texts(content)).toEqual(["første", " ord"]);
    expect(fonts(content)).toEqual(["BOLD", "PLAIN"]);
    expect(literalAt(content, 0).fontType).toBe("PLAIN");
    expect(result.focus).toEqual(caret(block, 0, 2));
  });

  test("switches font of the last word", () => {
    const state = stateV2(paragraph(literal("siste ord")));
    const result = switchAt(state, caret(block, 0, 9), "BOLD");

    const content = textBlockAt(result, 0).content;
    expect(texts(content)).toEqual(["siste ", "ord"]);
    expect(fonts(content)).toEqual(["PLAIN", "BOLD"]);
    expect(result.focus).toEqual(caret(block, 1, 3));
  });

  test("keeps the font of the surrounding text when unbolding a word", () => {
    const state = stateV2(paragraph(literal({ text: "alt er fett", fontType: "BOLD" })));
    const result = switchAt(state, caret(block, 0, 4), "BOLD");

    expect(fonts(textBlockAt(result, 0).content)).toEqual(["BOLD", "PLAIN", "BOLD"]);
  });

  test("switches font of an empty new literal so that new text gets the font", () => {
    const state = stateV2(paragraph(literal("tekst"), newText("")));
    const result = switchAt(state, caret(block, 1, 0), "BOLD");

    const content = textBlockAt(result, 0).content;
    expect(content).toHaveLength(2);
    expect(fontTypeOf(content[1])).toBe("BOLD");
    expect(literalAt(content, 1).editedText).toBe("");
  });

  test("does nothing when the caret is at a space", () => {
    const state = stateV2(paragraph(literal("to ord")));

    expect(switchAt(state, caret(block, 0, 2), "BOLD").redigertBrev).toBe(state.redigertBrev);
  });

  test("does nothing on a variable", () => {
    const state = stateV2(paragraph(variable("variabel")));

    expect(switchAt(state, caret(block, 0, 1), "BOLD").redigertBrev).toBe(state.redigertBrev);
  });

  test("does nothing in titles", () => {
    const state = stateV2(title2(literal("tittel")));

    expect(switchAt(state, caret(block, 0, 1), "BOLD").redigertBrev).toBe(state.redigertBrev);
    expect(switchAt(state, caret({ kind: "title" }, 0, 1), "BOLD").redigertBrev).toBe(state.redigertBrev);
  });

  test("switches font in list items", () => {
    const state = stateV2(list(item(literal("punkt"))));
    const path: ContainerPath = { kind: "item", block: 0, item: 0 };
    const result = switchAt(state, caret(path, 0, 1), "BOLD");

    expect(fonts(resolve(result.redigertBrev, path)?.content ?? [])).toEqual(["BOLD"]);
  });

  test("switches font in table header and cells", () => {
    const state = stateV2(table([column(literal("Overskrift"))], [row(cell(literal("Celle")))]));
    const header: ContainerPath = { kind: "header", block: 0, column: 0 };
    const body: ContainerPath = { kind: "cell", block: 0, row: 0, cell: 0 };
    const result = switchAt(switchAt(state, caret(header, 0, 0), "BOLD"), caret(body, 0, 0), "ITALIC");

    const resultTable = tableAtIndex(result, 0);
    expect(fonts(resultTable.header.colSpec[0].content)).toEqual(["BOLD"]);
    expect(fonts(resultTable.rows[0].cells[0].content)).toEqual(["ITALIC"]);
  });
});
