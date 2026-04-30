import { describe, expect, test } from "vitest";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { newCell, newLiteral, newParagraph, newTable } from "~/Brevredigering/LetterEditor/actions/common";
import { type LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { LITERAL, type LiteralValue, type TextContent } from "~/types/brevbakerTypes";

import { nyBrevResponse, nyRedigertBrev } from "../../../utils/brevredigeringTestUtils";

function asLiteral(content: TextContent): LiteralValue {
  if (content.type !== LITERAL) throw new Error(`Expected LITERAL, got ${content.type}`);
  return content;
}

function tableRow(...texts: string[]) {
  return {
    id: null,
    parentId: null,
    cells: texts.map((text) => newCell([newLiteral({ editedText: text })])),
  };
}

function createEditorState(): LetterEditorState {
  const rows = [tableRow("Rad 1 kolonne 1", "Rad 1 kolonne 2"), tableRow("Rad 2 kolonne 1", "Rad 2 kolonne 2")];
  const brev = nyBrevResponse({
    redigertBrev: nyRedigertBrev({
      blocks: [
        newParagraph({
          content: [newTable(rows)],
        }),
      ],
    }),
  });

  const state = Actions.create(brev);
  const table = state.redigertBrev.blocks[0].content[0];

  if (table.type !== "TABLE") {
    throw new Error("Expected table content in test setup");
  }

  state.focus = {
    blockIndex: 0,
    contentIndex: 0,
    rowIndex: 0,
    cellIndex: 0,
    cellContentIndex: 0,
    cursorPosition: 0,
  };

  return state;
}

function getTable(state: LetterEditorState) {
  const content = state.redigertBrev.blocks[0].content[0];
  if (content.type !== "TABLE") throw new Error("Expected TABLE");
  return content;
}

describe("removeTableRow action", () => {
  test("should delete a row and preserve the table", () => {
    const state = createEditorState();

    // Focus on second row, remove it
    state.focus = { blockIndex: 0, contentIndex: 0, rowIndex: 1, cellIndex: 0, cellContentIndex: 0, cursorPosition: 0 };
    const nextState = Actions.removeTableRow(state);

    const table = getTable(nextState);
    expect(table.rows).toHaveLength(1);
    // First row still exists
    expect(asLiteral(table.rows[0].cells[0].text[0]).editedText).toBe("Rad 1 kolonne 1");
  });

  test("should replace last row with empty row when deleting it", () => {
    const state = createEditorState();

    // Delete second row
    state.focus = { blockIndex: 0, contentIndex: 0, rowIndex: 1, cellIndex: 0, cellContentIndex: 0, cursorPosition: 0 };
    const state2 = Actions.removeTableRow(state);

    // Delete first (now only) row — table should keep one empty row
    state2.focus = {
      blockIndex: 0,
      contentIndex: 0,
      rowIndex: 0,
      cellIndex: 0,
      cellContentIndex: 0,
      cursorPosition: 0,
    };
    const state3 = Actions.removeTableRow(state2);

    const table = getTable(state3);
    expect(table.rows).toHaveLength(1);
    // Row content should be empty
    expect(asLiteral(table.rows[0].cells[0].text[0]).editedText).toBe("");
  });
});
