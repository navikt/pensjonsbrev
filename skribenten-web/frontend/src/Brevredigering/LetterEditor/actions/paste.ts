import type { Draft } from "immer";
import { produce } from "immer";
import { isEqual } from "lodash";

import {
  addElements,
  findAdjoiningContent,
  isBlockContentIndex,
  isItemContentIndex,
  newItem,
  newItemList,
  newLiteral,
  newParagraph,
  removeElements,
  splitLiteralAtOffset,
  text,
} from "~/Brevredigering/LetterEditor/actions/common";
import { splitRecipe } from "~/Brevredigering/LetterEditor/actions/split";
import { updateLiteralText } from "~/Brevredigering/LetterEditor/actions/updateContentText";
import type { Action } from "~/Brevredigering/LetterEditor/lib/actions";
import type { Focus, LetterEditorState, LiteralIndex } from "~/Brevredigering/LetterEditor/model/state";
import type { AnyBlock, Item, ItemList, LiteralValue, ParagraphBlock, TextContent } from "~/types/brevbakerTypes";
import { ITEM_LIST, LITERAL, NEW_LINE, VARIABLE } from "~/types/brevbakerTypes";

import { isEmptyContent, isItemList, isLiteral, isTextContent, isVariable } from "../model/utils";

export const paste: Action<LetterEditorState, [literalIndex: LiteralIndex, offset: number, clipboard: DataTransfer]> =
  produce((draft, literalIndex, offset, clipboard) => {
    // Siden man limer inn der markøren står, så må focus være det samme som literalIndex (tester bryter typisk med denne forutsetningen).
    draft.focus = { ...literalIndex, cursorPosition: offset };

    if (clipboard.types.includes("text/html")) {
      insertHtmlClipboardInLetter(draft, literalIndex, offset, clipboard);
    } else if (clipboard.types.includes("text/plain")) {
      insertTextInLetter(draft, literalIndex, offset, clipboard.getData("text/plain"), false);
    } else {
      log("unsupported clipboard datatype(s) - " + JSON.stringify(clipboard.types));
    }
  });

export function logPastedClipboard(clipboardData: DataTransfer) {
  log("available paste types - " + JSON.stringify(clipboardData.types));
  log("pasted html content - " + clipboardData.getData("text/html"));
  log("pasted plain content - " + clipboardData.getData("text/plain"));
}

/**
 * Insert text in letter at specified index and offset.
 *
 * If multipartPaste is true then the function will try to do what is best in regard to preserving ids:
 * - will add a new literal instead of modifying current if offset is 0
 *
 * @param draft
 * @param literalIndex
 * @param offset
 * @param str text to insert
 * @param multipartPaste str is part of larger paste
 */
function insertTextInLetter(
  draft: Draft<LetterEditorState>,
  literalIndex: LiteralIndex,
  offset: number,
  str: string,
  multipartPaste: boolean = true,
) {
  const block = draft.redigertBrev.blocks[literalIndex.blockIndex];
  const content = block?.content[literalIndex.contentIndex];

  if (content?.type === ITEM_LIST && "itemContentIndex" in literalIndex) {
    const item = content.items[literalIndex.itemIndex];
    const itemContent = item?.content[literalIndex?.itemContentIndex];
    if (item && itemContent?.type === LITERAL) {
      if (isEmptyContent(itemContent) || offset > 0 || !multipartPaste) {
        insertText(itemContent, draft.focus, offset, str);
      } else {
        const newIndex = offset <= 0 ? literalIndex.itemContentIndex : literalIndex.itemContentIndex + 1;
        addElements([newLiteral({ text: "", editedText: str })], newIndex, item.content, item.deletedContent);
        draft.focus = { ...literalIndex, itemContentIndex: newIndex, cursorPosition: str.length };
      }
    } else {
      log("cannot insert text into variable - " + str);
    }
  } else if (block && content?.type === LITERAL && !("itemContentIndex" in literalIndex)) {
    if (isEmptyContent(content) || offset > 0 || !multipartPaste) {
      insertText(content, draft.focus, offset, str);
    } else {
      const newIndex = offset <= 0 ? literalIndex.contentIndex : literalIndex.contentIndex + 1;
      addElements([newLiteral({ text: "", editedText: str })], newIndex, block.content, block.deletedContent);
      draft.focus = { ...literalIndex, contentIndex: newIndex, cursorPosition: str.length };
    }
  } else {
    log("literalIndex is invalid " + JSON.stringify(literalIndex));
    log("text - " + str);
  }
}

function insertText(draft: Draft<LiteralValue>, focus: Draft<Focus>, offset: number, str: string) {
  const existingText = text(draft);
  const safeOffset = Math.min(Math.max(0, offset), existingText.length);

  const newText = existingText.slice(0, safeOffset) + str + existingText.slice(safeOffset);
  updateLiteralText(draft, newText);
  focus.cursorPosition = safeOffset + str.length;

  // if (offset <= 0) {
  //   updateLiteralText(draft, str + existingText);
  //   focus.cursorPosition = str.length;
  // } else if (offset >= existingText.length) {
  //   updateLiteralText(draft, existingText + str);
  //   focus.cursorPosition = existingText.length + str.length;
  // } else {
  //   const text = existingText.slice(0, Math.max(0, offset)) + str + existingText.slice(Math.max(0, offset));
  //   updateLiteralText(draft, text);
  //   focus.cursorPosition = text.length;
  // }
}
/**
 * Pasting funksjonalitet skal etterligne hvordan det fungerer i microsoft word.
 * Merk da at det er forskjellige regler hvis man limer inn i start/midten/slutt, på en literal, eller punktliste,
 * og om det kopierte innholdet er bare literal eller punktliste, eller begge.
 *
 * TODO - vi har util funksjoner som tar seg hånd om splitting av blokker/content, etc. Vi burde helst bruke disse enn å gjøre
 * det manuelt som her.
 */
