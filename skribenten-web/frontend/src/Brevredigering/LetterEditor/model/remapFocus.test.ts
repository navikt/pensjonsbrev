import { describe, expect, test } from "vitest";

import { TITLE_INDEX } from "~/types/brevbakerTypes";
import { editedLetter } from "~test/support/brevFixtures";
import { cell, item, itemList, literal, paragraph, row, table } from "~test/support/letterEditorTestUtils";

import { remapFocus } from "./remapFocus";

const redigert = literal({ id: 1, text: "Redigert tekst" });
const avsnitt = paragraph({ id: 10, content: [literal({ id: 2, text: "Først" }), redigert] });
const flettetInn = paragraph({ id: 20, content: [literal({ id: 3, text: "Nytt fra malen" })] });

describe("remapFocus", () => {
  test("følger innholdet når malen setter inn et avsnitt foran", () => {
    const from = editedLetter({ blocks: [avsnitt] });
    const to = editedLetter({ blocks: [flettetInn, avsnitt] });

    expect(remapFocus({ blockIndex: 0, contentIndex: 1, cursorPosition: 4 }, from, to)).toEqual({
      blockIndex: 1,
      contentIndex: 1,
      cursorPosition: 4,
    });
  });

  test("følger nye elementer uten id etter rekkefølge og begrenser markøren til teksten", () => {
    const nyttAvsnitt = paragraph({ content: [{ ...literal("Ny tekst"), id: null }] });
    const from = editedLetter({ blocks: [avsnitt, nyttAvsnitt] });
    const to = editedLetter({
      blocks: [flettetInn, avsnitt, paragraph({ content: [{ ...literal("Ny"), id: null }] })],
    });

    expect(remapFocus({ blockIndex: 1, contentIndex: 0, cursorPosition: 8 }, from, to)).toEqual({
      blockIndex: 2,
      contentIndex: 0,
      cursorPosition: 2,
    });
  });

  test("følger punkter og tabellceller", () => {
    const liste = itemList({ id: 30, items: [item({ id: 31, content: [literal({ id: 32, text: "Punkt" })] })] });
    const tabell = table(
      [cell(literal({ id: 40, text: "Overskrift" }))],
      [row(cell(literal({ id: 41, text: "Celle" })))],
    );
    const from = editedLetter({ blocks: [paragraph({ id: 11, content: [liste, tabell] })] });
    const to = editedLetter({ blocks: [flettetInn, paragraph({ id: 11, content: [tabell, liste] })] });

    expect(
      remapFocus({ blockIndex: 0, contentIndex: 0, itemIndex: 0, itemContentIndex: 0, cursorPosition: 1 }, from, to),
    ).toEqual({ blockIndex: 1, contentIndex: 1, itemIndex: 0, itemContentIndex: 0, cursorPosition: 1 });
    expect(
      remapFocus(
        { blockIndex: 0, contentIndex: 1, rowIndex: -1, cellIndex: 0, cellContentIndex: 0, cursorPosition: 2 },
        from,
        to,
      ),
    ).toEqual({ blockIndex: 1, contentIndex: 0, rowIndex: -1, cellIndex: 0, cellContentIndex: 0, cursorPosition: 2 });
    expect(
      remapFocus(
        { blockIndex: 0, contentIndex: 1, rowIndex: 0, cellIndex: 0, cellContentIndex: 0, cursorPosition: 3 },
        from,
        to,
      ),
    ).toEqual({ blockIndex: 1, contentIndex: 0, rowIndex: 0, cellIndex: 0, cellContentIndex: 0, cursorPosition: 3 });
  });

  test("beholder fokus i tittelen", () => {
    const from = editedLetter({ blocks: [avsnitt] });
    const to = editedLetter({ blocks: [flettetInn, avsnitt] });

    expect(remapFocus({ blockIndex: TITLE_INDEX, contentIndex: 0, cursorPosition: 3 }, from, to)).toEqual({
      blockIndex: TITLE_INDEX,
      contentIndex: 0,
      cursorPosition: 3,
    });
  });

  test("faller tilbake til starten av nærmeste foregående avsnitt når innholdet er fjernet", () => {
    const fjernet = paragraph({ id: 50, content: [literal({ id: 51, text: "Fjernes" })] });
    const from = editedLetter({ blocks: [avsnitt, fjernet] });
    const to = editedLetter({ blocks: [flettetInn, avsnitt] });

    expect(remapFocus({ blockIndex: 1, contentIndex: 0, cursorPosition: 3, selectAll: true }, from, to)).toEqual({
      blockIndex: 1,
      contentIndex: 0,
      cursorPosition: 0,
    });
  });
});
