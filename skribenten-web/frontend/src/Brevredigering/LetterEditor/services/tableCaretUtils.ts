import type { Draft } from "immer";
import { produce } from "immer";

import type { AnyBlock, Cell, ParagraphBlock, Row, Table } from "~/types/brevbakerTypes";
import { LITERAL, PARAGRAPH, TABLE } from "~/types/brevbakerTypes";

import { isItemContentIndex, newLiteral, newRow } from "../actions/common";
import type { Focus, LetterEditorState } from "../model/state";
import { getCursorOffset } from "./caretUtils";

// Result of a Tab move from inside a table cell.
export type MoveResult = Focus | "EXIT_FORWARD" | "EXIT_BACKWARD";

/**
 * Determines the next focus when user presses Tab/Shift-Tab inside a table.
 */
export function nextTableFocus(editorState: LetterEditorState, direction: "forward" | "backward"): MoveResult {
  const currentFocus = editorState.focus;
  if (!isItemContentIndex(currentFocus)) return currentFocus;

  const currentBlock = editorState.redigertBrev.blocks[currentFocus.blockIndex];
  const currentTable = currentBlock.content[currentFocus.contentIndex];
  if (currentTable?.type !== TABLE) return currentFocus;

  const inHeaderRow = currentFocus.itemIndex === -1;
  const headerColumns = currentTable.header.colSpec.length;

  const totalRows = currentTable.rows.length;
  const totalColumns = currentTable.rows[currentFocus.itemIndex]?.cells.length ?? 0;

  if (direction === "forward") {
    if (inHeaderRow) {
      if (currentFocus.itemContentIndex < headerColumns - 1) {
        return { ...currentFocus, itemContentIndex: currentFocus.itemContentIndex + 1, cursorPosition: 0 };
      }
      if (totalRows > 0) {
        return { ...currentFocus, itemIndex: 0, itemContentIndex: 0, cursorPosition: 0 };
      }
      return "EXIT_FORWARD";
    }

    if (currentFocus.itemContentIndex < totalColumns - 1) {
      return { ...currentFocus, itemContentIndex: currentFocus.itemContentIndex + 1, cursorPosition: 0 };
    }
    if (currentFocus.itemIndex < totalRows - 1) {
      return { ...currentFocus, itemIndex: currentFocus.itemIndex + 1, itemContentIndex: 0, cursorPosition: 0 };
    }
    return "EXIT_FORWARD";
  } else {
    if (inHeaderRow) {
      if (currentFocus.itemContentIndex > 0) {
        return { ...currentFocus, itemContentIndex: currentFocus.itemContentIndex - 1, cursorPosition: 0 };
      }
      return "EXIT_BACKWARD";
    }

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

    if (headerColumns > 0) {
      return { ...currentFocus, itemIndex: -1, itemContentIndex: headerColumns - 1, cursorPosition: 0 };
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
        itemIndex: isLastRow ? updatedRows.length - 1 : currentRowIndex + 1,
        itemContentIndex: currentColIndex,
        cursorPosition: 0,
      },
    };
  });

  return true;
}

export type SelectResult =
  | "AT_TOP"
  | "AT_BOTTOM"
  | {
      blockIndex: number;
      contentIndex: number;
      rowIndex: number;
    };

