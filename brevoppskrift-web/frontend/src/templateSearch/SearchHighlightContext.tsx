import { createContext, type ReactNode, useContext } from "react";

import { HighlightText } from "~/search/components/highlight";

/**
 * Search highlighting inside a rendered template.
 *
 * The documentation view stays search-agnostic: it asks this module for the
 * attributes of each content block and renders literal text through it.
 * Without a `SearchHighlightProvider` above it (e.g. in the Word export), both
 * are no-ops and the view renders exactly as without search.
 *
 * Text is marked per literal segment, so an exact phrase that spans a variable
 * or a segment boundary marks its block, but not the words.
 */
export type SearchHighlight = {
  needle: string;
  exact: boolean;
  /** Block indexes (`data-block-index`) that matched the search. */
  matchingBlocks: ReadonlySet<number>;
  /** The block the user is at. Marked as active only if it is one of `matchingBlocks`. */
  activeBlock: number | undefined;
};

const SearchHighlightContext = createContext<SearchHighlight | undefined>(undefined);
/** The block the surrounding content belongs to, set by the block element. */
const BlockIndexContext = createContext<number | undefined>(undefined);

export function SearchHighlightProvider({
  highlight,
  children,
}: {
  highlight: SearchHighlight | undefined;
  children: ReactNode;
}) {
  return <SearchHighlightContext.Provider value={highlight}>{children}</SearchHighlightContext.Provider>;
}

export type SearchHitState = "match" | "active";

export type SearchBlockAttributes = {
  "data-block-index": number;
  "data-search-hit"?: SearchHitState;
};

/** Attributes for a block element: its index, and whether it is a search hit. */
export function useSearchBlockAttributes(index: number): SearchBlockAttributes {
  const highlight = useContext(SearchHighlightContext);
  if (!highlight?.matchingBlocks.has(index)) {
    return { "data-block-index": index };
  }
  return { "data-block-index": index, "data-search-hit": highlight.activeBlock === index ? "active" : "match" };
}

/** Marks everything inside as belonging to block `index`. */
export function SearchBlock({ index, children }: { index: number; children: ReactNode }) {
  return <BlockIndexContext.Provider value={index}>{children}</BlockIndexContext.Provider>;
}

/** Literal template text, with the search needle marked if its block is a hit. */
export function HighlightedLiteral({ text }: { text: string }) {
  const highlight = useContext(SearchHighlightContext);
  const block = useContext(BlockIndexContext);
  if (!highlight || block === undefined || !highlight.matchingBlocks.has(block)) {
    return <>{text}</>;
  }
  return <HighlightText exact={highlight.exact} needle={highlight.needle} text={text} variant="document" />;
}
