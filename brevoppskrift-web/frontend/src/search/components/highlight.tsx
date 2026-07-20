import { css } from "@emotion/react";
import Fuse from "fuse.js";
import { Fragment, type ReactNode } from "react";

import { FUZZY_MATCH_OPTIONS, type Line } from "~/search/textSearch";

type Range = [number, number];

/** All start/end indices (end exclusive) of `term` in `text`, case-insensitive. */
function exactRanges(text: string, term: string): Range[] {
  const ranges: Range[] = [];
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
 *  highlights here if it would also have counted as a match in search. */
function fuzzyRange(text: string, term: string): Range | undefined {
  const result = Fuse.match(term, text, { ...FUZZY_MATCH_OPTIONS, includeMatches: true });
  if (!result.isMatch) {
    return undefined;
  }
  const range = result.indices?.[0];
  // Fuse indices are inclusive [start, end]; convert to exclusive end.
  return range ? [range[0], range[1] + 1] : undefined;
}

/** Merges overlapping/adjacent ranges into a minimal non-overlapping set. */
function mergeRanges(ranges: Range[]): Range[] {
  if (ranges.length === 0) {
    return ranges;
  }
  const sorted = [...ranges].sort((a, b) => a[0] - b[0]);
  const merged: Range[] = [sorted[0]];
  for (const [start, end] of sorted.slice(1)) {
    const last = merged[merged.length - 1];
    if (start <= last[1]) {
      last[1] = Math.max(last[1], end);
    } else {
      merged.push([start, end]);
    }
  }
  return merged;
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

/**
 * Renders text with needle terms wrapped in `<mark>`. Per term: if it's an
 * exact (case-insensitive) substring, all its occurrences are highlighted;
 * otherwise, if it only fuzzy-matches (e.g. query "ble" matching "blue"),
 * only its single best-match location is highlighted. Terms with no match at
 * all in this particular text are left unhighlighted.
 */
function HighlightText({ text, needle }: { text: string; needle: string }) {
  const terms = needle.split(/\s+/).filter((t) => t.length > 0);
  if (terms.length === 0) {
    return <>{text}</>;
  }

  const ranges = mergeRanges(
    terms.flatMap((term) => {
      const exact = exactRanges(text, term);
      if (exact.length > 0) {
        return exact;
      }
      const fuzzy = fuzzyRange(text, term);
      return fuzzy ? [fuzzy] : [];
    }),
  );
  if (ranges.length === 0) {
    return <>{text}</>;
  }

  const nodes: ReactNode[] = [];
  let cursor = 0;
  ranges.forEach(([start, end], i) => {
    if (start > cursor) {
      nodes.push(<Fragment key={`plain-${i}`}>{text.slice(cursor, start)}</Fragment>);
    }
    nodes.push(
      <mark
        css={{
          background: "transparent",
          color: "inherit",
          fontWeight: "var(--ax-font-weight-bold)",
        }}
        key={`mark-${i}`}
      >
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

/** Renders a line of segments (text + variable chips), with optional needle highlighting. */
export function LineContent({ line, needle }: { line: Line; needle?: string }) {
  return (
    <>
      {line.map((segment, index) => {
        if (segment.type === "var") {
          return <VariableChip key={index} label={segment.label} />;
        }
        if (needle) {
          return <HighlightText key={index} needle={needle} text={segment.value} />;
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
