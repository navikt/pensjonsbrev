import { produce } from "immer";

import {
  addElements,
  findAdjoiningContent,
  newParagraph,
  newTitle,
  removeElements,
} from "~/Brevredigering/LetterEditor/actions/common";
import { PARAGRAPH, TITLE1, TITLE2 } from "~/types/brevbakerTypes";

import type { Action } from "../lib/actions";
import type { BlockContentIndex, LetterEditorState } from "../model/state";
import { isTextContent } from "../model/utils";

export const switchTypography: Action<
  LetterEditorState,
  [literalIndex: BlockContentIndex, typography: typeof PARAGRAPH | typeof TITLE1 | typeof TITLE2]
> = produce((draft, literalIndex, typography) => {
  const editedLetter = draft.redigertBrev;
  const block = editedLetter.blocks[literalIndex.blockIndex];

  if (!isTextContent(block.content[literalIndex.contentIndex])) {
    return;
  }

  // mark the block with originalType so that the block is concidered edited.
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
      const { startIndex, count } = findAdjoiningContent(literalIndex.contentIndex, block.content, isTextContent);

      if (startIndex === 0 && count === block.content.length) {
        block.type = typography;
      } else {
        const changedTypography = newTitle({
          type: typography,
          content: removeElements(startIndex, count, {
            content: block.content,
            deletedContent: block.deletedContent,
            id: block.id,
          }).filter(isTextContent),
        });
        const changedTypographyIndex = literalIndex.blockIndex + (startIndex === 0 ? 0 : 1);
        addElements([changedTypography], changedTypographyIndex, editedLetter.blocks, editedLetter.deletedBlocks);

        // If the content we changed typography for was in between non-text content,
        // also extract the subsequent content into a new block to maintain the order.
        if (startIndex > 0 && startIndex < block.content.length) {
          const afterChanged = newParagraph({
            content: removeElements(startIndex, block.content.length, {
              content: block.content,
              deletedContent: block.deletedContent,
              id: block.id,
            }),
          });
          addElements([afterChanged], changedTypographyIndex + 1, editedLetter.blocks, editedLetter.deletedBlocks);
        }
      }
      break;
    }
  }
  if (block.originalType === block.type) {
    delete block.originalType;
  }
});
