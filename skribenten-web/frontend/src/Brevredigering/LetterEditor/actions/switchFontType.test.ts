import { applyPatches } from "immer";
import { describe, expect, it } from "vitest";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { text } from "~/Brevredigering/LetterEditor/actions/common";
import { type LetterEditorState, type SelectionIndex } from "~/Brevredigering/LetterEditor/model/state";
import { FontType, type LiteralValue, type TextContent } from "~/types/brevbakerTypes";
import { item, itemList, letter, literal, paragraph } from "~test/support/letterEditorTestUtils";
import { projectLetter } from "~test/support/pasteTestUtils";

const select = (state: LetterEditorState, selection: SelectionIndex): LetterEditorState => ({
  ...state,
  focus: { ...selection.start },
  selection,
});

/** The text the model selection covers, when it starts and ends in the same paragraph or list item. */
function selectedText(state: LetterEditorState): string {
  const { start, end } = state.selection!;
  const content =
    "itemIndex" in start
      ? (
          state.redigertBrev.blocks[start.blockIndex].content[start.contentIndex] as {
            items: { content: TextContent[] }[];
          }
        ).items[start.itemIndex].content
      : (state.redigertBrev.blocks[start.blockIndex].content as TextContent[]);
  const position = (index: SelectionIndex["start"]) =>
    "itemIndex" in index ? index.itemContentIndex : index.contentIndex;
  const parts = content.slice(position(start), position(end) + 1).map((c) => text(c as LiteralValue));
  const joined = parts.join("");
  const startOffset = start.cursorPosition;
  const endOffset = joined.length - (parts.at(-1)!.length - end.cursorPosition);
  return joined.slice(startOffset, endOffset);
}

function undo(state: LetterEditorState): LetterEditorState {
  const entry = state.history.entries[state.history.entryPointer];
  if (entry.type !== "PATCH") throw new Error("Expected a patch entry");
  return {
    ...applyPatches(state, entry.inversePatches),
    history: { ...state.history, entryPointer: state.history.entryPointer - 1 },
  };
}

function redo(state: LetterEditorState): LetterEditorState {
  const entry = state.history.entries[state.history.entryPointer + 1];
  if (entry.type !== "PATCH") throw new Error("Expected a patch entry");
  return {
    ...applyPatches(state, entry.patches),
    history: { ...state.history, entryPointer: state.history.entryPointer + 1 },
  };
}

describe("switchFontType keeps the selection", () => {
  const sentence = "Noen stoler knirker i rom 4A.";
  const knirker = {
    start: { blockIndex: 0, contentIndex: 0, cursorPosition: 12 },
    end: { blockIndex: 0, contentIndex: 0, cursorPosition: 19 },
  };

  it("keeps the same text selected when the literal is split, through Bold then Italic, undo and redo", () => {
    const initial = select(letter(paragraph([literal(sentence)])), knirker);

    const bold = Actions.switchFontType(initial, FontType.BOLD);
    expect(projectLetter(bold)).toEqual(["P: Noen stoler **knirker** i rom 4A."]);
    expect(bold.selection).toEqual({
      start: { blockIndex: 0, contentIndex: 1, cursorPosition: 0 },
      end: { blockIndex: 0, contentIndex: 1, cursorPosition: 7 },
    });
    expect(selectedText(bold)).toBe("knirker");

    const italic = Actions.switchFontType(bold, FontType.ITALIC);
    expect(projectLetter(italic)).toEqual(["P: Noen stoler _knirker_ i rom 4A."]);
    expect(selectedText(italic)).toBe("knirker");

    const undoneOnce = undo(italic);
    expect(projectLetter(undoneOnce)).toEqual(["P: Noen stoler **knirker** i rom 4A."]);
    expect(selectedText(undoneOnce)).toBe("knirker");

    const undoneTwice = undo(undoneOnce);
    expect(projectLetter(undoneTwice)).toEqual([`P: ${sentence}`]);
    expect(undoneTwice.selection).toEqual(knirker);

    const redone = redo(redo(undoneTwice));
    expect(projectLetter(redone)).toEqual(["P: Noen stoler _knirker_ i rom 4A."]);
    expect(selectedText(redone)).toBe("knirker");
  });

  it("keeps the selection when the same format is toggled off again", () => {
    const bold = Actions.switchFontType(select(letter(paragraph([literal(sentence)])), knirker), FontType.BOLD);

    const plain = Actions.switchFontType(bold, FontType.BOLD);

    expect(projectLetter(plain)).toEqual([`P: ${sentence}`]);
    expect(selectedText(plain)).toBe("knirker");
  });

  it("keeps the selection in a list item", () => {
    const state = select(letter(paragraph([itemList({ items: [item(literal("Første punkt her"))] })])), {
      start: { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0, cursorPosition: 7 },
      end: { blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0, cursorPosition: 12 },
    });

    const result = Actions.switchFontType(Actions.switchFontType(state, FontType.BOLD), FontType.ITALIC);

    expect(projectLetter(result)).toEqual(["• Første _punkt_ her"]);
    expect(selectedText(result)).toBe("punkt");
  });

  it("keeps a selection of a whole literal next to other literals", () => {
    const state = select(letter(paragraph([literal("Før "), literal("midt"), literal(" etter")])), {
      start: { blockIndex: 0, contentIndex: 1, cursorPosition: 0 },
      end: { blockIndex: 0, contentIndex: 1, cursorPosition: 4 },
    });

    const result = Actions.switchFontType(state, FontType.ITALIC);

    expect(projectLetter(result)).toEqual(["P: Før _midt_ etter"]);
    expect(result.selection).toEqual(state.selection);
  });
});
