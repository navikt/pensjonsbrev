import { describe, expect, test } from "vitest";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { letter, literal, paragraph } from "~test/support/letterEditorTestUtils";
import { MockDataTransfer, projectLetter } from "~test/support/pasteTestUtils";

// Shaped like Chrome's clipboard HTML when copying inside the editor (inline styles trimmed to the relevant ones).
const literalSpan = (text: string, style = "line-height: 1.25rem;") =>
  `<span class="css-0" data-literal-index="{&quot;blockIndex&quot;:0,&quot;contentIndex&quot;:0}" contenteditable="true" style="${style}">${text}</span>`;
const block = (className: string, content: string, style = "font-weight: 400;") =>
  `<div class="${className}" style="${style}">${content}</div>`;

function pasteIntoEmptyParagraph(html: string) {
  const state = letter(paragraph([literal({ text: "" })]));
  return projectLetter(
    Actions.paste(state, { blockIndex: 0, contentIndex: 0 }, 0, new MockDataTransfer({ "text/html": html })),
  );
}

describe("paste content copied from Skribenten", () => {
  test("a heading followed by a paragraph keeps the heading as its own block", () => {
    const html =
      block("TITLE2", literalSpan("Knirkende stoler i rom 4A"), "font-size: 18px; font-weight: 700;") +
      block("PARAGRAPH", literalSpan("Noen stoler i 4A har begynt å kommunisere via knirk."));

    expect(pasteIntoEmptyParagraph(html)).toEqual([
      "H2: Knirkende stoler i rom 4A",
      "P: Noen stoler i 4A har begynt å kommunisere via knirk.",
    ]);
  });

  test.each([
    ["TITLE1", "H1"],
    ["TITLE2", "H2"],
    ["TITLE3", "H3"],
  ])("%s becomes %s, also with extra block classes", (blockType, expected) => {
    const html =
      block(`${blockType} inserted-flash-block`, literalSpan("Overskrift")) +
      block("PARAGRAPH missing-from-template-block", literalSpan("Tekst"));

    expect(pasteIntoEmptyParagraph(html)).toEqual([`${expected}: Overskrift`, "P: Tekst"]);
  });

  test("consecutive paragraphs stay separate", () => {
    const html = block("PARAGRAPH", literalSpan("Første")) + block("PARAGRAPH", literalSpan("Andre"));

    expect(pasteIntoEmptyParagraph(html)).toEqual(["P: Første", "P: Andre"]);
  });

  test("bold and italic literals keep their font, fritekst placeholder weight does not become bold", () => {
    const html = block(
      "PARAGRAPH",
      literalSpan("Vanlig ") +
        literalSpan("fet", "font-weight: 700;") +
        literalSpan(" og ") +
        literalSpan("kursiv", "font-style: italic;") +
        literalSpan(" og ") +
        literalSpan("[fritekst]", "font-weight: 600;"),
    );

    expect(pasteIntoEmptyParagraph(html)).toEqual(["P: Vanlig **fet** og _kursiv_ og [fritekst]"]);
  });

  test("a paragraph with text and a list pastes like the equivalent semantic html", () => {
    const html = block(
      "PARAGRAPH",
      `${literalSpan("Innledning")}<ul><li>${literalSpan("Første punkt")}</li><li>${literalSpan("Andre punkt")}</li></ul>`,
    );

    expect(pasteIntoEmptyParagraph(html)).toEqual(
      pasteIntoEmptyParagraph("<p>Innledning</p><ul><li>Første punkt</li><li>Andre punkt</li></ul>"),
    );
    expect(pasteIntoEmptyParagraph(html).slice(0, 3)).toEqual(["P: Innledning", "• Første punkt", "• Andre punkt"]);
  });
});
