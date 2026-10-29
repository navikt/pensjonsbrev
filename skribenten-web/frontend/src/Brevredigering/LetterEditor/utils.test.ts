import { afterEach, describe, expect, test, vi } from "vitest";

import { FontType } from "~/types/brevbakerTypes";

import { switchFontTypeAtSelection } from "./utils";

function setup(decorated: boolean) {
  const root = document.createElement("div");
  root.setAttribute("data-editor-root", "");
  const literal = document.createElement("span");
  literal.setAttribute("contenteditable", "true");
  literal.setAttribute("data-literal-index", JSON.stringify({ blockIndex: 0, contentIndex: 0 }));
  if (decorated) literal.dataset.diffVersion = "1";
  literal.textContent = "gammel ABCDEF";
  root.append(literal);
  document.body.append(root);
  return { root, textNode: literal.firstChild! };
}

function select(node: Node, start: number, end = start) {
  const range = document.createRange();
  range.setStart(node, start);
  range.setEnd(node, end);
  globalThis.getSelection()?.removeAllRanges();
  globalThis.getSelection()?.addRange(range);
}

afterEach(() => {
  globalThis.getSelection()?.removeAllRanges();
  document.body.innerHTML = "";
});

describe("switchFontTypeAtSelection", () => {
  test.each([
    ["markering", 7, 9],
    ["markør", 8, 8],
  ])("%s i diff-markert literal slår bare av diff-modus", (_, start, end) => {
    const { root, textNode } = setup(true);
    select(textNode, start, end);
    const setEditorState = vi.fn();
    const disableDiffMode = vi.fn();
    const beforeAction = vi.fn();

    switchFontTypeAtSelection(setEditorState, FontType.BOLD, root, disableDiffMode, beforeAction);

    expect(disableDiffMode).toHaveBeenCalledTimes(1);
    expect(setEditorState).not.toHaveBeenCalled();
    expect(beforeAction).not.toHaveBeenCalled();
  });

  test("markering uten diff-markering endrer fonttype", () => {
    const { root, textNode } = setup(false);
    select(textNode, 7, 9);
    const setEditorState = vi.fn();
    const disableDiffMode = vi.fn();
    const beforeAction = vi.fn();

    switchFontTypeAtSelection(setEditorState, FontType.BOLD, root, disableDiffMode, beforeAction);

    expect(disableDiffMode).not.toHaveBeenCalled();
    expect(beforeAction).toHaveBeenCalledTimes(1);
    expect(setEditorState).toHaveBeenCalledTimes(1);
  });
});
