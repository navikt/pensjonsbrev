import { expressjwt, GetVerificationKey } from "express-jwt";
import jwksRsa from "jwks-rsa";

import config from "./config.js";

export const verifyJWTToken = expressjwt({
  secret: jwksRsa.expressJwtSecret({
    cache: true,
    rateLimit: true,
    jwksRequestsPerMinute: 5,
    jwksUri: config.azureApp.openIdJwkUris,
  }) as GetVerificationKey,

  algorithms: ["RS256"],
  audience: config.azureApp.clientId,
  issuer: config.azureApp.issuer,
});
