import express from "express";

import { setupActuators } from "./actuators.js";
import { setupSkribentenBackendApiProxy } from "./apiProxy.js";
import { setupStaticRoutes } from "./frontendRoute.js";
import { internalRoutes } from "./internalRoutes.js";
import { setupLogging } from "./logging.js";
import { setupMetrics } from "./metrics.js";
import { verifyToken } from "./tokenValidation.js";

const server = express();

// Restricts the server to only accept UTF-8 encoding of bodies
server.use(express.urlencoded({ extended: true }));

// Må registreres før verifyToken, som ellers svarer 401 på scrapet fra prometheus. Ligger først
// slik at målingen omfatter alle forespørsler, også de som håndteres av setupActuators.
setupMetrics(server);

setupActuators(server);
setupLogging(server);

server.set("trust proxy", 1);

server.use(verifyToken);

setupSkribentenBackendApiProxy(server);
internalRoutes(server);
setupStaticRoutes(server);

export { server };
