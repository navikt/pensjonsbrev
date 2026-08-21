import Actions from "~/Brevredigering/LetterEditor/actions";
import { LetterEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { useManagedLetterEditorContext } from "~/components/ManagedLetterEditor/ManagedLetterEditorContext";
import TilbakestillMalModal from "~/components/TilbakestillMalModal";
import { type BrevResponse } from "~/types/brev";
import { type Redigeringsflate } from "~/utils/editorTracking";

/**
 * Redigeringsflaten for selve brevet.
 *
 * Autolagringen bor i <ManagedLetterEditorContextProvider />, ikke her, slik at den overlever at
 * denne komponenten avmonteres når saksbehandler bytter til et vedlegg.
 */
const ManagedLetterEditor = (props: {
  brev: BrevResponse;
  freeze: boolean;
  error: boolean;
  showDebug?: boolean;
  redigeringsflate: Redigeringsflate;
}) => {
  const { editorState, setEditorState, lagringFeilet } = useManagedLetterEditorContext();

  return (
    <LetterEditor
      editorState={editorState}
      error={props.error || lagringFeilet}
      freeze={props.freeze}
      redigeringsflate={props.redigeringsflate}
      renderTilbakestillModal={({ åpen, onClose }) => (
        <TilbakestillMalModal
          brevId={props.brev.info.id}
          onClose={onClose}
          resetEditor={(brevResponse) => setEditorState(Actions.create(brevResponse))}
          åpen={åpen}
        />
      )}
      setEditorState={setEditorState}
      showDebug={props.showDebug ?? false}
    />
  );
};

export default ManagedLetterEditor;
