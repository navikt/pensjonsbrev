import { produce } from "immer";

import { text } from "~/Brevredigering/LetterEditor/actions/common";
import type { AnyBlock, Identifiable } from "~/types/brevbakerTypes";
import { VARIABLE } from "~/types/brevbakerTypes";
import { ITEM_LIST, LITERAL } from "~/types/brevbakerTypes";

import type { Action } from "../lib/actions";
import type { Focus, LetterEditorState } from "../model/state";
import { getMergeIds, isEmptyBlock, isEmptyItem, isTextContent, mergeContentArrays } from "../model/utils";
import type { LiteralIndex } from "./model";

export enum MergeTarget {
  PREVIOUS = "PREVIOUS",
  NEXT = "NEXT",
}

function deleteElement(toDelete: Identifiable, verifyNotPresent: Identifiable[], deleted: number[]) {
  if (
    toDelete.id !== null &&
    !deleted.includes(toDelete.id) &&
    !verifyNotPresent.map((b) => b.id).includes(toDelete.id)
  ) {
    deleted.push(toDelete.id);
  }
}
function updateDeleted(deleted: number[], present: Identifiable[]): number[] {
  const presentIds = new Set(present.map((element) => element.id));
  return deleted.filter((d) => !presentIds.has(d));
}

export const merge: Action<LetterEditorState, [literalIndex: LiteralIndex, target: MergeTarget]> = produce(
  (draft, literalIndex, target) => {
    const editedLetter = draft.redigertBrev;
    const blocks = editedLetter.blocks;
    const previousContentSameBlock = blocks[literalIndex.blockIndex]?.content[literalIndex.contentIndex - 1];

    if ("itemIndex" in literalIndex) {
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
            itemList.items.splice(firstId, 1);
            deleteElement(first, itemList.items, itemList.deletedItems);
          } else if (isEmptyItem(second)) {
            draft.focus = {
              blockIndex: literalIndex.blockIndex,
              contentIndex: literalIndex.contentIndex,
              cursorPosition: text(first.content.at(-1))?.length ?? 0,
              itemContentIndex: first.content.length - 1,
              itemIndex: firstId,
            };
            itemList.items.splice(secondId, 1);
            deleteElement(second, itemList.items, itemList.deletedItems);
          } else {
            draft.focus = {
              blockIndex: literalIndex.blockIndex,
              contentIndex: literalIndex.contentIndex,
              cursorPosition: text(first.content.at(-1))?.length ?? 0,
              itemContentIndex: first.content.length - 1,
              itemIndex: firstId,
            };
            first.content = mergeContentArrays(first.content, second.content);
            itemList.items.splice(secondId, 1);
            deleteElement(second, itemList.items, itemList.deletedItems);
          }
        }
      } else {
        // eslint-disable-next-line no-console
        console.warn("Got itemIndex, but block.content is not an itemList");
      }
    } else if (target === MergeTarget.PREVIOUS && previousContentSameBlock?.type === ITEM_LIST) {
      // This is when merging inside a block with an itemList
      // The previous content of the block is an itemList, so we want to merge with the last item
      const content = blocks[literalIndex.blockIndex].content;
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
              contentIndex: literalIndex.contentIndex - 1,
              cursorPosition: 0,
              itemIndex: lastItemId,
              blockIndex: literalIndex.blockIndex,
              itemContentIndex: lastItem.content.length,
            };

      // extract and remove all consecutive textContent after the itemList we want to merge into
      const nonTextContentRelativeIndex = content.slice(literalIndex.contentIndex).findIndex((c) => !isTextContent(c));
      const textContentAfterList = content
        .splice(
          literalIndex.contentIndex,
          nonTextContentRelativeIndex === -1 ? content.length : nonTextContentRelativeIndex,
        )
        .filter(isTextContent);

      lastItem.content = mergeContentArrays(lastItem.content, textContentAfterList);
    } else {
      const [firstId, secondId] = getMergeIds(literalIndex.blockIndex, target);
      const first = blocks[firstId];
      const second = blocks[secondId];

      if (first != null && second != null) {
        if (isEmptyBlock(first)) {
          blocks.splice(firstId, 1);
          deleteElement(first, blocks, editedLetter.deletedBlocks);
          draft.focus = { contentIndex: 0, cursorPosition: 0, blockIndex: firstId };
        } else if (isEmptyBlock(second)) {
          blocks.splice(secondId, 1);
          deleteElement(second, blocks, editedLetter.deletedBlocks);
          if (first.content.at(-1)?.type === VARIABLE) {
            first.content.push({ type: LITERAL, id: null, text: "", editedText: "" });
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

          first.content = mergeContentArrays(first.content, second.content);
          blocks.splice(secondId, 1);
          deleteElement(second, blocks, editedLetter.deletedBlocks);
          first.deletedContent = updateDeleted(first.deletedContent, first.content);
        }
      }
    }
  },
);

function focusEndOfBlock(blockId: number, block: AnyBlock): Focus {
  const lastContent = block.content.at(-1);
  return {
    blockIndex: blockId,
    contentIndex: block.content.length - 1,
    cursorPosition: lastContent?.type === LITERAL ? text(lastContent).length : 0,
  };
}
