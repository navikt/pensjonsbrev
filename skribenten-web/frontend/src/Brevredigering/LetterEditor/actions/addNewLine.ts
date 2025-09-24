import {
  addElements,
  createNewLine,
  newLiteral,
  splitLiteralAtOffset,
  text,
} from "~/Brevredigering/LetterEditor/actions/common";
import type { Action } from "~/Brevredigering/LetterEditor/lib/actions";
import { withPatches } from "~/Brevredigering/LetterEditor/lib/actions";
import type { Focus, LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { isParagraph, isTextContent } from "~/Brevredigering/LetterEditor/model/utils";
import { LITERAL, NEW_LINE, VARIABLE } from "~/types/brevbakerTypes";

export const addNewLine: Action<LetterEditorState, [focus: Focus]> = withPatches((draft, focus) => {
  const block = draft.redigertBrev.blocks[focus.blockIndex];

  if (isParagraph(block)) {
    const content = block.content[focus.contentIndex];
    const offset = focus.cursorPosition;

    if (isTextContent(content) && !("itemIndex" in focus) && offset !== undefined) {
      switch (content.type) {
        case LITERAL: {
          const atStartOfContentWithLength = offset === 0 && text(content).length > 0;
          const atEndOfContentOrContentZeroLength = offset >= text(content).length;
          if (atStartOfContentWithLength) {
            if (block.content[focus.contentIndex - 1]?.type === NEW_LINE) {
              break;
            }

            const isAtStartOfBlock = focus.contentIndex === 0;
            const toAdd = isAtStartOfBlock ? [newLiteral(), createNewLine()] : [createNewLine()];
            const previousIsVariable = block.content[focus.contentIndex - 1]?.type === VARIABLE;
            if (previousIsVariable) {
              toAdd.unshift(newLiteral());
            }
            addElements(toAdd, focus.contentIndex, block.content, block.deletedContent);
            draft.focus = {
              contentIndex: focus.contentIndex + toAdd.length,
              cursorPosition: 0,
              blockIndex: focus.blockIndex,
            };
          } else if (atEndOfContentOrContentZeroLength) {
            if (
              block.content[focus.contentIndex + 1]?.type === NEW_LINE ||
              block.content[focus.contentIndex - 1]?.type === NEW_LINE
            ) {
              break;
            }
            const isAtEndOfBlock = focus.contentIndex + 1 === block.content.length;
            const toAdd = isAtEndOfBlock ? [createNewLine(), newLiteral()] : [createNewLine()];
            const nextIsVariable = block.content[focus.contentIndex + 1]?.type === VARIABLE;
            if (nextIsVariable) {
              toAdd.push(newLiteral());
            }
            addElements(toAdd, focus.contentIndex + 1, block.content, block.deletedContent);
            draft.focus = {
              contentIndex: focus.contentIndex + 2,
              cursorPosition: 0,
              blockIndex: focus.blockIndex,
            };
          } else {
            const newLiteral = splitLiteralAtOffset(content, offset);
            addElements([createNewLine(), newLiteral], focus.contentIndex + 1, block.content, block.deletedContent);
            draft.focus = {
              contentIndex: focus.contentIndex + 2,
              cursorPosition: 0,
              blockIndex: focus.blockIndex,
            };
          }
          draft.saveStatus = "DIRTY";
          break;
        }
        case VARIABLE: {
          break;
        }
        case NEW_LINE: {
          break;
        }
      }
    }
  }
});
