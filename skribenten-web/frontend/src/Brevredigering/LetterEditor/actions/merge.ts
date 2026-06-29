import { type Draft, type WritableDraft } from "immer";

import {
  absorbListIntoList,
  addElements,
  breakOutEmptyItem,
  getMergeIds,
  isValidIndex,
  mergeLiteralsIfPossible,
  newLiteral,
  removeElements,
  text,
} from "~/Brevredigering/LetterEditor/actions/common";
import {
  type AnyBlock,
  type ItemList,
  LITERAL,
  type LiteralValue,
  NEW_LINE,
  type ParagraphBlock,
  TITLE_INDEX,
  type Title1Block,
  type Title2Block,
  type Title3Block,
} from "~/types/brevbakerTypes";

import { type Action, withPatches } from "../lib/actions";
import { type Focus, type ItemContentIndex, type LetterEditorState, type LiteralIndex } from "../model/state";
import {
  effectiveListType,
  isEmptyBlock,
  isEmptyContent,
  isEmptyItem,
  isItemList,
  isLiteral,
  isTextContent,
} from "../model/utils";

export enum MergeTarget {
  PREVIOUS = "PREVIOUS",
  NEXT = "NEXT",
}

export const merge: Action<LetterEditorState, [literalIndex: LiteralIndex, target: MergeTarget]> =
  withPatches(mergeRecipe);

export function mergeRecipe(draft: Draft<LetterEditorState>, literalIndex: LiteralIndex, target: MergeTarget) {
  if (literalIndex.blockIndex === TITLE_INDEX) {
    return;
  }
  if (!isValidIndex(draft.redigertBrev, literalIndex)) {
    console.warn("Invalid index for merge", literalIndex);
    return;
  }

  const block = draft.redigertBrev.blocks[literalIndex.blockIndex];
  const previousContentSameBlock = block.content[literalIndex.contentIndex - 1];
  const nextContentSameBlock = block.content[literalIndex.contentIndex + 1];

  if ("itemIndex" in literalIndex) {
    mergeFromItemList(draft, literalIndex, target);
    draft.saveStatus = "DIRTY";
  } else if (target === MergeTarget.PREVIOUS && previousContentSameBlock?.type === "ITEM_LIST") {
    mergeIntoItemList(draft, previousContentSameBlock, literalIndex);
    draft.saveStatus = "DIRTY";
  } else if (target === MergeTarget.PREVIOUS && previousContentSameBlock?.type === "LITERAL") {
    const content = block?.content[literalIndex.contentIndex];
    const cursorPosition = text(previousContentSameBlock).length;
    if (isEmptyContent(content)) {
      removeElements(literalIndex.contentIndex, 1, {
        content: block.content,
        deletedContent: block.deletedContent,
        id: block.id,
      });
    } else if (isLiteral(previousContentSameBlock) && isLiteral(content)) {
      updateElementsWithPossiblyMergedLiterals(block, literalIndex.contentIndex - 1, previousContentSameBlock, content);
    }
    draft.saveStatus = "DIRTY";
    draft.focus = {
      blockIndex: literalIndex.blockIndex,
      contentIndex: literalIndex.contentIndex - 1,
      cursorPosition: cursorPosition,
    };
  } else if (
    (target === MergeTarget.PREVIOUS && literalIndex.contentIndex === 0) ||
    (target === MergeTarget.NEXT && isLastIndex(literalIndex.contentIndex, block.content))
  ) {
    mergeBlocks(draft, literalIndex, target);
    draft.saveStatus = "DIRTY";
  } else if (
    (target === MergeTarget.PREVIOUS && previousContentSameBlock?.type === "NEW_LINE") ||
    (target === MergeTarget.NEXT && nextContentSameBlock?.type === "NEW_LINE")
  ) {
    const nextOrPrevMod = target === MergeTarget.NEXT ? 1 : -1;
    // Remove NEW_LINE
    removeElements(literalIndex.contentIndex + nextOrPrevMod, 1, {
      content: block.content,
      deletedContent: block.deletedContent,
      id: block.id,
    });
    draft.saveStatus = "DIRTY";

    const beforeNewLineContent = block.content[literalIndex.contentIndex - 1 + nextOrPrevMod];
    const afterNewLineContent = block.content[literalIndex.contentIndex + nextOrPrevMod];
    const cursorPosition = isTextContent(beforeNewLineContent) ? text(beforeNewLineContent).length : 0;
    if (isLiteral(beforeNewLineContent) && isLiteral(afterNewLineContent)) {
      updateElementsWithPossiblyMergedLiterals(
        block,
        literalIndex.contentIndex - 1 + nextOrPrevMod,
        beforeNewLineContent,
        afterNewLineContent,
      );
      draft.focus = {
        blockIndex: literalIndex.blockIndex,
        contentIndex: literalIndex.contentIndex - 1 + nextOrPrevMod,
        cursorPosition: cursorPosition,
      };
    } else {
      draft.focus = {
        blockIndex: literalIndex.blockIndex,
        contentIndex: literalIndex.contentIndex - (target === MergeTarget.PREVIOUS ? 1 : 0),
        cursorPosition: target === MergeTarget.PREVIOUS ? 0 : cursorPosition,
      };
    }
  }
}

