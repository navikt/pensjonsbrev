import { Express } from "express";
import morgan from "morgan";

export function setupLogging(server: Express) {
  const loggingFormat = {
    timestamp: ":date[iso]",
    method: ":method",
    url: ":url",
    statusCode: ":status",
  };

  const errorLoggingFormat = {
    ...loggingFormat,
    level: "error",
  };

  server.use(
    morgan(JSON.stringify(loggingFormat), {
      skip: (request) => !request.path.startsWith("/bff") || request.path.startsWith("/bff/internal/logg"),
    }),
    morgan(JSON.stringify(errorLoggingFormat), {
      skip: (request) => !request.path.startsWith("/bff/internal/logg"),
    }),
  );
}
