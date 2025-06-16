import type { AnyBlock, ParagraphBlock } from "~/types/brevbakerTypes";
import { PARAGRAPH, TABLE } from "~/types/brevbakerTypes";

import { isItemContentIndex, makeBlankRow } from "../actions/common";
import type { Focus, LetterEditorState } from "../model/state";

/** Result of a Tab move from inside a table cell. */
export type MoveResult = Focus | "EXIT_FORWARD" | "EXIT_BACKWARD";

/**
 * Calculates the next focus when user presses Tab/Shift-Tab inside a table.
 */
export function nextTableFocus(editorState: LetterEditorState, direction: "forward" | "backward"): MoveResult {
  const currentFocus = editorState.focus;
  if (!isItemContentIndex(currentFocus)) return currentFocus;

  const currentBlock = editorState.redigertBrev.blocks[currentFocus.blockIndex];
  const currentTable = currentBlock.content[currentFocus.contentIndex];
  if (currentTable?.type !== TABLE) return currentFocus;

  const totalRows = currentTable.rows.length;
  const totalColumns = currentTable.rows[currentFocus.itemIndex]?.cells.length ?? 0;

  if (direction === "forward") {
    if (currentFocus.itemContentIndex < totalColumns - 1) {
      return { ...currentFocus, itemContentIndex: currentFocus.itemContentIndex + 1, cursorPosition: 0 };
    }
    if (currentFocus.itemIndex < totalRows - 1) {
      return { ...currentFocus, itemIndex: currentFocus.itemIndex + 1, itemContentIndex: 0, cursorPosition: 0 };
    }
    return "EXIT_FORWARD";
  } else {
    if (currentFocus.itemContentIndex > 0) {
      return { ...currentFocus, itemContentIndex: currentFocus.itemContentIndex - 1, cursorPosition: 0 };
    }
    if (currentFocus.itemIndex > 0) {
      const previousRowColumns = currentTable.rows[currentFocus.itemIndex - 1].cells.length;
      return {
        ...currentFocus,
        itemIndex: currentFocus.itemIndex - 1,
        itemContentIndex: previousRowColumns - 1,
        cursorPosition: 0,
      };
    }
    return "EXIT_BACKWARD";
  }
}

export function addRow(
  editorState: LetterEditorState,
  updateEditorState: (updater: (prevState: LetterEditorState) => LetterEditorState) => void,
  keyboardEvent: React.KeyboardEvent,
): boolean {
  const currentBlock = editorState.redigertBrev.blocks[editorState.focus.blockIndex];
  const currentContent = currentBlock.content[editorState.focus.contentIndex];

  if (currentContent?.type !== TABLE || !isItemContentIndex(editorState.focus)) {
    return false;
  }
  if (keyboardEvent.shiftKey) {
    // Shift+Enter => fall back to normal newline
    return false;
  }

  keyboardEvent.preventDefault();
  updateEditorState((prevState) => {
    if (!isItemContentIndex(prevState.focus)) return prevState;
    const currentTable = prevState.redigertBrev.blocks[prevState.focus.blockIndex].content[
      prevState.focus.contentIndex
    ] as typeof currentContent;

    const currentRowIndex = prevState.focus.itemIndex;
    const currentColIndex = prevState.focus.itemContentIndex;
    const isLastRow = currentRowIndex === currentTable.rows.length - 1;

    const columnCount =
      currentTable.header.colSpec.length > 0 ? currentTable.header.colSpec.length : currentTable.rows[0].cells.length;
    const updatedRows = isLastRow ? [...currentTable.rows, makeBlankRow(columnCount)] : currentTable.rows;

    const updatedTable = { ...currentTable, rows: updatedRows };

    const updatedBlocks: AnyBlock[] = prevState.redigertBrev.blocks.map((block, blockIndex) => {
      if (blockIndex !== prevState.focus.blockIndex) return block;

      if (block.type !== PARAGRAPH) return block;
      const paragraph = block as ParagraphBlock;

      return {
        ...paragraph,
        content: paragraph.content.map((content, contentIndex) =>
          contentIndex !== prevState.focus.contentIndex ? content : updatedTable,
        ),
      };
    });

    return {
      ...prevState,
      redigertBrev: {
        ...prevState.redigertBrev,
        blocks: updatedBlocks,
      },
      focus: {
        ...prevState.focus,
        itemIndex: isLastRow ? updatedRows.length - 1 : currentRowIndex + 1,
        itemContentIndex: currentColIndex,
        cursorPosition: 0,
      },
    };
  });

  return true;
}
