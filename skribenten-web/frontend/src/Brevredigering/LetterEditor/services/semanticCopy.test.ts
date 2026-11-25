import type React from "react";
import { afterEach, describe, expect, test } from "vitest";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { letter, literal, paragraph } from "~test/support/letterEditorTestUtils";
import { MockDataTransfer, projectLetter } from "~test/support/pasteTestUtils";

import { copyAsSemanticHtml } from "./semanticCopy";

// Shaped like the editor's markup: blocks are divs with the block type as class, literals are spans.
const lit = (text: string, fontType?: string) =>
  `<span contenteditable="true" data-literal-index="{}"${fontType ? ` data-font-type="${fontType}"` : ""}>${text}</span>`;
const title = (type: string, tag: string, content: string) => `<div class="${type}"><${tag}>${content}</${tag}></div>`;
const para = (content: string) => `<div class="PARAGRAPH">${content}</div>`;
const actions = `<div class="missing-from-template-actions"><button>Behold</button><button>Slett</button></div>`;

function renderEditor(html: string) {
  document.body.innerHTML = `<div data-editor-root>${html}</div>`;
  return document.querySelector<HTMLElement>("[data-editor-root]")!;
}

function select(root: HTMLElement, from: [literal: number, offset: number], to: [literal: number, offset: number]) {
  const literals = root.querySelectorAll("[data-literal-index]");
  const range = document.createRange();
  range.setStart(literals[from[0]].firstChild!, from[1]);
  range.setEnd(literals[to[0]].firstChild!, to[1]);
  globalThis.getSelection()!.removeAllRanges();
  globalThis.getSelection()!.addRange(range);
}

