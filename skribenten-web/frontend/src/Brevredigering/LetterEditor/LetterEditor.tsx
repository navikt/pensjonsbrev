import "./editor.css";

import { css } from "@emotion/react";
import { Heading } from "@navikt/ds-react";
import type { Dispatch, SetStateAction } from "react";
import { createContext, useContext } from "react";

import { DebugPanel } from "~/Brevredigering/LetterEditor/components/DebugPanel";
import { applyAction, type CallbackReceiver } from "~/Brevredigering/LetterEditor/lib/actions";

import Actions from "./actions";
import { ContentGroup } from "./components/ContentGroup";
import { EditorMenu, Typography } from "./components/EditorMenu";
import { SakspartView } from "./components/SakspartView";
import { SignaturView } from "./components/SignaturView";
import type { LetterEditorState } from "./model/state";

const useEditorKeyboardShortcuts = (
  editorState: LetterEditorState,
  setEditorState: Dispatch<SetStateAction<LetterEditorState>>,
) => {
  return (event: React.KeyboardEvent<HTMLDivElement>) => {
    if (event.altKey && event.code === "Digit1") {
      event.preventDefault();
      applyAction(Actions.switchTypography, setEditorState, editorState.focus, Typography.PARAGRAPH);
    } else if (event.altKey && event.code === "Digit2") {
      event.preventDefault();
      applyAction(Actions.switchTypography, setEditorState, editorState.focus, Typography.TITLE1);
    } else if (event.altKey && event.code === "Digit3") {
      event.preventDefault();
      applyAction(Actions.switchTypography, setEditorState, editorState.focus, Typography.TITLE2);
    }
  };
};

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
          <EditorMenu />
        </div>
        <div className="editor">
          <SakspartView sakspart={letter.sakspart} />
          <Heading
            css={css`
              margin: var(--a-spacing-8) 0;
            `}
            level="1"
            size="large"
          >
            {letter.title}
          </Heading>
          <div onKeyDown={editorKeyboardShortcuts}>
            {blocks.map((block, blockIndex) => (
              <div className={block.type} key={blockIndex}>
                <ContentGroup literalIndex={{ blockIndex, contentIndex: 0 }} />
              </div>
            ))}
          </div>
          <SignaturView signatur={letter.signatur} />
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
