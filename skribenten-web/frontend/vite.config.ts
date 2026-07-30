/// <reference types="vitest" />
import { fileURLToPath, URL } from "node:url";

import { tanstackRouter } from "@tanstack/router-vite-plugin";
import react from "@vitejs/plugin-react";
import { defineConfig, type Plugin } from "vite";

function umamiConfigPlugin(): Plugin {
  return {
    name: "umami-config",
    transformIndexHtml(html) {
      return html
        .replace("{{UMAMI_HOST_URL}}", process.env.UMAMI_HOST_URL ?? "https://reops-event-proxy.ekstern.dev.nav.no")
        .replace("{{UMAMI_WEBSITE_ID}}", process.env.UMAMI_WEBSITE_ID ?? "85abe8ab-e9d6-4179-b727-9c856715343f");
    },
  };
}

// https://vitejs.dev/config/
export default defineConfig(({ command }) => ({
  plugins: [
    react({
      jsxImportSource: "@emotion/react",
    }),
    tanstackRouter(),
    ...(command === "serve" ? [umamiConfigPlugin()] : []),
  ],
  resolve: {
    alias: {
      "~": fileURLToPath(new URL("src", import.meta.url)),
    },
  },
  build: {
    chunkSizeWarningLimit: 700,
    // "hidden" writes .map files next to the bundles without adding a
    // "//# sourceMappingURL" comment, so browsers never auto-fetch them.
    // The maps are stripped out of the publicly served folder before the
    // bff docker image is built (see .build-frontend.yaml) and are only
    // used server-side to resolve stack traces reported by the frontend.
    sourcemap: "hidden",
    rollupOptions: {
      output: {
        manualChunks: (id) => {
          if (!id.includes("node_modules")) return;
          if (id.includes("react-pdf") || id.includes("pdfjs-dist")) return "pdf";
          if (id.includes("@navikt/ds-react") || id.includes("@navikt/aksel-icons")) return "navikt-ds";
          if (id.includes("@tanstack/react-router") || id.includes("@tanstack/react-query")) return "tanstack";

          return "vendor";
        },
      },
    },
  },
  server: {
    origin: "http://localhost:5173",
    cors: true,
  },
  test: {
    environment: "jsdom",
    globals: true, // Enables Vitest to automatically cleanup after each test
    setupFiles: "./src/setupTests.ts",
    exclude: ["**/node_modules/**", "**/test/e2e/**"],
  },
}));
