import { getToken } from "@navikt/oasis";
import { Express } from "express";
import { jwtDecode } from "jwt-decode";

export const internalRoutes = (server: Express) => {
  server.get("/bff/internal/logout", (request, response) => {
    response.redirect("/oauth2/logout");
  });

  server.get("/bff/internal/userinfo", (request, response) => {
    const token = getToken(request);

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
