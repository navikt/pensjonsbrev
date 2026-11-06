import { css } from "@emotion/react";
import Fuse from "fuse.js";
import { Fragment, type ReactNode } from "react";

import { FUZZY_MATCH_OPTIONS, type Line, normalizeForExactMatch, SHORT_TERM_LENGTH } from "~/search/textSearch";

export type HighlightRange = { start: number; end: number; terms: number[] };

export type HighlightParts = { ranges: HighlightRange[]; fullMatch: boolean };

/** All start/end indices (end exclusive) of `term` in `text`, case-insensitive. */
function exactRanges(text: string, term: string): Array<[number, number]> {
  const ranges: Array<[number, number]> = [];
  const lowerText = text.toLowerCase();
  const lowerTerm = term.toLowerCase();
  if (!lowerTerm) {
    return ranges;
  }
  let from = 0;
  for (;;) {
    const at = lowerText.indexOf(lowerTerm, from);
    if (at === -1) {
      break;
    }
    ranges.push([at, at + lowerTerm.length]);
    from = at + lowerTerm.length;
  }
  return ranges;
}

/** The single best fuzzy-match location of `term` in `text`, or none. Uses the
 *  same tuning as the search index (`FUZZY_MATCH_OPTIONS`) so a term only
 *  highlights here if it would also have counted as a match in search. Terms
 *  short enough to require a verbatim match in search (`SHORT_TERM_LENGTH`)
 *  are never fuzzy-highlighted, for the same reason. */
function fuzzyRange(text: string, term: string): [number, number] | undefined {
  if (term.length <= SHORT_TERM_LENGTH) {
    return undefined;
  }
  const result = Fuse.match(term, text, { ...FUZZY_MATCH_OPTIONS, includeMatches: true });
  if (!result.isMatch) {
    return undefined;
  }
  const range = result.indices?.[0];
  // Fuse indices are inclusive [start, end]; convert to exclusive end.
  return range ? [range[0], range[1] + 1] : undefined;
}

/** Per-term ranges: all exact occurrences of a term, or else its single best
 *  fuzzy location. Mirrors how fuzzy search matches each term on its own. */
function termRanges(text: string, needle: string): HighlightRange[] {
  return needle
    .split(/\s+/)
    .map((term, termIndex) => ({ term, termIndex }))
    .filter(({ term }) => term.length > 0)
    .flatMap(({ term, termIndex }) => {
      const exact = exactRanges(text, term);
      if (exact.length > 0) {
        return exact.map(([start, end]) => ({ start, end, terms: [termIndex] }));
      }
      const fuzzy = fuzzyRange(text, term);
      return fuzzy ? [{ start: fuzzy[0], end: fuzzy[1], terms: [termIndex] }] : [];
    });
}

/** Merges overlapping/adjacent ranges into a minimal non-overlapping set. */
export function mergeRanges(ranges: HighlightRange[]): HighlightRange[] {
  if (ranges.length === 0) {
    return ranges;
  }
  const sorted = [...ranges].sort((a, b) => a.start - b.start);
  const merged: HighlightRange[] = [sorted[0]];
  for (const { start, end, terms } of sorted.slice(1)) {
    const last = merged[merged.length - 1];
    if (start <= last.end) {
      last.end = Math.max(last.end, end);
      last.terms = [...new Set([...last.terms, ...terms])];
    } else {
      merged.push({ start, end, terms });
    }
  }
  return merged;
}

/** Searchable text across adjacent literal segments, with offsets mapped back
 *  to their original segment and character. */
function joinTextSegments(
  texts: string[],
  separator: "" | " ",
): { text: string; source: Array<{ segment: number; index: number }> } {
  let text = "";
  const source: Array<{ segment: number; index: number }> = [];
  let pendingSpace = false;
  texts.forEach((value, segment) => {
    for (let index = 0; index < value.length; index++) {
      const character = value[index];
      if (/\s/.test(character)) {
        pendingSpace = true;
        continue;
      }
      if (pendingSpace && text.length > 0) {
        text += " ";
        source.push({ segment, index });
      }
      pendingSpace = false;
      const lower = character.toLowerCase();
      text += lower;
      for (let i = 0; i < lower.length; i++) {
        source.push({ segment, index });
      }
    }
    pendingSpace ||= separator === " ";
  });
  return { text, source };
}

function mapJoinedRanges(
  ranges: Array<{ start: number; end: number; terms: number[] }>,
  source: Array<{ segment: number; index: number }>,
  segmentCount: number,
  fullMatch: boolean,
): HighlightParts[] {
  const bySegment: HighlightRange[][] = Array.from({ length: segmentCount }, () => []);
  for (const { start, end, terms } of ranges) {
    const positions = new Map<number, number[]>();
    for (let offset = start; offset < end; offset++) {
      const origin = source[offset];
      if (origin) {
        const indexes = positions.get(origin.segment) ?? [];
        indexes.push(origin.index);
        positions.set(origin.segment, indexes);
      }
    }
    for (const [segment, indexes] of positions) {
      bySegment[segment]?.push({
        start: Math.min(...indexes),
        end: Math.max(...indexes) + 1,
        terms,
      });
    }
  }
  return bySegment.map((segmentRanges) => ({
    ranges: mergeRanges(segmentRanges),
    fullMatch,
  }));
}

/** Computes ranges across literals. Snippets separate segments with a space;
 *  document literals concatenate without adding a separator. */
