import { useMutation } from "@tanstack/react-query";
import { type AxiosError } from "axios";
import { useCallback, useEffect, useRef } from "react";

import { AUTOSAVE_TIMER } from "~/components/ManagedLetterEditor/autosave_timer";

export type SaveStatus = "DIRTY" | "SAVE_PENDING" | "SAVED";

export type DokumentLagring = {
  lagringFeilet: boolean;
  /** Saves all pending edits and rejects if saving fails. */
  lagreNaa: () => Promise<void>;
  /** Pauses autosave while the provided operation runs(arbeid), after waiting for any active save to finish. */
  medLagringPaaPause: <T>(arbeid: () => Promise<T>) => Promise<T>;
};

/**
 * Generic autosave, decoupled from any specific document type. The caller owns the editor
 * session state and supplies a TanStack mutationFn plus lifecycle callbacks; this hook watches
 * `saveStatus`/`content`, debounces, and persists DIRTY content. It deliberately knows nothing about
 * any specific document, response shape, or query caches — those belong to the caller.
 */
export function useDocumentAutosave<TDoc, TResponse>(args: {
  content: TDoc;
  saveStatus: SaveStatus;
  mutationFn: (doc: TDoc) => Promise<TResponse>;
  onSaveStart: () => void;
  onSaveSuccess: (response: TResponse) => void;
  onSaveError: () => void;
}): DokumentLagring {
  const { content, saveStatus, mutationFn, onSaveStart, onSaveSuccess, onSaveError } = args;

  // Keep the latest callbacks without restarting the debounce effect.
  const latest = useRef({ mutationFn, onSaveStart, onSaveSuccess, onSaveError });
  latest.current = { mutationFn, onSaveStart, onSaveSuccess, onSaveError };

  // Keep the latest content/status available to queued saves and unmount cleanup.
  const stateRef = useRef({ content, saveStatus });
  stateRef.current = { content, saveStatus };

  // Prevent automatic retries of the exact payload that just failed.
  const feiletInnholdRef = useRef<TDoc | null>(null);
  const pausetRef = useRef(false);

  // Serialize saves so an older request cannot finish after and overwrite a newer one.
  const koeRef = useRef<Promise<void>>(Promise.resolve());

  const { mutateAsync, isError } = useMutation<TResponse, AxiosError, TDoc>({
    mutationFn: (doc) => {
      latest.current.onSaveStart();
      return latest.current.mutationFn(doc);
    },
    onSuccess: (response) => {
      feiletInnholdRef.current = null;
      latest.current.onSaveSuccess(response);
    },
    onError: (_feil, feiletInnhold) => {
      feiletInnholdRef.current = feiletInnhold;
      latest.current.onSaveError();
    },
  });

  const mutateAsyncRef = useRef(mutateAsync);
  mutateAsyncRef.current = mutateAsync;

  const skalLagre = (doc: TDoc, status: SaveStatus) =>
    !pausetRef.current && status === "DIRTY" && doc !== feiletInnholdRef.current;

  // Queue a save using the latest content when its turn starts.
  const koeLagring = useCallback((eksplisitt: boolean) => {
    const tur = koeRef.current.then(async () => {
      const { content: sisteInnhold, saveStatus: sisteStatus } = stateRef.current;

      // Explicit saves may retry content that previously failed.
      const skal = eksplisitt ? !pausetRef.current && sisteStatus === "DIRTY" : skalLagre(sisteInnhold, sisteStatus);
      if (skal) {
        await mutateAsyncRef.current(sisteInnhold);
      }
    });

    // Keep the queue usable even if one save fails.
    koeRef.current = tur.then(
      () => undefined,
      () => undefined,
    );
    return tur;
  }, []);

  // Explicit saves propagate failures to the caller.
  const lagreNaa = useCallback(async () => {
    await koeLagring(true);
    while (stateRef.current.saveStatus === "DIRTY") {
      await koeLagring(true);
    }
  }, [koeLagring]);

  const medLagringPaaPause = useCallback(async <T>(arbeid: () => Promise<T>): Promise<T> => {
    pausetRef.current = true;
    try {
      await koeRef.current;
      return await arbeid();
    } finally {
      pausetRef.current = false;
    }
  }, []);

  useEffect(() => {
    const timeoutId = setTimeout(() => {
      if (skalLagre(content, saveStatus)) {
        void koeLagring(false).catch(() => undefined);
      }
    }, AUTOSAVE_TIMER);
    return () => clearTimeout(timeoutId);
  }, [saveStatus, content, koeLagring]);

  // Save dirty content immediately when this editor session unmounts.
  useEffect(
    () => () => {
      const { content: sisteInnhold, saveStatus: sisteStatus } = stateRef.current;
      if (skalLagre(sisteInnhold, sisteStatus)) {
        void koeLagring(false).catch(() => undefined);
      }
    },
    [koeLagring],
  );

  return { lagringFeilet: isError, lagreNaa: lagreNaa, medLagringPaaPause: medLagringPaaPause };
}