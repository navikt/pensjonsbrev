import { addSimpleProxyHandler } from "@navikt/backend-for-frontend-utils";
import express from "express";

import { setupActuators } from "./actuators.js";
import config from "./config.js";
import { setupStaticRoutes } from "./frontendRoute.js";

const server = express();

// Restricts the server to only accept UTF-8 encoding of bodies
server.use(express.urlencoded({ extended: true }));

setupActuators(server);
addSimpleProxyHandler(server, { ingoingUrl: "/brevbaker", outgoingUrl: config.brevbakerApiProxy.url });
setupStaticRoutes(server);

export { server };
