import { describe, expect, it } from "vitest";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { create, text } from "~/Brevredigering/LetterEditor/actions/common";
import { type LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { FontType, type LiteralValue, TITLE_INDEX } from "~/types/brevbakerTypes";
import { brevResponse, editedLetter } from "~test/support/brevFixtures";
import { letter, literal, paragraph, variable } from "~test/support/letterEditorTestUtils";
import { projectLetter } from "~test/support/pasteTestUtils";

const bold = (value: string): LiteralValue => ({ ...literal(value), fontType: FontType.BOLD });
const historyLength = (state: LetterEditorState) => state.history?.entries.length ?? 0;

describe("typing over a selection", () => {
  const state = letter(paragraph([literal("Før "), bold("fet"), literal(" etter")]));
  const acrossLiterals = {
    start: { blockIndex: 0, contentIndex: 1, cursorPosition: 1 },
    end: { blockIndex: 0, contentIndex: 2, cursorPosition: 3 },
  };

  it("replaces text across literals and keeps the formatting where the selection starts", () => {
    const result = Actions.replaceSelectionWithText(state, acrossLiterals, "X");

    expect(projectLetter(result)).toEqual(["P: Før **fX**ter"]);
    expect(result.focus).toMatchObject({ blockIndex: 0, contentIndex: 1, cursorPosition: 2 });
  });

  it("replaces text across literals in the letter title", () => {
    const withTitle = create(
      brevResponse({
        redigertBrev: editedLetter({
          title: { text: [literal("Vedtak om "), variable("alderspensjon"), literal(" fra mai")], deletedContent: [] },
          blocks: [paragraph([literal("Tekst")])],
        }),
      }),
    );

    const result = Actions.replaceSelectionWithText(
      withTitle,
      {
        start: { blockIndex: TITLE_INDEX, contentIndex: 0, cursorPosition: 7 },
        end: { blockIndex: TITLE_INDEX, contentIndex: 2, cursorPosition: 4 },
      },
      "endring",
    );

    expect(result.redigertBrev.title.text.map((content) => text(content as LiteralValue)).join("")).toBe(
      "Vedtak endring mai",
    );
  });

  it("does nothing for a selection that ends before it starts", () => {
    const result = Actions.replaceSelectionWithText(
      state,
      { start: acrossLiterals.end, end: acrossLiterals.start },
      "X",
    );

    expect(result).toBe(state);
  });

  it("Enter over a selection deletes it and splits the paragraph as one undo step", () => {
    const result = Actions.replaceSelectionWithBreak(state, acrossLiterals, "paragraph");

    expect(projectLetter(result)).toEqual(["P: Før **f**", "P: ter"]);
    expect(historyLength(result)).toBe(historyLength(state) + 1);
  });

  it("Shift+Enter over a selection deletes it and adds a line break", () => {
    const result = Actions.replaceSelectionWithBreak(state, acrossLiterals, "lineBreak");

    expect(result.redigertBrev.blocks).toHaveLength(1);
    expect(result.redigertBrev.blocks[0].content.map((content) => content.type)).toContain("NEW_LINE");
  });
});

describe("typing with the focus on a variable", () => {
  it("inserts the text in a new literal after the variable", () => {
    const state = {
      ...letter(paragraph([literal("Dato: "), variable("1. mai")])),
      focus: { blockIndex: 0, contentIndex: 1 },
    };

    const result = Actions.insertText(state, "!");

    expect(projectLetter(result)).toEqual(["P: Dato: 1. mai!"]);
    expect(result.redigertBrev.blocks[0].content.map((content) => content.type)).toEqual([
      "LITERAL",
      "VARIABLE",
      "LITERAL",
    ]);
    expect(result.focus).toMatchObject({ blockIndex: 0, contentIndex: 2, cursorPosition: 1 });
  });
});

describe("selection after an action", () => {
  it("is cleared when the action changes the letter but not the selection", () => {
    const state: LetterEditorState = {
      ...letter(paragraph([literal("Tekst")])),
      selection: {
        start: { blockIndex: 0, contentIndex: 0, cursorPosition: 0 },
        end: { blockIndex: 0, contentIndex: 0, cursorPosition: 2 },
      },
    };

    const result = Actions.updateContentText(state, { blockIndex: 0, contentIndex: 0 }, "Tekst!", 6);

    expect(result.selection).toBeUndefined();
  });
});
