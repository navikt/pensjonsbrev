/* eslint-disable no-console */

import { loggFeil } from "~/api/bff-endpoints";

const GYLDIG_FNR = (input: string | undefined) => /^\d{11}$/.test(input ?? "");

function sanitizeUrlPossibleFnr(url?: string): string {
  if (url) {
    const splittedUrl = url.split("/");
    return splittedUrl
      .map((urlpart) => {
        if (GYLDIG_FNR(urlpart)) {
          return urlpart.substring(0, 5).concat("******");
        }
        return urlpart;
      })
      .join("/");
  }
  return "";
}

const defaultContext = {
  url: sanitizeUrlPossibleFnr(globalThis.location.href),
  userAgent: globalThis.navigator.userAgent,
};

export const logger = {
  error: (error: unknown) => {
    if (error instanceof Error) {
      const data = { type: "error", message: error.message, stack: error.stack, jsonContent: { ...defaultContext } };
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

export const setupWindowOnError = () => {
  addEventListener("error", (event) => {
    const { error: kanskjeError, message } = event;

    const error = kanskjeError || {};
    if (import.meta.env.MODE === "development") {
      console.error(error.message, error.stack);
    } else {
      if (message !== "ResizeObserver loop completed with undelivered notifications.") {
        logger.error(error);
      }

      if (error.stack && error.stack?.indexOf("invokeGuardedCallbackDev") >= 0 && !error.alreadySeen) {
        error.alreadySeen = true;
        event.preventDefault();
        return true;
      }
    }
    return true;
  });
};
