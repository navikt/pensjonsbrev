import { useMutation } from "@tanstack/react-query";
import { type AxiosError } from "axios";
import { useEffect, useRef } from "react";

import { AUTOSAVE_TIMER } from "../ManagedLetterEditor/autosave_timer";
import { type DocumentPersistence, type SaveStatus } from "./DocumentPersistence";

/**
 * Generic autosave engine, decoupled from any specific document type. The caller owns the editor
 * session state; this hook watches its `saveStatus`/`content`, debounces, persists DIRTY content
 * through the persistence adapter, and reports the saved result back via callbacks.
 *
 * It deliberately knows nothing about LetterEditorState, BrevResponse, saksbehandlerValg, or query
 * caches — those belong to the caller and the DocumentPersistence adapter.
 */
export function useManagedDocument<TDoc, TResponse>(args: {
  content: TDoc;
  saveStatus: SaveStatus;
  persistence: DocumentPersistence<TDoc, TResponse>;
  /** Build the content to persist from the current content (e.g. stamp the cursor position). */
  prepareForSave?: (content: TDoc) => TDoc;
  onSaveStart: () => void;
  onSaveSuccess: (response: TResponse) => void;
  onSaveError: () => void;
}) {
  const { content, saveStatus, persistence, prepareForSave, onSaveStart, onSaveSuccess, onSaveError } = args;

  // Keep latest callbacks/values in refs so the debounce effect does not re-subscribe on every render.
  const latest = useRef({ prepareForSave, onSaveStart, onSaveSuccess, onSaveError, persistence });
  latest.current = { prepareForSave, onSaveStart, onSaveSuccess, onSaveError, persistence };

  const { mutate, isError } = useMutation<TResponse, AxiosError, TDoc>({
    mutationFn: (doc) => {
      onSaveStart();
      return persistence.save(doc);
    },
    onSuccess: (response) => {
      persistence.onSaved?.(response);
      onSaveSuccess(response);
    },
    onError: () => onSaveError(),
  });

  // Autosave: when the content becomes DIRTY, persist after a debounce.
  useEffect(() => {
    const timeoutId = setTimeout(() => {
      if (saveStatus === "DIRTY") {
        const prepared = latest.current.prepareForSave ? latest.current.prepareForSave(content) : content;
        mutate(prepared);
      }
    }, AUTOSAVE_TIMER);
    return () => clearTimeout(timeoutId);
  }, [saveStatus, content, mutate]);

  return { isError };
}
