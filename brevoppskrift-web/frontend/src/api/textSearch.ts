import { type MalType } from "~/api/brevbaker-api-endpoints";
import {
  type Attachment,
  type ContentOrControlStructure,
  ContentOrControlStructureType,
  ElementType,
  type Expression,
  type Item,
  type OutlineContent,
  type ParagraphContent,
  type Row,
  type TemplateDocumentation,
  type Text,
} from "~/api/brevbakerTypes";

/** A line is rendered (and indexed) as a sequence of segments. Literal text is
 * searchable; variables are shown as a placeholder but are never searchable. */
export type LineSegment = { kind: "text"; value: string } | { kind: "var"; label: string };
export type Line = LineSegment[];

export type TextIndexEntry = {
  id: string;
  malType: MalType;
  name: string;
  language: string;
  lines: Line[];
};

const MAX_VAR_LABEL_LENGTH = 40;
// Sentinel marks where a variable sits while we normalise whitespace on the raw
// string. It is not whitespace, so it survives collapsing, and is extremely
// unlikely to occur in real letter text.
const VAR_SENTINEL = "\u0001";

function normalize(value: string): string {
  return value.replace(/\s+/g, " ").trim();
}

/** Best-effort short, human readable label for an expression so the reader sees
 * what kind of variable sits in the text. Only used for display. */
/** Best-effort short, human readable name for the variable in an expression so
 * the reader sees which field sits in the text. Field references are nested
 * POSTFIX accessors rooted at the `argument` scope, e.g. `argument.foo.bar`; we
 * surface the leaf field name (`bar`) rather than the whole path. Only used for
 * display. */
function variableName(expression: Expression | undefined): string {
  if (!expression) {
    return "";
  }
  if ("scopeName" in expression) {
    // The synthetic root scopes are not useful names.
    if (expression.scopeName === "argument" || expression.scopeName === "forEach_item") {
      return "";
    }
    return expression.scopeName;
  }
  if ("value" in expression) {
    return String(expression.value);
  }
  // Invoke: a POSTFIX `.field` accessor is the property name; the outermost one
  // is the leaf field. Other operators (functions like format(), infix, …) wrap
  // the underlying field, so look inside their operands.
  if (expression.operator.syntax === "POSTFIX" && expression.operator.text.startsWith(".")) {
    const field = expression.operator.text.slice(1).trim();
    if (field) {
      return field;
    }
  }
  return variableName(expression.first) || variableName(expression.second);
}

function describeExpression(expression: Expression): string {
  const label = normalize(variableName(expression));
  if (!label) {
    return "variabel";
  }
  return label.length > MAX_VAR_LABEL_LENGTH ? `${label.slice(0, MAX_VAR_LABEL_LENGTH)}…` : label;
}

/** Iterates the CONTENT elements of a control-structure array, recursing through
 * conditionals (all branches) and for-each bodies, preserving document order. */
function eachContent<E>(items: ContentOrControlStructure<E>[], fn: (element: E) => void): void {
  for (const cocs of items) {
    switch (cocs.controlStructureType) {
      case ContentOrControlStructureType.CONDITIONAL: {
        eachContent(cocs.showIf, fn);
        for (const elseIf of cocs.elseIf) {
          eachContent(elseIf.showIf, fn);
        }
        eachContent(cocs.showElse, fn);
        break;
      }
      case ContentOrControlStructureType.FOR_EACH: {
        eachContent(cocs.body, fn);
        break;
      }
      case ContentOrControlStructureType.CONTENT: {
        fn(cocs.content);
        break;
      }
    }
  }
}

/** Accumulates raw text (with variable sentinels) plus the labels of those
 * variables, so a line can be assembled once whitespace has been normalised. */
type LineBuilder = { raw: string; labels: string[] };

function newBuilder(): LineBuilder {
  return { raw: "", labels: [] };
}

