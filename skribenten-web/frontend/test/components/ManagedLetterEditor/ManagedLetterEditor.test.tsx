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

import { brevInfo, brevResponse } from "../../modules/LetterEditor/utils";

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
  const lagreNa = { current: null as (() => Promise<void>) | null };

  const Testkomponent = () => {
    const { setEditorState, saveNow } = useManagedLetterEditorContext();
    markerSomEndret.current = () => setEditorState((state) => ({ ...state, saveStatus: "DIRTY" }));
    lagreNa.current = saveNow;
    return null;
  };

  render(
    <QueryClientProvider client={new QueryClient({ defaultOptions: { queries: { retry: false } } })}>
      <RedigeringsflateProvider redigeringsflate={redigeringsflate}>
        <ManagedLetterEditorContextProvider brev={lagretBrev}>
          <Testkomponent />
          <ManagedLetterEditor brev={lagretBrev} error={false} freeze={false} />
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

  return { autolagre, markerSomEndret, lagreNa };
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

describe("saveNow", () => {
  beforeEach(() => {
    vi.clearAllMocks();
    lagreAttestertBrevtekstMock.mockResolvedValue(lagretBrev);
    oppdaterBrevtekstMock.mockResolvedValue(lagretBrev);
    oppdaterBrevMock.mockResolvedValue(lagretBrev);
    vi.useFakeTimers({ shouldAdvanceTime: true });
    return () => vi.useRealTimers();
  });

  test("lagrer umiddelbart, uten å vente på autolagringsintervallet", async () => {
    const { markerSomEndret, lagreNa } = renderEditor("attestant-redigering");

    act(() => markerSomEndret.current?.());
    await act(async () => {
      await lagreNa.current?.();
    });

    expect(lagreAttestertBrevtekstMock).toHaveBeenCalledTimes(1);
  });

  test("gjør ingenting når brevet allerede er lagret", async () => {
    const { lagreNa } = renderEditor("attestant-redigering");

    await act(async () => {
      await lagreNa.current?.();
    });

    expect(lagreAttestertBrevtekstMock).not.toHaveBeenCalled();
  });

  test("avviser når lagringen feiler, slik at kalleren kan rulle tilbake", async () => {
    lagreAttestertBrevtekstMock.mockRejectedValue(new Error("lagring feilet"));
    const { markerSomEndret, lagreNa } = renderEditor("attestant-redigering");

    act(() => markerSomEndret.current?.());
    await act(async () => {
      await expect(lagreNa.current?.()).rejects.toThrow("lagring feilet");
    });
  });

  test("lar samtidige kall vente på samme lagring i stedet for å sende en ny", async () => {
    const { markerSomEndret, lagreNa } = renderEditor("attestant-redigering");

    act(() => markerSomEndret.current?.());
    await act(async () => {
      await Promise.all([lagreNa.current?.(), lagreNa.current?.()]);
    });

    expect(lagreAttestertBrevtekstMock).toHaveBeenCalledTimes(1);
  });

  test("sender ikke en konkurrerende lagring mens autolagringen er underveis", async () => {
    // Endepunktene for brev har ingen versjon, så to samtidige PUT-er kan lande i feil rekkefølge
    // og lagre det eldste brevet. Autolagringen som allerede er underveis får fullføre alene.
    let fullførAutolagring = () => {};
    lagreAttestertBrevtekstMock.mockReturnValueOnce(
      new Promise<BrevResponse>((resolve) => {
        fullførAutolagring = () => resolve(lagretBrev);
      }),
    );

    const { autolagre, markerSomEndret, lagreNa } = renderEditor("attestant-redigering");
    await autolagre();
    expect(lagreAttestertBrevtekstMock).toHaveBeenCalledTimes(1);

    act(() => markerSomEndret.current?.());
    await act(async () => {
      await lagreNa.current?.();
    });

    expect(lagreAttestertBrevtekstMock).toHaveBeenCalledTimes(1);

    await act(async () => {
      fullførAutolagring();
    });
  });
});
