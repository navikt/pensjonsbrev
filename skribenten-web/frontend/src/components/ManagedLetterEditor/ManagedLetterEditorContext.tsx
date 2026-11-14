import { useQueryClient } from "@tanstack/react-query";
import isEqual from "lodash/isEqual";
import {
  createContext,
  type Dispatch,
  type ReactNode,
  type SetStateAction,
  useCallback,
  useContext,
  useEffect,
  useRef,
  useState,
} from "react";

import {
  attesteringBrevKeys,
  getBrev,
  lagreAttestertBrevtekst,
  oppdaterBrev,
  oppdaterBrevtekst,
  tilbakestillBrev,
} from "~/api/brev-queries";
import { redigerbareVedleggKeys } from "~/api/redigerbareVedlegg-endpoints";
import { hentPdfForAttestering, hentPdfForBrev } from "~/api/sak-api-endpoints";
import Actions from "~/Brevredigering/LetterEditor/actions";
import { isLetterDocument, normalizeDocumentForComparison } from "~/Brevredigering/LetterEditor/actions/common";
import {
  addHistoryEntry,
  createLetterSnapshot,
  createSaksbehandlerValgEndretHistoryEntry,
  type HistoryEntry,
  snapshotPendingEdit,
} from "~/Brevredigering/LetterEditor/history";
import { useEditorAutosave } from "~/Brevredigering/LetterEditor/hooks/useEditorAutosave";
import { remapFocus } from "~/Brevredigering/LetterEditor/model/remapFocus";
import { type Focus, type LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { useRedigeringsflate } from "~/Brevredigering/LetterEditor/RedigeringsflateContext";
import { getCursorOffset } from "~/Brevredigering/LetterEditor/services/caretUtils";
import { type BrevResponse, type SaksbehandlerValg } from "~/types/brev";
import { type EditedDocument, type EditedLetter } from "~/types/brevbakerTypes";

import { SAKSBEHANDLERVALG_TIMER } from "./autosave_timer";

type ApplySavedResponseOptions = {
  createHistoryEntry?: (previousState: LetterEditorState, response: BrevResponse) => HistoryEntry | null;
  preserveUnchangedValg?: boolean;
  merge?: boolean;
};

interface ManagedLetterEditorContextValue {
  editorState: LetterEditorState;

  /** Letter-specific view of the edited document for consumers that need `sakspart` or `signatur`. */
  redigertBrev: EditedLetter;

  setEditorState: Dispatch<SetStateAction<LetterEditorState>>;
  updateSaksbehandlerValg: (valg: SaksbehandlerValg, debounce: boolean) => void;
  /** From a choice change until its merged letter is applied (or the merge fails); the letter text is frozen meanwhile. */
  awaitingValgMerge: boolean;
  saveLetterOperation: (
    operation: (state: LetterEditorState & { redigertBrev: EditedLetter }) => Promise<BrevResponse>,
    options?: ApplySavedResponseOptions,
  ) => Promise<BrevResponse>;

  /** Whether autosaving the letter has failed. */
  saveFailed: boolean;

  savePendingChanges: () => Promise<void>;
  resetLetter: () => Promise<void>;
  resetting: boolean;

  /** The route registers how to clear its own submit-error state so the autosave can reset it before retrying. */
  registerSaveErrorReset: (reset: (() => void) | null) => void;

  /** The route registers how to discard its pending form values when the letter is reset to the template. */
  registerLetterResetHandler: (handler: ((response: BrevResponse) => void) | null) => void;
}

const requireLetterDocument = (document: EditedDocument): EditedLetter => {
  if (!isLetterDocument(document)) {
    throw new Error("ManagedLetterEditorContextProvider received a non-letter document");
  }
  return document;
};

const resolveHistoryAfterSave = (
  previousState: LetterEditorState,
  response: BrevResponse,
  historyEntry: HistoryEntry | null | undefined,
): LetterEditorState["history"] => {
  if (historyEntry != null) {
    return addHistoryEntry(previousState.history, historyEntry);
  }

  const redigertBrevUnchanged = isEqual(
    normalizeDocumentForComparison(previousState.redigertBrev),
    normalizeDocumentForComparison(response.redigertBrev),
  );

  return redigertBrevUnchanged ? previousState.history : { entries: [], entryPointer: -1 };
};

const ManagedLetterEditorContext = createContext<ManagedLetterEditorContextValue | null>(null);

const applySavedResponse = (
  state: LetterEditorState,
  response: BrevResponse,
  options?: ApplySavedResponseOptions,
): LetterEditorState => ({
  ...state,
  redigertBrev: response.redigertBrev,
  redigertBrevHash: response.redigertBrevHash,
  saksbehandlerValg:
    options?.preserveUnchangedValg && isEqual(state.saksbehandlerValg, response.saksbehandlerValg)
      ? state.saksbehandlerValg
      : response.saksbehandlerValg,
  info: response.info,
  history: resolveHistoryAfterSave(state, response, options?.createHistoryEntry?.(state, response)),
});

export const ManagedLetterEditorContextProvider = (props: { brev: BrevResponse; children: ReactNode }) => {
  const queryClient = useQueryClient();
  const redigeringsflate = useRedigeringsflate();
  const savedBrev = useRef(props.brev);
  const observedBrev = useRef(props.brev);
  const saveErrorResetRef = useRef<(() => void) | null>(null);
  const letterResetHandlerRef = useRef<((response: BrevResponse) => void) | null>(null);
  // Persisted choices do not imply their merged letter was applied: a newer revision may have discarded it.
  const needsFullMerge = useRef(false);
  // Positional text edits cannot cross a pending template merge, so the text stays frozen until the merge is applied.
  const [awaitingValgMerge, setAwaitingValgMerge] = useState(false);

  const registerSaveErrorReset = useCallback((reset: (() => void) | null) => {
    saveErrorResetRef.current = reset;
  }, []);

  const registerLetterResetHandler = useCallback((handler: ((response: BrevResponse) => void) | null) => {
    letterResetHandlerRef.current = handler;
  }, []);

  const requestFullMerge = useCallback(() => {
    needsFullMerge.current = true;
    setAwaitingValgMerge(true);
  }, []);

  const completeFullMerge = useCallback(() => {
    needsFullMerge.current = false;
    setAwaitingValgMerge(false);
  }, []);

  const onSaved = (response: BrevResponse) => {
    savedBrev.current = response;
    queryClient.setQueryData(getBrev.queryKey(response.info.id), response);
    queryClient.setQueryData(attesteringBrevKeys.id(response.info.id), response);
    const pdfQuery = redigeringsflate === "attestant-redigering" ? hentPdfForAttestering : hentPdfForBrev;
    void queryClient.resetQueries({ queryKey: pdfQuery.queryKey(response.info.id) });
  };

  const applyMergedResponse = (state: LetterEditorState, response: BrevResponse): LetterEditorState => {
    if (!needsFullMerge.current) return applySavedResponse(state, response, { preserveUnchangedValg: true });
    const snapshot = createLetterSnapshot(response);
    const toMerged = (focus: Focus, from: EditedDocument) => remapFocus(focus, from, snapshot.redigertBrev);
    const history = {
      ...state.history,
      entries: state.history.entries.map((entry, index) => {
        if (entry.type === "PATCH") return entry;
        // The current history boundary now has a rendered snapshot, including after undo/redo.
        if (index === state.history.entryPointer) {
          return entry.type === "DOCUMENT_SNAPSHOT"
            ? { ...entry, after: snapshot, afterFocus: toMerged(entry.afterFocus, entry.after.redigertBrev) }
            : { ...entry, after: snapshot };
        }
        if (index === state.history.entryPointer + 1) {
          return entry.type === "DOCUMENT_SNAPSHOT"
            ? { ...entry, before: snapshot, beforeFocus: toMerged(entry.beforeFocus, entry.before.redigertBrev) }
            : { ...entry, before: snapshot };
        }
        return entry;
      }),
    };
    completeFullMerge();
    return {
      ...applySavedResponse(state, response, { preserveUnchangedValg: true }),
      focus: toMerged(state.focus, state.redigertBrev),
      history,
    };
  };

  const {
    editorState,
    setEditorState: updateEditorState,
    saveFailed,
    savePendingChanges,
    saveWith,
    reset,
    resetting,
    scheduleSave,
  } = useEditorAutosave<BrevResponse>({
    initialState: Actions.create(props.brev),
    save: async (state) => {
      saveErrorResetRef.current?.();
      const stateWithCursor = Actions.cursorPosition(state, getCursorOffset());
      const letterWithCursor = requireLetterDocument(stateWithCursor.redigertBrev);

      // Autosave must never release the user's reservation on the letter.
      if (redigeringsflate === "attestant-redigering") {
        return lagreAttestertBrevtekst({
          saksId: String(stateWithCursor.info.saksId),
          brevId: props.brev.info.id,
          redigertBrev: letterWithCursor,
          frigiReservasjon: false,
        });
      }

      if (!needsFullMerge.current && isEqual(stateWithCursor.saksbehandlerValg, savedBrev.current.saksbehandlerValg)) {
        return oppdaterBrevtekst({
          brevId: props.brev.info.id,
          redigertBrev: letterWithCursor,
          frigiReservasjon: false,
        });
      }

      requestFullMerge();
      try {
        return await oppdaterBrev({
          saksId: stateWithCursor.info.saksId,
          brevId: stateWithCursor.info.id,
          frigiReservasjon: false,
          request: {
            redigertBrev: letterWithCursor,
            saksbehandlerValg: stateWithCursor.saksbehandlerValg,
          },
        });
      } catch (error) {
        // Unfreeze so the user can keep editing; the merge is retried with the next save.
        setAwaitingValgMerge(false);
        throw error;
      }
    },
    applyLatestResponse: applyMergedResponse,
    onSaved,
  });

  const setEditorState = useCallback<Dispatch<SetStateAction<LetterEditorState>>>(
    (updater) =>
      updateEditorState((previous) => {
        const next = typeof updater === "function" ? updater(previous) : updater;
        if (next === previous) return next;
        if (next.saveStatus === "DIRTY" && next.saksbehandlerValg !== previous.saksbehandlerValg) {
          needsFullMerge.current = true;
        }
        if (
          next.history.entries === previous.history.entries &&
          next.history.entryPointer !== previous.history.entryPointer
        ) {
          const entry = previous.history.entries[Math.max(previous.history.entryPointer, next.history.entryPointer)];
          if (entry && entry.type !== "PATCH") {
            requestFullMerge();
            scheduleSave(SAKSBEHANDLERVALG_TIMER);
          }
        }
        const entry = next.history.entries[next.history.entryPointer];
        if (
          needsFullMerge.current &&
          next.history !== previous.history &&
          entry?.type === "PATCH" &&
          next.history.entries !== previous.history.entries
        ) {
          return {
            ...next,
            history: addHistoryEntry(
              previous.history,
              snapshotPendingEdit(
                { ...previous, redigertBrev: requireLetterDocument(previous.redigertBrev) },
                { ...next, redigertBrev: requireLetterDocument(next.redigertBrev) },
                entry,
              ),
            ),
          };
        }
        return next;
      }),
    [updateEditorState, scheduleSave, requestFullMerge],
  );

  const updateSaksbehandlerValg = useCallback(
    (valg: SaksbehandlerValg, debounce: boolean) => {
      let changed = false;
      setEditorState((state) => {
        if (isEqual(state.saksbehandlerValg, valg)) return state;
        changed = true;
        const next = { ...state, saksbehandlerValg: structuredClone(valg), saveStatus: "DIRTY" as const };
        return {
          ...next,
          history: addHistoryEntry(
            state.history,
            createSaksbehandlerValgEndretHistoryEntry(
              { ...state, redigertBrev: requireLetterDocument(state.redigertBrev) },
              { ...next, redigertBrev: requireLetterDocument(next.redigertBrev) },
            ),
          ),
        };
      });
      if (changed) {
        requestFullMerge();
        scheduleSave(debounce ? SAKSBEHANDLERVALG_TIMER : 0);
      }
    },
    [setEditorState, scheduleSave, requestFullMerge],
  );

  const resetLetter = () =>
    reset(async () => {
      try {
        const response = await tilbakestillBrev(props.brev.info.id);
        needsFullMerge.current = false;
        onSaved(response);
        letterResetHandlerRef.current?.(response);
        void queryClient.invalidateQueries({
          queryKey: redigerbareVedleggKeys.brev(response.info.id, redigeringsflate),
        });
        return Actions.create(response);
      } finally {
        // A failed reset keeps `needsFullMerge`, so the next save still merges the pending choices.
        setAwaitingValgMerge(false);
      }
    });

  const saveLetterOperation = useCallback<ManagedLetterEditorContextValue["saveLetterOperation"]>(
    (operation, options) =>
      saveWith({
        save: async (state) => {
          let response: BrevResponse;
          try {
            response = await operation({ ...state, redigertBrev: requireLetterDocument(state.redigertBrev) });
          } catch (error) {
            // This operation replaced any scheduled choice save, so do not keep the text frozen for it.
            setAwaitingValgMerge(false);
            throw error;
          }
          if (options?.merge) needsFullMerge.current = true;
          setEditorState((current) =>
            current.saksbehandlerValg === state.saksbehandlerValg
              ? { ...current, saksbehandlerValg: response.saksbehandlerValg }
              : current,
          );
          return response;
        },
        applyLatestResponse: (state, response) => {
          if (options?.merge) completeFullMerge();
          return applySavedResponse(state, response, options);
        },
      }),
    [saveWith, setEditorState, completeFullMerge],
  );

  useEffect(() => {
    if (observedBrev.current === props.brev) return;
    observedBrev.current = props.brev;
    if (editorState.saveStatus === "SAVED" && savedBrev.current.redigertBrevHash !== props.brev.redigertBrevHash) {
      savedBrev.current = props.brev;
      void reset(async () => applySavedResponse(editorState, props.brev));
    }
  }, [editorState, props.brev, reset]);

  return (
    <ManagedLetterEditorContext.Provider
      value={{
        editorState: editorState,
        redigertBrev: requireLetterDocument(editorState.redigertBrev),
        setEditorState: setEditorState,
        updateSaksbehandlerValg,
        awaitingValgMerge,
        saveLetterOperation,
        saveFailed: saveFailed,
        savePendingChanges,
        resetLetter,
        resetting,
        registerSaveErrorReset: registerSaveErrorReset,
        registerLetterResetHandler,
      }}
    >
      {props.children}
    </ManagedLetterEditorContext.Provider>
  );
};

export const useManagedLetterEditorContext = (): ManagedLetterEditorContextValue => {
  const context = useContext(ManagedLetterEditorContext);
  if (!context) {
    throw new Error("useManagedLetterEditorContext must be used within a <ManagedLetterEditorContextProvider>");
  }
  return context;
};

export default ManagedLetterEditorContext;
