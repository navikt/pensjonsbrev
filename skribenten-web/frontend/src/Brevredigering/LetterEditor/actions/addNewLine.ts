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
      const newFocusIdx = insertNewLineInContent(block.content, block.deletedContent, focus.contentIndex, offset);
      if (newFocusIdx !== null) {
        draft.focus = { blockIndex: focus.blockIndex, contentIndex: newFocusIdx, cursorPosition: 0 };
        draft.saveStatus = "DIRTY";
      }
    } else if (isItemList(content) && "itemIndex" in focus && offset !== undefined) {
      const itemFocus = focus as ItemContentIndex;
      const item = content.items[itemFocus.itemIndex];
      if (item) {
        const itemContent = item.content[itemFocus.itemContentIndex];
        if (itemContent?.type === LITERAL) {
          const newFocusIdx = insertNewLineInContent(
            item.content,
            item.deletedContent,
            itemFocus.itemContentIndex,
            offset,
          );
          if (newFocusIdx !== null) {
            draft.focus = {
              blockIndex: focus.blockIndex,
              contentIndex: focus.contentIndex,
              itemIndex: itemFocus.itemIndex,
              itemContentIndex: newFocusIdx,
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
 * Inserts a NEW_LINE at position `idx` within `content`, handling start/end/mid-literal cases.
 * Also inserts a guard literal when inserting at the boundary of the array or adjacent to a VARIABLE.
 * No-ops (returns null) if a NEW_LINE already neighbours the insertion point.
 *
 * Returns the index of the content after the inserted NEW_LINE (suitable for new focus),
 * or null if nothing was inserted.
 */
function insertNewLineInContent(
  content: Draft<Content[]>,
  deletedContent: Draft<number[]>,
  idx: number,
  offset: number,
): number | null {
  const literal = content[idx];
  if (!isTextContent(literal) || literal.type !== LITERAL) return null;

  const currentText = text(literal);
  const atStart = offset === 0 && currentText.length > 0;
  const atEnd = offset >= currentText.length;

  if (atStart) {
    if (content[idx - 1]?.type === NEW_LINE) return null;
    const isFirst = idx === 0;
    const toAdd = isFirst ? [newLiteral(), createNewLine()] : [createNewLine()];
    if (content[idx - 1]?.type === VARIABLE) toAdd.unshift(newLiteral());
    addElements(toAdd, idx, content, deletedContent);
    return idx + toAdd.length;
  } else if (atEnd) {
    if (content[idx + 1]?.type === NEW_LINE || content[idx - 1]?.type === NEW_LINE) return null;
    const isLast = idx + 1 === content.length;
    const toAdd = isLast ? [createNewLine(), newLiteral()] : [createNewLine()];
    if (content[idx + 1]?.type === VARIABLE) toAdd.push(newLiteral());
    addElements(toAdd, idx + 1, content, deletedContent);
    return idx + 2;
  } else {
    const splitLiteral = splitLiteralAtOffset(literal, offset);
    addElements([createNewLine(), splitLiteral], idx + 1, content, deletedContent);
    return idx + 2;
  }
}
