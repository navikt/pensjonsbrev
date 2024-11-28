import { is } from "cypress/types/bluebird";
import type { Draft } from "immer";
import { produce } from "immer";

import type { FontType, LiteralValue, ParagraphBlock } from "~/types/brevbakerTypes";
import { handleSwitchContent, handleSwitchTextContent, isItemContentIndex } from "~/utils/brevbakerUtils";

import type { Action } from "../lib/actions";
import type { LetterEditorState } from "../model/state";
import { getCursorOffset } from "../services/caretUtils";
import { newLiteral } from "./common";
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

/**
 * Switcher fonttype på en literal som har hele/deler av teksten markert.
 */
const switchFontTypeOnLiteral = (
  literal: LiteralValue,
  newFonttype: FontType,
): { newLiterals: Draft<LiteralValue>[]; deletedContent: number[] } => {
  const selection = window.getSelection();

  //safe-guard i tilfelle det ikke er noen selection - selv om funksjonen skal kalles med markert tekst
  if (!selection) {
    return { newLiterals: [], deletedContent: [] };
  }
  const range = selection.getRangeAt(0);

  const textBeforeTheSelection = literal.text.slice(0, range.startOffset);
  const hasTextBeforeTheSelection = textBeforeTheSelection.length > 0;
  const textInSelection = literal.text.slice(range.startOffset, range.endOffset);
  const textAfterTheSelection = literal.text.slice(range.endOffset);
  const hasTextAfterTheSelection = textAfterTheSelection.length > 0;

  const newPreviousLiteral = newLiteral({
    text: literal.text,
    editedText: textBeforeTheSelection,
    editedFontType: literal.editedFontType,
  });
  const newThisLiteral = newLiteral({ text: literal.text, editedText: textInSelection, editedFontType: newFonttype });
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
  };
};

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
    onLiteral: (literal) => {
      const { newLiterals, deletedContent } = switchFontTypeOnLiteral(literal, fonttype);

      const newContent = [
        ...(hasContentBeforeTheLiteralWeAreOn ? contentBeforeTheLiteralWeAreOn : []),
        ...newLiterals,
        ...(hasContentAfterTheLiteralWeAreOn ? contentAfterTheLiteralWeAreOn : []),
      ];

      const result = {
        ...block,
        content: newContent,
        deletedContent: [...block.deletedContent, ...deletedContent],
      };

      return (draft.redigertBrev.blocks[literalIndex.blockIndex] = result);
    },
    onVariable: () => {
      return;
    },
    onItemList: (itemList) => {
      if (!isItemContentIndex(literalIndex)) {
        return;
      }
      const item = itemList.items[literalIndex.itemIndex].content[literalIndex.itemContentIndex];

      return handleSwitchTextContent({
        content: item,
        onLiteral: (literal) => {
          const { newLiterals, deletedContent } = switchFontTypeOnLiteral(literal, fonttype);

          const newContent = [
            ...(hasContentBeforeTheLiteralWeAreOn ? contentBeforeTheLiteralWeAreOn : []),
            ...newLiterals,
            ...(hasContentAfterTheLiteralWeAreOn ? contentAfterTheLiteralWeAreOn : []),
          ];

          const result = {
            ...block,
            content: newContent,
            deletedContent: [...block.deletedContent, ...deletedContent],
          };

          return (draft.redigertBrev.blocks[literalIndex.blockIndex] = result);
        },
        onVariable: () => {
          return;
        },
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
    onLiteral: (literal) => {
      const cursorPosition = getCursorOffset();
      console.log("the cursor position is:", cursorPosition);

      const text = literal.text;

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

      const newPreviousLiteral = newLiteral({
        text: text,
        editedText: textBeforeTheWord,
        editedFontType: literal.editedFontType,
      });
      const newThisLiteral = newLiteral({
        text: text,
        editedText: theWord,
        editedFontType: fonttype,
      });
      const newNextLiteral = newLiteral({
        text: text,
        editedText: textAfterTheWord,
        editedFontType: literal.editedFontType,
      });

      const newContent = [
        ...(hasContentBeforeTheLiteralWeAreOn ? contentBeforeTheLiteralWeAreOn : []),
        ...(hasTextBeforeTheWord ? [newPreviousLiteral] : []),
        newThisLiteral,
        ...(hasTextAfterTheWord ? [newNextLiteral] : []),
        ...(hasContentAfterTheLiteralWeAreOn ? contentAfterTheLiteralWeAreOn : []),
      ];

      const result = {
        ...block,
        content: newContent,
        deletedContent: [...block.deletedContent, ...(literal.id ? [literal.id] : [])],
      };

      draft.redigertBrev.blocks[literalIndex.blockIndex] = result;
    },
    onVariable: () => {
      console.log("variable");
    },
    onItemList: (itemList) => {
      if (!isItemContentIndex(literalIndex)) {
        return;
      }

      const item = itemList.items[literalIndex.itemIndex].content[literalIndex.itemContentIndex];
      handleSwitchTextContent({
        content: item,
        onLiteral: () => {
          console.log("TODO");
        },
        onVariable: () => {},
      });
    },
  });
};
