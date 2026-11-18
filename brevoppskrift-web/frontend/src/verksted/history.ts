/** Ren angre/gjør om-historikk. Påfølgende endringer med samme `coalesceKey` slås sammen til ett angresteg. */
export type History<T> = { past: T[]; present: T; future: T[]; lastKey?: string };

const MAX_HISTORY = 200;

export function createHistory<T>(present: T): History<T> {
  return { past: [], present, future: [] };
}

export function pushHistory<T>(history: History<T>, next: T, coalesceKey?: string): History<T> {
  if (Object.is(next, history.present)) {
    return history;
  }
  if (coalesceKey !== undefined && coalesceKey === history.lastKey) {
    return { ...history, present: next, future: [] };
  }
  return {
    past: [...history.past, history.present].slice(-MAX_HISTORY),
    present: next,
    future: [],
    lastKey: coalesceKey,
  };
}

export function undo<T>(history: History<T>): History<T> {
  const previous = history.past.at(-1);
  if (previous === undefined) {
    return history;
  }
  return { past: history.past.slice(0, -1), present: previous, future: [history.present, ...history.future] };
}

export function redo<T>(history: History<T>): History<T> {
  const [next, ...rest] = history.future;
  if (next === undefined) {
    return history;
  }
  return { past: [...history.past, history.present], present: next, future: rest };
}

export const canUndo = (history: History<unknown>) => history.past.length > 0;
export const canRedo = (history: History<unknown>) => history.future.length > 0;
