import { expect, type Page, test } from "@playwright/test";

import { newCell, newLiteral, newParagraph, newTable } from "~/Brevredigering/LetterEditor/actions/common";

import { brevResponse, editedLetter } from "../../utils/letterEditorTestUtils";
import { setupSakStubs } from "../utils/helpers";

function tableRow(...texts: string[]) {
  return {
    id: null,
    parentId: null,
    deletedCells: [],
    cells: texts.map((text) => newCell([newLiteral({ editedText: text, text })])),
  };
}

async function setupEditor(page: Page, blocks: ReturnType<typeof newParagraph>[]) {
  await setupSakStubs(page);

  await page.route("**/bff/skribenten-backend/sak/123456/brev/1?reserver=true", (route) =>
    route.fulfill({
      json: brevResponse({
        redigertBrev: editedLetter({ blocks }),
      }),
    }),
  );
  await page.route("**/bff/skribenten-backend/brev/1/reservasjon", (route) =>
    route.fulfill({ path: "test/e2e/fixtures/brevreservasjon.json", contentType: "application/json" }),
  );
  await page.route("**/bff/skribenten-backend/brevmal/*/modelSpecification", (route) =>
    route.fulfill({ path: "test/e2e/fixtures/modelSpecification.json", contentType: "application/json" }),
  );
  await page.route("**/bff/skribenten-backend/brev/1/redigertBrev?frigiReservasjon=*", async (route) => {
    if (route.request().method() === "PUT") {
      await route.fulfill({ status: 200, json: { ok: true } });
    } else {
      await route.fallback();
    }
  });

  await page.goto("/saksnummer/123456/brev/1");
  await expect(page.getByTestId("letter-table").first()).toBeVisible();
  await page.evaluate(() => document.fonts.ready);
}

function headerCell(page: Page, columnIndex: number) {
  return page.getByTestId(`table-header-${columnIndex}`).locator("span[contenteditable=true]").first();
}

function bodyCell(page: Page, rowIndex: number, cellIndex: number) {
  return page.getByTestId(`table-cell-${rowIndex}-${cellIndex}`).locator("span[contenteditable=true]").first();
}

async function assertFocused(_page: Page, locator: ReturnType<typeof bodyCell>) {
  await expect(locator).toBeFocused();
}

test.describe("Table ArrowDown navigation", () => {
  test("moves focus from header to first body row", async ({ page }) => {
    await setupEditor(page, [
      newParagraph({ content: [newTable([tableRow("R0C0", "R0C1"), tableRow("R1C0", "R1C1")])] }),
    ]);

    await headerCell(page, 0).click();
    await assertFocused(page, headerCell(page, 0));
    await page.keyboard.press("ArrowDown");
    await assertFocused(page, bodyCell(page, 0, 0));
  });

  test("moves focus down through body rows", async ({ page }) => {
    await setupEditor(page, [
      newParagraph({ content: [newTable([tableRow("R0C0"), tableRow("R1C0"), tableRow("R2C0")])] }),
    ]);

    await bodyCell(page, 0, 0).click();
    await assertFocused(page, bodyCell(page, 0, 0));
    await page.keyboard.press("ArrowDown");
    await assertFocused(page, bodyCell(page, 1, 0));
    await page.keyboard.press("ArrowDown");
    await assertFocused(page, bodyCell(page, 2, 0));
  });

  test("preserves cellIndex when moving down between rows", async ({ page }) => {
    await setupEditor(page, [
      newParagraph({ content: [newTable([tableRow("R0C0", "R0C1"), tableRow("R1C0", "R1C1")])] }),
    ]);

    await headerCell(page, 1).click();
    await assertFocused(page, headerCell(page, 1));
    await page.keyboard.press("ArrowDown");
    await assertFocused(page, bodyCell(page, 0, 1));
  });

  test("exits table forward into the next literal when pressing ArrowDown on the last row", async ({ page }) => {
    await setupEditor(page, [
      newParagraph({
        content: [newTable([tableRow("R0C0"), tableRow("R1C0")]), newLiteral({ editedText: "After table" })],
      }),
    ]);

    await bodyCell(page, 1, 0).click();
    await page.keyboard.press("ArrowDown");
    await expect(page.locator(":focus")).toHaveText("After table");
  });

  test("enters table header when pressing ArrowDown on a literal directly preceding the table", async ({ page }) => {
    await setupEditor(page, [
      newParagraph({
        content: [newLiteral({ editedText: "Before table" }), newTable([tableRow("R0C0"), tableRow("R1C0")])],
      }),
    ]);

    await page.getByText("Before table").click();
    await expect(page.locator(":focus")).toHaveText("Before table");
    await page.keyboard.press("ArrowDown");
    await assertFocused(page, headerCell(page, 0));
  });

  test("enters next block's table header when pressing ArrowDown on the last row of a table", async ({ page }) => {
    await setupEditor(page, [
      newParagraph({ content: [newTable([tableRow("T1R0"), tableRow("T1R1")])] }),
      newParagraph({ content: [newTable([tableRow("T2R0"), tableRow("T2R1")])] }),
    ]);

    // Focus on last row of first table
    await page.getByText("T1R1").click();
    await page.keyboard.press("ArrowDown");
    // Should enter second table's header — verify by checking focused text contains column spec
    const secondTable = page.getByTestId("letter-table").nth(1);
    await expect(secondTable.locator("th span[contenteditable=true]").first()).toBeFocused();
  });
});

