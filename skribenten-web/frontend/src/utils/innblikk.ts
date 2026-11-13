import { addMonths, startOfMonth } from "date-fns";
import Cookies from "js-cookie";

interface UmamiTracker {
  track: (eventName: string, eventData?: Record<string, string | number | boolean>) => void;
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

type InnblikkPayload = {
  website: string;
  screen: string;
  language: string;
  title: string;
  hostname: string;
  url: string;
  referrer: string;
  id?: string;
  name?: string;
  data?: Record<string, unknown>;
};

declare global {
  var umami: UmamiTracker | undefined;
  var addSkribentenInnblikkIdentity: ((type: string, payload: InnblikkPayload) => InnblikkPayload) | undefined;
}

const USER_ID_COOKIE = "pensjon-psak-innblikk-uuid";
const SCRIPT_ID = "skribenten-innblikk-sporing";
const SHARED_DOMAINS = ["intern.nav.no", "dev.nav.no"];

let trackerLoading = false;
const pendingEvents: { name: UmamiEventName; data?: UmamiEventData }[] = [];
const MAX_PENDING_EVENTS = 100;

function finishTrackerLoading(success: boolean): void {
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

export function getDomain(hostname = globalThis.location.hostname): string | undefined {
  return SHARED_DOMAINS.find((domain) => hostname.endsWith(`.${domain}`));
}

export function getOrCreateUserId(): string | undefined {
  try {
    const stored = Cookies.get(USER_ID_COOKIE);
    if (stored) {
      return stored;
    }

    const id = crypto.randomUUID();
    const domain = getDomain();
    Cookies.set(USER_ID_COOKIE, id, {
      expires: startOfMonth(addMonths(new Date(), 1)),
      sameSite: "lax",
      secure: globalThis.location.protocol === "https:",
      ...(domain ? { domain } : {}),
    });

    return Cookies.get(USER_ID_COOKIE) === id ? id : undefined;
  } catch {
    return undefined;
  }
}

export function beforeSend(_type: string, payload: InnblikkPayload): InnblikkPayload {
  return { ...payload, id: getOrCreateUserId() };
}

export function initInnblikk(): void {
  const websiteId = document.querySelector<HTMLMetaElement>('meta[name="umami-website-id"]')?.content;
  const hostUrl = document.querySelector<HTMLMetaElement>('meta[name="umami-host-url"]')?.content;
  if (!websiteId || !hostUrl || websiteId.includes("{{") || hostUrl.includes("{{")) {
    return;
  }

  globalThis.addSkribentenInnblikkIdentity = beforeSend;
  if (document.getElementById(SCRIPT_ID)) {
    return;
  }

  const script = document.createElement("script");
  script.id = SCRIPT_ID;
  script.src = "https://cdn.nav.no/team-researchops/sporing/sporing.js";
  script.defer = true;
  script.dataset.websiteId = websiteId;
  script.dataset.hostUrl = hostUrl;
  script.dataset.beforeSend = "addSkribentenInnblikkIdentity";
  script.addEventListener("load", () => finishTrackerLoading(true), { once: true });
  script.addEventListener(
    "error",
    () => {
      finishTrackerLoading(false);
      script.remove();
    },
    { once: true },
  );
  trackerLoading = true;
  document.head.appendChild(script);
}
