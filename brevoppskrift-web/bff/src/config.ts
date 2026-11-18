const app = {
  nodeEnv: requireEnvironment("NODE_ENV"),
  host: requireEnvironment("EXPRESS_HOST"),
  port: Number(requireEnvironment("EXPRESS_PORT")),
};

const brevbakerApiProxy = {
  url: requireEnvironment("BREVBAKER_API_URL"),
};

const workshop = {
  backendUrl: requireEnvironment("BREVOPPSKRIFT_BACKEND_URL"),
  scope: requireEnvironment("BREVOPPSKRIFT_BACKEND_SCOPE"),
  origin: new URL(requireEnvironment("PUBLIC_ORIGIN")).origin,
  origins: (process.env.PUBLIC_ORIGINS ?? "")
    .split(",")
    .filter(Boolean)
    .map((origin) => new URL(origin.trim()).origin),
};

export default {
  app,
  brevbakerApiProxy,
  workshop,
};

function requireEnvironment(environmentName: string) {
  const environmentContent = process.env[environmentName];
  if (!environmentContent) {
    throw new Error(`Missing environment variable with name: ${environmentName}`);
  }
  return environmentContent;
}
