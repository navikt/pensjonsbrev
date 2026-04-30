import { useMutation, useQuery } from "@tanstack/react-query";
import { type AxiosError } from "axios";
import { useEffect, useState } from "react";

import { getDiff, oppdaterBrevtekst } from "~/api/brev-queries";
import Actions from "~/Brevredigering/LetterEditor/actions";
import { LetterEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { LetterViewer } from "~/Brevredigering/LetterEditor/LetterViewer";
import { getCursorOffset } from "~/Brevredigering/LetterEditor/services/caretUtils";
import { useManagedLetterEditorContext } from "~/components/ManagedLetterEditor/ManagedLetterEditorContext";
import { type BrevResponse } from "~/types/brev";
import { type EditedLetter } from "~/types/brevbakerTypes";

/**
 * Wrapper av <LetterEditor /> som håndterer lagring av brevtekst.
 *
 * <ManagedLetterEditor /> krever at har <ManagedLetterEditorContextProvider /> som parent.
 */
const ManagedLetterEditor = (props: { brev: BrevResponse; freeze: boolean; error: boolean; showDebug?: boolean }) => {
  const { editorState, setEditorState, onSaveSuccess } = useManagedLetterEditorContext();

  const [showDiff, setShowDiff] = useState(false);

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

  const diff = useQuery({
    queryKey: getDiff.queryKey(editorState.info.id),
    queryFn: () => getDiff.queryFn(editorState.info.id, editorState.redigertBrev),
    refetchInterval: 1000,
    enabled: showDiff,
  }).data;

  return (
    <>
      <LetterEditor
        diff={diff}
        editorState={editorState}
        error={props.error || isError}
        freeze={props.freeze}
        setEditorState={setEditorState}
        setShowDiff={setShowDiff}
        showDebug={props.showDebug ?? false}
        showDiff={showDiff}
      />
      {showDiff && diff && (
        <div
          style={{
            position: "fixed",
            top: 0,
            left: "calc(50vw + 553px)",
            width: "720px",
            height: "100vh",
            zIndex: 10,
            background: "white",
            borderLeft: "1px solid #c6c2bf",
            boxShadow: "-4px 0 16px rgba(0,0,0,0.08)",
            overflowY: "auto",
          }}
        >
          <LetterViewer
            diff={diff}
            letter={diff.rendretBrev}
            showDiff
            spraak={editorState.info.spraak}
          />
        </div>
      )}
    </>
  );
};

export default ManagedLetterEditor;
