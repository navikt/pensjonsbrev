import "./editor.css";

import { css } from "@emotion/react";
import { Heading } from "@navikt/ds-react";
import { useState } from "react";

import { ContentGroup } from "~/pages/Brevredigering/LetterEditor/components/ContentGroup";
import type { RenderedLetter } from "~/types/brevbakerTypes";

import Actions from "./actions";
import { EditorMenu } from "./components/EditorMenu";
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
      <div
        css={css`
          margin-top: var(--a-spacing-6);
          width: 758px;
        `}
      >
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
          {blocks.map((block, blockId) => (
            <div className={block.type} key={blockId}>
              <ContentGroup
                content={block.content}
                editable={block.editable}
                focusStolen={focusStolen.bind(null, blockId)}
                id={{ blockId }}
                onFocus={setCurrentBlock.bind(null, blockId)}
                stealFocus={editorState.stealFocus[blockId]}
                updateLetter={setEditorState}
              />
            </div>
          ))}
        </div>
        <SignaturView signatur={editorState.editedLetter.letter.signatur} />
      </div>
    </div>
  );
};
