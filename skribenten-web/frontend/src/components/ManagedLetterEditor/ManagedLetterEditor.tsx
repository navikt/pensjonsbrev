import { useMutation } from "@tanstack/react-query";
import { type AxiosError } from "axios";
import isEqual from "lodash/isEqual";
import { useEffect } from "react";

import { lagreAttestertBrevtekst, oppdaterBrev, oppdaterBrevtekst } from "~/api/brev-queries";
import Actions from "~/Brevredigering/LetterEditor/actions";
import { LetterEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { type LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { getCursorOffset } from "~/Brevredigering/LetterEditor/services/caretUtils";
import { useManagedLetterEditorContext } from "~/components/ManagedLetterEditor/ManagedLetterEditorContext";
import { type BrevResponse } from "~/types/brev";
import { type Redigeringsflate } from "~/utils/editorTracking";

import { AUTOSAVE_TIMER } from "./autosave_timer";

/**
 * Wrapper av <LetterEditor /> som håndterer lagring av brevtekst.
 *
 * <ManagedLetterEditor /> krever at har <ManagedLetterEditorContextProvider /> som parent.
 */
const ManagedLetterEditor = (props: {
  brev: BrevResponse;
  freeze: boolean;
  error: boolean;
  resetParentSaveError?: () => void;
  showDebug?: boolean;
  redigeringsflate: Redigeringsflate;
}) => {
  const { editorState, setEditorState, onSaveSuccess } = useManagedLetterEditorContext();
  const { resetParentSaveError } = props;

  const { mutate, isError, reset } = useMutation<BrevResponse, AxiosError, LetterEditorState>({
    mutationFn: (state) => {
      const stateWithCursor = Actions.cursorPosition(state, getCursorOffset());

      setEditorState((previousState) => ({
        ...previousState,
        saveStatus: "SAVE_PENDING",
      }));

      // Autolagring skal aldri frigi reservasjonen saksbehandler har på brevet.
      if (props.redigeringsflate === "attestant-redigering") {
        return lagreAttestertBrevtekst({
          saksId: String(stateWithCursor.info.saksId),
          brevId: props.brev.info.id,
          redigertBrev: stateWithCursor.redigertBrev,
          frigiReservasjon: false,
        });
      }

      if (isEqual(stateWithCursor.saksbehandlerValg, props.brev.saksbehandlerValg)) {
        return oppdaterBrevtekst({
          brevId: props.brev.info.id,
          redigertBrev: stateWithCursor.redigertBrev,
          frigiReservasjon: false,
        });
      }
      // Tekstvalg er endret, og de lagres ikke av redigertBrev-endepunktet.
      return oppdaterBrev({
        saksId: stateWithCursor.info.saksId,
        brevId: stateWithCursor.info.id,
        frigiReservasjon: false,
        request: {
          redigertBrev: stateWithCursor.redigertBrev,
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
        reset();
        resetParentSaveError?.();
        mutate(editorState);
      }
    }, AUTOSAVE_TIMER);
    return () => clearTimeout(timeoutId);
  }, [editorState.saveStatus, editorState.redigertBrev, mutate, reset, resetParentSaveError]);

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
    setEditorState,
    editorState.saveStatus,
  ]);

  return (
    <LetterEditor
      editorState={editorState}
      error={props.error || isError}
      freeze={props.freeze}
      redigeringsflate={props.redigeringsflate}
      setEditorState={setEditorState}
      showDebug={props.showDebug ?? false}
    />
  );
};

export default ManagedLetterEditor;
