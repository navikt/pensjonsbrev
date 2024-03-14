import { inRange, minBy } from "lodash";
import React, { useEffect, useRef } from "react";

import Actions from "~/Brevredigering/LetterEditor/actions";
import { MergeTarget } from "~/Brevredigering/LetterEditor/actions/merge";
import type { LiteralIndex } from "~/Brevredigering/LetterEditor/actions/model";
import { Text } from "~/Brevredigering/LetterEditor/components/Text";
import { useEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { applyAction } from "~/Brevredigering/LetterEditor/lib/actions";
import type { Focus } from "~/Brevredigering/LetterEditor/model/state";
import { SelectionService } from "~/Brevredigering/LetterEditor/services/SelectionService";
import type { LiteralValue, RenderedLetter } from "~/types/brevbakerTypes";
import { ITEM_LIST, LITERAL, VARIABLE } from "~/types/brevbakerTypes";

const selectService = new SelectionService(true);

function getContent(letter: RenderedLetter, literalIndex: LiteralIndex) {
  const content = letter.blocks[literalIndex.blockIndex].content;
  const contentValue = content[literalIndex.contentIndex];
  if ("itemIndex" in literalIndex && contentValue.type === ITEM_LIST) {
    return contentValue.items[literalIndex.itemIndex].content;
  }
  return content;
}

export function ContentGroup({ literalIndex }: { literalIndex: LiteralIndex }) {
  const { editorState } = useEditor();
  const block = editorState.editedLetter.letter.blocks[literalIndex.blockIndex];
  const contents = getContent(editorState.editedLetter.letter, literalIndex);

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
  const { editorState, setEditorState } = useEditor();

  const shouldBeFocused = hasFocus(editorState.focus, literalIndex);

  const text = content.text || "​";
  useEffect(() => {
    if (contentEditableReference.current !== null && contentEditableReference.current.textContent !== text) {
      contentEditableReference.current.textContent = text;
    }
  }, [text]);

  useEffect(() => {
    if (
      shouldBeFocused &&
      contentEditableReference.current !== null &&
      editorState.focus.cursorPosition !== undefined
    ) {
      selectService.focusAtOffset(contentEditableReference.current.childNodes[0], editorState.focus.cursorPosition);
    }
  }, [editorState.focus.cursorPosition, shouldBeFocused]);

  const handleEnter = (event: React.KeyboardEvent<HTMLSpanElement>) => {
    event.preventDefault();
    const offset = selectService.getCursorOffset();

    applyAction(Actions.split, setEditorState, literalIndex, offset);
  };

  const handleBackspace = (event: React.KeyboardEvent<HTMLSpanElement>) => {
    const cursorPosition = selectService.getCursorOffset();
    if (
      cursorPosition === 0 ||
      (contentEditableReference.current?.textContent?.startsWith("​") && cursorPosition === 1)
    ) {
      event.preventDefault();
      applyAction(Actions.merge, setEditorState, literalIndex, MergeTarget.PREVIOUS);
    }
  };

  const handleDelete = (event: React.KeyboardEvent<HTMLSpanElement>) => {
    const cursorIsAtEnd = selectService.getCursorOffset() >= content.text.length;
    const cursorIsInLastContent =
      getContent(editorState.editedLetter.letter, literalIndex).length - 1 === literalIndex.contentIndex;
    if (cursorIsAtEnd && cursorIsInLastContent) {
      event.preventDefault();
      applyAction(Actions.merge, setEditorState, literalIndex, MergeTarget.NEXT);
    }
  };

  const handleArrowLeft = (event: React.KeyboardEvent<HTMLSpanElement>) => {
    if (contentEditableReference.current === null) return;

    const allSpans = [...document.querySelectorAll<HTMLSpanElement>("span[contenteditable]")];
    const thisSpanIndex = allSpans.indexOf(contentEditableReference.current);
    const cursorIsAtBeginning = selectService.getCursorOffset() === 0;
    if (!cursorIsAtBeginning) return;

    const previousSpanIndex = thisSpanIndex - 1;
    if (previousSpanIndex === -1) return;

    event.preventDefault();
    const nextFocus = allSpans[previousSpanIndex];
    nextFocus.focus();
    selectService.focusAtOffset(nextFocus.childNodes[0], nextFocus.textContent?.length ?? 0);
  };

  const handleArrowRight = (event: React.KeyboardEvent<HTMLSpanElement>) => {
    if (contentEditableReference.current === null) return;

    const allSpans = [...document.querySelectorAll<HTMLSpanElement>("span[contenteditable]")];
    const thisSpanIndex = allSpans.indexOf(contentEditableReference.current);

    const cursorIsAtEnd = selectService.getCursorOffset() >= content.text.length;
    if (!cursorIsAtEnd) return;

    const nextSpanIndex = thisSpanIndex + 1;

    if (nextSpanIndex > allSpans.length - 1) return;

    event.preventDefault();
    const nextFocus = allSpans[nextSpanIndex];
    nextFocus.focus();
    selectService.focusAtOffset(nextFocus.childNodes[0], 0);
  };

  return (
    <span
      // NOTE: ideally this would be "plaintext-only", and it works in practice.
      // However, the tests will not work if set to plaintext-only. For some reason focus/input and other events will not be triggered by userEvent as expected.
      // This is not documented anywhere I could find and caused a day of frustration, beware
      contentEditable="true"
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
          const element = contentEditableReference.current;
          const caretCoords = getCaretCoords();

          if (element === null || caretCoords === undefined) {
            return;
          }

          const shouldDoItOurselves = !areAnyContentEditableSiblingsPlacedLower(element);
          if (shouldDoItOurselves) {
            const next = findOnLineBelow(element);

            if (next) {
              gotoCoord({ x: caretCoords.x, y: next.top + 10 });
              event.preventDefault();
            }
          }
        }
        if (event.key === "ArrowUp") {
          const element = contentEditableReference.current;
          const caretCoords = getCaretCoords();

          if (element === null || caretCoords === undefined) {
            return;
          }

          const shouldDoItOurselves = !areAnyContentEditableSiblingsPlacedHigher(element);

          if (shouldDoItOurselves) {
            const next = findOnLineAbove(element);

            if (next) {
              gotoCoord({ x: caretCoords.x, y: next.bottom - 10 });
              event.preventDefault();
            }
          }
        }
      }}
      ref={contentEditableReference}
    />
  );
}

