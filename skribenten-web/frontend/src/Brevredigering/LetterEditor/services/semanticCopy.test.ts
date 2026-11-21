import type React from "react";
import { afterEach, describe, expect, test } from "vitest";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { letter, literal, paragraph } from "~test/support/letterEditorTestUtils";
import { MockDataTransfer, projectLetter } from "~test/support/pasteTestUtils";

import { writeSemanticClipboard } from "./semanticCopy";

// Shaped like the editor's markup: blocks are divs with the block type as class, literals and variables are spans.
const lit = (text: string, fontType?: string) =>
  `<span data-literal="" data-literal-index="{}"${fontType ? ` data-font-type="${fontType}"` : ""}>${text}</span>`;
const vari = (text: string) => `<span contenteditable="false" data-variable="">${text}</span>`;
const block = (type: string, content: string) => `<div class="${type}">${content}</div>`;
const actions = `<div class="missing-from-template-actions" contenteditable="false"><button>Behold</button><button>Slett</button></div>`;

function renderEditor(html: string) {
  document.body.innerHTML = `<div contenteditable="true" data-editor-root>${html}</div>`;
  return document.querySelector<HTMLElement>("[data-editor-root]")!;
}

function select(root: HTMLElement, from: [literal: number, offset: number], to: [literal: number, offset: number]) {
  const literals = root.querySelectorAll("[data-literal]");
  const range = document.createRange();
  range.setStart(literals[from[0]].firstChild!, from[1]);
  range.setEnd(literals[to[0]].firstChild!, to[1]);
  globalThis.getSelection()!.removeAllRanges();
  globalThis.getSelection()!.addRange(range);
}

function copy(root: HTMLElement) {
  const clipboardData = new MockDataTransfer({});
  const written = writeSemanticClipboard({ clipboardData } as unknown as React.ClipboardEvent<HTMLElement>, root);
  return { html: clipboardData.getData("text/html"), text: clipboardData.getData("text/plain"), written };
}

function pasteIntoEmptyParagraph(html: string) {
  const state = letter(paragraph([literal({ text: "" })]));
  return projectLetter(
    Actions.paste(state, { blockIndex: 0, contentIndex: 0 }, 0, new MockDataTransfer({ "text/html": html })),
  );
}

afterEach(() => {
  globalThis.getSelection()?.removeAllRanges();
  document.body.innerHTML = "";
});

describe("copy from the editor as semantic html", () => {
  test("headings, paragraphs, variables and bold/italic survive a copy and paste", () => {
    const root = renderEditor(
      block("TITLE2", lit("Knirkende stoler")) +
        block(
          "PARAGRAPH",
          lit("Noen ") + vari("4A") + lit(" stoler ") + lit("knirker", "BOLD") + lit(" og ") + lit("hvisker", "ITALIC"),
        ),
    );
    select(root, [0, 0], [5, "hvisker".length]);

    const { html, text, written } = copy(root);

    expect(written).toBe(true);
    expect(html).toBe("<h2>Knirkende stoler</h2><p>Noen 4A stoler <b>knirker</b> og <i>hvisker</i></p>");
    expect(text).toBe("Knirkende stoler\nNoen 4A stoler knirker og hvisker");
    expect(pasteIntoEmptyParagraph(html)).toEqual([
      "H2: Knirkende stoler",
      "P: Noen 4A stoler **knirker** og _hvisker_",
    ]);
  });

  test.each([
    ["TITLE1", "h1"],
    ["TITLE2", "h2"],
    ["TITLE3", "h3"],
  ])("a whole %s inside one literal is copied as <%s>", (type, tag) => {
    const root = renderEditor(block(type, lit("Overskrift")) + block("PARAGRAPH", lit("Tekst")));
    select(root, [0, 0], [0, "Overskrift".length]);

    expect(copy(root).html).toBe(`<${tag}>Overskrift</${tag}>`);
  });

  test("part of a heading is copied as plain text, but bold within a literal is kept", () => {
    const root = renderEditor(block("TITLE1", lit("Overskrift")) + block("PARAGRAPH", lit("Noe fet tekst", "BOLD")));

    select(root, [0, 0], [0, 4]);
    expect(copy(root).html).toBe("Over");

    select(root, [1, 4], [1, 7]);
    expect(copy(root).html).toBe("<b>fet</b>");
  });

  test("leaves out editor-only markup and splits lists out of paragraphs", () => {
    const root = renderEditor(
      `<div class="TITLE1 missing-from-template-block">${actions}${lit("Over")}<span contenteditable="false" data-word-joiner="">\u2060</span>${lit("skrift")}</div>` +
        `<div class="PARAGRAPH" contenteditable="false" data-diff-deleted-block="">Slettet avsnitt</div>` +
        block("PARAGRAPH", `${lit("Innledning")}<ul><li>${lit("Første punkt")}</li><li>${lit("\u200B")}</li></ul>`),
    );
    select(root, [0, 0], [4, 1]);

    const { html, text } = copy(root);

    expect(html).toBe("<h1>Overskrift</h1><p>Innledning</p><ul><li>Første punkt</li><li></li></ul>");
    expect(text).toBe("Overskrift\nInnledning\nFørste punkt");
    expect(pasteIntoEmptyParagraph(html).slice(0, 3)).toEqual(["H1: Overskrift", "P: Innledning", "• Første punkt"]);
  });

  test("tables are copied with their cells, and as tab-separated plain text", () => {
    const root = renderEditor(
      block(
        "PARAGRAPH",
        `<table><thead><tr><th>${lit("Land")}</th><th>${lit("Svartid")}</th></tr></thead><tbody><tr><td>${lit("Spania")}</td><td>${lit("10 uker")}</td></tr></tbody></table>${lit("\u200B")}`,
      ),
    );
    select(root, [0, 0], [3, "10 uker".length]);

    const { html, text } = copy(root);

    expect(html).toBe(
      "<table><thead><tr><th>Land</th><th>Svartid</th></tr></thead><tbody><tr><td>Spania</td><td>10 uker</td></tr></tbody></table>",
    );
    expect(text).toBe("Land\tSvartid\nSpania\t10 uker");
  });

  test("several list items keep their list, while text within one item is copied as text", () => {
    const root = renderEditor(block("PARAGRAPH", `<ol><li>${lit("Første")}</li><li>${lit("Andre")}</li></ol>`));

    select(root, [0, 2], [1, 3]);
    expect(copy(root).html).toBe("<ol><li>rste</li><li>And</li></ol>");

    select(root, [1, 0], [1, 3]);
    expect(copy(root).html).toBe("And");
  });

  test("a selection reaching outside the editor keeps the browser's own copy", () => {
    const root = renderEditor(block("PARAGRAPH", lit("Tekst")));
    const outside = document.createElement("p");
    outside.textContent = "Utenfor";
    document.body.prepend(outside);

    const range = document.createRange();
    range.setStart(outside.firstChild!, 0);
    range.setEnd(root.querySelector("[data-literal]")!.firstChild!, 2);
    globalThis.getSelection()!.removeAllRanges();
    globalThis.getSelection()!.addRange(range);

    expect(copy(root).written).toBe(false);
  });
});