function insertHtmlClipboardInLetter(
  draft: Draft<LetterEditorState>,
  literalIndex: LiteralIndex,
  offset: number,
  clipboard: DataTransfer,
) {
  const parser = new DOMParser();
  const document = parser.parseFromString(clipboard.getData("text/html"), "text/html");
  const parsedAndCombinedHtml = parseAndCombineHTML(document.body);
  const isInsertingOnlyWords = parsedAndCombinedHtml[0].tag === "SPAN";

  if (parsedAndCombinedHtml.length === 0) {
    //trenger ikke å lime inn tomt innhold
    return;
  } else if (isInsertingOnlyWords) {
    insertTextInLetter(draft, literalIndex, offset, parsedAndCombinedHtml[0].content.join(" "), false);
  } else {
    insertTraversedElements(draft, literalIndex, offset, parsedAndCombinedHtml);
  }

  // const thisBlock = draft.redigertBrev.blocks[literalIndex.blockIndex];
  // const thisContent = thisBlock.content[literalIndex.contentIndex];

  //
  //
  // handleSwitchContent({
  //   content: thisContent,
  //   onVariable: () => {
  //     throw new Error("Cannot paste into variable");
  //   },
  //   onNewLine: () => {
  //     //TODO - newLine er en spesiell type content. Skal vi støtte paste inn i newLine?
  //     throw new Error("Cannot paste into newline");
  //   },
  //   onLiteral: (literalToBePastedInto) => {
  //     const result = pasteIntoLiteral(
  //       draft,
  //       literalIndex,
  //       thisBlock,
  //       offset,
  //       literalToBePastedInto,
  //       parsedAndCombinedHtml,
  //     );
  //
  //     if (result !== undefined) {
  //       const newBlocks = [
  //         ...draft.redigertBrev.blocks.slice(0, literalIndex.blockIndex),
  //         ...result.replaceThisBlockWith,
  //         ...draft.redigertBrev.blocks.slice(literalIndex.blockIndex + 1),
  //       ];
  //       draft.redigertBrev.blocks = newBlocks;
  //       draft.focus = { ...result.updatedFocus };
  //     }
  //     draft.isDirty = true;
  //   },
  //   onItemList: (itemList) => {
  //     if (!isItemContentIndex(literalIndex)) {
  //       return;
  //     }
  //
  //     const item = itemList.items[literalIndex.itemIndex];
  //     const literalToBePastedInto = item.content[literalIndex.itemContentIndex] as LiteralValue;
  //     //Hvis første elementet er en span, så vet vi at det ikke finnes flere elementer enn det ene.
  //     const isInsertingOnlyWords = parsedAndCombinedHtml[0].tag === "SPAN";
  //
  //     const result = isInsertingOnlyWords
  //       ? insertSpanElementsIntoLiteral(literalIndex, draft, literalToBePastedInto, parsedAndCombinedHtml[0], offset)
  //       : insertNonSpanElementsIntoItemList(
  //           literalIndex,
  //           thisBlock,
  //           itemList,
  //           item,
  //           literalToBePastedInto,
  //           parsedAndCombinedHtml,
  //           offset,
  //         );
  //
  //     if (result !== undefined) {
  //       const { replaceThisBlockWith, updatedFocus } = result;
  //       const newBlocks = [
  //         ...draft.redigertBrev.blocks.slice(0, literalIndex.blockIndex),
  //         ...replaceThisBlockWith,
  //         ...draft.redigertBrev.blocks.slice(literalIndex.blockIndex + 1),
  //       ];
  //       draft.redigertBrev.blocks = newBlocks;
  //       draft.focus = { ...updatedFocus };
  //     }
  //     draft.isDirty = true;
  //   },
  // });
}

interface UpdatedBlocksAndFocus {
  replaceThisBlockWith: ParagraphBlock[];
  updatedFocus: Focus;
}

const pasteIntoLiteral = (
  draft: Draft<LetterEditorState>,
  literalIndex: LiteralIndex,
  thisBlock: Draft<AnyBlock>,
  offset: number,
  literalToBePastedInto: Draft<LiteralValue>,
  parsedAndCombinedHtml: TraversedElement[],
): UpdatedBlocksAndFocus | undefined => {
  //Hvis første elementet er en span, så vet vi at det ikke finnes flere elementer enn det ene.
  const isInsertingOnlyWords = parsedAndCombinedHtml[0].tag === "SPAN";

  if (isInsertingOnlyWords) {
    insertSpanElementsIntoLiteral(literalIndex, draft, literalToBePastedInto, parsedAndCombinedHtml[0], offset);
    return undefined;
  }

  return insertNonSpanElementsIntoLiteral(
    draft,
    literalIndex,
    thisBlock,
    literalToBePastedInto,
    parsedAndCombinedHtml,
    offset,
  );
};

function insertSpanElementsIntoLiteral(
  literalIndex: LiteralIndex,
  draft: Draft<LetterEditorState>,
  literalToBePastedInto: Draft<LiteralValue>,
  traversedElement: TraversedElement,
  offset: number,
) {
  if (traversedElement.tag !== "SPAN") {
    throw new Error("Expected traversed element to be a span element to insert at the start of literal");
  }

  const literalText = text(literalToBePastedInto);
  const combinedSpanText = traversedElement.content.join(" ");

  const newText = literalText.slice(0, offset) + combinedSpanText + literalText.slice(offset);
  const cursorPosition = offset + combinedSpanText.length;

  updateLiteralText(literalToBePastedInto, newText);
  draft.focus = { ...literalIndex, cursorPosition };
}

