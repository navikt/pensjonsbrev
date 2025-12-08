import { css } from "@emotion/react";
import React, { useEffect, useRef } from "react";

import Actions from "~/Brevredigering/LetterEditor/actions";
import {
  fontTypeOf,
  isBlockContentIndex,
  isItemContentIndex,
  isTable,
  text as textOf,
} from "~/Brevredigering/LetterEditor/actions/common";
import { MergeTarget } from "~/Brevredigering/LetterEditor/actions/merge";
import { logPastedClipboard } from "~/Brevredigering/LetterEditor/actions/paste";
import TableView from "~/Brevredigering/LetterEditor/components/TableView";
import { Text } from "~/Brevredigering/LetterEditor/components/Text";
import { useEditor } from "~/Brevredigering/LetterEditor/LetterEditor";
import { applyAction } from "~/Brevredigering/LetterEditor/lib/actions";
import type { Focus, LiteralIndex } from "~/Brevredigering/LetterEditor/model/state";
import {
  areAnyContentEditableSiblingsPlacedHigher,
  areAnyContentEditableSiblingsPlacedLower,
  findOnLineAbove,
  findOnLineBelow,
  focusAtOffset,
  getCaretRect,
  getCharacterOffset,
  getCursorOffset,
  getCursorOffsetOrRange,
  gotoCoordinates,
} from "~/Brevredigering/LetterEditor/services/caretUtils";
import type { EditedLetter, LiteralValue } from "~/types/brevbakerTypes";
import { NEW_LINE, TABLE, TITLE_INDEX } from "~/types/brevbakerTypes";
import { ElementTags, FontType, ITEM_LIST, LITERAL, VARIABLE } from "~/types/brevbakerTypes";

import { updateFocus } from "../actions/cursorPosition";
import { isTableCellIndex, ZERO_WIDTH_SPACE } from "../model/utils";
import {
  addRow,
  exitTable,
  handleBackspaceInTableCell,
  isAtLastTableCell,
  nextTableFocus,
} from "../services/tableCaretUtils";
import { isMac } from "../utils";

/**
 * When changing lines with ArrowUp/ArrowDown we sometimes "artificially click" the next line.
 * If y-coord is exactly at the edge it sometimes misses. To avoid that we move the point a little bit away from the line.
 */
const Y_COORD_SAFETY_MARGIN = 10;

function getContent(letter: EditedLetter, literalIndex: LiteralIndex) {
  if (literalIndex.blockIndex === TITLE_INDEX) {
    return letter.title.text;
  }
  const content = letter.blocks[literalIndex.blockIndex].content;
  const contentValue = content[literalIndex.contentIndex];
  if ("itemIndex" in literalIndex && contentValue.type === ITEM_LIST) {
    return contentValue.items[literalIndex.itemIndex].content;
  }
  return content;
}

export function ContentGroup({ literalIndex }: { literalIndex: LiteralIndex }) {
  const { editorState } = useEditor();
  const contents = getContent(editorState.redigertBrev, literalIndex);

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
          case NEW_LINE:
          case VARIABLE: {
            return (
              <Text
                content={content}
                key={_contentIndex}
                literalIndex={{
                  blockIndex: literalIndex.blockIndex,
                  contentIndex: _contentIndex,
                }}
              />
            );
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
          case TABLE:
            return (
              <TableView
                blockIndex={literalIndex.blockIndex}
                contentIndex={_contentIndex}
                key={_contentIndex}
                node={content}
              />
            );
        }
      })}
    </div>
  );
}

const hasFocus = (focus: Focus, literalIndex: LiteralIndex) => {
  if (isTableCellIndex(focus) && isTableCellIndex(literalIndex)) {
    return (
      focus.blockIndex === literalIndex.blockIndex &&
      focus.contentIndex === literalIndex.contentIndex &&
      focus.rowIndex === literalIndex.rowIndex &&
      focus.cellIndex === literalIndex.cellIndex &&
      focus.cellContentIndex === literalIndex.cellContentIndex
    );
  } else if (isItemContentIndex(focus) && isItemContentIndex(literalIndex)) {
    return (
      focus.blockIndex === literalIndex.blockIndex &&
      focus.contentIndex === literalIndex.contentIndex &&
      focus.itemIndex === literalIndex.itemIndex &&
      focus.itemContentIndex === literalIndex.itemContentIndex
    );
  } else if (isBlockContentIndex(focus) && isBlockContentIndex(literalIndex)) {
    return focus.blockIndex === literalIndex.blockIndex && focus.contentIndex === literalIndex.contentIndex;
  }
  return false;
};

