import type { Draft } from "immer";
import _ from "lodash";

import { MergeTarget } from "~/Brevredigering/LetterEditor/actions/merge";
import { updateLiteralText } from "~/Brevredigering/LetterEditor/actions/updateContentText";
import {
  isFritekst,
  isItemList,
  isLiteral,
  isTableCellIndex,
  isTextContent,
} from "~/Brevredigering/LetterEditor/model/utils";
import type { BrevResponse } from "~/types/brev";
import type {
  Cell,
  ColumnSpec,
  Content,
  EditedLetter,
  ElementTags,
  Identifiable,
  Item,
  ItemList,
  LiteralValue,
  NewLine,
  ParagraphBlock,
  Row,
  Table,
  TextContent,
  TITLE1,
  Title1Block,
  TITLE2,
  Title2Block,
  VariableValue,
} from "~/types/brevbakerTypes";
import { FontType, ITEM_LIST, LITERAL, NEW_LINE, PARAGRAPH, TABLE, VARIABLE } from "~/types/brevbakerTypes";
import type { Nullable } from "~/types/Nullable";

import type {
  BlockContentIndex,
  Focus,
  ItemContentIndex,
  LetterEditorState,
  LiteralIndex,
  TableCellIndex,
} from "../model/state";

export function cleanseText(text: string): string {
  return text.replaceAll("<br>", "").replaceAll("&nbsp;", " ").replaceAll("\n", " ").replaceAll("\r", "");
}

export function isEditableContent(content: Content | undefined | null): boolean {
  return content != null && (content.type === VARIABLE || content.type === ITEM_LIST);
}

export function isBlockContentIndex(f: Focus | LiteralIndex | undefined): f is BlockContentIndex {
  return (
    f !== undefined &&
    "blockIndex" in f &&
    f.blockIndex !== undefined &&
    "contentIndex" in f &&
    f.contentIndex !== undefined &&
    !isItemContentIndex(f) &&
    !isTableCellIndex(f)
  );
}

export function isTable(content: Content | undefined | null): content is Table {
  return content?.type === TABLE;
}

export function isItemContentIndex(f: Focus | LiteralIndex | undefined): f is ItemContentIndex {
  return (
    f !== undefined &&
    "itemIndex" in f &&
    f.itemIndex !== undefined &&
    "itemContentIndex" in f &&
    f.itemContentIndex !== undefined
  );
}

export function isIndicesOfSameType<T extends LiteralIndex>(first: T, second: LiteralIndex): second is T {
  return (
    (isItemContentIndex(first) && isItemContentIndex(second)) ||
    (isTableCellIndex(first) && isTableCellIndex(second)) ||
    (isBlockContentIndex(first) && isBlockContentIndex(second))
  );
}

export function isAtStartOfBlock(f: Focus, offset?: number): boolean {
  if (isItemContentIndex(f)) {
    return f.contentIndex === 0 && f.itemIndex === 0 && f.itemContentIndex === 0 && (offset ?? f.cursorPosition) === 0;
  } else if (isTableCellIndex(f)) {
    return (
      f.contentIndex === 0 &&
      f.rowIndex === 0 &&
      f.cellIndex === 0 &&
      f.cellContentIndex === 0 &&
      (offset ?? f.cursorPosition) === 0
    );
  } else if (isBlockContentIndex(f)) {
    return f.contentIndex === 0 && (offset ?? f.cursorPosition) === 0;
  } else {
    return false;
  }
}

export function isAtStartOfItemList(f: Focus): boolean {
  return isItemContentIndex(f) && f.itemIndex === 0 && f.itemContentIndex === 0 && f.cursorPosition === 0;
}

export function isAtEndOfItemList(f: Focus, itemList: ItemList): boolean {
  return (
    isItemContentIndex(f) &&
    f.itemIndex === itemList.items.length - 1 &&
    f.itemContentIndex === itemList.items[f.itemIndex].content.length - 1 &&
    (f.cursorPosition ?? 0) >= text(itemList.items[f.itemIndex].content[f.itemContentIndex]).length
  );
}

