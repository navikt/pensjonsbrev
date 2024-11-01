import type { Draft } from "immer";
import { produce } from "immer";

import { updateLiteralText } from "~/Brevredigering/LetterEditor/actions/updateContentText";
import type { Content, Item, LiteralValue, ParagraphBlock, TextContent } from "~/types/brevbakerTypes";
import { FontType, ITEM_LIST, LITERAL, VARIABLE } from "~/types/brevbakerTypes";

import type { Action } from "../lib/actions";
import type { LetterEditorState } from "../model/state";
import { isEmptyBlock, isEmptyContent, isEmptyItem } from "../model/utils";
import { cleanseText, newItem, newLiteral, newParagraph, text } from "./common";
import type { LiteralIndex } from "./model";

export const split: Action<LetterEditorState, [literalIndex: LiteralIndex, offset: number]> = produce(splitRecipe);

export function splitRecipe(draft: Draft<LetterEditorState>, literalIndex: LiteralIndex, offset: number) {
  const editedLetter = draft.redigertBrev;
  const block = editedLetter.blocks[literalIndex.blockIndex];
  const previousBlock = editedLetter.blocks[literalIndex.blockIndex - 1];
  const content = block.content[literalIndex.contentIndex];

  draft.isDirty = true;

  if (content.type === LITERAL) {
    const isAtStartOfBlock = literalIndex.contentIndex === 0 && offset === 0;
    const previousBlockIsNotEmpty = previousBlock && !isEmptyBlock(previousBlock);
    const isAtFirstBlock = literalIndex.blockIndex === 0;

    if (!isEmptyBlock(block) && (!isAtStartOfBlock || previousBlockIsNotEmpty || isAtFirstBlock)) {
      if (isAtStartOfBlock) {
        // Since we're at the very beginning of a block, it makes sense that we create a new block and push `block`
        // one position.
        const newBlock: ParagraphBlock = newParagraph(newLiteral(""));
        editedLetter.blocks.splice(literalIndex.blockIndex, 0, newBlock);
      } else {
        // We're splitting a block somewhere inside it, so we modify `block` and move content after cursor to a new block.

        // Update content in `block` and get the content for the new (next) block
        const nextContent = splitContentArrayAtLiteral(block.content, literalIndex.contentIndex, offset);

        // mark content moved to new block as deleted in original
        for (const c of nextContent) {
          if (c.id !== null) block.deletedContent.push(c.id);
        }

        const nextBlock: ParagraphBlock = newParagraph(...nextContent);
        editedLetter.blocks.splice(literalIndex.blockIndex + 1, 0, nextBlock);
      }

      draft.focus = { contentIndex: 0, cursorPosition: 0, blockIndex: literalIndex.blockIndex + 1 };
    }
  } else if (content.type === ITEM_LIST) {
    if ("itemIndex" in literalIndex) {
      const item = content.items[literalIndex.itemIndex];
      const previousItem = content.items[literalIndex.itemIndex - 1];
      const nextItem = content.items[literalIndex.itemIndex + 1];

      const isAtLastItem = literalIndex.itemIndex === content.items.length - 1;
      if (isAtLastItem && isEmptyItem(item)) {
        // We're at the last item, and it's empty, so the split should result in converting it to content in the same block after the ItemList (or move focus to ít).
        content.items.splice(literalIndex.itemIndex, 1);
        if (literalIndex.contentIndex >= block.content.length - 1) {
          block.content.push({
            type: LITERAL,
            id: null,
            text: "",
            editedText: "",
            fontType: FontType.PLAIN,
            editedFontType: null,
          });
        }
        draft.focus = {
          blockIndex: literalIndex.blockIndex,
          contentIndex: literalIndex.contentIndex + 1,
          cursorPosition: 0,
        };
      } else {
        // Validate that splitting should be performed
        const itemContent = item.content[literalIndex.itemContentIndex];
        if (itemContent.type !== LITERAL) {
          // cannot split non literal
          return;
        } else if (isEmptyItem(item)) {
          // Item splitting from is empty; prevent multiple consecutive empty items
          return;
        } else if (
          nextItem &&
          isEmptyItem(nextItem) &&
          literalIndex.itemContentIndex + 1 >= item.content.length &&
          offset >= text(itemContent).length
        ) {
          // next item is empty; prevent multiple consecutive empty items
          return;
        } else if (previousItem && isEmptyItem(previousItem) && literalIndex.itemContentIndex === 0 && offset === 0) {
          // previous item is empty; prevent multiple consecutive empty items
          return;
        }

        if (literalIndex.itemContentIndex === 0 && offset === 0) {
          // We're at the very beginning of an item, so it makes sense to insert a new item before it istead of splitting
          content.items.splice(literalIndex.itemIndex, 0, newItem(""));
        } else {
          // Update content of current item, and build content of new item
          const nextContent = splitContentArrayAtLiteral(item.content, literalIndex.itemContentIndex, offset);

          const newItem: Item = {
            id: null,
            content: nextContent,
          };

          // insert new item after specified item
          content.items.splice(literalIndex.itemIndex + 1, 0, newItem);
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
    } else {
      // eslint-disable-next-line no-console
      console.warn("Can't split an ItemList without itemIndex");
    }
  } else {
    // eslint-disable-next-line no-console
    console.warn("Don't know how to split content with type: " + content.type);
  }
}

function splitContentArrayAtLiteral<T extends Content | TextContent>(
  contentArray: Draft<T[]>,
  atIndex: number,
  offset: number,
): Draft<T>[] {
  const content = contentArray[atIndex];
  if (content.type === LITERAL) {
    const origText = text(content as LiteralValue);
    const nextText = cleanseText(origText.slice(Math.max(0, offset)));
    const contentAfterSplit = contentArray.slice(atIndex + 1);

    // Prevent that if we split at the end of a content we get an empty content as first element in the new block
    const nextContent =
      nextText.length === 0 && contentAfterSplit.length > 0 && contentAfterSplit[0].type !== VARIABLE
        ? contentAfterSplit
        : [
            {
              ...content,
              id: offset > 0 ? null : content.id,
              text: "",
              editedText: nextText,
            },
            ...contentArray.slice(atIndex + 1),
          ];

    // Update existing
    updateLiteralText(content, cleanseText(origText.slice(0, Math.max(0, offset))));
    contentArray.splice(atIndex + 1, contentArray.length - atIndex + 1);

    // prevent dangling empty content at end of contentArray after itemList.
    const lastContent = contentArray.at(-1);
    const secondToLastContent = contentArray.at(-2);
    if (lastContent?.type === LITERAL && isEmptyContent(lastContent) && secondToLastContent?.type === ITEM_LIST) {
      contentArray.pop();
    }

    return nextContent;
  } else {
    throw "Cannot split content array at non LiteralValue: " + content.type;
  }
}
