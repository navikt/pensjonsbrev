import { createContext, type ReactNode, useContext, useMemo } from "react";

import { type LiteralIndex } from "~/Brevredigering/LetterEditor/model/state";
import { type AnyBlock, type Cell, type Content, type Item, type Row, type TextContent } from "~/types/brevbakerTypes";

import {
  buildDiffSegments,
  type DiffSegment,
  deletedBlocksAt,
  deletedCellContentAt,
  deletedCellsAt,
  deletedContentAt,
  deletedItemContentAt,
  deletedItemsAt,
  deletedRowsAt,
  diffKey,
  textEditForLiteral,
  type UnifiedLetterDiff,
} from "./diffModel";

type AttestantDiffContextValue = {
  diff: UnifiedLetterDiff | undefined;
  diffHash: string | undefined;
  invalidatedDiffHashes: ReadonlySet<string>;
  dismissedDiffs: ReadonlyMap<string, string>;
  dismissLiteral: (key: string, diffHash: string) => void;
  invalidateDiff: (diffHash: string) => void;
};

const EMPTY_DISMISSED_DIFFS: ReadonlyMap<string, string> = new Map();
const EMPTY_INVALIDATED_DIFF_HASHES: ReadonlySet<string> = new Set();
const EMPTY_DELETED: never[] = [];

const AttestantDiffContext = createContext<AttestantDiffContextValue>({
  diff: undefined,
  diffHash: undefined,
  invalidatedDiffHashes: EMPTY_INVALIDATED_DIFF_HASHES,
  dismissedDiffs: EMPTY_DISMISSED_DIFFS,
  dismissLiteral: () => {},
  invalidateDiff: () => {},
});

export const AttestantDiffProvider = ({
  diff,
  diffHash,
  invalidatedDiffHashes,
  dismissedDiffs,
  dismissLiteral,
  invalidateDiff,
  children,
}: {
  diff: UnifiedLetterDiff | undefined;
  diffHash: string | undefined;
  invalidatedDiffHashes: ReadonlySet<string>;
  dismissedDiffs: ReadonlyMap<string, string>;
  dismissLiteral: (key: string, diffHash: string) => void;
  invalidateDiff: (diffHash: string) => void;
  children: ReactNode;
}) => {
  const value = useMemo(
    () => ({ diff, diffHash, invalidatedDiffHashes, dismissedDiffs, dismissLiteral, invalidateDiff }),
    [diff, diffHash, invalidatedDiffHashes, dismissedDiffs, dismissLiteral, invalidateDiff],
  );

  return <AttestantDiffContext.Provider value={value}>{children}</AttestantDiffContext.Provider>;
};

export const useAttestantDiff = () => useContext(AttestantDiffContext);

/**
 * The diff that is currently allowed to decorate the letter: only present when it is bound to the
 * latest saved hash and that hash has not been invalidated by a structural edit.
 */
export function useActiveDiff(): UnifiedLetterDiff | undefined {
  const { diff, diffHash, invalidatedDiffHashes } = useAttestantDiff();
  if (!diff || !diffHash || invalidatedDiffHashes.has(diffHash)) return undefined;
  return diff;
}

function useDeleted<T>(select: (diff: UnifiedLetterDiff) => T[]): T[] {
  const diff = useActiveDiff();
  return diff ? select(diff) : (EMPTY_DELETED as unknown as T[]);
}

/** Entirely deleted blocks that should be rendered just before the surviving block at `blockIndex`. */
export const useDeletedBlocks = (blockIndex: number, trailing = false): AnyBlock[] =>
  useDeleted((diff) => deletedBlocksAt(diff, blockIndex, trailing));

/** Entirely deleted paragraph content within a still-existing block. */
export const useDeletedContent = (blockIndex: number, contentIndex: number, trailing = false): Content[] =>
  useDeleted((diff) => deletedContentAt(diff, blockIndex, contentIndex, trailing));

/** Entirely deleted items within a still-existing item list. */
export const useDeletedItems = (
  blockIndex: number,
  contentIndex: number,
  itemIndex: number,
  trailing = false,
): Item[] => useDeleted((diff) => deletedItemsAt(diff, blockIndex, contentIndex, itemIndex, trailing));

/** Entirely deleted text content within a still-existing item. */
export const useDeletedItemContent = (
  blockIndex: number,
  contentIndex: number,
  itemIndex: number,
  itemContentIndex: number,
  trailing = false,
): TextContent[] =>
  useDeleted((diff) => deletedItemContentAt(diff, blockIndex, contentIndex, itemIndex, itemContentIndex, trailing));

/** Entirely deleted rows within a still-existing table. */
export const useDeletedRows = (blockIndex: number, contentIndex: number, rowIndex: number, trailing = false): Row[] =>
  useDeleted((diff) => deletedRowsAt(diff, blockIndex, contentIndex, rowIndex, trailing));

/** Entirely deleted cells within a still-existing row. */
export const useDeletedCells = (
  blockIndex: number,
  contentIndex: number,
  rowIndex: number,
  cellIndex: number,
  trailing = false,
): Cell[] => useDeleted((diff) => deletedCellsAt(diff, blockIndex, contentIndex, rowIndex, cellIndex, trailing));

/** Entirely deleted text content within a still-existing table cell. */
export const useDeletedCellContent = (
  blockIndex: number,
  contentIndex: number,
  rowIndex: number,
  cellIndex: number,
  cellContentIndex: number,
  trailing = false,
): TextContent[] =>
  useDeleted((diff) =>
    deletedCellContentAt(diff, blockIndex, contentIndex, rowIndex, cellIndex, cellContentIndex, trailing),
  );

export function useDiffSegmentsForLiteral(literalIndex: LiteralIndex, currentText: string): DiffSegment[] | null {
  const { diff, diffHash, invalidatedDiffHashes, dismissedDiffs } = useAttestantDiff();

  return useMemo(() => {
    if (!diff || !diffHash) return null;
    if (invalidatedDiffHashes.has(diffHash)) return null;

    const key = diffKey(literalIndex);
    if (dismissedDiffs.get(key) === diffHash) return null;

    const textEdit = textEditForLiteral(diff, literalIndex);
    if (!textEdit || (textEdit.inserts.length === 0 && textEdit.deletes.length === 0)) return null;

    const result = buildDiffSegments({
      currentText,
      inserts: textEdit.inserts,
      deletes: textEdit.deletes,
    });

    if (!result.ok) {
      console.warn(`[AttestantDiff] Rejected diff for literal ${key}: ${result.reason}`);
      return null;
    }

    return result.segments;
  }, [literalIndex, currentText, diff, diffHash, invalidatedDiffHashes, dismissedDiffs]);
}