function pushVariable(builder: LineBuilder, expression: Expression): void {
  builder.raw += VAR_SENTINEL;
  builder.labels.push(describeExpression(expression));
}

/** Turns an accumulated builder into a Line, or null when there is nothing to
 * show. Variable sentinels are replaced by `var` segments paired with their
 * labels in order. */
function finishLine(builder: LineBuilder): Line | null {
  const normalized = normalize(builder.raw);
  if (!normalized) {
    return null;
  }
  const parts = normalized.split(VAR_SENTINEL);
  const segments: Line = [];
  for (let i = 0; i < parts.length; i++) {
    let value = parts[i];
    if (i === 0) {
      value = value.replace(/^\s+/, "");
    }
    if (i === parts.length - 1) {
      value = value.replace(/\s+$/, "");
    }
    if (value) {
      segments.push({ kind: "text", value });
    }
    if (i < builder.labels.length) {
      segments.push({ kind: "var", label: builder.labels[i] });
    }
  }
  return segments.length > 0 ? segments : null;
}

function appendTextArray(builder: LineBuilder, items: ContentOrControlStructure<Text>[]): void {
  eachContent(items, (text) => {
    if (text.elementType === ElementType.PARAGRAPH_TEXT_LITERAL) {
      builder.raw += text.text;
    } else {
      pushVariable(builder, text.expression);
    }
  });
}

function lineFromTextArray(items: ContentOrControlStructure<Text>[]): Line | null {
  const builder = newBuilder();
  appendTextArray(builder, items);
  return finishLine(builder);
}

function rowToLine(row: Row): Line | null {
  const builder = newBuilder();
  let first = true;
  for (const cell of row.cells) {
    const cellBuilder = newBuilder();
    appendTextArray(cellBuilder, cell.text);
    if (!normalize(cellBuilder.raw)) {
      continue;
    }
    if (!first) {
      builder.raw += " | ";
    }
    builder.raw += cellBuilder.raw;
    builder.labels.push(...cellBuilder.labels);
    first = false;
  }
  return finishLine(builder);
}

function linesFromParagraph(items: ContentOrControlStructure<ParagraphContent>[]): Line[] {
  const lines: Line[] = [];
  let buffer = newBuilder();
  const flush = () => {
    const line = finishLine(buffer);
    if (line) {
      lines.push(line);
    }
    buffer = newBuilder();
  };
  const pushLine = (line: Line | null) => {
    if (line) {
      lines.push(line);
    }
  };

  eachContent(items, (content) => {
    switch (content.elementType) {
      case ElementType.PARAGRAPH_TEXT_LITERAL: {
        buffer.raw += content.text;
        break;
      }
      case ElementType.PARAGRAPH_TEXT_EXPRESSION: {
        pushVariable(buffer, content.expression);
        break;
      }
      case ElementType.PARAGRAPH_ITEMLIST: {
        flush();
        eachContent(content.items, (item: Item) => pushLine(lineFromTextArray(item.text)));
        break;
      }
      case ElementType.PARAGRAPH_ITEMLIST_ITEM: {
        flush();
        pushLine(lineFromTextArray(content.text));
        break;
      }
      case ElementType.PARAGRAPH_TABLE: {
        flush();
        pushLine(rowToLine(content.header));
        eachContent(content.rows, (row: Row) => pushLine(rowToLine(row)));
        break;
      }
      case ElementType.PARAGRAPH_TABLE_ROW: {
        flush();
        pushLine(rowToLine(content));
        break;
      }
    }
  });
  flush();
  return lines;
}

function linesFromOutline(items: ContentOrControlStructure<OutlineContent>[]): Line[] {
  const lines: Line[] = [];
  eachContent(items, (element) => {
    switch (element.elementType) {
      case ElementType.TITLE1:
      case ElementType.TITLE2:
      case ElementType.TITLE3: {
        const line = lineFromTextArray(element.text);
        if (line) {
          lines.push(line);
        }
        break;
      }
      case ElementType.PARAGRAPH: {
        lines.push(...linesFromParagraph(element.paragraph));
        break;
      }
    }
  });
  return lines;
}

