import {
  collectAllLiteralValues,
  isItemContentIndex,
  isTable,
  isUneditedFritekst,
  text,
} from "~/Brevredigering/LetterEditor/actions/common";
import { type Content, type EditedDocument, type Table, TITLE_INDEX } from "~/types/brevbakerTypes";

import { type Focus, type LiteralIndex, SIGNATURE_FOCUS, type TableCellIndex } from "./state";
import { isItemList, isLiteral, isTableCellIndex } from "./utils";

type Identified = { id?: number | null };
type MappingDirection = "forward" | "reverse";

/**
 * Finds the element in `to` that corresponds to `from[index]`, aligning the lists in order like the backend
 * template merge (`UpdateEditedLetter.mergeAligned`): each element of `from` matches the first element with the
 * same id (or, for new elements, without id) after the previous match. Unmatched elements were dropped by the merge.
 */
function correspondingIndex(
  from: readonly Identified[],
  to: readonly Identified[],
  index: number,
  direction: MappingDirection,
): number | undefined {
  if (direction === "reverse") {
    for (let toIndex = 0; toIndex < to.length; toIndex++) {
      if (correspondingIndex(to, from, toIndex, "forward") === index) return toIndex;
    }
    return undefined;
  }

  if (from[index] === undefined) return undefined;
  let next = 0;
  for (let fromIndex = 0; fromIndex <= index; fromIndex++) {
    const id = from[fromIndex].id ?? null;
    const match = to.findIndex((element, toIndex) => toIndex >= next && (element.id ?? null) === id);
    if (fromIndex === index) return match === -1 ? undefined : match;
    if (match !== -1) next = match + 1;
  }
  return undefined;
}

function withLiteral(focus: Focus, index: LiteralIndex, literal: Content | undefined): Focus | undefined {
  // Only literals are editable; a variable or new line with the same id cannot receive the caret.
  if (!isLiteral(literal)) return undefined;
  return {
    ...index,
    cursorPosition:
      focus.cursorPosition === undefined ? undefined : Math.min(focus.cursorPosition, text(literal).length),
    selectAll: focus.selectAll,
  };
}

function remapContent(
  focus: Focus,
  from: Content,
  to: Content,
  blockContent: LiteralIndex,
  direction: MappingDirection,
): Focus | undefined {
  const { blockIndex, contentIndex } = blockContent;
  if (isItemContentIndex(focus)) {
    if (!isItemList(from) || !isItemList(to)) return undefined;
    const itemIndex = correspondingIndex(from.items, to.items, focus.itemIndex, direction);
    if (itemIndex === undefined) return undefined;
    const fromItem = from.items[focus.itemIndex].content;
    const toItem = to.items[itemIndex].content;
    const itemContentIndex = correspondingIndex(fromItem, toItem, focus.itemContentIndex, direction);
    if (itemContentIndex === undefined) return undefined;
    return withLiteral(focus, { blockIndex, contentIndex, itemIndex, itemContentIndex }, toItem[itemContentIndex]);
  }

  if (isTableCellIndex(focus)) {
    if (!isTable(from) || !isTable(to)) return undefined;
    const cells = correspondingCell(focus, from, to, direction);
    if (!cells) return undefined;
    const { rowIndex, cellIndex, fromCell, toCell } = cells;
    const cellContentIndex = correspondingIndex(fromCell.text, toCell.text, focus.cellContentIndex, direction);
    if (cellContentIndex === undefined) return undefined;
    return withLiteral(
      focus,
      { blockIndex, contentIndex, rowIndex, cellIndex, cellContentIndex },
      toCell.text[cellContentIndex],
    );
  }

  return withLiteral(focus, { blockIndex, contentIndex }, to);
}

/** Row index -1 is the header row. */
function correspondingCell(focus: TableCellIndex, from: Table, to: Table, direction: MappingDirection) {
  if (focus.rowIndex === -1) {
    const cellIndex = correspondingIndex(from.header.colSpec, to.header.colSpec, focus.cellIndex, direction);
    if (cellIndex === undefined) return undefined;
    return {
      rowIndex: -1,
      cellIndex,
      fromCell: from.header.colSpec[focus.cellIndex].headerContent,
      toCell: to.header.colSpec[cellIndex].headerContent,
    };
  }
  const rowIndex = correspondingIndex(from.rows, to.rows, focus.rowIndex, direction);
  if (rowIndex === undefined) return undefined;
  const fromCells = from.rows[focus.rowIndex].cells;
  const toCells = to.rows[rowIndex].cells;
  const cellIndex = correspondingIndex(fromCells, toCells, focus.cellIndex, direction);
  if (cellIndex === undefined) return undefined;
  return { rowIndex, cellIndex, fromCell: fromCells[focus.cellIndex], toCell: toCells[cellIndex] };
}

function remapLiteral(
  focus: Focus,
  from: EditedDocument,
  to: EditedDocument,
  direction: MappingDirection,
): Focus | undefined {
  if (focus.blockIndex === TITLE_INDEX) {
    const contentIndex = correspondingIndex(from.title.text, to.title.text, focus.contentIndex, direction);
    if (contentIndex === undefined) return undefined;
    return withLiteral(focus, { blockIndex: TITLE_INDEX, contentIndex }, to.title.text[contentIndex]);
  }

  const blockIndex = correspondingIndex(from.blocks, to.blocks, focus.blockIndex, direction);
  if (blockIndex === undefined) return undefined;
  const fromContent = from.blocks[focus.blockIndex].content;
  const toContent = to.blocks[blockIndex].content;
  const contentIndex = correspondingIndex(fromContent, toContent, focus.contentIndex, direction);
  if (contentIndex === undefined) return undefined;
  return remapContent(
    focus,
    fromContent[focus.contentIndex],
    toContent[contentIndex],
    { blockIndex, contentIndex },
    direction,
  );
}

