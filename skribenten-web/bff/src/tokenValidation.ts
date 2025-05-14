import { getToken, validateToken } from "@navikt/oasis";
import { NextFunction, Request, Response } from "express";

export const verifyToken = async (request: Request, response: Response, next: NextFunction): Promise<void> => {
  const token = getToken(request);
  if (!token) {
    response.status(401).send();
    return;
  }

  const validation = await validateToken(token as string);
  if (!validation.ok) {
    console.log("Invalid token validation", validation);
    response.status(403).send();
  }

  next();
};
