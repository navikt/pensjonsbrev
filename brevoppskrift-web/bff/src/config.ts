const app = {
  nodeEnv: requireEnvironment("NODE_ENV"),
  host: requireEnvironment("EXPRESS_HOST"),
  port: Number(requireEnvironment("EXPRESS_PORT")),
};

const brevbakerApiProxy = {
  url: requireEnvironment("BREVBAKER_API_URL"),
};

const workshop = {
  backendUrl: httpUrl("BREVOPPSKRIFT_BACKEND_URL").href,
  scope: requireEnvironment("BREVOPPSKRIFT_BACKEND_SCOPE"),
  origins: requireEnvironment("PUBLIC_ORIGINS")
    .split(",")
    .map((origin) => origin.trim())
    .filter(Boolean)
    .map((origin) => httpUrl("PUBLIC_ORIGINS", origin).origin),
};

for (const name of [
  "AZURE_OPENID_CONFIG_JWKS_URI",
  "AZURE_OPENID_CONFIG_ISSUER",
  "NAIS_TOKEN_ENDPOINT",
  "NAIS_TOKEN_INTROSPECTION_ENDPOINT",
  "NAIS_TOKEN_EXCHANGE_ENDPOINT",
]) {
  httpUrl(name);
}
requireEnvironment("AZURE_APP_CLIENT_ID");

export default {
  app,
  brevbakerApiProxy,
  workshop,
};

function httpUrl(environmentName: string, value = requireEnvironment(environmentName)) {
  const url = new URL(value);
  if (!["http:", "https:"].includes(url.protocol) || url.username || url.password || url.search || url.hash) {
    throw new Error(`Invalid URL in environment variable: ${environmentName}`);
  }
  return url;
}

function requireEnvironment(environmentName: string) {
  const environmentContent = process.env[environmentName];
  if (!environmentContent) {
    throw new Error(`Missing environment variable with name: ${environmentName}`);
  }
  return environmentContent;
}
