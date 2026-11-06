import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { act, render } from "@testing-library/react";
import { afterEach, beforeEach, describe, expect, test, vi } from "vitest";

import { useOppdaterBrevMutation } from "~/Brevredigering/hooks/useOppdaterBrevMutation";
import { addToHistory } from "~/Brevredigering/LetterEditor/history";
import { RedigeringsflateProvider } from "~/Brevredigering/LetterEditor/RedigeringsflateContext";
import { AUTOSAVE_TIMER, SAKSBEHANDLERVALG_TIMER } from "~/components/ManagedLetterEditor/autosave_timer";
import {
  ManagedLetterEditorContextProvider,
  useManagedLetterEditorContext,
} from "~/components/ManagedLetterEditor/ManagedLetterEditorContext";
import { type BrevResponse } from "~/types/brev";
import { type EditedLetter } from "~/types/brevbakerTypes";
import { brevInfo, brevResponse } from "~test/support/brevFixtures";

const { oppdaterBrevMock, oppdaterBrevtekstMock, tilbakestillBrevMock } = vi.hoisted(() => ({
  oppdaterBrevMock: vi.fn(),
  oppdaterBrevtekstMock: vi.fn(),
  tilbakestillBrevMock: vi.fn(),
}));

vi.mock("~/api/brev-queries", async (importOriginal) => ({
  ...(await importOriginal<typeof import("~/api/brev-queries")>()),
  oppdaterBrev: oppdaterBrevMock,
  oppdaterBrevtekst: oppdaterBrevtekstMock,
  tilbakestillBrev: tilbakestillBrevMock,
}));

const lagretBrev = brevResponse({
  info: brevInfo({ id: 1, saksId: 123456 }),
  saksbehandlerValg: { ytelse: "alderspensjon" },
});
const nyeValg = { ytelse: "Supplerende stønad" };
const lagringsfeil = new Error("Lagring feilet");

