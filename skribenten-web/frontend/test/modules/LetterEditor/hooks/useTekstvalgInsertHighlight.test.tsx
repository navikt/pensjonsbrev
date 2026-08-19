import { QueryClient, QueryClientProvider, useQuery } from "@tanstack/react-query";
import { act, render, waitFor } from "@testing-library/react";
import { type ReactNode } from "react";
import { beforeEach, describe, expect, test, vi } from "vitest";

import { getBrev } from "~/api/brev-queries";
import { useOppdaterBrevAutosave } from "~/Brevredigering/hooks/useOppdaterBrevAutosave";
import { useTekstvalgInsertHighlight } from "~/Brevredigering/LetterEditor/hooks/useTekstvalgInsertHighlight";
import {
  ManagedLetterEditorContextProvider,
  useManagedLetterEditorContext,
} from "~/components/ManagedLetterEditor/ManagedLetterEditorContext";
import { type BrevResponse, type SaksbehandlerValg } from "~/types/brev";

import {
  brevInfo,
  brevResponse,
  editedLetter,
  literal,
  paragraph,
  withParent,
} from "../../../utils/letterEditorTestUtils";

const { oppdaterBrevMock } = vi.hoisted(() => ({ oppdaterBrevMock: vi.fn() }));

vi.mock("~/api/brev-queries", async (importOriginal) => ({
  ...(await importOriginal<typeof import("~/api/brev-queries")>()),
  oppdaterBrev: oppdaterBrevMock,
}));

const SAKS_ID = "123456";
const BREV_ID = 1;

const INSERTED_BLOCK_ID = 999_000;
const INSERTED_LITERAL_ID = 999_001;

const opprinneligBlokk = paragraph({
  id: 100,
  content: withParent([literal({ id: 101, text: "Opprinnelig avsnitt." })], 100),
});

const innsattTekstvalgBlokk = paragraph({
  id: INSERTED_BLOCK_ID,
  content: withParent([literal({ id: INSERTED_LITERAL_ID, text: "Innsatt tekstvalg." })], INSERTED_BLOCK_ID),
});

const lagretBrev: BrevResponse = brevResponse({
  info: brevInfo({ id: BREV_ID }),
  redigertBrev: editedLetter({ blocks: [opprinneligBlokk] }),
  saksbehandlerValg: { visTekstvalg: false },
});

/** Svaret fra backend når tekstvalget er slått på: brevet har fått en ny blokk. */
const brevMedTekstvalg: BrevResponse = {
  ...lagretBrev,
  redigertBrev: editedLetter({ blocks: [opprinneligBlokk, innsattTekstvalgBlokk] }),
  redigertBrevHash: "etterTekstvalg",
  saksbehandlerValg: { visTekstvalg: true },
};

const nyeValg: SaksbehandlerValg = { visTekstvalg: true };

type Harness = {
  highlightedIds: ReadonlySet<number>;
  focus: { blockIndex: number; contentIndex?: number };
  beforeTekstvalgChange: (valg: SaksbehandlerValg) => void;
  lagreTekstvalg: () => void;
  simulerTasting: () => void;
};

/**
 * Speiler hvordan rutene kobler sammen autolagring og tekstvalg-highlight.
 *
 * Den ekte <ManagedLetterEditorContextProvider /> brukes med vilje: feilen vi tester for oppstår
 * bare når `onSaveSuccess` og `setEditorState` treffer samme fiber/hook-kø.
 */