const insertNonSpanElementsIntoLiteral = (
  draft: Draft<LetterEditorState>,
  literalIndex: LiteralIndex,
  thisBlock: Draft<AnyBlock>,
  literalToBePastedInto: LiteralValue,
  parsedAndCombinedHtml: TraversedElement[],
  offset: number,
) => {
  const appendingToStartOfLiteral = offset === 0;
  const appendingToMiddleOfLiteral = offset > 0 && offset < text(literalToBePastedInto).length;

  insertTraversedElements(draft, literalIndex, offset, parsedAndCombinedHtml);
  return undefined;
  // if (appendingToStartOfLiteral) {
  //   insertTraversedElements(draft, literalIndex, offset, parsedAndCombinedHtml);
  //   return undefined;
  //   // return insertTextAtStartOfLiteral(draft, literalIndex, thisBlock, literalToBePastedInto, parsedAndCombinedHtml);
  // } else if (appendingToMiddleOfLiteral) {
  //   return insertTextInTheMiddleOfLiteral(
  //     draft,
  //     literalIndex,
  //     thisBlock,
  //     literalToBePastedInto,
  //     parsedAndCombinedHtml,
  //     offset,
  //   );
  // } else {
  //   return insertTextAtEndOfLiteral(
  //     draft,
  //     literalIndex,
  //     thisBlock,
  //     literalToBePastedInto,
  //     parsedAndCombinedHtml,
  //     offset,
  //   );
  // }
};

const insertNonSpanElementsIntoItemList = (
  literalIndex: LiteralIndex,
  thisBlock: Draft<AnyBlock>,
  itemList: Draft<ItemList>,
  item: Draft<Item>,
  literalToBePastedInto: LiteralValue,
  parsedAndCombinedHtml: TraversedElement[],
  offset: number,
) => {
  if (!isItemContentIndex(literalIndex)) {
    throw new Error("Expected literalIndex to be an item content index");
  }

  const mappedToBrevbaker = traversedElementsToBrevbaker(parsedAndCombinedHtml);
  //vi bryr oss kun om det første elementet siden den skal spesial håndteres, resten blir
  //konvertert til items, fra det originale formatet
  const firstMapped = mappedToBrevbaker[0];

  const literalText = literalToBePastedInto.editedText ?? literalToBePastedInto.text;
  const insertingAtStart = offset === 0;
  const insertingInTheMiddle = offset > 0 && offset < literalText.length;

  const contentBeforeItemList = thisBlock.content.slice(0, literalIndex.contentIndex);
  const contentAfterItemList = thisBlock.content.slice(literalIndex.contentIndex + 1);

  const allItemsBeforeLiteral = itemList.items.slice(0, literalIndex.itemIndex);
  const allItemsAfterLiteral = itemList.items.slice(literalIndex.itemIndex + 1);

  const textContentsBeforeLiteral = item.content.slice(0, literalIndex.itemContentIndex);
  const textContentsAfterLiteral = item.content.slice(literalIndex.itemContentIndex + 1);

  if (insertingAtStart) {
    //TODO - kan muligens heller lage en konverteringsfunksjon fra literals/variables til items
    const traversedElementsAsItemListItems = parsedAndCombinedHtml.flatMap((t) =>
      t.content.map((c) => newItem({ content: [newLiteral({ text: c as string })] })),
    );

    const theNewItemList = newItemList({
      ...itemList,
      items: [...allItemsBeforeLiteral, ...traversedElementsAsItemListItems, item, ...allItemsAfterLiteral],
    });

    const newThisBlock = newParagraph({
      ...thisBlock,
      content: [...contentBeforeItemList, theNewItemList, ...contentAfterItemList],
    });

    return {
      replaceThisBlockWith: [newThisBlock],
      updatedFocus: {
        ...literalIndex,
        itemContentIndex: 0,
        itemIndex: theNewItemList.items.findIndex((i) => isEqual(i, item)),
        cursorPosition: 0,
      },
    };
  } else if (insertingInTheMiddle) {
    const textBeforeOffset = literalText.slice(0, offset);
    const textAfterOffset = literalText.slice(offset);

    const theNewLiteral =
      firstMapped.content[0].type === "LITERAL"
        ? newLiteral({
            editedText: textBeforeOffset + (firstMapped.content[0] as LiteralValue).text,
            text: textBeforeOffset + (firstMapped.content[0] as LiteralValue).text,
          })
        : newLiteral({
            editedText:
              textBeforeOffset + (firstMapped.content[0] as ItemList).items[0].content.map((c) => c.text).join(" "),
            text: textBeforeOffset + (firstMapped.content[0] as ItemList).items[0].content.map((c) => c.text).join(" "),
          });

    const theNewItem = newItem({
      content: [...textContentsBeforeLiteral, theNewLiteral],
    });

    //TODO - kan muligens heller lage en konverteringsfunksjon fra literals/variables til items
    const traversedElementsAsItemListItems = parsedAndCombinedHtml
      .flatMap((t) => {
        return t.content.map((c) => newItem({ content: [newLiteral({ text: c as string })] }));
      })
      .slice(1);

    const newItemAfter = newItem({
      content: [newLiteral({ text: textAfterOffset }), ...textContentsAfterLiteral],
    });

    const theNewItemList = newItemList({
      ...itemList,
      items: [
        ...allItemsBeforeLiteral,
        theNewItem,
        ...traversedElementsAsItemListItems,
        newItemAfter,
        ...allItemsAfterLiteral,
      ],
      deletedItems: [item?.id ? [item.id!] : []].flat(),
    });

    const newThisBlock = newParagraph({
      ...thisBlock,
      content: [...contentBeforeItemList, theNewItemList, ...contentAfterItemList],
    });

    return {
      replaceThisBlockWith: [newThisBlock],
      updatedFocus: {
        ...literalIndex,
        itemContentIndex: 0,
        itemIndex: theNewItemList.items.findIndex((i) => isEqual(i, newItemAfter)),
        cursorPosition: 0,
      },
    };
  }

  const theNewLiteral =
    firstMapped.content[0].type === "LITERAL"
      ? newLiteral({
          ...literalToBePastedInto,
          editedText:
            (literalToBePastedInto.editedText ?? literalToBePastedInto.text) +
            (firstMapped.content[0] as LiteralValue).text,
        })
      : newLiteral({
          ...literalToBePastedInto,
          editedText:
            (literalToBePastedInto.editedText ?? literalToBePastedInto.text) +
            (firstMapped.content[0] as ItemList).items[0].content.map((c) => c.text).join(" "),
        });

  const theNewItem = newItem({
    ...item,
    content: [...textContentsBeforeLiteral, theNewLiteral],
  });

  //TODO - kan muligens heller lage en konverteringsfunksjon fra literals/variables til items
  const traversedElementsAsItemListItems = parsedAndCombinedHtml
    .flatMap((t) => t.content.map((c) => newItem({ content: [newLiteral({ text: c as string })] })))
    .slice(1);

  const theNewItemList = newItemList({
    ...itemList,
    items: [...allItemsBeforeLiteral, theNewItem, ...traversedElementsAsItemListItems, ...allItemsAfterLiteral],
  });

  const newThisBlock = newParagraph({
    ...thisBlock,
    content: [contentBeforeItemList, theNewItemList, contentAfterItemList].flat(),
  });

  return {
    replaceThisBlockWith: [newThisBlock],
    updatedFocus: {
      ...literalIndex,
      itemContentIndex:
        traversedElementsAsItemListItems.length > 0
          ? (traversedElementsAsItemListItems.at(-1)?.content.length ?? 0) - 1
          : literalIndex.itemContentIndex,
      itemIndex: literalIndex.itemIndex + Math.max(traversedElementsAsItemListItems.length, 0),
      cursorPosition:
        traversedElementsAsItemListItems.length > 0
          ? ((traversedElementsAsItemListItems.at(-1)?.content.at(-1) as LiteralValue).editedText?.length ??
            traversedElementsAsItemListItems.at(-1)?.content.at(-1)?.text.length ??
            0)
          : (theNewLiteral.editedText ?? theNewLiteral.text).length,
    },
  };
};

