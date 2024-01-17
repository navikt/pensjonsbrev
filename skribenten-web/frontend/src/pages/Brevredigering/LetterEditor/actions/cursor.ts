import { produce } from "immer";

import type { LiteralIndex } from "~/pages/Brevredigering/LetterEditor/actions/model";
import type { Action } from "~/pages/Brevredigering/LetterEditor/lib/actions";
import type { LetterEditorState } from "~/pages/Brevredigering/LetterEditor/model/state";
import { LITERAL } from "~/types/brevbakerTypes";

// export enum ArrowKey {
//   UP = "UP",
//   DOWN = "DOWN",
//   LEFT = "LEFT",
//   RIGHT = "RIGHT",
// }

type ArrowKey = "ArrowUp" | "ArrowDown" | "ArrowRight" | "ArrowLeft";

export const cursor: Action<
  LetterEditorState,
  [literalIndex: LiteralIndex, event: KeyboardEvent, cursorPosition: number]
> = produce((draft, literalIndex, event, cursorPosition) => {
  const block = draft.editedLetter.letter.blocks[literalIndex.blockIndex];
  const content = block.content[literalIndex.contentIndex];

  if (content.type === LITERAL) {
    switch (event.key) {
      case "ArrowUp":
      case "ArrowDown": {
        console.log("UP and DOWN not implemented");
        return draft;
      }
      case "ArrowRight": {
        const isAtLastContentInBlock = block.content.length - 1 === literalIndex.contentIndex;
        const isAtEndOfContent = content.text.length === cursorPosition;

        if (!isAtEndOfContent) break;

        if (isAtLastContentInBlock) {
          const isAtLastBlock = draft.editedLetter.letter.blocks.length - 1 === literalIndex.blockIndex;
          if (isAtLastBlock) {
            break; // We are at the end of the document, there is nowhere to go.
          }
          const nextBlock = draft.editedLetter.letter.blocks[literalIndex.blockIndex + 1];
          const firstLiteralContent = nextBlock.content.findIndex((content) => content.type === "LITERAL");

          draft.focus = {
            blockIndex: literalIndex.blockIndex + 1,
            contentIndex: firstLiteralContent,
            cursorPosition: 0,
          };
          event.preventDefault();
        } else {
          // TODO: POC, refactor this
          let searchingInBlockIndex = literalIndex.blockIndex;
          let searchAfterContentIndex = literalIndex.contentIndex + 1;
          let nextLiteralIndex = -1;

          while (true) {
            nextLiteralIndex = draft.editedLetter.letter.blocks[searchingInBlockIndex].content.findIndex(
              (content, index) => index >= searchAfterContentIndex && content.type === "LITERAL",
            );
            if (nextLiteralIndex === -1) {
              searchAfterContentIndex = 0;
              searchingInBlockIndex = searchingInBlockIndex + 1;
            } else {
              break;
            }
            if (searchingInBlockIndex > draft.editedLetter.letter.blocks.length) {
              break;
            }
          }

          draft.focus = {
            blockIndex: searchingInBlockIndex,
            contentIndex: nextLiteralIndex,
            cursorPosition: 0,
          };
          event.preventDefault();
        }
        break;
      }
      case "ArrowLeft": {
        {
          const isAtStartOfContent = cursorPosition === 0;
          if (!isAtStartOfContent) break;

          // TODO: POC, refactor this
          let searchingInBlockIndex = literalIndex.blockIndex;
          let searchBeforeContentIndex = literalIndex.contentIndex;
          let previousLiteralIndex = -1;

          while (true) {
            previousLiteralIndex = draft.editedLetter.letter.blocks[searchingInBlockIndex].content.findLastIndex(
              (content, index) => index < searchBeforeContentIndex && content.type === "LITERAL",
            );
            if (previousLiteralIndex === -1) {
              searchingInBlockIndex = searchingInBlockIndex - 1;

              if (searchingInBlockIndex < 0) {
                break;
              }
              searchBeforeContentIndex = draft.editedLetter.letter.blocks[searchingInBlockIndex].content.length;
            } else {
              break;
            }
          }

          // We ran out of blocks, that means we were at the end of the editable document
          if (previousLiteralIndex === -1 || searchingInBlockIndex === -1) break;

          draft.focus = {
            blockIndex: searchingInBlockIndex,
            contentIndex: previousLiteralIndex,
            cursorPosition:
              draft.editedLetter.letter.blocks[searchingInBlockIndex].content[previousLiteralIndex].text.length,
          };
          event.preventDefault();
        }
        break;
      }
    }
  }
});
