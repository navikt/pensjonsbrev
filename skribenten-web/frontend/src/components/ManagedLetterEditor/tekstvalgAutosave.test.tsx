import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { act, render } from "@testing-library/react";
import { afterEach, beforeEach, describe, expect, test, vi } from "vitest";

import { useOppdaterBrevAutosave } from "~/Brevredigering/hooks/useOppdaterBrevAutosave";
import { RedigeringsflateProvider } from "~/Brevredigering/LetterEditor/RedigeringsflateContext";
import { AUTOSAVE_TIMER } from "~/components/ManagedLetterEditor/autosave_timer";
import {
  ManagedLetterEditorContextProvider,
  useManagedLetterEditorContext,
} from "~/components/ManagedLetterEditor/ManagedLetterEditorContext";
import { type BrevResponse } from "~/types/brev";
import { type EditedLetter } from "~/types/brevbakerTypes";
import { brevInfo, brevResponse } from "~test/support/brevFixtures";

const { oppdaterBrevMock, oppdaterBrevtekstMock } = vi.hoisted(() => ({
  oppdaterBrevMock: vi.fn(),
  oppdaterBrevtekstMock: vi.fn(),
}));

vi.mock("~/api/brev-queries", async (importOriginal) => ({
  ...(await importOriginal<typeof import("~/api/brev-queries")>()),
  oppdaterBrev: oppdaterBrevMock,
  oppdaterBrevtekst: oppdaterBrevtekstMock,
}));

const lagretBrev = brevResponse({
  info: brevInfo({ id: 1, saksId: 123456 }),
  saksbehandlerValg: { ytelse: "alderspensjon" },
});
const nyeValg = { ytelse: "Supplerende stønad" };
const lagringsfeil = new Error("Lagring feilet");

function renderAutosave() {
  const result: {
    current?: ReturnType<typeof useManagedLetterEditorContext> & { lagreValg: () => Promise<BrevResponse> };
  } = {};

  const Testkomponent = () => {
    const context = useManagedLetterEditorContext();
    const { oppdaterBrevMutation } = useOppdaterBrevAutosave({
      saksId: "123456",
      brevId: 1,
      saveStatus: context.editorState.saveStatus,
      setEditorState: context.setEditorState,
      onSaveSuccess: context.onSaveSuccess,
    });
    result.current = {
      ...context,
      lagreValg: () =>
        oppdaterBrevMutation.mutateAsync({
          redigertBrev: context.redigertBrev,
          saksbehandlerValg: nyeValg,
        }),
    };
    return null;
  };

  render(
    <QueryClientProvider client={new QueryClient({ defaultOptions: { mutations: { retry: false } } })}>
      <RedigeringsflateProvider redigeringsflate="saksbehandler-redigering">
        <ManagedLetterEditorContextProvider brev={lagretBrev}>
          <Testkomponent />
        </ManagedLetterEditorContextProvider>
      </RedigeringsflateProvider>
    </QueryClientProvider>,
  );

  return () => {
    if (!result.current) throw new Error("Testkomponenten er ikke rendret");
    return result.current;
  };
}

function endreBrev(harness: ReturnType<typeof renderAutosave>, id: number) {
  act(() =>
    harness().setEditorState((state) => ({
      ...state,
      saveStatus: "DIRTY",
      redigertBrev: { ...state.redigertBrev, deletedBlocks: [...state.redigertBrev.deletedBlocks, id] },
    })),
  );
}

async function vent(ms: number) {
  await act(async () => vi.advanceTimersByTimeAsync(ms));
}

