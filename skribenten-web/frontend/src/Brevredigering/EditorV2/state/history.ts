import { applyPatches, type Patch } from "immer";

export type Snapshot<Document, Valg> = {
  redigertBrev: Document;
  redigertBrevHash: string;
  saksbehandlerValg: Valg;
};

export type PatchHistoryEntry = {
  type: "PATCH";
  patches: Patch[];
  inversePatches: Patch[];
  kind?: "TEXT_UPDATE";
  timestamp?: number;
};

export type SnapshotHistoryEntry<Document, Valg> = {
  type: "SAKSBEHANDLERVALG_ENDRET";
  before: Snapshot<Document, Valg>;
  after: Snapshot<Document, Valg>;
  timestamp?: number;
};

export type HistoryEntry<Document = unknown, Valg = unknown> = PatchHistoryEntry | SnapshotHistoryEntry<Document, Valg>;

export type History<Document = unknown, Valg = unknown> = {
  entries: HistoryEntry<Document, Valg>[];
  entryPointer: number;
};

export type HistoryState<Document, Valg> = Snapshot<Document, Valg> & {
  saveStatus: "DIRTY" | "SAVE_PENDING" | "SAVED";
  history: History<Document, Valg>;
};

const MERGE_TIME_THRESHOLD_MS = 1000;
const TEXT_UPDATE_PATHS: (string | number)[] = ["editedText", "saveStatus", "focus"];

export const emptyHistory = <Document, Valg>(): History<Document, Valg> => ({ entries: [], entryPointer: -1 });

const lastSegment = (patch: Patch) => patch.path.at(-1) ?? "";

function isTextUpdate(patches: Patch[]): boolean {
  return (
    patches.some((p) => lastSegment(p) === "editedText" && typeof p.value === "string") &&
    patches.every((p) => TEXT_UPDATE_PATHS.includes(lastSegment(p)))
  );
}

export function snapshotOf<Document, Valg>(state: Snapshot<Document, Valg>): Snapshot<Document, Valg> {
  return {
    redigertBrev: structuredClone(state.redigertBrev),
    redigertBrevHash: state.redigertBrevHash,
    saksbehandlerValg: structuredClone(state.saksbehandlerValg),
  };
}

export function snapshotEntry<Document, Valg>(
  before: Snapshot<Document, Valg>,
  after: Snapshot<Document, Valg>,
): SnapshotHistoryEntry<Document, Valg> {
  return {
    type: "SAKSBEHANDLERVALG_ENDRET",
    before: snapshotOf(before),
    after: snapshotOf(after),
    timestamp: Date.now(),
  };
}

function shouldMerge(last: HistoryEntry | undefined, entry: HistoryEntry): last is PatchHistoryEntry {
  return (
    last?.type === "PATCH" &&
    entry.type === "PATCH" &&
    last.kind === "TEXT_UPDATE" &&
    entry.kind === "TEXT_UPDATE" &&
    last.timestamp !== undefined &&
    entry.timestamp !== undefined &&
    entry.timestamp - last.timestamp < MERGE_TIME_THRESHOLD_MS &&
    last.patches[0]?.path.toString() === entry.patches[0]?.path.toString()
  );
}

export function addHistoryEntry<Document, Valg>(
  history: History<Document, Valg>,
  entry: HistoryEntry<Document, Valg>,
): History<Document, Valg> {
  const entries = history.entries.slice(0, history.entryPointer + 1);
  const last = entries.at(-1);
  const merged =
    entry.type === "PATCH" && shouldMerge(last, entry)
      ? [
          ...entries.slice(0, -1),
          {
            ...entry,
            patches: [...last.patches, ...entry.patches],
            inversePatches: [...entry.inversePatches, ...last.inversePatches],
          },
        ]
      : [...entries, entry];
  return { entries: merged, entryPointer: merged.length - 1 };
}

export function addPatches<Document, Valg>(
  history: History<Document, Valg>,
  patches: Patch[],
  inversePatches: Patch[],
): History<Document, Valg> {
  return addHistoryEntry(history, {
    type: "PATCH",
    patches,
    inversePatches,
    kind: isTextUpdate(patches) ? "TEXT_UPDATE" : undefined,
    timestamp: Date.now(),
  });
}

export const canUndo = (history: History) => history.entryPointer >= 0;
export const canRedo = (history: History) => history.entryPointer < history.entries.length - 1;

function restore<S extends HistoryState<Document, Valg>, Document, Valg>(
  state: S,
  snapshot: Snapshot<Document, Valg>,
): S {
  return { ...state, ...snapshotOf(snapshot) };
}

export function undo<S extends HistoryState<Document, Valg>, Document, Valg>(state: S): S {
  const entry = state.history.entries[state.history.entryPointer];
  if (!entry) return state;
  const previous = entry.type === "PATCH" ? applyPatches(state, entry.inversePatches) : restore(state, entry.before);
  return {
    ...previous,
    saveStatus: "DIRTY",
    history: { ...previous.history, entryPointer: state.history.entryPointer - 1 },
  };
}

export function redo<S extends HistoryState<Document, Valg>, Document, Valg>(state: S): S {
  const pointer = state.history.entryPointer + 1;
  const entry = state.history.entries[pointer];
  if (!entry) return state;
  const next = entry.type === "PATCH" ? applyPatches(state, entry.patches) : restore(state, entry.after);
  return { ...next, saveStatus: "DIRTY", history: { ...next.history, entryPointer: pointer } };
}