export function selectTableRow(state: LetterEditorState, direction: "up" | "down"): SelectResult {
  const focus = state.focus;
  if (!isItemContentIndex(focus)) return "AT_TOP";

  const paragraphBlock = state.redigertBrev.blocks[focus.blockIndex];
  const tableContent = paragraphBlock.content[focus.contentIndex];
  if (tableContent?.type !== TABLE) return "AT_TOP";

  // if no rows selected yet, start with caret's current row
  if (!state.tableSelection) {
    return {
      blockIndex: focus.blockIndex,
      contentIndex: focus.contentIndex,
      rowIndex: focus.itemIndex,
    };
  }

  // Move selection up or down
  const selectedRowIndex = state.tableSelection.rowIndex;

  if (selectedRowIndex === undefined) return "AT_TOP";

  if (direction === "up") {
    if (selectedRowIndex === 0) return "AT_TOP";
    return {
      blockIndex: focus.blockIndex,
      contentIndex: focus.contentIndex,
      rowIndex: selectedRowIndex - 1,
    };
  } else {
    if (selectedRowIndex === tableContent.rows.length - 1) return "AT_BOTTOM";
    return {
      blockIndex: focus.blockIndex,
      contentIndex: focus.contentIndex,
      rowIndex: selectedRowIndex + 1,
    };
  }
}

export function deleteRow(
  editorState: LetterEditorState,
  updateEditorState: (updater: (prevState: LetterEditorState) => LetterEditorState) => void,
) {
  const selection = editorState.tableSelection;
  if (!selection) return;

  updateEditorState((prevState) =>
    produce(prevState, (draftState) => {
      const table = draftState.redigertBrev.blocks[selection.blockIndex].content[selection.contentIndex];
      if (table?.type !== TABLE) return;

      const [removedRow] = table.rows.splice(selection.rowIndex ?? 0, 1);
      if (removedRow?.id != null) table.deletedRows.push(removedRow.id);

      // When no rows remain, delete the table entirely
      if (table.rows.length === 0) {
        const paragraph = draftState.redigertBrev.blocks[selection.blockIndex];
        paragraph.content.splice(selection.contentIndex, 1, newLiteral({ editedText: "" }));
        draftState.focus = {
          blockIndex: selection.blockIndex,
          contentIndex: selection.contentIndex,
          cursorPosition: 0,
        };
      } else {
        const newRowIndex = Math.min(selection.rowIndex ?? 0, table.rows.length - 1);
        draftState.focus = {
          blockIndex: selection.blockIndex,
          contentIndex: selection.contentIndex,
          itemIndex: newRowIndex,
          itemContentIndex: 0,
          cursorPosition: 0,
        };
      }

      draftState.tableSelection = undefined;
      draftState.isDirty = true;
    }),
  );
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
  if (!isItemContentIndex(editorState.focus)) return false;

  const focus = editorState.focus;

  // header row: let the browser delete text normally
  if (focus.itemIndex === -1) return false;

  const paragraphBlock = editorState.redigertBrev.blocks[focus.blockIndex];
  const tableContent = paragraphBlock.content[focus.contentIndex];
  if (tableContent?.type !== TABLE) return false;

  const cursorOffset = getCursorOffset();
  const currentRow = tableContent.rows[focus.itemIndex];

  const caretAtStartOfCell = cursorOffset <= 1;
  if (!caretAtStartOfCell || !rowIsEmpty(currentRow)) return false;

  event.preventDefault();

  updateEditorState((prev) =>
    produce(prev, (draft) => {
      const table = draft.redigertBrev.blocks[focus.blockIndex].content[focus.contentIndex] as Draft<Table>;

      const [removed] = table.rows.splice(focus.itemIndex, 1);
      if (removed?.id != null) table.deletedRows.push(removed.id);

      if (table.rows.length === 0) {
        const paragraph = draft.redigertBrev.blocks[focus.blockIndex] as Draft<ParagraphBlock>;
        paragraph.content.splice(focus.contentIndex, 1, newLiteral({ editedText: "" }));
        draft.focus = { blockIndex: focus.blockIndex, contentIndex: focus.contentIndex, cursorPosition: 0 };
      } else {
        const newRowIndex = Math.min(focus.itemIndex, table.rows.length - 1);
        draft.focus = {
          blockIndex: focus.blockIndex,
          contentIndex: focus.contentIndex,
          itemIndex: newRowIndex,
          itemContentIndex: 0,
          cursorPosition: 0,
        };
      }

      draft.isDirty = true;
    }),
  );

  return true;
}
