import type { Draft } from "immer";
import { produce } from "immer";
import { isEqual } from "lodash";

import type { Content, ItemList, ParagraphBlock, TextContent } from "~/types/brevbakerTypes";

import type { Action } from "../lib/actions";
import type { LetterEditorState } from "../model/state";
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

/**
 * Slår sammen tilstøtende ITEM_LISTs
 */
const mergeItemLists = (content: Content[]): Content[] => {
  const result: Content[] = [];

  for (let i = 0; i < content.length; i++) {
    const current = content[i];

    if (current.type === "ITEM_LIST") {
      const next = content[i + 1];

      if (next?.type === "ITEM_LIST") {
        const mergedItems = [...current.items, ...next.items];
        const mergedItemList = newItemList(...mergedItems);
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
      toggleBulletListOff(draft, literalIndex as ItemContentIndex);
    }
  },
);

/**
 * Når vi lager et punkt, så må vi ta høyde for at det kan være en punktliste før, etter, eller ingen punktliste.
 *  Hvis det finnes en punktlisten før/etter/begge, må vi merge blockene til kun 1 block.
 *
 * Fordi vi gjør en såpass stor endring i dokument strukturen, Så må vi oppdatere fokuset til editorstaten til å være på rett plass
 */
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
    JSON.parse(JSON.stringify(blockAfterThisBlock));
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
    draft.focus = {
      blockIndex: newBlockContentIndex,
      contentIndex: theIndexOfMySentenceInItemList,
      itemIndex: itemIndex,
      itemContentIndex: newItemContentIndex,
      cursorPosition: draft.focus.cursorPosition,
    };
  } else if (hasItemListBlockBefore) {
    const theItemListBefore = blockBeforeThisblock!.content.at(-1) as ItemList;

    const mergedItems = newItemList(...theItemListBefore.items, ...theItemListThatHasMySentence.items);

    const newBlocks = [
      ...draft.redigertBrev.blocks.slice(0, literalIndex.blockIndex - 1),
      newParagraph(...blockBeforeThisblock.content.slice(0, -1)),
      newParagraph(mergedItems),
      ...draft.redigertBrev.blocks.slice(literalIndex.blockIndex + 1),
    ].filter((block) => block.content.length > 0);

    const newBlockContentIndex = newBlocks.findIndex((block) => {
      const blockItemLists = block.content.filter((content) => content.type === "ITEM_LIST") as ItemList[];
      return blockItemLists.some((block) => isEqual(block, mergedItems));
    });

    const itemIndex = mergedItems.items.findIndex((item) =>
      isEqual(
        item.content,
        sentence.map((r) => r.element),
      ),
    );

    draft.redigertBrev.blocks = newBlocks;
    draft.focus = {
      blockIndex: newBlockContentIndex,
      contentIndex: theIndexOfMySentenceInItemList,
      itemIndex: itemIndex,
      itemContentIndex: newItemContentIndex,
      cursorPosition: draft.focus.cursorPosition,
    };
  } else if (hasItemListBlockAfter) {
    const theItemListAfter = blockAfterThisBlock!.content.at(0) as ItemList;

    const mergedItems = newItemList(...theItemListThatHasMySentence.items, ...theItemListAfter.items);

    const newBlockContent = [
      ...(literalIndex.blockIndex === 0 ? [] : draft.redigertBrev.blocks.slice(0, literalIndex.blockIndex)),
      newParagraph(mergedItems),
      newParagraph(...(blockAfterThisBlock.content.slice(0, -1) || [])),
      ...draft.redigertBrev.blocks.slice(literalIndex.blockIndex + 2),
    ].filter((block) => block.content.length > 0);

    const newBlockContentIndex = newBlockContent.findIndex((block) => {
      const blockItemLists = block.content.filter((content) => content.type === "ITEM_LIST") as ItemList[];
      return blockItemLists.some((block) => isEqual(block, mergedItems));
    });

    const itemIndex = mergedItems.items.findIndex((i) =>
      isEqual(
        i.content,
        sentence.map((r) => r.element),
      ),
    );

    draft.redigertBrev.blocks = newBlockContent;
    draft.focus = {
      blockIndex: newBlockContentIndex,
      contentIndex: newContentIndex,
      itemIndex: itemIndex,
      itemContentIndex: newItemContentIndex,
      cursorPosition: draft.focus.cursorPosition,
    };
  } else {
    draft.redigertBrev.blocks[literalIndex.blockIndex].content = mergedItemLists;
    draft.focus = {
      blockIndex: literalIndex.blockIndex,
      contentIndex: newContentIndex,
      itemIndex: theIndexOfMySentenceInItemList,
      itemContentIndex: newItemContentIndex,
      cursorPosition: draft.focus.cursorPosition,
    };
  }
};

/**
 * Når vi fjerner et punkt fra en punktliste, så må man ta høyde for at man kan potensielt ha content før og etter punktlisten.
 * Vi må også ta høyde for at vi kan være på første, siste eller et sted i mellom punktene i listen.
 *  Det er fordi vi må styre hvor den nye blocken skal havne, samt resten av innholdet i punktlisten.
 *
 * Fordi vi gjør en såpass stor endring i dokument strukturen, Så må vi oppdatere fokuset til editorstaten til å være på rett plass
 */
