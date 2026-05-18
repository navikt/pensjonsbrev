import { useMutation } from "@tanstack/react-query";
import { type AxiosError } from "axios";
import isEqual from "lodash/isEqual";
import { useEffect } from "react";

import { oppdaterBrevtekst } from "~/api/brev-queries";
import Actions from "~/Brevredigering/LetterEditor/actions";
import { LetterEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { type LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { getCursorOffset } from "~/Brevredigering/LetterEditor/services/caretUtils";
import { useManagedLetterEditorContext } from "~/components/ManagedLetterEditor/ManagedLetterEditorContext";
import { type BrevResponse } from "~/types/brev";

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
  showDebug?: boolean;
  saveDirtyLetter?: (editorState: LetterEditorState) => Promise<BrevResponse>;
}) => {
  const { editorState, setEditorState, onSaveSuccess } = useManagedLetterEditorContext();

  const { mutate, isError } = useMutation<BrevResponse, AxiosError, LetterEditorState>({
    mutationFn: (state) => {
      const stateWithCursor = Actions.cursorPosition(state, getCursorOffset());

      setEditorState((previousState) => ({
        ...previousState,
        saveStatus: "SAVE_PENDING",
      }));
      // oppdaterBrevtekst only saves redigertBrev; tekstvalg changes require saveDirtyLetter.
      if (isEqual(stateWithCursor.saksbehandlerValg, props.brev.saksbehandlerValg)) {
        return oppdaterBrevtekst(props.brev.info.id, stateWithCursor.redigertBrev);
      }
      if (!props.saveDirtyLetter) {
        throw new Error("saveDirtyLetter is required when saksbehandlerValg has changed");
      }
      return props.saveDirtyLetter(stateWithCursor);
    },
    onSuccess: (response) => onSaveSuccess(response),
    onError: () => setEditorState((s) => ({ ...s, saveStatus: "DIRTY" })),
  });

  useEffect(() => {
    const timoutId = setTimeout(() => {
      if (editorState.saveStatus === "DIRTY") {
        mutate(editorState);
      }
    }, AUTOSAVE_TIMER);
    return () => clearTimeout(timoutId);
  }, [editorState.saveStatus, editorState.redigertBrev, mutate]);

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
      setEditorState={setEditorState}
      showDebug={props.showDebug ?? false}
    />
  );
};

export default ManagedLetterEditor;
