/**
 * Separates editing from persistence. The generic autosave engine (useManagedDocument) owns
 * dirty/save-pending/saved state, autosave timing, focus, and history. A DocumentPersistence
 * adapter owns everything document-specific: which endpoint to call, how to read the saved
 * content and a revision token out of the response, and which query caches to update.
 *
 * Implementations:
 *  - brevPersistence    — the main letter (oppdaterBrevtekst / saveDirtyLetter).
 *  - vedleggPersistence — a redigerbart vedlegg (lagreRedigerbartVedlegg). Wired in Phase 5.
 */

export type SaveStatus = "DIRTY" | "SAVE_PENDING" | "SAVED";

export interface DocumentPersistence<TDoc, TResponse> {
  /**
   * Persist the current document content. Called by the autosave engine when state is DIRTY.
   */
  save: (doc: TDoc) => Promise<TResponse>;

  /**
   * Extract the saved document content from a save response, to sync back into editor state.
   */
  getSavedContent: (response: TResponse) => TDoc;

  /**
   * A token that changes whenever the server-side document changes. Used to detect external
   * updates and to decide whether history can be kept after a save. There is deliberately no
   * shared "hash" abstraction: the brev uses redigertBrevHash, a vedlegg uses a save counter,
   * since the vedlegg response carries no document hash.
   */
  getSavedRevision: (response: TResponse) => string;

  /**
   * Side effects after a successful save (e.g. updating TanStack Query caches). Optional.
   */
  onSaved?: (response: TResponse) => void;
}
