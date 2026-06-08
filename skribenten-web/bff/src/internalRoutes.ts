import bodyParser from "body-parser";
import cookieParser from "cookie-parser";
import { type Express } from "express";

import config from "./config.js";

export const internalRoutes = (server: Express) => {
  server.get("/bff/api/logout", (_request, response) => {
    response.redirect("/oauth2/logout");
  });

  const baseUrls = config.baseUrls;
  server.get("/bff/api/baseurls", (request, response) => {
    let psak = baseUrls.psak;

    const requestHost = request.hostname;
    if (requestHost.endsWith("ansatt.dev.nav.no")) {
      psak = psak.replace("intern.dev.nav.no", "ansatt.dev.nav.no");
    }

    response.json({
      psak,
    });
  });

  function findLogLevel(status: number | undefined): string {
    switch (status) {
      case undefined:
        return "ERROR";
      case 401:
      case 403:
      case 404:
      case 504:
        return "WARN";
      case 422:
        return "INFO";
      default:
        return "ERROR";
    }
  }

  server.post("/bff/api/logg", bodyParser.json(), cookieParser(), (request, response) => {
    if (request.cookies["use-local-vite-server"] === "true") {
      response.status(200).end();
      return;
    }

    const body = request.body;

    console.error(
      JSON.stringify({
        level: findLogLevel(body.status),
        statusCode: body.status,
        timestamp: body.jsonContent.timestamp,
        message: `Feil fra frontend: ${body.message}: ${body.jsonContent.url}`,
        stack_trace: body.stack,
        x_correlationId: body.requestId,
      }),
    );
    response.status(200).end();
  });
};
