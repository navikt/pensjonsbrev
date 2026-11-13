import { afterEach, beforeEach, describe, expect, it, vi } from "vitest";

import { beforeSend, getDomain, getOrCreateUserId, initInnblikk } from "~/utils/innblikk";
import { finishTrackerLoading, trackEvent } from "~/utils/umami";

declare const jsdom: { reconfigure(options: { url: string }): void };

const { getCookie, setCookie } = vi.hoisted(() => ({ getCookie: vi.fn(), setCookie: vi.fn() }));
vi.mock("js-cookie", () => ({ default: { get: getCookie, set: setCookie } }));

const cookieName = "pensjon-psak-innblikk-uuid";
const id = "ec458791-8536-41b6-bf86-a5fe83226946";
const payload = {
  website: "skribenten-website",
  screen: "1200x800",
  language: "nb",
  title: "Skribenten",
  hostname: "pensjon-skribenten.intern.nav.no",
  url: "/personvern",
  referrer: "",
};

beforeEach(() => {
  getCookie.mockReset();
  setCookie.mockReset();
});

afterEach(() => {
  vi.useRealTimers();
  vi.restoreAllMocks();
  vi.unstubAllGlobals();
  jsdom.reconfigure({ url: "http://localhost:3000" });
  for (const node of document.querySelectorAll('meta[name^="umami-"], #skribenten-innblikk-sporing')) {
    node.remove();
  }
  globalThis.addSkribentenInnblikkIdentity = undefined;
  globalThis.umami = undefined;
  finishTrackerLoading(false);
});

describe("felles cookie med Penny", () => {
  it.each([
    ["pensjon-skribenten.intern.nav.no", "intern.nav.no"],
    ["pensjon-skribenten-web-q2.intern.dev.nav.no", "dev.nav.no"],
    ["pensjon-skribenten-web-q2.ansatt.dev.nav.no", "dev.nav.no"],
    ["localhost", undefined],
    ["notintern.nav.no", undefined],
    ["intern.nav.no.example.com", undefined],
  ])("velger riktig cookiedomene for %s", (hostname, domain) => {
    expect(getDomain(hostname)).toBe(domain);
  });

  it("bruker eksisterende UUID uten å fornye utløpet", () => {
    getCookie.mockReturnValue(id);
    expect(getOrCreateUserId()).toBe(id);
    expect(getCookie).toHaveBeenCalledWith(cookieName);
    expect(setCookie).not.toHaveBeenCalled();
  });

  it.each([
    [new Date(2026, 10, 30, 12), new Date(2026, 11, 1)],
    [new Date(2026, 11, 31, 12), new Date(2027, 0, 1)],
  ])("lager en UUID som utløper ved neste månedsskifte (%s)", (now, expires) => {
    vi.useFakeTimers();
    vi.setSystemTime(now);
    jsdom.reconfigure({ url: "https://pensjon-skribenten.intern.nav.no" });
    vi.spyOn(crypto, "randomUUID").mockReturnValue(id);
    getCookie.mockReturnValueOnce(undefined).mockReturnValueOnce(id);

    expect(getOrCreateUserId()).toBe(id);
    expect(setCookie).toHaveBeenCalledWith(cookieName, id, {
      domain: "intern.nav.no",
      expires,
      sameSite: "lax",
      secure: true,
    });
  });

  it("bruker host-only uten Secure på lokal HTTP", () => {
    jsdom.reconfigure({ url: "http://localhost:5173" });
    vi.spyOn(crypto, "randomUUID").mockReturnValue(id);
    getCookie.mockReturnValueOnce(undefined).mockReturnValueOnce(id);

    expect(getOrCreateUserId()).toBe(id);
    expect(setCookie).toHaveBeenCalledWith(cookieName, id, expect.objectContaining({ sameSite: "lax", secure: false }));
    expect(setCookie.mock.calls[0][2]).not.toHaveProperty("domain");
  });

  it("returnerer ingen UUID når nettleseren blokkerer lagring", () => {
    expect(getOrCreateUserId()).toBeUndefined();
    expect(setCookie).toHaveBeenCalledOnce();
  });

  it("lar ikke feil ved cookie-tilgang påvirke sporing eller applikasjonen", () => {
    getCookie.mockImplementation(() => {
      throw new DOMException("Blocked", "SecurityError");
    });
    expect(beforeSend("event", payload)).toEqual({ ...payload, id: undefined });
  });

  it("gir sidevisninger og egendefinerte hendelser samme identitet uten å endre innholdet", () => {
    getCookie.mockReturnValue(id);
    const event = { ...payload, name: "brev opprettet", data: { brevkode: "E001" } };
    expect(beforeSend("event", payload)).toEqual({ ...payload, id });
    expect(beforeSend("event", event)).toEqual({ ...event, id });
    expect(payload).not.toHaveProperty("id");
  });

  it("leser cookien på nytt for hver hendelse, også etter endring eller sletting", () => {
    getCookie.mockReturnValueOnce(id).mockReturnValueOnce("changed").mockReturnValue(undefined);
    expect(beforeSend("event", payload).id).toBe(id);
    expect(beforeSend("event", payload).id).toBe("changed");
    expect(beforeSend("event", payload).id).toBeUndefined();
  });
});

