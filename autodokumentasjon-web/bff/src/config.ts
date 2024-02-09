import { requireEnvironment } from "@navikt/backend-for-frontend-utils";

const app = {
  nodeEnv: requireEnvironment("NODE_ENV"),
  host: requireEnvironment("EXPRESS_HOST"),
  port: Number(requireEnvironment("EXPRESS_PORT")),
};

const azureApp = {
  clientId: requireEnvironment("AZURE_APP_CLIENT_ID"),
  issuer: requireEnvironment("AZURE_OPENID_CONFIG_ISSUER"),
  tokenEndpoint: requireEnvironment("AZURE_OPENID_CONFIG_TOKEN_ENDPOINT"),
  jwk: requireEnvironment("AZURE_APP_JWK"),
  openIdJwkUris: requireEnvironment("AZURE_OPENID_CONFIG_JWKS_URI"),
};

const brevbakerApiProxy = {
  url: requireEnvironment("BREVBAKER_API_URL"),
  scope: requireEnvironment("BREVBAKER_API_SCOPE"),
};

export default {
  app,
  azureApp,
  brevbakerApiProxy,
};