function renderHarness() {
  const harness = { current: null as Harness | null };

  const Testkomponent = () => {
    const { editorState, setEditorState, onSaveSuccess } = useManagedLetterEditorContext();

    // Rutene leser brevet fra query-cachen, som `onSaveSuccess` skriver til. Abonnementet her
    // gjør at `lagretRedigertBrev` oppdateres på samme måte som i produksjon.
    const { data: brevFraCache } = useQuery({
      queryKey: getBrev.queryKey(BREV_ID),
      queryFn: () => lagretBrev,
      initialData: lagretBrev,
      staleTime: Number.POSITIVE_INFINITY,
    });

    const { highlightedIds, beforeTekstvalgChange } = useTekstvalgInsertHighlight({
      lagretRedigertBrev: brevFraCache.redigertBrev,
      editorState: editorState,
      setEditorState: setEditorState,
    });

    const { oppdaterBrevMutation } = useOppdaterBrevAutosave({
      saksId: SAKS_ID,
      brevId: BREV_ID,
      setEditorState: setEditorState,
      onSaveSuccess: onSaveSuccess,
    });

    harness.current = {
      highlightedIds: highlightedIds,
      focus: editorState.focus,
      beforeTekstvalgChange: (valg) => beforeTekstvalgChange(valg, editorState.redigertBrev),
      lagreTekstvalg: () =>
        oppdaterBrevMutation.mutate({ redigertBrev: editorState.redigertBrev, saksbehandlerValg: nyeValg }),
      simulerTasting: () => setEditorState((state) => ({ ...state, saveStatus: "DIRTY" })),
    };

    return null;
  };

  const wrapper = (props: { children: ReactNode }) => (
    <QueryClientProvider client={new QueryClient({ defaultOptions: { queries: { retry: false } } })}>
      <ManagedLetterEditorContextProvider brev={lagretBrev}>{props.children}</ManagedLetterEditorContextProvider>
    </QueryClientProvider>
  );

  render(wrapper({ children: <Testkomponent /> }));

  return () => {
    if (!harness.current) throw new Error("Testkomponenten er ikke rendret");
    return harness.current;
  };
}

/** Lar testen bestemme nøyaktig når lagringen svarer. */
function utsattSvar() {
  let resolve: (response: BrevResponse) => void = () => {};
  const promise = new Promise<BrevResponse>((r) => {
    resolve = r;
  });
  oppdaterBrevMock.mockReturnValue(promise);
  return async (response: BrevResponse) => {
    await act(async () => {
      resolve(response);
      await promise;
    });
  };
}

describe("tekstvalg-highlight ved lagring", () => {
  beforeEach(() => {
    oppdaterBrevMock.mockReset();
  });

  test("markerer innsatt tekstvalg og flytter markøren når lagringen blir tatt i bruk", async () => {
    const svar = utsattSvar();
    const harness = renderHarness();

    act(() => {
      harness().beforeTekstvalgChange(nyeValg);
      harness().lagreTekstvalg();
    });
    await svar(brevMedTekstvalg);

    await waitFor(() => expect([...harness().highlightedIds]).toContain(INSERTED_BLOCK_ID));
    expect([...harness().highlightedIds]).toContain(INSERTED_LITERAL_ID);
    expect(harness().focus.blockIndex).toBe(1);
  });

  test("markerer ingenting når brukeren taster mens lagringen pågår, slik at svaret forkastes", async () => {
    const svar = utsattSvar();
    const harness = renderHarness();

    act(() => {
      harness().beforeTekstvalgChange(nyeValg);
      harness().lagreTekstvalg();
    });

    // Brukeren taster før svaret kommer. `onSaveSuccess` forkaster da svaret, og brukeren ser
    // fortsatt sin egen tekst — da må vi verken markere noe eller flytte markøren.
    act(() => harness().simulerTasting());
    await svar(brevMedTekstvalg);

    expect([...harness().highlightedIds]).toEqual([]);
    expect(harness().focus).toEqual({ blockIndex: 0, contentIndex: 0 });
  });

  test("lar ikke et tekstvalg som ikke satte inn noe markere en senere lagring", async () => {
    // Et tekstvalg kan endres uten at det kommer nye id-er, f.eks. når et radio-tekstvalg bare
    // bytter ut tekst i eksisterende innhold.
    const førsteSvar = utsattSvar();
    const harness = renderHarness();

    act(() => {
      harness().beforeTekstvalgChange(nyeValg);
      harness().lagreTekstvalg();
    });
    await førsteSvar({ ...lagretBrev, redigertBrevHash: "utenNyeIder", saksbehandlerValg: nyeValg });

    expect([...harness().highlightedIds]).toEqual([]);

    // Neste lagring inneholder nytt innhold saksbehandler selv har skrevet. Den forrige,
    // tomme tekstvalg-endringen skal ikke få denne til å blinke eller flytte markøren.
    const andreSvar = utsattSvar();
    act(() => harness().lagreTekstvalg());
    await andreSvar(brevMedTekstvalg);

    expect([...harness().highlightedIds]).toEqual([]);
    expect(harness().focus).toEqual({ blockIndex: 0, contentIndex: 0 });
  });
});
