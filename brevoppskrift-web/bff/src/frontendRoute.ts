import path from "node:path";

import { addLocalViteServerHandler, addServeSpaHandler } from "@navikt/backend-for-frontend-utils";
import express, { Express } from "express";

export function setupStaticRoutes(server: Express) {
  server.use(express.static("./public", { index: false }));

  // When deployed, the built frontend is copied into the public directory. If running BFF locally the directory will not exist.
  const spaFilePath = path.resolve("./public", "index.html");

  addLocalViteServerHandler(server);
  addServeSpaHandler(server, spaFilePath);
}
