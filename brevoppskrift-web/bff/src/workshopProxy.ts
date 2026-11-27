import { getToken, requestAzureOboToken, validateAzureToken } from "@navikt/oasis";
import { type Express, type NextFunction, type Request, type Response } from "express";
import { createProxyMiddleware } from "http-proxy-middleware";

export type WorkshopConfig = { backendUrl: string; scope: string; origins: string[] };
export type TokenServices = {
  token: (request: Request) => string | null | undefined;
  validate: (token: string) => Promise<boolean>;
  exchange: (token: string, scope: string) => Promise<string | null>;
};

export function createOasisTokenServices(): TokenServices {
  return {
    token: getToken,
    validate: async (token) => {
      const result = await validateAzureToken(token);
      return result.ok && typeof result.payload.NAVident === "string" && result.payload.NAVident.trim().length > 0;
    },
    exchange: async (token, scope) => {
      const result = await requestAzureOboToken(token, scope);
      return result.ok && typeof result.token === "string" && /^\S+$/.test(result.token) ? result.token : null;
    },
  };
}

function problem(response: Response, status: number, code: string) {
  response.status(status).set("Cache-Control", "no-store").type("application/problem+json").send({
    type: "about:blank",
    title: code,
    status,
    code,
    detail: "Forespørselen kunne ikke behandles.",
  });
}

export function setupWorkshopProxy(server: Express, config: WorkshopConfig, tokens: TokenServices) {
  const origins = new Set(config.origins);
  if (origins.size === 0) throw new Error("Workshop origins must not be empty");
  if (
    [...origins].some(
      (origin) => !["https:", "http:"].includes(new URL(origin).protocol) || new URL(origin).origin !== origin,
    )
  ) {
    throw new Error("Invalid workshop origin");
  }
  const outboundTokens = new WeakMap<Request, string>();
  server.use("/api", async (request: Request, response: Response, next: NextFunction) => {
    try {
      const token = tokens.token(request);
      if (!token || !(await tokens.validate(token))) {
        response.set("WWW-Authenticate", "Bearer");
        problem(response, 401, "UNAUTHORIZED");
        return;
      }
      next();
    } catch {
      problem(response, 503, "AUTH_UNAVAILABLE");
    }
  });

  server.use(
    "/api",
    async (request: Request, response: Response, next: NextFunction) => {
      if (!["GET", "HEAD", "OPTIONS"].includes(request.method)) {
        if (!origins.has(request.get("origin") ?? "") || request.get("sec-fetch-site") === "cross-site") {
          problem(response, 403, "CSRF_REJECTED");
          return;
        }
        if (!request.is("application/json")) {
          problem(response, 415, "UNSUPPORTED_MEDIA_TYPE");
          return;
        }
      }
      try {
        const token = tokens.token(request);
        const exchanged = token ? await tokens.exchange(token, config.scope) : null;
        if (!exchanged) {
          problem(response, 503, "OBO_UNAVAILABLE");
          return;
        }
        outboundTokens.set(request, exchanged);
        next();
      } catch {
        problem(response, 503, "OBO_UNAVAILABLE");
      }
    },
    createProxyMiddleware<Request, Response>({
      target: config.backendUrl,
      changeOrigin: true,
      followRedirects: false,
      proxyTimeout: 45_000,
      timeout: 60_000,
      pathRewrite: (_path, request) => request.originalUrl,
      on: {
        proxyRes: (proxyResponse) => {
          proxyResponse.headers["cache-control"] = "no-store";
        },
        proxyReq: (proxyRequest, request) => {
          proxyRequest.removeHeader("cookie");
          proxyRequest.removeHeader("obo-token");
          proxyRequest.removeHeader("x-csrf-token");
          proxyRequest.setHeader("Authorization", `Bearer ${outboundTokens.get(request)}`);
        },
        error: (_error, _request, response) => {
          if ("headersSent" in response && !response.headersSent)
            problem(response as Response, 503, "BACKEND_UNAVAILABLE");
        },
      },
    }),
  );
}
