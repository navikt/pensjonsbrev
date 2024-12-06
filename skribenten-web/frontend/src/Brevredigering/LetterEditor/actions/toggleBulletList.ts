import type { Draft } from "immer";
import { produce } from "immer";
import { isEqual } from "lodash";

import type { Content, ItemList, TextContent } from "~/types/brevbakerTypes";

import type { Action } from "../lib/actions";
import type { LetterEditorState } from "../model/state";
import { isTextContent } from "../model/utils";
import { deleteElement, newItemList } from "./common";
import type { ItemContentIndex, LiteralIndex } from "./model";

export const toggleBulletList: Action<LetterEditorState, [literalIndex: LiteralIndex]> = produce(
  (draft, literalIndex) => {
    const block = draft.redigertBrev.blocks[literalIndex.blockIndex];
    if (block.type !== "PARAGRAPH") {
      return;
    }

    draft.isDirty = true;
    const theContentTheUserIsOn = block.content[literalIndex.contentIndex];
    if (isTextContent(theContentTheUserIsOn)) {
      toggleBulletListOn(draft, literalIndex);
    } else if (theContentTheUserIsOn.type === "ITEM_LIST" && "itemIndex" in literalIndex) {
      toggleBulletListOff(draft, literalIndex as ItemContentIndex);
    }
  },
);

/**
 * Når vi lager et punkt, så må vi ta høyde for at det kan være en punktliste før, etter, eller ingen punktliste.
 * Det kan finnes punktliste i blokk før, blokk etter, eller begge. Det kan også være punktliste i samme blokk, før / etter / begge, på literalen vi er på
 *
 * Det vil si at når vi converterer en literal til en ny punktliste, merger vi med nabo-punktlistene, hvis de eksisterer
 * Så må vi sjekke om det finnes nabo-punktlister på blokk-nivå, og merge de også
 *
 * Fordi vi gjør en såpass stor endring i dokument strukturen, Så må vi oppdatere fokuset til editorstaten til å være på rett plass
 */
