import { useMutation } from "@tanstack/react-query";
import { type AxiosError } from "axios";
import { useCallback, useEffect, useRef } from "react";

import { AUTOSAVE_TIMER } from "~/components/ManagedLetterEditor/autosave_timer";

export type SaveStatus = "DIRTY" | "SAVE_PENDING" | "SAVED";

export type DokumentLagring = {
  lagringFeilet: boolean;
  /**
   * Persist any unsaved edits and resolve once nothing is in flight. Rejects if the save fails, so
   * callers that are about to leave the editing session can stop instead of discarding the edits.
   */
  lagreNaa: () => Promise<void>;
  /**
   * Run `arbeid` with autosave suspended and no save in flight, so a destructive operation
   * (e.g. tilbakestilling) cannot be overtaken by a save that was already on its way.
   */
  medLagringPaaPause: <T>(arbeid: () => Promise<T>) => Promise<T>;
};

/**
 * Generic autosave engine, decoupled from any specific document type. The caller owns the editor
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

  // Keep latest callbacks/values in refs so the debounce effect does not re-subscribe on every render.
  const latest = useRef({ mutationFn, onSaveStart, onSaveSuccess, onSaveError });
  latest.current = { mutationFn, onSaveStart, onSaveSuccess, onSaveError };

  // The freshest content/saveStatus, so an unmount flush captures edits made after the last render's
  // debounce was scheduled. Mutated every render; read only outside the render pass.
  const stateRef = useRef({ content, saveStatus });
  stateRef.current = { content, saveStatus };

  // A failed save leaves the content DIRTY, which would otherwise re-arm the debounce and retry the
  // exact same payload forever. This holds the payload that actually failed — not the current
  // content, which may already have moved on while the failing request was in flight.
  const feiletInnholdRef = useRef<TDoc | null>(null);
  const pausetRef = useRef(false);
  // Saves run strictly one at a time: two PUTs in flight for the same document can be applied by the
  // server in the wrong order, so a slow save must delay the next one rather than overlap it.
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

  /**
   * Queues one save turn. The turn reads the newest content when it finally runs, so edits made
   * while an earlier save was in flight are covered by the next turn instead of a second, competing
   * request — and a turn that has been superseded does nothing.
   */
  const koeLagring = useCallback((eksplisitt: boolean) => {
    const tur = koeRef.current.then(async () => {
      const { content: sisteInnhold, saveStatus: sisteStatus } = stateRef.current;
      // An explicit save deliberately ignores `feiletInnhold`: that guard only exists to stop the
      // automatic retry loop, and a user-initiated save must retry rather than report false success.
      const skal = eksplisitt ? !pausetRef.current && sisteStatus === "DIRTY" : skalLagre(sisteInnhold, sisteStatus);
      if (skal) {
        await mutateAsyncRef.current(sisteInnhold);
      }
    });
    // The queue has to survive a failed turn, so the tail never carries a rejection.
    koeRef.current = tur.then(
      () => undefined,
      () => undefined,
    );
    return tur;
  }, []);

  // Background saves report failure through onSaveError/lagringFeilet; only lagreNaa() propagates it.
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

  // Flush on unmount (e.g. switching to another document): if there are unsaved edits when the
  // editor unmounts, save them immediately instead of dropping them with the cleared debounce timer.
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
