import { current, type Draft } from "immer";
import { isEqual } from "lodash";

import { type ItemList, ListType } from "~/types/brevbakerTypes";

import { type Action, withPatches } from "../lib/actions";
import { type ItemContentIndex, type LetterEditorState, type LiteralIndex } from "../model/state";
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

  const blockContent = block.content[literalIndex.contentIndex];
  if (isTextContent(blockContent)) {
    toggleListOn(draft, literalIndex, listType);
    mergeListWithAdjacentBlocks(draft, draft.focus.blockIndex, draft.focus.contentIndex, listType);
  } else if (isItemList(blockContent) && "itemIndex" in literalIndex) {
    if (blockContent.listType === listType) {
      toggleListOff(draft, literalIndex as ItemContentIndex);
    } else {
      switchListType(draft, literalIndex, listType);
      const { newContentIndex, itemIndexOffset } = mergeAdjacentListsInBlock(
        draft,
        literalIndex.blockIndex,
        literalIndex.contentIndex,
        listType,
      );
      draft.focus = {
        blockIndex: literalIndex.blockIndex,
        contentIndex: newContentIndex,
        itemIndex: itemIndexOffset + (literalIndex as ItemContentIndex).itemIndex,
        itemContentIndex: (literalIndex as ItemContentIndex).itemContentIndex,
        cursorPosition: draft.focus.cursorPosition,
      };
      mergeListWithAdjacentBlocks(draft, literalIndex.blockIndex, newContentIndex, listType);
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
 * Når vi lager et nytt listeelement, må vi ta høyde for at det kan ligge inntil en eksisterende liste.  Det kan
 * være en liste i blokken før, etter, eller begge. Det kan også være en liste i samme
 * blokk; før, etter eller begge.
 *
 * Det vil si at når vi konverterer en literal til en liste, må vi merge med
 * nabolister - hvis de eksisterer - i samme blokk eller naboblokk.
 *
 * Denne mergingen kan også påvirke adressen til fokus
 */
const toggleListOn = (draft: Draft<LetterEditorState>, literalIndex: LiteralIndex, listType: ListType) => {
  draft.saveStatus = "DIRTY";

  const block = draft.redigertBrev.blocks[literalIndex.blockIndex];
  const contentIndex = literalIndex.contentIndex;

  // move sentence into a new item list
  const textIndex = findAdjoiningContent(contentIndex, block.content, isTextContent);
  const text = removeElements(textIndex.startIndex, textIndex.count, {
    content: block.content,
    deletedContent: block.deletedContent,
    id: block.id,
  }).filter(isTextContent);
  addElements(
    [newItemList({ listType, items: [newItem({ content: text })] })],
    textIndex.startIndex,
    block.content,
    block.deletedContent,
  );

  // merge with neighbors
  const itemListIndex = findAdjoiningContent(
    textIndex.startIndex,
    block.content,
    (c): c is ItemList => isItemList(c) && c.listType === listType,
  );
  const itemLists = removeElements(itemListIndex.startIndex, itemListIndex.count, {
    content: block.content,
    deletedContent: block.deletedContent,
    id: block.id,
  }).filter(isItemList);

  // TODO(stw): May this fail if there are no list with ID?
  // Collect all items from the lists to merge, preserving order.
  // Identify a list with a non-null id to keep (so the backend can track it),
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
  addElements([mergedList], itemListIndex.startIndex, block.content, block.deletedContent);

  // update focus
  const newItemIndex = mergedList.items.findIndex((i) => isEqual(i.content, text));
  draft.focus = {
    blockIndex: literalIndex.blockIndex,
    contentIndex: itemListIndex.startIndex,
    itemIndex: newItemIndex,
    itemContentIndex: contentIndex - textIndex.startIndex,
    cursorPosition: draft.focus.cursorPosition,
  };
};

/**
 * After switching a list's type in-place, merges it with any immediately adjacent same-type
 * lists within the same block. Returns the new contentIndex of the merged list and the number
 * of items prepended from lists that preceded the current list (used to update itemIndex).
 */
const mergeAdjacentListsInBlock = (
  draft: Draft<LetterEditorState>,
  blockIndex: number,
  contentIndex: number,
  listType: ListType,
): { newContentIndex: number; itemIndexOffset: number } => {
  const block = draft.redigertBrev.blocks[blockIndex];
  if (!block || block.type !== "PARAGRAPH") return { newContentIndex: contentIndex, itemIndexOffset: 0 };

  const currentList = block.content[contentIndex];
  if (!isItemList(currentList) || currentList.listType !== listType)
    return { newContentIndex: contentIndex, itemIndexOffset: 0 };

  const itemListsIndex = findAdjoiningContent(
    contentIndex,
    block.content,
    (c): c is ItemList => isItemList(c) && c.listType === listType,
  );

  if (itemListsIndex.count <= 1) return { newContentIndex: contentIndex, itemIndexOffset: 0 };

  const itemLists = removeElements(itemListsIndex.startIndex, itemListsIndex.count, {
    content: block.content,
    deletedContent: block.deletedContent,
    id: block.id,
  }).filter(isItemList);

  // Count items from lists that preceded the current list in the adjoining range.
  const currentListPosition = contentIndex - itemListsIndex.startIndex;
  const itemIndexOffset = itemLists.slice(0, currentListPosition).reduce((sum, l) => sum + l.items.length, 0);

  const listWithId = itemLists.find((l) => l.id !== null) ?? itemLists[itemLists.length - 1];
  const allItems = itemLists.flatMap((l) => [...l.items]);
  const allDeletedItems = itemLists.flatMap((l) => [...l.deletedItems]);

  const mergedList = newItemList({ id: listWithId.id, listType, items: allItems, deletedItems: allDeletedItems });
  addElements([mergedList], itemListsIndex.startIndex, block.content, block.deletedContent);

  return { newContentIndex: itemListsIndex.startIndex, itemIndexOffset };
};

/**
 * If end/start with a same-type list, merge them all into one paragraph with one list.
 */
const mergeListWithAdjacentBlocks = (
  draft: Draft<LetterEditorState>,
  initialBlockIndex: number,
  contentIndex: number,
  listType: ListType,
) => {
  const blocks = draft.redigertBrev.blocks;
  let blockIndex = initialBlockIndex;

  const currentBlock = blocks[blockIndex];
  if (!currentBlock || currentBlock.type !== "PARAGRAPH") return;

  const currentList = currentBlock.content[contentIndex];
  if (!isItemList(currentList) || currentList.listType !== listType) return;

  // Only merge across block boundaries when the current block contains nothing but this list.
  // If the block has other content (text, other lists), the list is part of a richer paragraph
  // and should not be pulled into adjacent blocks.
  if (currentBlock.content.length !== 1) return;

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
        if ("itemIndex" in draft.focus) {
          draft.focus.itemIndex += prevItems.length;
        }
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
 * - om det er det første elelentet så flyttes item.content ut i block.content før listen
 * - om det er det siste elementet så flyttes item.content ut i block.content etter listen
 * - om det er et element i midten så splittes listen i to ved angitt punkt og item.content settes inn i mellom listene.
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
