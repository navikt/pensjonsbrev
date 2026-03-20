import { expect, test } from "@playwright/test";

import sak from "../fixtures/sak.json" with { type: "json" };
import { dataE2E, setupSakStubs } from "../utils/helpers";

test.describe("Brevvelger spec", () => {
  test.beforeEach(async ({ page }) => {
    await setupSakStubs(page);
  });

  test("Søk med saksnummer", async ({ page }) => {
    await page.goto("/");
    await expect(page.getByRole("heading", { name: "Brevvelger" })).not.toBeVisible();
    await page.getByText("Skriv inn saksnummer").click();
    await page.locator(":focus").pressSequentially("123");
    await page.keyboard.press("Enter");
    await expect(page.getByText("Finner ikke saksnummer")).toBeVisible();
    await page.locator(":focus").pressSequentially("456");
    await page.keyboard.press("Enter");
    await expect(page.getByRole("heading", { name: "Brevvelger" })).toBeVisible();
  });

  test("Søk etter brevmal", async ({ page }) => {
    await page.goto("/saksnummer/123456/brevvelger");

    await dataE2E(page, "brevmal-search").click();
    await dataE2E(page, "brevmal-search").pressSequentially("Varsel tilbakekreving");
    await expect(dataE2E(page, "category-item")).toContainText("Feilutbetaling");
    await expect(dataE2E(page, "brevmal-button").filter({ hasText: "Varsel - tilbakekreving" })).toBeVisible();

    await page.locator(":focus").fill("!");
    await expect(dataE2E(page, "category-item")).toHaveCount(0);
    await expect(dataE2E(page, "brevmal-button")).toHaveCount(0);
    await expect(dataE2E(page, "ingen-treff-alert")).toBeVisible();

    await page.keyboard.press("Escape");
    await expect(dataE2E(page, "category-item")).toHaveCount(9);
    for (const item of await dataE2E(page, "category-item").all()) {
      await expect(item).not.toHaveClass(/aksel-accordion__item--open/);
    }
  });

  test("Favoritter", async ({ page }) => {
    await page.route("**/bff/skribenten-backend/me/favourites", (route) => {
      if (route.request().method() === "GET") {
        return route.fulfill({ json: [] });
      }
      return route.fallback();
    });

    await page.goto("/saksnummer/123456/brevvelger");
    await expect(page.locator(".aksel-accordion__item").filter({ hasText: "Favoritter" })).toHaveCount(0);

    await page.getByText("Informasjonsbrev").click();
    await expect(page.locator("p").filter({ hasText: "Informasjon om saksbehandlingstid" })).toHaveCount(1);
    await page.getByText("Informasjon om saksbehandlingstid").click();

    // Set up POST intercept to verify request body, and update GET to return 1 favourite
    let postBody: unknown;
    await page.route("**/bff/skribenten-backend/me/favourites", async (route) => {
      if (route.request().method() === "POST") {
        postBody = route.request().postData();
        return route.fulfill({ json: {} });
      }
      if (route.request().method() === "GET") {
        return route.fulfill({ json: ["INFORMASJON_OM_SAKSBEHANDLINGSTID"] });
      }
      return route.fallback();
    });

    await dataE2E(page, "add-favorite-button").click();
    expect(postBody).toBe("INFORMASJON_OM_SAKSBEHANDLINGSTID");

    await expect(page.locator(".aksel-accordion__item").filter({ hasText: "Favoritter" })).toHaveCount(1);
    // Should exist 2 elements in DOM
    await expect(page.locator("p").filter({ hasText: "Informasjon om saksbehandlingstid" })).toHaveCount(2);
    await expect(page.locator("p:visible").filter({ hasText: "Informasjon om saksbehandlingstid" })).toHaveCount(1);

    // Set up DELETE intercept, and update GET to return empty favourites
    let deleteBody: unknown;
    await page.route("**/bff/skribenten-backend/me/favourites", async (route) => {
      if (route.request().method() === "DELETE") {
        deleteBody = route.request().postData();
        return route.fulfill({ json: {} });
      }
      if (route.request().method() === "GET") {
        return route.fulfill({ json: [] });
      }
      return route.fallback();
    });

    await dataE2E(page, "remove-favorite-button").click();
    expect(deleteBody).toBe("INFORMASJON_OM_SAKSBEHANDLINGSTID");

    await expect(page.locator(".aksel-accordion__item").filter({ hasText: "Favoritter" })).toHaveCount(0);
  });

  test("Bestill Exstream brev", async ({ page }) => {
    let requestBody: Record<string, unknown> | undefined;
    await page.route("**/bff/skribenten-backend/sak/123456/bestillBrev/exstream", async (route) => {
      requestBody = route.request().postDataJSON();
      return route.fulfill({ path: "test/e2e/fixtures/bestillBrevExstream.json", contentType: "application/json" });
    });

    // Stub globalThis.open before navigation
    const _windowOpenCalls: string[] = [];
    await page.addInitScript(() => {
      globalThis.open = (...args: Parameters<typeof globalThis.open>) => {
        (globalThis as unknown as Record<string, unknown[]>).__windowOpenCalls =
          (globalThis as unknown as Record<string, unknown[]>).__windowOpenCalls || [];
        (globalThis as unknown as Record<string, unknown[]>).__windowOpenCalls.push(args[0]);
        return null;
      };
    });

    await page.goto("/saksnummer/123456/brevvelger");

    await dataE2E(page, "category-item").filter({ hasText: "Feilutbetaling" }).click();
    await dataE2E(page, "brevmal-button").filter({ hasText: "Varsel - tilbakekreving" }).click();

    await page.locator("select[name=spraak]").selectOption({ label: "Nynorsk" });
    await page.locator("select[name=enhetsId]").selectOption({ label: "Nav Arbeid og ytelser Innlandet" });

    const orderLetterResponsePromise = page.waitForResponse(
      (r) => r.url().includes("/bestillBrev/exstream") && r.request().method() === "POST",
    );
    await dataE2E(page, "order-letter").click();
    await orderLetterResponsePromise;

    expect(requestBody).toMatchObject({
      brevkode: "PE_IY_05_027",
      idTSSEkstern: null,
      spraak: "NN",
      brevtittel: "",
      enhetsId: "4405",
    });

    const calls = await page.evaluate(() => (globalThis as unknown as Record<string, unknown[]>).__windowOpenCalls);
    expect(calls).toEqual([
      "mbdok://PE2@brevklient/dokument/453864183?token=1711014877285&server=https%3A%2F%2Fwasapp-q2.adeo.no%2Fbrevweb%2F",
    ]);
    await expect(dataE2E(page, "order-letter-success-message")).toBeVisible();
  });

  test("Skriv notat", async ({ page }) => {
    let requestBody: Record<string, unknown> | undefined;
    await page.route("**/bff/skribenten-backend/sak/123456/bestillBrev/exstream", async (route) => {
      requestBody = route.request().postDataJSON();
      return route.fulfill({ path: "test/e2e/fixtures/bestillBrevNotat.json", contentType: "application/json" });
    });

    await page.addInitScript(() => {
      globalThis.open = (...args: Parameters<typeof globalThis.open>) => {
        (globalThis as unknown as Record<string, unknown[]>).__windowOpenCalls =
          (globalThis as unknown as Record<string, unknown[]>).__windowOpenCalls || [];
        (globalThis as unknown as Record<string, unknown[]>).__windowOpenCalls.push(args[0]);
        return null;
      };
    });

    await page.goto("/saksnummer/123456/brevvelger");

    await dataE2E(page, "brevmal-search").click();
    await dataE2E(page, "brevmal-search").pressSequentially("notat");
    await dataE2E(page, "brevmal-button").click();

    await expect(dataE2E(page, "språk-velger-select")).not.toBeVisible();

    await dataE2E(page, "brev-title-textfield").click();
    await dataE2E(page, "brev-title-textfield").pressSequentially("GGMU");
    await page.locator("select[name=enhetsId]").selectOption({ label: "Nav Arbeid og ytelser Innlandet" });
    const orderLetterResponsePromise = page.waitForResponse(
      (r) => r.url().includes("/bestillBrev/exstream") && r.request().method() === "POST",
    );
    await dataE2E(page, "order-letter").click();
    await orderLetterResponsePromise;

    expect(requestBody).toMatchObject({
      brevkode: "PE_IY_03_156",
      spraak: "NB",
      brevtittel: "GGMU",
      enhetsId: "4405",
    });

    const calls = await page.evaluate(() => (globalThis as unknown as Record<string, unknown[]>).__windowOpenCalls);
    expect(calls).toEqual([
      "mbdok://PE2@brevklient/dokument/453864212?token=1711023327721&server=https%3A%2F%2Fwasapp-q2.adeo.no%2Fbrevweb%2F",
    ]);
    await expect(dataE2E(page, "order-letter-success-message")).toBeVisible();
  });

  test("Bestill E-blankett", async ({ page }) => {
    let requestBody: Record<string, unknown> | undefined;
    await page.route("**/bff/skribenten-backend/sak/123456/bestillBrev/exstream/eblankett", async (route) => {
      requestBody = route.request().postDataJSON();
      return route.fulfill({ path: "test/e2e/fixtures/bestillBrevEblankett.json", contentType: "application/json" });
    });

    await page.addInitScript(() => {
      globalThis.open = (...args: Parameters<typeof globalThis.open>) => {
        (globalThis as unknown as Record<string, unknown[]>).__windowOpenCalls =
          (globalThis as unknown as Record<string, unknown[]>).__windowOpenCalls || [];
        (globalThis as unknown as Record<string, unknown[]>).__windowOpenCalls.push(args[0]);
        return null;
      };
    });

    await page.goto("/saksnummer/123456/brevvelger");

    await dataE2E(page, "brevmal-search").click();
    await dataE2E(page, "brevmal-search").pressSequentially("E 001");
    await dataE2E(page, "brevmal-button").filter({ hasText: "E 001" }).click();

    await dataE2E(page, "order-letter").click();
    await page.locator("select[name=enhetsId]").selectOption({ label: "Nav Arbeid og ytelser Innlandet" });

    await expect(
      page.locator("label").filter({ hasText: "Land" }).locator("..").locator(".aksel-error-message"),
    ).toBeVisible();
    await page.locator("select[name=landkode]").selectOption({ label: "Storbritannia" });
    await expect(
      page.locator("label").filter({ hasText: "Land" }).locator("..").locator(".aksel-error-message"),
    ).not.toBeVisible();

    await expect(dataE2E(page, "mottaker-text-textfield").locator("..").locator(".aksel-error-message")).toBeVisible();
    await dataE2E(page, "mottaker-text-textfield").pressSequentially("Haaland");
    await expect(
      dataE2E(page, "mottaker-text-textfield").locator("..").locator(".aksel-error-message"),
    ).not.toBeVisible();

    const orderLetterResponsePromise = page.waitForResponse(
      (r) => r.url().includes("/bestillBrev/exstream/eblankett") && r.request().method() === "POST",
    );
    await dataE2E(page, "order-letter").click();
    await orderLetterResponsePromise;

    expect(requestBody).toMatchObject({
      brevkode: "E001",
      landkode: "GBR",
      mottakerText: "Haaland",
      enhetsId: "4405",
    });

    const calls = await page.evaluate(() => (globalThis as unknown as Record<string, unknown[]>).__windowOpenCalls);
    expect(calls).toEqual([
      "mbdok://PE2@brevklient/dokument/453864284?token=1711101230605&server=https%3A%2F%2Fwasapp-q2.adeo.no%2Fbrevweb%2F",
    ]);
    await expect(dataE2E(page, "order-letter-success-message")).toBeVisible();
  });

  test("enhetsId som url param gjenspeiles i form og inputs", async ({ page }) => {
    await page.addInitScript(() => {
      globalThis.open = () => null;
    });

    const enheterResponsePromise = page.waitForResponse((r) => r.url().includes("/me/enheter") && r.status() === 200);
    await page.goto('/saksnummer/123456/brevvelger?templateId=PE_IY_03_163&enhetsId="4815"');
    await enheterResponsePromise;
    await expect(dataE2E(page, "avsenderenhet-select")).toHaveValue("4815");
  });

  test("valg av ikke-foretrukket språk viser advarsel (ikke feilmelding)", async ({ page }) => {
    await page.route("**/bff/skribenten-backend/sak/123456/foretrukketSpraak", (route) => {
      return route.fulfill({ json: { spraakKode: "EN" } });
    });

    await page.addInitScript(() => {
      globalThis.open = () => null;
    });

    const enheterResponsePromise = page.waitForResponse((r) => r.url().includes("/me/enheter") && r.status() === 200);
    await page.goto('/saksnummer/123456/brevvelger?templateId=PE_IY_03_163&enhetsId="4815"');
    await enheterResponsePromise;

    await dataE2E(page, "språk-velger-select").selectOption({ label: "Bokmål" });
    const warning = page.getByText("Brukers foretrukne språk er engelsk.");
    await expect(warning).toBeVisible();

    await dataE2E(page, "språk-velger-select").selectOption({ label: "Engelsk (foretrukket språk)" });
    await expect(warning).not.toBeVisible();

    await dataE2E(page, "språk-velger-select").selectOption({ label: "Bokmål" });
    await expect(warning).toBeVisible();
  });

  test("Saksinformasjon i subheader", async ({ page }) => {
    await page.route("**/bff/skribenten-backend/sak/123456", (route) => {
      return route.fulfill({
        json: {
          ...sak,
          sak: { ...sak.sak, foedselsdato: "1999-12-31" },
        },
        contentType: "application/json",
      });
    });

    await page.goto("/saksnummer/123456/brevvelger");
    await expect(page.getByText("Født: 31.12.1999")).toBeVisible();
    await expect(page.getByText("Død: 31.12.2014")).not.toBeVisible();
    await expect(page.getByText("Egen ansatt")).not.toBeVisible();
    await expect(page.getByText("Vergemål")).not.toBeVisible();
    await expect(page.getByText("Diskresjon")).not.toBeVisible();

    // Override routes for second visit
    await page.route("**/bff/skribenten-backend/sak/123456", (route) => {
      return route.fulfill({
        json: {
          ...sak,
          sak: { ...sak.sak, foedselsdato: "1999-12-31" },
        },
        contentType: "application/json",
      });
    });
    await page.route("**/bff/skribenten-backend/sak/**/brukerstatus", (route) => {
      return route.fulfill({
        json: {
          doedsfall: "2014-12-31",
          erSkjermet: true,
          vergemaal: true,
          adressebeskyttelse: true,
        },
      });
    });

    await page.goto("/saksnummer/123456/brevvelger");
    await expect(page.getByText("Født: 31.12.1999")).toBeVisible();
    await expect(page.getByText("Død: 31.12.2014")).toBeVisible();
    await expect(page.getByText("Egen ansatt")).toBeVisible();
    await expect(page.getByText("Vergemål")).toBeVisible();
    await expect(page.getByText("Diskresjon")).toBeVisible();
  });
});
