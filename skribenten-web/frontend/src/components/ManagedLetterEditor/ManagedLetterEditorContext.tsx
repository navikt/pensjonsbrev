import { useMutation, useQueryClient } from "@tanstack/react-query";
import { type AxiosError } from "axios";
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
} from "~/api/brev-queries";
import { hentPdfForAttestering, hentPdfForBrev } from "~/api/sak-api-endpoints";
import Actions from "~/Brevredigering/LetterEditor/actions";
import { isLetterDocument, normalizeDocumentForComparison } from "~/Brevredigering/LetterEditor/actions/common";
import { addHistoryEntry, type HistoryEntry } from "~/Brevredigering/LetterEditor/history";
import { type LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { useRedigeringsflate } from "~/Brevredigering/LetterEditor/RedigeringsflateContext";
import { getCursorOffset } from "~/Brevredigering/LetterEditor/services/caretUtils";
import { AUTOSAVE_TIMER } from "~/components/ManagedLetterEditor/autosave_timer";
import { type BrevResponse } from "~/types/brev";
import { type EditedDocument, type EditedLetter } from "~/types/brevbakerTypes";

type SaveSuccessOptions = {
  createHistoryEntry?: (previousState: LetterEditorState, response: BrevResponse) => HistoryEntry | null;
  preserveUnchangedValg?: boolean;
};

interface ManagedLetterEditorContextValue {
  editorState: LetterEditorState;

  /** Letter-specific view of the edited document for consumers that need `sakspart` or `signatur`. */
  redigertBrev: EditedLetter;

  setEditorState: Dispatch<SetStateAction<LetterEditorState>>;
  onSaveSuccess: (response: BrevResponse, options?: SaveSuccessOptions) => void;

  /** Whether autosaving the letter has failed. */
  saveFailed: boolean;

  /**
   * Saves pending letter edits immediately instead of waiting for the autosave debounce, and
   * rejects if saving fails. Concurrent calls join the save already in flight rather than sending
   * a second one.
   *
   * Resolving means "no save is outstanding from this call", not "every edit is persisted": when
   * nothing is dirty, and when a save is already on the wire, this resolves without sending
   * anything. The letter endpoints carry no version, so a second concurrent PUT could land out of
   * order and persist the older letter; those edits are left to the debounced autosave. Observe
   * `saveStatus` to know when the letter is actually saved. Routing both paths through a single
   * queue — as `useDocumentAutosave` already does for vedlegg — would lift the restriction.
   */
  saveNow: () => Promise<void>;

  /** The route registers how to clear its own submit-error state so the autosave can reset it before retrying. */
  registerSaveErrorReset: (reset: (() => void) | null) => void;
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

/**
 * Autosave lives in this provider so it survives when `ManagedLetterEditor`
 * unmounts while switching to an attachment. If autosave lived in the editor,
 * unmounting would clean up the autosave effect and cancel a pending debounce,
 * potentially leaving letter changes unsaved.
 */
export const ManagedLetterEditorContextProvider = (props: { brev: BrevResponse; children: ReactNode }) => {
  const queryClient = useQueryClient();
  const redigeringsflate = useRedigeringsflate();
  const [editorState, setEditorState] = useState<LetterEditorState>(Actions.create(props.brev));
  const saveErrorResetRef = useRef<(() => void) | null>(null);

  const registerSaveErrorReset = useCallback((reset: (() => void) | null) => {
    saveErrorResetRef.current = reset;
  }, []);

  const onSaveSuccess = useCallback(
    (response: BrevResponse, options?: SaveSuccessOptions) => {
      queryClient.setQueryData(getBrev.queryKey(response.info.id), response);
      queryClient.setQueryData(attesteringBrevKeys.id(response.info.id), response);
      // Reset the query so returning to brevbehandler fetches the latest data
      // instead of silently showing a stale cached version.
      const pdfQuery = redigeringsflate === "attestant-redigering" ? hentPdfForAttestering : hentPdfForBrev;
      queryClient.resetQueries({ queryKey: pdfQuery.queryKey(props.brev.info.id) });
      setEditorState((previousState) => {
        if (previousState.saveStatus === "DIRTY") {
          return previousState;
        }

        const historyEntry = options?.createHistoryEntry?.(previousState, response);

        return {
          ...previousState,
          redigertBrev: response.redigertBrev,
          redigertBrevHash: response.redigertBrevHash,
          // A text save must not reset an unsaved form. A form save must still acknowledge its values.
          saksbehandlerValg:
            options?.preserveUnchangedValg && isEqual(previousState.saksbehandlerValg, response.saksbehandlerValg)
              ? previousState.saksbehandlerValg
              : response.saksbehandlerValg,
          info: response.info,
          saveStatus: "SAVED",
          history: resolveHistoryAfterSave(previousState, response, historyEntry),
        };
      });
    },
    [queryClient, props.brev.info.id, redigeringsflate],
  );

  const redigertBrev = requireLetterDocument(editorState.redigertBrev);

  // Set for every save, whether started by the debounced autosave or by `saveNow`.
  const saveInFlightRef = useRef(false);

  const {
    mutate: saveLetter,
    mutateAsync: saveLetterAsync,
    isError: saveFailed,
    reset: resetSaveError,
  } = useMutation<BrevResponse, AxiosError, LetterEditorState>({
    mutationFn: (state) => {
      const stateWithCursor = Actions.cursorPosition(state, getCursorOffset());
      const letterWithCursor = requireLetterDocument(stateWithCursor.redigertBrev);

      saveInFlightRef.current = true;
      setEditorState((previousState) => ({ ...previousState, saveStatus: "SAVE_PENDING" }));

      // Autosave must never release the user's reservation on the letter.
      if (redigeringsflate === "attestant-redigering") {
        return lagreAttestertBrevtekst({
          saksId: String(stateWithCursor.info.saksId),
          brevId: props.brev.info.id,
          redigertBrev: letterWithCursor,
          frigiReservasjon: false,
        });
      }

      if (isEqual(stateWithCursor.saksbehandlerValg, props.brev.saksbehandlerValg)) {
        return oppdaterBrevtekst({
          brevId: props.brev.info.id,
          redigertBrev: letterWithCursor,
          frigiReservasjon: false,
        });
      }

      // Save the full letter when tekstvalg has changed
      return oppdaterBrev({
        saksId: stateWithCursor.info.saksId,
        brevId: stateWithCursor.info.id,
        frigiReservasjon: false,
        request: {
          redigertBrev: letterWithCursor,
          saksbehandlerValg: stateWithCursor.saksbehandlerValg,
        },
      });
    },
    onSuccess: (response) => onSaveSuccess(response, { preserveUnchangedValg: true }),
    onError: () => setEditorState((s) => ({ ...s, saveStatus: "DIRTY" })),
    onSettled: () => {
      saveInFlightRef.current = false;
    },
  });

  // Keep the latest state available to `saveNow` without rebuilding it on every keystroke.
  const editorStateRef = useRef(editorState);
  editorStateRef.current = editorState;

  // Lets concurrent `saveNow` callers await the same send instead of queueing a duplicate.
  const saveNowRef = useRef<Promise<void> | null>(null);

  const saveNow = useCallback((): Promise<void> => {
    // Join our own explicit save rather than sending a duplicate.
    if (saveNowRef.current) return saveNowRef.current;

    // A save is already on the wire. The letter endpoints carry no version, so adding a second
    // concurrent PUT risks the two landing out of order and persisting the older letter. Leave
    // these edits to the debounced autosave instead.
    if (saveInFlightRef.current) return Promise.resolve();

    // Anything other than DIRTY is either already persisted or has a save in flight that the
    // caller can simply wait out via `saveStatus`.
    if (editorStateRef.current.saveStatus !== "DIRTY") return Promise.resolve();

    resetSaveError();
    saveErrorResetRef.current?.();

    const save = saveLetterAsync(editorStateRef.current)
      .then(() => undefined)
      .finally(() => {
        saveNowRef.current = null;
      });

    saveNowRef.current = save;
    return save;
  }, [saveLetterAsync, resetSaveError]);

  useEffect(() => {
    const timeoutId = setTimeout(() => {
      if (editorState.saveStatus === "DIRTY") {
        resetSaveError();
        saveErrorResetRef.current?.();
        saveLetter(editorState);
      }
    }, AUTOSAVE_TIMER);

    return () => clearTimeout(timeoutId);
  }, [editorState.saveStatus, editorState.redigertBrev, editorState.saksbehandlerValg, saveLetter, resetSaveError]);

  useEffect(() => {
    if (editorState.saveStatus === "SAVED" && editorState.redigertBrevHash !== props.brev.redigertBrevHash) {
      setEditorState((previousState) => ({
        ...previousState,
        redigertBrev: props.brev.redigertBrev,
        redigertBrevHash: props.brev.redigertBrevHash,
        saksbehandlerValg: props.brev.saksbehandlerValg,
      }));
    }
  }, [
    props.brev.redigertBrev,
    props.brev.redigertBrevHash,
    props.brev.saksbehandlerValg,
    editorState.redigertBrevHash,
    editorState.saveStatus,
  ]);

  return (
    <ManagedLetterEditorContext.Provider
      value={{
        editorState: editorState,
        redigertBrev: redigertBrev,
        setEditorState: setEditorState,
        onSaveSuccess: onSaveSuccess,
        saveFailed: saveFailed,
        saveNow: saveNow,
        registerSaveErrorReset: registerSaveErrorReset,
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
