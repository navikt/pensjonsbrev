import { produce } from "immer";

import {
  addElements,
  createNewLine,
  newLiteral,
  splitLiteralAtOffset,
  text,
} from "~/Brevredigering/LetterEditor/actions/common";
import type { Action } from "~/Brevredigering/LetterEditor/lib/actions";
import type { Focus, LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { isParagraph, isTextContent } from "~/Brevredigering/LetterEditor/model/utils";
import { LITERAL, NEW_LINE, VARIABLE } from "~/types/brevbakerTypes";

export const addNewLine: Action<LetterEditorState, [focus: Focus]> = produce((draft, focus) => {
  const block = draft.redigertBrev.blocks[focus.blockIndex];

  if (isParagraph(block)) {
    const content = block.content[focus.contentIndex];
    const offset = focus.cursorPosition;

    if (isTextContent(content) && !("itemIndex" in focus) && offset !== undefined) {
      switch (content.type) {
        case LITERAL: {
          // split literal and add new line
          if (offset === 0) {
            addElements([createNewLine()], focus.contentIndex, block.content, block.deletedContent);
          } else if (offset >= text(content).length) {
            const isAtEndOfBlock = focus.contentIndex + 1 === block.content.length;
            const toAdd = isAtEndOfBlock ? [createNewLine(), newLiteral({ text: "" })] : [createNewLine()];
            addElements(toAdd, focus.contentIndex + 1, block.content, block.deletedContent);
          } else {
            const newLiteral = splitLiteralAtOffset(content, offset);
            addElements([createNewLine(), newLiteral], focus.contentIndex + 1, block.content, block.deletedContent);
          }
          draft.isDirty = true;
          break;
        }
        case VARIABLE: {
          break;
        }
        case NEW_LINE: {
          addElements([createNewLine()], focus.contentIndex, block.content, block.deletedContent);
          draft.isDirty = true;
          break;
        }
      }
    }
  }
});
