import { type QueryClient } from "@tanstack/react-query";

import { lagreRedigerbartVedlegg, redigerbareVedleggKeys } from "~/api/redigerbareVedlegg-endpoints";
import { hentPdfForBrev } from "~/api/sak-api-endpoints";
import { type BrevResponse, type EditAttachment } from "~/types/brev";

import { type DocumentPersistence } from "./DocumentPersistence";

/**
 * Persistence adapter for a redigerbart vedlegg. Unlike the brev there is no saksbehandlerValg and
 * no document hash, so the save body is just the edited attachment and the revision token is a
 * save counter.
 *
 * The vedlegg content query (redigerbareVedleggKeys.vedlegg) is the source of truth for the editor's
 * content; after a save we write the saved attachment back into it so a remount does not refetch
 * stale content. We also invalidate the vedlegg LIST (the edited title may have changed) and the
 * brev PDF (the vedlegg is part of the rendered letter).
 */
export function createVedleggPersistence(args: {
  saksId: string;
  brevId: number;
  vedleggId: string;
  queryClient: QueryClient;
}): DocumentPersistence<EditAttachment, BrevResponse> & { getSavedRevision: (response: BrevResponse) => string } {
  const { saksId, brevId, vedleggId, queryClient } = args;
  let saveCount = 0;

  return {
    save: (redigertVedlegg) => lagreRedigerbartVedlegg(saksId, brevId, vedleggId, redigertVedlegg),
    // A redigerbart vedlegg response is the parent BrevResponse; the attachment content we saved is
    // what we sent. There is no per-vedlegg payload to read back, so getSavedContent is unused by the
    // vedlegg editor's sync (it syncs from its own state). It is implemented for interface
    // completeness only and should not be relied upon to reflect server-side content.
    getSavedContent: () => {
      throw new Error("vedleggPersistence.getSavedContent: not supported — vedlegg saves echo no content");
    },
    getSavedRevision: () => {
      saveCount += 1;
      return `${vedleggId}:${saveCount}`;
    },
    onSaved: (response) => {
      queryClient.invalidateQueries({ queryKey: redigerbareVedleggKeys.liste(brevId) });
      queryClient.resetQueries({ queryKey: hentPdfForBrev.queryKey(response.info.id) });
    },
  };
}
