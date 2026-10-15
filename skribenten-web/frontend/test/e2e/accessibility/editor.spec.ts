import fs from "node:fs";

import AxeBuilder from "@axe-core/playwright";
import { expect, type Page, type TestInfo, test } from "@playwright/test";

import { type BrevResponse } from "~/types/brev";
import { type AnyBlock, ElementTags, ListType } from "~/types/brevbakerTypes";
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
  await page.route("**/bff/skribenten-backend/brev/1/redigertBrev?frigiReservasjon=*", (route) => {
    response.redigertBrev = route.request().postDataJSON();
    return route.fulfill({ json: response });
  });

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

  const results = await new AxeBuilder({ page }).withTags(["wcag2a", "wcag2aa", "wcag21a", "wcag21aa"]).analyze();

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
    for (const activationKey of ["Enter", "Space"]) {
      for (const targetDocument of ["brev", "vedlegg"]) {
        test(`switching documents with ${activationKey} keeps tab focus and preserves ${targetDocument} fritekst selection`, async ({
          page,
        }) => {
          const placeholder = "Fyll inn fritekst i vedlegget";
          const letterPlaceholder = "Fyll inn fritekst i brevet";
          const vedleggId = "redigerbart-vedlegg";
          const vedlegg = {
            includeSakspart: false,
            title: { text: [literal("Redigerbart vedlegg")], deletedContent: [] },
            blocks: [paragraph([literal({ text: placeholder, tags: [ElementTags.FRITEKST] })])],
            deletedBlocks: [],
          };
          const vedleggUrl = "**/bff/skribenten-backend/sak/123456/brev/1/redigerbareVedlegg";
          await page.route(vedleggUrl, (route) =>
            route.fulfill({ json: [{ vedleggId, tittel: "Redigerbart vedlegg" }] }),
          );
          await page.route(`${vedleggUrl}/${vedleggId}`, (route) => route.fulfill({ json: vedlegg }));
          await openEditor(page, [paragraph([literal({ text: letterPlaceholder, tags: [ElementTags.FRITEKST] })])]);
          await expect(page.getByText(letterPlaceholder, { exact: true })).toBeFocused();

          const brevmalTab = page.getByRole("tab", { name: "Brevmal", exact: true });
          const vedleggTab = page.getByRole("tab", { name: "Vedlegg", exact: true });
          for (let count = 0; count < 50; count++) {
            await page.keyboard.press("Tab");
            if (await brevmalTab.evaluate((tab) => tab === document.activeElement)) break;
          }
          await expect(brevmalTab).toBeFocused();
          await page.keyboard.press("ArrowRight");
          await expect(vedleggTab).toBeFocused();
          await page.keyboard.press(activationKey);
          await expect(page.getByText(placeholder, { exact: true })).toBeVisible();
          await expect(vedleggTab).toHaveAttribute("aria-selected", "true");
          await expect(vedleggTab).toBeFocused();
          await page.keyboard.press("ArrowLeft");
          await page.keyboard.press(activationKey);
          await expect(page.getByText(letterPlaceholder, { exact: true })).toBeVisible();
          await expect(brevmalTab).toHaveAttribute("aria-selected", "true");
          await expect(brevmalTab).toBeFocused();

          if (targetDocument === "vedlegg") {
            await page.keyboard.press("ArrowRight");
            await page.keyboard.press(activationKey);
            await expect(page.getByText(placeholder, { exact: true })).toBeVisible();
            await expect(vedleggTab).toBeFocused();
          }
          const targetPlaceholder = targetDocument === "vedlegg" ? placeholder : letterPlaceholder;
          const field = page.getByText(targetPlaceholder, { exact: true });
          for (let count = 0; count < 50; count++) {
            await page.keyboard.press("Tab");
            if (await field.evaluate((field) => field === document.activeElement)) break;
          }
          await expect(field).toBeFocused();
          await expect.poll(() => page.evaluate(() => globalThis.getSelection()?.toString())).toBe(targetPlaceholder);
          await expect(page.locator(".editor")).toHaveCSS("outline-style", "solid");
          await page.keyboard.type("Utfylt vedlegg");
          await expect(page.locator(".editor")).toContainText("Utfylt vedlegg");
          await expect(page.locator(".editor")).not.toContainText(targetPlaceholder);
        });
      }
    }

    test("loaded editor page", async ({ page }, testInfo) => {
      await openEditor(page);
      await expect(page.getByText("Saksbehandlingstiden vår er vanligvis 10 uker.")).toBeVisible();
      await expectNoAxeViolations(page, testInfo);
    });

    const placeholder = "Fyll inn fritekst";
    for (const scenario of [
      {
        name: "the first body text when there is no fritekst",
        blocks: listBlocks(ListType.PUNKTLISTE),
        expectBody: true,
      },
      {
        name: "the first body text when fritekst has been filled in",
        blocks: [paragraph([literal({ text: placeholder, editedText: "Utfylt tekst", tags: [ElementTags.FRITEKST] })])],
        expectBody: true,
      },
      {
        name: "fritekst in a list before the title",
        blocks: [
          paragraph([itemList({ items: [item(literal({ text: placeholder, tags: [ElementTags.FRITEKST] }))] })]),
        ],
        expectBody: false,
      },
      {
        name: "fritekst in a table before the title",
        blocks: [
          paragraph([
            table(
              [cell(literal("Opplysning"))],
              [row(cell(literal({ text: placeholder, tags: [ElementTags.FRITEKST] })))],
            ),
          ]),
        ],
        expectBody: false,
      },
    ]) {
      test(`Tab enters ${scenario.name} after the toolbar`, async ({ page }) => {
        await openEditor(page, scenario.blocks);
        const resetButton = page.getByRole("button", { name: "Tilbakestill mal", exact: true });
        for (let tabCount = 0; tabCount < 50; tabCount++) {
          await page.keyboard.press("Tab");
          if (await resetButton.evaluate((button) => button === document.activeElement)) break;
        }
        await expect(resetButton, "Toolbar should be reachable within 50 Tab presses").toBeFocused();
        const editor = page.locator(".editor");
        await expect(editor).toHaveCSS("outline-style", "none");
        await page.keyboard.press("Tab");
        const entry = scenario.expectBody
          ? page.locator(".editor-surface span[contenteditable=true][tabindex='0']").first()
          : page.getByText(placeholder, { exact: true });
        await expect(entry).toBeFocused();
        await expect(editor).toHaveCSS("outline-style", "solid");
        await expect(editor).toHaveCSS("outline-width", "2px");
        await expect(editor).toHaveCSS("outline-offset", "-2px");
        await expect(page.locator(".editor .letter-title span[contenteditable=true]").first()).toHaveAttribute(
          "tabindex",
          "-1",
        );
        if (scenario.expectBody) {
          await expect
            .poll(() =>
              entry.evaluate((element) => {
                const selection = globalThis.getSelection();
                return selection?.isCollapsed && element.contains(selection.anchorNode) ? selection.anchorOffset : -1;
              }),
            )
            .toBe(0);
          await expect(page.getByTestId("toolbar-table-button")).toBeEnabled();
          await expect(page.getByTestId("typography-select")).toBeEnabled();
          const originalText = await entry.textContent();
          await page.keyboard.press("End");
          await page.keyboard.type("!");
          await expect(entry).toHaveText(`${originalText}!`);
          await expect(entry).toBeFocused();
          await expect(editor).toHaveCSS("outline-style", "solid");
          await page.keyboard.press("Backspace");
          await expect(entry).toHaveText(originalText ?? "");
          await entry.click();
          await expect(editor).toHaveCSS("outline-style", "none");
          await page.keyboard.press("Shift+Tab");
          await expect(resetButton).toBeFocused();
          await page.keyboard.press("Tab");
          await expect(entry).toBeFocused();
          await expect(editor).toHaveCSS("outline-style", "solid");
        } else {
          await expect.poll(() => page.evaluate(() => globalThis.getSelection()?.toString())).toBe(placeholder);
        }
      });
    }

    for (const tool of ["table", "bullet-list", "number-list"]) {
      for (const documentType of ["letter", "attachment"]) {
        test(`inserting ${tool} in a new ${documentType} paragraph using only the keyboard`, async ({ page }) => {
          if (documentType === "attachment") {
            const vedleggUrl = "**/bff/skribenten-backend/sak/123456/brev/1/redigerbareVedlegg";
            await page.route(vedleggUrl, (route) =>
              route.fulfill({ json: [{ vedleggId: "vedlegg", tittel: "Redigerbart vedlegg" }] }),
            );
            await page.route(`${vedleggUrl}/vedlegg`, (route) =>
              route.fulfill({
                json: {
                  includeSakspart: false,
                  title: { text: [literal("Redigerbart vedlegg")], deletedContent: [] },
                  blocks: [paragraph([literal("Første avsnitt")])],
                  deletedBlocks: [],
                },
              }),
            );
          }
          await openEditor(page, [paragraph([literal("Første avsnitt")])]);
          if (documentType === "attachment") {
            const brevmalTab = page.getByRole("tab", { name: "Brevmal", exact: true });
            for (let count = 0; count < 50; count++) {
              await page.keyboard.press("Tab");
              if (await brevmalTab.evaluate((tab) => tab === document.activeElement)) break;
            }
            await expect(brevmalTab).toBeFocused();
            await page.keyboard.press("ArrowRight");
            await page.keyboard.press("Enter");
            await expect(page.locator(".editor .letter-title")).toHaveText("Redigerbart vedlegg");
          }
          const lastToolbarControl =
            documentType === "letter"
              ? page.getByRole("button", { name: "Tilbakestill mal", exact: true })
              : page.getByTestId("typography-select");
          for (let count = 0; count < 50; count++) {
            await page.keyboard.press("Tab");
            if (await lastToolbarControl.evaluate((control) => control === document.activeElement)) break;
          }
          await expect(lastToolbarControl).toBeFocused();
          await page.keyboard.press("Tab");
          await expect(page.getByText("Første avsnitt", { exact: true })).toBeFocused();
          await page.keyboard.press("End");
          await page.keyboard.press("Enter");
          const blankParagraph = page.locator(".editor-surface > div").nth(1);
          await expect(blankParagraph.locator("[contenteditable=true]")).toBeFocused();
          await page.keyboard.press("Space");
          await expect(blankParagraph.locator("[contenteditable=true]")).toBeFocused();
          const toolButton = page.getByTestId(tool === "table" ? "toolbar-table-button" : `${tool}-button`);
          await expect(toolButton).toBeEnabled();
          await page.keyboard.press("Shift+Tab");
          await expect(lastToolbarControl).toBeFocused();
          for (let count = 0; count < 10; count++) {
            await page.keyboard.press("Shift+Tab");
            if (await toolButton.evaluate((button) => button === document.activeElement)) break;
          }
          await expect(toolButton).toBeFocused();
          await page.keyboard.press("Enter");
          if (tool !== "table") {
            await expect(blankParagraph.locator(tool === "bullet-list" ? "ul" : "ol")).toBeVisible();
            await page.keyboard.type("Nytt punkt");
            await expect(blankParagraph).toContainText("Nytt punkt");
            await expect(page.locator(".editor-surface > div").first()).toHaveText("Første avsnitt");
            return;
          }
          const dialog = page.getByTestId("insert-table-modal");
          await expect(dialog).toBeVisible();
          const insertButton = dialog.getByTestId("insert-table-confirm-btn");
          for (let count = 0; count < 10; count++) {
            await page.keyboard.press("Tab");
            if (await insertButton.evaluate((button) => button === document.activeElement)) break;
          }
          await expect(insertButton).toBeFocused();
          await page.keyboard.press("Enter");
          await expect(dialog).toBeHidden();
          await expect(page.getByTestId("letter-table")).toBeVisible();
          await expect(blankParagraph.getByTestId("letter-table")).toBeVisible();
          await expect(blankParagraph.locator("span[contenteditable=true]:focus")).toHaveCount(1);
          await page.keyboard.type("Tabelltekst");
          await expect(blankParagraph.getByTestId("letter-table")).toContainText("Tabelltekst");
          await expect(page.locator(".editor")).toContainText("Første avsnitt");
        });
      }
    }
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
    for (const cancelWith of ["Escape", "Avbryt"]) {
      test(`cancelling table insertion with ${cancelWith} returns focus to the toolbar`, async ({ page }) => {
        await openEditor(page);
        const tableButton = page.getByTestId("toolbar-table-button");
        await tableButton.click();
        const dialog = page.getByTestId("insert-table-modal");
        await expect(dialog).toBeVisible();
        if (cancelWith === "Escape") {
          await page.keyboard.press("Escape");
        } else {
          await dialog.getByRole("button", { name: "Avbryt", exact: true }).click();
        }
        await expect(dialog).toBeHidden();
        await expect(tableButton).toBeFocused();
        await expect(page.getByTestId("letter-table")).toHaveCount(0);
      });
    }

    test("table with headers and editable cells", async ({ page }, testInfo) => {
      await openTableEditor(page);
      await expectNoAxeViolations(page, testInfo);
    });

    test("Tab between cells and in the last cell does not show the editor focus ring", async ({ page }) => {
      await openTableEditor(page);
      const editor = page.locator(".editor");
      const cellText = (rowIndex: number, cellIndex: number) =>
        page.getByTestId(`table-cell-${rowIndex}-${cellIndex}`).locator("span[contenteditable=true]");

      await cellText(0, 0).click();
      await page.keyboard.press("Tab");
      await expect(cellText(0, 1)).toBeFocused();
      await expect(editor).toHaveCSS("outline-style", "none");

      await cellText(1, 1).click();
      await page.keyboard.press("Tab");
      await expect(page.getByTestId("letter-table").locator("tbody tr")).toHaveCount(3);
      await expect(editor).toHaveCSS("outline-style", "none");
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
      const menu = page.getByRole("menu");
      await expect(menu).toBeVisible();
      const trigger = page.getByRole("button", { name: "Tabellmeny", exact: true });
      await expect(trigger).not.toHaveAttribute("aria-hidden", "true");
      await expect(trigger).toHaveAttribute("tabindex", "-1");
      await expect(page.getByRole("menuitem", { name: "Sett inn kolonne til høyre", exact: true })).toBeVisible();
      await expectNoAxeViolations(page, testInfo);
      await page.keyboard.press("ArrowDown");
      await expect(menu.locator('[role="menuitem"]:focus')).toHaveCount(1);
      await page.keyboard.press("Escape");
      await expect(menu).not.toBeVisible();
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
