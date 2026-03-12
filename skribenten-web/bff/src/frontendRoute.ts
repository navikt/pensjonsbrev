import fs from "node:fs";
import path from "node:path";

import { serveViteMode } from "@navikt/vite-mode";
import express, { type Express } from "express";

function getUmamiHostUrl(): string {
  return process.env.UMAMI_HOST_URL ?? "";
}

export function setupStaticRoutes(server: Express) {
  server.use(express.static("./public", { index: false }));

  // When deployed, the built frontend is copied into the public directory. If running BFF locally the directory will not exist.
  const spaFilePath = path.resolve("./public", "index.html");

  serveViteMode(server, { port: "5173" });

  server.get("*", (_req, res) => {
    try {
      const html = fs.readFileSync(spaFilePath, "utf-8");
      const injected = html.replace("{{UMAMI_HOST_URL}}", getUmamiHostUrl());
      res.type("html").send(injected);
    } catch {
      res.sendStatus(404);
    }
  });
}
