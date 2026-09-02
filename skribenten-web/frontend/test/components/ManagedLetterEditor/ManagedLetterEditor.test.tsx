import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { act, render, waitFor } from "@testing-library/react";
import { beforeEach, describe, expect, test, vi } from "vitest";

import ManagedLetterEditor from "~/components/ManagedLetterEditor/ManagedLetterEditor";
import {
  ManagedLetterEditorContextProvider,
  useManagedLetterEditorContext,
} from "~/components/ManagedLetterEditor/ManagedLetterEditorContext";
import { type BrevResponse } from "~/types/brev";
import { type Redigeringsflate } from "~/utils/editorTracking";

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

  const Testkomponent = () => {
    const { setEditorState } = useManagedLetterEditorContext();
    markerSomEndret.current = () => setEditorState((state) => ({ ...state, saveStatus: "DIRTY" }));
    return null;
  };

  render(
    <QueryClientProvider client={new QueryClient({ defaultOptions: { queries: { retry: false } } })}>
      <ManagedLetterEditorContextProvider brev={lagretBrev} redigeringsflate={redigeringsflate}>
        <Testkomponent />
        <ManagedLetterEditor brev={lagretBrev} error={false} freeze={false} redigeringsflate={redigeringsflate} />
      </ManagedLetterEditorContextProvider>
    </QueryClientProvider>,
  );

  return async () => {
    act(() => markerSomEndret.current?.());
    await act(async () => {
      await vi.advanceTimersByTimeAsync(6000);
    });
  };
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
    const autolagre = renderEditor("attestant-redigering");
    await autolagre();

    await waitFor(() => expect(lagreAttestertBrevtekstMock).toHaveBeenCalledTimes(1));
    expect(lagreAttestertBrevtekstMock).toHaveBeenCalledWith(
      expect.objectContaining({ saksId: String(SAKS_ID), brevId: BREV_ID, frigiReservasjon: false }),
    );
    expect(oppdaterBrevtekstMock).not.toHaveBeenCalled();
    expect(oppdaterBrevMock).not.toHaveBeenCalled();
  });

  test("saksbehandleren lagrer fortsatt via det mergende endepunktet", async () => {
    const autolagre = renderEditor("saksbehandler-redigering");
    await autolagre();

    await waitFor(() => expect(oppdaterBrevtekstMock).toHaveBeenCalledTimes(1));
    expect(lagreAttestertBrevtekstMock).not.toHaveBeenCalled();
  });
});
