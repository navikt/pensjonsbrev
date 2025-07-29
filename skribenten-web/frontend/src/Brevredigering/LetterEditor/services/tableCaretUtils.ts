import type { Draft } from "immer";
import { produce } from "immer";

import type { AnyBlock, Cell, ParagraphBlock, Row, Table } from "~/types/brevbakerTypes";
import { LITERAL, PARAGRAPH, TABLE } from "~/types/brevbakerTypes";

import { newLiteral, newRow } from "../actions/common";
import type { Focus, LetterEditorState } from "../model/state";
import { isTableCellIndex } from "../model/utils";
import { getCursorOffset } from "./caretUtils";

// Result of a Tab move from inside a table cell.
export type MoveResult = Focus | "EXIT_FORWARD" | "EXIT_BACKWARD";

/**
 * Determines the next focus when user presses Tab/Shift-Tab inside a table.
 */
export function nextTableFocus(editorState: LetterEditorState, direction: "forward" | "backward"): MoveResult {
  const currentFocus = editorState.focus;
  if (!isTableCellIndex(currentFocus)) return currentFocus;

  const currentBlock = editorState.redigertBrev.blocks[currentFocus.blockIndex];
  const currentTable = currentBlock.content[currentFocus.contentIndex];
  if (currentTable?.type !== TABLE) return currentFocus;

  const inHeaderRow = currentFocus.rowIndex === -1;
  const headerColumns = currentTable.header.colSpec.length;

  const totalRows = currentTable.rows.length;
  const totalColumns = currentTable.rows[currentFocus.rowIndex]?.cells.length ?? 0;

  if (direction === "forward") {
    if (inHeaderRow) {
      if (currentFocus.cellContentIndex < headerColumns - 1) {
        return { ...currentFocus, cellContentIndex: currentFocus.cellContentIndex + 1, cursorPosition: 0 };
      }
      if (totalRows > 0) {
        return { ...currentFocus, rowIndex: 0, cellContentIndex: 0, cursorPosition: 0 };
      }
      return "EXIT_FORWARD";
    }

    if (currentFocus.cellContentIndex < totalColumns - 1) {
      return { ...currentFocus, cellContentIndex: currentFocus.cellContentIndex + 1, cursorPosition: 0 };
    }
    if (currentFocus.rowIndex < totalRows - 1) {
      return { ...currentFocus, rowIndex: currentFocus.rowIndex + 1, cellContentIndex: 0, cursorPosition: 0 };
    }
    return "EXIT_FORWARD";
  } else {
    if (inHeaderRow) {
      if (currentFocus.cellContentIndex > 0) {
        return { ...currentFocus, cellContentIndex: currentFocus.cellContentIndex - 1, cursorPosition: 0 };
      }
      return "EXIT_BACKWARD";
    }

    if (currentFocus.cellContentIndex > 0) {
      return { ...currentFocus, cellContentIndex: currentFocus.cellContentIndex - 1, cursorPosition: 0 };
    }

    if (currentFocus.rowIndex > 0) {
      const previousRowColumns = currentTable.rows[currentFocus.rowIndex - 1].cells.length;
      return {
        ...currentFocus,
        rowIndex: currentFocus.rowIndex - 1,
        cellContentIndex: previousRowColumns - 1,
        cursorPosition: 0,
      };
    }

    if (headerColumns > 0) {
      return { ...currentFocus, rowIndex: -1, cellContentIndex: headerColumns - 1, cursorPosition: 0 };
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

  if (currentContent?.type !== TABLE || !isTableCellIndex(editorState.focus)) {
    return false;
  }

  keyboardEvent.preventDefault();
  updateEditorState((prevState) => {
    if (!isTableCellIndex(prevState.focus)) return prevState;
    const currentTable = prevState.redigertBrev.blocks[prevState.focus.blockIndex].content[
      prevState.focus.contentIndex
    ] as typeof currentContent;

    const currentRowIndex = prevState.focus.rowIndex;
    const currentColIndex = prevState.focus.cellContentIndex;
    const isLastRow = currentRowIndex === currentTable.rows.length - 1;

    const columnCount =
      currentTable.header.colSpec.length > 0 ? currentTable.header.colSpec.length : currentTable.rows[0].cells.length;
    const updatedRows = isLastRow ? [...currentTable.rows, newRow(columnCount)] : currentTable.rows;

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
        rowIndex: isLastRow ? updatedRows.length - 1 : currentRowIndex + 1,
        cellContentIndex: currentColIndex,
        cursorPosition: 0,
      },
    };
  });

  return true;
}

// Zero-width space, used as a placeholder for empty cells.
const ZERO_WIDTH = /\u200B/g;

export function isCellEmpty(cell: Cell): boolean {
  return cell.text.every((textContent) => {
    if (textContent.type !== LITERAL) return false;
    const cleanedText = (textContent.editedText ?? textContent.text).replace(ZERO_WIDTH, "");
    return cleanedText.trim() === "";
  });
}

export function rowIsEmpty(row: Row): boolean {
  return row.cells.every(isCellEmpty);
}

/**
 * Handles **Shift + Backspace** inside a table cell.
 *
 * • When the caret is at the very beginning (offset 0 or 1) of *any* cell in a
 *   row **and the entire row is empty**, the row is deleted.
 * • If it was the last remaining row, the whole table is replaced by a blank
 *   paragraph.
 *
 * Returns **true** when the key event is consumed; otherwise **false** so the
 * browser performs its normal action.
 */
export function handleBackspaceInTableCell(
  event: React.KeyboardEvent,

  editorState: LetterEditorState,
  updateEditorState: (updater: (prev: LetterEditorState) => LetterEditorState) => void,
): boolean {
  if (event.key !== "Backspace" || !event.shiftKey) return false;
  if (!isTableCellIndex(editorState.focus)) return false;

  const focus = editorState.focus;

  // header row: let the browser delete text normally
  if (focus.rowIndex === -1) return false;

  const paragraphBlock = editorState.redigertBrev.blocks[focus.blockIndex];
  const tableContent = paragraphBlock.content[focus.contentIndex];
  if (tableContent?.type !== TABLE) return false;

  const cursorOffset = getCursorOffset();
  const currentRow = tableContent.rows[focus.rowIndex];

  const caretAtStartOfCell = cursorOffset <= 1;
  if (!caretAtStartOfCell || !rowIsEmpty(currentRow)) return false;

  event.preventDefault();

  updateEditorState((prev) =>
    produce(prev, (draft) => {
      const table = draft.redigertBrev.blocks[focus.blockIndex].content[focus.contentIndex] as Draft<Table>;

      const [removed] = table.rows.splice(focus.rowIndex, 1);
      if (removed?.id != null) table.deletedRows.push(removed.id);

      if (table.rows.length === 0) {
        const paragraph = draft.redigertBrev.blocks[focus.blockIndex] as Draft<ParagraphBlock>;
        paragraph.content.splice(focus.contentIndex, 1, newLiteral({ editedText: "" }));
        draft.focus = { blockIndex: focus.blockIndex, contentIndex: focus.contentIndex, cursorPosition: 0 };
      } else {
        const newRowIndex = Math.min(focus.rowIndex, table.rows.length - 1);
        draft.focus = {
          blockIndex: focus.blockIndex,
          contentIndex: focus.contentIndex,
          rowIndex: newRowIndex,
          cellContentIndex: 0,
          cursorPosition: 0,
        };
      }

      draft.isDirty = true;
    }),
  );

  return true;
}
