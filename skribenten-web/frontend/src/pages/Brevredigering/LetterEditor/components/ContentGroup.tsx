import React, { useEffect, useRef } from "react";

import Actions from "~/pages/Brevredigering/LetterEditor/actions";
import type { ContentIndex } from "~/pages/Brevredigering/LetterEditor/actions/model";
import { MergeTarget } from "~/pages/Brevredigering/LetterEditor/actions/model";
import { Text } from "~/pages/Brevredigering/LetterEditor/components/Text";
import { useEditor } from "~/pages/Brevredigering/LetterEditor/LetterEditor";
import { applyAction } from "~/pages/Brevredigering/LetterEditor/lib/actions";
import { SelectionService } from "~/pages/Brevredigering/LetterEditor/services/SelectionService";
import type { LiteralValue, RenderedLetter } from "~/types/brevbakerTypes";
import { ITEM_LIST, LITERAL, VARIABLE } from "~/types/brevbakerTypes";

const selectService = new SelectionService(true);

function getContent(letter: RenderedLetter, id: ContentIndex) {
  const content = letter.blocks[id.blockIndex].content;
  const contentValue = content[id.contentIndex];
  if ("itemIndex" in id && contentValue.type === ITEM_LIST) {
    return contentValue.items[id.itemIndex].content;
  }
  return content;
}

export function ContentGroup({ id }: { id: ContentIndex }) {
  const { editorState, setEditorState } = useEditor();
  const block = editorState.editedLetter.letter.blocks[id.blockIndex];
  const contents = getContent(editorState.editedLetter.letter, id);

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
    <div onFocus={() => setEditorState((oldState) => ({ ...oldState, currentBlock: id.blockIndex }))}>
      {contents.map((content, _contentIndex) => {
        switch (content.type) {
          case LITERAL: {
            const index =
              "itemIndex" in id ? { ...id, itemContentIndex: _contentIndex } : { ...id, contentIndex: _contentIndex };
            return <EditableText content={content} id={index} key={_contentIndex} />;
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
                      id={{ blockIndex: id.blockIndex, contentIndex: _contentIndex, itemIndex: _itemIndex }}
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

export function EditableText({ id, content }: { id: ContentIndex; content: LiteralValue }) {
  const contentEditableReference = useRef<HTMLSpanElement>(null);
  const { editorState, setEditorState } = useEditor();

  const isFocus =
    editorState.nextFocus?.blockIndex === id.blockIndex && editorState.nextFocus.contentIndex === id.contentIndex;

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
