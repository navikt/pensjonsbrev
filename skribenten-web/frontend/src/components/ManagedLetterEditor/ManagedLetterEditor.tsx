import { useMutation } from "@tanstack/react-query";
import { type AxiosError } from "axios";
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
      setEditorState(() => ({
        ...stateWithCursor,
        saveStatus: "SAVE_PENDING",
      }));
      return (
        props.saveDirtyLetter?.(stateWithCursor) ?? oppdaterBrevtekst(props.brev.info.id, stateWithCursor.redigertBrev)
      );
    },
    onSuccess: (response) => onSaveSuccess(response),
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
      }));
    }
  }, [
    props.brev.redigertBrev,
    props.brev.redigertBrevHash,
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
