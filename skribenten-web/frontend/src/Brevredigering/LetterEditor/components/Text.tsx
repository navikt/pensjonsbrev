import { css } from "@emotion/react";

import type { LiteralIndex } from "~/Brevredigering/LetterEditor/model/state";
import type { TextContent } from "~/types/brevbakerTypes";
import { FontType, LITERAL, NEW_LINE, VARIABLE } from "~/types/brevbakerTypes";

import { useEditor } from "../LetterEditor";

export type TextProperties = {
  content: TextContent;
  literalIndex: LiteralIndex;
};

export const Text = ({ content, literalIndex }: TextProperties) => {
  const { editorState, setEditorState } = useEditor();
  const isFocused =
    editorState.focus.blockIndex === literalIndex.blockIndex &&
    editorState.focus.contentIndex === literalIndex.contentIndex;

  switch (content.type) {
    case LITERAL: {
      return <span>{content.text}</span>;
    }
    case NEW_LINE: {
      return <br />;
    }
    case VARIABLE: {
      return (
        <span
          css={css`
            border-radius: 4px;
            border: ${isFocused ? "2px" : "1px"} solid ${isFocused ? "blue" : "var(--a-border-default)"};
            background: var(--a-gray-50);
            padding: 1px 4px;
            display: inline-block;
            margin: 0 1px;
            cursor: default;

            ${content.fontType === FontType.BOLD && "font-weight: bold;"}
            ${content.fontType === FontType.ITALIC && "font-style: italic;"}
          `}
          onClick={() => {
            setEditorState((oldState) => ({
              ...oldState,
              focus: literalIndex,
            }));
          }}
        >
          {content.text}
        </span>
      );
    }
  }
};
