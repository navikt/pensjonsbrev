import { describe, expect, test } from "vitest";

import {
  addHistoryEntry,
  createTekstvalgHistoryEntry,
  createTekstvalgSnapshotFromEditorState,
  createTekstvalgSnapshotFromResponse,
  type History,
} from "~/Brevredigering/LetterEditor/history";
import { type EditedLetter } from "~/types/brevbakerTypes";

const brev = (text: string) => ({ text }) as unknown as EditedLetter;

const makeSnapshot = (label: string) => ({
  redigertBrev: brev(label),
  redigertBrevHash: `hash-${label}`,
  saksbehandlerValg: { ytelse: label },
});

describe("history", () => {
  test("stores tekstvalg snapshots as one undoable history entry", () => {
    const before = makeSnapshot("før");
    const after = makeSnapshot("etter");

    const entry = createTekstvalgHistoryEntry(before, after);

    expect(entry).toMatchObject({
      type: "TEKSTVALG",
      before,
      after,
    });
  });

  test("drops redo entries when adding tekstvalg history after undo", () => {
    const history: History = {
      entries: [
        {
          type: "PATCH",
          patches: [{ op: "replace", path: ["saveStatus"], value: "DIRTY" }],
          inversePatches: [{ op: "replace", path: ["saveStatus"], value: "SAVED" }],
        },
        {
          type: "PATCH",
          patches: [{ op: "replace", path: ["saveStatus"], value: "SAVE_PENDING" }],
          inversePatches: [{ op: "replace", path: ["saveStatus"], value: "DIRTY" }],
        },
      ],
      entryPointer: 0,
    };

    const nextEntry = createTekstvalgHistoryEntry(makeSnapshot("før"), makeSnapshot("etter"));

    const result = addHistoryEntry(history, nextEntry);

    expect(result.entries).toEqual([history.entries[0], nextEntry]);
    expect(result.entryPointer).toBe(1);
  });

  test("createTekstvalgSnapshotFromEditorState clones redigertBrev and saksbehandlerValg", () => {
    const state = makeSnapshot("original");
    const snapshot = createTekstvalgSnapshotFromEditorState(state);

    expect(snapshot).toEqual(state);

    (snapshot.redigertBrev as unknown as { text: string }).text = "mutated";
    (snapshot.saksbehandlerValg as Record<string, string>).ytelse = "mutated";

    expect((state.redigertBrev as unknown as { text: string }).text).toBe("original");
    expect((state.saksbehandlerValg as Record<string, string>).ytelse).toBe("original");
  });

  test("createTekstvalgSnapshotFromResponse clones redigertBrev and saksbehandlerValg", () => {
    const response = makeSnapshot("response");
    const snapshot = createTekstvalgSnapshotFromResponse(response);

    expect(snapshot).toEqual(response);

    (snapshot.redigertBrev as unknown as { text: string }).text = "mutated";
    (snapshot.saksbehandlerValg as Record<string, string>).ytelse = "mutated";

    expect((response.redigertBrev as unknown as { text: string }).text).toBe("response");
    expect((response.saksbehandlerValg as Record<string, string>).ytelse).toBe("response");
  });

  test("adjacent PATCH TEXT_UPDATE entries within 1 second are merged", () => {
    const makePatch = (path: (string | number)[], value: string) => ({
      type: "PATCH" as const,
      patches: [{ op: "replace" as const, path, value }],
      inversePatches: [{ op: "replace" as const, path, value: "" }],
      kind: "TEXT_UPDATE" as const,
      timestamp: Date.now(),
    });

    const first = makePatch(["blocks", 0, "content", 0, "editedText"], "A");
    let history: History = { entries: [], entryPointer: -1 };
    history = addHistoryEntry(history, first);

    const second = makePatch(["blocks", 0, "content", 0, "editedText"], "Ab");
    history = addHistoryEntry(history, second);

    expect(history.entries).toHaveLength(1);
    expect(history.entryPointer).toBe(0);
    expect(history.entries[0].type).toBe("PATCH");
  });

  test("TEKSTVALG entry does not merge with adjacent PATCH entry", () => {
    const patchEntry = {
      type: "PATCH" as const,
      patches: [{ op: "replace" as const, path: ["blocks", 0, "content", 0, "editedText"], value: "text" }],
      inversePatches: [{ op: "replace" as const, path: ["blocks", 0, "content", 0, "editedText"], value: "" }],
      kind: "TEXT_UPDATE" as const,
      timestamp: Date.now(),
    };

    let history: History = { entries: [], entryPointer: -1 };
    history = addHistoryEntry(history, patchEntry);

    const tekstvalgEntry = createTekstvalgHistoryEntry(makeSnapshot("før"), makeSnapshot("etter"));
    history = addHistoryEntry(history, tekstvalgEntry);

    expect(history.entries).toHaveLength(2);
    expect(history.entries[0].type).toBe("PATCH");
    expect(history.entries[1].type).toBe("TEKSTVALG");
  });
});
