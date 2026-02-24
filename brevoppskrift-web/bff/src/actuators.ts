import type { Express } from "express";

export function setupActuators(server: Express) {
  server.get("/internal/health/liveness", (_request, response): void => {
    response.status(200).end();
  });
  server.get("/internal/health/readiness", (_request, response): void => {
    response.status(200).end();
  });
}
