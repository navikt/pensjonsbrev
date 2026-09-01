import { type LiteralIndex } from "~/Brevredigering/LetterEditor/model/state";
import { type AnyBlock, type Cell, type Content, type Item, type Row, type TextContent } from "~/types/brevbakerTypes";

/**
 * Mirrors the backend `UnifiedDiff` (see EditLetterDiff.kt).
 *
 * All map keys are *unified* indexes, i.e. positions in the currently edited letter (`new`), not in
 * the rendered template (`old`). Entirely deleted nodes carry their full markup, and their key is the
 * unified position they should be displayed at - meaning "render this deleted node just before the
 * surviving node at that index". A key at or past the number of surviving siblings means "render last".
 *
 * Kotlin `Map<Int, T>` is serialized by Jackson with string keys, hence `Record<string, T>`.
 */

/** A word-level range within a still-existing piece of text, in current (new) text coordinates. */
export type DiffTextSegment = {
  startOffset: number;
  endOffset: number;
};

/** A word-level range removed from a still-existing piece of text, in original (old) text coordinates. */
export type DiffDeletedTextSegment = {
  startOffset: number;
  endOffset: number;
  text: string;
};

/** Word-level edits within a single, still-existing text node (LITERAL/VARIABLE/NEW_LINE). */
export type TextEdit = {
  inserts: DiffTextSegment[];
  deletes: DiffDeletedTextSegment[];
};

/** Edits within a content list that only ever holds text content (item content, table cell content). */
export type TextOnlyEdit = {
  textEdits: Record<string, TextEdit>;
  deletedContent: Record<string, TextContent[]>;
};

/** Edits within a single table row. */
export type RowEdit = {
  cellEdits: Record<string, TextOnlyEdit>;
  deletedCells: Record<string, Cell[]>;
};

export type TextContentEdit = { edit: TextEdit };

export type ItemListEdit = {
  itemEdits: Record<string, TextOnlyEdit>;
  deletedItems: Record<string, Item[]>;
};

export type TableEdit = {
  rowEdits: Record<string, RowEdit>;
  deletedRows: Record<string, Row[]>;
};

/**
 * The backend `ContentEdit` is a sealed class serialized without a type discriminator,
 * so the variants are told apart by their shape.
 */
export type ContentEdit = TextContentEdit | ItemListEdit | TableEdit;

export type BlockEdit = {
  contentEdits: Record<string, ContentEdit>;
  deletedContent: Record<string, Content[]>;
};

export type UnifiedLetterDiff = {
  editedBlocks: Record<string, BlockEdit>;
  deletedBlocks: Record<string, AnyBlock[]>;
};

const isTextContentEdit = (edit: ContentEdit): edit is TextContentEdit => "edit" in edit;
const isItemListEdit = (edit: ContentEdit): edit is ItemListEdit => "itemEdits" in edit;
const isTableEdit = (edit: ContentEdit): edit is TableEdit => "rowEdits" in edit;

export type DiffSegment =
  | { type: "unchanged"; text: string }
  | { type: "inserted"; text: string }
  | { type: "deleted"; text: string };

const EMPTY: never[] = [];

const emptyList = <T>(): T[] => EMPTY as unknown as T[];

/** Stable string identity of a literal, used to remember per-literal dismissals. */
export function diffKey(index: LiteralIndex): string {
  if ("itemIndex" in index) {
    return [index.blockIndex, index.contentIndex, "item", index.itemIndex, index.itemContentIndex].join("-");
  }
  if ("rowIndex" in index) {
    return [
      index.blockIndex,
      index.contentIndex,
      "table",
      index.rowIndex,
      index.cellIndex,
      index.cellContentIndex,
    ].join("-");
  }
  return `${index.blockIndex}-${index.contentIndex}`;
}

function contentEditAt(diff: UnifiedLetterDiff, blockIndex: number, contentIndex: number): ContentEdit | undefined {
  return diff.editedBlocks[blockIndex]?.contentEdits[contentIndex];
}

