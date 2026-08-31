import Actions from "~/Brevredigering/LetterEditor/actions";
import { LetterEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { useManagedLetterEditorContext } from "~/components/ManagedLetterEditor/ManagedLetterEditorContext";
import TilbakestillMalModal from "~/components/TilbakestillMalModal";
import { type BrevResponse } from "~/types/brev";
import { type Redigeringsflate } from "~/utils/editorTracking";

/**
 * Renders the editor for the letter.
 *
 * Autosave lives in <ManagedLetterEditorContextProvider /> so it stays active when this component
 * unmounts while switching to a vedlegg. If autosave lived here, unmounting could cancel a pending
 * debounced save and leave letter changes unsaved.
 */
const ManagedLetterEditor = (props: {
  brev: BrevResponse;
  freeze: boolean;
  error: boolean;
  redigeringsflate: Redigeringsflate;
  kanTilbakestille: boolean;
  showDebug?: boolean;
}) => {
  const { editorState, setEditorState, lagringFeilet } = useManagedLetterEditorContext();

  return (
    <LetterEditor
      editorState={editorState}
      error={props.error || lagringFeilet}
      freeze={props.freeze}
      redigeringsflate={props.redigeringsflate}
      renderTilbakestillModal={
        props.kanTilbakestille
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
