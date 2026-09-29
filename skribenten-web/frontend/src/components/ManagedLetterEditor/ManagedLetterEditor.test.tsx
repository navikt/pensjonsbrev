import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { act, render, waitFor } from "@testing-library/react";
import { beforeEach, describe, expect, test, vi } from "vitest";

import { type Redigeringsflate, RedigeringsflateProvider } from "~/Brevredigering/LetterEditor/RedigeringsflateContext";
import ManagedLetterEditor from "~/components/ManagedLetterEditor/ManagedLetterEditor";
import {
  ManagedLetterEditorContextProvider,
  useManagedLetterEditorContext,
} from "~/components/ManagedLetterEditor/ManagedLetterEditorContext";
import { type BrevResponse } from "~/types/brev";
import { brevInfo, brevResponse } from "~test/support/brevFixtures";

const { lagreAttestertBrevtekstMock, oppdaterBrevtekstMock, oppdaterBrevMock } = vi.hoisted(() => ({
  lagreAttestertBrevtekstMock: vi.fn(),
  oppdaterBrevtekstMock: vi.fn(),
  oppdaterBrevMock: vi.fn(),
}));

vi.mock("~/api/brev-queries", async (importOriginal) => ({
  ...(await importOriginal<typeof import("~/api/brev-queries")>()),
  lagreAttestertBrevtekst: lagreAttestertBrevtekstMock,
  oppdaterBrevtekst: oppdaterBrevtekstMock,
  oppdaterBrev: oppdaterBrevMock,
}));

vi.mock("~/Brevredigering/LetterEditor/LetterEditor", () => ({
  LetterEditor: () => null,
}));

const SAKS_ID = 22_981_081;
const BREV_ID = 1;

const lagretBrev: BrevResponse = brevResponse({
  info: brevInfo({ id: BREV_ID, saksId: SAKS_ID }),
  saksbehandlerValg: { visTekstvalg: false },
});

function renderEditor(redigeringsflate: Redigeringsflate) {
  const markerSomEndret = { current: null as (() => void) | null };
  const lagreEndringer = { current: null as (() => Promise<void>) | null };

  const Testkomponent = () => {
    const { setEditorState, savePendingChanges } = useManagedLetterEditorContext();
    markerSomEndret.current = () =>
      setEditorState((state) => ({ ...state, redigertBrev: { ...state.redigertBrev }, saveStatus: "DIRTY" }));
    lagreEndringer.current = savePendingChanges;
    return null;
  };

  render(
    <QueryClientProvider client={new QueryClient({ defaultOptions: { queries: { retry: false } } })}>
      <RedigeringsflateProvider redigeringsflate={redigeringsflate}>
        <ManagedLetterEditorContextProvider brev={lagretBrev}>
          <Testkomponent />
          <ManagedLetterEditor error={false} freeze={false} />
        </ManagedLetterEditorContextProvider>
      </RedigeringsflateProvider>
    </QueryClientProvider>,
  );

  const autolagre = async () => {
    act(() => markerSomEndret.current?.());
    await act(async () => {
      await vi.advanceTimersByTimeAsync(6000);
    });
  };

  return { autolagre, markerSomEndret, lagreEndringer };
}

describe("<ManagedLetterEditor /> velger lagringsendepunkt ut fra redigeringsflate", () => {
  beforeEach(() => {
    vi.clearAllMocks();
    lagreAttestertBrevtekstMock.mockResolvedValue(lagretBrev);
    oppdaterBrevtekstMock.mockResolvedValue(lagretBrev);
    oppdaterBrevMock.mockResolvedValue(lagretBrev);
    vi.useFakeTimers({ shouldAdvanceTime: true });
    return () => vi.useRealTimers();
  });

  test("attestanten lagrer via attestering-endepunktet, som ikke merger mot malen", async () => {
    const { autolagre } = renderEditor("attestant-redigering");
    await autolagre();

    await waitFor(() => expect(lagreAttestertBrevtekstMock).toHaveBeenCalledTimes(1));
    expect(lagreAttestertBrevtekstMock).toHaveBeenCalledWith(
      expect.objectContaining({ saksId: String(SAKS_ID), brevId: BREV_ID, frigiReservasjon: false }),
    );
    expect(oppdaterBrevtekstMock).not.toHaveBeenCalled();
    expect(oppdaterBrevMock).not.toHaveBeenCalled();
  });

  test("saksbehandleren lagrer fortsatt via det mergende endepunktet", async () => {
    const { autolagre } = renderEditor("saksbehandler-redigering");
    await autolagre();

    await waitFor(() => expect(oppdaterBrevtekstMock).toHaveBeenCalledTimes(1));
    expect(lagreAttestertBrevtekstMock).not.toHaveBeenCalled();
  });
});

