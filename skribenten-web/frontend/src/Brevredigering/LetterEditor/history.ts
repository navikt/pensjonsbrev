import type { Patch } from "immer";
export interface HistoryEntry {
  patches: Patch[];
  inversePatches: Patch[];
  label?: string;
  timestamp?: number;
}
export interface History {
  entries: HistoryEntry[];
  entryPointer: number;
}

// Time threshold for merging text update actions in history (1 second)
const MERGE_TIME_THRESHOLD_MS = 1000;

export function updateHistory(
  history: { entries: HistoryEntry[]; entryPointer: number },
  newHistoryEntry: HistoryEntry,
): { entries: HistoryEntry[]; entryPointer: number } {
  const lastHistoryEntry = history.entries[history.entries.length - 1];
  // Check if we should merge with the previous history entry
  const shouldMerge =
    lastHistoryEntry &&
    newHistoryEntry.label === "TEXT_UPDATE" &&
    lastHistoryEntry.label === "TEXT_UPDATE" &&
    lastHistoryEntry.timestamp != null &&
    newHistoryEntry.timestamp != null &&
    newHistoryEntry.timestamp - lastHistoryEntry.timestamp < MERGE_TIME_THRESHOLD_MS &&
    newHistoryEntry.patches.length > 0 &&
    lastHistoryEntry.patches.length > 0 &&
    newHistoryEntry.patches[0]?.path.toString() === lastHistoryEntry.patches[0]?.path.toString();

  if (shouldMerge) {
    const mergedInversePatches = [...newHistoryEntry.inversePatches, ...lastHistoryEntry.inversePatches];
    const mergedEntry: HistoryEntry = { ...newHistoryEntry, inversePatches: mergedInversePatches };

    const newEntries = [...history.entries.slice(0, -1), mergedEntry];
    return {
      entries: newEntries,
      entryPointer: newEntries.length - 1,
    };
  } else {
    const newEntries = [...history.entries, newHistoryEntry];
    return {
      entries: newEntries,
      entryPointer: newEntries.length - 1,
    };
  }
}
