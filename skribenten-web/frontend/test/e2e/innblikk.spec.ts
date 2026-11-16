import { type BrowserContext, expect, test } from "@playwright/test";

import { setupSakStubs } from "~test/e2e/support/helpers";

test.use({ timezoneId: "UTC" });

const COOKIE_NAME = "pensjon-psak-innblikk-uuid";
const WEBSITE_ID = "85abe8ab-e9d6-4179-b727-9c856715343f";
const PENNY_ID = "ec458791-8536-41b6-bf86-a5fe83226946";
const NEXT_ID = "f1b85a1a-86fe-4bcc-af02-a287defeb106";
const SCRIPT_URL = "https://cdn.nav.no/team-researchops/sporing/sporing.js";

type AnalyticsEvent = {
  type: string;
  payload: { id?: string; website: string; name?: string; data?: Record<string, unknown> };
};

async function interceptAnalytics(context: BrowserContext, baseURL: string, origin: string) {
  const events: AnalyticsEvent[] = [];
  // Pinned copy of SCRIPT_URL: exercise the tracker without CDN access or real analytics traffic.
  await context.route(SCRIPT_URL, (route) =>
    route.fulfill({ path: "test/e2e/fixtures/sporing.js.txt", contentType: "application/javascript" }),
  );
  await context.route("**/api/send", (route) => {
    if (route.request().method() !== "OPTIONS") {
      events.push(route.request().postDataJSON() as AnalyticsEvent);
    }
    return route.fulfill({
      headers: { "Access-Control-Allow-Origin": "*", "Access-Control-Allow-Headers": "*" },
      json: { cache: "test-cache" },
    });
  });
  await context.route(`${origin}/**`, async (route) => {
    const url = new URL(route.request().url());
    const response = await route.fetch({ url: `${baseURL}${url.pathname}${url.search}` });
    if (route.request().isNavigationRequest()) {
      const html = (await response.text())
        .replace("{{UMAMI_WEBSITE_ID}}", WEBSITE_ID)
        .replace("{{UMAMI_HOST_URL}}", "https://reops-event-proxy.ekstern.dev.nav.no");
      return route.fulfill({ response, body: html });
    }
    return route.fulfill({ response });
  });
  return events;
}

test("sender oppstartshendelser etter forsinket skriptlasting uten å blokkere arbeidsflaten", async ({
  context,
  page,
  baseURL,
}) => {
  const origin = "https://pensjon-skribenten.intern.nav.no";
  const events = await interceptAnalytics(context, baseURL as string, origin);
  let releaseScript: () => void = () => {};
  const scriptReleased = new Promise<void>((resolve) => {
    releaseScript = resolve;
  });
  await context.route(SCRIPT_URL, async (route) => {
    await scriptReleased;
    await route.fallback();
  });
  await setupSakStubs(page);
  await page.goto(`${origin}/aapne/brev/ugyldig`, { waitUntil: "domcontentloaded" });
  try {
    await expect(page.getByRole("heading", { name: "Ugyldig lenke fra Pesys" })).toBeVisible();
    expect(events).toHaveLength(0);
  } finally {
    releaseScript();
  }

  await expect.poll(() => events.filter((event) => event.payload.name === "pesys feil").length).toBe(1);
  const startupEvent = events.find((event) => event.payload.name === "pesys feil");
  const cookie = (await context.cookies()).find((entry) => entry.name === COOKIE_NAME);
  expect(cookie).toBeDefined();
  expect(startupEvent).toMatchObject({
    type: "event",
    payload: {
      website: WEBSITE_ID,
      id: cookie?.value,
      data: { feiltype: "ugyldig brev-id", brevId: "ugyldig" },
    },
  });
});

