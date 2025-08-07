import type { Draft } from "immer";
import { produce } from "immer";

import type { Cell, ParagraphBlock, Row, Title1Block, Title2Block } from "~/types/brevbakerTypes";
import { PARAGRAPH, TITLE1, TITLE2 } from "~/types/brevbakerTypes";

import { addElements, isTable, newLiteral, newRow, removeElements } from "../actions/common";
import type { Focus, LetterEditorState } from "../model/state";
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

function insertBlankLiteralIfEmptyBlock(block: ParagraphBlock | Title1Block | Title2Block, contentIndex: number) {
  if (block.type === PARAGRAPH || block.type === TITLE1 || block.type === TITLE2) {
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
          draft.isDirty = true;
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
        draft.isDirty = true;
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
        draft.isDirty = true;
        return;
      }
      draft.focus = { blockIndex: f.blockIndex - 1, contentIndex: 0, cursorPosition: 0 };
      return;
    }
    // 3) Start of document: insert before the table so focus can land there
    const inserted = insertBlankLiteralIfEmptyBlock(block, f.contentIndex);
    if (inserted) {
      draft.focus = { blockIndex: f.blockIndex, contentIndex: f.contentIndex, cursorPosition: 0 };
      draft.isDirty = true;
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
        draft.isDirty = true;
      }

      draft.focus = {
        ...f,
        rowIndex: isLastRow ? table.rows.length - 1 : currentRowIndex + 1,
        cellIndex: currentColIndex,
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
  // We only care about Backspace (plain or with Shift)
  if (event.key !== "Backspace") return false;
  if (!isTableCellIndex(editorState.focus)) return false;

  const f = editorState.focus;
  const block = editorState.redigertBrev.blocks[f.blockIndex];
  const contentAtFocus = block.content[f.contentIndex];

  if (!isTable(contentAtFocus)) return false;

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
  if (!cell) return false;

  const cursorOffset = getCursorOffset();
  const atStartOfThisTextNode = cursorOffset === 0;
  const isFirstTextInCell = f.cellContentIndex === 0;
  const cellIsEmpty = isCellEmpty(cell);

  if (!event.shiftKey) {
    // Prevent "merge with previous" when:
    // - Caret is at the very start of the *first* text node in the cell, or
    // - The cell is effectively empty (including zero-width space)
    //
    // In these cases, block the merge and do nothing (so one doesn't exit the table by mistake).
    // Otherwise, allow normal character deletion inside the cell.
    if ((isFirstTextInCell && atStartOfThisTextNode) || cellIsEmpty) {
      event.preventDefault();
      return true;
    }
    // All other cases: let browser handle (delete character within cell text)
    return false;
  }

  if (f.rowIndex === -1) {
    // In header cell: row deletion not allowed, let browser handle as normal
    return false;
  }

  const currentRow = table.rows[f.rowIndex];
  if (!currentRow) return false;

  if (!(atStartOfThisTextNode && rowIsEmpty(currentRow))) return false;

  event.preventDefault();

  updateEditorState((prevState) =>
    produce(prevState, (draft) => {
      const df = draft.focus;
      if (!isTableCellIndex(df)) return;

      const dBlock = draft.redigertBrev.blocks[df.blockIndex];
      const dContent = dBlock.content[df.contentIndex];
      if (!isTable(dContent)) return;
      const dTable = dContent;

      removeElements(df.rowIndex, 1, {
        content: dTable.rows,
        deletedContent: dTable.deletedRows,
        id: dTable.id,
      });

      if (dTable.rows.length === 0) {
        // Replace the whole table with a blank paragraph literal
        const paragraphDraft = draft.redigertBrev.blocks[df.blockIndex] as Draft<ParagraphBlock>;

        removeElements(df.contentIndex, 1, {
          content: paragraphDraft.content,
          deletedContent: paragraphDraft.deletedContent,
          id: paragraphDraft.id,
        });

        addElements(
          [newLiteral({ editedText: "" })],
          df.contentIndex,
          paragraphDraft.content,
          paragraphDraft.deletedContent,
        );

        draft.focus = { blockIndex: df.blockIndex, contentIndex: df.contentIndex, cursorPosition: 0 };
      } else {
        const newRowIndex = Math.min(df.rowIndex, dTable.rows.length - 1);
        const newRow = dTable.rows[newRowIndex];
        const newColIndex = Math.min(df.cellIndex, newRow.cells.length - 1);

        draft.focus = {
          blockIndex: df.blockIndex,
          contentIndex: df.contentIndex,
          rowIndex: newRowIndex,
          cellIndex: newColIndex,
          cellContentIndex: 0,
          cursorPosition: 0,
        };
      }

      draft.isDirty = true;
    }),
  );

  return true;
}
