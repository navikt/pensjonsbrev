import { css } from "@emotion/react";
import React, { useEffect, useRef } from "react";

import Actions from "~/Brevredigering/LetterEditor/actions";
import type { LiteralIndex } from "~/Brevredigering/LetterEditor/actions/model";
import { logPastedClipboard } from "~/Brevredigering/LetterEditor/actions/paste";
import { Text } from "~/Brevredigering/LetterEditor/components/Text";
import { useEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { applyAction } from "~/Brevredigering/LetterEditor/lib/actions";
import { getCursorOffset } from "~/Brevredigering/LetterEditor/services/caretUtils";
import type { EditedLetter, LiteralValue } from "~/types/brevbakerTypes";
import { FontType, ITEM_LIST, LITERAL, VARIABLE } from "~/types/brevbakerTypes";

function getContent(letter: EditedLetter, literalIndex: LiteralIndex) {
  const content = letter.blocks[literalIndex.blockIndex].content;
  const contentValue = content[literalIndex.contentIndex];
  if ("itemIndex" in literalIndex && contentValue.type === ITEM_LIST) {
    return contentValue.items[literalIndex.itemIndex].content;
  }
  return content;
}

export function ContentGroup({ literalIndex }: { literalIndex: LiteralIndex }) {
  const { editorState } = useEditor();
  const block = editorState.redigertBrev.blocks[literalIndex.blockIndex];
  const contents = getContent(editorState.redigertBrev, literalIndex);

  if (!block.editable) {
    return (
      <div>
        {block.content.map((content, index) => {
          switch (content.type) {
            case LITERAL:
            case VARIABLE: {
              return <Text content={content} key={index} />;
            }
            case ITEM_LIST: {
              return <span key={index}>TODO</span>;
            }
          }
        })}
      </div>
    );
  }

  return (
    <div>
      {contents.map((content, _contentIndex) => {
        switch (content.type) {
          case LITERAL: {
            const updatedLiteralIndex =
              "itemIndex" in literalIndex
                ? { ...literalIndex, itemContentIndex: _contentIndex }
                : { ...literalIndex, contentIndex: _contentIndex };
            return <EditableText content={content} key={_contentIndex} literalIndex={updatedLiteralIndex} />;
          }
          case VARIABLE: {
            return <Text content={content} key={_contentIndex} />;
          }
          case ITEM_LIST: {
            return (
              <ul key={_contentIndex}>
                {content.items.map((item, _itemIndex) => (
                  <li key={_itemIndex}>
                    <ContentGroup
                      literalIndex={{
                        blockIndex: literalIndex.blockIndex,
                        contentIndex: _contentIndex,
                        itemIndex: _itemIndex,
                      }}
                    />
                  </li>
                ))}
              </ul>
            );
          }
        }
      })}
    </div>
  );
}

export function EditableText({ literalIndex, content }: { literalIndex: LiteralIndex; content: LiteralValue }) {
  const contentEditableReference = useRef<HTMLSpanElement>(null);
  const { setEditorState } = useEditor();

  const text = (content.editedText ?? content.text) || "​";
  useEffect(() => {
    if (contentEditableReference.current !== null && contentEditableReference.current.textContent !== text) {
      contentEditableReference.current.textContent = text;
    }
  }, [text]);

  const handlePaste = (event: React.ClipboardEvent<HTMLSpanElement>) => {
    event.preventDefault();
    // TODO: for debugging frem til vi er ferdig å teste liming
    logPastedClipboard(event.clipboardData);

    const offset = getCursorOffset();
    if (offset >= 0) {
      applyAction(Actions.paste, setEditorState, literalIndex, offset, event.clipboardData);
    }
  };

  return (
    <span
      css={css`
        ${content.editedFontType === FontType.BOLD && "font-weight: bold;"}
        ${content.editedFontType === FontType.ITALIC && "font-style: italic;"}
      `}
      onPaste={handlePaste}
      ref={contentEditableReference}
    />
  );
}
