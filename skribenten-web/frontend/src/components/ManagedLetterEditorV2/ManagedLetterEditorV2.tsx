import { EditorV2 } from "~/Brevredigering/EditorV2/view/EditorV2";
import TilbakestillMalModal from "~/components/TilbakestillMalModal";

import { useManagedLetterEditorContextV2 } from "./ManagedLetterEditorContextV2";

const ManagedLetterEditorV2 = (props: {
  freeze: boolean;
  error: boolean;
  canReset?: boolean;
  showDebug?: boolean;
  diffToggleMenu?: React.ReactNode;
}) => {
  const { editorState, setEditorState, saveFailed, resetLetter, resetting } = useManagedLetterEditorContextV2();

  return (
    <EditorV2
      diffToggleMenu={props.diffToggleMenu}
      editorState={editorState}
      error={props.error || saveFailed}
      freeze={props.freeze || resetting}
      renderTilbakestillModal={
        props.canReset
          ? ({ open, onClose }) => <TilbakestillMalModal onClose={onClose} resetLetter={resetLetter} åpen={open} />
          : undefined
      }
      saksinformasjon={editorState.redigertBrev.saksinformasjon}
      setEditorState={setEditorState}
      showDebug={props.showDebug ?? false}
      signatur={editorState.redigertBrev.signatur}
    />
  );
};

export default ManagedLetterEditorV2;
