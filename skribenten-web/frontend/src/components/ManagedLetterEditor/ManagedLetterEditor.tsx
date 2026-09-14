import Actions from "~/Brevredigering/LetterEditor/actions";
import { LetterEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { useManagedLetterEditorContext } from "~/components/ManagedLetterEditor/ManagedLetterEditorContext";
import TilbakestillMalModal from "~/components/TilbakestillMalModal";
import { type BrevResponse } from "~/types/brev";

/**
 * Renders the editor for the letter.
 *
 * Autosave lives in <ManagedLetterEditorContextProvider /> so it stays active when this component
 * unmounts while switching to an attachment. If autosave lived here, unmounting could cancel a pending
 * debounced save and leave letter changes unsaved.
 */
const ManagedLetterEditor = (props: {
  brev: BrevResponse;
  freeze: boolean;
  error: boolean;
  canReset?: boolean;
  showDebug?: boolean;
}) => {
  const { editorState, setEditorState, saveFailed } = useManagedLetterEditorContext();

  return (
    <LetterEditor
      editorState={editorState}
      error={props.error || saveFailed}
      freeze={props.freeze}
      renderTilbakestillModal={
        props.canReset
          ? ({ open, onClose }) => (
              <TilbakestillMalModal
                brevId={props.brev.info.id}
                onClose={onClose}
                resetEditor={(brevResponse) => setEditorState(Actions.create(brevResponse))}
                åpen={open}
              />
            )
          : undefined
      }
      setEditorState={setEditorState}
      showDebug={props.showDebug ?? false}
    />
  );
};

export default ManagedLetterEditor;
