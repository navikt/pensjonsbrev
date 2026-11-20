import { applyPatches } from "immer";
import { describe, expect, test } from "vitest";

import { cell, item, itemList, letter, literal, paragraph, row, table } from "~test/support/letterEditorTestUtils";

import { type SelectionIndex } from "../model/state";
import Actions from ".";

describe("deleteSelectionAndEnter", () => {
  for (const softBreak of [false, true]) {
    test.each(["avsnitt", "liste", "tabell"] as const)(
      `bruker fokus etter sletting i %s (softBreak=${softBreak})`,
      (container) => {
        const content = [literal("Ett to "), literal("tre fire")];
        const state = letter(
          paragraph(
            container === "avsnitt"
              ? content
              : container === "liste"
                ? [itemList({ items: [item({ content })] })]
                : [table([cell(literal("Overskrift"))], [row(cell(...content))]), literal("")],
          ),
        );
        const start = { blockIndex: 0, contentIndex: 0, cursorPosition: 4 };
        const end = { blockIndex: 0, contentIndex: 1, cursorPosition: 3 };
        const selection: SelectionIndex =
          container === "avsnitt"
            ? { start, end }
            : container === "liste"
              ? {
                  start: { ...start, itemIndex: 0, itemContentIndex: 0 },
                  end: { ...end, contentIndex: 0, itemIndex: 0, itemContentIndex: 1 },
                }
              : {
                  start: { ...start, rowIndex: 0, cellIndex: 0, cellContentIndex: 0 },
                  end: { ...end, contentIndex: 0, rowIndex: 0, cellIndex: 0, cellContentIndex: 1 },
                };
        state.focus = selection.end;

        const deleted = Actions.deleteSelection(state, selection);
        const expected =
          container === "tabell"
            ? deleted
            : softBreak
              ? Actions.addNewLine(deleted, deleted.focus)
              : Actions.split(deleted, deleted.focus, deleted.focus.cursorPosition ?? 0);

        const result = Actions.deleteSelectionAndEnter(state, selection, softBreak);

        expect(result.redigertBrev).toEqual(expected.redigertBrev);
        expect(result.focus).toEqual(expected.focus);
        if (softBreak && container !== "tabell") {
          expect(result.focus).toMatchObject(
            container === "avsnitt"
              ? { blockIndex: 0, contentIndex: 2, cursorPosition: 0 }
              : { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 2, cursorPosition: 0 },
          );
        }
        expect(result.history.entries).toHaveLength(1);
        const entry = result.history.entries[0];
        expect(entry.type).toBe("PATCH");
        if (entry.type !== "PATCH") throw new Error("Expected patch history");
        const undone = applyPatches(result, entry.inversePatches);
        expect(undone.redigertBrev).toEqual(state.redigertBrev);
        expect(undone.focus).toEqual(state.focus);
      },
    );
  }

  test("ugyldig markering setter ikke inn linjeskift", () => {
    const state = letter(paragraph([literal("Ett to ")]));
    const start = { blockIndex: 0, contentIndex: 0, cursorPosition: 4 };
    const end = { blockIndex: 99, contentIndex: 0, cursorPosition: 3 };

    expect(Actions.deleteSelectionAndEnter(state, { start, end }, false)).toBe(state);
  });
});
