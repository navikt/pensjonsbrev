import fs from "node:fs";
import path from "node:path";

import { expect, test } from "@playwright/test";
import { formatISO } from "date-fns";

import { dataE2E, setupSakStubs } from "../utils/helpers";

const fixturesDir = path.resolve("test/e2e/fixtures");
const brevResponseEtterLagring = JSON.parse(
  fs.readFileSync(path.join(fixturesDir, "brevResponseEtterLagring.json"), "utf-8"),
);
const brevResponse = JSON.parse(fs.readFileSync(path.join(fixturesDir, "brevResponse.json"), "utf-8"));
const brevResponseTestBrev = JSON.parse(fs.readFileSync(path.join(fixturesDir, "brevResponseTestBrev.json"), "utf-8"));
const modelSpecificationBrukertestBrevPensjon2025 = JSON.parse(
  fs.readFileSync(path.join(fixturesDir, "modelSpecificationBrukertestBrevPensjon2025.json"), "utf-8"),
);

test.describe("Brevredigering", () => {
  const hurtiglagreTidspunkt = formatISO(new Date());

  test.beforeEach(async ({ page }) => {
    await setupSakStubs(page);

    await page.route("**/bff/skribenten-backend/sak/123456/brev/1?reserver=true", (route) =>
      route.fulfill({ path: "test/e2e/fixtures/brevResponse.json", contentType: "application/json" }),
    );

    const brevEtterLagring = JSON.parse(JSON.stringify(brevResponseEtterLagring));
    brevEtterLagring.info.sistredigert = hurtiglagreTidspunkt;
    await page.route("**/bff/skribenten-backend/brev/1/redigertBrev?frigiReservasjon=*", async (route) => {
      if (route.request().method() === "PUT") {
        return route.fulfill({ json: brevEtterLagring });
      }
      return route.fallback();
    });

    await page.route(
      "**/bff/skribenten-backend/brevmal/INFORMASJON_OM_SAKSBEHANDLINGSTID/modelSpecification",
      (route) => route.fulfill({ path: "test/e2e/fixtures/modelSpecification.json", contentType: "application/json" }),
    );

    await page.route("**/bff/skribenten-backend/brev/1/reservasjon", (route) =>
      route.fulfill({ path: "test/e2e/fixtures/brevreservasjon.json", contentType: "application/json" }),
    );
  });

  test("Åpne brevredigering", async ({ page }) => {
    await page.goto("/saksnummer/123456/brev/1");
    await expect(page.getByText("Saksbehandlingstiden vår er vanligvis 10 uker.")).toBeVisible();
  });

  test("Autolagrer etter redigering", async ({ page }) => {
    await page.goto("/saksnummer/123456/brev/1");
    await expect(page.getByText("Lagret")).toBeVisible();

    await page.getByText("Dersom vi trenger flere opplysninger").click();
    await page.locator(":focus").pressSequentially(" hello!");

    await page.waitForResponse(
      (resp) => resp.url().includes("/brev/1/redigertBrev") && resp.request().method() === "PUT",
      { timeout: 20000 },
    );
    await expect(page.getByText("Lagret")).toBeVisible();
    await expect(page.getByText("hello!")).toBeVisible();
  });

  test("lagrer signatur og saksbehandlerValg ved fortsett klikk", async ({ page }) => {
    let lagreBrevCount = 0;
    let requestBody: Record<string, unknown> = {};

    await page.route(
      (url: URL) => url.pathname.includes("/sak/123456/brev/1") && !url.search.includes("reserver"),
      async (route) => {
        if (route.request().method() !== "PUT") {
          return route.fallback();
        }
        lagreBrevCount++;
        requestBody = route.request().postDataJSON();
        await route.fulfill({ json: brevResponse });
      },
    );

    await page.goto("/saksnummer/123456/brev/1");

    // Change "Land" field
    await page.getByLabel("Land").fill("Mars");

    // Change "Underskrift" field
    await page.getByLabel("Underskrift").fill("Det nye saksbehandlernavnet");

    // Click "Fortsett" and then "Fortsett til brevbehandler"
    await page.getByText("Fortsett", { exact: true }).click();

    const responsePromise = page.waitForResponse(
      (resp) =>
        resp.url().includes("/sak/123456/brev/1") &&
        !resp.url().includes("reserver") &&
        resp.request().method() === "PUT",
      { timeout: 20000 },
    );
    await page.getByText("Fortsett til brevbehandler").click();
    const response = await responsePromise;

    expect(response.status()).toBe(200);
    expect((requestBody as Record<string, unknown>).saksbehandlerValg).toEqual({
      mottattSoeknad: "2024-07-24",
      ytelse: "alderspensjon",
      land: "Mars",
      svartidUker: "10",
    });
    expect(
      ((requestBody as Record<string, unknown>).redigertBrev as Record<string, Record<string, unknown>>).signatur
        .saksbehandlerNavn,
    ).toBe("Det nye saksbehandlernavnet");

    await expect(page).toHaveURL(/\/saksnummer\/123456\/brevbehandler\?brevId=1/);
    expect(lagreBrevCount).toBe(1);
  });

  test("Blokkerer redigering om brev er reservert av noen andre", async ({ page }) => {
    await page.goto("/saksnummer/123456/brev/1");

    // Override reservasjon to return opptatt
    await page.route("**/bff/skribenten-backend/brev/1/reservasjon", (route) =>
      route.fulfill({ path: "test/e2e/fixtures/brevreservasjon_opptatt.json", contentType: "application/json" }),
    );

    await expect(page.getByText("Brevet er utilgjengelig for deg fordi Hugo Weaving har brevet åpent.")).toBeVisible();
  });

  test("Gjenoppta redigering", async ({ page }) => {
    await page.goto("/saksnummer/123456/brev/1");

    // Override reservasjon to return opptatt
    await page.route("**/bff/skribenten-backend/brev/1/reservasjon", (route) =>
      route.fulfill({ path: "test/e2e/fixtures/brevreservasjon_opptatt.json", contentType: "application/json" }),
    );

    await expect(page.getByText("Brevet er utilgjengelig for deg fordi Hugo Weaving har brevet åpent.")).toBeVisible();

    // Override routes for reopening
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1?reserver=true", (route) =>
      route.fulfill({ path: "test/e2e/fixtures/brevResponse_ny_hash.json", contentType: "application/json" }),
    );
    await page.route("**/bff/skribenten-backend/brev/1/reservasjon", (route) =>
      route.fulfill({ path: "test/e2e/fixtures/brevreservasjon_ny_hash.json", contentType: "application/json" }),
    );

    await page.getByText("Ja, åpne på nytt").click();
    await expect(page.getByText("Informasjon om saksbehandlingstiden vår med ny hash")).toBeVisible();
  });

  test("Åpne brev som er reservert av noen andre", async ({ page }) => {
    // Override the brev GET to return 423 BEFORE navigation
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1?reserver=true", (route) =>
      route.fulfill({
        status: 423,
        path: "test/e2e/fixtures/brevreservasjon_opptatt.json",
        contentType: "application/json",
      }),
    );

    await page.goto("/saksnummer/123456/brev/1");

    await expect(page.getByText("Saksbehandlingstiden vår er vanligvis 10 uker.")).toHaveCount(0);
    await expect(page.getByText("Brevet redigeres av noen andre")).toBeVisible();
  });

  test("kan tilbakestille malen", async ({ page }) => {
    await page.route("**/bff/skribenten-backend/brev/1/tilbakestill", (route) =>
      route.fulfill({ path: "test/e2e/fixtures/brevResponse.json", contentType: "application/json" }),
    );

    await page.goto("/saksnummer/123456/brev/1");
    await expect(page.getByText("Lagret")).toBeVisible();

    await page.getByText("Dersom vi trenger flere opplysninger").click();
    await page.locator(":focus").pressSequentially(" hello!");

    await dataE2E(page, "tilbakestill-mal-button").click();
    await expect(page.getByText("Vil du tilbakestille brevmalen?")).toBeVisible();
    await expect(page.getByText("Innholdet du har endret eller lagt til i brevet vil bli slettet.")).toBeVisible();
    await expect(page.getByText("Du kan ikke angre denne handlingen.")).toBeVisible();
    await page.getByText("Ja, tilbakestill malen").click();
    await expect(page.getByText("hello!")).toHaveCount(0);
  });

  test("beholder brevet etter å ville tilbakestille", async ({ page }) => {
    await page.goto("/saksnummer/123456/brev/1");
    await expect(page.getByText("Lagret")).toBeVisible();

    await page.getByText("Dersom vi trenger flere opplysninger").click();
    await page.locator(":focus").pressSequentially(" hello!");

    await dataE2E(page, "tilbakestill-mal-button").click();
    await page.getByText("Nei, behold brevet").click();
    await expect(page.getByText("hello!")).toBeVisible();
  });

  test("kan ikke redigere et arkivert brev", async ({ page }) => {
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1?reserver=true", (route) =>
      route.fulfill({ status: 409 }),
    );

    await page.goto("/saksnummer/123456/brev/1");
    await expect(page.getByText("Brevet er arkivert, og kan derfor ikke redigeres.")).toBeVisible();
    await page.getByText("Gå til brevbehandler").click();
    await expect(page).toHaveURL(/\/saksnummer\/123456\/brevbehandler$/);
  });

  test("kan toggle punktliste", async ({ page }) => {
    await page.goto("/saksnummer/123456/brev/1");

    await page.getByText("Du må melde").click();
    await dataE2E(page, "editor-bullet-list").click();
    await expect(page.locator("ul li span").last()).toContainText("Du må melde");

    await dataE2E(page, "editor-bullet-list").click();
    await expect(page.locator("ul li span").last()).not.toContainText("Du må melde");
  });

  test("lagrer ikke brev som har tomme non-nullable enum felt", async ({ page }) => {
    // Load and modify the model specification
    const spec = JSON.parse(JSON.stringify(modelSpecificationBrukertestBrevPensjon2025));
    spec.types[`${spec.letterModelTypeName}.SaksbehandlerValg`].utsiktenFraKontoret.nullable = false;
    spec.types[`${spec.letterModelTypeName}.SaksbehandlerValg`].denBesteKaken.nullable = false;

    await page.route("**/bff/skribenten-backend/brevmal/BRUKERTEST_BREV_PENSJON_2025/modelSpecification", (route) =>
      route.fulfill({ json: spec }),
    );

    // Load and modify the brev response
    const brev = JSON.parse(JSON.stringify(brevResponseTestBrev));
    brev.saksbehandlerValg = {};

    await page.route("**/bff/skribenten-backend/sak/123456/brev/1?reserver=true", (route) =>
      route.fulfill({ json: brev }),
    );

    await page.goto("/saksnummer/123456/brev/1");
    await page.getByText("Overstyring").click();

    // Check first radio group
    const radioGroup1Option1 = page.getByText("Utsikten fra kontoret").locator("..").locator('[type="radio"]').first();
    await expect(radioGroup1Option1).not.toBeChecked();
    await radioGroup1Option1.click();
    await expect(radioGroup1Option1).toBeChecked();

    // Check second radio group
    const radioGroup2Option1 = page.getByText("Den beste kaken er").locator("..").locator('[type="radio"]').first();
    await expect(radioGroup2Option1).not.toBeChecked();

    // Click Fortsett - should stay on same page with validation error
    await page.getByText("Fortsett", { exact: true }).click();
    await expect(page).toHaveURL(/\/saksnummer\/123456\/brev\/1$/);
    await expect(page.getByText("Obligatorisk: du må velge et alternativ")).toBeVisible();

    // Fix the second radio group - error should disappear
    await radioGroup2Option1.click();
    await expect(radioGroup2Option1).toBeChecked();
    await expect(page.getByText("Obligatorisk: du må velge et alternativ")).toHaveCount(0);
  });

  test("lagrer brev som har tomme nullable enum felt", async ({ page }) => {
    // Load and modify the model specification
    const spec = JSON.parse(JSON.stringify(modelSpecificationBrukertestBrevPensjon2025));
    spec.types[`${spec.letterModelTypeName}.SaksbehandlerValg`].utsiktenFraKontoret.nullable = true;
    spec.types[`${spec.letterModelTypeName}.SaksbehandlerValg`].denBesteKaken.nullable = true;

    await page.route("**/bff/skribenten-backend/brevmal/BRUKERTEST_BREV_PENSJON_2025/modelSpecification", (route) =>
      route.fulfill({ json: spec }),
    );

    // Load and modify the brev response
    const brev = JSON.parse(JSON.stringify(brevResponseTestBrev));
    brev.saksbehandlerValg = {};

    await page.route("**/bff/skribenten-backend/sak/123456/brev/1?reserver=true", (route) =>
      route.fulfill({ json: brev }),
    );

    await page.goto("/saksnummer/123456/brev/1");
    await expect(page.getByText("Overstyring")).toHaveCount(0);

    // Check all radios are unchecked
    const radios1 = page.getByText("Utsikten fra kontoret").locator("..").locator('[type="radio"]');
    const count1 = await radios1.count();
    for (let i = 0; i < count1; i++) {
      await expect(radios1.nth(i)).not.toBeChecked();
    }
    const radios2 = page.getByText("Den beste kaken er").locator("..").locator('[type="radio"]');
    const count2 = await radios2.count();
    for (let i = 0; i < count2; i++) {
      await expect(radios2.nth(i)).not.toBeChecked();
    }

    // Click Fortsett - should stay on page but no validation error
    await page.getByText("Fortsett", { exact: true }).click();
    await expect(page).toHaveURL(/\/saksnummer\/123456\/brev\/1$/);
    await expect(page.getByText("Obligatorisk: du må velge et alternativ")).toHaveCount(0);
  });
});
