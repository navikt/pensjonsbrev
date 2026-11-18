import assert from "node:assert/strict";
import { createServer } from "node:http";
import { test } from "node:test";

import express from "express";

import { setupWorkshopProxy } from "../dist/workshopProxy.js";

const origin = "https://workshop.invalid";

async function listening(app) {
  const server = createServer(app);
  await new Promise((resolve) => server.listen(0, "127.0.0.1", resolve));
  return { server, url: `http://127.0.0.1:${server.address().port}` };
}

async function withProxy(action, tokens, origins = []) {
  const backend = await listening((_request, response) => {
    response.setHeader("Content-Type", "application/json");
    response.end(JSON.stringify({ path: _request.url, headers: _request.headers }));
  });
  const app = express();
  setupWorkshopProxy(
    app,
    { backendUrl: backend.url, scope: "dev:pensjonsbrev:brevoppskrift-backend", origin, origins },
    tokens === null
      ? undefined
      : (tokens ?? {
          token: (request) => request.get("authorization")?.replace("Bearer ", ""),
          validate: async (token) => token === "synthetic-user-token",
          exchange: async () => "synthetic-obo-token",
        }),
  );
  app.use((_request, response) => response.type("html").send("<html>public frontend</html>"));
  const frontend = await listening(app);
  try {
    await action(frontend.url);
  } finally {
    await Promise.all([frontend, backend].map(({ server }) => new Promise((resolve) => server.close(resolve))));
  }
}

const authorization = { Authorization: "Bearer synthetic-user-token" };

async function withTexas(handler, action) {
  const texas = await listening(handler);
  const previous = {
    introspection: process.env.NAIS_TOKEN_INTROSPECTION_ENDPOINT,
    exchange: process.env.NAIS_TOKEN_EXCHANGE_ENDPOINT,
  };
  process.env.NAIS_TOKEN_INTROSPECTION_ENDPOINT = `${texas.url}/introspect`;
  process.env.NAIS_TOKEN_EXCHANGE_ENDPOINT = `${texas.url}/exchange`;
  try {
    await withProxy(action, null);
  } finally {
    if (previous.introspection === undefined) delete process.env.NAIS_TOKEN_INTROSPECTION_ENDPOINT;
    else process.env.NAIS_TOKEN_INTROSPECTION_ENDPOINT = previous.introspection;
    if (previous.exchange === undefined) delete process.env.NAIS_TOKEN_EXCHANGE_ENDPOINT;
    else process.env.NAIS_TOKEN_EXCHANGE_ENDPOINT = previous.exchange;
    await new Promise((resolve) => texas.server.close(resolve));
  }
}

test("native Texas requests use Entra introspection and user-context OBO", async () => {
  const requests = [];
  await withTexas(
    async (request, response) => {
      const chunks = [];
      for await (const chunk of request) chunks.push(chunk);
      requests.push({ path: request.url, body: JSON.parse(Buffer.concat(chunks).toString()) });
      response.setHeader("Content-Type", "application/json");
      response.end(
        JSON.stringify(
          request.url === "/introspect" ? { active: true, NAVident: "Z000000" } : { access_token: "native-obo-token" },
        ),
      );
    },
    async (url) => {
      const response = await fetch(`${url}/api/v1/meg`, { headers: authorization });
      assert.equal(response.status, 200);
      assert.equal((await response.json()).headers.authorization, "Bearer native-obo-token");
    },
  );
  assert.deepEqual(requests, [
    { path: "/introspect", body: { identity_provider: "azuread", token: "synthetic-user-token" } },
    {
      path: "/exchange",
      body: {
        identity_provider: "azuread",
        target: "dev:pensjonsbrev:brevoppskrift-backend",
        user_token: "synthetic-user-token",
      },
    },
  ]);
});

test("Texas redirects never forward user tokens", async () => {
  let redirected = false;
  await withTexas(
    (request, response) => {
      if (request.url === "/redirected") {
        redirected = true;
        response.setHeader("Content-Type", "application/json");
        response.end(JSON.stringify({ active: false }));
      } else {
        response.writeHead(302, { Location: "/redirected" });
        response.end();
      }
    },
    async (url) => {
      const response = await fetch(`${url}/api/v1/meg`, { headers: authorization });
      assert.equal(response.status, 503);
    },
  );
  assert.equal(redirected, false);
});

test("unauthenticated requests are rejected before token exchange", async () => {
  await withProxy(async (url) => {
    const response = await fetch(`${url}/api/v1/meg`);
    assert.equal(response.status, 401);
    assert.equal(response.headers.get("www-authenticate"), "Bearer");
    assert.match(response.headers.get("content-type"), /application\/problem\+json/);
  });
});

