import { useState } from "react";

import {
  nyBrevResponse,
  nyLiteral,
  nyRedigertBrev,
  nyTitle1Block,
  nyTitle2Block,
  nyTitle3Block,
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
        nyTitle3Block({ content: [nyLiteral({ text: "Dette er en title3 block" })] }),
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

  describe("overskrift 1", () => {
    describe("manuelt", () => {
      it("toggler av/på", () => {
        cy.mount(<EditorWithState />);
        cy.contains("title1").click();
        cy.getDataCy("typography-select").contains("Overskrift 1 (Alt+1)").should("be.selected");
        cy.getDataCy("typography-select").select("Normal (Alt+4)");
        cy.getDataCy("typography-select").contains("Normal (Alt+4)").should("be.selected");
        cy.get('.PARAGRAPH:contains("Dette er en title1 block")').should("be.visible");
        cy.getDataCy("typography-select").select("Overskrift 1 (Alt+1)");
        cy.getDataCy("typography-select").contains("Overskrift 1 (Alt+1)").should("be.selected");
        cy.get('.TITLE1:contains("Dette er en title1 block")').should("be.visible");
      });
      it("caret holder seg i samme posisjon ved toggle", () => {
        cy.mount(<EditorWithState />);
        cy.contains("title1").click().type("{leftArrow}{leftArrow}");
        cy.getDataCy("typography-select").select("Normal (Alt+4)");
        assertCaret("title1", 22);
      });
    });
    describe("shortcut", () => {
      it("toggler av/på", () => {
        cy.mount(<EditorWithState />);
        cy.contains("title1").click();
        cy.getDataCy("typography-select").contains("Overskrift 1 (Alt+1)").should("be.selected");
        cy.contains("title1").type("{alt}4");
        cy.getDataCy("typography-select").contains("Normal (Alt+4)").should("be.selected");
        cy.get('.PARAGRAPH:contains("Dette er en title1 block")').should("be.visible");
        cy.contains("title1").type("{alt}1");
        cy.getDataCy("typography-select").contains("Overskrift 1 (Alt+1)").should("be.selected");
        cy.get('.TITLE1:contains("Dette er en title1 block")').should("be.visible");
      });
      it("caret holder seg i samme posisjon ved toggle", () => {
        cy.mount(<EditorWithState />);
        cy.contains("title1").click();
        cy.contains("title1").type("{leftArrow}{leftArrow}{alt}1");
        assertCaret("title1", 22);
      });
    });
  });
  describe("overskrift 2", () => {
    describe("manuelt", () => {
      it("toggler av/på", () => {
        cy.mount(<EditorWithState />);
        cy.contains("title2").click();
        cy.getDataCy("typography-select").contains("Overskrift 2 (Alt+2)");
        cy.getDataCy("typography-select").select("Normal (Alt+4)");
        cy.getDataCy("typography-select").contains("Normal (Alt+4)").should("be.selected");
        cy.get('.PARAGRAPH:contains("Dette er en title2 block")').should("be.visible");
        cy.getDataCy("typography-select").select("Overskrift 2 (Alt+2)");
        cy.getDataCy("typography-select").contains("Overskrift 2 (Alt+2)").should("be.selected");
        cy.get('.TITLE2:contains("Dette er en title2 block")').should("be.visible");
      });
      it("caret holder seg i samme posisjon ved toggle", () => {
        cy.mount(<EditorWithState />);
        cy.contains("title2").click().type("{leftArrow}{leftArrow}");
        cy.getDataCy("typography-select").select("Normal (Alt+4)");
        assertCaret("title2", 22);
      });
    });
    describe("shortcut", () => {
      it("toggler av/på", () => {
        cy.mount(<EditorWithState />);
        cy.contains("title2").click();
        cy.getDataCy("typography-select").contains("Overskrift 2 (Alt+2)").should("be.selected");
        cy.contains("title2").type("{alt}4");
        cy.getDataCy("typography-select").contains("Normal (Alt+4)").should("be.selected");
        cy.get('.PARAGRAPH:contains("Dette er en title2 block")').should("be.visible");
        cy.contains("title2").type("{alt}2");
        cy.getDataCy("typography-select").contains("Overskrift 2 (Alt+2)").should("be.selected");
        cy.get('.TITLE2:contains("Dette er en title2 block")').should("be.visible");
      });
      it("caret holder seg i samme posisjon ved toggle", () => {
        cy.mount(<EditorWithState />);
        cy.contains("title2").click();
        cy.contains("title2").type("{leftArrow}{leftArrow}{alt}2");
        assertCaret("title2", 22);
      });
    });
  });
  describe("overskrift 3", () => {
    describe("manuelt", () => {
      it("toggler av/på", () => {
        cy.mount(<EditorWithState />);
        cy.contains("title3").click();
        cy.getDataCy("typography-select").contains("Overskrift 3 (Alt+3)");
        cy.getDataCy("typography-select").select("Normal (Alt+4)");
        cy.getDataCy("typography-select").contains("Normal (Alt+4)").should("be.selected");
        cy.get('.PARAGRAPH:contains("Dette er en title3 block")').should("be.visible");
        cy.getDataCy("typography-select").select("Overskrift 3 (Alt+3)");
        cy.getDataCy("typography-select").contains("Overskrift 3 (Alt+3)").should("be.selected");
        cy.get('.TITLE3:contains("Dette er en title3 block")').should("be.visible");
      });
      it("caret holder seg i samme posisjon ved toggle", () => {
        cy.mount(<EditorWithState />);
        cy.contains("title3").click().type("{leftArrow}{leftArrow}");
        cy.getDataCy("typography-select").select("Normal (Alt+4)");
        assertCaret("title3", 22);
      });
    });
    describe("shortcut", () => {
      it("toggler av/på", () => {
        cy.mount(<EditorWithState />);
        cy.contains("title3").click();
        cy.getDataCy("typography-select").contains("Overskrift 3 (Alt+3)").should("be.selected");
        cy.contains("title3").type("{alt}4");
        cy.getDataCy("typography-select").contains("Normal (Alt+4)").should("be.selected");
        cy.get('.PARAGRAPH:contains("Dette er en title3 block")').should("be.visible");
        cy.contains("title3").type("{alt}3");
        cy.getDataCy("typography-select").contains("Overskrift 3 (Alt+3)").should("be.selected");
        cy.get('.TITLE3:contains("Dette er en title3 block")').should("be.visible");
      });
      it("caret holder seg i samme posisjon ved toggle", () => {
        cy.mount(<EditorWithState />);
        cy.contains("title3").click();
        cy.contains("title3").type("{leftArrow}{leftArrow}{alt}3");
        assertCaret("title3", 22);
      });
    });
  });

  describe("normal", () => {
    describe("manuelt", () => {
      it("toggler av/på", () => {
        cy.mount(<EditorWithState />);
        cy.contains("paragraph").click();
        cy.getDataCy("typography-select").contains("Normal (Alt+4)").should("be.selected");
        cy.getDataCy("typography-select").select("Overskrift 1 (Alt+1)");
        cy.getDataCy("typography-select").contains("Overskrift 1 (Alt+1)").should("be.selected");
        cy.get('.TITLE1:contains("Dette er en paragraph block")').should("be.visible");
        cy.getDataCy("typography-select").select("Normal (Alt+4)");
        cy.getDataCy("typography-select").contains("Normal (Alt+4)").should("be.selected");
        cy.get('.PARAGRAPH:contains("Dette er en paragraph block")').should("be.visible");
      });
      it("caret holder seg i samme posisjon ved toggle", () => {
        cy.mount(<EditorWithState />);
        cy.contains("paragraph").click().type("{leftArrow}{leftArrow}");
        cy.getDataCy("typography-select").select("Overskrift 1 (Alt+1)");
        assertCaret("paragraph", 25);
      });
    });
    describe("shortcut", () => {
      it("toggler av/på", () => {
        cy.mount(<EditorWithState />);
        cy.contains("paragraph").click();
        cy.getDataCy("typography-select").contains("Normal (Alt+4)").should("be.selected");
        cy.contains("paragraph").type("{alt}1");
        cy.getDataCy("typography-select").contains("Overskrift 1 (Alt+1)").should("be.selected");
        cy.get('.TITLE1:contains("Dette er en paragraph block")').should("be.visible");
        cy.contains("paragraph").type("{alt}4");
        cy.getDataCy("typography-select").contains("Normal (Alt+4)").should("be.selected");
        cy.get('.PARAGRAPH:contains("Dette er en paragraph block")').should("be.visible");
      });
      it("caret holder seg i samme posisjon ved toggle", () => {
        cy.mount(<EditorWithState />);
        cy.contains("paragraph").click();
        cy.contains("paragraph").type("{leftArrow}{leftArrow}{alt}3");
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
