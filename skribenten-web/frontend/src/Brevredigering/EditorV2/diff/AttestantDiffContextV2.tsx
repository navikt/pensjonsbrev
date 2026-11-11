import { createContext, type ReactNode, useContext, useMemo } from "react";

import { type UnifiedDiffV2 } from "~/types/brevV2";

import { type ContainerPath, pathKey } from "../model/paths";
import { contentKey, type DiffSegment, segmentsFor } from "./diffModel";

type AttestantDiffContextValueV2 = {
  diff: UnifiedDiffV2 | undefined;
  diffHash: string | undefined;
  showInsertions: boolean;
  showDeletions: boolean;
  disableDiffMode: () => void;
};

const NONE: never[] = [];

const NO_DIFF: AttestantDiffContextValueV2 = {
  diff: undefined,
  diffHash: undefined,
  showInsertions: false,
  showDeletions: false,
  disableDiffMode: () => {},
};

const AttestantDiffContextV2 = createContext<AttestantDiffContextValueV2>(NO_DIFF);

export const AttestantDiffProviderV2 = ({
  diff,
  diffHash,
  showInsertions = true,
  showDeletions = true,
  disableDiffMode,
  children,
}: {
  diff: UnifiedDiffV2 | undefined;
  diffHash: string | undefined;
  showInsertions?: boolean;
  showDeletions?: boolean;
  disableDiffMode: () => void;
  children: ReactNode;
}) => {
  const value = useMemo(
    () => ({ diff, diffHash, showInsertions, showDeletions, disableDiffMode }),
    [diff, diffHash, showInsertions, showDeletions, disableDiffMode],
  );

  return <AttestantDiffContextV2.Provider value={value}>{children}</AttestantDiffContextV2.Provider>;
};

export const useAttestantDiffV2 = () => useContext(AttestantDiffContextV2);

function useActiveDiffV2(): UnifiedDiffV2 | undefined {
  const { diff, diffHash } = useAttestantDiffV2();
  return diff && diffHash ? diff : undefined;
}

export function useDeletedV2<T>(select: (diff: UnifiedDiffV2) => readonly T[]): readonly T[] {
  const diff = useActiveDiffV2();
  const { showDeletions } = useAttestantDiffV2();
  return diff && showDeletions ? select(diff) : NONE;
}

export function useDiffSegmentsV2(path: ContainerPath, content: number, text: string): DiffSegment[] | undefined {
  const diff = useActiveDiffV2();
  const { showInsertions, showDeletions } = useAttestantDiffV2();
  const key = pathKey(path);

  return useMemo(() => {
    if (!diff) return undefined;
    const result = segmentsFor(diff, path, content, text, { showInsertions, showDeletions });
    if (result.ok) return result.segments;
    console.warn(`[AttestantDiff] Rejected diff for ${contentKey(path, content)}: ${result.reason}`);
    return undefined;
  }, [diff, key, content, text, showInsertions, showDeletions]);
}
