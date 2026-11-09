import { css } from "@emotion/react";
import { Fragment, type ReactNode } from "react";

import { type HighlightParts, type HighlightRange, matchTextFlow, type TextFlow } from "~/search/matchTextFlow";
import { type Line, lineFlow } from "~/search/textSearch";

export type { HighlightParts, HighlightRange } from "~/search/matchTextFlow";

/** Computes ranges across literals. Snippets separate segments with a space;
 *  document literals concatenate without adding a separator. */
export function highlightParts(
  texts: string[],
  needle: string,
  exact = false,
  separator: "" | " " = " ",
): HighlightParts[] {
  const flow = texts.flatMap((text, source): TextFlow<number>[] => [
    ...(separator === " " && source > 0 ? [{ type: "space" } as const] : []),
    { type: "text", source, text },
  ]);
  const result = matchTextFlow(flow, needle, exact);
  return texts.map((_, index) => result.parts.get(index) ?? { ranges: [], fullMatch: false });
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

// Spaces stay selectable, so copying a snippet keeps cell contents apart without the divider.
const CELL_DIVIDER = " | ";
function CellDivider() {
  return (
    <>
      {" "}
      <span
        aria-hidden
        css={css`
          color: var(--ax-text-subtle);
          user-select: none;
        `}
      >
        |
      </span>{" "}
    </>
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

/** Renders a line of segments (text, variable chips and table-cell dividers),
 *  with optional needle highlighting. `exact` mirrors the search mode: the
 *  needle is highlighted as one continuous phrase rather than term by term. */
export function LineContent({ line, needle, exact }: { line: Line; needle?: string; exact?: boolean }) {
  const parts = needle ? matchTextFlow(lineFlow(line), needle, exact).parts : undefined;
  return (
    <>
      {line.map((segment, index) => {
        if (segment.type === "var") {
          return <VariableChip key={index} label={segment.label} />;
        }
        if (segment.type === "cell") {
          return <CellDivider key={index} />;
        }
        if (needle) {
          const segmentParts = parts?.get(index);
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
    const text = segment.type === "text" ? segment.value : segment.type === "var" ? segment.label : CELL_DIVIDER;
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
