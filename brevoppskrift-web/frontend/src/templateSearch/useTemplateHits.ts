import { useMemo } from "react";

import { type ResultPosition, resultPosition } from "~/search/resultNavigation";
import {
  formatResultRef,
  isSameTemplate,
  type ResultRef,
  resultRefOf,
  type SearchCriteria,
} from "~/search/searchContext";
import { useAllTemplateRefs } from "~/search/templateRefs";
import { type ContentHit } from "~/search/textSearch";
import { type DisplayedSearch, type Highlight, useSearchResults } from "~/search/useSearchResults";

/** A result to move to, and the block to open it at. */
export type ResultLinkTarget = { ref: ResultRef; block?: number };

type ReadyTemplateHits = {
  status: "ready";
  /** The query and mode the hits were found with, for marking text. */
  highlight: Highlight;
  /** Whether the displayed hits answer an earlier query than the URL. */
  stale: boolean;
  /** Matching block indexes in this template and language, in document order. */
  matchingBlocks: number[];
  /** All searchable block indexes in this template's document order. */
  blockOrder: number[];
  /** Whether another language version of this template has matching blocks. */
  matchesInOtherLanguage: boolean;
  /** The block the user is at: the requested one, else the first match if
   *  `focusFirstHit`. Not necessarily a match, e.g. when kept from another
   *  language. `undefined` when nothing was requested and either nothing
   *  matched or the first hit is not to be focused. */
  activeBlock: number | undefined;
  /** Where this template is among the active tab's results. */
  result: ResultPosition;
  previousResult: ResultLinkTarget | undefined;
  nextResult: ResultLinkTarget | undefined;
};

export type TemplateHits =
  | { status: "loading" }
  | { status: "failed"; retry?: () => void }
  | (ReadyTemplateHits & {
      /** Whether a failed corpus means these results may be incomplete. */
      incomplete: boolean;
      /** Retries the malType corpus fetches that failed. */
      retry: () => void;
    });

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
function templateHitOf(displayed: DisplayedSearch, current: ResultRef): ContentHit | undefined {
  return displayed.contentHits.find(
    ({ template }) =>
      template.malType === current.malType && template.id === current.id && template.language === current.language,
  );
}

function blockOrderOf(displayed: DisplayedSearch, current: ResultRef): number[] {
  const hit =
    templateHitOf(displayed, current) ??
    displayed.contentHits.find(({ template }) => isSameTemplate(template, current));
  return hit ? [...new Set<number>(hit.template.indexes)] : [];
}

/** Block indexes of every matching line of `current`, unique and in document order. */
function matchingBlocksOf(displayed: DisplayedSearch, current: ResultRef): number[] {
  const hit = templateHitOf(displayed, current);
  if (!hit) {
    return [];
  }
  const matchingIndexes = new Set<number>(hit.matchedLineIndexes.map((line) => hit.template.indexes[line]));
  return [...new Set<number>(hit.template.indexes)].filter((block) => matchingIndexes.has(block));
}

/** What a completed search says about the template being viewed. Pure; see
 *  `useTemplateHits`. `focusFirstHit` makes the first match the active
 *  block when no block was requested. */
export function hitsInTemplate(
  displayed: DisplayedSearch,
  criteria: SearchCriteria,
  current: ResultRef,
  requestedBlock: number | undefined,
  focusFirstHit: boolean,
): ReadyTemplateHits {
  const matchingBlocks = matchingBlocksOf(displayed, current);
  const blockOrder = blockOrderOf(displayed, current);
  const activeBlock = requestedBlock ?? (focusFirstHit ? matchingBlocks[0] : undefined);
  const entries = rankedEntries(displayed, criteria.tab);
  const ranked = entries.map((entry) => entry.ref);
  const result = resultPosition(ranked, current);
  const blockByRef = new Map(entries.map((entry) => [formatResultRef(entry.ref), entry.block]));
  // Another template opens at its best hit in the language it opens in.
  const target = (ref: ResultRef | undefined): ResultLinkTarget | undefined =>
    ref && { ref, block: blockByRef.get(formatResultRef(ref)) };
  return {
    status: "ready",
    highlight: displayed.highlight,
    stale: displayed.highlight.needle !== criteria.q || displayed.highlight.exactOnly !== criteria.exact,
    matchingBlocks,
    blockOrder,
    matchesInOtherLanguage: displayed.contentHits.some(
      ({ template }) => isSameTemplate(template, current) && template.language !== current.language,
    ),
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
  blockOrder: number[] = matchingBlocks,
): { at: number | undefined; previousBlock: number | undefined; nextBlock: number | undefined } {
  if (activeBlock === undefined) {
    return { at: undefined, previousBlock: undefined, nextBlock: matchingBlocks[0] };
  }
  const at = matchingBlocks.indexOf(activeBlock);
  if (at !== -1) {
    return {
      at,
      previousBlock: matchingBlocks[at - 1],
      nextBlock: matchingBlocks[at + 1],
    };
  }
  const activePosition = blockOrder.indexOf(activeBlock);
  if (activePosition !== -1) {
    const positions = new Map(blockOrder.map((block, index) => [block, index]));
    return {
      at: undefined,
      previousBlock: matchingBlocks.findLast((block) => (positions.get(block) ?? -1) < activePosition),
      nextBlock: matchingBlocks.find((block) => (positions.get(block) ?? Number.POSITIVE_INFINITY) > activePosition),
    };
  }
  return {
    at: undefined,
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
  focusFirstHit: boolean,
): TemplateHits | undefined {
  const templates = useAllTemplateRefs(criteria !== undefined);
  const retryMetadata = templates.status === "failed" ? templates.retry : undefined;
  const { isLoading, displayed, searchFailed, failedMalTypes, retryFailed } = useSearchResults(
    criteria && templates.status === "ready" ? templates.refs : undefined,
    criteria?.q ?? "",
    criteria?.exact ?? false,
  );
  return useMemo<TemplateHits | undefined>(() => {
    if (!criteria) {
      return undefined;
    }
    if (failedMalTypes.includes(current.malType)) {
      return { status: "failed", retry: retryFailed };
    }
    if (templates.status === "failed") {
      return { status: "failed", retry: retryMetadata };
    }
    if (searchFailed) {
      return { status: "failed" };
    }
    if (isLoading || !displayed) {
      return { status: "loading" };
    }
    return {
      ...hitsInTemplate(displayed, criteria, current, requestedBlock, focusFirstHit),
      incomplete: failedMalTypes.length > 0 || displayed.incomplete,
      retry: retryFailed,
    };
  }, [
    criteria,
    current,
    requestedBlock,
    focusFirstHit,
    retryMetadata,
    templates.status,
    isLoading,
    displayed,
    searchFailed,
    failedMalTypes,
    retryFailed,
  ]);
}