/** The word-level edits for a single still-existing literal, or undefined when it is unchanged. */
export function textEditForLiteral(diff: UnifiedLetterDiff, index: LiteralIndex): TextEdit | undefined {
  const contentEdit = contentEditAt(diff, index.blockIndex, index.contentIndex);
  if (!contentEdit) return undefined;

  if ("itemIndex" in index) {
    return isItemListEdit(contentEdit)
      ? contentEdit.itemEdits[index.itemIndex]?.textEdits[index.itemContentIndex]
      : undefined;
  }
  if ("rowIndex" in index) {
    return isTableEdit(contentEdit)
      ? contentEdit.rowEdits[index.rowIndex]?.cellEdits[index.cellIndex]?.textEdits[index.cellContentIndex]
      : undefined;
  }
  return isTextContentEdit(contentEdit) ? contentEdit.edit : undefined;
}

/**
 * Picks the entirely deleted nodes to render at `index`. When `trailing` is true, every entry from
 * `index` and onwards is collected instead, so deletions past the last surviving sibling are rendered
 * at the end rather than silently dropped.
 */
export function selectDeleted<T>(map: Record<string, T[]> | undefined, index: number, trailing: boolean): T[] {
  if (!map) return emptyList<T>();
  if (!trailing) return map[index] ?? emptyList<T>();

  const keys = Object.keys(map)
    .map(Number)
    .filter((key) => key >= index)
    .sort((a, b) => a - b);

  if (keys.length === 0) return emptyList<T>();
  return keys.flatMap((key) => map[key]);
}

export const deletedBlocksAt = (diff: UnifiedLetterDiff, blockIndex: number, trailing = false): AnyBlock[] =>
  selectDeleted(diff.deletedBlocks, blockIndex, trailing);

export const deletedContentAt = (
  diff: UnifiedLetterDiff,
  blockIndex: number,
  contentIndex: number,
  trailing = false,
): Content[] => selectDeleted(diff.editedBlocks[blockIndex]?.deletedContent, contentIndex, trailing);

export const deletedItemsAt = (
  diff: UnifiedLetterDiff,
  blockIndex: number,
  contentIndex: number,
  itemIndex: number,
  trailing = false,
): Item[] => {
  const contentEdit = contentEditAt(diff, blockIndex, contentIndex);
  return selectDeleted(
    contentEdit && isItemListEdit(contentEdit) ? contentEdit.deletedItems : undefined,
    itemIndex,
    trailing,
  );
};

export const deletedItemContentAt = (
  diff: UnifiedLetterDiff,
  blockIndex: number,
  contentIndex: number,
  itemIndex: number,
  itemContentIndex: number,
  trailing = false,
): TextContent[] => {
  const contentEdit = contentEditAt(diff, blockIndex, contentIndex);
  const itemEdit = contentEdit && isItemListEdit(contentEdit) ? contentEdit.itemEdits[itemIndex] : undefined;
  return selectDeleted(itemEdit?.deletedContent, itemContentIndex, trailing);
};

export const deletedRowsAt = (
  diff: UnifiedLetterDiff,
  blockIndex: number,
  contentIndex: number,
  rowIndex: number,
  trailing = false,
): Row[] => {
  const contentEdit = contentEditAt(diff, blockIndex, contentIndex);
  return selectDeleted(
    contentEdit && isTableEdit(contentEdit) ? contentEdit.deletedRows : undefined,
    rowIndex,
    trailing,
  );
};

export const deletedCellsAt = (
  diff: UnifiedLetterDiff,
  blockIndex: number,
  contentIndex: number,
  rowIndex: number,
  cellIndex: number,
  trailing = false,
): Cell[] => {
  const contentEdit = contentEditAt(diff, blockIndex, contentIndex);
  const rowEdit = contentEdit && isTableEdit(contentEdit) ? contentEdit.rowEdits[rowIndex] : undefined;
  return selectDeleted(rowEdit?.deletedCells, cellIndex, trailing);
};

export const deletedCellContentAt = (
  diff: UnifiedLetterDiff,
  blockIndex: number,
  contentIndex: number,
  rowIndex: number,
  cellIndex: number,
  cellContentIndex: number,
  trailing = false,
): TextContent[] => {
  const contentEdit = contentEditAt(diff, blockIndex, contentIndex);
  const rowEdit = contentEdit && isTableEdit(contentEdit) ? contentEdit.rowEdits[rowIndex] : undefined;
  return selectDeleted(rowEdit?.cellEdits[cellIndex]?.deletedContent, cellContentIndex, trailing);
};

