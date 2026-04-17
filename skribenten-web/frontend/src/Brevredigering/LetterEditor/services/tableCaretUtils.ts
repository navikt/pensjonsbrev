import { type Draft, produce } from "immer";

import {
  type Cell,
  PARAGRAPH,
  type ParagraphBlock,
  TITLE1,
  TITLE2,
  TITLE3,
  type Title1Block,
  type Title2Block,
  type Title3Block,
} from "~/types/brevbakerTypes";

import { addElements, isTable, newLiteral, newRow } from "../actions/common";
import { type Focus, type LetterEditorState } from "../model/state";
import { isEmptyContentList, isTableCellIndex } from "../model/utils";
import { getCursorOffset } from "./caretUtils";

export type MoveResult = Focus;

/**
 * Computes the next logical focus position within a table, for Tab/Shift+Tab navigation.
 * Returns the same focus if at the start/end (caller decides whether to exit).
 */
export function nextTableFocus(editorState: LetterEditorState, direction: "forward" | "backward"): MoveResult {
  const currentFocus = editorState.focus;
  if (!isTableCellIndex(currentFocus)) {
    return { ...currentFocus };
  }

  const block = editorState.redigertBrev.blocks[currentFocus.blockIndex];
  const content = block.content[currentFocus.contentIndex];
  if (!isTable(content)) {
    return { ...currentFocus };
  }

  const table = content;

  const headerCols = table.header?.colSpec?.length ?? 0;
  const headerPointers = Array.from({ length: headerCols }, (_, c) => ({ rowIndex: -1, cellIndex: c }));

  const bodyPointers = table.rows.flatMap((row, r) => (row.cells ?? []).map((_, c) => ({ rowIndex: r, cellIndex: c })));

  const pointers = [...headerPointers, ...bodyPointers];

  // If there are no cells, just return a new copy of the original focus.
  if (pointers.length === 0) {
    return { ...currentFocus };
  }

  // Find our position in that flat list
  const idx = pointers.findIndex((p) => p.rowIndex === currentFocus.rowIndex && p.cellIndex === currentFocus.cellIndex);
  if (idx === -1) {
    return { ...currentFocus };
  }

  const nextIdx = direction === "forward" ? idx + 1 : idx - 1;
  // Off the ends? return a new copy of the original focus.
  if (nextIdx < 0 || nextIdx >= pointers.length) {
    return { ...currentFocus };
  }

  const { rowIndex, cellIndex } = pointers[nextIdx];
  return {
    ...currentFocus,
    rowIndex,
    cellIndex,
    cellContentIndex: 0,
    cursorPosition: 0,
  };
}

/**
 * Returns true when the current focus is on the *last addressable cell* of the table.
 * (Rows can be ragged.)
 */
export function isAtLastTableCell(state: LetterEditorState): boolean {
  const f = state.focus;
  if (!isTableCellIndex(f)) return false;

  const block = state.redigertBrev.blocks[f.blockIndex];
  const content = block.content[f.contentIndex];
  if (!isTable(content)) return false;

  const table = content;
  const lastRowIndex = table.rows.length - 1;
  if (lastRowIndex < 0) return false;

  const lastColIndex = (table.rows[lastRowIndex]?.cells.length ?? 0) - 1;

  return f.rowIndex === lastRowIndex && f.cellIndex === lastColIndex;
}

function insertBlankLiteralIfEmptyBlock(
  block: ParagraphBlock | Title1Block | Title2Block | Title3Block,
  contentIndex: number,
) {
  if (block.type === PARAGRAPH || block.type === TITLE1 || block.type === TITLE2 || block.type === TITLE3) {
    addElements([newLiteral({ editedText: "" })], contentIndex, block.content, block.deletedContent);
    return true;
  }
  return false;
}

/**
 * Move focus out of a table cell to the next or previous editable position.
 *
 * @param direction
 *   - "forward": Tab or equivalent, advance focus after the table.
 *   - "backward": Shift+Tab or equivalent, move focus before the table.
 */
export const exitTable = (direction: "forward" | "backward") =>
  produce<LetterEditorState>((draft) => {
    const f = draft.focus;
    if (!isTableCellIndex(f)) return;

    const blocks = draft.redigertBrev.blocks;
    const block = blocks[f.blockIndex];

    if (direction === "forward") {
      // 1) Next content in the same block
      if (f.contentIndex + 1 < block.content.length) {
        draft.focus = { blockIndex: f.blockIndex, contentIndex: f.contentIndex + 1, cursorPosition: 0 };
        return;
      }

      // 2) First content in the next block
      if (f.blockIndex + 1 < blocks.length) {
        const nextBlock = blocks[f.blockIndex + 1];
        if (nextBlock.content.length > 0) {
          draft.focus = { blockIndex: f.blockIndex + 1, contentIndex: 0, cursorPosition: 0 };
          return;
        }
        // If next block is an empty paragraph, insert a blank literal so it can receive focus
        if (insertBlankLiteralIfEmptyBlock(nextBlock, 0)) {
          draft.focus = { blockIndex: f.blockIndex + 1, contentIndex: 0, cursorPosition: 0 };
          draft.saveStatus = "DIRTY";
          return;
        }
        // Otherwise, focus the (empty) next block
        draft.focus = { blockIndex: f.blockIndex + 1, contentIndex: 0, cursorPosition: 0 };
        return;
      }
      // 3) End of document: append an empty literal after the table
      const inserted = insertBlankLiteralIfEmptyBlock(block, f.contentIndex + 1);
      if (inserted) {
        draft.focus = { blockIndex: f.blockIndex, contentIndex: f.contentIndex + 1, cursorPosition: 0 };
        draft.saveStatus = "DIRTY";
      }
      return;
    }
    // Backward direction
    // 1) Previous content in the same block
    if (f.contentIndex - 1 >= 0) {
      draft.focus = { blockIndex: f.blockIndex, contentIndex: f.contentIndex - 1, cursorPosition: 0 };
      return;
    }
    // 2) Last content of the previous block
    if (f.blockIndex - 1 >= 0) {
      const prevBlock = blocks[f.blockIndex - 1];
      if (prevBlock.content.length > 0) {
        const last = prevBlock.content.length - 1;
        draft.focus = { blockIndex: f.blockIndex - 1, contentIndex: last, cursorPosition: 0 };
        return;
      }
      // If previous block is empty paragraph, insert a blank literal for focus
      if (insertBlankLiteralIfEmptyBlock(prevBlock, 0)) {
        draft.focus = { blockIndex: f.blockIndex - 1, contentIndex: 0, cursorPosition: 0 };
        draft.saveStatus = "DIRTY";
        return;
      }
      draft.focus = { blockIndex: f.blockIndex - 1, contentIndex: 0, cursorPosition: 0 };
      return;
    }
    // 3) Start of document: insert before the table so focus can land there
    const inserted = insertBlankLiteralIfEmptyBlock(block, f.contentIndex);
    if (inserted) {
      draft.focus = { blockIndex: f.blockIndex, contentIndex: f.contentIndex, cursorPosition: 0 };
      draft.saveStatus = "DIRTY";
    }
  });

