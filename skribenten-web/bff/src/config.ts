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

const skribentenBackendApiProxy = {
  url: requireEnvironment("SKRIBENTEN_API_URL"),
  scope: requireEnvironment("SKRIBENTEN_API_SCOPE"),
};

export default {
  app,
  azureApp,
  skribentenBackendApiProxy,
};

function requireEnvironment(environmentName: string) {
  const environmentContent = process.env[environmentName];
  if (!environmentContent) {
    throw new Error("Missing environment variable with name: " + environmentName);
  }
  return environmentContent;
}
