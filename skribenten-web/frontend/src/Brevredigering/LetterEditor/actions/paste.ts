import type { Draft } from "immer";
import { produce } from "immer";

import {
  addElements,
  cleanseText,
  findAdjoiningContent,
  fontTypeOf,
  isAtStartOfBlock,
  isAtStartOfItem,
  isBlockContentIndex,
  isItemContentIndex,
  isNew,
  newItem,
  newItemList,
  newLiteral,
  newParagraph,
  newTitle,
  removeElements,
  splitLiteralAtOffset,
  text,
} from "~/Brevredigering/LetterEditor/actions/common";
import { splitRecipe } from "~/Brevredigering/LetterEditor/actions/split";
import { updateLiteralText } from "~/Brevredigering/LetterEditor/actions/updateContentText";
import type { Action } from "~/Brevredigering/LetterEditor/lib/actions";
import type {
  Focus,
  ItemContentIndex,
  LetterEditorState,
  LiteralIndex,
} from "~/Brevredigering/LetterEditor/model/state";
import type { AnyBlock, Content, LiteralValue, TextContent } from "~/types/brevbakerTypes";
import { FontType } from "~/types/brevbakerTypes";

import { isEmptyBlock, isItemList, isLiteral, isTextContent } from "../model/utils";

export const paste: Action<LetterEditorState, [literalIndex: LiteralIndex, offset: number, clipboard: DataTransfer]> =
  produce((draft, literalIndex, offset, clipboard) => {
    // Since we always paste where the cursor is, then focus has to be at literalIndex and offset.
    // (Tests typically break this assertions)
    draft.focus = { ...literalIndex, cursorPosition: offset };

    if (clipboard.types.includes("text/html")) {
      insertHtmlClipboardInLetter(draft, clipboard);
    } else if (clipboard.types.includes("text/plain")) {
      insertTextInLetter(draft, clipboard.getData("text/plain"), FontType.PLAIN, false);
    } else {
      log("unsupported clipboard datatype(s) - " + JSON.stringify(clipboard.types));
    }
  });

export function logPastedClipboard(clipboardData: DataTransfer) {
  log("available paste types - ", clipboardData.types);
  log("pasted html content - " + clipboardData.getData("text/html"));
  log("pasted plain content - " + clipboardData.getData("text/plain"));
}

/**
 * Insert text in letter at specified index and offset.
 *
 * If multipartPaste is true then the function will try to do what is best in regard to preserving ids:
 * - will add a new literal instead of modifying current if offset is 0
 *
 * @param draft state to update
 * @param str text to insert
 * @param fontType fontType of text to insert
 * @param multipartPaste str is part of larger paste
 */
function insertTextInLetter(
  draft: Draft<LetterEditorState>,
  str: string,
  fontType: FontType,
  multipartPaste: boolean = true,
) {
  const updateContent = getInsertTextContentContext(draft);
  if (updateContent === undefined) {
    log("literalIndex is invalid ", { ...draft.focus });
    return;
  }
  const { content, parent, getContentIndex, setContentIndex } = updateContent;
  const offset = draft.focus.cursorPosition ?? 0;

  if (isLiteral(content)) {
    if (shouldModifyExistingLiteral(content, offset, multipartPaste, fontType)) {
      const existingText = text(content);
      const safeOffset = Math.min(Math.max(0, offset), existingText.length);

      const newText = existingText.slice(0, safeOffset) + str + existingText.slice(safeOffset);
      updateLiteralText(content, newText);
      draft.focus.cursorPosition = safeOffset + str.length;
    } else if (offset > 0 && offset < text(content).length) {
      // We can't modify existing literal due to other fontType, so we need to split it and insert pasted text in between.
      const literalPart2 = splitLiteralAtOffset(content, offset);
      const contentIndex = getContentIndex(draft.focus) + 1;

      addElements(
        [newLiteral({ editedText: str, fontType }), literalPart2],
        contentIndex,
        parent.content,
        parent.deletedContent,
      );
      setContentIndex(draft.focus, contentIndex);
      draft.focus.cursorPosition = str.length;
    } else {
      const insertAtIndex = offset <= 0 ? getContentIndex(draft.focus) : getContentIndex(draft.focus) + 1;
      addElements([newLiteral({ editedText: str, fontType })], insertAtIndex, parent.content, parent.deletedContent);
      setContentIndex(draft.focus, insertAtIndex);
      draft.focus.cursorPosition = str.length;
    }
    draft.isDirty = true;
  } else if (content !== undefined) {
    log(`cannot paste text into ${content.type}, must be literal.`);
  }
}

