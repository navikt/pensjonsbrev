import { type QueryClient } from "@tanstack/react-query";
import isEqual from "lodash/isEqual";

import { attesteringBrevKeys, getBrev, oppdaterBrevtekst } from "~/api/brev-queries";
import { hentPdfForBrev } from "~/api/sak-api-endpoints";
import { type BrevResponse } from "~/types/brev";
import { type EditedLetter } from "~/types/brevbakerTypes";

import { type DocumentPersistence } from "./DocumentPersistence";

/**
 * Persistence adapter for the main letter. Encapsulates the brev-specific save-path decision
 * (plain text save vs. full save when saksbehandlerValg changed) and the brev query-cache updates.
 *
 * This is the document-specific half of ManagedLetterEditor's save logic; the generic autosave
 * engine lives in useManagedDocument. It is defined here so the adapter contract has a concrete
 * reference implementation; wiring it into ManagedLetterEditor happens as part of the same change.
 */
export function createBrevPersistence(args: {
  brev: BrevResponse;
  queryClient: QueryClient;
  /**
   * Current saksbehandlerValg, used to decide whether a plain text save suffices or a full save
   * (which also persists saksbehandlerValg) is required.
   */
  currentSaksbehandlerValg: BrevResponse["saksbehandlerValg"];
  saveDirtyLetter?: (redigertBrev: EditedLetter) => Promise<BrevResponse>;
}): DocumentPersistence<EditedLetter, BrevResponse> {
  const { brev, queryClient, currentSaksbehandlerValg, saveDirtyLetter } = args;

  return {
    save: (redigertBrev) => {
      // oppdaterBrevtekst only persists redigertBrev; tekstvalg changes require a full save.
      if (isEqual(currentSaksbehandlerValg, brev.saksbehandlerValg)) {
        return oppdaterBrevtekst(brev.info.id, redigertBrev);
      }
      if (!saveDirtyLetter) {
        throw new Error("saveDirtyLetter is required when saksbehandlerValg has changed");
      }
      return saveDirtyLetter(redigertBrev);
    },
    getSavedContent: (response) => response.redigertBrev,
    getSavedRevision: (response) => response.redigertBrevHash,
    onSaved: (response) => {
      queryClient.setQueryData(getBrev.queryKey(response.info.id), response);
      queryClient.setQueryData(attesteringBrevKeys.id(response.info.id), response);
      // Reset the PDF query so that returning to brevbehandler fetches fresh data instead of
      // showing a cached PDF without the saksbehandler knowing a new one is on its way.
      queryClient.resetQueries({ queryKey: hentPdfForBrev.queryKey(response.info.id) });
    },
  };
}
