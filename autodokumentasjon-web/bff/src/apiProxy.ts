import { addProxyHandler } from "@navikt/backend-for-frontend-utils";
import type { Express } from "express";

import config from "./config.js";

export const setupSkribentenBackendApiProxy = (server: Express) =>
  addProxyHandler(server, {
    ingoingUrl: "/brevbaker",
    outgoingUrl: config.brevbakerApiProxy.url,
    scope: config.brevbakerApiProxy.scope,
    flow: "ON_BEHALF_OF",
  });
