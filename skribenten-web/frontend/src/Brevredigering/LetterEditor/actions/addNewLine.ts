import { type Draft } from "immer";

import {
  addElements,
  createNewLine,
  isBlockContentIndex,
  isItemContentIndex,
  newLiteral,
  splitLiteralAtOffset,
  text,
} from "~/Brevredigering/LetterEditor/actions/common";
import { type Action, withPatches } from "~/Brevredigering/LetterEditor/lib/actions";
import { type Focus, type LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { isEmptyContent, isItemList, isLiteral, isParagraph } from "~/Brevredigering/LetterEditor/model/utils";
import { type Content, TITLE_INDEX } from "~/types/brevbakerTypes";

export const addNewLine: Action<LetterEditorState, [focus: Focus]> = withPatches((draft, focus) => {
  if (focus.blockIndex === TITLE_INDEX) return;

  const block = draft.redigertBrev.blocks[focus.blockIndex];

  if (isParagraph(block)) {
    const content = block.content[focus.contentIndex];
    const offset = focus.cursorPosition;

    if (offset === undefined) return;

    if (isLiteral(content) && isBlockContentIndex(focus)) {
      const newFocusIndex = insertNewLineInContent(block.content, block.deletedContent, focus.contentIndex, offset);
      if (newFocusIndex !== null) {
        draft.focus = { blockIndex: focus.blockIndex, contentIndex: newFocusIndex, cursorPosition: 0 };
        draft.saveStatus = "DIRTY";
      }
    } else if (isItemList(content) && isItemContentIndex(focus)) {
      const item = content.items[focus.itemIndex];
      if (item) {
        const itemContent = item.content[focus.itemContentIndex];
        if (isLiteral(itemContent)) {
          const newFocusIndex = insertNewLineInContent(
            item.content,
            item.deletedContent,
            focus.itemContentIndex,
            offset,
          );
          if (newFocusIndex !== null) {
            draft.focus = {
              blockIndex: focus.blockIndex,
              contentIndex: focus.contentIndex,
              itemIndex: focus.itemIndex,
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
 * Inserts a NEW_LINE at position `contentIndex` within `content`, handling start/end/mid-literal
 * cases. Also inserts a guard literal when inserting at the boundary of the array or adjacent to a
 * VARIABLE. No-ops (returns null) if a NEW_LINE already neighbours the insertion point.
 *
 * Returns the index of the content after the inserted NEW_LINE (suitable for new focus),
 * or null if nothing was inserted.
 */
function insertNewLineInContent(
  content: Draft<Content[]>,
  deletedContent: Draft<number[]>,
  contentIndex: number,
  offset: number,
): number | null {
  const literal = content[contentIndex];
  if (!isLiteral(literal)) return null;

  const nextContentType = content[contentIndex + 1]?.type;
  const previousContentType = content[contentIndex - 1]?.type;

  const atStartOfCurrent = offset === 0;
  const atEndOfCurrent = offset >= text(literal).length;

  if (isEmptyContent(literal)) {
    if (previousContentType === "NEW_LINE" || nextContentType === "NEW_LINE") return null;
    addElements([createNewLine()], contentIndex, content, deletedContent);
    return contentIndex + 1;
  } else if (atStartOfCurrent) {
    if (previousContentType === "NEW_LINE") return null;

    const isFirstInBlock = contentIndex === 0;
    const toAdd =
      isFirstInBlock || previousContentType === "VARIABLE" ? [newLiteral(), createNewLine()] : [createNewLine()];
    addElements(toAdd, contentIndex, content, deletedContent);
    return contentIndex + toAdd.length;
  } else if (atEndOfCurrent) {
    if (nextContentType === "NEW_LINE") return null;

    const isLastInBlock = contentIndex + 1 === content.length;
    const toAdd = isLastInBlock || nextContentType === "VARIABLE" ? [createNewLine(), newLiteral()] : [createNewLine()];
    addElements(toAdd, contentIndex + 1, content, deletedContent);
    return contentIndex + toAdd.length;
  } else {
    const splitLiteral = splitLiteralAtOffset(literal, offset);
    addElements([createNewLine(), splitLiteral], contentIndex + 1, content, deletedContent);
    return contentIndex + 2;
  }
}
