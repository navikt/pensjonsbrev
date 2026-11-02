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
const LAST_DISPLAY_DAY = `${ATTESTANT_DIFF_ANNOUNCEMENT_SHOW_UNTIL}T12:00:00`;

const givenAcknowledged = (page: Page, ids: string[]) =>
  page.route("**/bff/skribenten-backend/me/bekreftedeNyhetsvarsler", (route) => route.fulfill({ json: ids }));

const bell = (page: Page) => page.getByRole("button", { name: /^Nytt i Skribenten og nyttige lenker/ });
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
  });

  test("viser rød prikk for en ny nyhet, og innholdet er tilgjengelig", async ({ page }) => {
    await givenAcknowledged(page, []);
    await page.goto("/saksnummer/123456/brev/1");

    await expect(dot(page)).toBeVisible();
    await bell(page).click();

    await expect(page.getByRole("heading", { name: "Nytt i Skribenten" })).toBeVisible();
    await expect(page.getByRole("heading", { name: ANNOUNCEMENT_TITLE })).toBeVisible();
    await expect(page.getByRole("link", { name: /Dokumentasjon og veiledning/ })).toBeVisible();
    const axe = await new AxeBuilder({ page })
      .include(".aksel-popover")
      .withTags(["wcag2a", "wcag2aa", "wcag21a", "wcag21aa"])
      .analyze();
    expect(axe.violations).toEqual([]);
  });

  test("«Vis meg» starter gjennomgangen på nytt i attestantens redigeringsvisning", async ({ page }) => {
    await givenAcknowledged(page, ["diff-toggles"]);
    await page.goto("/saksnummer/123456/attester/1/redigering");
    await expect(page.getByRole("checkbox", { name: "Marker tekst som er lagt til" })).toBeVisible();
    await expect(dot(page)).toBeHidden();
    await expect(tour(page)).toBeHidden();

    await bell(page).click();
    await page.getByRole("button", { name: "Vis meg" }).click();

    await expect(tour(page)).toBeVisible();
    await tour(page).getByRole("button", { name: "Skjønner!" }).click();
    await expect(tour(page)).toBeHidden();
    await expect(bell(page)).toBeFocused();
  });

  test("kan vises på nytt med «Vis meg» etter at den automatiske nyheten er lukket", async ({ page }) => {
    await givenAcknowledged(page, []);
    await page.route("**/bff/skribenten-backend/me/bekreftedeNyhetsvarsler/*", (route) =>
      route.fulfill({ status: 204 }),
    );
    await page.goto("/saksnummer/123456/attester/1/redigering");
    await expect(tour(page)).toBeVisible();
    await expect(dot(page)).toBeVisible();

    await tour(page).getByRole("button", { name: "Skjønner!" }).click();
    await expect(tour(page)).toBeHidden();
    await expect(dot(page)).toBeHidden();

    await bell(page).click();
    await page.getByRole("button", { name: "Vis meg" }).click();
    await expect(tour(page)).toBeVisible();
  });

  test("etter siste automatiske visningsdag starter nyheten bare med «Vis meg»", async ({ page }) => {
    await page.clock.setFixedTime(`${ATTESTANT_DIFF_ANNOUNCEMENT_REPLAYABLE_UNTIL}T12:00:00`);
    await givenAcknowledged(page, []);
    await page.goto("/saksnummer/123456/attester/1/redigering");
    await expect(page.getByRole("checkbox", { name: "Marker tekst som er lagt til" })).toBeVisible();
    await page.waitForTimeout(500);
    await expect(tour(page)).toBeHidden();

    await bell(page).click();
    await page.getByRole("button", { name: "Vis meg" }).click();
    await expect(tour(page)).toBeVisible();
  });

  test("«Vis meg» forklarer hvor funksjonen finnes utenfor attesteringen", async ({ page }) => {
    await givenAcknowledged(page, ["diff-toggles"]);
    await page.goto("/saksnummer/123456/brev/1");
    await expect(page.getByText("weeks.")).toBeVisible();

    await bell(page).click();
    await page.getByRole("button", { name: "Vis meg" }).click();

    const dialog = page.getByRole("dialog", { name: "Du må åpne et brev til attestering" });
    await expect(dialog).toBeVisible();
    await expect(dialog).toContainText("Manuelle endringer i brevet kan bare vises når du attesterer et brev.");
    await expect(tour(page)).toBeHidden();
    await expect
      .poll(() => dialog.evaluate((element) => element.contains(document.activeElement)), {
        message: "Fokus skal flyttes inn i dialogen",
      })
      .toBe(true);

    await page.keyboard.press("Escape");
    await expect(dialog).toBeHidden();
    await expect(bell(page)).toBeFocused();
  });
});