const insertTextAtStartOfLiteral = (
  draft: Draft<LetterEditorState>,
  literalIndex: LiteralIndex,
  thisBlock: Draft<AnyBlock>,
  literalToBePastedInto: LiteralValue,
  parsedAndCombinedHtml: TraversedElement[],
): UpdatedBlocksAndFocus => {
  const contentBeforeLiteral = thisBlock.content.slice(0, literalIndex.contentIndex);
  const contentAfterLiteral = thisBlock.content.slice(literalIndex.contentIndex + 1);

  const newThisBlock = traversedElementsToBrevbaker(parsedAndCombinedHtml);
  const newNextBlock = newParagraph({
    ...thisBlock,
    content: [contentBeforeLiteral, literalToBePastedInto, contentAfterLiteral].flat(),
  });

  const replaceThisBlockWith = [...newThisBlock, newNextBlock].flat();
  const newBlockPosition = replaceThisBlockWith.indexOf(newNextBlock);

  return {
    replaceThisBlockWith: [...newThisBlock, newNextBlock].flat(),
    updatedFocus: {
      blockIndex: draft.focus.blockIndex + newBlockPosition,
      contentIndex: 0,
      cursorPosition: 0,
    },
  };
};

const insertTextInTheMiddleOfLiteral = (
  draft: Draft<LetterEditorState>,
  literalIndex: LiteralIndex,
  thisBlock: Draft<AnyBlock>,
  literalToBePastedInto: LiteralValue,
  parsedAndCombinedHtml: TraversedElement[],
  offset: number,
) => {
  const textBeforeOffset = (literalToBePastedInto.editedText ?? literalToBePastedInto.text).slice(0, offset);
  const textAfterOffset = (literalToBePastedInto.editedText ?? literalToBePastedInto.text).slice(offset);
  const contentBeforeLiteral = thisBlock.content.slice(0, literalIndex.contentIndex);
  const contentAfterLiteral = thisBlock.content.slice(literalIndex.contentIndex + 1);

  const mappedToBrevbaker = traversedElementsToBrevbaker(parsedAndCombinedHtml);
  const firstMapped = mappedToBrevbaker[0];
  const restMapped = mappedToBrevbaker.slice(1);
  const theNewLiteral =
    firstMapped.content[0].type === "LITERAL"
      ? newLiteral({
          editedText: textBeforeOffset + (firstMapped.content[0] as LiteralValue).text,
          text: textBeforeOffset + (firstMapped.content[0] as LiteralValue).text,
        })
      : newLiteral({
          editedText:
            textBeforeOffset + (firstMapped.content[0] as ItemList).items[0].content.map((c) => c.text).join(" "),
          text: textBeforeOffset + (firstMapped.content[0] as ItemList).items[0].content.map((c) => c.text).join(" "),
        });

  const shouldConvertToItemList = firstMapped.content[0].type === "ITEM_LIST";

  if (shouldConvertToItemList) {
    return convertLiteralAndAdjoiningToItemList(
      draft,
      literalIndex,
      thisBlock,
      literalToBePastedInto,
      parsedAndCombinedHtml,
      offset,
    );
  }

  const newThisBlock = newParagraph({
    id: thisBlock.id,
    content: [
      contentBeforeLiteral,
      firstMapped.content[0].type === "LITERAL"
        ? [theNewLiteral, ...(firstMapped.content.length > 1 ? firstMapped.content.slice(1) : [])]
        : [
            newItemList({
              items: [newItem({ content: [theNewLiteral] }), ...(firstMapped.content[0] as ItemList).items.slice(1)],
            }),
          ],
    ].flat(),
    deletedContent: [
      literalToBePastedInto.id ? [literalToBePastedInto.id] : [],
      contentAfterLiteral.map((c) => c.id).filter((i) => i !== null),
    ].flat(),
  });

  const newBlocksAfterThisBlock = restMapped;

  const restAfterBlock = newParagraph({
    content: [newLiteral({ text: textAfterOffset }), contentAfterLiteral].flat(),
  });

  const replaceThisBlockWith = [newThisBlock, ...newBlocksAfterThisBlock, restAfterBlock].flat();

  const newBlockPosition = replaceThisBlockWith.indexOf(restAfterBlock);
  const newContentPosition = Math.max((newBlocksAfterThisBlock.at(-1)?.content?.length ?? 0) - 1, 0);

  return {
    replaceThisBlockWith: replaceThisBlockWith,
    updatedFocus: {
      blockIndex: literalIndex.blockIndex + newBlockPosition,
      contentIndex: newContentPosition,
      cursorPosition: 0,
    },
  };
};

