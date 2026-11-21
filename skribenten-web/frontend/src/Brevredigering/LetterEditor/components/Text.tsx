import { type Focus, type LiteralIndex } from "~/Brevredigering/LetterEditor/model/state";
import { FontType, type NewLine, type VariableValue } from "~/types/brevbakerTypes";

import { isBlockContentIndex, isItemContentIndex } from "../actions/common";
import { updateFocus } from "../actions/cursorPosition";
import { isTekstValgHighlighted, useInsertedTekstValgHighlight } from "../InsertedTekstValgHighlight";
import { useEditor } from "../LetterEditor";
import { applyAction } from "../lib/actions";
import { isTableCellIndex } from "../model/utils";
import { focusEditingHost } from "../services/caretUtils";

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
      const isEmpty = content.text.trim().length === 0;
      return (
        /**
         * biome-ignore lint/a11y/useKeyWithClickEvents: Klikk trenger ikke en tilsvarende
         * tastehendelse her siden det håndteres i ContentGroup.tsx
         * biome-ignore lint/a11y/noStaticElementInteractions: Ikke egentlig en interaksjon, hjelper
         * oss bare med å holde oversikt over fokus
         */
        <span
          className={isInserted ? "inserted-flash-text" : undefined}
          contentEditable={false}
          css={{
            background: "var(--ax-bg-neutral-moderate)",
            color: "var(--ax-text-neutral)",
            borderRadius: "var(--ax-radius-2)",
            cursor: "default",
            display: "inline-block",
            margin: "0 var(--ax-space-1)",
            outline: `${isFocused ? "2px solid var(--ax-border-accent)" : "1px solid var(--ax-border-neutral)"}`,
            padding: "0 var(--ax-space-2)",

            ...(isEmpty && {
              background: "transparent",
              margin: 0,
              outline: "none",
              padding: 0,
            }),
            ...(content.fontType === FontType.BOLD ? { fontWeight: "var(--ax-font-weight-bold)" } : {}),
            ...(content.fontType === FontType.ITALIC ? { fontStyle: "italic" } : {}),
          }}
          data-font-type={content.fontType === FontType.PLAIN ? undefined : content.fontType}
          data-literal-index={JSON.stringify(literalIndex)}
          data-variable=""
          onClick={(e) => {
            const host = e.currentTarget.closest<HTMLElement>('[contenteditable="true"]');
            focusEditingHost(host);
            globalThis.getSelection()?.removeAllRanges();
            applyAction(updateFocus, setEditorState, literalIndex);
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
