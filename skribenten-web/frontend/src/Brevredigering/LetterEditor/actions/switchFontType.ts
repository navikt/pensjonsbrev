import { type Draft } from "immer";

import {
  type Cell,
  FontType,
  type ItemList,
  type LiteralValue,
  type TextContent,
  type VariableValue,
} from "~/types/brevbakerTypes";
import { handleSwitchContent, handleSwitchTextContent } from "~/utils/brevbakerUtils";

import { type Action, withPatches } from "../lib/actions";
import { type LetterEditorState } from "../model/state";
import { isItemList, isLiteral, isTableCellIndex, isTextContent } from "../model/utils";
import { getCursorOffset } from "../services/caretUtils";
import {
  fontTypeOf,
  isFocusInTableHeader,
  isItemContentIndex,
  isTable,
  newLiteral,
  TABLE_HEADER_ROW_INDEX,
} from "./common";

export const getCurrentActiveFontTypeAtCursor = (editorState: LetterEditorState): FontType => {
  const block = editorState.redigertBrev.blocks[editorState.focus.blockIndex];
  const focus = editorState.focus;
  const blockContent = block?.content[editorState.focus.contentIndex];

  let textContent: TextContent | undefined;

  if (isTable(blockContent) && isTableCellIndex(focus)) {
    const cell =
      focus.rowIndex === TABLE_HEADER_ROW_INDEX
        ? blockContent.header.colSpec[focus.cellIndex]?.headerContent
        : blockContent.rows[focus.rowIndex]?.cells[focus.cellIndex];

    textContent = cell?.text?.at(focus.cellContentIndex);
  } else if (isItemList(blockContent) && isItemContentIndex(focus)) {
    textContent = blockContent?.items[focus.itemIndex]?.content[focus.itemContentIndex];
  } else if (isTextContent(blockContent)) {
    textContent = blockContent;
  }

  return isTextContent(textContent) ? fontTypeOf(textContent) : FontType.PLAIN;
};

