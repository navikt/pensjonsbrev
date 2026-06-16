import { current, type Draft } from "immer";

import { type Content, type Item, type ItemList, ListType } from "~/types/brevbakerTypes";

import { type Action, withPatches } from "../lib/actions";
import { type ItemContentIndex, type LetterEditorState, type LiteralIndex } from "../model/state";
import { effectiveListType, isItemList, isTextContent } from "../model/utils";
import {
  absorbListIntoList,
  addElements,
  coalesceAdjacentSameTypeLists,
  findAdjoiningContent,
  isItemContentIndex,
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
  } else if (isItemList(blockContent) && isItemContentIndex(literalIndex)) {
    if (effectiveListType(blockContent) === listType) {
      toggleListOff(draft, literalIndex as ItemContentIndex);
    } else {
      switchListType(draft, literalIndex, listType);
      const { newContentIndex, itemIndexOffset } = coalesceAdjacentSameTypeLists(
        draft,
        literalIndex.blockIndex,
        literalIndex.contentIndex,
        listType,
      );
      draft.focus = {
        blockIndex: literalIndex.blockIndex,
        contentIndex: newContentIndex,
        itemIndex: itemIndexOffset + literalIndex.itemIndex,
        itemContentIndex: (literalIndex as ItemContentIndex).itemContentIndex,
        cursorPosition: draft.focus.cursorPosition,
      };
      mergeListWithAdjacentBlocks(draft, literalIndex.blockIndex, newContentIndex, listType);
    }
  }
};

/**
 * Changes the listType of an existing list without altering its content or focus.
 */
const switchListType = (draft: Draft<LetterEditorState>, literalIndex: LiteralIndex, listType: ListType) => {
  const block = draft.redigertBrev.blocks[literalIndex.blockIndex];
  const itemList = block.content[literalIndex.contentIndex] as Draft<ItemList>;
  itemList.editedListType = listType !== itemList.listType ? listType : null;
  draft.saveStatus = "DIRTY";
};

/**
 * When creating a new list item, we must account for the possibility that it may be adjacent to an
 * existing list. It can be a list in the block before, after, or both. It can also be a list in the
 * same block; before, after, or both.
 *
 * This means that when we convert a literal to a list, we must merge with neighboring lists - if
 * they exist - in the same block or neighboring blocks.
 *
 * This merging can also affect the address of the focus.
 */
const toggleListOn = (draft: Draft<LetterEditorState>, literalIndex: LiteralIndex, listType: ListType) => {
  draft.saveStatus = "DIRTY";

  const block = draft.redigertBrev.blocks[literalIndex.blockIndex];
  const contentIndex = literalIndex.contentIndex;

  // move text into a new item list element
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

  // merge the new list with any same-type neighbours in this block
  const { newContentIndex, itemIndexOffset } = coalesceAdjacentSameTypeLists(
    draft,
    literalIndex.blockIndex,
    textIndex.startIndex,
    listType,
  );

  // update focus — the new item's position accounts for items from lists that preceded it
  draft.focus = {
    blockIndex: literalIndex.blockIndex,
    contentIndex: newContentIndex,
    itemIndex: itemIndexOffset,
    itemContentIndex: contentIndex - textIndex.startIndex,
    cursorPosition: draft.focus.cursorPosition,
  };
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
        const { movedItemCount, sourceBlockRemoved } = absorbListIntoList(
          draft,
          currentList,
          blockIndex - 1,
          prevBlock.content.length - 1,
          "front",
        );
        if (sourceBlockRemoved) blockIndex--;
        draft.focus.blockIndex = blockIndex;
        if ("itemIndex" in draft.focus) {
          draft.focus.itemIndex += movedItemCount;
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
        absorbListIntoList(draft, currentList, nextBlockIndex, 0, "back");
      }
    }
  }
};

/**
 * Converts a single list item back to normal paragraph text by removing it
 * from its list and placing its content in a dedicated paragraph block.
 *
 * The extracted text and any remaining list halves are placed in separate blocks.
 * New (id: null) blocks are inserted AFTER the original block, which keeps its
 * current index and id.
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
