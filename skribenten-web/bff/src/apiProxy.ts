import { getToken, requestOboToken } from "@navikt/oasis";
import { Express, NextFunction, Request, Response, Router } from "express";
import { createProxyMiddleware } from "http-proxy-middleware";

import config from "./config.js";

type ProxyOptions = {
  ingoingUrl: string;
  outgoingUrl: string;
  scope: string;
};

export const setupSkribentenBackendApiProxy = (server: Express) =>
  addProxyHandler(server, {
    ingoingUrl: "/bff/skribenten-backend",
    outgoingUrl: config.skribentenBackendApiProxy.url,
    scope: config.skribentenBackendApiProxy.scope,
  });

export function addProxyHandler(router: Router, { ingoingUrl, outgoingUrl, scope }: ProxyOptions) {
  router.use(
    ingoingUrl,
    async (request: Request, response: Response, next: NextFunction) => {
      const token = getToken(request);
      if (!token) {
        return response.status(401).send();
      }
      const onBehalfOfTokenResponse = await requestOboToken(token, scope);
      if (onBehalfOfTokenResponse.ok) {
        request.headers["obo-token"] = onBehalfOfTokenResponse.token;
        return next();
      } else {
        console.log("OBO-exchange failed", onBehalfOfTokenResponse.error);
        return response.status(403).send();
      }
    },

    createProxyMiddleware({
      target: outgoingUrl,
      changeOrigin: true,
      logger: console,
      on: {
        proxyReq: (proxyRequest, request) => {
          const onBehalfOfToken = request.headers["obo-token"];
          if (onBehalfOfToken) {
            proxyRequest.removeHeader("obo-token");
            proxyRequest.removeHeader("cookie");
            proxyRequest.setHeader("Authorization", `Bearer ${onBehalfOfToken}`);
          } else {
            console.log(`Access token var not present in session for scope ${scope}`);
          }
        },
      },
    }),
  );
}
