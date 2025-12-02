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
import { fontTypeOf, isFirstBeforeAfter, isItemContentIndex, isTable, newLiteral, text } from "./common";

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

const getLiteralAtFocus = (focusedContent: Content, focus: Focus): LiteralValue | undefined => {
  if (["VARIABLE", "NEWLINE"].includes(focusedContent.type)) {
    // VARIABLE kan foreløpig ikke endres fontType på, siden den ikke har editedFontType
    // TODO(stw): NEWLINE har ikke fontType, så den må evt. erstattes med en tom LITERAL med fontType
    return;
  } else if (focusedContent.type === "ITEM_LIST" && isItemContentIndex(focus)) {
    const itemContent = focusedContent.items[focus.itemIndex].content[focus.itemContentIndex];
    return itemContent.type === "LITERAL" ? (itemContent as LiteralValue) : undefined;
  } else if (focusedContent.type === "TABLE" && isTableCellIndex(focus)) {
    if (focus.rowIndex < 0) {
      const itemContent = focusedContent.header.colSpec[focus.cellIndex].headerContent.text[focus.cellContentIndex];
      return itemContent.type === "LITERAL" ? (itemContent as LiteralValue) : undefined;
    } else {
      const itemContent = focusedContent.rows[focus.rowIndex].cells[focus.cellIndex].text[focus.cellContentIndex];
      return itemContent.type === "LITERAL" ? (itemContent as LiteralValue) : undefined;
    }
  } else if (focusedContent.type === "LITERAL") {
    return focusedContent;
  } else {
    return;
  }
};

const getWordBoundariesAtIndex = (text: string, position: number) => {
  let wordStartPosition = position;
  while (wordStartPosition > 0 && text[wordStartPosition - 1]?.trim() !== "") {
    wordStartPosition--;
  }
  let wordEndPosition = position;
  while (wordEndPosition < text.length && text[wordEndPosition]?.trim() !== "") {
    wordEndPosition++;
  }
  return [wordStartPosition, wordEndPosition];
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
  // - ellers settes formatering til målformatering for hele tekstmarkeringen
  //   [¹ eksisterende fonttype baseres på øverst venstre tegn (som i Word)]

  // TODO(stw) Husk å verifiser undo/redo fungerer, og preserver markering under undo/redo
  // TODO(stw) Sjekk markering som starter/slutter i overskrift eller utenfor redigeringsfelt

  // Sett start- og endeindeks for markering/fokus (hvis fokus i et ord markeres hele ordet)
  let start: Focus & { cursorPosition: number };
  let end: Focus & { cursorPosition: number };
  if (selection) {
    start = { ...selection.start };
    end = { ...selection.end };
  } else {
    // Hvis startIdx === endIdx: markør i ord (ikke markert) -> marker hele ordet
    const focusedTextContent = getLiteralAtFocus(
      draft.redigertBrev.blocks[draft.focus.blockIndex].content[draft.focus.contentIndex],
      draft.focus,
    );

    console.log("Target:", JSON.stringify(focusedTextContent, null, 2));
    // TODO(stw): Hvis vi ikke har textContent i fokus, er det noe som har gått galt (sjekk om fokus er på variabel?/newline?/utenfor redigeringsflate?)
    if (!focusedTextContent) return;

    // Fokusert textContent kan innholde fra 0 til mange ord. Hvilket av ordene har fokus?
    const [wordStartPosition, wordEndPosition] = getWordBoundariesAtIndex(
      text(focusedTextContent),
      draft.focus.cursorPosition ?? 0,
    );

    console.log("wordStart", wordStartPosition, "wordEnd", wordEndPosition);

    // Behandle fokusert ord som om det er markert
    start = { ...draft.focus, cursorPosition: wordStartPosition };
    end = { ...draft.focus, cursorPosition: wordEndPosition };
  }
  console.log(start, end);

  // TODO(stw): Sjekk guards som denne for å se om det bør settes feilmelding også
  // if (!isFirstBeforeAfter(start, end)) return;
  if (!isFirstBeforeAfter(start, end)) {
    console.log("first not before after");
    return;
  }

  if (fontType === getCurrentActiveFontTypeAtFocus(draft, start)) {
    fontType = FontType.PLAIN;
  }
  draft.saveStatus = "DIRTY";

  // splitte innhold?

  const textContents: WritableDraft<TextContent>[] = [];
  // Hvis start===end gjør denne seksjonen ingenting
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
            // VARIABLE has a fontType property, but no editedFontType
            return [content];
          case NEW_LINE:
            // NEW_LINE has neither fontType nor editedFontType
            return [];
        }
      });
    }),
  );
  // Her kan textContent være tom []
  console.log(textContents);

  // TODO(stw): Ta en gjennomgang og sjekk når saveStatus faktisk bør være DIRTY
  if (textContents.length > 0) {
    draft.saveStatus = "DIRTY";
  }

  // Mellom-blokker er håndtert (hvis noen), men må også håndtere
  // - fra startmarkør i startContent i startBlock til enden av siste content i startBlock
  // - fra starten av første content i endBlock til sluttmarkør i endContent i endBlock
  // - evt. splitting av content ved start-/sluttmarkør

  const firstContent = getLiteralAtFocus(
    draft.redigertBrev.blocks[start.blockIndex].content[start.contentIndex],
    start,
  );

  textContents.forEach((content) => {
    if (content.type === LITERAL) {
      content.editedFontType = fontType;
    }
  });

  // Fonttype på "tomme" felt
  // Hvis stat===end, er caretPos ikke i eller ved et ord, kan være mellom to mellomrom
  // I Word er det sånn at en markør mellom to mellomrom settes til bold mens videre input avventes,
  // men det settes tilbake til plain hvis man navigerer bort og tilbake
  // Man kan forøvrig nå samme tilstand ved å slette fet tekst og stå igjen med et fett mellomrom,
  // men da bør vi sjekke om det skal flettes, før vi sjekker om fonttype skal "glemmes"
  // Skal vi flette når vi navigerer bort?

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