type InsertTextContext = {
  content: Draft<TextContent>;
  parent: {
    content: Draft<Content[]>;
    deletedContent: Draft<number[]>;
  };
  getContentIndex(focus: Focus): number;
  setContentIndex(focus: Draft<Focus>, value: number): void;
};

function getInsertTextContentContext(draft: Draft<LetterEditorState>): InsertTextContext | undefined {
  const literalIndex = draft.focus;
  const block = draft.redigertBrev.blocks[literalIndex.blockIndex];
  const blockContent = block?.content[literalIndex.contentIndex];

  if (isItemList(blockContent) && isItemContentIndex(literalIndex)) {
    const item = blockContent.items[literalIndex.itemIndex];
    return {
      content: item?.content[literalIndex.itemContentIndex],
      parent: item,
      getContentIndex: (focus) => (focus as ItemContentIndex).itemContentIndex,
      setContentIndex: (focus: Draft<Focus>, value: number) => {
        (focus as Draft<ItemContentIndex>).itemContentIndex = value;
      },
    };
  } else if (isTextContent(blockContent) && isBlockContentIndex(literalIndex)) {
    return {
      content: blockContent,
      parent: block,
      getContentIndex: (focus) => focus.contentIndex,
      setContentIndex: (focus: Draft<Focus>, value: number) => {
        focus.contentIndex = value;
      },
    };
  } else {
    return undefined;
  }
}

// We want to modify existing literal only if fontTypes matches, and it is either:
// - a new literal
// - we're inserting somewhere inside it (not start)
// - not part of a multipart-paste (which will include line breaks (paragraphs/items)
function shouldModifyExistingLiteral(
  literal: Draft<LiteralValue>,
  offset: number,
  multipartPaste: boolean,
  fontType: FontType,
): boolean {
  return (isNew(literal) || offset > 0 || !multipartPaste) && fontType === fontTypeOf(literal);
}

/**
 * Pasting funksjonalitet skal etterligne hvordan det fungerer i microsoft word.
 * Merk da at det er forskjellige regler hvis man limer inn i start/midten/slutt, på en literal, eller punktliste,
 * og om det kopierte innholdet er bare literal eller punktliste, eller begge.
 *
 */
function insertHtmlClipboardInLetter(draft: Draft<LetterEditorState>, clipboard: DataTransfer) {
  const parsedAndCombinedHtml = parseAndCombineHTML(clipboard);

  if (parsedAndCombinedHtml.length === 0) {
    //trenger ikke å lime inn tomt innhold
    return;
  } else if (parsedAndCombinedHtml.every((e) => e.type === "TEXT")) {
    parsedAndCombinedHtml.forEach((e) => {
      insertTextInLetter(draft, e.text, e.font, false);
    });
  } else {
    insertTraversedElements(draft, parsedAndCombinedHtml);
  }
  draft.isDirty = true;
}

function insertTraversedElements(draft: Draft<LetterEditorState>, elements: TraversedElement[]) {
  elements.forEach((el) => {
    switch (el.type) {
      case "TEXT": {
        insertTextInLetter(draft, el.text, el.font);
        break;
      }
      case "H1": {
        insertBlock(draft, "TITLE1", el.content);
        break;
      }
      case "H2": {
        insertBlock(draft, "TITLE2", el.content);
        break;
      }
      case "P": {
        insertBlock(draft, "PARAGRAPH", el.content);
        break;
      }
      case "ITEM": {
        insertItem(draft, el);
        break;
      }
    }
  });
}

