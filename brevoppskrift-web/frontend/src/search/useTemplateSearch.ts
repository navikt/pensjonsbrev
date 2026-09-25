import { useQueries } from "@tanstack/react-query";
import { useDeferredValue, useEffect, useMemo, useState, useTransition } from "react";

import { getAllTemplateDocumentation, type MalType } from "~/api/brevbaker-api-endpoints";
import { type SearchClient, sharedSearchClient } from "~/search/searchClient";
import { type HitRefs, type TemplateKey, templateKey } from "~/search/searchProtocol";
import {
  type BrevHit,
  type ContentHit,
  type SearchResults,
  SPECIAL_CHAR_QUERY,
  type TemplateText,
} from "~/search/textSearch";
export const MIN_QUERY_LENGTH = 2;
/** The batch payload carries an ETag, so periodic refetches revalidate cheaply
 *  (304 Not Modified) and only transfer the corpus when its content changes. */
const DOC_STALE_TIME_MS = 30 * 1000;
const DOC_REFETCH_MS = 60 * 1000;
/** A template the search should cover, with all the languages it supports. */
export type TemplateRef = {
  malType: MalType;
  brevkode: string;
  title: string;
  languages: string[];
};
/** The query and match mode a set of results was produced by. Kept as one value
 *  so the two can never be read from different searches. */
export type Highlight = {
  needle: string;
  exactOnly: boolean;
};
/** A completed search, as it is on screen. Committed as one value, so the hits,
 *  their counts and the query they were matched against can never be rendered
 *  out of step with each other. */
export type DisplayedSearch = {
  /** The query and match mode these hits were produced by. Lags the input
   *  while the user is still typing, and lags the checkbox while a re-search
   *  runs. Highlighting must use this rather than the live `query` and
   *  `exactOnly`: emphasising a needle the hits were never matched against
   *  would mark the wrong words (or none at all). */
  highlight: Highlight;
  contentHits: ContentHit[];
  brevHits: BrevHit[];
  contentTemplateCount: number;
  contentLineCount: number;
  brevTemplateCount: number;
};
export type TemplateSearch = {
  query: string;
  setQuery: (query: string) => void;
  /** Whether the user wants exact matches only (no typo tolerance). Off by
   *  default. This is the live checkbox state - see `displayed.highlight` for
   *  the mode the hits on screen actually ran in. */
  exactOnly: boolean;
  setExactOnly: (exactOnly: boolean) => void;
  /** True until every malType's corpus has either arrived or failed. */
  isLoading: boolean;
  /** True while a search is catching up with the input: the query or match
   *  mode has changed and the hits on screen don't reflect it yet. Never true
   *  for input that neither starts a search nor replaces hits on screen, such
   *  as the first character typed into an empty box. */
  isPending: boolean;
  /** The latest completed search, kept on screen while the next one runs.
   *  `undefined` until a search has completed, when the query is too short to
   *  search, and when the latest search failed - so "nothing searched yet" and
   *  "searched, found nothing" (empty hit lists) are never confused. */
  displayed: DisplayedSearch | undefined;
  /** The latest search failed. Reset by the next successful search, or by
   *  clearing the query. */
  searchFailed: boolean;
  /** malType(s) whose corpus fetch failed; search results may be incomplete. */
  failedMalTypes: MalType[];
  /** Refetches every malType whose corpus fetch is currently failing. */
  retryFailed: () => void;
};
/** Whether a trimmed query is worth sending to the worker. A query made
 *  entirely of special characters (e.g. "§" or "§§") is allowed to search
 *  despite being shorter than MIN_QUERY_LENGTH - see SPECIAL_CHAR_QUERY in
 *  textSearch.ts. */
function isSearchQuery(trimmed: string): boolean {
  return trimmed.length >= MIN_QUERY_LENGTH || SPECIAL_CHAR_QUERY.test(trimmed);
}
function templateCount(hits: { template: TemplateText }[]): number {
  return new Set(hits.map((hit) => `${hit.template.malType}/${hit.template.id}`)).size;
}
/** The corpus as the main thread needs it: the list handed to the worker for
 *  indexing, plus a lookup so the keys the worker sends back can be resolved to
 *  the templates the result components render. */
