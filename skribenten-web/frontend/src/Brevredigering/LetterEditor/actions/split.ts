import { produce } from "immer";

import type { Item, ParagraphBlock } from "~/types/brevbakerTypes";
import { ITEM_LIST, LITERAL, PARAGRAPH } from "~/types/brevbakerTypes";

import type { Action } from "../lib/actions";
import type { LetterEditorState } from "../model/state";
import { isEmptyBlock, isEmptyContent, isEmptyItem } from "../model/utils";
import { cleanseText, isEditableContent } from "./common";
import type { LiteralIndex } from "./model";

function getId(id: number, isNew: boolean): number {
  return isNew ? -1 : id;
}

export const split: Action<LetterEditorState, [literalIndex: LiteralIndex, offset: number]> = produce(
  (draft, literalIndex, offset) => {
    const editedLetter = draft.renderedLetter.editedLetter;
    const block = editedLetter.blocks[literalIndex.blockIndex];
    const previousBlock = editedLetter.blocks[literalIndex.blockIndex - 1];
    const content = block.content[literalIndex.contentIndex];

    if (content.type === LITERAL) {
      if (!isEmptyBlock(block) && (previousBlock == null || offset > 0 || !isEmptyBlock(previousBlock))) {
        // Create next block
        const nextText = cleanseText(content.text.slice(Math.max(0, offset)));
        const isNew = nextText.length < 2 && block.content.length - literalIndex.contentIndex <= 1;

        const nextBlock: ParagraphBlock = {
          type: PARAGRAPH,
          id: -1, //getId(block.id, isNew),
          editable: true,
          content: [
            { ...content, id: getId(content.id, isNew), text: cleanseText(content.text.slice(Math.max(0, offset))) },
            ...block.content.slice(literalIndex.contentIndex + 1).map((c) => ({ ...c, id: getId(c.id, isNew) })),
          ],
        };
        editedLetter.blocks.splice(literalIndex.blockIndex + 1, 0, nextBlock);
        draft.focus = { contentIndex: 0, cursorPosition: 0, blockIndex: literalIndex.blockIndex + 1 };
        // Update existing
        content.text = cleanseText(content.text.slice(0, Math.max(0, offset)));
        block.content.splice(literalIndex.contentIndex + 1, block.content.length - literalIndex.contentIndex + 1);
        if (isEmptyContent(content) && isEditableContent(block.content[literalIndex.contentIndex - 1])) {
          // We don't want to leave an empty dangling literal if the previous content is editable
          block.content.splice(literalIndex.contentIndex, 1);
        }
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
            block.content.push({ type: LITERAL, id: -1, text: "" });
          }
          draft.focus = {
            blockIndex: literalIndex.blockIndex,
            contentIndex: literalIndex.contentIndex + 1,
            cursorPosition: 0,
          };
        } else {
          if (isEmptyItem(item)) {
            // An empty item would result in two consecutive empty items
            return;
          }

          const itemContent = item.content[literalIndex.itemContentIndex];

          const firstText = cleanseText(itemContent.text.slice(0, Math.max(0, offset)));
          const secondText = cleanseText(itemContent.text.slice(Math.max(0, offset)));

          const newItem: Item = {
            ...item,
            content: [{ ...itemContent, text: secondText }, ...item.content.slice(literalIndex.itemContentIndex + 1)],
          };

          // new and next item are both empty
          if (isEmptyItem(newItem) && nextItem != null && isEmptyItem(nextItem)) {
            return;
          }
          // previous item and current would both be empty
          if (
            previousItem != null &&
            isEmptyItem(previousItem) &&
            literalIndex.itemContentIndex === 0 &&
            offset === 0
          ) {
            return;
          }

          // insert new item after specified item
          content.items.splice(literalIndex.itemIndex + 1, 0, newItem);

          // split specified content
          item.content.splice(literalIndex.itemContentIndex + 1, item.content.length);
          itemContent.text = firstText;

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
    }
  },
);
