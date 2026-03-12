import { current, type Draft } from "immer";
import { isEqual } from "lodash";

import { type ItemList, ListType } from "~/types/brevbakerTypes";

import { type Action, withPatches } from "../lib/actions";
import type { ItemContentIndex, LetterEditorState, LiteralIndex } from "../model/state";
import { isItemList, isTextContent } from "../model/utils";
import { addElements, findAdjoiningContent, newItem, newItemList, removeElements } from "./common";

export const toggleBulletList: Action<LetterEditorState, [literalIndex: LiteralIndex]> = withPatches(
  (draft, literalIndex) => toggleList(draft, literalIndex, ListType.PUNKTLISTE),
);

export const toggleNumberList: Action<LetterEditorState, [literalIndex: LiteralIndex]> = withPatches(
  (draft, literalIndex) => toggleList(draft, literalIndex, ListType.NUMMERERT_LISTE),
);

const toggleList = (draft: Draft<LetterEditorState>, literalIndex: LiteralIndex, listType: ListType) => {
  const block = draft.redigertBrev.blocks[literalIndex.blockIndex];
  if (block?.type !== "PARAGRAPH") {
    return;
  }

  const theContentTheUserIsOn = block.content[literalIndex.contentIndex];
  if (isTextContent(theContentTheUserIsOn)) {
    toggleListOn(draft, literalIndex, listType);
    mergeListWithAdjacentBlocks(draft, listType);
  } else if (isItemList(theContentTheUserIsOn) && "itemIndex" in literalIndex) {
    if (theContentTheUserIsOn.listType === listType) {
      toggleListOff(draft, literalIndex as ItemContentIndex);
    } else {
      switchListType(draft, literalIndex, listType);
    }
  }
};

/**
 * Endrer listType på en eksisterende liste uten å endre innholdet eller fokuset.
 */
const switchListType = (draft: Draft<LetterEditorState>, literalIndex: LiteralIndex, listType: ListType) => {
  const block = draft.redigertBrev.blocks[literalIndex.blockIndex];
  const itemList = block.content[literalIndex.contentIndex] as Draft<ItemList>;
  itemList.listType = listType;
  draft.saveStatus = "DIRTY";
};

/**
 * Når vi lager et punkt, så må vi ta høyde for at det kan være en punktliste før, etter, eller
 * ingen punktliste.  Det kan finnes punktliste i blokk før, blokk etter, eller begge. Det kan også
 * være punktliste i samme blokk, før / etter / begge, på literalen vi er på
 *
 * Det vil si at når vi converterer en literal til en ny punktliste, merger vi med
 * nabo-punktlistene, hvis de eksisterer Så må vi sjekke om det finnes nabo-punktlister på
 * blokk-nivå, og merge de også
 *
 * Fordi vi gjør en såpass stor endring i dokument strukturen, Så må vi oppdatere fokuset til
 * editorstaten til å være på rett plass
 */
const toggleListOn = (draft: Draft<LetterEditorState>, literalIndex: LiteralIndex, listType: ListType) => {
  draft.saveStatus = "DIRTY";

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
    [newItemList({ listType, items: [newItem({ content: sentence })] })],
    sentenceIndex.startIndex,
    thisBlock.content,
    thisBlock.deletedContent,
  );

  // merge adjoining item lists of the same type only
  const itemListsIndex = findAdjoiningContent(
    sentenceIndex.startIndex,
    thisBlock.content,
    (c): c is ItemList => isItemList(c) && c.listType === listType,
  );
  const itemLists = removeElements(itemListsIndex.startIndex, itemListsIndex.count, {
    content: thisBlock.content,
    deletedContent: thisBlock.deletedContent,
    id: thisBlock.id,
  }).filter(isItemList);

  // Collect all items from the lists to merge, preserving order.
  // We identify the list with a non-null id to keep (so the backend can track it),
  // falling back to the last list in the sequence.
  const listWithId = itemLists.find((l) => l.id !== null) ?? itemLists[itemLists.length - 1];
  const allItems = itemLists.flatMap((l) => [...l.items]);
  const allDeletedItems = itemLists.flatMap((l) => [...l.deletedItems]);

  const mergedList = newItemList({
    id: listWithId.id,
    listType,
    items: allItems,
    deletedItems: allDeletedItems,
  });
  addElements([mergedList], itemListsIndex.startIndex, thisBlock.content, thisBlock.deletedContent);

  // update focus
  const newItemIndex = mergedList.items.findIndex((i) => isEqual(i.content, sentence));
  draft.focus = {
    blockIndex: literalIndex.blockIndex,
    contentIndex: itemListsIndex.startIndex,
    itemIndex: newItemIndex,
    itemContentIndex: theIndexOfTheContent - sentenceIndex.startIndex,
    cursorPosition: draft.focus.cursorPosition,
  };
};

