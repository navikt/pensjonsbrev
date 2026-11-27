import assert from "node:assert/strict";
import { test } from "node:test";

import express from "express";

import { createOasisTokenServices, setupWorkshopProxy } from "../dist/workshopProxy.js";
import { close, identityProvider, json, listening } from "./tokenFixtures.mjs";

const origin = "https://workshop.invalid";
const scope = "api://dev-gcp.pensjonsbrev.brevoppskrift-backend/.default";
const authorization = { Authorization: "Bearer test-user-token" };
const fakeTokens = {
  token: (request) => request.get("authorization")?.replace("Bearer ", ""),
  validate: async (token) => token === "test-user-token",
  exchange: async () => "test-exchanged-token",
};

async function withProxy(action, { tokens = fakeTokens, origins = [origin], handler } = {}) {
  const backend = await listening(
    handler ??
      (async (request, response) => {
        const chunks = [];
        for await (const chunk of request) chunks.push(chunk);
        json(response, { path: request.url, headers: request.headers, body: Buffer.concat(chunks).toString() });
      }),
  );
  const app = express();
  setupWorkshopProxy(app, { backendUrl: backend.url, scope, origins }, tokens);
  app.use((_request, response) => response.type("html").send("<html>public frontend</html>"));
  const frontend = await listening(app);
  try {
    await action(frontend.url);
  } finally {
    await Promise.all([frontend, backend].map(close));
  }
}

function writeHeaders(requestOrigin = origin) {
  return { ...authorization, Origin: requestOrigin, "Content-Type": "application/json" };
}

test("Oasis validates signed Entra JWTs and exchanges user context for the configured backend scope", async () => {
  const identity = await identityProvider();
  const token = identity.token();
  try {
    await withProxy(
      async (url) => {
        for (let attempt = 0; attempt < 2; attempt++) {
          const response = await fetch(`${url}/api/v1/meg`, { headers: { Authorization: `Bearer ${token}` } });
          assert.equal(response.status, 200);
          assert.equal((await response.json()).headers.authorization, `Bearer ${identity.exchangedToken}`);
        }
      },
      { tokens: createOasisTokenServices() },
    );
    assert.deepEqual(identity.requests, [
      { path: "/exchange", body: { identity_provider: "azuread", target: scope, user_token: token } },
    ]);
  } finally {
    await close(identity);
  }
});

test("invalid JWTs and tokens without Nav user context never reach OBO", async () => {
  const identity = await identityProvider();
  const valid = identity.token();
  const parts = valid.split(".");
  parts[1] = Buffer.from(
    JSON.stringify({ ...JSON.parse(Buffer.from(parts[1], "base64url").toString()), NAVident: "Z999999" }),
  ).toString("base64url");
  try {
    await withProxy(
      async (url) => {
        for (const token of [
          parts.join("."),
          identity.token({ exp: Math.floor(Date.now() / 1000) - 3600 }),
          identity.token({ nbf: Math.floor(Date.now() / 1000) + 3600 }),
          identity.token({ iss: "https://other.invalid" }),
          identity.token({ aud: "another-client" }),
          identity.token({ NAVident: undefined }),
          identity.token({ NAVident: " " }),
          "not-a-jwt",
        ]) {
          const response = await fetch(`${url}/api/v1/meg`, { headers: { Authorization: `Bearer ${token}` } });
          assert.equal(response.status, 401);
          assert.equal(response.headers.get("www-authenticate"), "Bearer");
          assert.equal((await response.json()).code, "UNAUTHORIZED");
        }
      },
      { tokens: createOasisTokenServices() },
    );
    assert.deepEqual(identity.requests, []);
  } finally {
    await close(identity);
  }
});

test("Oasis exchange errors return safe responses without forwarding credentials", async (t) => {
  t.mock.method(console, "warn", () => undefined);
  for (const exchange of [
    (_request, response) => {
      response.statusCode = 500;
      json(response, { error_description: "private authentication failure" });
    },
    (_request, response) => response.end("invalid JSON"),
    (_request, response) => json(response, {}),
    (_request, response) => json(response, { access_token: " " }),
  ]) {
    const identity = await identityProvider(exchange);
    const token = identity.token();
    try {
      await withProxy(
        async (url) => {
          const response = await fetch(`${url}/api/v1/meg`, { headers: { Authorization: `Bearer ${token}` } });
          assert.equal(response.status, 503);
          const text = await response.text();
          assert.match(text, /OBO_UNAVAILABLE/);
          assert.ok(!text.includes(token));
          assert.doesNotMatch(text, /private authentication/);
        },
        { tokens: createOasisTokenServices() },
      );
    } finally {
      await close(identity);
    }
  }
});

