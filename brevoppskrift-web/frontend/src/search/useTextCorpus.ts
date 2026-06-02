import { useQueries } from "@tanstack/react-query";
import { useMemo } from "react";

import { getAllTemplateDocumentation, type MalType } from "~/api/brevbaker-api-endpoints";
import { type TextIndexEntry } from "~/search/textSearch";

/** A template (per language) to include in the search corpus. */
export type IndexableTemplate = {
  id: string;
  malType: "autobrev" | "redigerbar";
  name: string;
  displayTitle: string;
  language: string;
};

export type TextCorpus = {
  /** One entry per template+language, ready to be indexed by the search worker.
   * Stable reference: only changes when the fetched documentation changes. */
  entries: TextIndexEntry[];
  /** True while the documentation is still being fetched. */
  isLoading: boolean;
  /** Number of batch requests that failed. */
  failed: number;
};

const STALE_TIME_MS = 5 * 60 * 1000;

/**
 * Fetches every template's documentation (one batch request per brevtype, cached
 * by TanStack Query) and shapes it into {@link TextIndexEntry}s. The actual search
 * index is built off the main thread from these entries (see the search worker), so
 * this hook only owns fetching and the (cheap) reshaping; TanStack Query owns the
 * caching and staleness.
 *
 * The display name/title for each line come from `templates` (the batch endpoint
 * only returns brevkode + language).
 */
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

  // Stable signature of the fetched data, so the entries (and the downstream index
  // rebuild they trigger) are only recomputed when the documentation actually
  // changes, not on every render. `queries` itself is a fresh array every render.
  const dataSignature = queries.map((q) => `${q.status}:${q.dataUpdatedAt}`).join("|");

  // biome-ignore lint/correctness/useExhaustiveDependencies: keyed on dataSignature (which captures each query's data freshness); `queries` must not be a dependency as it is a fresh array every render.
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
          // Lines are flattened server-side (body only), so occurrence ordinals
          // align 1:1 with the rendered document on the detail page. Name/brevkode
          // are matched separately in searchContent (see meta results).
          lines: item.lines,
        });
      }
    });
    return built;
  }, [dataSignature, malTypes, nameByKey, displayTitleByKey]);

  const isLoading = queries.some((q) => q.isLoading);
  const failed = queries.filter((q) => q.isError).length;

  return { entries, isLoading, failed };
}
