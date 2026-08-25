import { expect, test } from "@playwright/test";
import { formatISO } from "date-fns";

import { AUTOSAVE_TIMER } from "~/components/ManagedLetterEditor/autosave_timer";

import { brevResponse } from "../../utils/letterEditorTestUtils";
import { setupSakStubs } from "../utils/helpers";

const defaultBrev = brevResponse({});
const VEDLEGG_ID = "vedlegg-om-alderspensjon";
const VEDLEGG_TITTEL = "Opplysninger om alderspensjon";
const VEDLEGG_TEKST = "Vedleggsteksten attestanten kan redigere.";

const vedlegg = {
  includeSakspart: false,
  title: {
    text: [{ type: "LITERAL", id: 900, text: VEDLEGG_TITTEL, editedText: null, tags: [] }],
    deletedContent: [],
  },
  deletedBlocks: [],
  blocks: [
    {
      id: 901,
      type: "PARAGRAPH",
      editable: true,
      deletedContent: [],
      content: [{ type: "LITERAL", id: 902, text: VEDLEGG_TEKST, editedText: null, tags: [] }],
    },
  ],
};

const setupVedlegg = async (page: import("@playwright/test").Page) => {
  await page.route("**/bff/skribenten-backend/sak/123456/brev/1/redigerbareVedlegg", (route) =>
    route.fulfill({ json: [{ vedleggId: VEDLEGG_ID, tittel: VEDLEGG_TITTEL }] }),
  );
};

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

  test("kan gå til brevbehandler når henting av vedtaksbrev feiler", async ({ page }) => {
    let requestCount = 0;
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1/attestering?reserver=true", (route) => {
      requestCount++;
      return route.fulfill({ status: 500 });
    });

    await page.goto("/saksnummer/123456/attester/1/redigering");

    await expect(page.getByText("En feil skjedde ved henting av vedtaksbrev")).toBeVisible({ timeout: 15_000 });
    expect(requestCount).toBe(4);
    await page.getByRole("button", { name: "Gå til brevbehandler" }).click();
    await expect(page).toHaveURL(/\/saksnummer\/123456\/brevbehandler\?brevId=1/);
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

  test("lagrer redigert vedlegg før brevet attesteres", async ({ page }) => {
    await setupVedlegg(page);
    const hendelser: string[] = [];

    await page.route(`**/bff/skribenten-backend/sak/123456/brev/1/redigerbareVedlegg/${VEDLEGG_ID}`, async (route) => {
      if (route.request().method() !== "PUT") return route.fulfill({ json: vedlegg });
      hendelser.push("vedlegg-lagring");
      return route.fulfill({ json: route.request().postDataJSON().redigertVedlegg });
    });
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1/attestering?frigiReservasjon=true", (route) => {
      hendelser.push("attestering");
      const body = route.request().postDataJSON();
      return route.fulfill({ json: { ...defaultBrev, ...body } });
    });

    await page.goto("/saksnummer/123456/attester/1/redigering");
    await page.getByRole("textbox", { name: "Underskrift" }).fill("Attestant Testesen");
    await page.getByRole("tab", { name: "Vedlegg" }).click();
    await page.getByText(VEDLEGG_TEKST).click();
    await page.locator(":focus").pressSequentially(" Endret.");

    await page.getByRole("button", { name: "Fortsett" }).click();

    await expect(page).toHaveURL(/\/attester\/1\/forhandsvisning/, { timeout: 15_000 });
    expect(hendelser).toEqual(["vedlegg-lagring", "attestering"]);
  });

  test("attesterer ikke når redigert vedlegg ikke kan lagres", async ({ page }) => {
    await setupVedlegg(page);
    let attesteringer = 0;

    await page.route(`**/bff/skribenten-backend/sak/123456/brev/1/redigerbareVedlegg/${VEDLEGG_ID}`, (route) =>
      route.request().method() === "PUT"
        ? route.fulfill({ status: 500, json: "Lagring feilet" })
        : route.fulfill({ json: vedlegg }),
    );
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1/attestering?frigiReservasjon=true", (route) => {
      attesteringer += 1;
      return route.fulfill({ json: defaultBrev });
    });

    await page.goto("/saksnummer/123456/attester/1/redigering");
    await page.getByRole("textbox", { name: "Underskrift" }).fill("Attestant Testesen");
    await page.getByRole("tab", { name: "Vedlegg" }).click();
    await page.getByText(VEDLEGG_TEKST).click();
    await page.locator(":focus").pressSequentially(" Endret.");

    await page.getByRole("button", { name: "Fortsett" }).click();

    await expect(page.getByText("Klarte ikke lagre")).toBeVisible();
    await expect(page).toHaveURL(/\/attester\/1\/redigering/);
    expect(attesteringer).toBe(0);
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