const insertTextAtEndOfLiteral = (
  draft: Draft<LetterEditorState>,
  literalIndex: LiteralIndex,
  thisBlock: Draft<AnyBlock>,
  literalToBePastedInto: LiteralValue,
  parsedAndCombinedHtml: TraversedElement[],
  offset: number,
): UpdatedBlocksAndFocus => {
  const contentBeforeLiteral = thisBlock.content.slice(0, literalIndex.contentIndex);
  const contentAfterLiteral = thisBlock.content.slice(literalIndex.contentIndex + 1);

  const mappedToBrevbaker = traversedElementsToBrevbaker(parsedAndCombinedHtml);
  const firstMapped = mappedToBrevbaker[0];
  const restMapped = mappedToBrevbaker.slice(1);

  const theNewLiteral =
    firstMapped.content[0].type === "LITERAL"
      ? newLiteral({
          ...literalToBePastedInto,
          editedText:
            (literalToBePastedInto.editedText ?? literalToBePastedInto.text) +
            (firstMapped.content[0] as LiteralValue).text,
        })
      : newLiteral({
          ...literalToBePastedInto,
          editedText:
            (literalToBePastedInto.editedText ?? literalToBePastedInto.text) +
            (firstMapped.content[0] as ItemList).items[0].content.map((c) => c.text).join(" "),
        });

  const shouldConvertToItemList = firstMapped.content[0].type === "ITEM_LIST";

  if (shouldConvertToItemList) {
    return convertLiteralAndAdjoiningToItemList(
      draft,
      literalIndex,
      thisBlock,
      literalToBePastedInto,
      parsedAndCombinedHtml,
      offset,
    );
  }

  const newThisBlockContent = [theNewLiteral, ...(firstMapped.content.length > 1 ? firstMapped.content.slice(1) : [])];

  const newThisBlock = newParagraph({
    ...thisBlock,
    content: [contentBeforeLiteral, newThisBlockContent].flat(),
  });

  const newBlocksAfterThisBlock = restMapped;
  const newContentAfterLiteralBlock = newParagraph({ content: contentAfterLiteral });
  const replaceThisBlockWith = [
    newThisBlock,
    ...newBlocksAfterThisBlock,
    newContentAfterLiteralBlock.content.length > 0 ? newContentAfterLiteralBlock : [],
  ].flat();

  const newBlockPosition = replaceThisBlockWith.findIndex((b) =>
    isEqual(b, newBlocksAfterThisBlock.length > 0 ? newBlocksAfterThisBlock.at(-1) : newThisBlock),
  );
  const newContentPosition = Math.max(
    (newBlocksAfterThisBlock.length > 0
      ? (newBlocksAfterThisBlock.at(-1)?.content.length ?? 0)
      : (newThisBlock.content.length ?? 0)) - 1,
    0,
  );

  const blockToGetCursorPositionFrom =
    newBlocksAfterThisBlock.length > 0 ? (newBlocksAfterThisBlock.at(-1) ?? newThisBlock) : newThisBlock;
  const lastContent = blockToGetCursorPositionFrom.content.at(-1) ?? newThisBlock.content.at(-1);

  const newCursorPosition =
    lastContent?.type === "LITERAL"
      ? (lastContent.editedText ?? lastContent.text).length
      : ((lastContent && ((lastContent as ItemList).items.at(-1)?.content.at(-1) as LiteralValue))?.editedText
          ?.length ??
        (lastContent && (lastContent as ItemList).items.at(-1)?.content.at(-1)?.text)?.length ??
        0);

  const itemContentIndex = lastContent?.type === "ITEM_LIST" ? 0 : undefined;
  const itemIndex = lastContent?.type === "ITEM_LIST" ? lastContent.items.length - 1 : undefined;

  /*
    Denne var litt vanskelig å finne ut av, har ikke klart å reprodusere for testene
    editoren vil sette markøren på feil posisjon dersom man returnerer itemIndex og itemContentIndex som undefined
    hvis man limer inn 2 p-tags. Siden dem blir satt som undefined, skal det ikke spille noe rolle om 
    dem kommer med i objektet som undefined, eller om dem ikke kommer med i det hele tatt.
    Men pga en bug(?) et eller annet sted, må man faktisk fjerne dem fra objektet for at det skal fungere.
    */
  const updatedFocus =
    itemContentIndex !== undefined && itemIndex !== undefined
      ? {
          blockIndex: draft.focus.blockIndex + newBlockPosition,
          contentIndex: newContentPosition,
          cursorPosition: newCursorPosition,
          itemContentIndex: itemContentIndex,
          itemIndex: itemIndex,
        }
      : {
          blockIndex: draft.focus.blockIndex + newBlockPosition,
          contentIndex: newContentPosition,
          cursorPosition: newCursorPosition,
        };

  return { replaceThisBlockWith, updatedFocus };
};