test("all API paths reject anonymous requests without redirects or HTML", async () => {
  await withProxy(async (url) => {
    for (const path of ["/api", "/api/v1/meg", "/api/v1/unknown"]) {
      const response = await fetch(`${url}${path}`, { redirect: "manual" });
      assert.equal(response.status, 401, path);
      assert.equal(response.headers.get("location"), null);
      assert.equal(response.headers.get("www-authenticate"), "Bearer");
      assert.equal(response.headers.get("cache-control"), "no-store");
      assert.match(response.headers.get("content-type"), /application\/problem\+json/);
      assert.equal((await response.json()).code, "UNAUTHORIZED");
    }
    const unknown = await fetch(`${url}/api/v1/unknown`, { headers: authorization });
    assert.match(unknown.headers.get("content-type"), /application\/json/);
    assert.equal((await unknown.json()).path, "/api/v1/unknown");
  });
});

test("OBO replaces authorization and strips cookies and injected internal headers", async () => {
  await withProxy(async (url) => {
    const response = await fetch(`${url}/api/v1/meg?test=true`, {
      headers: { ...authorization, Cookie: "session=test", "obo-token": "injected", "x-csrf-token": "injected" },
    });
    assert.equal(response.status, 200);
    assert.equal(response.headers.get("cache-control"), "no-store");
    const body = await response.json();
    assert.equal(body.path, "/api/v1/meg?test=true");
    assert.equal(body.headers.authorization, "Bearer test-exchanged-token");
    assert.equal(body.headers.cookie, undefined);
    assert.equal(body.headers["obo-token"], undefined);
    assert.equal(body.headers["x-csrf-token"], undefined);
  });
});

test("configured origins permit intact JSON writes without CSRF tokens or cookies", async () => {
  await withProxy(
    async (url) => {
      for (const requestOrigin of [origin, "https://alternate.invalid"]) {
        for (const method of ["POST", "PUT", "PATCH", "DELETE"]) {
          const content = JSON.stringify({ name: "test draft", text: "tekst" });
          const response = await fetch(`${url}/api/v1/utkast`, {
            method,
            headers: { ...writeHeaders(requestOrigin), "Content-Type": "application/json; charset=utf-8" },
            body: content,
          });
          assert.equal(response.status, 200);
          assert.equal(response.headers.get("set-cookie"), null);
          assert.equal((await response.json()).body, content);
        }
      }
      const formerEndpoint = await fetch(`${url}/api/v1/csrf`, { headers: authorization });
      assert.equal(formerEndpoint.headers.get("set-cookie"), null);
      assert.equal((await formerEndpoint.json()).path, "/api/v1/csrf");
    },
    { origins: [origin, "https://alternate.invalid"] },
  );
});

test("writes reject missing, null, foreign and same-site sibling origins before OBO", async () => {
  let exchanges = 0;
  await withProxy(
    async (url) => {
      for (const mutation of [
        { Origin: undefined },
        { Origin: "" },
        { Origin: "null" },
        { Origin: "https://other.invalid" },
        { Origin: "https://sibling.workshop.invalid", "Sec-Fetch-Site": "same-site" },
        { Origin: `${origin}.attacker.invalid` },
        { Origin: `${origin}/` },
        { "Sec-Fetch-Site": "cross-site" },
      ]) {
        for (const method of ["POST", "PUT", "PATCH", "DELETE"]) {
          const headers = { ...writeHeaders(), ...mutation };
          if (headers.Origin === undefined) delete headers.Origin;
          const response = await fetch(`${url}/api/v1/utkast`, { method, headers, body: "{}" });
          assert.equal(response.status, 403);
          assert.equal(response.headers.get("access-control-allow-origin"), null);
          assert.equal((await response.json()).code, "CSRF_REJECTED");
        }
      }
      assert.equal(exchanges, 0);
    },
    {
      tokens: {
        ...fakeTokens,
        exchange: async () => {
          exchanges++;
          return "test-exchanged-token";
        },
      },
    },
  );
});