type Corpus = {
  templates: TemplateText[];
  byKey: Map<TemplateKey, TemplateText>;
};
/** What the result area shows: the latest completed search, or that it failed. */
type Shown = {
  displayed: DisplayedSearch | undefined;
  searchFailed: boolean;
};
const NOTHING_SHOWN: Shown = { displayed: undefined, searchFailed: false };
const SEARCH_FAILED: Shown = { displayed: undefined, searchFailed: true };
/** Resolves the worker's template keys back to templates, preserving order:
 *  content hits arrive already ranked and brev hits carry Fuse's ranking. The
 *  search ran against this very corpus, so every key should resolve; one that
 *  doesn't is dropped rather than rendered as a broken hit. */
function hydrate(hits: HitRefs, byKey: Map<TemplateKey, TemplateText>): SearchResults {
  const content: ContentHit[] = [];
  for (const ref of hits.content) {
    const template = byKey.get(ref.key);
    if (template) {
      content.push({ template, lineIndex: ref.lineIndex, matchCount: ref.matchCount, score: ref.score });
    }
  }
  const brev: BrevHit[] = [];
  for (const ref of hits.brev) {
    const template = byKey.get(ref.key);
    if (template) {
      brev.push({ template });
    }
  }
  return { content, brev };
}
/** Everything the result area needs from one search, counted once when the
 *  search completes rather than on every render. */
function toDisplayed({ content, brev }: SearchResults, highlight: Highlight): DisplayedSearch {
  return {
    highlight,
    contentHits: content,
    brevHits: brev,
    contentTemplateCount: templateCount(content),
    contentLineCount: content.reduce((sum, hit) => sum + hit.matchCount, 0),
    brevTemplateCount: templateCount(brev),
  };
}
/** Stable id per object reference. React Query's structural sharing keeps the
 *  same `data` reference across refetches when the content is unchanged, so this
 *  lets us rebuild the search index only when the corpus actually changes. */
