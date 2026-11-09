import AxeBuilder from "@axe-core/playwright";
import { expect, type Page, test } from "@playwright/test";

import {
  ATTESTANT_DIFF_ANNOUNCEMENT_REPLAYABLE_UNTIL,
  ATTESTANT_DIFF_ANNOUNCEMENT_SHOW_UNTIL,
} from "~/Brevredigering/LetterEditor/diff/AttestantDiffAnnouncement";
import { setupSakStubs } from "~test/e2e/support/helpers";
import { brevInfo, brevResponse } from "~test/support/brevFixtures";

const defaultLetter = brevResponse({ info: brevInfo({ saksId: 123456 }) });
const ANNOUNCEMENT_TITLE = "Se manuelle endringer i brevet";
const BELL_TITLE = "Se endringer gjort av saksbehandler";
const LAST_DISPLAY_DAY = `${ATTESTANT_DIFF_ANNOUNCEMENT_SHOW_UNTIL}T12:00:00`;

const givenAcknowledged = (page: Page, ids: string[]) =>
  page.route("**/bff/skribenten-backend/me/bekreftedeNyhetsvarsler", (route) => route.fulfill({ json: ids }));

const bell = (page: Page) => page.getByRole("button", { name: /^Nytt i Skribenten/ });
const dot = (page: Page) => page.getByTestId("unseen-announcements-dot");
const tour = (page: Page) => page.getByRole("alertdialog", { name: ANNOUNCEMENT_TITLE });

test.describe("varslingsklokke i toppmenyen", () => {
  test.beforeEach(async ({ page }) => {
    await page.clock.setFixedTime(LAST_DISPLAY_DAY);
    await setupSakStubs(page);
    await page.route("**/bff/skribenten-backend/me/userinfo", (route) =>
      route.fulfill({ json: { navident: "Z990297", name: "F_Z990297 E_Z990297", erAttestant: true } }),
    );
    await page.route("**/bff/skribenten-backend/features/diff", (route) => route.fulfill({ json: { enabled: true } }));
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1/attestering?reserver=true", (route) =>
      route.fulfill({ json: defaultLetter }),
    );
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1?reserver=true", (route) =>
      route.fulfill({ json: defaultLetter }),
    );
    await page.route("**/bff/skribenten-backend/brev/1/diff", (route) =>
      route.fulfill({ json: { editedBlocks: {}, deletedBlocks: {} } }),
    );
    await page.route("**/bff/skribenten-backend/me/bekreftedeNyhetsvarsler/*", (route) =>
      route.fulfill({ status: 204 }),
    );
    await page.route("**/bff/skribenten-backend/me/setteNyhetsvarsler", (route) => route.fulfill({ json: [] }));
    await page.route("**/bff/skribenten-backend/me/setteNyhetsvarsler/*", (route) => route.fulfill({ status: 204 }));
  });

  test("åpning av klokken fjerner prikken, men gjennomgangen starter automatisk i attesteringen", async ({ page }) => {
    const seen: string[] = [];
    await givenAcknowledged(page, []);
    await page.route("**/bff/skribenten-backend/me/setteNyhetsvarsler", (route) => route.fulfill({ json: seen }));
    await page.route("**/bff/skribenten-backend/me/setteNyhetsvarsler/*", (route) => {
      seen.push(decodeURIComponent(route.request().url().split("/").pop() ?? ""));
      return route.fulfill({ status: 204 });
    });
    await page.goto("/saksnummer/123456/brev/1");
    await expect(dot(page)).toBeVisible();

    const markedSeen = page.waitForRequest(
      (request) => request.url().endsWith("/me/setteNyhetsvarsler/diff-toggles") && request.method() === "PUT",
    );
    await bell(page).click();
    await markedSeen;

    await expect(page.getByRole("heading", { name: "Nytt i Skribenten" })).toBeVisible();
    await expect(page.getByRole("heading", { name: BELL_TITLE })).toBeVisible();
    await expect(dot(page)).toBeHidden();
    const axe = await new AxeBuilder({ page })
      .include(".aksel-modal")
      .withTags(["wcag2a", "wcag2aa", "wcag21a", "wcag21aa"])
      .analyze();
    expect(axe.violations).toEqual([]);

    const dialog = page.getByRole("dialog", { name: "Nytt i Skribenten" });
    const bellBox = await bell(page).boundingBox();
    const dialogBox = await dialog.boundingBox();
    if (!bellBox || !dialogBox) throw new Error("Fant ikke klokken eller dialogen");
    expect(dialogBox.width).toBe(451);
    expect(dialogBox.y).toBeGreaterThanOrEqual(bellBox.y + bellBox.height);
    expect(dialogBox.y).toBeLessThan(bellBox.y + bellBox.height + 16);
    expect(Math.abs(dialogBox.x + dialogBox.width - (bellBox.x + bellBox.width))).toBeLessThanOrEqual(1);

    await page.keyboard.press("Escape");
    await expect(dialog).toBeHidden();
    await expect(bell(page)).toBeFocused();
    await page.goto("/saksnummer/123456/attester/1/redigering");
    await expect(tour(page)).toBeVisible();
    await expect(dot(page)).toBeHidden();
  });

  test("«Se hvordan» starter gjennomgangen på nytt i attestantens redigeringsvisning", async ({ page }) => {
    await givenAcknowledged(page, ["diff-toggles"]);
    await page.goto("/saksnummer/123456/attester/1/redigering");
    await expect(page.getByRole("checkbox", { name: "Marker tekst som er lagt til" })).toBeVisible();
    await expect(dot(page)).toBeHidden();
    await expect(tour(page)).toBeHidden();

    await bell(page).click();
    await page.getByRole("button", { name: "Se hvordan" }).click();

    await expect(tour(page)).toBeVisible();
    await tour(page).getByRole("button", { name: "Skjønner!" }).click();
    await expect(tour(page)).toBeHidden();
    await expect(bell(page)).toBeFocused();
  });

  test("kan vises på nytt med «Se hvordan» etter at den automatiske nyheten er lukket", async ({ page }) => {
    await givenAcknowledged(page, []);
    await page.goto("/saksnummer/123456/attester/1/redigering");
    await expect(tour(page)).toBeVisible();
    await expect(dot(page)).toBeVisible();

    await tour(page).getByRole("button", { name: "Skjønner!" }).click();
    await expect(tour(page)).toBeHidden();
    await expect(dot(page)).toBeHidden();

    await bell(page).click();
    await page.getByRole("button", { name: "Se hvordan" }).click();
    await expect(tour(page)).toBeVisible();
  });

  test("etter siste automatiske visningsdag starter nyheten bare med «Se hvordan»", async ({ page }) => {
    await page.clock.setFixedTime(`${ATTESTANT_DIFF_ANNOUNCEMENT_REPLAYABLE_UNTIL}T12:00:00`);
    await givenAcknowledged(page, []);
    await page.goto("/saksnummer/123456/attester/1/redigering");
    await expect(page.getByRole("checkbox", { name: "Marker tekst som er lagt til" })).toBeVisible();
    await page.waitForTimeout(500);
    await expect(tour(page)).toBeHidden();

    await bell(page).click();
    await page.getByRole("button", { name: "Se hvordan" }).click();
    await expect(tour(page)).toBeVisible();
  });

  test("viser bare «Se dokumentasjon» utenfor attesteringen", async ({ page }) => {
    await givenAcknowledged(page, ["diff-toggles"]);
    await page.goto("/saksnummer/123456/brev/1");
    await expect(page.getByText("weeks.")).toBeVisible();

    await bell(page).click();

    await expect(page.getByRole("heading", { name: BELL_TITLE })).toBeVisible();
    await expect(page.getByRole("button", { name: "Se hvordan" })).toHaveCount(0);
    const docs = page.getByRole("link", { name: /^Se dokumentasjon/ });
    await expect(docs).toBeVisible();
    await expect(docs).toHaveAttribute(
      "href",
      "https://navikt.github.io/pensjonsbrev/docs/main/skribenten-101.html#_attestering",
    );
    await expect(page.getByText("10.11.2026")).toBeVisible();
  });
});

