import "./editor.css";

import { css } from "@emotion/react";
import { Heading } from "@navikt/ds-react";
import { createContext, useContext, useState } from "react";

import type { RenderedLetter } from "~/types/brevbakerTypes";

import Actions from "./actions";
import { ContentGroup } from "./components/ContentGroup";
import { EditorMenu } from "./components/EditorMenu";
import { SakspartView } from "./components/SakspartView";
import { SignaturView } from "./components/SignaturView";
import type { CallbackReceiver } from "./lib/actions";
import type { LetterEditorState } from "./model/state";

export const LetterEditor = ({ initialState }: { initialState: RenderedLetter }) => {
  const [editorState, setEditorState] = useState<LetterEditorState>(Actions.create(initialState));
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
                <ContentGroup blockIndex={blockIndex} />
              </div>
            ))}
          </div>
          <SignaturView signatur={editorState.editedLetter.letter.signatur} />
        </div>
      </EditorStateContext.Provider>
    </div>
  );
};

export const EditorStateContext = createContext<{
  editorState: LetterEditorState;
  setEditorState: CallbackReceiver<LetterEditorState>;
}>({
  editorState: {} as LetterEditorState,
  setEditorState: () => {},
});
export const useEditor = () => {
  return useContext(EditorStateContext);
};
