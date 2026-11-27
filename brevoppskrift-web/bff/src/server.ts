import express, { type Express } from "express";
import { createProxyMiddleware } from "http-proxy-middleware";

import { setupActuators } from "./actuators.js";
import config from "./config.js";
import { setupStaticRoutes } from "./frontendRoute.js";
import { setupLogging } from "./logging.js";
import { createOasisTokenServices, setupWorkshopProxy } from "./workshopProxy.js";

const server = express();

setupActuators(server);
setupLogging(server);

server.set("trust proxy", 1);
setupWorkshopProxy(server, config.workshop, createOasisTokenServices());

// Restricts the server to only accept UTF-8 encoding of bodies
server.use(express.urlencoded({ extended: true }));
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
      plugins: [
        (proxy) => {
          proxy.on("error", (error) => {
            const code = "code" in error && typeof error.code === "string" ? error.code : "UNKNOWN";
            console.error("Brevbaker proxy error", { code });
          });
        },
      ],
      on: {
        proxyReq: (proxyRequest) => {
          proxyRequest.removeHeader("cookie");
          proxyRequest.removeHeader("authorization");
        },
      },
    }),
  );
}

type SimpleProxyOptions = {
  ingoingUrl: string;
  outgoingUrl: string;
};
