import fs from "node:fs";

import AxeBuilder from "@axe-core/playwright";
import { expect, type Page, type TestInfo, test } from "@playwright/test";

import { type BrevResponse } from "~/types/brev";
import { type AnyBlock, ListType } from "~/types/brevbakerTypes";
import { setupSakStubs } from "~test/e2e/support/helpers";
import { cell, item, itemList, literal, paragraph, row, table } from "~test/support/letterEditorTestUtils";

const dialogName = "Vil du tilbakestille brevmalen?";
const editorFixture: BrevResponse = JSON.parse(
  fs.readFileSync(new URL("../fixtures/brevResponse.json", import.meta.url), "utf-8"),
);

async function openEditor(page: Page, blocks?: AnyBlock[]) {
  const response = structuredClone(editorFixture);
  if (blocks) {
    response.redigertBrev.blocks = blocks;
  }
  await page.route("**/bff/skribenten-backend/sak/123456/brev/1?reserver=true", (route) =>
    route.fulfill({ json: response }),
  );

  await page.goto("/saksnummer/123456/brev/1");
  await expect(page.locator(".editor .letter-title")).toBeVisible();
  await expect(page.getByLabel("Land", { exact: true })).toBeVisible();
  await expect(page.getByText("Lagret", { exact: true })).toBeVisible();
  await expect(page.getByRole("button", { name: "Tilbakestill mal", exact: true })).toBeEnabled();
}

function listBlocks(listType: ListType): AnyBlock[] {
  return [paragraph([itemList({ listType, items: [item(literal("Punkt 1")), item(literal("Punkt 2"))] })])];
}

function tableBlocks(): AnyBlock[] {
  return [
    paragraph([
      table(
        [cell(literal("Opplysning")), cell(literal("Verdi"))],
        [row(cell(literal("Land")), cell(literal("Spania"))), row(cell(literal("Svartid")), cell(literal("10 uker")))],
      ),
    ]),
  ];
}

async function openTableEditor(page: Page) {
  await openEditor(page, tableBlocks());
  const letterTable = page.getByTestId("letter-table");
  await expect(letterTable).toBeVisible();
  await expect(letterTable.getByRole("columnheader")).toHaveText(["Opplysning", "Verdi"]);
  await expect(letterTable.locator("tbody tr")).toHaveCount(2);
  await expect(letterTable.getByRole("cell")).toHaveText(["Land", "Spania", "Svartid", "10 uker"]);
  await expect(page.getByTestId("table-cell-0-0").locator("span[contenteditable=true]")).toBeEditable();
}

async function expectNoAxeViolations(page: Page, testInfo: TestInfo) {
  await page.evaluate(async () => {
    await document.fonts.ready;
    await Promise.all(document.getAnimations().map((animation) => animation.finished.catch(() => {})));
  });

  const results = await new AxeBuilder({ page }).withTags(["wcag2a", "wcag2aa", "wcag21aa"]).analyze();

  await testInfo.attach("axe-results", {
    body: JSON.stringify(results, null, 2),
    contentType: "application/json",
  });

  const details = results.violations
    .map((violation) =>
      [
        `${violation.id} (${violation.impact ?? "unknown impact"}) - ${violation.help}`,
        violation.description,
        `Tags: ${violation.tags.join(", ")}`,
        `Help: ${violation.helpUrl}`,
        ...violation.nodes.map((node) =>
          [
            `Target: ${JSON.stringify(node.target)}`,
            `HTML: ${node.html}`,
            node.failureSummary ?? "No failure summary available",
          ].join("\n"),
        ),
      ].join("\n"),
    )
    .join("\n\n");

  await testInfo.attach("accessibility-details", {
    body: details || "No WCAG 2.1 A/AA violations found.",
    contentType: "text/plain",
  });

  const affectedElements = results.violations.reduce((total, violation) => total + violation.nodes.length, 0);
  const summary = [
    `WCAG 2.1 A/AA: ${results.violations.length} violated rules, ${affectedElements} affected elements`,
    ...results.violations.map((violation) => {
      const criteria = violation.tags
        .filter((tag) => /^wcag\d{3,}$/.test(tag))
        .map((tag) => tag.replace(/^wcag(\d)(\d)(\d+)$/, "WCAG $1.$2.$3"))
        .join(", ");

      return [
        `[${violation.impact ?? "unknown"}] ${violation.id} (${criteria})`,
        `${violation.help} - ${violation.nodes.length} affected elements`,
      ].join("\n");
    }),
    "See accessibility-details and axe-results attachments for full details.",
  ].join("\n\n");

  expect(results.violations.length, summary).toBe(0);
}

