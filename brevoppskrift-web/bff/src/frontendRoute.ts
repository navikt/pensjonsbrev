import path from "node:path";

import { addServeSpaHandler, addViteModeHtmlToResponse } from "@navikt/vite-mode";
import express, { type Express } from "express";

export function setupStaticRoutes(server: Express) {
  server.use(express.static("./public", { index: false }));

  // When deployed, the built frontend is copied into the public directory. If running BFF locally the directory will not exist.
  const spaFilePath = path.resolve("./public", "index.html");

  serveViteModeWithWorkers(server);
  addServeSpaHandler(server, spaFilePath);
}

const VITE_PORT = "5173";

/** `serveViteMode` from `@navikt/vite-mode`, but with web workers allowed.
 *
 *  In vite-mode the page comes from this server while every script comes from
 *  the Vite dev server, and browsers only start same-origin worker scripts. The
 *  frontend therefore starts its search worker from a same-origin `blob:` URL
 *  that imports the real script from Vite. Without a `worker-src` directive the
 *  CSP falls back to `script-src`, which does not list `blob:`. And the worker's
 *  own imports are checked against `worker-src` too, so it must also allow the
 *  dev server. Added only to vite-mode responses, never to the deployed app's. */
function serveViteModeWithWorkers(server: Express) {
  addViteModeHtmlToResponse(server, { port: VITE_PORT });
  server.get("*splat", (_request, response, next) => {
    const viteModeHtml = response.viteModeHtml;
    if (!viteModeHtml) {
      return next();
    }
    const csp = response.getHeader("content-security-policy");
    if (typeof csp === "string") {
      response.setHeader("content-security-policy", `${csp}; worker-src blob: http://localhost:${VITE_PORT}`);
    }
    response.send(viteModeHtml);
  });
}
