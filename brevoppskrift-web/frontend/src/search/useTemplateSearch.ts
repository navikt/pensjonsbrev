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
export type TemplateSearch = {
  query: string;
  setQuery: (query: string) => void;
  /** The query the hits on screen were produced from - lags `query` while the
   *  user is still typing. Highlighting must use this, not `query`, so the
   *  emphasis matches the hits being shown and re-highlighting does not run on
   *  the keystroke's critical path. */
  needle: string;
  /** Whether the user wants exact matches only (no typo tolerance). Off by default. */
  exactOnly: boolean;
  setExactOnly: (exactOnly: boolean) => void;
  isSearching: boolean;
  isLoading: boolean;
  /** True while a query is out with the worker and the results shown are still
   *  the previous query's. */
  isPending: boolean;
  failedCount: number;
  /** malType(s) whose corpus fetch failed; search results may be incomplete. */
  failedMalTypes: MalType[];
  /** Refetches every malType whose corpus fetch is currently failing. */
  retryFailed: () => void;
  contentHits: ContentHit[];
  brevHits: BrevHit[];
  contentTemplateCount: number;
  contentLineCount: number;
  brevTemplateCount: number;
  templateTotal: number;
  languageTotal: number;
};
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
const NO_RESULTS: SearchResults = { content: [], brev: [] };
/** Resolves the worker's template keys back to templates, preserving order:
 *  content hits arrive already ranked and brev hits carry Fuse's ranking. A key
 *  with no template belongs to a corpus that has since been replaced, and is
 *  dropped rather than rendered against stale content. */
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
  // A query made entirely of special characters (e.g. "§" or "§§") is
  // allowed to search despite being shorter than MIN_QUERY_LENGTH - see
  // SPECIAL_CHAR_QUERY in textSearch.ts.
  const isSearching = trimmedQuery.length >= MIN_QUERY_LENGTH || SPECIAL_CHAR_QUERY.test(trimmedQuery);
  // The results currently rendered. They are replaced only when new results
  // arrive, so the previous query's hits stay on screen (dimmed) while the
  // worker answers - rather than the list emptying on every keystroke.
  const [results, setResults] = useState<SearchResults>(NO_RESULTS);
  // The corpus the rendered results were produced from, so hits are always
  // resolved against the corpus that produced them.
  const [resultsCorpus, setResultsCorpus] = useState<Corpus | undefined>();
  const [isAwaitingHits, setIsAwaitingHits] = useState(false);
  // Rendering a result list is hundreds of React elements, and at default
  // priority that render blocks the keystroke that caused it - which is the
  // whole jank the worker was supposed to remove. Committing results as a
  // transition keeps them interruptible, so typing always wins over painting
  // results for a query the user has already moved past.
  const [isRenderingHits, startHitsTransition] = useTransition();
  useEffect(() => {
    if (corpus) {
      void client.setCorpus(corpus.templates);
    }
  }, [client, corpus]);
  useEffect(() => {
    if (!corpus || !isSearching) {
      setIsAwaitingHits(false);
      startHitsTransition(() => {
        setResults(NO_RESULTS);
        setResultsCorpus(corpus);
      });
      return;
    }
    let active = true;
    setIsAwaitingHits(true);
    void client.search(trimmedQuery, exactOnly).then((hits) => {
      // `undefined` means a newer query replaced this one before it ran, so
      // that query's own effect owns both the results and the pending state.
      if (!active || !hits) {
        return;
      }
      // Batched into the transition rather than set urgently: an urgent
      // `false` here would commit a render with the *old* results before the
      // transition renders the new ones, so every search cost two renders of
      // the result list instead of one. `isRenderingHits` keeps `isPending`
      // true until the new results are actually on screen.
      startHitsTransition(() => {
        setIsAwaitingHits(false);
        setResults(hydrate(hits, corpus.byKey));
        setResultsCorpus(corpus);
      });
    });
    return () => {
      active = false;
    };
  }, [client, corpus, isSearching, trimmedQuery, exactOnly]);
  // Pending covers the whole round trip: the deferred render that hasn't caught
  // up with the input yet, waiting for the worker, and the low-priority render
  // of what it returned. All three mean "what you see is older than what you
  // typed", which is exactly what the dimming tells the user.
  const isPending = query !== deferredQuery || isAwaitingHits || isRenderingHits;
  // Results hydrated from a corpus that has since been replaced would render
  // stale content, so drop them until the new corpus' results arrive.
  const visibleResults = resultsCorpus === corpus ? results : NO_RESULTS;
  const languageTotal = useMemo(() => new Set(templates.flatMap((t) => t.languages)).size, [templates]);
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
    needle: trimmedQuery,
    exactOnly,
    setExactOnly,
    isSearching,
    isLoading,
    isPending,
    failedCount: failedMalTypes.length,
    failedMalTypes,
    retryFailed,
    contentHits: visibleResults.content,
    brevHits: visibleResults.brev,
    contentTemplateCount: templateCount(visibleResults.content),
    contentLineCount: visibleResults.content.reduce((sum, hit) => sum + hit.matchCount, 0),
    brevTemplateCount: templateCount(visibleResults.brev),
    templateTotal: templates.length,
    languageTotal,
  };
}
