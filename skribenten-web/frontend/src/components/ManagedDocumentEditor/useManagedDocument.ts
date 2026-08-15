import { useMutation } from "@tanstack/react-query";
import { type AxiosError } from "axios";
import { useEffect, useRef } from "react";

import { AUTOSAVE_TIMER } from "../ManagedLetterEditor/autosave_timer";

export type SaveStatus = "DIRTY" | "SAVE_PENDING" | "SAVED";

/**
 * Generic autosave engine, decoupled from any specific document type. The caller owns the editor
 * session state and supplies a TanStack mutationFn plus lifecycle callbacks; this hook watches
 * `saveStatus`/`content`, debounces, and persists DIRTY content. It deliberately knows nothing about
 * any specific document, response shape, or query caches — those belong to the caller.
 */
export function useManagedDocument<TDoc, TResponse>(args: {
  content: TDoc;
  saveStatus: SaveStatus;
  /** Persist the current document content. Called when state is DIRTY. */
  mutationFn: (doc: TDoc) => Promise<TResponse>;
  /** Build the content to persist from the current content (e.g. stamp the cursor position). */
  prepareForSave?: (content: TDoc) => TDoc;
  onSaveStart: () => void;
  onSaveSuccess: (response: TResponse) => void;
  onSaveError: () => void;
}) {
  const { content, saveStatus, mutationFn, prepareForSave, onSaveStart, onSaveSuccess, onSaveError } = args;

  // Keep latest callbacks/values in refs so the debounce effect does not re-subscribe on every render.
  const latest = useRef({ mutationFn, prepareForSave, onSaveStart, onSaveSuccess, onSaveError });
  latest.current = { mutationFn, prepareForSave, onSaveStart, onSaveSuccess, onSaveError };

  // The freshest content/saveStatus, so an unmount flush captures edits made after the last render's
  // debounce was scheduled. Mutated every render; read only in the unmount cleanup.
  const stateRef = useRef({ content, saveStatus });
  stateRef.current = { content, saveStatus };

  const { mutate, isError } = useMutation<TResponse, AxiosError, TDoc>({
    mutationFn: (doc) => {
      latest.current.onSaveStart();
      return latest.current.mutationFn(doc);
    },
    onSuccess: (response) => latest.current.onSaveSuccess(response),
    onError: () => latest.current.onSaveError(),
  });

  const mutateRef = useRef(mutate);
  mutateRef.current = mutate;

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

  // Flush on unmount (e.g. switching to another document tab): if there are unsaved edits when the
  // editor unmounts, save them immediately instead of dropping them with the cleared debounce timer.
  useEffect(
    () => () => {
      const { content: latestContent, saveStatus: latestStatus } = stateRef.current;
      if (latestStatus === "DIRTY") {
        const prepared = latest.current.prepareForSave ? latest.current.prepareForSave(latestContent) : latestContent;
        mutateRef.current(prepared);
      }
    },
    [],
  );

  return { isError };
}
