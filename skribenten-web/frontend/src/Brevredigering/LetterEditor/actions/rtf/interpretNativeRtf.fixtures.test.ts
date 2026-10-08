/**
 * Parser tests against RTF produced by real applications (test/fixtures/rtf/).
 *
 * Sources, copied from tbluemel/rtf.js at commit 85fddf55b2f262bfd450769120c18c9ccef1f21c, licensed MIT:
 * https://github.com/tbluemel/rtf.js/blob/85fddf55b2f262bfd450769120c18c9ccef1f21c/LICENSE
 * - word2003-sample.rtf: verbatim copy (Microsoft Word 11) of
 *   https://github.com/tbluemel/rtf.js/blob/85fddf55b2f262bfd450769120c18c9ccef1f21c/test/rtf-test-files/sample/source.rtf
 * - word-numbered-lists.rtf: excerpt of
 *   https://github.com/tbluemel/rtf.js/blob/85fddf55b2f262bfd450769120c18c9ccef1f21c/test/rtf-test-files/wmf-and-emf/source.rtf
 *   Trimmed from 1.7 MB to the header, list tables and one list section; images and CRs removed.
 * - richedit-wordpad.rtf: verbatim copy (Msftedit 5.41, i.e. WordPad/RichEdit) of
 *   https://github.com/tbluemel/rtf.js/blob/85fddf55b2f262bfd450769120c18c9ccef1f21c/samples/.common/data/rtf/simple5.rtf
 *
 * Synthetic:
 * - word365-nb.rtf: structured according to the Word 365 clipboard format (Norwegian
 *   Bokmål), since no freely licensed modern captures were found.
 *
 * Output is projected to text: **fet**, _kursiv_, "•"/"1." for list items, "(n)" for nested items (`projectElements`).
 */
import { describe, expect, test } from "vitest";

import { interpretNativeRtf } from "~/Brevredigering/LetterEditor/actions/rtf/interpretNativeRtf";
import { createByteDecoder } from "~/Brevredigering/LetterEditor/actions/rtf/rtfDecoding";
import { tokenizeRtf } from "~/Brevredigering/LetterEditor/actions/rtf/tokenizeRtf";
import wordpad from "~test/fixtures/rtf/richedit-wordpad.rtf?raw";
import wordNumberedLists from "~test/fixtures/rtf/word-numbered-lists.rtf?raw";
import word365 from "~test/fixtures/rtf/word365-nb.rtf?raw";
import word2003Sample from "~test/fixtures/rtf/word2003-sample.rtf?raw";
import { projectElements } from "~test/support/pasteTestUtils";

const interpret = (rtf: string) => {
  const tokens = tokenizeRtf(rtf);
  return interpretNativeRtf(tokens, createByteDecoder(tokens));
};

describe("RTF from Word 365 (syntetisk, norsk bokmål)", () => {
  test("keeps headings, formatting, lists and the table", () => {
    expect(projectElements(interpret(word365))).toEqual([
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
    const text = projectElements(interpret(word365)).join("\n");

    expect(text).not.toContain("Slettet tekst");
    expect(text).not.toContain("Skjult tekst");
  });
});

describe("RTF from Word 2003 (rtf.js sample)", () => {
  test("keeps the heading, list and table, and drops hidden text and footnotes", () => {
    expect(projectElements(interpret(word2003Sample))).toEqual([
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

    expect(projectElements(interpret(wordNumberedLists))).toEqual([
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
    expect(projectElements(interpret(wordpad))).toEqual([
      "P: This is a **simple five paragraph **_document_.",
      "P: This is the second paragraph with a line break and it is centered.",
      "P: This is the third paragraph.",
      "P: This is the fourth paragraph and it is right aligned.",
      "P: And this paragraph is left aligned again.",
    ]);
  });
});
