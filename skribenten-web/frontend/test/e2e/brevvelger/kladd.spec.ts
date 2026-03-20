import { expect, test } from "@playwright/test";

import { nyBrevInfo, nyBrevResponse } from "../../utils/brevredigeringTestUtils";
import { setupSakStubs } from "../utils/helpers";

test.describe("Kladd", () => {
  test.beforeEach(async ({ page }) => {
    await setupSakStubs(page);
  });

  test("Viser kladder i brevvelgeren", async ({ page }) => {
    await page.route("**/bff/skribenten-backend/sak/123456/brev", (route) => {
      if (route.request().method() === "GET") {
        return route.fulfill({
          json: [
            {
              id: 1,
              opprettetAv: { id: "Z990297", navn: "F_Z990297 E_Z990297" },
              opprettet: "2024-09-17T08:36:09.785Z",
              sistredigertAv: { id: "Z990297", navn: "F_Z990297 E_Z990297" },
              sistredigert: "2024-09-17T08:38:48.503Z",
              brevkode: "INFORMASJON_OM_SAKSBEHANDLINGSTID",
              brevtittel: "Informasjon om saksbehandlingstid",
              status: { type: "Kladd" },
              distribusjonstype: "SENTRALPRINT",
              mottaker: null,
              avsenderEnhet: {
                enhetNr: "0001",
                navn: "NAV Familie- og pensjonsytelser",
              },
              spraak: "EN",
            },
          ],
        });
      }
      return route.fallback();
    });

    const brevListResponsePromise = page.waitForResponse(
      (r) => r.url().includes("/sak/123456/brev") && !r.url().includes("/pdf") && r.request().method() === "GET",
    );
    await page.goto("/saksnummer/123456/brevvelger");
    await brevListResponsePromise;

    await page.getByRole("button", { name: /Informasjon om saksbehandlingstid.*Opprettet/ }).click();
    await expect(page.getByText("Mottaker", { exact: true })).toBeVisible();
    await expect(page.getByText("Avsenderenhet")).toBeVisible();
    await expect(page.getByText("Språk")).toBeVisible();
    await expect(page.getByText("Gå til brevbehandler")).toBeVisible();
    await expect(page.getByText("Fortsett")).toBeVisible();
  });

  test("Kan slette kladd", async ({ page }) => {
    let requestCount = 0;
    await page.route("**/bff/skribenten-backend/sak/123456/brev", (route) => {
      if (route.request().method() === "GET") {
        if (requestCount < 2) {
          requestCount++;
          return route.fulfill({
            json: [
              {
                id: 1,
                opprettetAv: { id: "Z990297", navn: "F_Z990297 E_Z990297" },
                opprettet: "2024-09-17T08:36:09.785Z",
                sistredigertAv: { id: "Z990297", navn: "F_Z990297 E_Z990297" },
                sistredigert: "2024-09-17T08:38:48.503Z",
                brevkode: "INFORMASJON_OM_SAKSBEHANDLINGSTID",
                brevtittel: "Informasjon om saksbehandlingstid",
                status: { type: "Kladd" },
                distribusjonstype: "SENTRALPRINT",
                mottaker: null,
                avsenderEnhet: {
                  enhetNr: "0001",
                  navn: "NAV Familie- og pensjonsytelser",
                },
                spraak: "EN",
              },
            ],
          });
        }
        return route.fulfill({ json: [] });
      }
      return route.fallback();
    });

    await page.route("**/bff/skribenten-backend/sak/123456/brev/1", (route) => {
      if (route.request().method() === "DELETE") {
        return route.fulfill({ status: 204, body: "" });
      }
      return route.fallback();
    });

    const brevListResponsePromise = page.waitForResponse(
      (r) => r.url().includes("/sak/123456/brev") && !r.url().includes("/pdf") && r.request().method() === "GET",
    );
    await page.goto("/saksnummer/123456/brevvelger");
    await brevListResponsePromise;

    await page.getByRole("button", { name: /Informasjon om saksbehandlingstid.*Opprettet/ }).click();
    await page.getByText("Slett kladd").click();
    await expect(page.getByText("Vil du slette kladden?")).toBeVisible();
    await expect(page.getByText("Kladden vil bli slettet, og du kan ikke angre denne handlingen.")).toBeVisible();
    await expect(page.getByText("Nei, behold kladden")).toBeVisible();
    await expect(page.getByText("Ja, slett kladden")).toBeVisible();

    const deletePromise = page.waitForResponse(
      (r) => r.url().includes("/sak/123456/brev/1") && r.request().method() === "DELETE",
    );
    await page.getByText("Ja, slett kladden").click();
    await deletePromise;

    await expect(page.getByText("Informasjon om saksbehandlingstid")).not.toBeVisible();
  });

  test("bruker eksisterende kladd", async ({ page }) => {
    await page.route("**/bff/skribenten-backend/sak/123456/brev", (route) => {
      if (route.request().method() === "GET") {
        return route.fulfill({
          json: [
            nyBrevInfo({ id: 1, opprettet: "2024-09-17T08:36:09.785Z" }),
            nyBrevInfo({ id: 2, opprettet: "2024-10-17T08:36:09.785Z" }),
          ],
        });
      }
      return route.fallback();
    });

    await page.route(
      "**/bff/skribenten-backend/brevmal/INFORMASJON_OM_SAKSBEHANDLINGSTID/modelSpecification",
      (route) => {
        return route.fulfill({ path: "test/e2e/fixtures/modelSpecification.json", contentType: "application/json" });
      },
    );

    const brevListResponsePromise = page.waitForResponse(
      (r) => r.url().includes("/sak/123456/brev") && !r.url().includes("/pdf") && r.request().method() === "GET",
    );
    await page.goto("/saksnummer/123456/brevvelger");
    await brevListResponsePromise;

    await page.getByText("Informasjonsbrev").click();
    await page.locator("p").filter({ hasText: "Informasjon om saksbehandlingstid" }).nth(2).click();
    await page.locator("select[name=enhetsId]").selectOption({ label: "Nav Arbeid og ytelser Innlandet" });
    await expect(page.locator("select[name=spraak]")).toHaveValue("NB");

    await page.getByLabel("Mottatt søknad").click();
    await page.locator(":focus").pressSequentially("09.10.2024");
    await page.getByLabel("Ytelse").click();
    await page.locator(":focus").pressSequentially("Alderspensjon");
    await page.getByLabel("Svartid uker").click();
    await page.locator(":focus").pressSequentially("4");
    await page.getByText("Fortsett").click();

    await expect(page.getByText("Vil du bruke eksisterende kladd?")).toBeVisible();
    await expect(page.getByText("Du har en eksisterende kladd basert på samme brevmal.")).toBeVisible();
    await expect(page.getByText("Nei, lag nytt brev")).toBeVisible();
    await page.getByText("Ja, bruk eksisterende kladd").click();
    await expect(page).toHaveURL(/\/saksnummer\/123456\/brev\/2/);
  });

  test("lager nytt brev selv om saken har eksisterende kladd", async ({ page }) => {
    await page.route("**/bff/skribenten-backend/sak/123456/brev", async (route) => {
      if (route.request().method() === "GET") {
        return route.fulfill({
          json: [nyBrevInfo({ id: 1, opprettet: "2024-09-17T08:36:09.785Z" })],
        });
      }
      if (route.request().method() === "POST") {
        const body = route.request().postDataJSON();
        expect(body).toEqual({
          brevkode: "INFORMASJON_OM_SAKSBEHANDLINGSTID",
          spraak: "NB",
          avsenderEnhetsId: "4405",
          mottaker: null,
          saksbehandlerValg: {
            mottattSoeknad: "2024-10-09",
            ytelse: "Alderspensjon",
            svartidUker: "4",
          },
          vedtaksId: null,
        });
        return route.fulfill({
          json: nyBrevResponse({
            info: nyBrevInfo({
              id: 2,
              avsenderEnhet: {
                enhetNr: "4405",
                navn: "Nav Arbeid og ytelser Innlandet",
              },
            }),
          }),
          contentType: "application/json",
        });
      }
      return route.fallback();
    });

    await page.route(
      "**/bff/skribenten-backend/brevmal/INFORMASJON_OM_SAKSBEHANDLINGSTID/modelSpecification",
      (route) => {
        return route.fulfill({ path: "test/e2e/fixtures/modelSpecification.json", contentType: "application/json" });
      },
    );

    const brevListResponsePromise = page.waitForResponse(
      (r) => r.url().includes("/sak/123456/brev") && !r.url().includes("/pdf") && r.request().method() === "GET",
    );
    await page.goto("/saksnummer/123456/brevvelger");
    await brevListResponsePromise;

    await page.getByText("Informasjonsbrev").click();
    await page.locator("p").filter({ hasText: "Informasjon om saksbehandlingstid" }).nth(1).click();
    await page.locator("select[name=enhetsId]").selectOption({ label: "Nav Arbeid og ytelser Innlandet" });
    await expect(page.locator("select[name=spraak]")).toHaveValue("NB");

    await page.getByLabel("Mottatt søknad").click();
    await page.locator(":focus").pressSequentially("09.10.2024");
    await page.getByLabel("Ytelse").click();
    await page.locator(":focus").pressSequentially("Alderspensjon");
    await page.getByLabel("Svartid uker").click();
    await page.locator(":focus").pressSequentially("4");
    await page.getByText("Fortsett").click();

    await expect(page.getByText("Vil du bruke eksisterende kladd?")).toBeVisible();
    await expect(page.getByText("Du har en eksisterende kladd basert på samme brevmal.")).toBeVisible();
    await page.getByText("Nei, lag nytt brev").click();
    await expect(page).toHaveURL(/\/saksnummer\/123456\/brev\/2/);
    expect(page.url()).toContain("enhetsId");
  });

  test("arkiverte brev i brevvelger skal ikke kunne gjøre endringer på brev, og kun navigere videre til brevbehandler", async ({
    page,
  }) => {
    await page.route("**/bff/skribenten-backend/sak/123456/brev", (route) => {
      if (route.request().method() === "GET") {
        return route.fulfill({
          json: [
            nyBrevInfo({
              id: 1,
              opprettet: "2024-09-17T08:36:09.785Z",
              status: { type: "Arkivert" },
              journalpostId: 123_456,
            }),
          ],
        });
      }
      return route.fallback();
    });

    const brevListResponsePromise = page.waitForResponse(
      (r) => r.url().includes("/sak/123456/brev") && !r.url().includes("/pdf") && r.request().method() === "GET",
    );
    await page.goto("/saksnummer/123456/brevvelger?brevId=1");
    await brevListResponsePromise;
    await expect(page.getByText("Endre mottaker")).not.toBeVisible();
    await expect(page.getByText("Slett brev")).not.toBeVisible();
    await page.getByText("Fortsett").click();
    await expect(page).toHaveURL(/\/saksnummer\/123456\/brevbehandler/);
    expect(page.url()).toContain("brevId=1");
  });
});
