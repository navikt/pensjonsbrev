import { describe, expect, it } from "vitest";

import { type DiffSegmentV2, type UnifiedDiffV2 } from "~/types/brevV2";
import { cell, column, item, literal, paragraph, row } from "~test/support/editorV2TestUtils";

import {
  deletedBlocksAt,
  deletedCellsAt,
  deletedColumnsAt,
  deletedContentAt,
  deletedItemsAt,
  deletedRowsAt,
  groupSplitSegments,
  isEmptyDiff,
  segmentsFor,
  splitSegmentsAt,
  textEditAt,
  textPositionOf,
} from "./diffModel";

const insert = (startOffset: number, endOffset: number) => ({ inserts: [{ startOffset, endOffset }], deletes: [] });

const removedText = literal("borte");
const removedBlock = paragraph(literal("slettet avsnitt"));
const removedItem = item(literal("slettet punkt"));
const removedColumn = column(literal("slettet kolonne"));
const removedRow = row(cell(literal("slettet rad")));
const removedCell = cell(literal("slettet celle"));

const diff: UnifiedDiffV2 = {
  type: "UNIFIED",
  deletedBlocks: { 1: [removedBlock], 5: [removedBlock, removedBlock] },
  editedBlocks: {
    0: { type: "TEXT", textEdits: { 2: insert(0, 1) }, deletedContent: { 1: [removedText] } },
    2: {
      type: "LIST",
      deletedItems: { 0: [removedItem] },
      itemEdits: { 1: { textEdits: { 0: insert(0, 2) }, deletedContent: { 3: [removedText] } } },
    },
    3: {
      type: "TABLE",
      columnSpecEdits: { 1: { textEdits: { 0: insert(0, 3) }, deletedContent: {} } },
      deletedColumnSpecs: { 2: [removedColumn] },
      deletedRows: { 4: [removedRow] },
      rowEdits: {
        1: {
          cellEdits: { 0: { textEdits: { 1: insert(0, 4) }, deletedContent: { 0: [removedText] } } },
          deletedCells: { 2: [removedCell] },
        },
      },
    },
  },
};

describe("textEditAt", () => {
  it("finds word edits for every kind of text container", () => {
    expect(textEditAt(diff, { kind: "block", block: 0 }, 2)).toEqual(insert(0, 1));
    expect(textEditAt(diff, { kind: "item", block: 2, item: 1 }, 0)).toEqual(insert(0, 2));
    expect(textEditAt(diff, { kind: "header", block: 3, column: 1 }, 0)).toEqual(insert(0, 3));
    expect(textEditAt(diff, { kind: "cell", block: 3, row: 1, cell: 0 }, 1)).toEqual(insert(0, 4));
  });

  it("ignores edits when the path does not match the kind of block edit", () => {
    expect(textEditAt(diff, { kind: "item", block: 0, item: 2 }, 0)).toBeUndefined();
    expect(textEditAt(diff, { kind: "block", block: 2 }, 0)).toBeUndefined();
    expect(textEditAt(diff, { kind: "cell", block: 2, row: 1, cell: 0 }, 1)).toBeUndefined();
    expect(textEditAt(diff, { kind: "title" }, 0)).toBeUndefined();
  });
});

describe("deleted elements", () => {
  it("are found before the surviving sibling at their unified position", () => {
    expect(deletedBlocksAt(diff, 1)).toEqual([removedBlock]);
    expect(deletedContentAt(diff, { kind: "block", block: 0 }, 1)).toEqual([removedText]);
    expect(deletedContentAt(diff, { kind: "item", block: 2, item: 1 }, 3)).toEqual([removedText]);
    expect(deletedContentAt(diff, { kind: "cell", block: 3, row: 1, cell: 0 }, 0)).toEqual([removedText]);
    expect(deletedItemsAt(diff, 2, 0)).toEqual([removedItem]);
    expect(deletedColumnsAt(diff, 3, 2)).toEqual([removedColumn]);
    expect(deletedRowsAt(diff, 3, 4)).toEqual([removedRow]);
    expect(deletedCellsAt(diff, 3, 1, 2)).toEqual([removedCell]);
  });

  it("are empty at positions without deletions", () => {
    expect(deletedBlocksAt(diff, 0)).toEqual([]);
    expect(deletedContentAt(diff, { kind: "block", block: 0 }, 0)).toEqual([]);
    expect(deletedItemsAt(diff, 0, 0)).toEqual([]);
    expect(deletedRowsAt(diff, 2, 4)).toEqual([]);
  });

  it("collects every deletion from the position and onwards when trailing", () => {
    expect(deletedBlocksAt(diff, 1, true)).toEqual([removedBlock, removedBlock, removedBlock]);
    expect(deletedBlocksAt(diff, 2, true)).toEqual([removedBlock, removedBlock]);
    expect(deletedBlocksAt(diff, 6, true)).toEqual([]);
  });
});

