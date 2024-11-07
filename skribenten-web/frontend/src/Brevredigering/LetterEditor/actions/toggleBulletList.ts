import type { Draft } from "immer";
import { current, produce } from "immer";
import { isEqual } from "lodash";

import type { Content, ItemList, ParagraphBlock, TextContent } from "~/types/brevbakerTypes";

import type { Action } from "../lib/actions";
import type { Focus, LetterEditorState } from "../model/state";
import { newItemList, newParagraph } from "./common";
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

const toggleBulletListOn = (draft: Draft<LetterEditorState>, literalIndex: LiteralIndex) => {
  const block = draft.redigertBrev.blocks[literalIndex.blockIndex];
  const theIdexOfTheContent = literalIndex.contentIndex;

  const sentence = getSurroundingLiteralsAndVariables(block.content, theIdexOfTheContent);

  const replacedWithItemList = replaceElementsBetweenIncluding(
    block.content,
    sentence[0].originalIndex,
    sentence.at(-1)!.originalIndex,
    newItemList({ id: null, content: sentence.map((r) => r.element) }),
  );

  const mergedItemLists = mergeItemLists(replacedWithItemList);

  /**
   * Fordi vi gjør en såpass stor endring i dokument strukturen, Så må vi oppdatere fokuset til editorstaten til å være på rett plass-
   */

  //asserter typen til ItemList fordi at hvis den er udnefined, så er det en programmeringsfeil. Elementet våres skal finnes inni
  const theItemListThatHasMySentence = mergedItemLists.find(
    (content) =>
      content.type === "ITEM_LIST" &&
      content.items.some((item) =>
        isEqual(
          item.content,
          sentence.map((r) => r.element),
        ),
      ),
  ) as ItemList;

  const theIndexOfMySentenceInItemList = theItemListThatHasMySentence?.items.findIndex((item) =>
    isEqual(
      item.content,
      sentence.map((r) => r.element),
    ),
  );

  const newContentIndex = mergedItemLists.indexOf(theItemListThatHasMySentence);
  const newItemContentIndex = sentence.findIndex((r) => r.originalIndex === theIdexOfTheContent);
  //-------------------

  const blockBeforeThisblock = draft.redigertBrev.blocks[literalIndex.blockIndex - 1];
  const hasItemListBlockBefore =
    blockBeforeThisblock?.type === "PARAGRAPH" && blockBeforeThisblock.content.at(-1)?.type === "ITEM_LIST";

  const blockAfterThisBlock = draft.redigertBrev.blocks[literalIndex.blockIndex + 1];
  const hasItemListBlockAfter =
    blockAfterThisBlock?.type === "PARAGRAPH" && blockAfterThisBlock.content.at(0)?.type === "ITEM_LIST";

  if (hasItemListBlockBefore && hasItemListBlockAfter) {
    console.log("hasItemListBlockBefore && hasItemListBlockAfter");
    const theItemListBefore = blockBeforeThisblock!.content.at(-1) as ItemList;
    const theItemListAfter = blockAfterThisBlock!.content.at(0) as ItemList;

    const mergedItems = newItemList(
      ...theItemListBefore.items,
      ...theItemListThatHasMySentence.items,
      ...theItemListAfter.items,
    );

    const newBlockContent = [
      ...draft.redigertBrev.blocks.slice(0, literalIndex.blockIndex - 1),
      newParagraph(...blockBeforeThisblock.content.slice(0, -1)),
      newParagraph(mergedItems),
      newParagraph(...blockAfterThisBlock.content.slice(1)),
      ...draft.redigertBrev.blocks.slice(literalIndex.blockIndex + 2),
    ].filter((block) => block.content.length > 0);

    const newBlockContentIndex = newBlockContent.findIndex((block) => {
      const blockItemLists = block.content.filter((content) => content.type === "ITEM_LIST") as ItemList[];
      return blockItemLists.some((block) => isEqual(block, mergedItems));
    });

    const itemIndex = mergedItems.items.findIndex((item) =>
      isEqual(
        item.content,
        sentence.map((r) => r.element),
      ),
    );

    draft.redigertBrev.blocks = newBlockContent;
    const newFocus = {
      blockIndex: newBlockContentIndex,
      contentIndex: theIndexOfMySentenceInItemList,
      itemIndex: itemIndex,
      itemContentIndex: newItemContentIndex,
      cursorPosition: draft.focus.cursorPosition,
    };

    draft.focus = newFocus;
  } else if (hasItemListBlockBefore) {
    console.log("hasItemListBlockBefore", JSON.parse(JSON.stringify(blockBeforeThisblock)));
    const theItemListBefore = blockBeforeThisblock!.content.at(-1) as ItemList;

    const mergedItems = newItemList(...theItemListBefore.items, ...theItemListThatHasMySentence.items);

    const newBlockContent = [
      ...draft.redigertBrev.blocks.slice(0, literalIndex.blockIndex - 1),
      newParagraph(...blockBeforeThisblock.content.slice(0, -1)),
      newParagraph(mergedItems),
      ...draft.redigertBrev.blocks.slice(literalIndex.blockIndex + 2),
    ].filter((block) => block.content.length > 0);

    const newBlockContentIndex = newBlockContent.findIndex((block) => {
      const blockItemLists = block.content.filter((content) => content.type === "ITEM_LIST") as ItemList[];
      return blockItemLists.some((block) => isEqual(block, mergedItems));
    });

    const itemIndex = mergedItems.items.findIndex((item) =>
      isEqual(
        item.content,
        sentence.map((r) => r.element),
      ),
    );

    draft.redigertBrev.blocks = newBlockContent;
    const newFocus = {
      blockIndex: newBlockContentIndex,
      contentIndex: theIndexOfMySentenceInItemList,
      itemIndex: itemIndex,
      itemContentIndex: newItemContentIndex,
      cursorPosition: draft.focus.cursorPosition,
    };

    draft.focus = newFocus;
  } else if (hasItemListBlockAfter) {
    console.log("hasItemListBlockAfter", JSON.parse(JSON.stringify(blockAfterThisBlock)));
    const theItemListAfter = blockAfterThisBlock!.content.at(0) as ItemList;

    const mergedItems = newItemList(...theItemListThatHasMySentence.items, ...theItemListAfter.items);

    const newBlockContent = [
      ...(literalIndex.blockIndex === 0 ? [] : draft.redigertBrev.blocks.slice(0, literalIndex.blockIndex)),
      newParagraph(...blockBeforeThisblock.content.slice(0, -1)),
      newParagraph(mergedItems),
      newParagraph(...(blockAfterThisBlock.content.slice(0, -1) || [])),
      ...draft.redigertBrev.blocks.slice(literalIndex.blockIndex + 2),
    ].filter((block) => block.content.length > 0);

    const newBlockContentIndex = newBlockContent.findIndex((block) => {
      const blockItemLists = block.content.filter((content) => content.type === "ITEM_LIST") as ItemList[];
      return blockItemLists.some((block) => isEqual(block, mergedItems));
    });

    const itemIndex = mergedItems.items.findIndex((item) =>
      isEqual(
        item.content,
        sentence.map((r) => r.element),
      ),
    );
    console.log("resulting block content:", JSON.parse(JSON.stringify(newBlockContent)));
    draft.redigertBrev.blocks = newBlockContent;
    const newFocus = {
      blockIndex: newBlockContentIndex,
      contentIndex: theIndexOfMySentenceInItemList,
      itemIndex: itemIndex,
      itemContentIndex: newItemContentIndex,
      cursorPosition: draft.focus.cursorPosition,
    };

    draft.focus = newFocus;
  } else {
    console.log("no item list content around");
    draft.redigertBrev.blocks[literalIndex.blockIndex].content = mergedItemLists;
    console.log("blocks after new bullet point:", current(draft.redigertBrev.blocks));
    draft.focus = {
      blockIndex: literalIndex.blockIndex,
      contentIndex: newContentIndex,
      itemIndex: theIndexOfMySentenceInItemList,
      itemContentIndex: newItemContentIndex,
      cursorPosition: draft.focus.cursorPosition,
    };
  }
};

