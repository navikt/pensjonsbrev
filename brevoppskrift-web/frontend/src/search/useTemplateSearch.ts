import { useQueries } from "@tanstack/react-query";
import { useDeferredValue, useMemo, useState } from "react";

import { getAllTemplateDocumentation, type MalType } from "~/api/brevbaker-api-endpoints";
import { type BrevHit, buildIndex, type ContentHit, search, type TemplateText } from "~/search/textSearch";
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
  /** Whether the user wants exact matches only (no typo tolerance). Off by default. */
  exactOnly: boolean;
  setExactOnly: (exactOnly: boolean) => void;
  isSearching: boolean;
  isLoading: boolean;
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
export function useTemplateSearch(templates: TemplateRef[]): TemplateSearch {
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
  // Reused below both to gate index building and in the returned object, so
  // the two can never disagree on what "still loading" means.
  const isLoading = queries.some((q) => q.data === undefined && !q.isError);
  // Depends on data identity (not fetch timestamps), so an unchanged corpus that
  // revalidated to a 304 keeps the same reference and does not rebuild the index.
  const freshnessKey = queries.map((q) => referenceId(q.data)).join("|");
  // Both the fuzzy and the exact index are built together whenever the corpus
  // changes, and `exactOnly` (the toggle) is deliberately NOT in this memo's
  // deps: toggling it below only switches which already-built index we read
  // from, so it never re-triggers a (synchronous, main-thread-blocking) Fuse
  // rebuild.
  // While `isLoading` is true, some malType's corpus hasn't arrived yet, so we
  // skip building entirely rather than repeatedly indexing a partial corpus
  // that no one can search yet (the UI shows a loading spinner instead).
  // biome-ignore lint/correctness/useExhaustiveDependencies: `queries` is a new array every render; `freshnessKey` captures the data we actually depend on.
  const indexes = useMemo(() => {
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
    performance.mark("search-index-build-start");
    const built = { fuzzy: buildIndex(entries, true), exact: buildIndex(entries, false) };
    performance.mark("search-index-build-end");
    const measure = performance.measure("search-index-build", "search-index-build-start", "search-index-build-end");
    console.info(
      `[search] built fuzzy+exact indexes for ${entries.length} entries in ${measure.duration.toFixed(1)}ms`,
    );
    // Avoid unbounded growth of the browser's performance entry buffer across
    // repeated corpus reloads (e.g. periodic refetches that change the data).
    performance.clearMarks("search-index-build-start");
    performance.clearMarks("search-index-build-end");
    performance.clearMeasures("search-index-build");
    return built;
  }, [freshnessKey, malTypes, titleByKey, isLoading]);
  const index = indexes ? (exactOnly ? indexes.exact : indexes.fuzzy) : undefined;
  const [query, setQuery] = useState("");
  const deferredQuery = useDeferredValue(query);
  const trimmedQuery = deferredQuery.trim();
  const isSearching = trimmedQuery.length >= MIN_QUERY_LENGTH;
  const results = useMemo(
    () => (index && isSearching ? search(index, trimmedQuery) : { content: [], brev: [] }),
    [index, isSearching, trimmedQuery],
  );
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
    exactOnly,
    setExactOnly,
    isSearching,
    isLoading,
    failedCount: failedMalTypes.length,
    failedMalTypes,
    retryFailed,
    contentHits: results.content,
    brevHits: results.brev,
    contentTemplateCount: templateCount(results.content),
    contentLineCount: results.content.reduce((sum, hit) => sum + hit.matchCount, 0),
    brevTemplateCount: templateCount(results.brev),
    templateTotal: templates.length,
    languageTotal,
  };
}
