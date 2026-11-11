import { expect, test } from "@playwright/test";

test.beforeEach(async ({ context }) => {
  await context.route("**/bff/skribenten-backend/me/userinfo", (route) =>
    route.fulfill({ json: { navident: "Z990297", name: "Testbruker", erAttestant: false } }),
  );
});

test("Personvernerklæringen åpnes i ny fane uten å forlate arbeidsflaten", async ({ page }) => {
  await page.goto("/saksnummer");
  const saksnummer = page.getByRole("textbox", { name: "Skriv inn saksnummer" });
  await saksnummer.fill("123456");

  await page.getByRole("button", { name: "Nyttige lenker" }).click();
  await expect(page.getByRole("menuitem", { name: "Dokumentasjon og veiledning (åpnes i ny fane)" })).toBeVisible();
  const link = page.getByRole("menuitem", { name: "Personvernerklæring (åpnes i ny fane)" });
  await link.focus();
  const popupPromise = page.waitForEvent("popup");
  await link.press("Enter");
  const privacyPage = await popupPromise;

  await expect(privacyPage).toHaveURL(/\/personvern$/);
  await expect(privacyPage.getByRole("heading", { name: "Personvernerklæring", level: 1 })).toBeVisible();
  await expect(page).toHaveURL(/\/saksnummer\/?$/);
  await expect(saksnummer).toHaveValue("123456");
});

test("Personvernerklæringen kan åpnes direkte og lastes på nytt", async ({ page }) => {
  await page.goto("/personvern");
  await page.reload();

  const main = page.getByRole("main");
  await expect(main.getByRole("heading", { name: "Personvernerklæring", level: 1 })).toBeVisible();
  await expect(main.getByRole("heading", { name: "Personvern og sikkerhet i Skribenten", level: 2 })).toBeVisible();
  await expect(main.getByRole("heading", { name: "Bruk av informasjonskapsler (cookies)", level: 2 })).toBeVisible();
  await expect(main.getByRole("heading", { name: "Skyra", level: 3 })).toBeAttached();
  await expect(main.getByRole("heading", { name: "Statistikk og analyseverktøy (Innblikk)", level: 3 })).toBeAttached();
  await expect(main.getByRole("link", { name: "Navs generelle personvernerklæring" })).toHaveAttribute(
    "href",
    "https://www.nav.no/personvern",
  );
});

test("Hele erklæringen kan leses med tastatur i et kort og smalt vindu", async ({ page }) => {
  await page.setViewportSize({ width: 768, height: 500 });
  await page.goto("/personvern");

  const main = page.getByRole("main");
  const finalParagraph = main.getByText(/Du har rett til å klage til Datatilsynet/);
  const header = page.getByRole("banner");
  const initialHeaderBounds = await header.boundingBox();
  await expect(finalParagraph).not.toBeInViewport();

  await main.focus();
  await main.press("End");
  await expect(finalParagraph).toBeInViewport({ ratio: 1 });
  await expect(header).toBeInViewport({ ratio: 1 });
  expect(await header.boundingBox()).toEqual(initialHeaderBounds);
  const usefulLinks = page.getByRole("button", { name: "Nyttige lenker" });
  await expect(usefulLinks).toBeInViewport({ ratio: 1 });
  await usefulLinks.click();
  await expect(page.getByRole("menuitem", { name: "Personvernerklæring (åpnes i ny fane)" })).toBeInViewport({
    ratio: 1,
  });
});
