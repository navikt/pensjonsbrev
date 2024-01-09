// eslint-disable-next-line testing-library/no-manual-cleanup
import { cleanup, render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { afterEach, beforeEach, describe, expect, test, vi } from "vitest";

import Actions from "~/pages/Brevredigering/LetterEditor/actions";
import { ContentGroup } from "~/pages/Brevredigering/LetterEditor/components/ContentGroup";
import type { LetterEditorState } from "~/pages/Brevredigering/LetterEditor/model/state";
import type { LiteralValue, ParagraphBlock } from "~/types/brevbakerTypes";
import { LITERAL, PARAGRAPH } from "~/types/brevbakerTypes";

import { boundActionStub } from "../../../../testUtils";
import { letter } from "../../utils";

const content: LiteralValue[] = [
  { type: LITERAL, id: 1, text: "Heisann" },
  { type: LITERAL, id: 2, text: "Velkommen" },
];

const block: ParagraphBlock = {
  id: 1,
  editable: true,
  type: PARAGRAPH,
  content,
};
const state: LetterEditorState = letter(block, block, block);

const user = userEvent.setup();
const updateLetter = vi.fn<[(l: LetterEditorState) => LetterEditorState]>();

const contentGroup = (
  <ContentGroup
    content={content}
    editable
    focusStolen={boundActionStub}
    id={{ blockId: 1 }}
    onFocus={boundActionStub}
    stealFocus={undefined}
    updateLetter={updateLetter}
  />
);

beforeEach(() => {
  // eslint-disable-next-line testing-library/no-render-in-lifecycle
  render(contentGroup);
});

afterEach(() => {
  cleanup();
  vi.restoreAllMocks();
});

describe("updateContent", () => {
  test("text changes are propagated", async () => {
    const firstSpan = screen.getByText(content[0].text);
    await user.click(firstSpan);
    await user.keyboard(" person");
    expect(updateLetter).toHaveBeenCalled();
    expect(updateLetter.mock.lastCall?.[0](state)).toEqual(
      Actions.updateContentText(state, { blockId: 1, contentId: 0 }, content[0].text + " person"),
    );
  });
  test("enter is not propagated as br-element", async () => {
    const firstSpan = screen.getByText(content[0].text);

    await user.click(firstSpan);
    await user.keyboard("{Enter}asd");

    // Enter does cause a splitAction-event but we're only rendering the original block, so the focus is still in the span we clicked.
    expect(updateLetter.mock.lastCall?.[0](state)).toEqual(
      Actions.updateContentText(state, { blockId: 1, contentId: 0 }, content[0].text + "asd"),
    );
  });
  test("space is not propagated as nbsp-entity", async () => {
    const firstSpan = screen.getByText(content[0].text);

    await user.click(firstSpan);
    await user.keyboard("  asd");

    expect(updateLetter.mock.lastCall?.[0](state)).toEqual(
      Actions.updateContentText(state, { blockId: 1, contentId: 0 }, content[0].text + "  asd"),
    );
  });
});

describe("deleteHandler", () => {
  test("delete at end of group, and after last character, triggers merge with next", async () => {
    const lastSpan = screen.getByText(content[1].text);
    await user.click(lastSpan);
    await user.keyboard("{Delete}");

    expect(updateLetter).toHaveBeenCalledOnce();
  });
  test("delete at end of group, but not after last character, does not trigger merge", async () => {
    const lastSpan = screen.getByText(content[1].text);
    await user.click(lastSpan);
    await user.keyboard("{ArrowLeft}{ArrowLeft}{Delete}");

    expect(updateLetter).toHaveBeenCalledOnce();
    expect(updateLetter.mock.lastCall?.[0](state).editedLetter.letter.blocks).toHaveLength(
      state.editedLetter.letter.blocks.length,
    );
  });
  test("delete not at end of group, but after last character, does not trigger merge", async () => {
    const firstSpan = screen.getByText(content[0].text);
    await user.click(firstSpan);
    await user.keyboard("{End}{Delete}");

    // since this doesn't cause any text updates, there should not be any updates
    expect(updateLetter).not.toHaveBeenCalled();
  });
});

describe("backspaceHandler", () => {
  test("backspace at beginning of group triggers merge with previous", async () => {
    const firstSpan = screen.getByText(content[0].text);
    await user.click(firstSpan);
    await user.keyboard("{Home}{Backspace}");

    expect(updateLetter).toHaveBeenCalledOnce();
  });
  test("backspace at beginning of group, but not before first character of TextContent, does not trigger merge", async () => {
    const firstSpan = screen.getByText(content[0].text);
    await user.click(firstSpan);
    await user.keyboard("{End}{ArrowLeft}{ArrowLeft}{Backspace}");

    expect(updateLetter.mock.lastCall?.[0](state).editedLetter.letter.blocks).toHaveLength(
      state.editedLetter.letter.blocks.length,
    );
  });
  test("backspace not at beginning of group, but before first character of a TextContent, does not trigger merge", async () => {
    const firstSpan = screen.getByText(content[1].text);
    await user.click(firstSpan);
    await user.keyboard("{Home}{Backspace}");

    expect(updateLetter).not.toHaveBeenCalled();
  });
});

describe("enterHandler", () => {
  test("enter at the very end of group triggers split with empty text for new group", async () => {
    const span = screen.getByText(content[1].text);

    await user.click(span);
    await user.keyboard("{End}{Enter}");

    expect(updateLetter.mock.lastCall?.[0](state)).toEqual(
      Actions.split(state, { blockId: 1, contentId: 1 }, content[1].text.length),
    );
  });
  test("enter not at the end of a content in group triggers split at cursor (text after cursor for new group)", async () => {
    const text = content[1].text;
    const span = screen.getByText(text);

    await user.click(span);
    await user.keyboard("{End}{ArrowLeft}{ArrowLeft}{ArrowLeft}{Enter}");

    expect(updateLetter.mock.lastCall?.[0](state)).toEqual(
      Actions.split(state, { blockId: 1, contentId: 1 }, content[1].text.length - 3),
    );
  });
  test("enter at the end of an element that is not the last of group triggers split at current element", async () => {
    const span = screen.getByText(content[0].text);

    await user.click(span);
    await user.keyboard("{End}{Enter}");

    expect(updateLetter.mock.lastCall?.[0](state)).toEqual(
      Actions.split(state, { blockId: 1, contentId: 0 }, content[0].text.length),
    );
  });
});
