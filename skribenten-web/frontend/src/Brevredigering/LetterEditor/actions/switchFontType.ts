import type { Draft, WritableDraft } from "immer";

import type {
  Cell,
  Content,
  ItemList,
  LiteralValue,
  ParagraphBlock,
  Row,
  TextContent,
  VariableValue,
} from "~/types/brevbakerTypes";
import { FontType, ITEM_LIST, LITERAL, NEW_LINE, TABLE, VARIABLE } from "~/types/brevbakerTypes";
import { handleSwitchContent, handleSwitchTextContent } from "~/utils/brevbakerUtils";

import { type Action, withPatches } from "../lib/actions";
import type { Focus, LetterEditorState, LiteralIndex, SelectionIndex } from "../model/state";
import { isItemList, isLiteral, isTableCellIndex, isTextContent } from "../model/utils";
import { getCursorOffset } from "../services/caretUtils";
import {
  fontTypeOf,
  isFirstBeforeAfter,
  isItemContentIndex,
  isMatchingFoci,
  isTable,
  isValidIndex,
  newLiteral,
  text,
} from "./common";

export const getCurrentActiveFontTypeAtFocus = (
  editorState: LetterEditorState,
  focus = editorState.focus,
): FontType => {
  const block = editorState.redigertBrev.blocks[focus.blockIndex];
  const blockContent = block?.content[focus.contentIndex];

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

// export const switchFontType: Action<LetterEditorState, [fontType: FontType]> = withPatches((draft, fontType) => {
export const switchFontType2: Action<
  Draft<LetterEditorState>,
  [selection: SelectionIndex | undefined, fontType: FontType]
> = withPatches(switchFontTypeRecipe);

export function switchFontTypeRecipe(
  draft: WritableDraft<LetterEditorState>,
  selection: SelectionIndex | undefined,
  fontType: FontType,
) {
  // - tillater kun en fonttype (normal, kursiv eller fet), aldri kursiv OG fet
  // - hvis målformatering er samme som eksisterende¹ settes den til normal
  // - ellers settes formatering til målformatering for hele markeringen
  //   [¹ eksisterende fonttype baseres på øverst venstre tegn (ref Word)]

  // TODO(stw) Husk å verifiser undo/redo fungerer, og preserver markering under undo/redo

  let start: Focus & { cursorPosition?: number };
  let end: Focus & { cursorPosition?: number };
  if (selection) {
    start = { ...selection.start };
    end = { ...selection.end };
  } else {
    start = draft.focus;
    end = draft.focus;
  }

  if (!isFirstBeforeAfter(start, end)) return;
  if (fontType === getCurrentActiveFontTypeAtFocus(draft, start)) {
    fontType = FontType.PLAIN;
  }
  draft.saveStatus = "DIRTY";

  const textContents: WritableDraft<TextContent>[] = [];
  if (start.blockIndex !== end.blockIndex) {
    // ikke samme block, så heller ikke samme content
    const blocksBetween = draft.redigertBrev.blocks.slice(start.blockIndex + 1, end.blockIndex);
    textContents.push(
      ...blocksBetween.flatMap((block) => {
        // Ikke endre fonttype på overskrifter
        if (block.type !== "PARAGRAPH") {
          return [] as TextContent[];
        }
        return block.content.flatMap((content) => {
          switch (content.type) {
            case ITEM_LIST:
              return content.items.flatMap((item) => item.content);
            case TABLE:
              return content.header.colSpec
                .flatMap((colSpec) => colSpec.headerContent.text)
                .concat(
                  content.rows.flatMap((row) => {
                    return row.cells.flatMap((cell) => cell.text);
                  }),
                );
            case LITERAL:
              return [content];
            case VARIABLE:
              // VARIABLE has a fontType property, let's test it
              return [content];
            case NEW_LINE:
              // NEW_LINE has no fontType property
              return [];
          }
        });
      }),
    );

    if (textContents.length > 0) {
      draft.saveStatus = "DIRTY";
    }
    // textContent may be empty [] at this point
    // betweens are handled, but also need to handle
    // - end of startBlock and start of endBlock from startIdx & split
    // - end of startContent and start of endContent until endIdx & split
  } else {
    if (start.contentIndex !== end.contentIndex) {
      // not same content, but still same block
    } else {
      // same content, could still be a sel of items/cells
    }
  }
  textContents.forEach((content) => {
    if (content.type === LITERAL) {
      content.editedFontType = fontType;
    }
  });

  // if start.block != end.block, change all inbetweens (may be 0)
  // then handle from start IN first block til end of block AND start of last block til end IN last block (split content)
  // if start.block == end.block, check content != content, change all inbetweens (may be 0)
  // then handle from start IN first content til end of content AND start of last content til end IN last content
  // if start.block == end.block && content == content, if

  // split if start isBlock && start.cIdx > 0 && content.type == LITERAL
  // split if start isItem && start.itemContentIdx > 0 && content.type == LITERAL
  // split if start isTable && start.cellContentIdx > 0 && content.type == LITERAL

  // split if end isBlock (i.e. is not item or table) && content.type == LITERAL && end.cIdx < 0 content.text.length
  // split if end isItem && content.items[].content.type == LITERAL && end.itemContentIdx < 0 content.text.length
  // split if end isTable && content.rows[].cells[].text[].type == LITERAL || content.header.colSPec.headerContent.text[].type == LITERAL && end.cellContentIdx < 0 content.text.length

  // transform between:
  // getLiteralsBetween(startIdx, endIdx).forEach((lit) => lit.editedFontType = fontType)
  // assume:
  // if start isBlock: contentIdx + 1
  // trans start.block.content.

  // generalisering av problemet
  // collectTextContentBetween(startIdx, endIdx) if selection
  // getTextContentAt(start)
  // if split, unshift splitted.part2 in front of collected
  // getTextContentAt(end)
  // if split, push splitted.part1 at end of collected
  // process collected, mark dirty, continue

  //avslutt med return
}

// TODO: Denne bør skrives om til å gjenbruke funksjonalitet (addElements, removeElements, osv).
export const switchFontType: Action<LetterEditorState, [fontType: FontType]> = withPatches((draft, fontType) => {
  const literalIndex = draft.focus;
  const block = draft.redigertBrev.blocks[literalIndex.blockIndex];

  if (block?.type !== "PARAGRAPH") {
    return;
  }

  if (fontType === getCurrentActiveFontTypeAtFocus(draft)) {
    fontType = FontType.PLAIN;
  }

  // håndter tabell
  const contentAtFocus = block.content[literalIndex.contentIndex];
  if (isTable(contentAtFocus) && isTableCellIndex(literalIndex)) {
    const table = contentAtFocus;

    const TABLE_HEADER_INDEX = -1;
    if (literalIndex.rowIndex === TABLE_HEADER_INDEX) {
      const colSpec = table.header.colSpec[literalIndex.cellIndex];
      const headerLiteral = colSpec.headerContent.text[literalIndex.cellContentIndex];

      if (isLiteral(headerLiteral)) {
        headerLiteral.editedFontType = headerLiteral.editedFontType === fontType ? null : fontType;
      }
      draft.focus = { ...draft.focus, cursorPosition: 0 };
      draft.saveStatus = "DIRTY";
      return;
    }

    const row: Draft<Row> = table.rows[literalIndex.rowIndex];
    const cell: Draft<Cell> = row.cells[literalIndex.cellIndex];

    const textContent = cell.text[literalIndex.cellContentIndex];

    if (isLiteral(textContent)) {
      textContent.editedFontType = textContent.editedFontType === fontType ? null : fontType;
    }

    draft.focus = { ...draft.focus, cursorPosition: 0 };
    draft.saveStatus = "DIRTY";
    return;
  }

  draft.saveStatus = "DIRTY";

  // antar her at vi opererer innenfor et enkelt segment (eff. TextContent)(blokk->segment->fragment)
  // denne antakelsen henger igjen fra da vi ikke kunne merke tekst på tvers av segment
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

      return handleSwitchContent({
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
        onItemList: () => void 0,
      });
    },
  });
});

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

  return hasSelectionAndMarkedText ? switchFontTypeOfSelectedText(args) : switchFontTypeOfCurrentWord(args);
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

const switchFontTypeOfSelectedText = (args: {
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
  // assumes selection within single literal
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