function copy(root: HTMLElement) {
  const clipboardData = new MockDataTransfer({});
  let prevented = false;
  const preventDefault = () => {
    prevented = true;
  };
  copyAsSemanticHtml({
    currentTarget: root,
    clipboardData,
    preventDefault,
  } as unknown as React.ClipboardEvent<HTMLElement>);
  return { html: clipboardData.getData("text/html"), text: clipboardData.getData("text/plain"), prevented };
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
  test("headings, paragraphs and bold/italic survive a copy and paste", () => {
    const root = renderEditor(
      title("TITLE2", "h3", lit("Knirkende stoler")) +
        para(lit("Noen stoler ") + lit("knirker", "BOLD") + lit(" og ") + lit("hvisker", "ITALIC") + lit(".")),
    );
    select(root, [0, 0], [5, 1]);

    const { html, text, prevented } = copy(root);

    expect(prevented).toBe(true);
    expect(html).toBe("<h2>Knirkende stoler</h2><p>Noen stoler <b>knirker</b> og <i>hvisker</i>.</p>");
    expect(text).toContain("Knirkende stoler");
    expect(pasteIntoEmptyParagraph(html)).toEqual(["H2: Knirkende stoler", "P: Noen stoler **knirker** og _hvisker_."]);
  });

  test.each([
    ["TITLE1", "h2", "h1"],
    ["TITLE2", "h3", "h2"],
    ["TITLE3", "h4", "h3"],
  ])("a whole %s inside one literal is copied as <%s>, numbered like the editor's headings", (type, domTag, tag) => {
    const root = renderEditor(title(type, domTag, lit("Overskrift")) + para(lit("Tekst")));
    select(root, [0, 0], [0, "Overskrift".length]);

    expect(copy(root).html).toBe(`<${tag}>Overskrift</${tag}>`);
  });

  test("part of a heading is copied as plain text, but keeps bold within a literal", () => {
    const root = renderEditor(title("TITLE1", "h2", lit("Overskrift")) + para(lit("Noe fet tekst", "BOLD")));

    select(root, [0, 0], [0, 4]);
    expect(copy(root).html).toBe("Over");

    select(root, [1, 4], [1, 7]);
    expect(copy(root).html).toBe("<b>fet</b>");
  });

  test("leaves out action buttons and editor-only characters, and splits lists out of paragraphs", () => {
    const root = renderEditor(
      `<div class="TITLE1 missing-from-template-block">${actions}<h2>${lit("Over\u2060")}${lit("skrift")}</h2></div>` +
        para(`${lit("Innledning")}<ul><li>${lit("Første punkt")}</li><li>${lit("\u200B")}</li></ul>`),
    );
    select(root, [0, 0], [4, 1]);

    const { html } = copy(root);

    expect(html).toBe("<h1>Overskrift</h1><p>Innledning</p><ul><li>Første punkt</li><li></li></ul>");
    expect(pasteIntoEmptyParagraph(html).slice(0, 3)).toEqual(["H1: Overskrift", "P: Innledning", "• Første punkt"]);
  });

  test("a selection across template actions has only letter content in both clipboard formats", () => {
    const root = renderEditor(`${para(lit("Start"))}<div class="TITLE1">${actions}<h2>${lit("End")}</h2></div>`);
    select(root, [0, 0], [1, 3]);
    const { html, text } = copy(root);
    expect(html).toBe("<p>Start</p><h1>End</h1>");
    expect(text).toBe("Start\nEnd");
  });

  test("copying items within a numbered list keeps <ol>, while text inside one item stays inline", () => {
    const root = renderEditor(para(`<ol><li>${lit("Første")}</li><li>${lit("Andre", "BOLD")}</li></ol>`));

    select(root, [0, 0], [1, 5]);
    const { html } = copy(root);
    expect(html).toBe("<ol><li>Første</li><li><b>Andre</b></li></ol>");
    expect(pasteIntoEmptyParagraph(html)).toEqual(["1. Første", "1. **Andre**", "1. "]);

    select(root, [1, 0], [1, 5]);
    expect(copy(root).html).toBe("<b>Andre</b>");
  });

  test("copying text and list items within one paragraph block keeps the paragraph separate from the list", () => {
    const root = renderEditor(para(`${lit("Innledning")}<ol><li>${lit("Første")}</li><li>${lit("Andre")}</li></ol>`));

    select(root, [0, 0], [1, 6]);
    const { html } = copy(root);
    expect(html).toBe("<p>Innledning</p><ol><li>Første</li></ol>");
    expect(pasteIntoEmptyParagraph(html)).toEqual(["P: Innledning", "1. Første", "1. "]);
  });

  test("copying across table cells or a whole table keeps table wrappers, while one cell stays inline", () => {
    const root = renderEditor(
      para(
        `<table><thead><tr><th>${lit("H1")}</th><th>${lit("H2")}</th></tr></thead>` +
          `<tbody><tr><td>${lit("A1")}</td><td>${lit("B1", "ITALIC")}</td></tr></tbody></table>` +
          `<button aria-label="Tabellmeny" type="button"></button>`,
      ),
    );

    select(root, [2, 0], [3, 2]);
    const rowCopy = copy(root);
    expect(rowCopy.html).toBe("<table><tbody><tr><td>A1</td><td><i>B1</i></td></tr></tbody></table>");
    expect(pasteIntoEmptyParagraph(rowCopy.html)).toEqual(["TABLE", "  th: A1 | _B1_"]);

    select(root, [0, 0], [3, 2]);
    const tableCopy = copy(root);
    expect(tableCopy.html).toBe(
      "<table><thead><tr><th>H1</th><th>H2</th></tr></thead><tbody><tr><td>A1</td><td><i>B1</i></td></tr></tbody></table>",
    );
    expect(pasteIntoEmptyParagraph(tableCopy.html)).toEqual(["TABLE", "  th: H1 | H2", "  tr: A1 | _B1_"]);

    select(root, [3, 0], [3, 2]);
    expect(copy(root).html).toBe("<i>B1</i>");
  });

  test("a selection reaching outside the editor surface keeps the browser's own copy", () => {
    const root = renderEditor(para(lit("Tekst")));
    const outside = document.createElement("p");
    outside.textContent = "Utenfor";
    document.body.prepend(outside);

    const range = document.createRange();
    range.setStart(outside.firstChild!, 0);
    range.setEnd(root.querySelector("[data-literal-index]")!.firstChild!, 2);
    globalThis.getSelection()!.removeAllRanges();
    globalThis.getSelection()!.addRange(range);

    expect(copy(root).prevented).toBe(false);
  });
});
