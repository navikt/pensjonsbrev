/* eslint-disable no-console */
import type { Draft } from "immer";

import {
  addElements,
  isAtEndOfItemList,
  isAtEndOfTable,
  isAtSameBlockContent,
  isAtSameItem,
  isAtSameTableCell,
  isAtStartOfItemList,
  isAtStartOfTable,
  isBlockContentIndex,
  isIndexAfter,
  isIndicesOfSameType,
  isItemContentIndex,
  isTable,
  isValidIndex,
  newLiteral,
  removeElements,
  text,
} from "~/Brevredigering/LetterEditor/actions/common";
import { mergeRecipe, MergeTarget } from "~/Brevredigering/LetterEditor/actions/merge";
import { updateLiteralText } from "~/Brevredigering/LetterEditor/actions/updateContentText";
import type { Action } from "~/Brevredigering/LetterEditor/lib/actions";
import { withPatches } from "~/Brevredigering/LetterEditor/lib/actions";
import type {
  Focus,
  ItemContentIndex,
  LetterEditorState,
  LiteralIndex,
  SelectionIndex,
  TableCellIndex,
} from "~/Brevredigering/LetterEditor/model/state";
import {
  isItemList,
  isLiteral,
  isNewLine,
  isTableCellIndex,
  isTextContent,
  isVariable,
} from "~/Brevredigering/LetterEditor/model/utils";
import type { AnyBlock, Content, EditedLetter, ItemList, Table, TextContent } from "~/types/brevbakerTypes";

export const deleteSelection: Action<LetterEditorState, [selection: SelectionIndex]> =
  withPatches(deleteSelectionRecipe);

export function deleteSelectionRecipe(draft: LetterEditorState, selection: SelectionIndex) {
  const redigertBrev = draft.redigertBrev;

  // If selection is not valid, do nothing
  if (!isValidIndex(redigertBrev, selection.start) || !isValidIndex(redigertBrev, selection.end)) return;
  // Selection must end after it starts
  if (!isIndexAfter(selection.start, selection.end)) return;

  const start = { ...selection.start };
  const end = { ...selection.end };

  const startBlock = redigertBrev.blocks[start.blockIndex];
  const startContent = startBlock.content[start.contentIndex];
  const endBlock = redigertBrev.blocks[end.blockIndex];
  const endContent = endBlock.content[end.contentIndex];

  if (isBlockContentIndex(start) && isTextContent(startContent)) {
    // selection ends in same block as it starts
    if (start.blockIndex === end.blockIndex) {
      if (isBlockContentIndex(end) && isTextContent(endContent)) {
        // start and end is at TextContent in same block
        removeInTextContentParent(startBlock, start, end);
      } else {
        // TODO: below 4 statements are also part of removeInTextContentParent, cannot use it currently since end is not TextContent
        // update text in startContent and remove everything before end
        const didRemoveStart = deleteInTextContentFromOffset(
          startBlock,
          startContent,
          start.contentIndex,
          start.cursorPosition,
        );
        const startDeletionOffset = didRemoveStart ? 0 : 1;

        // remove content between start and end
        const removeCount = end.contentIndex - start.contentIndex - 1;
        removeElements(start.contentIndex + startDeletionOffset, removeCount, startBlock);

        // remove in endContent up to cursor position
        deleteInContentToOffset(startBlock, endContent, {
          ...end,
          contentIndex: start.contentIndex + startDeletionOffset,
        });
        makeSureBlockIsValid(startBlock);
      }
    } else {
      // end is after start block so we remove everything after start in startBlock
      removeInTextContentParent(startBlock, start);
    }
  } else if (
    (isTable(startContent) && isTableCellIndex(start)) ||
    (isItemList(startContent) && isItemContentIndex(start))
  ) {
    deleteInContentAndRemoveRemainingInBlock(startBlock, startContent, start, end);
  }

  if (start.blockIndex < end.blockIndex) {
    // remove all blocks between start and end blocks
    const removedBlocks = removeElements(start.blockIndex + 1, end.blockIndex - start.blockIndex - 1, {
      content: redigertBrev.blocks,
      deletedContent: redigertBrev.deletedBlocks,
      id: null,
    });

    // remove content before endContent in endBlock
    removeElements(0, end.contentIndex, endBlock);
    deleteInContentToOffset(endBlock, endContent, {
      ...end,
      blockIndex: end.blockIndex - removedBlocks.length,
      contentIndex: 0,
    });
    makeSureBlockIsValid(endBlock);
  }

  // Make sure we have a valid literalIndex
  const indexAfterDeletion = isValidIndex(redigertBrev, start) ? start : deduceValidIndex(redigertBrev, start);

  if (indexAfterDeletion) {
    draft.focus = indexAfterDeletion;

    // Potentially merge start and end which should now be adjacent.
    if (shouldMergeAfterDeletetion(indexAfterDeletion, selection)) {
      mergeRecipe(draft, indexAfterDeletion, MergeTarget.NEXT);
    }
  }
  draft.saveStatus = "DIRTY";
}

