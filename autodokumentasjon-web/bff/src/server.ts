import express from "express";

import { setupActuators } from "./actuators.js";
import { setupStaticRoutes } from "./frontendRoute.js";

const server = express();

// Restricts the server to only accept UTF-8 encoding of bodies
server.use(express.urlencoded({ extended: true }));

setupActuators(server);
setupStaticRoutes(server);

export { server };