test("OBO replaces authorization and strips cookies and injected internal headers", async () => {
  await withProxy(async (url) => {
    const response = await fetch(`${url}/api/v1/meg?test=true`, {
      headers: { ...authorization, Cookie: "session=synthetic", "obo-token": "injected", "x-csrf-token": "injected" },
    });
    assert.equal(response.status, 200);
    assert.equal(response.headers.get("cache-control"), "no-store");
    const body = await response.json();
    assert.equal(body.path, "/api/v1/meg?test=true");
    assert.equal(body.headers.authorization, "Bearer synthetic-obo-token");
    assert.equal(body.headers.cookie, undefined);
    assert.equal(body.headers["obo-token"], undefined);
    assert.equal(body.headers["x-csrf-token"], undefined);
  });
});

test("both configured Nav ingresses permit authenticated CSRF-protected writes", async () => {
  await withProxy(
    async (url) => {
      const csrf = await fetch(`${url}/api/v1/csrf`, { headers: authorization });
      const { token } = await csrf.json();
      const cookie = csrf.headers.get("set-cookie").split(";")[0];
      const response = await fetch(`${url}/api/v1/utkast`, {
        method: "POST",
        headers: { ...authorization, Origin: "https://alternate.invalid", Cookie: cookie, "X-CSRF-Token": token },
        body: "{}",
      });
      assert.equal(response.status, 200);
    },
    undefined,
    ["https://alternate.invalid"],
  );
});

test("writes require the exact origin and an unpredictable double-submit token", async () => {
  await withProxy(async (url) => {
    const csrf = await fetch(`${url}/api/v1/csrf`, { headers: authorization });
    const { token } = await csrf.json();
    const cookie = csrf.headers.get("set-cookie").split(";")[0];
    assert.match(cookie, /^__Host-brevoppskrift-csrf=/);
    const headers = {
      ...authorization,
      Origin: origin,
      Cookie: cookie,
      "X-CSRF-Token": token,
      "Content-Type": "application/json",
    };
    for (const mutation of [
      { Origin: "https://other.invalid" },
      { "X-CSRF-Token": "bad" },
      { Cookie: "" },
      { "Sec-Fetch-Site": "cross-site" },
    ]) {
      const response = await fetch(`${url}/api/v1/utkast`, {
        method: "POST",
        headers: { ...headers, ...mutation },
        body: "{}",
      });
      assert.equal(response.status, 403);
      assert.equal((await response.json()).code, "CSRF_REJECTED");
    }
    const accepted = await fetch(`${url}/api/v1/utkast`, { method: "POST", headers, body: "{}" });
    assert.equal(accepted.status, 200);
  });
});

test("exchange failures return a safe error without exposing tokens", async () => {
  await withProxy(
    async (url) => {
      const response = await fetch(`${url}/api/v1/meg`, { headers: authorization });
      assert.equal(response.status, 503);
      assert.doesNotMatch(await response.text(), /synthetic-user-token|private/);
    },
    {
      token: () => "synthetic-user-token",
      validate: async () => true,
      exchange: async () => {
        throw new Error("private failure containing synthetic-user-token");
      },
    },
  );
});

test("legacy pages, assets and Brevbaker routes never require token validation", async () => {
  await withProxy(
    async (url) => {
      for (const path of ["/", "/templates", "/template/autobrev/Test", "/assets/app.js", "/brevbaker/templates"]) {
        const response = await fetch(`${url}${path}`, { headers: { Authorization: "Bearer expired-token" } });
        assert.equal(response.status, 200, path);
        assert.match(await response.text(), /public frontend/);
        const anonymous = await fetch(`${url}${path}`);
        assert.equal(anonymous.status, 200, path);
      }
    },
    {
      token: () => {
        throw new Error("Public requests must not consult authentication");
      },
      validate: async () => {
        throw new Error("Public requests must not validate tokens");
      },
      exchange: async () => {
        throw new Error("Public requests must not exchange tokens");
      },
    },
  );
});

test("all API paths including CSRF reject anonymous requests without redirecting or serving HTML", async () => {
  await withProxy(async (url) => {
    for (const path of ["/api", "/api/v1/csrf", "/api/v1/unknown"]) {
      const response = await fetch(`${url}${path}`, { redirect: "manual" });
      assert.equal(response.status, 401, path);
      assert.equal(response.headers.get("location"), null);
      assert.match(response.headers.get("content-type"), /application\/problem\+json/);
      assert.equal((await response.json()).code, "UNAUTHORIZED");
    }
    const unknown = await fetch(`${url}/api/v1/unknown`, { headers: authorization });
    assert.match(unknown.headers.get("content-type"), /application\/json/);
    assert.equal((await unknown.json()).path, "/api/v1/unknown");
  });
});

test("invalid user tokens never reach token exchange", async () => {
  await withProxy(
    async (url) => {
      const response = await fetch(`${url}/api/v1/utkast`, { headers: authorization });
      assert.equal(response.status, 401);
    },
    {
      token: () => "invalid-token",
      validate: async () => false,
      exchange: async () => {
        throw new Error("Invalid tokens must not be exchanged");
      },
    },
  );
});
