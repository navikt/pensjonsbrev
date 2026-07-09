import fs from "node:fs";
import path from "node:path";

import { expect, test } from "@playwright/test";

import { setupSakStubs } from "../utils/helpers";

const fixturesDir = path.resolve("test/e2e/fixtures");
const brevResponse = JSON.parse(fs.readFileSync(path.join(fixturesDir, "brevResponse.json"), "utf-8"));

// Simulates the backend having flagged a block as missingFromTemplate: the saksbehandler edited
// this block previously, but it can no longer be matched against a fresh render of the template
// (see UpdateEditedLetter.mergeList/FerdigRedigertPolicy on the backend).
const brevMedAvsnittIkkeIMal = JSON.parse(JSON.stringify(brevResponse));
brevMedAvsnittIkkeIMal.redigertBrev.blocks[0].missingFromTemplate = true;
// Fill in the (nullable, but flagged-as-missing-until-set) tekstvalg field so that the
// tekstValg warning doesn't take priority over the avsnittIkkeIMal warning we're testing here.
brevMedAvsnittIkkeIMal.saksbehandlerValg.ettEllerIngenAvMangeAlternativer = "RULLEKAKE";

test.describe("Avsnitt som ikke finnes i malen (missingFromTemplate)", () => {
  test.beforeEach(async ({ page }) => {
    await setupSakStubs(page);

    await page.route("**/bff/skribenten-backend/sak/123456/brev/1?reserver=true", (route) =>
      route.fulfill({ json: brevMedAvsnittIkkeIMal }),
    );

    await page.route(
      "**/bff/skribenten-backend/brevmal/INFORMASJON_OM_SAKSBEHANDLINGSTID/modelSpecification",
      (route) => route.fulfill({ path: "test/e2e/fixtures/modelSpecification.json", contentType: "application/json" }),
    );

    await page.route("**/bff/skribenten-backend/brev/1/reservasjon", (route) =>
      route.fulfill({ path: "test/e2e/fixtures/brevreservasjon.json", contentType: "application/json" }),
    );

    await page.route(
      (url: URL) => url.pathname.includes("/sak/123456/brev/1") && !url.search.includes("reserver"),
      async (route) => {
        if (route.request().method() !== "PUT") {
          return route.fallback();
        }
        return route.fulfill({ json: brevMedAvsnittIkkeIMal });
      },
    );

    await page.route("**/bff/skribenten-backend/brev/1/redigertBrev?frigiReservasjon=*", async (route) => {
      if (route.request().method() === "PUT") {
        return route.fulfill({ json: brevMedAvsnittIkkeIMal });
      }
      return route.fallback();
    });
  });

  test("viser advarsel på avsnitt som ikke lenger kan kobles til malen", async ({ page }) => {
    await page.goto("/saksnummer/123456/brev/1");

    await expect(page.getByText("Saksbehandlingstiden vår er vanligvis")).toBeVisible();
    await expect(page.locator(".missing-from-template-block")).toBeVisible();
  });

  test("varsler om uhåndtert avsnitt ved klikk på Fortsett", async ({ page }) => {
    await page.goto("/saksnummer/123456/brev/1");

    await page.getByText("Fortsett", { exact: true }).click();

    await expect(page.getByText("Du må velge om du vil beholde eller slette 1 avsnitt")).toBeVisible();
    await expect(
      page.getByText(
        "Disse avsnittene er markert i brevet. Velg «Behold» eller «Slett» for hvert av dem. Du kan fortsette, men brevet kan ikke sendes før dette er gjort.",
      ),
    ).toBeVisible();

    // Saksbehandler can still choose to continue to brevbehandler despite the warning.
    await page.getByText("Fortsett til brevbehandler").click();
    await expect(page).toHaveURL(/\/saksnummer\/123456\/brevbehandler\?brevId=1/);
  });

  test("Behold beholder avsnittsteksten og fjerner markeringen", async ({ page }) => {
    await page.goto("/saksnummer/123456/brev/1");

    await expect(page.locator(".missing-from-template-block")).toBeVisible();

    await page.getByRole("button", { name: "Behold" }).click();

    // Content is kept, but the block is no longer flagged and the actions are gone.
    await expect(page.getByText("Saksbehandlingstiden vår er vanligvis")).toBeVisible();
    await expect(page.locator(".missing-from-template-block")).toBeHidden();
    await expect(page.getByRole("button", { name: "Behold" })).toBeHidden();
    await expect(page.getByRole("button", { name: "Slett" })).toBeHidden();
  });

  test("Slett fjerner avsnittet fra brevet", async ({ page }) => {
    await page.goto("/saksnummer/123456/brev/1");

    await expect(page.locator(".missing-from-template-block")).toBeVisible();

    await page.getByRole("button", { name: "Slett" }).click();

    // The whole block is removed, so its text and the flagged styling are gone.
    await expect(page.getByText("Saksbehandlingstiden vår er vanligvis")).toBeHidden();
    await expect(page.locator(".missing-from-template-block")).toBeHidden();
  });
});
