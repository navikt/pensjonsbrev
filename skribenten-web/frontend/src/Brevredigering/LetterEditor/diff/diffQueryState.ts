export type HashBoundValue<T> = {
  value: T;
  redigertBrevHash: string;
};

export function pickValueForCurrentHash<T>(
  response: HashBoundValue<T> | undefined,
  currentSavedHash: string,
): T | undefined {
  if (!response) return undefined;
  if (response.redigertBrevHash !== currentSavedHash) return undefined;
  return response.value;
}
