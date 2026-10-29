import { useDeferredValue, useState } from "react";

import { type SearchClient } from "~/search/searchClient";
import {
  type DisplayedSearch,
  isSearchQuery,
  type SearchResultsState,
  type TemplateRef,
  useSearchResults,
} from "~/search/useSearchResults";

export type { DisplayedSearch, Highlight, TemplateRef } from "~/search/useSearchResults";
export { MIN_QUERY_LENGTH } from "~/search/useSearchResults";

export type TemplateSearch = Omit<SearchResultsState, "isSearching"> & {
  query: string;
  setQuery: (query: string) => void;
  /** Whether the user wants exact matches only (no typo tolerance). Off by
   *  default. This is the live checkbox state - see `displayed.highlight` for
   *  the mode the hits on screen actually ran in. */
  exactOnly: boolean;
  setExactOnly: (exactOnly: boolean) => void;
  /** True while a search is catching up with the input: the query or match
   *  mode has changed and the hits on screen don't reflect it yet. Never true
   *  for input that neither starts a search nor replaces hits on screen, such
   *  as the first character typed into an empty box. */
  isPending: boolean;
  /** The trimmed query the search is currently running for. Lags `query` while
   *  the user types, so it is the value to persist (e.g. in the URL). */
  settledQuery: string;
  displayed: DisplayedSearch | undefined;
};

export type TemplateSearchOptions = {
  /** Where the input starts, e.g. restored from the URL. Read on mount only. */
  initialQuery?: string;
  initialExactOnly?: boolean;
  searchClient?: SearchClient;
};

/** The search box: live input state on top of `useSearchResults`. */
export function useTemplateSearch(
  templates: TemplateRef[] | undefined,
  { initialQuery = "", initialExactOnly = false, searchClient }: TemplateSearchOptions = {},
): TemplateSearch {
  const [query, setQuery] = useState(initialQuery);
  const [exactOnly, setExactOnly] = useState(initialExactOnly);
  // The input renders `query` at urgent priority so a keypress paints
  // immediately, while everything downstream of the search hangs off the
  // deferred copy. That keeps the *whole* search pipeline off the keystroke's
  // critical path, not just the final commit: because the deferred re-render is
  // interruptible, a burst of fast typing is abandoned and restarted, so
  // intermediate characters never reach the search at all - no worker round
  // trip, no pending-state render and no result render is spent on a query the
  // user has already typed past.
  const deferredQuery = useDeferredValue(query);
  const settledQuery = deferredQuery.trim();
  const { isSearching, ...results } = useSearchResults(templates, settledQuery, exactOnly, searchClient);
  // Pending covers the whole round trip: the deferred render that hasn't caught
  // up with the input yet, waiting for the worker, and the low-priority render
  // of what it returned. All three mean "what you see is older than what you
  // typed". Only counted when a search is involved, though: typing the first
  // character into an empty box also defers a render, but nothing is searched
  // and nothing on screen changes, so it should not flash the search outline.
  const involvesSearch = isSearchQuery(query.trim()) || results.displayed !== undefined;
  const isPending = involvesSearch && (query !== deferredQuery || isSearching);
  return { ...results, query, setQuery, exactOnly, setExactOnly, isPending, settledQuery };
}
