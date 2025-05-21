import { useQueryClient } from "@tanstack/react-query";
import { createContext, useCallback, useContext, useState } from "react";

import { getBrev } from "~/api/brev-queries";
import { hentPdfForBrev } from "~/api/sak-api-endpoints";
import Actions from "~/Brevredigering/LetterEditor/actions";
import type { LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import type { BrevResponse } from "~/types/brev";

const ManagedLetterEditorContext = createContext<{
  editorState: LetterEditorState;
  setEditorState: React.Dispatch<React.SetStateAction<LetterEditorState>>;
  onSaveSuccess: (response: BrevResponse) => void;
}>({
  /*
    casten er for at vi skal slippe å null-håndtere denne i komponenter som bruker denne
    Contexten skal i praksis alltid være satt når den brukes. Dersom den ikke er satt er det en programmeringsfeil
    */
  editorState: {} as LetterEditorState,
  setEditorState: () => {},
  onSaveSuccess: () => {},
});

export const ManagedLetterEditorContextProvider = (props: { brev: BrevResponse; children: React.ReactNode }) => {
  const queryClient = useQueryClient();
  const [editorState, setEditorState] = useState<LetterEditorState>(Actions.create(props.brev));

  const onSaveSuccess = useCallback(
    (response: BrevResponse) => {
      queryClient.setQueryData(getBrev.queryKey(response.info.id), response);
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

export const useManagedLetterEditorContext = () => useContext(ManagedLetterEditorContext);
