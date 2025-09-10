import type { Patch } from "immer";
export interface HistoryEntry {
  patches: Patch[];
  inversePatches: Patch[];
  label?: string;
}