test.describe("Table ArrowUp navigation", () => {
  test("moves focus from first body row to header", async ({ page }) => {
    await setupEditor(page, [
      newParagraph({ content: [newTable([tableRow("R0C0", "R0C1"), tableRow("R1C0", "R1C1")])] }),
    ]);

    await bodyCell(page, 0, 0).click();
    await assertFocused(page, bodyCell(page, 0, 0));
    await page.keyboard.press("ArrowUp");
    await assertFocused(page, headerCell(page, 0));
  });

  test("moves focus up through body rows", async ({ page }) => {
    await setupEditor(page, [
      newParagraph({ content: [newTable([tableRow("R0C0"), tableRow("R1C0"), tableRow("R2C0")])] }),
    ]);

    await bodyCell(page, 2, 0).click();
    await assertFocused(page, bodyCell(page, 2, 0));
    await page.keyboard.press("ArrowUp");
    await assertFocused(page, bodyCell(page, 1, 0));
    await page.keyboard.press("ArrowUp");
    await assertFocused(page, bodyCell(page, 0, 0));
  });

  test("preserves cellIndex when moving up between rows", async ({ page }) => {
    await setupEditor(page, [
      newParagraph({ content: [newTable([tableRow("R0C0", "R0C1"), tableRow("R1C0", "R1C1")])] }),
    ]);

    await bodyCell(page, 1, 1).click();
    await assertFocused(page, bodyCell(page, 1, 1));
    await page.keyboard.press("ArrowUp");
    await assertFocused(page, bodyCell(page, 0, 1));
    await page.keyboard.press("ArrowUp");
    await assertFocused(page, headerCell(page, 1));
  });

  test("exits table backward into the preceding literal when pressing ArrowUp on the header", async ({ page }) => {
    await setupEditor(page, [
      newParagraph({
        content: [newLiteral({ editedText: "Before table" }), newTable([tableRow("R0C0"), tableRow("R1C0")])],
      }),
    ]);

    await headerCell(page, 0).click();
    await page.keyboard.press("ArrowUp");
    await expect(page.locator(":focus")).toHaveText("Before table");
  });

  test("enters table last row when pressing ArrowUp on a literal directly following the table", async ({ page }) => {
    await setupEditor(page, [
      newParagraph({
        content: [newTable([tableRow("R0C0"), tableRow("R1C0")]), newLiteral({ editedText: "After table" })],
      }),
    ]);

    await page.getByText("After table").click();
    await expect(page.locator(":focus")).toHaveText("After table");
    await page.keyboard.press("ArrowUp");
    await assertFocused(page, bodyCell(page, 1, 0));
  });

  test("enters previous block's last row when pressing ArrowUp on the header of a table", async ({ page }) => {
    await setupEditor(page, [
      newParagraph({ content: [newTable([tableRow("T1R0"), tableRow("T1R1")])] }),
      newParagraph({ content: [newTable([tableRow("T2R0"), tableRow("T2R1")])] }),
    ]);

    // Focus on second table's first body row then navigate up to its header
    await page.getByText("T2R0").click();
    await page.keyboard.press("ArrowUp");
    // Now at second table's header
    const secondTable = page.getByTestId("letter-table").nth(1);
    await expect(secondTable.locator("th span[contenteditable=true]").first()).toBeFocused();
    // Press ArrowUp again — should enter first table's last row
    await page.keyboard.press("ArrowUp");
    await expect(page.locator(":focus")).toContainText("T1R1");
  });
});