export type BuildDiffSegmentsResult = { ok: true; segments: DiffSegment[] } | { ok: false; reason: string };

type BuildDiffSegmentsInput = {
  currentText: string;
  inserts: DiffTextSegment[];
  deletes: DiffDeletedTextSegment[];
};

export function buildDiffSegments({ currentText, inserts, deletes }: BuildDiffSegmentsInput): BuildDiffSegmentsResult {
  if (inserts.length === 0 && deletes.length === 0) {
    return { ok: true, segments: [{ type: "unchanged", text: currentText }] };
  }

  const sortedInserts = [...inserts].sort((a, b) => a.startOffset - b.startOffset || a.endOffset - b.endOffset);
  const sortedDeletes = [...deletes].sort((a, b) => a.startOffset - b.startOffset || a.endOffset - b.endOffset);

  for (const ins of sortedInserts) {
    if (ins.startOffset < 0 || ins.endOffset < ins.startOffset || ins.endOffset > currentText.length) {
      return {
        ok: false,
        reason: `Invalid insert range [${ins.startOffset}, ${ins.endOffset}] for text length ${currentText.length}`,
      };
    }
  }
  for (const del of sortedDeletes) {
    if (del.startOffset < 0 || del.endOffset < del.startOffset) {
      return { ok: false, reason: `Invalid delete range [${del.startOffset}, ${del.endOffset}]` };
    }
  }

  for (let i = 1; i < sortedInserts.length; i++) {
    if (sortedInserts[i].startOffset < sortedInserts[i - 1].endOffset) {
      return { ok: false, reason: `Overlapping insert ranges at index ${i}` };
    }
  }

  // Inserts are expressed in current-text (new) coordinates, deletes in original-text (old) coordinates.
  // Map both into the shared "unchanged" coordinate space so they can be interleaved without a single
  // cursor drifting out of sync when inserts and deletes change the text length differently.
  type MergedEvent =
    | { kind: "insert"; commonPos: number; order: number; entry: DiffTextSegment }
    | { kind: "delete"; commonPos: number; order: number; entry: DiffDeletedTextSegment };

  let insertedBefore = 0;
  const insertEvents: MergedEvent[] = sortedInserts.map((ins) => {
    const event: MergedEvent = {
      kind: "insert",
      commonPos: ins.startOffset - insertedBefore,
      order: ins.startOffset,
      entry: ins,
    };
    insertedBefore += ins.endOffset - ins.startOffset;
    return event;
  });

  let deletedBefore = 0;
  const deleteEvents: MergedEvent[] = sortedDeletes.map((del) => {
    const event: MergedEvent = {
      kind: "delete",
      commonPos: del.startOffset - deletedBefore,
      order: del.startOffset,
      entry: del,
    };
    deletedBefore += del.endOffset - del.startOffset;
    return event;
  });

  const merged = [...deleteEvents, ...insertEvents].sort((a, b) => {
    if (a.commonPos !== b.commonPos) return a.commonPos - b.commonPos;
    if (a.kind !== b.kind) return a.kind === "insert" ? -1 : 1;
    return a.order - b.order;
  });

  const segments: DiffSegment[] = [];
  let newCursor = 0;
  let commonCursor = 0;

  for (const event of merged) {
    const unchangedLen = event.commonPos - commonCursor;
    if (unchangedLen > 0) {
      const end = Math.min(newCursor + unchangedLen, currentText.length);
      if (end > newCursor) {
        segments.push({ type: "unchanged", text: currentText.slice(newCursor, end) });
      }
      newCursor = end;
      commonCursor = event.commonPos;
    }

    if (event.kind === "delete") {
      segments.push({ type: "deleted", text: event.entry.text });
    } else {
      const ins = event.entry;
      segments.push({ type: "inserted", text: currentText.slice(ins.startOffset, ins.endOffset) });
      newCursor = ins.endOffset;
    }
  }

  if (newCursor < currentText.length) {
    segments.push({ type: "unchanged", text: currentText.slice(newCursor) });
  }

  return { ok: true, segments };
}