/**
 * After toggling a text content to a list within a block, check if the previous and next blocks
 * also end/start with a same-type list, and merge them all into one paragraph with one list.
 */
const mergeListWithAdjacentBlocks = (draft: Draft<LetterEditorState>, listType: ListType) => {
  const blocks = draft.redigertBrev.blocks;
  let blockIndex = draft.focus.blockIndex;
  const contentIndex = draft.focus.contentIndex;

  const currentBlock = blocks[blockIndex];
  if (!currentBlock || currentBlock.type !== "PARAGRAPH") return;

  const currentList = currentBlock.content[contentIndex];
  if (!isItemList(currentList) || currentList.listType !== listType) return;

  // Check previous block: if its last content is a same-type ITEM_LIST, merge it in front.
  if (blockIndex > 0) {
    const prevBlock = blocks[blockIndex - 1];
    if (prevBlock.type === "PARAGRAPH" && prevBlock.content.length > 0) {
      const lastPrevContent = prevBlock.content[prevBlock.content.length - 1];
      if (isItemList(lastPrevContent) && lastPrevContent.listType === listType) {
        const prevItems = current(lastPrevContent.items);
        const prevDeletedItems = current(lastPrevContent.deletedItems);

        removeElements(prevBlock.content.length - 1, 1, {
          content: prevBlock.content,
          deletedContent: prevBlock.deletedContent,
          id: prevBlock.id,
        });

        if (prevBlock.content.length === 0) {
          removeElements(blockIndex - 1, 1, {
            content: blocks,
            deletedContent: draft.redigertBrev.deletedBlocks,
            id: null,
          });
          blockIndex--;
        }

        const curList = blocks[blockIndex].content[contentIndex] as Draft<ItemList>;
        curList.items.unshift(...(prevItems as ItemList["items"]));
        curList.deletedItems.push(...(prevDeletedItems as ItemList["deletedItems"]));
        draft.focus.blockIndex = blockIndex;
      }
    }
  }

  // Check next block: if its first content is a same-type ITEM_LIST, merge it at the end.
  const nextBlockIndex = blockIndex + 1;
  if (nextBlockIndex < blocks.length) {
    const nextBlock = blocks[nextBlockIndex];
    if (nextBlock.type === "PARAGRAPH" && nextBlock.content.length > 0) {
      const firstNextContent = nextBlock.content[0];
      if (isItemList(firstNextContent) && firstNextContent.listType === listType) {
        const nextItems = current(firstNextContent.items);
        const nextDeletedItems = current(firstNextContent.deletedItems);

        removeElements(0, 1, {
          content: nextBlock.content,
          deletedContent: nextBlock.deletedContent,
          id: nextBlock.id,
        });

        if (nextBlock.content.length === 0) {
          removeElements(nextBlockIndex, 1, {
            content: blocks,
            deletedContent: draft.redigertBrev.deletedBlocks,
            id: null,
          });
        }

        const curList = blocks[blockIndex].content[contentIndex] as Draft<ItemList>;
        curList.items.push(...(nextItems as ItemList["items"]));
        curList.deletedItems.push(...(nextDeletedItems as ItemList["deletedItems"]));
      }
    }
  }
};

/**
 * - om det er det første punktet så flyttes item.content ut i block.content før listen
 * - om det er det siste punktet så flyttes item.content ut i block.content etter listen
 * - om det er et punkt i midten så splittes listen i to ved angitt punkt og item.content settes inn i mellom listene.
 *
 */
const toggleListOff = (draft: Draft<LetterEditorState>, literalIndex: ItemContentIndex) => {
  const block = draft.redigertBrev.blocks[literalIndex.blockIndex];
  const itemList = block.content[literalIndex.contentIndex] as ItemList;

  if (literalIndex.itemIndex >= 0 && literalIndex.itemIndex < itemList.items.length) {
    draft.saveStatus = "DIRTY";

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
        [...itemContent, newItemList({ listType: itemList.listType, items: itemsAfter })],
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
