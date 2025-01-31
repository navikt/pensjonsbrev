import { useState } from "react";

import Actions from "~/Brevredigering/LetterEditor/actions";
import type { LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { type BrevResponse } from "~/types/brev";
import type { EditedLetter } from "~/types/brevbakerTypes";

import {
  nyBrevResponse,
  nyLiteral,
  nyParagraphBlock,
  nyRedigertBrev,
} from "../../../../cypress/utils/brevredigeringTestUtils";
import exampleLetter1Json from "../example-letter-1.json";
import { LetterEditor } from "../LetterEditor";

/**
 * Disse testene gjøres i cypress fordi det ikke ser ut til å være en enkel måte å teste merking av tekst i andre deler.
 */

const exampleLetter1 = exampleLetter1Json as EditedLetter;

function EditorWithState({ initial }: { initial?: EditedLetter }) {
  const brevresponse: BrevResponse = nyBrevResponse({
    redigertBrev: initial ?? exampleLetter1,
  });

  const [editorState, setEditorState] = useState<LetterEditorState>(Actions.create(brevresponse));
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

describe("Switch font type ", () => {
  beforeEach(() => {
    cy.viewport(1200, 1400);
  });

  describe("literals", () => {
    describe("marked text", () => {
      it("handles switching plain/bold/plain", () => {
        cy.mount(<EditorWithState />);
        cy.getDataCy("PARAGRAPH").then((el) => {
          expect(el[0].childNodes).to.have.length(3);
        });
        cy.contains("Er laget for").then((el) => {
          const element = el![0] as HTMLElement;
          const range = document.createRange();
          const selection = globalThis.getSelection()!;
          range.setStart(element.firstChild!, 15);
          range.setEnd(element.firstChild!, 28);
          selection.addRange(range);
          element.focus();
        });
        cy.document().then((doc) => {
          const selectedText = doc.getSelection()?.toString();
          expect(selectedText).to.eq("teste piltast"); // Sanity check
        });
        cy.getDataCy("fonttype-bold").click();
        cy.contains("Er laget for").should("have.css", "font-weight", "400");
        cy.contains("teste piltast").should("have.css", "font-weight", "700");
        cy.contains(
          "opp og ned innad samme avsnitt[CP1-2]. Poenget er å teste [CP1-3] at caret går til nærmeste side av variable.",
        ).should("have.css", "font-weight", "400");
        cy.contains("teste piltast").then((el) => {
          const element = el![0] as HTMLElement;
          const range = document.createRange();
          const selection = globalThis.getSelection()!;
          selection.removeAllRanges();
          range.setStart(element.firstChild!, 0);
          range.setEnd(element.firstChild!, 13);
          selection.addRange(range);
          element.focus();
        });
        cy.getDataCy("fonttype-bold").click();
        cy.contains("Er laget for").should("have.css", "font-weight", "400");
        cy.contains("teste piltast").should("have.css", "font-weight", "400");
        cy.contains(
          "opp og ned innad samme avsnitt[CP1-2]. Poenget er å teste [CP1-3] at caret går til nærmeste side av variable.",
        ).should("have.css", "font-weight", "400");
        cy.getDataCy("PARAGRAPH")
          .eq(0)
          .then((el) => {
            expect(el[0].childNodes).to.have.length(5);
          });
      });
      it("handles switching plain/italic/plain", () => {
        cy.mount(<EditorWithState />);
        cy.getDataCy("PARAGRAPH").then((el) => {
          expect(el[0].childNodes).to.have.length(3);
        });
        cy.contains("Er laget for").then((el) => {
          const element = el![0] as HTMLElement;
          const range = document.createRange();
          const selection = globalThis.getSelection()!;
          selection.removeAllRanges();
          range.setStart(element.firstChild!, 15);
          range.setEnd(element.firstChild!, 28);
          selection.addRange(range);
          element.focus();
        });
        cy.document().then((doc) => {
          const selectedText = doc.getSelection()?.toString();
          expect(selectedText).to.eq("teste piltast"); // Sanity check
        });
        cy.getDataCy("fonttype-italic").click();
        cy.contains("Er laget for").should("have.css", "font-style", "normal");
        cy.contains("teste piltast").should("have.css", "font-style", "italic");
        cy.contains(
          "opp og ned innad samme avsnitt[CP1-2]. Poenget er å teste [CP1-3] at caret går til nærmeste side av variable.",
        ).should("have.css", "font-style", "normal");
        cy.contains("teste piltast").then((el) => {
          const element = el![0] as HTMLElement;
          const range = document.createRange();
          const selection = globalThis.getSelection()!;
          selection.removeAllRanges();
          range.setStart(element.firstChild!, 0);
          range.setEnd(element.firstChild!, 13);
          selection.addRange(range);
          element.focus();
        });
        cy.getDataCy("fonttype-italic").click();
        cy.contains("Er laget for").should("have.css", "font-style", "normal");
        cy.contains("teste piltast").should("have.css", "font-style", "normal");
        cy.contains(
          "opp og ned innad samme avsnitt[CP1-2]. Poenget er å teste [CP1-3] at caret går til nærmeste side av variable.",
        ).should("have.css", "font-style", "normal");
        cy.getDataCy("PARAGRAPH").then((el) => {
          expect(el[0].childNodes).to.have.length(5);
        });
      });
      it("handles switching plain/bold/italic", () => {
        cy.mount(<EditorWithState />);
        cy.getDataCy("PARAGRAPH").then((el) => {
          expect(el[0].childNodes).to.have.length(3);
        });
        cy.contains("Er laget for").then((el) => {
          const element = el![0] as HTMLElement;
          const range = document.createRange();
          const selection = globalThis.getSelection()!;
          selection.removeAllRanges();
          range.setStart(element.firstChild!, 15);
          range.setEnd(element.firstChild!, 28);
          selection.addRange(range);
          element.focus();
        });
        cy.document().then((doc) => {
          const selectedText = doc.getSelection()?.toString();
          expect(selectedText).to.eq("teste piltast"); // Sanity check
        });
        cy.getDataCy("fonttype-bold").click();
        cy.contains("Er laget for").should("have.css", "font-weight", "400");
        cy.contains("teste piltast").should("have.css", "font-weight", "700");
        cy.contains(
          "opp og ned innad samme avsnitt[CP1-2]. Poenget er å teste [CP1-3] at caret går til nærmeste side av variable.",
        ).should("have.css", "font-weight", "400");
        cy.contains("Er laget for").should("have.css", "font-style", "normal");
        cy.contains("teste piltast").should("have.css", "font-style", "normal");
        cy.contains(
          "opp og ned innad samme avsnitt[CP1-2]. Poenget er å teste [CP1-3] at caret går til nærmeste side av variable.",
        ).should("have.css", "font-style", "normal");
        cy.contains("teste piltast").then((el) => {
          const element = el![0] as HTMLElement;
          const range = document.createRange();
          const selection = globalThis.getSelection()!;
          selection.removeAllRanges();
          range.setStart(element.firstChild!, 0);
          range.setEnd(element.firstChild!, 13);
          selection.addRange(range);
          element.focus();
        });
        cy.getDataCy("fonttype-italic").click();
        cy.contains("Er laget for").should("have.css", "font-weight", "400");
        cy.contains("teste piltast").should("have.css", "font-weight", "400");
        cy.contains(
          "opp og ned innad samme avsnitt[CP1-2]. Poenget er å teste [CP1-3] at caret går til nærmeste side av variable.",
        ).should("have.css", "font-weight", "400");
        cy.contains("Er laget for").should("have.css", "font-style", "normal");
        cy.contains("teste piltast").should("have.css", "font-style", "italic");
        cy.contains(
          "opp og ned innad samme avsnitt[CP1-2]. Poenget er å teste [CP1-3] at caret går til nærmeste side av variable.",
        ).should("have.css", "font-style", "normal");
        cy.getDataCy("PARAGRAPH").then((el) => {
          expect(el[0].childNodes).to.have.length(5);
        });
      });
    });
    describe("unmarked text", () => {
      it("endring av fonttype av en literal inne i et punkt skal bevare all content i punkten", () => {
        cy.mount(
          <EditorWithState
            initial={
              nyBrevResponse({
                redigertBrev: nyRedigertBrev({
                  blocks: [
                    nyParagraphBlock({
                      content: [
                        nyLiteral({ text: "Jeg er en literal," }),
                        nyLiteral({ text: " som blir fulgt opp av en annen literal," }),
                        nyLiteral({ text: " og til slutt, en siste literal" }),
                      ],
                    }),
                  ],
                }),
              }).redigertBrev
            }
          />,
        );
        cy.contains("Jeg er en literal, som blir fulgt opp av en annen literal, og til slutt, en siste literal")
          .should("exist")
          .click();
        cy.getDataCy("editor-bullet-list").click();
        cy.getDataCy("fonttype-bold").click();
        cy.contains("Jeg er en literal, som blir fulgt opp av en annen literal, og til slutt, en siste literal")
          .should("exist")
          .click();
      });

      it("handles switching plain/bold/plain", () => {
        cy.mount(<EditorWithState />);
        cy.getDataCy("PARAGRAPH").then((el) => {
          expect(el[0].childNodes).to.have.length(3);
        });
        cy.contains("Er laget").dblclick().type("{leftarrow}{leftarrow}");
        cy.getDataCy("fonttype-bold").click();
        cy.getDataCy("PARAGRAPH").then((el) => {
          expect(el[0].childNodes).to.have.length(4);
        });
        cy.contains(
          "Er laget for å teste piltast opp og ned innad samme avsnitt[CP1-2]. Poenget er å teste [CP1-3] at caret går til nærmeste side av",
        ).should("have.css", "font-weight", "400");
        cy.contains("variable.").should("have.css", "font-weight", "700");
        cy.contains("variable.").click();
        cy.getDataCy("fonttype-bold").click();
        cy.contains(
          "Er laget for å teste piltast opp og ned innad samme avsnitt[CP1-2]. Poenget er å teste [CP1-3] at caret går til nærmeste side av",
        ).should("have.css", "font-weight", "400");
        cy.contains("variable.").should("have.css", "font-weight", "400");
        cy.getDataCy("PARAGRAPH").then((el) => {
          expect(el[0].childNodes).to.have.length(4);
        });
      });
      it("handles switching plain/italic/plain", () => {
        cy.mount(<EditorWithState />);
        cy.getDataCy("PARAGRAPH").then((el) => {
          expect(el[0].childNodes).to.have.length(3);
        });
        cy.contains("Er laget").dblclick().type("{leftarrow}{leftarrow}");
        cy.getDataCy("fonttype-italic").click();
        cy.getDataCy("PARAGRAPH").then((el) => {
          expect(el[0].childNodes).to.have.length(4);
        });
        cy.contains(
          "Er laget for å teste piltast opp og ned innad samme avsnitt[CP1-2]. Poenget er å teste [CP1-3] at caret går til nærmeste side av",
        ).should("have.css", "font-style", "normal");
        cy.contains("variable.").should("have.css", "font-style", "italic");
        cy.contains("variable.").click();
        cy.getDataCy("fonttype-italic").click();
        cy.contains(
          "Er laget for å teste piltast opp og ned innad samme avsnitt[CP1-2]. Poenget er å teste [CP1-3] at caret går til nærmeste side av",
        ).should("have.css", "font-style", "normal");
        cy.contains("variable.").should("have.css", "font-style", "normal");
        cy.getDataCy("PARAGRAPH").then((el) => {
          expect(el[0].childNodes).to.have.length(4);
        });
      });
      it("handles switching plain/bold/italic", () => {
        cy.mount(<EditorWithState />);
        cy.getDataCy("PARAGRAPH").then((el) => {
          expect(el[0].childNodes).to.have.length(3);
        });
        cy.contains("Er laget").dblclick().type("{leftarrow}{leftarrow}");
        cy.getDataCy("fonttype-bold").click();
        cy.getDataCy("PARAGRAPH").then((el) => {
          expect(el[0].childNodes).to.have.length(4);
        });
        cy.contains(
          "Er laget for å teste piltast opp og ned innad samme avsnitt[CP1-2]. Poenget er å teste [CP1-3] at caret går til nærmeste side av",
        ).should("have.css", "font-weight", "400");
        cy.contains("variable.").should("have.css", "font-weight", "700");
        cy.contains(
          "Er laget for å teste piltast opp og ned innad samme avsnitt[CP1-2]. Poenget er å teste [CP1-3] at caret går til nærmeste side av",
        ).should("have.css", "font-style", "normal");
        cy.contains("variable.").should("have.css", "font-style", "normal");
        cy.contains("variable.").click();
        cy.getDataCy("fonttype-italic").click();
        cy.contains(
          "Er laget for å teste piltast opp og ned innad samme avsnitt[CP1-2]. Poenget er å teste [CP1-3] at caret går til nærmeste side av",
        ).should("have.css", "font-weight", "400");
        cy.contains("variable.").should("have.css", "font-weight", "400");
        cy.contains(
          "Er laget for å teste piltast opp og ned innad samme avsnitt[CP1-2]. Poenget er å teste [CP1-3] at caret går til nærmeste side av",
        ).should("have.css", "font-style", "normal");
        cy.contains("variable.").should("have.css", "font-style", "italic");
        cy.getDataCy("PARAGRAPH").then((el) => {
          expect(el[0].childNodes).to.have.length(4);
        });
      });
    });
  });

  describe("variables", () => {
    it("plain/bold/plain", () => {
      cy.mount(<EditorWithState />);
      cy.getDataCy("PARAGRAPH").then((el) => {
        expect(el[0].childNodes).to.have.length(3);
      });

      cy.contains("VARIABLE-MED-LITT-LENGDE").should("have.css", "font-weight", "400");
      cy.contains("VARIABLE-MED-LITT-LENGDE").click();
      cy.getDataCy("fonttype-bold").click();

      cy.getDataCy("PARAGRAPH").then((el) => {
        expect(el[0].childNodes).to.have.length(3);
      });
      cy.contains("VARIABLE-MED-LITT-LENGDE").should("have.css", "font-weight", "700");
      cy.getDataCy("fonttype-bold").click();
      cy.contains("VARIABLE-MED-LITT-LENGDE").should("have.css", "font-weight", "400");

      cy.getDataCy("PARAGRAPH").then((el) => {
        expect(el[0].childNodes).to.have.length(3);
      });
    });
    it("plain/italic/plain", () => {
      cy.mount(<EditorWithState />);
      cy.getDataCy("PARAGRAPH").then((el) => {
        expect(el[0].childNodes).to.have.length(3);
      });

      cy.contains("VARIABLE-MED-LITT-LENGDE").should("have.css", "font-style", "normal");
      cy.contains("VARIABLE-MED-LITT-LENGDE").click();
      cy.getDataCy("fonttype-italic").click();

      cy.getDataCy("PARAGRAPH").then((el) => {
        expect(el[0].childNodes).to.have.length(3);
      });

      cy.contains("VARIABLE-MED-LITT-LENGDE").should("have.css", "font-style", "italic");
      cy.getDataCy("fonttype-italic").click();
      cy.contains("VARIABLE-MED-LITT-LENGDE").should("have.css", "font-style", "normal");

      cy.getDataCy("PARAGRAPH").then((el) => {
        expect(el[0].childNodes).to.have.length(3);
      });
    });
    it("plain/bold/italic", () => {
      cy.mount(<EditorWithState />);
      cy.getDataCy("PARAGRAPH").then((el) => {
        expect(el[0].childNodes).to.have.length(3);
      });
      cy.contains("VARIABLE-MED-LITT-LENGDE").should("have.css", "font-style", "normal");
      cy.contains("VARIABLE-MED-LITT-LENGDE").should("have.css", "font-weight", "400");

      cy.contains("VARIABLE-MED-LITT-LENGDE").click();
      cy.getDataCy("fonttype-bold").click();

      cy.getDataCy("PARAGRAPH").then((el) => {
        expect(el[0].childNodes).to.have.length(3);
      });

      cy.contains("VARIABLE-MED-LITT-LENGDE").should("have.css", "font-style", "normal");
      cy.contains("VARIABLE-MED-LITT-LENGDE").should("have.css", "font-weight", "700");

      cy.getDataCy("fonttype-italic").click();

      cy.contains("VARIABLE-MED-LITT-LENGDE").should("have.css", "font-style", "italic");
      cy.contains("VARIABLE-MED-LITT-LENGDE").should("have.css", "font-weight", "400");

      cy.getDataCy("PARAGRAPH").then((el) => {
        expect(el[0].childNodes).to.have.length(3);
      });
    });
    it("merker deler av en variabel skal endre fonttypen på hele", () => {
      cy.mount(<EditorWithState />);
      cy.getDataCy("PARAGRAPH").then((el) => {
        expect(el[0].childNodes).to.have.length(3);
      });

      cy.contains("VARIABLE-MED-LITT-LENGDE").should("have.css", "font-weight", "400");
      cy.contains("VARIABLE-MED-LITT-LENGDE")
        .click()
        .then((el) => {
          const element = el![0] as HTMLElement;
          const range = document.createRange();
          const selection = globalThis.getSelection()!;
          selection.removeAllRanges();
          range.setStart(element.firstChild!, 5);
          range.setEnd(element.firstChild!, 10);
          selection.addRange(range);
          element.focus();
        });
      cy.getDataCy("fonttype-bold").click();

      cy.getDataCy("PARAGRAPH").then((el) => {
        expect(el[0].childNodes).to.have.length(3);
      });
      cy.contains("VARIABLE-MED-LITT-LENGDE").should("have.css", "font-weight", "700");
    });

    it("endring av fonttype av en variable inne i et punkt skal bevare all content i punkten", () => {
      cy.mount(<EditorWithState initial={nyBrevResponse({}).redigertBrev} />);

      cy.contains("Our processing time for this type of application is usually 10 weeks.").should("exist").click();
      cy.getDataCy("editor-bullet-list").click();
      cy.getDataCy("fonttype-bold").click();
      cy.contains("Our processing time for this type of application is usually 10 weeks.").should("exist").click();
    });
  });
});
