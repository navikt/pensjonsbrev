import MiniSearch from "minisearch";

import { type MalType } from "~/api/brevbaker-api-endpoints";
import { type Line, type LineSegment } from "~/api/brevbakerTypes";

export type { Line, LineSegment };

export type TextIndexEntry = {
  id: string;
  malType: MalType;
  name: string;
  displayTitle?: string;
  language: string;
  lines: Line[];
  blockIds: string[];
};

export function textLine(value: string): Line {
  return [{ kind: "text", value }];
}

export type SnippetResult = {
  id: string;
  malType: MalType;
  name: string;
  language: string;
  startLine: number;
  matchCount: number;
  templateMatchCount: number;
  lines: Line[];
  blockId?: string;
  score: number;
  primaryLineIndex?: number;
  meta?: boolean;
  metaNeedle?: string;
};

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

function proximityBoost(line: Line, terms: string[]): number {
  if (terms.length <= 1) return 1;
  const text = lineSearchText(line);
  if (!text) return 1;

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

  const verbatimTermIndices = new Set(positions.map((p) => p.termIndex));
  if (verbatimTermIndices.size < terms.length) return 1;

  const termCount = new Map<number, number>();
  let covered = 0;
  let left = 0;
  let shortestSpan = text.length;
  for (let right = 0; right < positions.length; right++) {
    const ti = positions[right].termIndex;
    const prev = termCount.get(ti) ?? 0;
    termCount.set(ti, prev + 1);
    if (prev === 0) covered++;

    while (covered === terms.length) {
      const span = positions[right].start + terms[positions[right].termIndex].length - positions[left].start;
      if (span < shortestSpan) shortestSpan = span;
      const lti = positions[left].termIndex;
      const lc = termCount.get(lti)! - 1;
      termCount.set(lti, lc);
      if (lc === 0) covered--;
      left++;
    }
  }

  const idealSpan = terms.reduce((sum, t) => sum + t.length, 0) + terms.length - 1;
  return idealSpan / Math.max(shortestSpan, idealSpan);
}

export type ContentIndex = {
  miniSearch: MiniSearch;
  entries: TextIndexEntry[];
  lineRefs: { entryIndex: number; lineIndex: number }[];
};

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

export type SearchContext = { index: ContentIndex | null };

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

    const linesByScoreDescending = [...lineMatches.keys()].sort(
      (a, b) => (lineMatches.get(b)?.score ?? 0) - (lineMatches.get(a)?.score ?? 0) || a - b,
    );

    for (const window of mergeWindows(linesByScoreDescending, entry.lines.length)) {
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
      results.push({
        id: entry.id,
        malType: entry.malType,
        name: entry.name,
        language: entry.language,
        startLine: window.start,
        matchCount,
        templateMatchCount: lineMatches.size,
        lines: entry.lines.slice(window.start, window.end + 1),
        blockId: entry.blockIds[primaryLine],
        score: windowScore,
        primaryLineIndex: primaryLine - window.start,
      });
    }
  }

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
