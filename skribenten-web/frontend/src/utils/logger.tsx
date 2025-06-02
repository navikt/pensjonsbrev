/* eslint-disable no-console */
import type { ErrorInfo } from "react";

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

export interface IStackLineNoColumnNo {
  readonly message: unknown;
  readonly error: unknown;
}

interface ErrorData {
  msg: string;
  errorInfo?: ErrorInfo;
  apiErrorInfo?: ApiErrorInfo;
  err?: Error;
}

interface ApiErrorInfo {
  url: string;
  method: string;
  error?: JsonError;
}

interface JsonError {
  status: number;
  detail: string;
  code?: string;
  meta?: Record<string, unknown>;
}

export const logger = {
  info: (stackLineNoColumnNo: IStackLineNoColumnNo) => {
    const data = { type: "info", stackInfo: stackLineNoColumnNo, jsonContent: { ...defaultContext } };
    loggFeil(data).catch((error: unknown) => {
      console.error("Unable to log info message: ", data, " err: ", error);
    });
  },
  error: (stackLineNoColumnNo: IStackLineNoColumnNo) => {
    const data = { type: "error", stackInfo: stackLineNoColumnNo, jsonContent: { ...defaultContext } };
    loggFeil(data).catch((error: unknown) => {
      console.error("Unable to log error message: ", data, " err: ", error);
    });
  },
  generalError: (info: ErrorData) => {
    const data = { type: "Error", data: info, jsonContent: { ...defaultContext } };
    loggFeil(data).catch((error: unknown) => {
      console.error("Unable to log error message: ", data, " err: ", error);
    });
  },
  generalWarning: (info: ErrorData) => {
    const data = { type: "warning", data: info, jsonContent: { ...defaultContext } };
    loggFeil(data).catch((error: unknown) => {
      console.error("Unable to log error message: ", data, " err: ", error);
    });
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
        logger.error({ message, error: JSON.stringify(error) });
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
