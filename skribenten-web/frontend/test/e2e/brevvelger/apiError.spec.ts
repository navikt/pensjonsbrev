import { expect, test } from "@playwright/test";

import { setupSakStubs } from "../utils/helpers";

const LONG_TITLE =
  "Forventa verdi av typen class no.nav.domain.pensjon.kjerne.beregning.SomeVeryLongClassName men den mangla og den var veldig lang og skulle teste om det skjer overflow i alerten";
const LONG_MELDING =
  "Forventa verdi, men den mangla og den var veldig lang og skulle teste om det skjer overflow i alerten";

test.describe("ApiError - overflow", () => {
  // These tests navigate to brevvelger with a brevId param, which renders the ApiError inside
  // BrevmalPanel (VStack width="385px" padding="space-16"), giving ~353px of usable width —
  // well below the Alert's maxWidth of 512px. The tests assert that overflowWrap prevents
  // the long text from causing horizontal scroll.

  test("functional error (422) wraps long text without overflow", async ({ page }) => {
    await setupSakStubs(page);

    // Override the brev endpoint to return a 422 functional error (takes priority over setupSakStubs)
    await page.route("**/bff/skribenten-backend/sak/123456/brev", (route) => {
      return route.fulfill({
        status: 422,
        contentType: "application/json",
        body: JSON.stringify({ tittel: LONG_TITLE, melding: LONG_MELDING }),
      });
    });

    await page.goto("/saksnummer/123456/brevvelger?brevId=1");

    const alert = page.locator('[data-testid="functional-error-alert"]');
    // TanStack Query retries 3 times by default (total wait ~7s), so we need a generous timeout
    await expect(alert).toBeVisible({ timeout: 15000 });

    const overflows = await alert.evaluate((el) => el.scrollWidth > el.clientWidth);
    expect(overflows).toBe(false);
  });

  test("generic error (500) wraps long title and correlationId without overflow", async ({ page }) => {
    await setupSakStubs(page);

    // Override the brev endpoint to return a 500 generic error with a correlation ID header
    await page.route("**/bff/skribenten-backend/sak/123456/brev", (route) => {
      return route.fulfill({
        status: 500,
        contentType: "application/json",
        body: JSON.stringify({}),
        headers: { "x-request-id": "abc-123-correlation-id" },
      });
    });

    await page.goto("/saksnummer/123456/brevvelger?brevId=1");

    const alert = page.locator('[data-testid="generic-error-alert"]');
    // TanStack Query retries 3 times by default (total wait ~7s), so we need a generous timeout
    await expect(alert).toBeVisible({ timeout: 15000 });

    const overflows = await alert.evaluate((el) => el.scrollWidth > el.clientWidth);
    expect(overflows).toBe(false);
  });
});
