import { useState } from "react";

import type { BrevResponse } from "~/types/brev";

import { nyBrevResponse, nyRedigertBrev } from "../../../../cypress/utils/brevredigeringTestUtils";
import Actions from "../actions";
import { newItem, newItemList, newItems, newLiteral, newParagraph } from "../actions/common";
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
          blocks: [newParagraph({ content: [newLiteral("Dette er kun et avsnitt")] })],
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

    it("lager en punktliste når man allerede har en ItemList i samme blokk før", () => {
      const brev = nyBrevResponse({
        redigertBrev: nyRedigertBrev({
          blocks: [
            newParagraph({
              content: [newItemList({ items: [newItem("Punkt 1")] }), newLiteral("Avsnitt med punktliste")],
            }),
          ],
        }),
      });
      cy.mount(<EditorWithState brev={brev} />);
      cy.get("li").should("have.length", 1);
      cy.contains("Avsnitt med punktliste").click();
      cy.getDataCy("editor-bullet-list").click();
      cy.get("li").should("have.length", 2);
      cy.get("li div span").eq(1).contains("Avsnitt med punktliste");
    });

    it("lager en punktliste når man allerede har en ItemList i samme blokk etter", () => {
      const brev = nyBrevResponse({
        redigertBrev: nyRedigertBrev({
          blocks: [
            newParagraph({
              content: [newLiteral("Avsnitt med punktliste"), newItemList({ items: [newItem("Punkt 1")] })],
            }),
          ],
        }),
      });

      cy.mount(<EditorWithState brev={brev} />);
      cy.get("li").should("have.length", 1);
      cy.contains("Avsnitt med punktliste").click();
      cy.getDataCy("editor-bullet-list").click();
      cy.get("li").should("have.length", 2);
      cy.get("li div span").eq(0).contains("Avsnitt med punktliste");
    });

    it("lager en punktliste når man allerede har en ItemList i samme blokk før og etter", () => {
      const brev = nyBrevResponse({
        redigertBrev: nyRedigertBrev({
          blocks: [
            newParagraph({
              content: [
                newItemList({ items: [newItem("Punkt 1")] }),
                newLiteral("Avsnitt med punktliste"),
                newItemList({ items: [newItem("Punkt 2")] }),
              ],
            }),
          ],
        }),
      });
      cy.mount(<EditorWithState brev={brev} />);
      cy.get("ul").should("have.length", 2);
      cy.get("li").should("have.length", 2);
      cy.contains("Avsnitt med punktliste").click();
      cy.getDataCy("editor-bullet-list").click();
      cy.get("ul").should("have.length", 1);
      cy.get("li").should("have.length", 3);
      cy.get("li div span").eq(1).contains("Avsnitt med punktliste");
    });

    it("lager en punktliste når man allerede har en itemList i en annen blokk før", () => {
      const brev = nyBrevResponse({
        redigertBrev: nyRedigertBrev({
          blocks: [
            newParagraph({ content: [newItemList({ items: [newItem("Punkt 1")] })] }),
            newParagraph({ content: [newLiteral("Avsnitt uten punktliste")] }),
          ],
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

    it("lager en punktliste når man allerede har en itemList i en annen blokk etter", () => {
      const brev = nyBrevResponse({
        redigertBrev: nyRedigertBrev({
          blocks: [
            newParagraph({ content: [newLiteral("Avsnitt uten punktliste")] }),
            newParagraph({ content: [newItemList({ items: [newItem("Punkt 1")] })] }),
          ],
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
            newParagraph({ content: [newItemList({ items: [newItem("Punkt 1")] })] }),
            newParagraph({ content: [newLiteral("Avsnitt uten punktliste")] }),
            newParagraph({ content: [newItemList({ items: [newItem("Punkt 2")] })] }),
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
          blocks: [newParagraph({ content: [newItemList({ items: [newItem("Dette er kun et avsnitt")] })] })],
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

    it("toggler av et punkt på starten av en punktliste", () => {
      const brev = nyBrevResponse({
        redigertBrev: nyRedigertBrev({
          blocks: [newParagraph({ content: [newItemList({ items: newItems("skal brytes ut", "Punkt 1") })] })],
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

    it("bevarer content som er rundt en punktliste når man toggler av på starten av en punktliste", () => {
      const brev = nyBrevResponse({
        redigertBrev: nyRedigertBrev({
          blocks: [
            newParagraph({
              content: [
                newLiteral("Denne skal ikke forsvinne når man trigger av punktliste"),
                newItemList({ items: newItems("skal brytes ut", "Punkt 1") }),
                newLiteral("Denne skal heller ikke forsvinne når man trigger av punktliste"),
              ],
            }),
          ],
        }),
      });

      cy.mount(<EditorWithState brev={brev} />);
      cy.get("ul").should("have.length", 1);
      cy.get("li").should("have.length", 2);
      cy.contains("Denne skal ikke forsvinne når man trigger av punktliste").should("be.visible");
      cy.contains("Denne skal heller ikke forsvinne når man trigger av punktliste").should("be.visible");
      cy.contains("skal brytes ut").click();
      cy.getDataCy("editor-bullet-list").click();
      cy.get("ul").should("have.length", 1);
      cy.get("li").should("have.length", 1);
      cy.contains("skal brytes ut").should("be.visible");
      cy.contains("Denne skal ikke forsvinne når man trigger av punktliste").should("be.visible");
      cy.contains("Denne skal heller ikke forsvinne når man trigger av punktliste").should("be.visible");
    });

    it("bevarer content som er rundt en punktliste når man toggler av på midten av en punktliste", () => {
      const brev = nyBrevResponse({
        redigertBrev: nyRedigertBrev({
          blocks: [
            newParagraph({
              content: [
                newLiteral("Denne skal ikke forsvinne når man trigger av punktliste"),
                newItemList({ items: newItems("Punkt 1", "skal brytes ut", "punkt 2") }),
                newLiteral("Denne skal heller ikke forsvinne når man trigger av punktliste"),
              ],
            }),
          ],
        }),
      });

      cy.mount(<EditorWithState brev={brev} />);
      cy.get("ul").should("have.length", 1);
      cy.get("li").should("have.length", 3);
      cy.contains("Denne skal ikke forsvinne når man trigger av punktliste").should("be.visible");
      cy.contains("Denne skal heller ikke forsvinne når man trigger av punktliste").should("be.visible");
      cy.contains("skal brytes ut").click();
      cy.getDataCy("editor-bullet-list").click();
      cy.get("ul").should("have.length", 2);
      cy.get("li").should("have.length", 2);
      cy.contains("skal brytes ut").should("be.visible");
      cy.contains("Denne skal ikke forsvinne når man trigger av punktliste").should("be.visible");
      cy.contains("Denne skal heller ikke forsvinne når man trigger av punktliste").should("be.visible");
    });

    it("bevarer content som er rundt en punktliste når man toggler av på slutten av en punktliste", () => {
      const brev = nyBrevResponse({
        redigertBrev: nyRedigertBrev({
          blocks: [
            newParagraph({
              content: [
                newLiteral("Denne skal ikke forsvinne når man trigger av punktliste"),
                newItemList({ items: newItems("Punkt 1", "skal brytes ut") }),
                newLiteral("Denne skal heller ikke forsvinne når man trigger av punktliste"),
              ],
            }),
          ],
        }),
      });

      cy.mount(<EditorWithState brev={brev} />);
      cy.get("ul").should("have.length", 1);
      cy.get("li").should("have.length", 2);
      cy.contains("Denne skal ikke forsvinne når man trigger av punktliste").should("be.visible");
      cy.contains("Denne skal heller ikke forsvinne når man trigger av punktliste").should("be.visible");
      cy.contains("skal brytes ut").click();
      cy.getDataCy("editor-bullet-list").click();
      cy.get("ul").should("have.length", 1);
      cy.get("li").should("have.length", 1);
      cy.contains("skal brytes ut").should("be.visible");
      cy.contains("Denne skal ikke forsvinne når man trigger av punktliste").should("be.visible");
      cy.contains("Denne skal heller ikke forsvinne når man trigger av punktliste").should("be.visible");
    });

    it("toggler av et punkt på slutten av en punktliste", () => {
      const brev = nyBrevResponse({
        redigertBrev: nyRedigertBrev({
          blocks: [newParagraph({ content: [newItemList({ items: newItems("Punkt 1", "skal brytes ut") })] })],
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
          blocks: [
            newParagraph({ content: [newItemList({ items: newItems("Punkt 1", "skal brytes ut", "Punkt 2") })] }),
          ],
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
