import { produce } from "immer";

import type { FontType } from "~/types/brevbakerTypes";

import type { Action } from "../lib/actions";
import type { LetterEditorState } from "../model/state";
import { isTextContent } from "../model/utils";
import type { BlockContentIndex } from "./model";

export const switchFontType: Action<LetterEditorState, [literalIndex: BlockContentIndex, fontType: FontType]> = produce(
  (draft, literalIndex, fontType) => {
    const block = draft.redigertBrev.blocks[literalIndex.blockIndex];

    if (!isTextContent(block.content[literalIndex.contentIndex])) {
      return;
    }

    draft.isDirty = true;

    for (const content of block.content) {
      switch (content.type) {
        case "LITERAL": {
          content.editedFontType = fontType;
          break;
        }
        case "VARIABLE": {
          //Variable kan ikke endres på enda
          break;
        }
        case "ITEM_LIST": {
          //TODO - Implementer fonttype for itemlist
          break;
        }
      }
    }
  },
);
