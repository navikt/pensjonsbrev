import { useMemo } from "react";

import { type ResultRef, type SearchCriteria } from "~/search/searchContext";
import { type SearchHighlight } from "~/templateSearch/SearchHighlightContext";
import { type TemplateHits, useTemplateHits } from "~/templateSearch/useTemplateHits";

export type HitFocus = {
  hits: TemplateHits | undefined;
  /** What to mark in the document, or `undefined` when nothing is to be marked. */
  highlight: SearchHighlight | undefined;
  /** The block the user is at, to keep when switching language. */
  activeBlock: number | undefined;
  /** The block to scroll to. */
  scrollTarget: number | undefined;
};

/**
 * Which search hits a template shows, and which block is in focus.
 *
 * `marking` is whether the hits are shown ("Vis søketreff"). Showing them
 * focuses the first hit, unless a block was requested. That is derived rather
 * than navigated to, so it also holds when the hits are shown before they have
 * loaded.
 */
export function useHitFocus(
  criteria: SearchCriteria | undefined,
  current: ResultRef,
  requestedBlock: number | undefined,
  marking: boolean,
): HitFocus {
  const hits = useTemplateHits(criteria, current, requestedBlock, marking);
  const ready = hits?.status === "ready" ? hits : undefined;
  const highlight = useMemo<SearchHighlight | undefined>(
    () =>
      marking && ready
        ? {
            needle: ready.highlight.needle,
            exact: ready.highlight.exactOnly,
            matchingBlocks: new Set(ready.matchingBlocks),
            activeBlock: ready.activeBlock,
          }
        : undefined,
    [marking, ready],
  );
  // Until the hits are ready, only a requested block is known.
  const activeBlock = ready?.activeBlock ?? requestedBlock;
  return {
    hits,
    highlight,
    activeBlock,
    // Without a search, a plain `focus` link still scrolls to its block. With
    // one, only while the hits are shown, so showing them scrolls there.
    scrollTarget: criteria && !marking ? undefined : activeBlock,
  };
}
