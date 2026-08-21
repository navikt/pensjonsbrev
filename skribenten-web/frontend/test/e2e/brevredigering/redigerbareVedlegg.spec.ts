import { expect, test } from "@playwright/test";

import { setupSakStubs } from "../utils/helpers";

const VEDLEGG_ID = "vedlegg-om-alderspensjon";
const VEDLEGG_TITTEL = "Opplysninger om alderspensjon";
const VEDLEGG_BROEDTEKST = "Vedleggsteksten som saksbehandler kan redigere.";

const literal = (text: string, id: number) => ({
  type: "LITERAL",
  id: id,
  text: text,
  editedText: null,
  tags: [],
});

const vedlegg = {
  includeSakspart: false,
  title: { text: [literal(VEDLEGG_TITTEL, 901)], deletedContent: [] },
  deletedBlocks: [],
  blocks: [
    {
      id: 910,
      type: "PARAGRAPH",
      editable: true,
      deletedContent: [],
      content: [literal(VEDLEGG_BROEDTEKST, 912)],
    },
  ],
};

test.describe("Redigerbare vedlegg", () => {
  test.beforeEach(async ({ page }) => {
    await setupSakStubs(page);

    await page.route("**/bff/skribenten-backend/sak/123456/brev/1?reserver=true", (route) =>
      route.fulfill({ path: "test/e2e/fixtures/brevResponse.json", contentType: "application/json" }),
    );
    await page.route(
      "**/bff/skribenten-backend/brevmal/INFORMASJON_OM_SAKSBEHANDLINGSTID/modelSpecification",
      (route) => route.fulfill({ path: "test/e2e/fixtures/modelSpecification.json", contentType: "application/json" }),
    );
    await page.route("**/bff/skribenten-backend/brev/1/reservasjon", (route) =>
      route.fulfill({ path: "test/e2e/fixtures/brevreservasjon.json", contentType: "application/json" }),
    );

    await page.route("**/bff/skribenten-backend/sak/123456/brev/1/redigerbareVedlegg", (route) =>
      route.fulfill({ json: [{ vedleggId: VEDLEGG_ID, tittel: VEDLEGG_TITTEL }] }),
    );
    await page.route(`**/bff/skribenten-backend/sak/123456/brev/1/redigerbareVedlegg/${VEDLEGG_ID}`, (route) =>
      route.fulfill({ json: vedlegg }),
    );
  });

  test("åpner brevet på brevmal-fanen", async ({ page }) => {
    await page.goto("/saksnummer/123456/brev/1");

    await expect(page.getByText("Saksbehandlingstiden vår er vanligvis 10 uker.")).toBeVisible();
    await expect(page.getByRole("tab", { name: "Brevmal" })).toHaveAttribute("aria-selected", "true");
    await expect(page.getByText(VEDLEGG_BROEDTEKST)).toBeHidden();
  });

  test("viser vedlegget i redigeringsfeltet når det åpnes fra vedlegg-fanen", async ({ page }) => {
    await page.goto("/saksnummer/123456/brev/1");
    await expect(page.getByText("Saksbehandlingstiden vår er vanligvis 10 uker.")).toBeVisible();

    await page.getByRole("tab", { name: "Vedlegg" }).click();
    await page.getByRole("button", { name: VEDLEGG_TITTEL }).click();

    await expect(page.getByText(VEDLEGG_BROEDTEKST)).toBeVisible();
    await expect(page.getByText("Saksbehandlingstiden vår er vanligvis 10 uker.")).toBeHidden();
    await expect(page).toHaveURL(new RegExp(`vedlegg=${VEDLEGG_ID}`));
  });

  test("dyplenke til et vedlegg åpner vedlegg-fanen", async ({ page }) => {
    await page.goto(`/saksnummer/123456/brev/1?vedlegg=${VEDLEGG_ID}`);

    await expect(page.getByRole("tab", { name: "Vedlegg" })).toHaveAttribute("aria-selected", "true");
    await expect(page.getByText(VEDLEGG_BROEDTEKST)).toBeVisible();
  });

  test("går tilbake til brevet når vedlegget lukkes", async ({ page }) => {
    await page.goto(`/saksnummer/123456/brev/1?vedlegg=${VEDLEGG_ID}`);
    await expect(page.getByText(VEDLEGG_BROEDTEKST)).toBeVisible();

    await page.getByRole("button", { name: VEDLEGG_TITTEL }).click();

    await expect(page.getByText("Saksbehandlingstiden vår er vanligvis 10 uker.")).toBeVisible();
  });
});
