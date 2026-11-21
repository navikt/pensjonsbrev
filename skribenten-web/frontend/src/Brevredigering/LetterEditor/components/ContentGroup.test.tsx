// jsdom sammenklapper eventuelle tekstmarkeringer umiddelbart etter avfyring av
// fokus-hendelse, som gjør at vår programmatiske markering av fritekstfelt i
// fokus-hendelsen ikke blir stående i den påfølgende klikk-hendelsen:
// https://github.com/jsdom/jsdom/blob/adb999a12912f2f5ceb49fde6b1c9f7051968dc8/lib/jsdom/living/nodes/HTMLOrSVGElement-impl.js#L73
// happy-dom gjør ikke "collapse()" etter avfyring av "focus" og oppfører seg
// derfor likt som i nettleseren, derfor bruker vi happy-dom i denne testen:
// @vitest-environment happy-dom

import { act, fireEvent, render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { useState } from "react";
import { afterEach, beforeEach, describe, expect, type Mock, test, vi } from "vitest";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { newLiteral } from "~/Brevredigering/LetterEditor/actions/common";
import { MergeTarget } from "~/Brevredigering/LetterEditor/actions/merge";
import { ContentGroup } from "~/Brevredigering/LetterEditor/components/ContentGroup";
import { EditableSurface } from "~/Brevredigering/LetterEditor/components/EditableSurface";
import { EditorStateContext } from "~/Brevredigering/LetterEditor/LetterEditor";
import { type CallbackReceiver } from "~/Brevredigering/LetterEditor/lib/actions";
import { type LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { ElementTags, type LiteralValue, type ParagraphBlock } from "~/types/brevbakerTypes";
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

const content: LiteralValue[] = [newLiteral({ id: 1, text: "Heisann" }), newLiteral({ id: 2, text: "Velkommen" })];

const block: ParagraphBlock = {
  id: 1,
  parentId: null,
  editable: true,
  type: "PARAGRAPH",
  deletedContent: [],
  missingFromTemplate: false,
  content,
};
const editorState = letter(block, block, block);
const setEditorState: Mock<CallbackReceiver<LetterEditorState>> = vi.fn();

beforeEach(() => {
  // This makes the tests deterministic by ensuring every call to Date.now() returns the same value.
  vi.spyOn(Date, "now").mockImplementation(() => new Date("2025-01-01T00:00:00.000Z").getTime());
});

afterEach(() => {
  vi.restoreAllMocks();
});

const undo = vi.fn();
const redo = vi.fn();

function StatefulHarness({
  initialState,
  renderAllBlocks,
  stateRef,
}: {
  initialState: LetterEditorState;
  renderAllBlocks: boolean;
  stateRef: { current: LetterEditorState };
}) {
  const [state, setState] = useState(initialState);
  stateRef.current = state;
  const wrappedSetState: CallbackReceiver<LetterEditorState> = (updater) => {
    setEditorState(updater);
    setState((prev) => updater(prev));
  };

  return (
    <EditorStateContext.Provider
      value={{ freeze: false, error: false, editorState: state, setEditorState: wrappedSetState, undo, redo }}
    >
      <EditableSurface data-editor-root>
        {renderAllBlocks ? (
          state.redigertBrev.blocks.map((b, blockIndex) => (
            <div className={b.type} key={blockIndex}>
              <ContentGroup literalIndex={{ blockIndex, contentIndex: 0 }} />
            </div>
          ))
        ) : (
          <ContentGroup literalIndex={{ blockIndex: 0, contentIndex: 0 }} />
        )}
      </EditableSurface>
    </EditorStateContext.Provider>
  );
}

function setup(renderAllBlocks = false) {
  const stateRef = { current: editorState };
  return {
    user: userEvent.setup(),
    getState: () => stateRef.current,
    ...render(<StatefulHarness initialState={editorState} renderAllBlocks={renderAllBlocks} stateRef={stateRef} />),
  };
}

const complexEditorState = letter(
  paragraph([
    variable("Dokumentet starter med variable"),
    literal({ text: "første literal" }),
    variable("X"),
    literal({ text: "andre literal", tags: [ElementTags.FRITEKST] }),
  ]),
  paragraph([variable("Paragraf med kun variable")]),
  paragraph([literal({ text: "paragraf med kun tekst" })]),
  paragraph([
    itemList({
      items: [
        item(literal({ text: "1. item" })),
        item(variable("2. item variable")),
        item(literal({ text: "3. item" }), variable("med variable")),
        item(literal({ text: "nth item" })),
      ],
    }),
  ]),
  paragraph([literal({ text: "Dokumentet avsluttes med literal" })]),
);

function setupComplex(stateOverride?: LetterEditorState) {
  const initial = stateOverride ?? complexEditorState;
  const stateRef = { current: initial };
  return {
    user: userEvent.setup(),
    getState: () => stateRef.current,
    ...render(<StatefulHarness initialState={initial} renderAllBlocks stateRef={stateRef} />),
  };
}

function placeCaret(element: HTMLElement, edge: "start" | "end") {
  const textNode = element.firstChild;
  if (!textNode) throw new Error("Expected text node");
  const range = document.createRange();
  range.setStart(textNode, edge === "start" ? 0 : (textNode.textContent?.length ?? 0));
  range.collapse(true);
  const selection = globalThis.getSelection();
  selection?.removeAllRanges();
  selection?.addRange(range);
}

describe("updateContent", () => {
  test("text changes are propagated", async () => {
    const { user, getState } = setup();
    const target = screen.getByText(content[0].text);
    await user.click(target);
    placeCaret(target, "end");
    await user.keyboard(" person");
    expect(setEditorState).toHaveBeenCalled();

    const newText = `${content[0].text} person`;
    expect(getState().redigertBrev.blocks[0].content[0]).toMatchObject({ editedText: newText });
    expect(getState().focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: newText.length });
  });
  test("enter is not propagated as br-element", async () => {
    const { user, container, getState } = setup(true);
    const target = screen.getAllByText(content[0].text)[0];
    await user.click(target);
    placeCaret(target, "end");
    await user.keyboard("{Enter}asd");

    expect(container.querySelectorAll("br")).toHaveLength(0);
    expect(getState().redigertBrev.blocks).toHaveLength(editorState.redigertBrev.blocks.length + 1);
    expect(getState().redigertBrev.blocks[1].content[0]).toMatchObject({ editedText: `asd${content[1].text}` });
  });
  test("space is not propagated as nbsp-entity", async () => {
    const { user, getState } = setup();
    const target = screen.getByText(content[0].text);
    await user.click(target);
    placeCaret(target, "end");
    await user.keyboard("  asd");

    const newText = `${content[0].text}  asd`;
    expect(getState().redigertBrev.blocks[0].content[0]).toMatchObject({ editedText: newText });
    expect(getState().focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: newText.length });
  });
});

