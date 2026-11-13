import { afterEach, beforeEach, describe, expect, it, vi } from "vitest";

import { finishTrackerLoading, startTrackerLoading, trackEvent } from "~/utils/umami";

beforeEach(() => {
  globalThis.umami = undefined;
  finishTrackerLoading(false);
  vi.spyOn(console, "info").mockImplementation(() => {});
});

afterEach(() => {
  globalThis.umami = undefined;
  finishTrackerLoading(false);
  vi.restoreAllMocks();
});

describe("hendelser under lasting av sporingsskriptet", () => {
  it("sender køen i rekkefølge én gang, med opprinnelige data uten undefined", () => {
    startTrackerLoading();
    const data = { feiltype: "ugyldig brev-id", brevId: undefined };
    trackEvent("pesys feil", data);
    data.feiltype = "endret";
    trackEvent("pesys omdirigering");
    const track = vi.fn();
    globalThis.umami = { track };

    finishTrackerLoading(true);
    finishTrackerLoading(true);

    expect(track.mock.calls).toEqual([
      ["pesys feil", { feiltype: "ugyldig brev-id" }],
      ["pesys omdirigering", undefined],
    ]);
  });

  it("forkaster hendelser ved lastefeil og tar dem ikke med ved nytt forsøk", () => {
    startTrackerLoading();
    trackEvent("pesys feil");
    finishTrackerLoading(false);
    trackEvent("pesys feil");
    startTrackerLoading();
    const track = vi.fn();
    globalThis.umami = { track };
    finishTrackerLoading(true);
    expect(track).not.toHaveBeenCalled();
  });

  it("lagrer ikke hendelser når sporing ikke lastes", () => {
    trackEvent("pesys feil");
    const track = vi.fn();
    globalThis.umami = { track };
    finishTrackerLoading(true);
    trackEvent("brev sendt");
    expect(track.mock.calls).toEqual([["brev sendt", undefined]]);
  });

  it("begrenser køen hvis skriptet aldri blir ferdig lastet", () => {
    startTrackerLoading();
    for (let i = 0; i < 150; i++) {
      trackEvent("pesys feil");
    }
    const track = vi.fn();
    globalThis.umami = { track };
    finishTrackerLoading(true);
    expect(track).toHaveBeenCalledTimes(100);
  });
});
