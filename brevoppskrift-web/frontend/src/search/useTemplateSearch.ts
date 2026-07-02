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
  isSearching: boolean;
  isLoading: boolean;
  failedCount: number;
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
  // Depends on data identity (not fetch timestamps), so an unchanged corpus that
  // revalidated to a 304 keeps the same reference and does not rebuild the index.
  const freshnessKey = queries.map((q) => referenceId(q.data)).join("|");
  // biome-ignore lint/correctness/useExhaustiveDependencies: `queries` is a new array every render; `freshnessKey` captures the data we actually depend on.
  const index = useMemo(() => {
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
    return buildIndex(entries);
  }, [freshnessKey, malTypes, titleByKey]);
  const [query, setQuery] = useState("");
  const deferredQuery = useDeferredValue(query);
  const trimmedQuery = deferredQuery.trim();
  const isSearching = trimmedQuery.length >= MIN_QUERY_LENGTH;
  const results = useMemo(
    () => (isSearching ? search(index, trimmedQuery) : { content: [], brev: [] }),
    [index, isSearching, trimmedQuery],
  );
  const languageTotal = useMemo(() => new Set(templates.flatMap((t) => t.languages)).size, [templates]);
  return {
    query,
    setQuery,
    isSearching,
    isLoading: queries.some((q) => q.data === undefined && !q.isError),
    failedCount: queries.filter((q) => q.isError).length,
    contentHits: results.content,
    brevHits: results.brev,
    contentTemplateCount: templateCount(results.content),
    contentLineCount: results.content.reduce((sum, hit) => sum + hit.matchCount, 0),
    brevTemplateCount: templateCount(results.brev),
    templateTotal: templates.length,
    languageTotal,
  };
}
