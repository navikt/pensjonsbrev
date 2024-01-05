import React, { useEffect, useRef } from "react";

import Actions from "~/pages/Brevredigering/LetterEditor/actions";
import { MergeTarget } from "~/pages/Brevredigering/LetterEditor/actions/model";
import { Text } from "~/pages/Brevredigering/LetterEditor/components/Text";
import type { CallbackReceiver } from "~/pages/Brevredigering/LetterEditor/lib/actions";
import { applyAction, bindActionWithCallback } from "~/pages/Brevredigering/LetterEditor/lib/actions";
import type { LetterEditorState } from "~/pages/Brevredigering/LetterEditor/model/state";
import { SelectionService } from "~/pages/Brevredigering/LetterEditor/services/SelectionService";
import type { AnyBlock, LiteralValue } from "~/types/brevbakerTypes";
import { ITEM_LIST, LITERAL, VARIABLE } from "~/types/brevbakerTypes";

const selectService = new SelectionService(true);

export function NewContentGroup({
  block,
  setEditorState,
  blockIndex,
  nextFocus,
}: {
  block: AnyBlock;
  nextFocus: unknown;
  setEditorState: CallbackReceiver<LetterEditorState>;
  blockIndex: number;
}) {
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
      {block.content.map((content, contentIndex) => {
        switch (content.type) {
          case LITERAL: {
            return (
              <OurOwnEditableText
                content={content}
                focusOffset={nextFocus?.startOffset}
                id={{
                  blockId: blockIndex,
                  contentId: contentIndex,
                }}
                isFocus={nextFocus?.blockIndex === blockIndex && contentIndex === nextFocus?.contentIndex}
                key={contentIndex}
                onChange={bindActionWithCallback(Actions.updateContentText, setEditorState, {
                  blockId: blockIndex,
                  contentId: contentIndex,
                })}
                setEditorState={setEditorState}
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

// TODO: rename
type ID = {
  blockId: number;
  contentId: number;
};

function OurOwnEditableText({
  content,
  onChange,
  id,
  setEditorState,
  isFocus,
  focusOffset,
}: {
  content: LiteralValue;
  onChange: unknown;
  id: ID;
  setEditorState: CallbackReceiver<LetterEditorState>;
  isFocus: unknown;
  focusOffset: number;
}) {
  const contentEditableReference = useRef<HTMLSpanElement>(null);

  const text = content.text || "​";
  useEffect(() => {
    if (contentEditableReference.current !== null && contentEditableReference.current.textContent !== text) {
      contentEditableReference.current.textContent = text;
    }
  }, [text]);

  useEffect(() => {
    if (isFocus && contentEditableReference.current !== null) {
      selectService.focusAtOffset(contentEditableReference.current.childNodes[0], focusOffset);
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
      onInput={(event) => onChange(event.target.textContent)}
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
