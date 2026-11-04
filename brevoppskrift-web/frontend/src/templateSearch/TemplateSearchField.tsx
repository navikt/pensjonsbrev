import { useDeferredValue, useEffect, useRef, useState } from "react";

import { SearchField } from "~/search/components/SearchField";
import { isSearchQuery } from "~/search/searchQuery";
import { type TemplateHits } from "~/templateSearch/useTemplateHits";

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
  onSearch: (q: string | undefined) => void;
  q: string | undefined;
}) {
  const [query, setQuery] = useState(q);
  const deferredQuery = useDeferredValue(query);
  const settled = deferredQuery.trim();
  const target = settled === "" || isSearchQuery(settled) ? settled : q;

  // The queries this input has written that the URL has not caught up with yet.
  const written = useRef<string[]>([]);
  const latestOnSearch = useRef(onSearch);
  latestOnSearch.current = onSearch;
  const latestQ = useRef(q);
  latestQ.current = q;
  useEffect(() => {
    const latestRequested = written.current.at(-1) ?? latestQ.current;
    if (target !== latestRequested) {
      written.current.push(target);
      latestOnSearch.current(target || undefined);
    }
  }, [target]);
  useEffect(() => {
    const at = written.current.indexOf(q);
    if (at === -1) {
      setQuery(q);
    }
    written.current.splice(0, at + 1);
  }, [q]);

  const searching =
    query !== deferredQuery ||
    target !== q ||
    hits?.status === "loading" ||
    (hits?.status === "ready" && hits.highlight.needle !== q);
  return <SearchField active={searching} label="Søk etter innhold" onChange={setQuery} value={query} />;
}
