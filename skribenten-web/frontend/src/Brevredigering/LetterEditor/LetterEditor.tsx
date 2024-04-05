import "./editor.css";

import { css } from "@emotion/react";
import { Heading } from "@navikt/ds-react";
import type { Dispatch, SetStateAction } from "react";
import { createContext, useContext } from "react";

import { DebugPanel } from "~/Brevredigering/LetterEditor/components/DebugPanel";

import { ContentGroup } from "./components/ContentGroup";
import { EditorMenu } from "./components/EditorMenu";
import { SakspartView } from "./components/SakspartView";
import { SignaturView } from "./components/SignaturView";
import type { LetterEditorState } from "./model/state";

export const LetterEditor = ({
  editorState,
  setEditorState,
}: {
  editorState: LetterEditorState;
  setEditorState: Dispatch<SetStateAction<LetterEditorState | undefined>>;
}) => {
  const blocks = editorState.editedLetter.letter.blocks;

  return (
    <div
      css={css`
        display: flex;
        flex-direction: column;
        align-items: center;
      `}
    >
      <EditorStateContext.Provider value={{ editorState, setEditorState }}>
        <EditorMenu />
        <div className="editor">
          <SakspartView sakspart={editorState.editedLetter.letter.sakspart} />
          <Heading
            css={css`
              margin: var(--a-spacing-8) 0;
            `}
            level="1"
            size="large"
          >
            {editorState.editedLetter.letter.title}
          </Heading>
          <div>
            {blocks.map((block, blockIndex) => (
              <div className={block.type} key={blockIndex}>
                <ContentGroup literalIndex={{ blockIndex, contentIndex: 0 }} />
              </div>
            ))}
          </div>
          <SignaturView signatur={editorState.editedLetter.letter.signatur} />
        </div>
        <DebugPanel />
      </EditorStateContext.Provider>
    </div>
  );
};

export const EditorStateContext = createContext<{
  editorState: LetterEditorState;
  setEditorState: Dispatch<LetterEditorState | undefined>;
}>({
  editorState: {} as LetterEditorState,
  setEditorState: () => {},
});
export const useEditor = () => {
  return useContext(EditorStateContext);
};