// TODO: Denne bør skrives om til å gjenbruke funksjonalitet (addElements, removeElements, osv).
export const switchFontType: Action<LetterEditorState, [fontType: FontType]> = withPatches((draft, fontType) => {
  const literalIndex = draft.focus;
  const block = draft.redigertBrev.blocks[literalIndex.blockIndex];

  if (block?.type !== "PARAGRAPH") {
    return;
  }

  // Tabelloverskrifter er allerede fete, så fet skrift er ikke tillatt der.
  if (fontType === FontType.BOLD && isFocusInTableHeader(draft)) {
    return;
  }

  if (fontType === getCurrentActiveFontTypeAtCursor(draft)) {
    fontType = FontType.PLAIN;
  }

  const contentAtFocus = block.content[literalIndex.contentIndex];
  if (isTable(contentAtFocus) && isTableCellIndex(literalIndex)) {
    const isHeader = literalIndex.rowIndex === TABLE_HEADER_ROW_INDEX;
    const cell: Draft<Cell> | undefined = isHeader
      ? contentAtFocus.header.colSpec[literalIndex.cellIndex]?.headerContent
      : contentAtFocus.rows[literalIndex.rowIndex]?.cells[literalIndex.cellIndex];
    const textContent = cell?.text[literalIndex.cellContentIndex];

    if (!cell || !isLiteral(textContent)) {
      return;
    }

    const result = switchFontTypeForLiteral({ literal: textContent, fonttype: fontType });
    if (!result) {
      return;
    }

    cell.text.splice(literalIndex.cellContentIndex, 1, ...result.resultingContent);
    if (typeof result.deletedContent === "number") {
      cell.deletedContent.push(result.deletedContent);
    }
    draft.focus = {
      ...literalIndex,
      cellContentIndex: literalIndex.cellContentIndex + result.newThisLiteralPositionInNewContent,
      cursorPosition: result.updatedCursorPosition,
      selectionStart: result.selectionStart,
    };
    draft.saveStatus = "DIRTY";
    return;
  }

  draft.saveStatus = "DIRTY";

  const contentBeforeTheLiteralWeAreOn = block.content.slice(0, literalIndex.contentIndex);
  const theContentWeAreOn = block.content[literalIndex.contentIndex];
  const contentAfterTheLiteralWeAreOn = block.content.slice(literalIndex.contentIndex + 1);

  handleSwitchContent({
    content: theContentWeAreOn,
    onLiteral: (literal) => {
      const result = switchFontTypeForLiteral({
        literal: literal,
        fonttype: fontType,
      });

      if (!result) {
        return;
      }

      draft.focus = {
        ...draft.focus,
        blockIndex: literalIndex.blockIndex,
        contentIndex:
          block.content.slice(0, literalIndex.contentIndex).length + result.newThisLiteralPositionInNewContent,
        cursorPosition: result.updatedCursorPosition,
        selectionStart: result.selectionStart,
      };
      block.content = [...contentBeforeTheLiteralWeAreOn, ...result.resultingContent, ...contentAfterTheLiteralWeAreOn];
      block.deletedContent = [...block.deletedContent, ...(result.deletedContent ? [result.deletedContent] : [])];
    },
    onVariable: (variable) => {
      const newVariable = switchFontTypeForVariable({
        fonttype: fontType,
        variable,
      });

      block.content = [...contentBeforeTheLiteralWeAreOn, newVariable, ...contentAfterTheLiteralWeAreOn];
    },
    onNewLine: () => void 0,
    onItemList: (itemList) => {
      if (!isItemContentIndex(literalIndex)) {
        return;
      }

      const item = itemList.items[literalIndex.itemIndex];
      const itemContent = item.content[literalIndex.itemContentIndex];

      return handleSwitchTextContent({
        content: itemContent,
        onLiteral: (literal) => {
          const result = switchFontTypeForLiteral({
            literal: literal,
            fonttype: fontType,
          });

          if (!result) {
            return;
          }

          const newItemContent = [
            ...item.content.slice(0, literalIndex.itemContentIndex),
            ...result.resultingContent,
            ...item.content.slice(literalIndex.itemContentIndex + 1),
          ];

          draft.focus = {
            ...draft.focus,
            blockIndex: literalIndex.blockIndex,
            cursorPosition: result.updatedCursorPosition,
            selectionStart: result.selectionStart,
            itemContentIndex:
              item.content.slice(0, literalIndex.itemContentIndex).length + result.newThisLiteralPositionInNewContent,
          };
          (
            draft.redigertBrev.blocks[literalIndex.blockIndex].content[literalIndex.contentIndex] as Draft<ItemList>
          ).items[literalIndex.itemIndex].content = newItemContent;
          (
            draft.redigertBrev.blocks[literalIndex.blockIndex].content[literalIndex.contentIndex] as Draft<ItemList>
          ).items[literalIndex.itemIndex].deletedContent = [
            ...item.deletedContent,
            ...(result.deletedContent ? [result.deletedContent] : []),
          ];
        },
        onVariable: (variable) => {
          const newVariable = switchFontTypeForVariable({
            fonttype: fontType,
            variable,
          });

          const newItemContent = [
            ...item.content.slice(0, literalIndex.itemContentIndex),
            newVariable,
            ...item.content.slice(literalIndex.itemContentIndex + 1),
          ];

          (
            draft.redigertBrev.blocks[literalIndex.blockIndex].content[literalIndex.contentIndex] as Draft<ItemList>
          ).items[literalIndex.itemIndex].content = newItemContent;
        },
        onNewLine: () => void 0,
      });
    },
  });
});

const switchFontTypeForLiteral = (args: { literal: Draft<LiteralValue>; fonttype: FontType }) => {
  const selection = globalThis.getSelection();

  const hasSelectionAndMarkedText = selection && (selection.toString?.()?.length ?? 0) > 0;

  return hasSelectionAndMarkedText ? switchFontTypeOfMarkedText(args) : switchFontTypeOfCurrentWord(args);
};

const switchFontTypeForVariable = (args: { fonttype: FontType; variable: Draft<VariableValue> }) => {
  return args.variable;
  //TODO: Implement this - her trenger vi editedFontType, på samme måte som literals
  // return newVariable({
  //   id: args.variable.id,
  //   text: args.variable.text,
  //   editedFontType: args.fonttype,
  // });
};

