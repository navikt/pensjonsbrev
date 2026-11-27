import { applyPatches } from "immer";
import { describe, expect, it } from "vitest";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { fontTypeOf, text } from "~/Brevredigering/LetterEditor/actions/common";
import { formattingStateOf } from "~/Brevredigering/LetterEditor/actions/switchFontType";
import { type Focus, type LetterEditorState, type SelectionIndex } from "~/Brevredigering/LetterEditor/model/state";
import { ElementTags, FontType, type LiteralValue, type TextContent, TITLE_INDEX } from "~/types/brevbakerTypes";
import {
  cell,
  item,
  itemList,
  letter,
  literal,
  paragraph,
  row,
  table,
  title1,
  variable,
} from "~test/support/letterEditorTestUtils";
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

const { PLAIN, BOLD, ITALIC } = FontType;

const styled = (value: string, fontType: FontType = PLAIN): LiteralValue => ({ ...literal(value), fontType });

const at = (state: LetterEditorState, focus: Focus): LetterEditorState => ({ ...state, focus });

/** The paragraph's text as runs of the same font type, regardless of how it is split into literals. */
function runs(state: LetterEditorState, blockIndex = 0): [string, FontType][] {
  const result: [string, FontType][] = [];
  for (const content of state.redigertBrev.blocks[blockIndex].content as TextContent[]) {
    const last = result.at(-1);
    if (last && last[1] === fontTypeOf(content)) last[0] += text(content);
    else result.push([text(content), fontTypeOf(content)]);
  }
  return result;
}

/** Selects all text of a paragraph made of literals. */
function selectParagraph(state: LetterEditorState, blockIndex = 0): LetterEditorState {
  const content = state.redigertBrev.blocks[blockIndex].content as LiteralValue[];
  return select(state, {
    start: { blockIndex, contentIndex: 0, cursorPosition: 0 },
    end: { blockIndex, contentIndex: content.length - 1, cursorPosition: text(content.at(-1)!).length },
  });
}

