import { expect, test } from "@playwright/test";
import { formatISO } from "date-fns";

import { AUTOSAVE_TIMER } from "~/components/ManagedLetterEditor/autosave_timer";

import { nyBrevResponse } from "../../utils/brevredigeringTestUtils";
import { setupSakStubs } from "../utils/helpers";

const defaultBrev = nyBrevResponse({});

test.describe("attestant redigering", () => {
  test.beforeEach(async ({ page }) => {
    await setupSakStubs(page);

    await page.route("**/bff/skribenten-backend/sak/123456/brev/1/attestering?reserver=true", (route) => {
      if (route.request().method() === "GET") {
        return route.fulfill({ json: defaultBrev });
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
  });

  test("Autolagrer brev etter redigering", async ({ page }) => {
    const hurtiglagreTidspunkt = formatISO(new Date());

    await page.route("**/bff/skribenten-backend/brev/1/redigertBrev?frigiReservasjon=false", async (route) => {
      if (route.request().method() === "PUT") {
        const body = route.request().postDataJSON();
        return route.fulfill({
          json: {
            ...defaultBrev,
            info: { ...defaultBrev.info, sistredigert: hurtiglagreTidspunkt },
            redigertBrev: body,
          },
        });
      }
      return route.fallback();
    });

    await page.goto("/saksnummer/123456/attester/1/redigering");
    await expect(page.getByText("Lagret")).toBeVisible();
    await page.clock.install();
    const textSavePromise = page.waitForResponse(
      (r) => r.url().includes("/redigertBrev?frigiReservasjon=false") && r.request().method() === "PUT",
    );
    await page.getByText("weeks.").click();
    await page.keyboard.type(" hello!");
    await page.clock.fastForward(AUTOSAVE_TIMER);
    await textSavePromise;
    await expect(page.getByText("Lagret")).toBeVisible();
    await expect(page.getByText("hello!")).toBeVisible();
  });

  test("lagrer underskrift", async ({ page }) => {
    await page.route("**/bff/skribenten-backend/brev/1/redigertBrev?frigiReservasjon=false", async (route) => {
      if (route.request().method() === "PUT") {
        const body = route.request().postDataJSON();
        return route.fulfill({
          json: { ...defaultBrev, redigertBrev: body },
        });
      }
      return route.fallback();
    });

    await page.goto("/saksnummer/123456/attester/1/redigering");
    await page.clock.install();
    const signatureSavePromise = page.waitForResponse(
      (r) => r.url().includes("/redigertBrev?frigiReservasjon=false") && r.request().method() === "PUT",
    );
    await page.getByRole("textbox", { name: "Underskrift" }).fill("Den nye attestanten");
    await page.clock.fastForward(AUTOSAVE_TIMER);
    await signatureSavePromise;

    await expect(page.getByText("Den nye attestanten")).toBeVisible();
  });

  test("Blokkerer redigering om brev er reservert av noen andre", async ({ page }) => {
    await page.goto("/saksnummer/123456/attester/1/redigering");

    await page.route("**/bff/skribenten-backend/brev/1/reservasjon", (route) => {
      return route.fulfill({
        path: "test/e2e/fixtures/brevreservasjon_opptatt.json",
        contentType: "application/json",
      });
    });
    await page.waitForResponse((r) => r.url().includes("/brev/1/reservasjon"));

    await expect(page.getByText("Brevet er utilgjengelig for deg fordi Hugo Weaving har brevet åpent.")).toBeVisible();
    await expect(page.getByText("Nei, gå til brevbehandler")).toBeVisible();
    await page.getByText("Nei, gå til brevbehandler").click();
    await expect(page).toHaveURL(/\/saksnummer\/123456\/brevbehandler\?enhetsId=0001/);
  });

  test("Gjenoppta redigering", async ({ page }) => {
    await page.goto("/saksnummer/123456/attester/1/redigering");

    await page.route("**/bff/skribenten-backend/brev/1/reservasjon", (route) => {
      return route.fulfill({
        path: "test/e2e/fixtures/brevreservasjon_opptatt.json",
        contentType: "application/json",
      });
    });
    await page.waitForResponse((r) => r.url().includes("/brev/1/reservasjon"));

    await expect(page.getByText("Brevet er utilgjengelig for deg fordi Hugo Weaving har brevet åpent.")).toBeVisible();

    await page.route("**/bff/skribenten-backend/sak/123456/brev/1/attestering?reserver=true", (route) => {
      if (route.request().method() === "GET") {
        return route.fulfill({
          path: "test/e2e/fixtures/brevResponse_ny_hash.json",
          contentType: "application/json",
        });
      }
      return route.fallback();
    });
    await page.route("**/bff/skribenten-backend/brev/1/reservasjon", (route) => {
      return route.fulfill({
        path: "test/e2e/fixtures/brevreservasjon_ny_hash.json",
        contentType: "application/json",
      });
    });

    const reopenResponsePromise = page.waitForResponse((r) => r.url().includes("/attestering?reserver=true"));
    await page.getByText("Ja, åpne på nytt").click();
    await reopenResponsePromise;

    await expect(page.getByText("Informasjon om saksbehandlingstiden vår med ny hash")).toBeVisible();
  });
});