const toggleBulletListOff = (draft: Draft<LetterEditorState>, literalIndex: LiteralIndex) => {
  const blocks = draft.redigertBrev.blocks;
  const block = blocks[literalIndex.blockIndex];
  const itemContentIndex = literalIndex as ItemContentIndex;
  const itemList = block.content[itemContentIndex.contentIndex] as ItemList;
  const item = itemList.items[itemContentIndex.itemIndex];

  const newParagraphContent = item.content;

  const blockContentBeforeItemList = block.content.slice(0, itemContentIndex.contentIndex);
  const blockContentAfterItemList = block.content.slice(itemContentIndex.contentIndex + 1);

  const listItemsBeforeNewParagraph = itemList.items.slice(0, itemContentIndex.itemIndex);
  const listItemsAfterNewParagraph = itemList.items.slice(itemContentIndex.itemIndex + 1);

  /**
   * Vi har muligens content før itemListen, men vi er på det første item i listen. Vi har muligens content etter itemListen.
   */
  if (itemContentIndex.itemIndex === 0) {
    console.log("vi fjerner punkt fra første item i listen");
    const newBlockContentBefore = newParagraph(...blockContentBeforeItemList);
    const newParagraphBlock = newParagraph(...newParagraphContent);
    const blockContentAfter = newParagraph(newItemList(...listItemsAfterNewParagraph));

    const newBlockContentList = [
      ...blocks.slice(0, literalIndex.blockIndex - 1),
      newBlockContentBefore,
      newParagraphBlock,
      blockContentAfter,
      ...blocks.slice(literalIndex.blockIndex + 1),
    ].filter((block) => block.content.length > 0);
    const newParagraphBlockIndex = newBlockContentList.findIndex((block) => isEqual(block, newParagraphBlock));

    console.log("the new blockContent:", JSON.parse(JSON.stringify(newBlockContentList)));
    // draft.redigertBrev.blocks = newBlockContentList;
    // draft.focus = {
    //   blockIndex: newParagraphBlockIndex,
    //   contentIndex: newParagraphBlock.content.length - 1,
    //   cursorPosition: draft.focus.cursorPosition,
    // };

    /**
     * Vi har muligens content før itemListen, men vi er mellom to item i listen. Vi har muligens content etter itemListen.
     */
  } else if (itemContentIndex.itemIndex > 0 && itemContentIndex.itemIndex < itemList.items.length - 1) {
    console.log("vi fjerner punkt fra et item i midten av listen");
    const hasContentBefore = blockContentBeforeItemList.length > 0;

    const newBeforeBlock: ParagraphBlock = hasContentBefore
      ? {
          ...block,
          type: "PARAGRAPH",
          content: [...blockContentBeforeItemList, newItemList(...listItemsBeforeNewParagraph)],
        }
      : newParagraph(newItemList(...listItemsBeforeNewParagraph));
    const newParagraphBlock = newParagraph(...newParagraphContent);

    const blockContentAfter = newParagraph(newItemList(...listItemsAfterNewParagraph));

    const newBlockContentList = [
      ...blocks.slice(0, literalIndex.blockIndex),
      newBeforeBlock,
      newParagraphBlock,
      blockContentAfter,
      ...blocks.slice(literalIndex.blockIndex + 1),
    ].filter((block) => block.content.length > 0);

    const newParagraphBlockIndex = newBlockContentList.findIndex((block) => isEqual(block, newParagraphBlock));

    draft.redigertBrev.blocks = newBlockContentList;
    draft.focus = {
      blockIndex: newParagraphBlockIndex,
      contentIndex: newParagraphBlock.content.length - 1,
      cursorPosition: draft.focus.cursorPosition,
    };

    /**
     * Vi har muligens content før itemListen, men vi er på den siste item i listen. Vi har muligens content etter itemListen.
     */
  } else if (itemContentIndex.itemIndex === itemList.items.length - 1) {
    console.log("vi fjerner punkt fra siste item i listen");
    const newBlockContentBefore = newParagraph(
      ...blockContentBeforeItemList,
      newItemList(...listItemsBeforeNewParagraph),
    );
    const newParagraphBlock = newParagraph(...newParagraphContent);
    const hasContentAfter = blockContentAfterItemList.length > 0;

    const result = hasContentAfter
      ? [newBlockContentBefore, newParagraphBlock, newParagraph(...blockContentAfterItemList)]
      : [newBlockContentBefore, newParagraphBlock];

    const newBlockContentList = [
      ...blocks.slice(0, literalIndex.blockIndex),
      ...result,
      ...blocks.slice(literalIndex.blockIndex + 1),
    ].filter((block) => block.content.length > 0);

    const newParagraphBlockIndex = newBlockContentList.findIndex((block) => isEqual(block, newParagraphBlock));

    draft.redigertBrev.blocks = newBlockContentList;
    draft.focus = {
      blockIndex: newParagraphBlockIndex,
      contentIndex: newParagraphBlock.content.length - 1,
      cursorPosition: draft.focus.cursorPosition,
    };
  }
};
