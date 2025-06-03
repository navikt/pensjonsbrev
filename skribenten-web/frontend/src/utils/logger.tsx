/* eslint-disable no-console */

import { loggFeil } from "~/api/bff-endpoints";

export const logger = {
  error: (error: unknown) => {
    if (error instanceof Error) {
      const data = {
        type: "error",
        message: error.message,
        stack: error.stack,
        jsonContent: {
          url: globalThis.location.href,
          userAgent: globalThis.navigator.userAgent,
        },
      };
      loggFeil(data).catch((error: unknown) => {
        console.error("Unable to log error message: ", data, " err: ", error);
      });
    } else {
      const data = {
        type: "error",
        message: "Error som ikke var av type error",
        stack: "",
        jsonContent: JSON.stringify(error),
      };
      loggFeil(data).catch((error: unknown) => {
        console.error("Unable to log error message: ", data, " err: ", error);
      });
    }
  },
};
