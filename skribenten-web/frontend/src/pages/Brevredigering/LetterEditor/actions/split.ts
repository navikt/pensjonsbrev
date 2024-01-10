import { produce } from "immer";

import type { Item, ParagraphBlock } from "~/types/brevbakerTypes";
import { ITEM_LIST, LITERAL, PARAGRAPH } from "~/types/brevbakerTypes";

import type { Action } from "../lib/actions";
import type { LetterEditorState } from "../model/state";
import { isEmptyBlock, isEmptyContent, isEmptyItem } from "../model/utils";
import { cleanseText, isEditableContent } from "./common";
import type { ContentIndex } from "./model";

function getId(id: number, isNew: boolean): number {
  return isNew ? -1 : id;
}

export const split: Action<LetterEditorState, [contentIndex: ContentIndex, offset: number]> = produce(
  (draft, contentIndex, offset) => {
    const letter = draft.editedLetter.letter;
    const block = letter.blocks[contentIndex.blockIndex];
    const previousBlock = letter.blocks[contentIndex.blockIndex - 1];
    const content = block.content[contentIndex.contentIndex];

    if (content.type === LITERAL) {
      if (!isEmptyBlock(block) && (previousBlock == null || offset > 0 || !isEmptyBlock(previousBlock))) {
        // Create next block
        const nextText = cleanseText(content.text.slice(Math.max(0, offset)));
        const isNew = nextText.length < 2 && block.content.length - contentIndex.contentIndex <= 1;

        const nextBlock: ParagraphBlock = {
          type: PARAGRAPH,
          id: -1, //getId(block.id, isNew),
          editable: true,
          content: [
            { ...content, id: getId(content.id, isNew), text: cleanseText(content.text.slice(Math.max(0, offset))) },
            ...block.content.slice(contentIndex.contentIndex + 1).map((c) => ({ ...c, id: getId(c.id, isNew) })),
          ],
        };
        letter.blocks.splice(contentIndex.blockIndex + 1, 0, nextBlock);
        // draft.stealFocus[contentIndex.blockIndex + 1] = { contentIndex: 0, startOffset: 0 };
        draft.nextFocus = { contentIndex: 0, cursorPosition: 0, blockIndex: contentIndex.blockIndex + 1 };
        draft.currentBlock = contentIndex.blockIndex + 1;
        // Update existing
        content.text = cleanseText(content.text.slice(0, Math.max(0, offset)));
        block.content.splice(contentIndex.contentIndex + 1, block.content.length - contentIndex.contentIndex + 1);
        if (isEmptyContent(content) && isEditableContent(block.content[contentIndex.contentIndex - 1])) {
          // We don't want to leave an empty dangling literal if the previous content is editable
          block.content.splice(contentIndex.contentIndex, 1);
        }
      }
    } else if (content.type === ITEM_LIST) {
      if ("itemIndex" in contentIndex) {
        const item = content.items[contentIndex.itemIndex];
        const previousItem = content.items[contentIndex.itemIndex - 1];
        const nextItem = content.items[contentIndex.itemIndex + 1];

        if (contentIndex.itemIndex === content.items.length - 1 && isEmptyItem(item)) {
          // We're at the last item, and it's empty, so the split should result in converting it to content in the same block after the ItemList (or steal focus at ít).
          content.items.splice(contentIndex.itemIndex, 1);
          if (contentIndex.contentIndex >= block.content.length - 1) {
            block.content.push({ type: LITERAL, id: -1, text: "" });
          }
          // draft.stealFocus[contentIndex.blockIndex] = { contentIndex: contentIndex.contentIndex + 1, startOffset: 0 };
        } else {
          if (isEmptyItem(item)) {
            // An empty item would result in two consecutive empty items
            return;
          }

          const itemContent = item.content[contentIndex.itemContentIndex];

          const firstText = cleanseText(itemContent.text.slice(0, Math.max(0, offset)));
          const secondText = cleanseText(itemContent.text.slice(Math.max(0, offset)));

          // create new item
          const newItem: Item = {
            ...item,
            content: [{ ...itemContent, text: secondText }, ...item.content.slice(contentIndex.itemContentIndex + 1)],
          };

          // new and next item are both empty
          if (isEmptyItem(newItem) && nextItem != null && isEmptyItem(nextItem)) {
            return;
          }
          // previous item and current would both be empty
          if (
            previousItem != null &&
            isEmptyItem(previousItem) &&
            contentIndex.itemContentIndex === 0 &&
            offset === 0
          ) {
            return;
          }

          // insert new item after specified item
          content.items.splice(contentIndex.itemIndex + 1, 0, newItem);

          // split specified content
          item.content.splice(contentIndex.itemContentIndex + 1, item.content.length);
          itemContent.text = firstText;

          // steal focus
          // draft.stealFocus[contentIndex.blockIndex] = {
          //   contentIndex: contentIndex.contentIndex,
          //   startOffset: 0,
          //   item: {
          //     id: contentIndex.itemIndex + 1,
          //     contentIndex: 0,
          //   },
          // };
        }
      } else {
        // eslint-disable-next-line no-console
        console.warn("Can't split an ItemList without itemIndex");
      }
    }
  },
);
