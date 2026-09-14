import { useMutation } from "@tanstack/react-query";
import { type AxiosError } from "axios";
import { useCallback, useEffect, useRef } from "react";

import { AUTOSAVE_TIMER } from "~/components/ManagedLetterEditor/autosave_timer";

export type SaveStatus = "DIRTY" | "SAVE_PENDING" | "SAVED";

/**
 * How many times `saveNow` may re-save before giving up and leaving the rest to the debounced
 * autosave. A re-save only happens when the document actually changed while the previous save ran,
 * so this cap is a safety net against callers that hand the hook a new document object on every
 * save rather than a limit on normal editing.
 */
const MAX_SAVE_NOW_ATTEMPTS = 5;

export type DocumentSaver = {
  saveFailed: boolean;
  /** Saves all pending edits and rejects if saving fails. */
  saveNow: () => Promise<void>;
  /** Pauses autosave while the provided operation runs, after waiting for any active save to finish. */
  withSavingPaused: <T>(operation: () => Promise<T>) => Promise<T>;
};

/**
 * Generic autosave, decoupled from any specific document type. The caller owns the editor
 * session state and supplies a TanStack mutationFn plus lifecycle callbacks; this hook watches
 * `saveStatus`/`document`, debounces, and persists DIRTY documents. It deliberately knows nothing
 * about any specific document, response shape, or query caches — those belong to the caller.
 *
 * Caller contract: `document` is compared by reference, never by value. The caller must therefore
 * keep the same object whenever the content is unchanged — i.e. return the previous state object
 * from its state updates instead of rebuilding an equal one. Two things depend on it:
 *
 * - a document that just failed to save is not retried automatically, so a new-but-equal object
 *   slips past that guard and re-sends a save that is expected to fail again;
 * - `saveNow` re-saves only while the document keeps changing, so a caller that swaps the object on
 *   every save would keep saving until {@link MAX_SAVE_NOW_ATTEMPTS} stops it.
 *
 * `ManagedAttachmentEditor` satisfies this by comparing with `isEqual(normalizeDocumentForComparison(...))`
 * before replacing its state, both after a save and when refreshing from the server.
 */
