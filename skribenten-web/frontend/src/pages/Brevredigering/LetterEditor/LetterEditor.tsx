import { useState } from "react";

import type { AnyBlock, RenderedLetter } from "~/types/brevbakerTypes";
import { PARAGRAPH, TITLE1, TITLE2 } from "~/types/brevbakerTypes";

import Actions from "./actions";
import { EditorMenu } from "./components/EditorMenu";
import { Paragraph } from "./components/Paragraph";
import { SakspartView } from "./components/SakspartView";
import { SignaturView } from "./components/SignaturView";
import { Title1 } from "./components/Title1";
import { Title2 } from "./components/Title2";
import styles from "./LetterEditor.module.css";
import type { BoundAction, CallbackReceiver } from "./lib/actions";
import { bindActionWithCallback } from "./lib/actions";
import type { CursorPosition, LetterEditorState } from "./model/state";

type AnyBlockProperties = {
  block: AnyBlock;
  blockId: number;
  updateLetter: CallbackReceiver<LetterEditorState>;
  stealFocus?: CursorPosition;
  blockFocusStolen: BoundAction<[]>;
  onFocus: BoundAction<[]>;
};

const AnyBlockView = ({ block, blockId, updateLetter, stealFocus, blockFocusStolen, onFocus }: AnyBlockProperties) => {
  switch (block.type) {
    case TITLE1: {
      return (
        <Title1
          block={block}
          blockFocusStolen={blockFocusStolen}
          blockId={blockId}
          blockStealFocus={stealFocus}
          onFocus={onFocus}
          updateLetter={updateLetter}
        />
      );
    }
    case TITLE2: {
      return (
        <Title2
          block={block}
          blockFocusStolen={blockFocusStolen}
          blockId={blockId}
          blockStealFocus={stealFocus}
          onFocus={onFocus}
          updateLetter={updateLetter}
        />
      );
    }
    case PARAGRAPH: {
      return (
        <Paragraph
          block={block}
          blockFocusStolen={blockFocusStolen}
          blockId={blockId}
          blockStealFocus={stealFocus}
          onFocus={onFocus}
          updateLetter={updateLetter}
        />
      );
    }
  }
};

export const LetterEditor = ({ initialState }: { initialState: RenderedLetter }) => {
  const [editorState, setEditorState] = useState<LetterEditorState>(Actions.create(initialState));

  const blocks = editorState.editedLetter.letter.blocks;

  const [currentBlock, setCurrentBlock] = useState(0);

  const focusStolen = bindActionWithCallback(Actions.focusStolen, setEditorState);
  const switchType = bindActionWithCallback(Actions.switchType, setEditorState, currentBlock);

  return (
    <div className={styles.container}>
      <EditorMenu switchType={switchType} />
      <div className={styles.letter}>
        <SakspartView sakspart={editorState.editedLetter.letter.sakspart} />
        <h1>{editorState.editedLetter.letter.title}</h1>
        {blocks.map((block, blockId) => (
          <AnyBlockView
            block={block}
            blockFocusStolen={focusStolen.bind(null, blockId)}
            blockId={blockId}
            key={blockId}
            onFocus={setCurrentBlock.bind(null, blockId)}
            stealFocus={editorState.stealFocus[blockId]}
            updateLetter={setEditorState}
          />
        ))}
        <SignaturView signatur={editorState.editedLetter.letter.signatur} />
      </div>
      {/*eslint-disable-next-line no-console*/}
      <button onClick={() => console.log(editorState.editedLetter)}>Save</button>
    </div>
  );
};
