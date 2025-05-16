import type { Draft } from "immer";
import { produce } from "immer";

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
import type { AnyBlock, LiteralValue, TextContent } from "~/types/brevbakerTypes";

import { isEmptyContent, isItemList, isLiteral, isTextContent } from "../model/utils";

export const paste: Action<LetterEditorState, [literalIndex: LiteralIndex, offset: number, clipboard: DataTransfer]> =
  produce((draft, literalIndex, offset, clipboard) => {
    // Siden man alltid limer inn der markøren står, så må focus være det samme som literalIndex (tester bryter typisk med denne forutsetningen).
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

  if (isItemList(content) && "itemContentIndex" in literalIndex) {
    const item = content.items[literalIndex.itemIndex];
    const itemContent = item?.content[literalIndex?.itemContentIndex];
    if (item && isLiteral(itemContent)) {
      if (isEmptyContent(itemContent) || offset > 0 || !multipartPaste) {
        insertText(itemContent, draft.focus, offset, str);
      } else {
        const newIndex = offset <= 0 ? literalIndex.itemContentIndex : literalIndex.itemContentIndex + 1;
        addElements([newLiteral({ text: "", editedText: str })], newIndex, item.content, item.deletedContent);
        draft.focus = { ...literalIndex, itemContentIndex: newIndex, cursorPosition: str.length };
      }
      draft.isDirty = true;
    } else {
      log("cannot insert text into variable - " + str);
    }
  } else if (block && isLiteral(content) && !("itemContentIndex" in literalIndex)) {
    if (isEmptyContent(content) || offset > 0 || !multipartPaste) {
      insertText(content, draft.focus, offset, str);
    } else {
      const newIndex = offset <= 0 ? literalIndex.contentIndex : literalIndex.contentIndex + 1;
      addElements([newLiteral({ text: "", editedText: str })], newIndex, block.content, block.deletedContent);
      draft.focus = { ...literalIndex, contentIndex: newIndex, cursorPosition: str.length };
    }
    draft.isDirty = true;
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
}

/**
 * Pasting funksjonalitet skal etterligne hvordan det fungerer i microsoft word.
 * Merk da at det er forskjellige regler hvis man limer inn i start/midten/slutt, på en literal, eller punktliste,
 * og om det kopierte innholdet er bare literal eller punktliste, eller begge.
 *
 * det manuelt som her.
 */
function insertHtmlClipboardInLetter(
  draft: Draft<LetterEditorState>,
  literalIndex: LiteralIndex,
  offset: number,
  clipboard: DataTransfer,
) {
  const parsedAndCombinedHtml = parseAndCombineHTML(clipboard);
  const isInsertingOnlyWords = parsedAndCombinedHtml[0]?.tag === "SPAN";

  if (parsedAndCombinedHtml.length === 0) {
    //trenger ikke å lime inn tomt innhold
    return;
  } else if (isInsertingOnlyWords) {
    insertTextInLetter(draft, literalIndex, offset, parsedAndCombinedHtml[0].content.join(" "), false);
  } else {
    insertTraversedElements(draft, parsedAndCombinedHtml);
  }
  draft.isDirty = true;
}

function insertTraversedElements(draft: Draft<LetterEditorState>, elements: TraversedElement[]) {
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

function parseAndCombineHTML(clipboard: DataTransfer) {
  const parser = new DOMParser();
  const document = parser.parseFromString(clipboard.getData("text/html"), "text/html");

  const result = [...document.body.children].flatMap(traverseElement);
  return mergeNeighboringTags(result);
}

interface TraversedElement {
  tag: "P" | "LI" | "SPAN";
  content: (TraversedElement | string)[];
}

/**
 * Flat ut html-struktur
 */
const traverseElement = (element: Element): TraversedElement[] | TraversedElement => {
  switch (element.tagName) {
    case "LI": {
      if (element.children.length > 0) {
        const content = [...element.children].flatMap(traverseElement).flatMap((e) => e.content);
        return { tag: "LI", content: content };
      }

      const textContent = element.textContent?.trim();
      return { tag: "LI", content: textContent ? [textContent] : [] };
    }
    case "P": {
      if (element.children.length > 0) {
        const content = [...element.children].flatMap(traverseElement).flatMap((el) => el.content);

        //dersom content er tom, betyr det at den bar inneholdt en tom span, som skal være linjeskift
        return { tag: "P", content: content.length > 0 ? content : [" "] };
      }

      //p elementet burde inneholde andre elementer, men dersom den ikke gjør det og kun inneholder tekst, skal den returneres som en string
      const textContent = element.textContent?.trim();
      return { tag: "P", content: textContent ? [textContent] : [] };
    }

    case "SPAN": {
      if (element.children.length > 0) {
        const content = [...element.children].flatMap(traverseElement).flatMap((el) => el.content);
        return { tag: "SPAN", content: content };
      }
      const textContent = element.textContent?.trim();
      return { tag: "SPAN", content: textContent ? [textContent] : [] };
    }

    case "DIV": {
      return [...element.children].flatMap(traverseElement);
    }
    case "TABLE": {
      return [];
    }
    default: {
      return [...element.children].flatMap(traverseElement);
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

function log(message: string) {
  // eslint-disable-next-line no-console
  console.log("Skribenten:pasteHandler: " + message);
}
