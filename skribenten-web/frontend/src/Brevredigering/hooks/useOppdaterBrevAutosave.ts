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
import { type BrevResponse, type OppdaterBrevRequest } from "~/types/brev";

type SaveSuccessOptions = {
  createHistoryEntry?: (
    previousState: LetterEditorState,
    response: BrevResponse,
  ) => ReturnType<typeof createSaksbehandlerValgEndretHistoryEntry>;
};

export type OppdaterBrevMutationVariables = OppdaterBrevRequest & {
  historySnapshot?: LetterSnapshot;
  /**
   * Defaults to `false`: å lagre en tekstvalg-/overstyringsendring skal aldri frigi reservasjonen
   * saksbehandler har på brevet. Send `true` eksplisitt for en avsluttende "ferdig"-innsending som
   * skal frigi reservasjonen (f.eks. "Fortsett"-knappen i `brev.$brevId.tsx`, som gjenbruker denne
   * mutasjonen — i motsetning til attestering, som sender inn via en egen `attesterBrev`-mutasjon).
   */
  frigiReservasjon?: boolean;
};

/**
 * Rutens lagring av tekstvalg-/overstyringsendringer og av "ferdig"-innsendingen.
 *
 * Mutasjonen eies av ruten fordi ruten utleder `freeze = oppdaterBrevMutation.isPending` (og
 * tilsvarende for feilvisning) og sender det inn i <ManagedLetterEditor />. Autolagringen av selve
 * brevteksten har derfor sin egen mutasjon inne i <ManagedLetterEditor />: deler de én mutasjon,
 * ville `freeze` slått inn for hvert tastetrykk og låst editoren mens saksbehandler skriver.
 */
export function useOppdaterBrevAutosave({
  saksId,
  brevId,
  setEditorState,
  onSaveSuccess,
}: {
  saksId: string;
  brevId: number;
  setEditorState: Dispatch<SetStateAction<LetterEditorState>>;
  onSaveSuccess: (response: BrevResponse, options?: SaveSuccessOptions) => void;
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
    },
    onError: () => setEditorState((s) => ({ ...s, saveStatus: "DIRTY" })),
  });

  return { oppdaterBrevMutation };
}
