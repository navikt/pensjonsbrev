import { expect, test } from "@playwright/test";

import { setupSakStubs } from "./utils/helpers";

test.describe("Kvittering", () => {
  test.beforeEach(async ({ page }) => {
    await setupSakStubs(page);
    await page.goto("/saksnummer/123456/kvittering");
  });

  test("åpner en annen sak", async ({ page }) => {
    await page.getByRole("button", { name: "Åpne annen sak" }).click();
    await expect(page).toHaveURL(/\/saksnummer$/);
  });

  test("lager nytt brev for bruker", async ({ page }) => {
    await page.getByRole("button", { name: "Lage nytt brev på denne saken" }).click();
    await expect(page).toHaveURL(/\/saksnummer\/123456\/brevvelger$/);
  });
});
