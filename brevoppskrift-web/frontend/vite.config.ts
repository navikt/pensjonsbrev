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
    tanstackRouter({
      routeFileIgnorePattern: "\\.test\\.",
    }),
  ],
  resolve: {
    alias: {
      "~": fileURLToPath(new URL("src", import.meta.url)),
    },
  },
  server: {
    origin: "http://localhost:5173",
    // Siden lastes via wonderwall (/vite-on) fra en annen origin enn Vite-serveren.
    cors: {
      origin: [
        "http://localhost:8088",
        "https://pensjon-brevoppskrift.intern.dev.nav.no",
        "https://pensjon-brevoppskrift.ansatt.dev.nav.no",
      ],
    },
  },
  // The search worker imports fuse.js, so its bundle gets code-split. Vite's
  // build default for workers is "iife", which cannot import chunks.
  worker: {
    format: "es",
  },
});
