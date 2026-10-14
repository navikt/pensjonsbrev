import DOMPurify from "dompurify";
import { type Draft } from "immer";

import {
  addElements,
  findAdjoiningContent,
  fontTypeOf,
  isAtStartOfBlock,
  isAtStartOfItem,
  isBlockContentIndex,
  isItemContentIndex,
  isNew,
  isTable,
  isValidIndex,
  newColSpec,
  newItem,
  newItemList,
  newLiteral,
  newParagraph,
  newTitle,
  removeElements,
  splitLiteralAtOffset,
  splitMixedListBlock,
  text,
} from "~/Brevredigering/LetterEditor/actions/common";
import { deleteSelectionRecipe } from "~/Brevredigering/LetterEditor/actions/deleteSelection";
import { getRtfClipboardData, parseRtfClipboard } from "~/Brevredigering/LetterEditor/actions/rtf/parseRtfClipboard";
import { splitRecipe } from "~/Brevredigering/LetterEditor/actions/split";
import {
  cleansePastedText,
  type ItemElement,
  mergeNeighbouringText,
  type ParagraphElement,
  type Table,
  type TableCell,
  type TableRow,
  type Text,
  type Title1Element,
  type Title2Element,
  type Title3Element,
  type TraversedElement,
} from "~/Brevredigering/LetterEditor/actions/traversedElement";
import { updateLiteralText } from "~/Brevredigering/LetterEditor/actions/updateContentText";
import { type Action, withPatches } from "~/Brevredigering/LetterEditor/lib/actions";
import {
  type Focus,
  type ItemContentIndex,
  type LetterEditorState,
  type LiteralIndex,
  type SelectionIndex,
  type TableCellIndex,
} from "~/Brevredigering/LetterEditor/model/state";
import {
  type AnyBlock,
  type Table as BrevbakerTable,
  type Cell,
  type ColumnSpec,
  type Content,
  FontType,
  type ItemList,
  ListType,
  type LiteralValue,
  type Row,
  type TextContent,
  TITLE_INDEX,
} from "~/types/brevbakerTypes";
import { getPasteMetadata } from "~/utils/pasteTracking";

import {
  effectiveListType,
  isEmptyBlock,
  isEmptyItem,
  isItemList,
  isLiteral,
  isParagraph,
  isTableCellIndex,
  isTextContent,
} from "../model/utils";

export const paste: Action<LetterEditorState, [literalIndex: LiteralIndex, offset: number, clipboard: DataTransfer]> =
  withPatches((draft, literalIndex, offset, clipboard) => {
    if (literalIndex.blockIndex === TITLE_INDEX) {
      return;
    }
    // Since we always paste where the cursor is, then focus has to be at literalIndex and offset.
    // (Tests typically break this assertions)
    draft.focus = { ...literalIndex, cursorPosition: offset };

    const insertion = resolveClipboardInsertion(draft, draft.focus, clipboard);
    if (insertion) insertInLetter(draft, insertion);
  });

export const pasteReplacingSelection: Action<LetterEditorState, [selection: SelectionIndex, clipboard: DataTransfer]> =
  withPatches((draft, selection, clipboard) => {
    if (selection.start.blockIndex === TITLE_INDEX) {
      return;
    }
    if (!isValidIndex(draft.redigertBrev, selection.start) || !isValidIndex(draft.redigertBrev, selection.end)) {
      return;
    }
    // The selection is only deleted if the clipboard has something we can insert where it starts.
    const insertion = resolveClipboardInsertion(draft, selection.start, clipboard);
    if (!insertion) return;

    deleteSelectionRecipe(draft, selection);

    // After deletion, draft.focus is set to the collapsed position where the selection was.
    // Now paste at that position.
    insertInLetter(draft, insertion);
  });

type ClipboardInsertion = { kind: "elements"; elements: TraversedElement[] } | { kind: "text"; text: string };

/**
 * Picks what to insert before the letter is changed: HTML, then RTF, then plain text, using the first
 * one that has something insertable at `focus`. Returns undefined if nothing can be inserted.
 */
