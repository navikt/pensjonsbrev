import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import type { Mock } from "vitest";
import { afterEach, describe, expect, test, vi } from "vitest";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { newLiteral } from "~/Brevredigering/LetterEditor/actions/common";
import { MergeTarget } from "~/Brevredigering/LetterEditor/actions/merge";
import { ContentGroup } from "~/Brevredigering/LetterEditor/components/ContentGroup";
import { EditorStateContext } from "~/Brevredigering/LetterEditor/LetterEditor";
import type { CallbackReceiver } from "~/Brevredigering/LetterEditor/lib/actions";
import type { LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import type { LiteralValue, ParagraphBlock } from "~/types/brevbakerTypes";
import { ElementTags, PARAGRAPH } from "~/types/brevbakerTypes";

import { item, itemList, letter, literal, paragraph, variable } from "../../utils";

const content: LiteralValue[] = [newLiteral({ id: 1, text: "Heisann" }), newLiteral({ id: 2, text: "Velkommen" })];

const block: ParagraphBlock = {
  id: 1,
  parentId: null,
  editable: true,
  type: PARAGRAPH,
  deletedContent: [],
  content,
};
const editorState = letter(block, block, block);
const setEditorState: Mock<CallbackReceiver<LetterEditorState>> = vi.fn();

afterEach(() => {
  vi.restoreAllMocks();
});

function setup() {
  return {
    user: userEvent.setup(),
    ...render(
      <EditorStateContext.Provider value={{ freeze: false, error: false, editorState, setEditorState }}>
        <ContentGroup literalIndex={{ blockIndex: 0, contentIndex: 0 }} />
      </EditorStateContext.Provider>,
    ),
  };
}

const complexEditorState = letter(
  paragraph(
    variable("Dokumentet starter med variable"),
    literal({ text: "første literal" }),
    variable("X"),
    literal({ text: "andre literal", tags: [ElementTags.FRITEKST] }),
  ),
  paragraph(variable("Paragraf med kun variable")),
  paragraph(literal({ text: "paragraf med kun tekst" })),
  paragraph(
    itemList({
      items: [
        item(literal({ text: "1. item" })),
        item(variable("2. item variable")),
        item(literal({ text: "3. item" }), variable("med variable")),
        item(literal({ text: "nth item" })),
      ],
    }),
  ),
  paragraph(literal({ text: "Dokumentet avsluttes med literal" })),
);

function setupComplex(stateOverride?: LetterEditorState) {
  return {
    user: userEvent.setup(),
    ...render(
      <EditorStateContext.Provider
        value={{ freeze: false, error: false, editorState: stateOverride ?? complexEditorState, setEditorState }}
      >
        {(stateOverride ?? complexEditorState).redigertBrev.blocks.map((block, blockIndex) => (
          <div className={block.type} key={blockIndex}>
            <ContentGroup literalIndex={{ blockIndex, contentIndex: 0 }} />
          </div>
        ))}
      </EditorStateContext.Provider>,
    ),
  };
}

describe("updateContent", () => {
  test("text changes are propagated", async () => {
    const { user } = setup();
    await user.click(screen.getByText(content[0].text));
    await user.keyboard("{End} person");
    expect(setEditorState).toHaveBeenCalled();
    expect(setEditorState.mock.lastCall?.[0](editorState)).toEqual(
      Actions.updateContentText(editorState, { blockIndex: 0, contentIndex: 0 }, content[0].text + " person"),
    );
  });
  test("enter is not propagated as br-element", async () => {
    const { user } = setup();
    await user.click(screen.getByText(content[0].text));
    await user.keyboard("{End}{Enter}asd");

    // The expectation is that the Enter key does not insert a line break
    // in the final text, so we expect only "asd" to be appended.
    expect(setEditorState.mock.lastCall?.[0](editorState)).toEqual(
      Actions.updateContentText(editorState, { blockIndex: 0, contentIndex: 0 }, content[0].text + "asd"),
    );
  });
  test("space is not propagated as nbsp-entity", async () => {
    const { user } = setup();
    await user.click(screen.getByText(content[0].text));
    await user.keyboard("{End}  asd");

    expect(setEditorState.mock.lastCall?.[0](editorState)).toEqual(
      Actions.updateContentText(editorState, { blockIndex: 0, contentIndex: 0 }, content[0].text + "  asd"),
    );
  });
});

describe("deleteHandler", () => {
  test("delete at end of block, and after last character, triggers merge with next", async () => {
    const { user } = setup();

    await user.click(screen.getByText(content[1].text));
    await user.keyboard("{End}{Delete}");

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
    await user.click(screen.getByText(content[0].text));
    await user.keyboard("{End}{Delete}");

    expect(setEditorState.mock.lastCall?.[0](editorState)).toEqual(editorState);
  });
});

describe("backspaceHandler", () => {
  test("backspace at beginning of block triggers merge with previous", async () => {
    const { user } = setup();
    await user.click(screen.getByText(content[0].text));
    await user.keyboard("{Home}{Backspace}");
    expect(setEditorState.mock.lastCall?.[0](editorState)).toEqual(
      Actions.merge(editorState, { blockIndex: 0, contentIndex: 0 }, MergeTarget.PREVIOUS),
    );
  });
  test("backspace at beginning of block, but not before first character of TextContent, does not trigger merge", async () => {
    const { user } = setup();
    await user.click(screen.getByText(content[0].text));
    await user.keyboard("{End}{ArrowLeft}{ArrowLeft}{Backspace}");

    expect(setEditorState.mock.lastCall?.[0](editorState)?.redigertBrev.blocks).toHaveLength(
      editorState.redigertBrev.blocks.length,
    );
  });
  test("backspace not at beginning of block, but before first character of a TextContent, does not trigger merge", async () => {
    const { user } = setup();
    await user.click(screen.getByText(content[0].text));
    await user.keyboard("{Home}{Backspace}");

    expect(setEditorState.mock.lastCall?.[0](editorState)?.redigertBrev).toBe(editorState.redigertBrev);
  });
});

describe("enterHandler", () => {
  test("enter at the very end of block triggers split with empty text for new block", async () => {
    const { user } = setup();
    await user.click(screen.getByText(content[1].text));
    await user.keyboard("{End}{Enter}");

    expect(setEditorState.mock.lastCall?.[0](editorState)).toEqual(
      Actions.split(editorState, { blockIndex: 0, contentIndex: 1 }, content[1].text.length),
    );
  });
  test("enter not at the end of a content in block triggers split at cursor (text after cursor for new block)", async () => {
    const { user } = setup();
    await user.click(screen.getByText(content[1].text));
    await user.keyboard("{End}{ArrowLeft}{ArrowLeft}{ArrowLeft}{Enter}");

    expect(setEditorState.mock.lastCall?.[0](editorState)).toEqual(
      Actions.split(editorState, { blockIndex: 0, contentIndex: 1 }, content[1].text.length - 3),
    );
  });
  test("enter at the end of an element that is not the last of block triggers split at current element", async () => {
    const { user } = setup();
    await user.click(screen.getByText(content[0].text));
    await user.keyboard("{End}{Enter}");

    expect(setEditorState.mock.lastCall?.[0](editorState)).toEqual(
      Actions.split(editorState, { blockIndex: 0, contentIndex: 0 }, content[0].text.length),
    );
  });

  test("shift-enter will add a newLine instead of a new paragraph", async () => {
    const { user } = setup();
    await user.click(screen.getByText(content[0].text));
    await user.keyboard("{End}{Shift>}{Enter}{/Shift}");

    expect(setEditorState.mock.lastCall?.[0](editorState)).toEqual(
      Actions.addNewLine(editorState, { blockIndex: 0, contentIndex: 0, cursorPosition: "Heisann".length }),
    );
  });
});

describe("ArrowLeft will move focus to previous editable content", () => {
  test("unless already at the start of the document", async () => {
    const { user } = setupComplex();
    await user.click(screen.getByText("første literal"));
    expect(setEditorState.mock.lastCall?.[0](editorState)?.focus).toEqual({
      blockIndex: 0,
      contentIndex: 1,
    });

    await user.keyboard("{Home}{ArrowLeft}");

    expect(setEditorState.mock.lastCall?.[0](editorState)?.focus).toEqual({
      blockIndex: 0,
      contentIndex: 1,
    });
  });

  test("will skip variables in the same block", async () => {
    const { user } = setupComplex();
    await user.click(screen.getByText("andre literal"));
    expect(setEditorState.mock.lastCall?.[0](editorState)?.focus).toEqual({
      blockIndex: 0,
      contentIndex: 3,
    });

    await user.keyboard("{Home}{ArrowLeft}");

    expect(setEditorState.mock.lastCall?.[0](editorState)?.focus).toEqual({
      blockIndex: 0,
      contentIndex: 1,
    });
  });
  test("will skip over a block if it has no editable content", async () => {
    const { user } = setupComplex();
    await user.click(screen.getByText("paragraf med kun tekst"));
    expect(setEditorState.mock.lastCall?.[0](editorState)?.focus).toEqual({
      blockIndex: 2,
      contentIndex: 0,
    });

    await user.keyboard("{Home}{ArrowLeft}");

    expect(setEditorState.mock.lastCall?.[0](editorState)?.focus).toEqual({
      blockIndex: 0,
      contentIndex: 3,
    });
  });
  test("will skip an item if it is not editable", async () => {
    const { user } = setupComplex();
    await user.click(screen.getByText("3. item"));
    expect(setEditorState.mock.lastCall?.[0](editorState)?.focus).toEqual({
      blockIndex: 3,
      contentIndex: 0,
      itemIndex: 2,
      itemContentIndex: 0,
    });

    await user.keyboard("{Home}{ArrowLeft}");

    expect(setEditorState.mock.lastCall?.[0](editorState)?.focus).toEqual({
      blockIndex: 3,
      contentIndex: 0,
      itemIndex: 0,
      itemContentIndex: 0,
    });
  });
  test("When marked a fritekst-felt, 2 arrow-lefts required to move to next editable content", async () => {
    const { user } = setupComplex();
    await user.click(screen.getByText("andre literal"));
    expect(setEditorState.mock.lastCall?.[0](editorState)?.focus).toEqual({
      blockIndex: 0,
      contentIndex: 3,
    });

    await user.keyboard("{ArrowLeft}");
    expect(setEditorState.mock.lastCall?.[0](editorState)?.focus).toEqual({
      blockIndex: 0,
      contentIndex: 3,
    });

    await user.keyboard("{ArrowLeft}");
    expect(setEditorState.mock.lastCall?.[0](editorState)?.focus).toEqual({
      blockIndex: 0,
      contentIndex: 1,
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
    });

    await user.keyboard("{ArrowRight}");

    expect(setEditorState.mock.lastCall?.[0](editorState)?.focus).toEqual({
      blockIndex: 4,
      contentIndex: 0,
    });
  });

  test("will skip variables in the same block", async () => {
    const { user } = setupComplex();
    const element = screen.getByText("første literal");
    await user.click(element);

    // Explicitly set the caret at the end of the element.
    const range = document.createRange();
    range.selectNodeContents(element);
    range.collapse(false); // collapse to the end
    const selection = window.getSelection();
    selection?.removeAllRanges();
    selection?.addRange(range);

    // After clicking, the component's focus should be at { blockIndex: 0, contentIndex: 1 }.
    expect(setEditorState.mock.lastCall?.[0](editorState)?.focus).toEqual({
      blockIndex: 0,
      contentIndex: 1,
    });

    await user.keyboard("{End}{ArrowRight}");

    // Now, with the caret correctly at the end of "første literal",
    // ArrowRight should update the focus to skip variables and move to contentIndex 3.
    expect(setEditorState.mock.lastCall?.[0](editorState)?.focus).toEqual({
      blockIndex: 0,
      contentIndex: 3,
    });
  });
  test("will skip over a block if it has no editable content", async () => {
    const { user } = setupComplex();
    await user.click(screen.getByText("andre literal"));
    expect(setEditorState.mock.lastCall?.[0](editorState)?.focus).toEqual({
      blockIndex: 0,
      contentIndex: 3,
    });

    await user.keyboard("{ArrowRight}");
    await user.keyboard("{ArrowRight}");

    expect(setEditorState.mock.lastCall?.[0](editorState)?.focus).toEqual({
      blockIndex: 2,
      contentIndex: 0,
    });
  });
  test("will skip an item if it is not editable", async () => {
    const { user } = setupComplex();
    await user.click(screen.getByText("3. item"));
    expect(setEditorState.mock.lastCall?.[0](editorState)?.focus).toEqual({
      blockIndex: 3,
      contentIndex: 0,
      itemIndex: 2,
      itemContentIndex: 0,
    });

    await user.keyboard("{End}{ArrowRight}");

    expect(setEditorState.mock.lastCall?.[0](editorState)?.focus).toEqual({
      blockIndex: 3,
      contentIndex: 0,
      itemIndex: 3,
      itemContentIndex: 0,
    });
  });
  test("When marked a fritekst-felt, 2 arrow-rights required to move to next editable content", async () => {
    const { user } = setupComplex();
    await user.click(screen.getByText("andre literal"));
    expect(setEditorState.mock.lastCall?.[0](editorState)?.focus).toEqual({
      blockIndex: 0,
      contentIndex: 3,
    });

    await user.keyboard("{ArrowRight}");
    expect(setEditorState.mock.lastCall?.[0](editorState)?.focus).toEqual({
      blockIndex: 0,
      contentIndex: 3,
    });

    await user.keyboard("{ArrowRight}");
    expect(setEditorState.mock.lastCall?.[0](editorState)?.focus).toEqual({
      blockIndex: 2,
      contentIndex: 0,
    });
  });
});

describe("onClickHandler", () => {
  test("clicking on a fritekst variable will select the whole element", async () => {
    const { user } = setupComplex();

    await user.click(screen.getByText("andre literal"));
    const selection = globalThis.getSelection();
    expect(selection).not.toBeNull();
    expect(selection?.toString()).toBe("andre literal");
  });

  test("clicking on a fritekst variable that has been edited should not select the whole element", async () => {
    const state = letter(
      paragraph(
        literal({ text: "første literal" }),
        variable("X"),
        newLiteral({ text: "andre literal", editedText: "Hei på deg", tags: [ElementTags.FRITEKST] }),
      ),
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
    await user.tab();
    const selection = globalThis.getSelection();
    expect(selection).not.toBeNull();
    expect(selection?.focusNode?.textContent).toBe("andre literal");
  });
  test("tabbing through a fritkest variable that has been edited should not be focused", async () => {
    const state = letter(
      paragraph(
        literal({ text: "første literal" }),
        variable("X"),
        newLiteral({
          text: "andre literal",
          editedText: "Denne skal ikke bli fokusert fordi den er redigert",
          tags: [ElementTags.FRITEKST],
        }),
        newLiteral({ text: "tredje literal", tags: [ElementTags.FRITEKST] }),
      ),
    );
    const { user } = setupComplex(state);

    await user.click(screen.getByText("første literal"));
    await user.tab();
    const selection = globalThis.getSelection();
    expect(selection).not.toBeNull();
    expect(selection?.focusNode?.textContent).toBe("tredje literal");
  });
});