function shouldMergeAfterDeletetion(indexAfterDeletion: Focus, { start, end }: SelectionIndex): boolean {
  if (start.blockIndex < end.blockIndex) {
    return true;
  } else if (isItemContentIndex(indexAfterDeletion) && start.contentIndex < end.contentIndex) {
    return true;
  } else {
    return (
      isItemContentIndex(indexAfterDeletion) &&
      isItemContentIndex(start) &&
      isItemContentIndex(end) &&
      start.itemIndex < end.itemIndex
    );
  }
}

function deleteInTextContentFromOffset(
  parent: { content: Draft<Content[]>; deletedContent: Draft<number[]>; id: number | null },
  content: Draft<TextContent>,
  contentIndex: number,
  offset: number,
): boolean {
  switch (content.type) {
    case "NEW_LINE":
    case "VARIABLE": {
      removeElements(contentIndex, 1, parent);
      return true;
    }
    case "LITERAL": {
      if (offset > 0) {
        updateLiteralText(content, text(content).slice(0, offset));
        return false;
      } else {
        removeElements(contentIndex, 1, parent);
        return true;
      }
    }
  }
}

function deleteInTextContentToOffset(
  parent: { content: Draft<Content[]>; deletedContent: Draft<number[]>; id: number | null },
  content: Draft<TextContent>,
  contentIndex: number,
  offset: number,
): boolean {
  switch (content.type) {
    case "NEW_LINE":
    case "VARIABLE": {
      removeElements(contentIndex, 1, parent);
      return true;
    }
    case "LITERAL": {
      const currentText = text(content);
      if (offset < currentText.length) {
        updateLiteralText(content, currentText.slice(offset));
        return false;
      } else {
        removeElements(contentIndex, 1, parent);
        return true;
      }
    }
  }
}

function deleteInItemList(
  parent: { content: Draft<Content[]>; deletedContent: Draft<number[]>; id: number | null },
  itemList: Draft<ItemList>,
  start: ItemContentIndex & { cursorPosition: number },
  end?: ItemContentIndex & { cursorPosition: number },
): boolean {
  const includesEndOfItemList = end === undefined || isAtEndOfItemList(end, itemList);

  if (isAtStartOfItemList(start) && includesEndOfItemList) {
    removeElements(start.contentIndex, 1, parent);
    return true;
  } else {
    const startItem = itemList.items[start.itemIndex];

    if (end && isAtSameItem(start, end)) {
      // at TextContent in same itemList-item
      removeInTextContentParent(
        startItem,
        { contentIndex: start.itemContentIndex, cursorPosition: start.cursorPosition },
        { contentIndex: end.itemContentIndex, cursorPosition: end.cursorPosition },
      );
    } else if (!end || start.itemIndex < end.itemIndex) {
      // end is at another item in same itemList, or the rest of the itemList
      const endItem = end && itemList.items[end.itemIndex];

      const removeStartItem = start.itemContentIndex === 0 && start.cursorPosition === 0;
      if (removeStartItem) {
        // remove entire start item if we are at the beginning of it
        removeElements(start.itemIndex, 1, {
          content: itemList.items,
          deletedContent: itemList.deletedItems,
          id: itemList.id,
        });
      } else {
        // remove content after start in startItem
        removeInTextContentParent(startItem, {
          contentIndex: start.itemContentIndex,
          cursorPosition: start.cursorPosition,
        });
      }

      // remove items between start and end in itemList, or rest of itemList if no end
      const endItemIndex = end ? end.itemIndex : itemList.items.length;
      const nextStartIndex = removeStartItem ? start.itemIndex : start.itemIndex + 1;
      removeElements(nextStartIndex, endItemIndex - start.itemIndex - 1, {
        content: itemList.items,
        deletedContent: itemList.deletedItems,
        id: itemList.id,
      });

      if (end && endItem) {
        // remove content before end in endItem
        removeInTextContentParent(
          endItem,
          { contentIndex: 0, cursorPosition: 0 },
          {
            contentIndex: end.itemContentIndex,
            cursorPosition: end.cursorPosition,
          },
        );
      }
    }
    return false;
  }
}

