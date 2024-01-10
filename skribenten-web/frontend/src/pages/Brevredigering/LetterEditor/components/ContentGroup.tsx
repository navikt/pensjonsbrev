import React, { useEffect, useRef } from "react";

import Actions from "~/pages/Brevredigering/LetterEditor/actions";
import { MergeTarget } from "~/pages/Brevredigering/LetterEditor/actions/model";
import { Text } from "~/pages/Brevredigering/LetterEditor/components/Text";
import { useEditor } from "~/pages/Brevredigering/LetterEditor/LetterEditor";
import { applyAction } from "~/pages/Brevredigering/LetterEditor/lib/actions";
import { SelectionService } from "~/pages/Brevredigering/LetterEditor/services/SelectionService";
import type { LiteralValue } from "~/types/brevbakerTypes";
import { ITEM_LIST, LITERAL, VARIABLE } from "~/types/brevbakerTypes";

const selectService = new SelectionService(true);

export type EditableIndex = {
  blockIndex: number;
  contentIndex?: number;
  itemIndex?: number;
};

export function ContentGroup({ blockIndex, contentIndex, itemIndex }: EditableIndex) {
  console.log(blockIndex, contentIndex, itemIndex);
  const { editorState, setEditorState } = useEditor();
  const block = editorState.editedLetter.letter.blocks[blockIndex];
  const contents = itemIndex === undefined ? block.content : block.content[contentIndex].items[itemIndex].content;

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
    <div onFocus={() => setEditorState((oldState) => ({ ...oldState, currentBlock: blockIndex }))}>
      {contents.map((content, _contentIndex) => {
        switch (content.type) {
          case LITERAL: {
            return (
              <EditableText
                blockIndex={blockIndex}
                content={content}
                contentIndex={_contentIndex}
                itemIndex={itemIndex}
                key={contentIndex}
              />
            );
          }
          case VARIABLE: {
            return <Text content={content} key={_contentIndex} />;
          }
          case ITEM_LIST: {
            return (
              <ul>
                {content.items.map((item, _itemIndex) => (
                  <li key={_itemIndex}>
                    <ContentGroup blockIndex={blockIndex} contentIndex={_contentIndex} itemIndex={_itemIndex} />
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

export function EditableText({
  blockIndex,
  contentIndex,
  itemIndex,
  content,
}: {
  blockIndex: number;
  contentIndex: number;
  itemIndex?: number;
  content: LiteralValue;
}) {
  const id = { blockIndex, contentIndex, itemIndex };
  const contentEditableReference = useRef<HTMLSpanElement>(null);
  const { editorState, setEditorState } = useEditor();

  const isFocus =
    editorState.nextFocus?.blockIndex === blockIndex && editorState.nextFocus.contentIndex === contentIndex;

  const text = content.text || "​";
  useEffect(() => {
    if (contentEditableReference.current !== null && contentEditableReference.current.textContent !== text) {
      contentEditableReference.current.textContent = text;
    }
  }, [text]);

  useEffect(() => {
    if (isFocus && contentEditableReference.current !== null) {
      selectService.focusAtOffset(
        contentEditableReference.current.childNodes[0],
        editorState.nextFocus?.cursorPosition,
      );
    }
  }, [editorState.nextFocus?.cursorPosition, isFocus]);

  const handleEnter = (event: React.KeyboardEvent<HTMLSpanElement>) => {
    event.preventDefault();
    const offset = selectService.getCursorOffset();

    applyAction(Actions.split, setEditorState, id, offset);
  };

  const handleBackspace = (event: React.KeyboardEvent<HTMLSpanElement>) => {
    const cursorPosition = selectService.getCursorOffset();
    if (
      cursorPosition === 0 ||
      (contentEditableReference.current?.textContent?.startsWith("​") && cursorPosition === 1)
    ) {
      event.preventDefault();
      applyAction(Actions.merge, setEditorState, id, MergeTarget.PREVIOUS);
    }
  };

  const handleDelete = (event: React.KeyboardEvent<HTMLSpanElement>) => {
    const cursorIsAtEnd = selectService.getCursorOffset() >= content.text.length;
    if (cursorIsAtEnd) {
      event.preventDefault();
      applyAction(Actions.merge, setEditorState, id, MergeTarget.NEXT);
    }
  };

  return (
    <span
      // NOTE: ideally this would be "plaintext-only", and it works in practice.
      // However, the tests will not work if set to plaintext-only. For some reason focus/input and other events will not be triggered by userEvent as expected.
      // This is not documented anywhere I could find and caused a day of frustration, beware
      contentEditable="true"
      onInput={(event) => {
        applyAction(Actions.updateContentText, setEditorState, id, (event.target as HTMLSpanElement).textContent ?? "");
      }}
      onKeyDown={(event) => {
        if (event.key === "Enter") {
          handleEnter(event);
        }
        if (event.key === "Backspace") {
          handleBackspace(event);
        }
        if (event.key === "Delete") {
          handleDelete(event);
        }
      }}
      ref={contentEditableReference}
    />
  );
}
