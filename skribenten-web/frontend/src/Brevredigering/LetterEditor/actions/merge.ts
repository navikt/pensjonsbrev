import type { Draft } from "immer";
import { produce } from "immer";

import {
  addElements,
  getMergeIds,
  newLiteral,
  removeElements,
  text,
} from "~/Brevredigering/LetterEditor/actions/common";
import type { AnyBlock, ItemList } from "~/types/brevbakerTypes";
import { ITEM_LIST, LITERAL, VARIABLE } from "~/types/brevbakerTypes";

import type { Action } from "../lib/actions";
import type { Focus, LetterEditorState } from "../model/state";
import { isEmptyBlock, isEmptyContent, isEmptyItem, isTextContent } from "../model/utils";
import type { ItemContentIndex, LiteralIndex } from "./model";

export enum MergeTarget {
  PREVIOUS = "PREVIOUS",
  NEXT = "NEXT",
}

export const merge: Action<LetterEditorState, [literalIndex: LiteralIndex, target: MergeTarget]> = produce(
  (draft, literalIndex, target) => {
    const editedLetter = draft.redigertBrev;
    const blocks = editedLetter.blocks;
    const block = blocks[literalIndex.blockIndex];
    const previousContentSameBlock = blocks[literalIndex.blockIndex]?.content[literalIndex.contentIndex - 1];

    draft.isDirty = true;

    if ("itemIndex" in literalIndex) {
      mergeFromItemList(draft, literalIndex, target);
    } else if (target === MergeTarget.PREVIOUS && previousContentSameBlock?.type === ITEM_LIST) {
      mergeIntoItemList(draft, previousContentSameBlock, literalIndex);
    } else if (target === MergeTarget.PREVIOUS && previousContentSameBlock?.type === LITERAL) {
      // TODO: må se på denne her.
      const block = blocks[literalIndex.blockIndex];
      const content = block?.content[literalIndex.contentIndex];
      if (isEmptyContent(content)) {
        removeElements(literalIndex.contentIndex, 1, block.content, block.deletedContent);
        draft.focus = {
          blockIndex: literalIndex.blockIndex,
          contentIndex: literalIndex.contentIndex - 1,
          cursorPosition: text(previousContentSameBlock).length,
        };
      } else {
        draft.focus = {
          blockIndex: literalIndex.blockIndex,
          contentIndex: literalIndex.contentIndex - 1,
          cursorPosition: text(previousContentSameBlock).length,
        };
      }
    } else if (
      (target === MergeTarget.PREVIOUS && literalIndex.contentIndex === 0) ||
      (target == MergeTarget.NEXT && isLastIndex(literalIndex.contentIndex, block.content))
    ) {
      mergeBlocks(draft, literalIndex, target);
    }
  },
);

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
      removeElements(firstId, 1, blocks, draft.redigertBrev.deletedBlocks);
      draft.focus = { contentIndex: 0, cursorPosition: 0, blockIndex: firstId };
    } else if (isEmptyBlock(second)) {
      removeElements(secondId, 1, blocks, draft.redigertBrev.deletedBlocks);
      if (first.content.at(-1)?.type === VARIABLE) {
        first.content.push(newLiteral({ text: "" }));
      }
      draft.focus = focusEndOfBlock(firstId, first);
    } else {
      const lastContentOfFirst = first.content.at(-1);

      const nextContentIndexFocus = first.content.length - (lastContentOfFirst?.type === LITERAL ? 1 : 0);
      const nextStartOffset = lastContentOfFirst?.type === LITERAL ? text(lastContentOfFirst).length : 0;
      draft.focus = {
        contentIndex: nextContentIndexFocus,
        cursorPosition: nextStartOffset,
        blockIndex: firstId,
      };

      addElements(second.content, first.content.length, first.content, first.deletedContent);
      removeElements(secondId, 1, blocks, draft.redigertBrev.deletedBlocks);
    }
  }
}

