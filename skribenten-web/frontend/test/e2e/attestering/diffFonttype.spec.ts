import { expect, type Page, test } from "@playwright/test";

import { setupSakStubs } from "~test/e2e/support/helpers";
import { brevInfo, brevResponse, editedLetter } from "~test/support/brevFixtures";
import { literal, paragraph } from "~test/support/letterEditorTestUtils";

// Modellteksten er "Ny tekst her"; diffen viser "Ny " som lagt til og "gammel " som slettet foran "tekst her".
const brev = brevResponse({
  info: brevInfo({ saksId: 123456 }),
  redigertBrev: editedLetter({
    blocks: [paragraph({ id: 1, content: [{ ...literal({ id: 11, text: "Ny tekst her" }), parentId: 1 }] })],
  }),
});

const diff = {
  type: "UNIFIED",
  deletedBlocks: {},
  editedBlocks: {
    0: {
      deletedContent: {},
      contentEdits: {
        0: {
          type: "TEXT",
          edit: {
            inserts: [{ startOffset: 0, endOffset: 3 }],
            deletes: [{ startOffset: 0, endOffset: 7, text: "gammel " }],
          },
        },
      },
    },
  },
};

/** Markerer fra tegnet `from.offset` i tekstnoden `from.text` til `to.offset` i `to.text`. */
async function select(page: Page, from: { text: string; offset: number }, to: { text: string; offset: number }) {
  await page.evaluate(
    ({ from, to }) => {
      const find = (text: string) => {
        const walker = document.createTreeWalker(document.body, NodeFilter.SHOW_TEXT);
        while (walker.nextNode()) if ((walker.currentNode as Text).data === text) return walker.currentNode;
        throw new Error(`Fant ikke tekstnoden "${text}"`);
      };
      document.getSelection()?.setBaseAndExtent(find(from.text), from.offset, find(to.text), to.offset);
    },
    { from, to },
  );
}

const selectedText = (page: Page) => page.evaluate(() => document.getSelection()?.toString());

test.describe("Endring av fonttype med diff-markering", () => {
  test.beforeEach(async ({ page }) => {
    await setupSakStubs(page);
    await page.route("**/bff/skribenten-backend/features/diff", (route) => route.fulfill({ json: { enabled: true } }));
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1/attestering?reserver=true", (route) =>
      route.request().method() === "GET" ? route.fulfill({ json: brev }) : route.fallback(),
    );
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1/attestering/redigertBrev?*", (route) =>
      route.request().method() === "PUT"
        ? route.fulfill({ json: { ...brev, redigertBrev: route.request().postDataJSON() } })
        : route.fallback(),
    );
    await page.route("**/bff/skribenten-backend/brev/1/diff", (route) => route.fulfill({ json: diff }));

    await page.goto("/saksnummer/123456/attester/1/redigering");
    await page.getByText("Marker tekst som er lagt til").click();
    await page.getByText("Vis slettet tekst").click();
    await expect(page.locator("[data-diff-deletion]")).toHaveText("gammel ");
  });

  test("markering over slettet tekst gjøres fet, diff slås av og markeringen beholdes", async ({ page }) => {
    await select(page, { text: "Ny ", offset: 0 }, { text: "tekst her", offset: 5 });

    await page.getByTestId("fonttype-bold-button").click();

    await expect(page.locator("[data-diff-deletion]")).toHaveCount(0);
    await expect(page.getByText("Ny tekst", { exact: true })).toHaveCSS("font-weight", "700");
    await expect(page.getByText(" her", { exact: true })).toHaveCSS("font-weight", "400");
    await expect.poll(() => selectedText(page)).toBe("Ny tekst");
  });

  test("start og slutt i slettet tekst flyttes til nærmeste gjeldende tekst", async ({ page }) => {
    await select(page, { text: "gammel ", offset: 2 }, { text: "tekst her", offset: 5 });

    await page.getByTestId("fonttype-bold-button").click();
    await expect(page.locator("[data-diff-deletion]")).toHaveCount(0);
    await expect(page.getByText("tekst", { exact: true })).toHaveCSS("font-weight", "700");
    await expect.poll(() => selectedText(page)).toBe("tekst");
  });
});
