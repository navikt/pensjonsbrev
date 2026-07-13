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
  dismissedKeys: ReadonlySet<string>;
  dismissLiteral: (key: string) => void;
};

const EMPTY_MAP: DiffRangesByLiteral = new Map();
const EMPTY_SET: ReadonlySet<string> = new Set();

const AttestantDiffContext = createContext<AttestantDiffContextValue>({
  diffByLiteral: EMPTY_MAP,
  diffHash: undefined,
  dismissedKeys: EMPTY_SET,
  dismissLiteral: () => {},
});

export const AttestantDiffProvider = ({
  diff,
  diffHash,
  dismissedKeys,
  dismissLiteral,
  children,
}: {
  diff: LetterDiff | undefined;
  diffHash: string | undefined;
  dismissedKeys: ReadonlySet<string>;
  dismissLiteral: (key: string) => void;
  children: ReactNode;
}) => {
  const diffByLiteral = useMemo(() => (diff ? groupDiffByLiteral(diff) : EMPTY_MAP), [diff]);

  const value = useMemo(
    () => ({ diffByLiteral, diffHash, dismissedKeys, dismissLiteral }),
    [diffByLiteral, diffHash, dismissedKeys, dismissLiteral],
  );

  return <AttestantDiffContext.Provider value={value}>{children}</AttestantDiffContext.Provider>;
};

export const useAttestantDiff = () => useContext(AttestantDiffContext);

export function useDiffSegmentsForLiteral(
  literalIndex: LiteralIndex,
  currentText: string,
): DiffSegment[] | null {
  const { diffByLiteral, dismissedKeys } = useAttestantDiff();

  return useMemo(() => {
    const key = diffKey(literalIndex);
    if (dismissedKeys.has(key)) return null;

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
  }, [literalIndex, currentText, diffByLiteral, dismissedKeys]);
}