const convertLiteralAndAdjoiningToItemList = (
  draft: Draft<LetterEditorState>,
  literalIndex: LiteralIndex,
  thisBlock: Draft<AnyBlock>,
  literalToBePastedInto: LiteralValue,
  parsedAndCombinedHtml: TraversedElement[],
  offset: number,
): UpdatedBlocksAndFocus => {
  const literalText = literalToBePastedInto.editedText ?? literalToBePastedInto.text;
  const mappedToBrevbaker = traversedElementsToBrevbaker(parsedAndCombinedHtml);
  const firstMapped = mappedToBrevbaker[0];
  const restMapped = mappedToBrevbaker.slice(1);

  const contentBeforeLiteral = thisBlock.content.slice(0, literalIndex.contentIndex);
  const contentAfterLiteral = thisBlock.content.slice(literalIndex.contentIndex + 1);

  const insertingInTheMiddle = offset > 0 && offset < literalText.length;
  const insertingAtTheEnd = offset === literalText.length;

  const adjoiningItemListsIndexes = findAdjoiningContent(literalIndex.contentIndex, thisBlock.content, isItemList);
  const allItemsBeforeLiteral = (
    thisBlock.content.slice(adjoiningItemListsIndexes.startIndex, literalIndex.contentIndex) as ItemList[]
  ).flatMap((i) => i.items);
  const itemListsAfterLiteral = (
    thisBlock.content.slice(literalIndex.contentIndex + 1, adjoiningItemListsIndexes.endIndex) as ItemList[]
  ).flatMap((i) => i.items);

  const textContentIndexes = findAdjoiningContent(
    literalIndex.contentIndex,
    thisBlock.content,
    (v) => isLiteral(v) || isVariable(v),
  );
  const textContentsBeforeLiteral = thisBlock.content.slice(
    textContentIndexes.startIndex,
    literalIndex.contentIndex,
  ) as TextContent[];

  const textContentsAfterLiteral = thisBlock.content.slice(
    literalIndex.contentIndex + 1,
    textContentIndexes.endIndex,
  ) as TextContent[];

  const textBeforeOffset = literalText.slice(0, offset);
  const textAfterOffset = literalText.slice(offset);

  const theNewLiteral = insertingInTheMiddle
    ? newLiteral({
        text: textBeforeOffset + (firstMapped.content[0] as ItemList).items[0].content.map((c) => c.text).join(" "),
      })
    : newLiteral({
        text: literalText + (firstMapped.content[0] as ItemList).items[0].content.map((c) => c.text).join(" "),
      });

  const newLiteralContent = newItemList({
    items: [
      ...allItemsBeforeLiteral,
      newItem({
        content: [
          textContentsBeforeLiteral,
          theNewLiteral,
          insertingAtTheEnd ? [...textContentsAfterLiteral] : [],
        ].flat(),
      }),
      ...(firstMapped.content[0] as ItemList).items.slice(1),
      ...itemListsAfterLiteral,
    ],
  });

  const newThisBlock = newParagraph({
    ...thisBlock,
    content: [
      thisBlock.content.slice(
        0,
        textContentIndexes.count > 1 ? textContentIndexes.startIndex : adjoiningItemListsIndexes.startIndex,
      ),
      newLiteralContent,
    ].flat(),
    deletedContent: [
      ...thisBlock.deletedContent,
      contentBeforeLiteral.map((c) => c.id).filter((i) => i !== null),
      literalToBePastedInto.id ? [literalToBePastedInto.id] : [],
      contentAfterLiteral.map((c) => c.id).filter((i) => i !== null),
    ].flat(),
  });

  const newBlocksAfterThisBlock = restMapped;
  const newContentAfterLiteralBlock = newParagraph({
    content: [insertingInTheMiddle ? [newLiteral({ text: textAfterOffset })] : [], contentAfterLiteral].flat(),
  });
  const replaceThisBlockWith = [
    newThisBlock,
    ...newBlocksAfterThisBlock,
    newContentAfterLiteralBlock.content.length > 0 ? newContentAfterLiteralBlock : [],
  ].flat();

  const newBlockPosition = replaceThisBlockWith.findIndex((b) =>
    isEqual(b, newBlocksAfterThisBlock.length > 0 ? newBlocksAfterThisBlock.at(-1) : newThisBlock),
  );
  const newContentPosition = Math.max(
    (newBlocksAfterThisBlock.length > 0
      ? (newBlocksAfterThisBlock.at(-1)?.content.length ?? 0)
      : (newThisBlock.content.length ?? 0)) - 1,
    0,
  );

  const blockToGetCursorPositionFrom =
    newBlocksAfterThisBlock.length > 0 ? (newBlocksAfterThisBlock.at(-1) ?? newThisBlock) : newThisBlock;
  const lastContent = blockToGetCursorPositionFrom.content.at(-1) ?? newThisBlock.content.at(-1);

  const newCursorPosition =
    lastContent?.type === "LITERAL"
      ? lastContent.text.length
      : ((lastContent && (lastContent as ItemList).items.at(-1)?.content.at(-1)?.text.length) ?? 0);

  const itemContentIndex = lastContent?.type === "ITEM_LIST" ? 0 : undefined;
  const itemIndex = lastContent?.type === "ITEM_LIST" ? lastContent.items.length - 1 : undefined;

  return {
    replaceThisBlockWith: replaceThisBlockWith,
    updatedFocus: {
      blockIndex: draft.focus.blockIndex + newBlockPosition,
      contentIndex: newContentPosition,
      cursorPosition: newCursorPosition,
      itemContentIndex: itemContentIndex,
      itemIndex: itemIndex,
    },
  };
};