function linesFromDocument(document: TemplateDocumentation | Attachment): Line[] {
  const lines: Line[] = [];
  // `title` carries TITLE elements just like `outline`, so process it the same way.
  lines.push(...linesFromOutline(document.title as ContentOrControlStructure<OutlineContent>[]));
  lines.push(...linesFromOutline(document.outline));
  return lines;
}

/** Flattens a template's documentation (title, outline and every attachment) into
 * an ordered list of lines. Variables are kept as `var` segments. */
export function extractDocumentationLines(documentation: TemplateDocumentation): Line[] {
  const lines = linesFromDocument(documentation);
  for (const attachment of documentation.attachments) {
    lines.push(...linesFromDocument(attachment));
  }
  return lines;
}

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

/** The lower-cased literal text segments of a line. Variables are excluded, and
 * segments are kept separate (not concatenated) so occurrence counts line up
 * with the separately-rendered DOM text nodes on the detail page. */
function segmentTexts(line: Line): string[] {
  const texts: string[] = [];
  for (const segment of line) {
    if (segment.kind === "text") {
      texts.push(segment.value.toLowerCase());
    }
  }
  return texts;
}

/** Number of literal occurrences of `needle` in a line, summed per segment. */
function lineOccurrences(line: Line, needle: string): number {
  let count = 0;
  for (const text of segmentTexts(line)) {
    count += countOccurrences(text, needle);
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
  /** Concrete literal substring to scroll to on the detail page. With fuzzy
   * search the raw query may not appear verbatim (typos, variable slots), so
   * navigation anchors on a real matched document substring instead. */
  anchorQuery: string;
  /** Per-segment character ranges to highlight on the primary (closest) line. */
  highlightRanges: HighlightRange[];
  /** Index into `lines` of the line to highlight (the closest match). */
  highlightLineIndex?: number;
  /** True when the match is in the template name or brevkode rather than the body. */
  meta?: boolean;
  /** For `meta` matches: the exact needle to highlight on every shown line. */
  metaNeedle?: string;
};

/** A highlight range within one segment of a line: character offsets `[start,end)`
 * into that segment's `value`. */
export type HighlightRange = { segmentIndex: number; start: number; end: number };

type SearchCharCell = { segmentIndex: number; offset: number } | null;

const lineSearchCache = new WeakMap<Line, { text: string; map: SearchCharCell[] }>();

/** Builds the lower-cased, whitespace-collapsed searchable text of a line plus a
 * map from each character back to its `{segmentIndex, offset}` in the original
 * segment value (or `null` for the synthetic boundary space between segments).
 * Variables are excluded and act as separators. Whitespace inside a single
 * segment maps to that segment so highlights stay contiguous. Cached per line. */
function getLineSearch(line: Line): { text: string; map: SearchCharCell[] } {
  const cached = lineSearchCache.get(line);
  if (cached) {
    return cached;
  }
  const chars: string[] = [];
  const map: SearchCharCell[] = [];
  let boundaryPending = false;
  let started = false;
  for (let segmentIndex = 0; segmentIndex < line.length; segmentIndex++) {
    const segment = line[segmentIndex];
    if (segment.kind !== "text") {
      if (started) {
        boundaryPending = true;
      }
      continue;
    }
    const value = segment.value.toLowerCase();
    let intraPending = -1;
    for (let offset = 0; offset < value.length; offset++) {
      const ch = value[offset];
      if (/\s/.test(ch)) {
        if (started || chars.length > 0) {
          intraPending = offset;
        }
        continue;
      }
      if (boundaryPending) {
        chars.push(" ");
        map.push(null);
        boundaryPending = false;
        intraPending = -1;
      } else if (intraPending >= 0) {
        chars.push(" ");
        map.push({ segmentIndex, offset: intraPending });
        intraPending = -1;
      }
      chars.push(ch);
      map.push({ segmentIndex, offset });
      started = true;
    }
    if (intraPending >= 0) {
      boundaryPending = true;
    }
  }
  const result = { text: chars.join(""), map };
  lineSearchCache.set(line, result);
  return result;
}

/** The lower-cased, whitespace-collapsed searchable text of a line. Variables
 * are excluded and replaced by a single separating space. */
export function lineSearchText(line: Line): string {
  return getLineSearch(line).text;
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

/** Maximum edit distance allowed for a fuzzy substring match, by query length.
 * Short queries are matched (near-)exactly to avoid noise; longer queries get a
 * little more slack so a stray typo or a single variable slot (e.g. "x") still
 * matches. Capped at 3 so matching stays phrase-anchored, never scattered. */
function maxEditsFor(length: number): number {
  if (length < 4) {
    return 0;
  }
  if (length < 8) {
    return 1;
  }
  if (length < 16) {
    return 2;
  }
  return 3;
}

/** Splits a query into `parts` roughly equal contiguous chunks for the
 * pigeonhole prefilter: a line can only match within `parts - 1` edits if at
 * least one chunk occurs in it verbatim. */
function queryChunks(query: string, parts: number): string[] {
  if (parts <= 1) {
    return [query];
  }
  const size = Math.ceil(query.length / parts);
  const chunks: string[] = [];
  for (let i = 0; i < query.length; i += size) {
    chunks.push(query.slice(i, i + size));
  }
  return chunks;
}

/** Sellers' fuzzy substring match: finds the best-matching contiguous window of
 * `text` for `pattern` (the match may start and end anywhere), tolerating up to
 * `maxEdits` insertions/deletions/substitutions. Returns the window `[start,end)`
 * and its edit distance, or `null` if no window is within `maxEdits`. */
function fuzzySubstring(
  pattern: string,
  text: string,
  maxEdits: number,
): { distance: number; start: number; end: number } | null {
  const m = pattern.length;
  const n = text.length;
  if (m === 0 || n === 0) {
    return null;
  }
  // dp row: edit distance of pattern[0..i) against the best text window ending
  // at column j; start row tracks where that window began.
  let prevDist = new Array<number>(n + 1);
  let prevStart = new Array<number>(n + 1);
  for (let j = 0; j <= n; j++) {
    prevDist[j] = 0;
    prevStart[j] = j;
  }
  let curDist = new Array<number>(n + 1);
  let curStart = new Array<number>(n + 1);
  for (let i = 1; i <= m; i++) {
    curDist[0] = i;
    curStart[0] = 0;
    const pc = pattern.charCodeAt(i - 1);
    let rowMin = Number.POSITIVE_INFINITY;
    for (let j = 1; j <= n; j++) {
      const cost = pc === text.charCodeAt(j - 1) ? 0 : 1;
      let best = prevDist[j - 1] + cost;
      let bestStart = prevStart[j - 1];
      const deletion = prevDist[j] + 1;
      if (deletion < best) {
        best = deletion;
        bestStart = prevStart[j];
      }
      const insertion = curDist[j - 1] + 1;
      if (insertion < best) {
        best = insertion;
        bestStart = curStart[j - 1];
      }
      curDist[j] = best;
      curStart[j] = bestStart;
      if (best < rowMin) {
        rowMin = best;
      }
    }
    // The minimum of each row is non-decreasing in i, so if it already exceeds
    // maxEdits the final row cannot match.
    if (rowMin > maxEdits) {
      return null;
    }
    const swapDist = prevDist;
    prevDist = curDist;
    curDist = swapDist;
    const swapStart = prevStart;
    prevStart = curStart;
    curStart = swapStart;
  }
  let bestDist = Number.POSITIVE_INFINITY;
  let bestEnd = -1;
  let bestStart = 0;
  for (let j = 1; j <= n; j++) {
    if (prevDist[j] < bestDist) {
      bestDist = prevDist[j];
      bestEnd = j;
      bestStart = prevStart[j];
    }
  }
  if (bestEnd < 0 || bestDist > maxEdits) {
    return null;
  }
  return { distance: bestDist, start: bestStart, end: bestEnd };
}

/** Matches a single line against the query, using an exact `indexOf` fast path
 * when no edits are allowed, otherwise a pigeonhole prefilter followed by the
 * fuzzy-substring DP. Returns the matched window and its edit distance. */
function matchLine(
  line: Line,
  query: string,
  maxEdits: number,
  chunks: string[],
): { distance: number; start: number; end: number } | null {
  const { text } = getLineSearch(line);
  if (maxEdits === 0) {
    const at = text.indexOf(query);
    return at < 0 ? null : { distance: 0, start: at, end: at + query.length };
  }
  let candidate = false;
  for (const chunk of chunks) {
    if (chunk.length > 0 && text.includes(chunk)) {
      candidate = true;
      break;
    }
  }
  if (!candidate) {
    return null;
  }
  return fuzzySubstring(query, text, maxEdits);
}

/** Maps a matched window `[start,end)` in a line's search text back to per-segment
 * highlight ranges, and picks the longest single-segment range as the navigation
 * anchor (a real document substring the detail page can scroll to). */
function buildHighlight(
  line: Line,
  start: number,
  end: number,
): { ranges: HighlightRange[]; anchorRange: HighlightRange | null } {
  const { map } = getLineSearch(line);
  const ranges: HighlightRange[] = [];
  let current: HighlightRange | null = null;
  for (let i = start; i < end; i++) {
    const cell = map[i];
    if (!cell) {
      if (current) {
        ranges.push(current);
        current = null;
      }
      continue;
    }
    if (current && current.segmentIndex === cell.segmentIndex && cell.offset === current.end) {
      current.end = cell.offset + 1;
    } else {
      if (current) {
        ranges.push(current);
      }
      current = { segmentIndex: cell.segmentIndex, start: cell.offset, end: cell.offset + 1 };
    }
  }
  if (current) {
    ranges.push(current);
  }
  let anchorRange: HighlightRange | null = null;
  let bestLength = 0;
  for (const range of ranges) {
    const length = range.end - range.start;
    if (length > bestLength) {
      bestLength = length;
      anchorRange = range;
    }
  }
  return { ranges, anchorRange };
}

/** Occurrence ordinal (0-based, document order) of the anchor substring up to and
 * including its position in the primary line, so the detail page's substring
 * walker scrolls to the right spot. */
function anchorOrdinal(lines: Line[], primaryLine: number, anchorRange: HighlightRange): number {
  const segment = lines[primaryLine][anchorRange.segmentIndex];
  if (segment.kind !== "text") {
    return 0;
  }
  const needle = segment.value.slice(anchorRange.start, anchorRange.end).toLowerCase();
  if (!needle) {
    return 0;
  }
  let ordinal = 0;
  for (let i = 0; i < primaryLine; i++) {
    ordinal += lineOccurrences(lines[i], needle);
  }
  const primarySegments = lines[primaryLine];
  for (let s = 0; s < anchorRange.segmentIndex; s++) {
    const seg = primarySegments[s];
    if (seg.kind === "text") {
      ordinal += countOccurrences(seg.value.toLowerCase(), needle);
    }
  }
  ordinal += countOccurrences(segment.value.toLowerCase().slice(0, anchorRange.start), needle);
  return ordinal;
}

/** Context needed to run a search: just the indexed entries. */
export type FuzzySearchContext = { entries: TextIndexEntry[] };

/** Fuzzy substring search over the indexed lines using Levenshtein distance.
 * Matches the query as a contiguous phrase that may start/end anywhere on a
 * line, tolerating a few edits (typos, or a single variable slot bridged by its
 * separating space). Because matching is contiguous, words are never picked from
 * scattered positions across a sentence. Returns a flat list of snippet windows
 * ranked best-match first, each anchored on a concrete matched substring. */
export function searchFuzzy(context: FuzzySearchContext, rawQuery: string): SnippetResult[] {
  const query = rawQuery.toLowerCase().replace(/\s+/g, " ").trim();
  const { entries } = context;
  if (!query) {
    return [];
  }
  const maxEdits = maxEditsFor(query.length);
  const chunks = queryChunks(query, maxEdits + 1);

  const results: SnippetResult[] = [];
  const matchedEntries = new Set<number>();

  for (let entryIndex = 0; entryIndex < entries.length; entryIndex++) {
    const entry = entries[entryIndex];
    const lineMatches = new Map<number, { distance: number; start: number; end: number }>();
    for (let i = 0; i < entry.lines.length; i++) {
      const match = matchLine(entry.lines[i], query, maxEdits, chunks);
      if (match) {
        lineMatches.set(i, match);
      }
    }
    if (lineMatches.size === 0) {
      continue;
    }
    matchedEntries.add(entryIndex);

    // Seed snippet windows from the best (lowest-distance) matched lines.
    const seeds = [...lineMatches.keys()].sort(
      (a, b) => (lineMatches.get(a)?.distance ?? 0) - (lineMatches.get(b)?.distance ?? 0) || a - b,
    );

    for (const window of mergeWindows(seeds, entry.lines.length)) {
      let matchCount = 0;
      let windowScore = 0;
      let primaryLine = window.start;
      let bestDistance = Number.POSITIVE_INFINITY;
      for (let i = window.start; i <= window.end; i++) {
        const match = lineMatches.get(i);
        if (match) {
          matchCount++;
          windowScore += query.length - match.distance;
          if (match.distance < bestDistance) {
            bestDistance = match.distance;
            primaryLine = i;
          }
        }
      }
      const primaryMatch = lineMatches.get(primaryLine);
      const { ranges, anchorRange } =
        primaryMatch != null
          ? buildHighlight(entry.lines[primaryLine], primaryMatch.start, primaryMatch.end)
          : { ranges: [], anchorRange: null };
      const anchorSegment = anchorRange ? entry.lines[primaryLine][anchorRange.segmentIndex] : null;
      const anchorQuery =
        anchorRange && anchorSegment?.kind === "text"
          ? anchorSegment.value.slice(anchorRange.start, anchorRange.end)
          : query;
      const matchOrdinal = anchorRange ? anchorOrdinal(entry.lines, primaryLine, anchorRange) : undefined;
      results.push({
        id: entry.id,
        malType: entry.malType,
        name: entry.name,
        language: entry.language,
        startLine: window.start,
        matchCount,
        templateMatchCount: lineMatches.size,
        lines: entry.lines.slice(window.start, window.end + 1),
        matchOrdinal,
        // Rank fuller matches first, then prefer the window with the lowest edit
        // distance among equal coverage.
        score: windowScore - bestDistance * 0.001,
        anchorQuery,
        highlightRanges: ranges,
        highlightLineIndex: primaryLine - window.start,
      });
    }
  }

  // Exact name/brevkode matches for templates with no body hit, so a brevkode
  // search still surfaces its template.
  for (let entryIndex = 0; entryIndex < entries.length; entryIndex++) {
    if (matchedEntries.has(entryIndex)) {
      continue;
    }
    const entry = entries[entryIndex];
    const metaLines: Line[] = [];
    if (entry.name.toLowerCase().includes(query)) {
      metaLines.push(textLine(entry.name));
    }
    if (entry.id.toLowerCase().includes(query)) {
      metaLines.push(textLine(entry.id));
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
