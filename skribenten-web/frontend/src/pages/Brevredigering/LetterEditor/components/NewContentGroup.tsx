import React, { useEffect, useRef } from "react";

import Actions from "~/pages/Brevredigering/LetterEditor/actions";
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
}: {
  content: LiteralValue;
  onChange: unknown;
  id: ID;
  setEditorState: CallbackReceiver<LetterEditorState>;
  isFocus: unknown;
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
      console.log("Trying to focus")
      // contentEditableReference.current.focus();
      selectService.focusAtOffset(contentEditableReference.current, 0);
    }
  }, [isFocus]);

  const handleEnter = (event: React.KeyboardEvent<HTMLSpanElement>) => {
    if (event.key === "Enter") {
      event.preventDefault();
      const offset = selectService.getCursorOffset();

      applyAction(Actions.split, setEditorState, id, offset);
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
      }}
      ref={contentEditableReference}
    />
  );
}
