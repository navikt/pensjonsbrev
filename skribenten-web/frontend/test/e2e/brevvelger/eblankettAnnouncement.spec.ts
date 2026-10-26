import AxeBuilder from "@axe-core/playwright";
import { expect, type Page, test } from "@playwright/test";

import { setupSakStubs } from "~test/e2e/support/helpers";
import { brevInfo } from "~test/support/brevFixtures";

const walkthrough = (page: Page) => page.locator('[data-feature-announcement="brevvelger-eblanketter"]');
const eblanketter = (page: Page) => page.getByTestId("category-item").filter({ hasText: "E-blanketter" });
const panelHeading = (page: Page, name: string) => page.getByRole("heading", { name });
const steg1 = (page: Page) => page.getByRole("alertdialog", { name: "E-blanketter har fått egen kategori" });
const steg2 = (page: Page) => page.getByRole("alertdialog", { name: "Velg en e-blankett" });
const E001 = "E 001 - Anmodning om opplysninger";

test.describe("nyhet om e-blanketter i brevvelgeren", () => {
  test.beforeEach(async ({ page }) => {
    await setupSakStubs(page, { lukkEblankettNyhet: false });
  });

  test("viser kategorien, en e-blankett og panelet, og setter brevvelgeren tilbake", async ({ page }) => {
    await page.goto("/saksnummer/123456/brevvelger");

    await expect(steg1(page)).toBeVisible();
    await expect(eblanketter(page)).not.toHaveClass(/aksel-accordion__item--open/);
    await walkthrough(page).getByRole("button", { name: "Neste" }).click();

    await expect(steg2(page)).toBeVisible();
    await expect(eblanketter(page)).toHaveClass(/aksel-accordion__item--open/);
    await expect(panelHeading(page, E001)).toBeVisible();
    await walkthrough(page).getByRole("button", { name: "Neste" }).click();

    const steg3 = page.getByRole("alertdialog", { name: "Fyll ut e-blanketten" });
    await expect(steg3).toBeVisible();
    await expect(steg3).toContainText("Implementasjonen her kommer snart.");
    await expect(page.locator(".react-joyride__floater")).toHaveCSS("opacity", "1");
    const axe = await new AxeBuilder({ page })
      .include('[data-feature-announcement="brevvelger-eblanketter"]')
      .withTags(["wcag2a", "wcag2aa", "wcag21a", "wcag21aa"])
      .analyze();
    expect(axe.violations).toEqual([]);
    await steg3.getByRole("button", { name: "Skjønner!" }).click();

    await expect(walkthrough(page)).toBeHidden();
    await expect(panelHeading(page, E001)).toBeHidden();
    await expect(eblanketter(page)).not.toHaveClass(/aksel-accordion__item--open/);
    await expect(page.getByTestId("order-letter")).toBeHidden();
    await expect(page.getByTestId("brevmal-search")).toBeFocused();
  });

  test("endrer aldri URL-en, så en ny innlasting midt i gjennomgangen ikke beholder e-blanketten", async ({ page }) => {
    await page.goto("/saksnummer/123456/brevvelger");
    await walkthrough(page).getByRole("button", { name: "Neste" }).click();
    await expect(steg2(page)).toBeVisible();
    await expect(page).not.toHaveURL(/templateId=/);

    await page.reload();

    await expect(steg1(page)).toBeVisible();
    await expect(panelHeading(page, E001)).toBeHidden();
  });

  test("viser brevmalen brukeren hadde valgt igjen når gjennomgangen lukkes med Escape", async ({ page }) => {
    await page.goto("/saksnummer/123456/brevvelger?templateId=INFORMASJON_OM_SAKSBEHANDLINGSTID");
    await expect(steg1(page)).toBeVisible();
    await walkthrough(page).getByRole("button", { name: "Neste" }).click();
    await expect(steg2(page)).toBeVisible();
    await expect(panelHeading(page, E001)).toBeVisible();

    await page.keyboard.press("Escape");

    await expect(walkthrough(page)).toBeHidden();
    await expect(panelHeading(page, "Informasjon om saksbehandlingstid")).toBeVisible();
    await expect(page.getByTestId("order-letter")).toBeVisible();
    await expect(eblanketter(page)).not.toHaveClass(/aksel-accordion__item--open/);
  });

  test("klikk på den dimmede siden lukker hele gjennomgangen og setter brevvelgeren tilbake", async ({ page }) => {
    await page.goto("/saksnummer/123456/brevvelger");
    await walkthrough(page).getByRole("button", { name: "Neste" }).click();
    await expect(steg2(page)).toBeVisible();

    await page.mouse.click(5, 300);

    await expect(walkthrough(page)).toBeHidden();
    await expect(panelHeading(page, E001)).toBeHidden();
  });

  test("Forrige går tilbake et steg uten å lukke gjennomgangen", async ({ page }) => {
    await page.goto("/saksnummer/123456/brevvelger");
    await walkthrough(page).getByRole("button", { name: "Neste" }).click();
    await expect(steg2(page)).toBeVisible();

    await walkthrough(page).getByRole("button", { name: "Forrige" }).click();

    await expect(steg1(page)).toBeVisible();
  });
});

test.describe("nyhet om e-blanketter med mye innhold i et lavt vindu", () => {
  test.use({ viewport: { width: 1000, height: 600 } });

  test("venter til kladder og favoritter er lastet, og scroller til kategorien og e-blanketten", async ({ page }) => {
    await setupSakStubs(page, { lukkEblankettNyhet: false });
    const kladder = Array.from({ length: 8 }, (_, index) =>
      brevInfo({ id: 100 + index, saksId: 123456, brevtittel: `Kladd ${index + 1}` }),
    );
    await page.route("**/bff/skribenten-backend/sak/123456/brev", async (route) => {
      await new Promise((resolve) => setTimeout(resolve, 1500));
      return route.fulfill({ json: kladder });
    });
    await page.route("**/bff/skribenten-backend/me/favourites", async (route) => {
      await new Promise((resolve) => setTimeout(resolve, 1000));
      return route.fulfill({ json: ["INFORMASJON_OM_SAKSBEHANDLINGSTID"] });
    });

    await page.goto("/saksnummer/123456/brevvelger");
    await expect(page.getByText("Kladd 8")).toBeVisible();

    await expect(steg1(page)).toBeInViewport({ ratio: 1 });
    // E-blanketter is last in the list, and Joyride's rounded scroll position stops about a pixel short of the end.
    await expect(page.getByRole("button", { name: "E-blanketter" })).toBeInViewport({ ratio: 0.9 });
    await walkthrough(page).getByRole("button", { name: "Neste" }).click();

    await expect(steg2(page)).toBeInViewport({ ratio: 1 });
    await expect(page.getByTestId("brevmal-button").filter({ hasText: "E 001" })).toBeInViewport({ ratio: 1 });
  });
});
