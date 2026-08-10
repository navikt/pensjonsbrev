// @vitest-environment happy-dom

import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { act, render } from "@testing-library/react";
import { afterEach, describe, expect, test, vi } from "vitest";

import * as brevQueries from "~/api/brev-queries";
import { type LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { AUTOSAVE_TIMER } from "~/components/ManagedLetterEditor/autosave_timer";
import ManagedLetterEditor from "~/components/ManagedLetterEditor/ManagedLetterEditor";
import {
  ManagedLetterEditorContextProvider,
  useManagedLetterEditorContext,
} from "~/components/ManagedLetterEditor/ManagedLetterEditorContext";

import { brevResponse } from "../../utils/letterEditorTestUtils";

vi.mock("~/api/brev-queries", async (importOriginal) => {
  const original = await importOriginal<typeof brevQueries>();
  return {
    ...original,
    oppdaterBrevtekst: vi.fn(),
    getBrev: { queryKey: (id: number) => ["brev", id] },
    attesteringBrevKeys: { id: (id: number) => ["attestering", id] },
  };
});

vi.mock("~/api/sak-api-endpoints", async (importOriginal) => {
  const original = await importOriginal<typeof import("~/api/sak-api-endpoints")>();
  return {
    ...original,
    hentPdfForBrev: { queryKey: (id: number) => ["pdf", id] },
  };
});

vi.mock("~/Brevredigering/LetterEditor/LetterEditor", async (importOriginal) => {
  const original = await importOriginal<typeof import("~/Brevredigering/LetterEditor/LetterEditor")>();
  return {
    ...original,
    // Redigeringsflaten erstattes med en knapp som simulerer en redigering.
    LetterEditor: (props: { setEditorState: (fn: (s: LetterEditorState) => LetterEditorState) => void }) => (
      <button
        data-testid="rediger"
        onClick={() =>
          props.setEditorState((s) => ({
            ...s,
            saveStatus: "DIRTY",
            focus: { blockIndex: 0, contentIndex: 0, cursorPosition: 3 },
          }))
        }
        type="button"
      />
    ),
  };
});

let observedState: LetterEditorState | null = null;
const StateProbe = () => {
  observedState = useManagedLetterEditorContext().editorState;
  return null;
};

describe("ManagedLetterEditor autolagring", () => {
  afterEach(() => {
    vi.restoreAllMocks();
    observedState = null;
  });

  test("lagrer gjeldende markørposisjon i state når autolagring starter", async () => {
    vi.useFakeTimers();
    const brev = brevResponse();
    vi.mocked(brevQueries.oppdaterBrevtekst).mockResolvedValue(brev);

    const queryClient = new QueryClient();
    const { getByTestId } = render(
      <QueryClientProvider client={queryClient}>
        <ManagedLetterEditorContextProvider brev={brev}>
          <ManagedLetterEditor brev={brev} error={false} freeze={false} />
          <StateProbe />
        </ManagedLetterEditorContextProvider>
      </QueryClientProvider>,
    );

    // Brukeren redigerer (state får cursorPosition 3), og flytter deretter
    // markøren med piltaster — noe som ikke oppdaterer editorState.focus.
    act(() => {
      getByTestId("rediger").click();
    });
    expect(observedState?.focus.cursorPosition).toBe(3);

    // Simuler at markøren nå står på offset 7 i DOM.
    const textNode = document.createTextNode("tekst som er lang nok");
    document.body.append(textNode);
    const range = document.createRange();
    range.setStart(textNode, 7);
    range.collapse(true);
    const selection = globalThis.getSelection();
    selection?.removeAllRanges();
    selection?.addRange(range);

    await act(async () => {
      await vi.advanceTimersByTimeAsync(AUTOSAVE_TIMER);
    });

    // State skal ha fersk markørposisjon (7), ikke den utdaterte (3).
    expect(observedState?.focus.cursorPosition).toBe(7);
    expect(observedState?.saveStatus).not.toBe("DIRTY");
    vi.useRealTimers();
  });
});
