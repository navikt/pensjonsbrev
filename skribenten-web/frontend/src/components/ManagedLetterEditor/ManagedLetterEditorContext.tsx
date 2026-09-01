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
import { getCursorOffset } from "~/Brevredigering/LetterEditor/services/caretUtils";
import { AUTOSAVE_TIMER } from "~/components/ManagedLetterEditor/autosave_timer";
import { type BrevResponse } from "~/types/brev";
import { type EditedDocument, type EditedLetter } from "~/types/brevbakerTypes";
import { type Redigeringsflate } from "~/utils/editorTracking";

type SaveSuccessOptions = {
  createHistoryEntry?: (previousState: LetterEditorState, response: BrevResponse) => HistoryEntry | null;
};

interface ManagedLetterEditorContextValue {
  editorState: LetterEditorState;

  /** Letter-specific view of the edited document for consumers that need `sakspart` or `signatur`. */
  redigertBrev: EditedLetter;

  setEditorState: Dispatch<SetStateAction<LetterEditorState>>;
  onSaveSuccess: (response: BrevResponse, options?: SaveSuccessOptions) => void;

  /** Whether autosaving the letter has failed. */
  lagringFeilet: boolean;

  registrerNullstillLagringsfeil: (nullstill: (() => void) | null) => void;
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
 * unmounts while switching to a vedlegg. If autosave lived in the editor,
 * unmounting would clean up the autosave effect and cancel a pending debounce,
 * potentially leaving letter changes unsaved.
 */
export const ManagedLetterEditorContextProvider = (props: {
  brev: BrevResponse;
  redigeringsflate: Redigeringsflate;
  children: ReactNode;
}) => {
  const queryClient = useQueryClient();
  const [editorState, setEditorState] = useState<LetterEditorState>(Actions.create(props.brev));
  const nullstillLagringsfeilFraRutenRef = useRef<(() => void) | null>(null);

  const registrerNullstillLagringsfeil = useCallback((nullstill: (() => void) | null) => {
    nullstillLagringsfeilFraRutenRef.current = nullstill;
  }, []);

  const onSaveSuccess = useCallback(
    (response: BrevResponse, options?: SaveSuccessOptions) => {
      queryClient.setQueryData(getBrev.queryKey(response.info.id), response);
      queryClient.setQueryData(attesteringBrevKeys.id(response.info.id), response);
      const pdfQuery = props.redigeringsflate === "attestant-redigering" ? hentPdfForAttestering : hentPdfForBrev;
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
          saksbehandlerValg: response.saksbehandlerValg,
          info: response.info,
          saveStatus: "SAVED",
          history: resolveHistoryAfterSave(previousState, response, historyEntry),
        };
      });
    },
    [queryClient, props.brev.info.id, props.redigeringsflate],
  );

  const redigertBrev = requireLetterDocument(editorState.redigertBrev);

  const {
    mutate: lagreBrevtekst,
    isError: lagringFeilet,
    reset: nullstillLagringsfeil,
  } = useMutation<BrevResponse, AxiosError, LetterEditorState>({
    mutationFn: (state) => {
      const stateWithCursor = Actions.cursorPosition(state, getCursorOffset());
      const redigertBrevMedMarkoer = requireLetterDocument(stateWithCursor.redigertBrev);

      setEditorState((previousState) => ({ ...previousState, saveStatus: "SAVE_PENDING" }));

      // Autosave must never release the user's reservation on the letter.
      if (props.redigeringsflate === "attestant-redigering") {
        return lagreAttestertBrevtekst({
          saksId: String(stateWithCursor.info.saksId),
          brevId: props.brev.info.id,
          redigertBrev: redigertBrevMedMarkoer,
          frigiReservasjon: false,
        });
      }

      if (isEqual(stateWithCursor.saksbehandlerValg, props.brev.saksbehandlerValg)) {
        return oppdaterBrevtekst({
          brevId: props.brev.info.id,
          redigertBrev: redigertBrevMedMarkoer,
          frigiReservasjon: false,
        });
      }

      // Save the full letter when tekstvalg has changed
      return oppdaterBrev({
        saksId: stateWithCursor.info.saksId,
        brevId: stateWithCursor.info.id,
        frigiReservasjon: false,
        request: {
          redigertBrev: redigertBrevMedMarkoer,
          saksbehandlerValg: stateWithCursor.saksbehandlerValg,
        },
      });
    },
    onSuccess: (response) => onSaveSuccess(response),
    onError: () => setEditorState((s) => ({ ...s, saveStatus: "DIRTY" })),
  });

  useEffect(() => {
    const timeoutId = setTimeout(() => {
      if (editorState.saveStatus === "DIRTY") {
        nullstillLagringsfeil();
        nullstillLagringsfeilFraRutenRef.current?.();
        lagreBrevtekst(editorState);
      }
    }, AUTOSAVE_TIMER);

    return () => clearTimeout(timeoutId);
  }, [
    editorState.saveStatus,
    editorState.redigertBrev,
    editorState.saksbehandlerValg,
    lagreBrevtekst,
    nullstillLagringsfeil,
  ]);

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
        lagringFeilet: lagringFeilet,
        registrerNullstillLagringsfeil: registrerNullstillLagringsfeil,
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