function deleteInTable(
  parent: { content: Draft<Content[]>; deletedContent: Draft<number[]>; id: number | null },
  table: Draft<Table>,
  start: TableCellIndex & { cursorPosition: number },
  end?: TableCellIndex & { cursorPosition: number },
): boolean {
  if (isAtStartOfTable(start) && (!end || isAtEndOfTable(end, table))) {
    // entire table is selected
    removeElements(start.contentIndex, 1, parent);
    return true;
  } else if (end) {
    // selection ends in same table as it starts
    if (isAtSameTableCell(start, end)) {
      // at TextContent in same table cell
      const startCell =
        start.rowIndex === -1
          ? table.header.colSpec[start.cellIndex].headerContent
          : table.rows[start.rowIndex].cells[start.cellIndex];

      removeInTextContentParent(
        { content: startCell.text, deletedContent: [], id: startCell.id },
        { contentIndex: start.cellContentIndex, cursorPosition: start.cursorPosition },
        { contentIndex: end.cellContentIndex, cursorPosition: end.cursorPosition },
      );
    } else {
      for (let r = start.rowIndex; r <= end.rowIndex; r++) {
        const cells = r === -1 ? table.header.colSpec.map((spec) => spec.headerContent) : table.rows[r].cells;

        const startCellIndex = r === start.rowIndex ? start.cellIndex : 0;
        const endCellIndex = r === end.rowIndex ? end.cellIndex : cells.length - 1;
        for (let c = startCellIndex; c <= endCellIndex; c++) {
          const cell = cells[c];
          if (r === start.rowIndex && c === start.cellIndex) {
            // first cell in selection, remove from start position
            removeInTextContentParent(
              { content: cell.text, deletedContent: [], id: cell.id },
              { contentIndex: start.cellContentIndex, cursorPosition: start.cursorPosition },
            );
          } else if (r === end.rowIndex && c === end.cellIndex) {
            // last cell in selection, remove up to end position
            removeInTextContentParent(
              { content: cell.text, deletedContent: [], id: cell.id },
              { contentIndex: 0, cursorPosition: 0 },
              { contentIndex: end.cellContentIndex, cursorPosition: end.cursorPosition },
            );
          } else {
            // middle cells, remove everything
            removeInTextContentParent(
              { content: cell.text, deletedContent: [], id: cell.id },
              { contentIndex: 0, cursorPosition: 0 },
            );
          }
        }
      }
    }
  }
  return false;
}

type ContentIndex<T extends Table | ItemList> = T extends ItemList ? ItemContentIndex : TableCellIndex;

function deleteInContentAndRemoveRemainingInBlock<T extends ItemList | Table>(
  block: Draft<AnyBlock>,
  content: Draft<T>,
  start: ContentIndex<T> & { cursorPosition: number },
  end: LiteralIndex & { cursorPosition: number },
) {
  if (isAtSameBlockContent(start, end) && isIndicesOfSameType(start, end)) {
    // selection starts and ends in same content
    deleteInContent(block, content, start, end);
  } else {
    const endContent = block.content[end.contentIndex];
    const didRemoveContent = deleteInContent(block, content, start);
    const nextStartIndex = didRemoveContent ? start.contentIndex : start.contentIndex + 1;

    // remove rest of content in block, or up to end if in same block
    if (start.blockIndex < end.blockIndex) {
      // remove content between start and end in block, or everything after start if end is in another block
      removeElements(nextStartIndex, block.content.length, block);
    } else if (start.blockIndex === end.blockIndex && start.contentIndex < end.contentIndex) {
      // remove from endContent if in same block
      deleteInContentToOffset(block, endContent, end);
      // remove content between start and end in block
      removeElements(nextStartIndex, end.contentIndex - start.contentIndex - 1, block);
    }
  }

  makeSureBlockIsValid(block);
}

function makeSureBlockIsValid(block: Draft<AnyBlock>) {
  // Make sure we don't end up with a block without any content
  if (block.content.length === 0) {
    addElements([newLiteral()], 0, block.content, block.deletedContent);
  }
}

function deleteInContent<T extends ItemList | Table>(
  parent: { content: Draft<Content[]>; deletedContent: Draft<number[]>; id: number | null },
  content: Draft<ItemList | Table>,
  start: ContentIndex<T> & { cursorPosition: number },
  end?: ContentIndex<T> & { cursorPosition: number },
): boolean {
  if (isItemList(content) && isItemContentIndex(start) && (end === undefined || isItemContentIndex(end))) {
    return deleteInItemList(parent, content, start, end);
  } else if (isTable(content) && isTableCellIndex(start) && (end === undefined || isTableCellIndex(end))) {
    return deleteInTable(parent, content, start, end);
  } else {
    console.error("Cannot delete in content: incompatible start or end index", content, start, end);
    throw new Error("Cannot delete in content: incompatible start or end index");
  }
}

