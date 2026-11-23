import { type Draft } from "immer";

import {
  type Cell,
  type Content,
  type EditedDocument,
  FontType,
  type ItemList,
  type LiteralValue,
  type ParagraphBlock,
  type Row,
  type TextContent,
  type VariableValue,
} from "~/types/brevbakerTypes";
import { handleSwitchContent, handleSwitchTextContent } from "~/utils/brevbakerUtils";

import { type Action, withPatches } from "../lib/actions";
import { type LetterEditorState, type LiteralIndex, type SelectionIndex } from "../model/state";
import { isItemList, isLiteral, isNewLine, isTableCellIndex, isTextContent } from "../model/utils";
import { getCursorOffset } from "../services/caretUtils";
import { fontTypeOf, isItemContentIndex, isTable, newLiteral, text } from "./common";

export const getCurrentActiveFontTypeAtCursor = (editorState: LetterEditorState): FontType => {
  const block = editorState.redigertBrev.blocks[editorState.focus.blockIndex];
  const focus = editorState.focus;
  const blockContent = block?.content[editorState.focus.contentIndex];

  let textContent: TextContent | undefined;

  if (isTable(blockContent) && isTableCellIndex(focus)) {
    const cell =
      focus.rowIndex === -1
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

/**
 * Formatting splits and rebuilds literals, so the selection is kept as character offsets within the text it is in
 * (a paragraph, list item or table cell) and mapped back onto the new literals. The selection is part of the undo
 * step, so undo and redo restore the selection that matches the text.
 */
export const switchFontType: Action<LetterEditorState, [fontType: FontType]> = withPatches((draft, fontType) => {
  const selection = draft.selection;
  const offsets = selection && {
    start: offsetInText(draft.redigertBrev, selection.start),
    end: offsetInText(draft.redigertBrev, selection.end),
  };

  if (!switchFontTypeRecipe(draft, fontType) || !selection || !offsets) return;

  const start =
    (offsets.start === undefined
      ? undefined
      : endpointAt(draft.redigertBrev, selection.start, offsets.start, "start")) ?? selection.start;
  const end =
    (offsets.end === undefined ? undefined : endpointAt(draft.redigertBrev, selection.end, offsets.end, "end")) ??
    selection.end;
  draft.selection = { start, end };
  draft.focus = { ...start };
});

type SelectionEndpoint = SelectionIndex["start"];

/** The content a literal index points into: the paragraph itself, a list item or a table cell. */
function textOf(document: EditedDocument, index: LiteralIndex): Content[] | undefined {
  const block = document.blocks[index.blockIndex];
  if (!block) return undefined;
  const content = block.content[index.contentIndex];
  if (isTableCellIndex(index)) {
    if (!isTable(content)) return undefined;
    const cell =
      index.rowIndex === -1
        ? content.header.colSpec[index.cellIndex]?.headerContent
        : content.rows[index.rowIndex]?.cells[index.cellIndex];
    return cell?.text;
  }
  if (isItemContentIndex(index)) {
    return isItemList(content) ? content.items[index.itemIndex]?.content : undefined;
  }
  return block.content;
}

function positionInText(index: LiteralIndex): number {
  if (isTableCellIndex(index)) return index.cellContentIndex;
  if (isItemContentIndex(index)) return index.itemContentIndex;
  return index.contentIndex;
}

function withPositionInText(index: LiteralIndex, position: number): LiteralIndex {
  if (isTableCellIndex(index)) return { ...index, cellContentIndex: position };
  if (isItemContentIndex(index)) return { ...index, itemContentIndex: position };
  return { ...index, contentIndex: position };
}

// Lists and tables inside a paragraph count as one character, so offsets on either side of them stay apart.
const lengthOf = (content: Content) => (isTextContent(content) && !isNewLine(content) ? text(content).length : 1);

function offsetInText(document: EditedDocument, endpoint: SelectionEndpoint): number | undefined {
  const content = textOf(document, endpoint);
  if (!content) return undefined;
  const preceding = content.slice(0, positionInText(endpoint)).reduce((sum, item) => sum + lengthOf(item), 0);
  return preceding + endpoint.cursorPosition;
}

/** The literal at `offset`: for a start, the literal that begins there; for an end, the literal that ends there. */
function endpointAt(
  document: EditedDocument,
  index: LiteralIndex,
  offset: number,
  side: "start" | "end",
): SelectionEndpoint | undefined {
  const content = textOf(document, index);
  if (!content) return undefined;

  let fallback: SelectionEndpoint | undefined;
  let start = 0;
  for (const [position, item] of content.entries()) {
    const end = start + lengthOf(item);
    if (isLiteral(item) && offset >= start && offset <= end) {
      const endpoint = { ...withPositionInText(index, position), cursorPosition: offset - start };
      const isPreferred = side === "start" ? offset < end : offset > start;
      if (isPreferred) return endpoint;
      fallback ??= endpoint;
    }
    start = end;
  }
  return fallback;
}

// TODO: Denne bør skrives om til å gjenbruke funksjonalitet (addElements, removeElements, osv).
/** Returns whether the letter was changed. */
function switchFontTypeRecipe(draft: Draft<LetterEditorState>, fontType: FontType): boolean {
  const literalIndex = draft.focus;
  const block = draft.redigertBrev.blocks[literalIndex.blockIndex];
  let changed = false;

  if (block?.type !== "PARAGRAPH") {
    return false;
  }

  if (fontType === getCurrentActiveFontTypeAtCursor(draft)) {
    fontType = FontType.PLAIN;
  }

  const contentAtFocus = block.content[literalIndex.contentIndex];
  if (isTable(contentAtFocus) && isTableCellIndex(literalIndex)) {
    const table = contentAtFocus;

    const TABLE_HEADER_INDEX = -1;
    if (literalIndex.rowIndex === TABLE_HEADER_INDEX) {
      const colSpec = table.header.colSpec[literalIndex.cellIndex];
      const headerLiteral = colSpec.headerContent.text[literalIndex.cellContentIndex];

      if (isLiteral(headerLiteral)) {
        headerLiteral.editedFontType = headerLiteral.editedFontType === fontType ? undefined : fontType;
      }
      draft.focus = { ...draft.focus, cursorPosition: 0 };
      draft.saveStatus = "DIRTY";
      return true;
    }

    const row: Draft<Row> = table.rows[literalIndex.rowIndex];
    const cell: Draft<Cell> = row.cells[literalIndex.cellIndex];

    const textContent = cell.text[literalIndex.cellContentIndex];

    if (isLiteral(textContent)) {
      textContent.editedFontType = textContent.editedFontType === fontType ? undefined : fontType;
    }

    draft.focus = { ...draft.focus, cursorPosition: 0 };
    draft.saveStatus = "DIRTY";
    return true;
  }

  draft.saveStatus = "DIRTY";

  const contentBeforeTheLiteralWeAreOn = block.content.slice(0, literalIndex.contentIndex);
  const theContentWeAreOn = block.content[literalIndex.contentIndex];
  const contentAfterTheLiteralWeAreOn = block.content.slice(literalIndex.contentIndex + 1);

  handleSwitchContent({
    content: theContentWeAreOn,
    onLiteral: (literal) => {
      const result = switchFontTypeForLiteral({
        draft: draft,
        thisBlock: block,
        literalIndex: literalIndex,
        contentBeforeTheLiteralWeAreOn: contentBeforeTheLiteralWeAreOn,
        contentAfterTheLiteralWeAreOn: contentAfterTheLiteralWeAreOn,
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
      };
      block.content = [...contentBeforeTheLiteralWeAreOn, ...result.resultingContent, ...contentAfterTheLiteralWeAreOn];
      changed = true;
      block.deletedContent = [...block.deletedContent, ...(result.deletedContent ? [result.deletedContent] : [])];
    },
    onVariable: (variable) => {
      const newVariable = switchFontTypeForVariable({
        fonttype: fontType,
        variable,
      });

      block.content = [...contentBeforeTheLiteralWeAreOn, newVariable, ...contentAfterTheLiteralWeAreOn];
      changed = true;
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
            draft: draft,
            thisBlock: block,
            literalIndex: literalIndex,
            contentBeforeTheLiteralWeAreOn: contentBeforeTheLiteralWeAreOn,
            contentAfterTheLiteralWeAreOn: contentAfterTheLiteralWeAreOn,
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
          changed = true;
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
          changed = true;
        },
        onNewLine: () => void 0,
      });
    },
  });
  return changed;
}

