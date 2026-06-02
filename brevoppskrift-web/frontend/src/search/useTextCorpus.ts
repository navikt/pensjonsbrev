import { useQueries } from "@tanstack/react-query";
import { useMemo } from "react";

import { getAllTemplateDocumentation, type MalType } from "~/api/brevbaker-api-endpoints";
import { type TextIndexEntry } from "~/search/textSearch";

export type IndexableTemplate = {
  id: string;
  malType: "autobrev" | "redigerbar";
  name: string;
  displayTitle: string;
  language: string;
};

export type TextCorpus = {
  entries: TextIndexEntry[];
  isLoading: boolean;
  failed: number;
};

const STALE_TIME_MS = 5 * 60 * 1000;

export function useTextCorpus(templates: IndexableTemplate[]): TextCorpus {
  const malTypes = useMemo(() => [...new Set(templates.map((t) => t.malType))] as MalType[], [templates]);

  const queries = useQueries({
    queries: malTypes.map((malType) => ({
      queryKey: getAllTemplateDocumentation.queryKey(malType),
      queryFn: () => getAllTemplateDocumentation.queryFn(malType),
      staleTime: STALE_TIME_MS,
    })),
  });

  const nameByKey = useMemo(() => new Map(templates.map((t) => [`${t.malType}/${t.id}`, t.name])), [templates]);
  const displayTitleByKey = useMemo(
    () => new Map(templates.map((t) => [`${t.malType}/${t.id}`, t.displayTitle])),
    [templates],
  );

  const queryFreshnessKey = queries.map((q) => `${q.status}:${q.dataUpdatedAt}`).join("|");

  // biome-ignore lint/correctness/useExhaustiveDependencies: keyed on queryFreshnessKey (which captures each query's data freshness); `queries` must not be a dependency as it is a fresh array every render.
  const entries = useMemo<TextIndexEntry[]>(() => {
    const built: TextIndexEntry[] = [];
    queries.forEach((query, i) => {
      const batch = query.data;
      if (!batch) {
        return;
      }
      const malType = malTypes[i];
      for (const item of batch) {
        const key = `${malType}/${item.brevkode}`;
        built.push({
          id: item.brevkode,
          malType,
          name: nameByKey.get(key) ?? item.brevkode,
          displayTitle: displayTitleByKey.get(key),
          language: item.language,
          lines: item.lines,
        });
      }
    });
    return built;
  }, [queryFreshnessKey, malTypes, nameByKey, displayTitleByKey]);

  const isLoading = queries.some((q) => q.isLoading);
  const failed = queries.filter((q) => q.isError).length;

  return { entries, isLoading, failed };
}
