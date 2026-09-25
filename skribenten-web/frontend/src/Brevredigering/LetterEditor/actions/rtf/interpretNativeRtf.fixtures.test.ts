/**
 * Parser tests against RTF produced by real applications (test/fixtures/rtf/).
 *
 * Sources:
 * - word2003-sample.rtf: tbluemel/rtf.js@85fddf5 test/rtf-test-files/sample/source.rtf
 *   (Microsoft Word 11), MIT, https://github.com/tbluemel/rtf.js
 * - word-numbered-lists.rtf: excerpt of tbluemel/rtf.js@85fddf5 test/rtf-test-files/wmf-and-emf/source.rtf,
 *   MIT. Trimmed from 1.7 MB to the header, list tables and one list section; images removed.
 * - richedit-wordpad.rtf: tbluemel/rtf.js@85fddf5 samples/.common/data/rtf/simple5.rtf
 *   (Msftedit 5.41, i.e. WordPad/RichEdit), MIT
 * - word365-nb.rtf: syntetisk, etter strukturen i Word 365-utklipp (norsk bokmål), since no freely
 *   licensed modern captures were found.
 *
 * Output is projected to text: **fet**, _kursiv_, "•"/"1." for list items.
 */
import { describe, expect, test } from "vitest";

import { interpretNativeRtf } from "~/Brevredigering/LetterEditor/actions/rtf/interpretNativeRtf";
import { type Text, type TraversedElement } from "~/Brevredigering/LetterEditor/actions/traversedElement";
import { FontType, ListType } from "~/types/brevbakerTypes";
import wordpad from "~test/fixtures/rtf/richedit-wordpad.rtf?raw";
import wordNumberedLists from "~test/fixtures/rtf/word-numbered-lists.rtf?raw";
import word365 from "~test/fixtures/rtf/word365-nb.rtf?raw";
import word2003Sample from "~test/fixtures/rtf/word2003-sample.rtf?raw";

function runs(content: Text[]): string {
  return content
    .map((run) => {
      if (run.font === FontType.BOLD) return `**${run.text}**`;
      if (run.font === FontType.ITALIC) return `_${run.text}_`;
      return run.text;
    })
    .join("");
}

function project(elements: TraversedElement[]): string[] {
  return elements.flatMap((element) => {
    switch (element.type) {
      case "TEXT": {
        return [`TEXT: ${runs([element])}`];
      }
      case "ITEM": {
        return [`${element.listType === ListType.NUMMERERT_LISTE ? "1." : "•"} ${runs(element.content)}`];
      }
      case "TABLE": {
        const row = (cells: { content: Text[] }[]) => cells.map((cell) => runs(cell.content)).join(" | ");
        return [
          "TABLE",
          ...(element.headerCells ? [`  th: ${row(element.headerCells)}`] : []),
          ...element.rows.map((tableRow) => `  tr: ${row(tableRow.cells)}`),
        ];
      }
      default: {
        return [`${element.type}: ${runs(element.content)}`];
      }
    }
  });
}

describe("RTF from Word 365 (syntetisk, norsk bokmål)", () => {
  test("keeps headings, formatting, lists and the table", () => {
    expect(project(interpretNativeRtf(word365))).toEqual([
      "H1: Vedtak om alderspensjon",
      "P: Vi har **innvilget** søknaden din om _alderspensjon_ fra 1. mai 2026.",
      "P: ",
      "H2: Dette er grunnlaget for vedtaket",
      "• Du har fylt 67 år.",
      "• Du har bodd i Norge i minst fem år.",
      "1. Pensjonen blir utbetalt den 20. hver måned.",
      "1. Utbetaling i utlandet skjer i euro (€).",
      "P: Du skrev “jeg vil ha pensjon” i søknaden – det har vi tatt hensyn til. Ny linje i samme avsnitt.",
      "TABLE",
      "  th: **Periode** | **Beløp per måned**",
      "  tr: Fra 1. mai 2026 | kr 25 000",
      "  tr: Fra 1. mai 2027 | kr 25 500",
      "P: ",
      "P: Med vennlig hilsen",
    ]);
  });

  test("drops tracked deletions and hidden text", () => {
    const text = project(interpretNativeRtf(word365)).join("\n");

    expect(text).not.toContain("Slettet tekst");
    expect(text).not.toContain("Skjult tekst");
  });
});

describe("RTF from Word 2003 (rtf.js sample)", () => {
  test("keeps the heading, list and table, and drops hidden text and footnotes", () => {
    expect(project(interpretNativeRtf(word2003Sample))).toEqual([
      "H1: **This is a test RTF**",
      "P: Hi! I’m a test file. This is some **bold** text, and some _italic_ text, as well as some underline text. And a bit of text. So we’re going to end this paragraph here and go on to a nice little list:",
      "P: ",
      "• Item 1",
      "• Item 2",
      "• Item 3",
      "• Item 4",
      "P: ",
      "P: And now comes a fun table:",
      "P: ",
      "TABLE",
      "  tr: Cell 1 | Cell 2 More in cell 2 | Cell 3",
      "  tr: Next row | Next row | Next row",
      "P: ",
      "P: A page break:",
      "P: And here we’re on the next page.",
      "P: This para has a footnote.",
      "P: And here’s yet another paragraph.",
    ]);
  });
});

describe("RTF from Word with {\\listtext} numbered lists (rtf.js wmf-and-emf excerpt)", () => {
  test("reads numbered lists from the marker text and keeps the table", () => {
    const items = (...texts: string[]) => texts.map((text) => `1. ${text}`);

    expect(project(interpretNativeRtf(wordNumberedLists))).toEqual([
      "P: **Technology Solution System Integration Test Process**",
      "P: ",
      "P: The Technology Solution System Integration Testing (SIT) process validates that the technology solution and its features conform to the Technology Solution Design document specifications and the Technology Solution Requirements, prior to the customer testing.",
      "P: ",
      "P: The objectives of the Technology Solution System Integration Testing Process are:",
      "P: ",
      ...items(
        "To validate that the technology solution meets all documented requirements.",
        "To identify and correct problems in the technology solution before it is installed in the Customer Acceptance Testing (CAT) environment.",
        "To determine how the technology solution measures against technology service provider expectations.",
        "To ensure no increase in risk to existing Postal Service infrastructure and applications.",
      ),
      "P: ",
      "P: ",
      "TABLE",
      "  tr: **SCOPE**",
      "P: ",
      "P: The SIT process includes, but is not limited to, the following:",
      "P: ",
      ...items(
        "All technology solutions and their components (hardware, software, database, and/or network) to be authorized in the approved Technology Solution Requirements document.",
        "All services (including network, server, and mainframe) to be deployed in the Postal Service Technical Environment.",
        "All database management systems (DBMS) to be developed to operate in the Postal Service Technical Environment.",
        "All maintenance releases for services and technology solutions to be operable in the Postal Service Technical Environment. Maintenance releases include hardware, software, network, and DBMS upgrades.",
      ),
    ]);
  });
});

describe("RTF from WordPad (rtf.js simple5)", () => {
  test("keeps formatting toggled mid-paragraph and joins \\line breaks", () => {
    expect(project(interpretNativeRtf(wordpad))).toEqual([
      "P: This is a **simple five paragraph **_document_.",
      "P: This is the second paragraph with a line break and it is centered.",
      "P: This is the third paragraph.",
      "P: This is the fourth paragraph and it is right aligned.",
      "P: And this paragraph is left aligned again.",
    ]);
  });
});
