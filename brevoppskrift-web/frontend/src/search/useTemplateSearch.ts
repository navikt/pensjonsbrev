import { useQueries } from "@tanstack/react-query";
import { useDeferredValue, useMemo, useState } from "react";

import { getAllTemplateDocumentation, getTemplateDocVersion, type MalType } from "~/api/brevbaker-api-endpoints";
import { type BrevHit, buildIndex, type ContentHit, search, type TemplateText } from "~/search/textSearch";
export const MIN_QUERY_LENGTH = 2;
/** The cheap version endpoint is polled often so we notice template changes
 *  quickly; the large /all payload is only re-fetched and re-indexed when the
 *  content hash changes. */
const VERSION_STALE_TIME_MS = 30 * 1000;
const VERSION_REFETCH_MS = 60 * 1000;
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
export function useTemplateSearch(templates: TemplateRef[]): TemplateSearch {
  const malTypes = useMemo(() => [...new Set(templates.map((t) => t.malType))] as MalType[], [templates]);
  // Poll the cheap content fingerprint often. The hash changes on a new deploy
  // or when an Unleash toggle changes which templates are visible.
  const versionQueries = useQueries({
    queries: malTypes.map((malType) => ({
      queryKey: getTemplateDocVersion.queryKey(malType),
      queryFn: () => getTemplateDocVersion.queryFn(malType),
      staleTime: VERSION_STALE_TIME_MS,
      refetchInterval: VERSION_REFETCH_MS,
    })),
  });
  // The large batch payload is keyed by the content hash, so it is only
  // re-fetched (and the index only rebuilt) when the hash actually changes. We
  // wait for the version query to settle to avoid an extra fetch on first load,
  // but still fetch if the version endpoint is unavailable (degraded mode).
  const queries = useQueries({
    queries: malTypes.map((malType, i) => {
      const version = versionQueries[i].data?.hash;
      return {
        queryKey: [...getAllTemplateDocumentation.queryKey(malType), version ?? "no-version"],
        queryFn: () => getAllTemplateDocumentation.queryFn(malType),
        enabled: !versionQueries[i].isLoading,
        staleTime: Number.POSITIVE_INFINITY,
      };
    }),
  });
  const titleByKey = useMemo(() => new Map(templates.map((t) => [`${t.malType}/${t.brevkode}`, t.title])), [templates]);
  const freshnessKey = queries.map((q) => `${q.status}:${q.dataUpdatedAt}`).join("|");
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
          blockIds: content.lines.map((line) => line.blockId),
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
    isLoading: versionQueries.some((q) => q.isLoading) || queries.some((q) => q.data === undefined && !q.isError),
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
