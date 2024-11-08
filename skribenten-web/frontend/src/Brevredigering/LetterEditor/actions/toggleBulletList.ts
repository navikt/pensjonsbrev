import type { Draft } from "immer";
import { produce } from "immer";
import { isEqual } from "lodash";

import type {
  Content,
  Item,
  ItemList,
  LiteralValue,
  ParagraphBlock,
  TextContent,
  Title1Block,
  Title2Block,
  VariableValue,
} from "~/types/brevbakerTypes";

import type { Action } from "../lib/actions";
import type { LetterEditorState } from "../model/state";
import { newItemList, newParagraph } from "./common";
import type { ItemContentIndex, LiteralIndex } from "./model";

export const toggleBulletList: Action<LetterEditorState, [literalIndex: LiteralIndex]> = produce(
  (draft, literalIndex) => {
    const block = draft.redigertBrev.blocks[literalIndex.blockIndex];
    if (block.type !== "PARAGRAPH") {
      return;
    }

    draft.isDirty = true;
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
  const sentenceElements = sentence.map((r) => r.element);
  const replacedWithItemList = replaceElementsBetweenIncluding(
    block.content,
    sentence[0].originalIndex,
    sentence.at(-1)!.originalIndex,
    newItemList({ id: null, content: sentenceElements }),
  );

  const mergedItemLists = mergeItemLists(replacedWithItemList);

  //asserter typen til ItemList fordi at hvis den er udnefined, så er det en programmeringsfeil. Elementet våres skal finnes inni
  const theItemListThatHasMySentence = mergedItemLists.find(
    (content) => content.type === "ITEM_LIST" && content.items.some((i) => isEqual(i.content, sentenceElements)),
  ) as ItemList;

  const theIndexOfMySentenceInItemList = theItemListThatHasMySentence?.items.findIndex((i) =>
    isEqual(i.content, sentenceElements),
  );

  const newContentIndex = mergedItemLists.indexOf(theItemListThatHasMySentence);
  const newItemContentIndex = sentence.findIndex((r) => r.originalIndex === theIdexOfTheContent);

  const blockBeforeThisblock = draft.redigertBrev.blocks[literalIndex.blockIndex - 1];
  const hasItemListBlockBefore =
    blockBeforeThisblock?.type === "PARAGRAPH" && blockBeforeThisblock.content.at(-1)?.type === "ITEM_LIST";

  const blockAfterThisBlock = draft.redigertBrev.blocks[literalIndex.blockIndex + 1];
  const hasItemListBlockAfter =
    blockAfterThisBlock?.type === "PARAGRAPH" && blockAfterThisBlock.content.at(0)?.type === "ITEM_LIST";

  if (hasItemListBlockBefore && hasItemListBlockAfter) {
    toggleBulletListOnBetweenElemenets({
      draft,
      literalIndex,
      blockBeforeThisblock,
      blockAfterThisBlock,
      theItemListThatHasMySentence,
      sentenceElements,
      newContentIndex,
      newItemContentIndex,
    });
  } else if (hasItemListBlockBefore) {
    toggleBulletListOnWithItemListBefore({
      draft,
      literalIndex,
      blockBeforeThisblock,
      theItemListThatHasMySentence,
      sentenceElements,
      newContentIndex,
      newItemContentIndex,
    });
  } else if (hasItemListBlockAfter) {
    toggleBulletListOnWithItemListAfter({
      draft,
      literalIndex,
      blockAfterThisBlock,
      theItemListThatHasMySentence,
      sentenceElements,
      newContentIndex,
      newItemContentIndex,
    });
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
 * Henter alle literals/vars som er rundt en gitt index, fram til en ITEM_LIST er funnet
 *
 * Merk at vi returnerer også original indexen til elementene, for å brukes videre i replaceElementsBetweenIncluding
 */
const getSurroundingLiteralsAndVariables = (
  content: Content[],
  index: number,
): Array<{ element: TextContent; originalIndex: number }> => {
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
};

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
        const mergedItemList = newItemList(...current.items, ...next.items);
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

/**
 * Lager en ny punktliste, som er mellom to eksisterende punktlister.
 * Disse punktlistene skal merges til en punktliste.
 */
const toggleBulletListOnBetweenElemenets = (args: {
  draft: Draft<LetterEditorState>;
  literalIndex: LiteralIndex;
  blockBeforeThisblock: ParagraphBlock;
  blockAfterThisBlock: ParagraphBlock;
  theItemListThatHasMySentence: ItemList;
  sentenceElements: TextContent[];
  newContentIndex: number;
  newItemContentIndex: number;
}) => {
  const theItemListBefore = args.blockBeforeThisblock!.content.at(-1) as ItemList;
  const theItemListAfter = args.blockAfterThisBlock!.content.at(0) as ItemList;

  const mergedItems = newItemList(
    ...theItemListBefore.items,
    ...args.theItemListThatHasMySentence.items,
    ...theItemListAfter.items,
  );

  const newBlockContent = [
    ...args.draft.redigertBrev.blocks.slice(0, args.literalIndex.blockIndex - 1),
    newParagraph(...args.blockBeforeThisblock.content.slice(0, -1)),
    newParagraph(mergedItems),
    newParagraph(...args.blockAfterThisBlock.content.slice(1)),
    ...args.draft.redigertBrev.blocks.slice(args.literalIndex.blockIndex + 2),
  ].filter((block) => block.content.length > 0);

  const newBlockContentIndex = newBlockContent.findIndex((block) => {
    const blockItemLists = block.content.filter((content) => content.type === "ITEM_LIST") as ItemList[];
    return blockItemLists.some((block) => isEqual(block, mergedItems));
  });

  const itemIndex = mergedItems.items.findIndex((i) => isEqual(i.content, args.sentenceElements));

  args.draft.redigertBrev.blocks = newBlockContent;
  args.draft.focus = {
    blockIndex: newBlockContentIndex,
    contentIndex: args.newContentIndex,
    itemIndex: itemIndex,
    itemContentIndex: args.newItemContentIndex,
    cursorPosition: args.draft.focus.cursorPosition,
  };
};

/**
 * Lager en ny punktliste, som har en eksisterende punktliste før seg.
 * Merger disse 2 punktlistene til en punktliste.
 */
const toggleBulletListOnWithItemListBefore = (args: {
  draft: Draft<LetterEditorState>;
  literalIndex: LiteralIndex;
  blockBeforeThisblock: ParagraphBlock;
  theItemListThatHasMySentence: ItemList;
  sentenceElements: TextContent[];
  newContentIndex: number;
  newItemContentIndex: number;
}) => {
  const theItemListBefore = args.blockBeforeThisblock!.content.at(-1) as ItemList;

  const mergedItems = newItemList(...theItemListBefore.items, ...args.theItemListThatHasMySentence.items);

  const newBlocks = [
    ...args.draft.redigertBrev.blocks.slice(0, args.literalIndex.blockIndex - 1),
    newParagraph(...args.blockBeforeThisblock.content.slice(0, -1)),
    newParagraph(mergedItems),
    ...args.draft.redigertBrev.blocks.slice(args.literalIndex.blockIndex + 1),
  ].filter((block) => block.content.length > 0);

  const newBlockContentIndex = newBlocks.findIndex((block) => {
    const blockItemLists = block.content.filter((content) => content.type === "ITEM_LIST") as ItemList[];
    return blockItemLists.some((block) => isEqual(block, mergedItems));
  });

  const itemIndex = mergedItems.items.findIndex((i) => isEqual(i.content, args.sentenceElements));
  const theIndexOfMySentenceInItemList = args.theItemListThatHasMySentence?.items.findIndex((i) =>
    isEqual(i.content, args.sentenceElements),
  );

  args.draft.redigertBrev.blocks = newBlocks;
  args.draft.focus = {
    blockIndex: newBlockContentIndex,
    contentIndex: theIndexOfMySentenceInItemList,
    itemIndex: itemIndex,
    itemContentIndex: args.newItemContentIndex,
    cursorPosition: args.draft.focus.cursorPosition,
  };
};

const toggleBulletListOnWithItemListAfter = (args: {
  draft: Draft<LetterEditorState>;
  literalIndex: LiteralIndex;
  blockAfterThisBlock: ParagraphBlock;
  theItemListThatHasMySentence: ItemList;
  sentenceElements: TextContent[];
  newContentIndex: number;
  newItemContentIndex: number;
}) => {
  const theItemListAfter = args.blockAfterThisBlock!.content.at(0) as ItemList;

  const mergedItems = newItemList(...args.theItemListThatHasMySentence.items, ...theItemListAfter.items);

  const newBlockContent = [
    ...(args.literalIndex.blockIndex === 0
      ? []
      : args.draft.redigertBrev.blocks.slice(0, args.literalIndex.blockIndex)),
    newParagraph(mergedItems),
    newParagraph(...(args.blockAfterThisBlock.content.slice(0, -1) || [])),
    ...args.draft.redigertBrev.blocks.slice(args.literalIndex.blockIndex + 2),
  ].filter((block) => block.content.length > 0);

  const newBlockContentIndex = newBlockContent.findIndex((block) => {
    const blockItemLists = block.content.filter((content) => content.type === "ITEM_LIST") as ItemList[];
    return blockItemLists.some((block) => isEqual(block, mergedItems));
  });

  const itemIndex = mergedItems.items.findIndex((i) => isEqual(i.content, args.sentenceElements));

  args.draft.redigertBrev.blocks = newBlockContent;
  args.draft.focus = {
    blockIndex: newBlockContentIndex,
    contentIndex: args.newContentIndex,
    itemIndex: itemIndex,
    itemContentIndex: args.newItemContentIndex,
    cursorPosition: args.draft.focus.cursorPosition,
  };
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
  const blockContentAfterItemList = thisBlock.content.slice(itemContentIndex.contentIndex + 1);

  const listItemsBeforeNewParagraph = thisItemList.items.slice(0, itemContentIndex.itemIndex);
  const listItemsAfterNewParagraph = thisItemList.items.slice(itemContentIndex.itemIndex + 1);

  if (itemContentIndex.itemIndex === 0) {
    toggleBulletListOffAtTheStartOfItemList({
      dokumentBlocks,
      draft,
      itemContentIndex,
      existingParagraphContent,
      listItemsAfterNewParagraph,
      blockContentBeforeItemList,
    });
  } else if (itemContentIndex.itemIndex > 0 && itemContentIndex.itemIndex < thisItemList.items.length - 1) {
    toggleBulletListOffBetweenListElements({
      dokumentBlocks,
      draft,
      itemContentIndex,
      existingParagraphContent,
      listItemsAfterNewParagraph,
      blockContentBeforeItemList,
      listItemsBeforeNewParagraph,
    });
  } else if (itemContentIndex.itemIndex === thisItemList.items.length - 1) {
    toggleBulletListOffAtTheEndOfItemList({
      dokumentBlocks,
      draft,
      itemContentIndex,
      existingParagraphContent,
      blockContentBeforeItemList,
      listItemsBeforeNewParagraph,
      blockContentAfterItemList,
    });
  }
};

/**
 * Fjerner det første punktet fra en punktliste
 * Bygger dette punktet som en ny block, og setter den inn i dokumentet
 * Legger tilbake resten av punktene i punktlisten
 */
const toggleBulletListOffAtTheStartOfItemList = (args: {
  dokumentBlocks: (Draft<Title1Block> | Draft<Title2Block> | Draft<ParagraphBlock>)[];
  draft: Draft<LetterEditorState>;
  itemContentIndex: ItemContentIndex;
  existingParagraphContent: TextContent[];
  listItemsAfterNewParagraph: Item[];
  blockContentBeforeItemList: (Draft<ItemList> | Draft<LiteralValue> | Draft<VariableValue>)[];
}) => {
  const hasBlockContentBeforeItemList = args.blockContentBeforeItemList.length > 0;
  const newParagraphBlock = newParagraph(...args.existingParagraphContent);
  const hasListItemsAfter = args.listItemsAfterNewParagraph.length > 0;
  const blockContentAfter = newParagraph(newItemList(...args.listItemsAfterNewParagraph));

  const newBlockContents = [
    ...(hasBlockContentBeforeItemList ? [newParagraph(...args.blockContentBeforeItemList)] : []),
    newParagraphBlock,
    ...(hasListItemsAfter ? [blockContentAfter] : []),
  ];

  const newBlockContentList = [
    ...args.dokumentBlocks.slice(0, args.itemContentIndex.blockIndex),
    ...newBlockContents,
    ...args.dokumentBlocks.slice(args.itemContentIndex.blockIndex + 1),
  ].filter((block) => block.content.length > 0);

  //TODO - bug - hvis det eksisterer blocker som er lik den nye (for eksempel et tom literal, og en tomt punkt, vil vi treffe literalen)
  const newParagraphBlockIndex = newBlockContentList.findIndex((block) => isEqual(block, newParagraphBlock));

  args.draft.redigertBrev.blocks = newBlockContentList;
  args.draft.focus = {
    blockIndex: newParagraphBlockIndex,
    contentIndex: args.itemContentIndex.itemContentIndex,
    cursorPosition: args.draft.focus.cursorPosition,
  };
};

/**
 * Fjerner et punkt fra en punktliste, som ikke er det første eller siste punktet i punktlisten
 * Bygger en ny block med punktene før det punktet som skal fjernes, og setter den inn i dokumentet
 * Bygger 2 nye blocks, med innhold som er før og etter det punktet som skal fjernes, og setter de inn i dokumentet
 */
const toggleBulletListOffBetweenListElements = (args: {
  dokumentBlocks: (Draft<Title1Block> | Draft<Title2Block> | Draft<ParagraphBlock>)[];
  draft: Draft<LetterEditorState>;
  itemContentIndex: ItemContentIndex;
  existingParagraphContent: TextContent[];
  listItemsBeforeNewParagraph: Item[];
  listItemsAfterNewParagraph: Item[];
  blockContentBeforeItemList: (Draft<ItemList> | Draft<LiteralValue> | Draft<VariableValue>)[];
}) => {
  const hasBlockContentBeforeItemList = args.blockContentBeforeItemList.length > 0;
  const newBeforeBlock: ParagraphBlock = hasBlockContentBeforeItemList
    ? newParagraph(...args.blockContentBeforeItemList, newItemList(...args.listItemsBeforeNewParagraph))
    : newParagraph(newItemList(...args.listItemsBeforeNewParagraph));

  const newParagraphBlock = newParagraph(...args.existingParagraphContent);
  const blockContentAfter = newParagraph(newItemList(...args.listItemsAfterNewParagraph));

  const newBlockContents = [newBeforeBlock, newParagraphBlock, blockContentAfter];

  const newBlockContentList = [
    ...args.dokumentBlocks.slice(0, args.itemContentIndex.blockIndex),
    ...newBlockContents,
    ...args.dokumentBlocks.slice(args.itemContentIndex.blockIndex + 1),
  ].filter((block) => block.content.length > 0);

  //TODO - bug - hvis det eksisterer blocker som er lik den nye (for eksempel et tom literal, og en tomt punkt, vil vi treffe literalen)
  const newParagraphBlockIndex = newBlockContentList.findIndex((block) => isEqual(block, newParagraphBlock));

  args.draft.redigertBrev.blocks = newBlockContentList;
  args.draft.focus = {
    blockIndex: newParagraphBlockIndex,
    contentIndex: args.itemContentIndex.itemContentIndex,
    cursorPosition: args.draft.focus.cursorPosition,
  };
};

/**
 * Fjerner det siste punktet fra en punktliste
 * Bygger dette punktet som en ny block, og setter den inn i dokumentet
 * Legger tilbake resten av punktene i punktlisten før den nye blocken
 */
const toggleBulletListOffAtTheEndOfItemList = (args: {
  dokumentBlocks: (Draft<Title1Block> | Draft<Title2Block> | Draft<ParagraphBlock>)[];
  draft: Draft<LetterEditorState>;
  itemContentIndex: ItemContentIndex;
  existingParagraphContent: TextContent[];
  blockContentBeforeItemList: (Draft<ItemList> | Draft<LiteralValue> | Draft<VariableValue>)[];
  listItemsBeforeNewParagraph: Item[];
  blockContentAfterItemList: (Draft<ItemList> | Draft<LiteralValue> | Draft<VariableValue>)[];
}) => {
  const hasBlockContentAfterItemList = args.blockContentAfterItemList.length > 0;
  const newBlockContentBefore = newParagraph(
    ...args.blockContentBeforeItemList,
    newItemList(...args.listItemsBeforeNewParagraph),
  );
  const newParagraphBlock = newParagraph(...args.existingParagraphContent);

  const newBlockContents = hasBlockContentAfterItemList
    ? [newBlockContentBefore, newParagraphBlock, newParagraph(...args.blockContentAfterItemList)]
    : [newBlockContentBefore, newParagraphBlock];

  const newBlockContentList = [
    ...args.dokumentBlocks.slice(0, args.itemContentIndex.blockIndex),
    ...newBlockContents,
    ...args.dokumentBlocks.slice(args.itemContentIndex.blockIndex + 1),
  ].filter((block) => block.content.length > 0);

  //TODO - bug - hvis det eksisterer blocker som er lik den nye (for eksempel et tom literal, og en tomt punkt, vil vi treffe literalen)
  const newParagraphBlockIndex = newBlockContentList.findIndex((block) => isEqual(block, newParagraphBlock));

  args.draft.redigertBrev.blocks = newBlockContentList;
  args.draft.focus = {
    blockIndex: newParagraphBlockIndex,
    contentIndex: args.itemContentIndex.itemContentIndex,
    cursorPosition: args.draft.focus.cursorPosition,
  };
};