export function highlightParts(
  texts: string[],
  needle: string,
  exact = false,
  separator: "" | " " = " ",
): HighlightParts[] {
  const joined = joinTextSegments(texts, separator);
  const terms = needle.split(/\s+/).filter((term) => term.length > 0);
  const matches: HighlightRange[] = [];
  if (exact) {
    const phrase = normalizeForExactMatch(needle);
    const indexes = terms.map((_, index) => index);
    for (const [start, end] of exactRanges(joined.text, phrase)) {
      matches.push({ start, end, terms: indexes.length > 0 ? indexes : [0] });
    }
  } else {
    matches.push(...termRanges(joined.text, needle));
  }
  const fullMatch =
    matches.length > 0 &&
    (exact || terms.every((_, index) => matches.some(({ terms: matchedTerms }) => matchedTerms.includes(index))));
  return mapJoinedRanges(matches, joined.source, texts.length, fullMatch);
}

function VariableChip({ label }: { label: string }) {
  return (
    <span
      css={css`
        display: inline-flex;
        align-items: baseline;
        gap: 2px;
        margin: 0 1px;
        padding: 0 var(--ax-space-4);
        border-radius: var(--ax-radius-4);
        background: var(--ax-bg-accent-moderate);
        color: var(--ax-text-accent-subtle);
        font-style: italic;
        white-space: nowrap;
      `}
      title={`Variabel: ${label}`}
    >
      <span aria-hidden>⟨</span>
      {label}
      <span aria-hidden>⟩</span>
    </span>
  );
}

/** How a match is marked. Snippets in the result list are short, so bold is
 *  enough; in a whole document the marks have to stand out while scrolling. */
export type HighlightVariant = "snippet" | "document";
const MARK_STYLES = {
  snippet: css`
    background: transparent;
    color: inherit;
    font-weight: var(--ax-font-weight-bold);
  `,
  document: css`
    background: var(--ax-bg-warning-moderate);
    color: inherit;
    border-radius: var(--ax-radius-2);
    box-shadow: inset 0 -2px 0 var(--ax-border-warning);
  `,
};
/**
 * Renders text with the matched parts of the needle wrapped in `<mark>`.
 *
 * In exact mode the needle matched as one continuous phrase, so only whole
 * occurrences of that phrase are highlighted. Otherwise the needle is split
 * into terms and each is highlighted on its own: if a term is an exact
 * (case-insensitive) substring, all its occurrences are highlighted; if it only
 * fuzzy-matches (e.g. query "ble" matching "blue"), only its single best-match
 * location is. Terms with no match at all in this particular text are left
 * unhighlighted.
 */
export function HighlightText({
  text,
  needle,
  exact,
  variant = "snippet",
  ranges,
  fullMatch,
}: {
  text: string;
  needle: string;
  exact?: boolean;
  variant?: HighlightVariant;
  ranges?: HighlightRange[];
  fullMatch?: boolean;
}) {
  const parts = ranges
    ? { ranges, fullMatch: fullMatch ?? false }
    : (highlightParts([text], needle, exact)[0] ?? { ranges: [], fullMatch: false });
  if (parts.ranges.length === 0) {
    return <>{text}</>;
  }

  const nodes: ReactNode[] = [];
  let cursor = 0;
  parts.ranges.forEach(({ start, end }, i) => {
    if (start > cursor) {
      nodes.push(<Fragment key={`plain-${i}`}>{text.slice(cursor, start)}</Fragment>);
    }
    nodes.push(
      <mark css={MARK_STYLES[variant]} data-search-full-match={parts.fullMatch || undefined} key={`mark-${i}`}>
        {text.slice(start, end)}
      </mark>,
    );
    cursor = end;
  });
  if (cursor < text.length) {
    nodes.push(<Fragment key="plain-end">{text.slice(cursor)}</Fragment>);
  }
  return <>{nodes}</>;
}

/** Renders a line of segments (text + variable chips), with optional needle
 *  highlighting. `exact` mirrors the search mode: the needle is highlighted as
 *  one continuous phrase rather than term by term. */
export function LineContent({ line, needle, exact }: { line: Line; needle?: string; exact?: boolean }) {
  const parts = new Map<number, HighlightParts>();
  if (needle) {
    let start = 0;
    while (start < line.length) {
      if (line[start]?.type === "var") {
        start++;
        continue;
      }
      const indexes: number[] = [];
      while (start < line.length && line[start]?.type === "text") {
        indexes.push(start);
        start++;
      }
      const highlighted = highlightParts(
        indexes.map((index) => (line[index]?.type === "text" ? line[index].value : "")),
        needle,
        exact,
      );
      indexes.forEach((index, i) => {
        const segmentParts = highlighted[i];
        if (segmentParts) {
          parts.set(index, segmentParts);
        }
      });
    }
  }
  return (
    <>
      {line.map((segment, index) => {
        if (segment.type === "var") {
          return <VariableChip key={index} label={segment.label} />;
        }
        if (needle) {
          const segmentParts = parts.get(index);
          return (
            <HighlightText
              exact={exact}
              fullMatch={segmentParts?.fullMatch}
              key={index}
              needle={needle}
              ranges={segmentParts?.ranges}
              text={segment.value}
            />
          );
        }
        return <Fragment key={index}>{segment.value}</Fragment>;
      })}
    </>
  );
}

/** Truncates a line to at most `maxChars` characters, appending "…" if truncated. */
export function truncateLine(line: Line, maxChars: number): Line {
  let remaining = maxChars;
  const result: Line = [];
  for (const segment of line) {
    const text = segment.type === "text" ? segment.value : segment.label;
    if (text.length <= remaining) {
      result.push(segment);
      remaining -= text.length;
    } else {
      if (segment.type === "text") {
        result.push({ type: "text", value: `${segment.value.slice(0, remaining).replace(/\s+$/, "")} …` });
      }
      break;
    }
  }
  return result;
}