test("writes reject form-compatible or missing content types even with an allowed origin", async () => {
  let exchanges = 0;
  await withProxy(
    async (url) => {
      for (const type of [
        undefined,
        "application/x-www-form-urlencoded",
        "multipart/form-data; boundary=test",
        "text/plain",
        "application/jsonp",
      ]) {
        for (const method of ["POST", "PUT", "PATCH", "DELETE"]) {
          const headers = { ...authorization, Origin: origin };
          if (type !== undefined) headers["Content-Type"] = type;
          const response = await fetch(`${url}/api/v1/utkast`, {
            method,
            headers,
            body: Buffer.from('{"name":"test draft"}'),
          });
          assert.equal(response.status, 415);
          assert.equal((await response.json()).code, "UNSUPPORTED_MEDIA_TYPE");
        }
      }
      assert.equal(exchanges, 0);
    },
    {
      tokens: {
        ...fakeTokens,
        exchange: async () => {
          exchanges++;
          return "test-exchanged-token";
        },
      },
    },
  );
});

test("token service failures return safe errors", async () => {
  for (const services of [
    {
      ...fakeTokens,
      validate: async () => {
        throw new Error("private validation failure");
      },
    },
    { ...fakeTokens, exchange: async () => null },
    {
      ...fakeTokens,
      exchange: async () => {
        throw new Error("private exchange failure");
      },
    },
  ]) {
    await withProxy(
      async (url) => {
        const response = await fetch(`${url}/api/v1/meg`, { headers: authorization });
        assert.equal(response.status, 503);
        const body = await response.text();
        assert.match(body, /AUTH_UNAVAILABLE|OBO_UNAVAILABLE/);
        assert.doesNotMatch(body, /test-user-token|private/);
      },
      { tokens: services },
    );
  }
});

test("backend authorization failures preserve status, challenge and Problem content", async () => {
  for (const status of [401, 403]) {
    await withProxy(
      async (url) => {
        const response = await fetch(`${url}/api/v1/utkast`, {
          method: "POST",
          headers: writeHeaders(),
          body: "{}",
        });
        assert.equal(response.status, status);
        assert.match(response.headers.get("content-type"), /application\/problem\+json/);
        assert.equal(response.headers.get("cache-control"), "no-store");
        if (status === 401) assert.equal(response.headers.get("www-authenticate"), "Bearer");
        assert.equal((await response.json()).code, status === 401 ? "UNAUTHORIZED" : "EDITOR_REQUIRED");
      },
      {
        handler: (request, response) => {
          request.resume();
          response.statusCode = status;
          response.setHeader("Content-Type", "application/problem+json");
          response.setHeader("Cache-Control", "public");
          if (status === 401) response.setHeader("WWW-Authenticate", "Bearer");
          response.end(JSON.stringify({ status, code: status === 401 ? "UNAUTHORIZED" : "EDITOR_REQUIRED" }));
        },
      },
    );
  }
});

test("backend connection errors return a safe service error", async () => {
  await withProxy(
    async (url) => {
      const response = await fetch(`${url}/api/v1/meg`, { headers: authorization });
      assert.equal(response.status, 503);
      assert.equal((await response.json()).code, "BACKEND_UNAVAILABLE");
    },
    { handler: (request) => request.socket.destroy() },
  );
});

test("backend responses slower than the external GitHub timeout still complete", { timeout: 25_000 }, async () => {
  await withProxy(
    async (url) => {
      const response = await fetch(`${url}/api/v1/utkast/test/forslag`, {
        method: "POST",
        headers: writeHeaders(),
        body: "{}",
        signal: AbortSignal.timeout(20_000),
      });
      assert.equal(response.status, 200);
      assert.deepEqual(await response.json(), { suggestion: "Test" });
    },
    {
      handler: (request, response) => {
        request.resume();
        setTimeout(() => json(response, { suggestion: "Test" }), 11_000);
      },
    },
  );
});

test("legacy pages and assets never consult token services", async () => {
  const unexpected = () => {
    throw new Error("Legacy requests must not consult authentication");
  };
  await withProxy(
    async (url) => {
      for (const path of ["/", "/templates", "/template/autobrev/Test", "/assets/app.js", "/brevbaker/templates"]) {
        for (const headers of [{}, { Authorization: "Bearer expired-test-token" }]) {
          const response = await fetch(`${url}${path}`, { headers });
          assert.equal(response.status, 200, path);
          assert.match(await response.text(), /public frontend/);
        }
      }
    },
    { tokens: { token: unexpected, validate: unexpected, exchange: unexpected } },
  );
});
