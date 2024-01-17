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
          const nextLiteralIndex = block.content.findIndex(
            (content, index) => index > literalIndex.contentIndex && content.type === "LITERAL",
          );

          draft.focus = {
            blockIndex: literalIndex.blockIndex,
            contentIndex: nextLiteralIndex,
            cursorPosition: 0,
          };
          event.preventDefault();
        }
        break;
      }
      case "ArrowLeft": {
        const isAtStartOfContent = cursorPosition === 0;
        const isAtFirstContentInBlock = literalIndex.contentIndex === 0;

        if (!isAtStartOfContent) break;

        if (isAtFirstContentInBlock) {
          if (literalIndex.blockIndex === 0) {
            break; // We are at the beginning of the document, there is nowhere to go.
          }
          const previousBlock = draft.editedLetter.letter.blocks[literalIndex.blockIndex - 1];
          const lastLiteralContent = previousBlock.content.findLastIndex((content) => content.type === "LITERAL");

          draft.focus = {
            blockIndex: literalIndex.blockIndex - 1,
            contentIndex: lastLiteralContent,
            cursorPosition: previousBlock.content[lastLiteralContent].text.length,
          };
          event.preventDefault();
        } else {
          const previousLiteralIndex = block.content.findLastIndex(
            (content, index) => index < literalIndex.contentIndex && content.type === "LITERAL",
          );

          draft.focus = {
            blockIndex: literalIndex.blockIndex,
            contentIndex: previousLiteralIndex,
            cursorPosition: block.content[previousLiteralIndex].text.length,
          };
          event.preventDefault();
        }
        break;
      }
    }
  }
});
