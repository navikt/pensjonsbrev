export type HashBoundValue<T> = {
  value: T;
  redigertBrevHash: string;
};

export function getSnapshotForHash<T>(snapshotsByHash: ReadonlyMap<string, T>, redigertBrevHash: string): T | undefined {
  return snapshotsByHash.get(redigertBrevHash);
}

export function pickValueForCurrentHash<T>(
  response: HashBoundValue<T> | undefined,
  currentSavedHash: string,
): T | undefined {
  if (!response) return undefined;
  if (response.redigertBrevHash !== currentSavedHash) return undefined;
  return response.value;
}

export function shouldRenderDiffMarkers<T>({
  visDiff,
  currentSavedHash,
  invalidatedDiffHashes,
  diff,
}: {
  visDiff: boolean;
  currentSavedHash: string;
  invalidatedDiffHashes: ReadonlySet<string>;
  diff: T | undefined;
}): boolean {
  return visDiff && diff !== undefined && !invalidatedDiffHashes.has(currentSavedHash);
}