const toggleBulletListOn = (draft: Draft<LetterEditorState>, literalIndex: LiteralIndex) => {
  const thisBlock = draft.redigertBrev.blocks[literalIndex.blockIndex];
  const theIdexOfTheContent = literalIndex.contentIndex;
  const sentence = getSurroundingLiteralsAndVariables(thisBlock.content, theIdexOfTheContent);
  const sentenceElements = sentence.map((r) => r.element);

  const replacedWithItemList = replaceElementsBetweenIncluding(
    thisBlock.content,
    sentence[0].originalIndex,
    sentence.at(-1)!.originalIndex,
    newItemList({
      items: [{ id: null, content: sentenceElements }],
    }),
  );

  const { newContent, newDeletedContent } = mergeAdjoiningItemLists(replacedWithItemList);
  thisBlock.content = newContent;
  thisBlock.deletedContent.push(...sentenceElements.filter((s) => !!s.id).map((r) => r.id!), ...newDeletedContent);

  const newContentIndex = draft.redigertBrev.blocks[literalIndex.blockIndex].content.findIndex(
    (content) => content.type === "ITEM_LIST" && content.items.some((i) => isEqual(i.content, sentenceElements)),
  );
  const newItemIndex = (
    draft.redigertBrev.blocks[literalIndex.blockIndex].content[newContentIndex] as ItemList
  ).items.findIndex((i) => isEqual(i.content, sentenceElements));
  const newItemContentIndex = sentence.findIndex((r) => r.originalIndex === theIdexOfTheContent);

  draft.focus = {
    blockIndex: literalIndex.blockIndex,
    contentIndex: newContentIndex,
    itemIndex: newItemIndex,
    itemContentIndex: newItemContentIndex,
    cursorPosition: draft.focus.cursorPosition,
  };
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

  // Loop backwards from index until an a non-textContent is encountered
  for (let i = index; i >= 0; i--) {
    const current = content[i];
    if (!isTextContent(current)) break;
    result.unshift({ element: current, originalIndex: i });
  }

  // Loop forwards from index + 1 until an non-textContent is encountered
  for (let i = index + 1; i < content.length; i++) {
    const current = content[i];
    if (!isTextContent(current)) break;
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

const mergeAdjoiningItemLists = (content: Content[]): { newContent: Content[]; newDeletedContent: number[] } => {
  const newContent: Content[] = [];
  const newDeletedContent: number[] = [];
  let temp: ItemList[] = [];

  for (const current of content) {
    if (current.type === "ITEM_LIST") {
      temp.push(current);
    } else {
      if (temp.length > 0) {
        const { itemList, deletedItemLists } = mergeItemLists(temp);
        newContent.push(itemList);
        newDeletedContent.push(...deletedItemLists);
        temp = [];
      }
      newContent.push(current);
    }
  }

  if (temp.length > 0) {
    const { itemList, deletedItemLists } = mergeItemLists(temp);
    newContent.push(itemList);
    newDeletedContent.push(...deletedItemLists);
  }

  return { newContent, newDeletedContent };
};

function mergeItemLists(itemLists: ItemList[]): { itemList: ItemList; deletedItemLists: number[] } {
  const idsToMerge = itemLists.filter((il) => !!il.id).map((il) => il.id!);
  const idToKeep = idsToMerge.at(0);

  const itemList = newItemList({
    id: idToKeep,
    items: itemLists.flatMap((i) => i.items),
    deletedItems: idToKeep ? itemLists.find((l) => l.id === idToKeep)?.deletedItems : [],
  });

  return { itemList, deletedItemLists: idsToMerge.filter((id) => id !== idToKeep) };
}

/**
 * Når vi fjerner et punkt fra en punktliste, så må man ta høyde for at man kan potensielt ha content før og etter punktlisten.
 * Vi må også ta høyde for at vi kan være på første, siste eller et sted i mellom punktene i listen.
 *  Det er fordi vi må styre hvor den nye contenten skal havne, samt resten av innholdet i punktlisten.
 *
 * Fordi vi gjør en såpass stor endring i dokument strukturen, Så må vi oppdatere fokuset til editorstaten til å være på rett plass
 */
const toggleBulletListOff = (draft: Draft<LetterEditorState>, itemContentIndex: ItemContentIndex) => {
  const thisBlock = draft.redigertBrev.blocks[itemContentIndex.blockIndex];
  const thisItemList = thisBlock.content[itemContentIndex.contentIndex] as ItemList;

  if (itemContentIndex.itemIndex === 0) {
    toggleBulletListOffAtTheStartOfItemList({ draft, itemContentIndex });
  } else if (itemContentIndex.itemIndex > 0 && itemContentIndex.itemIndex < thisItemList.items.length - 1) {
    toggleBulletListOffBetweenListElements({ draft, itemContentIndex });
  } else if (itemContentIndex.itemIndex === thisItemList.items.length - 1) {
    toggleBulletListOffAtTheEndOfItemList({ draft, itemContentIndex });
  }
};

/**
 * Fjerner det første punktet fra en punktliste, og legger det som en vanlig text-content i blocken.
 * Legger tilbake resten av punktene i punktlisten
 *
 * Fordi vi gjør en såpass stor endring i dokument strukturen, Så må vi oppdatere fokuset til editorstaten til å være på rett plass
 */
const toggleBulletListOffAtTheStartOfItemList = (args: {
  draft: Draft<LetterEditorState>;
  itemContentIndex: ItemContentIndex;
}) => {
  const thisBlock = args.draft.redigertBrev.blocks[args.itemContentIndex.blockIndex];
  const thisItemList = thisBlock.content[args.itemContentIndex.contentIndex] as ItemList;
  const thisItem = thisItemList.items[0];

  thisItemList.items.splice(0, 1);
  deleteElement(thisItem, thisItemList.items, thisItemList.deletedItems);

  thisBlock.content.splice(
    args.itemContentIndex.contentIndex,
    thisItemList.items.length === 0 ? 1 : 0,
    ...thisItem.content,
  );
  deleteElement(thisItemList, thisBlock.content, thisBlock.deletedContent);

  args.draft.focus = {
    blockIndex: args.itemContentIndex.blockIndex,
    contentIndex:
      args.itemContentIndex.contentIndex + (thisItem.content.length > 1 ? args.itemContentIndex.itemContentIndex : 0),
    cursorPosition: args.draft.focus.cursorPosition,
  };
};

/**
 * Fjerner et punkt fra en punktliste, som ikke er det første eller siste punktet i punktlisten
 * legger det som en vanlig text-content i blocken.
 * Bygger 2 nye itemLists, med innhold som er før og etter det punktet som skal fjernes, og setter de inn i blocken
 *
 * Fordi vi gjør en såpass stor endring i dokument strukturen, Så må vi oppdatere fokuset til editorstaten til å være på rett plass
 */
const toggleBulletListOffBetweenListElements = (args: {
  draft: Draft<LetterEditorState>;
  itemContentIndex: ItemContentIndex;
}) => {
  const thisBlock = args.draft.redigertBrev.blocks[args.itemContentIndex.blockIndex];

  const thisBlockContentBeforeItemList = thisBlock.content.slice(0, args.itemContentIndex.contentIndex);
  const thisItemList = thisBlock.content[args.itemContentIndex.contentIndex] as ItemList;
  const itemsBefore = thisItemList.items.slice(0, args.itemContentIndex.itemIndex);
  const thisItem = thisItemList.items[args.itemContentIndex.itemIndex];
  const itemsAfter = thisItemList.items.slice(args.itemContentIndex.itemIndex + 1);
  const thisBlockContentAfterItemList = thisBlock.content.slice(args.itemContentIndex.contentIndex + 1);

  thisBlock.content = [
    ...thisBlockContentBeforeItemList,
    newItemList({
      ...thisItemList,
      items: itemsBefore,
      deletedItems: [...thisItemList.deletedItems, ...(thisItem.id ? [thisItem.id] : [])],
    }),
    ...thisItem.content,
    newItemList({ items: itemsAfter }),
    ...thisBlockContentAfterItemList,
  ];

  args.draft.focus = {
    blockIndex: args.itemContentIndex.blockIndex,
    contentIndex: args.itemContentIndex.itemContentIndex,
    cursorPosition: args.draft.focus.cursorPosition,
  };
};

/**
 * Fjerner det siste punktet fra en punktliste, , og legger det som en vanlig text-content i blocken.
 * Legger tilbake resten av punktene i punktlisten før den nye blocken
 *
 * Fordi vi gjør en såpass stor endring i dokument strukturen, Så må vi oppdatere fokuset til editorstaten til å være på rett plass
 */
const toggleBulletListOffAtTheEndOfItemList = (args: {
  draft: Draft<LetterEditorState>;
  itemContentIndex: ItemContentIndex;
}) => {
  const thisBlock = args.draft.redigertBrev.blocks[args.itemContentIndex.blockIndex];
  const thisItemList = thisBlock.content[args.itemContentIndex.contentIndex] as ItemList;
  const thisItem = thisItemList.items[args.itemContentIndex.itemIndex];

  thisItemList.items.splice(-1, 1);
  deleteElement(thisItem, thisItemList.items, thisItemList.deletedItems);

  thisBlock.content.splice(args.itemContentIndex.contentIndex + 1, 0, ...thisItem.content);
  if (thisItemList.items.length === 0) {
    thisBlock.content.splice(args.itemContentIndex.contentIndex, 1);
  }

  deleteElement(thisItemList, thisBlock.content, thisBlock.deletedContent);

  args.draft.focus = {
    blockIndex: args.itemContentIndex.blockIndex,
    contentIndex:
      args.itemContentIndex.contentIndex + args.itemContentIndex.itemContentIndex + args.itemContentIndex.itemIndex,
    cursorPosition: args.draft.focus.cursorPosition,
  };
};
