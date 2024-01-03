import "./editor.css";

import { css } from "@emotion/react";
import { Heading } from "@navikt/ds-react";
import { useState } from "react";

import type { BlockProperties } from "~/pages/Brevredigering/LetterEditor/BlockProperties";
import type { AnyBlock, RenderedLetter } from "~/types/brevbakerTypes";
import { PARAGRAPH, TITLE1, TITLE2 } from "~/types/brevbakerTypes";

import Actions from "./actions";
import { EditorMenu } from "./components/EditorMenu";
import { Paragraph } from "./components/Paragraph";
import { SakspartView } from "./components/SakspartView";
import { SignaturView } from "./components/SignaturView";
import { Title1 } from "./components/Title1";
import { Title2 } from "./components/Title2";
import { bindActionWithCallback } from "./lib/actions";
import type { LetterEditorState } from "./model/state";

const AnyBlockView = (properties: BlockProperties<AnyBlock>) => {
  const block = properties.block;
  switch (block.type) {
    case TITLE1: {
      return <Title1 {...properties} block={block} />;
    }
    case TITLE2: {
      return <Title2 {...properties} block={block} />;
    }
    case PARAGRAPH: {
      return <Paragraph {...properties} block={block} />;
    }
  }
};

export const LetterEditor = ({ initialState }: { initialState: RenderedLetter }) => {
  const [editorState, setEditorState] = useState<LetterEditorState>(Actions.create(initialState));
  const [currentBlock, setCurrentBlock] = useState(0);

  const blocks = editorState.editedLetter.letter.blocks;

  const focusStolen = bindActionWithCallback(Actions.focusStolen, setEditorState);
  const switchType = bindActionWithCallback(Actions.switchType, setEditorState, currentBlock);

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
        onSwitchTypography={switchType}
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
            <AnyBlockView
              block={block}
              blockFocusStolen={focusStolen.bind(null, blockId)}
              blockId={blockId}
              blockStealFocus={editorState.stealFocus[blockId]}
              key={blockId}
              onFocus={setCurrentBlock.bind(null, blockId)}
              updateLetter={setEditorState}
            />
          ))}
        </div>
        <SignaturView signatur={editorState.editedLetter.letter.signatur} />
      </div>
    </div>
  );
};
