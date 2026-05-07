import { expect, test } from "@playwright/test";

import modelSpecificationBrukertestBrevPensjon2025 from "../fixtures/modelSpecificationBrukertestBrevPensjon2025.json" with {
  type: "json",
};
import { setupSakStubs } from "../utils/helpers";

test.describe("Oppretter brevbakerbrev", () => {
  test.beforeEach(async ({ page }) => {
    await setupSakStubs(page);
  });

  test("kun obligatoriske felter vises ved opprettelse av brev", async ({ page }) => {
    await page.route(
      "**/bff/skribenten-backend/brevmal/INFORMASJON_OM_SAKSBEHANDLINGSTID/modelSpecification",
      (route) => {
        return route.fulfill({ path: "test/e2e/fixtures/modelSpecification.json", contentType: "application/json" });
      },
    );

    await page.goto("saksnummer/123456/brevvelger?templateId=INFORMASJON_OM_SAKSBEHANDLINGSTID");
    await expect(page.getByLabel("Mottatt søknad")).toBeVisible();
    await expect(page.getByLabel("Ytelse")).toBeVisible();
    await expect(page.getByLabel("Land")).not.toBeVisible();
    await expect(page.getByLabel("inkluder venter svar afp")).not.toBeVisible();
    await expect(page.getByLabel("Svartid uker")).toBeVisible();
  });

  test("validering trigges", async ({ page }) => {
    await page.route(
      "**/bff/skribenten-backend/brevmal/INFORMASJON_OM_SAKSBEHANDLINGSTID/modelSpecification",
      (route) => {
        return route.fulfill({ path: "test/e2e/fixtures/modelSpecification.json", contentType: "application/json" });
      },
    );

    const modelSpecResponsePromise = page.waitForResponse(
      (r) => r.url().includes("/brevmal/INFORMASJON_OM_SAKSBEHANDLINGSTID/modelSpecification") && r.status() === 200,
    );
    await page.goto("/saksnummer/123456/brevvelger?templateId=INFORMASJON_OM_SAKSBEHANDLINGSTID");
    await modelSpecResponsePromise;
    await page.getByRole("button", { name: "Fortsett" }).click();
    await expect(page.locator(".aksel-error-message")).toHaveCount(4);
  });

  test("oppretter brev", async ({ page }) => {
    await page.route(
      "**/bff/skribenten-backend/brevmal/INFORMASJON_OM_SAKSBEHANDLINGSTID/modelSpecification",
      (route) => {
        return route.fulfill({ path: "test/e2e/fixtures/modelSpecification.json", contentType: "application/json" });
      },
    );

    let requestBody: Record<string, unknown> | undefined;
    await page.route("**/bff/skribenten-backend/sak/123456/brev", async (route) => {
      if (route.request().method() === "POST") {
        requestBody = route.request().postDataJSON();
        return route.fulfill({ path: "test/e2e/fixtures/brevResponse.json", contentType: "application/json" });
      }
      return route.fallback();
    });

    await page.goto("/saksnummer/123456/brevvelger?templateId=INFORMASJON_OM_SAKSBEHANDLINGSTID");

    await page.locator("select[name=enhetsId]").selectOption({ label: "Nav Arbeid og ytelser Innlandet" });
    await expect(page.locator("select[name=spraak]")).toHaveValue("NB");

    await page.getByLabel("Mottatt søknad").click();
    await page.locator(":focus").pressSequentially("09.10.2024");
    await page.getByLabel("Ytelse").click();
    await page.locator(":focus").pressSequentially("Alderspensjon");
    await page.getByLabel("Svartid uker").click();
    await page.locator(":focus").pressSequentially("4");
    await page.getByRole("button", { name: "Fortsett" }).click();

    expect(requestBody).toEqual({
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

    await expect(page).toHaveURL(/\/saksnummer\/123456\/brev\/1/);
    expect(page.url()).toContain("enhetsId");
  });

  test("oppretter brev som har non-nullable boolean felt", async ({ page }) => {
    await page.route(
      "**/bff/skribenten-backend/brevmal/UT_ORIENTERING_OM_SAKSBEHANDLINGSTID/modelSpecification",
      (route) => {
        return route.fulfill({
          path: "test/e2e/fixtures/modelSpecificationOrienteringOmSaksbehandlingstid.json",
          contentType: "application/json",
        });
      },
    );

    let requestBody: Record<string, unknown> | undefined;
    await page.route("**/bff/skribenten-backend/sak/123456/brev", async (route) => {
      if (route.request().method() === "POST") {
        requestBody = route.request().postDataJSON();
        return route.fulfill({ path: "test/e2e/fixtures/brevResponse.json", contentType: "application/json" });
      }
      return route.fallback();
    });

    await page.goto("/saksnummer/123456/brevvelger?templateId=UT_ORIENTERING_OM_SAKSBEHANDLINGSTID");

    await page.locator("select[name=enhetsId]").selectOption({ label: "Nav Arbeid og ytelser Innlandet" });
    await expect(page.locator("select[name=spraak]")).toHaveValue("NB");

    await page.getByLabel("Mottatt søknad").click();
    await page.locator(":focus").pressSequentially("09.10.2024");
    await page.getByRole("button", { name: "Fortsett" }).click();

    expect(requestBody).toEqual({
      brevkode: "UT_ORIENTERING_OM_SAKSBEHANDLINGSTID",
      spraak: "NB",
      avsenderEnhetsId: "4405",
      mottaker: null,
      saksbehandlerValg: {
        mottattSøknad: "2024-10-09",
        søknadOversendesTilUtlandet: false,
      },
      vedtaksId: null,
    });

    await expect(page).toHaveURL(/\/saksnummer\/123456\/brev\/1/);
    expect(page.url()).toContain("enhetsId");
  });

  test("kan opprette brev som har tom saksbehandlerValg", async ({ page }) => {
    await page.route(
      "**/bff/skribenten-backend/brevmal/PE_BEKREFTELSE_PAA_FLYKTNINGSTATUS/modelSpecification",
      (route) => {
        return route.fulfill({ json: { types: {}, letterModelTypeName: null } });
      },
    );

    let requestBody: Record<string, unknown> | undefined;
    await page.route("**/bff/skribenten-backend/sak/123456/brev", async (route) => {
      if (route.request().method() === "POST") {
        requestBody = route.request().postDataJSON();
        return route.fulfill({ path: "test/e2e/fixtures/brevResponse.json", contentType: "application/json" });
      }
      return route.fallback();
    });

    await page.goto("saksnummer/123456/brevvelger?templateId=PE_BEKREFTELSE_PAA_FLYKTNINGSTATUS");
    await page.locator("select[name=enhetsId]").selectOption({ label: "Nav Arbeid og ytelser Innlandet" });
    await page.getByRole("button", { name: "Fortsett" }).click();

    expect(requestBody).toEqual({
      brevkode: "PE_BEKREFTELSE_PAA_FLYKTNINGSTATUS",
      spraak: "NB",
      avsenderEnhetsId: "4405",
      mottaker: null,
      saksbehandlerValg: {},
      vedtaksId: null,
    });

    await expect(page).toHaveURL(/\/saksnummer\/123456\/brev\/1/);
    expect(page.url()).toContain("enhetsId");
  });

  test("oppretter brev som har tomme nullable enum felt", async ({ page }) => {
    const spec = JSON.parse(JSON.stringify(modelSpecificationBrukertestBrevPensjon2025));
    spec.types[`${spec.letterModelTypeName}.SaksbehandlerValg`].utsiktenFraKontoret.nullable = true;
    spec.types[`${spec.letterModelTypeName}.SaksbehandlerValg`].denBesteKaken.nullable = true;

    await page.route("**/bff/skribenten-backend/brevmal/BRUKERTEST_BREV_PENSJON_2025/modelSpecification", (route) => {
      return route.fulfill({ json: spec });
    });

    await page.route("**/bff/skribenten-backend/sak/123456/brev", async (route) => {
      if (route.request().method() === "POST") {
        return route.fulfill({ path: "test/e2e/fixtures/brevResponseTestBrev.json", contentType: "application/json" });
      }
      return route.fallback();
    });

    const modelSpecResponsePromise = page.waitForResponse(
      (r) => r.url().includes("/brevmal/BRUKERTEST_BREV_PENSJON_2025/modelSpecification") && r.status() === 200,
    );
    await page.goto("/saksnummer/123456/brevvelger?templateId=BRUKERTEST_BREV_PENSJON_2025");
    await modelSpecResponsePromise;

    await expect(page.getByRole("heading", { name: "Brevvelger" })).toBeVisible();

    await page.locator("select[name=enhetsId]").selectOption({ label: "Nav Arbeid og ytelser Innlandet" });

    await expect(page.getByText("Mot trær og natur")).not.toBeVisible();

    await page.getByRole("button", { name: "Fortsett" }).click();

    await expect(page.getByText("Obligatorisk: du må velge et alternativ").first()).not.toBeVisible();

    await expect(page).toHaveURL(/\/saksnummer\/123456\/brev\/1/);
    expect(page.url()).toContain("enhetsId");
  });

  test("oppretter ikke brev som har tomme non-nullable enum felt", async ({ page }) => {
    const spec = JSON.parse(JSON.stringify(modelSpecificationBrukertestBrevPensjon2025));
    spec.types[`${spec.letterModelTypeName}.SaksbehandlerValg`].utsiktenFraKontoret.nullable = false;
    spec.types[`${spec.letterModelTypeName}.SaksbehandlerValg`].denBesteKaken.nullable = false;

    await page.route("**/bff/skribenten-backend/brevmal/BRUKERTEST_BREV_PENSJON_2025/modelSpecification", (route) => {
      return route.fulfill({ json: spec });
    });

    await page.route("**/bff/skribenten-backend/sak/123456/brev", async (route) => {
      if (route.request().method() === "POST") {
        return route.fulfill({ path: "test/e2e/fixtures/brevResponseTestBrev.json", contentType: "application/json" });
      }
      return route.fallback();
    });

    const modelSpecResponsePromise = page.waitForResponse(
      (r) => r.url().includes("/brevmal/BRUKERTEST_BREV_PENSJON_2025/modelSpecification") && r.status() === 200,
    );
    await page.goto("/saksnummer/123456/brevvelger?templateId=BRUKERTEST_BREV_PENSJON_2025");
    await modelSpecResponsePromise;
    await expect(page.getByRole("heading", { name: "Brevvelger" })).toBeVisible();

    await page.locator("select[name=enhetsId]").selectOption({ label: "Nav Arbeid og ytelser Innlandet" });
    await expect(page.locator("select[name=spraak]")).toHaveValue("NB");

    await page.getByRole("button", { name: "Fortsett" }).click();

    await expect(page).not.toHaveURL(/\/saksnummer\/123456\/brev\/1/);
    await expect(page.getByText("Obligatorisk: du må velge et alternativ").first()).toBeVisible();

    await page.getByLabel("Mot trær og natur").click();
    await page.getByLabel("Ostekake").click();
    await expect(page.getByText("@errorMessage")).not.toBeVisible();

    await page.getByRole("button", { name: "Fortsett" }).click();
    await expect(page).toHaveURL(/\/saksnummer\/123456\/brev\/1/);
    expect(page.url()).toContain("enhetsId");
  });
});