describe("deleteHandler", () => {
  test("delete at end of block, and after last character, triggers merge with next", async () => {
    const { user } = setup();
    const target = screen.getByText(content[1].text);

    await user.click(target);
    placeCaret(target, "end");
    await user.keyboard("{Delete}");

    expect(setEditorState.mock.lastCall?.[0](editorState)).toEqual(
      Actions.merge(editorState, { blockIndex: 0, contentIndex: 1 }, MergeTarget.NEXT),
    );
  });
  test("delete at end of block, but not after last character, does not trigger merge", async () => {
    const { user } = setup();

    await user.click(screen.getByText(content[1].text));
    await user.keyboard("{ArrowLeft}{ArrowLeft}{Delete}");

    expect(setEditorState.mock.lastCall?.[0](editorState)?.redigertBrev.blocks).toHaveLength(
      editorState.redigertBrev.blocks.length,
    );
  });
  test("delete not at end of block, but after last character, does not trigger merge", async () => {
    const { user } = setup();
    const target = screen.getByText(content[0].text);
    await user.click(target);
    placeCaret(target, "end");
    await user.keyboard("{Delete}");

    expect(setEditorState.mock.lastCall?.[0](editorState)).toEqual(editorState);
  });
});

describe("backspaceHandler", () => {
  test("backspace at beginning of block triggers merge with previous", async () => {
    const { user } = setup();
    const target = screen.getByText(content[0].text);
    await user.click(target);
    placeCaret(target, "start");
    await user.keyboard("{Backspace}");
    expect(setEditorState.mock.lastCall?.[0](editorState)).toEqual(
      Actions.merge(editorState, { blockIndex: 0, contentIndex: 0 }, MergeTarget.PREVIOUS),
    );
  });
  test("backspace at beginning of block, but not before first character of TextContent, does not trigger merge", async () => {
    const { user } = setup();
    const target = screen.getByText(content[0].text);
    await user.click(target);
    placeCaret(target, "end");
    await user.keyboard("{ArrowLeft}{ArrowLeft}{Backspace}");

    expect(setEditorState.mock.lastCall?.[0](editorState)?.redigertBrev.blocks).toHaveLength(
      editorState.redigertBrev.blocks.length,
    );
  });
  test("backspace not at beginning of block, but before first character of a TextContent, does not trigger merge", async () => {
    const { user } = setup();
    const target = screen.getByText(content[0].text);
    await user.click(target);
    placeCaret(target, "start");
    await user.keyboard("{Backspace}");

    expect(setEditorState.mock.lastCall?.[0](editorState)?.redigertBrev).toBe(editorState.redigertBrev);
  });
});

