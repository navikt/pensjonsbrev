import { expect, type Page, test } from "@playwright/test";

import { newCell, newLiteral, newParagraph, newTable, newVariable } from "~/Brevredigering/LetterEditor/actions/common";
import { setupSakStubs } from "~test/e2e/support/helpers";
import { brevResponse, editedLetter } from "~test/support/brevFixtures";

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

function cellLocator(page: Page, rowIndex: number, cellIndex: number) {
  return page.getByTestId(`table-cell-${rowIndex}-${cellIndex}`);
}

async function expectSameVisualLine(...boxes: { y: number }[]) {
  const firstY = boxes[0].y;
  for (const box of boxes) {
    expect(Math.abs(box.y - firstY)).toBeLessThan(2);
  }
}

test.describe("Table cell layout", () => {
  test("literal, variable and literal flow on the same line inside a cell", async ({ page }) => {
    await setupEditor(page, [
      newParagraph({
        content: [
          newTable([
            {
              id: null,
              parentId: null,
              deletedCells: [],
              cells: [
                newCell([
                  newLiteral({ editedText: "Tekst: " }),
                  newVariable({ text: "en testverdi" }),
                  newLiteral({ editedText: " etter." }),
                ]),
              ],
            },
          ]),
        ],
      }),
    ]);

    const cell = cellLocator(page, 0, 0);
    const literalBefore = cell.locator("span[contenteditable=true]").nth(0);
    const variable = cell.locator("span:not([contenteditable])").first();
    const literalAfter = cell.locator("span[contenteditable=true]").nth(1);

    const cellBox = await cell.boundingBox();
    const beforeBox = await literalBefore.boundingBox();
    const variableBox = await variable.boundingBox();
    const afterBox = await literalAfter.boundingBox();
    expect(cellBox && beforeBox && variableBox && afterBox).toBeTruthy();

    // Alt skal ligge på samme visuelle linje
    await expectSameVisualLine(beforeBox!, variableBox!, afterBox!);

    // Variabel-boksen skal ligge mellom tekstene, ikke utenfor cellen
    expect(variableBox!.x).toBeGreaterThanOrEqual(beforeBox!.x + beforeBox!.width - 1);
    expect(variableBox!.x + variableBox!.width).toBeLessThanOrEqual(cellBox!.x + cellBox!.width);
  });

  test("a long unbroken variable is wrapped inside the cell instead of overflowing", async ({ page }) => {
    const longValue = "VeldigLangVariabelverdiUtenMellomrom".repeat(3);
    await setupEditor(page, [
      newParagraph({
        content: [
          newTable([
            {
              id: null,
              parentId: null,
              deletedCells: [],
              cells: [
                newCell([newLiteral({ editedText: "Verdi: " }), newVariable({ text: longValue })]),
                newCell([newLiteral({ editedText: "Kort" })]),
              ],
            },
          ]),
        ],
      }),
    ]);

    const cell = cellLocator(page, 0, 0);
    const variable = cell.locator("span:not([contenteditable])").first();

    const cellBox = await cell.boundingBox();
    const variableBox = await variable.boundingBox();
    expect(cellBox && variableBox).toBeTruthy();

    // Boksen skal ikke overstige cellens bredde, og skal brytes over flere linjer
    expect(variableBox!.x + variableBox!.width).toBeLessThanOrEqual(cellBox!.x + cellBox!.width + 1);
    expect(variableBox!.height).toBeGreaterThan(24);
  });

  test("an empty literal in a cell stays clickable and focusable", async ({ page }) => {
    await setupEditor(page, [
      newParagraph({
        content: [
          newTable([
            {
              id: null,
              parentId: null,
              deletedCells: [],
              cells: [newCell(), newCell([newLiteral({ editedText: "Tekst" })])],
            },
          ]),
        ],
      }),
    ]);

    const emptySpan = cellLocator(page, 0, 0).locator("span[contenteditable=true]").first();
    await expect(emptySpan).toHaveAttribute("data-empty", "");

    // Skal ha et klikkbart område selv om den ikke inneholder synlig tekst
    const box = await emptySpan.boundingBox();
    expect(box).toBeTruthy();
    expect(box!.width).toBeGreaterThan(3);
    expect(box!.height).toBeGreaterThan(10);

    await emptySpan.click();
    await expect(emptySpan).toBeFocused();
    await page.keyboard.type("Ny tekst");
    await expect(emptySpan).toHaveText("Ny tekst");
  });
});
