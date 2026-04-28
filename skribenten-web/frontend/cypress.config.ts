import { defineConfig } from "cypress";

export default defineConfig({
  allowCypressEnv: false,
  component: {
    devServer: {
      framework: "react",
      bundler: "vite",
    },
    setupNodeEvents(on, _) {
      on("before:browser:launch", (browser = {} as Cypress.Browser, launchOptions) => {
        if (browser.name === "chrome") {
          launchOptions.args.push(`--window-size=3000,2000`);
          launchOptions.args.push("--force-device-scale-factor=1");
        }
        return launchOptions;
      });
    },
  },
  e2e: {
    baseUrl: "http://localhost:5173",
  },
  viewportWidth: 1200,
  viewportHeight: 1400,
});