function updateElementsWithPossiblyMergedLiterals(
  block:
    | WritableDraft<Title1Block>
    | WritableDraft<Title2Block>
    | WritableDraft<Title3Block>
    | WritableDraft<ParagraphBlock>,
  contentIndex: number,
  firstLiteral: WritableDraft<LiteralValue>,
  secondLiteral: WritableDraft<LiteralValue>,
) {
  removeElements(contentIndex, 2, {
    content: block.content,
    deletedContent: block.deletedContent,
    id: block.id,
  });
  addElements(mergeLiteralsIfPossible(firstLiteral, secondLiteral), contentIndex, block.content, block.deletedContent);
}

function isLastIndex(index: number, array: unknown[]): boolean {
  return (index === 0 && array.length === 0) || index === array.length - 1;
}

function mergeBlocks(draft: Draft<LetterEditorState>, literalIndex: LiteralIndex, target: MergeTarget) {
  const blocks = draft.redigertBrev.blocks;
  const [firstId, secondId] = getMergeIds(literalIndex.blockIndex, target);
  const first = blocks[firstId];
  const second = blocks[secondId];

  if (first != null && second != null) {
    if (isEmptyBlock(first)) {
      removeElements(firstId, 1, { content: blocks, deletedContent: draft.redigertBrev.deletedBlocks, id: null });
      draft.focus = { contentIndex: 0, cursorPosition: 0, blockIndex: firstId };
      // After removing the empty block, try to merge adjacent list blocks
      mergeAdjacentListBlocks(draft, firstId - 1);
    } else if (isEmptyBlock(second)) {
      removeElements(secondId, 1, { content: blocks, deletedContent: draft.redigertBrev.deletedBlocks, id: null });
      if (first.content.at(-1)?.type === "VARIABLE") {
        first.content.push(newLiteral());
      }
      draft.focus = focusEndOfBlock(firstId, first);
      // After removing the empty block, try to merge adjacent list blocks
      mergeAdjacentListBlocks(draft, firstId);
    } else {
      const lastContentOfFirst = first.content.at(-1);

      if (isItemList(lastContentOfFirst)) {
        // Previous block ends with a list: merge text into last item, then merge adjacent lists
        mergeBlockIntoList(draft, firstId, secondId, lastContentOfFirst);
      } else {
        const nextContentIndexFocus = first.content.length - (lastContentOfFirst?.type === LITERAL ? 1 : 0);
        const nextStartOffset = lastContentOfFirst?.type === LITERAL ? text(lastContentOfFirst).length : 0;
        draft.focus = {
          contentIndex: nextContentIndexFocus,
          cursorPosition: nextStartOffset,
          blockIndex: firstId,
        };

        addElements(second.content, first.content.length, first.content, first.deletedContent);
        removeElements(secondId, 1, { content: blocks, deletedContent: draft.redigertBrev.deletedBlocks, id: null });
      }
    }
  }
}

