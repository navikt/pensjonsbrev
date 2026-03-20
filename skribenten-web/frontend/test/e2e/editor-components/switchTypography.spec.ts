import { expect, type Page, test } from "@playwright/test";

import { dataE2E, setupSakStubs } from "../utils/helpers";

function makeBrevResponse(redigertBrev: object) {
  return {
    info: {
      id: 1,
      brevkode: "BREV1",
      brevtittel: "Brev 1",
      opprettet: "2024-01-01",
      sistredigert: "2024-01-01",
      sistredigertAv: { id: "Z123", navn: "Z entotre" },
      opprettetAv: { id: "Z123", navn: "Z entotre" },
      status: { type: "UnderRedigering", redigeresAv: { id: "Z123", navn: "Z entotre" } },
      distribusjonstype: "SENTRALPRINT",
      mottaker: null,
      avsenderEnhet: { enhetNr: "0001", navn: "NAV Familie- og pensjonsytelser" },
      spraak: "NB",
      journalpostId: null,
      vedtaksId: null,
      brevtype: "INFORMASJONSBREV",
      saksId: "22981081",
    },
    redigertBrev,
    redigertBrevHash: "hash1",
    saksbehandlerValg: {},
    propertyUsage: null,
    valgteVedlegg: null,
  };
}

function makeLiteral(id: number, text: string) {
  return {
    id,
    parentId: null,
    type: "LITERAL",
    text,
    editedText: text,
    fontType: "PLAIN",
    editedFontType: null,
    tags: [],
  };
}

const typographyLetter = {
  title: {
    text: [makeLiteral(1, "Test Typography")],
    deletedContent: [],
  },
  sakspart: {
    gjelderNavn: "Test",
    gjelderFoedselsnummer: "12345678910",
    saksnummer: "1234",
    dokumentDato: "2024-03-15",
  },
  blocks: [
    {
      id: 10,
      parentId: null,
      editable: true,
      content: [makeLiteral(11, "Dette er en title1 block")],
      deletedContent: [],
      type: "TITLE1",
    },
    {
      id: 20,
      parentId: null,
      editable: true,
      content: [makeLiteral(21, "Dette er en title2 block")],
      deletedContent: [],
      type: "TITLE2",
    },
    {
      id: 30,
      parentId: null,
      editable: true,
      content: [makeLiteral(31, "Dette er en title3 block")],
      deletedContent: [],
      type: "TITLE3",
    },
    {
      id: 40,
      parentId: null,
      editable: true,
      content: [makeLiteral(41, "Dette er en paragraph block")],
      deletedContent: [],
      type: "PARAGRAPH",
    },
  ],
  signatur: {
    hilsenTekst: "Med vennlig hilsen",
    saksbehandlerRolleTekst: "Saksbehandler",
    saksbehandlerNavn: "Ole",
    attesterendeSaksbehandlerNavn: "",
    navAvsenderEnhet: "Nav",
  },
  deletedBlocks: [],
};

async function setupBrevRoute(page: Page, brevResponse: object) {
  await setupSakStubs(page);

  await page.route("**/bff/skribenten-backend/sak/123456/brev/1**", (route) => {
    if (route.request().url().includes("/pdf")) {
      return route.fulfill({ path: "test/e2e/fixtures/helloWorldPdf.txt", contentType: "application/json" });
    }
    return route.fulfill({ json: brevResponse, contentType: "application/json" });
  });

  await page.route("**/bff/skribenten-backend/sak/123456/brev/1", (route) => {
    if (route.request().method() === "PUT" || route.request().method() === "PATCH") {
      return route.fulfill({ json: brevResponse, contentType: "application/json" });
    }
    return route.fallback();
  });
}

async function navigateToEditor(page: Page) {
  await page.goto("/saksnummer/123456/brev/1");
  await page.locator(".editor").waitFor({ state: "visible", timeout: 15_000 });
  await page.evaluate(() => document.fonts.ready);
}

async function assertCaret(page: Page, content: string, caretOffset: number) {
  const focused = page.locator(":focus");
  await expect(focused).toContainText(content);
  const offset = await page.evaluate(() => {
    const sel = globalThis.getSelection();
    return sel?.rangeCount ? sel.getRangeAt(0).startOffset : -1;
  });
  expect(offset).toBe(caretOffset);
}