function insertBlock(draft: Draft<LetterEditorState>, type: "TITLE1" | "TITLE2" | "PARAGRAPH", content: Text[]) {
  const focusedBlock = draft.redigertBrev.blocks[draft.focus.blockIndex];
  const blockContent = focusedBlock?.content[draft.focus.contentIndex];

  if (isBlockContentIndex(draft.focus) && isAtStartOfBlock(draft.focus)) {
    if (isEmptyBlock(focusedBlock)) {
      // If we're at the start of an empty block, we want to change it to whatever type we're inserting
      focusedBlock.type = type;
      insertTextElement(draft, content);
    } else {
      // If we're at the start of a non-empty block then we want to insert a new block to preserve IDs of existing block and it's content.
      const literals = content.map((t) => newLiteral({ editedText: t.text, fontType: t.font }));
      const block = type === "PARAGRAPH" ? newParagraph({ content: literals }) : newTitle({ type, content: literals });
      addElements([block], draft.focus.blockIndex, draft.redigertBrev.blocks, draft.redigertBrev.deletedBlocks);
      draft.focus.blockIndex += 1;
    }
  } else if (isItemContentIndex(draft.focus) && isAtStartOfItem(draft.focus) && isItemList(blockContent)) {
    // If we're at the start of an Item then we want to preserve any potential existing IDs, so we instead insert a new item.
    const literals = content.map((t) => newLiteral({ editedText: t.text, fontType: t.font }));
    addElements([newItem({ content: literals })], draft.focus.itemIndex, blockContent.items, blockContent.deletedItems);
    draft.focus.itemIndex++;
  } else {
    insertTextElement(draft, content);
  }
}

function insertTextElement(draft: Draft<LetterEditorState>, content: Text[]) {
  content.forEach((t) => {
    insertTextInLetter(draft, t.text, t.font);
  });
  splitRecipe(draft, draft.focus, draft.focus.cursorPosition ?? 0);
}

function insertItem(draft: Draft<LetterEditorState>, el: ItemElement) {
  const currentBlock = draft.redigertBrev.blocks[draft.focus.blockIndex];
  const blockContent = currentBlock.content[draft.focus.contentIndex];

  if (isItemContentIndex(draft.focus) && isItemList(blockContent)) {
    if (isAtStartOfItem(draft.focus)) {
      // If we're at the start of an Item then we want to preserve any potential existing IDs, so we instead insert a new item.
      const literals = el.content.map((t) => newLiteral({ editedText: t.text, fontType: t.font }));
      addElements(
        [newItem({ content: literals })],
        draft.focus.itemIndex,
        blockContent.items,
        blockContent.deletedItems,
      );
      draft.focus.itemIndex++;
    } else {
      insertTextElement(draft, el.content);
    }
  } else if (!isItemContentIndex(draft.focus) && !isItemList(blockContent)) {
    // We're also validating that current blockContent isn't an ItemList, because then the focus should also have been an ItemContentIndex.
    toggleItemListAndSplitAtCursor(draft, currentBlock);
    insertTextElement(draft, el.content);
  }
}

