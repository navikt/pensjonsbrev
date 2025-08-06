import { css } from "@emotion/react";

import type { Focus, LiteralIndex } from "~/Brevredigering/LetterEditor/model/state";
import type { NewLine, VariableValue } from "~/types/brevbakerTypes";
import { FontType, NEW_LINE, VARIABLE } from "~/types/brevbakerTypes";

import { isBlockContentIndex, isItemContentIndex } from "../actions/common";
import { useEditor } from "../LetterEditor";
import { isTableCellIndex } from "../model/utils";

export type TextProperties = {
  content: VariableValue | NewLine;
  literalIndex: LiteralIndex;
};

export const Text = ({ content, literalIndex }: TextProperties) => {
  const { editorState, setEditorState } = useEditor();
  const isFocused = hasFocus(editorState.focus, literalIndex);

  switch (content.type) {
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

const hasFocus = (focus: Focus, idx: LiteralIndex) => {
  const isBlockContentFocused = focus.blockIndex === idx.blockIndex && focus.contentIndex === idx.contentIndex;

  if (isTableCellIndex(focus) && isTableCellIndex(idx)) {
    return (
      isBlockContentFocused &&
      focus.rowIndex === idx.rowIndex &&
      focus.cellIndex === idx.cellIndex &&
      focus.cellContentIndex === idx.cellContentIndex
    );
  } else if (isItemContentIndex(focus) && isItemContentIndex(idx)) {
    return (
      isBlockContentFocused && focus.itemIndex === idx.itemIndex && focus.itemContentIndex === idx.itemContentIndex
    );
  } else if (isBlockContentIndex(focus) && isBlockContentIndex(idx)) {
    return isBlockContentFocused;
  } else {
    return false;
  }
};