export function isAtStartOfTable(f: Focus): boolean {
  return (
    isTableCellIndex(f) && f.rowIndex === -1 && f.cellIndex === 0 && f.cellContentIndex === 0 && f.cursorPosition === 0
  );
}

export function isAtEndOfTable(f: Focus, table: Table): boolean {
  if (isTableCellIndex(f)) {
    if (isTable(table)) {
      const lastRowIndex = table.rows.length - 1;
      const lastRowCells =
        lastRowIndex >= 0
          ? (table.rows[lastRowIndex]?.cells ?? [])
          : table.header.colSpec.map((cs) => cs.headerContent);

      const lastCellIndex = Math.max(0, lastRowCells?.length - 1);
      const lastCellContentIndex = Math.max(0, (lastRowCells[lastCellIndex]?.text.length ?? 0) - 1);
      const lastContent = lastRowCells[lastCellIndex]?.text[lastCellContentIndex];
      return (
        f.rowIndex === lastRowIndex &&
        f.cellIndex === lastCellIndex &&
        f.cellContentIndex === lastCellContentIndex &&
        f.cursorPosition != null &&
        f.cursorPosition >= (lastContent ? text(lastContent).length : 0)
      );
    }
  }
  return false;
}

export function isAtSameBlockContent(first: LiteralIndex, second: LiteralIndex): boolean {
  return first.blockIndex === second.blockIndex && first.contentIndex === second.contentIndex;
}

export function isAtSameItem(first: ItemContentIndex, second: ItemContentIndex): boolean {
  return (
    first.blockIndex === first.blockIndex &&
    second.contentIndex === second.contentIndex &&
    first.itemIndex === second.itemIndex
  );
}

export function isAtSameTableCell(first: TableCellIndex, second: TableCellIndex): boolean {
  return (
    first.blockIndex === second.blockIndex &&
    first.contentIndex === second.contentIndex &&
    first.rowIndex === second.rowIndex &&
    first.cellIndex === second.cellIndex
  );
}

export function isIndexAfter(
  first: LiteralIndex & { cursorPosition: number },
  after: LiteralIndex & { cursorPosition: number },
): boolean {
  if (first.blockIndex === after.blockIndex) {
    if (first.contentIndex === after.contentIndex) {
      if (isItemContentIndex(first) && isItemContentIndex(after)) {
        if (first.itemIndex === after.itemIndex) {
          if (first.itemContentIndex === after.itemContentIndex) {
            return first.cursorPosition < after.cursorPosition;
          } else {
            return first.itemContentIndex < after.itemContentIndex;
          }
        } else {
          return first.itemIndex < after.itemIndex;
        }
      } else if (isTableCellIndex(first) && isTableCellIndex(after)) {
        if (first.rowIndex === after.rowIndex) {
          if (first.cellIndex === after.cellIndex) {
            if (first.cellContentIndex === after.cellContentIndex) {
              return first.cursorPosition < after.cursorPosition;
            } else {
              return first.cellContentIndex < after.cellContentIndex;
            }
          } else {
            return first.cellIndex < after.cellIndex;
          }
        } else {
          return first.rowIndex < after.rowIndex;
        }
      } else {
        return first.cursorPosition < after.cursorPosition;
      }
    } else {
      return first.contentIndex < after.contentIndex;
    }
  } else {
    return first.blockIndex < after.blockIndex;
  }
}

export function isAtStartOfItem(f: Focus, offset?: number): boolean {
  return isItemContentIndex(f) && f.itemContentIndex === 0 && (offset ?? f.cursorPosition) === 0;
}

export function text<T extends TextContent | undefined>(
  content: T,
): string | (undefined extends T ? undefined : never) {
  if (content?.type === LITERAL) {
    return content.editedText ?? content.text;
  } else if (content?.type === VARIABLE || content?.type === NEW_LINE) {
    return content.text;
  } else {
    return undefined as undefined extends T ? undefined : never;
  }
}

