import type { Draft } from "immer";
import { produce } from "immer";

import type { Content, Item, ItemList, TextContent } from "~/types/brevbakerTypes";

import type { Action } from "../lib/actions";
import type { LetterEditorState } from "../model/state";
import { newItemList } from "./common";
import type { ItemContentIndex, LiteralIndex } from "./model";

function getSurroundingLiteralsAndVariables(
  content: Content[],
  index: number,
): Array<{ element: TextContent; originalIndex: number }> {
  const result: Array<{ element: TextContent; originalIndex: number }> = [];

  // Loop backwards from index until an ITEM_LIST is encountered
  for (let i = index; i >= 0; i--) {
    const current = content[i];
    if (current.type === "ITEM_LIST") break;
    result.unshift({ element: current, originalIndex: i });
  }

  // Loop forwards from index + 1 until an ITEM_LIST is encountered
  for (let i = index + 1; i < content.length; i++) {
    const current = content[i];
    if (current.type === "ITEM_LIST") break;
    result.push({ element: current, originalIndex: i });
  }

  return result;
}

function replaceElementsBetweenIncluding<T>(array: T[], A: number, B: number, C: T): T[] {
  if (A < 0 || B >= array.length || A > B) {
    throw new Error("Ugyldige indekser");
  }

  return [...array.slice(0, A), C, ...array.slice(B + 1)];
}

const mergeItemLists = (content: Content[]): Content[] => {
  const result: Content[] = [];

  for (let i = 0; i < content.length; i++) {
    const current = content[i];

    if (current.type === "ITEM_LIST") {
      const next = content[i + 1];

      if (next?.type === "ITEM_LIST") {
        // Merge the two ITEM_LISTs
        const mergedItems = [...current.items, ...next.items];
        const mergedItemList: ItemList = {
          id: null,
          type: "ITEM_LIST",
          items: mergedItems,
          deletedItems: [],
        };

        result.push(mergedItemList);
        i++; // skipper neste item_list
      } else {
        result.push(current);
      }
    } else {
      result.push(current);
    }
  }

  return result;
};

export const toggleBulletList: Action<LetterEditorState, [literalIndex: LiteralIndex]> = produce(
  (draft, literalIndex) => {
    const block = draft.redigertBrev.blocks[literalIndex.blockIndex];
    if (block.type !== "PARAGRAPH") {
      return;
    }

    //draft.isDirty = true;

    const theContentTheUserIsOn = block.content[literalIndex.contentIndex];

    if (theContentTheUserIsOn.type === "LITERAL" || theContentTheUserIsOn.type === "VARIABLE") {
      toggleBulletListOn(draft, literalIndex);
    } else if (theContentTheUserIsOn.type === "ITEM_LIST") {
      toggleBulletListOff(draft, literalIndex);
    }
  },
);

const convertItemToParagraph = (item: Item): TextContent[] => {
  return item.content;
};

const toggleBulletListOn = (draft: Draft<LetterEditorState>, literalIndex: LiteralIndex) => {
  const block = draft.redigertBrev.blocks[literalIndex.blockIndex];
  const theIdexOfTheContent = literalIndex.contentIndex;

  const sentence = getSurroundingLiteralsAndVariables(block.content, theIdexOfTheContent);

  const replacedWithItemList = replaceElementsBetweenIncluding(
    block.content,
    sentence[0].originalIndex,
    sentence.at(-1)!.originalIndex,
    {
      type: "ITEM_LIST",
      id: null,
      deletedItems: [],
      items: [
        {
          id: null,
          content: sentence.map((r) => r.element),
        },
      ],
    },
  );

  const mergedItemLists = mergeItemLists(replacedWithItemList);

  draft.redigertBrev.blocks[literalIndex.blockIndex].content = mergedItemLists;
};

const toggleBulletListOff = (draft: Draft<LetterEditorState>, literalIndex: LiteralIndex) => {
  const block = draft.redigertBrev.blocks[literalIndex.blockIndex];
  const itemContentIndex = literalIndex as ItemContentIndex;
  const itemList = block.content[itemContentIndex.contentIndex] as ItemList;
  const item = itemList.items[itemContentIndex.itemIndex];

  const newParagraph = convertItemToParagraph(item);

  const contentBeforeNewParagraph = itemList.items.slice(0, itemContentIndex.itemIndex);
  const contentAfterNewParagraph = itemList.items.slice(itemContentIndex.itemIndex + 1);

  if (itemContentIndex.itemIndex === 0) {
    const newBlockContent: Content[] = [...newParagraph, newItemList(...contentAfterNewParagraph)];
    console.log("newBlockContent", JSON.parse(JSON.stringify(newBlockContent)));
  }
  if (itemContentIndex.itemIndex > 0 && itemContentIndex.itemIndex < itemList.items.length - 1) {
    const newBlockContent: Content[] = [
      newItemList(...contentBeforeNewParagraph),
      ...newParagraph,
      newItemList(...contentAfterNewParagraph),
    ];
    console.log("newBlockContent", JSON.parse(JSON.stringify(newBlockContent)));
  }

  if (itemContentIndex.itemIndex === itemList.items.length - 1) {
    const newBlockContent: Content[] = [newItemList(...contentBeforeNewParagraph), ...newParagraph];
    console.log("newBlockContent", JSON.parse(JSON.stringify(newBlockContent)));
  }
};
