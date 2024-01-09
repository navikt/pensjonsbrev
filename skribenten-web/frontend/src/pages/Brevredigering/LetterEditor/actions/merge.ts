import { produce } from "immer";

import type { Block } from "~/types/brevbakerTypes";
import { ITEM_LIST, LITERAL } from "~/types/brevbakerTypes";

import type { Action } from "../lib/actions";
import type { LetterEditorState } from "../model/state";
import { getMergeIds, isEmptyBlock, isEmptyItem, isTextContent, mergeContentArrays } from "../model/utils";
import type { ContentId } from "./model";
import { MergeTarget } from "./model";

function deleteBlock(block: Block, blocks: Block[], deleted: number[]) {
  if (block.id != -1 && !deleted.includes(block.id) && !blocks.map((b) => b.id).includes(block.id)) {
    deleted.push(block.id);
  }
}

export const merge: Action<LetterEditorState, [mergeId: ContentId, target: MergeTarget]> = produce(
  (draft, mergeId, target) => {
    const editedLetter = draft.editedLetter;
    const blocks = editedLetter.letter.blocks;
    const previousContentSameBlock = blocks[mergeId.blockId]?.content[mergeId.contentId - 1];

    if ("itemId" in mergeId) {
      const itemList = blocks[mergeId.blockId].content[mergeId.contentId];
      if (itemList.type === ITEM_LIST) {
        const [firstId, secondId] = getMergeIds(mergeId.itemId, target);
        const first = itemList.items[firstId];
        const second = itemList.items[secondId];

        if (first != null && second != null) {
          if (isEmptyItem(first)) {
            draft.stealFocus[mergeId.blockId] = {
              contentId: mergeId.contentId,
              startOffset: 0,
              item: { id: firstId, contentId: 0 },
            };
            itemList.items.splice(firstId, 1);
          } else if (isEmptyItem(second)) {
            draft.stealFocus[mergeId.blockId] = {
              contentId: mergeId.contentId,
              startOffset: first.content.at(-1)?.text.length ?? 0,
              item: { id: firstId, contentId: first.content.length - 1 },
            };
            itemList.items.splice(secondId, 1);
          } else {
            draft.stealFocus[mergeId.blockId] = {
              contentId: mergeId.contentId,
              startOffset: first.content.at(-1)?.text.length ?? 0,
              item: { id: firstId, contentId: first.content.length - 1 },
            };
            first.content = mergeContentArrays(first.content, second.content);
            itemList.items.splice(secondId, 1);
          }
        }
      } else {
        // eslint-disable-next-line no-console
        console.warn("Got itemId, but block.content is not an itemList");
      }
    } else if (target === MergeTarget.PREVIOUS && previousContentSameBlock?.type === ITEM_LIST) {
      // The previous content of the block is an itemList, so we want to merge with the last item
      const content = blocks[mergeId.blockId].content;
      const lastItemId = previousContentSameBlock.items.length - 1;
      const lastItem = previousContentSameBlock.items[lastItemId];
      const lastContentOfLastItem = lastItem.content.at(-1);

      // Steal focus before we modify
      draft.stealFocus[mergeId.blockId] =
        lastContentOfLastItem?.type === LITERAL
          ? {
              contentId: mergeId.contentId - 1,
              startOffset: lastContentOfLastItem.text.length,
              item: { id: lastItemId, contentId: lastItem.content.length - 1 },
            }
          : {
              contentId: mergeId.contentId - 1,
              startOffset: 0,
              item: { id: lastItemId, contentId: lastItem.content.length },
            };

      // extract and remove all consecutive textContent after the itemList we want to merge into
      const nonTextContentRelativeIndex = content.slice(mergeId.contentId).findIndex((c) => !isTextContent(c));
      const textContentAfterList = content
        .splice(mergeId.contentId, nonTextContentRelativeIndex === -1 ? content.length : nonTextContentRelativeIndex)
        .filter(isTextContent);

      lastItem.content = mergeContentArrays(lastItem.content, textContentAfterList);
    } else {
      const [firstId, secondId] = getMergeIds(mergeId.blockId, target);
      const first = blocks[firstId];
      const second = blocks[secondId];

      if (first != null && second != null) {
        if (isEmptyBlock(first)) {
          blocks.splice(firstId, 1);
          deleteBlock(first, blocks, editedLetter.deletedBlocks);
          draft.stealFocus[firstId] = { contentId: 0, startOffset: 0 };
          draft.nextFocus = { contentIndex: 0, cursorPosition: 0, blockIndex: mergeId.blockId + 1 };
          draft.currentBlock = mergeId.blockId + 1;
        } else if (isEmptyBlock(second)) {
          blocks.splice(secondId, 1);
          deleteBlock(second, blocks, editedLetter.deletedBlocks);
          draft.nextFocus = { contentIndex: 0, cursorPosition: 0, blockIndex: mergeId.blockId - 1 };
          draft.currentBlock = mergeId.blockId - 1;
        } else {
          const lastContentOfFirst = first.content.at(-1);

          const nextContentIndexFocus = first.content.length - (lastContentOfFirst?.type === LITERAL ? 1 : 0);
          const nextStartOffset = lastContentOfFirst?.type === LITERAL ? lastContentOfFirst.text.length : 0;
          draft.nextFocus = {
            contentIndex: nextContentIndexFocus,
            cursorPosition: nextStartOffset,
            blockIndex: firstId,
          };
          draft.currentBlock = firstId;

          draft.stealFocus[firstId] =
            lastContentOfFirst?.type === LITERAL
              ? {
                  contentId: first.content.length - 1,
                  startOffset: lastContentOfFirst.text.length,
                }
              : { contentId: first.content.length, startOffset: 0 };

          first.content = mergeContentArrays(first.content, second.content);
          blocks.splice(secondId, 1);
          deleteBlock(second, blocks, editedLetter.deletedBlocks);
        }
      }
    }
  },
);
