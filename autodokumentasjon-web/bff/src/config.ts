import { requireEnvironment } from "@navikt/backend-for-frontend-utils";

const app = {
  nodeEnv: requireEnvironment("NODE_ENV"),
  host: requireEnvironment("EXPRESS_HOST"),
  port: Number(requireEnvironment("EXPRESS_PORT")),
};

export default {
  app,
};
