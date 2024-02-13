import { requireEnvironment } from "@navikt/backend-for-frontend-utils";

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
