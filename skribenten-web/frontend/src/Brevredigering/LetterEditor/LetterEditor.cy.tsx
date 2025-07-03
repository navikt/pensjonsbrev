import "./editor.css";

import React, { useState } from "react";

import Actions from "~/Brevredigering/LetterEditor/actions";
import type { Focus, LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { getRange } from "~/Brevredigering/LetterEditor/services/caretUtils";
import { SpraakKode } from "~/types/apiTypes";
import { type BrevResponse, Distribusjonstype } from "~/types/brev";
import type { EditedLetter, LiteralValue } from "~/types/brevbakerTypes";

import exampleLetter1Json from "./example-letter-1.json";
import { LetterEditor } from "./LetterEditor";

const exampleLetter1 = exampleLetter1Json as EditedLetter;

function EditorWithState({ initial, focus }: { initial: EditedLetter; focus?: Focus }) {
  const brevresponse: BrevResponse = {
    info: {
      id: 1,
      brevkode: "BREV1",
      brevtittel: "Brev 1",
      opprettet: "2024-01-01",
      sistredigert: "2024-01-01",
      sistredigertAv: { id: "Z123", navn: "Z entotre" },
      opprettetAv: { id: "Z123", navn: "Z entotre" },
      status: { type: "UnderRedigering", redigeresAv: { id: "Z123", navn: "Z entotre" } },
      distribusjonstype: Distribusjonstype.SENTRALPRINT,
      mottaker: null,
      avsenderEnhet: null,
      spraak: SpraakKode.Bokmaal,
      journalpostId: null,
      vedtaksId: null,
      brevtype: "INFORMASJONSBREV",
    },
    redigertBrev: initial,
    redigertBrevHash: "hash1",
    saksbehandlerValg: {},
  };
  const newState = Actions.create(brevresponse);
  if (focus) {
    newState.focus = focus;
  }
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
}

describe("<LetterEditor />", () => {
  beforeEach(() => {
    cy.viewport(800, 1400);
  });

  describe("Navigation", () => {
    it("ArrowUp works within sibling contenteditables", () => {
      cy.mount(<EditorWithState initial={exampleLetter1} />);
      cy.contains("CP1-3").click();
      move("{end}", 1);
      move("{leftArrow}", 10);
      move("{upArrow}", 1);
      assertCaret("[CP1-2]", 22);
    });
    it("ArrowDown works within sibling contenteditables", () => {
      cy.mount(<EditorWithState initial={exampleLetter1} />);
      cy.contains("CP1-1").click();
      move("{downArrow}", 1);
      assertCaret("[CP1-2]", 74);
    });
    it("ArrowUp moves to the right of a variable if that is closest", () => {
      cy.mount(<EditorWithState initial={exampleLetter1} />);
      cy.contains("CP1-3").click();
      move("{leftArrow}", 45);
      move("{upArrow}", 1);
      assertCaret("[CP1-2]", 0);
    });
    it("ArrowUp moves to the left of a variable if that is closest", () => {
      cy.mount(<EditorWithState initial={exampleLetter1} />);
      cy.contains("CP1-3").click();
      move("{home}", 1);
      move("{rightArrow}", 30);
      move("{upArrow}", 1);
      assertCaret("[CP1-1]", 21);
    });
    it("ArrowUp works between paragraphs", () => {
      cy.mount(<EditorWithState initial={exampleLetter1} />);
      cy.contains("CP2-2").click();
      move("{upArrow}", 1);
      assertCaret("[CP2-1]", 17);
    });
    it("ArrowDown works between paragraphs", () => {
      cy.mount(<EditorWithState initial={exampleLetter1} />);
      cy.contains("CP2-1").click();
      move("{downArrow}", 1);
      assertCaret("[CP2-3]", 7);
    });
    it("ArrowDown moves between paragraphs and to the nearest side of a variable [LEFT]", () => {
      cy.mount(<EditorWithState initial={exampleLetter1} />);
      cy.contains("CP2-1").click();
      move("{end}", 1);
      move("{leftArrow}", 20);
      move("{downArrow}", 1);
      assertCaret("[CP2-2]", 17);
    });
    it("ArrowDown moves between paragraphs and to the nearest side of a variable [RIGHT]", () => {
      cy.mount(<EditorWithState initial={exampleLetter1} />);
      cy.contains("CP2-1").click();
      move("{leftArrow}", 10);
      move("{downArrow}", 1);
      assertCaret("[CP2-3]", 0);
    });
    it("Can move up an itemlist", () => {
      cy.mount(<EditorWithState initial={exampleLetter1} />);
      // CP3
      cy.contains("CP3-3").click();
      move("{upArrow}", 1);
      assertCaret("[CP3-2]", 28);
      move("{upArrow}", 1);
      assertCaret("[CP3-1]", 136);
      move("{upArrow}", 1);
      assertCaret("[CP3-1]", 29);
      move("{upArrow}", 1);
      assertCaret("Tittel over punktliste", 22);
    });
    it("Can move down an itemlist", () => {
      cy.mount(<EditorWithState initial={exampleLetter1} />);
      // CP3
      cy.contains("CP3-1").click();
      move("{home}", 1);
      assertCaret("[CP3-1]", 108);
      move("{downArrow}", 1);
      assertCaret("[CP3-2]", 0);
      move("{downArrow}", 1);
      assertCaret("[CP3-3]", 0);
      move("{downArrow}", 1);
      assertCaret("Tittel under punktliste", 5);
    });
    it("ArrowUp at first node moves caret to the beginning", () => {
      cy.mount(<EditorWithState initial={exampleLetter1} />);
      cy.contains("CP1-1").click();
      move("{upArrow}", 1);
      assertCaret("[CP1-1]", 0);
    });
    it("ArrowDown at last node moves caret to the end", () => {
      cy.mount(<EditorWithState initial={exampleLetter1} />);
      cy.contains("CP4-1").click();
      move("{leftArrow}", 10);
      move("{downArrow}", 1);
      assertCaret("CP4-1", 68);
    });
    it("ArrowUp moves caret to start of previous line  even if the caret is before the start of that line", () => {
      cy.mount(<EditorWithState initial={exampleLetter1} />);
      cy.contains("CP4-1").click();
      move("{home}", 1);
      move("{upArrow}", 2);
      assertCaret("CP3-3", 0);
    });
    it("ArrowDown moves caret to start of next line even if the caret is before the start of that line", () => {
      cy.mount(<EditorWithState initial={exampleLetter1} />);
      cy.contains("Tittel over punktliste").click();
      move("{home}", 1);
      move("{downArrow}", 1);
      assertCaret("CP3-1", 0);
    });
  });

  describe("Focus", () => {
    it("invalid focus is ignored", () => {
      const invalidCursorPosition = (exampleLetter1.blocks[0].content[2] as LiteralValue).text.length + 10;
      const invalidFocus = { blockIndex: 0, contentIndex: 2, cursorPosition: invalidCursorPosition };
      cy.mount(<EditorWithState focus={invalidFocus} initial={exampleLetter1} />);

      cy.contains("Informasjon om saksbehandlingstiden vår");
    });
  });
});

function move(key: string, times: number) {
  cy.focused().type(Cypress._.repeat(key, times));
}

function assertCaret(content: string, caretOffset: number) {
  cy.get(".editor").then(() => {
    cy.focused().contains(content);
    expect(getRange()?.startOffset).to.eq(caretOffset);
  });
}
