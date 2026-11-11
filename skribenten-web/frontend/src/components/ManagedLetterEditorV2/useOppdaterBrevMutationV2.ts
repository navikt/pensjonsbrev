import { useMutation } from "@tanstack/react-query";
import { type AxiosError } from "axios";

import { oppdaterBrevV2 } from "~/api/brevV2-queries";
import { type Snapshot, snapshotEntry, snapshotOf } from "~/Brevredigering/EditorV2/state/history";
import { type SaksbehandlerValg } from "~/types/brev";
import { type BrevResponseV2, type LetterV2 } from "~/types/brevV2";

import { useManagedLetterEditorContextV2 } from "./ManagedLetterEditorContextV2";

type Variables = { saksbehandlerValg: SaksbehandlerValg; recordHistory?: boolean; frigiReservasjon?: boolean };

export function useOppdaterBrevMutationV2() {
  const { saveLetterOperation } = useManagedLetterEditorContextV2();
  return useMutation<BrevResponseV2, AxiosError, Variables>({
    mutationFn: ({ saksbehandlerValg, recordHistory, frigiReservasjon = false }) => {
      let before: Snapshot<LetterV2, SaksbehandlerValg> | undefined;
      return saveLetterOperation(
        (state) => {
          before = snapshotOf(state);
          return oppdaterBrevV2({
            saksId: state.info.saksId,
            brevId: state.info.id,
            frigiReservasjon,
            request: { redigertBrev: state.redigertBrev, saksbehandlerValg },
          });
        },
        recordHistory
          ? { createHistoryEntry: (_state, response) => (before ? snapshotEntry(before, response) : null) }
          : undefined,
      );
    },
  });
}
