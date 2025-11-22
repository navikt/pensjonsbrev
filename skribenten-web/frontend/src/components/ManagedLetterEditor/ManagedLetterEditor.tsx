import { useMutation } from "@tanstack/react-query";
import type { AxiosError } from "axios";
import { useEffect } from "react";

import { oppdaterBrevtekst } from "~/api/brev-queries";
import Actions from "~/Brevredigering/LetterEditor/actions";
import { LetterEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { getCursorOffset } from "~/Brevredigering/LetterEditor/services/caretUtils";
import { useManagedLetterEditorContext } from "~/components/ManagedLetterEditor/ManagedLetterEditorContext";
import type { BrevResponse } from "~/types/brev";
import type { EditedLetter } from "~/types/brevbakerTypes";

/**
 * Wrapper av <LetterEditor /> som håndterer lagring av brevtekst.
 *
 * <ManagedLetterEditor /> krever at har <ManagedLetterEditorContextProvider /> som parent.
 */
const ManagedLetterEditor = (props: { brev: BrevResponse; freeze: boolean; error: boolean; showDebug?: boolean }) => {
  const { editorState, setEditorState, onSaveSuccess: onSaveSuccess } = useManagedLetterEditorContext();

  const { mutate, isError } = useMutation<BrevResponse, AxiosError, EditedLetter>({
    mutationFn: (redigertBrev: EditedLetter) => {
      setEditorState((previousState) => ({
        ...Actions.cursorPosition(previousState, getCursorOffset()),
        saveStatus: "SAVE_PENDING",
      }));
      return oppdaterBrevtekst(props.brev.info.id, redigertBrev);
    },
    onSuccess: (response) => onSaveSuccess(response),
  });

  useEffect(() => {
    const timoutId = setTimeout(() => {
      if (editorState.saveStatus === "DIRTY") {
        mutate(editorState.redigertBrev);
      }
    }, 5000);
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
      editorHeight={"var(--main-page-content-height)"}
      editorState={editorState}
      error={props.error || isError}
      freeze={props.freeze}
      setEditorState={setEditorState}
      showDebug={props.showDebug ?? false}
    />
  );
};

export default ManagedLetterEditor;