describe("enterHandler", () => {
  test("enter at the very end of block triggers split with empty text for new block", async () => {
    const { user } = setup();
    const target = screen.getByText(content[1].text);
    await user.click(target);
    placeCaret(target, "end");
    await user.keyboard("{Enter}");

    expect(setEditorState.mock.lastCall?.[0](editorState)).toEqual(
      Actions.split(editorState, { blockIndex: 0, contentIndex: 1 }, content[1].text.length),
    );
  });
  test("enter not at the end of a content in block triggers split at cursor (text after cursor for new block)", async () => {
    const { user } = setup();
    const target = screen.getByText(content[1].text);
    await user.click(target);
    placeCaret(target, "end");
    await user.keyboard("{ArrowLeft}{ArrowLeft}{ArrowLeft}{Enter}");

    expect(setEditorState.mock.lastCall?.[0](editorState)).toEqual(
      Actions.split(editorState, { blockIndex: 0, contentIndex: 1 }, content[1].text.length - 3),
    );
  });
  test("enter at the end of an element that is not the last of block triggers split at current element", async () => {
    const { user } = setup();
    const target = screen.getByText(content[0].text);
    await user.click(target);
    placeCaret(target, "end");
    await user.keyboard("{Enter}");

    expect(setEditorState.mock.lastCall?.[0](editorState)).toEqual(
      Actions.split(editorState, { blockIndex: 0, contentIndex: 0 }, content[0].text.length),
    );
  });

  test("shift-enter will add a newLine instead of a new paragraph", async () => {
    const { user } = setup();
    const target = screen.getByText(content[0].text);
    await user.click(target);
    placeCaret(target, "end");
    await user.keyboard("{Shift>}{Enter}{/Shift}");

    expect(setEditorState.mock.lastCall?.[0](editorState)).toEqual(
      Actions.addNewLine(editorState, { blockIndex: 0, contentIndex: 0, cursorPosition: "Heisann".length }),
    );
  });
});

