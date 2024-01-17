import { describe, expect, test, vi } from "vitest";

import Actions from "~/pages/Brevredigering/LetterEditor/actions";

import { letter, literal, paragraph, variable } from "../utils";

function getKeyboardEvent(key: string) {
  return {
    key,
    preventDefault: vi.fn(),
  };
}

describe("LetterEditorActions.cursor", () => {
  describe("pressing ArrowLeft", () => {
    test("at beginning of document does nothing", () => {
      const keyboardEvent = getKeyboardEvent("ArrowLeft");
      const state = letter(paragraph(literal("p1")), paragraph(literal("p2")));
      const result = Actions.cursor(state, { blockIndex: 0, contentIndex: 0 }, keyboardEvent, 0);
      expect(keyboardEvent.preventDefault).not.toHaveBeenCalled();
      expect(result).toBe(state);
    });
    test("when not at edge of literal to do nothing", () => {
      const keyboardEvent = getKeyboardEvent("ArrowLeft");
      const state = letter(paragraph(literal("tekst i første paragraf")), paragraph(literal("tekst i andre")));
      const result = Actions.cursor(state, { blockIndex: 0, contentIndex: 0 }, keyboardEvent, 3);
      expect(keyboardEvent.preventDefault).not.toHaveBeenCalled();
      expect(result).toBe(state);
    });
    test("at the beginning of a block moves focus to last literal of previous block", () => {
      const keyboardEvent = getKeyboardEvent("ArrowLeft");
      const state = letter(
        paragraph(literal("tekst i første paragraf"), variable("X")),
        paragraph(literal("tekst i andre")),
      );
      const result = Actions.cursor(state, { blockIndex: 1, contentIndex: 0 }, keyboardEvent, 0);
      expect(keyboardEvent.preventDefault).toHaveBeenCalledOnce();
      expect(result.focus).toEqual({
        blockIndex: 0,
        contentIndex: 0,
        cursorPosition: "tekst i første paragraf".length,
      });
    });
    test("at the beginning of non-first-literal moves focus to end of previous literal in same block", () => {
      const keyboardEvent = getKeyboardEvent("ArrowLeft");
      const state = letter(paragraph(literal("literal1"), variable("X"), literal("literal2")));
      const result = Actions.cursor(state, { blockIndex: 0, contentIndex: 2 }, keyboardEvent, 0);
      expect(keyboardEvent.preventDefault).toHaveBeenCalledOnce();
      expect(result.focus).toEqual({
        blockIndex: 0,
        contentIndex: 0,
        cursorPosition: "literal1".length,
      });
    });
  });
  describe("pressing ArrowRight", () => {
    test("at end of document does nothing", () => {
      const keyboardEvent = getKeyboardEvent("ArrowRight");
      const state = letter(paragraph(literal("p1")), paragraph(literal("p2")));
      const result = Actions.cursor(state, { blockIndex: 1, contentIndex: 0 }, keyboardEvent, "p2".length);
      expect(keyboardEvent.preventDefault).not.toHaveBeenCalled();
      expect(result).toBe(state);
    });
    test("when not at edge of literal to do nothing", () => {
      const keyboardEvent = getKeyboardEvent("ArrowRight");
      const state = letter(paragraph(literal("tekst i første paragraf")), paragraph(literal("tekst i andre")));
      const result = Actions.cursor(state, { blockIndex: 0, contentIndex: 0 }, keyboardEvent, 3);
      expect(keyboardEvent.preventDefault).not.toHaveBeenCalled();
      expect(result).toBe(state);
    });
    test("at the end of a block moves focus to first literal of next block", () => {
      const keyboardEvent = getKeyboardEvent("ArrowRight");
      const state = letter(paragraph(literal("tekst i første paragraf")), paragraph(literal("tekst i andre")));
      const result = Actions.cursor(
        state,
        { blockIndex: 0, contentIndex: 0 },
        keyboardEvent,
        "tekst i første paragraf".length,
      );
      expect(result.focus).toEqual({
        blockIndex: 1,
        contentIndex: 0,
        cursorPosition: 0,
      });
      expect(keyboardEvent.preventDefault).toHaveBeenCalledOnce();
    });
    test("at the last literal (but not last content) of a block moves focus to first literal of next block", () => {
      const keyboardEvent = getKeyboardEvent("ArrowRight");
      const state = letter(
        paragraph(literal("tekst i første paragraf"), variable("X")),
        paragraph(variable("Y"), literal("tekst i andre")),
      );
      const result = Actions.cursor(
        state,
        { blockIndex: 0, contentIndex: 0 },
        keyboardEvent,
        "tekst i første paragraf".length,
      );
      expect(result.focus).toEqual({
        blockIndex: 1,
        contentIndex: 1,
        cursorPosition: 0,
      });
      expect(keyboardEvent.preventDefault).toHaveBeenCalledOnce();
    });
    test("at the end of non-last-literal moves focus to beginning of next literal in same block", () => {
      const keyboardEvent = getKeyboardEvent("ArrowRight");
      const state = letter(paragraph(literal("literal1"), variable("X"), literal("literal2")));
      const result = Actions.cursor(state, { blockIndex: 0, contentIndex: 0 }, keyboardEvent, "literal1".length);
      expect(keyboardEvent.preventDefault).toHaveBeenCalledOnce();
      expect(result.focus).toEqual({
        blockIndex: 0,
        contentIndex: 2,
        cursorPosition: 0,
      });
    });
  });
});
