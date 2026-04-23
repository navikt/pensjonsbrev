import { expect, type Page, test } from "@playwright/test";

import { newCell, newLiteral, newParagraph, newTable } from "~/Brevredigering/LetterEditor/actions/common";
import { AUTOSAVE_TIMER } from "~/components/ManagedLetterEditor/autosave_timer";

import { nyBrevResponse, nyRedigertBrev } from "../../utils/brevredigeringTestUtils";
import { dataE2E, setupSakStubs } from "../utils/helpers";

const undoShortcut = "Control+z";

test.describe.configure({ mode: "serial" });

// Helpers
const openInsertTableModal = async (page: Page) => {
  await dataE2E(page, "toolbar-table-btn").click();
  await expect(dataE2E(page, "insert-table-modal")).toBeVisible();
};

const insertTable = async (page: Page, cols: number, rows: number) => {
  await openInsertTableModal(page);
  await dataE2E(page, "input-cols").clear();
  await dataE2E(page, "input-cols").fill(String(cols));
  await dataE2E(page, "input-rows").clear();
  await dataE2E(page, "input-rows").fill(String(rows));
  await expect(dataE2E(page, "insert-table-confirm-btn")).toBeEnabled();
  await dataE2E(page, "insert-table-confirm-btn").click({ force: true });
  await expect(dataE2E(page, "insert-table-modal")).not.toBeVisible();
};

const rightClickCell = (page: Page, row: number, col: number) =>
  dataE2E(page, `table-cell-${row}-${col}`).click({ button: "right" });

const tableCellEditor = (page: Page, row: number, col: number) =>
  dataE2E(page, `table-cell-${row}-${col}`).locator("span[contenteditable=true]").first();

const waitAfterAutosave = async (page: Page) => {
  const autosavePromise = page.waitForResponse(
    (resp) => resp.url().includes("/redigertBrev") && resp.request().method() === "PUT",
  );
  await page.clock.fastForward(AUTOSAVE_TIMER);
  await autosavePromise;
};

function tableRow(...texts: string[]) {
  return {
    id: null,
    parentId: null,
    cells: texts.map((text) => newCell([newLiteral({ editedText: text, text })])),
  };
}

const brevMedUtfyltTabell = nyBrevResponse({
  redigertBrev: nyRedigertBrev({
    blocks: [
      newParagraph({
        content: [
          newTable([tableRow("Rad 1 kolonne 1", "Rad 1 kolonne 2"), tableRow("Rad 2 kolonne 1", "Rad 2 kolonne 2")]),
        ],
      }),
    ],
  }),
});

