import {
  buildDiffSegments,
  type DiffSegment,
  type DiffTextSegment,
} from "~/Brevredigering/LetterEditor/diff/diffModel";
import {
  type BlockV2,
  type CellV2,
  type ColumnSpecV2,
  type ContentIndexV2,
  type DiffSegmentV2,
  type ItemV2,
  type ListBlockEditV2,
  type RowEditV2,
  type RowV2,
  type TableEditV2,
  type TextEditV2,
  type TextOnlyEditV2,
  type TextV2,
  type UnifiedDiffV2,
} from "~/types/brevV2";

import { type ContainerPath, pathKey } from "../model/paths";

export type { DiffSegment };

export type Positioned<T> = Record<string, T[]>;

export type ContainerEditV2 = Pick<TextOnlyEditV2, "textEdits" | "deletedContent">;

export type TextPositionV2 = { path: ContainerPath; content: number };

const NONE: never[] = [];

export function selectDeleted<T>(map: Positioned<T> | undefined, index: number, trailing: boolean): readonly T[] {
  if (!map) return NONE;
  if (!trailing) return map[index] ?? NONE;
  return Object.keys(map)
    .map(Number)
    .filter((key) => key >= index)
    .sort((a, b) => a - b)
    .flatMap((key) => map[key]);
}

export function isEmptyDiff(diff: UnifiedDiffV2): boolean {
  return Object.keys(diff.editedBlocks).length === 0 && Object.keys(diff.deletedBlocks).length === 0;
}

function listEditAt(diff: UnifiedDiffV2, block: number): ListBlockEditV2 | undefined {
  const edit = diff.editedBlocks[block];
  return edit?.type === "LIST" ? edit : undefined;
}

function tableEditAt(diff: UnifiedDiffV2, block: number): TableEditV2 | undefined {
  const edit = diff.editedBlocks[block];
  return edit?.type === "TABLE" ? edit : undefined;
}

function rowEditAt(diff: UnifiedDiffV2, block: number, row: number): RowEditV2 | undefined {
  return tableEditAt(diff, block)?.rowEdits[row];
}

export function containerEditAt(diff: UnifiedDiffV2, path: ContainerPath): ContainerEditV2 | undefined {
  switch (path.kind) {
    case "title":
      return undefined;
    case "block": {
      const edit = diff.editedBlocks[path.block];
      return edit?.type === "TEXT" ? edit : undefined;
    }
    case "item":
      return listEditAt(diff, path.block)?.itemEdits[path.item];
    case "header":
      return tableEditAt(diff, path.block)?.columnSpecEdits[path.column];
    case "cell":
      return rowEditAt(diff, path.block, path.row)?.cellEdits[path.cell];
  }
}

export const textEditAt = (diff: UnifiedDiffV2, path: ContainerPath, content: number): TextEditV2 | undefined =>
  containerEditAt(diff, path)?.textEdits[content];

export const deletedBlocksAt = (diff: UnifiedDiffV2, block: number, trailing = false): readonly BlockV2[] =>
  selectDeleted(diff.deletedBlocks, block, trailing);

export const deletedContentAt = (
  diff: UnifiedDiffV2,
  path: ContainerPath,
  content: number,
  trailing = false,
): readonly TextV2[] => selectDeleted(containerEditAt(diff, path)?.deletedContent, content, trailing);

export const deletedItemsAt = (diff: UnifiedDiffV2, block: number, item: number, trailing = false): readonly ItemV2[] =>
  selectDeleted(listEditAt(diff, block)?.deletedItems, item, trailing);

export const deletedColumnsAt = (
  diff: UnifiedDiffV2,
  block: number,
  column: number,
  trailing = false,
): readonly ColumnSpecV2[] => selectDeleted(tableEditAt(diff, block)?.deletedColumnSpecs, column, trailing);

export const deletedRowsAt = (diff: UnifiedDiffV2, block: number, row: number, trailing = false): readonly RowV2[] =>
  selectDeleted(tableEditAt(diff, block)?.deletedRows, row, trailing);

export const deletedCellsAt = (
  diff: UnifiedDiffV2,
  block: number,
  row: number,
  cell: number,
  trailing = false,
): readonly CellV2[] => selectDeleted(rowEditAt(diff, block, row)?.deletedCells, cell, trailing);

export function textPositionOf(index: ContentIndexV2): TextPositionV2 | undefined {
  switch (index.type) {
    case "BLOCK_CONTENT":
      return { path: { kind: "block", block: index.blockIndex }, content: index.contentIndex };
    case "ITEM_CONTENT":
      return {
        path: { kind: "item", block: index.blockIndex, item: index.itemIndex },
        content: index.itemContentIndex,
      };
    case "COLUMN_SPEC_CONTENT":
      return {
        path: { kind: "header", block: index.blockIndex, column: index.columnIndex },
        content: index.columnContentIndex,
      };
    case "TABLE_CELL_CONTENT":
      return {
        path: { kind: "cell", block: index.blockIndex, row: index.rowIndex, cell: index.cellIndex },
        content: index.cellContentIndex,
      };
    case "BLOCK":
    case "ITEM":
    case "COLUMN_SPEC":
    case "TABLE_ROW":
    case "TABLE_CELL":
      return undefined;
  }
}

export const contentKey = (path: ContainerPath, content: number): string => `${pathKey(path)}:${content}`;

export type SplitLookupV2 = ReadonlyMap<string, readonly DiffTextSegment[]>;

export function groupSplitSegments(segments: readonly DiffSegmentV2[]): SplitLookupV2 {
  const lookup = new Map<string, DiffTextSegment[]>();
  for (const { index, startOffset, endOffset } of segments) {
    const position = textPositionOf(index);
    if (!position) continue;
    const key = contentKey(position.path, position.content);
    lookup.set(key, [...(lookup.get(key) ?? []), { startOffset, endOffset }]);
  }
  return lookup;
}

export const splitSegmentsAt = (lookup: SplitLookupV2, path: ContainerPath, content: number) =>
  lookup.get(contentKey(path, content)) ?? NONE;

export type SegmentFilter = { showInsertions: boolean; showDeletions: boolean };

export type SegmentsResult = { ok: true; segments: DiffSegment[] | undefined } | { ok: false; reason: string };

export function segmentsFor(
  diff: UnifiedDiffV2,
  path: ContainerPath,
  content: number,
  currentText: string,
  { showInsertions, showDeletions }: SegmentFilter,
): SegmentsResult {
  const textEdit = textEditAt(diff, path, content);
  if (!textEdit || (textEdit.inserts.length === 0 && textEdit.deletes.length === 0)) {
    return { ok: true, segments: undefined };
  }

  const result = buildDiffSegments({ currentText, inserts: textEdit.inserts, deletes: textEdit.deletes });
  if (!result.ok) return result;

  const segments = result.segments
    .filter((segment) => showDeletions || segment.type !== "deleted")
    .map(
      (segment): DiffSegment =>
        showInsertions || segment.type !== "inserted" ? segment : { ...segment, type: "unchanged" },
    );

  return { ok: true, segments: segments.some((segment) => segment.type !== "unchanged") ? segments : undefined };
}