function mergeFromItemList(draft: Draft<LetterEditorState>, literalIndex: ItemContentIndex, target: MergeTarget) {
  const blocks = draft.redigertBrev.blocks;

  const itemList = blocks[literalIndex.blockIndex].content[literalIndex.contentIndex];
  if (itemList.type === ITEM_LIST) {
    const [firstId, secondId] = getMergeIds(literalIndex.itemIndex, target);
    const first = itemList.items[firstId];
    const second = itemList.items[secondId];

    if (first != null && second != null) {
      if (isEmptyItem(first)) {
        draft.focus = {
          blockIndex: literalIndex.blockIndex,
          contentIndex: literalIndex.contentIndex,
          cursorPosition: 0,
          itemIndex: firstId,
          itemContentIndex: 0,
        };
        removeElements(firstId, 1, itemList.items, itemList.deletedItems);
      } else if (isEmptyItem(second)) {
        draft.focus = {
          blockIndex: literalIndex.blockIndex,
          contentIndex: literalIndex.contentIndex,
          cursorPosition: text(first.content.at(-1))?.length ?? 0,
          itemContentIndex: first.content.length - 1,
          itemIndex: firstId,
        };
        removeElements(secondId, 1, itemList.items, itemList.deletedItems);
      } else {
        draft.focus = {
          blockIndex: literalIndex.blockIndex,
          contentIndex: literalIndex.contentIndex,
          cursorPosition: text(first.content.at(-1))?.length ?? 0,
          itemContentIndex: first.content.length - 1,
          itemIndex: firstId,
        };
        // TODO: items har ikke deletedContent (enda?), derav `[]` som deleted
        addElements(second.content, first.content.length, first.content, []);
        removeElements(secondId, 1, itemList.items, itemList.deletedItems);
      }
    } else if (target === MergeTarget.PREVIOUS && isEmptyItem(second) && itemList.items.length === 1) {
      // TODO: burde generaliseres slik at det fungerer som toggling av et hvilket som helst punkt
      // We have a list with one empty element. That means that we want to break out of the list.
      const block = blocks[literalIndex.blockIndex];
      const contentBeforeItemList = block.content[literalIndex.contentIndex - 1];

      removeElements(literalIndex.contentIndex, 1, block.content, block.deletedContent);

      // TODO: Denne burde nok være en switch, slik at vi får håndtert andre typer content.
      if (contentBeforeItemList?.type === VARIABLE) {
        addElements([newLiteral({ text: "" })], literalIndex.contentIndex, block.content, block.deletedContent);
        draft.focus = {
          blockIndex: literalIndex.blockIndex,
          contentIndex: literalIndex.contentIndex,
          cursorPosition: 0,
        };
      } else if (contentBeforeItemList?.type === LITERAL) {
        draft.focus = {
          blockIndex: literalIndex.blockIndex,
          contentIndex: Math.max(0, literalIndex.contentIndex - 1),
          cursorPosition: text(contentBeforeItemList).length,
        };
      }
    }
  } else {
    // eslint-disable-next-line no-console
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
    lastContentOfLastItem?.type === LITERAL
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
    .findIndex((c) => !isTextContent(c));
  const nonTextContentIndex = nonTextContentRelativeIndex === -1 ? block.content.length : nonTextContentRelativeIndex;

  const textContentAfterList = removeElements(
    literalIndex.contentIndex,
    nonTextContentIndex,
    block.content,
    block.deletedContent,
  ).filter(isTextContent);

  // TODO: item har ikke deletedContent (enda?), derav `[]` som deleted
  addElements(textContentAfterList, lastItem.content.length, lastItem.content, []);
}

function focusEndOfBlock(blockId: number, block: AnyBlock): Focus {
  const lastContent = block.content.at(-1);
  return {
    blockIndex: blockId,
    contentIndex: block.content.length - 1,
    cursorPosition: lastContent?.type === LITERAL ? text(lastContent).length : 0,
  };
}
