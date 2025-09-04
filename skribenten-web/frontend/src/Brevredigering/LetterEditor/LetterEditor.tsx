import "./editor.css";

import { css } from "@emotion/react";
import { Heading } from "@navikt/ds-react";
import { enablePatches } from "immer";
import type { Dispatch, SetStateAction } from "react";
import { createContext, useContext, useRef } from "react";

import { DebugPanel } from "~/Brevredigering/LetterEditor/components/DebugPanel";
import { type CallbackReceiver } from "~/Brevredigering/LetterEditor/lib/actions";
import { EditedLetterTitle } from "~/components/EditedLetterTitle";

import type { TableAction } from "./actions/tableRecipe";
import { patchGeneratingTableReducer } from "./actions/tableReducer";
import { ContentGroup } from "./components/ContentGroup";
import { EditorMenu } from "./components/EditorMenu";
import { SakspartView } from "./components/SakspartView";
import { SignaturView } from "./components/SignaturView";
import { createHistory } from "./history";
import type { LetterEditorState } from "./model/state";
import { useEditorKeyboardShortcuts } from "./utils";

export const LetterEditor = ({
  freeze,
  error,
  editorState,
  setEditorState,
  editorHeight,
  showDebug,
}: {
  freeze: boolean;
  error: boolean;
  editorState: LetterEditorState;
  setEditorState: Dispatch<SetStateAction<LetterEditorState>>;
  editorHeight?: string;
  showDebug: boolean;
}) => {
  const letter = editorState.redigertBrev;
  const blocks = letter.blocks;
  const editorKeyboardShortcuts = useEditorKeyboardShortcuts(editorState, setEditorState);
  enablePatches();

  const historyRef = useRef(createHistory<LetterEditorState>());

  function dispatch(action: TableAction) {
    setEditorState((currentEditorState) => {
      const [next, patches, inversePatches] = patchGeneratingTableReducer(currentEditorState, action);
      if (patches.length > 0) {
        historyRef.current.push({ patches, inversePatches, label: action.type });
      }
      return next;
    });
  }

  function undo() {
    setEditorState((curr) => historyRef.current.undo(curr));
  }

  function redo() {
    setEditorState((curr) => historyRef.current.redo(curr));
  }

  const canUndo = historyRef.current.canUndo();
  const canRedo = historyRef.current.canRedo();

  return (
    <div
      css={css`
        display: flex;
        flex-direction: column;
        align-items: center;
        height: ${editorHeight ?? "auto"};
        overflow-y: auto;
      `}
    >
      <EditorStateContext.Provider value={{ freeze, error, editorState, setEditorState, dispatch }}>
        <div
          css={css`
            position: sticky;
            top: 0;
            width: 100%;
            z-index: 1;
          `}
        >
          <EditorMenu canRedo={canRedo} canUndo={canUndo} redo={redo} undo={undo} />
        </div>
        <div
          className="editor"
          css={css`
            align-self: start;
            max-width: 694px;
            min-width: 480px;
          `}
        >
          <SakspartView
            sakspart={letter.sakspart}
            wrapperStyles={css`
              margin-bottom: 1.88rem;
            `}
          />
          <Heading
            css={css`
              line-height: 30px;
              margin-bottom: 27px;
              letter-spacing: 0.45px;
            `}
            level="3"
            size="medium"
          >
            <EditedLetterTitle title={letter.title} />
          </Heading>
          <div onKeyDown={editorKeyboardShortcuts}>
            {blocks.map((block, blockIndex) => (
              <div className={block.type} key={blockIndex}>
                <ContentGroup literalIndex={{ blockIndex, contentIndex: 0 }} />
              </div>
            ))}
          </div>
          <SignaturView
            signatur={letter.signatur}
            wrapperStyles={css`
              margin-bottom: 48px;
            `}
          />
        </div>
        {showDebug && <DebugPanel />}
      </EditorStateContext.Provider>
    </div>
  );
};

export const EditorStateContext = createContext<{
  freeze: boolean;
  error: boolean;
  editorState: LetterEditorState;
  setEditorState: CallbackReceiver<LetterEditorState>;
  dispatch: (a: TableAction) => void;
}>({
  freeze: false,
  error: false,
  editorState: {} as LetterEditorState,
  setEditorState: () => {},
  dispatch: () => {},
});
export const useEditor = () => {
  return useContext(EditorStateContext);
};