export function fontTypeOf(content: TextContent): FontType {
  switch (content.type) {
    case "NEW_LINE":
      return FontType.PLAIN;
    case "LITERAL":
      return content.editedFontType ?? content.fontType;
    case "VARIABLE":
      return content.fontType;
  }
}

export function isNew(obj: Identifiable): boolean {
  return obj.id === null;
}

export function create(brev: BrevResponse): LetterEditorState {
  return {
    info: brev.info,
    redigertBrev: brev.redigertBrev,
    redigertBrevHash: brev.redigertBrevHash,
    saveStatus: "SAVED",
    focus: { blockIndex: 0, contentIndex: 0 },
    history: { entries: [], entryPointer: -1 },
  };
}

export function removeElements<T extends Identifiable>(
  startIndex: number,
  count: number,
  from: { content: Draft<T[]>; deletedContent: Draft<number[]>; id: number | null },
): Draft<T[]> {
  const removedElements = from.content.splice(startIndex, count);
  for (const e of removedElements) deleteElement(e, from);
  return removedElements;
}

function deleteElement(
  toDelete: Identifiable,
  from: { content: Identifiable[]; deletedContent: Draft<number[]>; id: number | null },
) {
  if (
    toDelete.id !== null &&
    toDelete.parentId === from.id &&
    !from.deletedContent.includes(toDelete.id) &&
    !from.content.map((c) => c.id).includes(toDelete.id)
  ) {
    from.deletedContent.push(toDelete.id);
  }
}

export function addElements<T extends Identifiable, E extends T>(
  elements: E[],
  toIndex: number,
  to: Draft<T[]>,
  deleted: Draft<number[]>,
): void;
export function addElements<T extends Identifiable, E extends T>(
  elements: Draft<E[]>,
  toIndex: number,
  to: Draft<T[]>,
  deleted: Draft<number[]>,
): void {
  if (toIndex > 0) {
    const elementBeforeStartIndex = to[toIndex - 1];
    const firstElementToAdd = elements[0];

    to.splice(
      toIndex - 1,
      1,
      ...mergeLiteralsIfPossible(elementBeforeStartIndex, firstElementToAdd),
      ...elements.slice(1),
    );
  } else {
    to.splice(toIndex, 0, ...elements);
  }

  const presentIds = to.map((e) => e.id).filter((id) => id !== null) as number[];
  for (const id of presentIds) {
    const index = deleted.indexOf(id);
    if (index !== -1) {
      deleted.splice(index, 1);
    }
  }
}

export function findAdjoiningContent<T extends Content, S extends T>(
  atIndex: number,
  from: T[],
  predicate: (value: T) => value is S,
): { startIndex: number; endIndex: number; count: number } {
  if (from.length === 0) {
    return { startIndex: 0, endIndex: 0, count: 0 };
  }

  const reverseSearchNonMatching = from.slice(0, atIndex).findLastIndex((c) => !predicate(c));
  const forwardSearchNonMatching = from.slice(atIndex + 1).findIndex((c) => !predicate(c));

  const startIndex = reverseSearchNonMatching >= 0 ? reverseSearchNonMatching + 1 : 0;
  const endIndex = forwardSearchNonMatching >= 0 ? atIndex + forwardSearchNonMatching : from.length - 1;

  return {
    startIndex,
    endIndex,
    count: endIndex - startIndex + 1,
  };
}

export function mergeLiteralsIfPossible<T extends Identifiable>(first: Draft<T>, second: Draft<T>): Draft<T[]> {
  if (
    isLiteral(first) &&
    isLiteral(second) &&
    !isFritekst(first) &&
    !isFritekst(second) &&
    fontTypeOf(first) === fontTypeOf(second)
  ) {
    const mergedText = text(first) + text(second);
    if (first.id !== null && second.id !== null) {
      return [first, second];
    } else if (first.id === null) {
      updateLiteralText(second, mergedText);
      return [second];
    } else {
      updateLiteralText(first, mergedText);
      return [first];
    }
  } else {
    return [first, second];
  }
}

