import "./editor.css";

import React from "react";

import { getRange } from "~/Brevredigering/LetterEditor/services/caretUtils";
import type { RenderedLetter } from "~/types/brevbakerTypes";

import exampleLetter1 from "./example-letter-1.json";
import { LetterEditor } from "./LetterEditor";

describe("<LetterEditor />", () => {
  it("Vertical navigation", () => {
    cy.viewport(800, 1400);
    cy.mount(<LetterEditor initialState={exampleLetter1 as RenderedLetter} />);
    cy.contains("punkt 3").click();
    move("{upArrow}", 3);
    move("{rightArrow}", 20);
    move("{upArrow}", 1);
    assertCaret("CP1", 0);
    move("{leftArrow}", 1);
    move("{downArrow}", 1);
    move("{rightArrow}", 4);
    move("{upArrow}", 1);
    assertCaret("fra trygdemyndighetene i", 26);
  });
});

function move(key: string, times: number) {
  cy.focused().type(key.repeat(times));
}

function assertCaret(content: string, caretOffset: number) {
  cy.get(".editor").then(() => {
    cy.contains(content).focused();
    expect(getRange()?.startOffset).to.eq(caretOffset);
  });
}
