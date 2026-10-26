import { expect, type Page, test } from "@playwright/test";

import { setupSakStubs } from "~test/e2e/support/helpers";

import brevmetadata from "../fixtures/brevmetadata.json" with { type: "json" };
import sak from "../fixtures/sak.json" with { type: "json" };

const autobrev = {
  id: "PE_IY_05_411",
  name: "Orientering om automatisk vedtak",
  brevsystem: "EXSTREAM",
  spraak: ["NB", "NN"],
  brevkategori: "Informasjonsbrev",
  dokumentkategoriCode: "B",
  redigerbart: false,
  redigerbarBrevtittel: false,
  automatiskExstream: true,
};

const AUTOBREV_ORDER_URL = "**/bff/skribenten-backend/sak/123456/bestillBrev/exstream/autobrev";

async function selectAutobrevAndOrder(page: Page) {
  await page.goto("/saksnummer/123456/brevvelger?enhetsId=4405");
  await page.getByTestId("brevmal-search").pressSequentially(autobrev.name);
  await page.getByTestId("brevmal-button").filter({ hasText: autobrev.name }).click();
  await page.getByTestId("order-letter").click();
  await page.getByTestId("send-delautomatisk-brev").click();
  await page.waitForURL(/\/saksnummer\/123456\/kvittering/);
}

