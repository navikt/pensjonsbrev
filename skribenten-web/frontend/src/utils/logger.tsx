import { loggFeil } from "~/api/bff-endpoints";
import {AxiosError} from "axios";

export const logError = async (error: unknown, status: number | undefined) => {
  if (error instanceof Error) {
    const data = {
      type: "error",
      message: error.message,
      stack: error.stack,
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
      stack: "",
      jsonContent: JSON.stringify(error),
    };
    return loggFeil(data);
  }
};
