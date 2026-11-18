import { randomBytes, timingSafeEqual } from "node:crypto";

import { type Express, type NextFunction, type Request, type Response } from "express";
import { createProxyMiddleware } from "http-proxy-middleware";

export type WorkshopConfig = { backendUrl: string; scope: string; origin: string; origins?: string[] };
export type TokenServices = {
  token: (request: Request) => string | null | undefined;
  validate: (token: string) => Promise<boolean>;
  exchange: (token: string, scope: string) => Promise<string | null>;
};

const defaultTokens: TokenServices = {
  token: (request) => /^Bearer ([^\s]+)$/i.exec(request.get("authorization") ?? "")?.[1],
  validate: async (token) => {
    const result = await texas("NAIS_TOKEN_INTROSPECTION_ENDPOINT", { identity_provider: "azuread", token });
    return result.active === true && typeof result.NAVident === "string" && result.NAVident.trim().length > 0;
  },
  exchange: async (token, scope) => {
    const result = await texas("NAIS_TOKEN_EXCHANGE_ENDPOINT", {
      identity_provider: "azuread",
      target: scope,
      user_token: token,
    });
    return typeof result.access_token === "string" && result.access_token.length > 0 ? result.access_token : null;
  },
};

async function texas(variable: string, body: Record<string, string>): Promise<Record<string, unknown>> {
  const endpoint = process.env[variable];
  if (!endpoint) throw new Error("Texas is not configured");
  const response = await fetch(endpoint, {
    method: "POST",
    redirect: "error",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body),
    signal: AbortSignal.timeout(5000),
  });
  if (!response.ok) throw new Error("Texas request failed");
  return (await response.json()) as Record<string, unknown>;
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

export function setupWorkshopProxy(server: Express, config: WorkshopConfig, tokens: TokenServices = defaultTokens) {
  if (!["https:", "http:"].includes(new URL(config.origin).protocol)) throw new Error("Invalid workshop origin");
  const secure = new URL(config.origin).protocol === "https:";
  const origins = new Set([config.origin, ...(config.origins ?? [])]);
  if (
    [...origins].some(
      (origin) => new URL(origin).origin !== origin || (new URL(origin).protocol === "https:") !== secure,
    )
  ) {
    throw new Error("Workshop origins must use the same protocol");
  }
  const csrfCookie = secure ? "__Host-brevoppskrift-csrf" : "brevoppskrift-csrf";
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

  server.get("/api/v1/csrf", (_request, response) => {
    const token = randomBytes(32).toString("hex");
    response.cookie(csrfCookie, token, { httpOnly: true, secure, sameSite: "strict", path: "/" });
    response.set("Cache-Control", "no-store").json({ token });
  });

  server.use(
    "/api",
    async (request: Request, response: Response, next: NextFunction) => {
      if (!["GET", "HEAD", "OPTIONS"].includes(request.method)) {
        const cookie = request.headers.cookie
          ?.split(";")
          .map((value) => value.trim())
          .find((value) => value.startsWith(`${csrfCookie}=`))
          ?.slice(csrfCookie.length + 1);
        const header = request.get("x-csrf-token");
        if (
          !origins.has(request.get("origin") ?? "") ||
          request.get("sec-fetch-site") === "cross-site" ||
          !cookie ||
          !header ||
          !/^[a-f0-9]{64}$/.test(cookie) ||
          !/^[a-f0-9]{64}$/.test(header) ||
          !timingSafeEqual(Buffer.from(cookie), Buffer.from(header))
        ) {
          problem(response, 403, "CSRF_REJECTED");
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
