import { expect, type Page, test } from "@playwright/test";

import { AUTOSAVE_TIMER } from "~/components/ManagedLetterEditor/autosave_timer";
import { type BrevResponse } from "~/types/brev";

import { nyBrevInfo, nyBrevResponse } from "../../utils/brevredigeringTestUtils";
import brev from "../fixtures/bekreftelsePåFlyktningstatus/brev.json" with { type: "json" };
import { setupSakStubs } from "../utils/helpers";

const defaultBrev = nyBrevResponse({});
const bekreftelsePaaFlyktningstatusBrev = brev as unknown as BrevResponse;
const vedtaksBrev = nyBrevResponse({
  ...bekreftelsePaaFlyktningstatusBrev,
  info: { ...bekreftelsePaaFlyktningstatusBrev.info, brevtype: "VEDTAKSBREV" },
});

const openBrevCard = async (page: Page, title: string) => {
  const card = page.locator(`[aria-label="${title}"]`).first();
  await expect(card).toBeVisible();
  await card.getByRole("button").click();
};

test.describe("attestering", () => {
  test.beforeEach(async ({ page }) => {
    await setupSakStubs(page);
  });

  test("sender et brev til attestering", async ({ page }) => {
    // brev redigering
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1?reserver=true", (route) => {
      if (route.request().method() === "GET") {
        return route.fulfill({ json: vedtaksBrev });
      }
      return route.fallback();
    });

    let sistLagraRedigertBrev = "";
    await page.route("**/bff/skribenten-backend/brev/1/redigertBrev?frigiReservasjon=false", async (route) => {
      if (route.request().method() === "PUT") {
        const body = route.request().postDataJSON();
        sistLagraRedigertBrev = JSON.stringify(body);
        return route.fulfill({ json: { ...vedtaksBrev, redigertBrev: body } });
      }
      return route.fallback();
    });

    let lagreBrevCount = 0;
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1?frigiReservasjon=true", async (route) => {
      if (route.request().method() === "PUT") {
        lagreBrevCount++;
        const body = route.request().postDataJSON();
        return route.fulfill({
          json: {
            ...vedtaksBrev,
            saksbehandlerValg: body.saksbehandlerValg,
            redigertBrev: body.redigertBrev,
            info: nyBrevInfo({ ...body.info, status: { type: "Kladd" } }),
          },
        });
      }
      return route.fallback();
    });

    const reserverResponsePromise = page.waitForResponse(
      (r) => r.url().includes("/brev/1?reserver=true") && r.status() === 200,
    );
    await page.goto("/saksnummer/123456/brev/1");
    await reserverResponsePromise;

    await page.getByText("Underskrift").click();
    await page.keyboard.press("Control+A");
    await page.keyboard.press("Backspace");
    await page.locator(":focus").pressSequentially("Dette er en signatur");

    await page.clock.install();
    await page.locator("span[contenteditable='true']", { hasText: "ankomst til Norge." }).focus();
    await page.locator(":focus").press("End");
    await page.locator(":focus").press("Enter");
    await page.locator(":focus").pressSequentially("Dette er en ny tekstblokk", { delay: 20 });
    const saveResponsePromise = page.waitForResponse(
      (r) => r.url().includes("/redigertBrev?frigiReservasjon=false") && r.request().method() === "PUT",
    );
    await page.clock.fastForward(AUTOSAVE_TIMER);
    await saveResponsePromise;
    await page.clock.resume();

    await expect(page.locator("div").filter({ hasText: "Dette er en signatur" }).first()).toBeVisible();
    expect(sistLagraRedigertBrev).toContain("Dette er en signatur");
    expect(lagreBrevCount).toBe(0);

    // brevbehandler
    const brevEtterLaas = nyBrevInfo({
      ...vedtaksBrev.info,
      status: { type: "Attestering" },
    });
    let brevErLaast = false;
    await page.route("**/bff/skribenten-backend/sak/123456/brev", async (route) => {
      if (route.request().method() === "GET") {
        if (brevErLaast) {
          return route.fulfill({ json: [brevEtterLaas] });
        } else {
          return route.fulfill({ json: [vedtaksBrev.info] });
        }
      }
      return route.fallback();
    });

    await page.route("**/bff/skribenten-backend/sak/123456/brev/1/status", async (route) => {
      if (route.request().method() === "PUT") {
        const body = route.request().postDataJSON();
        expect(body).toEqual({ klar: true });
        brevErLaast = true;
        return route.fulfill({ json: brevEtterLaas });
      }
      return route.fallback();
    });

    const lagreBrevResponsePromise = page.waitForResponse(
      (r) => r.url().includes("/brev/1?frigiReservasjon=true") && r.request().method() === "PUT",
    );
    await page.getByText("Fortsett").click();
    await lagreBrevResponsePromise;
    expect(lagreBrevCount).toBe(1);

    const statusResponsePromise = page.waitForResponse(
      (r) => r.url().includes("/brev/1/status") && r.request().method() === "PUT",
    );
    await page.getByText("Brevet er klart for attestering").click();
    await statusResponsePromise;
    await expect(page.getByText("Klar for attestering")).toBeVisible();
  });

  test("kan attestere, forhåndsvise og sende brev", async ({ page }) => {
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1/attestering?reserver=true", (route) => {
      if (route.request().method() === "GET") {
        return route.fulfill({ json: defaultBrev });
      }
      return route.fallback();
    });

    await page.goto("/saksnummer/123456/attester/1/redigering");
    await expect(page.getByText("Underskrift")).toBeVisible();
    await page.clock.install();

    await page.route("**/bff/skribenten-backend/sak/123456/brev/1/pdf/send", (route) => {
      if (route.request().method() === "POST") {
        return route.fulfill({
          json: { journalpostId: 9908, error: null },
        });
      }
      return route.fallback();
    });

    await page.route("**/bff/skribenten-backend/brev/1/redigertBrev?frigiReservasjon=*", async (route) => {
      if (route.request().method() === "PUT") {
        const body = route.request().postDataJSON();
        return route.fulfill({ json: { ...defaultBrev, redigertBrev: body } });
      }
      return route.fallback();
    });

    let attesterCount = 0;
    let attesteringBody: Record<string, unknown> | null = null;
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1/attestering?frigiReservasjon=true", async (route) => {
      if (route.request().method() === "PUT") {
        attesterCount++;
        const body = route.request().postDataJSON();
        attesteringBody = body;

        return route.fulfill({
          json: {
            ...defaultBrev,
            redigertBrev: body.redigertBrev,
            saksbehandlerValg: body.saksbehandlerValg,
            info: { ...defaultBrev.info, status: { type: "Attestering" } },
          },
        });
      }
      return route.fallback();
    });

    // oppdaterer underskrift
    const signatureSavePromise = page.waitForResponse(
      (r) => r.url().includes("/redigertBrev?frigiReservasjon=") && r.request().method() === "PUT",
    );
    await page.getByRole("textbox", { name: "Underskrift" }).fill("Dette er det nye attestant navnet mitt");
    await page.clock.fastForward(AUTOSAVE_TIMER);
    await signatureSavePromise;

    // oppdaterer brevtekst
    await page.locator("span[contenteditable='true']", { hasText: "weeks." }).click();
    await page.keyboard.press("End");
    await page.keyboard.press("Enter");
    await page.keyboard.type("Dette er en ny tekstblokk", { delay: 20 });
    const textSaveResponsePromise = page.waitForResponse(
      (r) => r.url().includes("/redigertBrev?frigiReservasjon=") && r.request().method() === "PUT",
    );
    await page.clock.fastForward(AUTOSAVE_TIMER);
    await textSaveResponsePromise;
    await page.clock.resume();

    // underskrift og brevtekst er oppdatert
    await expect(
      page.locator("div").filter({ hasText: "Dette er det nye attestant navnet mitt" }).first(),
    ).toBeVisible();
    await expect(page.getByText("Dette er en ny tekstblokk")).toBeVisible();

    // attesterer
    expect(attesterCount).toBe(0);
    const attesteringResponsePromise = page.waitForResponse(
      (r) => r.url().includes("/attestering?frigiReservasjon=true") && r.request().method() === "PUT",
    );
    await page.getByText("Fortsett").click();
    await attesteringResponsePromise;
    expect(attesterCount).toBe(1);
    expect(attesteringBody).not.toBeNull();
    // biome-ignore lint/suspicious/noExplicitAny: asserting on raw route handler body
    const body = attesteringBody as any;
    expect(body.redigertBrev.signatur.attesterendeSaksbehandlerNavn).toBe("Dette er det nye attestant navnet mitt");
    expect(body.redigertBrev.blocks.length).toBeGreaterThan(0);
    expect(body.redigertBrev.blocks.at(-1).content.at(-1).editedText.trim()).toContain("Dette er en ny tekstblokk");
    await expect(page).toHaveURL(/\/saksnummer\/123456\/attester\/1\/forhandsvisning\?enhetsId=0001/);

    // ------ Forhåndsvisning ------
    await expect(page.getByText("Informasjon om saksbehandlingstid")).toBeVisible();
    await expect(page.getByText("Mottaker", { exact: true })).toBeVisible();
    await expect(page.getByText("Tydelig Bakke")).toBeVisible();
    await expect(page.getByText("Mauråsveien 29")).toBeVisible();
    await expect(page.getByText("4844 Arendal")).toBeVisible();
    await expect(page.getByText("Distribusjonstype")).toBeVisible();
    await expect(page.getByText("Sentral print")).toBeVisible();

    await page.getByText("Send brev").click();
    await expect(page.getByText("Vil du sende brevet?")).toBeVisible();
    await expect(page.getByText("Du kan ikke angre denne handlingen.")).toBeVisible();
    const pdfResponsePromise = page.waitForResponse(
      (r) => r.url().includes("/pdf/send") && r.request().method() === "POST",
    );
    await page.getByText("Ja, send brev").click();
    const pdfResponse = await pdfResponsePromise;
    expect(pdfResponse.status()).toBe(200);

    // ------ Kvittering ------
    await expect(page).toHaveURL(/\/saksnummer\/123456\/attester\/1\/kvittering\?enhetsId=0001/);
    await expect(page.getByText("Sendt til mottaker")).toBeVisible();
    await expect(page.getByText("Informasjon om saksbehandlingstid")).toBeVisible();
    await openBrevCard(page, "Informasjon om saksbehandlingstid");
    await expect(page.getByText("Mottaker", { exact: true })).toBeVisible();
    await expect(page.getByText("Tydelig Bakke")).toBeVisible();
    await expect(page.getByText("Distribusjon")).toBeVisible();
    await expect(page.getByText("Sentral print")).toBeVisible();
    await expect(page.getByText("Journalpost")).toBeVisible();
    await expect(page.getByText("9908")).toBeVisible();
  });

  test("kan slette brev til attestering", async ({ page }) => {
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1/attestering?reserver=true", (route) => {
      if (route.request().method() === "GET") {
        return route.fulfill({ json: defaultBrev });
      }
      return route.fallback();
    });

    await page.goto("/saksnummer/123456/attester/1/redigering");
    await expect(page.getByText("Underskrift")).toBeVisible();

    const pdfBase64 = (await import("node:fs")).readFileSync("test/e2e/fixtures/helloWorldPdf.txt", "base64");
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1/pdf", (route) => {
      if (route.request().method() === "GET") {
        return route.fulfill({
          json: { pdf: pdfBase64, rendretBrevErEndret: false },
        });
      }
      return route.fallback();
    });

    await page.route("**/bff/skribenten-backend/brev/1/redigertBrev?frigiReservasjon=*", async (route) => {
      if (route.request().method() === "PUT") {
        const body = route.request().postDataJSON();
        return route.fulfill({ json: { ...defaultBrev, redigertBrev: body } });
      }
      return route.fallback();
    });

    await page.route("**/bff/skribenten-backend/sak/123456/brev/1/attestering?frigiReservasjon=true", async (route) => {
      if (route.request().method() === "PUT") {
        const body = route.request().postDataJSON();
        return route.fulfill({
          json: {
            ...defaultBrev,
            redigertBrev: body.redigertBrev,
            saksbehandlerValg: body.saksbehandlerValg,
            info: { ...defaultBrev.info, status: { type: "Attestering" } },
          },
        });
      }
      return route.fallback();
    });

    // fyller inn underskrift for attestant
    await page.getByText("Underskrift").click();
    await page.locator(":focus").pressSequentially("Attestant Navn");

    // attesterer
    let attesterCount = 0;
    page.on("response", (response) => {
      if (response.url().includes("/attestering?frigiReservasjon=true") && response.request().method() === "PUT") {
        attesterCount++;
      }
    });
    expect(attesterCount).toBe(0);
    const pdfResponsePromise = page.waitForResponse(
      (r) => r.url().includes("/brev/1/pdf") && r.request().method() === "GET",
    );
    const attesteringResponsePromise = page.waitForResponse(
      (r) => r.url().includes("/attestering?frigiReservasjon=true") && r.request().method() === "PUT",
    );
    await page.getByText("Fortsett").click();
    await attesteringResponsePromise;
    await expect(page).toHaveURL(/\/saksnummer\/123456\/attester\/1\/forhandsvisning\?enhetsId=0001/);

    // ------ Forhåndsvisning ------
    await expect(page.getByText("Informasjon om saksbehandlingstid")).toBeVisible();
    await expect(page.getByText("Mottaker", { exact: true })).toBeVisible();
    await expect(page.getByText("Tydelig Bakke")).toBeVisible();
    await expect(page.getByText("Mauråsveien 29")).toBeVisible();
    await expect(page.getByText("4844 Arendal")).toBeVisible();
    await expect(page.getByText("Distribusjonstype")).toBeVisible();
    await expect(page.getByText("Sentral print")).toBeVisible();

    const pdfResponse = await pdfResponsePromise;
    expect(pdfResponse.status()).toBe(200);

    // Slett med "Nei, behold brev" først
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1", async (route) => {
      if (route.request().method() === "DELETE") {
        return route.fulfill({ status: 404 });
      }
      return route.fallback();
    });

    await page.getByRole("button", { name: /Slett/ }).click();
    await expect(page.getByText("Vil du slette brevet?")).toBeVisible();
    await expect(page.getByText("Brevet vil bli slettet, og du kan ikke angre denne handlingen.")).toBeVisible();

    await page.getByText("Nei, behold brev").click();
    await expect(page.getByText("Brevet vil bli slettet, og du kan ikke angre denne handlingen.")).not.toBeVisible();

    await page.getByRole("button", { name: /Slett/ }).click();
    await expect(page.getByText("Brevet vil bli slettet, og du kan ikke angre denne handlingen.")).toBeVisible();

    const slettBrev1ResponsePromise = page.waitForResponse(
      (r) => r.url().includes("/sak/123456/brev/1") && r.request().method() === "DELETE",
    );
    await page.getByText("Ja, slett brev").click();
    const slettBrev1Response = await slettBrev1ResponsePromise;
    expect(slettBrev1Response.status()).toBe(404);
    await expect(page.getByText("Kunne ikke slette brev 1. Vil du prøve igjen?")).toBeVisible();

    // Override delete route to succeed
    await page.unrouteAll({ behavior: "ignoreErrors" });
    await setupSakStubs(page);
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1/attestering?reserver=true", (route) => {
      if (route.request().method() === "GET") {
        return route.fulfill({ json: defaultBrev });
      }
      return route.fallback();
    });
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1", async (route) => {
      if (route.request().method() === "DELETE") {
        return route.fulfill({ status: 204 });
      }
      return route.fallback();
    });

    const slettBrev2ResponsePromise = page.waitForResponse(
      (r) => r.url().includes("/sak/123456/brev/1") && r.request().method() === "DELETE",
    );
    await page.getByText("Ja, slett brev").click();
    const slettBrev2Response = await slettBrev2ResponsePromise;
    expect(slettBrev2Response.status()).toBe(204);

    await page.getByRole("button", { name: "Gå til brevbehandler" }).click();
    await expect(page).toHaveURL(/\/saksnummer\/123456\/brevbehandler\?enhetsId=0001/);
  });
});
