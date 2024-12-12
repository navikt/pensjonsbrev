import type { Draft } from "immer";
import { produce } from "immer";

import { deleteElements } from "~/Brevredigering/LetterEditor/actions/common";
import type { BlockContentIndex } from "~/Brevredigering/LetterEditor/actions/model";
import type { AnyBlock } from "~/types/brevbakerTypes";
import { PARAGRAPH, TITLE1, TITLE2 } from "~/types/brevbakerTypes";

import type { Action } from "../lib/actions";
import type { LetterEditorState } from "../model/state";
import { isTextContent } from "../model/utils";

export const switchTypography: Action<
  LetterEditorState,
  [literalIndex: BlockContentIndex, typography: typeof PARAGRAPH | typeof TITLE1 | typeof TITLE2]
> = produce((draft, literalIndex, typography) => {
  const block = draft.redigertBrev.blocks[literalIndex.blockIndex];

  if (!isTextContent(block.content[literalIndex.contentIndex])) {
    return;
  }

  if (!block.originalType && block.id !== null) {
    block.originalType = block.type;
  }

  draft.isDirty = true;

  switch (typography) {
    case PARAGRAPH: {
      block.type = typography;
      break;
    }

    case TITLE1:
    case TITLE2: {
      const { start, count } = findStartIndexAndCount(block, literalIndex);
      const originalContentCount = block.content.length;

      if (start === 0 && start + count === block.content.length) {
        block.type = typography;
      } else {
        const changedTypography = extractContentIntoNewBlock(block, start, count, typography);
        const changedTypographyIndex = literalIndex.blockIndex + (start === 0 ? 0 : 1);
        draft.redigertBrev.blocks.splice(changedTypographyIndex, 0, changedTypography);

        // If the content we changed typography for was in between non-text content,
        // also extract the subsequent content into a new block to maintain the order.
        if (start > 0 && start + count < originalContentCount) {
          const afterChanged = extractContentIntoNewBlock(block, start, block.content.length - start, PARAGRAPH);
          draft.redigertBrev.blocks.splice(changedTypographyIndex + 1, 0, afterChanged);

          deleteElements(afterChanged.content, block.content, block.deletedContent);
        }
      }
      break;
    }
  }
  if (block.originalType === block.type) {
    delete block.originalType;
  }
});

function extractContentIntoNewBlock(
  from: Draft<AnyBlock>,
  start: number,
  extractCount: number,
  targetType: typeof PARAGRAPH | typeof TITLE1 | typeof TITLE2,
): AnyBlock {
  const newBlock: AnyBlock = {
    id: null,
    parentId: null,
    type: targetType,
    editable: true,
    deletedContent: [],
    content: from.content.splice(start, extractCount),
  } as AnyBlock;
  deleteElements(newBlock.content, from.content, from.deletedContent);
  return newBlock;
}

function findStartIndexAndCount(block: AnyBlock, literalIndex: BlockContentIndex): { start: number; count: number } {
  let start = literalIndex.contentIndex;
  for (let index = start - 1; index >= 0; index--) {
    if (isTextContent(block.content[index])) {
      start = index;
    } else {
      break;
    }
  }

  let count = 0;
  for (let index = start; index < block.content.length; index++) {
    if (isTextContent(block.content[index])) {
      count++;
    } else {
      break;
    }
  }
  return { start, count };
}
