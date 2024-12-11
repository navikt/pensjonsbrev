import path from "node:path";

import { addServeSpaHandler, addViteModeHtmlToResponse } from "@navikt/vite-mode";
import express, { Express } from "express";

export function setupStaticRoutes(server: Express) {
  server.use(express.static("./public", { index: false }));

  // When deployed, the built frontend is copied into the public directory. If running BFF locally the directory will not exist.
  const spaFilePath = path.resolve("./public", "index.html");

  addViteModeHtmlToResponse(server, {});
  addServeSpaHandler(server, spaFilePath);
}