describe("ArrowLeft will move focus to previous editable content", () => {
  test("unless already at the start of the document", async () => {
    const { user } = setupComplex();
    const target = screen.getByText("første literal");
    await user.click(target);
    expect(setEditorState.mock.lastCall?.[0](editorState)?.focus).toEqual({
      blockIndex: 0,
      contentIndex: 1,
      cursorPosition: 14,
    });

    placeCaret(target, "start");
    await user.keyboard("{ArrowLeft}");

    expect(setEditorState.mock.lastCall?.[0](editorState)?.focus).toEqual({
      blockIndex: 0,
      contentIndex: 1,
      cursorPosition: 0,
    });
  });

  test("will skip variables in the same block", async () => {
    const { user } = setupComplex();
    const target = screen.getByText("andre literal");
    await user.click(target);
    expect(setEditorState.mock.lastCall?.[0](editorState)?.focus).toEqual({
      blockIndex: 0,
      contentIndex: 3,
      cursorPosition: 13,
    });

    placeCaret(target, "start");
    await user.keyboard("{ArrowLeft}");

    expect(setEditorState.mock.lastCall?.[0](editorState)?.focus).toEqual({
      blockIndex: 0,
      contentIndex: 1,
      cursorPosition: 14,
    });
  });
  test("will skip over a block if it has no editable content", async () => {
    const { user } = setupComplex();
    const target = screen.getByText("paragraf med kun tekst");
    await user.click(target);
    expect(setEditorState.mock.lastCall?.[0](editorState)?.focus).toEqual({
      blockIndex: 2,
      contentIndex: 0,
      cursorPosition: 22,
    });

    placeCaret(target, "start");
    await user.keyboard("{ArrowLeft}");

    expect(setEditorState.mock.lastCall?.[0](editorState)?.focus).toEqual({
      blockIndex: 0,
      contentIndex: 3,
      cursorPosition: 13,
    });
  });
  test("will skip an item if it is not editable", async () => {
    const { user } = setupComplex();
    const target = screen.getByText("3. item");
    await user.click(target);
    expect(setEditorState.mock.lastCall?.[0](editorState)?.focus).toEqual({
      blockIndex: 3,
      contentIndex: 0,
      cursorPosition: 7,
      itemIndex: 2,
      itemContentIndex: 0,
    });

    placeCaret(target, "start");
    await user.keyboard("{ArrowLeft}");

    expect(setEditorState.mock.lastCall?.[0](editorState)?.focus).toEqual({
      blockIndex: 3,
      contentIndex: 0,
      itemIndex: 0,
      itemContentIndex: 0,
      cursorPosition: 7,
    });
  });
  test("When marked a fritekst-felt, 2 arrow-lefts required to move to next editable content", async () => {
    const { user } = setupComplex();
    await user.click(screen.getByText("andre literal"));
    expect(setEditorState.mock.lastCall?.[0](editorState)?.focus).toEqual({
      blockIndex: 0,
      contentIndex: 3,
      cursorPosition: 13,
    });

    await user.keyboard("{ArrowLeft}");
    expect(setEditorState.mock.lastCall?.[0](editorState)?.focus).toEqual({
      blockIndex: 0,
      contentIndex: 3,
      cursorPosition: 0,
    });

    await user.keyboard("{ArrowLeft}");
    expect(setEditorState.mock.lastCall?.[0](editorState)?.focus).toEqual({
      blockIndex: 0,
      contentIndex: 1,
      cursorPosition: 14,
    });
  });
});
describe("ArrowRight will move focus to next editable content", () => {
  test("unless already at the end of the document", async () => {
    const { user } = setupComplex();
    await user.click(screen.getByText("Dokumentet avsluttes med literal"));
    expect(setEditorState.mock.lastCall?.[0](editorState)?.focus).toEqual({
      blockIndex: 4,
      contentIndex: 0,
      cursorPosition: 32,
    });

    await user.keyboard("{ArrowRight}");

    expect(setEditorState.mock.lastCall?.[0](editorState)?.focus).toEqual({
      blockIndex: 4,
      contentIndex: 0,
      cursorPosition: 32,
    });
  });

  test("will skip variables in the same block", async () => {
    const { user, getState } = setupComplex();
    const element = screen.getByText("første literal");
    await user.click(element);

    placeCaret(element, "end");

    expect(getState().focus).toEqual({
      blockIndex: 0,
      contentIndex: 1,
      cursorPosition: 14,
    });

    await user.keyboard("{ArrowRight}");

    expect(getState().focus).toEqual({
      blockIndex: 0,
      contentIndex: 3,
      cursorPosition: 13,
    });
    expect(getState().selection).toEqual({
      start: { blockIndex: 0, contentIndex: 3, cursorPosition: 0 },
      end: { blockIndex: 0, contentIndex: 3, cursorPosition: 13 },
    });
  });
  test("will skip over a block if it has no editable content", async () => {
    const { user } = setupComplex();
    await user.click(screen.getByText("andre literal"));
    expect(setEditorState.mock.lastCall?.[0](editorState)?.focus).toEqual({
      blockIndex: 0,
      contentIndex: 3,
      cursorPosition: 13,
    });

    await user.keyboard("{ArrowRight}");
    await user.keyboard("{ArrowRight}");

    expect(setEditorState.mock.lastCall?.[0](editorState)?.focus).toEqual({
      blockIndex: 2,
      contentIndex: 0,
      cursorPosition: 0,
    });
  });
  test("will skip an item if it is not editable", async () => {
    const { user } = setupComplex();
    const target = screen.getByText("3. item");
    await user.click(target);
    expect(setEditorState.mock.lastCall?.[0](editorState)?.focus).toEqual({
      blockIndex: 3,
      contentIndex: 0,
      cursorPosition: 7,
      itemIndex: 2,
      itemContentIndex: 0,
    });

    placeCaret(target, "end");
    await user.keyboard("{ArrowRight}");

    expect(setEditorState.mock.lastCall?.[0](editorState)?.focus).toEqual({
      blockIndex: 3,
      contentIndex: 0,
      itemIndex: 3,
      itemContentIndex: 0,
      cursorPosition: 0,
    });
  });
  test("When marked a fritekst-felt, 2 arrow-rights required to move to next editable content", async () => {
    const { user } = setupComplex();
    await user.click(screen.getByText("andre literal"));
    expect(setEditorState.mock.lastCall?.[0](editorState)?.focus).toEqual({
      blockIndex: 0,
      contentIndex: 3,
      cursorPosition: 13,
    });

    await user.keyboard("{ArrowRight}");
    expect(setEditorState.mock.lastCall?.[0](editorState)?.focus).toEqual({
      blockIndex: 0,
      contentIndex: 3,
      cursorPosition: 13,
    });

    await user.keyboard("{ArrowRight}");
    expect(setEditorState.mock.lastCall?.[0](editorState)?.focus).toEqual({
      blockIndex: 2,
      contentIndex: 0,
      cursorPosition: 0,
    });
  });
});

