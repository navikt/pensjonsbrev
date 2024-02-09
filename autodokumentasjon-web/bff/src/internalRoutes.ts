import { deleteCachedTokens } from "@navikt/backend-for-frontend-utils";
import { Express } from "express";

export const internalRoutes = (server: Express) => {
  server.get("/bff/logout", (request, response) => {
    deleteCachedTokens(request);
    response.redirect("/oauth2/logout");
  });
};