/**
 * Splits literal text at given offset by updating 'editedText' of the given literal and returning a new literal.
 *
 * @param literal the literal to update
 * @param offset the offset to split at
 * @returns a new literal
 */
export function splitLiteralAtOffset(literal: Draft<LiteralValue>, offset: number): LiteralValue {
  const origText = text(literal);
  const newText = cleanseText(origText.slice(0, Math.max(0, offset)));
  const nextText = cleanseText(origText.slice(Math.max(0, offset)));

  updateLiteralText(literal, newText);

  return newLiteral({ editedText: nextText, fontType: literal.editedFontType ?? literal.fontType });
}

export function newTitle(args: {
  id?: Nullable<number>;
  content: TextContent[];
  type: typeof TITLE1 | typeof TITLE2;
  deletedContent?: number[];
}): Title1Block | Title2Block {
  return {
    type: args.type,
    id: args.id ?? null,
    parentId: null,
    editable: true,
    deletedContent: args.deletedContent ?? [],
    content: args.content,
  };
}

export function newParagraph(args: {
  id?: Nullable<number>;
  parentId?: Nullable<number>;
  content: Content[];
  deletedContent?: number[];
}): ParagraphBlock {
  return {
    type: PARAGRAPH,
    id: args.id ?? null,
    parentId: args.parentId ?? null,
    editable: true,
    deletedContent: args.deletedContent ?? [],
    content: args.content,
  };
}

export function newTable(rows: Row[]): Table {
  if (rows.length === 0) {
    throw new Error("newTable: rows must contain at least one row");
  }

  const colCount = rows[0].cells.length;

  if (!rows.every((row) => row.cells.length === colCount)) {
    throw new Error("newTable: all rows must have an identical column count");
  }
  return {
    type: TABLE,
    id: null,
    parentId: null,
    header: {
      id: null,
      parentId: null,
      colSpec: newColSpec(colCount),
    },
    rows,
    deletedRows: [],
  };
}

export function newLiteral(
  args: {
    id?: Nullable<number>;
    parentId?: Nullable<number>;
    text?: string;
    editedText?: Nullable<string>;
    fontType?: Nullable<FontType>;
    // TODO: Gir ikke mening å sette editedFontType i nye literals.
    editedFontType?: Nullable<FontType>;
    tags?: ElementTags[];
  } = {},
): LiteralValue {
  return {
    type: LITERAL,
    id: args?.id ?? null,
    parentId: args?.parentId ?? null,
    text: args?.text ?? "",
    editedText: args?.editedText ?? null,
    editedFontType: args?.editedFontType ?? null,
    fontType: args?.fontType ?? FontType.PLAIN,
    tags: args?.tags ?? [],
  };
}

export const newVariable = (args: {
  id?: Nullable<number>;
  text: string;
  parentId?: Nullable<number>;
  fontType?: FontType;
}): VariableValue => {
  return {
    type: VARIABLE,
    id: args.id ?? null,
    parentId: args.parentId ?? null,
    text: args.text,
    fontType: args.fontType ?? FontType.PLAIN,
  };
};

export function newItem({
  id,
  content,
  parentId,
}: {
  id?: Nullable<number>;
  content: TextContent[];
  parentId?: Nullable<number>;
}): Item {
  return {
    id: id ?? null,
    parentId: parentId ?? null,
    content,
    deletedContent: [],
  };
}

export function newItemList(args: { id?: Nullable<number>; items: Item[]; deletedItems?: number[] }): ItemList {
  return {
    id: args.id ?? null,
    parentId: null,
    type: "ITEM_LIST",
    items: args.items,
    deletedItems: args.deletedItems ?? [],
  };
}

