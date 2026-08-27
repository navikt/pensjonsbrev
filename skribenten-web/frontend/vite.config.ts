/// <reference types="vitest" />
import fs from "node:fs";
import path from "node:path";
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

function moveMapFiles(root: string, dir: string, destRoot: string) {
  for (const entry of fs.readdirSync(dir, { withFileTypes: true })) {
    const fullPath = path.join(dir, entry.name);
    if (entry.isDirectory()) {
      moveMapFiles(root, fullPath, destRoot);
      continue;
    }
    if (!entry.name.endsWith(".map")) continue;

    const relativePath = path.relative(root, fullPath);
    const destinationPath = path.join(destRoot, relativePath);
    fs.mkdirSync(path.dirname(destinationPath), { recursive: true });
    fs.renameSync(fullPath, destinationPath);
  }
}

// Vite/Rolldown always write sourcemaps next to the bundles they describe -
// there's no built-in option to output them to a separate directory. This
// plugin moves the generated .map files out of the publicly served build
// output into a sibling "dist-sourcemaps" directory right after they're
// written to disk, so they never ship to the browser (see build.sourcemap
// below) regardless of how/where "vite build" is invoked (CI or locally).
function extractSourcemapsPlugin(): Plugin {
  let outDir: string;
  let sourcemapsDir: string;

  return {
    name: "extract-sourcemaps",
    apply: "build",
    configResolved(config) {
      outDir = path.resolve(config.root, config.build.outDir);
      sourcemapsDir = path.resolve(outDir, "..", "dist-sourcemaps");
    },
    writeBundle() {
      fs.rmSync(sourcemapsDir, { recursive: true, force: true });
      if (fs.existsSync(outDir)) {
        moveMapFiles(outDir, outDir, sourcemapsDir);
      }
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
    ...(command === "serve" ? [umamiConfigPlugin()] : [extractSourcemapsPlugin()]),
  ],
  resolve: {
    alias: {
      "~": fileURLToPath(new URL("src", import.meta.url)),
      "~test": fileURLToPath(new URL("test", import.meta.url)),
    },
  },
  build: {
    chunkSizeWarningLimit: 700,
    // "hidden" writes .map files without adding a "//# sourceMappingURL"
    // comment, so browsers never auto-fetch them even before the
    // extractSourcemapsPlugin above moves them out of dist/.
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
