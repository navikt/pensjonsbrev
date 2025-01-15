import express from "express";

import { setupActuators } from "./actuators.js";
import { setupSkribentenBackendApiProxy } from "./apiProxy.js";
import { setupStaticRoutes } from "./frontendRoute.js";
import { internalRoutes } from "./internalRoutes.js";
import { setupLogging } from "./logging.js";
import { verifyToken } from "./tokenValidation.js";

const server = express();

// Restricts the server to only accept UTF-8 encoding of bodies
server.use(express.urlencoded({ extended: true }));

setupActuators(server);
setupLogging(server);

server.set("trust proxy", 1);

server.use(verifyToken);

setupSkribentenBackendApiProxy(server);
internalRoutes(server);
setupStaticRoutes(server);

export { server };
