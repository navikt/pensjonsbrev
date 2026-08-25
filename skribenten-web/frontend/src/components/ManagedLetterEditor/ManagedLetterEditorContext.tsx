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
  useState,
} from "react";

import { attesteringBrevKeys, getBrev, oppdaterBrev, oppdaterBrevtekst } from "~/api/brev-queries";
import { hentPdfForBrev } from "~/api/sak-api-endpoints";
import Actions from "~/Brevredigering/LetterEditor/actions";
import { isLetterDocument, normalizeDocumentForComparison } from "~/Brevredigering/LetterEditor/actions/common";
import { addHistoryEntry, type HistoryEntry } from "~/Brevredigering/LetterEditor/history";
import { type LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { getCursorOffset } from "~/Brevredigering/LetterEditor/services/caretUtils";
import { AUTOSAVE_TIMER } from "~/components/ManagedLetterEditor/autosave_timer";
import { type BrevResponse } from "~/types/brev";
import { type EditedLetter } from "~/types/brevbakerTypes";

type SaveSuccessOptions = {
  createHistoryEntry?: (previousState: LetterEditorState, response: BrevResponse) => HistoryEntry | null;
};

interface ManagedLetterEditorContextValue {
  editorState: LetterEditorState;
  /**
   * Letter-specific view of `editorState.redigertBrev` for consumers that need `sakspart` or
   * `signatur`. `LetterEditorState` uses the shared `EditedDocument` shape to also support
   * attachments, but this provider is initialized and updated exclusively with letter data.
   */
  redigertBrev: EditedLetter;
  setEditorState: Dispatch<SetStateAction<LetterEditorState>>;
  onSaveSuccess: (response: BrevResponse, options?: SaveSuccessOptions) => void;
  /** Whether the letter's own autosave last failed. */
  lagringFeilet: boolean;
}

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

export const ManagedLetterEditorContextProvider = (props: { brev: BrevResponse; children: ReactNode }) => {
  const queryClient = useQueryClient();
  const [editorState, setEditorState] = useState<LetterEditorState>(Actions.create(props.brev));

  const onSaveSuccess = useCallback(
    (response: BrevResponse, options?: SaveSuccessOptions) => {
      queryClient.setQueryData(getBrev.queryKey(response.info.id), response);
      queryClient.setQueryData(attesteringBrevKeys.id(response.info.id), response);
      //vi resetter queryen slik at når saksbehandler går tilbake til brevbehandler vil det hentes nyeste data
      //istedenfor at saksbehandler ser på cachet versjon uten at dem vet det kommer et ny en
      queryClient.resetQueries({ queryKey: hentPdfForBrev.queryKey(props.brev.info.id) });
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
    [queryClient, props.brev.info.id],
  );

  const redigertBrev = isLetterDocument(editorState.redigertBrev) ? editorState.redigertBrev : props.brev.redigertBrev;

  const { mutate: lagreBrevtekst, isError: lagringFeilet } = useMutation<BrevResponse, AxiosError, LetterEditorState>({
    mutationFn: (state) => {
      const stateWithCursor = Actions.cursorPosition(state, getCursorOffset());
      const redigertBrevMedMarkoer = isLetterDocument(stateWithCursor.redigertBrev)
        ? stateWithCursor.redigertBrev
        : props.brev.redigertBrev;

      setEditorState((previousState) => ({ ...previousState, saveStatus: "SAVE_PENDING" }));

      // Autolagring skal aldri frigi reservasjonen saksbehandler har på brevet.
      if (isEqual(stateWithCursor.saksbehandlerValg, props.brev.saksbehandlerValg)) {
        return oppdaterBrevtekst({
          brevId: props.brev.info.id,
          redigertBrev: redigertBrevMedMarkoer,
          frigiReservasjon: false,
        });
      }
      // Tekstvalg er endret, og de lagres ikke av redigertBrev-endepunktet.
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
        lagreBrevtekst(editorState);
      }
    }, AUTOSAVE_TIMER);
    return () => clearTimeout(timeoutId);
    // Only content changes may restart the debounce; caret/focus activity must not postpone a save.
  }, [editorState.saveStatus, editorState.redigertBrev, editorState.saksbehandlerValg, lagreBrevtekst]);

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
