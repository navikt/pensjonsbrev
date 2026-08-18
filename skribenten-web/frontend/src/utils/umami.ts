import { truncatedSha256Hash } from "~/utils/hashUtils";

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
  | "blokk slettet";

export interface UmamiEventData {
  [key: string]: string | number | boolean | undefined;
}

const isUmamiAvailable = (): boolean =>
  typeof globalThis.window !== "undefined" &&
  globalThis.umami !== undefined &&
  typeof globalThis.umami.track === "function";

export const trackEvent = (eventName: UmamiEventName, eventData?: UmamiEventData): void => {
  if (!isUmamiAvailable()) {
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

type MottakerClickEventName = "endre mottaker klikket" | "tilbakestill mottaker klikket";

export const trackMottakerClick = async (
  eventName: MottakerClickEventName,
  kontekst: string,
  saksId: string,
  eventData?: UmamiEventData,
): Promise<void> => {
  try {
    const hashedSaksId = await truncatedSha256Hash(saksId);
    trackEvent(eventName, {
      kontekst,
      saksId: hashedSaksId,
      ...eventData,
    });
  } catch {
    trackEvent(eventName, {
      kontekst,
      ...eventData,
    });
  }
};
