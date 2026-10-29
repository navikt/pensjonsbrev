import { useQueries } from "@tanstack/react-query";
import { useEffect, useMemo, useState, useTransition } from "react";

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
export type SearchResultsState = {
  /** True until every malType's corpus has either arrived or failed, and while
   *  the templates to search are not known yet. */
  isLoading: boolean;
  /** True from when a search is sent to the worker until its outcome is on
   *  screen: waiting for the worker, and the low-priority render of what it
   *  returned. */
  isSearching: boolean;
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
export function isSearchQuery(trimmed: string): boolean {
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
const NO_MAL_TYPES: MalType[] = [];
/** Resolves the worker's template keys back to templates, preserving order:
 *  content hits arrive already ranked and brev hits carry Fuse's ranking. The
 *  search ran against this very corpus, so every key should resolve; one that
 *  doesn't is dropped rather than rendered as a broken hit. */
function hydrate(hits: HitRefs, byKey: Map<TemplateKey, TemplateText>): SearchResults {
  const content: ContentHit[] = [];
  for (const ref of hits.content) {
    const template = byKey.get(ref.key);
    if (template) {
      content.push({
        template,
        lineIndex: ref.lineIndex,
        matchCount: ref.matchCount,
        matchedLineIndexes: ref.matchedLineIndexes,
        score: ref.score,
      });
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
/** The last corpus built, shared by every page that searches. Moving between
 *  the result list and a template mounts a new search, and handing the worker
 *  an equal but new corpus array would make it reindex everything. */
let lastCorpus: { key: string; corpus: Corpus } | undefined;
function memoizedCorpus(key: string, build: () => Corpus): Corpus {
  if (lastCorpus?.key !== key) {
    lastCorpus = { key, corpus: build() };
  }
  return lastCorpus.corpus;
}
/**
 * Runs `query` against every template in `templates`, in the search worker.
 * `query` is expected to be trimmed and is searched as given: debouncing or
 * deferring typed input is the caller's job (see `useTemplateSearch`).
 * `templates` is `undefined` while the caller is still finding out which
 * templates exist; nothing is searched until it is known.
 */
export function useSearchResults(
  templates: TemplateRef[] | undefined,
  query: string,
  exactOnly: boolean,
  searchClient?: SearchClient,
): SearchResultsState {
  const client = useMemo(() => searchClient ?? sharedSearchClient(), [searchClient]);
  const malTypes = useMemo(
    () => (templates ? ([...new Set(templates.map((t) => t.malType))] as MalType[]) : NO_MAL_TYPES),
    [templates],
  );
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
  const titles = useMemo(() => {
    const byKey = new Map((templates ?? []).map((t) => [`${t.malType}/${t.brevkode}`, t.title]));
    return { byKey, key: [...byKey].map(([key, title]) => `${key}=${title}`).join("\n") };
  }, [templates]);
  // True while the templates aren't known or any malType's corpus hasn't
  // resolved yet (success or error). Reused below both to gate corpus building
  // and in the returned object, so the two can never disagree on what "still
  // loading" means.
  const isLoading = templates === undefined || queries.some((q) => q.data === undefined && !q.isError);
  // Depends on data identity (not fetch timestamps), so an unchanged corpus that
  // revalidated to a 304 keeps the same reference and does not rebuild the index.
  const freshnessKey = queries.map((q) => referenceId(q.data)).join("|");
  // The corpus is flattened whenever the fetched data changes, and `exactOnly`
  // (the toggle) is deliberately NOT in this memo's deps: it only changes the
  // query mode, so it never re-triggers a reindex in the worker.
  // While `isLoading` is true, some malType's corpus hasn't arrived yet, so we
  // skip building entirely rather than repeatedly indexing a partial corpus
  // that no one can search yet (the UI shows "Klargjør malene for søk" instead).
  // biome-ignore lint/correctness/useExhaustiveDependencies: `queries` is a new array every render; `freshnessKey` captures the data we actually depend on.
  const corpus = useMemo<Corpus | undefined>(() => {
    if (isLoading) {
      return undefined;
    }
    return memoizedCorpus(`${freshnessKey}#${titles.key}`, () => {
      const entries: TemplateText[] = [];
      queries.forEach((query, i) => {
        const malType = malTypes[i];
        for (const content of query.data ?? []) {
          entries.push({
            id: content.brevkode,
            malType,
            title: titles.byKey.get(`${malType}/${content.brevkode}`) ?? content.brevkode,
            language: content.language,
            lines: content.lines.map((line) => line.segments),
            indexes: content.lines.map((line) => line.index),
          });
        }
      });
      return { templates: entries, byKey: new Map(entries.map((entry) => [templateKey(entry), entry])) };
    });
  }, [freshnessKey, malTypes, titles, isLoading]);
  const searchable = isSearchQuery(query);
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
    if (!searchable) {
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
    // `isRenderingHits` keeps `isSearching` true until the outcome is on screen.
    client.search(query, exactOnly).then(
      (hits) => {
        // `undefined` means a newer query replaced this one before it ran, so
        // that query's own effect owns both the results and the pending state.
        if (!active || !hits) {
          return;
        }
        const displayed = toDisplayed(hydrate(hits, corpus.byKey), { needle: query, exactOnly });
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
  }, [client, corpus, searchable, query, exactOnly]);
  const failedMalTypes = malTypes.filter((_, i) => queries[i]?.isError);
  const retryFailed = () => {
    for (const q of queries) {
      if (q.isError) {
        void q.refetch();
      }
    }
  };
  return {
    isLoading,
    isSearching: isAwaitingHits || isRenderingHits,
    displayed: shown.displayed,
    searchFailed: shown.searchFailed,
    failedMalTypes,
    retryFailed,
  };
}
