import { type Draft } from "immer";

import { type AnyBlock, type Content, type ItemList, type TextContent, TITLE_INDEX } from "~/types/brevbakerTypes";

import { type Action, withPatches } from "../lib/actions";
import { type ItemContentIndex, type LetterEditorState, type LiteralIndex } from "../model/state";
import { isEmptyBlock, isEmptyContent, isEmptyItem, isItemList, isLiteral, isVariable } from "../model/utils";
import {
  addElements,
  breakOutEmptyItem,
  isAtStartOfBlock,
  newItem,
  newLiteral,
  newParagraph,
  removeElements,
  splitLiteralAtOffset,
  text,
} from "./common";

export const split: Action<LetterEditorState, [literalIndex: LiteralIndex, offset: number]> = withPatches(splitRecipe);

export function splitRecipe(draft: Draft<LetterEditorState>, literalIndex: LiteralIndex, offset: number) {
  if (literalIndex.blockIndex === TITLE_INDEX) {
    return;
  }

  const block = draft.redigertBrev.blocks[literalIndex.blockIndex];
  const content = block?.content[literalIndex.contentIndex];

  if (isLiteral(content)) {
    splitBlockAtLiteral(draft, literalIndex, offset, block);
  } else if (isItemList(content)) {
    splitItemList(draft, literalIndex, offset, block, content);
  } else {
    console.warn(`Don't know how to split content with type: ${content.type}`);
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
        [newParagraph({ content: [newLiteral()] })],
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
    draft.saveStatus = "DIRTY";
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

    if (isEmptyItem(thisItem)) {
      // Empty item: break out of list into separate blocks with a blank line
      breakOutEmptyItem(draft, literalIndex as ItemContentIndex, content, block);
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
        (nextItemIsEmpty && splittingAtEndOfItem) ||
        (previousItemIsEmpty && splittingAtBeginningOfItem)
      ) {
        // Will not split due to either:
        // - Cannot split non-literal
        // - Next item is empty (prevent multiple consecutive empty items)
        // - Previous item is empty (prevent multiple consecutive empty items)
        return;
      }

      if (literalIndex.itemContentIndex === 0 && offset === 0) {
        // We're at the very beginning of an item, so it makes sense to insert a new item before it instead of splitting
        const item = newItem({ content: [newLiteral()] });
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
      draft.saveStatus = "DIRTY";
    }
  } else {
    console.warn("Can't split an ItemList without itemIndex");
  }
}

function splitContentArrayAtLiteral<T extends Content | TextContent>(
  from: { content: Draft<T[]>; deletedContent: Draft<number[]>; id?: number | null },
  atIndex: number,
  offset: number,
): Draft<T[]> {
  const content = from.content[atIndex];
  if (isLiteral(content)) {
    let contentAfterSplit: Draft<T[]>;
    if (offset > 0) {
      const newLiteral = splitLiteralAtOffset(content, offset);

      // removeElements marks the moved elements as deleted in from.deletedContent. This is
      // intentional: the "deleted" records in the source block prevent the backend from
      // re-introducing those template elements back into the source during re-merge. The
      // after-block is user-created (id: null) and kept verbatim by the backend. This is how
      // splits persist across fresh template renders (see letter-editor-actions SKILL.md).
      contentAfterSplit = removeElements(atIndex + 1, from.content.length, from);
      if (text(newLiteral).length > 0 || isVariable(contentAfterSplit[0]) || contentAfterSplit.length === 0) {
        addElements([newLiteral as T], 0, contentAfterSplit, []);
      }
    } else {
      contentAfterSplit = removeElements(atIndex, from.content.length, from);
      addElements([newLiteral() as T], from.content.length, from.content, []);
    }

    // prevent dangling empty content at end of from.content after itemList.
    const lastContent = from.content.at(-1);
    const secondToLastContent = from.content.at(-2);
    if (isLiteral(lastContent) && isEmptyContent(lastContent) && secondToLastContent?.type === "ITEM_LIST") {
      from.content.pop();
    }

    return contentAfterSplit;
  } else {
    throw `Cannot split content array at non LiteralValue: ${content.type}`;
  }
}
