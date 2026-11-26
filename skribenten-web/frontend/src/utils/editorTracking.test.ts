import { beforeEach, describe, expect, it, vi } from "vitest";

import { trackMissingInRenderedAction } from "~/utils/editorTracking";

const trackMock = vi.fn();

beforeEach(() => {
  trackMock.mockClear();
  globalThis.sporing = { identify: vi.fn(), track: trackMock };
});

const sisteKall = () => trackMock.mock.calls.at(-1) as [string, Record<string, unknown>];

describe("trackMissingInRenderedAction", () => {
  it("sender med redigeringsflate og øvrige felter", () => {
    trackMissingInRenderedAction("blokk beholdt", "attestant-redigering", {
      brevkode: "UT_UNG_UFOER_AUTO",
      antallGjenstaaende: 2,
    });

    const [eventName, data] = sisteKall();
    expect(eventName).toBe("blokk beholdt");
    expect(data.redigeringsflate).toBe("attestant-redigering");
    expect(data.brevkode).toBe("UT_UNG_UFOER_AUTO");
    expect(data.antallGjenstaaende).toBe(2);
  });

  it("lar ikke kalleren overstyre redigeringsflate", () => {
    trackMissingInRenderedAction("blokk slettet", "saksbehandler-redigering", {
      redigeringsflate: "attestant-redigering",
    });

    expect(sisteKall()[1].redigeringsflate).toBe("saksbehandler-redigering");
  });
});
