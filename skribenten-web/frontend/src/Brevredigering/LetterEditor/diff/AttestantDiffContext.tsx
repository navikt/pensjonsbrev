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
  dismissedDiffs: ReadonlyMap<string, string>;
  dismissLiteral: (key: string, diffHash: string) => void;
};

const EMPTY_MAP: DiffRangesByLiteral = new Map();
const EMPTY_DISMISSED_DIFFS: ReadonlyMap<string, string> = new Map();

const AttestantDiffContext = createContext<AttestantDiffContextValue>({
  diffByLiteral: EMPTY_MAP,
  diffHash: undefined,
  dismissedDiffs: EMPTY_DISMISSED_DIFFS,
  dismissLiteral: () => {},
});

export const AttestantDiffProvider = ({
  diff,
  diffHash,
  dismissedDiffs,
  dismissLiteral,
  children,
}: {
  diff: LetterDiff | undefined;
  diffHash: string | undefined;
  dismissedDiffs: ReadonlyMap<string, string>;
  dismissLiteral: (key: string, diffHash: string) => void;
  children: ReactNode;
}) => {
  const diffByLiteral = useMemo(() => (diff ? groupDiffByLiteral(diff) : EMPTY_MAP), [diff]);

  const value = useMemo(
    () => ({ diffByLiteral, diffHash, dismissedDiffs, dismissLiteral }),
    [diffByLiteral, diffHash, dismissedDiffs, dismissLiteral],
  );

  return <AttestantDiffContext.Provider value={value}>{children}</AttestantDiffContext.Provider>;
};

export const useAttestantDiff = () => useContext(AttestantDiffContext);

export function useDiffSegmentsForLiteral(
  literalIndex: LiteralIndex,
  currentText: string,
): DiffSegment[] | null {
  const { diffByLiteral, diffHash, dismissedDiffs } = useAttestantDiff();

  return useMemo(() => {
    if (!diffHash) return null;

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
  }, [literalIndex, currentText, diffByLiteral, diffHash, dismissedDiffs]);
}
