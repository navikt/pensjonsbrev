import fs from "node:fs";
import path from "node:path";

import { expect, test } from "@playwright/test";
import { formatISO } from "date-fns";

import { setupSakStubs } from "../utils/helpers";

const fixturesDir = path.resolve("test/e2e/fixtures");
const brev = JSON.parse(fs.readFileSync(path.join(fixturesDir, "brevResponse.json"), "utf-8"));
const orienteringOmSaksbehandlingstidResponse = JSON.parse(
  fs.readFileSync(path.join(fixturesDir, "orienteringOmSaksbehandlingstidResponse.json"), "utf-8"),
);
const orienteringOmSaksbehandlingstidResponseMedSoknadOversendesTilUtlandet = JSON.parse(
  fs.readFileSync(
    path.join(fixturesDir, "orienteringOmSaksbehandlingstidResponseMedSoknadOversendesTilUtlandet.json"),
    "utf-8",
  ),
);

test.describe("autolagring", () => {
  const hurtiglagreTidspunkt = formatISO(new Date());

  test.beforeEach(async ({ page }) => {
    await setupSakStubs(page);

    await page.route("**/bff/skribenten-backend/sak/123456/brev/1?reserver=true", (route) =>
      route.fulfill({ json: brev }),
    );

    await page.route(
      "**/bff/skribenten-backend/brevmal/INFORMASJON_OM_SAKSBEHANDLINGSTID/modelSpecification",
      (route) => route.fulfill({ path: "test/e2e/fixtures/modelSpecification.json", contentType: "application/json" }),
    );
  });

  test("lagrer endring av dato-felt automatisk", async ({ page }) => {
    let capturedBody: Record<string, unknown> | undefined;

    await page.route(
      (url: URL) => url.pathname.includes("/sak/123456/brev/1") && url.search.includes("frigiReservasjon"),
      async (route) => {
        if (route.request().method() === "PUT") {
          capturedBody = route.request().postDataJSON();
          await route.fulfill({
            json: {
              ...brev,
              info: { ...brev.info, sistredigert: hurtiglagreTidspunkt },
              saksbehandlerValg: capturedBody,
            },
          });
        } else {
          await route.fallback();
        }
      },
    );

    await page.goto("/saksnummer/123456/brev/1");
    await expect(page.getByText("Lagret")).toBeVisible();

    await page.getByText("Overstyring").click();
    await expect(page.getByLabel("Mottatt søknad")).toBeVisible();
    await expect(page.getByTestId("datepicker-editor")).toHaveValue("24.07.2024");

    await page.getByTestId("datepicker-editor").click();
    await page.getByTestId("datepicker-editor").clear();
    await page.getByTestId("datepicker-editor").fill("10.09.2024");

    await page.waitForResponse(
      (resp) =>
        resp.url().includes("/sak/123456/brev/1") &&
        resp.url().includes("frigiReservasjon") &&
        resp.request().method() === "PUT",
    );

    expect(capturedBody).toBeDefined();
    expect((capturedBody as Record<string, unknown>).saksbehandlerValg).toMatchObject({
      mottattSoeknad: "2024-09-10",
      ytelse: "alderspensjon",
      land: "Spania",
      svartidUker: "10",
    });

    await expect(page.getByText("Lagret")).toBeVisible();
  });

  test("lagrer endring av tekst-felt automatisk", async ({ page }) => {
    let capturedBody: Record<string, unknown> | undefined;

    await page.route(
      (url: URL) => url.pathname.includes("/sak/123456/brev/1") && url.search.includes("frigiReservasjon"),
      async (route) => {
        if (route.request().method() === "PUT") {
          capturedBody = route.request().postDataJSON();
          await route.fulfill({
            json: {
              ...brev,
              saksbehandlerValg: capturedBody,
              info: { ...brev.info, sistredigert: hurtiglagreTidspunkt },
            },
          });
        } else {
          await route.fallback();
        }
      },
    );

    await page.goto("/saksnummer/123456/brev/1");
    await expect(page.getByText("Lagret")).toBeVisible();

    await page.getByText("Overstyring").click();
    await expect(page.getByLabel("Ytelse")).toBeVisible();

    await page.getByLabel("Ytelse").fill("Supplerende stønad");

    await page.waitForResponse(
      (resp) =>
        resp.url().includes("/sak/123456/brev/1") &&
        resp.url().includes("frigiReservasjon") &&
        resp.request().method() === "PUT",
    );

    expect(capturedBody).toBeDefined();
    expect((capturedBody as Record<string, unknown>).saksbehandlerValg).toMatchObject({
      mottattSoeknad: "2024-07-24",
      ytelse: "Supplerende stønad",
      land: "Spania",
      svartidUker: "10",
    });

    await expect(page.getByText("Lagret")).toBeVisible();
  });

  test("angrer og gjør om regenerert brevtekst fra saksbehandlervalg", async ({ page }) => {
    const oppdatertBrev = JSON.parse(JSON.stringify(brev));
    oppdatertBrev.redigertBrev.blocks[0].content[3].text = "Supplerende stønad";
    oppdatertBrev.saksbehandlerValg = {
      ...brev.saksbehandlerValg,
      ytelse: "Supplerende stønad",
    };
    oppdatertBrev.info.sistredigert = hurtiglagreTidspunkt;

    await page.route(
      (url: URL) => url.pathname.includes("/sak/123456/brev/1") && url.search.includes("frigiReservasjon"),
      async (route) => {
        if (route.request().method() === "PUT") {
          await route.fulfill({ json: oppdatertBrev });
        } else {
          await route.fallback();
        }
      },
    );

    await page.goto("/saksnummer/123456/brev/1");
    await expect(page.getByText("Lagret")).toBeVisible();
    await expect(page.locator("span", { hasText: "alderspensjon" })).toBeVisible();

    await page.getByText("Overstyring").click();
    await expect(page.getByLabel("Ytelse")).toBeVisible();

    const savePromise = page.waitForResponse(
      (resp) =>
        resp.url().includes("/sak/123456/brev/1") &&
        resp.url().includes("frigiReservasjon") &&
        resp.request().method() === "PUT",
    );
    await page.getByLabel("Ytelse").fill("Supplerende stønad");
    await savePromise;

    await expect(page.locator("span", { hasText: "Supplerende stønad" })).toBeVisible();
    await expect(page.getByLabel("Ytelse")).toHaveValue("Supplerende stønad");

    await page.getByRole("button", { name: "Angre (Undo)" }).click();
    await expect(page.locator("span", { hasText: "alderspensjon" })).toBeVisible();
    await expect(page.locator("span", { hasText: "Supplerende stønad" })).not.toBeVisible();
    await expect(page.getByLabel("Ytelse")).toHaveValue("alderspensjon");

    await page.getByRole("button", { name: "Gjør om (Redo)" }).click();
    await expect(page.locator("span", { hasText: "Supplerende stønad" })).toBeVisible();
    await expect(page.getByLabel("Ytelse")).toHaveValue("Supplerende stønad");
  });

  test("autolagrer når nullable tekst felter tømmes", async ({ page }) => {
    let capturedBody: Record<string, unknown> | undefined;

    await page.route(
      (url: URL) => url.pathname.includes("/sak/123456/brev/1") && url.search.includes("frigiReservasjon"),
      async (route) => {
        if (route.request().method() === "PUT") {
          capturedBody = route.request().postDataJSON();
          await route.fulfill({
            json: {
              ...brev,
              saksbehandlerValg: capturedBody,
              info: { ...brev.info, sistredigert: hurtiglagreTidspunkt },
            },
          });
        } else {
          await route.fallback();
        }
      },
    );

    await page.goto("/saksnummer/123456/brev/1");
    await expect(page.locator("span", { hasText: "Spania" })).toBeVisible();

    await page.getByLabel("Land").fill("");

    await page.waitForResponse(
      (resp) =>
        resp.url().includes("/sak/123456/brev/1") &&
        resp.url().includes("frigiReservasjon") &&
        resp.request().method() === "PUT",
    );

    expect(capturedBody).toBeDefined();
    expect((capturedBody as Record<string, unknown>).saksbehandlerValg).toMatchObject({
      mottattSoeknad: "2024-07-24",
      ytelse: "alderspensjon",
      land: null,
      svartidUker: "10",
    });
  });

  test("autolagrer ikke før alle avhengige felter er utfylt", async ({ page }) => {
    let autoSaveCount = 0;
    let capturedBody: Record<string, unknown> | undefined;

    await page.route(
      (url: URL) => url.pathname.includes("/sak/123456/brev/1") && url.search.includes("frigiReservasjon"),
      async (route) => {
        if (route.request().method() === "PUT") {
          autoSaveCount++;
          capturedBody = route.request().postDataJSON();
          await route.fulfill({
            json: {
              ...brev,
              saksbehandlerValg: capturedBody,
              info: { ...brev.info, sistredigert: hurtiglagreTidspunkt },
            },
          });
        } else {
          await route.fallback();
        }
      },
    );

    await page.goto("/saksnummer/123456/brev/1");
    await expect(page.getByText("Lagret")).toBeVisible();
    await expect(page.getByText("Saksbehandlingstiden vår er vanligvis 10 uker.")).toBeVisible();

    await page.getByLabel("Inkluder venter svar AFP").click();
    await expect(page.getByTestId("datepicker-editor").nth(0)).toHaveValue("");
    await page.getByTestId("datepicker-editor").nth(0).click();
    await page.getByTestId("datepicker-editor").nth(0).clear();
    await page.getByTestId("datepicker-editor").nth(0).fill("10.09.2024");

    // Verify that autosave has not happened yet
    expect(autoSaveCount).toBe(0);

    await page.getByLabel("Uttak alderspensjon prosent").click();
    await page.locator(":focus").pressSequentially("55");

    await page.waitForResponse(
      (resp) =>
        resp.url().includes("/sak/123456/brev/1") &&
        resp.url().includes("frigiReservasjon") &&
        resp.request().method() === "PUT",
    );

    expect(autoSaveCount).toBe(1);
    expect(capturedBody).toBeDefined();
    expect((capturedBody as Record<string, unknown>).saksbehandlerValg).toEqual({
      mottattSoeknad: "2024-07-24",
      ytelse: "alderspensjon",
      land: "Spania",
      svartidUker: "10",
      inkluderVenterSvarAFP: {
        uttakAlderspensjonProsent: "55",
        uttaksDato: "2024-09-10",
      },
    });
  });

  test("autolagrer signatur", async ({ page }) => {
    let autoSaveCount = 0;
    let capturedBody: Record<string, unknown> | undefined;

    await page.route("**/bff/skribenten-backend/brev/1/redigertBrev?frigiReservasjon=*", async (route) => {
      if (route.request().method() === "PUT") {
        autoSaveCount++;
        capturedBody = route.request().postDataJSON();
        await route.fulfill({
          json: {
            ...brev,
            redigertBrev: capturedBody,
            info: { ...brev.info, sistredigert: hurtiglagreTidspunkt },
          },
        });
      } else {
        await route.fallback();
      }
    });

    await page.goto("/saksnummer/123456/brev/1");
    await expect(page.getByText("Lagret")).toBeVisible();
    await expect(page.getByTestId("brev-editor-saksbehandler")).toContainText("Sak S. Behandler");

    await page.getByLabel("Underskrift").fill("Min nye underskrift");

    await expect(page.getByTestId("brev-editor-saksbehandler")).toContainText("Min nye underskrift");

    await page.waitForResponse(
      (resp) => resp.url().includes("/brev/1/redigertBrev") && resp.request().method() === "PUT",
    );

    expect(autoSaveCount).toBe(1);
    expect(capturedBody).toBeDefined();
    expect((capturedBody as Record<string, Record<string, unknown>>).signatur.saksbehandlerNavn).toContain(
      "Min nye underskrift",
    );

    await expect(page.getByTestId("brev-editor-saksbehandler")).toContainText("Min nye underskrift");
  });

  test("autolagring av boolean felter", async ({ page }) => {
    await page.route(
      "**/bff/skribenten-backend/brevmal/UT_ORIENTERING_OM_SAKSBEHANDLINGSTID/modelSpecification",
      (route) =>
        route.fulfill({
          path: "test/e2e/fixtures/modelSpecificationOrienteringOmSaksbehandlingstid.json",
          contentType: "application/json",
        }),
    );

    await page.route("**/bff/skribenten-backend/sak/123456/brev/1?reserver=true", (route) =>
      route.fulfill({
        json: orienteringOmSaksbehandlingstidResponse,
      }),
    );

    await page.route(
      (url: URL) => url.pathname.includes("/sak/123456/brev/1") && url.search.includes("frigiReservasjon"),
      async (route) => {
        if (route.request().method() === "PUT") {
          await route.fulfill({
            json: orienteringOmSaksbehandlingstidResponseMedSoknadOversendesTilUtlandet,
          });
        } else {
          await route.fallback();
        }
      },
    );

    await page.goto("/saksnummer/123456/brev/1");
    await expect(page.getByText("Søknaden din vil også bli oversendt utlandet fordi")).toHaveCount(0);

    const responsePromise = page.waitForResponse(
      (resp) =>
        resp.url().includes("/sak/123456/brev/1") &&
        resp.url().includes("frigiReservasjon") &&
        resp.request().method() === "PUT",
    );

    await page.getByRole("checkbox", { name: /oversendes til utlandet/i }).click({ force: true });

    await responsePromise;

    await expect(page.getByText("Søknaden din vil også bli oversendt utlandet fordi")).toBeVisible();
  });

  test("deaktiverer undo/redo mens tekstvalg lagres", async ({ page }) => {
    let saveResolve: (() => void) | undefined;

    const oppdatertBrev = JSON.parse(JSON.stringify(brev));
    oppdatertBrev.redigertBrev.blocks[0].content[3].text = "Supplerende stønad";
    oppdatertBrev.saksbehandlerValg = { ...brev.saksbehandlerValg, ytelse: "Supplerende stønad" };
    oppdatertBrev.info.sistredigert = hurtiglagreTidspunkt;

    const oppdatertBrev2 = JSON.parse(JSON.stringify(oppdatertBrev));
    oppdatertBrev2.redigertBrev.blocks[0].content[3].text = "Uføretrygd";
    oppdatertBrev2.saksbehandlerValg = { ...brev.saksbehandlerValg, ytelse: "Uføretrygd" };

    let saveCount = 0;

    await page.route(
      (url: URL) => url.pathname.includes("/sak/123456/brev/1") && url.search.includes("frigiReservasjon"),
      async (route) => {
        if (route.request().method() !== "PUT") return route.fallback();
        saveCount++;
        if (saveCount === 1) {
          await route.fulfill({ json: oppdatertBrev });
        } else {
          await new Promise<void>((resolve) => {
            saveResolve = resolve;
          });
          await route.fulfill({ json: oppdatertBrev2 });
        }
      },
    );

    await page.goto("/saksnummer/123456/brev/1");
    await expect(page.getByText("Lagret")).toBeVisible();

    await page.getByText("Overstyring").click();
    await expect(page.getByLabel("Ytelse")).toBeVisible();

    const firstSavePromise = page.waitForResponse(
      (resp) =>
        resp.url().includes("/sak/123456/brev/1") &&
        resp.url().includes("frigiReservasjon") &&
        resp.request().method() === "PUT",
    );
    await page.getByLabel("Ytelse").fill("Supplerende stønad");
    await firstSavePromise;

    await expect(page.getByRole("button", { name: "Angre (Undo)" })).toBeEnabled();

    await page.getByLabel("Ytelse").fill("Uføretrygd");

    await expect(page.getByRole("button", { name: "Angre (Undo)" })).toBeDisabled();
    await expect(page.getByRole("button", { name: "Gjør om (Redo)" })).toBeDisabled();

    saveResolve?.();

    await expect(page.getByRole("button", { name: "Angre (Undo)" })).toBeEnabled();
    await expect(page.getByLabel("Ytelse")).toHaveValue("Uføretrygd");
  });
});
