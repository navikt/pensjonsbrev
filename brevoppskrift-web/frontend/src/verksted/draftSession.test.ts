import { describe, expect, it } from "vitest";

import { ApiProblem } from "./api";
import { newDraft } from "./draftOps";
import { createSession, isDirty, mergeServerStamps, sessionReducer } from "./draftSession";
import { type Draft, type DraftRecord } from "./types";

const draft = (): Draft =>
  newDraft({ name: "Mal", type: "AUTOBREV", targetModule: "UFOERE", languages: ["NB", "NN", "EN"] });
const record = (d: Draft, revision = 1): DraftRecord => ({
  id: "id",
  revision,
  status: "WORK",
  sourceKey: null,
  draft: d,
  createdAt: "",
  updatedAt: "",
  updatedBy: "",
});

describe("sessionReducer", () => {
  it("starts clean and becomes dirty on edit, clean again on undo", () => {
    const initial = draft();
    let s = createSession(record(initial));
    expect(isDirty(s)).toBe(false);
    s = sessionReducer(s, { type: "edit", next: { ...initial, title: { NB: "x" } } });
    expect(isDirty(s)).toBe(true);
    s = sessionReducer(s, { type: "undo" });
    expect(isDirty(s)).toBe(false);
  });

  it("keeps local changes and flags the conflict on 409, without taking the server revision", () => {
    const initial = draft();
    let s = createSession(record(initial, 3));
    const edited = { ...initial, title: { NB: "lokal" } };
    s = sessionReducer(s, { type: "edit", next: edited });
    s = sessionReducer(s, { type: "saveStarted" });
    s = sessionReducer(s, {
      type: "saveFailed",
      problem: new ApiProblem({ message: "konflikt", status: 409, currentRevision: 5 }),
    });
    expect(s.save).toEqual({ state: "conflict", currentRevision: 5 });
    expect(s.history.present).toBe(edited);
    expect(s.revision).toBe(3);
    expect(isDirty(s)).toBe(true);
  });

  it("keeps edits made while a save was in flight as unsaved", () => {
    const initial = draft();
    let s = createSession(record(initial, 1));
    const sent = { ...initial, title: { NB: "sendt" } };
    s = sessionReducer(s, { type: "edit", next: sent });
    s = sessionReducer(s, { type: "saveStarted" });
    const later = { ...sent, title: { NB: "sendt og mer" } };
    s = sessionReducer(s, { type: "edit", next: later });
    s = sessionReducer(s, { type: "saveSucceeded", record: record(sent, 2), sent });
    expect(s.revision).toBe(2);
    expect(s.history.present.title.NB).toBe("sendt og mer");
    expect(isDirty(s)).toBe(true);
  });

  it("becomes clean after a save with no changes in flight and takes server stamps", () => {
    const initial = draft();
    let s = createSession(record(initial, 1));
    const sent: Draft = {
      ...initial,
      comments: [{ id: "c", anchor: { type: "DOCUMENT" }, text: "hei", replies: [], states: [] }],
    };
    s = sessionReducer(s, { type: "edit", next: sent });
    const serverDraft: Draft = { ...sent, comments: [{ ...sent.comments![0], author: "Z123", at: "nå" }] };
    s = sessionReducer(s, { type: "saveSucceeded", record: record(serverDraft, 2), sent });
    expect(isDirty(s)).toBe(false);
    expect(s.history.present.comments?.[0].author).toBe("Z123");
  });

  it("merges stamps into later local edits", () => {
    const local: Draft = {
      ...draft(),
      title: { NB: "senere" },
      comments: [{ id: "c", anchor: { type: "DOCUMENT" }, text: "hei", replies: [], states: [] }],
    };
    const server: Draft = { ...local, title: { NB: "" }, comments: [{ ...local.comments![0], author: "Z1", at: "t" }] };
    const merged = mergeServerStamps(local, server);
    expect(merged.comments?.[0].author).toBe("Z1");
    expect(merged.title.NB).toBe("senere");
  });

  it("adopts a server record only on explicit action", () => {
    const initial = draft();
    let s = createSession(record(initial, 1));
    s = sessionReducer(s, { type: "edit", next: { ...initial, title: { NB: "lokal" } } });
    s = sessionReducer(s, { type: "adoptRecord", record: record({ ...initial, title: { NB: "server" } }, 9) });
    expect(s.revision).toBe(9);
    expect(s.history.present.title.NB).toBe("server");
    expect(isDirty(s)).toBe(false);
  });
});
