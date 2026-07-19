import { createContext, type ReactNode, useContext, useMemo } from "react";

import {
  buildDiffSegments,
  type DiffRangesByLiteral,
  type DiffSegment,
  diffKey,
  groupDiffByLiteral,
  type LetterDiff,
} from "./diffModel";
import { type LiteralIndex } from "~/Brevredigering/LetterEditor/model/state";

type AttestantDiffContextValue = {
  diffByLiteral: DiffRangesByLiteral;
  diffHash: string | undefined;
  invalidatedDiffHashes: ReadonlySet<string>;
  dismissedDiffs: ReadonlyMap<string, string>;
  dismissLiteral: (key: string, diffHash: string) => void;
  invalidateDiff: (diffHash: string) => void;
};

const EMPTY_MAP: DiffRangesByLiteral = new Map();
const EMPTY_DISMISSED_DIFFS: ReadonlyMap<string, string> = new Map();
const EMPTY_INVALIDATED_DIFF_HASHES: ReadonlySet<string> = new Set();

const AttestantDiffContext = createContext<AttestantDiffContextValue>({
  diffByLiteral: EMPTY_MAP,
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
  diff: LetterDiff | undefined;
  diffHash: string | undefined;
  invalidatedDiffHashes: ReadonlySet<string>;
  dismissedDiffs: ReadonlyMap<string, string>;
  dismissLiteral: (key: string, diffHash: string) => void;
  invalidateDiff: (diffHash: string) => void;
  children: ReactNode;
}) => {
  const diffByLiteral = useMemo(() => (diff ? groupDiffByLiteral(diff) : EMPTY_MAP), [diff]);

  const value = useMemo(
    () => ({ diffByLiteral, diffHash, invalidatedDiffHashes, dismissedDiffs, dismissLiteral, invalidateDiff }),
    [diffByLiteral, diffHash, invalidatedDiffHashes, dismissedDiffs, dismissLiteral, invalidateDiff],
  );

  return <AttestantDiffContext.Provider value={value}>{children}</AttestantDiffContext.Provider>;
};

export const useAttestantDiff = () => useContext(AttestantDiffContext);

export function useDiffSegmentsForLiteral(
  literalIndex: LiteralIndex,
  currentText: string,
): DiffSegment[] | null {
  const { diffByLiteral, diffHash, invalidatedDiffHashes, dismissedDiffs } = useAttestantDiff();

  return useMemo(() => {
    if (!diffHash) return null;
    if (invalidatedDiffHashes.has(diffHash)) return null;

    const key = diffKey(literalIndex);
    if (dismissedDiffs.get(key) === diffHash) return null;

    const ranges = diffByLiteral.get(key);
    if (!ranges || (ranges.inserts.length === 0 && ranges.deletes.length === 0)) return null;

    const result = buildDiffSegments({
      currentText,
      inserts: ranges.inserts,
      deletes: ranges.deletes,
    });

    if (!result.ok) {
      console.warn(`[AttestantDiff] Rejected diff for literal ${key}: ${result.reason}`);
      return null;
    }

    return result.segments;
  }, [literalIndex, currentText, diffByLiteral, diffHash, invalidatedDiffHashes, dismissedDiffs]);
}
