import DOMPurify from "dompurify";
import type { Draft } from "immer";
import { produce } from "immer";

import {
  addElements,
  findAdjoiningContent,
  fontTypeOf,
  isAtStartOfBlock,
  isBlockContentIndex,
  isItemContentIndex,
  isNew,
  newItem,
  newItemList,
  newLiteral,
  newParagraph,
  newTable,
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
import type { AnyBlock, Content, LiteralValue, Row, TextContent } from "~/types/brevbakerTypes";
import { FontType, PARAGRAPH, TABLE } from "~/types/brevbakerTypes";

import { isItemList, isLiteral, isTextContent } from "../model/utils";

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
  const litIdx = draft.focus;
  const block = draft.redigertBrev.blocks[litIdx.blockIndex];

  const blockContent = block.content[litIdx.contentIndex];

  if (blockContent?.type === TABLE && isItemContentIndex(litIdx)) {
    const row = blockContent.rows[litIdx.itemIndex];
    const cell = row?.cells[litIdx.itemContentIndex];

    if (!cell) return undefined;

    return {
      content: cell.text[0],
      parent: {
        content: cell.text as Draft<TextContent[]>,
        deletedContent: [] as Draft<number[]>,
      },
      getContentIndex: (currentFocus) => (currentFocus as ItemContentIndex).itemContentIndex,

      setContentIndex: (focusToUpdate: Draft<Focus>, newItemContentIndex: number) => {
        (focusToUpdate as Draft<ItemContentIndex>).itemContentIndex = newItemContentIndex;
      },
    };
  }

  if (isItemList(blockContent) && isItemContentIndex(litIdx)) {
    const item = blockContent.items[litIdx.itemIndex];
    return {
      content: item.content[litIdx.itemContentIndex],
      parent: item,
      getContentIndex: (f) => (f as ItemContentIndex).itemContentIndex,
      setContentIndex: (f: Draft<Focus>, v: number) => {
        (f as Draft<ItemContentIndex>).itemContentIndex = v;
      },
    };
  }

  if (isTextContent(blockContent) && isBlockContentIndex(litIdx)) {
    return {
      content: blockContent,
      parent: block,
      getContentIndex: (f) => f.contentIndex,
      setContentIndex: (f: Draft<Focus>, v: number) => {
        f.contentIndex = v;
      },
    };
  }

  return undefined;
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
      case "P": {
        // Om vi er på starten av en blokk så ønsker vi å sette inn en ny blokk før den for preservere ID'er på gjeldende
        if (isBlockContentIndex(draft.focus) && isAtStartOfBlock(draft.focus)) {
          const literals = el.content.map((t) => newLiteral({ editedText: t.text, fontType: t.font }));
          addElements(
            [newParagraph({ content: literals })],
            draft.focus.blockIndex,
            draft.redigertBrev.blocks,
            draft.redigertBrev.deletedBlocks,
          );
          draft.focus.blockIndex += 1;
        } else {
          el.content.forEach((t) => {
            insertTextInLetter(draft, t.text, t.font);
          });
          splitRecipe(draft, draft.focus, draft.focus.cursorPosition! ?? 0);
        }
        break;
      }
      case "ITEM": {
        const currentBlock = draft.redigertBrev.blocks[draft.focus.blockIndex];

        if (isItemContentIndex(draft.focus) && isItemList(currentBlock.content[draft.focus.contentIndex])) {
          el.content.forEach((t) => {
            insertTextInLetter(draft, t.text, t.font);
          });
          splitRecipe(draft, draft.focus, draft.focus.cursorPosition! ?? 0);
        } else if (!isItemContentIndex(draft.focus) && !isItemList(currentBlock.content[draft.focus.contentIndex])) {
          // vi sjekker også at gjeldende ikke er itemList fordi da _må_ også focus være ItemContentIndex.
          toggleItemListAndSplitAtCursor(draft, currentBlock);

          el.content.forEach((t) => {
            insertTextInLetter(draft, t.text, t.font);
          });
          splitRecipe(draft, draft.focus, draft.focus.cursorPosition ?? 0);
        }
        break;
      }
      case "TABLE": {
        const tableContent: Table = {
          type: TABLE,
          id: null,
          parentId: null,
          header: { id: null, parentId: null, colSpec: [] },
          deletedRows: [],
          rows: el.rows.map((r) => ({
            id: null,
            parentId: null,
            cells: r.cells.map((c) => ({
              id: null,
              parentId: null,
              text: c.content.map((t) => newLiteral({ editedText: t.text, fontType: t.font })),
            })),
          })),
        };

        /* Insert table *inside* the current paragraph’s content[].  
           We split paragraph first if caret is mid-literal. */
        const curBlock = draft.redigertBrev.blocks[draft.focus.blockIndex];

        if (isBlockContentIndex(draft.focus) && curBlock.type === PARAGRAPH) {
          // split at cursor so we insert after the current literal
          splitRecipe(draft, draft.focus, draft.focus.cursorPosition ?? 0);

          addElements([tableContent], draft.focus.contentIndex + 1, curBlock.content, curBlock.deletedContent);

          // focus now points to the first cell
          draft.focus = {
            blockIndex: draft.focus.blockIndex, // same paragraph
            contentIndex: draft.focus.contentIndex + 1, // the table element
            itemIndex: 0, // row 0
            itemContentIndex: 0, // cell 0
            cursorPosition: 0,
          };
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

interface Item {
  type: "ITEM";
  content: Text[];
}

interface Paragraph {
  type: "P";
  content: Text[];
}

interface TableCell {
  content: Text[];
}

interface TableRow {
  cells: TableCell[];
}

interface Table {
  type: "TABLE";
  rows: TableRow[];
}

type TraversedElement = Paragraph | Text | Item | Table;

/** Return clipboard HTML or plain text, sanitised through DOMPurify. */
function getCleanClipboardMarkup(dt: DataTransfer): string {
  const raw = dt.types.includes("text/html") ? dt.getData("text/html") : dt.getData("text/plain");

  return DOMPurify.sanitize(raw, {
    ALLOWED_TAGS: [
      "p",
      "br",
      "strong",
      "b",
      "em",
      "i",
      "ul",
      "ol",
      "li",
      "table",
      "thead",
      "tbody",
      "tr",
      "td",
      "th",
      "span",
    ],
    ALLOWED_ATTR: ["rowspan", "colspan"],
  });
}

function parseAndCombineHTML(clipboard: DataTransfer): TraversedElement[] {
  const cleanHtml = getCleanClipboardMarkup(clipboard);
  const document = new DOMParser().parseFromString(cleanHtml, "text/html");

  const elements = traverseChildren(document.body, FontType.PLAIN);
  return moveOuterTextIntoNeighbouringParagraphs(elements);
}

function traverseChildren(element: Element, font: FontType): TraversedElement[] {
  const traversedChildNodes: TraversedElement[] = [...element.childNodes].flatMap((node) => {
    switch (node.nodeType) {
      case Node.TEXT_NODE: {
        const txt = cleansePastedText((node.textContent ?? "").trim());

        return txt.length > 0 ? [{ type: "TEXT", font, text: txt }] : [];
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

function traverseTable(element: HTMLTableElement, font: FontType): Table {
  const rows: TableRow[] = [];

  element.querySelectorAll("tr").forEach((tr) => {
    const cells: TableCell[] = [];

    tr.querySelectorAll("th,td").forEach((td) => {
      const content = traverseChildren(td, font).flatMap((e) => {
        if (e.type === "TEXT") return [e];
        if (e.type === "P") return e.content;
        if (e.type === "ITEM") return e.content;
        return [];
      });
      cells.push({ content });
    });

    if (cells.length > 0) rows.push({ cells });
  });

  // eslint-disable-next-line no-console
  console.log("Paste traversal to Table node:", {
    rows: rows.length,
    cellsPerRow: rows.map((r) => r.cells.length),
    sampleCell: rows[0]?.cells[0]?.content?.[0],
  });

  return { type: "TABLE", rows };
}

function traverse(element: Element, font: FontType): TraversedElement[] {
  switch (element.tagName) {
    case "SPAN": {
      return traverseTextContainer(element, font);
    }

    case "STRONG":
    case "B": {
      const nextFont = font === FontType.PLAIN ? FontType.BOLD : font;
      return traverseTextContainer(element, nextFont);
    }

    case "EM":
    case "I": {
      const nextFont = font === FontType.PLAIN ? FontType.ITALIC : font;
      return traverseTextContainer(element, nextFont);
    }

    case "P": {
      if (element.children.length === 0) {
        const text = cleansePastedText(element.textContent ?? "");

        return text.length >= 0 ? [{ type: "P", content: [{ type: "TEXT", font, text }] }] : [];
      } else {
        return traverseParagraphChildren(element, font);
      }
    }

    case "LI": {
      if (element.children.length === 0) {
        const text = cleansePastedText(element.textContent ?? "");

        return text.length >= 0 ? [{ type: "ITEM", content: [{ type: "TEXT", font: font, text }] }] : [];
      } else {
        return [{ type: "ITEM", content: traverseItemChildren(element, font) }];
      }
    }

    case "TABLE": {
      return [traverseTable(element as HTMLTableElement, font)];
    }

    // case "DIV": {
    //   // skip and traverse children
    //   return traverseChildren(element, font);
    // }
    default: {
      return traverseChildren(element, font);
    }
  }
}

// Since we break (to new paragraph) after a p-element, any preceding text-element will belong to it. So we modify
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

function traverseTextContainer(element: Element, font: FontType): TraversedElement[] {
  if (element.children.length === 0) {
    const text = cleansePastedText(element.textContent ?? "");
    return text.length > 0 ? [{ type: "TEXT", font, text }] : [];
  } else {
    return traverseChildren(element, font);
  }
}

function traverseItemChildren(item: Element, font: FontType): Text[] {
  return traverseChildren(item, font).flatMap((e) => {
    switch (e.type) {
      case "TEXT": {
        return e;
      }
      case "ITEM": {
        return e.content;
      }
      case "P": {
        return e.content;
      }
    }
  });
}

// Will package any Text-elements into a Paragraph, and make sure we don't have nested paragraphs and items.
// function traverseParagraphChildren(paragraph: Element, font: FontType): (Paragraph | Item)[] {
//   return traverseChildren(paragraph, font).reduce<(Paragraph | Item)[]>((acc, current) => {
//     const previous = acc.at(-1);

//     switch (current.type) {
//       case "P": {
//         // should probably not be possible, since it would mean nested p-elements, but html-structures can be weird.
//         // append paragraph
//         return [...acc, current];
//       }
//       case "TEXT": {
//         if (previous?.type === "P") {
//           // insert into existing paragraph
//           return [...acc.slice(0, -1), { ...previous, content: mergeNeighbouringText([...previous.content, current]) }];
//         } else {
//           // create a paragraph to contain the text
//           return [...acc, { type: "P", content: [current] }];
//         }
//       }
//       case "ITEM": {
//         return [...acc, current];
//       }
//     }
//   }, []);
// }

function traverseParagraphChildren(paragraph: Element, font: FontType): (Paragraph | Item)[] {
  const result: (Paragraph | Item)[] = [];
  let buffer: Text[] = [];

  /** Flush any buffered TEXT nodes into a Paragraph element. */
  const flushBuffer = () => {
    if (buffer.length > 0) {
      result.push({ type: "P", content: buffer });
      buffer = [];
    }
  };

  // Walk immediate children that our earlier traversal returned
  for (const node of traverseChildren(paragraph, font)) {
    switch (node.type) {
      case "TEXT": {
        buffer = mergeNeighbouringText([...buffer, node]) as Text[];
        break;
      }

      case "ITEM": {
        flushBuffer();
        result.push(node);
        break;
      }

      case "P": {
        flushBuffer();
        result.push(node);
        break;
      }
    }
  }

  flushBuffer();
  return result;
}

function mergeNeighbouringText<T extends TraversedElement>(elements: T[]): T[] {
  return elements.reduce<T[]>((acc, curr) => {
    const prev = acc.at(-1);

    if (prev?.type === "TEXT" && curr.type === "TEXT" && prev.font === curr.font) {
      // We already sanitised both text nodes earlier, so just concatenate.
      return [...acc.slice(0, -1), { ...prev, text: prev.text + curr.text }];
    }

    return [...acc, curr];
  }, []);
}

function cleansePastedText(str: string): string {
  return str.replace(/\u00A0/g, " ").replace(/\s+/g, " ");
}
// eslint-disable-next-line @typescript-eslint/no-explicit-any
function log(message: string, ...obj: any[]) {
  // eslint-disable-next-line no-console
  console.log("Skribenten:pasteHandler: " + message, ...obj);
}
