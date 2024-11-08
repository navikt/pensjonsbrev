import { useState } from "react";

import type { BrevResponse } from "~/types/brev";

import { nyBrevResponse, nyRedigertBrev } from "../../../../cypress/utils/brevredigeringTestUtils";
import Actions from "../actions";
import { newItem, newItemList, newLiteral, newParagraph } from "../actions/common";
import { LetterEditor } from "../LetterEditor";
import type { LetterEditorState } from "../model/state";

function EditorWithState({ brev }: { brev: BrevResponse }) {
  const [editorState, setEditorState] = useState<LetterEditorState>(Actions.create(brev));
  return (
    <LetterEditor
      editorState={editorState}
      error={false}
      freeze={false}
      setEditorState={setEditorState}
      showDebug={false}
    />
  );
}

describe("toggle bullet-liet", () => {
  beforeEach(() => {
    cy.viewport(800, 1400);
  });

  describe("toggle on", () => {
    it("toggler et enkelt avsnitt", () => {
      const brev = nyBrevResponse({
        redigertBrev: nyRedigertBrev({
          blocks: [newParagraph(newLiteral("Dette er kun et avsnitt"))],
        }),
      });

      cy.mount(<EditorWithState brev={brev} />);

      cy.get("ul").should("have.length", 0);
      cy.contains("Dette er kun et avsnitt").click();
      cy.get("li div span").should("not.exist");
      cy.getDataCy("editor-bullet-list").click();
      cy.get("li div span").contains("Dette er kun et avsnitt");
      cy.get("ul").should("have.length", 1);
    });
    it("toggler et avsnitt med en eksisterende punktliste i samme blokk (avsnitt før punktliste)", () => {
      const brev = nyBrevResponse({
        redigertBrev: nyRedigertBrev({
          blocks: [newParagraph(newLiteral("Avsnitt med punktliste"), newItemList(newItem("Punkt 1")))],
        }),
      });

      cy.mount(<EditorWithState brev={brev} />);

      cy.get("li").should("have.length", 1);
      cy.contains("Avsnitt med punktliste").click();
      cy.getDataCy("editor-bullet-list").click();
      cy.get("li").should("have.length", 2);
      cy.get("li div span").eq(0).contains("Avsnitt med punktliste");
    });
    it("toggler et avsnitt med en eksisterende punktliste i samme blokk (avsnitt etter punktliste)", () => {
      const brev = nyBrevResponse({
        redigertBrev: nyRedigertBrev({
          blocks: [newParagraph(newItemList(newItem("Punkt 1")), newLiteral("Avsnitt med punktliste"))],
        }),
      });
      cy.mount(<EditorWithState brev={brev} />);
      cy.get("li").should("have.length", 1);
      cy.contains("Avsnitt med punktliste").click();
      cy.getDataCy("editor-bullet-list").click();
      cy.get("li").should("have.length", 2);
      cy.get("li div span").eq(1).contains("Avsnitt med punktliste");
    });
    it("toggler et avsnitt med en eksisterende punktliste i en annen blokk", () => {
      const brev = nyBrevResponse({
        redigertBrev: nyRedigertBrev({
          blocks: [newParagraph(newItemList(newItem("Punkt 1"))), newParagraph(newLiteral("Avsnitt uten punktliste"))],
        }),
      });

      cy.mount(<EditorWithState brev={brev} />);

      cy.get("ul").should("have.length", 1);
      cy.get("li").should("have.length", 1);
      cy.contains("Avsnitt uten punktliste").click();
      cy.getDataCy("editor-bullet-list").click();
      cy.get("ul").should("have.length", 1);
      cy.get("li").should("have.length", 2);
    });
    it("toggler punktliste mellom 2 punktlister", () => {
      const brev = nyBrevResponse({
        redigertBrev: nyRedigertBrev({
          blocks: [
            newParagraph(newItemList(newItem("Punkt 1"))),
            newParagraph(newLiteral("Avsnitt uten punktliste")),
            newParagraph(newItemList(newItem("Punkt 2"))),
          ],
        }),
      });
      cy.mount(<EditorWithState brev={brev} />);
      cy.get("ul").should("have.length", 2);
      cy.get("li").should("have.length", 2);
      cy.contains("Avsnitt uten punktliste").click();
      cy.getDataCy("editor-bullet-list").click();
      cy.get("ul").should("have.length", 1);
      cy.get("li").should("have.length", 3);
      cy.contains("Avsnitt uten punktliste").should("exist");
    });
  });
  describe("toggle off", () => {
    it("toggler av et enkelt avsnitt", () => {
      const brev = nyBrevResponse({
        redigertBrev: nyRedigertBrev({
          blocks: [newParagraph(newItemList(newItem("Dette er kun et avsnitt")))],
        }),
      });

      cy.mount(<EditorWithState brev={brev} />);

      cy.get("ul").should("have.length", 1);
      cy.get("li").should("have.length", 1);
      cy.contains("Dette er kun et avsnitt").click();
      cy.getDataCy("editor-bullet-list").click();
      cy.get("ul").should("have.length", 0);
      cy.get("li").should("have.length", 0);
      cy.contains("Dette er kun et avsnitt").should("exist");
    });
    it("toggler av et avsnitt med en eksisterende punktliste i samme blokk (avsnitt før punktliste)", () => {
      const brev = nyBrevResponse({
        redigertBrev: nyRedigertBrev({
          blocks: [newParagraph(newItemList(newItem("skal brytes ut"), newItem("Punkt 1")))],
        }),
      });

      cy.mount(<EditorWithState brev={brev} />);

      cy.get("ul").should("have.length", 1);
      cy.get("li").should("have.length", 2);
      cy.contains("skal brytes ut").click();
      cy.getDataCy("editor-bullet-list").click();
      cy.get("ul").should("have.length", 1);
      cy.get("li").should("have.length", 1);
      cy.get(".PARAGRAPH").eq(0).contains("skal brytes ut").should("exist");
    });
    it("toggler av et avsnitt med en eksisterende punktliste i samme blokk (avsnitt før punktliste)", () => {
      const brev = nyBrevResponse({
        redigertBrev: nyRedigertBrev({
          blocks: [newParagraph(newItemList(newItem("Punkt 1"), newItem("skal brytes ut")))],
        }),
      });

      cy.mount(<EditorWithState brev={brev} />);

      cy.get("ul").should("have.length", 1);
      cy.get("li").should("have.length", 2);
      cy.contains("skal brytes ut").click();
      cy.getDataCy("editor-bullet-list").click();
      cy.get("ul").should("have.length", 1);
      cy.get("li").should("have.length", 1);
      cy.get(".PARAGRAPH").eq(1).contains("skal brytes ut").should("exist");
    });
    it("fjerner punkt fra midten av en punktliste", () => {
      const brev = nyBrevResponse({
        redigertBrev: nyRedigertBrev({
          blocks: [newParagraph(newItemList(newItem("Punkt 1"), newItem("skal brytes ut"), newItem("Punkt 3")))],
        }),
      });
      cy.mount(<EditorWithState brev={brev} />);
      cy.get("ul").should("have.length", 1);
      cy.get("li").should("have.length", 3);
      cy.contains("skal brytes ut").click();
      cy.getDataCy("editor-bullet-list").click();
      cy.get("ul").should("have.length", 2);
      cy.get("li").should("have.length", 2);
      cy.get(".PARAGRAPH").eq(1).contains("skal brytes ut").should("exist");
    });
  });
});
