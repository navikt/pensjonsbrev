import { useQueryClient } from "@tanstack/react-query";
import { useEffect, useMemo, useRef, useState } from "react";

import { getAllTemplateDocumentation, type MalType } from "~/api/brevbaker-api-endpoints";
import {
  buildIndexSignature,
  type IndexableTemplate,
  invalidateTextIndexCache,
  loadTextIndexCache,
  saveTextIndexCache,
} from "~/api/textIndexCache";
import { extractDocumentationLines, type TextIndexEntry } from "~/api/textSearch";

export type TextIndexStatus = "idle" | "indexing" | "ready";

export type TextIndexState = {
  entries: TextIndexEntry[];
  status: TextIndexStatus;
  indexed: number;
  total: number;
  failed: number;
};

/**
 * Builds an in-memory full-text index of every template's documentation in the
 * background. The documentation is fetched with a single batch request per
 * brevtype (instead of one request per template per language), which keeps the
 * page load to a handful of requests and avoids the partial-index hang when an
 * individual request stalls. Results stream in per brevtype (so search works on
 * a partial index), are cached in localStorage for 5 minutes, and stale builds
 * are cancelled via a run id when inputs change.
 *
 * Call `reindexNonce` bumping (e.g. from a refresh button) together with
 * `invalidateTextIndexCache()` to force a rebuild.
 */
export function useTextIndex(templates: IndexableTemplate[], reindexNonce: number): TextIndexState {
  const queryClient = useQueryClient();
  const signature = useMemo(() => buildIndexSignature(templates), [templates]);
  const runIdRef = useRef(0);

  const [state, setState] = useState<TextIndexState>({
    entries: [],
    status: "idle",
    indexed: 0,
    total: 0,
    failed: 0,
  });

  // templates is rebuilt each render; capture by signature to avoid effect churn.
  const templatesRef = useRef(templates);
  templatesRef.current = templates;

  // biome-ignore lint/correctness/useExhaustiveDependencies: reindexNonce intentionally forces a rebuild even when the signature is unchanged (e.g. after the refresh button clears the cache).
  useEffect(() => {
    const runId = ++runIdRef.current;
    const isStale = () => runId !== runIdRef.current;
    const items = templatesRef.current;
    const total = items.length;

    const cached = loadTextIndexCache(signature);
    if (cached) {
      setState({ entries: cached, status: "ready", indexed: cached.length, total, failed: 0 });
      return;
    }

    // The batch endpoint returns brevkode + language, but not the display name, so
    // resolve it from the templates derived from the description endpoints.
    const nameByKey = new Map(items.map((t) => [`${t.malType}/${t.id}`, t.name]));
    const displayTitleByKey = new Map(items.map((t) => [`${t.malType}/${t.id}`, t.displayTitle]));
    const malTypes = [...new Set(items.map((t) => t.malType))] as MalType[];

    setState({ entries: [], status: "indexing", indexed: 0, total, failed: 0 });
    const collected: TextIndexEntry[] = [];

    Promise.allSettled(
      malTypes.map(async (malType) => {
        const batch = await queryClient.ensureQueryData({
          queryKey: getAllTemplateDocumentation.queryKey(malType),
          queryFn: () => getAllTemplateDocumentation.queryFn(malType),
        });
        if (isStale()) {
          return;
        }
        const entries: TextIndexEntry[] = batch.map((item) => ({
          id: item.brevkode,
          malType,
          name: nameByKey.get(`${malType}/${item.brevkode}`) ?? item.brevkode,
          displayTitle: displayTitleByKey.get(`${malType}/${item.brevkode}`),
          language: item.language,
          // Body lines only, so occurrence ordinals align 1:1 with the rendered
          // document on the detail page. Name/brevkode are matched separately in
          // searchFuzzy (see meta results).
          lines: extractDocumentationLines(item.documentation),
        }));
        collected.push(...entries);
        setState((s) =>
          isStale() ? s : { ...s, entries: [...s.entries, ...entries], indexed: s.indexed + entries.length },
        );
      }),
    ).then((results) => {
      if (isStale()) {
        return;
      }
      const failed = results.filter((r) => r.status === "rejected").length;
      setState((s) => ({ ...s, status: "ready", failed }));
      // Only cache a complete index, so a transient failure doesn't get pinned for
      // the whole TTL.
      if (failed === 0) {
        saveTextIndexCache(signature, collected);
      }
    });

    return () => {
      // Mark this run stale so its in-flight callbacks can't mutate state or cache.
      runIdRef.current++;
    };
    // reindexNonce forces a rebuild even when the signature is unchanged.
  }, [signature, reindexNonce, queryClient]);

  return state;
}

export { invalidateTextIndexCache };
