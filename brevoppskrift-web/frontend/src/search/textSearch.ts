import Fuse from "fuse.js";

import { type MalType } from "~/api/brevbaker-api-endpoints";
import { type Line, type LineSegment } from "~/api/brevbakerTypes";
import {
  FUZZY_MATCH_OPTIONS,
  isFuzzyTerm,
  normalizeForExactMatch,
  queryTerms,
  SPECIAL_CHAR_QUERY,
} from "~/search/matchingRules";
import { type TextFlow, textFlowHasMatch } from "~/search/matchTextFlow";

export type { Line, LineSegment };

/** One template rendered in a single language, flattened to searchable lines. */
export type TemplateText = {
  id: string; // brevkode
  malType: MalType;
  title: string;
  language: string;
  lines: Line[];
  indexes: number[];
};

/**
 * A content hit: the best-matching line in a template (its neighbours are
 * reachable via `template.lines`) plus every matching line, in document order.
 * `score` follows Fuse.js convention: 0 is a perfect match, 1 is the worst.
 */
export type ContentHit = {
  template: TemplateText;
  lineIndex: number;
  matchCount: number;
  /** Every matching line, ascending. `matchCount` is its length. */
  matchedLineIndexes: number[];
  score: number;
};

/** A metadata hit: the query matched a template's title or brevkode. */
export type BrevHit = {
  template: TemplateText;
};

export type SearchResults = {
  content: ContentHit[];
  brev: BrevHit[];
};

/** Lowercased, whitespace-collapsed text of a line. Variable segments are not
 *  searchable and are skipped. */
export function lineText(line: Line): string {
  return line
    .filter((segment) => segment.type === "text")
    .map((segment) => segment.value)
    .join(" ")
    .toLowerCase()
    .replace(/\s+/g, " ")
    .trim();
}

type ContentRecord = {
  template: TemplateText;
  lineIndex: number;
  text: string;
};
type BrevRecord = {
  template: TemplateText;
  title: string;
  id: string;
};

export {
  FUZZY_MATCH_OPTIONS,
  isFuzzyTerm,
  queryTerms,
  SHORT_TERM_LENGTH,
  SPECIAL_CHAR_QUERY,
} from "~/search/matchingRules";

/** True unless some non-fuzzy-safe term (too short, or containing any
 *  character outside Fuse's own "word" charset - see `isFuzzyTerm` and
 *  `SHORT_TERM_LENGTH`) is missing from every one of `fields` as a
 *  case-insensitive substring. Pure, longer-than-`SHORT_TERM_LENGTH` word
 *  terms are left to Fuse's own (possibly fuzzy) matching. */
export function hasShortTermsVerbatim(terms: string[], fields: string[]): boolean {
  const lowerFields = fields.map((field) => field.toLowerCase());
  return terms.every((term) => isFuzzyTerm(term) || lowerFields.some((field) => field.includes(term.toLowerCase())));
}

/** Multi-word queries are typo-tolerant per term (token search) and must match
 *  every term (`tokenMatch: "all"`), mirroring the previous AND-of-terms
 *  behaviour while adding fuzziness. */
const TOKEN_SEARCH_OPTIONS = {
  ...FUZZY_MATCH_OPTIONS,
  useTokenSearch: true,
  tokenMatch: "all",
  includeScore: true,
  // Lines/titles vary a lot in length; without this, matches in longer text
  // are penalized purely for being longer, which isn't a signal we want here.
  ignoreFieldNorm: true,
} as const;

/** Pre-built Fuse indexes for fuzzy search: one over every searchable line
 *  (content search), one over template title/brevkode (metadata search). The
 *  underlying records are kept alongside them because exact search matches
 *  them directly, without Fuse (see `exactSearch`). */
export type SearchIndex = {
  contentFuse: Fuse<ContentRecord>;
  brevFuse: Fuse<BrevRecord>;
  contentRecords: ContentRecord[];
  brevRecords: BrevRecord[];
};

export function buildIndex(templates: TemplateText[]): SearchIndex {
  const contentRecords: ContentRecord[] = [];
  const brevRecords: BrevRecord[] = [];
  for (const template of templates) {
    template.lines.forEach((line, lineIndex) => {
      const text = lineText(line);
      if (text) {
        contentRecords.push({ template, lineIndex, text });
      }
    });
    brevRecords.push({ template, title: template.title, id: template.id });
  }
  const contentFuse = new Fuse(contentRecords, {
    ...TOKEN_SEARCH_OPTIONS,
    keys: ["text"],
  });
  const brevFuse = new Fuse(brevRecords, {
    ...TOKEN_SEARCH_OPTIONS,
    keys: [
      { name: "title", weight: 2 },
      { name: "id", weight: 1 },
    ],
  });
  return { contentFuse, brevFuse, contentRecords, brevRecords };
}

/** A matched line, with its Fuse score (0 is perfect, 1 is worst). */
type ContentMatch = {
  record: ContentRecord;
  score: number;
};

