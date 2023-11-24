import { addProxyHandler } from "@navikt/backend-for-frontend-utils";
import type { Express } from "express";

import config from "./config.js";

export const setupSkribentenBackendApiProxy = (server: Express) =>
  addProxyHandler(server, {
    ingoingUrl: "/skribenten-backend",
    outgoingUrl: config.skribentenBackendApiProxy.url,
    scope: config.skribentenBackendApiProxy.scope,
    flow: "ON_BEHALF_OF",
  });
