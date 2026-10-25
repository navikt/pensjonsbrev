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

const AUTOBREV_URL = "**/bff/skribenten-backend/sak/123456/bestillBrev/exstream/autobrev";

async function velgAutobrevOgBestill(page: Page) {
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
    const bestillBrevUrler: string[] = [];
    page.on("request", (request) => {
      if (request.method() === "POST" && request.url().includes("/bestillBrev/")) {
        bestillBrevUrler.push(request.url());
      }
    });
    await page.route(AUTOBREV_URL, async (route) => {
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

    const modal = page.getByTestId("bekreft-delautomatisk-brev-modal");
    await page.getByTestId("order-letter").click();
    await expect(modal.getByRole("heading", { name: "Vil du bestille og sende brevet?" })).toBeVisible();
    await modal.getByRole("button", { name: "Nei" }).click();
    await expect(modal).toBeHidden();
    expect(bestillBrevUrler).toHaveLength(0);

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
    expect(bestillBrevUrler).toEqual([expect.stringMatching(/\/bestillBrev\/exstream\/autobrev$/)]);

    const kort = page.getByTestId("kvittert-delautomatisk-brev");
    await expect(page.getByText("Sendt til mottaker")).toBeVisible();
    await expect(page.getByText(autobrev.name)).toBeVisible();
    await expect(kort.getByText("Mottaker", { exact: true })).toBeVisible();
    await expect(kort.getByText("Tydelig Bakke")).toBeVisible();
    await expect(kort.getByText("Bruker", { exact: true })).toHaveCount(0);
    await expect(kort.getByText("453864999")).toBeVisible();
  });

  test("Viser feil i kvittering når bestilling feiler", async ({ page }) => {
    await page.route(AUTOBREV_URL, (route) =>
      route.fulfill({
        json: { journalpostId: null, failureType: "ENHET_UNAUTHORIZED" },
        headers: { "x-request-id": "test-request-id" },
      }),
    );

    await velgAutobrevOgBestill(page);

    await expect(page.getByText("Kunne ikke sende brev").first()).toBeVisible();
    await expect(page.getByTestId("generic-error-alert")).toContainText("Teknisk feil: ENHET_UNAUTHORIZED");
    await expect(page.getByRole("button", { name: "Prøv å sende igjen" })).toHaveCount(0);
  });

  test("Viser feilmelding fra PEN i kvittering", async ({ page }) => {
    await page.route(AUTOBREV_URL, (route) =>
      route.fulfill({
        status: 422,
        json: { tittel: "Adresse ikke registrert", melding: "Brukeren mangler adresse." },
      }),
    );

    await velgAutobrevOgBestill(page);

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
