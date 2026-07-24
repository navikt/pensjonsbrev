import { useMutation } from "@tanstack/react-query";
import { type AxiosError } from "axios";
import { type Dispatch, type SetStateAction } from "react";

import { oppdaterBrev } from "~/api/brev-queries";
import {
  createLetterSnapshot,
  createSaksbehandlerValgEndretHistoryEntry,
  type LetterSnapshot,
} from "~/Brevredigering/LetterEditor/history";
import { type LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { type BrevResponse, type OppdaterBrevRequest, type SaksbehandlerValg } from "~/types/brev";
import { type EditedLetter } from "~/types/brevbakerTypes";

type SaveSuccessOptions = {
  createHistoryEntry?: (
    previousState: LetterEditorState,
    response: BrevResponse,
  ) => ReturnType<typeof createSaksbehandlerValgEndretHistoryEntry>;
};

export type OppdaterBrevMutationVariables = OppdaterBrevRequest & {
  historySnapshot?: LetterSnapshot;
  /**
   * Defaults to `false`: autosaving a tekstvalg/overstyring change (or a dirty letter, via
   * `saveDirtyLetter`) must never release the caseworker's reservation lock on the letter.
   * Pass `true` explicitly for a final "done editing" submit that should release the lock
   * (e.g. `brev.$brevId.tsx`'s "Fortsett" button, which reuses this same mutation for its
   * final submit — unlike attestering, which submits via a separate `attesterBrev` mutation).
   */
  frigiReservasjon?: boolean;
};

/**
 * Shared autosave plumbing for tekstvalg/overstyring changes and dirty-letter saves.
 *
 * Passes `frigiReservasjon: false` to `oppdaterBrev` by default — autosaving a tekstvalg/overstyring
 * change or a dirty letter must never release the caseworker's reservation lock on the letter.
 * (This fixes a prior drift where `brev.$brevId.tsx`'s autosave omitted `frigiReservasjon`; the
 * `oppdaterBrev` API wrapper defaults it to `true` client-side, which released the lock on every
 * autosave.) Callers that reuse this mutation for a final submit (releasing the lock intentionally)
 * must pass `frigiReservasjon: true` explicitly via the mutate variables.
 */
export function useOppdaterBrevAutosave({
  saksId,
  brevId,
  setEditorState,
  onSaveSuccess,
  onAfterSave,
}: {
  saksId: string;
  brevId: number;
  setEditorState: Dispatch<SetStateAction<LetterEditorState>>;
  onSaveSuccess: (response: BrevResponse, options?: SaveSuccessOptions) => void;
  onAfterSave?: (response: BrevResponse, responseWasApplied: boolean) => void;
}) {
  const oppdaterBrevMutation = useMutation<BrevResponse, AxiosError, OppdaterBrevMutationVariables>({
    mutationFn: (values) => {
      // Mark the editor as saving so onSaveSuccess will apply the response
      // (it ignores responses while the editor is DIRTY).
      setEditorState((previousState) => ({ ...previousState, saveStatus: "SAVE_PENDING" }));
      return oppdaterBrev({
        saksId: Number.parseInt(saksId, 10),
        brevId,
        frigiReservasjon: values.frigiReservasjon ?? false,
        request: {
          redigertBrev: values.redigertBrev,
          saksbehandlerValg: values.saksbehandlerValg,
        },
      });
    },
    onSuccess: (response, variables) => {
      const historySnapshot = variables.historySnapshot;

      onSaveSuccess(
        response,
        historySnapshot
          ? {
              createHistoryEntry: () =>
                createSaksbehandlerValgEndretHistoryEntry(historySnapshot, createLetterSnapshot(response)),
            }
          : undefined,
      );

      // The editor may have gone DIRTY while the request was in flight (user typed);
      // onSaveSuccess discards the response in that case, so callers must know whether it was applied.
      let responseWasApplied = true;
      setEditorState((current) => {
        responseWasApplied = current.saveStatus !== "DIRTY";
        return current;
      });
      onAfterSave?.(response, responseWasApplied);
    },
    onError: () => setEditorState((s) => ({ ...s, saveStatus: "DIRTY" })),
  });

  const saveDirtyLetter = (state: { redigertBrev: EditedLetter; saksbehandlerValg: SaksbehandlerValg }) =>
    oppdaterBrev({
      saksId: Number.parseInt(saksId, 10),
      brevId,
      frigiReservasjon: false,
      request: {
        redigertBrev: state.redigertBrev,
        saksbehandlerValg: state.saksbehandlerValg,
      },
    });

  return { oppdaterBrevMutation, saveDirtyLetter };
}
