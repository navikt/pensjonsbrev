/// <reference types="vitest" />
import { fileURLToPath, URL } from "node:url";
import { TanStackRouterVite } from "@tanstack/router-vite-plugin";
import react from "@vitejs/plugin-react";
import { defineConfig } from "vite";

// https://vitejs.dev/config/
export default defineConfig(() => ({
  plugins: [
    react({
      jsxImportSource: "@emotion/react",
    }),
    TanStackRouterVite(),
  ],
  resolve: {
    alias: {
      "~": fileURLToPath(new URL("src", import.meta.url)),
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
  },
}));
