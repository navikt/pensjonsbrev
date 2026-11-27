import { isItemContentIndex, isTable, text } from "~/Brevredigering/LetterEditor/actions/common";
import { type Content, type EditedDocument, type Table, type TextContent, TITLE_INDEX } from "~/types/brevbakerTypes";

import { type Focus, type LiteralIndex, SIGNATURE_FOCUS, type TableCellIndex } from "./state";
import { isItemList, isTableCellIndex, isTextContent } from "./utils";

type Identified = { id?: number | null };

/**
 * Finds the element in `to` that corresponds to `from[index]`, aligning the lists in order like the backend
 * template merge (`UpdateEditedLetter.mergeAligned`): each element of `from` matches the first element with the
 * same id (or, for new elements, without id) after the previous match. Unmatched elements were dropped by the merge.
 */
function correspondingIndex(from: readonly Identified[], to: readonly Identified[], index: number): number | undefined {
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

function withLiteral(focus: Focus, index: LiteralIndex, literal: TextContent | undefined): Focus | undefined {
  if (!isTextContent(literal)) return undefined;
  return {
    ...index,
    cursorPosition:
      focus.cursorPosition === undefined ? undefined : Math.min(focus.cursorPosition, text(literal).length),
    selectAll: focus.selectAll,
  };
}

function remapContent(focus: Focus, from: Content, to: Content, blockContent: LiteralIndex): Focus | undefined {
  const { blockIndex, contentIndex } = blockContent;
  if (isItemContentIndex(focus)) {
    if (!isItemList(from) || !isItemList(to)) return undefined;
    const itemIndex = correspondingIndex(from.items, to.items, focus.itemIndex);
    if (itemIndex === undefined) return undefined;
    const fromItem = from.items[focus.itemIndex].content;
    const toItem = to.items[itemIndex].content;
    const itemContentIndex = correspondingIndex(fromItem, toItem, focus.itemContentIndex);
    if (itemContentIndex === undefined) return undefined;
    return withLiteral(focus, { blockIndex, contentIndex, itemIndex, itemContentIndex }, toItem[itemContentIndex]);
  }

  if (isTableCellIndex(focus)) {
    if (!isTable(from) || !isTable(to)) return undefined;
    const cells = correspondingCell(focus, from, to);
    if (!cells) return undefined;
    const { rowIndex, cellIndex, fromCell, toCell } = cells;
    const cellContentIndex = correspondingIndex(fromCell.text, toCell.text, focus.cellContentIndex);
    if (cellContentIndex === undefined) return undefined;
    return withLiteral(
      focus,
      { blockIndex, contentIndex, rowIndex, cellIndex, cellContentIndex },
      toCell.text[cellContentIndex],
    );
  }

  return isTextContent(to) ? withLiteral(focus, { blockIndex, contentIndex }, to) : undefined;
}

/** Row index -1 is the header row. */
function correspondingCell(focus: TableCellIndex, from: Table, to: Table) {
  if (focus.rowIndex === -1) {
    const cellIndex = correspondingIndex(from.header.colSpec, to.header.colSpec, focus.cellIndex);
    if (cellIndex === undefined) return undefined;
    return {
      rowIndex: -1,
      cellIndex,
      fromCell: from.header.colSpec[focus.cellIndex].headerContent,
      toCell: to.header.colSpec[cellIndex].headerContent,
    };
  }
  const rowIndex = correspondingIndex(from.rows, to.rows, focus.rowIndex);
  if (rowIndex === undefined) return undefined;
  const fromCells = from.rows[focus.rowIndex].cells;
  const toCells = to.rows[rowIndex].cells;
  const cellIndex = correspondingIndex(fromCells, toCells, focus.cellIndex);
  if (cellIndex === undefined) return undefined;
  return { rowIndex, cellIndex, fromCell: fromCells[focus.cellIndex], toCell: toCells[cellIndex] };
}

function remapLiteral(focus: Focus, from: EditedDocument, to: EditedDocument): Focus | undefined {
  if (focus.blockIndex === TITLE_INDEX) {
    const contentIndex = correspondingIndex(from.title.text, to.title.text, focus.contentIndex);
    if (contentIndex === undefined) return undefined;
    return withLiteral(focus, { blockIndex: TITLE_INDEX, contentIndex }, to.title.text[contentIndex]);
  }

  const blockIndex = correspondingIndex(from.blocks, to.blocks, focus.blockIndex);
  if (blockIndex === undefined) return undefined;
  const fromContent = from.blocks[focus.blockIndex].content;
  const toContent = to.blocks[blockIndex].content;
  const contentIndex = correspondingIndex(fromContent, toContent, focus.contentIndex);
  if (contentIndex === undefined) return undefined;
  return remapContent(focus, fromContent[focus.contentIndex], toContent[contentIndex], { blockIndex, contentIndex });
}

function startOfBlock(document: EditedDocument, blockIndex: number): Focus | undefined {
  const first = document.blocks[blockIndex]?.content[0];
  if (isTextContent(first)) return { blockIndex, contentIndex: 0, cursorPosition: 0 };
  if (isItemList(first) && isTextContent(first.items[0]?.content[0])) {
    return { blockIndex, contentIndex: 0, itemIndex: 0, itemContentIndex: 0, cursorPosition: 0 };
  }
  return undefined;
}

/**
 * Moves focus from a position in `from` to the same content in `to`, e.g. after a template merge has
 * inserted or removed blocks. Falls back to the start of the nearest preceding block that still exists.
 */
export function remapFocus(focus: Focus, from: EditedDocument, to: EditedDocument): Focus {
  if (focus.blockIndex === SIGNATURE_FOCUS.blockIndex && focus.contentIndex === SIGNATURE_FOCUS.contentIndex) {
    return focus;
  }

  const remapped = remapLiteral(focus, from, to);
  if (remapped) return remapped;

  for (let blockIndex = Math.min(focus.blockIndex, from.blocks.length - 1); blockIndex >= 0; blockIndex--) {
    const toBlockIndex = correspondingIndex(from.blocks, to.blocks, blockIndex);
    const start = toBlockIndex === undefined ? undefined : startOfBlock(to, toBlockIndex);
    if (start) return start;
  }
  return startOfBlock(to, 0) ?? { blockIndex: 0, contentIndex: 0, cursorPosition: 0 };
}