describe("switchFontType decides once for the whole range", () => {
  it.each([
    [PLAIN, BOLD, BOLD],
    [BOLD, BOLD, PLAIN],
    [ITALIC, BOLD, BOLD],
    [PLAIN, ITALIC, ITALIC],
    [BOLD, ITALIC, ITALIC],
    [ITALIC, ITALIC, PLAIN],
  ])("%s text becomes %s -> %s", (current, clicked, expected) => {
    const result = Actions.switchFontType(selectParagraph(letter(paragraph([styled("ord", current)]))), clicked);
    expect(runs(result)).toEqual([["ord", expected]]);
  });

  const fontTypes = [PLAIN, BOLD, ITALIC];
  const mixes = fontTypes.flatMap((a) =>
    fontTypes.flatMap((b) => [[a, b], ...fontTypes.map((c) => [a, b, c])]).filter((mix) => new Set(mix).size > 1),
  );
  it.each(mixes.flatMap((mix) => [BOLD, ITALIC].map((clicked) => [mix.join("+"), clicked, mix] as const)))(
    "mixed %s gets %s on all of it, and loses it on all of it on the next click",
    (_, clicked, mix) => {
      const state = selectParagraph(letter(paragraph(mix.map((fontType, index) => styled(`del${index} `, fontType)))));
      const allText = mix.map((_, index) => `del${index} `).join("");

      const applied = Actions.switchFontType(state, clicked);
      expect(runs(applied)).toEqual([[allText, clicked]]);
      expect(runs(Actions.switchFontType(applied, clicked))).toEqual([[allText, PLAIN]]);
    },
  );

  it("formats a whole sentence with bold and italic words, and repeats without reselecting", () => {
    const sentence = [
      styled("Du får "),
      styled("alderspensjon", BOLD),
      styled(" fra 1. mai 2025 etter "),
      styled("folketrygdloven § 20-2", ITALIC),
    ];
    const state = selectParagraph(letter(paragraph(sentence)));
    const all = "Du får alderspensjon fra 1. mai 2025 etter folketrygdloven § 20-2";

    const bold = Actions.switchFontType(state, BOLD);
    expect(runs(bold)).toEqual([[all, BOLD]]);
    const plain = Actions.switchFontType(bold, BOLD);
    expect(runs(plain)).toEqual([[all, PLAIN]]);
    const italic = Actions.switchFontType(plain, ITALIC);
    expect(runs(italic)).toEqual([[all, ITALIC]]);
    const boldAgain = Actions.switchFontType(italic, BOLD);
    expect(runs(boldAgain)).toEqual([[all, BOLD]]);
    for (const result of [bold, plain, italic, boldAgain]) expect(selectedText(result)).toBe(all);

    expect(runs(Actions.switchFontType(state, ITALIC))).toEqual([[all, ITALIC]]);
  });

  it("is a single undo step that restores the original mixed formatting", () => {
    const state = selectParagraph(
      letter(paragraph([styled("En "), styled("fet", BOLD), styled(" og "), styled("kursiv", ITALIC)])),
    );

    const result = Actions.switchFontType(state, BOLD);

    expect(result.history.entries).toHaveLength(1);
    expect(undo(result).redigertBrev).toEqual(state.redigertBrev);
    expect(runs(redo(undo(result)))).toEqual(runs(result));
  });

  it("keeps the template formatting and id of the text around a partial selection", () => {
    const template = styled("Dette er fet malstekst", BOLD);
    const state = select(letter(paragraph([template])), {
      start: { blockIndex: 0, contentIndex: 0, cursorPosition: 6 },
      end: { blockIndex: 0, contentIndex: 0, cursorPosition: 8 },
    });

    const italic = Actions.switchFontType(state, ITALIC);
    expect(runs(italic)).toEqual([
      ["Dette ", BOLD],
      ["er", ITALIC],
      [" fet malstekst", BOLD],
    ]);
    expect(italic.redigertBrev.blocks[0].content.map((content) => content.id)).toEqual([template.id, null, null]);
    expect(italic.redigertBrev.blocks[0].deletedContent).toEqual([]);
    expect(selectedText(italic)).toBe("er");

    expect(runs(Actions.switchFontType(state, BOLD))).toEqual([
      ["Dette ", BOLD],
      ["er", PLAIN],
      [" fet malstekst", BOLD],
    ]);
  });

  it("formats paragraphs, list items and table cells in the range, but not headings or variables", () => {
    const state = select(
      letter(
        paragraph([styled("Første avsnitt")]),
        title1(styled("Overskrift")),
        paragraph([itemList({ items: [item(styled("punkt"))] })]),
        paragraph([table([cell(styled("Topp"))], [row(cell(styled("celle")))])]),
        paragraph([styled("Beløp "), variable("100"), styled(" kr i alt")]),
      ),
      {
        start: { blockIndex: 0, contentIndex: 0, cursorPosition: 7 },
        end: { blockIndex: 4, contentIndex: 2, cursorPosition: 3 },
      },
    );

    const result = Actions.switchFontType(state, BOLD);

    expect(projectLetter(result)).toEqual([
      "P: Første **avsnitt**",
      "H1: Overskrift",
      "• **punkt**",
      "TABLE",
      "  th: **Topp**",
      "  tr: **celle**",
      "P: **Beløp **100** kr** i alt",
    ]);
  });

  it("formats only the selected characters in a table cell", () => {
    const state = select(letter(paragraph([table([cell(styled("Topp"))], [row(cell(styled("Celle med tekst")))])])), {
      start: { blockIndex: 0, contentIndex: 0, rowIndex: 0, cellIndex: 0, cellContentIndex: 0, cursorPosition: 6 },
      end: { blockIndex: 0, contentIndex: 0, rowIndex: 0, cellIndex: 0, cellContentIndex: 0, cursorPosition: 9 },
    });

    expect(projectLetter(Actions.switchFontType(state, BOLD))).toContain("  tr: Celle **med** tekst");
  });

  it("keeps a whole unedited fritekst a placeholder", () => {
    const fritekst = { ...styled("Skriv her"), tags: [ElementTags.FRITEKST] };
    const result = Actions.switchFontType(selectParagraph(letter(paragraph([fritekst]))), BOLD);

    expect(result.redigertBrev.blocks[0].content).toMatchObject([
      { id: fritekst.id, editedText: null, editedFontType: BOLD, tags: [ElementTags.FRITEKST] },
    ]);
  });

  it("keeps the caret at the end of the selection when the selection was made backwards", () => {
    const state = selectParagraph(letter(paragraph([styled("Noen stoler")])));
    const result = Actions.switchFontType({ ...state, focus: { ...state.selection!.end } }, BOLD);

    expect(result.focus).toEqual(result.selection!.end);
  });
});

