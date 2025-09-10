import DOMPurify from "dompurify";
import type { Draft } from "immer";

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
  isTable,
  newColSpec,
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
import { type Action, withPatches } from "~/Brevredigering/LetterEditor/lib/actions";
import type {
  Focus,
  ItemContentIndex,
  LetterEditorState,
  LiteralIndex,
  TableCellIndex,
} from "~/Brevredigering/LetterEditor/model/state";
import type {
  AnyBlock,
  Cell,
  ColumnSpec,
  Content,
  LiteralValue,
  Row,
  Table as BrevbakerTable,
  TextContent,
} from "~/types/brevbakerTypes";
import { FontType, TABLE } from "~/types/brevbakerTypes";

import { isEmptyBlock, isItemList, isLiteral, isParagraph, isTableCellIndex, isTextContent } from "../model/utils";

export const paste: Action<LetterEditorState, [literalIndex: LiteralIndex, offset: number, clipboard: DataTransfer]> =
  withPatches((draft, literalIndex, offset, clipboard) => {
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
    draft.saveStatus = "DIRTY";
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

// Ensures there's a Literal at `index` inside a TextContent[]; creates one if needed.
function ensureLiteralAtIndex(arr: Draft<TextContent[]>, index: number): Draft<LiteralValue> {
  while (arr.length <= index) {
    arr.push(newLiteral({ editedText: "" }));
  }
  const elementAtIndex = arr.at(index);
  if (isLiteral(elementAtIndex)) return elementAtIndex;
  const lit = newLiteral({ editedText: "" });
  arr.splice(index, 0, lit);
  return lit;
}

function getInsertTextContentContext(draft: Draft<LetterEditorState>): InsertTextContext | undefined {
  const focus = draft.focus;
  const currentBlock = draft.redigertBrev.blocks[focus.blockIndex];

  const blockContent = currentBlock.content[focus.contentIndex];

  if (isTable(blockContent) && isTableCellIndex(focus)) {
    const row = blockContent.rows[focus.rowIndex];
    const cell = row?.cells[focus.cellIndex];
    if (!cell) return undefined;

    // Make sure we have a literal to edit at the target slot
    const literal = ensureLiteralAtIndex(cell.text, focus.cellContentIndex ?? 0);

    return {
      content: literal,
      parent: {
        content: cell.text,
        deletedContent: [],
      },
      getContentIndex: () => focus.cellContentIndex ?? 0,
      setContentIndex: (f, value) => {
        (f as Draft<TableCellIndex>).cellContentIndex = value;
      },
    };
  }

  if (isItemList(blockContent) && isItemContentIndex(focus)) {
    const currentItem = blockContent.items[focus.itemIndex];
    return {
      content: currentItem.content[focus.itemContentIndex],
      parent: currentItem,
      getContentIndex: (currentFocus) => (currentFocus as ItemContentIndex).itemContentIndex,
      setContentIndex: (focusToUpdate: Draft<Focus>, newItemContentIndex: number) => {
        (focusToUpdate as Draft<ItemContentIndex>).itemContentIndex = newItemContentIndex;
      },
    };
  }

  if (isTextContent(blockContent) && isBlockContentIndex(focus)) {
    return {
      content: blockContent,
      parent: currentBlock,
      getContentIndex: (currentFocus) => currentFocus.contentIndex,
      setContentIndex: (focusToUpdate: Draft<Focus>, newContentIndex: number) => {
        focusToUpdate.contentIndex = newContentIndex;
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
  draft.saveStatus = "DIRTY";
}

// Ensure that every table row has exactly the same number of cells (colCount)
function normalizeCells(cells: ReadonlyArray<TableCell>, colCount: number): TableCell[] {
  return Array.from({ length: Math.max(0, colCount) }, (_, i) => ({
    content: cells[i]?.content ?? [],
  }));
}

function createCell(cell: TableCell): Cell {
  return {
    id: null,
    parentId: null,
    text:
      cell.content.length > 0
        ? cell.content.map((txt) => newLiteral({ editedText: txt.text, fontType: txt.font }))
        : [newLiteral({ editedText: "" })],
  };
}

function createRow(source: TableRow, colCount: number): Row {
  return {
    id: null,
    parentId: null,
    cells: normalizeCells(source.cells, colCount).map(createCell),
  };
}

function createTable(colSpec: ColumnSpec[], rows: Row[]): BrevbakerTable {
  return {
    type: TABLE,
    id: null,
    parentId: null,
    header: { id: null, parentId: null, colSpec },
    deletedRows: [],
    rows,
  };
}

// Insert a parsed table element at current focus.
function insertTable(draft: Draft<LetterEditorState>, el: Table) {
  if (isTableCellIndex(draft.focus)) {
    return;
  }
  if ((el.headerCells?.length ?? 0) === 0 && el.rows.length === 0) {
    return;
  }

  const headerCells = el.headerCells ?? [];
  // Determine the column count if there’s no header.
  const bodyColMax = Math.max(1, ...el.rows.map((row) => row.cells.length));
  //if there’s a header, use its length; otherwise, use the widest body row.
  const colCount = headerCells.length > 0 ? headerCells.length : bodyColMax;
  // If there are no header cells, we promote the first row to header.
  // This is to ensure that the table has a header row if it was pasted without one (pasted from Word for example)
  const hasHeader = headerCells.length > 0;
  const shouldPromoteFirstRowToHeader = !hasHeader && el.rows.length > 0;

  const getHeaderSpec = (cells: TableCell[]) =>
    cells.map((cell) => {
      const mergedTexts = mergeNeighbouringText(cell.content);
      return {
        text: cleansePastedText(mergedTexts.map((txt) => txt.text).join(" ")),
        font: mergedTexts[0]?.font ?? FontType.PLAIN,
      };
    });

  const headerSpecSource = hasHeader
    ? getHeaderSpec(headerCells)
    : getHeaderSpec(normalizeCells(el.rows[0]?.cells ?? [], colCount));

  const colSpec = newColSpec(colCount, headerSpecSource);
  const bodyRows = shouldPromoteFirstRowToHeader ? el.rows.slice(1) : el.rows;

  const rows: Row[] = bodyRows.map((row) => createRow(row, colCount));

  const table = createTable(colSpec, rows);

  const currentBlock = draft.redigertBrev.blocks[draft.focus.blockIndex];
  if (isBlockContentIndex(draft.focus) && isParagraph(currentBlock)) {
    // Split current literal at cursor so trailing text stays after table.
    splitRecipe(draft, draft.focus, draft.focus.cursorPosition ?? 0);

    const tableIndex = draft.focus.contentIndex + 1;
    addElements([table], tableIndex, currentBlock.content, currentBlock.deletedContent);

    const focusRowIndex = rows.length > 0 ? 0 : -1;

    draft.focus = {
      blockIndex: draft.focus.blockIndex,
      contentIndex: tableIndex,
      rowIndex: focusRowIndex,
      cellIndex: 0,
      cellContentIndex: 0,
      cursorPosition: 0,
    };

    draft.saveStatus = "DIRTY";
  }
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
      case "TABLE": {
        insertTable(draft, el);
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
interface TableCell {
  content: Text[];
}

interface TableRow {
  cells: TableCell[];
}

interface Table {
  type: "TABLE";
  rows: TableRow[];
  headerCells?: TableCell[];
}

type TraversedElement = ParagraphElement | Text | ItemElement | Title1Element | Title2Element | Table;

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
      "h1",
      "h2",
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

function isOnlyWhitespace(node: Node) {
  return !!node.textContent && !/[^\t\n\r ]/.test(node.textContent);
}

function traverseChildren(element: Element, font: FontType): TraversedElement[] {
  const traversedChildNodes: TraversedElement[] = [...element.childNodes].flatMap((node) => {
    switch (node.nodeType) {
      case Node.TEXT_NODE: {
        if (!isOnlyWhitespace(node)) {
          const text = cleansePastedText(node.textContent ?? "");
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

function traverseTable(element: Element, font: FontType): Table {
  const flattenCellContent = (cellElement: Element) => {
    const cellContentElements = traverseChildren(cellElement, font).flatMap((child) => {
      switch (child.type) {
        case "TEXT":
          return [child];
        case "P":
        case "ITEM":
          return child.content;
        case "H1":
        case "H2":
        case "TABLE":
          return [];
        default:
          return [];
      }
    });
    // Drop whitespace-only TEXT nodes so the cell content contains only meaningful text.
    const filteredCellTextContent = cellContentElements.filter(
      (txt) => txt.type !== "TEXT" || txt.text.trim().length > 0,
    );
    return filteredCellTextContent;
  };

  let rowElements = Array.from(element.querySelectorAll("tr"));
  let headerCells: TableCell[] | undefined;

  const theadRow = element.querySelector("thead tr");
  if (theadRow) {
    const headerCellElements = Array.from(theadRow.querySelectorAll("th"));
    if (headerCellElements.length > 0) {
      headerCells = headerCellElements.map((cellElement) => ({
        content: flattenCellContent(cellElement),
      }));
      // Remove the header row from body rows to avoid duplicating it as a regular row.
      rowElements = rowElements.filter((row) => row !== theadRow);
    }
  }

  if (!headerCells && rowElements.length > 0) {
    const firstRow = rowElements[0];
    const headerCellElements = Array.from(firstRow.querySelectorAll("th"));
    if (headerCellElements.length > 0) {
      headerCells = headerCellElements.map((cellElement) => ({
        content: flattenCellContent(cellElement),
      }));
      rowElements = rowElements.slice(1);
    }
  }

  const tableRows: TableRow[] = rowElements
    .map((rowEl) => ({
      cells: Array.from(rowEl.querySelectorAll("td,th")).map((cellEl) => ({
        content: flattenCellContent(cellEl),
      })),
    }))
    .filter((row) => row.cells.length > 0);

  return {
    type: "TABLE",
    rows: tableRows,
    headerCells,
  };
}

function traverseTextContainer(element: Element, type: "ITEM" | "H1" | "H2", font: FontType): TraversedElement[] {
  if (element.children.length === 0) {
    const sanitizedText = cleansePastedText(element.textContent ?? "");
    // allowed with empty list items
    return sanitizedText.length >= 0 ? [{ type, content: [{ type: "TEXT", font, text: sanitizedText }] }] : [];
  } else {
    const childElements = traverseChildren(element, font).flatMap((child) => {
      switch (child.type) {
        case "TEXT": {
          return [child];
        }
        case "P":
        case "H2":
        case "H1":
        case "ITEM": {
          return child.content;
        }
        case "TABLE": {
          return [];
        }
      }
    });
    return [{ type, content: childElements }];
  }
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
      if (element.children.length === 0) {
        const text = cleansePastedText(element.textContent ?? "");

        return text.length >= 0 ? [{ type: "ITEM", content: [{ type: "TEXT", font: font, text }] }] : [];
      } else {
        return [{ type: "ITEM", content: traverseItemChildren(element, font) }];
      }
    }

    case "H1": {
      return traverseTextContainer(element, "H1", font);
    }

    case "H2": {
      return traverseTextContainer(element, "H2", font);
    }

    case "TABLE": {
      return [traverseTable(element, font)];
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

function traverseItemChildren(item: Element, font: FontType): Text[] {
  return traverseChildren(item, font).flatMap((traversedElement) => {
    switch (traversedElement.type) {
      case "TEXT": {
        return [traversedElement];
      }
      case "ITEM":
      case "P":
      case "H1":
      case "H2": {
        return traversedElement.content;
      }
      case "TABLE": {
        // Should not happen, but if it does, we just ignore it.
        return [];
      }
    }
  });
}

type ParagraphChild = ParagraphElement | ItemElement | Title1Element | Title2Element;

function traverseParagraphChildren(paragraph: Element, font: FontType): ParagraphChild[] {
  const result: ParagraphChild[] = [];
  let buffer: Text[] = [];

  const flushBuffer = () => {
    if (buffer.length > 0) {
      result.push({ type: "P", content: buffer });
      buffer = [];
    }
  };

  for (const node of traverseChildren(paragraph, font)) {
    switch (node.type) {
      case "TEXT": {
        buffer.push(node);
        break;
      }

      case "ITEM":
      case "H1":
      case "H2":
      case "P":
        flushBuffer();
        result.push(node);
        break;

      case "TABLE": {
        // Tables should not be nested inside <p>. Ignore.
        flushBuffer();
        break;
      }
    }
  }

  flushBuffer();
  return result;
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