/**
 * Slutt resultatet vil vi skal inneholde kun tekst-strenger. Den kan inneholde traversedElement pga at vi kaller funksjonen rekursivt
 */
interface TraversedElement {
  tag: "P" | "LI" | "SPAN";
  content: (TraversedElement | string)[];
}

/**
 * fun fact: LI elementet sin content inneholder kun rene tekst-strenger, og kan bli castet til det.
 */
const traverseElement = (el: Element): TraversedElement[] | TraversedElement => {
  switch (el.tagName) {
    case "LI": {
      if (el.children.length > 0) {
        const content = [...el.children].flatMap(traverseElement).filter((el) => el.content !== null);

        const mapped = content.flatMap((el) => {
          // TODO Burde det være el.content?
          if (typeof el === "string") {
            return el;
          }

          return el.content;
        });

        return { tag: "LI", content: mapped };
      }

      return { tag: "LI", content: el.textContent?.trim() ? [el.textContent.trim()] : [] };
    }
    case "P": {
      if (el.children.length > 0) {
        const content = [...el.children].flatMap(traverseElement).flatMap((el) => el.content);

        //dersom content er tom, betyr det at den bar inneholdt en tom span, som skal være linjeskift
        return { tag: "P", content: content.length > 0 ? content : [" "] };
      }

      //p elementet skal forsåvidt inneholde andre elementer, men dersom den ikke gjør det og kun inneholder tekst, skal den returneres som en string
      return { tag: "P", content: el.textContent?.trim() ? [el.textContent.trim()] : [] };
    }

    case "SPAN": {
      if (el.children.length > 0) {
        const content = [...el.children].flatMap(traverseElement).flatMap((el) => el.content);
        return { tag: "SPAN", content: content };
      }
      const isEmptyOrNonExisting = el.textContent?.trim() === "" || !el.textContent;
      return { tag: "SPAN", content: isEmptyOrNonExisting ? [] : [el.textContent!.trim()] };
    }

    case "DIV": {
      return [...el.children].flatMap(traverseElement);
    }
    default: {
      return [...el.children].flatMap(traverseElement);
    }
  }
};

const mergeNeighboringTags = (blocks: TraversedElement[]): TraversedElement[] =>
  blocks.reduce<TraversedElement[]>((acc, curr) => {
    const last = acc.at(-1);

    if (last && last.tag === curr.tag && curr.tag === "SPAN") {
      last.content = [...last.content, ...curr.content];
    } else {
      acc.push({ ...curr });
    }

    return acc;
  }, []);

const traversedElementToBrevbaker = (t: TraversedElement) => {
  switch (t.tag) {
    case "LI": {
      return newItemList({
        items: t.content.map((item) => {
          //vi forventer at ItemLists sine items er kun rene tekst strenger
          if (typeof item !== "string") {
            throw new TypeError("item is not a string");
          }
          return newItem({ content: [newLiteral({ text: item })] });
        }),
      });
    }
    case "P": {
      return newParagraph({ content: [newLiteral({ text: t.content.join(" ") })] });
    }
    case "SPAN": {
      const text = t.content.length > 1 ? t.content.join(" ") : ` ${t.content[0]}`;
      return newParagraph({ content: [newLiteral({ text })] });
    }
  }
};

function insertTraversedElements(
  draft: Draft<LetterEditorState>,
  index: LiteralIndex,
  cursorOffset: number,
  elements: TraversedElement[],
) {
  draft.focus = { ...index, cursorPosition: cursorOffset };

  elements.forEach((el) => {
    const currentBlock = draft.redigertBrev.blocks[draft.focus.blockIndex];

    switch (el.tag) {
      case "SPAN": {
        insertTextInLetter(draft, draft.focus, draft.focus.cursorPosition!, el.content.join(" "));
        break;
      }
      case "P": {
        const pastedText = el.content.join(" ");
        // Om vi er på starten av en blokk så ønsker vi å sette inn en ny blokk før den for preservere ID'er på gjeldende
        if (isBlockContentIndex(draft.focus) && draft.focus.contentIndex === 0 && draft.focus.cursorPosition === 0) {
          addElements(
            [newParagraph({ content: [newLiteral({ text: "", editedText: pastedText })] })],
            draft.focus.blockIndex,
            draft.redigertBrev.blocks,
            draft.redigertBrev.deletedBlocks,
          );
          draft.focus.blockIndex += 1;
        } else {
          insertTextInLetter(draft, draft.focus, draft.focus.cursorPosition!, pastedText);
          splitRecipe(draft, draft.focus, draft.focus.cursorPosition!);
        }
        break;
      }
      case "LI": {
        if (isItemContentIndex(draft.focus) && isItemList(currentBlock.content[draft.focus.contentIndex])) {
          insertTextInLetter(draft, draft.focus, draft.focus.cursorPosition!, el.content.join(" "));
          splitRecipe(draft, draft.focus, draft.focus.cursorPosition!);
        } else if (!isItemContentIndex(draft.focus) && !isItemList(currentBlock.content[draft.focus.contentIndex])) {
          // vi sjekker også at gjeldende ikke er itemList fordi da _må_ også focus være ItemContentIndex.

          toggleItemListAndSplitAtCursor(draft, currentBlock);
          const pastedText = el.content.join(" ");
          insertTextInLetter(draft, draft.focus, draft.focus.cursorPosition!, pastedText);
          splitRecipe(draft, draft.focus, draft.focus.cursorPosition!);
        }
        break;
      }
    }
  });
}

