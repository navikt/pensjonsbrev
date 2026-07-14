import { type Focus, type LiteralIndex } from "~/Brevredigering/LetterEditor/model/state";
import { FontType, type NewLine, type VariableValue } from "~/types/brevbakerTypes";

import { isBlockContentIndex, isItemContentIndex } from "../actions/common";
import { isTekstValgHighlighted, useInsertedTekstValgHighlight } from "../InsertedTekstValgHighlight";
import { useEditor } from "../LetterEditor";
import { isTableCellIndex } from "../model/utils";

export type TextProperties = {
  content: VariableValue | NewLine;
  literalIndex: LiteralIndex;
};

export const Text = ({ content, literalIndex }: TextProperties) => {
  const { editorState, setEditorState } = useEditor();
  const highlightedIds = useInsertedTekstValgHighlight();
  const isFocused = hasFocus(editorState.focus, literalIndex);
  const isInserted = isTekstValgHighlighted(highlightedIds, content);

  switch (content.type) {
    case "NEW_LINE": {
      return <br data-literal-index={JSON.stringify(literalIndex)} />;
    }
    case "VARIABLE": {
      return (
        /**
         * biome-ignore lint/a11y/useKeyWithClickEvents: Klikk trenger ikke en tilsvarende
         * tastehendelse her siden det håndteres i ContentGroup.tsx
         * biome-ignore lint/a11y/noStaticElementInteractions: Ikke egentlig en interaksjon, hjelper
         * oss bare med å holde oversikt over fokus
         */
        <span
          className={isInserted ? "inserted-flash-text" : undefined}
          css={{
            background: "var(--ax-bg-neutral-moderate)",
            borderRadius: "var(--ax-radius-2)",
            cursor: "default",
            display: "inline-block",
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

const hasFocus = (focus: Focus, index: LiteralIndex) => {
  const isBlockContentFocused = focus.blockIndex === index.blockIndex && focus.contentIndex === index.contentIndex;

  if (isTableCellIndex(focus) && isTableCellIndex(index)) {
    return (
      isBlockContentFocused &&
      focus.rowIndex === index.rowIndex &&
      focus.cellIndex === index.cellIndex &&
      focus.cellContentIndex === index.cellContentIndex
    );
  } else if (isItemContentIndex(focus) && isItemContentIndex(index)) {
    return (
      isBlockContentFocused && focus.itemIndex === index.itemIndex && focus.itemContentIndex === index.itemContentIndex
    );
  } else if (isBlockContentIndex(focus) && isBlockContentIndex(index)) {
    return isBlockContentFocused;
  } else {
    return false;
  }
};
