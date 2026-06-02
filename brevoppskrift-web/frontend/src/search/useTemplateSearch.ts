import { useDeferredValue, useMemo, useState } from "react";

import { type IndexableTemplate } from "~/search/textIndexCache";
import { type SnippetResult, searchFuzzy } from "~/search/textSearch";
import { invalidateTextIndexCache, type TextIndexStatus, useTextIndex } from "~/search/useTextIndex";

/** Fewest characters before a query runs. */
export const MIN_QUERY_LENGTH = 2;

/** High-level lifecycle of the search, independent of how it is implemented. */
export type TemplateSearchPhase = "idle" | "loading" | "ready" | "error";

/** Progress of building the in-browser index. Specific to the client-side
 * implementation; a backend-backed search can omit this. */
export type IndexProgress = {
  status: TextIndexStatus;
  indexed: number;
  total: number;
  failed: number;
};

/**
 * Implementation-agnostic contract the `/templates` page consumes. The current
 * implementation searches an in-browser index ({@link useTemplateSearch}); a future
 * backend-indexed implementation can satisfy the same shape (returning `SnippetResult`s
 * from an API, reporting `phase` instead of `indexProgress`), so the route does not
 * need to change when the search backend is swapped.
 */
export type TemplateSearch = {
  query: string;
  setQuery: (query: string) => void;
  /** True once the query is long enough to search. */
  isSearching: boolean;
  phase: TemplateSearchPhase;
  /** Body (content) hits. */
  contentResults: SnippetResult[];
  /** Name/brevkode/title hits. */
  brevResults: SnippetResult[];
  /** Distinct templates among {@link contentResults}. */
  contentTemplateCount: number;
  /** Distinct templates among {@link brevResults}. */
  brevTemplateCount: number;
  /** Templates in the searchable corpus. */
  templateTotal: number;
  /** Languages in the searchable corpus. */
  languageTotal: number;
  /** Index-build progress (client-side implementation detail; optional). */
  indexProgress?: IndexProgress;
  /** Discard any cached index/results and search again from scratch. */
  refresh: () => void;
};

/**
 * Client-side template search: builds an in-browser full-text index of every
 * template's documentation and runs a fuzzy query against it, splitting hits into
 * body ("content") and name/brevkode/title ("brev") results. Owns the query state and
 * exposes the {@link TemplateSearch} contract.
 */
export function useTemplateSearch(templates: IndexableTemplate[]): TemplateSearch {
  const [query, setQuery] = useState("");
  const [reindexNonce, setReindexNonce] = useState(0);

  const index = useTextIndex(templates, reindexNonce);

  const deferredQuery = useDeferredValue(query);
  const trimmedQuery = deferredQuery.trim();
  const isSearching = trimmedQuery.length >= MIN_QUERY_LENGTH;

  const results = useMemo(
    () => (isSearching ? searchFuzzy({ entries: index.entries }, trimmedQuery) : []),
    [isSearching, index.entries, trimmedQuery],
  );

  const contentResults = useMemo(() => results.filter((result) => !result.meta), [results]);
  const brevResults = useMemo(() => results.filter((result) => result.meta), [results]);

  const contentTemplateCount = useMemo(
    () => new Set(contentResults.map((result) => `${result.malType}/${result.id}`)).size,
    [contentResults],
  );
  const brevTemplateCount = useMemo(
    () => new Set(brevResults.map((result) => `${result.malType}/${result.id}`)).size,
    [brevResults],
  );

  const templateTotal = useMemo(() => new Set(templates.map((t) => `${t.malType}/${t.id}`)).size, [templates]);
  const languageTotal = useMemo(() => new Set(templates.map((t) => t.language)).size, [templates]);

  const refresh = () => {
    invalidateTextIndexCache();
    setReindexNonce((nonce) => nonce + 1);
  };

  const phase: TemplateSearchPhase = index.status === "indexing" ? "loading" : "ready";

  return {
    query,
    setQuery,
    isSearching,
    phase,
    contentResults,
    brevResults,
    contentTemplateCount,
    brevTemplateCount,
    templateTotal,
    languageTotal,
    indexProgress: {
      status: index.status,
      indexed: index.indexed,
      total: index.total,
      failed: index.failed,
    },
    refresh,
  };
}