// TODO: Burde se på å bruke toggle-itemlist-action her!
function toggleItemListAndSplitAtCursor(draft: Draft<LetterEditorState>, currentBlock: Draft<AnyBlock>) {
  let itemContent: TextContent[] = [];

  // Finn indeks for all sammenhengende TextContent før der vi limer inn.
  const prevNonTextContentIndex = currentBlock.content
    .slice(0, draft.focus.contentIndex)
    .findLastIndex((c) => !isTextContent(c));
  const extractContentFrom = prevNonTextContentIndex >= 0 ? prevNonTextContentIndex + 1 : 0;

  if (extractContentFrom < draft.focus.contentIndex) {
    itemContent = removeElements(
      extractContentFrom,
      draft.focus.contentIndex - extractContentFrom,
      currentBlock,
    ).filter(isTextContent);
    // Vi har fjernet fra og med extractContentFrom frem til draft.focus.contentIndex som betyr at den nye indeksen til current er extractContentFrom
    draft.focus.contentIndex = extractContentFrom;
  }

  // Split currentContent om vi limer inn et sted som ikke er i starten av den, og legg den til i itemContent og resten tilbake til blokken.
  const currentContent = currentBlock.content[draft.focus.contentIndex];
  if (isLiteral(currentContent) && draft.focus.cursorPosition! > 0) {
    removeElements(draft.focus.contentIndex, 1, currentBlock);
    const afterSplit = splitLiteralAtOffset(currentContent, draft.focus.cursorPosition!);

    if (text(afterSplit).length > 0) {
      addElements([afterSplit], draft.focus.contentIndex, currentBlock.content, []);
    }

    itemContent.push(currentContent);
  }

  // Sørg for at vårt nye item har noe content.
  if (itemContent.length === 0) {
    itemContent.push(newLiteral({ text: "", editedText: "" }));
  }
  const theItem = newItem({ content: itemContent });

  // Sett inn item i eksisterende liste før current content, eller lag en ny liste og theItem inn i den.
  const prevNonTextContent = prevNonTextContentIndex >= 0 ? currentBlock.content[prevNonTextContentIndex] : null;
  if (isItemList(prevNonTextContent)) {
    addElements([theItem], prevNonTextContent.items.length, prevNonTextContent.items, prevNonTextContent.deletedItems);
    // Sett fokus til det theItem som vi har satt inn i eksisterende ItemList.
    draft.focus = {
      ...draft.focus,
      contentIndex: prevNonTextContentIndex,
      itemIndex: prevNonTextContent.items.length - 1,
      itemContentIndex: theItem.content.length - 1,
    };
  } else {
    addElements(
      [newItemList({ items: [theItem] })],
      draft.focus.contentIndex,
      currentBlock.content,
      currentBlock.deletedContent,
    );
    draft.focus = {
      ...draft.focus,
      itemIndex: 0,
      itemContentIndex: theItem.content.length - 1,
    };
  }
}

const traversedElementsToBrevbaker = (t: TraversedElement[]) =>
  t.reduce<ParagraphBlock[]>((acc, curr) => {
    const lastParagraph = acc.at(-1) ?? null;
    const isLastParagraphLineBreak =
      lastParagraph?.content.length === 1 &&
      isLiteral(lastParagraph.content[0]) &&
      lastParagraph.content[0].text === " ";

    switch (curr.tag) {
      case "LI": {
        const itemList = traversedElementToBrevbaker(curr) as ItemList;

        if (lastParagraph && !isLastParagraphLineBreak) {
          lastParagraph.content.push(itemList);
        } else {
          acc.push(newParagraph({ content: [itemList] }));
        }
        break;
      }

      case "P": {
        acc.push(traversedElementToBrevbaker(curr) as ParagraphBlock);
        break;
      }

      case "SPAN": {
        acc.push(traversedElementToBrevbaker(curr) as ParagraphBlock);
        break;
      }
    }
    return acc;
  }, []);

const parseAndCombineHTML = (bodyElement: HTMLElement) => {
  const result = [...bodyElement.children].flatMap(traverseElement);
  const combined = mergeNeighboringTags(result);
  return combined;
};

function log(message: string) {
  // eslint-disable-next-line no-console
  console.log("Skribenten:pasteHandler: " + message);
}

function isIndexAtStartOfBlock(index: LiteralIndex, cursorOffset: number): boolean {
  if (isItemContentIndex(index)) {
    return index.contentIndex === 0 && index.itemIndex === 0 && index.itemContentIndex === 0 && cursorOffset === 0;
  } else {
    return index.contentIndex === 0 && cursorOffset === 0;
  }
}

function isIndexAtEndOfBlock(index: LiteralIndex, cursorOffset: number, block: AnyBlock): boolean {
  if (isItemContentIndex(index)) {
    const itemList = isItemList(block.content[index.contentIndex])
      ? (block.content[index.contentIndex] as ItemList)
      : null;
    const item = itemList?.items[index.itemIndex];
    const itemContent = item?.content[index.itemContentIndex];
    return (
      index.contentIndex + 1 === block.content.length &&
      index.itemIndex + 1 === itemList?.items.length &&
      index.itemContentIndex + 1 === item?.content.length &&
      cursorOffset + 1 === text(itemContent)?.length
    );
  } else {
    const content = block.content[index.contentIndex];
    switch (content.type) {
      case VARIABLE:
      case LITERAL:
        return index.contentIndex + 1 === block.content.length && cursorOffset + 1 === text(content)?.length;
      case NEW_LINE:
        return index.contentIndex + 1 === block.content.length;
      case ITEM_LIST:
        return false;
    }
  }
}