describe("samspill mellom tekstvalg og brevets autolagring", () => {
  beforeEach(() => {
    vi.useFakeTimers();
    vi.resetAllMocks();
    oppdaterBrevMock.mockRejectedValue(lagringsfeil);
    oppdaterBrevtekstMock.mockImplementation(({ redigertBrev }: { redigertBrev: EditedLetter }) =>
      Promise.resolve({ ...structuredClone(lagretBrev), redigertBrev }),
    );
  });

  afterEach(() => vi.useRealTimers());

  test("en mislykket tekstvalglagring starter ikke autolagring av uendret brevtekst", async () => {
    const harness = renderAutosave();
    await act(async () => {
      await expect(harness().lagreValg()).rejects.toBe(lagringsfeil);
    });

    expect(harness().editorState.saveStatus).toBe("SAVED");
    await vent(AUTOSAVE_TIMER);
    expect(oppdaterBrevtekstMock).not.toHaveBeenCalled();
    expect(oppdaterBrevMock).toHaveBeenCalledTimes(1);
  });

  test("bevarer ulagret brevtekst fra før en mislykket tekstvalglagring", async () => {
    const harness = renderAutosave();
    endreBrev(harness, 100);
    await act(async () => {
      await expect(harness().lagreValg()).rejects.toBe(lagringsfeil);
    });

    expect(harness().editorState.saveStatus).toBe("DIRTY");
    await vent(AUTOSAVE_TIMER);
    expect(oppdaterBrevtekstMock).toHaveBeenCalledWith(
      expect.objectContaining({
        redigertBrev: expect.objectContaining({ deletedBlocks: [100] }),
        frigiReservasjon: false,
      }),
    );
    expect(harness().editorState.redigertBrev.deletedBlocks).toEqual([100]);
  });

  test("bevarer brevtekst endret mens tekstvalglagringen pågår", async () => {
    const response = Promise.withResolvers<BrevResponse>();
    oppdaterBrevMock.mockReturnValue(response.promise);
    const harness = renderAutosave();
    let lagring: Promise<unknown>;
    await act(async () => {
      lagring = harness()
        .lagreValg()
        .catch((error: unknown) => error);
    });
    endreBrev(harness, 101);
    await act(async () => {
      response.reject(lagringsfeil);
      expect(await lagring).toBe(lagringsfeil);
    });

    expect(harness().editorState.saveStatus).toBe("DIRTY");
    expect(harness().editorState.redigertBrev.deletedBlocks).toEqual([101]);
  });

  test("beholder identiteten til uendrede valg etter tekstlagring så ulagrede skjemaverdier ikke nullstilles", async () => {
    const harness = renderAutosave();
    const opprinneligeValg = harness().editorState.saksbehandlerValg;
    endreBrev(harness, 102);
    await vent(AUTOSAVE_TIMER);

    expect(oppdaterBrevtekstMock).toHaveBeenCalledTimes(1);
    expect(harness().editorState.saksbehandlerValg).toBe(opprinneligeValg);
  });

  test("bekrefter lagrede skjemaverdier selv om serveren normaliserer dem til opprinnelig verdi", async () => {
    const normalisertSvar = structuredClone(lagretBrev);
    oppdaterBrevMock.mockResolvedValue(normalisertSvar);
    const harness = renderAutosave();
    const opprinneligeValg = harness().editorState.saksbehandlerValg;
    await act(async () => {
      await harness().lagreValg();
    });

    expect(harness().editorState.saksbehandlerValg).toEqual(opprinneligeValg);
    expect(harness().editorState.saksbehandlerValg).not.toBe(opprinneligeValg);
    expect(harness().editorState.saveStatus).toBe("SAVED");
  });

  test("utsetter autolagring til fem sekunder etter siste innholdsendring", async () => {
    const harness = renderAutosave();
    endreBrev(harness, 100);
    await vent(AUTOSAVE_TIMER - 1);
    expect(oppdaterBrevtekstMock).not.toHaveBeenCalled();

    endreBrev(harness, 101);
    await vent(AUTOSAVE_TIMER - 1);
    expect(oppdaterBrevtekstMock).not.toHaveBeenCalled();

    endreBrev(harness, 102);
    await vent(AUTOSAVE_TIMER - 1);
    expect(oppdaterBrevtekstMock).not.toHaveBeenCalled();
    await vent(1);
    expect(oppdaterBrevtekstMock).toHaveBeenCalledTimes(1);
    expect(oppdaterBrevtekstMock).toHaveBeenCalledWith(
      expect.objectContaining({ redigertBrev: expect.objectContaining({ deletedBlocks: [100, 101, 102] }) }),
    );
  });

  test("markørflytting alene utsetter ikke autolagringen", async () => {
    const harness = renderAutosave();
    endreBrev(harness, 100);
    await vent(AUTOSAVE_TIMER - 1);
    act(() => harness().setEditorState((state) => ({ ...state, focus: { ...state.focus, cursorPosition: 2 } })));
    await vent(1);
    expect(oppdaterBrevtekstMock).toHaveBeenCalledTimes(1);
  });
});
