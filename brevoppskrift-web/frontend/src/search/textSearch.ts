import MiniSearch from "minisearch";

import { type MalType } from "~/api/brevbaker-api-endpoints";
import { type Line, type LineSegment } from "~/api/brevbakerTypes";

// The line types are the shape returned by the batch documentation endpoint
// (flattened server-side). Re-exported so search consumers can keep importing
// them from here.
export type { Line, LineSegment };

export type TextIndexEntry = {
  id: string;
  malType: MalType;
  name: string;
  displayTitle?: string;
  language: string;
  lines: Line[];
};

/** Wraps a plain string as a single-text-segment line (used for name/brevkode). */
export function textLine(value: string): Line {
  return [{ kind: "text", value }];
}

/** Counts non-overlapping, case-insensitive occurrences of `needle` in `haystack`.
 * Both arguments are expected to already be lower-cased. */
function countOccurrences(haystack: string, needle: string): number {
  if (!needle) {
    return 0;
  }
  let count = 0;
  let at = haystack.indexOf(needle);
  while (at >= 0) {
    count++;
    at = haystack.indexOf(needle, at + needle.length);
  }
  return count;
}

/** Number of literal occurrences of `needle` (lower-cased) in a line. */
function lineOccurrences(line: Line, needle: string): number {
  let count = 0;
  for (const segment of line) {
    if (segment.kind === "text") {
      count += countOccurrences(segment.value.toLowerCase(), needle);
    }
  }
  return count;
}

/** One contiguous snippet (~5 lines) of a template that contains matches. Results
 * are flattened across templates so the strongest matches sort to the top. */
export type SnippetResult = {
  id: string;
  malType: MalType;
  name: string;
  language: string;
  startLine: number;
  /** Matching lines inside this snippet window. */
  matchCount: number;
  /** Total matching lines in the whole template (for context). */
  templateMatchCount: number;
  lines: Line[];
  /** 0-based occurrence ordinal of `anchorQuery` in the template body that this
   * snippet should scroll to on the detail page. Undefined for `meta` matches
   * (where the query is only in the template name/brevkode, not the body). */
  matchOrdinal?: number;
  /** Relevance score (higher is better); used to rank results. */
  score: number;
  /** Concrete literal substring to scroll to on the detail page. Navigation
   * anchors on a real matched document substring. */
  anchorQuery: string;
  /** Per-segment character ranges to highlight on the primary (best-scoring) line. */
  highlightRanges: HighlightRange[];
  /** Index into `lines` of the line to highlight (the best-scoring match). */
  highlightLineIndex?: number;
  /** True when the match is in the template name or brevkode rather than the body. */
  meta?: boolean;
  /** For `meta` matches: the exact needle to highlight on every shown line. */
  metaNeedle?: string;
};

/** A highlight range within one segment of a line: character offsets `[start,end)`
 * into that segment's `value`. */
export type HighlightRange = { segmentIndex: number; start: number; end: number };

/** Lower-cased, whitespace-collapsed searchable text of a line. Variables are
 * excluded and treated as word separators. */
export function lineSearchText(line: Line): string {
  const parts: string[] = [];
  for (const segment of line) {
    if (segment.kind === "text") {
      const normalized = segment.value.toLowerCase().replace(/\s+/g, " ").trim();
      if (normalized) {
        parts.push(normalized);
      }
    }
  }
  return parts.join(" ");
}

const MAX_SNIPPET_GROUPS = 10;
const SNIPPET_RADIUS = 2;

function mergeWindows(matchIndices: number[], lineCount: number): { start: number; end: number }[] {
  const windows = matchIndices
    .slice(0, MAX_SNIPPET_GROUPS)
    .map((index) => ({
      start: Math.max(0, index - SNIPPET_RADIUS),
      end: Math.min(lineCount - 1, index + SNIPPET_RADIUS),
    }))
    .sort((a, b) => a.start - b.start);

  const merged: { start: number; end: number }[] = [];
  for (const window of windows) {
    const last = merged.at(-1);
    if (last && window.start <= last.end + 1) {
      last.end = Math.max(last.end, window.end);
    } else {
      merged.push({ ...window });
    }
  }
  return merged;
}

