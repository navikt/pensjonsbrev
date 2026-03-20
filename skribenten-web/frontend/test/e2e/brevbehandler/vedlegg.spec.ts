import { expect, type Page, test } from "@playwright/test";

import { SpraakKode } from "~/types/apiTypes";
import { type AlltidValgbartVedlegg } from "~/types/brev";

import { nyBrevInfo, nyBrevResponse } from "../../utils/brevredigeringTestUtils";
import { setupSakStubs } from "../utils/helpers";

const bokmaalBrev = nyBrevInfo({
  spraak: SpraakKode.Bokmaal,
  status: { type: "Kladd" },
});

const alleTilgjengelige: AlltidValgbartVedlegg[] = [
  {
    kode: "SKJEMA_FOR_BANKOPPLYSNINGER",
    visningstekst: "Skjema for bankopplysninger",
    spraak: ["BOKMAL", "NYNORSK"],
    tilgjengeligForSpraak: true,
  },
  { kode: "UTTAKSSKJEMA", visningstekst: "Uttaksskjema", spraak: ["BOKMAL", "ENGLISH"], tilgjengeligForSpraak: true },
];

const noenUtilgjengelige: AlltidValgbartVedlegg[] = [
  {
    kode: "SKJEMA_FOR_BANKOPPLYSNINGER",
    visningstekst: "Skjema for bankopplysninger",
    spraak: ["BOKMAL", "NYNORSK"],
    tilgjengeligForSpraak: true,
  },
  { kode: "UTTAKSSKJEMA", visningstekst: "Uttaksskjema", spraak: ["ENGLISH"], tilgjengeligForSpraak: false },
];

const ingenTilgjengelige: AlltidValgbartVedlegg[] = [
  {
    kode: "SKJEMA_FOR_BANKOPPLYSNINGER",
    visningstekst: "Skjema for bankopplysninger",
    spraak: ["ENGLISH"],
    tilgjengeligForSpraak: false,
  },
  {
    kode: "UTTAKSSKJEMA",
    visningstekst: "Uttaksskjema",
    spraak: ["ENGLISH", "NYNORSK"],
    tilgjengeligForSpraak: false,
  },
];

async function openBrevCard(page: Page, title: string) {
  const card = page.locator(`[aria-label="${title}"]`).first();
  await expect(card).toBeVisible();
  await card.getByRole("button").click();
}

async function setupBrevbehandler(page: Page, vedlegg: AlltidValgbartVedlegg[]) {
  await setupSakStubs(page);

  await page.route("**/bff/skribenten-backend/sak/123456/brev", (route) => {
    if (route.request().method() === "GET") {
      return route.fulfill({ json: [bokmaalBrev] });
    }
    return route.fallback();
  });
  await page.route("**/bff/skribenten-backend/sak/123456/brev/1?reserver=true", (route) => {
    if (route.request().method() === "GET") {
      return route.fulfill({ json: nyBrevResponse({ info: bokmaalBrev }) });
    }
    return route.fallback();
  });
  await page.route("**/bff/skribenten-backend/sak/123456/brev/1?reserver=false", (route) => {
    if (route.request().method() === "GET") {
      return route.fulfill({ json: nyBrevResponse({ info: bokmaalBrev }) });
    }
    return route.fallback();
  });
  await page.route("**/bff/skribenten-backend/sak/123456/brev/*/alltidValgbareVedlegg", (route) => {
    return route.fulfill({ json: vedlegg });
  });

  await page.goto("/saksnummer/123456/brevbehandler");
  await openBrevCard(page, bokmaalBrev.brevtittel);
}

async function openVedleggModal(page: Page) {
  await page.locator('button[title="Legg til vedlegg"]').click();
}

test.describe("Vedlegg modal - viser tilgjengelighet basert på brevets språk", () => {
  test("alle vedlegg er tilgjengelige", async ({ page }) => {
    await setupBrevbehandler(page, alleTilgjengelige);
    await openVedleggModal(page);

    await expect(page.getByText("Skjema for bankopplysninger")).toBeVisible();
    await expect(page.getByText("Uttaksskjema")).toBeVisible();
    await expect(page.getByText("Bokmål, Nynorsk")).toBeVisible();
    await expect(page.getByText("Bokmål, Engelsk")).toBeVisible();
  });

  test("noen vedlegg er utilgjengelige", async ({ page }) => {
    await setupBrevbehandler(page, noenUtilgjengelige);
    await openVedleggModal(page);

    await expect(page.getByText("Noen skjemaer er ikke tilgjengelig på språket i brevet.")).toBeVisible();
    await expect(page.getByText("Skjemaer som ikke er tilgjengelig på Bokmål")).toBeVisible();
    await expect(page.getByText("Skjema for bankopplysninger")).toBeVisible();
    await expect(page.getByText("Uttaksskjema")).toBeVisible();
  });

  test("ingen vedlegg er tilgjengelige", async ({ page }) => {
    await setupBrevbehandler(page, ingenTilgjengelige);
    await openVedleggModal(page);

    await expect(page.getByText("Ingen skjemaer er tilgjengelig basert på språket i brevet.")).toBeVisible();
    await expect(page.getByText("Hvorfor kan jeg ikke legge til skjema på et annet språk/målform?")).toBeVisible();
    await expect(page.getByText("Skjema for bankopplysninger")).toBeVisible();
    await expect(page.getByText("Uttaksskjema")).toBeVisible();
  });
});