describe("onClickHandler", () => {
  test.each(["first", "middle", "last"])(
    "triple-clicking the %s literal selects only that literal",
    async (position) => {
      const state = letter(paragraph(["first", "middle", "last"].map((name) => literal({ text: `${name} literal` }))));
      const { user } = setupComplex(state);

      await user.tripleClick(screen.getByText(`${position} literal`));

      expect(globalThis.getSelection()?.toString()).toBe(`${position} literal`);
    },
  );

  test("triple-click mousedown prevents native paragraph selection on a non-fritekst literal", () => {
    const state = letter(paragraph(["first", "middle", "last"].map((name) => literal({ text: `${name} literal` }))));
    setupComplex(state);

    const notPrevented = fireEvent.mouseDown(screen.getByText("middle literal"), { detail: 3 });

    expect(notPrevented).toBe(false);
    expect(globalThis.getSelection()?.toString()).toBe("middle literal");
  });

  test("triple-clicking a literal keeps it editable so typing replaces it", async () => {
    const state = letter(paragraph(["first", "middle", "last"].map((name) => literal({ text: `${name} literal` }))));
    const { user } = setupComplex(state);
    const middle = screen.getByText("middle literal");

    await user.tripleClick(middle);
    document.dispatchEvent(new Event("selectionchange"));

    expect(globalThis.getSelection()?.toString()).toBe("middle literal");
    expect(middle.isContentEditable).toBe(true);

    await user.keyboard("x");
    expect(middle.textContent).toBe("x");
  });

  test("triple-clicking literals in headings, list items, and table cells selects only that literal", async () => {
    const state = letter(
      title1(literal({ text: "heading literal" })),
      paragraph([
        itemList({
          items: [item(literal({ text: "first list literal" }), literal({ text: "middle list literal" }))],
        }),
      ]),
      paragraph([
        table(
          [cell(literal({ text: "table heading" }))],
          [row(cell(literal({ text: "first cell literal" }), literal({ text: "middle cell literal" })))],
        ),
      ]),
    );
    const { user } = setupComplex(state);

    await user.tripleClick(screen.getByText("heading literal"));
    expect(globalThis.getSelection()?.toString()).toBe("heading literal");

    await user.tripleClick(screen.getByText("middle list literal"));
    expect(globalThis.getSelection()?.toString()).toBe("middle list literal");

    await user.tripleClick(screen.getByText("middle cell literal"));
    expect(globalThis.getSelection()?.toString()).toBe("middle cell literal");
  });

  test("clicking on a fritekst variable will select the whole element", async () => {
    const { user } = setupComplex();

    await user.click(screen.getByText("andre literal"));
    const selection = globalThis.getSelection();
    expect(selection).not.toBeNull();
    expect(selection?.toString()).toBe("andre literal");
  });

  test("double-clicking on a fritekst variable that has focus, but no selection, should select the whole element", async () => {
    const { user } = setupComplex();
    await user.click(screen.getByText("andre literal"));
    await user.click(screen.getByText("andre literal"));
    const selection = globalThis.getSelection();
    expect(selection?.isCollapsed).toBeTruthy();
    await user.click(screen.getByText("andre literal"));
    expect(selection?.isCollapsed).toBeTruthy();
    expect(selection?.toString()).toBe("");
    await user.dblClick(screen.getByText("andre literal"));
    expect(selection?.isCollapsed).toBeFalsy();
    expect(selection?.toString()).toBe("andre literal");
  });

  test("clicking on a fritekst variable that has been edited should not select the whole element", async () => {
    const state = letter(
      paragraph([
        literal({ text: "første literal" }),
        variable("X"),
        newLiteral({ text: "andre literal", editedText: "Hei på deg", tags: [ElementTags.FRITEKST] }),
      ]),
    );
    const { user } = setupComplex(state);

    await user.click(screen.getByText("Hei på deg"));
    const selection = globalThis.getSelection();
    expect(selection).not.toBeNull();
    expect(selection?.toString()).toBe("");
  });
});

