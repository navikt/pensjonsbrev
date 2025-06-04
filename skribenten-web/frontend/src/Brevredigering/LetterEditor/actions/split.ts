import type { Draft } from "immer";
import { produce } from "immer";

import type { AnyBlock, Content, ItemList, TextContent } from "~/types/brevbakerTypes";
import { ITEM_LIST } from "~/types/brevbakerTypes";

import type { Action } from "../lib/actions";
import type { LetterEditorState, LiteralIndex } from "../model/state";
import { isEmptyBlock, isEmptyContent, isEmptyItem, isItemList, isLiteral, isVariable } from "../model/utils";
import {
  addElements,
  isAtStartOfBlock,
  newItem,
  newLiteral,
  newParagraph,
  removeElements,
  splitLiteralAtOffset,
  text,
} from "./common";

export const split: Action<LetterEditorState, [literalIndex: LiteralIndex, offset: number]> = produce(splitRecipe);

export function splitRecipe(draft: Draft<LetterEditorState>, literalIndex: LiteralIndex, offset: number) {
  const editedLetter = draft.redigertBrev;
  const block = editedLetter.blocks[literalIndex.blockIndex];
  const content = block.content[literalIndex.contentIndex];

  if (isLiteral(content)) {
    splitBlockAtLiteral(draft, literalIndex, offset, block);
  } else if (isItemList(content)) {
    splitItemList(draft, literalIndex, offset, block, content);
  } else {
    // eslint-disable-next-line no-console
    console.warn("Don't know how to split content with type: " + content.type);
  }
}

function splitBlockAtLiteral(
  draft: Draft<LetterEditorState>,
  literalIndex: LiteralIndex,
  offset: number,
  block: AnyBlock,
) {
  const editedLetter = draft.redigertBrev;
  const previousBlock = editedLetter.blocks[literalIndex.blockIndex - 1];
  const previousBlockIsNotEmpty = previousBlock && !isEmptyBlock(previousBlock);
  const isAtFirstBlock = literalIndex.blockIndex === 0;

  if (!isEmptyBlock(block) && (!isAtStartOfBlock(literalIndex, offset) || previousBlockIsNotEmpty || isAtFirstBlock)) {
    if (isAtStartOfBlock(literalIndex, offset)) {
      // Since we're at the very beginning of a block, it makes sense that we create a new block and push `block`
      // one position.
      addElements(
        [newParagraph({ content: [newLiteral({ text: "", editedText: "" })] })],
        literalIndex.blockIndex,
        editedLetter.blocks,
        editedLetter.deletedBlocks,
      );
    } else {
      // We're splitting a block somewhere inside it, so we modify `block` and move content after cursor to a new block.

      // Update content in `block` and get the content for the new (next) block
      const nextContent = splitContentArrayAtLiteral(
        { content: block.content, deletedContent: block.deletedContent, id: block.id },
        literalIndex.contentIndex,
        offset,
      );

      addElements(
        [newParagraph({ content: nextContent })],
        literalIndex.blockIndex + 1,
        editedLetter.blocks,
        editedLetter.deletedBlocks,
      );
    }

    draft.focus = { contentIndex: 0, cursorPosition: 0, blockIndex: literalIndex.blockIndex + 1 };
    draft.isDirty = true;
  }
}

