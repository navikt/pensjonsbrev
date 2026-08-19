import { useQueryClient } from "@tanstack/react-query";
import isEqual from "lodash/isEqual";
import {
  createContext,
  type Dispatch,
  type ReactNode,
  type SetStateAction,
  useCallback,
  useContext,
  useState,
} from "react";

import { attesteringBrevKeys, getBrev } from "~/api/brev-queries";
import { hentPdfForBrev } from "~/api/sak-api-endpoints";
import Actions from "~/Brevredigering/LetterEditor/actions";
import { normalizeDeletedArrays } from "~/Brevredigering/LetterEditor/actions/common";
import { addHistoryEntry, type HistoryEntry } from "~/Brevredigering/LetterEditor/history";
import { type LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { type BrevResponse } from "~/types/brev";

type SaveSuccessOptions = {
  createHistoryEntry?: (previousState: LetterEditorState, response: BrevResponse) => HistoryEntry | null;
};

interface ManagedLetterEditorContextValue {
  editorState: LetterEditorState;
  setEditorState: Dispatch<SetStateAction<LetterEditorState>>;
  onSaveSuccess: (response: BrevResponse, options?: SaveSuccessOptions) => void;
}

const nullsToUndefined = (obj: unknown) =>
  JSON.parse(JSON.stringify(obj, (_, value) => (value === null ? undefined : value)));

const resolveHistoryAfterSave = (
  previousState: LetterEditorState,
  response: BrevResponse,
  historyEntry: HistoryEntry | null | undefined,
): LetterEditorState["history"] => {
  if (historyEntry != null) {
    return addHistoryEntry(previousState.history, historyEntry);
  }

  const redigertBrevUnchanged = isEqual(
    normalizeDeletedArrays(nullsToUndefined(previousState.redigertBrev)),
    normalizeDeletedArrays(nullsToUndefined(response.redigertBrev)),
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

  return (
    <ManagedLetterEditorContext.Provider
      value={{ editorState: editorState, setEditorState: setEditorState, onSaveSuccess: onSaveSuccess }}
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
