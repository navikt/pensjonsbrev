import { useDeferredValue, useEffect, useRef, useState } from "react";

import { SearchField } from "~/search/components/SearchField";
import { isSearchQuery } from "~/search/searchQuery";
import { type TemplateHits } from "~/templateSearch/useTemplateHits";

/**
 * The search box on a template. Like on the result list, the input is live and
 * the URL's `q` follows it once it settles, so the hits in the template update
 * as the user types. A query too short to search keeps the previous search.
 *
 * `q` is the query of the latest navigation, which follows a write right away.
 * When it changes other than by this input (e.g. going back in history), the
 * input follows it.
 */
export function TemplateSearchField({
  hits,
  onSearch,
  q = "",
}: {
  hits: TemplateHits | undefined;
  onSearch: (q: string | undefined) => boolean;
  q: string | undefined;
}) {
  const [query, setQuery] = useState(q);
  const deferredQuery = useDeferredValue(query);
  const settled = deferredQuery.trim();
  const target = settled === "" || isSearchQuery(settled) ? settled : q;

  // The write `q` has not caught up with yet. It runs first, since the input may have moved on
  // to a new target when `q` catches up.
  const written = useRef<string | undefined>(undefined);
  useEffect(() => {
    const ownWrite = q === written.current;
    written.current = undefined;
    if (!ownWrite) {
      setQuery(q);
    }
  }, [q]);
  const latestQ = useRef(q);
  latestQ.current = q;
  const latestOnSearch = useRef(onSearch);
  latestOnSearch.current = onSearch;
  useEffect(() => {
    if (target !== latestQ.current) {
      if (latestOnSearch.current(target || undefined)) {
        written.current = target;
      } else {
        setQuery(latestQ.current ?? "");
      }
    }
  }, [target]);

  const searching =
    query !== deferredQuery ||
    target !== q ||
    hits?.status === "loading" ||
    (hits?.status === "ready" && hits.highlight.needle !== q);
  return <SearchField active={searching} label="Søk etter innhold" onChange={setQuery} value={query} />;
}
