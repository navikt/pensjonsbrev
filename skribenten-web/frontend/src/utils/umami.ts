interface UmamiTracker {
  track: (
    eventName: string,
    eventData?: Record<string, string | number | boolean>,
  ) => void;
}

declare global {
  var umami: UmamiTracker | undefined;
}

export type UmamiEventName =
  | "brev opprettet"
  | "brev attestert"
  | "brev sendt"
  | "brev klar status endret"
  | "brev distribusjonstype endret"
  | "tekst limt inn"
  | "pesys redirect"
  | "pesys feil"
  | "attestering blokkert";

export interface UmamiEventData {
  [key: string]: string | number | boolean | undefined;
}

const isUmamiAvailable = (): boolean =>
  typeof globalThis.window !== "undefined" &&
  globalThis.umami !== undefined &&
  typeof globalThis.umami.track === "function";

export const trackEvent = (
  eventName: UmamiEventName,
  eventData?: UmamiEventData,
): void => {
  if (!isUmamiAvailable()) {
    if (import.meta.env.DEV) {
      // biome-ignore lint/suspicious/noConsole: intentional dev logging
      console.log("[Umami]", eventName, eventData);
    }
    return;
  }

  try {
    const cleanedData = eventData
      ? (Object.fromEntries(
          Object.entries(eventData).filter(([, v]) => v !== undefined),
        ) as Record<string, string | number | boolean>)
      : undefined;

    globalThis.umami?.track(eventName, cleanedData);
  } catch {
    // Analytics should not break the app
  }
};
