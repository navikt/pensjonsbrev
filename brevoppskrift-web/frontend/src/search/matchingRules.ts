/** Shared by Fuse candidate ranking and incremental matching. minMatchCharLength
 * must stay 1: tokenMatch "all" otherwise silently discards single-character terms.
 * Those terms are checked verbatim instead. */
export const FUZZY_MATCH_OPTIONS = {
  threshold: 0.3,
  minMatchCharLength: 1,
  ignoreLocation: true,
} as const;

/** A one-character query must not fuzzy-match almost every word. */
export const SHORT_TERM_LENGTH = 1;

/** Fuse cannot tokenize symbol-only queries, so they use verbatim matching. */
export const SPECIAL_CHAR_QUERY = /^[^\p{L}\p{M}\p{N}_]+$/u;

export function queryTerms(query: string): string[] {
  return query.split(/\s+/).filter((term) => term.length > 0);
}

export function normalizeForExactMatch(value: string): string {
  return value.toLowerCase().replace(/\s+/g, " ").trim();
}

export function isFuzzyTerm(term: string): boolean {
  return term.length > SHORT_TERM_LENGTH && /^[\p{L}\p{M}\p{N}_]+$/u.test(term);
}
