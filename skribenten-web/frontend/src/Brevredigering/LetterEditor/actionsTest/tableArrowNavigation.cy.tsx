import { useState } from "react";

import { nyBrevResponse, nyRedigertBrev } from "../../../../cypress/utils/brevredigeringTestUtils";
import Actions from "../actions";
import { newCell, newLiteral, newParagraph, newTable } from "../actions/common";
import { LetterEditor } from "../LetterEditor";
import { type LetterEditorState } from "../model/state";

function EditorWithState({ editorState }: { editorState: LetterEditorState }) {
  const [state, setState] = useState(editorState);
  return <LetterEditor editorState={state} error={false} freeze={false} setEditorState={setState} showDebug={false} />;
}

function tableRow(...texts: string[]) {
  return {
    id: null,
    parentId: null,
    cells: texts.map((text) => newCell([newLiteral({ editedText: text })])),
  };
}

function arrowDown() {
  cy.focused().trigger("keydown", { bubbles: true, key: "ArrowDown" });
}

function arrowUp() {
  cy.focused().trigger("keydown", { bubbles: true, key: "ArrowUp" });
}

function assertFocusedCell(dataCy: string) {
  cy.get(`[data-cy="${dataCy}"] [contenteditable]`).should("be.focused");
}

function assertFocusNotInTable() {
  cy.focused().parents("[data-cy^='table-cell'], [data-cy^='table-header']").should("have.length", 0);
}

describe("table ArrowDown navigation", () => {
  beforeEach(() => {
    cy.viewport(800, 1400);
    cy.document().then((doc) => doc.fonts.ready);
  });

  it("moves focus from header to first body row", () => {
    const rows = [tableRow("R0C0", "R0C1"), tableRow("R1C0", "R1C1")];
    const brev = nyBrevResponse({
      redigertBrev: nyRedigertBrev({
        blocks: [newParagraph({ content: [newTable(rows)] })],
      }),
    });
    const state = Actions.create(brev);
    state.focus = {
      blockIndex: 0,
      contentIndex: 0,
      rowIndex: -1,
      cellIndex: 0,
      cellContentIndex: 0,
      cursorPosition: 0,
    };
    cy.mount(<EditorWithState editorState={state} />);

    assertFocusedCell("table-header-0");
    arrowDown();
    assertFocusedCell("table-cell-0-0");
  });

  it("moves focus down through body rows", () => {
    const rows = [tableRow("R0C0"), tableRow("R1C0"), tableRow("R2C0")];
    const brev = nyBrevResponse({
      redigertBrev: nyRedigertBrev({
        blocks: [newParagraph({ content: [newTable(rows)] })],
      }),
    });
    const state = Actions.create(brev);
    state.focus = { blockIndex: 0, contentIndex: 0, rowIndex: 0, cellIndex: 0, cellContentIndex: 0, cursorPosition: 0 };
    cy.mount(<EditorWithState editorState={state} />);

    assertFocusedCell("table-cell-0-0");
    arrowDown();
    assertFocusedCell("table-cell-1-0");
    arrowDown();
    assertFocusedCell("table-cell-2-0");
  });

  it("preserves cellIndex when moving down between rows", () => {
    const rows = [tableRow("R0C0", "R0C1"), tableRow("R1C0", "R1C1")];
    const brev = nyBrevResponse({
      redigertBrev: nyRedigertBrev({
        blocks: [newParagraph({ content: [newTable(rows)] })],
      }),
    });
    const state = Actions.create(brev);
    state.focus = {
      blockIndex: 0,
      contentIndex: 0,
      rowIndex: -1,
      cellIndex: 1,
      cellContentIndex: 0,
      cursorPosition: 0,
    };
    cy.mount(<EditorWithState editorState={state} />);

    assertFocusedCell("table-header-1");
    arrowDown();
    assertFocusedCell("table-cell-0-1");
  });

  it("does not insert a blank literal when pressing ArrowDown on the last row of a table that is the last element in the document", () => {
    const rows = [tableRow("R0C0"), tableRow("R1C0")];
    const brev = nyBrevResponse({
      redigertBrev: nyRedigertBrev({
        blocks: [newParagraph({ content: [newTable(rows)] })],
      }),
    });
    const state = Actions.create(brev);
    state.focus = { blockIndex: 0, contentIndex: 0, rowIndex: 1, cellIndex: 0, cellContentIndex: 0, cursorPosition: 0 };
    cy.mount(<EditorWithState editorState={state} />);

    arrowDown();
    // Focus must remain inside the table; no new editable span should appear outside it.
    assertFocusedCell("table-cell-1-0");
  });

  it("exits table forward into the next literal when pressing ArrowDown on the last row", () => {
    const rows = [tableRow("R0C0"), tableRow("R1C0")];
    const brev = nyBrevResponse({
      redigertBrev: nyRedigertBrev({
        blocks: [
          newParagraph({
            content: [newTable(rows), newLiteral({ editedText: "After table" })],
          }),
        ],
      }),
    });
    const state = Actions.create(brev);
    state.focus = { blockIndex: 0, contentIndex: 0, rowIndex: 1, cellIndex: 0, cellContentIndex: 0, cursorPosition: 0 };
    cy.mount(<EditorWithState editorState={state} />);

    arrowDown();
    cy.focused().should("have.text", "After table");
  });

  it("inserts a blank literal between adjacent tables and focuses it when pressing ArrowDown on the last row of the first table", () => {
    const rows1 = [tableRow("T1R0"), tableRow("T1R1")];
    const rows2 = [tableRow("T2R0"), tableRow("T2R1")];
    const brev = nyBrevResponse({
      redigertBrev: nyRedigertBrev({
        blocks: [newParagraph({ content: [newTable(rows1), newTable(rows2)] })],
      }),
    });
    const state = Actions.create(brev);
    state.focus = { blockIndex: 0, contentIndex: 0, rowIndex: 1, cellIndex: 0, cellContentIndex: 0, cursorPosition: 0 };
    cy.mount(<EditorWithState editorState={state} />);

    arrowDown();
    assertFocusNotInTable();
  });

  it("enters table header when pressing ArrowDown on a literal directly preceding the table", () => {
    const rows = [tableRow("R0C0"), tableRow("R1C0")];
    const brev = nyBrevResponse({
      redigertBrev: nyRedigertBrev({
        blocks: [
          newParagraph({
            content: [newLiteral({ editedText: "Before table" }), newTable(rows)],
          }),
        ],
      }),
    });
    const state = Actions.create(brev);
    state.focus = { blockIndex: 0, contentIndex: 0, cursorPosition: "Before table".length };
    cy.mount(<EditorWithState editorState={state} />);

    cy.contains("Before table").should("be.focused");
    arrowDown();
    assertFocusedCell("table-header-0");
  });
});