async function getCaretOffset(page: Page): Promise<number> {
  return page.evaluate(() => {
    const sel = globalThis.getSelection();
    return sel?.rangeCount ? sel.getRangeAt(0).startOffset : -1;
  });
}

test.describe("Typography", () => {
  test.beforeEach(async ({ page }) => {
    const brevResponse = makeBrevResponse(typographyLetter);
    await setupBrevRoute(page, brevResponse);
    await navigateToEditor(page);
  });

  test.describe("overskrift 1", () => {
    test.describe("manuelt", () => {
      test("toggler av/på", async ({ page }) => {
        await page.getByText("title1").click();
        const typographySelect = dataE2E(page, "typography-select");
        await expect(typographySelect).toHaveValue(/1/);

        await typographySelect.selectOption({ label: "Normal (Alt+4)" });
        await expect(page.locator('.PARAGRAPH:has-text("Dette er en title1 block")')).toBeVisible();

        await typographySelect.selectOption({ label: "Overskrift 1 (Alt+1)" });
        await expect(page.locator('.TITLE1:has-text("Dette er en title1 block")')).toBeVisible();
      });

      test("caret holder seg i samme posisjon ved toggle", async ({ page }) => {
        await page.getByText("title1").click();
        await page.keyboard.press("ArrowLeft");
        await page.keyboard.press("ArrowLeft");
        const offsetBefore = await getCaretOffset(page);
        await dataE2E(page, "typography-select").selectOption({ label: "Normal (Alt+4)" });
        await assertCaret(page, "title1", offsetBefore);
      });
    });

    test.describe("shortcut", () => {
      test("toggler av/på", async ({ page }) => {
        await page.getByText("title1").click();
        const typographySelect = dataE2E(page, "typography-select");
        await expect(typographySelect).toHaveValue(/1/);

        await page.getByText("title1").press("Alt+4");
        await expect(page.locator('.PARAGRAPH:has-text("Dette er en title1 block")')).toBeVisible();

        await page.getByText("title1").press("Alt+1");
        await expect(page.locator('.TITLE1:has-text("Dette er en title1 block")')).toBeVisible();
      });

      test("caret holder seg i samme posisjon ved toggle", async ({ page }) => {
        await page.getByText("title1").click();
        await page.keyboard.press("ArrowLeft");
        await page.keyboard.press("ArrowLeft");
        const offsetBefore = await getCaretOffset(page);
        await page.keyboard.press("Alt+1");
        await assertCaret(page, "title1", offsetBefore);
      });
    });
  });

  test.describe("overskrift 2", () => {
    test.describe("manuelt", () => {
      test("toggler av/på", async ({ page }) => {
        await page.getByText("title2").click();
        const typographySelect = dataE2E(page, "typography-select");

        await typographySelect.selectOption({ label: "Normal (Alt+4)" });
        await expect(page.locator('.PARAGRAPH:has-text("Dette er en title2 block")')).toBeVisible();

        await typographySelect.selectOption({ label: "Overskrift 2 (Alt+2)" });
        await expect(page.locator('.TITLE2:has-text("Dette er en title2 block")')).toBeVisible();
      });

      test("caret holder seg i samme posisjon ved toggle", async ({ page }) => {
        await page.getByText("title2").click();
        await page.keyboard.press("ArrowLeft");
        await page.keyboard.press("ArrowLeft");
        const offsetBefore = await getCaretOffset(page);
        await dataE2E(page, "typography-select").selectOption({ label: "Normal (Alt+4)" });
        await assertCaret(page, "title2", offsetBefore);
      });
    });

    test.describe("shortcut", () => {
      test("toggler av/på", async ({ page }) => {
        await page.getByText("title2").click();
        const typographySelect = dataE2E(page, "typography-select");
        await expect(typographySelect).toHaveValue(/2/);

        await page.getByText("title2").press("Alt+4");
        await expect(page.locator('.PARAGRAPH:has-text("Dette er en title2 block")')).toBeVisible();

        await page.getByText("title2").press("Alt+2");
        await expect(page.locator('.TITLE2:has-text("Dette er en title2 block")')).toBeVisible();
      });

      test("caret holder seg i samme posisjon ved toggle", async ({ page }) => {
        await page.getByText("title2").click();
        await page.keyboard.press("ArrowLeft");
        await page.keyboard.press("ArrowLeft");
        const offsetBefore = await getCaretOffset(page);
        await page.keyboard.press("Alt+2");
        await assertCaret(page, "title2", offsetBefore);
      });
    });
  });

  test.describe("overskrift 3", () => {
    test.describe("manuelt", () => {
      test("toggler av/på", async ({ page }) => {
        await page.getByText("title3").click();
        const typographySelect = dataE2E(page, "typography-select");

        await typographySelect.selectOption({ label: "Normal (Alt+4)" });
        await expect(page.locator('.PARAGRAPH:has-text("Dette er en title3 block")')).toBeVisible();

        await typographySelect.selectOption({ label: "Overskrift 3 (Alt+3)" });
        await expect(page.locator('.TITLE3:has-text("Dette er en title3 block")')).toBeVisible();
      });

      test("caret holder seg i samme posisjon ved toggle", async ({ page }) => {
        await page.getByText("title3").click();
        await page.keyboard.press("ArrowLeft");
        await page.keyboard.press("ArrowLeft");
        const offsetBefore = await getCaretOffset(page);
        await dataE2E(page, "typography-select").selectOption({ label: "Normal (Alt+4)" });
        await assertCaret(page, "title3", offsetBefore);
      });
    });

    test.describe("shortcut", () => {
      test("toggler av/på", async ({ page }) => {
        await page.getByText("title3").click();
        const typographySelect = dataE2E(page, "typography-select");
        await expect(typographySelect).toHaveValue(/3/);

        await page.getByText("title3").press("Alt+4");
        await expect(page.locator('.PARAGRAPH:has-text("Dette er en title3 block")')).toBeVisible();

        await page.getByText("title3").press("Alt+3");
        await expect(page.locator('.TITLE3:has-text("Dette er en title3 block")')).toBeVisible();
      });

      test("caret holder seg i samme posisjon ved toggle", async ({ page }) => {
        await page.getByText("title3").click();
        await page.keyboard.press("ArrowLeft");
        await page.keyboard.press("ArrowLeft");
        const offsetBefore = await getCaretOffset(page);
        await page.keyboard.press("Alt+3");
        await assertCaret(page, "title3", offsetBefore);
      });
    });
  });

  test.describe("normal", () => {
    test.describe("manuelt", () => {
      test("toggler av/på", async ({ page }) => {
        await page.getByText("paragraph").click();
        const typographySelect = dataE2E(page, "typography-select");

        await typographySelect.selectOption({ label: "Overskrift 1 (Alt+1)" });
        await expect(page.locator('.TITLE1:has-text("Dette er en paragraph block")')).toBeVisible();

        await typographySelect.selectOption({ label: "Normal (Alt+4)" });
        await expect(page.locator('.PARAGRAPH:has-text("Dette er en paragraph block")')).toBeVisible();
      });

      test("caret holder seg i samme posisjon ved toggle", async ({ page }) => {
        await page.getByText("paragraph").click();
        await page.keyboard.press("ArrowLeft");
        await page.keyboard.press("ArrowLeft");
        const offsetBefore = await getCaretOffset(page);
        await dataE2E(page, "typography-select").selectOption({ label: "Overskrift 1 (Alt+1)" });
        await assertCaret(page, "paragraph", offsetBefore);
      });
    });

    test.describe("shortcut", () => {
      test("toggler av/på", async ({ page }) => {
        await page.getByText("paragraph").click();

        await page.getByText("paragraph").press("Alt+1");
        await expect(page.locator('.TITLE1:has-text("Dette er en paragraph block")')).toBeVisible();

        await page.getByText("paragraph").press("Alt+4");
        await expect(page.locator('.PARAGRAPH:has-text("Dette er en paragraph block")')).toBeVisible();
      });

      test("caret holder seg i samme posisjon ved toggle", async ({ page }) => {
        await page.getByText("paragraph").click();
        await page.keyboard.press("ArrowLeft");
        await page.keyboard.press("ArrowLeft");
        const offsetBefore = await getCaretOffset(page);
        await page.keyboard.press("Alt+3");
        await assertCaret(page, "paragraph", offsetBefore);
      });
    });
  });
});
