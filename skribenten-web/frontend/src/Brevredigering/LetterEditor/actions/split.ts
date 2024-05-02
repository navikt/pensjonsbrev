import type { Draft } from "immer";
import { produce } from "immer";

import { updateLiteralText } from "~/Brevredigering/LetterEditor/actions/updateContentText";
import type { Content, Item, ParagraphBlock, TextContent } from "~/types/brevbakerTypes";
import { ITEM_LIST, LITERAL, PARAGRAPH } from "~/types/brevbakerTypes";

import type { Action } from "../lib/actions";
import type { LetterEditorState } from "../model/state";
import { isEmptyBlock, isEmptyItem } from "../model/utils";
import { cleanseText } from "./common";
import type { LiteralIndex } from "./model";

export const split: Action<LetterEditorState, [literalIndex: LiteralIndex, offset: number]> = produce(
  (draft, literalIndex, offset) => {
    const editedLetter = draft.renderedLetter.editedLetter;
    const block = editedLetter.blocks[literalIndex.blockIndex];
    const previousBlock = editedLetter.blocks[literalIndex.blockIndex - 1];
    const content = block.content[literalIndex.contentIndex];

    if (content.type === LITERAL) {
      // TODO: if split happens in the last content of a block and it results in an empty content, should it be removed?
      if (!isEmptyBlock(block) && (previousBlock == null || offset > 0 || !isEmptyBlock(previousBlock))) {
        // Create next block
        const nextContent = splitContentArrayAtLiteral(block.content, literalIndex.contentIndex, offset);

        const nextBlock: ParagraphBlock = {
          type: PARAGRAPH,
          id: null,
          editable: true,
          content: nextContent,
        };
        editedLetter.blocks.splice(literalIndex.blockIndex + 1, 0, nextBlock);
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
            block.content.push({ type: LITERAL, id: null, text: "", editedText: "" });
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
            offset >= (itemContent.editedText ?? itemContent.text).length
          ) {
            // next item is empty; prevent multiple consecutive empty items
            return;
          } else if (previousItem && isEmptyItem(previousItem) && literalIndex.itemContentIndex === 0 && offset === 0) {
            // previous item is empty; prevent multiple consecutive empty items
            return;
          }

          // Update content of current item, and build content of new item
          const nextContent = splitContentArrayAtLiteral(item.content, literalIndex.itemContentIndex, offset);

          const newItem: Item = {
            id: null,
            // content: [{ ...itemContent, text: secondText }, ...item.content.slice(literalIndex.itemContentIndex + 1)],
            content: nextContent,
          };

          // insert new item after specified item
          content.items.splice(literalIndex.itemIndex + 1, 0, newItem);

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
  },
);

function splitContentArrayAtLiteral<T extends Content | TextContent>(
  contentArray: Draft<T[]>,
  atIndex: number,
  offset: number,
): Draft<T>[] {
  const content = contentArray[atIndex];
  if (content.type === LITERAL) {
    const text = content.editedText ?? content.text;
    const nextText = cleanseText(text.slice(Math.max(0, offset)));
    const tailContent = [
      {
        ...content,
        id: offset > 0 ? null : content.id,
        text: "",
        editedText: nextText,
      },
      ...contentArray.slice(atIndex + 1),
    ];

    // Update existing
    updateLiteralText(content, cleanseText(text.slice(0, Math.max(0, offset))));
    contentArray.splice(atIndex + 1, contentArray.length - atIndex + 1);

    return tailContent;
  } else {
    throw "Cannot split content array at non LiteralValue: " + content.type;
  }
}
