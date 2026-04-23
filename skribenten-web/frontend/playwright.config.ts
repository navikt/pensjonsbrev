import { defineConfig, devices } from "@playwright/test";

const isUI = process.argv.includes("--ui");
if (isUI) {
  process.env.IS_UI_MODE = "true";
}
const isUiMode = process.env.IS_UI_MODE === "true";

export default defineConfig({
  testDir: "./test/e2e",
  fullyParallel: true,
  forbidOnly: !!process.env.CI,
  retries: process.env.CI ? 2 : 0,
  workers: process.env.CI ? 3 : undefined,
  outputDir: "./test/e2e-report",
  use: {
    baseURL: isUiMode ? "http://localhost:5173" : "http://localhost:4173",
    trace: "on-first-retry",
    screenshot: "only-on-failure",
  },
  projects: [
    {
      name: "chromium",
      use: {
        ...devices["Desktop Chrome"],
        viewport: { width: 1200, height: 1400 },
      },
    },
  ],
  webServer: isUiMode
    ? undefined
    : {
        command: "npm run build && npm run preview -- --port 4173",
        url: "http://localhost:4173",
        reuseExistingServer: !process.env.CI,
      },
});
