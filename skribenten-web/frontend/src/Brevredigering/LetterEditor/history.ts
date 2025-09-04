import type { Patch } from "immer";
import { applyPatches } from "immer";

export interface HistoryEntry {
  patches: Patch[];
  inversePatches: Patch[];
  label?: string;
}

export interface History<TState extends object> {
  push(e: HistoryEntry): void;
  canUndo(): boolean;
  canRedo(): boolean;
  undo(state: TState): TState;
  redo(state: TState): TState;
  clear(): void;
  pointer(): number;
  length(): number;
  entries(): ReadonlyArray<HistoryEntry>;
}

export function createHistory<TState extends object>(limit = 200): History<TState> {
  let entries: HistoryEntry[] = [];
  let pointer = -1;

  function push(entry: HistoryEntry) {
    if (pointer < entries.length - 1) {
      entries = entries.slice(0, pointer + 1);
    }
    entries.push(entry);
    pointer++;
    if (entries.length > limit) {
      entries.shift();
      pointer--;
    }
  }

  const canUndo = () => pointer >= 0;
  const canRedo = () => pointer < entries.length - 1;

  function undo(state: TState): TState {
    if (!canUndo()) return state;
    const { inversePatches } = entries[pointer];
    pointer--;
    return applyPatches(state, inversePatches);
  }

  function redo(state: TState): TState {
    if (!canRedo()) return state;
    pointer++;
    const { patches } = entries[pointer];
    return applyPatches(state, patches);
  }

  function clear() {
    entries = [];
    pointer = -1;
  }

  return {
    push,
    canUndo,
    canRedo,
    undo,
    redo,
    clear,
    pointer: () => pointer,
    length: () => entries.length,
    entries: () => entries,
  };
}