/**
 * Merges the content of the second block into the last item of the list that ends the first block.
 * Then tries to merge adjacent same-type lists (without overriding focus).
 */
function mergeBlockIntoList(
  draft: Draft<LetterEditorState>,
  firstBlockId: number,
  secondBlockId: number,
  targetList: Draft<ItemList>,
) {
  const blocks = draft.redigertBrev.blocks;
  const secondBlock = blocks[secondBlockId];
  const firstBlock = blocks[firstBlockId];
  // biome-ignore lint/complexity/useIndexOf: targetList is ItemList, not assignable to Content — findIndex avoids the type mismatch
  const listContentIndex = firstBlock.content.findIndex((content) => content === targetList);

  const lastItemIndex = targetList.items.length - 1;
  const lastItem = targetList.items[lastItemIndex];
  const lastContentOfLastItem = lastItem.content.at(-1);
  const cursorPosition = isLiteral(lastContentOfLastItem) ? text(lastContentOfLastItem).length : 0;
  const cursorItemContentIndex = lastItem.content.length - 1;

  // Extract leading text content from the second block to merge into the last item
  const firstNonTextIndex = secondBlock.content.findIndex((content) => !isTextContent(content));
  const textEndIndex = firstNonTextIndex === -1 ? secondBlock.content.length : firstNonTextIndex;

  // Add text content to the last item of the list (before removing the block, to avoid Immer proxy revocation)
  if (textEndIndex > 0) {
    const textToMerge = removeElements(0, textEndIndex, {
      content: secondBlock.content,
      deletedContent: secondBlock.deletedContent,
      id: secondBlock.id,
    }).filter(isTextContent);
    if (textToMerge.length > 0) {
      addElements(textToMerge, lastItem.content.length, lastItem.content, lastItem.deletedContent);
    }
  }

  // Remaining content stays in secondBlock. If empty, remove the block entirely.
  if (secondBlock.content.length === 0) {
    removeElements(secondBlockId, 1, { content: blocks, deletedContent: draft.redigertBrev.deletedBlocks, id: null });
  } else {
    // Leave the remaining non-text content as its own block (already in place)
  }

  // Merge adjacent same-type list blocks first, then override focus with the text-merge point.
  mergeAdjacentListBlocks(draft, firstBlockId);

  // Focus lands at the text-merge point: end of the original last item, before any appended text.
  draft.focus = {
    blockIndex: firstBlockId,
    contentIndex: listContentIndex,
    cursorPosition: cursorPosition,
    itemIndex: lastItemIndex,
    itemContentIndex: cursorItemContentIndex,
  };
}

/**
 * After removing a block, check if the blocks at prevBlockIndex and prevBlockIndex+1
 * are both single-list blocks of the same type, and if so merge them into one.
 */
function mergeAdjacentListBlocks(draft: Draft<LetterEditorState>, prevBlockIndex: number) {
  const blocks = draft.redigertBrev.blocks;
  const prevBlock = blocks[prevBlockIndex];
  const nextBlock = blocks[prevBlockIndex + 1];

  if (!prevBlock || !nextBlock) return;

  // Both blocks must contain exactly one item: an ItemList
  if (prevBlock.content.length !== 1 || nextBlock.content.length !== 1) return;
  const prevContent = prevBlock.content[0];
  const nextContent = nextBlock.content[0];
  if (!isItemList(prevContent) || !isItemList(nextContent)) return;

  // Lists must have the same effective type
  if (effectiveListType(prevContent) !== effectiveListType(nextContent)) return;

  // Append nextContent's items into prevContent (the lower-indexed, surviving list).
  const lastItemIndexOfFirst = prevContent.items.length - 1;
  absorbListIntoList(draft, prevContent, prevBlockIndex + 1, 0, "back");

  // Focus at end of the last item of the FIRST list (the merge boundary)
  const lastItemOfFirst = prevContent.items[lastItemIndexOfFirst];
  const lastContentOfItem = lastItemOfFirst?.content.at(-1);
  draft.focus = {
    blockIndex: prevBlockIndex,
    contentIndex: 0,
    cursorPosition: lastContentOfItem ? text(lastContentOfItem).length : 0,
    itemIndex: lastItemIndexOfFirst,
    itemContentIndex: (lastItemOfFirst?.content.length ?? 1) - 1,
  };
}

