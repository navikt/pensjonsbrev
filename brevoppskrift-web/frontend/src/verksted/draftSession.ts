import { isEqual } from "lodash";
import { useCallback, useEffect, useMemo, useReducer, useRef } from "react";

import { type ApiProblem, toApiProblem, workshopApi } from "./api";
import { createHistory, type History, pushHistory, redo, undo } from "./history";
import { type Draft, type DraftComment, type DraftRecord, type DraftStatus } from "./types";

export type SaveState =
  | { state: "idle" }
  | { state: "saving" }
  | { state: "conflict"; currentRevision?: number }
  | { state: "error"; message: string; status?: number; unavailable: boolean };

export type Session = {
  history: History<Draft>;
  saved: Draft;
  revision: number;
  status: DraftStatus;
  save: SaveState;
};

export type SessionAction =
  | { type: "edit"; next: Draft; key?: string }
  | { type: "undo" }
  | { type: "redo" }
  | { type: "saveStarted" }
  | { type: "saveSucceeded"; record: DraftRecord; sent: Draft }
  | { type: "saveFailed"; problem: ApiProblem }
  /** Serverens versjon tas inn og erstatter lokalt arbeid. Brukes bare etter eksplisitt bekreftelse eller når lokalt er rent. */
  | { type: "adoptRecord"; record: DraftRecord };

export function createSession(record: DraftRecord): Session {
  return {
    history: createHistory(record.draft),
    saved: record.draft,
    revision: record.revision,
    status: record.status,
    save: { state: "idle" },
  };
}

/** Kopierer serverstemplet forfatter/tid for eksisterende kommentarhistorikk inn i lokal tilstand. */
export function mergeServerStamps(local: Draft, server: Draft): Draft {
  const serverComments = new Map((server.comments ?? []).map((c) => [c.id, c]));
  let changed = false;
  const comments = (local.comments ?? []).map((comment): DraftComment => {
    const known = serverComments.get(comment.id);
    if (!known) {
      return comment;
    }
    const knownReplies = new Map(known.replies.map((r) => [r.id, r]));
    const knownStates = new Map(known.states.map((s) => [s.id, s]));
    const merged: DraftComment = {
      ...comment,
      author: known.author,
      at: known.at,
      replies: comment.replies.map((r) => knownReplies.get(r.id) ?? r),
      states: comment.states.map((s) => knownStates.get(s.id) ?? s),
    };
    changed = true;
    return merged;
  });
  const result: Draft = changed ? { ...local, comments } : { ...local };
  if ("baseline" in server) {
    result.baseline = server.baseline;
  } else {
    delete result.baseline;
  }
  return result;
}

export function sessionReducer(session: Session, action: SessionAction): Session {
  switch (action.type) {
    case "edit": {
      return { ...session, history: pushHistory(session.history, action.next, action.key) };
    }
    case "undo": {
      return { ...session, history: undo(session.history) };
    }
    case "redo": {
      return { ...session, history: redo(session.history) };
    }
    case "saveStarted": {
      return { ...session, save: { state: "saving" } };
    }
    case "saveSucceeded": {
      const { record, sent } = action;
      const untouchedSinceSend = Object.is(session.history.present, sent);
      return {
        ...session,
        revision: record.revision,
        status: record.status,
        saved: record.draft,
        save: { state: "idle" },
        // Endringer som ble gjort mens lagringen pågikk blir stående som ulagrede.
        history: untouchedSinceSend
          ? createHistory(record.draft)
          : { ...session.history, present: mergeServerStamps(session.history.present, record.draft) },
      };
    }
    case "saveFailed": {
      const { problem } = action;
      if (problem.isConflict) {
        return { ...session, save: { state: "conflict", currentRevision: problem.currentRevision } };
      }
      return {
        ...session,
        save: { state: "error", message: problem.message, status: problem.status, unavailable: problem.unavailable },
      };
    }
    case "adoptRecord": {
      return createSession(action.record);
    }
  }
}

export function isDirty(session: Session): boolean {
  return !isEqual(session.history.present, session.saved);
}

/**
 * Lokal arbeidsøkt: angre/gjør om, eksplisitt lagring med revisjon og konfliktbevaring.
 * Lagring skjer aldri av seg selv. Ved 409 beholdes lokale endringer uendret.
 */
export function useDraftSession(initial: DraftRecord, draftId: string) {
  const [session, dispatch] = useReducer(sessionReducer, initial, createSession);
  const ref = useRef(session);
  useEffect(() => {
    ref.current = session;
  });

  const draft = session.history.present;
  const dirty = useMemo(() => isDirty(session), [session]);

  const edit = useCallback((updater: (draft: Draft) => Draft, key?: string) => {
    const current = ref.current.history.present;
    const next = updater(current);
    dispatch({ type: "edit", next, key });
    // Hold ref oppdatert så raske etterfølgende endringer bygger på hverandre.
    ref.current = { ...ref.current, history: pushHistory(ref.current.history, next, key) };
  }, []);

  const save = useCallback(async (): Promise<boolean> => {
    const current = ref.current;
    if (current.save.state === "saving") {
      return false;
    }
    const sent = current.history.present;
    dispatch({ type: "saveStarted" });
    try {
      const record = await workshopApi.save(draftId, current.revision, sent);
      dispatch({ type: "saveSucceeded", record, sent });
      return true;
    } catch (error) {
      dispatch({ type: "saveFailed", problem: toApiProblem(error) });
      return false;
    }
  }, [draftId]);

  return {
    session,
    draft,
    dirty,
    edit,
    save,
    undo: useCallback(() => dispatch({ type: "undo" }), []),
    redo: useCallback(() => dispatch({ type: "redo" }), []),
    adoptRecord: useCallback((record: DraftRecord) => dispatch({ type: "adoptRecord", record }), []),
  };
}
