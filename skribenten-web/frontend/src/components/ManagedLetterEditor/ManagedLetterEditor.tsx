import { LetterEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { useManagedLetterEditorContext } from "~/components/ManagedLetterEditor/ManagedLetterEditorContext";
import TilbakestillMalModal from "~/components/TilbakestillMalModal";

/**
 * Renders the editor for the letter.
 *
 * The provider preserves the letter's editor state and autosave instance across document switches.
 * The document coordinator waits for pending saves before switching to an attachment.
 */
const ManagedLetterEditor = (props: { freeze: boolean; error: boolean; canReset?: boolean; showDebug?: boolean }) => {
  const { editorState, setEditorState, saveFailed, resetLetter, resetting } = useManagedLetterEditorContext();

  return (
    <LetterEditor
      editorState={editorState}
      error={props.error || saveFailed}
      freeze={props.freeze || resetting}
      renderTilbakestillModal={
        props.canReset
          ? ({ open, onClose }) => <TilbakestillMalModal onClose={onClose} resetLetter={resetLetter} åpen={open} />
          : undefined
      }
      setEditorState={setEditorState}
      showDebug={props.showDebug ?? false}
    />
  );
};

export default ManagedLetterEditor;
