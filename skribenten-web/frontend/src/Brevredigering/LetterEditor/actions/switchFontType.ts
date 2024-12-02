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

    const hasMarkedText = (window.getSelection()?.toString().length ?? 0) > 0;

    if (hasMarkedText) {
      switchFontTypeOnMarkedText(draft, literalIndex, fontType);
    } else {
      switchFontTypeWithoutMarkedText(draft, literalIndex, fontType);
    }
  },
);

const switchFontTypeOnMarkedText = (
  draft: Draft<LetterEditorState>,
  literalIndex: LiteralIndex,
  fonttype: FontType,
) => {
  const block = draft.redigertBrev.blocks[literalIndex.blockIndex] as ParagraphBlock;

  const contentBeforeTheLiteralWeAreOn = block.content.slice(0, literalIndex.contentIndex);
  const hasContentBeforeTheLiteralWeAreOn = contentBeforeTheLiteralWeAreOn.length > 0;
  const theContentWeAreOn = block.content[literalIndex.contentIndex];
  const contentAfterTheLiteralWeAreOn = block.content.slice(literalIndex.contentIndex + 1);
  const hasContentAfterTheLiteralWeAreOn = contentAfterTheLiteralWeAreOn.length > 0;

  return handleSwitchContent({
    content: theContentWeAreOn,
    onLiteral: (literal) =>
      handleOnLiteralSwitchFontTypeWithSelection({
        draft,
        thisBlock: block,
        literalIndex,
        contentBeforeTheLiteralWeAreOn,
        hasContentBeforeTheLiteralWeAreOn,
        contentAfterTheLiteralWeAreOn,
        hasContentAfterTheLiteralWeAreOn,
        literal,
        fonttype,
      }),
    onVariable: (variable) => handleSwitchVariable({ draft, thisBlock: block, literalIndex, fonttype, variable }),
    onItemList: (itemList) => {
      if (!isItemContentIndex(literalIndex)) {
        return;
      }
      const item = itemList.items[literalIndex.itemIndex].content[literalIndex.itemContentIndex];

      return handleSwitchTextContent({
        content: item,
        onLiteral: (literal) =>
          handleOnLiteralSwitchFontTypeWithSelection({
            draft,
            thisBlock: block,
            literalIndex,
            contentBeforeTheLiteralWeAreOn,
            hasContentBeforeTheLiteralWeAreOn,
            contentAfterTheLiteralWeAreOn,
            hasContentAfterTheLiteralWeAreOn,
            literal,
            fonttype,
          }),
        onVariable: (variable) => handleSwitchVariable({ draft, thisBlock: block, literalIndex, fonttype, variable }),
      });
    },
  });
};

/**
 * Switcher fonttype på en literal uten at det er noe markert tekst.
 * Det vil si at markøren ligger inni et ord, eller på en tom plass, og man trykker på fonttype-knappen.
 * Hvis markøren ligger i et ord, skal hele ordet bytte fonttype - dersom den står på en tom plass, skjer ingenting
 */
const switchFontTypeWithoutMarkedText = (
  draft: Draft<LetterEditorState>,
  literalIndex: LiteralIndex,
  fonttype: FontType,
) => {
  const block = draft.redigertBrev.blocks[literalIndex.blockIndex] as ParagraphBlock;
  const contentBeforeTheLiteralWeAreOn = block.content.slice(0, literalIndex.contentIndex);
  const hasContentBeforeTheLiteralWeAreOn = contentBeforeTheLiteralWeAreOn.length > 0;
  const theContentWeAreOn = block.content[literalIndex.contentIndex];
  const contentAfterTheLiteralWeAreOn = block.content.slice(literalIndex.contentIndex + 1);
  const hasContentAfterTheLiteralWeAreOn = contentAfterTheLiteralWeAreOn.length > 0;

  handleSwitchContent({
    content: theContentWeAreOn,
    onLiteral: (literal) =>
      handleOnLiteralSwitchFontType({
        draft,
        thisBlock: block,
        literalIndex,
        contentBeforeTheLiteralWeAreOn,
        hasContentBeforeTheLiteralWeAreOn,
        contentAfterTheLiteralWeAreOn,
        hasContentAfterTheLiteralWeAreOn,
        literal,
        fonttype,
      }),
    onVariable: (variable) => handleSwitchVariable({ draft, thisBlock: block, literalIndex, fonttype, variable }),
    onItemList: (itemList) => {
      if (!isItemContentIndex(literalIndex)) {
        return;
      }
      const item = itemList.items[literalIndex.itemIndex].content[literalIndex.itemContentIndex];

      handleSwitchTextContent({
        content: item,
        onLiteral: (literal) =>
          handleOnLiteralSwitchFontType({
            draft,
            thisBlock: block,
            literalIndex,
            contentBeforeTheLiteralWeAreOn,
            hasContentBeforeTheLiteralWeAreOn,
            contentAfterTheLiteralWeAreOn,
            hasContentAfterTheLiteralWeAreOn,
            literal,
            fonttype,
          }),
        onVariable: (variable) => handleSwitchVariable({ draft, thisBlock: block, literalIndex, fonttype, variable }),
      });
    },
  });
};