function resolveClipboardInsertion(
  draft: Draft<LetterEditorState>,
  focus: Focus,
  clipboard: DataTransfer,
): ClipboardInsertion | undefined {
  const insertable = (insertion: ClipboardInsertion | undefined) => insertion && fitToFocus(draft, focus, insertion);

  if (clipboard.types.includes("text/html")) {
    const fromHtml = insertable({
      kind: "elements",
      elements: parseHtmlToTraversedElements(clipboard.getData("text/html")),
    });
    if (fromHtml) return fromHtml;
  }

  const rtf = getRtfClipboardData(clipboard);
  if (rtf !== undefined) {
    const fromRtf = insertable(rtfInsertion(rtf));
    if (fromRtf) return fromRtf;
  }

  if (clipboard.types.includes("text/plain")) {
    return insertable({ kind: "text", text: clipboard.getData("text/plain") });
  }
  if (rtf === undefined && !clipboard.types.includes("text/html")) {
    log(`unsupported clipboard datatype(s) - ${JSON.stringify(clipboard.types)}`);
  }
  return undefined;
}

/**
 * What `insertion` becomes at `focus`, or undefined if it can't be inserted there.
 *
 * A table cell only takes bold and italic text: paragraphs are joined, and anything with more structure
 * (headings, lists, tables) is rejected as a whole and the paste falls back to plain text, instead of losing or
 * mangling parts of it.
 * Elsewhere, tables can only be inserted in a paragraph or a list item, where they end the list
 * (`breakOutOfListBeforeTable`); anything else goes wherever text can.
 */
function fitToFocus(
  draft: Draft<LetterEditorState>,
  focus: Focus,
  insertion: ClipboardInsertion,
): ClipboardInsertion | undefined {
  if (insertion.kind === "text") return insertion.text.length > 0 ? insertion : undefined;
  const { elements } = insertion;
  if (elements.length === 0) return undefined;
  if (isTableCellIndex(focus)) {
    const inlineText = inlineTextOf(elements);
    return inlineText ? { kind: "elements", elements: inlineText } : undefined;
  }
  if (
    elements.every((element) => element.type === "TABLE") &&
    !canInsertTableAt(draft, focus) &&
    !isItemContentIndex(focus)
  ) {
    return undefined;
  }
  return insertion;
}

/**
 * The text runs of text and paragraphs, with paragraphs joined by a space since cells have no line breaks.
 * Undefined for anything else, or if there is no text.
 */
function inlineTextOf(elements: TraversedElement[]): Text[] | undefined {
  const runs: Text[] = [];
  let previous: TraversedElement | undefined;
  for (const element of elements) {
    if (element.type !== "TEXT" && element.type !== "P") return undefined;
    const content = element.type === "P" ? element.content : [element];
    const last = runs.at(-1);
    const startsParagraph = element.type === "P" || previous?.type === "P";
    if (startsParagraph && last && content.some((run) => run.text.trim().length > 0)) {
      // On the previous run, so it keeps that font and same-font paragraphs merge into one literal.
      runs[runs.length - 1] = { ...last, text: `${last.text} ` };
    }
    runs.push(...content);
    previous = element;
  }
  const merged = mergeNeighbouringText(runs);
  return merged.some((run) => run.text.trim().length > 0) ? merged : undefined;
}

function canInsertTableAt(draft: Draft<LetterEditorState>, focus: Focus): boolean {
  return (
    isBlockContentIndex(focus) && !isTableCellIndex(focus) && isParagraph(draft.redigertBrev.blocks[focus.blockIndex])
  );
}

function insertInLetter(draft: Draft<LetterEditorState>, insertion: ClipboardInsertion) {
  if (insertion.kind === "text") {
    insertTextInLetter(draft, insertion.text, FontType.PLAIN, false);
  } else {
    insertParsedElements(draft, insertion.elements);
  }
}

/** Logs the pasted formats, their lengths and metadata, never the content: letters may contain personopplysninger. */
export function logPastedClipboard(clipboardData: DataTransfer) {
  const lengths = Object.fromEntries(
    Array.from(clipboardData.types, (type) => [type, clipboardData.getData(type).length]),
  );
  log("pasted clipboard - ", { lengths, ...getPasteMetadata(clipboardData) });
}

/**
 * Insert text in letter at specified index and offset.
 *
 * If multipartPaste is true then the function will try to do what is best in regard to preserving ids:
 * - will add a new literal instead of modifying current if offset is 0
 *
 * @param draft state to update
 * @param insertString text to insert
 * @param fontType fontType of text to insert
 * @param multipartPaste str is part of larger paste
 */
