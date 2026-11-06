import { afterEach, describe, expect, test, vi } from "vitest";

import { FontType } from "~/types/brevbakerTypes";
import { letter, literal, paragraph } from "~test/support/letterEditorTestUtils";

import { type LetterEditorState } from "./model/state";
import { switchFontTypeAtSelection } from "./utils";

// Modellteksten er "Ny tekst"; "gammel " er slettet tekst som bare vises i diff-modus.
function setup(decorated: boolean) {
  const root = document.createElement("div");
  const literalEl = document.createElement("span");
  literalEl.contentEditable = "true";
  literalEl.dataset.literalIndex = JSON.stringify({ blockIndex: 0, contentIndex: 0 });
  if (decorated) {
    literalEl.dataset.diffVersion = "1";
    const deleted = document.createElement("span");
    deleted.dataset.diffDeletion = "";
    deleted.textContent = "gammel ";
    literalEl.append("Ny ", deleted, "tekst");
  } else {
    literalEl.append("Ny tekst");
  }
  root.append(literalEl);
  document.body.append(root);
  return { root, last: literalEl.lastChild! };
}

function run(root: HTMLElement) {
  let state: LetterEditorState = letter(paragraph([literal("Ny tekst")]));
  const setEditorState = vi.fn((update: (previous: LetterEditorState) => LetterEditorState) => {
    state = update(state);
  });
  const disableDiffMode = vi.fn();
  const beforeAction = vi.fn();
  switchFontTypeAtSelection(setEditorState, FontType.BOLD, root, disableDiffMode, beforeAction);
  return { state: () => state, disableDiffMode, beforeAction };
}

afterEach(() => {
  globalThis.getSelection()?.removeAllRanges();
  document.body.innerHTML = "";
});

describe("switchFontTypeAtSelection", () => {
  test("i diff-modus endres fonttype uten slettet tekst, og diff-modus slås av", () => {
    const { root, last } = setup(true);
    globalThis.getSelection()?.setBaseAndExtent(last, 0, last, 5);

    const { state, disableDiffMode, beforeAction } = run(root);

    expect(disableDiffMode).toHaveBeenCalledTimes(1);
    expect(beforeAction).toHaveBeenCalledTimes(1);
    const content = state().redigertBrev.blocks[0].content;
    expect(
      content.map((c) => [
        c.type === "LITERAL" && (c.editedText ?? c.text),
        c.type === "LITERAL" && (c.editedFontType ?? c.fontType),
      ]),
    ).toEqual([
      ["Ny ", FontType.PLAIN],
      ["tekst", FontType.BOLD],
    ]);
    expect(state().focus.pendingSelection).toEqual({
      start: { blockIndex: 0, contentIndex: 1, cursorPosition: 0 },
      end: { blockIndex: 0, contentIndex: 1, cursorPosition: 5 },
    });
  });

  test("uten diff-markering slås ikke diff-modus av", () => {
    const { root, last } = setup(false);
    globalThis.getSelection()?.setBaseAndExtent(last, 3, last, 8);

    const { state, disableDiffMode } = run(root);

    expect(disableDiffMode).not.toHaveBeenCalled();
    expect(state().saveStatus).toBe("DIRTY");
  });
});