describe("switchFontType with a caret formats the word around it", () => {
  it("formats the word, and keeps the caret at the same place in it", () => {
    const result = Actions.switchFontType(
      at(letter(paragraph([styled("Noen stoler knirker")])), { blockIndex: 0, contentIndex: 0, cursorPosition: 8 }),
      BOLD,
    );

    expect(runs(result)).toEqual([
      ["Noen ", PLAIN],
      ["stoler", BOLD],
      [" knirker", PLAIN],
    ]);
    expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 1, cursorPosition: 3 });
    expect(result.selection).toBeUndefined();
  });

  it("decides for the whole word when it spans literals with different formatting", () => {
    const state = at(letter(paragraph([styled("kla", BOLD), styled("sserom her")])), {
      blockIndex: 0,
      contentIndex: 0,
      cursorPosition: 1,
    });

    const bold = Actions.switchFontType(state, BOLD);
    expect(runs(bold)).toEqual([
      ["klasserom", BOLD],
      [" her", PLAIN],
    ]);
    expect(runs(Actions.switchFontType(bold, BOLD))).toEqual([["klasserom her", PLAIN]]);
  });

  it.each([
    ["inside the word", 1],
    ["at the end of the text", 5],
  ])("formats a word with punctuation, with the caret %s", (_, cursorPosition) => {
    const result = Actions.switchFontType(
      at(letter(paragraph([styled("rom "), styled("4A.")])), {
        blockIndex: 0,
        contentIndex: 1,
        cursorPosition: Math.min(cursorPosition, 3),
      }),
      BOLD,
    );
    expect(runs(result)).toEqual([
      ["rom ", PLAIN],
      ["4A.", BOLD],
    ]);
  });

  const sentence = letter(
    paragraph([styled("rom 4A. og mer")]),
    paragraph([styled("")]),
    paragraph([styled("Beløp "), variable("100")]),
  );
  const withTitle = {
    ...sentence,
    redigertBrev: { ...sentence.redigertBrev, title: { text: [styled("Vedtak")], deletedContent: [] } },
  };
  it.each([
    ["on a space", { blockIndex: 0, contentIndex: 0, cursorPosition: 7 }],
    ["right before a space", { blockIndex: 0, contentIndex: 0, cursorPosition: 3 }],
    ["in an empty paragraph", { blockIndex: 1, contentIndex: 0, cursorPosition: 0 }],
    ["on a variable", { blockIndex: 2, contentIndex: 1, cursorPosition: 0 }],
    ["in the letter title", { blockIndex: TITLE_INDEX, contentIndex: 0, cursorPosition: 2 }],
  ])("changes nothing with the caret %s", (_, focus) => {
    const state = at(withTitle, focus);
    const result = Actions.switchFontType(state, BOLD);

    expect(result.redigertBrev).toBe(state.redigertBrev);
    expect(result.history.entries).toHaveLength(0);
    expect(result.saveStatus).toBe(state.saveStatus);
  });
});

describe("formattingStateOf", () => {
  const state = letter(paragraph([styled("fet", BOLD), styled(" og "), styled("kla", BOLD), styled("sse")]));

  it("is on, off or mixed for the selection", () => {
    const boldOnly = select(state, {
      start: { blockIndex: 0, contentIndex: 0, cursorPosition: 0 },
      end: { blockIndex: 0, contentIndex: 0, cursorPosition: 3 },
    });
    expect(formattingStateOf(boldOnly, BOLD)).toBe("on");
    expect(formattingStateOf(boldOnly, ITALIC)).toBe("off");
    expect(formattingStateOf(selectParagraph(state), BOLD)).toBe("mixed");
  });

  it("uses the word around a caret, or the text the caret is in outside a word", () => {
    expect(formattingStateOf(at(state, { blockIndex: 0, contentIndex: 3, cursorPosition: 1 }), BOLD)).toBe("mixed");
    expect(formattingStateOf(at(state, { blockIndex: 0, contentIndex: 0, cursorPosition: 1 }), BOLD)).toBe("on");
    expect(formattingStateOf(at(state, { blockIndex: 0, contentIndex: 1, cursorPosition: 0 }), BOLD)).toBe("off");
  });
});