describe("oppstart av Innblikk", () => {
  function configure() {
    document.head.insertAdjacentHTML(
      "beforeend",
      '<meta name="umami-website-id" content="skribenten-website"><meta name="umami-host-url" content="https://reops-event-proxy.nav.no">',
    );
  }

  it("installerer callback før sporingsskriptet kan kjøre, og laster bare én gang", () => {
    configure();
    getCookie.mockReturnValue(id);
    const append = vi.spyOn(document.head, "appendChild");
    append.mockImplementationOnce((node) => {
      expect(globalThis.addSkribentenInnblikkIdentity?.("event", payload).id).toBe(id);
      return node;
    });

    initInnblikk();
    const script = append.mock.calls[0][0] as HTMLScriptElement;
    expect(script.src).toBe("https://cdn.nav.no/team-researchops/sporing/sporing.js");
    expect(script.dataset).toMatchObject({
      websiteId: "skribenten-website",
      hostUrl: "https://reops-event-proxy.nav.no",
      beforeSend: "addSkribentenInnblikkIdentity",
    });
    document.head.appendChild(script);
    initInnblikk();
    expect(document.querySelectorAll("#skribenten-innblikk-sporing")).toHaveLength(1);
    expect(append).toHaveBeenCalledTimes(2);
  });

  it.each(["", "{{UMAMI_WEBSITE_ID}}"])("laster ikke skriptet uten ferdig konfigurasjon (%s)", (websiteId) => {
    configure();
    const meta = document.querySelector<HTMLMetaElement>('meta[name="umami-website-id"]');
    if (meta) meta.content = websiteId;
    initInnblikk();
    expect(document.getElementById("skribenten-innblikk-sporing")).toBeNull();
  });

  it("laster ikke skriptet når konfigurasjonen mangler", () => {
    initInnblikk();
    expect(document.getElementById("skribenten-innblikk-sporing")).toBeNull();
  });

  it.each(["load", "error"])("avslutter køen når skriptet melder %s", (event) => {
    configure();
    initInnblikk();
    trackEvent("pesys feil");
    const track = vi.fn();
    globalThis.umami = { track };
    document.getElementById("skribenten-innblikk-sporing")?.dispatchEvent(new Event(event));
    if (event === "load") {
      expect(track).toHaveBeenCalledWith("pesys feil", undefined);
    } else {
      expect(track).not.toHaveBeenCalled();
      expect(document.getElementById("skribenten-innblikk-sporing")).toBeNull();
    }
  });
});