/** A line as a match flow, keyed by segment index. Segments are space-separated;
 *  variables and table-cell boundaries end exact phrases. */
export function lineFlow(line: Line): TextFlow<number>[] {
  return line.flatMap((segment, source): TextFlow<number>[] => [
    ...(source > 0 ? [{ type: "space" } as const] : []),
    segment.type === "text"
      ? { type: "text", source, text: segment.value }
      : segment.type === "var"
        ? { type: "variable" }
        : { type: "cell" },
  ]);
}

function lineMatches(line: Line, query: string, exact: boolean): boolean {
  return textFlowHasMatch(lineFlow(line), query, exact);
}

/** Collapses the matched lines into one hit per template: the best-scoring
 *  line, plus every line that matched. */
function toContentHits(matches: ContentMatch[]): ContentHit[] {
  const byTemplate = new Map<string, ContentHit>();
  for (const { record, score } of matches) {
    const { template, lineIndex } = record;
    const key = `${template.malType}/${template.id}/${template.language}`;
    const existing = byTemplate.get(key);
    if (existing) {
      existing.matchCount++;
      existing.matchedLineIndexes.push(lineIndex);
      if (score < existing.score) {
        existing.score = score;
        existing.lineIndex = lineIndex;
      }
    } else {
      byTemplate.set(key, { template, lineIndex, matchCount: 1, matchedLineIndexes: [lineIndex], score });
    }
  }
  const hits = [...byTemplate.values()];
  for (const hit of hits) {
    // Fuse returns matches by score, not in document order.
    hit.matchedLineIndexes.sort((a, b) => a - b);
  }
  return hits.sort(
    (a, b) =>
      a.score - b.score || b.matchCount - a.matchCount || a.template.title.localeCompare(b.template.title, "no"),
  );
}

function fuzzySearch(index: SearchIndex, query: string): SearchResults {
  const terms = queryTerms(query);
  const matches: ContentMatch[] = [];
  for (const { item, score } of index.contentFuse.search(query)) {
    if (hasShortTermsVerbatim(terms, [item.text]) && lineMatches(item.template.lines[item.lineIndex], query, false)) {
      matches.push({ record: item, score: score ?? 1 });
    }
  }
  const brev = index.brevFuse
    .search(query)
    .filter(({ item }) => hasShortTermsVerbatim(terms, [item.title, item.id]))
    .map(({ item }) => ({ template: item.template }));

  return { content: toContentHits(matches), brev };
}

/** Every exact hit is a verbatim occurrence, so they are all equally good and
 *  rank by match count (then title) rather than by score. */
const EXACT_MATCH_SCORE = 0;

/** Exact search matches the query verbatim (case-insensitively) as a substring,
 *  deliberately bypassing Fuse. Fuse's exact operators are only reachable via
 *  its extended-search syntax, which would mean interpolating the raw query
 *  into a query string (where `"`, `\` and `|` are syntax, not text), and
 *  enabling that syntax on an index replaces token search - which fuzzy search
 *  needs - for every query on it. Matching here keeps user input literal and
 *  leaves the Fuse indexes purely for fuzzy search. */
function exactSearch(index: SearchIndex, query: string): SearchResults {
  // Line text is lowercased and whitespace-collapsed by `lineText`; normalize
  // the query the same way so it can be compared verbatim.
  const phrase = normalizeForExactMatch(query);
  const matches = index.contentRecords
    .filter(
      (record) => record.text.includes(phrase) && lineMatches(record.template.lines[record.lineIndex], query, true),
    )
    .map((record) => ({ record, score: EXACT_MATCH_SCORE }));
  const brev = index.brevRecords
    .filter(
      (record) =>
        normalizeForExactMatch(record.title).includes(phrase) || normalizeForExactMatch(record.id).includes(phrase),
    )
    .map((record) => ({ template: record.template }));

  return { content: toContentHits(matches), brev };
}

/** Lowercases and collapses whitespace, so a query can be compared verbatim
 *  against text normalized by `lineText`. Shared with `highlight.tsx`, so
 *  exact-mode highlighting marks exactly what exact search matched. */
export { normalizeForExactMatch } from "~/search/matchingRules";

export function search(index: SearchIndex, rawQuery: string, exactOnly = false): SearchResults {
  const query = rawQuery.trim();
  if (!query) {
    return { content: [], brev: [] };
  }

  // A query made entirely of special characters (e.g. "§" or "§§") can't be
  // tokenized by Fuse at all (see `SPECIAL_CHAR_QUERY`), so fuzzy search
  // would find no candidates to even check. Always fall back to verbatim
  // substring matching for it, regardless of `exactOnly`.
  if (SPECIAL_CHAR_QUERY.test(query)) {
    return exactSearch(index, query);
  }

  return exactOnly ? exactSearch(index, query) : fuzzySearch(index, query);
}
