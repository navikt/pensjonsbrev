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
import { addHistoryEntry, type HistoryEntry } from "~/Brevredigering/LetterEditor/history";
import { useEditorAutosave } from "~/Brevredigering/LetterEditor/hooks/useEditorAutosave";
import { type LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { useRedigeringsflate } from "~/Brevredigering/LetterEditor/RedigeringsflateContext";
import { getCursorOffset } from "~/Brevredigering/LetterEditor/services/caretUtils";
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
  saveLetterOperation: (
    operation: (state: LetterEditorState & { redigertBrev: EditedLetter }) => Promise<BrevResponse>,
    options?: SaveSuccessOptions,
  ) => Promise<BrevResponse>;

  /** Whether autosaving the letter has failed. */
  saveFailed: boolean;

  saveNow: () => Promise<void>;
  resetLetter: () => Promise<void>;
  resetting: boolean;

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

const applySavedResponse = (
  state: LetterEditorState,
  response: BrevResponse,
  options?: SaveSuccessOptions,
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

/**
 * Autosave lives in this provider so it survives when `ManagedLetterEditor`
 * unmounts while switching to an attachment. If autosave lived in the editor,
 * unmounting would clean up the autosave effect and cancel a pending debounce,
 * potentially leaving letter changes unsaved.
 */
export const ManagedLetterEditorContextProvider = (props: { brev: BrevResponse; children: ReactNode }) => {
  const queryClient = useQueryClient();
  const redigeringsflate = useRedigeringsflate();
  const savedBrev = useRef(props.brev);
  const observedBrev = useRef(props.brev);
  const saveErrorResetRef = useRef<(() => void) | null>(null);

  const registerSaveErrorReset = useCallback((reset: (() => void) | null) => {
    saveErrorResetRef.current = reset;
  }, []);

  const onSaved = (response: BrevResponse) => {
    savedBrev.current = response;
    queryClient.setQueryData(getBrev.queryKey(response.info.id), response);
    queryClient.setQueryData(attesteringBrevKeys.id(response.info.id), response);
    const pdfQuery = redigeringsflate === "attestant-redigering" ? hentPdfForAttestering : hentPdfForBrev;
    void queryClient.resetQueries({ queryKey: pdfQuery.queryKey(response.info.id) });
  };

  const { editorState, setEditorState, saveFailed, savePendingChanges, saveWith, reset, resetting } =
    useEditorAutosave<BrevResponse>({
      initialState: Actions.create(props.brev),
      save: (state) => {
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

        if (isEqual(stateWithCursor.saksbehandlerValg, savedBrev.current.saksbehandlerValg)) {
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
      applyResponse: (state, response) => applySavedResponse(state, response, { preserveUnchangedValg: true }),
      onSaved,
    });

  const resetLetter = () =>
    reset(async () => {
      const response = await tilbakestillBrev(props.brev.info.id);
      onSaved(response);
      void queryClient.invalidateQueries({ queryKey: redigerbareVedleggKeys.brev(response.info.id, redigeringsflate) });
      return Actions.create(response);
    });

  const saveLetterOperation = useCallback<ManagedLetterEditorContextValue["saveLetterOperation"]>(
    (operation, options) =>
      saveWith({
        save: (state) => operation({ ...state, redigertBrev: requireLetterDocument(state.redigertBrev) }),
        applyResponse: (state, response) => applySavedResponse(state, response, options),
      }),
    [saveWith],
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
        saveLetterOperation,
        saveFailed: saveFailed,
        saveNow: savePendingChanges,
        resetLetter,
        resetting,
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
