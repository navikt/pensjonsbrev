import { useQueries } from "@tanstack/react-query";
import { useDeferredValue, useMemo, useState } from "react";

import { getAllTemplateDocumentation, type MalType } from "~/api/brevbaker-api-endpoints";
import { type BrevHit, buildIndex, type ContentHit, search, type TemplateText } from "~/search/textSearch";
export const MIN_QUERY_LENGTH = 2;
const STALE_TIME_MS = 5 * 60 * 1000;
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
  const queries = useQueries({
    queries: malTypes.map((malType) => ({
      queryKey: getAllTemplateDocumentation.queryKey(malType),
      queryFn: () => getAllTemplateDocumentation.queryFn(malType),
      staleTime: STALE_TIME_MS,
    })),
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
    isLoading: queries.some((q) => q.isLoading),
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
