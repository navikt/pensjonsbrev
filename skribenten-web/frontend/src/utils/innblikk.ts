import { addMonths, startOfMonth } from "date-fns";
import Cookies from "js-cookie";

/** API-et sporingsskriptet til Innblikk eksponerer som `globalThis.sporing`. */
interface SporingApi {
  track: (eventName: string, eventData?: Record<string, string | number | boolean>) => void | Promise<void>;
  identify: (id: string, data?: Record<string, string | number | boolean>) => void | Promise<void>;
}

export type InnblikkEventName =
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

export interface InnblikkEventData {
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

type MatchRoute = (pathname: string) => string | undefined;

declare global {
  var sporing: SporingApi | undefined;
  var sanitizeSkribentenInnblikkPayload: ((type: string, payload: InnblikkPayload) => InnblikkPayload) | undefined;
}

const USER_ID_COOKIE = "pensjon-psak-innblikk-uuid";
const SCRIPT_ID = "skribenten-innblikk-sporing";
const SHARED_DOMAINS = ["intern.nav.no", "dev.nav.no"];
const UNKNOWN_ROUTE = "<ukjent>";

let trackerLoading = false;
let identifiedSporing: SporingApi | undefined;
let identifiedId: string | undefined;
const pendingEvents: { name: InnblikkEventName; data?: InnblikkEventData }[] = [];
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

const isSporingAvailable = (): boolean =>
  typeof globalThis.window !== "undefined" && typeof globalThis.sporing?.track === "function";

export const trackEvent = (eventName: InnblikkEventName, eventData?: InnblikkEventData): void => {
  if (!isSporingAvailable()) {
    if (trackerLoading && pendingEvents.length < MAX_PENDING_EVENTS) {
      pendingEvents.push({ name: eventName, data: eventData ? { ...eventData } : undefined });
    }
    if (import.meta.env.DEV) {
      console.info("[Innblikk]", eventName, eventData);
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

    void globalThis.sporing?.track(eventName, cleanedData);
  } catch (error) {
    if (import.meta.env.DEV) {
      console.warn("[Innblikk] tracking failed:", error);
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

function sanitizeUrl(value: string, matchRoute: MatchRoute): string {
  if (!value) {
    return "";
  }
  const origin = globalThis.location.origin;
  if (!URL.canParse(value, origin)) {
    return UNKNOWN_ROUTE;
  }
  const url = new URL(value, origin);
  if (url.origin !== origin) {
    return "<ekstern>";
  }
  return matchRoute(url.pathname)?.replace(/\/\$(\w+)/g, "/:$1") ?? UNKNOWN_ROUTE;
}

export function beforeSend(payload: InnblikkPayload, matchRoute: MatchRoute): InnblikkPayload {
  const url = sanitizeUrl(payload.url, matchRoute) || UNKNOWN_ROUTE;
  return {
    ...payload,
    id: getOrCreateUserId(),
    url,
    title: url,
    referrer: sanitizeUrl(payload.referrer, matchRoute),
  };
}

/**
 * Kaller `sporing.identify` når cookie-UUID-en ikke allerede er identitet for skriptet.
 * Skriptet holder identiteten kun i minnet, så den settes på nytt ved hver sidelasting og når cookien endres.
 */
function identify(id: string): void {
  const sporing = globalThis.sporing;
  if (typeof sporing?.identify !== "function" || (sporing === identifiedSporing && id === identifiedId)) {
    return;
  }
  identifiedSporing = sporing;
  identifiedId = id;
  try {
    void sporing.identify(id);
  } catch (error) {
    identifiedSporing = undefined;
    identifiedId = undefined;
    if (import.meta.env.DEV) {
      console.warn("[Innblikk] identify failed:", error);
    }
  }
}

export function initInnblikk(matchRoute: MatchRoute): void {
  const websiteId = document.querySelector<HTMLMetaElement>('meta[name="umami-website-id"]')?.content;
  const hostUrl = document.querySelector<HTMLMetaElement>('meta[name="umami-host-url"]')?.content;
  if (!websiteId || !hostUrl || websiteId.includes("{{") || hostUrl.includes("{{")) {
    return;
  }

  if (document.getElementById(SCRIPT_ID)) {
    return;
  }

  globalThis.sanitizeSkribentenInnblikkPayload = (type, payload) => {
    const result = beforeSend(payload, matchRoute);
    if (type !== "identify" && result.id) {
      identify(result.id);
    }
    return result;
  };

  const script = document.createElement("script");
  script.id = SCRIPT_ID;
  script.src = "https://cdn.nav.no/team-researchops/sporing/sporing.js";
  script.defer = true;
  script.dataset.websiteId = websiteId;
  script.dataset.hostUrl = hostUrl;
  script.dataset.beforeSend = "sanitizeSkribentenInnblikkPayload";
  script.addEventListener(
    "load",
    () => {
      const id = getOrCreateUserId();
      if (id) {
        identify(id);
      }
      finishTrackerLoading(true);
    },
    { once: true },
  );
  script.addEventListener(
    "error",
    () => {
      finishTrackerLoading(false);
      identifiedSporing = undefined;
      identifiedId = undefined;
      script.remove();
    },
    { once: true },
  );
  trackerLoading = true;
  document.head.appendChild(script);
}
