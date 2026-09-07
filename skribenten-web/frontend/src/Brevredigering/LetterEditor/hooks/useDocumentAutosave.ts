import { useMutation } from "@tanstack/react-query";
import { type AxiosError } from "axios";
import { useCallback, useEffect, useRef } from "react";

import { AUTOSAVE_TIMER } from "~/components/ManagedLetterEditor/autosave_timer";

export type SaveStatus = "DIRTY" | "SAVE_PENDING" | "SAVED";

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
  const activeSaveRef = useRef<Promise<TResponse> | null>(null);

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

  // Queue a save using the latest document when its turn starts.
  const queueSave = useCallback((explicit: boolean) => {
    const enqueuedSave = saveQueueRef.current.then(async () => {
      const { document: latestDocument, saveStatus: latestStatus } = autosaveStateRef.current;

      // Explicit saves may retry a document that previously failed.
      const isEligibleForSave = explicit
        ? !pausedRef.current && latestStatus === "DIRTY"
        : shouldSave(latestDocument, latestStatus);
      if (isEligibleForSave) {
        const save = performSaveRef.current(latestDocument);
        activeSaveRef.current = save;
        try {
          await save;
        } finally {
          activeSaveRef.current = null;
        }
      }
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
    await activeSaveRef.current;
    await queueSave(true);
    while (autosaveStateRef.current.saveStatus === "DIRTY") {
      await queueSave(true);
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
