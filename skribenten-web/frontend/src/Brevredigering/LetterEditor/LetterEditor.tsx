import { css } from "@emotion/react";
import { Heading } from "@navikt/ds-react";
import type { Dispatch, SetStateAction } from "react";
import { createContext, useContext } from "react";

import { DebugPanel } from "~/Brevredigering/LetterEditor/components/DebugPanel";
import { type CallbackReceiver } from "~/Brevredigering/LetterEditor/lib/actions";

import { EditorMenu } from "./components/EditorMenu";
import { SakspartView } from "./components/SakspartView";
import { SignaturView } from "./components/SignaturView";
import { EditableBlockComposer } from "./components/TextBlocks";
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
        <div
          css={css`
            margin-top: var(--a-spacing-6);
            /*clamp: minimum, ideal, max */
            padding: 0 clamp(1rem, 2vw, 4rem);
            caret-color: green;

            [contenteditable] {
              &:focus-within {
                outline: none;
              }
            }
          `}
          data-cy="letter-editor"
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
            {letter.title}
          </Heading>
          <div
            css={css`
              margin-bottom: 39px;
            `}
            onKeyDown={editorKeyboardShortcuts}
          >
            <EditableBlockComposer blocks={blocks} />
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