function deleteInContentToOffset(
  parent: { content: Draft<Content[]>; deletedContent: Draft<number[]>; id: number | null },
  content: Draft<Content>,
  end: SelectionIndex["end"],
): boolean {
  if (isTextContent(content) && isBlockContentIndex(end)) {
    return deleteInTextContentToOffset(parent, content, end.contentIndex, end.cursorPosition);
  } else if (isItemList(content) && isItemContentIndex(end)) {
    return deleteInItemList(parent, content, { ...end, itemIndex: 0, itemContentIndex: 0, cursorPosition: 0 }, end);
  }
  if (isTable(content) && isTableCellIndex(end)) {
    return deleteInTable(
      parent,
      content,
      { ...end, rowIndex: -1, cellIndex: 0, cellContentIndex: 0, cursorPosition: 0 },
      end,
    );
  }
  return false;
}

function removeInTextContentParent(
  parent: { content: Draft<Content[]>; deletedContent: Draft<number[]>; id: number | null },
  start: { contentIndex: number; cursorPosition: number },
  end?: { contentIndex: number; cursorPosition: number },
) {
  const startContent = parent.content[start.contentIndex];

  if (!isTextContent(startContent)) {
    console.warn("Can't delete selection in parent if start is not TextContent");
    return;
  }

  if (end) {
    if (start.contentIndex < end.contentIndex) {
      const endContent = parent.content[end.contentIndex];
      if (!isTextContent(endContent)) {
        console.warn("Can't delete selection in parent if end is not TextContent");
        return;
      }

      // update text in startContent and remove everything before end
      const didRemoveStart = deleteInTextContentFromOffset(
        parent,
        startContent,
        start.contentIndex,
        start.cursorPosition,
      );
      const startDeletionOffset = didRemoveStart ? 0 : 1;

      // remove content between start and end
      const removeCount = end.contentIndex - start.contentIndex - 1;
      removeElements(start.contentIndex + startDeletionOffset, removeCount, parent);
      // remove from endContent
      deleteInTextContentToOffset(parent, endContent, start.contentIndex + startDeletionOffset, end.cursorPosition);
    } else if (start.contentIndex === end.contentIndex) {
      // both start and end is in the same content
      const originalText = text(startContent);
      if (end.cursorPosition > start.cursorPosition) {
        if (isLiteral(startContent)) {
          const newText = originalText.slice(0, start.cursorPosition) + originalText.slice(end.cursorPosition);
          updateLiteralText(startContent, newText);
        } else if (isVariable(startContent) || isNewLine(startContent)) {
          removeElements(start.contentIndex, 1, parent);
        }
      }
    }
  } else {
    // update text in startContent and remove everything after
    const didRemoveStart = deleteInTextContentFromOffset(
      parent,
      startContent,
      start.contentIndex,
      start.cursorPosition,
    );
    removeElements(start.contentIndex + (didRemoveStart ? 0 : 1), parent.content.length, parent);
  }

  // make sure we don't end up with empty parent
  if (parent.content.length === 0) {
    addElements([newLiteral()], 0, parent.content, parent.deletedContent);
  }
}

function deduceValidIndex(
  redigertBrev: EditedLetter,
  start: Focus & { cursorPosition: number },
): (Focus & { cursorPosition: number }) | undefined {
  const blockIndex = Math.max(0, Math.min(redigertBrev.blocks.length - 1, start.blockIndex));
  const block = redigertBrev.blocks[blockIndex];

  const contentIndex = Math.max(0, Math.min(block.content.length - 1, start.contentIndex));
  const content = block.content[contentIndex];

  if (isTextContent(content)) {
    return {
      blockIndex,
      contentIndex,
      cursorPosition:
        contentIndex === start.contentIndex
          ? Math.min(text(content).length, start.cursorPosition)
          : text(content).length,
    };
  } else if (isItemList(content)) {
    const itemIndex = Math.max(0, content.items.length - 1);
    const item = content.items[itemIndex];

    const itemContentIndex = Math.max(0, (item?.content.length ?? 1) - 1);
    const itemContent = item?.content[itemContentIndex];

    return {
      blockIndex,
      contentIndex,
      itemIndex,
      itemContentIndex,
      cursorPosition: isTextContent(itemContent) ? text(itemContent).length : 0,
    };
  } else if (isTable(content)) {
    const rowIndex = Math.max(-1, content.rows.length - 1);
    const cells =
      rowIndex === -1
        ? content.header.colSpec.map((spec) => spec.headerContent)
        : (content.rows[rowIndex]?.cells ?? []);

    const cellIndex = Math.max(0, (cells?.length ?? 1) - 1);
    const cell = cells[cellIndex];
    const cellContentIndex = Math.max(0, (cell?.text.length ?? 1) - 1);
    const cellContent = cell?.text[cellContentIndex];
    return {
      blockIndex,
      contentIndex,
      rowIndex,
      cellIndex,
      cellContentIndex,
      cursorPosition: isTextContent(cellContent) ? text(cellContent).length : 0,
    };
  } else {
    console.warn("Couldn't deduce valid index after deletion of selection");
    return undefined;
  }
}
