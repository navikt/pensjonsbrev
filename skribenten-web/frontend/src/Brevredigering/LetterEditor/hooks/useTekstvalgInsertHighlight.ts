import { type Dispatch, type SetStateAction, useEffect, useRef, useState } from "react";

import {
  collectAllIds,
  collectNewIds,
  findLastInsertedFocus,
  hasAnyTekstvalgBeenToggledOn,
} from "~/Brevredigering/LetterEditor/InsertedTekstValgHighlight";
import { type LetterEditorState } from "~/Brevredigering/LetterEditor/model/state";
import { type BrevResponse, type SaksbehandlerValg } from "~/types/brev";
import { type EditedLetter } from "~/types/brevbakerTypes";

const HIGHLIGHT_DURATION_MS = 2200;

/**
 * Tracks which content ids were inserted by a tekstvalg toggle-on so they can be
 * flash-highlighted and focused once the autosave response comes back.
 *
 * `redigertBrev` should be the server-known letter (e.g. the initially fetched `brev.redigertBrev`,
 * not the in-progress `editorState.redigertBrev`) — it is only used to seed/refresh the set of
 * "already seen" ids, so newly typed-but-unsaved content is never mistaken for a tekstvalg insert.
 */
export function useTekstvalgInsertHighlight({
  redigertBrev,
  saksbehandlerValg,
  setEditorState,
}: {
  redigertBrev: EditedLetter;
  saksbehandlerValg: SaksbehandlerValg | null | undefined;
  setEditorState: Dispatch<SetStateAction<LetterEditorState>>;
}) {
  // Tracks the latest server-known letter and saksbehandlerValg for event handlers
  // and mutation callbacks. These refs do not affect rendering.
  const lastSeenIdsRef = useRef<ReadonlySet<number>>(collectAllIds(redigertBrev));
  const previousValgRef = useRef(saksbehandlerValg);

  const idsBeforeToggleRef = useRef<ReadonlySet<number> | null>(null);
  const highlightTimerRef = useRef<ReturnType<typeof setTimeout> | null>(null);
  const [highlightedIds, setHighlightedIds] = useState<ReadonlySet<number>>(() => new Set<number>());

  useEffect(() => {
    lastSeenIdsRef.current = collectAllIds(redigertBrev);
  }, [redigertBrev]);

  useEffect(() => {
    previousValgRef.current = saksbehandlerValg;
  }, [saksbehandlerValg]);

  useEffect(
    () => () => {
      if (highlightTimerRef.current) {
        clearTimeout(highlightTimerRef.current);
      }
    },
    [],
  );

  // Call before triggering the autosave mutation for a tekstvalg/overstyring change.
  // Records the pre-change ids only if a tekstvalg was toggled ON — not on toggle-off or overstyring edits.
  const beforeTekstvalgChange = (updatedValg: SaksbehandlerValg, currentRedigertBrev: EditedLetter) => {
    if (hasAnyTekstvalgBeenToggledOn(previousValgRef.current, updatedValg)) {
      idsBeforeToggleRef.current = collectAllIds(currentRedigertBrev);
    }
    previousValgRef.current = updatedValg;
  };

  // Call from the autosave mutation's onSuccess, after onSaveSuccess has been applied
  // (or discarded, if the editor went DIRTY while the request was in flight).
  const onAfterSave = (response: BrevResponse, responseWasApplied: boolean) => {
    const idsBeforeToggle = idsBeforeToggleRef.current;
    idsBeforeToggleRef.current = null;

    // The editor went DIRTY while the request was in flight (user typed);
    // onSaveSuccess discarded the response, so do not flash or move the cursor based on a letter the user is not seeing.
    if (!idsBeforeToggle || !responseWasApplied) return;

    const lastSeenIds = lastSeenIdsRef.current;
    const newIds = new Set<number>();
    for (const id of collectNewIds(idsBeforeToggle, response.redigertBrev)) {
      // Ignore ids that already existed in the letter before this save.
      if (!lastSeenIds.has(id)) newIds.add(id);
    }
    if (newIds.size === 0) return;

    setHighlightedIds(newIds);
    const focus = findLastInsertedFocus(response.redigertBrev, newIds);
    if (focus) {
      setEditorState((s) => ({ ...s, focus }));
    }
    if (highlightTimerRef.current) clearTimeout(highlightTimerRef.current);
    highlightTimerRef.current = setTimeout(() => setHighlightedIds(new Set<number>()), HIGHLIGHT_DURATION_MS);
  };

  return { highlightedIds, beforeTekstvalgChange, onAfterSave };
}