const matchesLiteralTarget = (a: LiteralIndex, b: LiteralIndex): boolean => {
  if (isItemContentIndex(a) && isItemContentIndex(b)) {
    return (
      a.blockIndex === b.blockIndex &&
      a.contentIndex === b.contentIndex &&
      a.itemIndex === b.itemIndex &&
      a.itemContentIndex === b.itemContentIndex
    );
  }
  return (
    !isItemContentIndex(a) &&
    !isItemContentIndex(b) &&
    !isTableCellIndex(a) &&
    !isTableCellIndex(b) &&
    a.blockIndex === b.blockIndex &&
    a.contentIndex === b.contentIndex
  );
};

const switchFontTypeForLiteral = (args: {
  draft: Draft<LetterEditorState>;
  thisBlock: ParagraphBlock;
  literalIndex: LiteralIndex;
  contentBeforeTheLiteralWeAreOn: Content[];
  contentAfterTheLiteralWeAreOn: Content[];
  literal: Draft<LiteralValue>;
  fonttype: FontType;
}) => {
  const modelSel = args.draft.selection;
  const hasModelMarkedText =
    !!modelSel &&
    matchesLiteralTarget(modelSel.start, args.literalIndex) &&
    matchesLiteralTarget(modelSel.end, args.literalIndex) &&
    modelSel.end.cursorPosition > modelSel.start.cursorPosition;

  const selection = globalThis.getSelection();
  const hasDomMarkedText = !modelSel && !!selection && (selection.toString?.()?.length ?? 0) > 0;

  return hasModelMarkedText || hasDomMarkedText
    ? switchFontTypeOfMarkedText(
        args,
        hasModelMarkedText
          ? { startOffset: modelSel.start.cursorPosition, endOffset: modelSel.end.cursorPosition }
          : undefined,
      )
    : switchFontTypeOfCurrentWord(args);
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

const switchFontTypeOfMarkedText = (
  args: {
    draft: Draft<LetterEditorState>;
    thisBlock: ParagraphBlock;
    literalIndex: LiteralIndex;
    literal: Draft<LiteralValue>;
    fonttype: FontType;
  },
  offsets?: { startOffset: number; endOffset: number },
) => {
  const resolvedOffsets =
    offsets ??
    (() => {
      const selection = globalThis.getSelection()!;
      const range = selection.getRangeAt(0);
      return { startOffset: range.startOffset, endOffset: range.endOffset };
    })();

  const textBeforeTheSelection = (args.literal.editedText ?? args.literal.text).slice(0, resolvedOffsets.startOffset);
  const hasTextBeforeTheSelection = textBeforeTheSelection.length > 0;
  const textInSelection = (args.literal.editedText ?? args.literal.text).slice(
    resolvedOffsets.startOffset,
    resolvedOffsets.endOffset,
  );
  const textAfterTheSelection = (args.literal.editedText ?? args.literal.text).slice(resolvedOffsets.endOffset);
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
    updatedCursorPosition: newThisLiteral.editedText?.length ?? 0,
    newThisLiteralPositionInNewContent: hasTextBeforeTheSelection ? 1 : 0,
    deletedContent: changesTheWholeLiteral ? null : args.literal.id,
  };
};

/**
 * Eksempel case er når dem har caret i midten av et ord, og vil bytte fonttypen på dette ordet.
 */
const switchFontTypeOfCurrentWord = (args: {
  draft: Draft<LetterEditorState>;
  thisBlock: ParagraphBlock;
  literalIndex: LiteralIndex;
  literal: Draft<LiteralValue>;
  fonttype: FontType;
}) => {
  const domCursorPosition = getCursorOffset();
  const cursorPosition =
    domCursorPosition >= 0
      ? domCursorPosition
      : args.draft.focus.cursorPosition !== undefined && args.draft.focus.cursorPosition >= 0
        ? args.draft.focus.cursorPosition
        : -1;

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
    newThisLiteralPositionInNewContent: hasTextBeforeTheWord ? 1 : 0,
    deletedContent: changesTheWholeLiteral ? null : args.literal.id,
  };
};