/** Computes a proximity multiplier for a line given the matched terms. Finds the
 * shortest span of text containing at least one occurrence of every term. Returns
 * a value in (0, 1] — closer to 1 means terms are tightly packed together.
 * Single-term queries always return 1. */
function proximityBoost(line: Line, terms: string[]): number {
  if (terms.length <= 1) return 1;
  const text = lineSearchText(line);
  if (!text) return 1;

  // Collect all [start, end, termIndex] positions.
  type Pos = { start: number; termIndex: number };
  const positions: Pos[] = [];
  for (let ti = 0; ti < terms.length; ti++) {
    let at = text.indexOf(terms[ti]);
    while (at >= 0) {
      positions.push({ start: at, termIndex: ti });
      at = text.indexOf(terms[ti], at + 1);
    }
  }
  if (positions.length === 0) return 1;
  positions.sort((a, b) => a.start - b.start);

  // Check that all terms were found; if some matched only via fuzzy (not verbatim
  // in the text), we can't measure proximity.
  const foundTerms = new Set(positions.map((p) => p.termIndex));
  if (foundTerms.size < terms.length) return 1;

  // Sliding window: find the shortest span covering all distinct terms.
  const termCount = new Map<number, number>();
  let covered = 0;
  let left = 0;
  let minSpan = text.length;
  for (let right = 0; right < positions.length; right++) {
    const ti = positions[right].termIndex;
    const prev = termCount.get(ti) ?? 0;
    termCount.set(ti, prev + 1);
    if (prev === 0) covered++;

    while (covered === terms.length) {
      const span = positions[right].start + terms[positions[right].termIndex].length - positions[left].start;
      if (span < minSpan) minSpan = span;
      const lti = positions[left].termIndex;
      const lc = termCount.get(lti)! - 1;
      termCount.set(lti, lc);
      if (lc === 0) covered--;
      left++;
    }
  }

  // Ideal span = just the terms with single spaces between them.
  const idealSpan = terms.reduce((sum, t) => sum + t.length, 0) + terms.length - 1;
  return idealSpan / Math.max(minSpan, idealSpan);
}

/** Finds all occurrences of each term in the line's text segments and returns
 * merged, non-overlapping highlight ranges sorted by position. */
function findTermHighlights(line: Line, terms: string[]): HighlightRange[] {
  const ranges: HighlightRange[] = [];
  for (let segmentIndex = 0; segmentIndex < line.length; segmentIndex++) {
    const segment = line[segmentIndex];
    if (segment.kind !== "text") continue;
    const lower = segment.value.toLowerCase();
    for (const term of terms) {
      let pos = lower.indexOf(term);
      while (pos >= 0) {
        ranges.push({ segmentIndex, start: pos, end: pos + term.length });
        pos = lower.indexOf(term, pos + term.length);
      }
    }
  }
  ranges.sort((a, b) => a.segmentIndex - b.segmentIndex || a.start - b.start);
  // Merge overlapping ranges within the same segment.
  const merged: HighlightRange[] = [];
  for (const range of ranges) {
    const last = merged.at(-1);
    if (last && last.segmentIndex === range.segmentIndex && range.start <= last.end) {
      last.end = Math.max(last.end, range.end);
    } else {
      merged.push({ ...range });
    }
  }
  return merged;
}

/** Picks the longest highlight range as the navigation anchor and computes its
 * 0-based occurrence ordinal within the template body (preceding lines + position
 * within the primary line). */
function computeAnchor(
  lines: Line[],
  primaryLine: number,
  ranges: HighlightRange[],
): { anchorQuery: string; matchOrdinal: number } | null {
  if (ranges.length === 0) return null;
  // Pick the longest range as anchor.
  let best = ranges[0];
  for (const range of ranges) {
    if (range.end - range.start > best.end - best.start) {
      best = range;
    }
  }
  const segment = lines[primaryLine][best.segmentIndex];
  if (segment.kind !== "text") return null;
  const anchorQuery = segment.value.slice(best.start, best.end);
  const needle = anchorQuery.toLowerCase();
  // Count occurrences in all preceding lines.
  let ordinal = 0;
  for (let i = 0; i < primaryLine; i++) {
    ordinal += lineOccurrences(lines[i], needle);
  }
  // Count occurrences in earlier segments of the primary line.
  const primarySegments = lines[primaryLine];
  for (let s = 0; s < best.segmentIndex; s++) {
    const seg = primarySegments[s];
    if (seg.kind === "text") {
      ordinal += countOccurrences(seg.value.toLowerCase(), needle);
    }
  }
  // Count occurrences before the anchor position within the same segment.
  ordinal += countOccurrences(segment.value.toLowerCase().slice(0, best.start), needle);
  return { anchorQuery, matchOrdinal: ordinal };
}