export function useDocumentAutosave<TDoc, TResponse>(args: {
  document: TDoc;
  saveStatus: SaveStatus;
  mutationFn: (doc: TDoc) => Promise<TResponse>;
  onSaveStart: () => void;
  onSaveSuccess: (response: TResponse) => void;
  onSaveError: () => void;
}): DocumentSaver {
  const { document, saveStatus, mutationFn, onSaveStart, onSaveSuccess, onSaveError } = args;

  // Keep the latest callbacks without restarting the debounce effect.
  const callbacks = useRef({ mutationFn, onSaveStart, onSaveSuccess, onSaveError });
  callbacks.current = { mutationFn, onSaveStart, onSaveSuccess, onSaveError };

  // Keep the latest document/status available to queued saves and unmount cleanup.
  const autosaveStateRef = useRef({ document, saveStatus });
  autosaveStateRef.current = { document, saveStatus };

  // Prevent automatic retries of the exact document that just failed.
  const failedDocumentRef = useRef<TDoc | null>(null);
  const pausedRef = useRef(false);

  // Serialize saves so an older request cannot finish after and overwrite a newer one.
  const saveQueueRef = useRef<Promise<void>>(Promise.resolve());

  // The document targeted by the send that is currently in flight (already dequeued and calling
  // the backend). Used by `saveNow` to join a same-document send instead of enqueueing a duplicate.
  const inFlightSaveRef = useRef<{ document: TDoc; promise: Promise<TDoc | null> } | null>(null);

  // The document targeted by a `saveNow`-initiated send that has been enqueued but may not have
  // started yet. Needed in addition to `inFlightSaveRef` so two `saveNow` calls issued back to
  // back for the same document (before either has reached the front of the queue) join each other
  // rather than both enqueueing a send.
  const pendingExplicitSaveRef = useRef<{ document: TDoc; promise: Promise<TDoc | null> } | null>(null);

  const { mutateAsync, isError } = useMutation<TResponse, AxiosError, TDoc>({
    mutationFn: (doc) => {
      autosaveStateRef.current.saveStatus = "SAVE_PENDING";
      callbacks.current.onSaveStart();
      return callbacks.current.mutationFn(doc);
    },
    onSuccess: (response, savedDocument) => {
      failedDocumentRef.current = null;
      autosaveStateRef.current.saveStatus = autosaveStateRef.current.document === savedDocument ? "SAVED" : "DIRTY";
      callbacks.current.onSaveSuccess(response);
    },
    onError: (_error, failedDocument) => {
      failedDocumentRef.current = failedDocument;
      autosaveStateRef.current.saveStatus = "DIRTY";
      callbacks.current.onSaveError();
    },
  });

  const performSaveRef = useRef(mutateAsync);
  performSaveRef.current = mutateAsync;

  const shouldSave = (doc: TDoc, status: SaveStatus) =>
    !pausedRef.current && status === "DIRTY" && doc !== failedDocumentRef.current;

  // Queue a save using the latest document when its turn starts. Resolves with the document that
  // was sent, or null when there was nothing to save by the time the turn came around.
  const queueSave = useCallback((explicit: boolean): Promise<TDoc | null> => {
    const enqueuedSave = saveQueueRef.current.then(async () => {
      const { document: latestDocument, saveStatus: latestStatus } = autosaveStateRef.current;

      // Explicit saves may retry a document that previously failed.
      const isEligibleForSave = explicit
        ? !pausedRef.current && latestStatus === "DIRTY"
        : shouldSave(latestDocument, latestStatus);
      if (!isEligibleForSave) return null;

      const send = performSaveRef.current(latestDocument);
      // `inFlight.promise` is only ever consumed by a `saveNow` call that opts in via `outstanding`
      // above (autosave never reads it). Give it its own rejection handler so an unobserved failure
      // here doesn't surface as an unhandled rejection when no such call happens to join it.
      const inFlightPromise = send.then(() => latestDocument);
      inFlightPromise.catch(() => undefined);
      const inFlight = { document: latestDocument, promise: inFlightPromise };
      inFlightSaveRef.current = inFlight;
      try {
        await send;
      } finally {
        if (inFlightSaveRef.current === inFlight) inFlightSaveRef.current = null;
      }
      return latestDocument;
    });

    // Keep the queue usable even if one save fails.
    saveQueueRef.current = enqueuedSave.then(
      () => undefined,
      () => undefined,
    );
    return enqueuedSave;
  }, []);

  // Explicit saves propagate failures to the caller.
  const saveNow = useCallback(async () => {
    for (let attempt = 0; attempt < MAX_SAVE_NOW_ATTEMPTS; attempt++) {
      const currentDocument = autosaveStateRef.current.document;

      // If a save already in flight or already enqueued by `saveNow` targets the exact document we
      // would send, join it instead of enqueueing a duplicate: a caller that lands here right after
      // that send's failure must see the same rejection and must not immediately resend a document
      // that is expected to fail again. A send that targets an older, superseded document is left
      // alone — its failure must not block saving the newer document below.
      const outstanding =
        pendingExplicitSaveRef.current?.document === currentDocument
          ? pendingExplicitSaveRef.current.promise
          : inFlightSaveRef.current?.document === currentDocument
            ? inFlightSaveRef.current.promise
            : null;

      let savedDocument: TDoc | null;
      if (outstanding) {
        savedDocument = await outstanding;
      } else {
        const send = queueSave(true);
        const pendingExplicit = { document: currentDocument, promise: send };
        pendingExplicitSaveRef.current = pendingExplicit;
        // Only used to clear the slot; the real result/rejection is propagated below via `send`.
        void send
          .catch(() => undefined)
          .finally(() => {
            if (pendingExplicitSaveRef.current === pendingExplicit) pendingExplicitSaveRef.current = null;
          });
        savedDocument = await send;
      }
      if (savedDocument === null) return;

      // Only re-save when the document actually moved on while the save was running. Looping on
      // `saveStatus` alone would depend on the caller collapsing to SAVED, and a caller that never
      // does would keep this loop — and the backend — spinning forever.
      const { document: latestDocument, saveStatus: latestStatus } = autosaveStateRef.current;
      if (latestStatus !== "DIRTY" || latestDocument === savedDocument) return;
    }
  }, [queueSave]);

  const withSavingPaused = useCallback(async <T>(operation: () => Promise<T>): Promise<T> => {
    pausedRef.current = true;
    try {
      await saveQueueRef.current;
      return await operation();
    } finally {
      pausedRef.current = false;
    }
  }, []);

  useEffect(() => {
    const timeoutId = setTimeout(() => {
      if (shouldSave(document, saveStatus)) {
        void queueSave(false).catch(() => undefined);
      }
    }, AUTOSAVE_TIMER);
    return () => clearTimeout(timeoutId);
  }, [saveStatus, document, queueSave]);

  // Save dirty document immediately when this editor session unmounts.
  useEffect(
    () => () => {
      const { document: latestDocument, saveStatus: latestStatus } = autosaveStateRef.current;
      if (shouldSave(latestDocument, latestStatus)) {
        void queueSave(false).catch(() => undefined);
      }
    },
    [queueSave],
  );

  return { saveFailed: isError, saveNow: saveNow, withSavingPaused: withSavingPaused };
}
