import { useMutation } from "@tanstack/react-query";
import { type AxiosError } from "axios";

import { oppdaterBrev } from "~/api/brev-queries";
import {
  createLetterSnapshot,
  createSaksbehandlerValgEndretHistoryEntry,
  type LetterSnapshot,
} from "~/Brevredigering/LetterEditor/history";
import { useManagedLetterEditorContext } from "~/components/ManagedLetterEditor/ManagedLetterEditorContext";
import { type BrevResponse, type OppdaterBrevRequest } from "~/types/brev";

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
 * tilsvarende for feilvisning) og sender det inn i <ManagedLetterEditor />.
 */
export function useOppdaterBrevAutosave(saksId: string) {
  const { saveLetterOperation } = useManagedLetterEditorContext();
  const oppdaterBrevMutation = useMutation<BrevResponse, AxiosError, OppdaterBrevMutationVariables>({
    mutationFn: (values) => {
      let historySnapshot = values.historySnapshot;
      return saveLetterOperation(
        (state) => {
          if (historySnapshot) historySnapshot = createLetterSnapshot(state);
          return oppdaterBrev({
            saksId: Number.parseInt(saksId, 10),
            brevId: state.info.id,
            frigiReservasjon: values.frigiReservasjon ?? false,
            request: { redigertBrev: state.redigertBrev, saksbehandlerValg: values.saksbehandlerValg },
          });
        },
        historySnapshot
          ? {
              createHistoryEntry: (_state, response) =>
                historySnapshot
                  ? createSaksbehandlerValgEndretHistoryEntry(historySnapshot, createLetterSnapshot(response))
                  : null,
            }
          : undefined,
      );
    },
  });

  return { oppdaterBrevMutation };
}
