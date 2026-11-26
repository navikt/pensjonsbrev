import { applyPatches } from "immer";
import { describe, expect, test } from "vitest";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { text } from "~/Brevredigering/LetterEditor/actions/common";
import { FontType, TITLE_INDEX } from "~/types/brevbakerTypes";
import { letter, literal, paragraph } from "~test/support/letterEditorTestUtils";
import { MockDataTransfer, projectLetter } from "~test/support/pasteTestUtils";

const titleIndex = { blockIndex: TITLE_INDEX, contentIndex: 0 };

function titledLetter() {
  const state = letter(paragraph({ id: 20, content: [literal({ id: 21, text: "Old body" })] }));
  return {
    ...state,
    redigertBrev: {
      ...state.redigertBrev,
      title: { text: [literal({ id: 10, text: "Old title" })], deletedContent: [] },
    },
  };
}

describe("pasting in the document title", () => {
  test("inline formatting stays in the title when replacing selected text", () => {
    const state = titledLetter();
    const result = Actions.pasteReplacingSelection(
      state,
      { start: { ...titleIndex, cursorPosition: 0 }, end: { ...titleIndex, cursorPosition: 3 } },
      new MockDataTransfer({ "text/html": "<p><b>New</b></p>" }),
    );

    expect(result.redigertBrev.title.text.map((run) => text(run))).toEqual(["New", " title"]);
    expect(result.redigertBrev.title.text[0]).toMatchObject({ id: null, fontType: FontType.BOLD });
    expect(result.redigertBrev.title.text[1]).toMatchObject({ id: 10 });
    expect(result.redigertBrev.blocks).toEqual(state.redigertBrev.blocks);
    expect(result.focus).toEqual({ ...titleIndex, cursorPosition: 3 });
  });

  test("multiline plain text preserves blank lines and leaves the title suffix after the paste", () => {
    const state = titledLetter();
    const result = Actions.paste(state, titleIndex, 3, new MockDataTransfer({ "text/plain": "!\r\n\r\nNext" }));

    expect(text(result.redigertBrev.title.text[0])).toBe("Old!");
    expect(result.redigertBrev.blocks).toHaveLength(3);
    expect(projectLetter(result)).toEqual(["P: Next title", "P: Old body"]);
    expect(result.focus).toEqual({ blockIndex: 1, contentIndex: 0, cursorPosition: 4 });
    expect(result.redigertBrev.blocks.at(-1)).toEqual(state.redigertBrev.blocks[0]);
  });

  test("formatted paragraphs split at the insertion boundary when the first run merges with its neighbour", () => {
    const state = titledLetter();
    state.redigertBrev.title.text = [
      { ...literal({ id: 10, text: "Old" }), fontType: FontType.BOLD },
      literal({ id: 11, text: " title" }),
    ];
    const result = Actions.paste(
      state,
      { ...titleIndex, contentIndex: 1 },
      0,
      new MockDataTransfer({ "text/html": "<p><b>!</b></p><p>Next</p>" }),
    );

    expect(result.redigertBrev.title.text).toMatchObject([{ id: 10, editedText: "Old!" }]);
    expect(result.redigertBrev.title.deletedContent).toEqual([11]);
    expect(projectLetter(result)).toEqual(["P: Next title", "P: Old body"]);
    expect(result.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 4 });
  });

  test("a table at the title cursor goes before the title suffix in the body, without a plain-text fallback", () => {
    const result = Actions.paste(
      titledLetter(),
      titleIndex,
      3,
      new MockDataTransfer({ "text/html": "<table><tr><th>H</th></tr><tr><td>A</td></tr></table>" }),
    );

    expect(text(result.redigertBrev.title.text[0])).toBe("Old");
    expect(projectLetter(result)).toEqual(["TABLE", "  th: H", "  tr: A", "P:  title", "P: Old body"]);
    expect(result.focus).toMatchObject({ blockIndex: 0, rowIndex: 0, cellIndex: 0 });
  });

  test("one undo restores both sides of a selection replaced with a heading, list and paragraph", () => {
    const state = titledLetter();
    const result = Actions.pasteReplacingSelection(
      state,
      { start: { ...titleIndex, cursorPosition: 4 }, end: { blockIndex: 0, contentIndex: 0, cursorPosition: 4 } },
      new MockDataTransfer({ "text/html": "<h1>heading</h1><ul><li>item</li></ul><p>more</p>" }),
    );

    expect(text(result.redigertBrev.title.text[0])).toBe("Old heading");
    expect(projectLetter(result)).toEqual(["• item", "P: more", "P: body"]);
    expect(result.redigertBrev.blocks.at(-1)).toMatchObject({ id: 20, content: [{ id: 21 }] });
    expect(result.history?.entries).toHaveLength(1);
    const entry = result.history?.entries[0];
    expect(entry?.type).toBe("PATCH");
    if (entry?.type === "PATCH")
      expect(applyPatches(result, entry.inversePatches).redigertBrev).toEqual(state.redigertBrev);
  });
});