function areAnyContentEditableSiblingsPlacedLower(element: HTMLSpanElement) {
  const lastContentEditable = element.parentElement
    ? [...element.parentElement.querySelectorAll(":scope > [contenteditable]")].pop()
    : undefined;
  const caretCoords = getCaretCoords();

  if (lastContentEditable === undefined || caretCoords === undefined) return false; // TODO: should not happen?

  return lastContentEditable.getBoundingClientRect().bottom > caretCoords.bottom;
}

function areAnyContentEditableSiblingsPlacedHigher(element: HTMLSpanElement) {
  const firstContentEditable = element.parentElement
    ? [...element.parentElement.querySelectorAll(":scope > [contenteditable]")][0]
    : undefined;
  const caretCoords = getCaretCoords();

  if (firstContentEditable === undefined || caretCoords === undefined) return false; // TODO: should not happen?
  return caretCoords.top > firstContentEditable.getBoundingClientRect().top;
}

function gotoCoord(coords: { x: number; y: number }) {
  console.log("orig coords", coords);
  const { x, y } = fineAdjustCoordinates(coords);

  const range = document.caretRangeFromPoint(x, y);
  if (range === null) {
    console.log("Could not get caret for position:", x, y);
    return;
  }
  const selection = window.getSelection();
  selection?.removeAllRanges();
  selection?.addRange(range);
}

function fineAdjustCoordinates({ x, y }: { x: number; y: number }) {
  const [clickedElement] = document.elementsFromPoint(x, y);

  const specific = clickedElement.querySelector(":scope > [contenteditable]");

  const seekedElement = specific || clickedElement; // explain why

  const seekedElementIsContenteditable = seekedElement.hasAttribute("contenteditable");
  if (seekedElementIsContenteditable) {
    return { x, y };
  }

  const contentEditableSiblings = seekedElement.parentElement
    ? [...seekedElement.parentElement.querySelectorAll(":scope > [contenteditable]")]
    : undefined;

  const seekedBox = seekedElement.getBoundingClientRect();
  const boxes = contentEditableSiblings?.map((s) => s.getBoundingClientRect());
  const siblingBoxes = boxes?.filter((b) => inRange(b.bottom, seekedBox.top, seekedBox.bottom)); // TODO: not caveats

  const closest = minBy(siblingBoxes, (b) => {
    const distanceFromTheLeft = Math.abs(b.left - x);
    const distanceFromTheRight = Math.abs(b.right - x);

    return Math.min(distanceFromTheLeft, distanceFromTheRight);
  });

  const distanceFromTheLeft = Math.abs(closest.left - x);
  const distanceFromTheRight = Math.abs(closest.right - x);

  const a = distanceFromTheLeft < distanceFromTheRight ? closest.left : closest.right; // TODO: refactor??

  return { x: a, y };
}

function getCaretCoords() {
  const selection = window.getSelection();
  const range = selection?.getRangeAt(0);
  return range?.getBoundingClientRect();
}

function findOnLineBelow(element: Element) {
  const currentBox = element.getBoundingClientRect();

  const allEditables = [...document.querySelectorAll("[contenteditable]")];
  const currentIndex = allEditables.indexOf(element);
  const next = allEditables.slice(currentIndex + 1)[0];

  if (next === undefined) {
    return undefined;
  }

  const nextBox = next.getBoundingClientRect();

  if (currentBox.bottom !== nextBox.bottom) {
    return nextBox;
    // return nextBox.top + 10;
  }

  return findOnLineBelow(next);
}

function findOnLineAbove(element: Element) {
  const currentBox = element.getBoundingClientRect();

  const allEditables = [...document.querySelectorAll("[contenteditable]")];
  const currentIndex = allEditables.indexOf(element);
  const previous = allEditables.slice(0, currentIndex).pop();

  if (previous === undefined) {
    return undefined;
  }

  const previousBox = previous.getBoundingClientRect();

  if (currentBox.bottom !== previousBox.bottom) {
    return previousBox;
    // return previousBox.bottom - 10;
  }

  return findOnLineAbove(previous);
}
