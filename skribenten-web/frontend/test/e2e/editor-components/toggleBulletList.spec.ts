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

function makeItem(id: number, text: string) {
  return { id, parentId: null, content: [makeLiteral(id * 10 + 1, text)], deletedContent: [] };
}

function makeItemList(id: number, items: object[]) {
  return { id, parentId: null, type: "ITEM_LIST", items, deletedItems: [] };
}

function makeBlock(id: number, content: object[]) {
  return { id, parentId: null, editable: true, content, deletedContent: [], type: "PARAGRAPH" };
}

function makeLetter(blocks: object[]) {
  return {
    title: {
      text: [makeLiteral(1, "Test")],
      deletedContent: [],
    },
    sakspart: {
      gjelderNavn: "Test",
      gjelderFoedselsnummer: "12345678910",
      saksnummer: "1234",
      dokumentDato: "2024-03-15",
    },
    blocks,
    signatur: {
      hilsenTekst: "Hilsen",
      saksbehandlerRolleTekst: "Saksbehandler",
      saksbehandlerNavn: "Ole",
      attesterendeSaksbehandlerNavn: "",
      navAvsenderEnhet: "Nav",
    },
    deletedBlocks: [],
  };
}

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
}

test.describe("toggle bullet-list", () => {
  test.describe("toggle on", () => {
    test("toggler et enkelt avsnitt", async ({ page }) => {
      const letter = makeLetter([makeBlock(10, [makeLiteral(11, "Dette er kun et avsnitt")])]);
      await setupBrevRoute(page, makeBrevResponse(letter));
      await navigateToEditor(page);

      await expect(page.locator("ul")).toHaveCount(0);
      await page.getByText("Dette er kun et avsnitt").click();
      await expect(page.locator("li span")).toHaveCount(0);
      await dataE2E(page, "editor-bullet-list").click();
      await expect(page.locator("li span").filter({ hasText: "Dette er kun et avsnitt" })).toBeVisible();
      await expect(page.locator("ul")).toHaveCount(1);
    });

    test("lager en punktliste når man allerede har en ItemList i samme blokk før", async ({ page }) => {
      const letter = makeLetter([
        makeBlock(10, [makeItemList(11, [makeItem(111, "Punkt 1")]), makeLiteral(12, "Avsnitt med punktliste")]),
      ]);
      await setupBrevRoute(page, makeBrevResponse(letter));
      await navigateToEditor(page);

      await expect(page.locator("li")).toHaveCount(1);
      await page.getByText("Avsnitt med punktliste").click();
      await dataE2E(page, "editor-bullet-list").click();
      await expect(page.locator("li")).toHaveCount(2);
      await expect(page.locator("li span").nth(1)).toContainText("Avsnitt med punktliste");
    });

    test("lager en punktliste når man allerede har en ItemList i samme blokk etter", async ({ page }) => {
      const letter = makeLetter([
        makeBlock(10, [makeLiteral(11, "Avsnitt med punktliste"), makeItemList(12, [makeItem(121, "Punkt 1")])]),
      ]);
      await setupBrevRoute(page, makeBrevResponse(letter));
      await navigateToEditor(page);

      await expect(page.locator("li")).toHaveCount(1);
      await page.getByText("Avsnitt med punktliste").click();
      await dataE2E(page, "editor-bullet-list").click();
      await expect(page.locator("li")).toHaveCount(2);
      await expect(page.locator("li span").nth(0)).toContainText("Avsnitt med punktliste");
    });

    test("lager en punktliste når man allerede har en ItemList i samme blokk før og etter", async ({ page }) => {
      const letter = makeLetter([
        makeBlock(10, [
          makeItemList(11, [makeItem(111, "Punkt 1")]),
          makeLiteral(12, "Avsnitt med punktliste"),
          makeItemList(13, [makeItem(131, "Punkt 2")]),
        ]),
      ]);
      await setupBrevRoute(page, makeBrevResponse(letter));
      await navigateToEditor(page);

      await expect(page.locator("ul")).toHaveCount(2);
      await expect(page.locator("li")).toHaveCount(2);
      await page.getByText("Avsnitt med punktliste").click();
      await dataE2E(page, "editor-bullet-list").click();
      await expect(page.locator("ul")).toHaveCount(1);
      await expect(page.locator("li")).toHaveCount(3);
      await expect(page.locator("li span").nth(1)).toContainText("Avsnitt med punktliste");
    });

    test("lager en punktliste når man allerede har en itemList i en annen blokk før", async ({ page }) => {
      const letter = makeLetter([
        makeBlock(10, [makeItemList(11, [makeItem(111, "Punkt 1")])]),
        makeBlock(20, [makeLiteral(21, "Avsnitt uten punktliste")]),
      ]);
      await setupBrevRoute(page, makeBrevResponse(letter));
      await navigateToEditor(page);

      await expect(page.locator("ul")).toHaveCount(1);
      await expect(page.locator("li")).toHaveCount(1);
      await page.getByText("Avsnitt uten punktliste").click();
      await dataE2E(page, "editor-bullet-list").click();
      await expect(page.locator("ul")).toHaveCount(2);
      await expect(page.locator("li")).toHaveCount(2);
    });

    test("lager en punktliste når man allerede har en itemList i en annen blokk etter", async ({ page }) => {
      const letter = makeLetter([
        makeBlock(10, [makeLiteral(11, "Avsnitt uten punktliste")]),
        makeBlock(20, [makeItemList(21, [makeItem(211, "Punkt 1")])]),
      ]);
      await setupBrevRoute(page, makeBrevResponse(letter));
      await navigateToEditor(page);

      await expect(page.locator("ul")).toHaveCount(1);
      await expect(page.locator("li")).toHaveCount(1);
      await page.getByText("Avsnitt uten punktliste").click();
      await dataE2E(page, "editor-bullet-list").click();
      await expect(page.locator("ul")).toHaveCount(2);
      await expect(page.locator("li")).toHaveCount(2);
    });

    test("toggler punktliste mellom 2 punktlister", async ({ page }) => {
      const letter = makeLetter([
        makeBlock(10, [makeItemList(11, [makeItem(111, "Punkt 1")])]),
        makeBlock(20, [makeLiteral(21, "Avsnitt uten punktliste")]),
        makeBlock(30, [makeItemList(31, [makeItem(311, "Punkt 2")])]),
      ]);
      await setupBrevRoute(page, makeBrevResponse(letter));
      await navigateToEditor(page);

      await expect(page.locator("ul")).toHaveCount(2);
      await expect(page.locator("li")).toHaveCount(2);
      await page.getByText("Avsnitt uten punktliste").click();
      await dataE2E(page, "editor-bullet-list").click();
      await expect(page.locator("ul")).toHaveCount(3);
      await expect(page.locator("li")).toHaveCount(3);
      await expect(page.getByText("Avsnitt uten punktliste")).toBeVisible();
    });
  });

  test.describe("toggle off", () => {
    test("toggler av et enkelt avsnitt", async ({ page }) => {
      const letter = makeLetter([makeBlock(10, [makeItemList(11, [makeItem(111, "Dette er kun et avsnitt")])])]);
      await setupBrevRoute(page, makeBrevResponse(letter));
      await navigateToEditor(page);

      await expect(page.locator("ul")).toHaveCount(1);
      await expect(page.locator("li")).toHaveCount(1);
      await page.getByText("Dette er kun et avsnitt").click();
      await dataE2E(page, "editor-bullet-list").click();
      await expect(page.locator("ul")).toHaveCount(0);
      await expect(page.locator("li")).toHaveCount(0);
      await expect(page.getByText("Dette er kun et avsnitt")).toBeVisible();
    });

    test("toggler av et punkt på starten av en punktliste", async ({ page }) => {
      const letter = makeLetter([
        makeBlock(10, [makeItemList(11, [makeItem(111, "skal brytes ut"), makeItem(112, "Punkt 1")])]),
      ]);
      await setupBrevRoute(page, makeBrevResponse(letter));
      await navigateToEditor(page);

      await expect(page.locator("ul")).toHaveCount(1);
      await expect(page.locator("li")).toHaveCount(2);
      await page.getByText("skal brytes ut").click();
      await dataE2E(page, "editor-bullet-list").click();
      await expect(page.locator("ul")).toHaveCount(1);
      await expect(page.locator("li")).toHaveCount(1);
      await expect(page.locator(".PARAGRAPH").first().getByText("skal brytes ut")).toBeVisible();
    });

    test("bevarer content som er rundt en punktliste når man toggler av på starten av en punktliste", async ({
      page,
    }) => {
      const letter = makeLetter([
        makeBlock(10, [
          makeLiteral(11, "Denne skal ikke forsvinne når man trigger av punktliste"),
          makeItemList(12, [makeItem(121, "skal brytes ut"), makeItem(122, "Punkt 1")]),
          makeLiteral(13, "Denne skal heller ikke forsvinne når man trigger av punktliste"),
        ]),
      ]);
      await setupBrevRoute(page, makeBrevResponse(letter));
      await navigateToEditor(page);

      await expect(page.locator("ul")).toHaveCount(1);
      await expect(page.locator("li")).toHaveCount(2);
      await expect(page.getByText("Denne skal ikke forsvinne når man trigger av punktliste")).toBeVisible();
      await expect(page.getByText("Denne skal heller ikke forsvinne når man trigger av punktliste")).toBeVisible();

      await page.getByText("skal brytes ut").click();
      await dataE2E(page, "editor-bullet-list").click();
      await expect(page.locator("ul")).toHaveCount(1);
      await expect(page.locator("li")).toHaveCount(1);
      await expect(page.getByText("skal brytes ut")).toBeVisible();
      await expect(page.getByText("Denne skal ikke forsvinne når man trigger av punktliste")).toBeVisible();
      await expect(page.getByText("Denne skal heller ikke forsvinne når man trigger av punktliste")).toBeVisible();
    });

    test("bevarer content som er rundt en punktliste når man toggler av på starten av en punktliste som kun inneholder 1 punkt", async ({
      page,
    }) => {
      const letter = makeLetter([
        makeBlock(10, [
          makeLiteral(11, "Denne skal ikke forsvinne når man trigger av punktliste"),
          makeItemList(12, [makeItem(121, "skal brytes ut")]),
          makeLiteral(13, "Denne skal heller ikke forsvinne når man trigger av punktliste"),
        ]),
      ]);
      await setupBrevRoute(page, makeBrevResponse(letter));
      await navigateToEditor(page);

      await expect(page.locator("ul")).toHaveCount(1);
      await expect(page.locator("li")).toHaveCount(1);
      await expect(page.getByText("Denne skal ikke forsvinne når man trigger av punktliste")).toBeVisible();
      await expect(page.getByText("Denne skal heller ikke forsvinne når man trigger av punktliste")).toBeVisible();

      await page.getByText("skal brytes ut").click();
      await dataE2E(page, "editor-bullet-list").click();
      await expect(page.locator("ul")).toHaveCount(0);
      await expect(page.locator("li")).toHaveCount(0);
      await expect(page.getByText("skal brytes ut")).toBeVisible();
      await expect(page.getByText("Denne skal ikke forsvinne når man trigger av punktliste")).toBeVisible();
      await expect(page.getByText("Denne skal heller ikke forsvinne når man trigger av punktliste")).toBeVisible();
    });

    test("bevarer content som er rundt en punktliste når man toggler av på midten av en punktliste", async ({
      page,
    }) => {
      const letter = makeLetter([
        makeBlock(10, [
          makeLiteral(11, "Denne skal ikke forsvinne når man trigger av punktliste"),
          makeItemList(12, [makeItem(121, "Punkt 1"), makeItem(122, "skal brytes ut"), makeItem(123, "punkt 2")]),
          makeLiteral(13, "Denne skal heller ikke forsvinne når man trigger av punktliste"),
        ]),
      ]);
      await setupBrevRoute(page, makeBrevResponse(letter));
      await navigateToEditor(page);

      await expect(page.locator("ul")).toHaveCount(1);
      await expect(page.locator("li")).toHaveCount(3);
      await expect(page.getByText("Denne skal ikke forsvinne når man trigger av punktliste")).toBeVisible();
      await expect(page.getByText("Denne skal heller ikke forsvinne når man trigger av punktliste")).toBeVisible();

      await page.getByText("skal brytes ut").click();
      await dataE2E(page, "editor-bullet-list").click();
      await expect(page.locator("ul")).toHaveCount(2);
      await expect(page.locator("li")).toHaveCount(2);
      await expect(page.getByText("skal brytes ut")).toBeVisible();
      await expect(page.getByText("Denne skal ikke forsvinne når man trigger av punktliste")).toBeVisible();
      await expect(page.getByText("Denne skal heller ikke forsvinne når man trigger av punktliste")).toBeVisible();
    });

    test("bevarer content som er rundt en punktliste når man toggler av på slutten av en punktliste", async ({
      page,
    }) => {
      const letter = makeLetter([
        makeBlock(10, [
          makeLiteral(11, "Denne skal ikke forsvinne når man trigger av punktliste"),
          makeItemList(12, [makeItem(121, "Punkt 1"), makeItem(122, "skal brytes ut")]),
          makeLiteral(13, "Denne skal heller ikke forsvinne når man trigger av punktliste"),
        ]),
      ]);
      await setupBrevRoute(page, makeBrevResponse(letter));
      await navigateToEditor(page);

      await expect(page.locator("ul")).toHaveCount(1);
      await expect(page.locator("li")).toHaveCount(2);
      await expect(page.getByText("Denne skal ikke forsvinne når man trigger av punktliste")).toBeVisible();
      await expect(page.getByText("Denne skal heller ikke forsvinne når man trigger av punktliste")).toBeVisible();

      await page.getByText("skal brytes ut").click();
      await dataE2E(page, "editor-bullet-list").click();
      await expect(page.locator("ul")).toHaveCount(1);
      await expect(page.locator("li")).toHaveCount(1);
      await expect(page.getByText("skal brytes ut")).toBeVisible();
      await expect(page.getByText("Denne skal ikke forsvinne når man trigger av punktliste")).toBeVisible();
      await expect(page.getByText("Denne skal heller ikke forsvinne når man trigger av punktliste")).toBeVisible();
    });

    test("bevarer content som er rundt en punktliste når man toggler av på slutten av en punktliste som kun inneholder 1 punkt", async ({
      page,
    }) => {
      const letter = makeLetter([
        makeBlock(10, [
          makeLiteral(11, "Denne skal ikke forsvinne når man trigger av punktliste"),
          makeItemList(12, [makeItem(121, "skal brytes ut")]),
          makeLiteral(13, "Denne skal heller ikke forsvinne når man trigger av punktliste"),
        ]),
      ]);
      await setupBrevRoute(page, makeBrevResponse(letter));
      await navigateToEditor(page);

      await expect(page.locator("ul")).toHaveCount(1);
      await expect(page.locator("li")).toHaveCount(1);
      await expect(page.getByText("Denne skal ikke forsvinne når man trigger av punktliste")).toBeVisible();
      await expect(page.getByText("Denne skal heller ikke forsvinne når man trigger av punktliste")).toBeVisible();

      await page.getByText("skal brytes ut").click();
      await dataE2E(page, "editor-bullet-list").click();
      await expect(page.locator("ul")).toHaveCount(0);
      await expect(page.locator("li")).toHaveCount(0);
      await expect(page.getByText("skal brytes ut")).toBeVisible();
      await expect(page.getByText("Denne skal ikke forsvinne når man trigger av punktliste")).toBeVisible();
      await expect(page.getByText("Denne skal heller ikke forsvinne når man trigger av punktliste")).toBeVisible();
    });

    test("toggler av et punkt på slutten av en punktliste", async ({ page }) => {
      const letter = makeLetter([
        makeBlock(10, [makeItemList(11, [makeItem(111, "Punkt 1"), makeItem(112, "skal brytes ut")])]),
      ]);
      await setupBrevRoute(page, makeBrevResponse(letter));
      await navigateToEditor(page);

      await expect(page.locator("ul")).toHaveCount(1);
      await expect(page.locator("li")).toHaveCount(2);
      await page.getByText("skal brytes ut").click();
      await dataE2E(page, "editor-bullet-list").click();
      await expect(page.locator("ul")).toHaveCount(1);
      await expect(page.locator("li")).toHaveCount(1);
      await expect(page.locator(".PARAGRAPH").first().getByText("skal brytes ut")).toBeVisible();
    });

    test("fjerner punkt fra midten av en punktliste", async ({ page }) => {
      const letter = makeLetter([
        makeBlock(10, [
          makeItemList(11, [makeItem(111, "Punkt 1"), makeItem(112, "skal brytes ut"), makeItem(113, "Punkt 2")]),
        ]),
      ]);
      await setupBrevRoute(page, makeBrevResponse(letter));
      await navigateToEditor(page);

      await expect(page.locator("ul")).toHaveCount(1);
      await expect(page.locator("li")).toHaveCount(3);
      await page.getByText("skal brytes ut").click();
      await dataE2E(page, "editor-bullet-list").click();
      await expect(page.locator("ul")).toHaveCount(2);
      await expect(page.locator("li")).toHaveCount(2);
      await expect(page.locator(".PARAGRAPH").first().getByText("skal brytes ut")).toBeVisible();
    });
  });
});
