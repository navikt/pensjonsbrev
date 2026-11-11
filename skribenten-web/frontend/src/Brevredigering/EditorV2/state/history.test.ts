import { describe, expect, test } from "vitest";

import { literal, paragraph, stateV2, textBlockAt, texts } from "~test/support/editorV2TestUtils";

import { moveFocus, updateText } from "../commands/updateText";
import { caret } from "../model/paths";
import { type Command, run } from "./editorState";
import {
  addHistoryEntry,
  canRedo,
  canUndo,
  emptyHistory,
  type History,
  type PatchHistoryEntry,
  redo,
  snapshotEntry,
  snapshotOf,
  undo,
} from "./history";

const makeSnapshot = (label: string) => ({
  redigertBrev: { text: label },
  redigertBrevHash: `hash-${label}`,
  saksbehandlerValg: { ytelse: label },
});

const textPatch = (value: string, timestamp = Date.now()): PatchHistoryEntry => ({
  type: "PATCH",
  patches: [{ op: "replace", path: ["redigertBrev", "blocks", 0, "content", 0, "editedText"], value }],
  inversePatches: [{ op: "replace", path: ["redigertBrev", "blocks", 0, "content", 0, "editedText"], value: "" }],
  kind: "TEXT_UPDATE",
  timestamp,
});

const at = (content = 0, offset = 0) => caret({ kind: "block", block: 0 }, content, offset);

const setTitle: Command<[]> = (draft) => {
  draft.redigertBrev.title.text = [literal("Ny tittel")];
};

describe("history", () => {
  test("stores tekstvalg snapshots as one undoable history entry", () => {
    const before = makeSnapshot("før");
    const after = makeSnapshot("etter");

    expect(snapshotEntry(before, after)).toMatchObject({ type: "SAKSBEHANDLERVALG_ENDRET", before, after });
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
    const nextEntry = snapshotEntry(makeSnapshot("før"), makeSnapshot("etter"));

    const result = addHistoryEntry(history, nextEntry);

    expect(result.entries).toEqual([history.entries[0], nextEntry]);
    expect(result.entryPointer).toBe(1);
  });

  test("snapshotOf clones redigertBrev and saksbehandlerValg", () => {
    const state = makeSnapshot("original");
    const snapshot = snapshotOf(state);

    expect(snapshot).toEqual(state);
    snapshot.redigertBrev.text = "mutated";
    snapshot.saksbehandlerValg.ytelse = "mutated";
    expect(state.redigertBrev.text).toBe("original");
    expect(state.saksbehandlerValg.ytelse).toBe("original");
  });

  test("adjacent PATCH TEXT_UPDATE entries within 1 second are merged", () => {
    const history = addHistoryEntry(addHistoryEntry(emptyHistory(), textPatch("A")), textPatch("Ab"));

    expect(history.entries).toHaveLength(1);
    expect(history.entryPointer).toBe(0);
    expect(history.entries[0].type).toBe("PATCH");
  });

  test("TEXT_UPDATE entries more than 1 second apart are not merged", () => {
    const history = addHistoryEntry(addHistoryEntry(emptyHistory(), textPatch("A", 0)), textPatch("Ab", 1500));

    expect(history.entries).toHaveLength(2);
  });

  test("TEKSTVALG entry does not merge with adjacent PATCH entry", () => {
    const history = addHistoryEntry(
      addHistoryEntry(emptyHistory(), textPatch("text")),
      snapshotEntry(makeSnapshot("før"), makeSnapshot("etter")),
    );

    expect(history.entries).toHaveLength(2);
    expect(history.entries[0].type).toBe("PATCH");
    expect(history.entries[1].type).toBe("SAKSBEHANDLERVALG_ENDRET");
  });
});

describe("run", () => {
  test("records patches and marks the state dirty when redigertBrev changes", () => {
    const state = stateV2(paragraph(literal("hei")));

    const result = run(state, updateText, at(0, 4), "heia");

    expect(result.saveStatus).toBe("DIRTY");
    expect(result.history.entries).toHaveLength(1);
    expect(result.history.entryPointer).toBe(0);
    expect(result.focus).toEqual(at(0, 4));
  });

  test("focus-only commands change focus without history or save status", () => {
    const state = stateV2(paragraph(literal("hei")));

    const result = run(state, moveFocus, at(0, 2));

    expect(result.focus).toEqual(at(0, 2));
    expect(result.saveStatus).toBe("SAVED");
    expect(result.history).toBe(state.history);
    expect(result.redigertBrev).toBe(state.redigertBrev);
  });

  test("commands that change nothing return the same state", () => {
    const state = stateV2(paragraph(literal("hei")));

    expect(run(state, updateText, at(5), "ingenting")).toBe(state);
  });

  test("consecutive text updates on the same literal are merged into one entry", () => {
    const state = stateV2(paragraph(literal("hei"), literal("du")));

    const typed = run(run(state, updateText, at(0, 4), "heia"), updateText, at(0, 5), "heias");

    expect(typed.history.entries).toHaveLength(1);
    expect(typed.history.entries[0]).toMatchObject({ type: "PATCH", kind: "TEXT_UPDATE" });
    expect(texts(textBlockAt(undo(typed), 0).content)).toEqual(["hei", "du"]);
  });

  test("text updates on different literals are separate entries", () => {
    const state = stateV2(paragraph(literal("hei"), literal("du")));

    const typed = run(run(state, updateText, at(0, 4), "heia"), updateText, at(1, 3), "dua");

    expect(typed.history.entries).toHaveLength(2);
  });

  test("structural edits are not marked as text updates", () => {
    const result = run(stateV2(paragraph(literal("hei"))), setTitle);

    expect(result.history.entries[0]).toMatchObject({ type: "PATCH", kind: undefined });
  });
});

describe("undo and redo", () => {
  const state = stateV2(paragraph(literal("hei")));
  const edited = run(state, setTitle);

  test("undo restores the document and focus and marks the state dirty", () => {
    const undone = undo(run(edited, updateText, at(0, 4), "heia"));

    expect(texts(textBlockAt(undone, 0).content)).toEqual(["hei"]);
    expect(undone.focus).toEqual(edited.focus);
    expect(undone.saveStatus).toBe("DIRTY");
    expect(canRedo(undone.history)).toBe(true);
  });

  test("redo reapplies the undone entry", () => {
    const undone = undo(edited);
    const redone = redo(undone);

    expect(undone.redigertBrev.title.text).toEqual(state.redigertBrev.title.text);
    expect(redone.redigertBrev.title.text).toEqual(edited.redigertBrev.title.text);
    expect(canUndo(redone.history)).toBe(true);
    expect(canRedo(redone.history)).toBe(false);
  });

  test("undo and redo are no-ops at the ends of history", () => {
    expect(undo(state)).toBe(state);
    expect(redo(edited)).toBe(edited);
    expect(canUndo(state.history)).toBe(false);
  });

  test("a new command after undo drops the redo entries", () => {
    const branched = run(undo(edited), updateText, at(0, 4), "heia");

    expect(branched.history.entries).toHaveLength(1);
    expect(canRedo(branched.history)).toBe(false);
  });

  test("undo and redo restore snapshot entries", () => {
    const after = { ...edited, saksbehandlerValg: { valg: true } };
    const withSnapshot = { ...after, history: addHistoryEntry(edited.history, snapshotEntry(edited, after)) };

    const undone = undo(withSnapshot);
    expect(undone.saksbehandlerValg).toEqual(edited.saksbehandlerValg);
    expect(redo(undone).saksbehandlerValg).toEqual({ valg: true });
  });
});