/**
 * Adds a new row to the table (if currently at the last row), and focuses the new row.
 * Returns true if a row was added, false otherwise.
 */
export function addRow(
  editorState: LetterEditorState,
  updateEditorState: (updater: (prevState: LetterEditorState) => LetterEditorState) => void,
  keyboardEvent: React.KeyboardEvent,
): boolean {
  const focus = editorState.focus;
  const currentBlock = editorState.redigertBrev.blocks[focus.blockIndex];
  const contentAtFocus = currentBlock.content[focus.contentIndex];

  if (!isTableCellIndex(focus) || !isTable(contentAtFocus)) {
    return false;
  }

  keyboardEvent.preventDefault();

  updateEditorState((prev) =>
    produce(prev, (draft) => {
      const f = draft.focus;
      if (!isTableCellIndex(f)) return;

      const block = draft.redigertBrev.blocks[f.blockIndex];
      if (block.type !== PARAGRAPH) return;
      const paragraphDraft = block as Draft<ParagraphBlock>;

      const content = paragraphDraft.content[f.contentIndex];
      if (!isTable(content)) return;
      const table = content;

      const currentRowIndex = f.rowIndex;
      const currentColIndex = f.cellIndex;
      const isLastRow = currentRowIndex === table.rows.length - 1;

      const columnCount =
        table.header.colSpec.length > 0 ? table.header.colSpec.length : (table.rows[0]?.cells.length ?? 0);

      if (isLastRow) {
        addElements([newRow(columnCount)], table.rows.length, table.rows, table.deletedRows);
        draft.saveStatus = "DIRTY";
      }

      draft.focus = {
        ...f,
        rowIndex: isLastRow ? table.rows.length - 1 : currentRowIndex + 1,
        cellIndex: isLastRow ? 0 : currentColIndex,
        cellContentIndex: 0,
        cursorPosition: 0,
      };
    }),
  );

  return true;
}

export function isCellEmpty(cell: Cell): boolean {
  return isEmptyContentList(cell.text);
}

export type TableCellDeleteShortcutAction = "IGNORE" | "BLOCK_DEFAULT" | "DELETE_ROW" | "DELETE_TABLE";

/**
 * Handles table-cell delete shortcuts.
 *
 * - Plain Backspace at the start of a cell is blocked so the caret does not
 *   merge content out of the table.
 * - Shift+Backspace and Shift+Delete in body cells signal row deletion,
 *   regardless of whether the row is empty.
 */
export function handleTableCellDeleteShortcut(
  event: React.KeyboardEvent,
  editorState: LetterEditorState,
): TableCellDeleteShortcutAction {
  if (event.key !== "Backspace" && event.key !== "Delete") return "IGNORE";
  if (!isTableCellIndex(editorState.focus)) return "IGNORE";

  const f = editorState.focus;
  const block = editorState.redigertBrev.blocks[f.blockIndex];
  const contentAtFocus = block.content[f.contentIndex];

  if (!isTable(contentAtFocus)) return "IGNORE";

  const table = contentAtFocus;

  const getFocusedCell = () => {
    if (f.rowIndex === -1) {
      // header cell
      const col = table.header.colSpec[f.cellIndex];
      return col?.headerContent;
    }
    // body cell
    const row = table.rows[f.rowIndex];
    return row?.cells[f.cellIndex];
  };

  const cell = getFocusedCell();
  if (!cell) return "IGNORE";

  if (event.shiftKey) {
    if (f.rowIndex === -1) {
      return "IGNORE";
    }

    const isOnlyRow = table.rows.length === 1;
    const allCellsEmpty = isOnlyRow && table.rows[0].cells.every((c) => isCellEmpty(c));

    if (allCellsEmpty) {
      return "DELETE_TABLE";
    }

    return "DELETE_ROW";
  }

  if (event.key !== "Backspace") {
    return "IGNORE";
  }

  const cursorOffset = getCursorOffset();
  const isFirstTextInCell = f.cellContentIndex === 0;
  const cellIsEmpty = isCellEmpty(cell);
  const atStartOfThisTextNode = cursorOffset === 0 || (cursorOffset === 1 && cellIsEmpty);

  if ((isFirstTextInCell && atStartOfThisTextNode) || cellIsEmpty) {
    return "BLOCK_DEFAULT";
  }

  return "IGNORE";
}