function toggleItemListAndSplitAtCursor(draft: Draft<LetterEditorState>, currentBlock: Draft<AnyBlock>) {
  let itemContent: TextContent[] = [];

  // Finn første indeks for all sammenhengende TextContent før der vi limer inn.
  const sentenceIndex = findAdjoiningContent(
    draft.focus.contentIndex,
    currentBlock.content.slice(0, draft.focus.contentIndex + 1),
    isTextContent,
  );

  // Om count er 1 så er det kun gjeldende content-element, så vi trenger ikke å inkludere flere.
  if (sentenceIndex.count > 1) {
    itemContent = removeElements(sentenceIndex.startIndex, sentenceIndex.count - 1, currentBlock).filter(isTextContent);
    // Vi har fjernet fra og med startIndex frem til draft.focus.contentIndex som betyr at den nye indeksen til current er startIndex
    draft.focus.contentIndex = sentenceIndex.startIndex;
  }

  // Split currentContent om vi limer inn et sted som ikke er i starten av den, og legg den til i itemContent og resten tilbake til blokken.
  const currentContent = currentBlock.content[draft.focus.contentIndex];
  if (isLiteral(currentContent) && (draft.focus.cursorPosition! ?? 0) > 0) {
    removeElements(draft.focus.contentIndex, 1, currentBlock);
    const afterSplit = splitLiteralAtOffset(currentContent, draft.focus.cursorPosition! ?? 0);

    if (text(afterSplit).length > 0) {
      addElements([afterSplit], draft.focus.contentIndex, currentBlock.content, []);
    }

    itemContent.push(currentContent);
  }

  // Sørg for at vårt nye item har noe content.
  if (itemContent.length === 0) {
    itemContent.push(newLiteral({ editedText: "" }));
  }
  const theItem = newItem({ content: itemContent });

  // Sett inn item i eksisterende liste før current content, eller lag en ny liste og theItem inn i den.
  const prevNonTextContent = sentenceIndex.startIndex > 0 ? currentBlock.content[sentenceIndex.startIndex - 1] : null;
  if (isItemList(prevNonTextContent)) {
    addElements([theItem], prevNonTextContent.items.length, prevNonTextContent.items, prevNonTextContent.deletedItems);
    // Sett fokus til det theItem som vi har satt inn i eksisterende ItemList.
    draft.focus = {
      ...draft.focus,
      contentIndex: sentenceIndex.startIndex - 1,
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

interface Text {
  type: "TEXT";
  font: FontType;
  text: string;
}

interface ItemElement {
  type: "ITEM";
  content: Text[];
}

interface ParagraphElement {
  type: "P";
  content: Text[];
}

interface Title1Element {
  type: "H1";
  content: Text[];
}

interface Title2Element {
  type: "H2";
  content: Text[];
}
type TextContainer = ParagraphElement | Title1Element | Title2Element | ItemElement;
type TraversedElement = TextContainer | Text;

function parseAndCombineHTML(clipboard: DataTransfer): TraversedElement[] {
  const parser = new DOMParser();
  const document = parser.parseFromString(clipboard.getData("text/html"), "text/html");

  const elements = traverseChildren(document.body, FontType.PLAIN);
  return moveOuterTextIntoNeighbouringParagraphs(elements);
}

function isOnlyWhitespace(node: Node) {
  return !!node.textContent && !/[^\t\n\r ]/.test(node.textContent);
}

function traverseChildren(element: Element, font: FontType): TraversedElement[] {
  const traversedChildNodes: TraversedElement[] = [...element.childNodes].flatMap((node) => {
    switch (node.nodeType) {
      case Node.TEXT_NODE: {
        if (!isOnlyWhitespace(node)) {
          const text = cleansePastedText(element.textContent ?? "");
          return text.length > 0 ? [{ type: "TEXT", font, text }] : [];
        } else {
          return [];
        }
      }
      case Node.ELEMENT_NODE: {
        return traverse(node as Element, font);
      }
      default: {
        return [];
      }
    }
  });
  return mergeNeighbouringText(traversedChildNodes);
}

function traverse(element: Element, font: FontType): TraversedElement[] {
  switch (element.tagName) {
    case "SPAN": {
      return traverseContainer(element, font);
    }

    case "STRONG":
    case "B": {
      // Since text in brevbaker-brev cannot be both bold (strong) and italic (emphasized) simultaneously, we pass along the first change (away from plain).
      const nextFont = font === FontType.PLAIN ? FontType.BOLD : font;
      return traverseContainer(element, nextFont);
    }

    case "EM":
    case "I": {
      // Since text in brevbaker-brev cannot be both bold (strong) and italic (emphasized) simultaneously, we pass along the first change (away from plain).
      const nextFont = font === FontType.PLAIN ? FontType.ITALIC : font;
      return traverseContainer(element, nextFont);
    }

    case "P": {
      if (element.children.length === 0) {
        const text = cleansePastedText(element.textContent ?? "");
        // Allowed with empty p-elements for line break
        return text.length >= 0 ? [{ type: "P", content: [{ type: "TEXT", font, text }] }] : [];
      } else {
        // traverse children, merge neighbouring plain text, and flatten any nested paragraphs and items
        return traverseParagraphChildren(element, font);
      }
    }

    case "LI": {
      return traverseTextContainer(element, "ITEM", font);
    }

    case "H1": {
      return traverseTextContainer(element, "H1", font);
    }

    case "H2": {
      return traverseTextContainer(element, "H2", font);
    }

    case "TABLE": {
      // TODO: ignore until support for table is implemented
      return [];
    }

    case "DIV": {
      // skip and traverse children
      return traverseChildren(element, font);
    }
    default: {
      return traverseChildren(element, font);
    }
  }
}

// Since we break (to new paragraph) AFTER a p-element, any preceding text-element will belong to it. So we modify
// our structure to reflect that.
function moveOuterTextIntoNeighbouringParagraphs(elements: TraversedElement[]): TraversedElement[] {
  return elements.reduceRight<TraversedElement[]>((acc, current) => {
    const next = acc?.at(0);
    if (current.type === "TEXT" && next?.type === "P") {
      return [{ ...next, content: mergeNeighbouringText([current, ...next.content]) }, ...acc.slice(1)];
    } else {
      return [current, ...acc];
    }
  }, []);
}

function traverseContainer(element: Element, font: FontType): TraversedElement[] {
  if (element.children.length === 0) {
    const text = cleansePastedText(element.textContent ?? "");
    return text.length > 0 ? [{ type: "TEXT", font, text }] : [];
  } else {
    return traverseChildren(element, font);
  }
}

function traverseTextContainer(element: Element, type: "ITEM" | "H1" | "H2", font: FontType): TraversedElement[] {
  if (element.children.length === 0) {
    const text = cleansePastedText(element.textContent ?? "");
    // allowed with empty list items
    return text.length >= 0 ? [{ type, content: [{ type: "TEXT", font, text }] }] : [];
  } else {
    const content = traverseChildren(element, font).flatMap((e) => {
      switch (e.type) {
        case "TEXT": {
          return e;
        }
        case "P":
        case "H2":
        case "H1":
        case "ITEM": {
          return e.content;
        }
      }
    });
    return [{ type, content }];
  }
}

// Will package any Text-elements into a Paragraph, and make sure we don't have nested paragraphs and items.
function traverseParagraphChildren(paragraph: Element, font: FontType): TextContainer[] {
  return traverseChildren(paragraph, font).reduce<TextContainer[]>((acc, current) => {
    const previous = acc.at(-1);

    switch (current.type) {
      case "H1":
      case "H2":
      case "P": {
        // should probably not be possible, since it would mean nested p-elements, but html-structures can be weird.
        // append paragraph
        return [...acc, current];
      }
      case "TEXT": {
        if (previous?.type === "P") {
          // insert into existing paragraph
          return [...acc.slice(0, -1), { ...previous, content: mergeNeighbouringText([...previous.content, current]) }];
        } else {
          // create a paragraph to contain the text
          return [...acc, { type: "P", content: [current] }];
        }
      }
      case "ITEM": {
        return [...acc, current];
      }
    }
  }, []);
}

function mergeNeighbouringText<T extends TraversedElement>(elements: T[]): T[] {
  return elements.reduce<T[]>((acc, curr) => {
    const previous = acc.at(-1);

    if (previous?.type === "TEXT" && curr.type === "TEXT" && previous?.font === curr.font) {
      return [...acc.slice(0, -1), { ...previous, text: cleansePastedText(previous.text + curr.text) }];
    } else {
      return [...acc, curr];
    }
  }, []);
}

function cleansePastedText(str: string): string {
  return cleanseText(str).replaceAll(/\s+/g, " ");
}

// eslint-disable-next-line @typescript-eslint/no-explicit-any
function log(message: string, ...obj: any[]) {
  // eslint-disable-next-line no-console
  console.log("Skribenten:pasteHandler: " + message, ...obj);
}
