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
      return <br data-literal-index={JSON.stringify(literalIndex)} />;
    }
    case VARIABLE: {
      return (
        /**
         * biome-ignore lint/a11y/useKeyWithClickEvents: Klikk trenger ikke en tilsvarende
         * tastehendelse her siden det håndteres i ContentGroup.tsx
         * biome-ignore lint/a11y/noStaticElementInteractions: Ikke egentlig en interaksjon, hjelper
         * oss bare med å holde oversikt over fokus
         */
        <span
          css={{
            background: "var(--ax-bg-neutral-moderate)",
            borderRadius: "var(--ax-radius-2)",
            cursor: "default",
            display: "inline-block",
            lineHeight: "var(--ax-font-line-height-medium)",
            margin: "0 var(--ax-space-1)",
            outline: `${isFocused ? "2px solid var(--ax-border-accent)" : "1px solid var(--ax-border-neutral-strong)"}`,
            padding: "0 var(--ax-space-2)",

            ...(content.fontType === FontType.BOLD ? { fontWeight: "var(--ax-font-weight-bold)" } : {}),
            ...(content.fontType === FontType.ITALIC ? { fontStyle: "italic" } : {}),
          }}
          data-literal-index={JSON.stringify(literalIndex)}
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
