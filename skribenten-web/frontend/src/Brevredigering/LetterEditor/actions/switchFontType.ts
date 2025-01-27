import type { Draft } from "immer";
import { produce } from "immer";
import { isEqual } from "lodash";

import type { Content, FontType, LiteralValue, ParagraphBlock, VariableValue } from "~/types/brevbakerTypes";
import { handleSwitchContent, handleSwitchTextContent, isItemContentIndex } from "~/utils/brevbakerUtils";

import type { Action } from "../lib/actions";
import type { LetterEditorState } from "../model/state";
import { getCursorOffset } from "../services/caretUtils";
import { newLiteral, newVariable } from "./common";
import type { LiteralIndex } from "./model";

export const switchFontType: Action<LetterEditorState, [literalIndex: LiteralIndex, fontType: FontType]> = produce(
  (draft, literalIndex, fontType) => {
    const block = draft.redigertBrev.blocks[literalIndex.blockIndex];

    if (block.type !== "PARAGRAPH") {
      return;
    }

    const contentBeforeTheLiteralWeAreOn = block.content.slice(0, literalIndex.contentIndex);
    const theContentWeAreOn = block.content[literalIndex.contentIndex];
    const contentAfterTheLiteralWeAreOn = block.content.slice(literalIndex.contentIndex + 1);

    handleSwitchContent({
      content: theContentWeAreOn,
      onLiteral: (literal) =>
        switchFontTypeForLiteral({
          draft: draft,
          thisBlock: block,
          literalIndex: literalIndex,
          contentBeforeTheLiteralWeAreOn: contentBeforeTheLiteralWeAreOn,
          contentAfterTheLiteralWeAreOn: contentAfterTheLiteralWeAreOn,
          literal: literal,
          fonttype: fontType,
        }),
      onVariable: (variable) =>
        switchFontTypeForVariable({ draft, thisBlock: block, literalIndex, fonttype: fontType, variable }),
      onNewLine: () => void 0,
      onItemList: (itemList) => {
        if (!isItemContentIndex(literalIndex)) {
          return;
        }
        const item = itemList.items[literalIndex.itemIndex].content[literalIndex.itemContentIndex];

        return handleSwitchTextContent({
          content: item,
          onLiteral: (literal) =>
            switchFontTypeForLiteral({
              draft: draft,
              thisBlock: block,
              literalIndex: literalIndex,
              contentBeforeTheLiteralWeAreOn: contentBeforeTheLiteralWeAreOn,
              contentAfterTheLiteralWeAreOn: contentAfterTheLiteralWeAreOn,
              literal: literal,
              fonttype: fontType,
            }),
          onVariable: (variable) =>
            switchFontTypeForVariable({ draft, thisBlock: block, literalIndex, fonttype: fontType, variable }),
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

  if (hasSelectionAndMarkedText) {
    switchFontTypeOfMarkedText(args);
  } else {
    switchFontTypeOfCurrentWord(args);
  }
};

const switchFontTypeForVariable = (args: {
  draft: Draft<LetterEditorState>;
  thisBlock: ParagraphBlock;
  literalIndex: LiteralIndex;
  fonttype: FontType;
  variable: Draft<VariableValue>;
}) => {
  const theNewVariable = newVariable({
    id: args.variable.id,
    text: args.variable.text,
    fontType: args.fonttype,
  });

  const newContent = [
    ...args.thisBlock.content.slice(0, args.literalIndex.contentIndex),
    theNewVariable,
    ...args.thisBlock.content.slice(args.literalIndex.contentIndex + 1),
  ];

  args.draft.redigertBrev.blocks[args.literalIndex.blockIndex].content = newContent;
};

const switchFontTypeOfMarkedText = (args: {
  draft: Draft<LetterEditorState>;
  thisBlock: ParagraphBlock;
  literalIndex: LiteralIndex;
  contentBeforeTheLiteralWeAreOn: Content[];
  contentAfterTheLiteralWeAreOn: Content[];
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
    ...args.contentBeforeTheLiteralWeAreOn,
    ...(hasTextBeforeTheSelection ? [newPreviousLiteral] : []),
    newThisLiteral,
    ...(hasTextAfterTheSelection ? [newNextLiteral] : []),
    ...args.contentAfterTheLiteralWeAreOn,
  ];

  const result: ParagraphBlock = {
    ...args.thisBlock,
    content: newContent,
    deletedContent: [...args.thisBlock.deletedContent, ...(args.literal.id ? [args.literal.id] : [])],
  };

  const newContentIndex = newContent.findIndex((c) => isEqual(c, newThisLiteral));

  args.draft.focus = {
    ...args.draft.focus,
    blockIndex: args.literalIndex.blockIndex,
    contentIndex: newContentIndex,
    cursorPosition: newThisLiteral.editedText?.length ?? 0,
  };
  args.draft.redigertBrev.blocks[args.literalIndex.blockIndex] = result;
};

/**
 * Eksempel case er når dem har caret i midten av et ord, og vil bytte fonttypen på dette ordet.
 */
const switchFontTypeOfCurrentWord = (args: {
  draft: Draft<LetterEditorState>;
  thisBlock: ParagraphBlock;
  literalIndex: LiteralIndex;
  contentBeforeTheLiteralWeAreOn: Content[];
  contentAfterTheLiteralWeAreOn: Content[];
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
    ...args.contentBeforeTheLiteralWeAreOn,
    ...(hasTextBeforeTheWord ? [newPreviousLiteral] : []),
    newThisLiteral,
    ...(hasTextAfterTheWord ? [newNextLiteral] : []),
    ...args.contentAfterTheLiteralWeAreOn,
  ];

  const result = {
    ...args.thisBlock,
    content: newContent,
    deletedContent: [...args.thisBlock.deletedContent, ...(args.literal.id ? [args.literal.id] : [])],
  };

  const newContentIndex = newContent.findIndex((c) => isEqual(c, newThisLiteral));
  args.draft.focus = {
    ...args.draft.focus,
    blockIndex: args.literalIndex.blockIndex,
    contentIndex: newContentIndex,
    cursorPosition: cursorPosition - wordStartPosition,
  };
  args.draft.redigertBrev.blocks[args.literalIndex.blockIndex] = result;
};
