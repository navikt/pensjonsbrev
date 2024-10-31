import { produce } from "immer";

import type { FontType } from "~/types/brevbakerTypes";
import { handleSwitchContent, handleSwitchTextContent } from "~/utils/brevbakerUtils";

import type { Action } from "../lib/actions";
import type { LetterEditorState } from "../model/state";
import { isItemContentIndex } from "../model/utils";
import type { LiteralIndex } from "./model";

export const switchFontType: Action<LetterEditorState, [literalIndex: LiteralIndex, fontType: FontType]> = produce(
  (draft, literalIndex, fontType) => {
    const block = draft.redigertBrev.blocks[literalIndex.blockIndex];
    draft.isDirty = true;

    for (const blockContent of block.content) {
      handleSwitchContent({
        content: blockContent,
        onLiteral: (literal) => {
          literal.editedFontType = fontType;
        },
        onVariable: () => {
          //Variable kan ikke endres på enda
        },
        onItemList: (itemList) => {
          if (!isItemContentIndex(literalIndex)) {
            return;
          }
          const singleItem = itemList.items[literalIndex.itemIndex];

          for (const itemContent of singleItem.content) {
            handleSwitchTextContent({
              content: itemContent,
              onLiteral: (literal) => {
                literal.editedFontType = fontType;
              },
              onVariable: () => {
                //Variable kan ikke endres på enda
              },
            });
          }
        },
      });
    }
  },
);
