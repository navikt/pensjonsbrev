import { getToken } from "@navikt/oasis";
import bodyParser from "body-parser";
import cookieParser from "cookie-parser";
import { Express } from "express";
import { jwtDecode } from "jwt-decode";

import config from "./config.js";

export const internalRoutes = (server: Express) => {
  server.get("/bff/internal/logout", (request, response) => {
    response.redirect("/oauth2/logout");
  });

  server.get("/bff/internal/userinfo", (request, response): void => {
    const token = getToken(request);

    if (!token) {
      response.status(400).json({ message: "Bruker er ikke logget inn" });
      return;
    }

    try {
      const { name, NAVident } = jwtDecode<{ name: string; NAVident: string }>(token as string);

      response.json({
        name,
        navident: NAVident,
      });
    } catch {
      response.status(404).json({ message: "Could not get username" });
    }
  });

  const baseUrls = config.baseUrls;
  server.get("/bff/internal/baseurls", (_, response) => {
    response.json({
      psak: baseUrls.psak,
    });
  });

  server.post("/bff/internal/logg", bodyParser.json(), cookieParser(), (request, response) => {
    if (request.cookies["use-local-vite-server"] === "true") {
      response.status(200).end();
      return;
    }

    const level = request.statusCode === 403 || request.statusCode === 404 ? "WARN" : "ERROR";

    const body = request.body;
    console.error(
      JSON.stringify({
        level: level,
        statusCode: level == "ERROR" ? "500" : request.statusCode,
        timestamp: body.jsonContent.timestamp,
        message: 'Feil fra frontend: ' + body.message + ": " + body.jsonContent.url,
        stack_trace: body.stack,
      }),
    );
    response.status(200).end();
  });
};
