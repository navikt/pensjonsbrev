import { addMonths, startOfMonth } from "date-fns";
import Cookies from "js-cookie";

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
  var addSkribentenInnblikkIdentity: ((type: string, payload: InnblikkPayload) => InnblikkPayload) | undefined;
}

const USER_ID_COOKIE = "pensjon-psak-innblikk-uuid";
const SCRIPT_ID = "skribenten-innblikk-sporing";
const SHARED_DOMAINS = ["intern.nav.no", "dev.nav.no"];

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
  script.addEventListener("error", () => script.remove());
  document.head.appendChild(script);
}