export function createNewLine(): NewLine {
  return {
    id: null,
    parentId: null,
    type: NEW_LINE,
    text: "",
  };
}

export function getMergeIds(sourceId: number, target: MergeTarget): [number, number] {
  switch (target) {
    case MergeTarget.PREVIOUS: {
      return [sourceId - 1, sourceId];
    }
    case MergeTarget.NEXT: {
      return [sourceId, sourceId + 1];
    }
  }
}

export function insertEmptyParagraphAfterBlock(draft: Draft<LetterEditorState>, blockIndex: number) {
  const emptyPara = newParagraph({
    content: [newLiteral()],
  });

  addElements([emptyPara], blockIndex + 1, draft.redigertBrev.blocks, draft.redigertBrev.deletedBlocks);

  draft.focus = {
    blockIndex: blockIndex + 1,
    contentIndex: 0,
    cursorPosition: 0,
  };
}

export function newCell(text?: TextContent[]): Cell {
  return {
    id: null,
    parentId: null,
    text: text ?? [newLiteral({ editedText: "" })],
  };
}

export function newRow(colCount: number): Row {
  return {
    id: null,
    parentId: null,
    cells: Array.from({ length: colCount }, () => newCell()),
  };
}

export function newColSpec(colCount: number, headers?: { text: string; font?: FontType }[]): ColumnSpec[] {
  return Array.from({ length: colCount }, (_, i) => ({
    id: null,
    parentId: null,
    alignment: "LEFT" as const,
    span: 1,
    headerContent: {
      id: null,
      parentId: null,
      text: [
        newLiteral({
          editedText: headers?.[i]?.text ?? `Kolonne ${i + 1}`,
          fontType: headers?.[i]?.font ?? FontType.PLAIN,
        }),
      ],
    },
  }));
}

export function safeIndex(index: number, array: unknown[]) {
  return Math.max(0, Math.min(index, array.length - 1));
}

export function isValidIndex(letter: EditedLetter, index: LiteralIndex) {
  const content = letter.blocks[index.blockIndex]?.content[index.contentIndex];

  if (isItemList(content) && isItemContentIndex(index)) {
    return content.items[index.itemIndex]?.content[index.itemContentIndex] !== undefined;
  } else if (isTable(content) && isTableCellIndex(index)) {
    return index.rowIndex >= 0
      ? content.rows[index.rowIndex]?.cells[index.cellIndex]?.text[index.cellContentIndex] !== undefined
      : content.header.colSpec[index.cellIndex]?.headerContent?.text[index.cellContentIndex] !== undefined;
  } else {
    return isTextContent(content) && isBlockContentIndex(index);
  }
}

/**
 * Normalizes the order of deletedContent and deletedItems arrays by sorting them.
 *
 * This is necessary because the frontend and backend store deletion IDs in different orders:
 * - Frontend tracks deletions in the order they occur
 * - Backend processes and returns them in a different order.
 *
 * Without this normalization, identical content with differently ordered deletion arrays
 * would be considered different, causing undo/redo history to be incorrectly cleared.
 * see ManagedLetterEditorContextProvider.tsx for usage.
 *
 * Example:
 * Frontend: deletedContent: [491536928, 1727594288, -830599486]
 * Backend:  deletedContent: [1727594288, 491536928, -830599486]
 * After normalization, both become: [-830599486, 491536928, 1727594288]
 */
export function normalizeDeletedArrays(obj: unknown): unknown {
  if (_.isArray(obj)) {
    return obj.map((item) => normalizeDeletedArrays(item));
  }
  if (_.isObject(obj) && obj !== null) {
    const normalized: Record<string, unknown> = {};
    for (const [key, value] of Object.entries(obj)) {
      if ((key === "deletedContent" || key === "deletedItems") && _.isArray(value)) {
        // Sort to normalize order differences between frontend and backend
        normalized[key] = [...value].sort();
      } else {
        normalized[key] = normalizeDeletedArrays(value);
      }
    }
    return normalized;
  }
  return obj;
}