function splitItemList(
  draft: Draft<LetterEditorState>,
  literalIndex: LiteralIndex,
  offset: number,
  block: AnyBlock,
  content: ItemList,
) {
  if ("itemIndex" in literalIndex) {
    const thisItem = content.items[literalIndex.itemIndex];
    const previousItem = content.items[literalIndex.itemIndex - 1];
    const nextItem = content.items[literalIndex.itemIndex + 1];

    const isAtLastItem = literalIndex.itemIndex === content.items.length - 1;
    if (isAtLastItem && isEmptyItem(thisItem)) {
      // We're at the last item, and it's empty, so the split should result in converting it to content in the same block after the ItemList (or move focus to ít).
      removeElements(literalIndex.itemIndex, 1, {
        content: content.items,
        deletedContent: content.deletedItems,
        id: content.id,
      });
      if (literalIndex.contentIndex >= block.content.length - 1) {
        addElements(
          [newLiteral({ text: "", editedText: "" })],
          block.content.length,
          block.content,
          block.deletedContent,
        );
      }
      draft.focus = {
        blockIndex: literalIndex.blockIndex,
        contentIndex: literalIndex.contentIndex + 1,
        cursorPosition: 0,
      };
    } else {
      // Validate that splitting should be performed
      const itemContent = thisItem.content[literalIndex.itemContentIndex];
      const previousItemIsEmpty = !!previousItem && isEmptyItem(previousItem);
      const nextItemIsEmpty = !!nextItem && isEmptyItem(nextItem);
      const splittingAtEndOfItem =
        literalIndex.itemContentIndex + 1 >= thisItem.content.length && offset >= text(itemContent).length;
      const splittingAtBeginningOfItem = literalIndex.itemContentIndex === 0 && offset === 0;

      if (
        !isLiteral(itemContent) ||
        isEmptyItem(thisItem) ||
        (nextItemIsEmpty && splittingAtEndOfItem) ||
        (previousItemIsEmpty && splittingAtBeginningOfItem)
      ) {
        // Will not split due to either:
        // - Cannot split non-literal
        // - Item splitting from is empty (prevent multiple consecutive empty items)
        // - Next item is empty (prevent multiple consecutive empty items)
        // - Previous item is empty (prevent multiple consecutive empty items)
        return;
      }

      if (literalIndex.itemContentIndex === 0 && offset === 0) {
        // We're at the very beginning of an item, so it makes sense to insert a new item before it instead of splitting
        const item = newItem({ content: [newLiteral({ text: "", editedText: "" })] });
        addElements([item], literalIndex.itemIndex, content.items, content.deletedItems);
      } else {
        // Update content of current item, and build content of new item
        const nextContent = splitContentArrayAtLiteral(
          { content: thisItem.content, deletedContent: thisItem.deletedContent, id: thisItem.id },
          literalIndex.itemContentIndex,
          offset,
        );
        const item = newItem({ content: nextContent });

        // insert new item after specified item
        addElements([item], literalIndex.itemIndex + 1, content.items, content.deletedItems);
      }

      // Update focus
      draft.focus = {
        blockIndex: literalIndex.blockIndex,
        contentIndex: literalIndex.contentIndex,
        cursorPosition: 0,
        itemIndex: literalIndex.itemIndex + 1,
        itemContentIndex: 0,
      };
    }
    draft.isDirty = true;
  } else {
    // eslint-disable-next-line no-console
    console.warn("Can't split an ItemList without itemIndex");
  }
}

function splitContentArrayAtLiteral<T extends Content | TextContent>(
  from: { content: Draft<T[]>; deletedContent: Draft<number[]>; id: number | null },
  atIndex: number,
  offset: number,
): Draft<T[]> {
  const content = from.content[atIndex];
  if (isLiteral(content)) {
    let contentAfterSplit;
    if (offset > 0) {
      const newLiteral = splitLiteralAtOffset(content, offset);

      contentAfterSplit = removeElements(atIndex + 1, from.content.length, from);
      if (text(newLiteral).length > 0 || isVariable(contentAfterSplit[0]) || contentAfterSplit.length === 0) {
        addElements([newLiteral as T], 0, contentAfterSplit, []);
      }
    } else {
      contentAfterSplit = removeElements(atIndex, from.content.length, from);
      addElements([newLiteral({ text: "", editedText: "" }) as T], from.content.length, from.content, []);
    }

    // prevent dangling empty content at end of from.content after itemList.
    const lastContent = from.content.at(-1);
    const secondToLastContent = from.content.at(-2);
    if (isLiteral(lastContent) && isEmptyContent(lastContent) && secondToLastContent?.type === ITEM_LIST) {
      from.content.pop();
    }

    return contentAfterSplit;
  } else {
    throw "Cannot split content array at non LiteralValue: " + content.type;
  }
}
