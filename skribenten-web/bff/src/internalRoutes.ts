import { deleteCachedTokens } from "@navikt/backend-for-frontend-utils";
import { Express } from "express";
import { jwtDecode } from "jwt-decode";

import { getTokenFromRequestHeader } from "./tokenValidation";

export const internalRoutes = (server: Express) => {
  server.get("/bff/logout", (request, response) => {
    deleteCachedTokens(request);
    response.redirect("/oauth2/logout");
  });

  server.get("/bff/userinfo", (request, response) => {
    const token = getTokenFromRequestHeader(request);

    if (!token) {
      return response.status(400).json({ message: "Bruker er ikke logget inn" });
    }

    try {
      const { name, NAVident } = jwtDecode<{ name: string; NAVident: string }>(token);

      return response.json({
        name,
        navident: NAVident,
      });
    } catch {
      return response.status(404).json({ message: "Could not get username" });
    }
  });
};