const handleOnLiteralSwitchFontTypeWithSelection = (args: {
  draft: Draft<LetterEditorState>;
  thisBlock: ParagraphBlock;
  literalIndex: LiteralIndex;
  contentBeforeTheLiteralWeAreOn: Content[];
  hasContentBeforeTheLiteralWeAreOn: boolean;
  contentAfterTheLiteralWeAreOn: Content[];
  hasContentAfterTheLiteralWeAreOn: boolean;
  literal: Draft<LiteralValue>;
  fonttype: FontType;
}) => {
  const { newLiterals, deletedContent, updatedLiteralIndex } = switchFontTypeOnLiteralWithSelection(
    args.literal,
    args.fonttype,
  );

  const newContent = [
    ...(args.hasContentBeforeTheLiteralWeAreOn ? args.contentBeforeTheLiteralWeAreOn : []),
    ...newLiterals,
    ...(args.hasContentAfterTheLiteralWeAreOn ? args.contentAfterTheLiteralWeAreOn : []),
  ];

  const result: ParagraphBlock = {
    ...args.thisBlock,
    content: newContent,
    deletedContent: [...args.thisBlock.deletedContent, ...deletedContent],
  };

  const newContentIndex = newContent.findIndex((c) => isEqual(c, newLiterals[updatedLiteralIndex]));

  args.draft.focus = {
    ...args.draft.focus,
    blockIndex: args.literalIndex.blockIndex,
    contentIndex: newContentIndex,
    cursorPosition: newLiterals[updatedLiteralIndex].editedText?.length ?? 0,
  };
  args.draft.redigertBrev.blocks[args.literalIndex.blockIndex] = result;
};

const handleSwitchVariable = (args: {
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

/**
 * Switcher fonttype på en literal som har hele/deler av teksten markert.
 */
const switchFontTypeOnLiteralWithSelection = (
  literal: LiteralValue,
  newFonttype: FontType,
): { newLiterals: Draft<LiteralValue>[]; deletedContent: number[]; updatedLiteralIndex: number } => {
  const selection = window.getSelection();

  //safe-guard i tilfelle det ikke er noen selection - selv om funksjonen skal kalles med markert tekst
  if (!selection) {
    return { newLiterals: [], deletedContent: [], updatedLiteralIndex: 0 };
  }
  const range = selection.getRangeAt(0);

  const textBeforeTheSelection = (literal.editedText ?? literal.text).slice(0, range.startOffset);
  const hasTextBeforeTheSelection = textBeforeTheSelection.length > 0;
  const textInSelection = (literal.editedText ?? literal.text).slice(range.startOffset, range.endOffset);
  const textAfterTheSelection = (literal.editedText ?? literal.text).slice(range.endOffset);
  const hasTextAfterTheSelection = textAfterTheSelection.length > 0;

  const changesTheWholeLiteral = textBeforeTheSelection.length === 0 && textAfterTheSelection.length === 0;

  const newPreviousLiteral = newLiteral({
    text: literal.text,
    editedText: textBeforeTheSelection,
    editedFontType: literal.editedFontType,
  });
  const newThisLiteral = newLiteral({
    id: changesTheWholeLiteral ? literal.id : null,
    text: literal.text,
    editedText: changesTheWholeLiteral && literal.editedText === null ? null : textInSelection,
    editedFontType: newFonttype,
    tags: changesTheWholeLiteral ? literal.tags : [],
  });
  const newNextLiteral = newLiteral({
    text: literal.text,
    editedText: textAfterTheSelection,
    editedFontType: literal.editedFontType,
  });

  return {
    newLiterals: [
      ...(hasTextBeforeTheSelection ? [newPreviousLiteral] : []),
      newThisLiteral,
      ...(hasTextAfterTheSelection ? [newNextLiteral] : []),
    ],
    deletedContent: [...(literal.id ? [literal.id] : [])],
    updatedLiteralIndex: hasTextBeforeTheSelection ? 1 : 0,
  };
};

const handleOnLiteralSwitchFontType = (args: {
  draft: Draft<LetterEditorState>;
  thisBlock: ParagraphBlock;
  literalIndex: LiteralIndex;
  contentBeforeTheLiteralWeAreOn: Content[];
  hasContentBeforeTheLiteralWeAreOn: boolean;
  contentAfterTheLiteralWeAreOn: Content[];
  hasContentAfterTheLiteralWeAreOn: boolean;
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

  let start = cursorPosition;
  while (start > 0 && text[start - 1].trim() !== "") {
    start--;
  }

  // Find the end of the word
  let end = cursorPosition;
  while (end < text.length && text[end].trim() !== "") {
    end++;
  }

  const textBeforeTheWord = text.slice(0, start);
  const hasTextBeforeTheWord = textBeforeTheWord.length > 0;
  const theWord = text.slice(start, end);
  const textAfterTheWord = text.slice(end);
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
    ...(args.hasContentBeforeTheLiteralWeAreOn ? args.contentBeforeTheLiteralWeAreOn : []),
    ...(hasTextBeforeTheWord ? [newPreviousLiteral] : []),
    newThisLiteral,
    ...(hasTextAfterTheWord ? [newNextLiteral] : []),
    ...(args.hasContentAfterTheLiteralWeAreOn ? args.contentAfterTheLiteralWeAreOn : []),
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
    cursorPosition: cursorPosition - start,
  };
  args.draft.redigertBrev.blocks[args.literalIndex.blockIndex] = result;
};
