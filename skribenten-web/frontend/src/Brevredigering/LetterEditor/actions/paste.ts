import type { Draft } from "immer";
import { produce } from "immer";
import { isEqual } from "lodash";

import {
  findAdjoiningContent,
  newItem,
  newItemList,
  newLiteral,
  newParagraph,
  text,
} from "~/Brevredigering/LetterEditor/actions/common";
import type { LiteralIndex } from "~/Brevredigering/LetterEditor/actions/model";
import { updateLiteralText } from "~/Brevredigering/LetterEditor/actions/updateContentText";
import type { Action } from "~/Brevredigering/LetterEditor/lib/actions";
import type { LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import type { AnyBlock, ItemList, LiteralValue, ParagraphBlock, TextContent } from "~/types/brevbakerTypes";
import { LITERAL } from "~/types/brevbakerTypes";
import { ITEM_LIST } from "~/types/brevbakerTypes";
import { handleSwitchContent, handleSwitchTextContent, isItemContentIndex } from "~/utils/brevbakerUtils";

import { isItemList, isLiteral, isVariable } from "../model/utils";

export const paste: Action<LetterEditorState, [literalIndex: LiteralIndex, offset: number, clipboard: DataTransfer]> =
  produce((draft, literalIndex, offset, clipboard) => {
    if (clipboard.types.includes("text/html")) {
      insertHtmlClipboardInLetter(draft, literalIndex, offset, clipboard);
    } else if (clipboard.types.includes("text/plain")) {
      insertTextInLetter(draft, literalIndex, offset, clipboard.getData("text/plain"));
    } else {
      log("unsupported clipboard datatype(s) - " + JSON.stringify(clipboard.types));
    }
  });

export function logPastedClipboard(clipboardData: DataTransfer) {
  // log("available paste types - " + JSON.stringify(clipboardData.types));
  // log("pasted html content - " + clipboardData.getData("text/html"));
  // log("pasted plain content - " + clipboardData.getData("text/plain"));
}

function insertTextInLetter(draft: Draft<LetterEditorState>, literalIndex: LiteralIndex, offset: number, str: string) {
  const content = draft.redigertBrev.blocks[literalIndex.blockIndex]?.content[literalIndex.contentIndex];
  const cursorPositionUpdater = (l: number) => (draft.focus.cursorPosition = l);

  if (content?.type === ITEM_LIST && "itemContentIndex" in literalIndex) {
    const itemContent = content.items[literalIndex.itemIndex]?.content[literalIndex?.itemContentIndex];
    if (itemContent?.type === LITERAL) {
      insertText(itemContent, offset, str, cursorPositionUpdater);
    } else {
      log("cannot insert text into variable - " + str);
    }
  } else if (content?.type === LITERAL && !("itemContentIndex" in literalIndex)) {
    insertText(content, offset, str, cursorPositionUpdater);
  } else {
    log("literalIndex is invalid " + JSON.stringify(literalIndex));
    log("text - " + str);
  }
}

function insertText(
  draft: Draft<LiteralValue>,
  offset: number,
  str: string,
  cursorPositionUpdater: (l: number) => void,
) {
  const existingText = text(draft);
  if (offset <= 0) {
    updateLiteralText(draft, str + existingText);
    cursorPositionUpdater(str.length);
  } else if (offset >= existingText.length) {
    updateLiteralText(draft, existingText + str);
    cursorPositionUpdater(existingText.length + str.length);
  } else {
    const text = existingText.slice(0, Math.max(0, offset)) + str + existingText.slice(Math.max(0, offset));
    updateLiteralText(draft, text);
    cursorPositionUpdater(text.length);
  }
}

function insertHtmlClipboardInLetter(
  draft: Draft<LetterEditorState>,
  literalIndex: LiteralIndex,
  offset: number,
  clipboard: DataTransfer,
) {
  const parser = new DOMParser();
  const document = parser.parseFromString(clipboard.getData("text/html"), "text/html");
  const parsedAndCombinedHtml = parseAndCombineHTML(document.body);

  if (parsedAndCombinedHtml.length === 0) {
    //trenger ikke å lime inn tomt innhold
    return;
  }

  const thisBlock = draft.redigertBrev.blocks[literalIndex.blockIndex];
  const thisContent = thisBlock.content[literalIndex.contentIndex];

  handleSwitchContent({
    content: thisContent,
    onLiteral: (literalToBePastedInto) => {
      const { replaceThisBlockWith, updatedFocus } = pasteIntoLiteral(
        draft,
        literalIndex,
        thisBlock,
        offset,
        literalToBePastedInto,
        parsedAndCombinedHtml,
      );

      const newBlocks = [
        ...draft.redigertBrev.blocks.slice(0, literalIndex.blockIndex),
        ...replaceThisBlockWith,
        ...draft.redigertBrev.blocks.slice(literalIndex.blockIndex + 1),
      ];
      draft.redigertBrev.blocks = newBlocks;
      draft.focus = { ...updatedFocus };
      draft.isDirty = true;
    },
    onVariable: () => {
      throw new Error("Cannot paste into variable");
    },
    onItemList: (itemList) => {
      if (!isItemContentIndex(literalIndex)) {
        return;
      }

      const item = itemList.items[literalIndex.itemIndex];
      const itemContent = item.content[literalIndex.itemContentIndex];

      handleSwitchTextContent({
        content: itemContent,
        onLiteral: (literalToBePastedInto) => {
          const { replaceThisBlockWith, updatedFocus } = pasteIntoLiteral(
            draft,
            literalIndex,
            thisBlock,
            offset,
            literalToBePastedInto,
            parsedAndCombinedHtml,
          );

          const newBlocks = [
            ...draft.redigertBrev.blocks.slice(0, literalIndex.blockIndex),
            ...replaceThisBlockWith,
            ...draft.redigertBrev.blocks.slice(literalIndex.blockIndex + 1),
          ];
          draft.redigertBrev.blocks = newBlocks;
          draft.focus = { ...updatedFocus };
          draft.isDirty = true;
        },
        onVariable: () => {
          throw new Error("Cannot paste into variable");
        },
        onNewLine: () => {
          throw new Error("Cannot paste into newline");
        },
      });
    },
    onNewLine: () => {
      //TODO - newLine er en spesiell type content. Skal vi støtte paste inn i newLine?
      throw new Error("Cannot paste into newline");
    },
  });
}

const pasteIntoLiteral = (
  draft: Draft<LetterEditorState>,
  literalIndex: LiteralIndex,
  thisBlock: Draft<AnyBlock>,
  offset: number,
  literalToBePastedInto: LiteralValue,
  parsedAndCombinedHtml: TraversedElement[],
): {
  replaceThisBlockWith: ParagraphBlock[];
  updatedFocus: {
    cursorPosition: number;
    itemContentIndex?: number;
    itemIndex?: number;
    blockIndex: number;
    contentIndex: number;
  };
} => {
  //Hvis første elementet er en span, så vet vi at det ikke finnes flere elementer enn det ene.
  const isInsertingOnlyWords = parsedAndCombinedHtml[0].tag === "SPAN";

  if (isInsertingOnlyWords) {
    return insertSpanElementsIntoLiteral(
      draft,
      literalIndex,
      thisBlock,
      literalToBePastedInto,
      parsedAndCombinedHtml[0],
      offset,
    );
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

const insertSpanElementsIntoLiteral = (
  draft: Draft<LetterEditorState>,
  literalIndex: LiteralIndex,
  thisBlock: Draft<AnyBlock>,
  literalToBePastedInto: LiteralValue,
  traversedElement: TraversedElement,
  offset: number,
): {
  replaceThisBlockWith: ParagraphBlock[];
  updatedFocus: {
    cursorPosition: number;
    itemContentIndex?: number;
    itemIndex?: number;
    blockIndex: number;
    contentIndex: number;
  };
} => {
  if (traversedElement.tag !== "SPAN") {
    throw new Error("Expected traversed element to be a span element to insert at the start of literal");
  }

  const literalText = literalToBePastedInto.editedText ?? literalToBePastedInto.text;
  const combinedSpanText = traversedElement.content.join(" ");
  const contentBeforeLiteral = thisBlock.content.slice(0, literalIndex.contentIndex);
  const contentAfterLiteral = thisBlock.content.slice(literalIndex.contentIndex + 1);
  const shouldBeItemList = isItemContentIndex(literalIndex);

  const insertingAtStart = offset === 0;
  const insertingInTheMiddle = offset > 0 && offset < literalText.length;

  const newText = insertingAtStart
    ? combinedSpanText + literalText
    : insertingInTheMiddle
      ? literalText.slice(0, offset) + combinedSpanText + literalText.slice(offset)
      : literalText + combinedSpanText;

  const theNewLiteral = newLiteral({ ...literalToBePastedInto, editedText: newText });

  const cursorPosition = insertingAtStart
    ? combinedSpanText.length
    : insertingInTheMiddle
      ? offset + combinedSpanText.length
      : literalText.length + combinedSpanText.length;

  if (shouldBeItemList) {
    const itemList = thisBlock.content[literalIndex.contentIndex] as ItemList;
    const item = itemList.items[literalIndex.itemIndex];

    const theNewItem = newItem({
      ...item,
      content: [
        ...item.content.slice(0, literalIndex.itemContentIndex),
        theNewLiteral,
        ...item.content.slice(literalIndex.itemContentIndex + 1),
      ],
    });

    const theNewItemList = newItemList({
      ...itemList,
      items: [
        ...(thisBlock.content[literalIndex.contentIndex] as ItemList).items.slice(0, literalIndex.itemIndex),
        theNewItem,
        ...(thisBlock.content[literalIndex.contentIndex] as ItemList).items.slice(literalIndex.itemIndex + 1),
      ],
    });

    const theNewParagraph = newParagraph({
      ...thisBlock,
      content: [...contentBeforeLiteral, theNewItemList, ...contentAfterLiteral],
    });

    return {
      replaceThisBlockWith: [theNewParagraph],
      updatedFocus: {
        ...literalIndex,
        cursorPosition: cursorPosition,
        itemContentIndex: literalIndex.itemContentIndex,
        itemIndex: literalIndex.itemIndex,
      },
    };
  }

  const newContent = [...contentBeforeLiteral, theNewLiteral, ...contentAfterLiteral];
  const newThisBlock = newParagraph({ ...thisBlock, content: newContent });
  const replaceThisBlockWith = [newThisBlock];
  const updatedFocus = {
    ...literalIndex,
    cursorPosition: cursorPosition,
  };

  return { replaceThisBlockWith, updatedFocus };
};

const insertNonSpanElementsIntoLiteral = (
  draft: Draft<LetterEditorState>,
  literalIndex: LiteralIndex,
  thisBlock: Draft<AnyBlock>,
  literalToBePastedInto: LiteralValue,
  parsedAndCombinedHtml: TraversedElement[],
  offset: number,
) => {
  const mappedToBrevbaker = traversedElementsToBrevbaker(parsedAndCombinedHtml);
  const firstMapped = mappedToBrevbaker[0];
  const restMapped = mappedToBrevbaker.slice(1);

  const contentBeforeLiteral = thisBlock.content.slice(0, literalIndex.contentIndex);
  const contentAfterLiteral = thisBlock.content.slice(literalIndex.contentIndex + 1);

  const appendingToStartOfLiteral = offset === 0;
  const appendingToMiddleOfLiteral =
    offset > 0 && offset < (literalToBePastedInto.editedText?.length ?? literalToBePastedInto.text.length);

  const shouldBeItemList = isItemContentIndex(literalIndex);
  const shouldConvertToItemList = firstMapped.content[0].type === "ITEM_LIST" && !appendingToStartOfLiteral;

  if (appendingToStartOfLiteral) {
    return insertTextAtStartOfLiteral(draft, literalIndex, thisBlock, literalToBePastedInto, parsedAndCombinedHtml);
  } else if (appendingToMiddleOfLiteral) {
    return insertTextInTheMiddleOfLiteral(
      draft,
      literalIndex,
      thisBlock,
      literalToBePastedInto,
      parsedAndCombinedHtml,
      offset,
    );
  } else {
    return insertTextAtEndOfLiteral(
      draft,
      literalIndex,
      thisBlock,
      literalToBePastedInto,
      parsedAndCombinedHtml,
      offset,
    );
  }
};

const insertTextAtStartOfLiteral = (
  draft: Draft<LetterEditorState>,
  literalIndex: LiteralIndex,
  thisBlock: Draft<AnyBlock>,
  literalToBePastedInto: LiteralValue,
  parsedAndCombinedHtml: TraversedElement[],
): {
  replaceThisBlockWith: ParagraphBlock[];
  updatedFocus: {
    cursorPosition: number;
    itemContentIndex?: number;
    itemIndex?: number;
    blockIndex: number;
    contentIndex: number;
  };
} => {
  const shouldBeItemList = isItemContentIndex(literalIndex);
  const contentBeforeLiteral = thisBlock.content.slice(0, literalIndex.contentIndex);
  const contentAfterLiteral = thisBlock.content.slice(literalIndex.contentIndex + 1);

  if (shouldBeItemList) {
    const itemList = thisBlock.content[literalIndex.contentIndex] as ItemList;
    const item = itemList.items[literalIndex.itemIndex];

    const traversedElementsAsItemListItems = parsedAndCombinedHtml.flatMap((t) =>
      t.content.map((c) => newItem({ content: [newLiteral({ text: c as string })] })),
    );

    const theNewItem = newItem({
      ...item,
      content: [
        ...item.content.slice(0, literalIndex.itemContentIndex),
        literalToBePastedInto,
        ...item.content.slice(literalIndex.itemContentIndex + 1),
      ],
    });
    const theNewItemList = newItemList({
      ...itemList,
      items: [
        ...itemList.items.slice(0, literalIndex.itemIndex),
        ...traversedElementsAsItemListItems,
        theNewItem,
        ...itemList.items.slice(literalIndex.itemIndex + 1),
      ],
    });

    const newThisBlock = newParagraph({
      ...thisBlock,
      content: [...contentBeforeLiteral, theNewItemList, ...contentAfterLiteral],
    });

    return {
      replaceThisBlockWith: [newThisBlock],
      updatedFocus: {
        ...literalIndex,
        itemContentIndex: 0,
        itemIndex: theNewItemList.items.findIndex((i) => isEqual(i, theNewItem)),
        cursorPosition: 0,
      },
    };
  } else {
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
  }
};

const insertTextInTheMiddleOfLiteral = (
  draft: Draft<LetterEditorState>,
  literalIndex: LiteralIndex,
  thisBlock: Draft<AnyBlock>,
  literalToBePastedInto: LiteralValue,
  parsedAndCombinedHtml: TraversedElement[],
  offset: number,
) => {
  const shouldBeItemList = isItemContentIndex(literalIndex);
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

  if (shouldBeItemList) {
    const theNewItem = newItem({
      content: [
        ...(thisBlock.content[literalIndex.contentIndex] as ItemList).items[literalIndex.itemIndex].content.slice(
          0,
          literalIndex.itemContentIndex,
        ),
        theNewLiteral,
      ],
    });
    const traversedElementsAsItemListItems = parsedAndCombinedHtml
      .flatMap((t) => {
        return t.content.map((c) => newItem({ content: [newLiteral({ text: c as string })] }));
      })
      .slice(1);

    const newItemAfter = newItem({
      content: [
        newLiteral({ text: textAfterOffset }),
        ...(thisBlock.content[literalIndex.contentIndex] as ItemList).items[literalIndex.itemIndex].content.slice(
          literalIndex.itemContentIndex + 1,
        ),
      ],
    });

    const theNewItemList = newItemList({
      ...(thisBlock.content[literalIndex.contentIndex] as ItemList),
      items: [
        ...(thisBlock.content[literalIndex.contentIndex] as ItemList).items.slice(0, literalIndex.itemIndex),
        theNewItem,
        ...traversedElementsAsItemListItems,
        newItemAfter,
        ...(thisBlock.content[literalIndex.contentIndex] as ItemList).items.slice(literalIndex.itemIndex + 1),
      ],
      deletedItems: [
        (thisBlock.content[literalIndex.contentIndex] as ItemList).items[literalIndex.itemIndex]?.id
          ? [(thisBlock.content[literalIndex.contentIndex] as ItemList).items[literalIndex.itemIndex].id!]
          : [],
      ].flat(),
    });

    const newThisBlock = newParagraph({
      ...thisBlock,
      content: [...contentBeforeLiteral, theNewItemList, ...contentAfterLiteral],
    });

    const replaceThisBlockWith = [newThisBlock];

    const updatedFocus = {
      ...literalIndex,
      itemContentIndex: 0,
      itemIndex: theNewItemList.items.findIndex((i) => isEqual(i, newItemAfter)),
      cursorPosition: 0,
    };

    return { replaceThisBlockWith, updatedFocus };
  }

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

  const updatedFocus = {
    blockIndex: literalIndex.blockIndex + newBlockPosition,
    contentIndex: newContentPosition,
    cursorPosition: 0,
  };

  return { replaceThisBlockWith, updatedFocus };
};

const insertTextAtEndOfLiteral = (
  draft: Draft<LetterEditorState>,
  literalIndex: LiteralIndex,
  thisBlock: Draft<AnyBlock>,
  literalToBePastedInto: LiteralValue,
  parsedAndCombinedHtml: TraversedElement[],
  offset: number,
): {
  replaceThisBlockWith: ParagraphBlock[];
  updatedFocus: {
    cursorPosition: number;
    itemContentIndex?: number;
    itemIndex?: number;
    blockIndex: number;
    contentIndex: number;
  };
} => {
  const shouldBeItemList = isItemContentIndex(literalIndex);
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

  if (shouldBeItemList) {
    const theNewItem = newItem({
      ...(thisBlock.content[literalIndex.contentIndex] as ItemList).items[literalIndex.itemIndex],
      content: [
        ...(thisBlock.content[literalIndex.contentIndex] as ItemList).items[literalIndex.itemIndex].content.slice(
          0,
          literalIndex.itemContentIndex,
        ),
        theNewLiteral,
      ],
    });
    const traversedElementsAsItemListItems = parsedAndCombinedHtml
      .flatMap((t) => {
        return t.content.map((c) => newItem({ content: [newLiteral({ text: c as string })] }));
      })
      .slice(1);

    const itemsBeforeLiteral = (thisBlock.content[literalIndex.contentIndex] as ItemList).items.slice(
      0,
      literalIndex.itemIndex,
    );
    const itemsAfterLiteral = (thisBlock.content[literalIndex.contentIndex] as ItemList).items.slice(
      literalIndex.itemIndex + 1,
    );

    const theNewItemList = newItemList({
      ...(thisBlock.content[literalIndex.contentIndex] as ItemList),
      items: [...itemsBeforeLiteral, theNewItem, ...traversedElementsAsItemListItems, ...itemsAfterLiteral],
    });

    const newThisBlock = newParagraph({
      ...thisBlock,
      content: [contentBeforeLiteral, theNewItemList, contentAfterLiteral].flat(),
    });

    const replaceThisBlockWith = [newThisBlock];

    const updatedFocus = {
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
    };

    return { replaceThisBlockWith, updatedFocus };
  }
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
): {
  replaceThisBlockWith: ParagraphBlock[];
  updatedFocus: {
    cursorPosition: number;
    itemContentIndex?: number;
    itemIndex?: number;
    blockIndex: number;
    contentIndex: number;
  };
} => {
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

  const updatedFocus = {
    blockIndex: draft.focus.blockIndex + newBlockPosition,
    contentIndex: newContentPosition,
    cursorPosition: newCursorPosition,
    itemContentIndex: itemContentIndex,
    itemIndex: itemIndex,
  };

  return { replaceThisBlockWith, updatedFocus };
};

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

function mergeNeighboringTags(blocks: TraversedElement[]): TraversedElement[] {
  return blocks.reduce<TraversedElement[]>((acc, curr) => {
    const last = acc.at(-1);

    if (last && last.tag === curr.tag && (curr.tag === "LI" || curr.tag === "SPAN")) {
      last.content = [...last.content, ...curr.content];
    } else {
      acc.push({ ...curr });
    }

    return acc;
  }, []);
}

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
