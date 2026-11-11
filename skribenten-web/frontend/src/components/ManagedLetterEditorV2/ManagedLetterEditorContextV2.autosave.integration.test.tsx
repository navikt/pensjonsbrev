import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { act, render } from "@testing-library/react";
import { afterEach, beforeEach, describe, expect, test, vi } from "vitest";

import { RedigeringsflateProvider } from "~/Brevredigering/LetterEditor/RedigeringsflateContext";
import { AUTOSAVE_TIMER } from "~/components/ManagedLetterEditor/autosave_timer";
import {
  ManagedLetterEditorContextV2Provider,
  useManagedLetterEditorContextV2,
} from "~/components/ManagedLetterEditorV2/ManagedLetterEditorContextV2";
import { useOppdaterBrevMutationV2 } from "~/components/ManagedLetterEditorV2/useOppdaterBrevMutationV2";
import { type BrevResponseV2, type LetterV2, type OppdaterBrevRequestV2 } from "~/types/brevV2";
import { brevInfoV2, brevResponseV2 } from "~test/support/brevV2Fixtures";

const { oppdaterBrevMock, oppdaterRedigertBrevMock, tilbakestillBrevMock } = vi.hoisted(() => ({
  oppdaterBrevMock: vi.fn(),
  oppdaterRedigertBrevMock: vi.fn(),
  tilbakestillBrevMock: vi.fn(),
}));

vi.mock("~/api/brevV2-queries", async (importOriginal) => ({
  ...(await importOriginal<typeof import("~/api/brevV2-queries")>()),
  oppdaterBrevV2: oppdaterBrevMock,
  oppdaterRedigertBrevV2: oppdaterRedigertBrevMock,
  tilbakestillBrevV2: tilbakestillBrevMock,
}));

const lagretBrev = brevResponseV2({
  info: brevInfoV2({ id: 1, saksId: 123456 }),
  saksbehandlerValg: { ytelse: "alderspensjon" },
});
const nyeValg = { ytelse: "Supplerende stønad" };
const lagringsfeil = new Error("Lagring feilet");

type AutosaveHarness = ReturnType<typeof useManagedLetterEditorContextV2> & {
  lagreValg: (recordHistory?: boolean) => Promise<BrevResponseV2>;
};

const Testkomponent = ({ result }: { result: { current?: AutosaveHarness } }) => {
  const context = useManagedLetterEditorContextV2();
  const oppdaterBrevMutation = useOppdaterBrevMutationV2();
  result.current = {
    ...context,
    lagreValg: (recordHistory) => oppdaterBrevMutation.mutateAsync({ saksbehandlerValg: nyeValg, recordHistory }),
  };
  return null;
};

