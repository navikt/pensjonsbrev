import React, { useEffect, useRef } from "react";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { MergeTarget } from "~/Brevredigering/LetterEditor/actions/merge";
import type { LiteralIndex } from "~/Brevredigering/LetterEditor/actions/model";
import { logPastedClipboard } from "~/Brevredigering/LetterEditor/actions/paste";
import { Text } from "~/Brevredigering/LetterEditor/components/Text";
import { useEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { applyAction } from "~/Brevredigering/LetterEditor/lib/actions";
import type { Focus } from "~/Brevredigering/LetterEditor/model/state";
import {
  areAnyContentEditableSiblingsPlacedHigher,
  areAnyContentEditableSiblingsPlacedLower,
  findOnLineAbove,
  findOnLineBelow,
  focusAtOffset,
  getCaretRect,
  getCursorOffset,
  gotoCoordinates,
} from "~/Brevredigering/LetterEditor/services/caretUtils";
import type { EditedLetter, LiteralValue } from "~/types/brevbakerTypes";
import { ITEM_LIST, LITERAL, VARIABLE } from "~/types/brevbakerTypes";

/**
 * When changing lines with ArrowUp/ArrowDown we sometimes "artificially click" the next line.
 * If y-coord is exactly at the edge it sometimes misses. To avoid that we move the point a little bit away from the line.
 */
const Y_COORD_SAFETY_MARGIN = 10;

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

function hasFocus(focus: Focus, literalIndex: LiteralIndex) {
  const basicMatch = focus.blockIndex === literalIndex.blockIndex && focus.contentIndex === literalIndex.contentIndex;
  if ("itemIndex" in literalIndex && "itemIndex" in focus) {
    const itemMatch =
      focus.itemIndex === literalIndex.itemIndex && focus.itemContentIndex === literalIndex.itemContentIndex;
    return itemMatch && basicMatch;
  }
  return basicMatch;
}

export function EditableText({ literalIndex, content }: { literalIndex: LiteralIndex; content: LiteralValue }) {
  const contentEditableReference = useRef<HTMLSpanElement>(null);
  const { freeze, editorState, setEditorState } = useEditor();

  const shouldBeFocused = hasFocus(editorState.focus, literalIndex);

  const text = (content.editedText ?? content.text) || "​";
  useEffect(() => {
    if (contentEditableReference.current !== null && contentEditableReference.current.textContent !== text) {
      contentEditableReference.current.textContent = text;
    }
  }, [text]);

  useEffect(() => {
    if (
      !freeze &&
      shouldBeFocused &&
      contentEditableReference.current !== null &&
      editorState.focus.cursorPosition !== undefined
    ) {
      focusAtOffset(contentEditableReference.current.childNodes[0], editorState.focus.cursorPosition);
    }
  }, [editorState.focus.cursorPosition, shouldBeFocused, freeze]);

  const handleEnter = (event: React.KeyboardEvent<HTMLSpanElement>) => {
    event.preventDefault();
    const offset = getCursorOffset();

    applyAction(Actions.split, setEditorState, literalIndex, offset);
  };

  const handleBackspace = (event: React.KeyboardEvent<HTMLSpanElement>) => {
    const cursorPosition = getCursorOffset();
    if (
      cursorPosition === 0 ||
      (contentEditableReference.current?.textContent?.startsWith("​") && cursorPosition === 1)
    ) {
      event.preventDefault();
      applyAction(Actions.merge, setEditorState, literalIndex, MergeTarget.PREVIOUS);
    }
  };

  const handleDelete = (event: React.KeyboardEvent<HTMLSpanElement>) => {
    const cursorIsAtEnd = getCursorOffset() >= text.length;
    const cursorIsInLastContent =
      getContent(editorState.redigertBrev, literalIndex).length - 1 === literalIndex.contentIndex;
    if (cursorIsAtEnd && cursorIsInLastContent) {
      event.preventDefault();
      applyAction(Actions.merge, setEditorState, literalIndex, MergeTarget.NEXT);
    }
  };

  const handleArrowLeft = (event: React.KeyboardEvent<HTMLSpanElement>) => {
    if (contentEditableReference.current === null) return;

    const allSpans = [...document.querySelectorAll<HTMLSpanElement>("span[contenteditable]")];
    const thisSpanIndex = allSpans.indexOf(contentEditableReference.current);
    const cursorIsAtBeginning = getCursorOffset() === 0;
    if (!cursorIsAtBeginning) return;

    const previousSpanIndex = thisSpanIndex - 1;
    if (previousSpanIndex === -1) return;

    event.preventDefault();
    const nextFocus = allSpans[previousSpanIndex];
    nextFocus.focus();
    focusAtOffset(nextFocus.childNodes[0], nextFocus.textContent?.length ?? 0);
  };

  const handleArrowRight = (event: React.KeyboardEvent<HTMLSpanElement>) => {
    if (contentEditableReference.current === null) return;

    const allSpans = [...document.querySelectorAll<HTMLSpanElement>("span[contenteditable]")];
    const thisSpanIndex = allSpans.indexOf(contentEditableReference.current);

    const cursorIsAtEnd = getCursorOffset() >= text.length;
    if (!cursorIsAtEnd) return;

    const nextSpanIndex = thisSpanIndex + 1;

    if (nextSpanIndex > allSpans.length - 1) return;

    event.preventDefault();
    const nextFocus = allSpans[nextSpanIndex];
    nextFocus.focus();
    focusAtOffset(nextFocus.childNodes[0], 0);
  };

  const handleArrowUp = (event: React.KeyboardEvent<HTMLSpanElement>) => {
    const element = contentEditableReference.current;
    const caretCoordinates = getCaretRect();

    if (element === null || caretCoordinates === undefined) {
      return;
    }

    const shouldDoItOurselves = !areAnyContentEditableSiblingsPlacedHigher(element);

    if (shouldDoItOurselves) {
      const next = findOnLineAbove(element);

      if (next) {
        gotoCoordinates({ x: caretCoordinates.x, y: next.bottom - Y_COORD_SAFETY_MARGIN });
        event.preventDefault();
      }
    }
  };

  const handleArrowDown = (event: React.KeyboardEvent<HTMLSpanElement>) => {
    const element = contentEditableReference.current;
    const caretCoordinates = getCaretRect();

    if (element === null || caretCoordinates === undefined) {
      return;
    }

    const shouldDoItOurselves = !areAnyContentEditableSiblingsPlacedLower(element);
    if (shouldDoItOurselves) {
      const next = findOnLineBelow(element);

      if (next) {
        gotoCoordinates({ x: caretCoordinates.x, y: next.top + Y_COORD_SAFETY_MARGIN });
        event.preventDefault();
      }
    }
  };

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
      // NOTE: ideally this would be "plaintext-only", and it works in practice.
      // However, the tests will not work if set to plaintext-only. For some reason focus/input and other events will not be triggered by userEvent as expected.
      // This is not documented anywhere I could find and caused a day of frustration, beware
      contentEditable={!freeze}
      onFocus={() => {
        setEditorState((oldState) => ({
          ...oldState,
          focus: literalIndex,
        }));
      }}
      onInput={(event) => {
        applyAction(
          Actions.updateContentText,
          setEditorState,
          literalIndex,
          (event.target as HTMLSpanElement).textContent ?? "",
        );
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
        if (event.key === "ArrowLeft") {
          handleArrowLeft(event);
        }
        if (event.key === "ArrowRight") {
          handleArrowRight(event);
        }
        if (event.key === "ArrowDown") {
          handleArrowDown(event);
        }
        if (event.key === "ArrowUp") {
          handleArrowUp(event);
        }
      }}
      onPaste={handlePaste}
      ref={contentEditableReference}
    />
  );
}
