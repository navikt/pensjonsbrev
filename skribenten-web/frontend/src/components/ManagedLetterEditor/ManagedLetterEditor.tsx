import Actions from "~/Brevredigering/LetterEditor/actions";
import { LetterEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { useManagedLetterEditorContext } from "~/components/ManagedLetterEditor/ManagedLetterEditorContext";
import TilbakestillMalModal from "~/components/TilbakestillMalModal";
import { useAktivtDokument } from "~/components/vedlegg/AktivtDokumentContext";
import { type BrevResponse } from "~/types/brev";

/**
 * Redigeringsflaten for selve brevet.
 *
 * Autolagringen bor i <ManagedLetterEditorContextProvider />, ikke her, slik at den overlever at
 * denne komponenten avmonteres når saksbehandler bytter til et vedlegg.
 */
const ManagedLetterEditor = (props: { brev: BrevResponse; freeze: boolean; error: boolean; showDebug?: boolean }) => {
  const { editorState, setEditorState, lagringFeilet } = useManagedLetterEditorContext();
  const { redigeringsflate, kanTilbakestille } = useAktivtDokument();

  return (
    <LetterEditor
      editorState={editorState}
      error={props.error || lagringFeilet}
      freeze={props.freeze}
      redigeringsflate={redigeringsflate}
      renderTilbakestillModal={
        kanTilbakestille
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
