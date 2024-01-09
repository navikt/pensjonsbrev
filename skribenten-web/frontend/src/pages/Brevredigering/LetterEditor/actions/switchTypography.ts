import { produce } from "immer";

import { PARAGRAPH, TITLE1, TITLE2 } from "~/types/brevbakerTypes";

import type { Action } from "../lib/actions";
import type { LetterEditorState } from "../model/state";
import { isTextContent } from "../model/utils";

export const switchTypography: Action<
  LetterEditorState,
  [blockId: number, typography: typeof PARAGRAPH | typeof TITLE1 | typeof TITLE2]
> = produce((draft, blockIndex, typography) => {
  const block = draft.editedLetter.letter.blocks[blockIndex];
  switch (typography) {
    case PARAGRAPH: {
      block.type = typography;
      break;
    }

    case TITLE1:
    case TITLE2: {
      if (block.content.every(isTextContent)) {
        block.type = typography;
      } else {
        // eslint-disable-next-line no-console
        console.warn("Cannot switch type of block to title1: contains non text content");
      }
      break;
    }
  }
});
