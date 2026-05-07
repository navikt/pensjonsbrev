import { readFileSync } from "node:fs";

import { expect, type Page, test } from "@playwright/test";

import { nyBrevResponse } from "../../utils/brevredigeringTestUtils";
import p1Data from "../fixtures/p1Data.json" with { type: "json" };
import { setupSakStubs } from "../utils/helpers";

const { p1BrevInfo, p1BrevData, p1BrevDataWithMissingFields, countriesSubset } = p1Data;

// Hjelpere
const openP1Modal = async (page: Page) => {
  const card = page.locator(`[aria-label="${p1BrevInfo.brevtittel}"]`).first();
  await expect(card).toBeVisible();
  await card.getByRole("button").click();
  await page.getByTestId("p1-edit-button").click();
  await expect(page.getByTestId("p1-edit-modal")).toBeVisible();
  await expect(page.getByRole("heading", { name: "3. Innvilget pensjon" })).toBeVisible();
};

test.describe("P1 med forsidebrev", () => {
  test.beforeEach(async ({ page }) => {
    await setupSakStubs(page);

    const pdfBase64 = readFileSync("test/e2e/fixtures/helloWorldPdf.txt", "base64");
    await page.route("**/bff/skribenten-backend/sak/123456/brev/*/pdf", (route) => {
      if (route.request().method() === "GET") {
        return route.fulfill({
          json: { pdf: pdfBase64, rendretBrevErEndret: false },
        });
      }
      return route.fallback();
    });

    await page.route("**/bff/skribenten-backend/sak/123456/brev/1?reserver=true", (route) => {
      if (route.request().method() === "GET") {
        // @ts-expect-error: JSON fixture data might miss optional complex types but works for runtime
        return route.fulfill({ json: nyBrevResponse({ info: p1BrevInfo }) });
      }
      return route.fallback();
    });
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1?reserver=false", (route) => {
      if (route.request().method() === "GET") {
        // @ts-expect-error: JSON fixture data might miss optional complex types but works for runtime
        return route.fulfill({ json: nyBrevResponse({ info: p1BrevInfo }) });
      }
      return route.fallback();
    });
    await page.route("**/bff/skribenten-backend/sak/123456/brev", (route) => {
      if (route.request().method() === "GET") {
        return route.fulfill({ json: [p1BrevInfo] });
      }
      return route.fallback();
    });
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1/p1", (route) => {
      if (route.request().method() === "GET") {
        return route.fulfill({ json: p1BrevData });
      }
      return route.fallback();
    });
    await page.route("**/bff/skribenten-backend/landForP1", (route) => {
      return route.fulfill({ json: countriesSubset });
    });
    await page.route("**/bff/skribenten-backend/sak/123456/brev/*/alltidValgbareVedlegg", (route) => {
      if (route.request().method() === "GET") {
        return route.fulfill({ json: [] });
      }
      return route.fallback();
    });

    await page.goto("/saksnummer/123456/brevbehandler");
  });

  test("viser og lagrer data i henholdsvis visningformat og api format", async ({ page }) => {
    const p1ResponsePromise = page.waitForResponse(
      (r) => r.url().includes("/brev/1/p1") && r.request().method() === "GET",
    );
    const landResponsePromise = page.waitForResponse((r) => r.url().includes("/landForP1"));
    await openP1Modal(page);
    await p1ResponsePromise;
    await landResponsePromise;
    await expect(page.getByRole("heading", { name: "3. Innvilget pensjon" })).toBeVisible();

    // verifiser at land vises med fullt norsk navn, mens to-bokstavskode brukes fra/til backend
    // verifiser at dato vises som dd.MM.yyyy, mens formatet yyyy-MM-dd brukes fra/til backend
    await expect(page.getByText("Bulgaria").first()).toBeVisible();
    await page.getByTestId("land-0").clear();
    await page.getByTestId("land-0").pressSequentially("Frankrike");
    await page.getByTestId("land-0").press("Enter");

    await expect(page.getByTestId("innvilget-0-datoForVedtak")).toHaveValue("23.09.2022");
    await page.getByTestId("innvilget-0-datoForVedtak").clear();
    await page.getByTestId("innvilget-0-datoForVedtak").pressSequentially("01.01.2025");

    await page.route("**/bff/skribenten-backend/sak/123456/brev/1/p1", async (route) => {
      if (route.request().method() === "POST") {
        const body = route.request().postDataJSON();
        expect(body.innvilgedePensjoner[0].institusjon.land).toBe("FR");
        expect(body.innvilgedePensjoner[0].institusjon.datoForVedtak).toBe("2025-01-01");
        return route.fulfill({ status: 200, body: "200" });
      }
      return route.fallback();
    });
    await page.getByRole("button", { name: "Lagre og lukk" }).click();
  });

  test("viser valideringsfeil når påkrevde felt mangler", async ({ page }) => {
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1/p1", (route) => {
      if (route.request().method() === "GET") {
        return route.fulfill({ json: p1BrevDataWithMissingFields });
      }
      return route.fallback();
    });
    await openP1Modal(page);
    await expect(page.getByRole("heading", { name: "3. Innvilget pensjon" })).toBeVisible();
    // Gjør skjemaet dirty ved å endre et felt
    await page.getByTestId("innvilget-0-datoForVedtak").clear();
    await page.getByTestId("innvilget-0-datoForVedtak").pressSequentially("01.01.2025");
    // Forsøk å lagre uten å fylle ut påkrevde felt
    await page.getByRole("button", { name: "Lagre og lukk" }).click();

    // Verifiser at individuelle feilmeldinger vises
    await expect(page.getByText("Institusjonsnavn er obligatorisk")).toBeVisible();
    await expect(page.getByText("Pensjonstype må velges")).toBeVisible();
  });

  test("viser valideringsfeil for ugyldig datoformat", async ({ page }) => {
    await openP1Modal(page);
    await expect(page.getByRole("heading", { name: "3. Innvilget pensjon" })).toBeVisible();
    // Skriv inn ugyldig dato
    await page.getByTestId("innvilget-0-datoForVedtak").clear();
    await page.getByTestId("innvilget-0-datoForVedtak").pressSequentially("32.13.2022");
    await page.getByRole("button", { name: "Lagre og lukk" }).click();
    await expect(page.getByText("Skjemaet har feil som må rettes")).toBeVisible();
    await expect(page.getByText("Ugyldig dato")).toBeVisible();
  });

  test("viser valideringsfeil når tekstfelt overskrider makslengde", async ({ page }) => {
    await openP1Modal(page);
    await expect(page.getByRole("heading", { name: "3. Innvilget pensjon" })).toBeVisible();
    const veryLongText = "A".repeat(300);
    // Bruker data-testid for å finne PIN feltet
    await page.getByTestId("innvilget-0-pin").clear();
    await page.getByTestId("innvilget-0-pin").pressSequentially(veryLongText, { delay: 0 });
    await page.getByTestId("innvilget-0-pin").press("Tab");

    await expect(page.getByText("PIN kan ikke være lengre enn")).toBeVisible();
    await expect(page.getByText("Skjemaet har feil som må rettes")).not.toBeVisible();
    await page.getByRole("button", { name: "Lagre og lukk" }).click();
    await expect(page.getByText("Skjemaet har feil som må rettes")).toBeVisible();
  });

  test("navigerer til fane med første feil ved validering", async ({ page }) => {
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1/p1", (route) => {
      if (route.request().method() === "GET") {
        return route.fulfill({ json: p1BrevDataWithMissingFields });
      }
      return route.fallback();
    });
    await openP1Modal(page);
    await expect(page.getByRole("heading", { name: "3. Innvilget pensjon" })).toBeVisible();
    await page.getByTestId("innvilget-0-datoForVedtak").pressSequentially("01.01.2025");
    await page.getByRole("tab", { name: /Avslag på pensjon/ }).click();
    await expect(page.getByText("Institusjon som har avslått")).toBeVisible();
    // Forsøk å lagre med feil i fane 3 (Innvilget)
    await page.getByRole("button", { name: "Lagre og lukk" }).click();
    // Verifiser at vi blir navigert tilbake til fane 3
    await expect(page.getByText("Institusjon som gir pensjonen")).toBeVisible();
    await expect(page.getByText("Institusjonsnavn er obligatorisk")).toBeVisible();
  });

  test("viser suksessmelding ved vellykket lagring", async ({ page }) => {
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1/p1", async (route) => {
      if (route.request().method() === "POST") {
        return route.fulfill({ status: 200, body: "200" });
      }
      return route.fallback();
    });
    await openP1Modal(page);
    await expect(page.getByRole("heading", { name: "3. Innvilget pensjon" })).toBeVisible();
    await page.getByTestId("innvilget-0-datoForVedtak").clear();
    await page.getByTestId("innvilget-0-datoForVedtak").pressSequentially("01.01.2025");
    await expect(page.getByRole("button", { name: "Lagre og lukk" })).toBeEnabled();
    await page.getByRole("button", { name: "Lagre og lukk" }).click();
    await expect(page.getByText("Endringene ble lagret")).toBeVisible();
  });

  test("håndterer sakstype UFOREP (uføretrygd) korrekt ved lagring", async ({ page }) => {
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1/p1", (route) => {
      if (route.request().method() === "GET") {
        const uforepData = structuredClone(p1BrevData);
        uforepData.sakstype = "UFOREP";
        return route.fulfill({ json: uforepData });
      }
      return route.fallback();
    });

    await openP1Modal(page);
    await expect(page.getByRole("heading", { name: "3. Innvilget pensjon" })).toBeVisible();

    // Modify field to enable save
    await page.getByTestId("innvilget-0-datoForVedtak").clear();
    await page.getByTestId("innvilget-0-datoForVedtak").pressSequentially("01.01.2025");

    await page.route("**/bff/skribenten-backend/sak/123456/brev/1/p1", async (route) => {
      if (route.request().method() === "POST") {
        const body = route.request().postDataJSON();
        expect(body.sakstype).toBe("UFOREP");
        return route.fulfill({ status: 200, body: "200" });
      }
      return route.fallback();
    });

    const postResponsePromise = page.waitForResponse(
      (r) => r.url().includes("/brev/1/p1") && r.request().method() === "POST",
    );
    await page.getByRole("button", { name: "Lagre og lukk" }).click();

    await postResponsePromise;
    await expect(page.getByText("Endringene ble lagret")).toBeVisible();
  });
});
