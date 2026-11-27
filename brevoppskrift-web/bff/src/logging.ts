import { type Express, type Request, type Response } from "express";
import morgan from "morgan";

export function setupLogging(server: Express) {
  for (const route of ["/api", "/brevbaker"]) {
    server.use(
      route,
      morgan<Request, Response>(
        (tokens, request, response) =>
          JSON.stringify({
            timestamp: new Date().toISOString(),
            method: request.method,
            route,
            statusCode: response.headersSent ? response.statusCode : null,
            responseTimeMs: Number(tokens["response-time"]?.(request, response)),
          }),
        { stream: { write: (message) => console.info(message.trimEnd()) } },
      ),
    );
  }
}
