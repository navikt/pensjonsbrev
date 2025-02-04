import { useState } from "react";

import {
  nyBrevResponse,
  nyLiteral,
  nyRedigertBrev,
  nyTitle1Block,
  nyTitle2Block,
} from "../../../../cypress/utils/brevredigeringTestUtils";
import Actions from "../actions";
import { newParagraph } from "../actions/common";
import { LetterEditor } from "../LetterEditor";
import type { LetterEditorState } from "../model/state";
import { getRange } from "../services/caretUtils";

const EditorWithState = () => {
  const brevresponse = nyBrevResponse({
    redigertBrev: nyRedigertBrev({
      blocks: [
        nyTitle1Block({ content: [nyLiteral({ text: "Dette er en title1 block" })] }),
        nyTitle2Block({ content: [nyLiteral({ text: "Dette er en title2 block" })] }),
        newParagraph({ content: [nyLiteral({ text: "Dette er en paragraph block" })] }),
      ],
    }),
  });
  const newState = Actions.create(brevresponse);
  const [editorState, setEditorState] = useState<LetterEditorState>(newState);

  return (
    <LetterEditor
      editorState={editorState}
      error={false}
      freeze={false}
      setEditorState={setEditorState}
      showDebug={false}
    />
  );
};

describe("Typography", () => {
  beforeEach(() => {
    cy.viewport(800, 1400);
  });

  describe("overskrift", () => {
    describe("manuelt", () => {
      it("toggler av/på", () => {
        cy.mount(<EditorWithState />);
        cy.contains("title1").click();
        cy.getDataCy("typography-select").contains("Overskrift (alt+2)").should("be.selected");
        cy.getDataCy("typography-select").select("Normal (alt+1)");
        cy.getDataCy("typography-select").contains("Normal (alt+1)").should("be.selected");
        cy.contains("title1").should("have.css", "font-size", "18px");
        cy.getDataCy("typography-select").select("Overskrift (alt+2)");
        cy.getDataCy("typography-select").contains("Overskrift (alt+2)").should("be.selected");
        cy.contains("title1").should("have.css", "font-size", "24px");
      });
      it("caret holder seg i samme posisjon ved toggle", () => {
        cy.mount(<EditorWithState />);
        cy.contains("title1").click().type("{leftArrow}{leftArrow}");
        cy.getDataCy("typography-select").select("Normal (alt+1)");
        assertCaret("title1", 22);
      });
    });
    describe("shortcut", () => {
      it("toggler av/på", () => {
        cy.mount(<EditorWithState />);
        cy.contains("title1").click();
        cy.getDataCy("typography-select").contains("Overskrift (alt+2)").should("be.selected");
        cy.contains("title1").type("{alt}1");
        cy.getDataCy("typography-select").contains("Normal (alt+1)").should("be.selected");
        cy.contains("title1").should("have.css", "font-size", "18px");
        cy.contains("title1").type("{alt}2");
        cy.getDataCy("typography-select").contains("Overskrift (alt+2)").should("be.selected");
        cy.contains("title1").should("have.css", "font-size", "24px");
      });
      it("caret holder seg i samme posisjon ved toggle", () => {
        cy.mount(<EditorWithState />);
        cy.contains("title1").click();
        cy.contains("title1").type("{leftArrow}{leftArrow}{alt}1");
        assertCaret("title1", 22);
      });
    });
  });
  describe("underoverskrift", () => {
    describe("manuelt", () => {
      it("toggler av/på", () => {
        cy.mount(<EditorWithState />);
        cy.contains("title2").click();
        cy.getDataCy("typography-select").contains("Underoverskrift (alt+3)").should("be.selected");
        cy.getDataCy("typography-select").select("Normal (alt+1)");
        cy.getDataCy("typography-select").contains("Normal (alt+1)").should("be.selected");
        cy.contains("title2").should("have.css", "font-size", "18px");
        cy.getDataCy("typography-select").select("Underoverskrift (alt+3)");
        cy.getDataCy("typography-select").contains("Underoverskrift (alt+3)").should("be.selected");
        cy.contains("title2").should("have.css", "font-size", "20px");
      });
      it("caret holder seg i samme posisjon ved toggle", () => {
        cy.mount(<EditorWithState />);
        cy.contains("title2").click().type("{leftArrow}{leftArrow}");
        cy.getDataCy("typography-select").select("Normal (alt+1)");
        assertCaret("title2", 22);
      });
    });
    describe("shortcut", () => {
      it("toggler av/på", () => {
        cy.mount(<EditorWithState />);
        cy.contains("title2").click();
        cy.getDataCy("typography-select").contains("Underoverskrift (alt+3)").should("be.selected");
        cy.contains("title2").type("{alt}1");
        cy.getDataCy("typography-select").contains("Normal (alt+1)").should("be.selected");
        cy.contains("title2").should("have.css", "font-size", "18px");
        cy.contains("title2").type("{alt}3");
        cy.getDataCy("typography-select").contains("Underoverskrift (alt+3)").should("be.selected");
        cy.contains("title2").should("have.css", "font-size", "20px");
      });
      it("caret holder seg i samme posisjon ved toggle", () => {
        cy.mount(<EditorWithState />);
        cy.contains("title2").click();
        cy.contains("title2").type("{leftArrow}{leftArrow}{alt}1");
        assertCaret("title2", 22);
      });
    });
  });
  describe("normal", () => {
    describe("manuelt", () => {
      it("toggler av/på", () => {
        cy.mount(<EditorWithState />);
        cy.contains("paragraph").click();
        cy.getDataCy("typography-select").contains("Normal (alt+1)").should("be.selected");
        cy.getDataCy("typography-select").select("Overskrift (alt+2)");
        cy.getDataCy("typography-select").contains("Overskrift (alt+2)").should("be.selected");
        cy.contains("paragraph").should("have.css", "font-size", "24px");
        cy.getDataCy("typography-select").select("Normal (alt+1)");
        cy.getDataCy("typography-select").contains("Normal (alt+1)").should("be.selected");
        cy.contains("paragraph").should("have.css", "font-size", "18px");
      });
      it("caret holder seg i samme posisjon ved toggle", () => {
        cy.mount(<EditorWithState />);
        cy.contains("paragraph").click().type("{leftArrow}{leftArrow}");
        cy.getDataCy("typography-select").select("Overskrift (alt+2)");
        assertCaret("paragraph", 25);
      });
    });
    describe("shortcut", () => {
      it("toggler av/på", () => {
        cy.mount(<EditorWithState />);
        cy.contains("paragraph").click();
        cy.getDataCy("typography-select").contains("Normal (alt+1)").should("be.selected");
        cy.contains("paragraph").type("{alt}2");
        cy.getDataCy("typography-select").contains("Overskrift (alt+2)").should("be.selected");
        cy.contains("paragraph").should("have.css", "font-size", "24px");
        cy.contains("paragraph").type("{alt}1");
        cy.getDataCy("typography-select").contains("Normal (alt+1)").should("be.selected");
        cy.contains("paragraph").should("have.css", "font-size", "18px");
      });
      it("caret holder seg i samme posisjon ved toggle", () => {
        cy.mount(<EditorWithState />);
        cy.contains("paragraph").click();
        cy.contains("paragraph").type("{leftArrow}{leftArrow}{alt}2");
        assertCaret("paragraph", 25);
      });
    });
  });
});

function assertCaret(content: string, caretOffset: number) {
  cy.get(".editor").then(() => {
    cy.focused().contains(content);
    expect(getRange()?.startOffset).to.eq(caretOffset);
  });
}
