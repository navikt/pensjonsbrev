import { expect, type Locator, test } from "@playwright/test";

import { type UnifiedLetterDiff } from "~/Brevredigering/LetterEditor/diff/diffModel";
import { FontType } from "~/types/brevbakerTypes";
import { setupSakStubs } from "~test/e2e/support/helpers";
import { brevInfo, brevResponse, editedLetter } from "~test/support/brevFixtures";
import {
  cell,
  item,
  itemList,
  literal,
  paragraph,
  row,
  table,
  title1,
  variable,
} from "~test/support/letterEditorTestUtils";

const addedText = "Tekst som er lagt til";
const removedText = "Tekst som er slettet";
const wrappingText = "Denne teksten er lagt til og skal brytes naturlig over flere linjer i brevet. ".repeat(4);
const letter = brevResponse({
  info: brevInfo({ saksId: 123456 }),
  redigertBrev: editedLetter({
    blocks: [
      title1(literal("Oppdatert overskrift")),
      paragraph([literal(`Uendret tekst. ${addedText}. `), { ...literal("Viktig tekst"), fontType: FontType.BOLD }]),
      paragraph([literal(wrappingText)]),
    ],
  }),
});

const diff: UnifiedLetterDiff = {
  type: "UNIFIED",
  editedBlocks: {
    0: {
      contentEdits: {
        0: { type: "TEXT", edit: { inserts: [{ startOffset: 0, endOffset: 9 }], deletes: [] } },
      },
      deletedContent: {},
    },
    1: {
      contentEdits: {
        0: {
          type: "TEXT",
          edit: {
            inserts: [{ startOffset: 15, endOffset: 15 + addedText.length }],
            deletes: [{ startOffset: 15, endOffset: 15 + removedText.length, text: removedText }],
          },
        },
        1: { type: "TEXT", edit: { inserts: [{ startOffset: 0, endOffset: 6 }], deletes: [] } },
      },
      deletedContent: {},
    },
    2: {
      contentEdits: {
        0: { type: "TEXT", edit: { inserts: [{ startOffset: 0, endOffset: wrappingText.length }], deletes: [] } },
      },
      deletedContent: {},
    },
  },
  deletedBlocks: {
    3: [
      title1(literal("Slettet overskrift")),
      paragraph([literal("Slettet avsnitt")]),
      paragraph([itemList({ items: [item(literal("Slettet listepunkt"))] })]),
      paragraph([table([cell(literal("Slettet kolonne"))], [row(cell(literal("Slettet tabelltekst")))])]),
      paragraph([
        { ...literal("Slettet fet tekst"), fontType: FontType.BOLD },
        literal(" "),
        { ...literal("Slettet kursiv tekst"), fontType: FontType.BOLD, editedFontType: FontType.ITALIC },
        literal(" "),
        { ...variable("Slettet fet variabel"), fontType: FontType.BOLD },
        literal(" "),
        { ...variable("Slettet kursiv variabel"), fontType: FontType.ITALIC },
        literal(" "),
        { ...literal("Slettet normal tekst"), fontType: FontType.BOLD, editedFontType: FontType.PLAIN },
      ]),
    ],
  },
};

async function expectAddedStyle(text: Locator) {
  await expect(text).toHaveCSS("color", "rgb(0, 108, 31)");
  await expect(text).toHaveCSS("background-color", "rgb(226, 253, 232)");
  await expect(text).toHaveCSS("text-decoration-line", "underline");
  await expect(text).toHaveCSS("text-decoration-style", "dotted");
  const thickness = await text.evaluate((element) => `${Number.parseFloat(getComputedStyle(element).fontSize) / 10}px`);
  await expect(text).toHaveCSS("text-decoration-thickness", thickness);
  await expect(text).toHaveCSS("text-underline-position", "from-font");
}

async function expectDeletedStyle(text: Locator) {
  await expect(text).toHaveCSS("color", "rgb(188, 0, 42)");
  await expect(text).toHaveCSS("background-color", "rgb(255, 242, 247)");
  await expect(text).toHaveCSS("text-decoration-line", "line-through");
  await expect(text).toHaveCSS("text-decoration-style", "solid");
  await expect(text).toHaveCSS("text-decoration-thickness", "from-font");
}

test.beforeEach(async ({ page }) => {
  await setupSakStubs(page);
  await page.route("**/bff/skribenten-backend/me/bekreftedeNyhetsvarsler", (route) =>
    route.fulfill({ json: ["diff-toggles"] }),
  );
  await page.route("**/bff/skribenten-backend/features/diff", (route) => route.fulfill({ json: { enabled: true } }));
  await page.route("**/bff/skribenten-backend/sak/123456/brev/1/attestering?reserver=true", (route) =>
    route.fulfill({ json: letter }),
  );
  await page.route("**/bff/skribenten-backend/brev/1/diff", (route) => route.fulfill({ json: diff }));
  await page.goto("/saksnummer/123456/attester/1/redigering");
});

