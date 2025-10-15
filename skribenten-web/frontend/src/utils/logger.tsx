import { loggFeil } from "~/api/bff-endpoints";

export const logError = async (error: unknown, status: number | undefined) => {
  if (error instanceof Error) {
    const data = {
      type: "error",
      message: error.message,
      stack: error.stack,
      status: status,
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
