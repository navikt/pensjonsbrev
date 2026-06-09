import { AxiosError } from "axios";

import { loggFeil } from "~/api/bff-endpoints";

export const logError = async (error: unknown, status: number | undefined) => {
  if (error instanceof Error) {
    const data = {
      type: "error",
      message: error.message,
      stack: error.stack,
      level: findLogLevel(error, status),
      status: status,
      requestId: error instanceof AxiosError ? error.response?.headers["x-request-id"] : undefined,
      jsonContent: {
        url: globalThis.location.href,
        userAgent: globalThis.navigator.userAgent,
      },
    };
    return loggFeil(data);
  } else {
    const data = {
      type: "error",
      message: "Error som ikke var av type error",
      status: status,
      level: findLogLevel(error, undefined),
      stack: "",
      jsonContent: JSON.stringify(error),
    };
    return loggFeil(data);
  }

  function findLogLevel(error: unknown, status: number | undefined): string {
    if (error instanceof AxiosError) {
      return "INFO";
    }
    switch (status) {
      case undefined:
        return "ERROR";
      case 401:
      case 403:
      case 404:
      case 504:
        return "WARN";
      case 422:
        return "INFO";
      default:
        return "ERROR";
    }
  }
};