describe("onFocusHandler", () => {
  test("tabbing through a fritkest variable will select the whole element", async () => {
    const { user } = setupComplex();

    await user.click(screen.getByText("Dokumentet starter med variable"));
    expect(globalThis.getSelection()?.isCollapsed).toBe(true);
    await user.tab();
    const selection = globalThis.getSelection();
    expect(selection).not.toBeNull();
    expect(selection?.isCollapsed).toBe(false);
    expect(selection?.focusNode?.textContent).toBe("andre literal");
    const fritekst = screen.getByText("andre literal");
    expect(fritekst.textContent).toBe("andre literal");
    expect(globalThis.getSelection()?.isCollapsed).toBe(false);
    await user.keyboard("abcdef");
    expect(fritekst.textContent).toBe("abcdef");
    expect(globalThis.getSelection()?.isCollapsed).toBe(true);
  });
  test("tabbing through a fritkest variable that has been edited should not be focused", async () => {
    const state = letter(
      paragraph([
        literal({ text: "første literal" }),
        variable("X"),
        newLiteral({
          text: "andre literal",
          editedText: "Denne skal ikke bli fokusert fordi den er redigert",
          tags: [ElementTags.FRITEKST],
        }),
        newLiteral({ text: "tredje literal", tags: [ElementTags.FRITEKST] }),
      ]),
    );
    const { user } = setupComplex(state);

    await user.click(screen.getByText("første literal"));
    await user.tab();
    const selection = globalThis.getSelection();
    expect(selection).not.toBeNull();
    expect(selection?.focusNode?.textContent).toBe("tredje literal");
  });
});

