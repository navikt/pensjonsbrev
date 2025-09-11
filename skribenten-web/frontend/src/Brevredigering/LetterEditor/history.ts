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