describe("savePendingChanges", () => {
  beforeEach(() => {
    vi.clearAllMocks();
    lagreAttestertBrevtekstMock.mockResolvedValue(lagretBrev);
    oppdaterBrevtekstMock.mockResolvedValue(lagretBrev);
    oppdaterBrevMock.mockResolvedValue(lagretBrev);
    vi.useFakeTimers({ shouldAdvanceTime: true });
    return () => vi.useRealTimers();
  });

  test("lagrer umiddelbart, uten å vente på autolagringsintervallet", async () => {
    const { markerSomEndret, lagreEndringer } = renderEditor("attestant-redigering");

    act(() => markerSomEndret.current?.());
    await act(async () => {
      await lagreEndringer.current?.();
    });

    expect(lagreAttestertBrevtekstMock).toHaveBeenCalledTimes(1);
  });

  test("gjør ingenting når brevet allerede er lagret", async () => {
    const { lagreEndringer } = renderEditor("attestant-redigering");

    await act(async () => {
      await lagreEndringer.current?.();
    });

    expect(lagreAttestertBrevtekstMock).not.toHaveBeenCalled();
  });

  test("avviser når lagringen feiler, slik at kalleren kan rulle tilbake", async () => {
    lagreAttestertBrevtekstMock.mockRejectedValue(new Error("lagring feilet"));
    const { markerSomEndret, lagreEndringer } = renderEditor("attestant-redigering");

    act(() => markerSomEndret.current?.());
    await act(async () => {
      await expect(lagreEndringer.current?.()).rejects.toThrow("lagring feilet");
    });
  });

  test("lar samtidige kall vente på samme lagring i stedet for å sende en ny", async () => {
    const { markerSomEndret, lagreEndringer } = renderEditor("attestant-redigering");

    act(() => markerSomEndret.current?.());
    await act(async () => {
      await Promise.all([lagreEndringer.current?.(), lagreEndringer.current?.()]);
    });

    expect(lagreAttestertBrevtekstMock).toHaveBeenCalledTimes(1);
  });

  test("sender ikke en konkurrerende lagring mens autolagringen er underveis", async () => {
    let fullførAutolagring = () => {};
    lagreAttestertBrevtekstMock.mockReturnValueOnce(
      new Promise<BrevResponse>((resolve) => {
        fullførAutolagring = () => resolve(lagretBrev);
      }),
    );

    const { autolagre, markerSomEndret, lagreEndringer } = renderEditor("attestant-redigering");
    await autolagre();
    expect(lagreAttestertBrevtekstMock).toHaveBeenCalledTimes(1);

    act(() => markerSomEndret.current?.());
    const ferdig = vi.fn();
    let lagring: Promise<void> | undefined;
    await act(async () => {
      lagring = lagreEndringer.current?.().then(ferdig);
    });

    expect(lagreAttestertBrevtekstMock).toHaveBeenCalledTimes(1);
    expect(ferdig).not.toHaveBeenCalled();

    await act(async () => {
      fullførAutolagring();
      await lagring;
    });
    expect(lagreAttestertBrevtekstMock).toHaveBeenCalledTimes(2);
    expect(ferdig).toHaveBeenCalledOnce();
  });
});
