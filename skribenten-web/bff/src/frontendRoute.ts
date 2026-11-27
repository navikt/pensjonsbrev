import fs from "node:fs";
import path from "node:path";

import { addServeSpaHandler, addViteModeHtmlToResponse, ViteModeCspPolicy } from "@navikt/vite-mode";
import express, { type Express } from "express";

function injectInnblikkConfig(html: string): string {
  return html
    .replace("{{UMAMI_HOST_URL}}", process.env.UMAMI_HOST_URL ?? "")
    .replace("{{UMAMI_WEBSITE_ID}}", process.env.UMAMI_WEBSITE_ID ?? "");
}

export function setupStaticRoutes(server: Express) {
  server.use(express.static("./public", { index: false }));

  // When deployed, the built frontend is copied into the public directory. If running BFF locally the directory will not exist.
  const spaFilePath = path.resolve("./public", "index.html");

  addViteModeHtmlToResponse(server, {
    port: "5173",
    templateTransform: (html) =>
      injectInnblikkConfig(
        html.replace(
          "</head>",
          '<meta name="umami-host-url" content="{{UMAMI_HOST_URL}}"><meta name="umami-website-id" content="{{UMAMI_WEBSITE_ID}}"></head>',
        ),
      ),
  });
  server.get("*splat", (_req, res, next) => {
    if (!res.viteModeHtml) {
      return next();
    }

    const policy = new ViteModeCspPolicy(String(res.getHeader("content-security-policy") ?? ""));
    const tracker = new Set(["https://cdn.nav.no/team-researchops/sporing/sporing-dev.js"]);
    policy.addToDirective("script-src", tracker);
    policy.addToDirective("script-src-elem", tracker);
    policy.addToDirective("connect-src", new Set(["https://reops-event-proxy.ekstern.dev.nav.no"]));
    res.setHeader("content-security-policy", policy.asString());
    res.send(res.viteModeHtml);
  });

  try {
    const injected = injectInnblikkConfig(fs.readFileSync(spaFilePath, "utf-8"));

    server.get("*splat", (_req, res) => {
      res.type("html").send(injected);
    });
  } catch {
    addServeSpaHandler(server, spaFilePath);
  }
}