for (const environment of [
  {
    domain: "intern.nav.no",
    skribenten: "https://pensjon-skribenten.intern.nav.no",
    penny: "https://pensjon-penny.intern.nav.no",
  },
  {
    domain: "dev.nav.no",
    skribenten: "https://pensjon-skribenten-web-q2.ansatt.dev.nav.no",
    penny: "https://pensjon-penny-q2.intern.dev.nav.no",
  },
]) {
  test.describe(environment.domain, () => {
    test("bruker Pennys cookie på sidevisninger og hendelser gjennom trackEvent", async ({
      context,
      page,
      baseURL,
    }) => {
      const events = await interceptAnalytics(context, baseURL as string, environment.skribenten);
      await setupSakStubs(page);
      await page.addInitScript(() => {
        window.open = () => null;
      });
      const now = new Date();
      const expires = Date.UTC(now.getUTCFullYear(), now.getUTCMonth() + 1, 1) / 1000;
      await context.addCookies([
        {
          name: COOKIE_NAME,
          value: PENNY_ID,
          domain: `.${environment.domain}`,
          path: "/",
          secure: true,
          sameSite: "Lax",
          expires,
        },
      ]);
      await page.route("**/sak/123456/bestillBrev/exstream/eblankett", (route) =>
        route.fulfill({ path: "test/e2e/fixtures/bestillBrevEblankett.json", contentType: "application/json" }),
      );

      await page.goto(`${environment.skribenten}/saksnummer/123456/brevvelger?templateId=E001&enhetsId=4815`);
      await expect.poll(() => events.some((event) => !event.payload.name)).toBe(true);
      await page.getByRole("textbox", { name: "Mottaker" }).fill("Testmottaker");
      await page.getByTestId("order-letter").click();
      await expect
        .poll(() => events.find((event) => event.payload.name === "brev opprettet"))
        .toMatchObject({
          type: "event",
          payload: {
            id: PENNY_ID,
            website: WEBSITE_ID,
            data: { brevkode: "E001", brevsystem: "exstream", landkode: "NOR" },
          },
        });
      expect(events.every((event) => event.payload.id === PENNY_ID && event.payload.website === WEBSITE_ID)).toBe(true);
      const cookies = (await context.cookies()).filter((cookie) => cookie.name === COOKIE_NAME);
      expect(cookies).toHaveLength(1);
      expect(cookies[0].expires).toBe(expires);
    });

    test("lager felles cookie før første sidevisning og observerer endringer fra Penny", async ({
      context,
      page,
      baseURL,
    }) => {
      const events = await interceptAnalytics(context, baseURL as string, environment.skribenten);
      await setupSakStubs(page);
      const now = new Date();
      // Cookie expiry uses the real browser clock, so keep both simulated months in the future.
      const beforeRollover = new Date(Date.UTC(now.getUTCFullYear(), now.getUTCMonth() + 2, 0, 12));
      const afterRollover = new Date(Date.UTC(now.getUTCFullYear(), now.getUTCMonth() + 2, 1, 12));
      const initialExpiry = Date.UTC(now.getUTCFullYear(), now.getUTCMonth() + 2, 1) / 1000;
      const renewedExpiry = Date.UTC(now.getUTCFullYear(), now.getUTCMonth() + 3, 1) / 1000;
      await page.clock.setFixedTime(beforeRollover);
      await page.goto(`${environment.skribenten}/personvern`);
      await expect.poll(() => events.length).toBeGreaterThan(0);

      const cookies = (await context.cookies()).filter((cookie) => cookie.name === COOKIE_NAME);
      expect(cookies).toHaveLength(1);
      const cookie = cookies[0];
      expect(cookie).toMatchObject({
        domain: `.${environment.domain}`,
        path: "/",
        secure: true,
        sameSite: "Lax",
        httpOnly: false,
        expires: initialExpiry,
      });
      expect(cookie.value).toMatch(/^[0-9a-f-]{36}$/);
      expect(events[0].payload.id).toBe(cookie.value);
      expect(events[0].payload.website).toBe(WEBSITE_ID);

      await context.route(`${environment.penny}/**`, (route) =>
        route.fulfill({ contentType: "text/html", body: "<!doctype html><title>Penny cookie test</title>" }),
      );
      const penny = await context.newPage();
      await penny.goto(environment.penny);
      expect(await penny.evaluate(() => document.cookie)).toContain(`${COOKIE_NAME}=${cookie.value}`);
      await penny.evaluate(
        ({ name, value, domain }) => {
          // biome-ignore lint/suspicious/noDocumentCookie: Simulates Penny writing its shared-domain cookie.
          document.cookie = `${name}=${value}; Domain=${domain}; Path=/; Secure; SameSite=Lax`;
        },
        { name: COOKIE_NAME, value: NEXT_ID, domain: environment.domain },
      );
      await page.evaluate(() => globalThis.umami?.track("tekst limt inn", { antallTegn: 12 }));
      await expect
        .poll(() => events.find((event) => event.payload.name === "tekst limt inn"))
        .toMatchObject({
          payload: { id: NEXT_ID, website: WEBSITE_ID, data: { antallTegn: 12 } },
        });

      // Simulate the browser expiring the cookie while Skribenten stays open across month-end.
      await context.clearCookies({ name: COOKIE_NAME });
      await page.clock.setFixedTime(afterRollover);
      await page.evaluate(() => globalThis.umami?.track("tekst erstattet"));
      await expect.poll(() => events.find((event) => event.payload.name === "tekst erstattet")).toBeDefined();
      const renewed = (await context.cookies()).find((entry) => entry.name === COOKIE_NAME);
      expect(renewed?.value).not.toBe(cookie.value);
      expect(renewed?.value).not.toBe(NEXT_ID);
      expect(renewed?.expires).toBe(renewedExpiry);
      expect(events.find((event) => event.payload.name === "tekst erstattet")?.payload.id).toBe(renewed?.value);
    });
  });
}
