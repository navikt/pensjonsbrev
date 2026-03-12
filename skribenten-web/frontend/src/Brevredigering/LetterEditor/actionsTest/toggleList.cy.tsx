import { useState } from "react";

import type { BrevResponse } from "~/types/brev";
import type { Item } from "~/types/brevbakerTypes";
import { ListType } from "~/types/brevbakerTypes";

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

function newItems(...texts: string[]): Item[] {
  return texts.map((t) => newItem({ content: [newLiteral({ text: t })] }));
}

describe("toggle bullet-list", () => {
  describe("toggle on", () => {
    it("toggler et enkelt avsnitt", () => {
      const brev = nyBrevResponse({
        redigertBrev: nyRedigertBrev({
          blocks: [newParagraph({ content: [newLiteral({ text: "Dette er kun et avsnitt" })] })],
        }),
      });

      cy.mount(<EditorWithState brev={brev} />);

      cy.get("ul").should("have.length", 0);
      cy.contains("Dette er kun et avsnitt").click();
      cy.get("li span").should("not.exist");
      cy.getDataCy("editor-bullet-list").click();
      cy.get("li span").contains("Dette er kun et avsnitt");
      cy.get("ul").should("have.length", 1);
    });

    it("lager en punktliste når man allerede har en ItemList i samme blokk før", () => {
      const brev = nyBrevResponse({
        redigertBrev: nyRedigertBrev({
          blocks: [
            newParagraph({
              content: [newItemList({ items: newItems("Punkt 1") }), newLiteral({ text: "Avsnitt med punktliste" })],
            }),
          ],
        }),
      });
      cy.mount(<EditorWithState brev={brev} />);
      cy.get("li").should("have.length", 1);
      cy.contains("Avsnitt med punktliste").click();
      cy.getDataCy("editor-bullet-list").click();
      cy.get("li").should("have.length", 2);
      cy.get("li span").eq(1).contains("Avsnitt med punktliste");
    });

    it("lager en punktliste når man allerede har en ItemList i samme blokk etter", () => {
      const brev = nyBrevResponse({
        redigertBrev: nyRedigertBrev({
          blocks: [
            newParagraph({
              content: [newLiteral({ text: "Avsnitt med punktliste" }), newItemList({ items: newItems("Punkt 1") })],
            }),
          ],
        }),
      });

      cy.mount(<EditorWithState brev={brev} />);
      cy.get("li").should("have.length", 1);
      cy.contains("Avsnitt med punktliste").click();
      cy.getDataCy("editor-bullet-list").click();
      cy.get("li").should("have.length", 2);
      cy.get("li span").eq(0).contains("Avsnitt med punktliste");
    });

    it("lager en punktliste når man allerede har en ItemList i samme blokk før og etter", () => {
      const brev = nyBrevResponse({
        redigertBrev: nyRedigertBrev({
          blocks: [
            newParagraph({
              content: [
                newItemList({ items: newItems("Punkt 1") }),
                newLiteral({ text: "Avsnitt med punktliste" }),
                newItemList({ items: newItems("Punkt 2") }),
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
      cy.get("li span").eq(1).contains("Avsnitt med punktliste");
    });

    it("lager en punktliste når man allerede har en itemList i en annen blokk før", () => {
      const brev = nyBrevResponse({
        redigertBrev: nyRedigertBrev({
          blocks: [
            newParagraph({ content: [newItemList({ items: newItems("Punkt 1") })] }),
            newParagraph({ content: [newLiteral({ text: "Avsnitt uten punktliste" })] }),
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
            newParagraph({ content: [newLiteral({ text: "Avsnitt uten punktliste" })] }),
            newParagraph({ content: [newItemList({ items: newItems("Punkt 1") })] }),
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
            newParagraph({ content: [newItemList({ items: newItems("Punkt 1") })] }),
            newParagraph({ content: [newLiteral({ text: "Avsnitt uten punktliste" })] }),
            newParagraph({ content: [newItemList({ items: newItems("Punkt 2") })] }),
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
      cy.get("li span").eq(1).contains("Avsnitt uten punktliste");
    });
  });

  describe("toggle off", () => {
    it("toggler av et enkelt avsnitt", () => {
      const brev = nyBrevResponse({
        redigertBrev: nyRedigertBrev({
          blocks: [newParagraph({ content: [newItemList({ items: newItems("Dette er kun et avsnitt") })] })],
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
                newLiteral({ text: "Denne skal ikke forsvinne når man trigger av punktliste" }),
                newItemList({ items: newItems("skal brytes ut", "Punkt 1") }),
                newLiteral({ text: "Denne skal heller ikke forsvinne når man trigger av punktliste" }),
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

    it("bevarer content som er rundt en punktliste når man toggler av på starten av en punktliste som kun inneholder 1 punkt", () => {
      const brev = nyBrevResponse({
        redigertBrev: nyRedigertBrev({
          blocks: [
            newParagraph({
              content: [
                newLiteral({ text: "Denne skal ikke forsvinne når man trigger av punktliste" }),
                newItemList({ items: newItems("skal brytes ut") }),
                newLiteral({ text: "Denne skal heller ikke forsvinne når man trigger av punktliste" }),
              ],
            }),
          ],
        }),
      });

      cy.mount(<EditorWithState brev={brev} />);
      cy.get("ul").should("have.length", 1);
      cy.get("li").should("have.length", 1);
      cy.contains("Denne skal ikke forsvinne når man trigger av punktliste").should("be.visible");
      cy.contains("Denne skal heller ikke forsvinne når man trigger av punktliste").should("be.visible");
      cy.contains("skal brytes ut").click();
      cy.getDataCy("editor-bullet-list").click();
      cy.get("ul").should("have.length", 0);
      cy.get("li").should("have.length", 0);
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
                newLiteral({ text: "Denne skal ikke forsvinne når man trigger av punktliste" }),
                newItemList({ items: newItems("Punkt 1", "skal brytes ut", "punkt 2") }),
                newLiteral({ text: "Denne skal heller ikke forsvinne når man trigger av punktliste" }),
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
                newLiteral({ text: "Denne skal ikke forsvinne når man trigger av punktliste" }),
                newItemList({ items: newItems("Punkt 1", "skal brytes ut") }),
                newLiteral({ text: "Denne skal heller ikke forsvinne når man trigger av punktliste" }),
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

    it("bevarer content som er rundt en punktliste når man toggler av på slutten av en punktliste som kun inneholder 1 punkt", () => {
      const brev = nyBrevResponse({
        redigertBrev: nyRedigertBrev({
          blocks: [
            newParagraph({
              content: [
                newLiteral({ text: "Denne skal ikke forsvinne når man trigger av punktliste" }),
                newItemList({ items: newItems("skal brytes ut") }),
                newLiteral({ text: "Denne skal heller ikke forsvinne når man trigger av punktliste" }),
              ],
            }),
          ],
        }),
      });

      cy.mount(<EditorWithState brev={brev} />);
      cy.get("ul").should("have.length", 1);
      cy.get("li").should("have.length", 1);
      cy.contains("Denne skal ikke forsvinne når man trigger av punktliste").should("be.visible");
      cy.contains("Denne skal heller ikke forsvinne når man trigger av punktliste").should("be.visible");
      cy.contains("skal brytes ut").click();
      cy.getDataCy("editor-bullet-list").click();
      cy.get("ul").should("have.length", 0);
      cy.get("li").should("have.length", 0);
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
      cy.get(".PARAGRAPH").eq(0).contains("skal brytes ut").should("exist");
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
      cy.get(".PARAGRAPH").eq(0).contains("skal brytes ut").should("exist");
    });
  });
});

describe("toggle number-list", () => {
  it("viser number-list som ol-element", () => {
    const brev = nyBrevResponse({
      redigertBrev: nyRedigertBrev({
        blocks: [
          newParagraph({
            content: [
              newItemList({ listType: ListType.NUMMERERT_LISTE, items: newItems("første punkt", "andre punkt") }),
            ],
          }),
        ],
      }),
    });

    cy.mount(<EditorWithState brev={brev} />);

    cy.get("ol").should("have.length", 1);
    cy.get("ul").should("have.length", 0);
    cy.get("ol li").should("have.length", 2);
    cy.get("ol li").eq(0).contains("første punkt");
    cy.get("ol li").eq(1).contains("andre punkt");
  });

  it("viser PUNKTLISTE som ul-element", () => {
    const brev = nyBrevResponse({
      redigertBrev: nyRedigertBrev({
        blocks: [
          newParagraph({
            content: [newItemList({ listType: ListType.PUNKTLISTE, items: newItems("kulepunkt1", "kulepunkt2") })],
          }),
        ],
      }),
    });

    cy.mount(<EditorWithState brev={brev} />);

    cy.get("ul").should("have.length", 1);
    cy.get("ol").should("have.length", 0);
  });

  it("toggler et avsnitt til nummerert liste", () => {
    const brev = nyBrevResponse({
      redigertBrev: nyRedigertBrev({
        blocks: [newParagraph({ content: [newLiteral({ text: "Et avsnitt" })] })],
      }),
    });

    cy.mount(<EditorWithState brev={brev} />);

    cy.get("ol").should("have.length", 0);
    cy.contains("Et avsnitt").click();
    cy.getDataCy("editor-number-list").click();
    cy.get("ol li span").contains("Et avsnitt");
    cy.get("ol").should("have.length", 1);
    cy.get("ul").should("have.length", 0);
  });

  it("nummerert-liste-knapp er aktiv når fokus er på nummerert liste", () => {
    const brev = nyBrevResponse({
      redigertBrev: nyRedigertBrev({
        blocks: [
          newParagraph({
            content: [newItemList({ listType: ListType.NUMMERERT_LISTE, items: newItems("punkt") })],
          }),
        ],
      }),
    });

    cy.mount(<EditorWithState brev={brev} />);

    cy.get("ol li span").contains("punkt").click();
    cy.getDataCy("editor-number-list")
      .should("have.attr", "data-variant", "primary")
      .should("have.attr", "data-color", "neutral");
    cy.getDataCy("editor-bullet-list")
      .should("have.attr", "data-variant", "tertiary")
      .should("have.attr", "data-color", "neutral");
  });

  it("punktliste-knapp er aktiv når fokus er på punktliste", () => {
    const brev = nyBrevResponse({
      redigertBrev: nyRedigertBrev({
        blocks: [
          newParagraph({
            content: [newItemList({ listType: ListType.PUNKTLISTE, items: newItems("punkt") })],
          }),
        ],
      }),
    });

    cy.mount(<EditorWithState brev={brev} />);

    cy.get("ul li span").contains("punkt").click();
    cy.getDataCy("editor-bullet-list")
      .should("have.attr", "data-variant", "primary")
      .should("have.attr", "data-color", "neutral");
    cy.getDataCy("editor-number-list")
      .should("have.attr", "data-variant", "tertiary")
      .should("have.attr", "data-color", "neutral");
  });

  it("toggler av nummerert liste med nummerert-liste-knapp", () => {
    const brev = nyBrevResponse({
      redigertBrev: nyRedigertBrev({
        blocks: [
          newParagraph({
            content: [newItemList({ listType: ListType.NUMMERERT_LISTE, items: newItems("punkt1", "punkt2") })],
          }),
        ],
      }),
    });

    cy.mount(<EditorWithState brev={brev} />);

    cy.get("ol li").should("have.length", 2);
    cy.get("ol li span").contains("punkt1").click();
    cy.getDataCy("editor-number-list").click();
    cy.get("ol li").should("have.length", 1);
    cy.contains("punkt1").should("exist");
    cy.get("ol").contains("punkt1").should("not.exist");
    cy.get("ol").contains("punkt2").should("exist");
  });

  it("konverterer nummerert liste til punktliste ved klikk på punktliste-knapp", () => {
    const brev = nyBrevResponse({
      redigertBrev: nyRedigertBrev({
        blocks: [
          newParagraph({
            content: [newItemList({ listType: ListType.NUMMERERT_LISTE, items: newItems("punkt1", "punkt2") })],
          }),
        ],
      }),
    });

    cy.mount(<EditorWithState brev={brev} />);

    cy.get("ol li span").contains("punkt1").click();
    cy.getDataCy("editor-bullet-list").click();
    cy.get("ol").should("have.length", 0);
    cy.get("ul").should("have.length", 1);
    cy.get("ul li").should("have.length", 2);
    cy.get("ul li").eq(0).contains("punkt1");
    cy.get("ul li").eq(1).contains("punkt2");
  });

  it("konverterer punktliste til nummerert liste ved klikk på nummerert-liste-knapp", () => {
    const brev = nyBrevResponse({
      redigertBrev: nyRedigertBrev({
        blocks: [
          newParagraph({
            content: [newItemList({ listType: ListType.PUNKTLISTE, items: newItems("punkt1", "punkt2") })],
          }),
        ],
      }),
    });

    cy.mount(<EditorWithState brev={brev} />);

    cy.get("ul li span").contains("punkt1").click();
    cy.getDataCy("editor-number-list").click();
    cy.get("ul").should("have.length", 0);
    cy.get("ol").should("have.length", 1);
    cy.get("ol li").should("have.length", 2);
    cy.get("ol li").eq(0).contains("punkt1");
    cy.get("ol li").eq(1).contains("punkt2");
  });
});

describe("blanding av liste-typer", () => {
  it("punkt 2 i en punktliste av 3 gjøres om til nummerert – deretter konverteres punkt 3 til nummerert og merges med punkt 2", () => {
    const brev = nyBrevResponse({
      redigertBrev: nyRedigertBrev({
        blocks: [
          newParagraph({
            content: [newItemList({ items: newItems("item1", "item2", "item3") })],
          }),
        ],
      }),
    });

    cy.mount(<EditorWithState brev={brev} />);

    // Step 1: toggle item2 off to regular text
    cy.get("ul li span").contains("item2").click();
    cy.getDataCy("editor-bullet-list").click();
    // Now: bulletList[item1] | text(item2) | bulletList[item3]
    cy.get("ul").should("have.length", 2);
    cy.get("ul li").should("have.length", 2);

    // Step 2: toggle item2 to number list
    cy.contains("item2").click();
    cy.getDataCy("editor-number-list").click();
    // Now: bulletList[item1] | numberList[item2] | bulletList[item3]
    cy.get("ul").should("have.length", 2);
    cy.get("ol").should("have.length", 1);
    cy.get("ol li").should("have.length", 1);

    // Step 3: toggle item3 from bullet list to number list
    cy.get("ul li span").contains("item3").click();
    cy.getDataCy("editor-number-list").click();
    // item3 converts to number list and merges with item2's number list
    cy.get("ul").should("have.length", 1);
    cy.get("ul li").should("have.length", 1);
    cy.get("ul li").contains("item1");
    cy.get("ol").should("have.length", 1);
    cy.get("ol li").should("have.length", 2);
    cy.get("ol li").eq(0).contains("item2");
    cy.get("ol li").eq(1).contains("item3");
  });

  it("toggler midterste punkt i en punktliste med 5 punkt til nummerert liste – hele listen bytter type", () => {
    const brev = nyBrevResponse({
      redigertBrev: nyRedigertBrev({
        blocks: [
          newParagraph({
            content: [newItemList({ items: newItems("item1", "item2", "item3", "item4", "item5") })],
          }),
        ],
      }),
    });

    cy.mount(<EditorWithState brev={brev} />);

    cy.get("ul").should("have.length", 1);
    cy.get("ul li").should("have.length", 5);

    cy.get("ul li span").contains("item3").click();
    cy.getDataCy("editor-number-list").click();

    cy.get("ul").should("have.length", 0);
    cy.get("ol").should("have.length", 1);
    cy.get("ol li").should("have.length", 5);
    cy.get("ol li").eq(0).contains("item1");
    cy.get("ol li").eq(1).contains("item2");
    cy.get("ol li").eq(2).contains("item3");
    cy.get("ol li").eq(3).contains("item4");
    cy.get("ol li").eq(4).contains("item5");
  });

  it("toggler første punkt av punktliste og gjør det om til nummerert liste, andre punkt forblir punktliste", () => {
    const brev = nyBrevResponse({
      redigertBrev: nyRedigertBrev({
        blocks: [
          newParagraph({
            content: [newItemList({ listType: ListType.PUNKTLISTE, items: newItems("item1", "item2") })],
          }),
        ],
      }),
    });

    cy.mount(<EditorWithState brev={brev} />);

    // toggle off the first bullet item
    cy.get("ul li span").contains("item1").click();
    cy.getDataCy("editor-bullet-list").click();

    // "item1" is now regular text; "item2" is still in the bullet list
    cy.get("ul li").should("have.length", 1);
    cy.get("ul li").contains("item2");

    // toggle "item1" as a number list
    cy.contains("item1").click();
    cy.getDataCy("editor-number-list").click();

    // item1 should be in an ordered list, item2 should still be in an unordered list
    cy.get("ol").should("have.length", 1);
    cy.get("ul").should("have.length", 1);
    cy.get("ol li").should("have.length", 1);
    cy.get("ol li").contains("item1");
    cy.get("ul li").should("have.length", 1);
    cy.get("ul li").contains("item2");
  });

  it("en kuleliste i ett avsnitt, tekst i neste avsnitt, kuleliste i tredje avsnitt – slås sammen til én liste", () => {
    const brev = nyBrevResponse({
      redigertBrev: nyRedigertBrev({
        blocks: [
          newParagraph({ content: [newItemList({ items: newItems("item1", "item2") })] }),
          newParagraph({ content: [newLiteral({ text: "middle" })] }),
          newParagraph({ content: [newItemList({ items: newItems("item3", "item4") })] }),
        ],
      }),
    });

    cy.mount(<EditorWithState brev={brev} />);

    cy.get("ul").should("have.length", 2);
    cy.get("ul li").should("have.length", 4);

    cy.contains("middle").click();
    cy.getDataCy("editor-bullet-list").click();

    cy.get("ul").should("have.length", 1);
    cy.get("ul li").should("have.length", 5);
    cy.get("ul li").eq(0).contains("item1");
    cy.get("ul li").eq(1).contains("item2");
    cy.get("ul li").eq(2).contains("middle");
    cy.get("ul li").eq(3).contains("item3");
    cy.get("ul li").eq(4).contains("item4");
  });

  it("to like nummererte lister med tekst i midten skal bli én liste med fem punkter", () => {
    const brev = nyBrevResponse({
      redigertBrev: nyRedigertBrev({
        blocks: [
          newParagraph({
            content: [
              newItemList({ listType: ListType.NUMMERERT_LISTE, items: newItems("item1", "item2") }),
              newLiteral({ text: "middle" }),
              newItemList({ listType: ListType.NUMMERERT_LISTE, items: newItems("item3", "item4") }),
            ],
          }),
        ],
      }),
    });

    cy.mount(<EditorWithState brev={brev} />);

    cy.get("ol").should("have.length", 2);
    cy.get("ol li").should("have.length", 4);

    cy.contains("middle").click();
    cy.getDataCy("editor-number-list").click();

    cy.get("ol").should("have.length", 1);
    cy.get("ol li").should("have.length", 5);
    cy.get("ol li").eq(0).contains("item1");
    cy.get("ol li").eq(1).contains("item2");
    cy.get("ol li").eq(2).contains("middle");
    cy.get("ol li").eq(3).contains("item3");
    cy.get("ol li").eq(4).contains("item4");
  });
});
