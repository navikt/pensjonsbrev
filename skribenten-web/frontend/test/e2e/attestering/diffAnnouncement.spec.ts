import AxeBuilder from "@axe-core/playwright";
import { expect, type Page, test } from "@playwright/test";

import { setupSakStubs } from "~test/e2e/support/helpers";
import { brevInfo, brevResponse } from "~test/support/brevFixtures";

const defaultBrev = brevResponse({ info: brevInfo({ saksId: 123456 }) });
const ATTESTERING_URL = "**/bff/skribenten-backend/sak/123456/brev/1/attestering?reserver=true";
const NYHET = "Se manuelle endringer i brevet";

const givenUser = (page: Page, erAttestant: boolean) =>
  page.route("**/bff/skribenten-backend/me/userinfo", (route) =>
    route.fulfill({ json: { navident: "Z990297", name: "F_Z990297 E_Z990297", erAttestant } }),
  );

const announcement = (page: Page) => page.getByRole("alertdialog", { name: NYHET });

test.describe("nyhet om diff-menyen for attestant", () => {
  test.beforeEach(async ({ page }) => {
    await setupSakStubs(page);
    await givenUser(page, true);

    await page.route("**/bff/skribenten-backend/features/diff", (route) => route.fulfill({ json: { enabled: true } }));
    await page.route(ATTESTERING_URL, (route) => route.fulfill({ json: defaultBrev }));
    await page.route(
      "**/bff/skribenten-backend/sak/123456/brev/1/attestering/redigertBrev?frigiReservasjon=*",
      (route) => route.fulfill({ json: { ...defaultBrev, redigertBrev: route.request().postDataJSON() } }),
    );
    await page.route("**/bff/skribenten-backend/brev/1/diff", (route) =>
      route.fulfill({ json: { editedBlocks: {}, deletedBlocks: {} } }),
    );
  });

  test("vises for attestant, kan lukkes og diff-bryterne virker fortsatt", async ({ page }) => {
    await page.goto("/saksnummer/123456/attester/1/redigering");

    await expect(announcement(page)).toBeVisible();
    await expect(announcement(page)).toHaveAttribute("aria-modal", "true");
    // Joyride fades the tooltip in; measure contrast once it is fully opaque.
    await expect(page.locator(".react-joyride__floater")).toHaveCSS("opacity", "1");
    const axe = await new AxeBuilder({ page })
      .include("[data-feature-announcement]")
      .withTags(["wcag2a", "wcag2aa", "wcag21a", "wcag21aa"])
      .analyze();
    expect(axe.violations).toEqual([]);

    await announcement(page).getByRole("button", { name: "Skjønner!" }).click();
    await expect(announcement(page)).toBeHidden();

    const diffRequest = page.waitForRequest(
      (request) => request.url().includes("/brev/1/diff") && request.method() === "POST",
    );
    await page.getByRole("checkbox", { name: "Marker tekst som er lagt til" }).check();
    await diffRequest;
    await expect(page.getByText("Ingen endringer å vise.")).toBeVisible();

    await page.getByRole("checkbox", { name: "Marker tekst som er lagt til" }).uncheck();
    await expect(announcement(page)).toBeHidden();
  });

  test("tar fokus, lukkes med Escape og gir fokus tilbake til brevet", async ({ page }) => {
    await page.goto("/saksnummer/123456/attester/1/redigering");
    await expect(announcement(page)).toBeVisible();
    await expect(announcement(page).getByRole("button", { name: "Skjønner!" })).toBeFocused();

    await page.keyboard.press("Escape");
    await expect(announcement(page)).toBeHidden();

    await page.keyboard.type("Hei ");
    await expect(page.locator(".editor").getByText("Hei")).toBeVisible();
  });

  test("klikk på den dimmede siden lukker nyheten uten å endre brevet", async ({ page }) => {
    await page.goto("/saksnummer/123456/attester/1/redigering");
    await expect(announcement(page)).toBeVisible();

    const tekst = await page.getByText("weeks.").boundingBox();
    if (!tekst) throw new Error("Fant ikke brevteksten");
    await page.mouse.click(tekst.x + tekst.width / 2, tekst.y + tekst.height / 2);
    await expect(announcement(page)).toBeHidden();
    await expect(page.getByTestId("undo-button")).toBeDisabled();

    await page.getByText("weeks.").click();
    await page.keyboard.type(" hello!");
    await expect(page.getByText("hello!")).toBeVisible();
  });

  test("venter til brevet er lastet", async ({ page }) => {
    let slippBrev: () => void = () => {};
    const brevSluppet = new Promise<void>((resolve) => {
      slippBrev = resolve;
    });
    await page.route(ATTESTERING_URL, async (route) => {
      await brevSluppet;
      return route.fulfill({ json: defaultBrev });
    });

    await page.goto("/saksnummer/123456/attester/1/redigering");
    await expect(page.getByRole("heading", { name: "Henter brev..." })).toBeVisible();
    await expect(announcement(page)).toBeHidden();

    slippBrev();
    await expect(announcement(page)).toBeVisible();
  });

  test("vises ikke for saksbehandler uten attestantrolle", async ({ page }) => {
    await givenUser(page, false);
    await page.goto("/saksnummer/123456/attester/1/redigering");

    await expect(page.getByRole("checkbox", { name: "Marker tekst som er lagt til" })).toBeVisible();
    await page.waitForTimeout(500);
    await expect(announcement(page)).toBeHidden();
  });

  test("vises ikke i saksbehandlers redigeringsvisning", async ({ page }) => {
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1?reserver=true", (route) =>
      route.fulfill({ json: defaultBrev }),
    );
    await page.goto("/saksnummer/123456/brev/1");

    await expect(page.getByText("weeks.")).toBeVisible();
    await page.waitForTimeout(500);
    await expect(announcement(page)).toBeHidden();
  });

  test("ryddes bort når attestanten går videre til forhåndsvisning", async ({ page }) => {
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1/attestering?frigiReservasjon=true", (route) =>
      route.fulfill({ json: { ...defaultBrev, ...route.request().postDataJSON() } }),
    );

    await page.goto("/saksnummer/123456/attester/1/redigering");
    await expect(announcement(page)).toBeVisible();
    await page.getByRole("textbox", { name: "Underskrift" }).fill("Attestant Testesen");
    // The dimmed page blocks pointer clicks, so trigger the navigation directly while the announcement is open.
    await page.getByRole("button", { name: "Fortsett" }).dispatchEvent("click");

    await expect(page).toHaveURL(/\/attester\/1\/forhandsvisning/, { timeout: 15_000 });
    await expect(announcement(page)).toBeHidden();
    await expect(page.locator("[data-feature-announcement]")).toHaveCount(0);
  });
});
