import AxeBuilder from "@axe-core/playwright";
import { expect, type Page, type TestInfo, test } from "@playwright/test";

import { setupSakStubs } from "~test/e2e/support/helpers";

const dialogName = "Vil du tilbakestille brevmalen?";

async function expectNoAxeViolations(page: Page, testInfo: TestInfo) {
  await page.evaluate(async () => {
    await document.fonts.ready;
    await Promise.all(document.getAnimations().map((animation) => animation.finished.catch(() => {})));
  });

  const results = await new AxeBuilder({ page }).withTags(["wcag2a", "wcag2aa", "wcag21aa"]).analyze();

  await testInfo.attach("axe-results", {
    body: JSON.stringify(results, null, 2),
    contentType: "application/json",
  });

  const details = results.violations
    .map((violation) =>
      [
        `${violation.id} (${violation.impact ?? "unknown impact"}) - ${violation.help}`,
        violation.description,
        `Tags: ${violation.tags.join(", ")}`,
        `Help: ${violation.helpUrl}`,
        ...violation.nodes.map((node) =>
          [
            `Target: ${JSON.stringify(node.target)}`,
            `HTML: ${node.html}`,
            node.failureSummary ?? "No failure summary available",
          ].join("\n"),
        ),
      ].join("\n"),
    )
    .join("\n\n");

  await testInfo.attach("accessibility-details", {
    body: details || "No WCAG 2.1 A/AA violations found.",
    contentType: "text/plain",
  });

  const affectedElements = results.violations.reduce((total, violation) => total + violation.nodes.length, 0);
  const summary = [
    `WCAG 2.1 A/AA: ${results.violations.length} violated rules, ${affectedElements} affected elements`,
    ...results.violations.map((violation) => {
      const criteria = violation.tags
        .filter((tag) => /^wcag\d{3,}$/.test(tag))
        .map((tag) => tag.replace(/^wcag(\d)(\d)(\d+)$/, "WCAG $1.$2.$3"))
        .join(", ");

      return [
        `[${violation.impact ?? "unknown"}] ${violation.id} (${criteria})`,
        `${violation.help} - ${violation.nodes.length} affected elements`,
      ].join("\n");
    }),
    "See accessibility-details and axe-results attachments for full details.",
  ].join("\n\n");

  expect(results.violations.length, summary).toBe(0);
}

test.describe("Letter editor accessibility", () => {
  test.beforeEach(async ({ page }) => {
    await setupSakStubs(page);
    await page.route("**/bff/skribenten-backend/sak/123456/brev/1?reserver=true", (route) =>
      route.fulfill({ path: "test/e2e/fixtures/brevResponse.json", contentType: "application/json" }),
    );
    await page.route("**/bff/skribenten-backend/brevmal/*/modelSpecification", (route) =>
      route.fulfill({ path: "test/e2e/fixtures/modelSpecification.json", contentType: "application/json" }),
    );

    await page.goto("/saksnummer/123456/brev/1");
    await expect(page.getByText("Saksbehandlingstiden vår er vanligvis 10 uker.")).toBeVisible();
    await expect(page.getByLabel("Land", { exact: true })).toBeVisible();
    await expect(page.getByText("Lagret", { exact: true })).toBeVisible();
    await expect(page.getByRole("button", { name: "Tilbakestill mal", exact: true })).toBeEnabled();
  });

  test.describe("WCAG 2.1 A/AA scans", () => {
    test("loaded editor page", async ({ page }, testInfo) => {
      await expectNoAxeViolations(page, testInfo);
    });

    test("reset letter dialog", async ({ page }, testInfo) => {
      await page.getByRole("button", { name: "Tilbakestill mal", exact: true }).click();
      const dialog = page.getByRole("dialog", { name: dialogName });
      await expect(dialog).toBeVisible();
      await expect(dialog.getByRole("button", { name: "Nei, behold brevet" })).toBeVisible();
      await expectNoAxeViolations(page, testInfo);
    });
  });

  test.describe("Keyboard interaction", () => {
    test("reset dialog opens and closes with focus restored to its trigger", async ({ page }) => {
      const resetButton = page.getByRole("button", { name: "Tilbakestill mal", exact: true });
      for (let tabCount = 0; tabCount < 50; tabCount++) {
        await page.keyboard.press("Tab");
        if (await resetButton.evaluate((button) => button === document.activeElement)) {
          break;
        }
      }
      await expect(resetButton, "Reset control should be reachable within 50 Tab presses").toBeFocused();
      await page.keyboard.press("Enter");

      const dialog = page.getByRole("dialog", { name: dialogName });
      await expect(dialog).toBeVisible();
      await expect.poll(() => dialog.evaluate((element) => element.contains(document.activeElement))).toBe(true);
      await page.keyboard.press("Tab");
      await expect.poll(() => dialog.evaluate((element) => element.contains(document.activeElement))).toBe(true);
      await page.keyboard.press("Escape");

      await expect(dialog).not.toBeVisible();
      await expect(resetButton).toBeFocused();
    });
  });
});
