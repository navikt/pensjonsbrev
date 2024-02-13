import type { Express } from "express";

export function setupActuators(server: Express) {
  server.get("/internal/health/liveness", (request, response) => response.status(200).end());
  server.get("/internal/health/readiness", (request, response) => response.status(200).end());
}
