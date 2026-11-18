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
  PUBLIC_ORIGIN: "https://frontend.invalid",
};

function readConfig(env) {
  return spawnSync(process.execPath, ["--input-type=module", "-e", 'await import("./dist/config.js")'], {
    cwd: new URL("../", import.meta.url),
    env,
    encoding: "utf8",
  });
}

test("workshop is configured without an activation flag", () => {
  const result = readConfig(environment);
  assert.equal(result.status, 0, result.stderr);
});

test("missing workshop configuration fails at startup", () => {
  for (const key of ["BREVOPPSKRIFT_BACKEND_URL", "BREVOPPSKRIFT_BACKEND_SCOPE", "PUBLIC_ORIGIN"]) {
    const env = { ...environment };
    delete env[key];
    const result = readConfig(env);
    assert.notEqual(result.status, 0);
    assert.match(result.stderr, new RegExp(`Missing environment variable with name: ${key}`));
  }
});
