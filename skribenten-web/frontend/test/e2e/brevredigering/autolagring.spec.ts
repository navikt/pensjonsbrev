import fs from "node:fs";
import path from "node:path";

import { expect, test } from "@playwright/test";
import { formatISO } from "date-fns";

import { AUTOSAVE_TIMER, SAKSBEHANDLERVALG_TIMER } from "~/components/ManagedLetterEditor/autosave_timer";
import { type OppdaterBrevRequest } from "~/types/brev";
import { setupSakStubs } from "~test/e2e/support/helpers";

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
for (const response of [
  brev,
  orienteringOmSaksbehandlingstidResponse,
  orienteringOmSaksbehandlingstidResponseMedSoknadOversendesTilUtlandet,
]) {
  response.info.saksId = 123456;
}

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
              saksbehandlerValg: capturedBody?.saksbehandlerValg,
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
              saksbehandlerValg: capturedBody?.saksbehandlerValg,
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
              saksbehandlerValg: capturedBody?.saksbehandlerValg,
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

  test("nullstiller feilmelding fra forrige lagring når ny autolagring lykkes", async ({ page }) => {
    const lagringer: OppdaterBrevRequest[] = [];
    let tekstlagringer = 0;
    const secondSaveGate = Promise.withResolvers<void>();

    await page.route("**/bff/skribenten-backend/brev/1/redigertBrev?frigiReservasjon=false", async (route) => {
      if (route.request().method() !== "PUT") return route.fallback();

      tekstlagringer++;
      return route.fulfill({
        json: {
          ...brev,
          redigertBrev: route.request().postDataJSON(),
          info: { ...brev.info, sistredigert: hurtiglagreTidspunkt },
        },
      });
    });

    await page.route(
      (url: URL) => url.pathname.includes("/sak/123456/brev/1") && url.search.includes("frigiReservasjon"),
      async (route) => {
        if (route.request().method() !== "PUT") return route.fallback();

        const body: OppdaterBrevRequest = route.request().postDataJSON();
        lagringer.push(body);
        if (lagringer.length === 1) {
          return route.fulfill({
            status: 422,
            json: { reason: "FritekstFelterUredigert" },
          });
        }

        await secondSaveGate.promise;
        return route.fulfill({
          json: {
            ...brev,
            redigertBrev: body.redigertBrev,
            saksbehandlerValg: body.saksbehandlerValg,
            info: { ...brev.info, sistredigert: hurtiglagreTidspunkt },
          },
        });
      },
    );

    await page.goto("/saksnummer/123456/brev/1");
    await expect(page.getByText("Lagret")).toBeVisible();
    await page.clock.install({ time: new Date("2026-01-01T12:00:00Z") });
    await page.clock.pauseAt(new Date("2026-01-01T12:00:01Z"));

    await page.getByText("Overstyring").click();
    await expect(page.getByLabel("Ytelse")).toBeVisible();

    const failedSavePromise = page.waitForResponse(
      (resp) =>
        resp.url().includes("/sak/123456/brev/1") &&
        resp.url().includes("frigiReservasjon") &&
        resp.request().method() === "PUT",
    );
    await page.getByLabel("Ytelse").fill("Supplerende stønad");
    await page.clock.runFor(2500);
    await failedSavePromise;
    await page.clock.runFor(1);

    await expect(page.getByText(/Klarte ikke lagre/)).toBeVisible();
    await expect(page.getByLabel("Ytelse")).toHaveValue("Supplerende stønad");
    expect(lagringer).toHaveLength(1);

    const retrySavePromise = page.waitForResponse(
      (resp) =>
        resp.url().includes("/sak/123456/brev/1") &&
        resp.url().includes("frigiReservasjon") &&
        resp.request().method() === "PUT",
    );
    // Samme mislykkede revisjon forsøkes ikke igjen uten en ny endring.
    await page.clock.runFor(AUTOSAVE_TIMER);
    expect(lagringer).toHaveLength(1);
    await page.getByLabel("Ytelse").fill("Supplerende stønad A");
    await page.clock.runFor(2500);
    await expect(page.getByText("Lagrer...")).toBeVisible();
    await expect(page.getByText(/Klarte ikke lagre/)).toBeHidden();

    secondSaveGate.resolve();
    await retrySavePromise;
    await page.clock.runFor(1);

    await expect(page.getByText("Lagret")).toBeVisible();
    await expect(page.getByText(/Klarte ikke lagre/)).toBeHidden();
    await expect(page.getByLabel("Ytelse")).toHaveValue("Supplerende stønad A");
    await page.clock.runFor(AUTOSAVE_TIMER);
    expect(lagringer).toHaveLength(2);
    expect(lagringer.map((request) => request.saksbehandlerValg.ytelse)).toEqual([
      "Supplerende stønad",
      "Supplerende stønad A",
    ]);
    expect(tekstlagringer).toBe(0);

    await page.getByRole("button", { name: "Angre (Undo)" }).click();
    await expect(page.getByLabel("Ytelse")).toHaveValue("Supplerende stønad");
    await page.getByRole("button", { name: "Gjør om (Redo)" }).click();
    await expect(page.getByLabel("Ytelse")).toHaveValue("Supplerende stønad A");
  });

  test("ny tekstendring lagrer siste gyldige valg uten å nullstille nyere skjemaverdier", async ({ page }) => {
    const lagringer: OppdaterBrevRequest[] = [];
    let tekstlagringer = 0;
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1?frigiReservasjon=false", (route) => {
      if (route.request().method() !== "PUT") return route.fallback();
      const body: OppdaterBrevRequest = route.request().postDataJSON();
      lagringer.push(body);
      return lagringer.length === 1
        ? route.fulfill({ status: 422, json: { reason: "FritekstFelterUredigert" } })
        : route.fulfill({ json: { ...brev, ...body } });
    });
    await page.route("**/bff/skribenten-backend/brev/1/redigertBrev?frigiReservasjon=false", (route) => {
      if (route.request().method() !== "PUT") return route.fallback();
      tekstlagringer++;
      return route.fulfill({ json: { ...brev, redigertBrev: route.request().postDataJSON() } });
    });

    await page.goto("/saksnummer/123456/brev/1");
    await expect(page.getByText("Lagret")).toBeVisible();
    await page.clock.install({ time: new Date("2026-01-01T12:00:00Z") });
    await page.clock.pauseAt(new Date("2026-01-01T12:00:01Z"));
    await page.getByRole("tab", { name: "Overstyring" }).click();
    await page.getByLabel("Ytelse").fill("Supplerende stønad");
    const failedSave = page.waitForResponse((response) => response.status() === 422);
    await page.clock.runFor(2500);
    await failedSave;
    await page.clock.runFor(1);
    await expect(page.getByText(/Klarte ikke lagre/)).toBeVisible();

    await page.getByRole("textbox", { name: "Underskrift" }).fill("Ny underskrift");
    // Nye feltendringer utsetter skjemalagringen, men ikke autolagringen av underskriften.
    await page.clock.runFor(2000);
    await page.getByLabel("Ytelse").fill("Supplerende stønad A");
    await page.clock.runFor(2000);
    await page.getByLabel("Ytelse").fill("Supplerende stønad B");

    const textSave = page.waitForResponse((response) =>
      response.url().endsWith("/sak/123456/brev/1?frigiReservasjon=false"),
    );
    await page.clock.runFor(1000);
    await textSave;
    await page.clock.runFor(1);
    expect(tekstlagringer).toBe(0);
    expect(lagringer).toHaveLength(2);
    expect(lagringer[1].saksbehandlerValg.ytelse).toBe("Supplerende stønad");
    await expect(page.getByLabel("Ytelse")).toHaveValue("Supplerende stønad B");

    const formSave = page.waitForResponse((response) =>
      response.url().endsWith("/sak/123456/brev/1?frigiReservasjon=false"),
    );
    await page.clock.runFor(2500);
    await formSave;
    await page.clock.runFor(1);
    await expect(page.getByText("Lagret", { exact: true })).toBeVisible();
    await expect(page.getByLabel("Ytelse")).toHaveValue("Supplerende stønad B");
    await expect(page.getByRole("textbox", { name: "Underskrift" })).toHaveValue("Ny underskrift");
    expect(lagringer).toHaveLength(3);
    expect(lagringer[2].saksbehandlerValg.ytelse).toBe("Supplerende stønad B");
    expect(lagringer[2].redigertBrev.signatur.saksbehandlerNavn).toBe("Ny underskrift");
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

  test("debouncer boolean og enum sammen og beholder nyere valg og uferdig overstyring", async ({ page }) => {
    const specification = JSON.parse(fs.readFileSync(path.join(fixturesDir, "modelSpecification.json"), "utf-8"));
    specification.types[
      "no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmSaksbehandlingstidDto.SaksbehandlerValg"
    ].ekstraTekstvalg = { type: "scalar", nullable: false, kind: "BOOLEAN", displayText: "Ekstra tekstvalg" };
    await page.route(
      "**/bff/skribenten-backend/brevmal/INFORMASJON_OM_SAKSBEHANDLINGSTID/modelSpecification",
      (route) => route.fulfill({ json: specification }),
    );
    const requests: OppdaterBrevRequest[] = [];
    const first = Promise.withResolvers<void>();
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1?frigiReservasjon=false", async (route) => {
      const request: OppdaterBrevRequest = route.request().postDataJSON();
      requests.push(request);
      if (requests.length === 1) await first.promise;
      await route.fulfill({ json: { ...brev, ...request } });
    });
    await page.goto("/saksnummer/123456/brev/1");
    await expect(page.getByText("Lagret", { exact: true })).toBeVisible();
    await page.clock.install();
    await page.clock.pauseAt(new Date());
    await page.getByRole("checkbox", { name: "Ekstra tekstvalg" }).check();
    await page.clock.runFor(SAKSBEHANDLERVALG_TIMER - 1);
    expect(requests).toHaveLength(0);
    await page.getByRole("radio", { name: "Rullekake", exact: true }).check();
    await page.clock.runFor(SAKSBEHANDLERVALG_TIMER - 1);
    expect(requests).toHaveLength(0);
    await page.getByRole("radio", { name: "Ostekake", exact: true }).check();
    await page.clock.runFor(SAKSBEHANDLERVALG_TIMER);
    await expect.poll(() => requests.length).toBe(1);
    await expect(page.getByRole("button", { name: "Angre (Undo)" })).toBeDisabled();
    await page.getByRole("radio", { name: "Gulrotkake", exact: true }).check();
    await page.getByRole("tab", { name: "Overstyring" }).click();
    await page.getByLabel("Ytelse").fill("Ny ytelse");
    await page.clock.runFor(SAKSBEHANDLERVALG_TIMER);
    expect(requests).toHaveLength(1);
    first.resolve();
    await expect.poll(() => requests.length).toBe(2);
    await expect(page.getByLabel("Ytelse")).toHaveValue("Ny ytelse");
    expect(requests.map((request) => request.saksbehandlerValg.ettEllerIngenAvMangeAlternativer)).toEqual([
      "OSTEKAKE",
      "GULROTKAKE",
    ]);
    expect(requests.every((request) => request.saksbehandlerValg.ekstraTekstvalg === true)).toBe(true);
    expect(requests[1].saksbehandlerValg.ytelse).toBe("alderspensjon");
    await page.clock.runFor(2500);
    await expect.poll(() => requests.length).toBe(3);
    expect(requests[2].saksbehandlerValg.ytelse).toBe("Ny ytelse");
  });

  test("Fortsett avbryter ventende valgdebounce før reservasjonen frigis", async ({ page }) => {
    const requests: { frigiReservasjon: string | null; body: OppdaterBrevRequest }[] = [];
    await page.route(
      (url) => url.pathname.endsWith("/sak/123456/brev/1") && url.searchParams.has("frigiReservasjon"),
      async (route) => {
        if (route.request().method() !== "PUT") return route.fallback();
        const url = new URL(route.request().url());
        const body: OppdaterBrevRequest = route.request().postDataJSON();
        requests.push({ frigiReservasjon: url.searchParams.get("frigiReservasjon"), body });
        await route.fulfill({ json: { ...brev, ...body } });
      },
    );
    await page.goto("/saksnummer/123456/brev/1");
    await expect(page.getByText("Lagret", { exact: true })).toBeVisible();
    await page.clock.install();
    await page.clock.pauseAt(new Date());

    await page.getByRole("radio", { name: "Rullekake", exact: true }).check();
    await page.getByRole("button", { name: "Fortsett", exact: true }).click();
    const confirmation = page.getByText("Fortsett til brevbehandler");
    await confirmation
      .waitFor({ state: "visible", timeout: 1000 })
      .then(() => confirmation.click())
      .catch(() => undefined);

    await expect(page).toHaveURL(/brevbehandler/, { timeout: 20_000 });
    await page.clock.runFor(AUTOSAVE_TIMER + SAKSBEHANDLERVALG_TIMER);

    expect(requests).toHaveLength(1);
    expect(requests[0].frigiReservasjon).toBe("true");
    expect(requests[0].body.saksbehandlerValg.ettEllerIngenAvMangeAlternativer).toBe("RULLEKAKE");
  });

  test("angrer hvert enumvalg selv om bare siste verdi ble sendt til backend", async ({ page }) => {
    const requests: OppdaterBrevRequest[] = [];
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1?frigiReservasjon=false", async (route) => {
      const request: OppdaterBrevRequest = route.request().postDataJSON();
      requests.push(request);
      const response = structuredClone(brev);
      response.saksbehandlerValg = request.saksbehandlerValg;
      response.redigertBrev = request.redigertBrev;
      response.redigertBrev.blocks[0].content[3].text =
        request.saksbehandlerValg.ettEllerIngenAvMangeAlternativer ?? "alderspensjon";
      await route.fulfill({ json: response });
    });
    await page.goto("/saksnummer/123456/brev/1");
    await expect(page.getByText("Lagret", { exact: true })).toBeVisible();
    await page.clock.install();
    await page.clock.pauseAt(new Date());
    await page.getByRole("radio", { name: "Rullekake", exact: true }).check();
    await page.getByRole("radio", { name: "Ostekake", exact: true }).check();
    await page.clock.runFor(SAKSBEHANDLERVALG_TIMER);
    await expect(page.getByText("Lagret", { exact: true })).toBeVisible();
    expect(requests).toHaveLength(1);
    await page.getByRole("button", { name: "Angre (Undo)" }).click();
    await expect(page.getByRole("radio", { name: "Rullekake", exact: true })).toBeChecked();
    await page.clock.runFor(SAKSBEHANDLERVALG_TIMER);
    await expect(page.getByText("RULLEKAKE", { exact: true })).toBeVisible();
    expect(requests).toHaveLength(2);
    await page.getByRole("button", { name: "Gjør om (Redo)" }).click();
    await expect(page.getByRole("radio", { name: "Ostekake", exact: true })).toBeChecked();
    await page.clock.runFor(SAKSBEHANDLERVALG_TIMER);
    await expect(page.getByText("OSTEKAKE", { exact: true })).toBeVisible();
  });

  test("bevarer teksthistorikk før, mellom og etter valg som lagres sammen", async ({ page }) => {
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1?frigiReservasjon=false", async (route) => {
      const request: OppdaterBrevRequest = route.request().postDataJSON();
      const response = structuredClone(brev);
      response.saksbehandlerValg = request.saksbehandlerValg;
      response.redigertBrev = request.redigertBrev;
      response.redigertBrev.blocks[0].content[3].text =
        request.saksbehandlerValg.ettEllerIngenAvMangeAlternativer ?? "alderspensjon";
      await route.fulfill({ json: response });
    });
    await page.goto("/saksnummer/123456/brev/1");
    await expect(page.getByText("Lagret", { exact: true })).toBeVisible();
    await page.clock.install();
    await page.clock.pauseAt(new Date());
    const text = page.getByText("Dersom vi trenger flere opplysninger", { exact: false });
    await text.click();
    await page.locator(":focus").press("End");
    await page.locator(":focus").pressSequentially(" FØR");
    await page.getByRole("radio", { name: "Rullekake", exact: true }).check();
    await text.click();
    await page.locator(":focus").press("End");
    await page.locator(":focus").pressSequentially(" MELLOM");
    await page.getByRole("radio", { name: "Ostekake", exact: true }).check();
    await text.click();
    await page.locator(":focus").press("End");
    await page.locator(":focus").pressSequentially(" ETTER");
    await page.clock.runFor(SAKSBEHANDLERVALG_TIMER);
    await expect(page.getByText("Lagret", { exact: true })).toBeVisible();

    const undo = page.getByRole("button", { name: "Angre (Undo)" });
    await undo.click();
    await expect(text).not.toContainText("ETTER");
    await expect(text).toContainText("MELLOM");
    await undo.click();
    await expect(page.getByRole("radio", { name: "Rullekake", exact: true })).toBeChecked();
    await undo.click();
    await expect(text).not.toContainText("MELLOM");
    await expect(text).toContainText("FØR");
    await undo.click();
    await undo.click();
    await expect(text).not.toContainText("FØR");
    const redo = page.getByRole("button", { name: "Gjør om (Redo)" });
    for (let i = 0; i < 5; i++) await redo.click();
    await page.clock.runFor(SAKSBEHANDLERVALG_TIMER);
    await expect(page.getByText("Lagret", { exact: true })).toBeVisible();
    await expect(text).toContainText("FØR");
    await expect(text).toContainText("MELLOM");
    await expect(text).toContainText("ETTER");
  });

  test("gjenoppretter fokus når en strukturell endring angres mens tekstvalg venter på lagring", async ({ page }) => {
    await page.goto("/saksnummer/123456/brev/1");
    await expect(page.getByText("Lagret", { exact: true })).toBeVisible();
    await page.clock.install();
    await page.clock.pauseAt(new Date());

    await page.getByRole("radio", { name: "Rullekake", exact: true }).check();
    const text = page.getByText("Dersom vi trenger flere opplysninger", { exact: false });
    await text.click();
    await page.locator(":focus").press("End");
    await page.locator(":focus").press("Enter");

    await expect(page.getByRole("button", { name: "Angre (Undo)" })).toBeEnabled();
    await page.getByRole("button", { name: "Angre (Undo)" }).click();
    await page.keyboard.type(" FOKUS");

    await expect(text).toContainText("FOKUS");
  });
});
