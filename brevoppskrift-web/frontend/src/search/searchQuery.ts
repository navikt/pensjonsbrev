import { SPECIAL_CHAR_QUERY } from "~/search/textSearch";

export const MIN_QUERY_LENGTH = 2;

/** Whether a trimmed query is worth sending to the worker. A query made
 *  entirely of special characters (e.g. "§" or "§§") is allowed to search
 *  despite being shorter than MIN_QUERY_LENGTH - see SPECIAL_CHAR_QUERY in
 *  textSearch.ts. */
export function isSearchQuery(trimmed: string): boolean {
  return trimmed.length >= MIN_QUERY_LENGTH || SPECIAL_CHAR_QUERY.test(trimmed);
}
