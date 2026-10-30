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
  /** The block the user is at. Outlined as active even when it is not one of
   *  `matchingBlocks`, e.g. kept from a language where it matched, so the same
   *  block is easy to find when switching languages. Its words are not marked. */
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

/** Attributes for a block element: its index, and whether it is the active block or another search hit. */
export function useSearchBlockAttributes(index: number): SearchBlockAttributes {
  const highlight = useContext(SearchHighlightContext);
  if (highlight?.activeBlock === index) {
    return { "data-block-index": index, "data-search-hit": "active" };
  }
  if (highlight?.matchingBlocks.has(index)) {
    return { "data-block-index": index, "data-search-hit": "match" };
  }
  return { "data-block-index": index };
}

/** Marks everything inside as belonging to block `index`. */
export function SearchBlock({ index, children }: { index: number; children: ReactNode }) {
  return <BlockIndexContext.Provider value={index}>{children}</BlockIndexContext.Provider>;
}

/** The search hit block `target` is inside, or `undefined` when it is in no
 *  hit block, or the hits are not shown. */
export function hitBlockOf(target: EventTarget | null): number | undefined {
  const block = target instanceof Element ? target.closest("[data-search-hit]") : null;
  const index = block?.getAttribute("data-block-index");
  return index ? Number(index) : undefined;
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