test.describe("Letter editor accessibility", () => {
  test.beforeEach(async ({ page }) => {
    await setupSakStubs(page);
    await page.route("**/bff/skribenten-backend/brevmal/*/modelSpecification", (route) =>
      route.fulfill({ path: "test/e2e/fixtures/modelSpecification.json", contentType: "application/json" }),
    );
  });

  test.describe("Page", () => {
    test("loaded editor page", async ({ page }, testInfo) => {
      await openEditor(page);
      await expect(page.getByText("Saksbehandlingstiden vår er vanligvis 10 uker.")).toBeVisible();
      await expectNoAxeViolations(page, testInfo);
    });
  });

  test.describe("Lists", () => {
    test("bulleted list", async ({ page }, testInfo) => {
      await openEditor(page, listBlocks(ListType.PUNKTLISTE));
      const list = page.locator(".editor ul");
      await expect(list).toBeVisible();
      await expect(list.getByRole("listitem")).toHaveText(["Punkt 1", "Punkt 2"]);
      await expect(page.locator(".editor ol")).toHaveCount(0);
      await expectNoAxeViolations(page, testInfo);
    });

    test("numbered list", async ({ page }, testInfo) => {
      await openEditor(page, listBlocks(ListType.NUMMERERT_LISTE));
      const list = page.locator(".editor ol");
      await expect(list).toBeVisible();
      await expect(list.getByRole("listitem")).toHaveText(["Punkt 1", "Punkt 2"]);
      await expect(page.locator(".editor ul")).toHaveCount(0);
      await expectNoAxeViolations(page, testInfo);
    });
  });

  test.describe("Tables", () => {
    test("table with headers and editable cells", async ({ page }, testInfo) => {
      await openTableEditor(page);
      await expectNoAxeViolations(page, testInfo);
    });

    test("insert table dialog", async ({ page }, testInfo) => {
      await openEditor(page);
      await page.getByRole("button", { name: "Sett inn tabell", exact: true }).click();
      const dialog = page.getByTestId("insert-table-modal");
      await expect(dialog).toBeVisible();
      await expect(dialog.getByTestId("input-cols")).toBeVisible();
      await expect(dialog.getByTestId("input-rows")).toBeVisible();
      await expect(dialog.getByTestId("insert-table-confirm-btn")).toBeEnabled();
      await expectNoAxeViolations(page, testInfo);
    });

    test("open table context menu", async ({ page }, testInfo) => {
      await openTableEditor(page);
      await page.getByTestId("table-cell-0-0").click({ button: "right" });
      await expect(page.getByRole("menu")).toBeVisible();
      await expect(page.getByRole("menuitem", { name: "Sett inn kolonne til høyre", exact: true })).toBeVisible();
      await expectNoAxeViolations(page, testInfo);
    });
  });

  test.describe("Reset letter", () => {
    test("reset letter dialog", async ({ page }, testInfo) => {
      await openEditor(page);
      await page.getByRole("button", { name: "Tilbakestill mal", exact: true }).click();
      const dialog = page.getByRole("dialog", { name: dialogName });
      await expect(dialog).toBeVisible();
      await expect(dialog.getByRole("button", { name: "Nei, behold brevet" })).toBeVisible();
      await expectNoAxeViolations(page, testInfo);
    });

    test("reset dialog opens and closes with focus restored to its trigger", async ({ page }) => {
      await openEditor(page);
      const resetButton = page.getByRole("button", { name: "Tilbakestill mal", exact: true });
      for (let tabCount = 0; tabCount < 50; tabCount++) {
        await page.keyboard.press("Tab");
        if (await resetButton.evaluate((button) => button === document.activeElement)) {
          break;
        }
      }
      await expect(resetButton, "Reset control should be reachable within 50 Tab presses").toBeFocused();
      await page.keyboard.press("Enter");

      const dialog = page.getByRole("dialog", { name: dialogName });
      await expect(dialog).toBeVisible();
      await expect.poll(() => dialog.evaluate((element) => element.contains(document.activeElement))).toBe(true);
      await page.keyboard.press("Tab");
      await expect.poll(() => dialog.evaluate((element) => element.contains(document.activeElement))).toBe(true);
      await page.keyboard.press("Escape");

      await expect(dialog).not.toBeVisible();
      await expect(resetButton).toBeFocused();
    });
  });
});
