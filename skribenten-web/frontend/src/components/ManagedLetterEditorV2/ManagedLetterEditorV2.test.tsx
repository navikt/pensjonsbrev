import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { act, render, waitFor } from "@testing-library/react";
import { beforeEach, describe, expect, test, vi } from "vitest";

import { type Redigeringsflate, RedigeringsflateProvider } from "~/Brevredigering/LetterEditor/RedigeringsflateContext";
import {
  ManagedLetterEditorContextV2Provider,
  useManagedLetterEditorContextV2,
} from "~/components/ManagedLetterEditorV2/ManagedLetterEditorContextV2";
import ManagedLetterEditorV2 from "~/components/ManagedLetterEditorV2/ManagedLetterEditorV2";
import { type BrevResponseV2 } from "~/types/brevV2";
import { brevInfoV2, brevResponseV2 } from "~test/support/brevV2Fixtures";

const { lagreAttestertBrevtekstMock, oppdaterRedigertBrevMock, oppdaterBrevMock } = vi.hoisted(() => ({
  lagreAttestertBrevtekstMock: vi.fn(),
  oppdaterRedigertBrevMock: vi.fn(),
  oppdaterBrevMock: vi.fn(),
}));

vi.mock("~/api/brevV2-queries", async (importOriginal) => ({
  ...(await importOriginal<typeof import("~/api/brevV2-queries")>()),
  lagreAttestertBrevtekstV2: lagreAttestertBrevtekstMock,
  oppdaterRedigertBrevV2: oppdaterRedigertBrevMock,
  oppdaterBrevV2: oppdaterBrevMock,
}));

vi.mock("~/Brevredigering/EditorV2/view/EditorV2", () => ({
  EditorV2: () => null,
}));

const SAKS_ID = 22_981_081;
const BREV_ID = 1;

const lagretBrev: BrevResponseV2 = brevResponseV2({
  info: brevInfoV2({ id: BREV_ID, saksId: SAKS_ID }),
  saksbehandlerValg: { visTekstvalg: false },
});

type Testhandlinger = {
  markerSomEndret: { current: (() => void) | null };
  lagreEndringer: { current: (() => Promise<void>) | null };
};

const Testkomponent = ({ markerSomEndret, lagreEndringer }: Testhandlinger) => {
  const { setEditorState, savePendingChanges } = useManagedLetterEditorContextV2();
  markerSomEndret.current = () =>
    setEditorState((state) => ({ ...state, redigertBrev: { ...state.redigertBrev }, saveStatus: "DIRTY" }));
  lagreEndringer.current = savePendingChanges;
  return null;
};

function renderEditor(redigeringsflate: Redigeringsflate) {
  const markerSomEndret: Testhandlinger["markerSomEndret"] = { current: null };
  const lagreEndringer: Testhandlinger["lagreEndringer"] = { current: null };

  render(
    <QueryClientProvider client={new QueryClient({ defaultOptions: { queries: { retry: false } } })}>
      <RedigeringsflateProvider redigeringsflate={redigeringsflate}>
        <ManagedLetterEditorContextV2Provider brev={lagretBrev}>
          <Testkomponent lagreEndringer={lagreEndringer} markerSomEndret={markerSomEndret} />
          <ManagedLetterEditorV2 error={false} freeze={false} />
        </ManagedLetterEditorContextV2Provider>
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

beforeEach(() => {
  vi.clearAllMocks();
  lagreAttestertBrevtekstMock.mockResolvedValue(lagretBrev);
  oppdaterRedigertBrevMock.mockResolvedValue(lagretBrev);
  oppdaterBrevMock.mockResolvedValue(lagretBrev);
  vi.useFakeTimers({ shouldAdvanceTime: true });
  return () => vi.useRealTimers();
});

describe("<ManagedLetterEditorV2 /> velger lagringsendepunkt ut fra redigeringsflate", () => {
  test("attestanten lagrer via attestering-endepunktet", async () => {
    const { autolagre } = renderEditor("attestant-redigering");
    await autolagre();

    await waitFor(() => expect(lagreAttestertBrevtekstMock).toHaveBeenCalledTimes(1));
    expect(lagreAttestertBrevtekstMock).toHaveBeenCalledWith(
      expect.objectContaining({ saksId: SAKS_ID, brevId: BREV_ID, frigiReservasjon: false }),
    );
    expect(oppdaterRedigertBrevMock).not.toHaveBeenCalled();
    expect(oppdaterBrevMock).not.toHaveBeenCalled();
  });

  test("saksbehandleren lagrer brevteksten via redigertBrev-endepunktet", async () => {
    const { autolagre } = renderEditor("saksbehandler-redigering");
    await autolagre();

    await waitFor(() => expect(oppdaterRedigertBrevMock).toHaveBeenCalledTimes(1));
    expect(lagreAttestertBrevtekstMock).not.toHaveBeenCalled();
    expect(oppdaterBrevMock).not.toHaveBeenCalled();
  });
});

describe("savePendingChanges", () => {
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

  test("avviser når lagringen feiler", async () => {
    lagreAttestertBrevtekstMock.mockRejectedValue(new Error("lagring feilet"));
    const { markerSomEndret, lagreEndringer } = renderEditor("attestant-redigering");

    act(() => markerSomEndret.current?.());
    await act(async () => {
      await expect(lagreEndringer.current?.()).rejects.toThrow("lagring feilet");
    });
  });

  test("lar samtidige kall vente på samme lagring", async () => {
    const { markerSomEndret, lagreEndringer } = renderEditor("attestant-redigering");

    act(() => markerSomEndret.current?.());
    await act(async () => {
      await Promise.all([lagreEndringer.current?.(), lagreEndringer.current?.()]);
    });

    expect(lagreAttestertBrevtekstMock).toHaveBeenCalledTimes(1);
  });
});