function renderAutosave() {
  const result: {
    current?: ReturnType<typeof useManagedLetterEditorContext> & {
      lagreValg: (recordHistory?: boolean) => Promise<BrevResponse>;
    };
  } = {};

  // biome-ignore lint/correctness/noComponentHookFactories: Created once per test harness, with stable identity across renders.
  const Testkomponent = () => {
    const context = useManagedLetterEditorContext();
    const { oppdaterBrevMutation } = useOppdaterBrevMutation("123456");
    result.current = {
      ...context,
      lagreValg: (recordHistory) =>
        oppdaterBrevMutation.mutateAsync({
          saksbehandlerValg: nyeValg,
          recordHistory,
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

  test("bevarer lagrede tekstvalg og nyere brevtekst ved neste autolagring", async () => {
    const response = Promise.withResolvers<BrevResponse>();
    const lagretMedNyeValg = { ...lagretBrev, saksbehandlerValg: nyeValg };
    oppdaterBrevMock.mockReturnValueOnce(response.promise);
    oppdaterBrevMock.mockImplementationOnce(({ request }) => Promise.resolve({ ...lagretMedNyeValg, ...request }));
    oppdaterBrevtekstMock.mockImplementation(({ redigertBrev }: { redigertBrev: EditedLetter }) =>
      Promise.resolve({ ...lagretMedNyeValg, redigertBrev }),
    );
    const harness = renderAutosave();
    let saving!: Promise<BrevResponse>;
    await act(async () => {
      saving = harness().lagreValg();
    });
    endreBrev(harness, 101);
    const nyereBrevtekst = harness().redigertBrev;
    await act(async () => {
      response.resolve(lagretMedNyeValg);
      expect(await saving).toBe(lagretMedNyeValg);
    });

    expect(harness().redigertBrev).toBe(nyereBrevtekst);
    expect(harness().editorState.saksbehandlerValg).toEqual(nyeValg);
    expect(harness().editorState.saveStatus).toBe("DIRTY");

    await vent(AUTOSAVE_TIMER);
    expect(oppdaterBrevMock).toHaveBeenCalledTimes(2);
    expect(oppdaterBrevMock).toHaveBeenLastCalledWith(
      expect.objectContaining({
        request: expect.objectContaining({ redigertBrev: expect.objectContaining({ deletedBlocks: [101] }) }),
        frigiReservasjon: false,
      }),
    );
    expect(oppdaterBrevtekstMock).not.toHaveBeenCalled();
    expect(harness().editorState.saksbehandlerValg).toEqual(nyeValg);
    expect(harness().redigertBrev.deletedBlocks).toEqual([101]);
    expect(harness().editorState.saveStatus).toBe("SAVED");
  });

  for (const ytelse of ["Uføretrygd", "alderspensjon"]) {
    test(`bevarer nyere lokale valg av ${ytelse} mens tekstvalglagringen pågår`, async () => {
      const response = Promise.withResolvers<BrevResponse>();
      oppdaterBrevMock.mockReturnValueOnce(response.promise);
      oppdaterBrevMock.mockImplementationOnce(({ request }) => Promise.resolve({ ...lagretBrev, ...request }));
      const harness = renderAutosave();
      let saving!: Promise<BrevResponse>;
      await act(async () => {
        saving = harness().lagreValg();
      });
      const nyereValg = { ytelse };
      act(() => harness().setEditorState((state) => ({ ...state, saksbehandlerValg: nyereValg, saveStatus: "DIRTY" })));
      await act(async () => {
        response.resolve({ ...lagretBrev, saksbehandlerValg: nyeValg });
        await saving;
      });

      expect(harness().editorState.saksbehandlerValg).toBe(nyereValg);
      expect(harness().editorState.saveStatus).toBe("DIRTY");
      await vent(AUTOSAVE_TIMER);
      expect(oppdaterBrevMock).toHaveBeenCalledTimes(2);
      expect(oppdaterBrevMock).toHaveBeenLastCalledWith(
        expect.objectContaining({
          request: expect.objectContaining({ saksbehandlerValg: nyereValg }),
          frigiReservasjon: false,
        }),
      );
      expect(oppdaterBrevtekstMock).not.toHaveBeenCalled();
      expect(harness().editorState.saksbehandlerValg).toEqual(nyereValg);
      expect(harness().editorState.saveStatus).toBe("SAVED");
    });
  }

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

  test("eksplisitt lagring venter på autolagring og lagrer nyere brevtekst", async () => {
    const first = Promise.withResolvers<BrevResponse>();
    oppdaterBrevtekstMock.mockReturnValueOnce(first.promise);
    const harness = renderAutosave();
    endreBrev(harness, 100);
    await vent(AUTOSAVE_TIMER);
    endreBrev(harness, 101);
    const finished = vi.fn();
    let saving!: Promise<void>;
    await act(async () => {
      saving = harness().savePendingChanges().then(finished);
    });
    expect(finished).not.toHaveBeenCalled();
    expect(oppdaterBrevtekstMock).toHaveBeenCalledTimes(1);

    await act(async () => {
      first.resolve({ ...lagretBrev, redigertBrev: { ...lagretBrev.redigertBrev, deletedBlocks: [100] } });
      await saving;
    });
    expect(oppdaterBrevtekstMock).toHaveBeenCalledTimes(2);
    expect(oppdaterBrevtekstMock).toHaveBeenLastCalledWith(
      expect.objectContaining({ redigertBrev: expect.objectContaining({ deletedBlocks: [100, 101] }) }),
    );
    expect(harness().editorState.saveStatus).toBe("SAVED");
  });

  test("tilbakestilling venter på autolagring og erstatter ulagrede endringer", async () => {
    const first = Promise.withResolvers<BrevResponse>();
    oppdaterBrevtekstMock.mockReturnValueOnce(first.promise);
    tilbakestillBrevMock.mockResolvedValue(lagretBrev);
    const harness = renderAutosave();
    endreBrev(harness, 100);
    await vent(AUTOSAVE_TIMER);
    endreBrev(harness, 101);
    let resetting!: Promise<void>;
    await act(async () => {
      resetting = harness().resetLetter();
    });
    expect(harness().resetting).toBe(true);
    expect(tilbakestillBrevMock).not.toHaveBeenCalled();
    await act(async () => {
      first.resolve({ ...lagretBrev, redigertBrev: { ...lagretBrev.redigertBrev, deletedBlocks: [100] } });
      await resetting;
    });
    expect(tilbakestillBrevMock).toHaveBeenCalledOnce();
    expect(harness().editorState.redigertBrev).toEqual(lagretBrev.redigertBrev);
    expect(harness().editorState.saveStatus).toBe("SAVED");
    expect(harness().resetting).toBe(false);
    await vent(AUTOSAVE_TIMER);
    expect(oppdaterBrevtekstMock).toHaveBeenCalledOnce();
  });

  test("tekstvalglagring venter på autolagring og bruker siste brevtekst", async () => {
    const first = Promise.withResolvers<BrevResponse>();
    oppdaterBrevtekstMock.mockReturnValueOnce(first.promise);
    oppdaterBrevMock.mockImplementation(({ request }) => Promise.resolve({ ...lagretBrev, ...request }));
    const harness = renderAutosave();
    endreBrev(harness, 100);
    await vent(AUTOSAVE_TIMER);
    endreBrev(harness, 101);
    let saving!: Promise<BrevResponse>;
    await act(async () => {
      saving = harness().lagreValg(true);
    });
    expect(oppdaterBrevMock).not.toHaveBeenCalled();
    await act(async () => {
      first.resolve({ ...lagretBrev, redigertBrev: { ...lagretBrev.redigertBrev, deletedBlocks: [100] } });
      await saving;
    });
    expect(oppdaterBrevMock).toHaveBeenCalledWith(
      expect.objectContaining({
        request: {
          redigertBrev: expect.objectContaining({ deletedBlocks: [100, 101] }),
          saksbehandlerValg: nyeValg,
        },
      }),
    );
    expect(harness().editorState.saksbehandlerValg).toEqual(nyeValg);
    expect(harness().editorState.history.entries.at(-1)).toMatchObject({
      type: "SAKSBEHANDLERVALG_ENDRET",
      before: { redigertBrev: { deletedBlocks: [100, 101] }, saksbehandlerValg: lagretBrev.saksbehandlerValg },
      after: { redigertBrev: { deletedBlocks: [100, 101] }, saksbehandlerValg: nyeValg },
    });
    expect(harness().editorState.saveStatus).toBe("SAVED");
    await vent(AUTOSAVE_TIMER);
    expect(oppdaterBrevtekstMock).toHaveBeenCalledOnce();
  });

  test("samler raske valg, utsetter fristen og registrerer hvert valg i historikken", async () => {
    oppdaterBrevMock.mockImplementation(({ request }) => Promise.resolve({ ...lagretBrev, ...request }));
    const harness = renderAutosave();
    const first = { ...lagretBrev.saksbehandlerValg, visTekst: true };
    const last = { ...first, ytelse: "Uføretrygd" };
    act(() => harness().updateSaksbehandlerValg(first, true));
    expect(harness().editorState.saksbehandlerValg).toEqual(first);
    await vent(SAKSBEHANDLERVALG_TIMER - 1);
    act(() => harness().updateSaksbehandlerValg(last, true));
    await vent(SAKSBEHANDLERVALG_TIMER - 1);
    expect(oppdaterBrevMock).not.toHaveBeenCalled();
    await vent(1);
    expect(oppdaterBrevMock).toHaveBeenCalledExactlyOnceWith(
      expect.objectContaining({
        request: expect.objectContaining({ saksbehandlerValg: last }),
        frigiReservasjon: false,
      }),
    );
    expect(harness().editorState.history.entries).toMatchObject([
      {
        type: "SAKSBEHANDLERVALG_ENDRET",
        before: { saksbehandlerValg: lagretBrev.saksbehandlerValg },
        after: { saksbehandlerValg: first },
      },
      { type: "SAKSBEHANDLERVALG_ENDRET", before: { saksbehandlerValg: first }, after: { saksbehandlerValg: last } },
    ]);
  });

  test("nyere valg venter på aktiv lagring uten å miste utløpt debounce", async () => {
    const first = Promise.withResolvers<BrevResponse>();
    oppdaterBrevMock.mockReturnValueOnce(first.promise);
    oppdaterBrevMock.mockImplementationOnce(({ request }) => Promise.resolve({ ...lagretBrev, ...request }));
    const harness = renderAutosave();
    act(() => harness().updateSaksbehandlerValg(nyeValg, true));
    expect(harness().awaitingValgMerge).toBe(true);
    await vent(SAKSBEHANDLERVALG_TIMER);
    expect(harness().awaitingValgMerge).toBe(true);
    const latest = { ytelse: "Uføretrygd" };
    act(() => harness().updateSaksbehandlerValg(latest, true));
    await vent(SAKSBEHANDLERVALG_TIMER);
    expect(oppdaterBrevMock).toHaveBeenCalledTimes(1);
    await act(async () => first.resolve({ ...lagretBrev, saksbehandlerValg: nyeValg }));
    expect(oppdaterBrevMock).toHaveBeenCalledTimes(2);
    expect(harness().editorState.saksbehandlerValg).toEqual(latest);
    expect(harness().editorState.saveStatus).toBe("SAVED");
    expect(harness().awaitingValgMerge).toBe(false);
  });

  test("frysingen oppheves når flettingen feiler, men neste lagring fletter likevel", async () => {
    const harness = renderAutosave();
    act(() => harness().updateSaksbehandlerValg(nyeValg, true));
    await vent(SAKSBEHANDLERVALG_TIMER);
    expect(harness().saveFailed).toBe(true);
    expect(harness().awaitingValgMerge).toBe(false);
    oppdaterBrevMock.mockImplementation(({ request }) => Promise.resolve({ ...lagretBrev, ...request }));
    endreBrev(harness, 100);
    await vent(AUTOSAVE_TIMER);
    expect(oppdaterBrevMock).toHaveBeenCalledTimes(2);
    expect(oppdaterBrevtekstMock).not.toHaveBeenCalled();
    expect(harness().editorState.saveStatus).toBe("SAVED");
  });

  test("nye valg forlenger også fristen mens et eldre kall pågår", async () => {
    const first = Promise.withResolvers<BrevResponse>();
    oppdaterBrevMock.mockReturnValueOnce(first.promise);
    oppdaterBrevMock.mockImplementationOnce(({ request }) => Promise.resolve({ ...lagretBrev, ...request }));
    const harness = renderAutosave();
    act(() => harness().updateSaksbehandlerValg(nyeValg, true));
    await vent(SAKSBEHANDLERVALG_TIMER);
    act(() => harness().updateSaksbehandlerValg({ ytelse: "Uføretrygd" }, true));
    await vent(SAKSBEHANDLERVALG_TIMER);
    act(() => harness().updateSaksbehandlerValg(lagretBrev.saksbehandlerValg, true));
    await act(async () => first.resolve({ ...lagretBrev, saksbehandlerValg: nyeValg }));
    expect(oppdaterBrevMock).toHaveBeenCalledTimes(1);
    expect(harness().editorState.saksbehandlerValg).toEqual(lagretBrev.saksbehandlerValg);
    await vent(SAKSBEHANDLERVALG_TIMER);
    expect(oppdaterBrevMock).toHaveBeenCalledTimes(2);
    expect(oppdaterBrevMock).toHaveBeenLastCalledWith(
      expect.objectContaining({
        request: expect.objectContaining({ saksbehandlerValg: lagretBrev.saksbehandlerValg }),
      }),
    );
  });

  test("tekstendringer utsetter ikke valgfristen og forkastet fletting gjentas", async () => {
    const first = Promise.withResolvers<BrevResponse>();
    oppdaterBrevMock.mockReturnValueOnce(first.promise);
    oppdaterBrevMock.mockImplementationOnce(({ request }) => Promise.resolve({ ...lagretBrev, ...request }));
    const harness = renderAutosave();
    act(() => harness().updateSaksbehandlerValg(nyeValg, true));
    await vent(SAKSBEHANDLERVALG_TIMER - 1);
    endreBrev(harness, 100);
    await vent(1);
    expect(oppdaterBrevMock).toHaveBeenCalledTimes(1);
    endreBrev(harness, 101);
    await act(async () => first.resolve({ ...lagretBrev, saksbehandlerValg: nyeValg }));
    expect(harness().editorState.saveStatus).toBe("DIRTY");
    await vent(AUTOSAVE_TIMER);
    expect(oppdaterBrevtekstMock).not.toHaveBeenCalled();
    expect(oppdaterBrevMock).toHaveBeenCalledTimes(2);
    expect(oppdaterBrevMock).toHaveBeenLastCalledWith(
      expect.objectContaining({
        request: expect.objectContaining({
          saksbehandlerValg: nyeValg,
          redigertBrev: expect.objectContaining({ deletedBlocks: [100, 101] }),
        }),
      }),
    );
    endreBrev(harness, 102);
    await vent(AUTOSAVE_TIMER);
    expect(oppdaterBrevtekstMock).toHaveBeenCalledOnce();
  });

  test("flytter fokus og fokus i historikken når flettingen setter inn et avsnitt foran", async () => {
    const innsatt = { ...lagretBrev.redigertBrev.blocks[0], id: 999, content: [] };
    oppdaterBrevMock.mockImplementationOnce(({ request }) =>
      Promise.resolve({
        ...lagretBrev,
        saksbehandlerValg: request.saksbehandlerValg,
        redigertBrev: { ...request.redigertBrev, blocks: [innsatt, ...request.redigertBrev.blocks] },
      }),
    );
    const harness = renderAutosave();
    act(() => harness().updateSaksbehandlerValg(nyeValg, true));
    const focus = { blockIndex: 1, contentIndex: 0, cursorPosition: 5 };
    act(() =>
      harness().setEditorState((state) => ({
        ...state,
        saveStatus: "DIRTY",
        focus,
        history: addToHistory(
          state.history,
          [{ op: "replace", path: ["redigertBrev", "blocks", 1, "content", 0, "editedText"], value: "x" }],
          [{ op: "replace", path: ["redigertBrev", "blocks", 1, "content", 0, "editedText"], value: null }],
        ),
      })),
    );
    await vent(SAKSBEHANDLERVALG_TIMER);

    const flyttet = { ...focus, blockIndex: 2 };
    expect(harness().redigertBrev.blocks[0].id).toBe(999);
    expect(harness().editorState.focus).toEqual(flyttet);
    expect(harness().editorState.history.entries.at(-1)).toMatchObject({
      type: "DOCUMENT_SNAPSHOT",
      afterFocus: flyttet,
    });
  });

  test("gjentatte feltsubmits starter ikke nye forsøk etter feil, men eksplisitt lagring gjør det", async () => {
    const harness = renderAutosave();
    act(() => harness().updateSaksbehandlerValg(nyeValg, true));
    await vent(SAKSBEHANDLERVALG_TIMER);
    expect(harness().saveFailed).toBe(true);
    act(() => harness().updateSaksbehandlerValg({ ...nyeValg }, true));
    await vent(AUTOSAVE_TIMER * 2);
    expect(oppdaterBrevMock).toHaveBeenCalledTimes(1);
    expect(harness().editorState.saksbehandlerValg).toEqual(nyeValg);
    oppdaterBrevMock.mockImplementation(({ request }) => Promise.resolve({ ...lagretBrev, ...request }));
    await act(() => harness().savePendingChanges());
    expect(oppdaterBrevMock).toHaveBeenCalledTimes(2);
    expect(harness().saveFailed).toBe(false);
  });

  test("eksplisitt lagring og tilbakestilling avklarer ventende valg uten ekstra kall", async () => {
    oppdaterBrevMock.mockImplementation(({ request }) => Promise.resolve({ ...lagretBrev, ...request }));
    tilbakestillBrevMock.mockResolvedValue(lagretBrev);
    const harness = renderAutosave();
    act(() => harness().updateSaksbehandlerValg(nyeValg, true));
    await act(() => harness().savePendingChanges());
    await vent(SAKSBEHANDLERVALG_TIMER);
    expect(oppdaterBrevMock).toHaveBeenCalledOnce();
    act(() => harness().updateSaksbehandlerValg({ ytelse: "Uføretrygd" }, true));
    await act(() => harness().resetLetter());
    await vent(AUTOSAVE_TIMER);
    expect(oppdaterBrevMock).toHaveBeenCalledOnce();
    expect(harness().editorState.saksbehandlerValg).toEqual(lagretBrev.saksbehandlerValg);
  });
});
