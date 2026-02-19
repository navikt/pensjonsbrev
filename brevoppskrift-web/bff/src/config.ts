const app = {
  nodeEnv: requireEnvironment("NODE_ENV"),
  host: requireEnvironment("EXPRESS_HOST"),
  port: Number(requireEnvironment("EXPRESS_PORT")),
};

const brevbakerApiProxy = {
  url: requireEnvironment("BREVBAKER_API_URL"),
};

export default {
  app,
  brevbakerApiProxy,
};

function requireEnvironment(environmentName: string) {
  const environmentContent = process.env[environmentName];
  if (!environmentContent) {
    throw new Error("Missing environment variable with name: " + environmentName);
  }
  return environmentContent;
}
