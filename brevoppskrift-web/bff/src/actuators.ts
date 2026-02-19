import type { Express } from "express";

export function setupActuators(server: Express) {
  // @ts-expect-error skjønner ikke hvorfor lint er så kranglete her
  server.get("/internal/health/liveness", (request, response) => response.status(200).end());
  // @ts-expect-error skjønner ikke hvorfor lint er så kranglete her
  server.get("/internal/health/readiness", (request, response) => response.status(200).end());
}
