import { beforeEach, describe, expect, it, vi } from "vitest";

import { trackMottakerClick } from "~/utils/mottakerTracking";

const trackMock = vi.fn();

beforeEach(() => {
  trackMock.mockClear();
  globalThis.umami = { track: trackMock };
});

const sisteKall = () => trackMock.mock.calls.at(-1) as [string, Record<string, unknown>];

describe("trackMottakerClick", () => {
  it("hasher saksId før den sendes til Umami", async () => {
    await trackMottakerClick("endre mottaker klikket", "brevbehandler", "12345678");

    const [eventName, data] = sisteKall();
    expect(eventName).toBe("endre mottaker klikket");
    expect(data.kontekst).toBe("brevbehandler");
    expect(data.saksId).not.toBe("12345678");
    expect(data.saksId).toMatch(/^[\da-f]{16}$/);
  });

  it("gir samme hash for samme saksId", async () => {
    await trackMottakerClick("endre mottaker klikket", "kladd", "12345678");
    const første = sisteKall()[1].saksId;

    await trackMottakerClick("tilbakestill mottaker klikket", "kladd", "12345678");
    expect(sisteKall()[1].saksId).toBe(første);
  });

  it("lar ikke kalleren overstyre saksId med en verdi i klartekst", async () => {
    await trackMottakerClick("endre mottaker klikket", "kladd", "12345678", { saksId: "12345678" });

    expect(sisteKall()[1].saksId).not.toBe("12345678");
  });

  it("lar ikke kalleren overstyre kontekst", async () => {
    await trackMottakerClick("endre mottaker klikket", "kladd", "12345678", { kontekst: "noe annet" });

    expect(sisteKall()[1].kontekst).toBe("kladd");
  });

  it("sender med øvrige felter fra eventData", async () => {
    await trackMottakerClick("endre mottaker klikket", "brevbehandler", "12345678", { enhetsId: "4405" });

    expect(sisteKall()[1].enhetsId).toBe("4405");
  });

  it("sender aldri saksId i klartekst når hashingen feiler", async () => {
    const digest = vi.spyOn(crypto.subtle, "digest").mockRejectedValue(new Error("crypto utilgjengelig"));

    await trackMottakerClick("endre mottaker klikket", "kladd", "12345678", { saksId: "12345678" });

    const [, data] = sisteKall();
    expect(data.saksId).toBeUndefined();
    expect(data.kontekst).toBe("kladd");
    digest.mockRestore();
  });
});