test("viser nye diff-stiler uten å endre brevets typografi eller linjebryting", async ({ page }, testInfo) => {
  const editor = page.locator(".editor");
  const baselineText = await editor.textContent();
  const wrappingLiteral = editor.getByText(wrappingText.trim(), { exact: true });
  const wrappingParagraph = editor.locator(".PARAGRAPH", { hasText: wrappingText.trim() });
  const baselineBounds = await wrappingParagraph.boundingBox();
  expect(baselineBounds).not.toBeNull();

  await page.getByRole("checkbox", { name: "Marker tekst som er lagt til" }).check();
  await page.getByRole("checkbox", { name: "Vis slettet tekst" }).check();

  const added = editor.locator("[data-diff-insertion]", { hasText: addedText });
  const removed = editor.locator("[data-diff-deletion]");
  await expectAddedStyle(added);
  await expectDeletedStyle(removed);
  await expect(added).toHaveCSS("font-weight", "400");
  await expect(removed).toHaveCSS("font-weight", "400");
  await expect(removed).toHaveAttribute("contenteditable", "false");
  await expect(editor.locator(".TITLE1 [data-diff-insertion]")).toHaveCSS("font-weight", "700");
  await expect(editor.locator(".TITLE1 [data-diff-insertion]")).toHaveCSS("font-size", "19.5px");
  await expect(editor.locator("[data-diff-insertion]", { hasText: "Viktig" })).toHaveCSS("font-weight", "700");
  await expect(added).toHaveCSS("font-size", "16.5px");

  const wrappedInsertion = editor.locator("[data-diff-insertion]", { hasText: wrappingText.trim() });
  const wrappedBounds = await wrappingParagraph.boundingBox();
  expect(wrappedBounds?.height).toBe(baselineBounds?.height);
  expect(await wrappedInsertion.evaluate((element) => element.getClientRects().length)).toBeGreaterThan(1);

  await page.getByRole("button", { name: "Slik vises endringer" }).click();
  const legendAdded = page.getByText("Slik vises ord som er lagt til manuelt", { exact: true });
  const legendRemoved = page.getByText("Slik vises ord som er slettet manuelt", { exact: true });
  await expectAddedStyle(legendAdded);
  await expectDeletedStyle(legendRemoved);
  await expect(legendAdded).toHaveCSS("font-weight", "400");
  await expect(legendRemoved).toHaveCSS("font-weight", "400");
  await testInfo.attach("diff-og-forklaring", {
    body: await page.screenshot({ fullPage: true }),
    contentType: "image/png",
  });
  await page.keyboard.press("Escape");

  await page.getByRole("checkbox", { name: "Marker tekst som er lagt til" }).uncheck();
  await page.getByRole("checkbox", { name: "Vis slettet tekst" }).uncheck();
  await expect(editor.locator("[data-diff-insertion],[data-diff-deletion],[data-diff-deleted]")).toHaveCount(0);
  await expect.poll(() => editor.textContent()).toBe(baselineText);
  await expect(wrappingLiteral).toHaveCSS("font-weight", "400");
});

test("bruker samme slettestil i hele avsnitt, lister og tabeller", async ({ page }, testInfo) => {
  await page.getByRole("checkbox", { name: "Vis slettet tekst" }).check();
  const editor = page.locator(".editor");

  for (const text of [
    "Slettet avsnitt",
    "Slettet listepunkt",
    "Slettet tabelltekst",
    "Slettet overskrift",
    "Slettet kolonne",
  ]) {
    const deleted = editor.getByText(text, { exact: true });
    await expectDeletedStyle(deleted);
    await expect(deleted).toHaveCSS(
      "font-weight",
      text === "Slettet overskrift" || text === "Slettet kolonne" ? "700" : "400",
    );
  }
  await expect(editor.locator("[data-diff-deleted-block]").first()).toHaveAttribute("contenteditable", "false");
  await expect(editor.getByRole("listitem")).toHaveText("Slettet listepunkt");
  await expect(editor.getByRole("columnheader", { name: "Slettet kolonne" })).toBeVisible();
  for (const [text, weight, style] of [
    ["Slettet fet tekst", "700", "normal"],
    ["Slettet kursiv tekst", "400", "italic"],
    ["Slettet fet variabel", "700", "normal"],
    ["Slettet kursiv variabel", "400", "italic"],
    ["Slettet normal tekst", "400", "normal"],
  ]) {
    const deleted = editor.getByText(text, { exact: true });
    await expectDeletedStyle(deleted);
    await expect(deleted).toHaveCSS("font-weight", weight);
    await expect(deleted).toHaveCSS("font-style", style);
  }
  await testInfo.attach("slettet-innhold", {
    body: await page.screenshot({ fullPage: true }),
    contentType: "image/png",
  });
});