// True when a fritekst has a live selection covering its entire text;
// used to avoid collapsing it to a caret so first key press removes placeholder.
const shouldPreserveFullSelection = (isFritekst: boolean, element: HTMLElement): boolean => {
  if (!isFritekst) return false;
  const sel = globalThis.getSelection();
  if (!sel || sel.rangeCount === 0 || sel.isCollapsed) return false;
  const r = sel.getRangeAt(0);
  if (!element.contains(r.startContainer) || !element.contains(r.endContainer)) return false;
  const fullText = element.textContent ?? "";
  return r.startOffset === 0 && r.endOffset === fullText.length;
};

export function EditableText({ literalIndex, content }: { literalIndex: LiteralIndex; content: LiteralValue }) {
  const contentEditableReference = useRef<HTMLSpanElement>(null);
  const { freeze, editorState, setEditorState, undo, redo } = useEditor();

  const shouldBeFocused = hasFocus(editorState.focus, literalIndex);

  // hvis teksten har endret seg, skal elementet oppføre seg som en helt vanlig literal
  const erFritekst =
    content.tags.includes(ElementTags.FRITEKST) &&
    (content.editedText === null || content.editedText === undefined || content.editedText === content.text);

  const text = textOf(content) || ZERO_WIDTH_SPACE;

  useEffect(() => {
    const element = contentEditableReference.current;
    if (!element) return;

    if (element.textContent !== text) {
      element.textContent = text;
    }

    if (!freeze && shouldBeFocused) {
      // Preserve full selection for untouched fritekst placeholders (first key should replace it).
      if (shouldPreserveFullSelection(erFritekst, element)) {
        return;
      }

      // If we do NOT yet have a stored cursorPosition, respect any existing DOM caret/selection.
      if (editorState.focus.cursorPosition === undefined) {
        const selection = globalThis.getSelection();
        if (
          selection &&
          selection.rangeCount > 0 &&
          element.contains(selection.anchorNode) &&
          element.contains(selection.focusNode)
        ) {
          // Do not set fallback or move caret yet.
          return;
        }

        // No stored cursor yet and no valid selection, fall back to end of text.
        const fallbackCursorPosition = text.length;
        setEditorState((s) => ({
          ...s,
          focus: { ...s.focus, cursorPosition: fallbackCursorPosition },
        }));
        if (element.childNodes[0]) {
          focusAtOffset(element.childNodes[0], fallbackCursorPosition);
        }
        return;
      }

      // Normal path once cursorPosition is known: clamp and restore.
      const resolvedCursorPosition = Math.min(editorState.focus.cursorPosition, text.length);
      if (element.childNodes[0]) {
        focusAtOffset(element.childNodes[0], resolvedCursorPosition);
      }
    }
  }, [text, shouldBeFocused, editorState.focus.cursorPosition, freeze, setEditorState, erFritekst]);

  const handleEnter = (event: React.KeyboardEvent<HTMLSpanElement>) => {
    event.preventDefault();
    const offset = getCursorOffset();
    if (event.shiftKey) {
      applyAction(Actions.addNewLine, setEditorState, { ...literalIndex, cursorPosition: offset });
    } else {
      applyAction(Actions.split, setEditorState, literalIndex, offset);
    }
  };

  const handleBackspace = (event: React.KeyboardEvent<HTMLSpanElement>) => {
    const cursorPosition = getCursorOffset();
    if (
      cursorPosition === 0 ||
      (contentEditableReference.current?.textContent?.startsWith(ZERO_WIDTH_SPACE) && cursorPosition === 1)
    ) {
      event.preventDefault();
      applyAction(Actions.merge, setEditorState, literalIndex, MergeTarget.PREVIOUS);
    }
  };

  const handleDelete = (event: React.KeyboardEvent<HTMLSpanElement>) => {
    const cursorIsAtEnd = getCursorOffset() >= (text === ZERO_WIDTH_SPACE ? 0 : text.length);
    if (cursorIsAtEnd) {
      event.preventDefault();
      applyAction(Actions.merge, setEditorState, literalIndex, MergeTarget.NEXT);
    }
  };

  /**
   * Vi har 2 forventinger når vi taster venste-pil:
   * 1. Hvis vi er i et fritekst-felt, og den er markert:
   *  - Skal første tast fjerne markeringen og sette markøren til starten av teksten
   *  - Skal andre tast flytte markøren til neste posisjon
   * 2. Hvis vi er i en vanlig literal (eller en redigert fritekst-felt):
   *  - skal tast alltid flytte posisjon
   */
  const handleArrowLeft = (event: React.KeyboardEvent<HTMLSpanElement>) => {
    if (contentEditableReference.current === null) return;

    const allSpans = [...document.querySelectorAll<HTMLSpanElement>("span[contenteditable]")];
    const thisSpanIndex = allSpans.indexOf(contentEditableReference.current);

    const cursorOffsetOrRange = getCursorOffsetOrRange();
    if (cursorOffsetOrRange === undefined) return;

    const isCursorOffset = typeof cursorOffsetOrRange === "number";
    const isRange = !isCursorOffset;

    if (isRange && erFritekst) {
      const nextFocus = allSpans[thisSpanIndex];
      focusAtOffset(nextFocus?.childNodes[0], cursorOffsetOrRange.startOffset);
    } else {
      const cursorIsAtBeginning = isCursorOffset ? cursorOffsetOrRange === 0 : cursorOffsetOrRange.startOffset === 0;

      if (!cursorIsAtBeginning) return;

      const previousSpanIndex = thisSpanIndex - 1;
      if (previousSpanIndex < 0) return;

      const isPreviousSpanInSameBlock =
        allSpans[previousSpanIndex].parentElement === contentEditableReference.current.parentElement;

      event.preventDefault();
      const nextFocus = allSpans[previousSpanIndex];

      focusAtOffset(
        nextFocus?.childNodes[0],
        isPreviousSpanInSameBlock ? (nextFocus.textContent?.length ?? 0) - 1 : (nextFocus.textContent?.length ?? 0),
      );
    }
  };

  /**
   * Vi har 2 forventinger når vi taster høyre-pil:
   * 1. Hvis vi er i et fritekst-felt, og den er markert:
   *  - Skal første tast fjerne markeringen og sette markøren til slutten av teksten
   *  - Skal andre tast flytte markøren til neste posisjon
   * 2. Hvis vi er i en vanlig literal (eller en redigert fritekst-felt):
   *  - skal tast alltid flytte posisjon
   */
  const handleArrowRight = (event: React.KeyboardEvent<HTMLSpanElement>) => {
    if (contentEditableReference.current === null || event.shiftKey) return;

    const allSpans = [...document.querySelectorAll<HTMLSpanElement>("span[contenteditable]")];
    const thisSpanIndex = allSpans.indexOf(contentEditableReference.current);

    const cursorOffsetOrRange = getCursorOffsetOrRange();
    if (cursorOffsetOrRange === undefined) return;

    const isCursorOffset = typeof cursorOffsetOrRange === "number";
    const isRange = !isCursorOffset;

    if (isRange && erFritekst) {
      const nextFocus = allSpans[thisSpanIndex];
      focusAtOffset(nextFocus?.childNodes[0], cursorOffsetOrRange.endOffset);
    } else {
      const textLength = text === ZERO_WIDTH_SPACE ? 0 : text.length;
      const cursorIsAtEnd = isCursorOffset
        ? cursorOffsetOrRange >= textLength
        : cursorOffsetOrRange.endOffset >= textLength;

      if (!cursorIsAtEnd) return;

      const nextSpanIndex = thisSpanIndex + 1;
      if (nextSpanIndex > allSpans.length - 1) return;

      const isNextSpanInSameBlock =
        allSpans[nextSpanIndex].parentElement === contentEditableReference.current.parentElement;

      event.preventDefault();
      const nextFocus = allSpans[nextSpanIndex];
      focusAtOffset(nextFocus?.childNodes[0], isNextSpanInSameBlock ? 1 : 0);
    }
  };

  const handleArrowUp = (event: React.KeyboardEvent<HTMLSpanElement>) => {
    const element = contentEditableReference.current;
    const caretCoordinates = getCaretRect();

    if (element === null || caretCoordinates === undefined || event.shiftKey) {
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

    if (element === null || caretCoordinates === undefined || event.shiftKey) {
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

  const handleTab = (event: React.KeyboardEvent<HTMLSpanElement>): boolean => {
    const { focus } = editorState;

    if (
      !isTableCellIndex(focus) ||
      !isTable(editorState.redigertBrev.blocks[focus.blockIndex]?.content[focus.contentIndex])
    ) {
      return false;
    }

    const direction = event.shiftKey ? "backward" : "forward";
    event.preventDefault();
    // If we're at the last cell and tabbing forward, append a row and stop here.
    if (direction === "forward" && isAtLastTableCell(editorState)) {
      addRow(editorState, setEditorState, event);
      return true;
    }

    // Otherwise, ask nextTableFocus helper where to go inside the table
    const next = nextTableFocus(editorState, direction);

    // nextTableFocus returns the same focus when we're at an edge,
    // so "no movement" means we should exit the table.
    const didMove =
      isTableCellIndex(next) &&
      (next.rowIndex !== focus.rowIndex ||
        next.cellIndex !== focus.cellIndex ||
        next.cellContentIndex !== focus.cellContentIndex);

    if (didMove) {
      // Move caret within the table
      setEditorState((prev) => ({ ...prev, focus: next }));
    } else {
      // At an edge, exit table
      setEditorState(exitTable(direction));
    }
    return true;
  };

  const handleOnPaste = (event: React.ClipboardEvent<HTMLSpanElement>) => {
    event.preventDefault();
    // TODO: for debugging frem til vi er ferdig å teste liming
    logPastedClipboard(event.clipboardData);

    const offset = getCursorOffset();
    if (offset >= 0) {
      applyAction(Actions.paste, setEditorState, literalIndex, offset, event.clipboardData);
    }
  };

  const handleOnInput = ({ currentTarget }: React.FormEvent<HTMLSpanElement>) => {
    const postEditCursorPosition = getCharacterOffset(currentTarget);
    applyAction(
      Actions.updateContentText,
      setEditorState,
      literalIndex,
      currentTarget.textContent ?? "",
      postEditCursorPosition,
    );
  };

  const handleOnKeyDown = (e: React.KeyboardEvent<HTMLSpanElement>) => {
    const selection = globalThis.getSelection();
    const hasRange = !!selection && selection.rangeCount > 0 && !selection.getRangeAt(0).collapsed;

    if (hasRange && (e.key === "Backspace" || e.key === "Delete")) {
      return; // bubble to wrapper div and handle there
    }
    const isUndo = (isMac ? e.metaKey : e.ctrlKey) && e.key === "z" && !e.shiftKey;
    const isRedo = (isMac ? e.metaKey : e.ctrlKey) && (e.key === "y" || (e.key === "z" && e.shiftKey));

    if (isUndo) {
      e.preventDefault();
      e.stopPropagation();
      undo();
      return;
    }
    if (isRedo) {
      e.preventDefault();
      e.stopPropagation();
      redo();
      return;
    }

    const isEditingKey =
      !e.ctrlKey &&
      !e.altKey &&
      !e.metaKey &&
      (e.key.length === 1 || e.key === "Backspace" || e.key === "Delete" || e.key === "Enter");

    if (isEditingKey && contentEditableReference.current) {
      const selection = globalThis.getSelection();
      const preEditCursorPosition = getCharacterOffset(contentEditableReference.current);
      // Store the caret position before a text-changing key
      // if it changed or focus moved, so undo/redo can restore the correct pre-edit cursor.
      if (editorState.focus.cursorPosition !== preEditCursorPosition || !hasFocus(editorState.focus, literalIndex)) {
        applyAction(updateFocus, setEditorState, {
          ...literalIndex,
          cursorPosition: selection?.isCollapsed ? preEditCursorPosition : undefined,
        });
      }
    }

    if (e.key === "Backspace") {
      if (handleBackspaceInTableCell(e, editorState, setEditorState)) return;

      handleBackspace(e);
      e.stopPropagation();
      return;
    }

    if (e.key === "Tab") {
      handleTab(e);
      return;
    }
    if (e.key === "Enter") {
      handleEnter(e);
      e.stopPropagation();
    }
    if (e.key === "Delete") {
      handleDelete(e);
      e.stopPropagation();
    }
    if (e.key === "ArrowLeft") {
      handleArrowLeft(e);
    }
    if (e.key === "ArrowRight") {
      handleArrowRight(e);
    }
    if (e.key === "ArrowDown") {
      handleArrowDown(e);
    }
    if (e.key === "ArrowUp") {
      handleArrowUp(e);
    }
  };

  const handleOnMouseDown = (e: React.MouseEvent) => {
    if (!erFritekst) return;

    // Tøm markering for å restarte dra-og-marker
    const selection = globalThis.getSelection();
    if (selection && !selection.isCollapsed && selection.containsNode(e.currentTarget, true)) {
      selection.collapse(e.currentTarget);
    }

    // Blokker dobbeltklikk-markering av enkeltord for å heller markere hele friteksten
    if (erFritekst && e.detail === 2) {
      e.preventDefault();
    }
  };

  const handleOnFocus = (e: React.FocusEvent<HTMLSpanElement>) => {
    // I word vil endring av fonttype beholde markering av teksten, mens denne focus state endringen vil fjerne markeringen
    const offset = getCursorOffset();
    setEditorState((oldState) => ({
      ...oldState,
      focus: { ...literalIndex, ...(offset && { cursorPosition: offset }) },
    }));
    if (!erFritekst) return;
    e.preventDefault();
    setSelection(e.currentTarget);
  };

  const handleOnClick = (e: React.MouseEvent<HTMLSpanElement>) => {
    if (!erFritekst) return;
    e.preventDefault();
    const selection = globalThis.getSelection();
    if (selection) {
      const span = e.currentTarget;
      // Elementet er allerede fokusert/markert eller har markør
      if (span.contains(selection.anchorNode) && span.contains(selection.focusNode)) {
        return;
      }
      const isDoubleClick = e.detail === 2;
      if (selection?.isCollapsed && !isDoubleClick) {
        setSelection(span);
      }
    }
  };

  const handleOnDoubleClick = (e: React.MouseEvent<HTMLSpanElement>) => {
    if (!erFritekst) return;
    // Blokker dobbeltklikk-markering av enkeltord for å heller markere hele friteksten
    e.preventDefault();
    e.stopPropagation();
    setSelection(e.currentTarget);
  };

  const setSelection = (element: HTMLSpanElement) => {
    const selection = globalThis.getSelection();
    if (selection) {
      const range = document.createRange();
      selection.removeAllRanges();
      range.selectNodeContents(element.childNodes[0]);
      selection.addRange(range);
    }
  };

  return (
    <span
      // contentEditable='plaintext-only' blocks rich text content and prevents
      // unhandled native bold/italic/underline formatting from interfering with
      // Skribenten-styling. However, Cypress and jsdom/happy-dom do not handle
      // 'plaintext-only' well, and browser native formatting shortcuts and
      // pasting can be blocked/overridden in event handlers.
      contentEditable={!freeze}
      css={css({
        ...(erFritekst && {
          color: "var(--ax-accent-600)",
          textDecoration: "underline",
          cursor: "pointer",
        }),
        ...(fontTypeOf(content) === FontType.BOLD && { fontWeight: "bold" }),
        ...(fontTypeOf(content) === FontType.ITALIC && { fontStyle: "italic" }),
      })}
      data-literal-index={JSON.stringify(literalIndex)}
      onClick={handleOnClick}
      onDoubleClick={handleOnDoubleClick}
      onFocus={handleOnFocus}
      onInput={handleOnInput}
      onKeyDown={handleOnKeyDown}
      onMouseDown={handleOnMouseDown}
      onPaste={handleOnPaste}
      ref={contentEditableReference}
      tabIndex={erFritekst ? 0 : -1}
    />
  );
}
