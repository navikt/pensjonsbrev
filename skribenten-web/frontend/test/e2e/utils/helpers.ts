import { type Page } from "@playwright/test";

/**
 * Sets up common API route stubs for a sak.
 */
export async function setupSakStubs(page: Page) {
  await page.route("**/bff/skribenten-backend/sak/**", (route) => {
    // Default 404 for any sak route not explicitly handled below
    if (
      !route
        .request()
        .url()
        .match(/sak\/123456/)
    ) {
      return route.fulfill({ status: 404 });
    }
    return route.fallback();
  });

  await page.route("**/bff/skribenten-backend/sak/123456", (route) => {
    if (route.request().url().includes("?vedtaksId=")) {
      return route.fulfill({ path: "test/e2e/fixtures/sak.json", contentType: "application/json" });
    }
    return route.fulfill({ path: "test/e2e/fixtures/sak.json", contentType: "application/json" });
  });

  await page.route("**/bff/skribenten-backend/sak/123456?vedtaksId=*", (route) => {
    return route.fulfill({ path: "test/e2e/fixtures/sak.json", contentType: "application/json" });
  });

  await page.route("**/bff/skribenten-backend/sak/123456/brev", (route) => {
    if (route.request().method() === "GET") {
      return route.fulfill({ json: [] });
    }
    return route.fallback();
  });

  await page.route("**/bff/skribenten-backend/sak/123456/adresse", (route) => {
    return route.fulfill({ path: "test/e2e/fixtures/adresse.json", contentType: "application/json" });
  });

  await page.route("**/bff/skribenten-backend/kodeverk/avtaleland", (route) => {
    return route.fulfill({ path: "test/e2e/fixtures/avtaleland.json", contentType: "application/json" });
  });

  await page.route("**/bff/skribenten-backend/me/enheter", (route) => {
    return route.fulfill({ path: "test/e2e/fixtures/enheter.json", contentType: "application/json" });
  });

  await page.route("**/bff/skribenten-backend/land", (route) => {
    return route.fulfill({ json: [] });
  });

  await page.route("**/bff/skribenten-backend/sak/**/brukerstatus", (route) => {
    return route.fulfill({
      json: { adressebeskyttelse: false, doedsfall: "2025-01-01", erSkjermet: false, vergemaal: false },
    });
  });

  await page.route("**/bff/skribenten-backend/sak/123456/foretrukketSpraak", (route) => {
    return route.fulfill({ path: "test/e2e/fixtures/foretrukketSpraak.json", contentType: "application/json" });
  });

  await page.route("**/bff/api/userInfo", (route) => {
    return route.fulfill({
      json: { id: "Z990297", navn: "F_Z990297 E_Z990297", rolle: "Saksbehandler" },
    });
  });

  await page.route("**/bff/api/logg", (route) => {
    return route.fulfill({ status: 200 });
  });

  await page.route("**/bff/skribenten-backend/sak/123456/brev/*/alltidValgbareVedlegg", (route) => {
    return route.fulfill({ json: [] });
  });

  await page.route("**/bff/skribenten-backend/sak/123456/brev/*", (route) => {
    if (route.request().method() === "GET") {
      return route.fulfill({ json: {} });
    }
    return route.fallback();
  });

  await page.route("**/bff/skribenten-backend/sak/123456/brev/*/pdf", (route) => {
    return route.fulfill({ path: "test/e2e/fixtures/helloWorldPdf.txt", contentType: "application/json" });
  });

  await page.route("**/bff/skribenten-backend/me/favourites", (route) => {
    return route.fulfill({ json: [] });
  });

  await page.route("**/bff/skribenten-backend/brevmal/*/modelSpecification", (route) => {
    return route.fulfill({ json: { types: {}, letterModelTypeName: null } });
  });

  await page.route("**/bff/skribenten-backend/brevmal", (route) => {
    return route.fulfill({ path: "test/e2e/fixtures/brevmetadata.json", contentType: "application/json" });
  });

  await page.route("**/bff/skribenten-backend/brev/1/reservasjon", (route) => {
    return route.fulfill({ path: "test/e2e/fixtures/brevreservasjon.json", contentType: "application/json" });
  });
}
