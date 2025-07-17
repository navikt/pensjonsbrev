import type { Draft } from "immer";
import { produce } from "immer";

import type {
  Cell,
  Content,
  FontType,
  ItemList,
  LiteralValue,
  ParagraphBlock,
  Row,
  Table,
  VariableValue,
} from "~/types/brevbakerTypes";
import { LITERAL } from "~/types/brevbakerTypes";
import { handleSwitchContent, handleSwitchTextContent } from "~/utils/brevbakerUtils";

import type { Action } from "../lib/actions";
import type { LetterEditorState, LiteralIndex } from "../model/state";
import { getCursorOffset } from "../services/caretUtils";
import { isItemContentIndex, newLiteral } from "./common";

// TODO: Denne bør skrives om til å gjenbruke funksjonalitet (addElements, removeElements, osv).
export const switchFontType: Action<LetterEditorState, [literalIndex: LiteralIndex, fontType: FontType]> = produce(
  (draft, literalIndex, fontType) => {
    const block = draft.redigertBrev.blocks[literalIndex.blockIndex];

    if (block.type !== "PARAGRAPH") {
      return;
    }

    const contentAtFocus = block.content[literalIndex.contentIndex];
    if (contentAtFocus?.type === "TABLE" && isItemContentIndex(literalIndex)) {
      const table = contentAtFocus as Draft<Table>;

      if (literalIndex.itemIndex === -1) {
        // itemIndex === -1 means header row
        const colSpec = table.header.colSpec[literalIndex.itemContentIndex];
        const headerLiteral = colSpec.headerContent.text.find((txt) => txt.type === LITERAL);

        if (headerLiteral) {
          headerLiteral.editedFontType = headerLiteral.editedFontType === fontType ? null : fontType;
        }
        draft.focus = { ...draft.focus, cursorPosition: 0 };
        draft.isDirty = true;
        return;
      }

      const row: Draft<Row> = table.rows[literalIndex.itemIndex];
      const cell: Draft<Cell> = row.cells[literalIndex.itemContentIndex];

      // we assume one literal per cell
      const literal = cell.text[0] as Draft<LiteralValue>;

      // Toggle the font: plain to bold/italic, keep existing italic/bold combo
      literal.editedFontType = literal.editedFontType === fontType ? null : fontType;

      draft.focus = { ...draft.focus, cursorPosition: 0 };
      draft.isDirty = true;
      return;
    }

    draft.isDirty = true;

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
        block.content = [
          ...contentBeforeTheLiteralWeAreOn,
          ...result.resultingContent,
          ...contentAfterTheLiteralWeAreOn,
        ];
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
  },
);

const switchFontTypeForLiteral = (args: {
  draft: Draft<LetterEditorState>;
  thisBlock: ParagraphBlock;
  literalIndex: LiteralIndex;
  contentBeforeTheLiteralWeAreOn: Content[];
  contentAfterTheLiteralWeAreOn: Content[];
  literal: Draft<LiteralValue>;
  fonttype: FontType;
}) => {
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

const switchFontTypeOfMarkedText = (args: {
  draft: Draft<LetterEditorState>;
  thisBlock: ParagraphBlock;
  literalIndex: LiteralIndex;
  literal: Draft<LiteralValue>;
  fonttype: FontType;
}) => {
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
  while (wordStartPosition > 0 && text[wordStartPosition - 1].trim() !== "") {
    wordStartPosition--;
  }

  let wordEndPosition = cursorPosition;
  while (wordEndPosition < text.length && text[wordEndPosition].trim() !== "") {
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
