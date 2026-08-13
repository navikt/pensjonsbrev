import { type Dispatch, type SetStateAction, useEffect, useRef, useState } from "react";

import {
  collectAllIds,
  collectNewIds,
  findLastInsertedFocus,
  hasAnyTekstvalgBeenToggledOn,
} from "~/Brevredigering/LetterEditor/InsertedTekstValgHighlight";
import { type LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { type SaksbehandlerValg } from "~/types/brev";
import { type EditedLetter } from "~/types/brevbakerTypes";

const HIGHLIGHT_DURATION_MS = 2200;

type PendingToggle = {
  /** All ids in the letter at the moment the tekstvalg was toggled on. */
  idsBeforeToggle: ReadonlySet<number>;
  /**
   * Ids known to be saved at that same moment. Captured here — rather than read when the save
   * lands — so the result cannot depend on the order effects happen to run in.
   */
  lastSeenIds: ReadonlySet<number>;
};

/**
 * Flash-highlights the content a tekstvalg toggle inserted, and moves the cursor to the end of it.
 *
 * The highlight is derived from the editor state the user is actually looking at, so content is
 * never flashed from a save response that `onSaveSuccess` discarded (which happens when the user
 * types while the save is in flight).
 *
 * `lagretRedigertBrev` is the server-known letter (e.g. `brev.redigertBrev` from the query cache,
 * not `editorState.redigertBrev`) — it only seeds the set of "already saved" ids, so newly
 * typed-but-unsaved content is never mistaken for a tekstvalg insert.
 */
export function useTekstvalgInsertHighlight({
  lagretRedigertBrev,
  editorState,
  setEditorState,
}: {
  lagretRedigertBrev: EditedLetter;
  editorState: LetterEditorState;
  setEditorState: Dispatch<SetStateAction<LetterEditorState>>;
}) {
  const lastSeenIdsRef = useRef<ReadonlySet<number>>(collectAllIds(lagretRedigertBrev));
  const previousValgRef = useRef(editorState.saksbehandlerValg);

  const pendingToggleRef = useRef<PendingToggle | null>(null);
  const highlightTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);
  const [highlightedIds, setHighlightedIds] = useState<ReadonlySet<number>>(() => new Set<number>());

  useEffect(() => {
    lastSeenIdsRef.current = collectAllIds(lagretRedigertBrev);
  }, [lagretRedigertBrev]);

  useEffect(() => {
    previousValgRef.current = editorState.saksbehandlerValg;
  }, [editorState.saksbehandlerValg]);

  useEffect(
    () => () => {
      if (highlightTimerRef.current) {
        clearTimeout(highlightTimerRef.current);
      }
    },
    [],
  );

  const { saveStatus, redigertBrev } = editorState;

  useEffect(() => {
    const pending = pendingToggleRef.current;
    if (!pending) return;

    // The user typed while the save was in flight, so `onSaveSuccess` discarded the response.
    // Drop the pending toggle instead of highlighting later, which would move the cursor
    // out from under someone who is still typing.
    if (saveStatus === "DIRTY") {
      pendingToggleRef.current = null;
      return;
    }
    // Still in flight.
    if (saveStatus !== "SAVED") return;

    const newIds = new Set<number>();
    for (const id of collectNewIds(pending.idsBeforeToggle, redigertBrev)) {
      if (!pending.lastSeenIds.has(id)) newIds.add(id);
    }
    // The save has not landed yet — `saveStatus` is also "SAVED" at rest, so keep waiting rather
    // than dropping the pending toggle.
    if (newIds.size === 0) return;

    pendingToggleRef.current = null;
    setHighlightedIds(newIds);

    const focus = findLastInsertedFocus(redigertBrev, newIds);
    if (focus) {
      setEditorState((state) => ({ ...state, focus }));
    }

    if (highlightTimerRef.current) clearTimeout(highlightTimerRef.current);
    highlightTimerRef.current = setTimeout(() => setHighlightedIds(new Set<number>()), HIGHLIGHT_DURATION_MS);
  }, [saveStatus, redigertBrev, setEditorState]);

  // Call before triggering the autosave for a tekstvalg/overstyring change.
  // Only a toggle ON inserts content — toggle-off and overstyring edits must not arm a highlight,
  // and must clear any previously armed one.
  const beforeTekstvalgChange = (updatedValg: SaksbehandlerValg, currentRedigertBrev: EditedLetter) => {
    pendingToggleRef.current = hasAnyTekstvalgBeenToggledOn(previousValgRef.current, updatedValg)
      ? { idsBeforeToggle: collectAllIds(currentRedigertBrev), lastSeenIds: lastSeenIdsRef.current }
      : null;
    previousValgRef.current = updatedValg;
  };

  return { highlightedIds, beforeTekstvalgChange };
}
