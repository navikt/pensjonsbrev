import { useQueryClient } from "@tanstack/react-query";
import type { Dispatch, ReactNode, SetStateAction } from "react";
import { createContext, useCallback, useContext, useState } from "react";

import { attesteringBrevKeys, getBrev } from "~/api/brev-queries";
import { hentPdfForBrev } from "~/api/sak-api-endpoints";
import Actions from "~/Brevredigering/LetterEditor/actions";
import type { LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import type { BrevResponse } from "~/types/brev";

interface ManagedLetterEditorContextValue {
  editorState: LetterEditorState;
  setEditorState: Dispatch<SetStateAction<LetterEditorState>>;
  onSaveSuccess: (response: BrevResponse) => void;
}

const ManagedLetterEditorContext = createContext<ManagedLetterEditorContextValue | null>(null);

export const ManagedLetterEditorContextProvider = (props: { brev: BrevResponse; children: ReactNode }) => {
  const queryClient = useQueryClient();
  const [editorState, setEditorState] = useState<LetterEditorState>(Actions.create(props.brev));

  const onSaveSuccess = useCallback(
    (response: BrevResponse) => {
      queryClient.setQueryData(getBrev.queryKey(response.info.id), response);
      queryClient.setQueryData(attesteringBrevKeys.id(response.info.id), response);
      //vi resetter queryen slik at når saksbehandler går tilbake til brevbehandler vil det hentes nyeste data
      //istedenfor at saksbehandler ser på cachet versjon uten at dem vet det kommer et ny en
      queryClient.resetQueries({ queryKey: hentPdfForBrev.queryKey(props.brev.info.id) });
      setEditorState((previousState) => ({
        ...previousState,
        redigertBrev: response.redigertBrev,
        redigertBrevHash: response.redigertBrevHash,
        saksbehandlerValg: response.saksbehandlerValg,
        info: response.info,
        isDirty: false,
      }));
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
