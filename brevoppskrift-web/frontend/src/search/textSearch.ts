import Fuse from "fuse.js";

import { type MalType } from "~/api/brevbaker-api-endpoints";
import { type Line, type LineSegment } from "~/api/brevbakerTypes";

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
 * reachable via `template.lines`) plus the total number of matching lines.
 * `score` follows Fuse.js convention: 0 is a perfect match, 1 is the worst.
 */
export type ContentHit = {
  template: TemplateText;
  lineIndex: number;
  matchCount: number;
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

/** Per-term fuzzy match tuning, shared between the token-search index (below)
 *  and single-term highlighting (`highlight.tsx`, via `Fuse.match()`), so the
 *  two never drift out of sync on what counts as a "fuzzy" match. Tuned
 *  tighter than Fuse's default threshold (0.6) so short brevkoder/terms don't
 *  match unrelated text. `ignoreLocation` mirrors what Fuse's own per-term
 *  token search uses internally.
 *
 *  `minMatchCharLength` must be 1: token search requires *every* term to match
 *  (`tokenMatch: "all"`), so a higher value silently discards single-character
 *  terms and makes the whole query fail - e.g. "paragraf 3" found nothing while
 *  "paragraf 12" worked. Single-character terms are instead required to be
 *  exact substrings (see `SHORT_TERM_LENGTH` / `hasShortTermsVerbatim`), which
 *  keeps them from fuzzily matching almost any text. */
export const FUZZY_MATCH_OPTIONS = {
  threshold: 0.3,
  minMatchCharLength: 1,
  ignoreLocation: true,
} as const;

/** Terms at or below this length are too short to fuzzy-match meaningfully
 *  (a single character is "within one edit" of almost anything), so they must
 *  occur verbatim in the matched text. */
export const SHORT_TERM_LENGTH = 1;

/** Splits a query into its whitespace-separated terms. */
export function queryTerms(query: string): string[] {
  return query.split(/\s+/).filter((term) => term.length > 0);
}

/** True unless some short term (see `SHORT_TERM_LENGTH`) is missing from every
 *  one of `fields` as a case-insensitive substring. Long terms are left to
 *  Fuse's own (possibly fuzzy) matching. */
export function hasShortTermsVerbatim(terms: string[], fields: string[]): boolean {
  const lowerFields = fields.map((field) => field.toLowerCase());
  return terms.every(
    (term) => term.length > SHORT_TERM_LENGTH || lowerFields.some((field) => field.includes(term.toLowerCase())),
  );
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

/** Collapses the matched lines into one hit per template: the best-scoring
 *  line, plus how many lines matched in total. */
function toContentHits(matches: ContentMatch[]): ContentHit[] {
  const byTemplate = new Map<string, ContentHit>();
  for (const { record, score } of matches) {
    const { template, lineIndex } = record;
    const key = `${template.malType}/${template.id}/${template.language}`;
    const existing = byTemplate.get(key);
    if (existing) {
      existing.matchCount++;
      if (score < existing.score) {
        existing.score = score;
        existing.lineIndex = lineIndex;
      }
    } else {
      byTemplate.set(key, { template, lineIndex, matchCount: 1, score });
    }
  }
  return [...byTemplate.values()].sort(
    (a, b) =>
      a.score - b.score || b.matchCount - a.matchCount || a.template.title.localeCompare(b.template.title, "no"),
  );
}

function fuzzySearch(index: SearchIndex, query: string): SearchResults {
  const terms = queryTerms(query);
  const matches: ContentMatch[] = [];
  for (const { item, score } of index.contentFuse.search(query)) {
    if (hasShortTermsVerbatim(terms, [item.text])) {
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
    .filter((record) => record.text.includes(phrase))
    .map((record) => ({ record, score: EXACT_MATCH_SCORE }));
  const brev = index.brevRecords
    .filter(
      (record) =>
        normalizeForExactMatch(record.title).includes(phrase) || normalizeForExactMatch(record.id).includes(phrase),
    )
    .map((record) => ({ template: record.template }));

  return { content: toContentHits(matches), brev };
}

function normalizeForExactMatch(value: string): string {
  return value.toLowerCase().replace(/\s+/g, " ").trim();
}

export function search(index: SearchIndex, rawQuery: string, exactOnly = false): SearchResults {
  const query = rawQuery.trim();
  if (!query) {
    return { content: [], brev: [] };
  }

  return exactOnly ? exactSearch(index, query) : fuzzySearch(index, query);
}
