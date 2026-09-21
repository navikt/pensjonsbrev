import { expect, type Page, test } from "@playwright/test";

import {
  createNewLine,
  newCell,
  newLiteral,
  newParagraph,
  newTable,
  newVariable,
} from "~/Brevredigering/LetterEditor/actions/common";
import { type Row } from "~/types/brevbakerTypes";
import { setupSakStubs } from "~test/e2e/support/helpers";
import { brevResponse, editedLetter } from "~test/support/brevFixtures";

function tableRow(...texts: string[]): Row {
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

async function visualLines(locator: ReturnType<typeof bodyCell>) {
  return locator.evaluate((element) => {
    const node = element.firstChild!;
    const lines: { offset: number; top: number }[] = [];
    for (let offset = 0; offset < node.textContent!.length; offset++) {
      const range = document.createRange();
      range.setStart(node, offset);
      range.setEnd(node, offset + 1);
      const { top } = range.getBoundingClientRect();
      if (lines.at(-1)?.top !== top) lines.push({ offset, top });
    }
    return lines;
  });
}

async function placeCaret(locator: ReturnType<typeof bodyCell>, offset: number) {
  await locator.focus();
  await locator.evaluate((element, position) => {
    const range = document.createRange();
    range.setStart(element.firstChild!, position);
    range.collapse(true);
    const selection = globalThis.getSelection()!;
    selection.removeAllRanges();
    selection.addRange(range);
  }, offset);
}

async function caretPosition(page: Page) {
  return page.evaluate(() => {
    const range = globalThis.getSelection()!.getRangeAt(0);
    const { x, top } = range.getBoundingClientRect();
    return { x, top, offset: range.startOffset };
  });
}

test.describe("Table fallback caret boundaries", () => {
  for (const boundary of ["before", "after"] as const) {
    test(`normalizes a parent position ${boundary} the target span`, async ({ page }) => {
      const destination = tableRow("short");
      destination.cells[0].text.unshift(newVariable({ text: "VARIABLE" }));
      await setupEditor(page, [
        newParagraph({ content: [newTable([tableRow("a much longer source cell"), destination])] }),
      ]);
      const target = bodyCell(page, 1, 0);
      await placeCaret(bodyCell(page, 0, 0), 20);
      await target.evaluate((element, edge) => {
        Object.defineProperty(document, "caretPositionFromPoint", { configurable: true, value: undefined });
        document.caretRangeFromPoint = () => {
          const range = document.createRange();
          if (edge === "before") range.setStartBefore(element);
          else range.setStartAfter(element);
          range.collapse(true);
          return range;
        };
      }, boundary);

      await page.keyboard.press("ArrowDown");

      await expect(target).toBeFocused();
      expect((await caretPosition(page)).offset).toBe(boundary === "before" ? 0 : 5);
      await page.keyboard.type("!");
      await expect(target).toHaveText(boundary === "before" ? "!short" : "short!");
      await expect(page.getByText("VARIABLE", { exact: true })).toHaveText("VARIABLE");
    });
  }

  test("rejects a caret position outside the target cell", async ({ page }) => {
    await setupEditor(page, [newParagraph({ content: [newTable([tableRow("long source text"), tableRow("short")])] })]);
    await placeCaret(bodyCell(page, 0, 0), 15);
    await page.evaluate(() => {
      Object.defineProperty(document, "caretPositionFromPoint", { configurable: true, value: undefined });
      document.caretRangeFromPoint = () => {
        const range = document.createRange();
        range.setStart(document.body, 0);
        range.collapse(true);
        return range;
      };
    });

    await page.keyboard.press("ArrowDown");

    await expect(bodyCell(page, 1, 0)).toBeFocused();
    expect((await caretPosition(page)).offset).toBe(0);
  });
});

test.describe("Table visual-line navigation", () => {
  for (const direction of ["up", "down"] as const) {
    for (const targetText of ["short", ""]) {
      test(`${direction} clamps the caret in a ${targetText ? "short" : "empty"} cell`, async ({ page }) => {
        const sourceText = "a much longer source cell";
        const rows =
          direction === "up"
            ? [tableRow(targetText), tableRow(sourceText)]
            : [tableRow(sourceText), tableRow(targetText)];
        await setupEditor(page, [newParagraph({ content: [newTable(rows)] })]);
        await placeCaret(bodyCell(page, direction === "up" ? 1 : 0, 0), 20);

        await page.keyboard.press(direction === "up" ? "ArrowUp" : "ArrowDown");

        const target = bodyCell(page, direction === "up" ? 0 : 1, 0);
        await expect(target).toBeFocused();
        expect((await caretPosition(page)).offset).toBe(targetText.length);
        await page.keyboard.type("!");
        await expect(target).toHaveText(`${targetText}!`);
      });
    }

    for (const acrossCells of [false, true]) {
      test(`${direction} navigates separate text spans ${acrossCells ? "across cells" : "inside a cell"}`, async ({
        page,
      }) => {
        const row = tableRow("");
        row.cells[0] = newCell([
          newLiteral({ editedText: "first editable line" }),
          createNewLine(),
          newLiteral({ editedText: "second editable line" }),
        ]);
        await setupEditor(page, [newParagraph({ content: [newTable([row, structuredClone(row)])] })]);
        const sourceRow = direction === "up" ? 1 : 0;
        const sourceSpan = direction === "up" ? (acrossCells ? 0 : 1) : acrossCells ? 1 : 0;
        const source = page
          .getByTestId(`table-cell-${sourceRow}-0`)
          .locator("span[contenteditable=true]")
          .nth(sourceSpan);
        await placeCaret(source, 4);
        const before = await caretPosition(page);

        await page.keyboard.press(direction === "up" ? "ArrowUp" : "ArrowDown");

        const targetRow = acrossCells ? 1 - sourceRow : sourceRow;
        const target = page
          .getByTestId(`table-cell-${targetRow}-0`)
          .locator("span[contenteditable=true]")
          .nth(1 - sourceSpan);
        await expect(target).toBeFocused();
        expect(Math.abs((await caretPosition(page)).x - before.x)).toBeLessThan(10);
        await page.keyboard.type("!");
        await expect(target).toContainText("!");
        await expect(source).not.toContainText("!");
      });

      test(`${direction} preserves horizontal position ${acrossCells ? "across cells" : "within a wrapped cell"}`, async ({
        page,
      }) => {
        const text = "alpha bravo charlie delta echo foxtrot golf hotel india juliet kilo lima";
        await setupEditor(page, [newParagraph({ content: [newTable([tableRow(text), tableRow(text)])] })]);
        await page.getByTestId("letter-table").evaluate((table) => {
          table.style.width = "260px";
        });

        const source = bodyCell(page, direction === "up" ? 1 : 0, 0);
        const lines = await visualLines(source);
        expect(lines.length).toBeGreaterThan(1);
        const sourceLine =
          direction === "up" ? (acrossCells ? lines[0] : lines.at(-1)!) : acrossCells ? lines.at(-1)! : lines[0];
        await placeCaret(source, sourceLine.offset + 4);
        const before = await caretPosition(page);

        await page.keyboard.press(direction === "up" ? "ArrowUp" : "ArrowDown");

        const target = acrossCells ? bodyCell(page, direction === "up" ? 0 : 1, 0) : source;
        await expect(target).toBeFocused();
        const targetLines = await visualLines(target);
        const expectedLine =
          direction === "up" ? targetLines.at(acrossCells ? -1 : -2)! : targetLines[acrossCells ? 0 : 1];
        const after = await caretPosition(page);
        expect(after.top).toBeCloseTo(expectedLine.top, 0);
        expect(Math.abs(after.x - before.x)).toBeLessThan(10);
        expect(after.offset).toBeGreaterThan(0);
      });
    }
  }
});

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
