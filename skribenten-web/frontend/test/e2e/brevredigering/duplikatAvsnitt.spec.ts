import fs from "node:fs";
import path from "node:path";

import { expect, test } from "@playwright/test";

import { setupSakStubs } from "../utils/helpers";

const fixturesDir = path.resolve("test/e2e/fixtures");
const brevResponse = JSON.parse(fs.readFileSync(path.join(fixturesDir, "brevResponse.json"), "utf-8"));

// Simulates the backend having flagged a block as missingFromTemplate: the saksbehandler edited
// this block previously, but it can no longer be matched against a fresh render of the template
// (see UpdateEditedLetter.mergeList/FerdigRedigertPolicy on the backend).
const brevMedDuplikatAvsnitt = JSON.parse(JSON.stringify(brevResponse));
brevMedDuplikatAvsnitt.redigertBrev.blocks[0].missingFromTemplate = true;
// Fill in the (nullable, but flagged-as-missing-until-set) tekstvalg field so that the
// tekstValg warning doesn't take priority over the duplikatAvsnitt warning we're testing here.
brevMedDuplikatAvsnitt.saksbehandlerValg.ettEllerIngenAvMangeAlternativer = "RULLEKAKE";

test.describe("Duplikate/ukoblede avsnitt (missingFromTemplate)", () => {
  test.beforeEach(async ({ page }) => {
    await setupSakStubs(page);

    await page.route("**/bff/skribenten-backend/sak/123456/brev/1?reserver=true", (route) =>
      route.fulfill({ json: brevMedDuplikatAvsnitt }),
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
        return route.fulfill({ json: brevMedDuplikatAvsnitt });
      },
    );

    await page.route("**/bff/skribenten-backend/brev/1/redigertBrev?frigiReservasjon=*", async (route) => {
      if (route.request().method() === "PUT") {
        return route.fulfill({ json: brevMedDuplikatAvsnitt });
      }
      return route.fallback();
    });
  });

  test("viser advarsel på avsnitt som ikke lenger kan kobles til malen", async ({ page }) => {
    await page.goto("/saksnummer/123456/brev/1");

    await expect(page.getByText("Saksbehandlingstiden vår er vanligvis")).toBeVisible();
    await expect(page.locator(".missing-from-template-block")).toBeVisible();
  });

  test("varsler om uhåndtert duplikat avsnitt ved klikk på Fortsett", async ({ page }) => {
    await page.goto("/saksnummer/123456/brev/1");

    await page.getByText("Fortsett", { exact: true }).click();

    await expect(page.getByText("Brevet inneholder 1 avsnitt som ikke lenger er del av malen")).toBeVisible();
    await expect(
      page.getByText(
        "Du kan fortsette til brevbehandler, men brevet kan ikke sendes før avsnittene som ikke lenger kan kobles til malen (markert i brevet) er fjernet eller redigert.",
      ),
    ).toBeVisible();

    // Saksbehandler can still choose to continue to brevbehandler despite the warning.
    await page.getByText("Fortsett til brevbehandler").click();
    await expect(page).toHaveURL(/\/saksnummer\/123456\/brevbehandler\?brevId=1/);
  });
});
