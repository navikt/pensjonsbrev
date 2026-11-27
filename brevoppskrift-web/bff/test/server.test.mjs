import assert from "node:assert/strict";
import { test } from "node:test";

import { close, identityProvider, json, listening } from "./tokenFixtures.mjs";

test("wired server preserves anonymous Vite, legacy and Word routes while protecting only API", async (t) => {
  const info = t.mock.method(console, "info", () => undefined);
  const errors = t.mock.method(console, "error", () => undefined);
  const backend = await listening(async (request, response) => {
    if (request.url.startsWith("/failure")) {
      request.socket.destroy();
      return;
    }
    const chunks = [];
    for await (const chunk of request) chunks.push(chunk);
    if (request.url.endsWith("/docx")) {
      response.setHeader("Content-Type", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
      response.setHeader("Content-Disposition", 'attachment; filename="test.docx"');
      response.end(Buffer.from([80, 75, 3, 4]));
    } else {
      json(response, { path: request.url, headers: request.headers, body: Buffer.concat(chunks).toString() });
    }
  });
  const identity = await identityProvider();
  Object.assign(process.env, {
    NODE_ENV: "test",
    EXPRESS_HOST: "127.0.0.1",
    EXPRESS_PORT: "8080",
    BREVBAKER_API_URL: backend.url,
    BREVOPPSKRIFT_BACKEND_URL: backend.url,
    BREVOPPSKRIFT_BACKEND_SCOPE: "api://dev-gcp.pensjonsbrev.brevoppskrift-backend/.default",
    PUBLIC_ORIGINS: "http://localhost:8088",
  });
  const { server } = await import("../dist/server.js");
  const bff = await listening(server);
  const accessLogs = () => info.mock.calls.map((call) => JSON.parse(call.arguments[0]));
  try {
    for (const path of ["/internal/health/liveness", "/internal/health/readiness"]) {
      assert.equal((await fetch(`${bff.url}${path}`)).status, 200);
    }
    const vite = await fetch(`${bff.url}/vite-on`, { redirect: "manual" });
    assert.equal(vite.status, 302);
    const cookie = vite.headers.get("set-cookie").split(";")[0];
    for (const path of ["/", "/templates", "/template/autobrev/Test"]) {
      const response = await fetch(`${bff.url}${path}`, { headers: { Cookie: cookie } });
      assert.equal(response.status, 200, path);
      assert.match(response.headers.get("content-type"), /text\/html/);
      assert.match(await response.text(), /localhost:5173/);
    }
    const legacy = await fetch(`${bff.url}/brevbaker/templates?private-query=test-value`, {
      headers: { Authorization: "Bearer test-session-token", Cookie: "test-session=example" },
    });
    assert.equal(legacy.status, 200);
    const body = await legacy.json();
    assert.equal(body.path, "/templates?private-query=test-value");
    assert.equal(body.headers.authorization, undefined);
    assert.equal(body.headers.cookie, undefined);
    const word = await fetch(`${bff.url}/brevbaker/templates/Test/docx`);
    assert.equal(word.status, 200);
    assert.match(word.headers.get("content-type"), /wordprocessingml/);
    assert.equal(word.headers.get("content-disposition"), 'attachment; filename="test.docx"');
    assert.deepEqual(Buffer.from(await word.arrayBuffer()), Buffer.from([80, 75, 3, 4]));
    assert.deepEqual(
      accessLogs().map(({ route, statusCode }) => ({ route, statusCode })),
      [
        { route: "/brevbaker", statusCode: 200 },
        { route: "/brevbaker", statusCode: 200 },
      ],
    );
    const failure = await fetch(`${bff.url}/brevbaker/failure?private-query=test-value`, {
      headers: { Authorization: "Bearer test-session-token", Cookie: "test-session=example" },
    });
    assert.equal(failure.status, 504);
    await failure.text();
    assert.deepEqual(
      errors.mock.calls.map((call) => call.arguments),
      [["Brevbaker proxy error", { code: "ECONNRESET" }]],
    );
    assert.deepEqual(identity.requests, []);

    const anonymous = await fetch(`${bff.url}/api/v1/meg`, { redirect: "manual" });
    assert.equal(anonymous.status, 401);
    assert.equal(anonymous.headers.get("location"), null);
    assert.match(anonymous.headers.get("content-type"), /application\/problem\+json/);
    const oversized = await fetch(`${bff.url}/api/v1/utkast`, {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: `value=${"x".repeat(120 * 1024)}`,
    });
    assert.equal(oversized.status, 401);
    assert.match(oversized.headers.get("content-type"), /application\/problem\+json/);

    const token = identity.token();
    const authorization = { Authorization: `Bearer ${token}` };
    const reader = await fetch(`${bff.url}/api/v1/meg?private-query=test-value`, { headers: authorization });
    assert.equal(reader.status, 200);
    assert.equal((await reader.json()).headers.authorization, `Bearer ${identity.exchangedToken}`);
    const rejected = await fetch(`${bff.url}/api/v1/utkast`, {
      method: "POST",
      headers: { ...authorization, "Content-Type": "application/json" },
      body: '{"text":"private-body"}',
    });
    assert.equal(rejected.status, 403);
    await rejected.json();
    const form = await fetch(`${bff.url}/api/v1/utkast`, {
      method: "POST",
      headers: {
        ...authorization,
        Origin: "http://localhost:8088",
        "Content-Type": "application/x-www-form-urlencoded",
      },
      body: "value=private-body",
    });
    assert.equal(form.status, 415);
    await form.json();
    const content = '{"text":"private-body"}';
    const write = await fetch(`${bff.url}/api/v1/utkast`, {
      method: "POST",
      headers: { ...authorization, Origin: "http://localhost:8088", "Content-Type": "application/json" },
      body: content,
      signal: AbortSignal.timeout(5000),
    });
    assert.equal(write.status, 200);
    assert.equal((await write.json()).body, content);
    assert.equal(identity.requests.length, 1);
    const access = accessLogs();
    assert.deepEqual(
      access.map(({ method, route, statusCode }) => ({ method, route, statusCode })),
      [
        { method: "GET", route: "/brevbaker", statusCode: 200 },
        { method: "GET", route: "/brevbaker", statusCode: 200 },
        { method: "GET", route: "/brevbaker", statusCode: 504 },
        { method: "GET", route: "/api", statusCode: 401 },
        { method: "POST", route: "/api", statusCode: 401 },
        { method: "GET", route: "/api", statusCode: 200 },
        { method: "POST", route: "/api", statusCode: 403 },
        { method: "POST", route: "/api", statusCode: 415 },
        { method: "POST", route: "/api", statusCode: 200 },
      ],
    );
    for (const entry of access) {
      assert.match(entry.timestamp, /^\d{4}-\d\d-\d\dT/);
      assert.equal(typeof entry.responseTimeMs, "number");
      assert.ok(entry.responseTimeMs >= 0);
    }
    const logs = JSON.stringify([...info.mock.calls, ...errors.mock.calls].map((call) => call.arguments));
    assert.doesNotMatch(
      logs,
      /private-query|test-value|private-body|test-session|127\.0\.0\.1|\/templates|\/failure|\/utkast|\/meg/,
    );
    assert.ok(!logs.includes(token));
    assert.ok(!logs.includes(identity.exchangedToken));
  } finally {
    await Promise.all([bff, identity, backend].map(close));
  }
});