type Edge = "start" | "end";

function indices<T>(list: readonly T[], edge: Edge): number[] {
  const all = list.map((_, index) => index);
  return edge === "start" ? all : all.reverse();
}

function literalAt(texts: readonly Content[], edge: Edge): { index: number; cursorPosition: number } | undefined {
  for (const index of indices(texts, edge)) {
    const content = texts[index];
    if (isLiteral(content)) return { index, cursorPosition: edge === "start" ? 0 : text(content).length };
  }
  return undefined;
}

/** Only literals are editable; variables and new lines cannot receive the caret. */
function literalInBlock(document: EditedDocument, blockIndex: number, edge: Edge): Focus | undefined {
  const content = document.blocks[blockIndex]?.content ?? [];
  for (const contentIndex of indices(content, edge)) {
    const element = content[contentIndex];
    if (isLiteral(element)) {
      return { blockIndex, contentIndex, cursorPosition: edge === "start" ? 0 : text(element).length };
    }
    if (isItemList(element)) {
      for (const itemIndex of indices(element.items, edge)) {
        const found = literalAt(element.items[itemIndex].content, edge);
        if (found) {
          return {
            blockIndex,
            contentIndex,
            itemIndex,
            itemContentIndex: found.index,
            cursorPosition: found.cursorPosition,
          };
        }
      }
    }
    if (isTable(element)) {
      const cells: { rowIndex: number; cellIndex: number; texts: readonly Content[] }[] = [
        ...element.header.colSpec.map((col, cellIndex) => ({ rowIndex: -1, cellIndex, texts: col.headerContent.text })),
        ...element.rows.flatMap((row, rowIndex) =>
          row.cells.map((cell, cellIndex) => ({ rowIndex, cellIndex, texts: cell.text })),
        ),
      ];
      for (const index of indices(cells, edge)) {
        const { rowIndex, cellIndex, texts } = cells[index];
        const found = literalAt(texts, edge);
        if (found) {
          return {
            blockIndex,
            contentIndex,
            rowIndex,
            cellIndex,
            cellContentIndex: found.index,
            cursorPosition: found.cursorPosition,
          };
        }
      }
    }
  }
  return undefined;
}

/**
 * Moves focus from a position in `from` to the same content in `to`, e.g. after a template merge has
 * inserted or removed blocks. Falls back to the end of the last literal in the nearest preceding block
 * that still exists, i.e. where the removed content used to be.
 */
export function remapFocus(
  focus: Focus,
  from: EditedDocument,
  to: EditedDocument,
  direction: MappingDirection = "forward",
): Focus {
  if (focus.blockIndex === SIGNATURE_FOCUS.blockIndex && focus.contentIndex === SIGNATURE_FOCUS.contentIndex) {
    return focus;
  }

  const remapped = remapLiteral(focus, from, to, direction);
  if (remapped) return remapped;

  for (let blockIndex = Math.min(focus.blockIndex, from.blocks.length - 1); blockIndex >= 0; blockIndex--) {
    const toBlockIndex = correspondingIndex(from.blocks, to.blocks, blockIndex, direction);
    const end = toBlockIndex === undefined ? undefined : literalInBlock(to, toBlockIndex, "end");
    if (end) return end;
  }
  for (let blockIndex = 0; blockIndex < to.blocks.length; blockIndex++) {
    const start = literalInBlock(to, blockIndex, "start");
    if (start) return start;
  }
  return { blockIndex: 0, contentIndex: 0, cursorPosition: 0 };
}

function contentAt(document: EditedDocument, focus: Focus): Content | undefined {
  if (focus.blockIndex === TITLE_INDEX) return document.title.text[focus.contentIndex];
  const content = document.blocks[focus.blockIndex]?.content[focus.contentIndex];
  if (isItemContentIndex(focus)) {
    return isItemList(content) ? content.items[focus.itemIndex]?.content[focus.itemContentIndex] : undefined;
  }
  if (isTableCellIndex(focus)) {
    if (!isTable(content)) return undefined;
    const cell =
      focus.rowIndex === -1
        ? content.header.colSpec[focus.cellIndex]?.headerContent
        : content.rows[focus.rowIndex]?.cells[focus.cellIndex];
    return cell?.text[focus.cellContentIndex];
  }
  return content;
}

/**
 * The literal that Tab enters the letter at: the saved caret when the user has placed one in a literal. Otherwise
 * the first literal of the title (then the letter), unless untouched fritekst placeholders already are the tab stops.
 */
export function tabEntryFocus(document: EditedDocument, focus: Focus): Focus | null {
  if (focus.cursorPosition !== undefined && isLiteral(contentAt(document, focus))) return focus;
  if (collectAllLiteralValues(document).some(isUneditedFritekst)) return null;
  const titleIndex = document.title.text.findIndex(isLiteral);
  if (titleIndex !== -1) return { blockIndex: TITLE_INDEX, contentIndex: titleIndex };
  for (let blockIndex = 0; blockIndex < document.blocks.length; blockIndex++) {
    const start = literalInBlock(document, blockIndex, "start");
    if (start) return { ...start, cursorPosition: undefined };
  }
  return null;
}
