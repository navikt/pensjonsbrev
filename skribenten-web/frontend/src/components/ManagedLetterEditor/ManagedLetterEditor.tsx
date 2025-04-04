import { useMutation } from "@tanstack/react-query";
import { useSearch } from "@tanstack/react-router";
import type { AxiosError } from "axios";
import { useEffect } from "react";

import { oppdaterBrevtekst } from "~/api/brev-queries";
import Actions from "~/Brevredigering/LetterEditor/actions";
import { LetterEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { applyAction } from "~/Brevredigering/LetterEditor/lib/actions";
import { getCursorOffset } from "~/Brevredigering/LetterEditor/services/caretUtils";
import { useManagedLetterEditorContext } from "~/components/ManagedLetterEditor/ManagedLetterEditorContext";
import type { BrevResponse } from "~/types/brev";

/**
 * Wrapper av <LetterEditor /> som håndterer lagring av brevtekst.
 *
 * <ManagedLetterEditor /> krever at har <ManagedLetterEditorContextProvider /> som parent.
 */
const ManagedLetterEditor = (props: { brev: BrevResponse; freeze: boolean; error: boolean }) => {
  const { editorState, setEditorState, onSaveSuccess: onSaveSuccess } = useManagedLetterEditorContext();

  const showDebug = useSearch({
    strict: false,
    select: (search: { debug?: string | boolean }) => search?.["debug"] === "true" || search?.["debug"] === true,
  });

  const { mutate, isError, isPending } = useMutation<BrevResponse, AxiosError>({
    mutationFn: () => {
      applyAction(Actions.cursorPosition, setEditorState, getCursorOffset());
      return oppdaterBrevtekst(props.brev.info.id, editorState.redigertBrev);
    },
    onSuccess: (response) => onSaveSuccess(response),
  });

  useEffect(() => {
    const timoutId = setTimeout(() => {
      if (editorState.isDirty) {
        mutate();
      }
    }, 5000);
    return () => clearTimeout(timoutId);
  }, [editorState.isDirty, editorState.redigertBrev, mutate]);

  useEffect(() => {
    if (editorState.redigertBrevHash !== props.brev.redigertBrevHash) {
      setEditorState((previousState) => ({
        ...previousState,
        redigertBrev: props.brev.redigertBrev,
        redigertBrevHash: props.brev.redigertBrevHash,
      }));
    }
  }, [props.brev.redigertBrev, props.brev.redigertBrevHash, editorState.redigertBrevHash, setEditorState]);

  return (
    <LetterEditor
      editorHeight={"var(--main-page-content-height)"}
      editorState={editorState}
      error={props.error || isError}
      freeze={props.freeze || isPending}
      setEditorState={setEditorState}
      showDebug={showDebug}
    />
  );
};

export default ManagedLetterEditor;