test.describe("Del-automatiske brev", () => {
  test.beforeEach(async ({ page }) => {
    await setupSakStubs(page);
    await page.route("**/bff/skribenten-backend/brevmal", (route) =>
      route.fulfill({ json: [...brevmetadata, autobrev] }),
    );
    await page.route("**/bff/skribenten-backend/sak/123456", (route) =>
      route.fulfill({ json: { ...sak, brevmalKoder: [...sak.brevmalKoder, autobrev.id] } }),
    );
  });

  test("Bestill og send del-automatisk brev", async ({ page }) => {
    let requestBody: Record<string, unknown> | undefined;
    const orderLetterUrls: string[] = [];
    page.on("request", (request) => {
      if (request.method() === "POST" && request.url().includes("/bestillBrev/")) {
        orderLetterUrls.push(request.url());
      }
    });
    await page.route(AUTOBREV_ORDER_URL, async (route) => {
      requestBody = route.request().postDataJSON();
      return route.fulfill({
        path: "test/e2e/fixtures/bestillBrevExstreamAutobrev.json",
        contentType: "application/json",
      });
    });

    await page.goto("/saksnummer/123456/brevvelger?enhetsId=4405");
    await page.getByTestId("brevmal-search").pressSequentially(autobrev.name);
    await page.getByTestId("brevmal-button").filter({ hasText: autobrev.name }).click();

    await expect(page.getByText("Del-automatisk", { exact: true })).toBeVisible();
    await expect(page.getByTestId("order-letter")).toHaveText("Bestill og send brev");

    await page.locator("select[name=spraak]").selectOption({ label: "Nynorsk" });

    const modal = page.getByTestId("confirm-delautomatisk-brev-modal");
    await page.getByTestId("order-letter").click();
    await expect(modal.getByRole("heading", { name: "Vil du bestille og sende brevet?" })).toBeVisible();
    await modal.getByRole("button", { name: "Nei" }).click();
    await expect(modal).toBeHidden();
    expect(orderLetterUrls).toHaveLength(0);

    await page.getByTestId("order-letter").click();
    await page.getByTestId("send-delautomatisk-brev").click();
    await page.waitForURL(/\/saksnummer\/123456\/kvittering/);

    expect(requestBody).toEqual({
      brevkode: autobrev.id,
      enhetsId: "4405",
      idTSSEkstern: null,
      vedtaksId: null,
      brevtittel: "",
      spraak: "NN",
    });
    expect(orderLetterUrls).toEqual([expect.stringMatching(/\/bestillBrev\/exstream\/autobrev$/)]);

    const card = page.getByTestId("kvittert-delautomatisk-brev");
    await expect(page.getByText("Sendt til mottaker")).toBeVisible();
    await expect(page.getByText(autobrev.name)).toBeVisible();
    await expect(card.getByText("Mottaker", { exact: true })).toBeVisible();
    await expect(card.getByText("Tydelig Bakke")).toBeVisible();
    await expect(card.getByText("Bruker", { exact: true })).toBeVisible();
    await expect(card.getByText("453864999")).toBeVisible();
  });

  test("Viser verge-tagg i kvittering når brukeren har verge", async ({ page }) => {
    await page.route("**/bff/skribenten-backend/sak/123456/adresse", (route) =>
      route.fulfill({
        json: {
          adresseString: "Vergeveien 1, 0150 OSLO",
          adresselinjer: ["Vergeveien 1", "0150 OSLO"],
          type: "VERGE_PERSON_POSTADRESSE",
        },
      }),
    );
    await page.route(AUTOBREV_ORDER_URL, (route) =>
      route.fulfill({ path: "test/e2e/fixtures/bestillBrevExstreamAutobrev.json", contentType: "application/json" }),
    );

    await selectAutobrevAndOrder(page);

    const card = page.getByTestId("kvittert-delautomatisk-brev");
    await expect(card.getByText("Vergeveien 1")).toBeVisible();
    await expect(card.getByText("Verge", { exact: true })).toBeVisible();
    await expect(card.getByText("Bruker", { exact: true })).toHaveCount(0);
  });

  test("Viser samhandler-tagg i kvittering når samhandler er mottaker", async ({ page }) => {
    let requestBody: Record<string, unknown> | undefined;
    await page.route("**/bff/skribenten-backend/hentSamhandler", (route) =>
      route.fulfill({
        json: {
          success: {
            navn: "Testsamhandler AS",
            samhandlerType: "ADVO",
            idType: "ORG",
            offentligId: "999999999",
            organisasjonsnummer: null,
          },
          failure: null,
        },
      }),
    );
    await page.route("**/bff/skribenten-backend/hentSamhandlerAdresse", (route) =>
      route.fulfill({
        json: {
          adresse: {
            navn: "Testsamhandler AS",
            linje1: "Samhandlerveien 2",
            postnr: "0151",
            poststed: "OSLO",
            land: "NOR",
          },
          failureType: null,
        },
      }),
    );
    await page.route(AUTOBREV_ORDER_URL, async (route) => {
      requestBody = route.request().postDataJSON();
      return route.fulfill({
        path: "test/e2e/fixtures/bestillBrevExstreamAutobrev.json",
        contentType: "application/json",
      });
    });

    await page.goto("/saksnummer/123456/brevvelger?enhetsId=4405&idTSSEkstern=80000000001");
    await page.getByTestId("brevmal-search").pressSequentially(autobrev.name);
    await page.getByTestId("brevmal-button").filter({ hasText: autobrev.name }).click();
    await page.getByTestId("order-letter").click();
    await page.getByTestId("send-delautomatisk-brev").click();
    await page.waitForURL(/\/saksnummer\/123456\/kvittering/);

    expect(requestBody).toMatchObject({ idTSSEkstern: "80000000001" });
    const card = page.getByTestId("kvittert-delautomatisk-brev");
    await expect(card.getByText("Testsamhandler AS")).toBeVisible();
    await expect(card.getByText("Samhandler", { exact: true })).toBeVisible();
  });

  test("Viser feil i kvittering når bestilling feiler", async ({ page }) => {
    await page.route(AUTOBREV_ORDER_URL, (route) =>
      route.fulfill({
        json: { journalpostId: null, failureType: "ENHET_UNAUTHORIZED" },
        headers: { "x-request-id": "test-request-id" },
      }),
    );

    await selectAutobrevAndOrder(page);

    await expect(page.getByText("Kunne ikke sende brev").first()).toBeVisible();
    await expect(page.getByTestId("generic-error-alert")).toContainText("Teknisk feil: ENHET_UNAUTHORIZED");
    await expect(page.getByRole("button", { name: "Prøv å sende igjen" })).toHaveCount(0);
  });

  test("Viser feilmelding fra PEN i kvittering", async ({ page }) => {
    await page.route(AUTOBREV_ORDER_URL, (route) =>
      route.fulfill({
        status: 422,
        json: { tittel: "Adresse ikke registrert", melding: "Brukeren mangler adresse." },
      }),
    );

    await selectAutobrevAndOrder(page);

    await expect(page.getByTestId("functional-error-alert")).toContainText("Adresse ikke registrert");
    await expect(page.getByTestId("functional-error-alert")).toContainText("Brukeren mangler adresse.");
  });

  test("Vanlige Exstream-brev bruker fortsatt Fortsett uten del-automatisk-tag", async ({ page }) => {
    await page.goto("/saksnummer/123456/brevvelger");
    await page.getByTestId("category-item").filter({ hasText: "Feilutbetaling" }).click();
    await page.getByTestId("brevmal-button").filter({ hasText: "Varsel - tilbakekreving" }).click();

    await expect(page.getByTestId("order-letter")).toHaveText("Fortsett");
    await expect(page.getByText("Del-automatisk", { exact: true })).toHaveCount(0);
  });
});
