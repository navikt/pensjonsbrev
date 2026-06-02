import { useQueryClient } from "@tanstack/react-query";
import { useDeferredValue, useEffect, useMemo, useRef, useState } from "react";

import { templateDocumentationKeys } from "~/api/brevbaker-api-endpoints";
import { type SearchWorkerRequest, type SearchWorkerResponse } from "~/search/searchWorker";
import { type SnippetResult } from "~/search/textSearch";
import { type IndexableTemplate, useTextCorpus } from "~/search/useTextCorpus";

/** Fewest characters before a query runs. */
export const MIN_QUERY_LENGTH = 2;

/** High-level lifecycle of the search, independent of how it is implemented. */
export type TemplateSearchPhase = "idle" | "loading" | "ready" | "error";

/** Progress of building the search index. Specific to the client-side
 * implementation; a backend-backed search can omit this. */
export type IndexStatus = "idle" | "indexing" | "ready";

export type IndexProgress = {
  status: IndexStatus;
  indexed: number;
  total: number;
  failed: number;
};

/**
 * Implementation-agnostic contract the `/templates` page consumes. The current
 * implementation builds a MiniSearch index in a Web Worker and queries it there
 * ({@link useTemplateSearch}); a future server-backed implementation can satisfy the
 * same shape (returning `SnippetResult`s from an API, reporting `phase` instead of
 * `indexProgress`), so the route does not need to change when the search backend is
 * swapped.
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
 * Client-side template search: builds a MiniSearch index of every template's
 * documentation in a Web Worker and runs queries against it there, splitting hits
 * into body ("content") and name/brevkode/title ("brev") results. Owns the query
 * state and exposes the {@link TemplateSearch} contract. The worker keeps both
 * the (heavy) index build and the per-keystroke search off the main thread, so
 * the page stays responsive while loading and while typing.
 */
export function useTemplateSearch(templates: IndexableTemplate[]): TemplateSearch {
  const queryClient = useQueryClient();
  const { entries, isLoading, failed } = useTextCorpus(templates);

  const [query, setQuery] = useState("");
  const deferredQuery = useDeferredValue(query);
  const trimmedQuery = deferredQuery.trim();
  const isSearching = trimmedQuery.length >= MIN_QUERY_LENGTH;

  const [results, setResults] = useState<SnippetResult[]>([]);
  const [indexStatus, setIndexStatus] = useState<IndexStatus>("idle");

  const workerRef = useRef<Worker | null>(null);
  // Generation of the current corpus/index; results from older generations are
  // dropped (see searchWorker). Held in a ref so message handlers always read the
  // latest value without being re-bound.
  const buildIdRef = useRef(0);
  // Latest in-flight search; older responses are dropped.
  const requestIdRef = useRef(0);

  // (Re)create the worker and (re)build the index whenever the corpus changes.
  // Recreating per corpus change (rare — only on (re)fetch) sidesteps any
  // worker-reuse races, including React StrictMode's mount/unmount/remount.
  useEffect(() => {
    if (entries.length === 0) {
      setIndexStatus("idle");
      setResults([]);
      return;
    }

    const worker = new Worker(new URL("./searchWorker.ts", import.meta.url), { type: "module" });
    workerRef.current = worker;
    const buildId = buildIdRef.current + 1;
    buildIdRef.current = buildId;

    setIndexStatus("indexing");
    setResults([]);

    worker.onmessage = (event: MessageEvent<SearchWorkerResponse>) => {
      const message = event.data;
      // Ignore anything from a superseded corpus generation.
      if (message.buildId !== buildIdRef.current) {
        return;
      }
      if (message.type === "ready") {
        setIndexStatus("ready");
      } else if (message.type === "results" && message.requestId === requestIdRef.current) {
        setResults(message.results);
      }
    };

    worker.postMessage({ type: "build", buildId, entries } satisfies SearchWorkerRequest);

    return () => {
      worker.onmessage = null;
      worker.terminate();
      if (workerRef.current === worker) {
        workerRef.current = null;
      }
    };
  }, [entries]);

  // Post the current query to the worker once the index is ready. Re-runs when the
  // index becomes ready (so a query typed during indexing is searched once it can
  // be), and whenever the deferred query changes.
  useEffect(() => {
    const worker = workerRef.current;
    if (!isSearching) {
      setResults([]);
      return;
    }
    if (indexStatus !== "ready" || !worker) {
      return;
    }
    const requestId = requestIdRef.current + 1;
    requestIdRef.current = requestId;
    worker.postMessage({
      type: "search",
      buildId: buildIdRef.current,
      requestId,
      query: trimmedQuery,
    } satisfies SearchWorkerRequest);
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
    queryClient.invalidateQueries({ queryKey: templateDocumentationKeys.all });
  };

  // "indexing" covers both fetching the documentation and building the index.
  const status: IndexStatus = isLoading || indexStatus === "indexing" ? "indexing" : indexStatus;
  const phase: TemplateSearchPhase = status === "indexing" ? "loading" : "ready";

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
      status,
      indexed: entries.length,
      total: templates.length,
      failed,
    },
    refresh,
  };
}