function insertTextInLetter(
  draft: Draft<LetterEditorState>,
  insertString: string,
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

      const newText = existingText.slice(0, safeOffset) + insertString + existingText.slice(safeOffset);
      updateLiteralText(content, newText);
      draft.focus.cursorPosition = safeOffset + insertString.length;
    } else if (offset > 0 && offset < text(content).length) {
      // We can't modify existing literal due to other fontType, so we need to split it and insert pasted text in between.
      const literalPart2 = splitLiteralAtOffset(content, offset);
      const contentIndex = getContentIndex(draft.focus) + 1;

      addElements(
        [newLiteral({ editedText: insertString, fontType }), literalPart2],
        contentIndex,
        parent.content,
        parent.deletedContent,
      );
      setContentIndex(draft.focus, contentIndex);
      draft.focus.cursorPosition = insertString.length;
    } else {
      const insertAtIndex = offset <= 0 ? getContentIndex(draft.focus) : getContentIndex(draft.focus) + 1;
      addElements(
        [newLiteral({ editedText: insertString, fontType })],
        insertAtIndex,
        parent.content,
        parent.deletedContent,
      );
      setContentIndex(draft.focus, insertAtIndex);
      draft.focus.cursorPosition = insertString.length;
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
function ensureLiteralAtIndex(textContents: Draft<TextContent[]>, index: number): Draft<LiteralValue> {
  while (textContents.length <= index) {
    textContents.push(newLiteral({ editedText: "" }));
  }
  const elementAtIndex = textContents.at(index);
  if (isLiteral(elementAtIndex)) return elementAtIndex;
  const literal = newLiteral({ editedText: "" });
  textContents.splice(index, 0, literal);
  return literal;
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
      setContentIndex: (focus, value) => {
        (focus as Draft<TableCellIndex>).cellContentIndex = value;
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

/** What the RTF has to insert; undefined if nothing, so the caller can fall back to plain text. */
function rtfInsertion(rtf: string): ClipboardInsertion | undefined {
  const parsed = parseRtfClipboard(rtf);
  switch (parsed.mode) {
    case "html": {
      return { kind: "elements", elements: parseHtmlToTraversedElements(parsed.html) };
    }
    case "elements": {
      return { kind: "elements", elements: parsed.elements };
    }
    case "text": {
      return { kind: "text", text: parsed.text };
    }
    case "empty": {
      return undefined;
    }
    case "unsupported": {
      log("unable to interpret rtf clipboard content", parsed.error);
      return undefined;
    }
  }
}

/**
 * Pasting should behave as it does in Microsoft Word.
 * The rules differ depending on whether the paste is at the start, middle, or end of a literal or list,
 * and whether the copied content contains only a literal, only a list, or both.
 *
 * Returns false if nothing was inserted.
 */
function insertParsedElements(draft: Draft<LetterEditorState>, elements: TraversedElement[]): boolean {
  let inserted: boolean;
  if (elements.length === 0) {
    inserted = false;
  } else if (elements.every((element) => element.type === "TEXT")) {
    for (const element of elements) {
      insertTextInLetter(draft, element.text, element.font, false);
    }
    inserted = true;
  } else {
    inserted = insertTraversedElements(draft, elements);
  }
  if (inserted) draft.saveStatus = "DIRTY";
  return inserted;
}

// Ensure that every table row has exactly the same number of cells (colCount)
function normalizeCells(cells: ReadonlyArray<TableCell>, colCount: number): TableCell[] {
  return Array.from({ length: Math.max(0, colCount) }, (_, index) => ({
    content: cells[index]?.content ?? [],
  }));
}

function createCell(cell: TableCell): Cell {
  return {
    id: null,
    parentId: null,
    deletedContent: [],
    text:
      cell.content.length > 0
        ? cell.content.map((content) => newLiteral({ editedText: content.text, fontType: content.font }))
        : [newLiteral({ editedText: "" })],
  };
}

function createRow(source: TableRow, colCount: number): Row {
  return {
    id: null,
    parentId: null,
    deletedCells: [],
    cells: normalizeCells(source.cells, colCount).map(createCell),
  };
}

function createTable(colSpec: ColumnSpec[], rows: Row[]): BrevbakerTable {
  return {
    type: "TABLE",
    id: null,
    parentId: null,
    header: { id: null, parentId: null, deletedColSpecs: [], colSpec },
    deletedRows: [],
    rows,
  };
}

function hasCells(tableElement: Table): boolean {
  return (tableElement.headerCells?.length ?? 0) > 0 || tableElement.rows.length > 0;
}

// Insert a parsed table element at current focus. Returns false if nothing was inserted.
function insertTable(draft: Draft<LetterEditorState>, tableElement: Table): boolean {
  if (!canInsertTableAt(draft, draft.focus) || !hasCells(tableElement)) {
    return false;
  }

  const headerCells = tableElement.headerCells ?? [];
  // The widest row decides the column count; shorter header and body rows are padded with empty cells.
  const colCount = Math.max(1, headerCells.length, ...tableElement.rows.map((row) => row.cells.length));
  // If there are no header cells, we promote the first row to header.
  // This is to ensure that the table has a header row if it was pasted without one (pasted from Word for example)
  const hasHeader = headerCells.length > 0;
  const shouldPromoteFirstRowToHeader = !hasHeader && tableElement.rows.length > 0;

  const getHeaderSpec = (cells: TableCell[]) =>
    cells.map((cell) => {
      const mergedTexts = mergeNeighbouringText(cell.content);
      return {
        text: cleansePastedText(mergedTexts.map((txt) => txt.text).join(" ")),
        font: mergedTexts[0]?.font ?? FontType.PLAIN,
      };
    });

  const headerSpecSource = getHeaderSpec(
    normalizeCells(hasHeader ? headerCells : (tableElement.rows[0]?.cells ?? []), colCount),
  );

  const colSpec = newColSpec(colCount, headerSpecSource);
  const bodyRows = shouldPromoteFirstRowToHeader ? tableElement.rows.slice(1) : tableElement.rows;

  const rows: Row[] = bodyRows.map((row) => createRow(row, colCount));

  const table = createTable(colSpec, rows);

  // Split current literal at cursor so trailing text stays after table.
  splitRecipe(draft, draft.focus, draft.focus.cursorPosition ?? 0);
  const currentBlock = draft.redigertBrev.blocks[draft.focus.blockIndex];

  const tableIndex = draft.focus.contentIndex;
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
  return true;
}

/** Returns false if none of the elements could be inserted. */
function insertTraversedElements(draft: Draft<LetterEditorState>, elements: TraversedElement[]): boolean {
  let inserted = false;
  let trailingBlock: Draft<AnyBlock> | undefined;
  for (const [index, element] of elements.entries()) {
    const previous = index > 0 ? elements[index - 1] : undefined;
    if (previous?.type === "ITEM" && element.type !== "TEXT" && !continuesPastedList(draft, element)) {
      trailingBlock = breakOutOfPastedList(draft) ?? trailingBlock;
    } else if (previous?.type === "TABLE") {
      trailingBlock = moveFocusAfterPastedTable(draft) ?? trailingBlock;
    }

    switch (element.type) {
      case "TEXT": {
        insertTextInLetter(draft, element.text, element.font);
        break;
      }
      case "H1": {
        insertBlock(draft, "TITLE1", element.content);
        break;
      }
      case "H2": {
        insertBlock(draft, "TITLE2", element.content);
        break;
      }
      case "H3": {
        insertBlock(draft, "TITLE3", element.content);
        break;
      }
      case "P": {
        insertBlock(draft, "PARAGRAPH", element.content);
        break;
      }
      case "ITEM": {
        insertItem(draft, element);
        break;
      }
      case "TABLE": {
        if (isItemContentIndex(draft.focus) && hasCells(element)) {
          trailingBlock = breakOutOfListBeforeTable(draft) ?? trailingBlock;
        }
        if (insertTable(draft, element)) inserted = true;
        continue;
      }
    }
    inserted = true;
  }

  if (trailingBlock) removeCursorParagraphBefore(draft, trailingBlock);
  return inserted;
}

/** A pasted item continues the current list unless it belongs to a different list type. */
function continuesPastedList(draft: Draft<LetterEditorState>, element: TraversedElement): boolean {
  if (element.type !== "ITEM") return false;
  if (element.nested || element.listType === undefined || !isItemContentIndex(draft.focus)) return true;

  const itemList = draft.redigertBrev.blocks[draft.focus.blockIndex]?.content[draft.focus.contentIndex];
  return !isItemList(itemList) || (itemList.editedListType ?? itemList.listType) === element.listType;
}

/**
 * After a pasted item, focus is in a new, empty item. If the next element is a block, a
 * table, or an item in a new list, remove the empty item and continue in a separate block after the list.
 * Returns the block containing the content that followed the list, if it was moved.
 */
function breakOutOfPastedList(draft: Draft<LetterEditorState>): Draft<AnyBlock> | undefined {
  const focus = draft.focus;
  if (!isItemContentIndex(focus)) return undefined;

  const itemList = draft.redigertBrev.blocks[focus.blockIndex]?.content[focus.contentIndex];
  if (!isItemList(itemList) || focus.itemIndex !== itemList.items.length - 1 || focus.itemIndex === 0) return undefined;
  if (!isEmptyItem(itemList.items[focus.itemIndex])) return undefined;

  removeElements(focus.itemIndex, 1, {
    content: itemList.items,
    deletedContent: itemList.deletedItems,
    id: itemList.id,
  });
  return focusNewBlockAfter(draft, focus.blockIndex, focus.contentIndex);
}

/**
 * After a pasted table, focus is inside the table; move it to a separate block after the table.
 * Returns the block containing the content that followed the table, if it was moved.
 */
function moveFocusAfterPastedTable(draft: Draft<LetterEditorState>): Draft<AnyBlock> | undefined {
  const focus = draft.focus;
  if (!isTableCellIndex(focus) || !isTable(draft.redigertBrev.blocks[focus.blockIndex]?.content[focus.contentIndex]))
    return undefined;

  return focusNewBlockAfter(draft, focus.blockIndex, focus.contentIndex);
}

/**
 * A table can't go in a list, so a table pasted in an item ends the list at the cursor: the items from the cursor on
 * move to a list of their own after the table, and focus moves to a new paragraph in between. Returns the block
 * containing the content that followed, if it was moved.
 */
function breakOutOfListBeforeTable(draft: Draft<LetterEditorState>): Draft<AnyBlock> | undefined {
  const focus = draft.focus;
  if (!isItemContentIndex(focus)) return undefined;
  const { blockIndex, contentIndex } = focus;
  const blocks = draft.redigertBrev.blocks;
  const block = blocks[blockIndex];
  const itemList = block?.content[contentIndex];
  if (!isItemList(itemList)) return undefined;

  const splitIndex = splitItemAtCursor(draft, itemList);
  const items = { content: itemList.items, deletedContent: itemList.deletedItems, id: itemList.id };

  if (splitIndex === 0) {
    // The whole list follows the table.
    if (contentIndex > 0) return focusNewBlockAfter(draft, blockIndex, contentIndex - 1);
    addElements(
      [newParagraph({ content: [newLiteral({ editedText: "" })] })],
      blockIndex,
      blocks,
      draft.redigertBrev.deletedBlocks,
    );
    draft.focus = { blockIndex, contentIndex: 0, cursorPosition: 0 };
    return blocks[blockIndex + 1];
  }

  // An empty item at the cursor, e.g. one left by a pasted paragraph or an emptied selection, would otherwise end up
  // as an empty first item after the table.
  const itemAtCursor = itemList.items[splitIndex];
  if (itemAtCursor && isEmptyItem(itemAtCursor)) removeElements(splitIndex, 1, items);

  const movedItems = removeElements(splitIndex, itemList.items.length - splitIndex, items);
  if (movedItems.length > 0) {
    addElements(
      [newItemList({ items: movedItems, listType: effectiveListType(itemList) })],
      contentIndex + 1,
      block.content,
      block.deletedContent,
    );
  }
  const trailingBlock = focusNewBlockAfter(draft, blockIndex, contentIndex);
  // The moved list is new, so it must be alone in its block.
  if (trailingBlock) splitMixedListBlock(draft, blockIndex + 2);
  return trailingBlock;
}

/** Splits the focused item at the cursor and returns the index of the first item after it. */
function splitItemAtCursor(draft: Draft<LetterEditorState>, itemList: Draft<ItemList>): number {
  const focus = draft.focus as Draft<ItemContentIndex & Focus>;
  const { itemIndex, itemContentIndex } = focus;
  const cursorPosition = focus.cursorPosition ?? 0;
  if (isAtStartOfItem(focus)) return itemIndex;

  const content = itemList.items[itemIndex]?.content ?? [];
  const atEndOfItem =
    itemContentIndex >= content.length - 1 && cursorPosition >= (text(content[itemContentIndex]) ?? "").length;
  if (atEndOfItem) return itemIndex + 1;

  splitRecipe(draft, focus, cursorPosition);
  // splitRecipe refuses some splits (e.g. next to an empty item); the table then goes after the whole item.
  return isItemContentIndex(draft.focus) && draft.focus.itemIndex > itemIndex ? draft.focus.itemIndex : itemIndex + 1;
}

/**
 * Sets focus at the start of a new block immediately after `contentIndex`. If there is a literal there, split
 * the block at that point. If there is other content (a list, table, or variable), move it to a separate block
 * and place focus in an empty paragraph before it, so the rest of the paste goes in between. Returns the moved
 * block so the cursor paragraph can be removed afterward.
 */
function focusNewBlockAfter(
  draft: Draft<LetterEditorState>,
  blockIndex: number,
  contentIndex: number,
): Draft<AnyBlock> | undefined {
  const blocks = draft.redigertBrev.blocks;
  const block = blocks[blockIndex];
  const afterIndex = { blockIndex, contentIndex: contentIndex + 1 };

  if (isLiteral(block.content[afterIndex.contentIndex])) {
    draft.focus = { ...afterIndex, cursorPosition: 0 };
    splitRecipe(draft, afterIndex, 0);
    return undefined;
  }

  // removeElements records moved template ids in the source block, so the split persists (see letter-editor-actions SKILL.md).
  const trailingContent = removeElements(afterIndex.contentIndex, block.content.length, block);
  const trailingBlock = trailingContent.length > 0 ? newParagraph({ content: trailingContent }) : undefined;
  addElements(
    [newParagraph({ content: [newLiteral({ editedText: "" })] }), ...(trailingBlock ? [trailingBlock] : [])],
    blockIndex + 1,
    blocks,
    draft.redigertBrev.deletedBlocks,
  );
  draft.focus = { blockIndex: blockIndex + 1, contentIndex: 0, cursorPosition: 0 };
  return trailingBlock && blocks[blockIndex + 2];
}

/**
 * Once the paste is complete, removes the empty cursor paragraph before content moved by `focusNewBlockAfter`
 * and places the cursor at the end of the last pasted paragraph.
 */
function removeCursorParagraphBefore(draft: Draft<LetterEditorState>, trailingBlock: Draft<AnyBlock>) {
  const focus = draft.focus;
  const blocks = draft.redigertBrev.blocks;
  const cursorBlock = blocks[focus.blockIndex];
  if (!isBlockContentIndex(focus) || focus.blockIndex === 0 || blocks[focus.blockIndex + 1] !== trailingBlock) return;
  if (!isParagraph(cursorBlock) || cursorBlock.id !== null || !isEmptyBlock(cursorBlock)) return;

  const previousBlock = blocks[focus.blockIndex - 1];
  const lastContent = previousBlock.content.at(-1);
  if (!isLiteral(lastContent)) return;

  removeElements(focus.blockIndex, 1, { content: blocks, deletedContent: draft.redigertBrev.deletedBlocks });
  draft.focus = {
    blockIndex: focus.blockIndex - 1,
    contentIndex: previousBlock.content.length - 1,
    cursorPosition: text(lastContent).length,
  };
}

function insertBlock(
  draft: Draft<LetterEditorState>,
  type: "TITLE1" | "TITLE2" | "TITLE3" | "PARAGRAPH",
  content: Text[],
) {
  const focusedBlock = draft.redigertBrev.blocks[draft.focus.blockIndex];
  const blockContent = focusedBlock?.content[draft.focus.contentIndex];

  if (isBlockContentIndex(draft.focus) && isAtStartOfBlock(draft.focus)) {
    if (isEmptyBlock(focusedBlock)) {
      // If we're at the start of an empty block, we want to change it to whatever type we're inserting
      focusedBlock.type = type;
      insertTextElement(draft, content);
    } else {
      // If we're at the start of a non-empty block then we want to insert a new block to preserve IDs of existing block and it's content.
      const literals = content.map((textContent) =>
        newLiteral({ editedText: textContent.text, fontType: textContent.font }),
      );
      const block = type === "PARAGRAPH" ? newParagraph({ content: literals }) : newTitle({ type, content: literals });
      addElements([block], draft.focus.blockIndex, draft.redigertBrev.blocks, draft.redigertBrev.deletedBlocks);
      draft.focus.blockIndex += 1;
    }
  } else if (isItemContentIndex(draft.focus) && isAtStartOfItem(draft.focus) && isItemList(blockContent)) {
    // If we're at the start of an Item then we want to preserve any potential existing IDs, so we instead insert a new item.
    const literals = content.map((textContent) =>
      newLiteral({ editedText: textContent.text, fontType: textContent.font }),
    );
    addElements([newItem({ content: literals })], draft.focus.itemIndex, blockContent.items, blockContent.deletedItems);
    draft.focus.itemIndex++;
  } else {
    insertTextElement(draft, content);
  }
}

function insertTextElement(draft: Draft<LetterEditorState>, content: Text[]) {
  for (const textContent of content) {
    insertTextInLetter(draft, textContent.text, textContent.font);
  }
  splitRecipe(draft, draft.focus, draft.focus.cursorPosition ?? 0);
}

function insertItem(draft: Draft<LetterEditorState>, element: ItemElement) {
  const currentBlock = draft.redigertBrev.blocks[draft.focus.blockIndex];
  const blockContent = currentBlock.content[draft.focus.contentIndex];

  if (isItemContentIndex(draft.focus) && isItemList(blockContent)) {
    if (isAtStartOfItem(draft.focus)) {
      // If we're at the start of an Item then we want to preserve any potential existing IDs, so we instead insert a new item.
      const literals = element.content.map((t) => newLiteral({ editedText: t.text, fontType: t.font }));
      addElements(
        [newItem({ content: literals })],
        draft.focus.itemIndex,
        blockContent.items,
        blockContent.deletedItems,
      );
      draft.focus.itemIndex++;
    } else {
      insertTextElement(draft, element.content);
    }
  } else if (!isItemContentIndex(draft.focus) && !isItemList(blockContent)) {
    // We're also validating that current blockContent isn't an ItemList, because then the focus should also have been an ItemContentIndex.
    toggleItemListAndSplitAtCursor(draft, currentBlock, element.listType);
    insertTextElement(draft, element.content);
  }
}

function toggleItemListAndSplitAtCursor(
  draft: Draft<LetterEditorState>,
  currentBlock: Draft<AnyBlock>,
  listType?: ListType,
) {
  let itemContent: TextContent[] = [];

  // Find the first index of the contiguous TextContent before the paste location.
  const sentenceIndex = findAdjoiningContent(
    draft.focus.contentIndex,
    currentBlock.content.slice(0, draft.focus.contentIndex + 1),
    isTextContent,
  );

  // If count is 1, only the current content element is included, so there is nothing else to include.
  if (sentenceIndex.count > 1) {
    itemContent = removeElements(sentenceIndex.startIndex, sentenceIndex.count - 1, currentBlock).filter(isTextContent);
    // We removed content from startIndex up to draft.focus.contentIndex, so the current element is now at startIndex.
    draft.focus.contentIndex = sentenceIndex.startIndex;
  }

  // If the paste is not at the start of currentContent, split it and add the first part to itemContent, leaving the rest in the block.
  const currentContent = currentBlock.content[draft.focus.contentIndex];
  if (isLiteral(currentContent) && (draft.focus.cursorPosition! ?? 0) > 0) {
    removeElements(draft.focus.contentIndex, 1, currentBlock);
    const afterSplit = splitLiteralAtOffset(currentContent, draft.focus.cursorPosition! ?? 0);

    if (text(afterSplit).length > 0) {
      addElements([afterSplit], draft.focus.contentIndex, currentBlock.content, []);
    }

    itemContent.push(currentContent);
  }

  // Ensure the new item has content.
  if (itemContent.length === 0) {
    itemContent.push(newLiteral({ editedText: "" }));
  }
  const theItem = newItem({ content: itemContent });

  // Insert the item into the existing list before the current content, or create a new list containing it.
  const prevNonTextContent = sentenceIndex.startIndex > 0 ? currentBlock.content[sentenceIndex.startIndex - 1] : null;
  if (isItemList(prevNonTextContent)) {
    addElements([theItem], prevNonTextContent.items.length, prevNonTextContent.items, prevNonTextContent.deletedItems);
    // Set focus to the item inserted into the existing ItemList.
    draft.focus = {
      ...draft.focus,
      contentIndex: sentenceIndex.startIndex - 1,
      itemIndex: prevNonTextContent.items.length - 1,
      itemContentIndex: theItem.content.length - 1,
    };
  } else {
    addElements(
      [newItemList({ items: [theItem], listType })],
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

/** Sanitises pasted HTML through DOMPurify. */
function sanitizePastedHtml(html: string): string {
  return DOMPurify.sanitize(html, {
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
      "h3",
    ],
    ALLOWED_ATTR: ["rowspan", "colspan"],
  });
}

function parseHtmlToTraversedElements(html: string): TraversedElement[] {
  const document = new DOMParser().parseFromString(sanitizePastedHtml(html), "text/html");

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
        case "H3":
        case "TABLE":
          return [];
        default:
          return [];
      }
    });
    // Drop whitespace-only TEXT nodes so the cell content contains only meaningful text.
    return cellContentElements.filter(
      (textContent) => textContent.type !== "TEXT" || textContent.text.trim().length > 0,
    );
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

function traverseTextContainer(
  element: Element,
  type: "ITEM" | "H1" | "H2" | "H3",
  font: FontType,
): TraversedElement[] {
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
        case "H1":
        case "H2":
        case "H3":
        case "ITEM": {
          return child.content;
        }
        case "TABLE": {
          return [];
        }
        default: {
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

    case "UL":
    case "OL": {
      const listType = element.tagName === "OL" ? ListType.NUMMERERT_LISTE : ListType.PUNKTLISTE;
      // Preserve listType already set on nested items (e.g. an <ol> inside a <ul> keeps NUMMERERT_LISTE).
      return traverseChildren(element, font).map((child) =>
        child.type === "ITEM" && child.listType == null ? { ...child, listType } : child,
      );
    }

    case "LI": {
      if (element.children.length === 0) {
        const text = cleansePastedText(element.textContent ?? "");

        return text.length >= 0 ? [{ type: "ITEM", content: [{ type: "TEXT", font: font, text }] }] : [];
      } else {
        return traverseLiChildren(element, font);
      }
    }

    case "H1": {
      return traverseTextContainer(element, "H1", font);
    }

    case "H2": {
      return traverseTextContainer(element, "H2", font);
    }

    case "H3": {
      return traverseTextContainer(element, "H3", font);
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
      return [current].concat(acc);
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

/**
 * Traverses the children of an `<li>` element and returns one or more `ItemElement`s.
 * When nested `<ul>`/`<ol>` elements are encountered, their items are emitted as siblings
 * rather than being concatenated into the parent item's text. This flattens nested lists
 * into a single level, which is the closest representation the edit model supports.
 *
 * Example: `<li>A <ol><li>B</li></ol></li>` → [ITEM("A"), ITEM("B", NUMMERERT_LISTE)]
 */
function traverseLiChildren(li: Element, font: FontType): ItemElement[] {
  const result: ItemElement[] = [];
  let directText: Text[] = [];

  const flushDirectText = () => {
    if (directText.length > 0) {
      result.push({ type: "ITEM", content: directText });
      directText = [];
    }
  };

  for (const child of traverseChildren(li, font)) {
    switch (child.type) {
      case "TEXT": {
        directText.push(child);
        break;
      }
      case "ITEM": {
        // Nested list item — flush any accumulated direct text as the parent item first.
        flushDirectText();
        result.push({ ...child, nested: true });
        break;
      }
      case "P":
      case "H1":
      case "H2":
      case "H3": {
        directText.push(...child.content);
        break;
      }
      case "TABLE": {
        // Tables nested inside list items are not supported; ignore.
        break;
      }
    }
  }

  flushDirectText();

  // Ensure at least one item is produced (e.g. a purely nested-list LI with no direct text).
  if (result.length === 0) {
    result.push({ type: "ITEM", content: [{ type: "TEXT", font, text: "" }] });
  }

  return result;
}

type ParagraphChild = ParagraphElement | ItemElement | Title1Element | Title2Element | Title3Element;

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
      case "H3":
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

function log(message: string, ...obj: unknown[]) {
  console.info(`Skribenten:pasteHandler: ${message}`, ...obj);
}
