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

export function NewContentGroup({ blockIndex }: { blockIndex: number }) {
  const { editorState, setEditorState } = useEditor();

  const block = editorState.editedLetter.letter.blocks[blockIndex];

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
      {block.content.map((content, contentIndex) => {
        switch (content.type) {
          case LITERAL: {
            return (
              <OurOwnEditableText
                blockIndex={blockIndex}
                content={content}
                contentIndex={contentIndex}
                key={contentIndex}
              />
            );
          }
          case VARIABLE: {
            return <Text content={content} key={contentIndex} />;
          }
          case ITEM_LIST: {
            return <span>TODO</span>;
          }
        }
      })}
    </div>
  );
}

function OurOwnEditableText({
  blockIndex,
  contentIndex,
  content,
}: {
  blockIndex: number;
  contentIndex: number;
  content: LiteralValue;
}) {
  const id = { blockId: blockIndex, contentId: contentIndex };
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
  }, [isFocus]);

  const handleEnter = (event: React.KeyboardEvent<HTMLSpanElement>) => {
    if (event.key === "Enter") {
      event.preventDefault();
      const offset = selectService.getCursorOffset();

      applyAction(Actions.split, setEditorState, id, offset);
    }
    if (event.key === "Backspace") {
      const cursorPosition = selectService.getCursorOffset();
      if (
        cursorPosition === 0 ||
        (contentEditableReference.current?.textContent?.startsWith("​") && cursorPosition === 1)
      ) {
        event.preventDefault();
        applyAction(Actions.merge, setEditorState, id, MergeTarget.PREVIOUS);
      }
    }
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

  return (
    <span
      contentEditable="plaintext-only"
      onInput={(event) =>
        applyAction(Actions.updateContentText, setEditorState, id, (event.target as HTMLSpanElement).textContent ?? "")
      }
      onKeyDown={(event) => {
        if (event.key === "Enter") {
          handleEnter(event);
        }
        if (event.key === "Backspace") {
          handleBackspace(event);
        }
      }}
      ref={contentEditableReference}
    />
  );
}
