import express from "express";

import { setupActuators } from "./actuators.js";
import { setupSkribentenBackendApiProxy } from "./apiProxy.js";
import { errorHandling } from "./errorHandler.js";
import { setupStaticRoutes } from "./frontendRoute.js";
import { internalRoutes } from "./internalRoutes.js";
import { verifyJWTToken } from "./tokenValidation.js";

const server = express();

// Restricts the server to only accept UTF-8 encoding of bodies
server.use(express.urlencoded({ extended: true }));

setupActuators(server);

server.set("trust proxy", 1);

server.use(verifyJWTToken);

setupSkribentenBackendApiProxy(server);
internalRoutes(server);
setupStaticRoutes(server);
server.use(errorHandling);

export { server };
