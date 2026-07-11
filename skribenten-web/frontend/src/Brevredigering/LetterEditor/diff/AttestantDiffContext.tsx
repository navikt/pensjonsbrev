import { createContext, type ReactNode, useContext, useMemo } from "react";

import {
  buildDiffSegments,
  type DiffRangesByLiteral,
  type DiffSegment,
  diffKey,
  groupDiffByLiteral,
  type LetterDiff,
} from "./diffModel";

type AttestantDiffContextValue = {
  diffByLiteral: DiffRangesByLiteral;
  dismissedKeys: ReadonlySet<string>;
  dismissLiteral: (key: string) => void;
};

const EMPTY_MAP: DiffRangesByLiteral = new Map();
const EMPTY_SET: ReadonlySet<string> = new Set();

const AttestantDiffContext = createContext<AttestantDiffContextValue>({
  diffByLiteral: EMPTY_MAP,
  dismissedKeys: EMPTY_SET,
  dismissLiteral: () => {},
});

export const AttestantDiffProvider = ({
  diff,
  dismissedKeys,
  dismissLiteral,
  children,
}: {
  diff: LetterDiff | undefined;
  dismissedKeys: ReadonlySet<string>;
  dismissLiteral: (key: string) => void;
  children: ReactNode;
}) => {
  const diffByLiteral = useMemo(() => (diff ? groupDiffByLiteral(diff) : EMPTY_MAP), [diff]);

  const value = useMemo(
    () => ({ diffByLiteral, dismissedKeys, dismissLiteral }),
    [diffByLiteral, dismissedKeys, dismissLiteral],
  );

  return <AttestantDiffContext.Provider value={value}>{children}</AttestantDiffContext.Provider>;
};

export const useAttestantDiff = () => useContext(AttestantDiffContext);

export function useDiffSegmentsForLiteral(
  blockIndex: number,
  contentIndex: number,
  currentText: string,
): DiffSegment[] | null {
  const { diffByLiteral, dismissedKeys } = useAttestantDiff();

  return useMemo(() => {
    const key = diffKey({ blockIndex, contentIndex });
    if (dismissedKeys.has(key)) return null;

    const ranges = diffByLiteral.get(key);
    if (!ranges || (ranges.inserts.length === 0 && ranges.deletes.length === 0)) return null;

    return buildDiffSegments({
      currentText,
      inserts: ranges.inserts,
      deletes: ranges.deletes,
    });
  }, [blockIndex, contentIndex, currentText, diffByLiteral, dismissedKeys]);
}
