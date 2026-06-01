/// <reference types="vitest/config" />
import { fileURLToPath, URL } from "node:url";

import { tanstackRouter } from "@tanstack/router-plugin/vite";
import react from "@vitejs/plugin-react";
import { defineConfig } from "vite";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    react({
      jsxImportSource: "@emotion/react",
    }),
    tanstackRouter(),
  ],
  resolve: {
    alias: {
      "~": fileURLToPath(new URL("src", import.meta.url)),
    },
  },
  server: {
    origin: "http://localhost:5173",
    proxy: {
      // Lets `npm run dev` on :5173 talk to a locally running brevbaker without going through the BFF.
      // Override the target with BREVBAKER_API_URL (e.g. when brevbaker runs on another host/port).
      "/brevbaker": {
        target: process.env.BREVBAKER_API_URL ?? "http://localhost:8080",
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/brevbaker/, ""),
      },
    },
  },
  test: {
    environment: "jsdom",
    globals: true, // Enables Vitest to automatically cleanup after each test
  },
});