/** A MiniSearch-backed content index over the documentation lines. Each indexed
 * document is one line (its searchable text); MiniSearch's inverted index provides
 * fast term-based retrieval with BM25-like scoring. */
export type ContentIndex = {
  miniSearch: MiniSearch;
  entries: TextIndexEntry[];
  /** docId -> the entry and line it came from. Indexed by the integer doc id. */
  lineRefs: { entryIndex: number; lineIndex: number }[];
};

/** Builds the {@link ContentIndex}: flattens every entry's lines into MiniSearch
 * documents (skipping lines with no searchable text), keeping a back-reference
 * from each doc id to its `{entryIndex, lineIndex}`. */
export function buildContentIndex(entries: TextIndexEntry[]): ContentIndex {
  const miniSearch = new MiniSearch<{ id: number; text: string }>({
    fields: ["text"],
    storeFields: [],
    searchOptions: {
      prefix: true,
      fuzzy: 0.2,
      combineWith: "AND",
    },
  });
  const docs: { id: number; text: string }[] = [];
  const lineRefs: { entryIndex: number; lineIndex: number }[] = [];
  for (let entryIndex = 0; entryIndex < entries.length; entryIndex++) {
    const entry = entries[entryIndex];
    for (let lineIndex = 0; lineIndex < entry.lines.length; lineIndex++) {
      const text = lineSearchText(entry.lines[lineIndex]);
      if (!text) {
        continue;
      }
      docs.push({ id: lineRefs.length, text });
      lineRefs.push({ entryIndex, lineIndex });
    }
  }
  miniSearch.addAll(docs);
  return { miniSearch, entries, lineRefs };
}

/** Matched lines grouped per entry with MiniSearch relevance scores (boosted by
 * proximity) and matched document terms. */
function matchedLinesByEntry(
  index: ContentIndex,
  query: string,
): Map<number, Map<number, { score: number; terms: string[] }>> {
  const byEntry = new Map<number, Map<number, { score: number; terms: string[] }>>();
  for (const hit of index.miniSearch.search(query)) {
    const ref = index.lineRefs[hit.id as number];
    if (!ref) {
      continue;
    }
    let lines = byEntry.get(ref.entryIndex);
    if (!lines) {
      lines = new Map();
      byEntry.set(ref.entryIndex, lines);
    }
    const entry = index.entries[ref.entryIndex];
    const boost = proximityBoost(entry.lines[ref.lineIndex], hit.terms);
    lines.set(ref.lineIndex, { score: hit.score * (1 + boost), terms: hit.terms });
  }
  return byEntry;
}

/** Context needed to run a search: the MiniSearch content index. */
export type SearchContext = { index: ContentIndex | null };

/** Content search powered by MiniSearch with fuzzy matching. Retrieves matching
 * lines via MiniSearch's inverted index (prefix + fuzzy + AND combination), then
 * highlights matched terms in the original segments. Returns a flat list of
 * snippet windows ranked by MiniSearch relevance. */
