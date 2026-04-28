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

function createEditorState() {
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

describe("remove table row shortcut", () => {
  it("deletes a filled row with Shift+Backspace and preserves the table when deleting the last row", () => {
    const state = createEditorState();
    cy.mount(<EditorWithState editorState={state} />);

    cy.contains("Rad 2 kolonne 1").click();
    cy.focused().trigger("keydown", { bubbles: true, key: "Backspace", shiftKey: true });

    cy.get("[data-cy=letter-table]").should("exist");
    cy.get("[data-cy=letter-table] tbody tr").should("have.length", 1);

    cy.focused().trigger("keydown", { bubbles: true, key: "Backspace", shiftKey: true });

    cy.get("[data-cy=letter-table]").should("exist");
    cy.get("[data-cy=letter-table] tbody tr").should("have.length", 1);
    cy.get("[data-cy=table-cell-0-0]").should("exist");
    cy.contains("Rad 1 kolonne 1").should("not.exist");
  });

  it("deletes a filled row with Shift+Delete and undo restores it", () => {
    const state = createEditorState();
    cy.mount(<EditorWithState editorState={state} />);

    cy.contains("Rad 1 kolonne 1").click();
    cy.focused().trigger("keydown", { bubbles: true, key: "Delete", shiftKey: true });

    cy.get("[data-cy=letter-table] tbody tr").should("have.length", 1);
    cy.contains("Rad 1 kolonne 1").should("not.exist");
    cy.contains("Rad 2 kolonne 1").should("exist");

    cy.focused().trigger("keydown", { bubbles: true, ctrlKey: true, key: "z" });

    cy.get("[data-cy=letter-table] tbody tr").should("have.length", 2);
    cy.contains("Rad 1 kolonne 1").should("exist");
    cy.contains("Rad 2 kolonne 1").should("exist");
  });

  it("deletes the entire table when Shift+Backspace is used on the last empty row", () => {
    const state = createEditorState();
    cy.mount(<EditorWithState editorState={state} />);

    cy.contains("Rad 1 kolonne 1").click();
    cy.focused().trigger("keydown", { bubbles: true, key: "Backspace", shiftKey: true });
    cy.focused().trigger("keydown", { bubbles: true, key: "Backspace", shiftKey: true });

    cy.get("[data-cy=letter-table]").should("exist");
    cy.get("[data-cy=letter-table] tbody tr").should("have.length", 1);

    cy.focused().trigger("keydown", { bubbles: true, key: "Backspace", shiftKey: true });

    cy.get("[data-cy=letter-table]").should("not.exist");
  });
});

describe("move table row shortcut", () => {
  it("moves a row down with Alt+Shift+ArrowDown", () => {
    const state = createEditorState();
    cy.mount(<EditorWithState editorState={state} />);

    cy.contains("Rad 1 kolonne 1").click();
    cy.focused().trigger("keydown", { bubbles: true, key: "ArrowDown", altKey: true, shiftKey: true });

    cy.get("[data-cy=letter-table] tbody tr").eq(0).should("contain.text", "Rad 2 kolonne 1");
    cy.get("[data-cy=letter-table] tbody tr").eq(1).should("contain.text", "Rad 1 kolonne 1");
  });

  it("moves a row up with Alt+Shift+ArrowUp", () => {
    const state = createEditorState();
    cy.mount(<EditorWithState editorState={state} />);

    cy.contains("Rad 2 kolonne 1").click();
    cy.focused().trigger("keydown", { bubbles: true, key: "ArrowUp", altKey: true, shiftKey: true });

    cy.get("[data-cy=letter-table] tbody tr").eq(0).should("contain.text", "Rad 2 kolonne 1");
    cy.get("[data-cy=letter-table] tbody tr").eq(1).should("contain.text", "Rad 1 kolonne 1");
  });

  it("does not move the first row up", () => {
    const state = createEditorState();
    cy.mount(<EditorWithState editorState={state} />);

    cy.contains("Rad 1 kolonne 1").click();
    cy.focused().trigger("keydown", { bubbles: true, key: "ArrowUp", altKey: true, shiftKey: true });

    cy.get("[data-cy=letter-table] tbody tr").eq(0).should("contain.text", "Rad 1 kolonne 1");
    cy.get("[data-cy=letter-table] tbody tr").eq(1).should("contain.text", "Rad 2 kolonne 1");
  });

  it("does not move the last row down", () => {
    const state = createEditorState();
    cy.mount(<EditorWithState editorState={state} />);

    cy.contains("Rad 2 kolonne 1").click();
    cy.focused().trigger("keydown", { bubbles: true, key: "ArrowDown", altKey: true, shiftKey: true });

    cy.get("[data-cy=letter-table] tbody tr").eq(0).should("contain.text", "Rad 1 kolonne 1");
    cy.get("[data-cy=letter-table] tbody tr").eq(1).should("contain.text", "Rad 2 kolonne 1");
  });

  it("undo restores the original row order", () => {
    const state = createEditorState();
    cy.mount(<EditorWithState editorState={state} />);

    cy.contains("Rad 1 kolonne 1").click();
    cy.focused().trigger("keydown", { bubbles: true, key: "ArrowDown", altKey: true, shiftKey: true });

    cy.get("[data-cy=letter-table] tbody tr").eq(0).should("contain.text", "Rad 2 kolonne 1");
    cy.get("[data-cy=letter-table] tbody tr").eq(1).should("contain.text", "Rad 1 kolonne 1");

    cy.focused().trigger("keydown", { bubbles: true, ctrlKey: true, key: "z" });

    cy.get("[data-cy=letter-table] tbody tr").eq(0).should("contain.text", "Rad 1 kolonne 1");
    cy.get("[data-cy=letter-table] tbody tr").eq(1).should("contain.text", "Rad 2 kolonne 1");
  });
});
