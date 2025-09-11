import type { Patch } from "immer";
export interface HistoryEntry {
  patches: Patch[];
  inversePatches: Patch[];
  label?: string;
}
export interface History {
  entries: HistoryEntry[];
  entryPointer: number;
}