function mergeFromItemList(draft: Draft<LetterEditorState>, literalIndex: ItemContentIndex, target: MergeTarget) {
  const blocks = draft.redigertBrev.blocks;
  const block = blocks[literalIndex.blockIndex];

  const itemList = block.content[literalIndex.contentIndex];
  if (isItemList(itemList)) {
    const currentItem = itemList.items[literalIndex.itemIndex];

    // If the adjacent content within the same item is a NEW_LINE, remove it (and merge surrounding
    // literals if possible).  This mirrors the block-level NEW_LINE removal in mergeRecipe.
    if (currentItem != null) {
      const mod = target === MergeTarget.PREVIOUS ? -1 : 1;
      const adjacentInItem = currentItem.content[literalIndex.itemContentIndex + mod];
      if (adjacentInItem?.type === NEW_LINE) {
        const newLineIndex = literalIndex.itemContentIndex + mod;
        removeElements(newLineIndex, 1, {
          content: currentItem.content,
          deletedContent: currentItem.deletedContent,
          id: currentItem.id,
        });
        // After NEW_LINE removal, before/after are now adjacent at newLineIndex-1 and newLineIndex
        const beforeContent = currentItem.content[newLineIndex - 1];
        const afterContent = currentItem.content[newLineIndex];
        const cursorPositionAtBoundary = isTextContent(beforeContent) ? text(beforeContent).length : 0;
        if (isLiteral(beforeContent) && isLiteral(afterContent)) {
          // Remove both and re-insert merged (or the pair if they can't merge)
          removeElements(newLineIndex - 1, 2, {
            content: currentItem.content,
            deletedContent: currentItem.deletedContent,
            id: currentItem.id,
          });
          addElements(
            mergeLiteralsIfPossible(beforeContent, afterContent),
            newLineIndex - 1,
            currentItem.content,
            currentItem.deletedContent,
          );
          // Focus at the start of the merged/before literal
          draft.focus = {
            blockIndex: literalIndex.blockIndex,
            contentIndex: literalIndex.contentIndex,
            itemIndex: literalIndex.itemIndex,
            itemContentIndex: newLineIndex - 1,
            cursorPosition: cursorPositionAtBoundary,
          };
        } else {
          // Focus stays at the literal where the cursor was (shifted by -1 if PREVIOUS due to removed NEW_LINE)
          const focusIndex =
            target === MergeTarget.PREVIOUS ? literalIndex.itemContentIndex - 1 : literalIndex.itemContentIndex;
          draft.focus = {
            blockIndex: literalIndex.blockIndex,
            contentIndex: literalIndex.contentIndex,
            itemIndex: literalIndex.itemIndex,
            itemContentIndex: focusIndex,
            cursorPosition: target === MergeTarget.PREVIOUS ? 0 : cursorPositionAtBoundary,
          };
        }
        return;
      }
    }

    const [firstId, secondId] = getMergeIds(literalIndex.itemIndex, target);
    const first = itemList.items[firstId];
    const second = itemList.items[secondId];

    // Backspace on an empty item: break out of list into separate blocks
    if (target === MergeTarget.PREVIOUS && second != null && isEmptyItem(second)) {
      breakOutEmptyItem(draft, literalIndex, itemList, block);
      return;
    }

    if (first != null && second != null) {
      if (isEmptyItem(first)) {
        draft.focus = {
          blockIndex: literalIndex.blockIndex,
          contentIndex: literalIndex.contentIndex,
          cursorPosition: 0,
          itemIndex: firstId,
          itemContentIndex: 0,
        };
        removeElements(firstId, 1, { content: itemList.items, deletedContent: itemList.deletedItems, id: itemList.id });
      } else if (isEmptyItem(second)) {
        draft.focus = {
          blockIndex: literalIndex.blockIndex,
          contentIndex: literalIndex.contentIndex,
          cursorPosition: text(first.content.at(-1))?.length ?? 0,
          itemContentIndex: first.content.length - 1,
          itemIndex: firstId,
        };
        removeElements(secondId, 1, {
          content: itemList.items,
          deletedContent: itemList.deletedItems,
          id: itemList.id,
        });
      } else {
        draft.focus = {
          blockIndex: literalIndex.blockIndex,
          contentIndex: literalIndex.contentIndex,
          cursorPosition: text(first.content.at(-1))?.length ?? 0,
          itemContentIndex: first.content.length - 1,
          itemIndex: firstId,
        };
        addElements(second.content, first.content.length, first.content, first.deletedContent);
        removeElements(secondId, 1, {
          content: itemList.items,
          deletedContent: itemList.deletedItems,
          id: itemList.id,
        });
      }
    }
  } else {
    console.warn("Got itemIndex, but block.content is not an itemList");
  }
}

