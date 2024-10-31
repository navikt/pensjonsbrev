import type { Draft } from "immer";

import type { LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { isItemContentIndex } from "~/Brevredigering/LetterEditor/model/utils";
import type {
  Block,
  Content,
  Item,
  ItemList,
  LiteralValue,
  ParagraphBlock,
  TextContent,
  VariableValue,
} from "~/types/brevbakerTypes";
import type { FontType } from "~/types/brevbakerTypes";
import type { Nullable } from "~/types/Nullable";

const isParagraphBlock = (b: Block): b is ParagraphBlock => b.type === "PARAGRAPH";

/**
 * Merk at vi kan bare endre på fonttypen til literals
 * @returns fonttypen som alle literals i blocken har, ellers null hvis vi ikke kan finne ut av den
 */
export const getLiteralEditedFontTypeForBlock = (editorState: LetterEditorState): Nullable<FontType> => {
  const block = editorState.redigertBrev.blocks[editorState.focus.blockIndex];

  if (!isParagraphBlock(block)) {
    return null;
  }

  const activeFontTypesInBlock = block.content.flatMap((content) =>
    handleSwitchContent({
      content: content,
      onLiteral: (literal) => {
        return [literal.editedFontType];
      },
      onVariable: () => {
        return [null];
      },
      onItemList: (itemList) => {
        if (!isItemContentIndex(editorState.focus)) {
          return [null];
        }
        const singleItemInItemList = itemList.items[editorState.focus.itemIndex];
        return getLiteralEditedFontTypeFromItemListContent(singleItemInItemList);
      },
    }),
  );

  return getSingleFontTypeOrNull(activeFontTypesInBlock);
};

/**
 *
 * @returns fonttypen hvis listen kun inneholder 1 fonttype, ellers null
 */
const getSingleFontTypeOrNull = (fontTypes: Nullable<FontType>[]): Nullable<FontType> => {
  const uniqueFontTypes = [...new Set(fontTypes.filter((fontType) => !!fontType))];
  if (uniqueFontTypes.length === 1) {
    return uniqueFontTypes[0];
  }
  return null;
};

const getLiteralEditedFontTypeFromItemListContent = (c: Draft<Item>) => {
  return c.content.flatMap((content) =>
    handleSwitchTextContent({
      content: content,
      onLiteral: (literal) => {
        return literal.editedFontType;
      },
      onVariable: () => {
        return null;
      },
    }),
  );
};

export const handleSwitchContent = <T>(args: {
  content: Content;
  onLiteral: (literal: Draft<LiteralValue>) => T;
  onVariable: (variable: Draft<VariableValue>) => T;
  onItemList: (itemList: Draft<ItemList>) => T;
}) => {
  switch (args.content.type) {
    case "LITERAL": {
      return args.onLiteral(args.content);
    }
    case "VARIABLE": {
      return args.onVariable(args.content);
    }
    case "ITEM_LIST": {
      return args.onItemList(args.content);
    }
  }
};

export const handleSwitchTextContent = <T>(args: {
  content: TextContent;
  onLiteral: (literal: Draft<LiteralValue>) => T;
  onVariable: (variable: Draft<VariableValue>) => T;
}) => {
  switch (args.content.type) {
    case "LITERAL": {
      return args.onLiteral(args.content);
    }
    case "VARIABLE": {
      return args.onVariable(args.content);
    }
  }
};