describe("isEmptyDiff", () => {
  it("is true only when nothing was edited or deleted", () => {
    expect(isEmptyDiff({ type: "UNIFIED", editedBlocks: {}, deletedBlocks: {} })).toBe(true);
    expect(isEmptyDiff(diff)).toBe(false);
  });
});

describe("textPositionOf", () => {
  it("maps text content indexes to a container path and content index", () => {
    expect(textPositionOf({ type: "BLOCK_CONTENT", blockIndex: 1, contentIndex: 2 })).toEqual({
      path: { kind: "block", block: 1 },
      content: 2,
    });
    expect(textPositionOf({ type: "ITEM_CONTENT", blockIndex: 1, itemIndex: 2, itemContentIndex: 3 })).toEqual({
      path: { kind: "item", block: 1, item: 2 },
      content: 3,
    });
    expect(
      textPositionOf({ type: "COLUMN_SPEC_CONTENT", blockIndex: 1, columnIndex: 2, columnContentIndex: 3 }),
    ).toEqual({ path: { kind: "header", block: 1, column: 2 }, content: 3 });
    expect(
      textPositionOf({ type: "TABLE_CELL_CONTENT", blockIndex: 1, rowIndex: 2, cellIndex: 3, cellContentIndex: 4 }),
    ).toEqual({ path: { kind: "cell", block: 1, row: 2, cell: 3 }, content: 4 });
  });

  it("has no text position for structural indexes", () => {
    expect(textPositionOf({ type: "BLOCK", blockIndex: 1 })).toBeUndefined();
    expect(textPositionOf({ type: "TABLE_ROW", blockIndex: 1, rowIndex: 0 })).toBeUndefined();
  });
});

describe("split diff lookup", () => {
  const segments: DiffSegmentV2[] = [
    { index: { type: "BLOCK_CONTENT", blockIndex: 0, contentIndex: 1 }, startOffset: 0, endOffset: 3 },
    { index: { type: "BLOCK_CONTENT", blockIndex: 0, contentIndex: 1 }, startOffset: 5, endOffset: 7 },
    { index: { type: "ITEM_CONTENT", blockIndex: 0, itemIndex: 1, itemContentIndex: 0 }, startOffset: 1, endOffset: 2 },
    { index: { type: "BLOCK", blockIndex: 0 }, startOffset: 0, endOffset: 0 },
  ];
  const lookup = groupSplitSegments(segments);

  it("groups segments by container and content", () => {
    expect(splitSegmentsAt(lookup, { kind: "block", block: 0 }, 1)).toEqual([
      { startOffset: 0, endOffset: 3 },
      { startOffset: 5, endOffset: 7 },
    ]);
    expect(splitSegmentsAt(lookup, { kind: "item", block: 0, item: 1 }, 0)).toEqual([{ startOffset: 1, endOffset: 2 }]);
  });

  it("does not confuse a block with an item of the same indexes", () => {
    expect(splitSegmentsAt(lookup, { kind: "block", block: 0 }, 0)).toEqual([]);
    expect(splitSegmentsAt(lookup, { kind: "item", block: 0, item: 0 }, 1)).toEqual([]);
  });
});

describe("segmentsFor", () => {
  const mixed: UnifiedDiffV2 = {
    type: "UNIFIED",
    deletedBlocks: {},
    editedBlocks: {
      0: {
        type: "TEXT",
        textEdits: {
          0: { inserts: [{ startOffset: 0, endOffset: 1 }], deletes: [{ startOffset: 1, endOffset: 4, text: "old" }] },
          1: { inserts: [{ startOffset: 0, endOffset: 20 }], deletes: [] },
        },
        deletedContent: {},
      },
    },
  };
  const path = { kind: "block", block: 0 } as const;
  const types = (showInsertions: boolean, showDeletions: boolean) => {
    const result = segmentsFor(mixed, path, 0, "AA", { showInsertions, showDeletions });
    return result.ok ? result.segments?.map((segment) => segment.type) : result.reason;
  };

  it("filters insertions and deletions independently", () => {
    expect(types(true, true)).toEqual(["inserted", "unchanged", "deleted"]);
    expect(types(true, false)).toEqual(["inserted", "unchanged"]);
    expect(types(false, true)).toEqual(["unchanged", "unchanged", "deleted"]);
    expect(types(false, false)).toBeUndefined();
  });

  it("leaves unchanged text undecorated", () => {
    expect(segmentsFor(mixed, path, 2, "AA", { showInsertions: true, showDeletions: true })).toEqual({
      ok: true,
      segments: undefined,
    });
  });

  it("rejects ranges outside the current text", () => {
    expect(segmentsFor(mixed, path, 1, "AA", { showInsertions: true, showDeletions: true }).ok).toBe(false);
  });
});
