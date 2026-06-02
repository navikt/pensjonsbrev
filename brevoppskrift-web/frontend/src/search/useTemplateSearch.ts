import { useQueryClient } from "@tanstack/react-query";
import { useDeferredValue, useEffect, useMemo, useRef, useState } from "react";

import { templateDocumentationKeys } from "~/api/brevbaker-api-endpoints";
import { invalidateTemplateCache } from "~/api/templateCache";
import { buildContentIndex, type ContentIndex, type SnippetResult, searchContent } from "~/search/textSearch";
import { type IndexableTemplate, useTextCorpus } from "~/search/useTextCorpus";

export const MIN_QUERY_LENGTH = 2;

export type TemplateSearchPhase = "idle" | "loading" | "ready" | "error";

export type IndexStatus = "idle" | "indexing" | "ready";

export type IndexProgress = {
  status: IndexStatus;
  indexed: number;
  total: number;
  failed: number;
};

export type TemplateSearch = {
  query: string;
  setQuery: (query: string) => void;
  isSearching: boolean;
  phase: TemplateSearchPhase;
  contentResults: SnippetResult[];
  brevResults: SnippetResult[];
  contentTemplateCount: number;
  brevTemplateCount: number;
  templateTotal: number;
  languageTotal: number;
  indexProgress?: IndexProgress;
  refresh: () => void;
};

export function useTemplateSearch(templates: IndexableTemplate[]): TemplateSearch {
  const queryClient = useQueryClient();
  const { entries, isLoading, failed } = useTextCorpus(templates);

  const [query, setQuery] = useState("");
  const deferredQuery = useDeferredValue(query);
  const trimmedQuery = deferredQuery.trim();
  const isSearching = trimmedQuery.length >= MIN_QUERY_LENGTH;

  const indexRef = useRef<ContentIndex | null>(null);
  const [indexStatus, setIndexStatus] = useState<IndexStatus>("idle");

  useEffect(() => {
    if (entries.length === 0) {
      indexRef.current = null;
      setIndexStatus("idle");
      return;
    }
    setIndexStatus("indexing");
    const handle = requestAnimationFrame(() => {
      indexRef.current = buildContentIndex(entries);
      setIndexStatus("ready");
    });
    return () => cancelAnimationFrame(handle);
  }, [entries]);

  const results = useMemo(() => {
    if (!isSearching || indexStatus !== "ready") return [];
    return searchContent({ index: indexRef.current }, trimmedQuery);
  }, [trimmedQuery, isSearching, indexStatus]);

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
    invalidateTemplateCache();
    queryClient.invalidateQueries({ queryKey: templateDocumentationKeys.all });
  };

  const combinedStatus: IndexStatus = isLoading || indexStatus === "indexing" ? "indexing" : indexStatus;
  const phase: TemplateSearchPhase = combinedStatus === "indexing" ? "loading" : "ready";

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
      status: combinedStatus,
      indexed: entries.length,
      total: templates.length,
      failed,
    },
    refresh,
  };
}
