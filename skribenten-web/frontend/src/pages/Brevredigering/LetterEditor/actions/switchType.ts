import { produce } from "immer";

import { PARAGRAPH, TITLE1, TITLE2 } from "~/types/brevbakerTypes";

import type { Action } from "../lib/actions";
import type { LetterEditorState } from "../model/state";
import { isTextContent } from "../model/utils";

export const switchType: Action<
  LetterEditorState,
  [blockId: number, toType: typeof PARAGRAPH | typeof TITLE1 | typeof TITLE2]
> = produce((draft, blockId, toType) => {
  const block = draft.editedLetter.letter.blocks[blockId];
  switch (toType) {
    case PARAGRAPH: {
      block.type = toType;
      break;
    }

    case TITLE1:
    case TITLE2: {
      if (block.content.every(isTextContent)) {
        block.type = toType;
      } else {
        // eslint-disable-next-line no-console
        console.warn("Cannot switch type of block to title1: contains non text content");
      }
      break;
    }
  }
});