function renderAutosave() {
  const result: { current?: AutosaveHarness } = {};

  render(
    <QueryClientProvider client={new QueryClient({ defaultOptions: { mutations: { retry: false } } })}>
      <RedigeringsflateProvider redigeringsflate="saksbehandler-redigering">
        <ManagedLetterEditorContextV2Provider brev={lagretBrev}>
          <Testkomponent result={result} />
        </ManagedLetterEditorContextV2Provider>
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

const medSlettedeBlokker = (deletedBlocks: number[]): BrevResponseV2 => ({
  ...lagretBrev,
  redigertBrev: { ...lagretBrev.redigertBrev, deletedBlocks },
});

async function vent(ms: number) {
  await act(async () => vi.advanceTimersByTimeAsync(ms));
}

describe("samspill mellom tekstvalg og brevets autolagring (V2)", () => {
  beforeEach(() => {
    vi.useFakeTimers();
    vi.resetAllMocks();
    oppdaterBrevMock.mockRejectedValue(lagringsfeil);
    oppdaterRedigertBrevMock.mockImplementation(({ redigertBrev }: { redigertBrev: LetterV2 }) =>
      Promise.resolve({ ...structuredClone(lagretBrev), redigertBrev }),
    );
  });

  afterEach(() => vi.useRealTimers());

  test("bevarer lagrede tekstvalg og nyere brevtekst ved neste autolagring", async () => {
    const response = Promise.withResolvers<BrevResponseV2>();
    const lagretMedNyeValg = { ...lagretBrev, saksbehandlerValg: nyeValg };
    oppdaterBrevMock.mockReturnValueOnce(response.promise);
    oppdaterRedigertBrevMock.mockImplementation(({ redigertBrev }: { redigertBrev: LetterV2 }) =>
      Promise.resolve({ ...lagretMedNyeValg, redigertBrev }),
    );
    const harness = renderAutosave();
    let saving!: Promise<BrevResponseV2>;
    await act(async () => {
      saving = harness().lagreValg();
    });
    endreBrev(harness, 101);
    const nyereBrevtekst = harness().editorState.redigertBrev;
    await act(async () => {
      response.resolve(lagretMedNyeValg);
      expect(await saving).toBe(lagretMedNyeValg);
    });

    expect(harness().editorState.redigertBrev).toBe(nyereBrevtekst);
    expect(harness().editorState.saksbehandlerValg).toEqual(nyeValg);
    expect(harness().editorState.saveStatus).toBe("DIRTY");

    await vent(AUTOSAVE_TIMER);
    expect(oppdaterBrevMock).toHaveBeenCalledTimes(1);
    expect(oppdaterRedigertBrevMock).toHaveBeenCalledExactlyOnceWith(
      expect.objectContaining({
        redigertBrev: expect.objectContaining({ deletedBlocks: [101] }),
        frigiReservasjon: false,
      }),
    );
    expect(harness().editorState.redigertBrev.deletedBlocks).toEqual([101]);
    expect(harness().editorState.saveStatus).toBe("SAVED");
  });

  test("lagrer nyere lokale tekstvalg via oppdaterBrev-endepunktet", async () => {
    const response = Promise.withResolvers<BrevResponseV2>();
    oppdaterBrevMock.mockReturnValueOnce(response.promise);
    oppdaterBrevMock.mockImplementationOnce(({ request }: { request: OppdaterBrevRequestV2 }) =>
      Promise.resolve({ ...lagretBrev, ...request }),
    );
    const harness = renderAutosave();
    let saving!: Promise<BrevResponseV2>;
    await act(async () => {
      saving = harness().lagreValg();
    });
    const nyereValg = { ytelse: "Uføretrygd" };
    act(() => harness().setEditorState((state) => ({ ...state, saksbehandlerValg: nyereValg, saveStatus: "DIRTY" })));
    await act(async () => {
      response.resolve({ ...lagretBrev, saksbehandlerValg: nyeValg });
      await saving;
    });

    expect(harness().editorState.saksbehandlerValg).toBe(nyereValg);
    await vent(AUTOSAVE_TIMER);
    expect(oppdaterBrevMock).toHaveBeenLastCalledWith(
      expect.objectContaining({
        request: expect.objectContaining({ saksbehandlerValg: nyereValg }),
        frigiReservasjon: false,
      }),
    );
    expect(oppdaterRedigertBrevMock).not.toHaveBeenCalled();
    expect(harness().editorState.saveStatus).toBe("SAVED");
  });

  test("en mislykket tekstvalglagring starter ikke autolagring av uendret brevtekst", async () => {
    const harness = renderAutosave();
    await act(async () => {
      await expect(harness().lagreValg()).rejects.toBe(lagringsfeil);
    });

    expect(harness().editorState.saveStatus).toBe("SAVED");
    await vent(AUTOSAVE_TIMER);
    expect(oppdaterRedigertBrevMock).not.toHaveBeenCalled();
    expect(oppdaterBrevMock).toHaveBeenCalledTimes(1);
  });

  test("utsetter autolagring til etter siste innholdsendring", async () => {
    const harness = renderAutosave();
    endreBrev(harness, 100);
    await vent(AUTOSAVE_TIMER - 1);
    endreBrev(harness, 101);
    await vent(AUTOSAVE_TIMER - 1);
    expect(oppdaterRedigertBrevMock).not.toHaveBeenCalled();
    await vent(1);
    expect(oppdaterRedigertBrevMock).toHaveBeenCalledExactlyOnceWith(
      expect.objectContaining({ redigertBrev: expect.objectContaining({ deletedBlocks: [100, 101] }) }),
    );
  });

  test("markørflytting alene utsetter ikke autolagringen", async () => {
    const harness = renderAutosave();
    endreBrev(harness, 100);
    await vent(AUTOSAVE_TIMER - 1);
    act(() => harness().setEditorState((state) => ({ ...state, focus: { ...state.focus, offset: 2 } })));
    await vent(1);
    expect(oppdaterRedigertBrevMock).toHaveBeenCalledTimes(1);
  });

  test("beholder historikken når lagret dokument er likt det lokale", async () => {
    const harness = renderAutosave();
    act(() =>
      harness().setEditorState((state) => ({
        ...state,
        saveStatus: "DIRTY",
        redigertBrev: { ...state.redigertBrev, deletedBlocks: [102] },
        history: { entries: [{ type: "PATCH", patches: [], inversePatches: [] }], entryPointer: 0 },
      })),
    );
    await vent(AUTOSAVE_TIMER);

    expect(harness().editorState.history.entries).toHaveLength(1);
  });

  test("tilbakestilling venter på autolagring og erstatter ulagrede endringer", async () => {
    const first = Promise.withResolvers<BrevResponseV2>();
    oppdaterRedigertBrevMock.mockReturnValueOnce(first.promise);
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
      first.resolve(medSlettedeBlokker([100]));
      await resetting;
    });
    expect(tilbakestillBrevMock).toHaveBeenCalledExactlyOnceWith(123456, 1);
    expect(harness().editorState.redigertBrev).toEqual(lagretBrev.redigertBrev);
    expect(harness().editorState.saveStatus).toBe("SAVED");
    expect(harness().resetting).toBe(false);
    await vent(AUTOSAVE_TIMER);
    expect(oppdaterRedigertBrevMock).toHaveBeenCalledOnce();
  });

  test("tekstvalglagring venter på autolagring, bruker siste brevtekst og registrerer historikk", async () => {
    const first = Promise.withResolvers<BrevResponseV2>();
    oppdaterRedigertBrevMock.mockReturnValueOnce(first.promise);
    oppdaterBrevMock.mockImplementation(({ request }: { request: OppdaterBrevRequestV2 }) =>
      Promise.resolve({ ...lagretBrev, ...request }),
    );
    const harness = renderAutosave();
    endreBrev(harness, 100);
    await vent(AUTOSAVE_TIMER);
    endreBrev(harness, 101);
    let saving!: Promise<BrevResponseV2>;
    await act(async () => {
      saving = harness().lagreValg(true);
    });
    expect(oppdaterBrevMock).not.toHaveBeenCalled();
    await act(async () => {
      first.resolve(medSlettedeBlokker([100]));
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
    expect(oppdaterRedigertBrevMock).toHaveBeenCalledOnce();
  });
});
