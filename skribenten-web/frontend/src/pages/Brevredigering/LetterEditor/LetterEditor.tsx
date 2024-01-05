import "./editor.css";

import { css } from "@emotion/react";
import { Heading } from "@navikt/ds-react";
import { Fragment, useState } from "react";

import { ContentGroup } from "~/pages/Brevredigering/LetterEditor/components/ContentGroup";
import type { RenderedLetter } from "~/types/brevbakerTypes";

import Actions from "./actions";
import { EditorMenu } from "./components/EditorMenu";
import { NewContentGroup } from "./components/NewContentGroup";
import { SakspartView } from "./components/SakspartView";
import { SignaturView } from "./components/SignaturView";
import { bindActionWithCallback } from "./lib/actions";
import type { LetterEditorState } from "./model/state";

export const LetterEditor = ({ initialState }: { initialState: RenderedLetter }) => {
  const [editorState, setEditorState] = useState<LetterEditorState>(Actions.create(initialState));
  const [currentBlock, setCurrentBlock] = useState(0);

  const blocks = editorState.editedLetter.letter.blocks;

  const focusStolen = bindActionWithCallback(Actions.focusStolen, setEditorState);
  const switchTypography = bindActionWithCallback(Actions.switchTypography, setEditorState, currentBlock);

  return (
    <div
      css={css`
        display: flex;
        flex-direction: column;
        align-items: center;
      `}
    >
      <EditorMenu
        activeTypography={editorState.editedLetter.letter.blocks[currentBlock].type}
        onSwitchTypography={switchTypography}
      />
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
            <Fragment key={blockIndex}>
              <div className={block.type}>
                <NewContentGroup nextFocus={editorState.nextFocus} block={block} blockIndex={blockIndex} setEditorState={setEditorState} />
              </div>
              {/*<div className={block.type}>*/}
              {/*  <ContentGroup*/}
              {/*    content={block.content}*/}
              {/*    editable={block.editable}*/}
              {/*    focusStolen={focusStolen.bind(null, blockIndex)}*/}
              {/*    id={{ blockId: blockIndex }}*/}
              {/*    onFocus={setCurrentBlock.bind(null, blockIndex)}*/}
              {/*    stealFocus={editorState.stealFocus[blockIndex]}*/}
              {/*    updateLetter={setEditorState}*/}
              {/*  />*/}
              {/*</div>*/}
            </Fragment>
          ))}
        </div>
        <SignaturView signatur={editorState.editedLetter.letter.signatur} />
      </div>
    </div>
  );
};
