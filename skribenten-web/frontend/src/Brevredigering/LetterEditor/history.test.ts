import { describe, expect, test } from "vitest";

import {
  addHistoryEntry,
  createLetterSnapshot,
  createSaksbehandlerValgEndretHistoryEntry,
  type History,
  snapshotPendingEdit,
} from "~/Brevredigering/LetterEditor/history";
import { type Focus } from "~/Brevredigering/LetterEditor/model/state";
import { type EditedLetter } from "~/types/brevbakerTypes";

const brev = (text: string) => ({ text }) as unknown as EditedLetter;

const makeSnapshot = (label: string) => ({
  redigertBrev: brev(label),
  redigertBrevHash: `hash-${label}`,
  saksbehandlerValg: { ytelse: label },
});
const makeFocusedSnapshot = (label: string, cursorPosition: number) => ({
  ...makeSnapshot(label),
  focus: { blockIndex: 0, contentIndex: 0, cursorPosition } satisfies Focus,
});

describe("history", () => {
  test("stores tekstvalg snapshots as one undoable history entry", () => {
    const before = makeSnapshot("før");
    const after = makeSnapshot("etter");

    const entry = createSaksbehandlerValgEndretHistoryEntry(before, after);

    expect(entry).toMatchObject({
      type: "SAKSBEHANDLERVALG_ENDRET",
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

    const nextEntry = createSaksbehandlerValgEndretHistoryEntry(makeSnapshot("før"), makeSnapshot("etter"));

    const result = addHistoryEntry(history, nextEntry);

    expect(result.entries).toEqual([history.entries[0], nextEntry]);
    expect(result.entryPointer).toBe(1);
  });

  test("createLetterSnapshot clones redigertBrev and saksbehandlerValg", () => {
    const state = makeSnapshot("original");
    const snapshot = createLetterSnapshot(state);

    expect(snapshot).toEqual(state);

    (snapshot.redigertBrev as unknown as { text: string }).text = "mutated";
    (snapshot.saksbehandlerValg as Record<string, string>).ytelse = "mutated";

    expect((state.redigertBrev as unknown as { text: string }).text).toBe("original");
    expect((state.saksbehandlerValg as Record<string, string>).ytelse).toBe("original");
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

  test("merged document snapshots preserve the focus before the first edit and after the last edit", () => {
    const makeEntry = (before: number, after: number, timestamp: number) =>
      snapshotPendingEdit(makeFocusedSnapshot("before", before), makeFocusedSnapshot("after", after), {
        type: "PATCH",
        patches: [{ op: "replace", path: ["blocks", 0, "content", 0, "editedText"], value: "text" }],
        inversePatches: [{ op: "replace", path: ["blocks", 0, "content", 0, "editedText"], value: "" }],
        kind: "TEXT_UPDATE",
        timestamp,
      });

    let history: History = { entries: [], entryPointer: -1 };
    history = addHistoryEntry(history, makeEntry(4, 5, 1000));
    history = addHistoryEntry(history, makeEntry(5, 6, 1500));

    expect(history.entries).toHaveLength(1);
    expect(history.entries[0]).toMatchObject({
      type: "DOCUMENT_SNAPSHOT",
      beforeFocus: { blockIndex: 0, contentIndex: 0, cursorPosition: 4 },
      afterFocus: { blockIndex: 0, contentIndex: 0, cursorPosition: 6 },
    });
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

    const tekstvalgEntry = createSaksbehandlerValgEndretHistoryEntry(makeSnapshot("før"), makeSnapshot("etter"));
    history = addHistoryEntry(history, tekstvalgEntry);

    expect(history.entries).toHaveLength(2);
    expect(history.entries[0].type).toBe("PATCH");
    expect(history.entries[1].type).toBe("SAKSBEHANDLERVALG_ENDRET");
  });
});