const toggleBulletListOff = (draft: Draft<LetterEditorState>, itemContentIndex: ItemContentIndex) => {
  const dokumentBlocks = draft.redigertBrev.blocks;
  const thisBlock = dokumentBlocks[itemContentIndex.blockIndex];
  const thisItemList = thisBlock.content[itemContentIndex.contentIndex] as ItemList;
  const thisItem = thisItemList.items[itemContentIndex.itemIndex];

  const existingParagraphContent = thisItem.content;

  const blockContentBeforeItemList = thisBlock.content.slice(0, itemContentIndex.contentIndex);
  const hasBlockContentBeforeItemList = blockContentBeforeItemList.length > 0;

  const blockContentAfterItemList = thisBlock.content.slice(itemContentIndex.contentIndex + 1);
  const hasBlockContentAfterItemList = blockContentAfterItemList.length > 0;

  const listItemsBeforeNewParagraph = thisItemList.items.slice(0, itemContentIndex.itemIndex);
  const listItemsAfterNewParagraph = thisItemList.items.slice(itemContentIndex.itemIndex + 1);

  if (itemContentIndex.itemIndex === 0) {
    const newParagraphBlock = newParagraph(...existingParagraphContent);
    const hasListItemsAfter = listItemsAfterNewParagraph.length > 0;
    const blockContentAfter = newParagraph(newItemList(...listItemsAfterNewParagraph));

    const newBlockContents = [
      ...(hasBlockContentBeforeItemList ? [newParagraph(...blockContentBeforeItemList)] : []),
      newParagraphBlock,
      ...(hasListItemsAfter ? [blockContentAfter] : []),
    ];

    const newBlockContentList = [
      ...dokumentBlocks.slice(0, itemContentIndex.blockIndex),
      ...newBlockContents,
      ...dokumentBlocks.slice(itemContentIndex.blockIndex + 1),
    ].filter((block) => block.content.length > 0);

    //TODO - bug - hvis det eksisterer blocker som er lik den nye (for eksempel et tom literal, og en tomt punkt, vil vi treffe literalen)
    const newParagraphBlockIndex = newBlockContentList.findIndex((block) => isEqual(block, newParagraphBlock));

    draft.redigertBrev.blocks = newBlockContentList;
    draft.focus = {
      blockIndex: newParagraphBlockIndex,
      contentIndex: itemContentIndex.itemContentIndex,
      cursorPosition: draft.focus.cursorPosition,
    };
  } else if (itemContentIndex.itemIndex > 0 && itemContentIndex.itemIndex < thisItemList.items.length - 1) {
    const newBeforeBlock: ParagraphBlock = hasBlockContentBeforeItemList
      ? newParagraph(...blockContentBeforeItemList, newItemList(...listItemsBeforeNewParagraph))
      : newParagraph(newItemList(...listItemsBeforeNewParagraph));

    const newParagraphBlock = newParagraph(...existingParagraphContent);
    const blockContentAfter = newParagraph(newItemList(...listItemsAfterNewParagraph));

    const newBlockContents = [newBeforeBlock, newParagraphBlock, blockContentAfter];

    const newBlockContentList = [
      ...dokumentBlocks.slice(0, itemContentIndex.blockIndex),
      ...newBlockContents,
      ...dokumentBlocks.slice(itemContentIndex.blockIndex + 1),
    ].filter((block) => block.content.length > 0);

    //TODO - bug - hvis det eksisterer blocker som er lik den nye (for eksempel et tom literal, og en tomt punkt, vil vi treffe literalen)
    const newParagraphBlockIndex = newBlockContentList.findIndex((block) => isEqual(block, newParagraphBlock));

    draft.redigertBrev.blocks = newBlockContentList;
    draft.focus = {
      blockIndex: newParagraphBlockIndex,
      contentIndex: itemContentIndex.itemContentIndex,
      cursorPosition: draft.focus.cursorPosition,
    };
  } else if (itemContentIndex.itemIndex === thisItemList.items.length - 1) {
    const newBlockContentBefore = newParagraph(
      ...blockContentBeforeItemList,
      newItemList(...listItemsBeforeNewParagraph),
    );
    const newParagraphBlock = newParagraph(...existingParagraphContent);

    const newBlockContents = hasBlockContentAfterItemList
      ? [newBlockContentBefore, newParagraphBlock, newParagraph(...blockContentAfterItemList)]
      : [newBlockContentBefore, newParagraphBlock];

    const newBlockContentList = [
      ...dokumentBlocks.slice(0, itemContentIndex.blockIndex),
      ...newBlockContents,
      ...dokumentBlocks.slice(itemContentIndex.blockIndex + 1),
    ].filter((block) => block.content.length > 0);

    //TODO - bug - hvis det eksisterer blocker som er lik den nye (for eksempel et tom literal, og en tomt punkt, vil vi treffe literalen)
    const newParagraphBlockIndex = newBlockContentList.findIndex((block) => isEqual(block, newParagraphBlock));

    draft.redigertBrev.blocks = newBlockContentList;
    draft.focus = {
      blockIndex: newParagraphBlockIndex,
      contentIndex: itemContentIndex.itemContentIndex,
      cursorPosition: draft.focus.cursorPosition,
    };
  }
};