const switchFontTypeOfMarkedText = (args: { literal: Draft<LiteralValue>; fonttype: FontType }) => {
  const selection = globalThis.getSelection()!;
  const range = selection.getRangeAt(0);

  const textBeforeTheSelection = (args.literal.editedText ?? args.literal.text).slice(0, range.startOffset);
  const hasTextBeforeTheSelection = textBeforeTheSelection.length > 0;
  const textInSelection = (args.literal.editedText ?? args.literal.text).slice(range.startOffset, range.endOffset);
  const textAfterTheSelection = (args.literal.editedText ?? args.literal.text).slice(range.endOffset);
  const hasTextAfterTheSelection = textAfterTheSelection.length > 0;

  const changesTheWholeLiteral = textBeforeTheSelection.length === 0 && textAfterTheSelection.length === 0;

  const newPreviousLiteral = newLiteral({
    text: args.literal.text,
    editedText: textBeforeTheSelection,
    editedFontType: args.literal.editedFontType,
  });
  // INTENTIONAL ID COPY when changesTheWholeLiteral: the replacement literal takes the original's
  // id so the backend continues to track this element — the caller passes deletedContent: null in
  // this branch, meaning the original is NOT recorded as deleted. addElements will un-delete the
  // id if it was in deletedContent. Rule 1 exception: ID preservation during merge (see SKILL.md).
  const newThisLiteral = newLiteral({
    id: changesTheWholeLiteral ? args.literal.id : null,
    text: args.literal.text,
    editedText: changesTheWholeLiteral && args.literal.editedText === null ? null : textInSelection,
    editedFontType: args.fonttype,
    tags: changesTheWholeLiteral ? args.literal.tags : [],
  });
  const newNextLiteral = newLiteral({
    text: args.literal.text,
    editedText: textAfterTheSelection,
    editedFontType: args.literal.editedFontType,
  });

  const newContent = [
    ...(hasTextBeforeTheSelection ? [newPreviousLiteral] : []),
    newThisLiteral,
    ...(hasTextAfterTheSelection ? [newNextLiteral] : []),
  ];

  return {
    resultingContent: newContent,
    updatedCursorPosition: textInSelection.length,
    selectionStart: 0,
    newThisLiteralPositionInNewContent: hasTextBeforeTheSelection ? 1 : 0,
    deletedContent: changesTheWholeLiteral ? null : args.literal.id,
  };
};

/**
 * Eksempel case er når dem har caret i midten av et ord, og vil bytte fonttypen på dette ordet.
 */
const switchFontTypeOfCurrentWord = (args: { literal: Draft<LiteralValue>; fonttype: FontType }) => {
  const cursorPosition = getCursorOffset();

  if (cursorPosition < 0) {
    return;
  }

  const text = args.literal.editedText ?? args.literal.text;

  const isWhiteSpace = text[cursorPosition] === " ";

  if (isWhiteSpace) {
    return;
  }

  let wordStartPosition = cursorPosition;
  while (wordStartPosition > 0 && text[wordStartPosition - 1]?.trim() !== "") {
    wordStartPosition--;
  }

  let wordEndPosition = cursorPosition;
  while (wordEndPosition < text.length && text[wordEndPosition]?.trim() !== "") {
    wordEndPosition++;
  }

  const textBeforeTheWord = text.slice(0, wordStartPosition);
  const hasTextBeforeTheWord = textBeforeTheWord.length > 0;
  const theWord = text.slice(wordStartPosition, wordEndPosition);
  const textAfterTheWord = text.slice(wordEndPosition);
  const hasTextAfterTheWord = textAfterTheWord.length > 0;
  const changesTheWholeLiteral = textBeforeTheWord.length === 0 && textAfterTheWord.length === 0;

  const newPreviousLiteral = newLiteral({
    text: args.literal.text,
    editedText: textBeforeTheWord,
    editedFontType: args.literal.editedFontType,
  });
  // INTENTIONAL ID COPY — same rationale as in switchFontTypeOfMarkedText above.
  // Rule 1 exception: ID preservation during merge (see letter-editor-actions SKILL.md).
  const newThisLiteral = newLiteral({
    id: changesTheWholeLiteral ? args.literal.id : null,
    text: args.literal.text,
    editedText: changesTheWholeLiteral && args.literal.editedText === null ? null : theWord,
    editedFontType: args.fonttype,
  });
  const newNextLiteral = newLiteral({
    text: args.literal.text,
    editedText: textAfterTheWord,
    editedFontType: args.literal.editedFontType,
  });

  const newContent = [
    ...(hasTextBeforeTheWord ? [newPreviousLiteral] : []),
    newThisLiteral,
    ...(hasTextAfterTheWord ? [newNextLiteral] : []),
  ];

  return {
    resultingContent: newContent,
    updatedCursorPosition: cursorPosition - wordStartPosition,
    selectionStart: cursorPosition - wordStartPosition,
    newThisLiteralPositionInNewContent: hasTextBeforeTheWord ? 1 : 0,
    deletedContent: changesTheWholeLiteral ? null : args.literal.id,
  };
};
