import { fireEvent, screen } from "@testing-library/react";
import { describe, expect, test } from "vitest";

import { literal, paragraph, stateV2 } from "~test/support/editorV2TestUtils";
import { at, block, renderWithDispatchSpy, selectText } from "~test/support/editorV2ViewTestUtils";

import { moveFocus } from "../../commands/updateText";
import { EditableLiteral } from "./EditableLiteral";

function renderLiteral() {
  const state = stateV2(paragraph(literal("Test tekst")));
  const content = state.redigertBrev.blocks[0];
  if (content.type !== "PARAGRAPH" || content.content[0].type !== "LITERAL") throw new Error("Expected a literal");
  return renderWithDispatchSpy(state, <EditableLiteral index={0} literal={content.content[0]} path={block(0)} />);
}

describe("EditableLiteral keeps the user's selection before text-mutating keys", () => {
  test("keeps a mouse selection and does not move focus when a range is selected", () => {
    const { dispatch } = renderLiteral();
    const span = screen.getByText("Test tekst");
    selectText(span, 0, 4);

    fireEvent.keyDown(span, { key: "a" });

    const selection = globalThis.getSelection();
    expect(selection?.isCollapsed).toBe(false);
    expect(selection?.getRangeAt(0).startOffset).toBe(0);
    expect(selection?.getRangeAt(0).endOffset).toBe(4);
    expect(dispatch).not.toHaveBeenCalledWith(moveFocus, expect.anything());
  });

  test("moves focus to the caret when only a caret is present", () => {
    const { dispatch } = renderLiteral();
    const span = screen.getByText("Test tekst");
    selectText(span, 4);

    fireEvent.keyDown(span, { key: "a" });

    const selection = globalThis.getSelection();
    expect(selection?.isCollapsed).toBe(true);
    expect(selection?.getRangeAt(0).startOffset).toBe(4);
    expect(dispatch).toHaveBeenLastCalledWith(moveFocus, at(block(0), 0, 4));
  });
});
