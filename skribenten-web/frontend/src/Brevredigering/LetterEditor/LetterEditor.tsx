import "./editor.css";

import { css } from "@emotion/react";
import { Heading } from "@navikt/ds-react";
import { applyPatches } from "immer";
import type { Dispatch, SetStateAction } from "react";
import { createContext, useCallback, useContext } from "react";

import { DebugPanel } from "~/Brevredigering/LetterEditor/components/DebugPanel";
import { type CallbackReceiver } from "~/Brevredigering/LetterEditor/lib/actions";
import { EditedLetterTitle } from "~/components/EditedLetterTitle";

import { ContentGroup } from "./components/ContentGroup";
import { EditorMenu } from "./components/EditorMenu";
import { SakspartView } from "./components/SakspartView";
import { SignaturView } from "./components/SignaturView";
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

  const canUndo = editorState.historyPointer >= 0;
  const canRedo = editorState.historyPointer < editorState.history.length - 1;

  const undo = useCallback(() => {
    if (!canUndo) return;
    setEditorState((current) => {
      const { inversePatches } = current.history[current.historyPointer];
      const previous = applyPatches(current, inversePatches);
      return {
        ...previous,
        historyPointer: current.historyPointer - 1,
      };
    });
  }, [canUndo, setEditorState]);

  const redo = useCallback(() => {
    if (!canRedo) return;
    setEditorState((current) => {
      const nextPointer = current.historyPointer + 1;
      const { patches } = current.history[nextPointer];
      const next = applyPatches(current, patches);
      return {
        ...next,
        historyPointer: nextPointer,
      };
    });
  }, [canRedo, setEditorState]);

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
      <EditorStateContext.Provider value={{ freeze, error, editorState, setEditorState }}>
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
            ${freeze && "cursor: wait;"}
          `}
        >
          <SakspartView
            sakspart={letter.sakspart}
            spraak={editorState.info.spraak}
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
}>({
  freeze: false,
  error: false,
  editorState: {} as LetterEditorState,
  setEditorState: () => {},
});
export const useEditor = () => {
  return useContext(EditorStateContext);
};
