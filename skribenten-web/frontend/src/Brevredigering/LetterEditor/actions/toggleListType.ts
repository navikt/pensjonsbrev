import { current, type Draft } from "immer";

import { type Content, type Item, type ItemList, ListType } from "~/types/brevbakerTypes";

import { type Action, withPatches } from "../lib/actions";
import { type ItemContentIndex, type LetterEditorState, type LiteralIndex } from "../model/state";
import { effectiveListType, isItemList, isTextContent } from "../model/utils";
import {
  addElements,
  findAdjoiningContent,
  newItem,
  newItemList,
  newParagraph,
  removeElements,
  splitMixedListBlock,
} from "./common";

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
    const focusBlockIndex = draft.focus.blockIndex;
    splitMixedListBlock(draft, focusBlockIndex);
    mergeListWithAdjacentBlocks(draft, draft.focus.blockIndex, draft.focus.contentIndex, listType);
  } else if (isItemList(blockContent) && "itemIndex" in literalIndex) {
    if (effectiveListType(blockContent) === listType) {
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
  itemList.editedListType = listType !== itemList.listType ? listType : null;
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
    (c): c is ItemList => isItemList(c) && effectiveListType(c) === listType,
  );
  const itemLists = removeElements(itemListIndex.startIndex, itemListIndex.count, {
    content: block.content,
    deletedContent: block.deletedContent,
    id: block.id,
  }).filter(isItemList);

  // Collect all items from the lists to merge, preserving order.
  // Identify a list with a non-null id to keep (so the backend can track it),
  // falling back to the last list in the sequence.
  const listWithId = itemLists.find((l) => l.id !== null) ?? itemLists[itemLists.length - 1];
  const allItems = itemLists.flatMap((l) => [...l.items]);
  const allDeletedItems = itemLists.flatMap((l) => [...l.deletedItems]);

  // INTENTIONAL ID COPY: the merged list is given listWithId.id even though it is a new object.
  // This preserves the backend's merge-tracking identity for this list across re-renders — without
  // it the backend would both "delete" and "re-add" the list from the fresh template render.
  // addElements below will un-delete that id from block.deletedContent if it was marked deleted.
  // Rule 1 exception: ID preservation during merge (see letter-editor-actions SKILL.md).
  const mergedList = newItemList({
    id: listWithId.id,
    listType: listWithId.listType,
    editedListType: listType !== listWithId.listType ? listType : listWithId.editedListType,
    items: allItems,
    deletedItems: allDeletedItems,
  });
  addElements([mergedList], itemListIndex.startIndex, block.content, block.deletedContent);

  // update focus — the new item's position is determined by items from lists that preceded it
  const newListPosition = textIndex.startIndex - itemListIndex.startIndex;
  const newItemIndex = itemLists.slice(0, newListPosition).reduce((sum, l) => sum + l.items.length, 0);
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
  if (!isItemList(currentList) || effectiveListType(currentList) !== listType)
    return { newContentIndex: contentIndex, itemIndexOffset: 0 };

  const itemListsIndex = findAdjoiningContent(
    contentIndex,
    block.content,
    (c): c is ItemList => isItemList(c) && effectiveListType(c) === listType,
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

  // INTENTIONAL ID COPY — same rationale as in toggleListOn above.
  // Rule 1 exception: ID preservation during merge (see letter-editor-actions SKILL.md).
  const mergedList = newItemList({
    id: listWithId.id,
    listType: listWithId.listType,
    editedListType: listType !== listWithId.listType ? listType : listWithId.editedListType,
    items: allItems,
    deletedItems: allDeletedItems,
  });
  addElements([mergedList], itemListsIndex.startIndex, block.content, block.deletedContent);

  return { newContentIndex: itemListsIndex.startIndex, itemIndexOffset };
};

/**
 * If the current block ends/starts with a same-type list in adjacent blocks, merge them all
 * into one paragraph with one list. Only activates when the current block contains nothing
 * but this list — blocks with mixed content (text + lists) are left untouched.
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
  if (!isItemList(currentList) || effectiveListType(currentList) !== listType) return;

  // Only merge across block boundaries when the current block contains nothing but this list.
  // If the block has other content (text, other lists), the list is part of a richer paragraph
  // and should not be pulled into adjacent blocks.
  if (currentBlock.content.length !== 1) return;

  // Check previous block: if its last content is a same-type ITEM_LIST, merge it in front.
  if (blockIndex > 0) {
    const prevBlock = blocks[blockIndex - 1];
    if (prevBlock.type === "PARAGRAPH" && prevBlock.content.length > 0) {
      const lastPrevContent = prevBlock.content[prevBlock.content.length - 1];
      if (isItemList(lastPrevContent) && effectiveListType(lastPrevContent) === listType) {
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
        // Direct mutation (not addElements): items from the prev list have parentId pointing to
        // the prev list's id, not curList.id, so they cannot be in curList.deletedItems — no
        // un-delete step is needed. addElements would also try to merge adjacent literals at the
        // item boundary, which does not apply to Item[].
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
      if (isItemList(firstNextContent) && effectiveListType(firstNextContent) === listType) {
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
        // Direct mutation — same reasoning as the unshift case above.
        curList.items.push(...(nextItems as ItemList["items"]));
        curList.deletedItems.push(...(nextDeletedItems as ItemList["deletedItems"]));
      }
    }
  }
};

/**
 * Converts a single list item back to normal paragraph text by removing it
 * from its list and placing its content in a dedicated paragraph block.
 *
 * To maintain the "one list per block, nothing else" invariant, the extracted
 * text and any remaining list halves are placed in separate blocks — never
 * inline in the same block as the remaining list.
 *
 * ID-preservation rule: the original block (with its template id) always stays
 * at its current index. New blocks (id: null) are inserted AFTER it. This
 * guarantees that if the user re-toggles the list on, mergeListWithAdjacentBlocks
 * will keep the lower-indexed block as the survivor, preserving the original id.
 *
 * Cases:
 *  - Empty list after removal: list removed, item content placed inline (no split needed).
 *  - First item: original block changes content to the extracted text; remaining
 *    list goes to a new block after (list id recorded in block.deletedContent).
 *  - Last item: original block keeps the list; extracted text goes to a new block after.
 *  - Middle item: original block keeps the first-half list; text block + second-half
 *    list block are inserted after.
 */
const toggleListOff = (draft: Draft<LetterEditorState>, literalIndex: ItemContentIndex) => {
  const blocks = draft.redigertBrev.blocks;
  const block = blocks[literalIndex.blockIndex];
  const blockIndex = literalIndex.blockIndex;
  const contentIndex = literalIndex.contentIndex;
  const itemIndex = literalIndex.itemIndex;
  const itemList = block.content[contentIndex] as ItemList;

  if (itemIndex < 0 || itemIndex >= itemList.items.length) return;

  draft.saveStatus = "DIRTY";

  // Remove the item from the list (records item.id in itemList.deletedItems).
  const itemContent = removeElements(itemIndex, 1, {
    content: itemList.items,
    deletedContent: itemList.deletedItems,
    id: itemList.id,
  }).flatMap((item) => item.content) as Content[];

  const isFirstItem = itemIndex === 0;
  const isLastItem = itemIndex === itemList.items.length; // length is now post-removal

  if (itemList.items.length === 0) {
    // List is now empty: remove it and place item content inline.
    removeElements(contentIndex, 1, { content: block.content, deletedContent: block.deletedContent, id: block.id });
    addElements(itemContent, contentIndex, block.content, block.deletedContent);
    draft.focus = {
      blockIndex,
      contentIndex: contentIndex + Math.min(itemContent.length - 1, literalIndex.itemContentIndex),
      cursorPosition: draft.focus.cursorPosition,
    };
    return;
  }

  if (isFirstItem) {
    // Original block BECOMES the text block; remaining list moves to a new block after.
    // Placing the original id at the lower index ensures id-preservation on re-merge.
    const remainingList = current(itemList) as ItemList;

    // Remove the list from the original block (records list.id in block.deletedContent —
    // split-persistence: prevents backend from re-introducing this list in block.id).
    removeElements(contentIndex, 1, { content: block.content, deletedContent: block.deletedContent, id: block.id });

    // Place item content at the same position (original block now holds text).
    addElements(itemContent, contentIndex, block.content, block.deletedContent);

    // New block (id: null) holds the remaining list.
    const listBlock = newParagraph({ content: [remainingList] });
    blocks.splice(blockIndex + 1, 0, listBlock);

    draft.focus = {
      blockIndex,
      contentIndex: contentIndex + Math.min(itemContent.length - 1, literalIndex.itemContentIndex),
      cursorPosition: draft.focus.cursorPosition,
    };
  } else if (isLastItem) {
    // Original block keeps the list; extracted text goes to a new block after.
    const textBlock = newParagraph({ content: itemContent });
    blocks.splice(blockIndex + 1, 0, textBlock);

    draft.focus = {
      blockIndex: blockIndex + 1,
      contentIndex: Math.min(itemContent.length - 1, literalIndex.itemContentIndex),
      cursorPosition: draft.focus.cursorPosition,
    };
  } else {
    // Middle item: original block keeps first-half list; text + second-half list follow.
    // removeElements marks the moved items as deleted in itemList.deletedItems (split-persistence).
    const itemsAfter = removeElements(itemIndex, itemList.items.length - itemIndex, {
      content: itemList.items,
      deletedContent: itemList.deletedItems,
      id: itemList.id,
    }) as unknown as Item[];

    const textBlock = newParagraph({ content: itemContent });
    const secondHalfBlock = newParagraph({
      content: [newItemList({ listType: effectiveListType(itemList), items: itemsAfter })],
    });
    blocks.splice(blockIndex + 1, 0, textBlock, secondHalfBlock);

    draft.focus = {
      blockIndex: blockIndex + 1,
      contentIndex: Math.min(itemContent.length - 1, literalIndex.itemContentIndex),
      cursorPosition: draft.focus.cursorPosition,
    };
  }
};