// Tests
test.describe("Tabell innsetting og redigering via kontekstmeny", () => {
  test.beforeEach(async ({ page }) => {
    await setupSakStubs(page);

    await page.route("**/bff/skribenten-backend/sak/123456/brev/1?reserver=true", (route) =>
      route.fulfill({ path: "test/e2e/fixtures/brevResponse.json", contentType: "application/json" }),
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
    // Wait for the page to be ready
    await expect(page.getByText("Saksbehandlingstiden vår er vanligvis")).toBeVisible();

    await page.clock.install();
    await insertTable(page, 3, 2);
    await waitAfterAutosave(page);
  });

  test("oppretter en 3x2-tabell", async ({ page }) => {
    const table = dataE2E(page, "letter-table");
    await expect(table).toHaveCount(1);
    await expect(table.locator("tbody tr")).toHaveCount(2);
    await expect(table.locator("tbody tr").first().locator("td")).toHaveCount(3);
  });

  test.describe("med en eksisterende 3x2-tabell", () => {
    test("legger til kolonne til høyre via kontekstmenyen", async ({ page }) => {
      await rightClickCell(page, 0, 1);
      await page.locator("[role=menuitem]", { hasText: "Sett inn kolonne til høyre" }).click();
      await waitAfterAutosave(page);

      await expect(dataE2E(page, "table-cell-0-2")).toBeVisible();
      await expect(dataE2E(page, "letter-table").locator("tbody tr").first().locator("td")).toHaveCount(4);
    });

    test("legger til rad under via kontekstmenyen", async ({ page }) => {
      await rightClickCell(page, 0, 0);
      await page.locator("[role=menuitem]", { hasText: "Sett inn rad under" }).click();
      await waitAfterAutosave(page);

      await expect(dataE2E(page, "table-cell-1-0")).toBeVisible();
      await expect(dataE2E(page, "letter-table").locator("tbody tr")).toHaveCount(3);
    });

    test("sletter en rad via kontekstmenyen", async ({ page }) => {
      await rightClickCell(page, 0, 0);
      await page.locator("[role=menuitem]", { hasText: "Sett inn rad under" }).click();
      await waitAfterAutosave(page);

      await rightClickCell(page, 1, 0);
      await page.locator("[role=menuitem]", { hasText: "Slett rad" }).click();
      await waitAfterAutosave(page);

      await expect(dataE2E(page, "letter-table").locator("tbody tr")).toHaveCount(2);
    });

    test("sletter en kolonne via kontekstmenyen", async ({ page }) => {
      await rightClickCell(page, 0, 1);
      await page.locator("[role=menuitem]", { hasText: "Sett inn kolonne til høyre" }).click();
      await waitAfterAutosave(page);

      await rightClickCell(page, 0, 2);
      await page.locator("[role=menuitem]", { hasText: "Slett kolonne" }).click();
      await waitAfterAutosave(page);

      await expect(dataE2E(page, "letter-table").locator("tbody tr").first().locator("td")).toHaveCount(3);
    });

    test("sletter hele tabellen via kontekstmenyen", async ({ page }) => {
      await rightClickCell(page, 0, 0);
      await page.locator("[role=menuitem]", { hasText: "Slett tabellen" }).click();
      await waitAfterAutosave(page);

      await expect(dataE2E(page, "letter-table")).toHaveCount(0);
    });
  });
});

test.describe("Tabellsnarveier for sletting", () => {
  test.beforeEach(async ({ page }) => {
    await setupSakStubs(page);

    await page.route("**/bff/skribenten-backend/sak/123456/brev/1?reserver=true", (route) =>
      route.fulfill({ json: brevMedUtfyltTabell }),
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
    await expect(dataE2E(page, "letter-table")).toHaveCount(1);
    await expect(page.getByText("Rad 1 kolonne 1")).toBeVisible();

    await page.clock.install();
  });

  test("sletter en utfylt rad med Shift+Backspace og beholder tabellen når siste rad slettes", async ({ page }) => {
    await page.getByText("Rad 2 kolonne 1").click();
    await page.keyboard.press("Shift+Backspace");
    await waitAfterAutosave(page);

    await expect(dataE2E(page, "letter-table").locator("tbody tr")).toHaveCount(1);
    await expect(page.getByText("Rad 2 kolonne 1")).toHaveCount(0);

    await page.getByText("Rad 1 kolonne 1").click();
    await page.keyboard.press("Shift+Backspace");
    await waitAfterAutosave(page);

    await expect(dataE2E(page, "letter-table")).toHaveCount(1);
    await expect(dataE2E(page, "letter-table").locator("tbody tr")).toHaveCount(1);
    await expect(page.getByText("Rad 1 kolonne 1")).toHaveCount(0);
    await expect(tableCellEditor(page, 0, 0)).toBeVisible();
  });

  test("sletter en utfylt rad med Shift+Delete og undo gjenoppretter raden", async ({ page }) => {
    await page.getByText("Rad 1 kolonne 1").click();
    await page.keyboard.press("Shift+Delete");
    await waitAfterAutosave(page);

    await expect(dataE2E(page, "letter-table").locator("tbody tr")).toHaveCount(1);
    await expect(page.getByText("Rad 1 kolonne 1")).toHaveCount(0);
    await expect(page.getByText("Rad 2 kolonne 1")).toBeVisible();

    await tableCellEditor(page, 0, 0).click();
    await page.keyboard.press(undoShortcut);

    await expect(dataE2E(page, "letter-table").locator("tbody tr")).toHaveCount(2);
    await expect(page.getByText("Rad 1 kolonne 1")).toBeVisible();
    await expect(page.getByText("Rad 2 kolonne 1")).toBeVisible();
  });

  test("sletter hele tabellen når Shift+Backspace brukes på siste tomme rad", async ({ page }) => {
    await page.getByText("Rad 2 kolonne 1").click();
    await page.keyboard.press("Shift+Backspace");
    await waitAfterAutosave(page);

    await page.getByText("Rad 1 kolonne 1").click();
    await page.keyboard.press("Shift+Backspace");
    await waitAfterAutosave(page);

    await tableCellEditor(page, 0, 0).click();
    await page.keyboard.press("Shift+Backspace");
    await waitAfterAutosave(page);

    await expect(dataE2E(page, "letter-table")).toHaveCount(0);
  });
});

test.describe("Tabellsnarveier for flytting av rader", () => {
  test.beforeEach(async ({ page }) => {
    await setupSakStubs(page);

    await page.route("**/bff/skribenten-backend/sak/123456/brev/1?reserver=true", (route) =>
      route.fulfill({ json: brevMedUtfyltTabell }),
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
    await expect(dataE2E(page, "letter-table")).toHaveCount(1);
    await expect(page.getByText("Rad 1 kolonne 1")).toBeVisible();

    await page.clock.install();
  });

  test("flytter en rad ned med Alt+Shift+ArrowDown", async ({ page }) => {
    await page.getByText("Rad 1 kolonne 1").click();
    await page.keyboard.press("Alt+Shift+ArrowDown");

    await expect(dataE2E(page, "letter-table").locator("tbody tr").nth(0)).toContainText("Rad 2 kolonne 1");
    await expect(dataE2E(page, "letter-table").locator("tbody tr").nth(1)).toContainText("Rad 1 kolonne 1");
  });

  test("flytter en rad opp med Alt+Shift+ArrowUp", async ({ page }) => {
    await page.getByText("Rad 2 kolonne 1").click();
    await page.keyboard.press("Alt+Shift+ArrowUp");

    await expect(dataE2E(page, "letter-table").locator("tbody tr").nth(0)).toContainText("Rad 2 kolonne 1");
    await expect(dataE2E(page, "letter-table").locator("tbody tr").nth(1)).toContainText("Rad 1 kolonne 1");
  });

  test("flytter ikke første rad opp", async ({ page }) => {
    await page.getByText("Rad 1 kolonne 1").click();
    await page.keyboard.press("Alt+Shift+ArrowUp");

    await expect(dataE2E(page, "letter-table").locator("tbody tr").nth(0)).toContainText("Rad 1 kolonne 1");
    await expect(dataE2E(page, "letter-table").locator("tbody tr").nth(1)).toContainText("Rad 2 kolonne 1");
  });

  test("flytter ikke siste rad ned", async ({ page }) => {
    await page.getByText("Rad 2 kolonne 1").click();
    await page.keyboard.press("Alt+Shift+ArrowDown");

    await expect(dataE2E(page, "letter-table").locator("tbody tr").nth(0)).toContainText("Rad 1 kolonne 1");
    await expect(dataE2E(page, "letter-table").locator("tbody tr").nth(1)).toContainText("Rad 2 kolonne 1");
  });

  test("undo gjenoppretter opprinnelig radrekkefølge", async ({ page }) => {
    await page.getByText("Rad 1 kolonne 1").click();
    await page.keyboard.press("Alt+Shift+ArrowDown");

    await expect(dataE2E(page, "letter-table").locator("tbody tr").nth(0)).toContainText("Rad 2 kolonne 1");
    await expect(dataE2E(page, "letter-table").locator("tbody tr").nth(1)).toContainText("Rad 1 kolonne 1");

    await page.keyboard.press(undoShortcut);

    await expect(dataE2E(page, "letter-table").locator("tbody tr").nth(0)).toContainText("Rad 1 kolonne 1");
    await expect(dataE2E(page, "letter-table").locator("tbody tr").nth(1)).toContainText("Rad 2 kolonne 1");
  });
});
