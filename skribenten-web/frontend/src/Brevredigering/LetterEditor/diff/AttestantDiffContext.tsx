import { createContext, type ReactNode, useContext, useEffect, useMemo } from "react";

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
  /** Called by every editing path: the attestant editing turns diff mode off entirely. */
  disableDiff: () => void;
  reportRejectedLiteral: (key: string, diffHash: string, reason: string | null) => void;
};

const EMPTY_DELETED: never[] = [];

const AttestantDiffContext = createContext<AttestantDiffContextValue>({
  diff: undefined,
  diffHash: undefined,
  disableDiff: () => {},
  reportRejectedLiteral: () => {},
});

export const AttestantDiffProvider = ({
  diff,
  diffHash,
  disableDiff,
  reportRejectedLiteral = () => {},
  children,
}: {
  diff: UnifiedLetterDiff | undefined;
  diffHash: string | undefined;
  disableDiff: () => void;
  reportRejectedLiteral?: (key: string, diffHash: string, reason: string | null) => void;
  children: ReactNode;
}) => {
  const value = useMemo(
    () => ({ diff, diffHash, disableDiff, reportRejectedLiteral }),
    [diff, diffHash, disableDiff, reportRejectedLiteral],
  );

  return <AttestantDiffContext.Provider value={value}>{children}</AttestantDiffContext.Provider>;
};

export const useAttestantDiff = () => useContext(AttestantDiffContext);

/** The diff that is currently allowed to decorate the letter: only present when bound to the latest saved hash. */
export function useActiveDiff(): UnifiedLetterDiff | undefined {
  const { diff, diffHash } = useAttestantDiff();
  if (!diff || !diffHash) return undefined;
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
  const { diff, diffHash, reportRejectedLiteral } = useAttestantDiff();
  const key = diffKey(literalIndex);

  const result = useMemo<{ segments: DiffSegment[] | null; rejectionReason: string | null }>(() => {
    if (!diff || !diffHash) return { segments: null, rejectionReason: null };

    const textEdit = textEditForLiteral(diff, literalIndex);
    if (!textEdit || (textEdit.inserts.length === 0 && textEdit.deletes.length === 0)) {
      return { segments: null, rejectionReason: null };
    }

    const result = buildDiffSegments({
      currentText,
      inserts: textEdit.inserts,
      deletes: textEdit.deletes,
    });

    if (!result.ok) {
      console.warn(`[AttestantDiff] Rejected diff for literal ${key}: ${result.reason}`);
      return { segments: null, rejectionReason: result.reason };
    }

    return { segments: result.segments, rejectionReason: null };
  }, [currentText, diff, diffHash, key]);

  useEffect(() => {
    if (!diffHash) return;
    reportRejectedLiteral(key, diffHash, result.rejectionReason);
  }, [diffHash, key, reportRejectedLiteral, result.rejectionReason]);

  return result.segments;
}