describe("Word Joiner before punctuation-starting literals", () => {
  const WORD_JOINER = "\u2060";

  test("inserts Word Joiner before literals starting with punctuation", () => {
    for (const text of [
      ",tekst",
      ".tekst",
      ";tekst",
      ":tekst",
      "!tekst",
      "?tekst",
      "'tekst",
      '"tekst',
      "(tekst",
      ")tekst",
      "[tekst",
      "]tekst",
      "%",
      "\u00B0C",
      "\u2026",
      "\u00ABtekst",
      "\u00BB",
      "\u201Dtekst",
      "\u2019tekst",
    ]) {
      const state = letter(paragraph([variable("verdi"), literal({ text })]));
      const { container } = setupComplex(state);
      expect(container.textContent).toContain(WORD_JOINER);
    }
  });

  test("does not insert Word Joiner before a literal starting with a letter", () => {
    const state = letter(paragraph([variable("verdi"), literal({ text: " og noe mer tekst" })]));
    const { container } = setupComplex(state);
    expect(container.textContent).not.toContain(WORD_JOINER);
  });

  test("does not insert Word Joiner before a literal starting with a digit", () => {
    const state = letter(paragraph([variable("verdi"), literal({ text: "100 kr" })]));
    const { container } = setupComplex(state);
    expect(container.textContent).not.toContain(WORD_JOINER);
  });

  test("does not insert Word Joiner before the first literal in a block (no preceding content)", () => {
    const state = letter(paragraph([literal({ text: ",bare en literal" })]));
    const { container } = setupComplex(state);
    expect(container.textContent).not.toContain(WORD_JOINER);
  });

  test("inserts Word Joiner before punctuation literal following another literal", () => {
    const state = letter(paragraph([literal({ text: "første" }), literal({ text: ", andre" })]));
    const { container } = setupComplex(state);
    expect(container.textContent).toContain(WORD_JOINER);
  });
});