test("saksbehandler uten attestantrolle ser nyheten med lenke til dokumentasjonen i brevbehandler", async ({
  page,
}) => {
  await page.clock.setFixedTime(LAST_DISPLAY_DAY);
  await setupSakStubs(page);
  await page.route("**/bff/skribenten-backend/me/userinfo", (route) =>
    route.fulfill({ json: { navident: "Z990297", name: "F_Z990297 E_Z990297", erAttestant: false } }),
  );
  await page.route("**/bff/skribenten-backend/sak/123456/brev", (route) => route.fulfill({ json: [] }));
  await givenAcknowledged(page, []);
  await page.route("**/bff/skribenten-backend/me/setteNyhetsvarsler", (route) => route.fulfill({ json: [] }));
  await page.route("**/bff/skribenten-backend/me/setteNyhetsvarsler/*", (route) => route.fulfill({ status: 204 }));
  await page.goto("/saksnummer/123456/brevbehandler");
  await expect(page.getByText("Fant ingen brev som er under behandling")).toBeVisible();
  await expect(dot(page)).toBeVisible();

  await bell(page).click();

  await expect(page.getByRole("heading", { name: BELL_TITLE })).toBeVisible();
  await expect(page.getByRole("link", { name: /^Se dokumentasjon/ })).toBeVisible();
  await expect(page.getByRole("button", { name: "Se hvordan" })).toHaveCount(0);
});

test.describe("menyen med nyttige lenker i toppmenyen", () => {
  test("har lenke til dokumentasjon og veiledning som åpnes i ny fane", async ({ page }) => {
    await setupSakStubs(page);
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1?reserver=true", (route) =>
      route.fulfill({ json: defaultLetter }),
    );
    await page.goto("/saksnummer/123456/brev/1");

    await page.getByRole("button", { name: "Nyttige lenker" }).click();

    const link = page.getByRole("menuitem", { name: "Dokumentasjon og veiledning (åpnes i ny fane)" });
    await expect(link).toBeVisible();
    await expect(link).toHaveAttribute("href", "https://navikt.github.io/pensjonsbrev/docs/main/index.html");
    await expect(link).toHaveAttribute("target", "_blank");
    const axe = await new AxeBuilder({ page })
      .include('[role="menu"]')
      .withTags(["wcag2a", "wcag2aa", "wcag21a", "wcag21aa"])
      .analyze();
    expect(axe.violations).toEqual([]);
  });
});