const referenceIds = new WeakMap<object, number>();
let nextReferenceId = 1;
function referenceId(value: object | undefined): string {
  if (!value) {
    return "0";
  }
  const existing = referenceIds.get(value);
  if (existing !== undefined) {
    return String(existing);
  }
  const id = nextReferenceId++;
  referenceIds.set(value, id);
  return String(id);
}
export function useTemplateSearch(templates: TemplateRef[], searchClient?: SearchClient): TemplateSearch {
  const client = useMemo(() => searchClient ?? sharedSearchClient(), [searchClient]);
  const malTypes = useMemo(() => [...new Set(templates.map((t) => t.malType))] as MalType[], [templates]);
  // The corpus is keyed only by malType; periodic refetches revalidate against
  // the server ETag and return a 304 (served from cache) while it is unchanged.
  const queries = useQueries({
    queries: malTypes.map((malType) => ({
      queryKey: getAllTemplateDocumentation.queryKey(malType),
      queryFn: () => getAllTemplateDocumentation.queryFn(malType),
      staleTime: DOC_STALE_TIME_MS,
      refetchInterval: DOC_REFETCH_MS,
    })),
  });
  const titleByKey = useMemo(() => new Map(templates.map((t) => [`${t.malType}/${t.brevkode}`, t.title])), [templates]);
  const [exactOnly, setExactOnly] = useState(false);
  // True while any malType's corpus hasn't resolved yet (success or error).
  // Reused below both to gate corpus building and in the returned object, so
  // the two can never disagree on what "still loading" means.
  const isLoading = queries.some((q) => q.data === undefined && !q.isError);
  // Depends on data identity (not fetch timestamps), so an unchanged corpus that
  // revalidated to a 304 keeps the same reference and does not rebuild the index.
  const freshnessKey = queries.map((q) => referenceId(q.data)).join("|");
  // The corpus is flattened whenever the fetched data changes, and `exactOnly`
  // (the toggle) is deliberately NOT in this memo's deps: it only changes the
  // query mode, so it never re-triggers a reindex in the worker.
  // While `isLoading` is true, some malType's corpus hasn't arrived yet, so we
  // skip building entirely rather than repeatedly indexing a partial corpus
  // that no one can search yet (the UI shows a loading spinner instead).
  // biome-ignore lint/correctness/useExhaustiveDependencies: `queries` is a new array every render; `freshnessKey` captures the data we actually depend on.
  const corpus = useMemo<Corpus | undefined>(() => {
    if (isLoading) {
      return undefined;
    }
    const entries: TemplateText[] = [];
    queries.forEach((query, i) => {
      const malType = malTypes[i];
      for (const content of query.data ?? []) {
        entries.push({
          id: content.brevkode,
          malType,
          title: titleByKey.get(`${malType}/${content.brevkode}`) ?? content.brevkode,
          language: content.language,
          lines: content.lines.map((line) => line.segments),
          indexes: content.lines.map((line) => line.index),
        });
      }
    });
    return { templates: entries, byKey: new Map(entries.map((entry) => [templateKey(entry), entry])) };
  }, [freshnessKey, malTypes, titleByKey, isLoading]);
  const [query, setQuery] = useState("");
  // The input renders `query` at urgent priority so a keypress paints
  // immediately, while everything downstream of the search hangs off the
  // deferred copy. That keeps the *whole* search pipeline off the keystroke's
  // critical path, not just the final commit: because the deferred re-render is
  // interruptible, a burst of fast typing is abandoned and restarted, so
  // intermediate characters never reach the effect below at all - no worker
  // round trip, no pending-state render and no result render is spent on a
  // query the user has already typed past.
  const deferredQuery = useDeferredValue(query);
  const trimmedQuery = deferredQuery.trim();
  const isSearching = isSearchQuery(trimmedQuery);
  // Replaced only when a search completes, so the previous search stays on
  // screen - and stays highlighted for its own query - while the worker
  // answers, rather than the list emptying on every keystroke. That includes a
  // background corpus refresh: the old hits reference the old corpus' objects,
  // so they stay self-consistent until the re-search replaces them.
  const [shown, setShown] = useState<Shown>(NOTHING_SHOWN);
  const [isAwaitingHits, setIsAwaitingHits] = useState(false);
  // Rendering a result list is hundreds of React elements, and at default
  // priority that render blocks the keystroke that caused it - which is the
  // whole jank the worker was supposed to remove. Committing results as a
  // transition keeps them interruptible, so typing always wins over painting
  // results for a query the user has already moved past.
  const [isRenderingHits, startHitsTransition] = useTransition();
  useEffect(() => {
    if (corpus) {
      client.setCorpus(corpus.templates);
    }
  }, [client, corpus]);
  useEffect(() => {
    if (!isSearching) {
      setIsAwaitingHits(false);
      startHitsTransition(() => setShown(NOTHING_SHOWN));
      return;
    }
    if (!corpus) {
      return;
    }
    let active = true;
    setIsAwaitingHits(true);
    // Both outcomes are committed the same way: batched into the transition
    // rather than set urgently. An urgent `false` here would commit a render
    // with the *old* results before the transition renders the new ones, so
    // every search would cost two renders of the result list instead of one.
    // `isRenderingHits` keeps `isPending` true until the outcome is on screen.
    client.search(trimmedQuery, exactOnly).then(
      (hits) => {
        // `undefined` means a newer query replaced this one before it ran, so
        // that query's own effect owns both the results and the pending state.
        if (!active || !hits) {
          return;
        }
        const displayed = toDisplayed(hydrate(hits, corpus.byKey), { needle: trimmedQuery, exactOnly });
        startHitsTransition(() => {
          setIsAwaitingHits(false);
          setShown({ displayed, searchFailed: false });
        });
      },
      () => {
        if (!active) {
          return;
        }
        // Old hits are not left under the error: they answer a different query.
        startHitsTransition(() => {
          setIsAwaitingHits(false);
          setShown(SEARCH_FAILED);
        });
      },
    );
    return () => {
      active = false;
    };
  }, [client, corpus, isSearching, trimmedQuery, exactOnly]);
  // Pending covers the whole round trip: the deferred render that hasn't caught
  // up with the input yet, waiting for the worker, and the low-priority render
  // of what it returned. All three mean "what you see is older than what you
  // typed". Only counted when a search is involved, though: typing the first
  // character into an empty box also defers a render, but nothing is searched
  // and nothing on screen changes, so it should not flash a spinner.
  const involvesSearch = isSearchQuery(query.trim()) || shown.displayed !== undefined;
  const isPending = involvesSearch && (query !== deferredQuery || isAwaitingHits || isRenderingHits);
  const failedMalTypes = malTypes.filter((_, i) => queries[i]?.isError);
  const retryFailed = () => {
    for (const q of queries) {
      if (q.isError) {
        void q.refetch();
      }
    }
  };
  return {
    query,
    setQuery,
    exactOnly,
    setExactOnly,
    isLoading,
    isPending,
    displayed: shown.displayed,
    searchFailed: shown.searchFailed,
    failedMalTypes,
    retryFailed,
  };
}
