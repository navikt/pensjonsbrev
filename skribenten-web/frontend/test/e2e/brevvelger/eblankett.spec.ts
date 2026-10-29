import { expect, type Page, test } from "@playwright/test";

import { type UmamiEventData } from "~/utils/umami";
import { setupSakStubs } from "~test/e2e/support/helpers";

import avtaleland from "../fixtures/avtaleland.json" with { type: "json" };

declare global {
  var eblankettEvents: { name: string; data?: UmamiEventData }[];
}

const orderUrl = "**/bff/skribenten-backend/sak/123456/bestillBrev/exstream/eblankett";

const creationEvents = (page: Page) =>
  page.evaluate(() => globalThis.eblankettEvents.filter((event) => event.name === "brev opprettet"));

test.beforeEach(async ({ page }) => {
  await setupSakStubs(page);
  await page.addInitScript(() => {
    globalThis.open = () => null;
    globalThis.eblankettEvents = [];
    globalThis.umami = {
      track: (name, data) => globalThis.eblankettEvents.push({ name, data }),
    };
  });
});

for (const landkode of ["NOR", "GBR"]) {
  test(`E-blankett bestiller og sporer valgt land ${landkode}`, async ({ page }) => {
    let releaseOrder: () => void = () => {};
    const orderReady = new Promise<void>((resolve) => {
      releaseOrder = resolve;
    });
    await page.route(orderUrl, async (route) => {
      await orderReady;
      await route.fulfill({ path: "test/e2e/fixtures/bestillBrevEblankett.json", contentType: "application/json" });
    });

    await page.goto("/saksnummer/123456/brevvelger?templateId=E001&enhetsId=4815");
    const country = page.getByRole("combobox", { name: "Land", exact: true });
    await expect(country).toHaveValue("NOR");
    await expect(country.locator("option")).toHaveText([
      "Velg land",
      "Norge",
      ...avtaleland.filter((land) => land.kode !== "NOR").map((land) => land.navn),
    ]);
    await expect(page.getByRole("combobox", { name: "Avsenderenhet" })).toHaveValue("4815");
    if (landkode !== "NOR") {
      await country.selectOption(landkode);
    }
    await page.getByRole("textbox", { name: "Mottaker" }).fill("Testmottaker");
    expect(await creationEvents(page)).toEqual([]);

    const orderRequest = page.waitForRequest(
      (request) => request.url().endsWith("/bestillBrev/exstream/eblankett") && request.method() === "POST",
    );
    await page.getByTestId("order-letter").click();
    expect((await orderRequest).postDataJSON()).toEqual({
      brevkode: "E001",
      landkode,
      mottakerText: "Testmottaker",
      enhetsId: "4815",
    });
    expect(await creationEvents(page)).toEqual([]);
    releaseOrder();
    await expect(page.getByTestId("order-letter-success-message")).toBeVisible();
    expect(await creationEvents(page)).toEqual([
      {
        name: "brev opprettet",
        data: {
          brevkode: "E001",
          brevtittel: "E 001 - Anmodning om opplysninger",
          brevsystem: "exstream",
          dokumentkategori: "E_BLANKETT",
          enhetsId: "4815",
          landkode,
        },
      },
    ]);
  });
}

test("E-blankett validerer tomt land og enhet og sporer ikke feil", async ({ page }) => {
  let orders = 0;
  await page.route(orderUrl, async (route) => {
    orders++;
    await route.fulfill({ status: 500, json: { message: "Bestilling feilet" } });
  });
  await page.goto("/saksnummer/123456/brevvelger?templateId=E001");
  const country = page.getByRole("combobox", { name: "Land", exact: true });
  const enhet = page.getByRole("combobox", { name: "Avsenderenhet" });
  await expect(country).toHaveValue("NOR");
  await expect(enhet).toHaveValue("");
  await country.selectOption("");
  await page.getByTestId("order-letter").click();
  await expect(country).toHaveAttribute("aria-invalid", "true");
  await expect(enhet).toHaveAttribute("aria-invalid", "true");
  await expect(page.getByRole("textbox", { name: "Mottaker" })).toHaveAttribute("aria-invalid", "true");
  expect(orders).toBe(0);
  expect(await creationEvents(page)).toEqual([]);

  await country.selectOption("GBR");
  await enhet.selectOption("4405");
  await page.getByRole("textbox", { name: "Mottaker" }).fill("Testmottaker");
  await page.getByTestId("order-letter").click();
  await expect(page.getByRole("heading", { name: "Bestilling feilet" })).toBeVisible();
  expect(orders).toBe(1);
  expect(await creationEvents(page)).toEqual([]);
});

test("E-blankett synkroniserer enhet uten å nullstille andre felt", async ({ page }) => {
  await page.goto("/saksnummer/123456/brevvelger?templateId=E001&enhetsId=4815");
  const enhet = page.getByRole("combobox", { name: "Avsenderenhet" });
  const country = page.getByRole("combobox", { name: "Land", exact: true });
  const mottaker = page.getByRole("textbox", { name: "Mottaker" });
  await expect(enhet).toHaveValue("4815");
  await country.selectOption("GBR");
  await mottaker.fill("Testmottaker");
  await enhet.selectOption("4405");
  await expect(page).toHaveURL((url) => JSON.parse(url.searchParams.get("enhetsId") ?? "null") === "4405");
  await expect(country).toHaveValue("GBR");
  await expect(mottaker).toHaveValue("Testmottaker");

  for (const enhetsId of ["4808", ""]) {
    await page.evaluate((id) => {
      const url = new URL(location.href);
      if (id) {
        url.searchParams.set("enhetsId", id);
      } else {
        url.searchParams.delete("enhetsId");
      }
      history.pushState({}, "", url);
      dispatchEvent(new PopStateEvent("popstate"));
    }, enhetsId);
    await expect(enhet).toHaveValue(enhetsId);
    await expect(country).toHaveValue("GBR");
    await expect(mottaker).toHaveValue("Testmottaker");
    expect(new URL(page.url()).searchParams.get("enhetsId")).toBe(enhetsId || null);
  }

  await enhet.selectOption("4815");
  await page.getByTestId("brevmal-search").fill("E 204");
  await page.getByTestId("brevmal-button").filter({ hasText: "E 204" }).click();
  await expect(country).toHaveValue("NOR");
  await expect(mottaker).toHaveValue("");
  await expect(enhet).toHaveValue("4815");
});

test("E-blankett viser standardverdier etter forsinket lasting av land og enheter", async ({ page }) => {
  let releaseOptions: () => void = () => {};
  const optionsReady = new Promise<void>((resolve) => {
    releaseOptions = resolve;
  });
  for (const path of ["kodeverk/avtaleland", "me/enheter"]) {
    await page.route(`**/bff/skribenten-backend/${path}`, async (route) => {
      await optionsReady;
      await route.fallback();
    });
  }
  await page.goto("/saksnummer/123456/brevvelger?templateId=E001&enhetsId=4815");
  await expect(page.getByRole("textbox", { name: "Mottaker" })).toBeVisible();
  await expect(page.getByRole("combobox", { name: "Land", exact: true }).locator("option")).toHaveCount(1);
  releaseOptions();
  await expect(page.getByRole("combobox", { name: "Land", exact: true })).toHaveValue("NOR");
  await expect(page.getByRole("combobox", { name: "Avsenderenhet" })).toHaveValue("4815");
});