function mergeIntoItemList(
  draft: Draft<LetterEditorState>,
  previousContentSameBlock: Draft<ItemList>,
  literalIndex: LiteralIndex,
) {
  // This is when merging inside a block with an itemList
  // The previous content of the block is an itemList, so we want to merge with the last item
  const block = draft.redigertBrev.blocks[literalIndex.blockIndex];
  const lastItemId = previousContentSameBlock.items.length - 1;
  const lastItem = previousContentSameBlock.items[lastItemId];
  const lastContentOfLastItem = lastItem.content.at(-1);

  draft.focus =
    lastContentOfLastItem?.type === "LITERAL"
      ? {
          blockIndex: literalIndex.blockIndex,
          contentIndex: literalIndex.contentIndex - 1,
          cursorPosition: text(lastContentOfLastItem).length,
          itemIndex: lastItemId,
          itemContentIndex: lastItem.content.length - 1,
        }
      : {
          blockIndex: literalIndex.blockIndex,
          contentIndex: literalIndex.contentIndex - 1,
          cursorPosition: 0,
          itemIndex: lastItemId,
          itemContentIndex: lastItem.content.length,
        };

  // extract and remove all consecutive textContent after the itemList we want to merge into
  const nonTextContentRelativeIndex = block.content
    .slice(literalIndex.contentIndex)
    .findIndex((content) => !isTextContent(content));
  const nonTextContentIndex = nonTextContentRelativeIndex === -1 ? block.content.length : nonTextContentRelativeIndex;

  const textContentAfterList = removeElements(literalIndex.contentIndex, nonTextContentIndex, {
    content: block.content,
    deletedContent: block.deletedContent,
    id: block.id,
  }).filter(isTextContent);

  addElements(textContentAfterList, lastItem.content.length, lastItem.content, lastItem.deletedContent);
}

function focusEndOfBlock(blockId: number, block: AnyBlock): Focus {
  const lastContent = block.content.at(-1);
  const contentIndex = block.content.length - 1;
  if (isItemList(lastContent) && lastContent.items.length > 0) {
    const lastItemIndex = lastContent.items.length - 1;
    const lastItem = lastContent.items[lastItemIndex];
    const lastItemContent = lastItem.content.at(-1);
    return {
      blockIndex: blockId,
      contentIndex,
      itemIndex: lastItemIndex,
      itemContentIndex: lastItem.content.length - 1,
      cursorPosition: isLiteral(lastItemContent) ? text(lastItemContent).length : 0,
    };
  }
  return {
    blockIndex: blockId,
    contentIndex,
    cursorPosition: lastContent?.type === LITERAL ? text(lastContent).length : 0,
  };
}
