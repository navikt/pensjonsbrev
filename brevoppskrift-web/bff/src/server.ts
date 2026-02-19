import express, { Express } from "express";
import { createProxyMiddleware } from "http-proxy-middleware";

import { setupActuators } from "./actuators.js";
import config from "./config.js";
import { setupStaticRoutes } from "./frontendRoute.js";

const server = express();

// Restricts the server to only accept UTF-8 encoding of bodies
server.use(express.urlencoded({ extended: true }));

setupActuators(server);

server.set("trust proxy", 1);
addSimpleProxyHandler(server, { ingoingUrl: "/brevbaker", outgoingUrl: config.brevbakerApiProxy.url });

setupStaticRoutes(server);

export { server };

/**
 * Proxies request without any modification or token exchange
 */
function addSimpleProxyHandler(server: Express, { ingoingUrl, outgoingUrl }: SimpleProxyOptions) {
  server.use(
    ingoingUrl,
    createProxyMiddleware({
      target: outgoingUrl,
      changeOrigin: true,
      logger: console,
    }),
  );
}

type SimpleProxyOptions = {
  ingoingUrl: string;
  outgoingUrl: string;
};
