import { produce } from "immer";

import type { AnyBlock, Cell, ParagraphBlock, Row } from "~/types/brevbakerTypes";
import { LITERAL, PARAGRAPH, TABLE } from "~/types/brevbakerTypes";

import { isItemContentIndex, makeBlankRow, newLiteral } from "../actions/common";
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
 * Handles Backspace when the caret is inside a table cell.
 * Returns true if it consumed the key (jumped left or deleted the row).
 */
export function handleBackspaceInTableCell(
  event: React.KeyboardEvent,
  editorState: LetterEditorState,
  updateEditorState: (updater: (prevState: LetterEditorState) => LetterEditorState) => void,
): boolean {
  if (event.key !== "Backspace") return false;
  if (!isItemContentIndex(editorState.focus)) return false;

  const focus = editorState.focus;
  if (focus.itemIndex === -1) return false;
  const paragraphBlock = editorState.redigertBrev.blocks[focus.blockIndex];
  const tableContent = paragraphBlock.content[focus.contentIndex];
  if (tableContent?.type !== TABLE) return false;

  const cursorOffset = getCursorOffset();
  const currentRow = tableContent.rows[focus.itemIndex];
  const currentCell = currentRow.cells[focus.itemContentIndex];

  // Non-empty cell: let browser delete characters.
  // Empty cell: we take over whenever offset ≤ 1.
  // (offset may be 1 because of the zero-width placeholder)
  if (!isCellEmpty(currentCell)) return false;
  if (cursorOffset > 1) return false;

  event.preventDefault();

  // Not first column: jump one cell left
  if (focus.itemContentIndex > 0) {
    updateEditorState((prevState) =>
      produce(prevState, (draftState) => {
        if (isItemContentIndex(draftState.focus)) {
          draftState.focus.itemContentIndex -= 1;
          draftState.focus.cursorPosition = 0;
        }
      }),
    );
    return true;
  }

  // First column: delete row if every cell empty
  if (rowIsEmpty(currentRow)) {
    updateEditorState((prevState) =>
      produce(prevState, (draftState) => {
        const tableDraft = draftState.redigertBrev.blocks[focus.blockIndex].content[focus.contentIndex];
        if (tableDraft?.type !== TABLE) return;

        tableDraft.rows.splice(focus.itemIndex, 1);
        const [removedRow] = tableDraft.rows.splice(focus.itemIndex, 1);
        if (removedRow?.id != null) tableDraft.deletedRows.push(removedRow.id);
        if (tableDraft.rows.length === 0) {
          const paragraphDraft = draftState.redigertBrev.blocks[focus.blockIndex];
          paragraphDraft.content.splice(focus.contentIndex, 1, newLiteral({ editedText: "" }));
          draftState.focus = { blockIndex: focus.blockIndex, contentIndex: focus.contentIndex, cursorPosition: 0 };
        } else {
          const newRowIndex = Math.min(focus.itemIndex, tableDraft.rows.length - 1);
          draftState.focus = {
            blockIndex: focus.blockIndex,
            contentIndex: focus.contentIndex,
            itemIndex: newRowIndex,
            itemContentIndex: 0,
            cursorPosition: 0,
          };
        }
        draftState.isDirty = true;
      }),
    );
    return true;
  }

  // First column but other cells contain text: do nothing special
  return false;
}