describe("single-root surface interactions", () => {
  test("typing over a selection spanning literals and a variable replaces the selection atomically", () => {
    const state = letter(
      paragraph([literal({ id: 1, text: "Beløp " }), variable("12 345 kr"), literal({ id: 3, text: " per mnd" })]),
    );
    setupComplex(state);

    const first = screen.getByText("Beløp");
    const second = screen.getByText("per mnd");
    const range = document.createRange();
    range.setStart(first.firstChild!, 3);
    range.setEnd(second.firstChild!, 4);
    const selection = globalThis.getSelection()!;
    selection.removeAllRanges();
    selection.addRange(range);

    const beforeInput = new InputEvent("beforeinput", {
      bubbles: true,
      cancelable: true,
      inputType: "insertText",
      data: "X",
    });
    const allowed = first.dispatchEvent(beforeInput);
    expect(allowed).toBe(false);

    const updated = setEditorState.mock.lastCall?.[0](state);
    expect(updated?.redigertBrev.blocks[0].content).toHaveLength(2);
    expect(updated?.focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 4 });
  });

  test("backspacing the last character in a literal keeps the literal span mounted with zero-width space", async () => {
    const singleCharState = letter(paragraph([literal({ id: 1, text: "x" })]));
    const { user } = setupComplex(singleCharState);
    const span = screen.getByText("x");
    await user.click(span);
    placeCaret(span, "end");
    await user.keyboard("{Backspace}");

    expect(span.isConnected).toBe(true);
    expect(span.textContent).toBe("\u200B");
    expect(setEditorState.mock.lastCall?.[0](singleCharState)).toEqual(
      Actions.updateContentText(singleCharState, { blockIndex: 0, contentIndex: 0 }, "", 0),
    );
  });

  test("IME composition defers state updates until compositionend", () => {
    const state = letter(paragraph([literal({ id: 1, text: "Hei " })]));
    setupComplex(state);
    setEditorState.mockClear();

    const span = screen.getByText("Hei");
    placeCaret(span, "end");
    setEditorState.mockClear();

    fireEvent.compositionStart(span);
    span.firstChild!.textContent = "Hei å";
    placeCaret(span, "end");
    fireEvent.input(span, { isComposing: true });
    expect(setEditorState).not.toHaveBeenCalled();

    fireEvent.compositionEnd(span);
    expect(setEditorState.mock.lastCall?.[0](state)).toEqual(
      Actions.updateContentText(state, { blockIndex: 0, contentIndex: 0 }, "Hei å", 5),
    );
  });

  test("caret at boundary between two literals inserts into the model-focused literal", () => {
    const initial = letter(paragraph([literal({ id: 1, text: "First" }), literal({ id: 2, text: "Second" })]));
    const focusedOnSecondStart: LetterEditorState = {
      ...initial,
      focus: { blockIndex: 0, contentIndex: 1, cursorPosition: 0 },
    };
    const { getState } = setupComplex(focusedOnSecondStart);

    const second = screen.getByText("Second");
    act(() => {
      const allowed = second.dispatchEvent(
        new InputEvent("beforeinput", {
          bubbles: true,
          cancelable: true,
          inputType: "insertText",
          data: "X",
        }),
      );
      expect(allowed).toBe(false);
    });
    expect(screen.getByText("First").textContent).toBe("First");
    expect(second.textContent).toBe("XSecond");
    expect(getState().focus).toEqual({ blockIndex: 0, contentIndex: 1, cursorPosition: 1 });
  });

  test("insertReplacementText uses getTargetRanges() for spellcheck and autocorrect", () => {
    const initial = letter(paragraph([literal({ id: 1, text: "Vi har teh vedtaket" })]));
    const withFocus: LetterEditorState = {
      ...initial,
      focus: { blockIndex: 0, contentIndex: 0, cursorPosition: 10 },
    };
    const { getState } = setupComplex(withFocus);
    const span = screen.getByText("Vi har teh vedtaket");
    const textNode = span.firstChild!;

    const staticRange = {
      startContainer: textNode,
      startOffset: 7,
      endContainer: textNode,
      endOffset: 10,
      collapsed: false,
    } as StaticRange;
    const event = new InputEvent("beforeinput", {
      bubbles: true,
      cancelable: true,
      inputType: "insertReplacementText",
      data: "det",
    });
    Object.defineProperty(event, "getTargetRanges", { value: () => [staticRange] });

    act(() => {
      const allowed = span.dispatchEvent(event);
      expect(allowed).toBe(false);
    });

    expect(span.textContent).toBe("Vi har det vedtaket");
    expect(getState().focus).toEqual({ blockIndex: 0, contentIndex: 0, cursorPosition: 10 });
  });

  test("MutationObserver safety net restores literals and removes stray DOM mutations bypassing beforeinput", async () => {
    const initial = letter(paragraph([literal({ id: 1, text: "Original tekst" })]));
    const { container } = setupComplex(initial);
    const span = screen.getByText("Original tekst");
    const paragraphEl = container.querySelector(".PARAGRAPH")!;

    await act(async () => {
      span.firstChild!.textContent = "Tampered by extension";
      const strayFont = document.createElement("font");
      strayFont.textContent = "injected";
      paragraphEl.appendChild(strayFont);
      await Promise.resolve();
    });

    expect(span.textContent).toBe("Original tekst");
    expect(paragraphEl.querySelector("font")).toBeNull();
  });
});
