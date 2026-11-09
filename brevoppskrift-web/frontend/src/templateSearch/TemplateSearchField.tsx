import { useDeferredValue, useEffect, useRef, useState } from "react";

import { SearchField } from "~/search/components/SearchField";
import { isSearchQuery } from "~/search/searchQuery";
import { type TemplateHits } from "~/templateSearch/useTemplateHits";

type PendingQueryWrite = { id: number; query: string };

/**
 * The search box on a template. Like on the result list, the input is live and
 * the URL's `q` follows it once it settles, so the hits in the template update
 * as the user types. A query too short to search keeps the previous search.
 *
 * `q` is the URL's query. When it changes other than by this input (e.g. going
 * back in history), the input follows it.
 */
export function TemplateSearchField({
  hits,
  onSearch,
  q = "",
}: {
  hits: TemplateHits | undefined;
  onSearch: (q: string | undefined) => void | Promise<void>;
  q: string | undefined;
}) {
  const [query, setQuery] = useState(q);
  const deferredQuery = useDeferredValue(query);
  const settled = deferredQuery.trim();
  const target = settled === "" || isSearchQuery(settled) ? settled : q;

  // The queries this input has written that the URL has not caught up with yet.
  const written = useRef<PendingQueryWrite[]>([]);
  const nextWriteId = useRef(0);
  const latestOnSearch = useRef(onSearch);
  latestOnSearch.current = onSearch;
  const latestQ = useRef(q);
  latestQ.current = q;
  useEffect(() => {
    const latestRequested = written.current.at(-1)?.query ?? latestQ.current;
    if (target !== latestRequested) {
      const write = { id: ++nextWriteId.current, query: target };
      written.current.push(write);
      const navigation = latestOnSearch.current(target || undefined);
      if (navigation) {
        void navigation.finally(() => {
          const index = written.current.findIndex(({ id }) => id === write.id);
          if (index === -1) {
            return;
          }
          if (latestQ.current === write.query) {
            let lastMatchingWrite = index;
            for (let next = index + 1; next < written.current.length; next++) {
              if (written.current[next]?.query === write.query) {
                lastMatchingWrite = next;
              }
            }
            written.current.splice(0, lastMatchingWrite + 1);
          } else {
            written.current.splice(index, 1);
          }
        });
      }
    }
  }, [target]);
  useEffect(() => {
    // The router may skip intermediate writes, so a repeated query retires everything up to its last write.
    let at = -1;
    for (let index = written.current.length - 1; index >= 0; index--) {
      if (written.current[index]?.query === q) {
        at = index;
        break;
      }
    }
    if (at === -1) {
      written.current = [];
      setQuery(q);
    } else {
      written.current.splice(0, at + 1);
    }
  }, [q]);

  const searching =
    query !== deferredQuery ||
    target !== q ||
    hits?.status === "loading" ||
    (hits?.status === "ready" && hits.highlight.needle !== q);
  return <SearchField active={searching} label="Søk etter innhold" onChange={setQuery} value={query} />;
}
