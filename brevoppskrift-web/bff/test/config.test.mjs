import assert from "node:assert/strict";
import { spawnSync } from "node:child_process";
import { test } from "node:test";

const environment = {
  NODE_ENV: "test",
  EXPRESS_HOST: "127.0.0.1",
  EXPRESS_PORT: "8080",
  BREVBAKER_API_URL: "http://brevbaker.invalid",
  BREVOPPSKRIFT_BACKEND_URL: "http://workshop.invalid",
  BREVOPPSKRIFT_BACKEND_SCOPE: "api://workshop.invalid/.default",
  PUBLIC_ORIGINS: "https://frontend.invalid,https://alternate.invalid",
  AZURE_OPENID_CONFIG_JWKS_URI: "https://identity.invalid/jwks",
  AZURE_OPENID_CONFIG_ISSUER: "https://identity.invalid/",
  AZURE_APP_CLIENT_ID: "test-bff",
  NAIS_TOKEN_ENDPOINT: "http://texas.invalid/api/v1/token",
  NAIS_TOKEN_INTROSPECTION_ENDPOINT: "http://texas.invalid/api/v1/introspect",
  NAIS_TOKEN_EXCHANGE_ENDPOINT: "http://texas.invalid/api/v1/token/exchange",
};

function readConfig(env, module = "config") {
  return spawnSync(process.execPath, ["--input-type=module", "-e", `await import("./dist/${module}.js")`], {
    cwd: new URL("../", import.meta.url),
    env,
    encoding: "utf8",
  });
}

test("workshop starts with Oasis configuration without Azure secrets or an activation flag", () => {
  const result = readConfig(environment, "server");
  assert.equal(result.status, 0, result.stderr);
});

test("missing workshop or Oasis configuration fails at startup", () => {
  for (const key of [
    "BREVOPPSKRIFT_BACKEND_URL",
    "BREVOPPSKRIFT_BACKEND_SCOPE",
    "PUBLIC_ORIGINS",
    "AZURE_OPENID_CONFIG_JWKS_URI",
    "AZURE_OPENID_CONFIG_ISSUER",
    "AZURE_APP_CLIENT_ID",
    "NAIS_TOKEN_ENDPOINT",
    "NAIS_TOKEN_INTROSPECTION_ENDPOINT",
    "NAIS_TOKEN_EXCHANGE_ENDPOINT",
  ]) {
    const env = { ...environment };
    delete env[key];
    const result = readConfig(env);
    assert.notEqual(result.status, 0, key);
    assert.match(result.stderr, new RegExp(`Missing environment variable with name: ${key}`));
  }
});

test("invalid URL protocols and credential-bearing URLs fail at startup", () => {
  for (const key of ["BREVOPPSKRIFT_BACKEND_URL", "PUBLIC_ORIGINS", "NAIS_TOKEN_EXCHANGE_ENDPOINT"]) {
    for (const value of [
      "file:///tmp/example",
      "https://user:example@service.invalid",
      "https://service.invalid/#fragment",
    ]) {
      const result = readConfig({ ...environment, [key]: value });
      assert.notEqual(result.status, 0, `${key}: ${value}`);
      assert.match(result.stderr, /Invalid URL in environment variable/);
    }
  }
});

test("explicit origins can use different protocols without a CSRF cookie", () => {
  const result = readConfig(
    { ...environment, PUBLIC_ORIGINS: "https://frontend.invalid,http://alternate.invalid" },
    "server",
  );
  assert.equal(result.status, 0, result.stderr);
});

test("an origin list containing only whitespace or separators fails at startup", () => {
  for (const value of [" ", " , , "]) {
    const result = readConfig({ ...environment, PUBLIC_ORIGINS: value }, "server");
    assert.notEqual(result.status, 0);
    assert.match(result.stderr, /Workshop origins must not be empty/);
  }
});
