import { produce } from "immer";

import type { LiteralIndex } from "~/pages/Brevredigering/LetterEditor/actions/model";
import type { Action } from "~/pages/Brevredigering/LetterEditor/lib/actions";
import type { LetterEditorState } from "~/pages/Brevredigering/LetterEditor/model/state";
import { LITERAL } from "~/types/brevbakerTypes";

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
        break;
      }
      case "ArrowRight": {
        const isAtEndOfContent = content.text.length === cursorPosition;
        if (!isAtEndOfContent) break;

        // TODO: POC: while-true... or recursion?
        let searchingInBlockIndex = literalIndex.blockIndex;
        let searchAfterContentIndex = literalIndex.contentIndex + 1;
        let nextLiteralIndex = -1;

        while (true) {
          nextLiteralIndex = draft.editedLetter.letter.blocks[searchingInBlockIndex].content.findIndex(
            (content, index) => index >= searchAfterContentIndex && content.type === "LITERAL",
          );
          if (nextLiteralIndex !== -1) break;
          if (nextLiteralIndex === -1) {
            searchingInBlockIndex = searchingInBlockIndex + 1;
            if (searchingInBlockIndex >= draft.editedLetter.letter.blocks.length) {
              break;
            }
            searchAfterContentIndex = 0;
          }
        }

        // We ran out of blocks, that means we were at the end of the editable document
        if (nextLiteralIndex === -1) break;

        draft.focus = {
          blockIndex: searchingInBlockIndex,
          contentIndex: nextLiteralIndex,
          cursorPosition: 0,
        };
        event.preventDefault();

        break;
      }
      case "ArrowLeft": {
        const isAtStartOfContent = cursorPosition === 0;
        if (!isAtStartOfContent) break;

        let searchingInBlockIndex = literalIndex.blockIndex;
        let searchBeforeContentIndex = literalIndex.contentIndex;
        let previousLiteralIndex = -1;

        while (true) {
          previousLiteralIndex = draft.editedLetter.letter.blocks[searchingInBlockIndex].content.findLastIndex(
            (content, index) => index < searchBeforeContentIndex && content.type === "LITERAL",
          );

          if (previousLiteralIndex !== -1) break;
          if (previousLiteralIndex === -1) {
            searchingInBlockIndex = searchingInBlockIndex - 1;

            if (searchingInBlockIndex < 0) {
              break;
            }
            searchBeforeContentIndex = draft.editedLetter.letter.blocks[searchingInBlockIndex].content.length;
          }
        }

        // We ran out of blocks, that means we were at the beginning of the editable document
        if (previousLiteralIndex === -1) break;

        draft.focus = {
          blockIndex: searchingInBlockIndex,
          contentIndex: previousLiteralIndex,
          cursorPosition:
            draft.editedLetter.letter.blocks[searchingInBlockIndex].content[previousLiteralIndex].text.length,
        };
        event.preventDefault();
        break;
      }
    }
  }
});
