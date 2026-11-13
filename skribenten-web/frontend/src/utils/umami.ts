interface UmamiTracker {
  track: (eventName: string, eventData?: Record<string, string | number | boolean>) => void;
}

declare global {
  var umami: UmamiTracker | undefined;
}

export type UmamiEventName =
  | "attestering blokkert"
  | "brev attestert"
  | "brev distribusjonstype endret"
  | "brev klar status endret"
  | "brev opprettet"
  | "exstream brev valgt"
  | "brev sendt"
  | "endre mottaker klikket"
  | "pdfjs mangler nettleserstøtte"
  | "pesys feil"
  | "pesys omdirigering"
  | "samhandler valgt"
  | "tekst limt inn"
  | "tekst erstattet"
  | "tid brukt i attestering"
  | "tid brukt i brevbehandler"
  | "tid brukt i brevvelger"
  | "tid brukt i editor"
  | "tilbakestill mottaker klikket"
  | "blokk beholdt"
  | "blokk slettet"
  | "diff modus endret";

export interface UmamiEventData {
  [key: string]: string | number | boolean | undefined;
}

let trackerLoading = false;
const pendingEvents: { name: UmamiEventName; data?: UmamiEventData }[] = [];
const MAX_PENDING_EVENTS = 100;

export function startTrackerLoading(): void {
  trackerLoading = true;
}

export function finishTrackerLoading(success: boolean): void {
  trackerLoading = false;
  const events = pendingEvents.splice(0);
  if (success) {
    for (const event of events) {
      trackEvent(event.name, event.data);
    }
  }
}

const isUmamiAvailable = (): boolean =>
  typeof globalThis.window !== "undefined" &&
  globalThis.umami !== undefined &&
  typeof globalThis.umami.track === "function";

export const trackEvent = (eventName: UmamiEventName, eventData?: UmamiEventData): void => {
  if (!isUmamiAvailable()) {
    if (trackerLoading && pendingEvents.length < MAX_PENDING_EVENTS) {
      pendingEvents.push({ name: eventName, data: eventData ? { ...eventData } : undefined });
    }
    if (import.meta.env.DEV) {
      console.info("[Umami]", eventName, eventData);
    }
    return;
  }

  try {
    const cleanedData = eventData
      ? (Object.fromEntries(Object.entries(eventData).filter(([, v]) => v !== undefined)) as Record<
          string,
          string | number | boolean
        >)
      : undefined;

    globalThis.umami?.track(eventName, cleanedData);
  } catch (error) {
    if (import.meta.env.DEV) {
      console.warn("[Umami] tracking failed:", error);
    }
  }
};
