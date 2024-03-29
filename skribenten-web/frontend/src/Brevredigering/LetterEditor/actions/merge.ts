import { produce } from "immer";

import type { Block } from "~/types/brevbakerTypes";
import { ITEM_LIST, LITERAL } from "~/types/brevbakerTypes";

import type { Action } from "../lib/actions";
import type { LetterEditorState } from "../model/state";
import { getMergeIds, isEmptyBlock, isEmptyItem, isTextContent, mergeContentArrays } from "../model/utils";
import type { LiteralIndex } from "./model";

export enum MergeTarget {
  PREVIOUS = "PREVIOUS",
  NEXT = "NEXT",
}

function deleteBlock(block: Block, blocks: Block[], deleted: number[]) {
  if (block.id != -1 && !deleted.includes(block.id) && !blocks.map((b) => b.id).includes(block.id)) {
    deleted.push(block.id);
  }
}

export const merge: Action<LetterEditorState, [literalIndex: LiteralIndex, target: MergeTarget]> = produce(
  (draft, literalIndex, target) => {
    const editedLetter = draft.editedLetter;
    const blocks = editedLetter.letter.blocks;
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
          } else if (isEmptyItem(second)) {
            draft.focus = {
              blockIndex: literalIndex.blockIndex,
              contentIndex: literalIndex.contentIndex,
              cursorPosition: first.content.at(-1)?.text.length ?? 0,
              itemContentIndex: first.content.length - 1,
              itemIndex: firstId,
            };
            itemList.items.splice(secondId, 1);
          } else {
            draft.focus = {
              blockIndex: literalIndex.blockIndex,
              contentIndex: literalIndex.contentIndex,
              cursorPosition: first.content.at(-1)?.text.length ?? 0,
              itemContentIndex: first.content.length - 1,
              itemIndex: firstId,
            };
            first.content = mergeContentArrays(first.content, second.content);
            itemList.items.splice(secondId, 1);
          }
        }
      } else {
        // eslint-disable-next-line no-console
        console.warn("Got itemIndex, but block.content is not an itemList");
      }
    } else if (target === MergeTarget.PREVIOUS && previousContentSameBlock?.type === ITEM_LIST) {
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
              cursorPosition: lastContentOfLastItem.text.length,
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
          deleteBlock(first, blocks, editedLetter.deletedBlocks);
          draft.focus = { contentIndex: 0, cursorPosition: 0, blockIndex: firstId };
        } else if (isEmptyBlock(second)) {
          blocks.splice(secondId, 1);
          deleteBlock(second, blocks, editedLetter.deletedBlocks);
          draft.focus = { contentIndex: 0, cursorPosition: 0, blockIndex: literalIndex.blockIndex - 1 };
        } else {
          const lastContentOfFirst = first.content.at(-1);

          const nextContentIndexFocus = first.content.length - (lastContentOfFirst?.type === LITERAL ? 1 : 0);
          const nextStartOffset = lastContentOfFirst?.type === LITERAL ? lastContentOfFirst.text.length : 0;
          draft.focus = {
            contentIndex: nextContentIndexFocus,
            cursorPosition: nextStartOffset,
            blockIndex: firstId,
          };

          first.content = mergeContentArrays(first.content, second.content);
          blocks.splice(secondId, 1);
          deleteBlock(second, blocks, editedLetter.deletedBlocks);
        }
      }
    }
  },
);
