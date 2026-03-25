import fs from "node:fs";
import path from "node:path";

import { addServeSpaHandler, serveViteMode } from "@navikt/vite-mode";
import express, { type Express } from "express";

export function setupStaticRoutes(server: Express) {
  server.use(express.static("./public", { index: false }));

  // When deployed, the built frontend is copied into the public directory. If running BFF locally the directory will not exist.
  const spaFilePath = path.resolve("./public", "index.html");

  serveViteMode(server, { port: "5173" });

  try {
    const html = fs.readFileSync(spaFilePath, "utf-8");
    const injected = html
      .replace("{{UMAMI_HOST_URL}}", process.env.UMAMI_HOST_URL ?? "")
      .replace("{{UMAMI_WEBSITE_ID}}", process.env.UMAMI_WEBSITE_ID ?? "");

    server.get("*splat", (_req, res) => {
      res.type("html").send(injected);
    });
  } catch {
    addServeSpaHandler(server, spaFilePath);
  }
}
