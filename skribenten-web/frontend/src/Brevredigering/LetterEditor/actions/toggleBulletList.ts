import type { Draft } from "immer";
import { produce } from "immer";
import { isEqual } from "lodash";

import type { ItemList } from "~/types/brevbakerTypes";

import type { Action } from "../lib/actions";
import type { LetterEditorState } from "../model/state";
import { isItemList, isTextContent } from "../model/utils";
import { addElements, findAdjoiningContent, newItem, newItemList, removeElements } from "./common";
import type { ItemContentIndex, LiteralIndex } from "./model";

export const toggleBulletList: Action<LetterEditorState, [literalIndex: LiteralIndex]> = produce(
  (draft, literalIndex) => {
    const block = draft.redigertBrev.blocks[literalIndex.blockIndex];
    if (block.type !== "PARAGRAPH") {
      return;
    }

    const theContentTheUserIsOn = block.content[literalIndex.contentIndex];
    if (isTextContent(theContentTheUserIsOn)) {
      toggleBulletListOn(draft, literalIndex);
    } else if (isItemList(theContentTheUserIsOn) && "itemIndex" in literalIndex) {
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
  draft.isDirty = true;

  const thisBlock = draft.redigertBrev.blocks[literalIndex.blockIndex];
  const theIndexOfTheContent = literalIndex.contentIndex;

  // move sentence into a new item list
  const sentenceIndex = findAdjoiningContent(theIndexOfTheContent, thisBlock.content, isTextContent);
  const sentence = removeElements(sentenceIndex.startIndex, sentenceIndex.count, {
    content: thisBlock.content,
    deletedContent: thisBlock.deletedContent,
    id: thisBlock.id,
  }).filter(isTextContent);
  addElements(
    [newItemList({ items: [newItem({ content: sentence })] })],
    sentenceIndex.startIndex,
    thisBlock.content,
    thisBlock.deletedContent,
  );

  // merge adjoining item lists
  const itemListsIndex = findAdjoiningContent(sentenceIndex.startIndex, thisBlock.content, isItemList);
  const itemLists = removeElements(itemListsIndex.startIndex, itemListsIndex.count, {
    content: thisBlock.content,
    deletedContent: thisBlock.deletedContent,
    id: thisBlock.id,
  }).filter(isItemList);

  let listToKeep = itemLists[0];
  for (const list of itemLists.slice(1)) {
    if (listToKeep.id === null) {
      addElements(listToKeep.items, 0, list.items, list.deletedItems);
      listToKeep = list;
    } else {
      addElements(list.items, listToKeep.items.length, listToKeep.items, listToKeep.deletedItems);
    }
  }
  addElements([listToKeep], itemListsIndex.startIndex, thisBlock.content, thisBlock.deletedContent);

  // update focus
  const newItemIndex = listToKeep.items.findIndex((i) => isEqual(i.content, sentence));
  draft.focus = {
    blockIndex: literalIndex.blockIndex,
    contentIndex: itemListsIndex.startIndex,
    itemIndex: newItemIndex,
    itemContentIndex: theIndexOfTheContent - sentenceIndex.startIndex,
    cursorPosition: draft.focus.cursorPosition,
  };
};

/**
 * Fjerner angitt punkt fra en punktliste, og flytter item.content ut til block.content.
 * - om det er det første punktet så flyttes item.content ut i block.content før listen
 * - om det er det siste punktet så flyttes item.content ut i block.content etter listen
 * - om det er et punkt i midten så splittes listen i to ved angitt punkt og item.content settes inn i mellom listene.
 *
 */
const toggleBulletListOff = (draft: Draft<LetterEditorState>, literalIndex: ItemContentIndex) => {
  const block = draft.redigertBrev.blocks[literalIndex.blockIndex];
  const itemList = block.content[literalIndex.contentIndex] as ItemList;

  if (literalIndex.itemIndex >= 0 && literalIndex.itemIndex < itemList.items.length) {
    draft.isDirty = true;

    const itemContent = removeElements(literalIndex.itemIndex, 1, {
      content: itemList.items,
      deletedContent: itemList.deletedItems,
      id: itemList.id,
    }).flatMap((item) => item.content);

    if (itemList.items.length === 0) {
      removeElements(literalIndex.contentIndex, 1, {
        content: block.content,
        deletedContent: block.deletedContent,
        id: block.id,
      });
    }

    const insertItemContentIndex = literalIndex.contentIndex + (literalIndex.itemIndex === 0 ? 0 : 1);
    if (literalIndex.itemIndex === 0 || literalIndex.itemIndex === itemList.items.length) {
      // since we've already removed the item, then if we're removing the last item the index will be equal to `thisItemList.items.length`.
      addElements(itemContent, insertItemContentIndex, block.content, block.deletedContent);
    } else {
      const itemsAfter = removeElements(literalIndex.itemIndex, itemList.items.length, {
        content: itemList.items,
        deletedContent: itemList.deletedItems,
        id: itemList.id,
      });
      addElements(
        [...itemContent, newItemList({ items: itemsAfter })],
        insertItemContentIndex,
        block.content,
        block.deletedContent,
      );
    }

    draft.focus = {
      blockIndex: literalIndex.blockIndex,
      contentIndex: insertItemContentIndex + Math.min(itemContent.length - 1, literalIndex.itemContentIndex),
      cursorPosition: draft.focus.cursorPosition,
    };
  }
};
