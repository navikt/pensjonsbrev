import { useMemo } from "react";

import { type ResultPosition, resultPosition } from "~/search/resultNavigation";
import { formatResultRef, type ResultRef, resultRefOf, type SearchCriteria } from "~/search/searchContext";
import { useAllTemplateRefs } from "~/search/templateRefs";
import { type DisplayedSearch, type Highlight, useSearchResults } from "~/search/useSearchResults";

/** A result to move to, and the block to open it at. */
export type ResultLinkTarget = { ref: ResultRef; block?: number };

export type TemplateHits =
  | { status: "loading" }
  | { status: "failed" }
  | {
      status: "ready";
      /** The query and mode the hits were found with, for marking text. */
      highlight: Highlight;
      /** Matching block indexes in this template and language, in document order. */
      matchingBlocks: number[];
      /** The block the user is at: the requested one, else the first match
       *  when opened from the Innhold tab. Not necessarily a match, e.g. when
       *  kept from another language. `undefined` when nothing was requested
       *  and either nothing matched or the template was opened from the Brev tab. */
      activeBlock: number | undefined;
      /** Where this template is among the active tab's results. */
      result: ResultPosition;
      previousResult: ResultLinkTarget | undefined;
      nextResult: ResultLinkTarget | undefined;
    };

type RankedEntry = { ref: ResultRef; block?: number };

/** The active tab's results, ranked, with the block each content hit opens at. */
function rankedEntries(displayed: DisplayedSearch, tab: SearchCriteria["tab"]): RankedEntry[] {
  if (tab === "brev") {
    return displayed.brevHits.map((hit) => ({ ref: resultRefOf(hit.template) }));
  }
  return displayed.contentHits.map((hit) => ({
    ref: resultRefOf(hit.template),
    block: hit.template.indexes[hit.lineIndex],
  }));
}

/** Block indexes of every matching line of `current`, unique and in document order. */
function matchingBlocksOf(displayed: DisplayedSearch, current: ResultRef): number[] {
  const hit = displayed.contentHits.find(
    ({ template }) =>
      template.malType === current.malType && template.id === current.id && template.language === current.language,
  );
  if (!hit) {
    return [];
  }
  const blocks = new Set(hit.matchedLineIndexes.map((line) => hit.template.indexes[line]));
  return [...blocks].sort((a, b) => a - b);
}

/** What a completed search says about the template being viewed. Pure; see
 *  `useTemplateHits`. */
export function hitsInTemplate(
  displayed: DisplayedSearch,
  tab: SearchCriteria["tab"],
  current: ResultRef,
  requestedBlock: number | undefined,
): TemplateHits {
  const matchingBlocks = matchingBlocksOf(displayed, current);
  // A Brev result is a hit on the template's title, not on any block, so it
  // opens with no block active; the block hits are still there to step through.
  const activeBlock = requestedBlock ?? (tab === "innhold" ? matchingBlocks[0] : undefined);
  const entries = rankedEntries(displayed, tab);
  const ranked = entries.map((entry) => entry.ref);
  const result = resultPosition(ranked, current);
  const blockByRef = new Map(entries.map((entry) => [formatResultRef(entry.ref), entry.block]));
  // Another template opens at its best hit in the language it opens in.
  const target = (ref: ResultRef | undefined): ResultLinkTarget | undefined =>
    ref && { ref, block: blockByRef.get(formatResultRef(ref)) };
  return {
    status: "ready",
    highlight: displayed.highlight,
    matchingBlocks,
    activeBlock,
    result,
    previousResult: target(result?.previous),
    nextResult: target(result?.next),
  };
}

/** Where `activeBlock` is among the matches, and the matches before and after
 *  it. The active block need not be a match itself (e.g. kept from another
 *  language), and then `at` is `undefined`. */
export function blockNavigation(
  matchingBlocks: number[],
  activeBlock: number | undefined,
): { at: number | undefined; previousBlock: number | undefined; nextBlock: number | undefined } {
  if (activeBlock === undefined) {
    return { at: undefined, previousBlock: undefined, nextBlock: matchingBlocks[0] };
  }
  const at = matchingBlocks.indexOf(activeBlock);
  return {
    at: at === -1 ? undefined : at,
    previousBlock: matchingBlocks.findLast((block) => block < activeBlock),
    nextBlock: matchingBlocks.find((block) => block > activeBlock),
  };
}

/**
 * The search hits inside the template being viewed, and its neighbours among
 * the search results. Runs the search from the URL criteria again, which reuses
 * the worker's index when the user comes from the result list.
 *
 * Without criteria (the template was not opened from a search) nothing is
 * fetched or searched and the result is `undefined`.
 */
export function useTemplateHits(
  criteria: SearchCriteria | undefined,
  current: ResultRef,
  requestedBlock: number | undefined,
): TemplateHits | undefined {
  const refs = useAllTemplateRefs(criteria !== undefined);
  const { isLoading, displayed, searchFailed } = useSearchResults(
    criteria ? refs : undefined,
    criteria?.q ?? "",
    criteria?.exact ?? false,
  );
  return useMemo<TemplateHits | undefined>(() => {
    if (!criteria) {
      return undefined;
    }
    if (searchFailed) {
      return { status: "failed" };
    }
    if (isLoading || !displayed) {
      return { status: "loading" };
    }
    return hitsInTemplate(displayed, criteria.tab, current, requestedBlock);
  }, [criteria, current, requestedBlock, isLoading, displayed, searchFailed]);
}
