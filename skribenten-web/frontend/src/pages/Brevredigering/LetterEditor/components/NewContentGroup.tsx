import React, { useEffect, useRef } from "react";

import Actions from "~/pages/Brevredigering/LetterEditor/actions";
import { Text } from "~/pages/Brevredigering/LetterEditor/components/Text";
import type { CallbackReceiver } from "~/pages/Brevredigering/LetterEditor/lib/actions";
import { bindActionWithCallback } from "~/pages/Brevredigering/LetterEditor/lib/actions";
import type { LetterEditorState } from "~/pages/Brevredigering/LetterEditor/model/state";
import type { AnyBlock, LiteralValue } from "~/types/brevbakerTypes";
import { ITEM_LIST, LITERAL, VARIABLE } from "~/types/brevbakerTypes";

export function NewContentGroup({
  block,
  setEditorState,
  blockIndex,
}: {
  block: AnyBlock;
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
                key={contentIndex}
                onChange={bindActionWithCallback(Actions.updateContentText, setEditorState, {
                  blockId: blockIndex,
                  contentId: contentIndex,
                })}
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

function OurOwnEditableText({ content, onChange }: { content: LiteralValue; onChange: unknown }) {
  const contentEditableReference = useRef<HTMLSpanElement>(null);

  useEffect(() => {
    if (contentEditableReference.current !== null && contentEditableReference.current.textContent !== content.text) {
      contentEditableReference.current.textContent = content.text;
    }
  }, []);

  return (
    <span
      contentEditable="plaintext-only"
      onInput={(event) => onChange(event.target.textContent)}
      ref={contentEditableReference}
    />
  );
}