export function searchContent(context: SearchContext, rawQuery: string): SnippetResult[] {
  const query = rawQuery.toLowerCase().replace(/\s+/g, " ").trim();
  const index = context.index;
  if (!query || !index) {
    return [];
  }
  const { entries } = index;

  const results: SnippetResult[] = [];

  for (const [entryIndex, lineScores] of matchedLinesByEntry(index, query)) {
    const entry = entries[entryIndex];
    const lineMatches = new Map<number, { score: number; terms: string[] }>();
    for (const [lineIndex, hit] of lineScores) {
      lineMatches.set(lineIndex, hit);
    }
    if (lineMatches.size === 0) {
      continue;
    }

    // Seed snippet windows from the highest-scoring matched lines.
    const seeds = [...lineMatches.keys()].sort(
      (a, b) => (lineMatches.get(b)?.score ?? 0) - (lineMatches.get(a)?.score ?? 0) || a - b,
    );

    for (const window of mergeWindows(seeds, entry.lines.length)) {
      let matchCount = 0;
      let windowScore = 0;
      let primaryLine = window.start;
      let bestScore = 0;
      for (let i = window.start; i <= window.end; i++) {
        const match = lineMatches.get(i);
        if (match) {
          matchCount++;
          windowScore += match.score;
          if (match.score > bestScore) {
            bestScore = match.score;
            primaryLine = i;
          }
        }
      }
      const primaryHit = lineMatches.get(primaryLine);
      const highlightTerms = primaryHit?.terms ?? [];
      const ranges = findTermHighlights(entry.lines[primaryLine], highlightTerms);
      const anchor = computeAnchor(entry.lines, primaryLine, ranges);

      results.push({
        id: entry.id,
        malType: entry.malType,
        name: entry.name,
        language: entry.language,
        startLine: window.start,
        matchCount,
        templateMatchCount: lineMatches.size,
        lines: entry.lines.slice(window.start, window.end + 1),
        matchOrdinal: anchor?.matchOrdinal,
        score: windowScore,
        anchorQuery: anchor?.anchorQuery ?? query,
        highlightRanges: ranges,
        highlightLineIndex: primaryLine - window.start,
      });
    }
  }

  // Name/brevkode/displayTitle matches power the "Brev" tab: a letter is listed
  // whenever its identity (not just its body) matches, independent of any content
  // hit, so e.g. searching the brevkode "P1" still surfaces its letter here even
  // though its body also mentions "P1".
  for (let entryIndex = 0; entryIndex < entries.length; entryIndex++) {
    const entry = entries[entryIndex];
    const metaLines: Line[] = [];
    if (entry.name.toLowerCase().includes(query)) {
      metaLines.push(textLine(entry.name));
    }
    if (entry.id.toLowerCase().includes(query)) {
      metaLines.push(textLine(entry.id));
    }
    if (entry.displayTitle?.toLowerCase().includes(query)) {
      metaLines.push(textLine(entry.displayTitle));
    }
    if (metaLines.length > 0) {
      results.push({
        id: entry.id,
        malType: entry.malType,
        name: entry.name,
        language: entry.language,
        startLine: 0,
        matchCount: 0,
        templateMatchCount: 0,
        lines: metaLines,
        score: 0,
        anchorQuery: query,
        highlightRanges: [],
        meta: true,
        metaNeedle: query,
      });
    }
  }

  results.sort(
    (a, b) =>
      b.score - a.score ||
      b.templateMatchCount - a.templateMatchCount ||
      a.name.localeCompare(b.name, "no") ||
      a.startLine - b.startLine,
  );
  return results;
}

/** All occurrences of `needle` (case-insensitive) within `value`, as character
 * ranges. Used to highlight name/brevkode meta matches. */
export function exactRanges(value: string, needle: string): { start: number; end: number }[] {
  const ranges: { start: number; end: number }[] = [];
  if (!needle) {
    return ranges;
  }
  const haystack = value.toLowerCase();
  let at = haystack.indexOf(needle);
  while (at >= 0) {
    ranges.push({ start: at, end: at + needle.length });
    at = haystack.indexOf(needle, at + needle.length);
  }
  return ranges;
}

/** Splits a segment's `value` into plain/highlighted parts around the given
 * (non-overlapping, ascending) character ranges, for rendering highlights. */
export function splitSegmentByRanges(
  value: string,
  ranges: { start: number; end: number }[],
): { text: string; match: boolean }[] {
  const sorted = [...ranges].sort((a, b) => a.start - b.start);
  const parts: { text: string; match: boolean }[] = [];
  let last = 0;
  for (const range of sorted) {
    const start = Math.max(range.start, last);
    if (start >= range.end) {
      continue;
    }
    if (start > last) {
      parts.push({ text: value.slice(last, start), match: false });
    }
    parts.push({ text: value.slice(start, range.end), match: true });
    last = range.end;
  }
  if (last < value.length) {
    parts.push({ text: value.slice(last), match: false });
  }
  if (parts.length === 0) {
    return [{ text: value, match: false }];
  }
  return parts;
}
