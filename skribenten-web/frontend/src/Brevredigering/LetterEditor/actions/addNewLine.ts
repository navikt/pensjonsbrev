import { type Draft } from "immer";

import {
  addElements,
  createNewLine,
  newLiteral,
  splitLiteralAtOffset,
  text,
} from "~/Brevredigering/LetterEditor/actions/common";
import { type Action, withPatches } from "~/Brevredigering/LetterEditor/lib/actions";
import { type Focus, type ItemContentIndex, type LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { isItemList, isParagraph, isTextContent } from "~/Brevredigering/LetterEditor/model/utils";
import { type Content, LITERAL, NEW_LINE, TITLE_INDEX, VARIABLE } from "~/types/brevbakerTypes";

export const addNewLine: Action<LetterEditorState, [focus: Focus]> = withPatches((draft, focus) => {
  if (focus.blockIndex === TITLE_INDEX) {
    return;
  }

  const block = draft.redigertBrev.blocks[focus.blockIndex];

  if (isParagraph(block)) {
    const content = block.content[focus.contentIndex];
    const offset = focus.cursorPosition;

    if (isTextContent(content) && content.type === LITERAL && !("itemIndex" in focus) && offset !== undefined) {
      const newFocusIndex = insertNewLineInContent(block.content, block.deletedContent, focus.contentIndex, offset);
      if (newFocusIndex !== null) {
        draft.focus = { blockIndex: focus.blockIndex, contentIndex: newFocusIndex, cursorPosition: 0 };
        draft.saveStatus = "DIRTY";
      }
    } else if (isItemList(content) && "itemIndex" in focus && offset !== undefined) {
      const itemFocus = focus as ItemContentIndex;
      const item = content.items[itemFocus.itemIndex];
      if (item) {
        const itemContent = item.content[itemFocus.itemContentIndex];
        if (itemContent?.type === LITERAL) {
          const newFocusIndex = insertNewLineInContent(
            item.content,
            item.deletedContent,
            itemFocus.itemContentIndex,
            offset,
          );
          if (newFocusIndex !== null) {
            draft.focus = {
              blockIndex: focus.blockIndex,
              contentIndex: focus.contentIndex,
              itemIndex: itemFocus.itemIndex,
              itemContentIndex: newFocusIndex,
              cursorPosition: 0,
            };
            draft.saveStatus = "DIRTY";
          }
        }
      }
    }
  }
});

/**
 * Inserts a NEW_LINE at position `index` within `content`, handling start/end/mid-literal cases.
 * Also inserts a guard literal when inserting at the boundary of the array or adjacent to a VARIABLE.
 * No-ops (returns null) if a NEW_LINE already neighbours the insertion point.
 *
 * Returns the index of the content after the inserted NEW_LINE (suitable for new focus),
 * or null if nothing was inserted.
 */
function insertNewLineInContent(
  content: Draft<Content[]>,
  deletedContent: Draft<number[]>,
  index: number,
  offset: number,
): number | null {
  const literal = content[index];
  if (!isTextContent(literal) || literal.type !== LITERAL) return null;

  const currentText = text(literal);
  const atStart = offset === 0 && currentText.length > 0;
  const atEnd = offset >= currentText.length;

  if (atStart) {
    if (content[index - 1]?.type === NEW_LINE) return null;
    const isFirst = index === 0;
    const toAdd = isFirst ? [newLiteral(), createNewLine()] : [createNewLine()];
    if (content[index - 1]?.type === VARIABLE) toAdd.unshift(newLiteral());
    addElements(toAdd, index, content, deletedContent);
    return index + toAdd.length;
  } else if (atEnd) {
    if (content[index + 1]?.type === NEW_LINE || content[index - 1]?.type === NEW_LINE) return null;
    const isLast = index + 1 === content.length;
    const toAdd = isLast ? [createNewLine(), newLiteral()] : [createNewLine()];
    if (content[index + 1]?.type === VARIABLE) toAdd.push(newLiteral());
    addElements(toAdd, index + 1, content, deletedContent);
    return index + 2;
  } else {
    const splitLiteral = splitLiteralAtOffset(literal, offset);
    addElements([createNewLine(), splitLiteral], index + 1, content, deletedContent);
    return index + 2;
  }
}