describe("table ArrowUp navigation", () => {
  beforeEach(() => {
    cy.viewport(800, 1400);
    cy.document().then((doc) => doc.fonts.ready);
  });

  it("moves focus from first body row to header", () => {
    const rows = [tableRow("R0C0", "R0C1"), tableRow("R1C0", "R1C1")];
    const brev = nyBrevResponse({
      redigertBrev: nyRedigertBrev({
        blocks: [newParagraph({ content: [newTable(rows)] })],
      }),
    });
    const state = Actions.create(brev);
    state.focus = { blockIndex: 0, contentIndex: 0, rowIndex: 0, cellIndex: 0, cellContentIndex: 0, cursorPosition: 0 };
    cy.mount(<EditorWithState editorState={state} />);

    assertFocusedCell("table-cell-0-0");
    arrowUp();
    assertFocusedCell("table-header-0");
  });

  it("moves focus up through body rows", () => {
    const rows = [tableRow("R0C0"), tableRow("R1C0"), tableRow("R2C0")];
    const brev = nyBrevResponse({
      redigertBrev: nyRedigertBrev({
        blocks: [newParagraph({ content: [newTable(rows)] })],
      }),
    });
    const state = Actions.create(brev);
    state.focus = { blockIndex: 0, contentIndex: 0, rowIndex: 2, cellIndex: 0, cellContentIndex: 0, cursorPosition: 0 };
    cy.mount(<EditorWithState editorState={state} />);

    assertFocusedCell("table-cell-2-0");
    arrowUp();
    assertFocusedCell("table-cell-1-0");
    arrowUp();
    assertFocusedCell("table-cell-0-0");
  });

  it("preserves cellIndex when moving up between rows", () => {
    const rows = [tableRow("R0C0", "R0C1"), tableRow("R1C0", "R1C1")];
    const brev = nyBrevResponse({
      redigertBrev: nyRedigertBrev({
        blocks: [newParagraph({ content: [newTable(rows)] })],
      }),
    });
    const state = Actions.create(brev);
    state.focus = { blockIndex: 0, contentIndex: 0, rowIndex: 1, cellIndex: 1, cellContentIndex: 0, cursorPosition: 0 };
    cy.mount(<EditorWithState editorState={state} />);

    assertFocusedCell("table-cell-1-1");
    arrowUp();
    assertFocusedCell("table-cell-0-1");
    arrowUp();
    assertFocusedCell("table-header-1");
  });

  it("exits table backward into the preceding literal when pressing ArrowUp on the header", () => {
    const rows = [tableRow("R0C0"), tableRow("R1C0")];
    const brev = nyBrevResponse({
      redigertBrev: nyRedigertBrev({
        blocks: [
          newParagraph({
            content: [newLiteral({ editedText: "Before table" }), newTable(rows)],
          }),
        ],
      }),
    });
    const state = Actions.create(brev);
    state.focus = {
      blockIndex: 0,
      contentIndex: 1,
      rowIndex: -1,
      cellIndex: 0,
      cellContentIndex: 0,
      cursorPosition: 0,
    };
    cy.mount(<EditorWithState editorState={state} />);

    arrowUp();
    cy.focused().should("have.text", "Before table");
  });

  it("inserts a blank literal between adjacent tables and focuses it when pressing ArrowUp on the header of the second table", () => {
    const rows1 = [tableRow("T1R0"), tableRow("T1R1")];
    const rows2 = [tableRow("T2R0"), tableRow("T2R1")];
    const brev = nyBrevResponse({
      redigertBrev: nyRedigertBrev({
        blocks: [newParagraph({ content: [newTable(rows1), newTable(rows2)] })],
      }),
    });
    const state = Actions.create(brev);
    state.focus = {
      blockIndex: 0,
      contentIndex: 1,
      rowIndex: -1,
      cellIndex: 0,
      cellContentIndex: 0,
      cursorPosition: 0,
    };
    cy.mount(<EditorWithState editorState={state} />);

    arrowUp();
    assertFocusNotInTable();
  });

  it("enters table last row when pressing ArrowUp on a literal directly following the table", () => {
    const rows = [tableRow("R0C0"), tableRow("R1C0")];
    const brev = nyBrevResponse({
      redigertBrev: nyRedigertBrev({
        blocks: [
          newParagraph({
            content: [newTable(rows), newLiteral({ editedText: "After table" })],
          }),
        ],
      }),
    });
    const state = Actions.create(brev);
    state.focus = { blockIndex: 0, contentIndex: 1, cursorPosition: 0 };
    cy.mount(<EditorWithState editorState={state} />);

    cy.contains("After table").should("be.focused");
    arrowUp();
    assertFocusedCell("table-cell-1-0");
  });
});
