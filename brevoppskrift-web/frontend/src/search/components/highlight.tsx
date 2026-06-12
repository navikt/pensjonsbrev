import { css } from "@emotion/react";
import { Fragment } from "react";

import { type Line } from "~/search/textSearch";

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

/** Renders text with all occurrences of each needle term wrapped in `<mark>`. */
function HighlightText({ text, needle }: { text: string; needle: string }) {
  const terms = needle.split(/\s+/).filter((t) => t.length > 0);
  if (terms.length === 0) {
    return <>{text}</>;
  }
  const escaped = terms.map((t) => t.replace(/[.*+?^${}()|[\]\\]/g, "\\$&"));
  const regex = new RegExp(`(${escaped.join("|")})`, "gi");
  const parts = text.split(regex);
  if (parts.length === 1) {
    return <>{text}</>;
  }
  return (
    <>{parts.map((part, i) => (i % 2 === 1 ? <mark key={i}>{part}</mark> : <Fragment key={i}>{part}</Fragment>))}</>
  );
}

/** Renders a line of segments (text + variable chips), with optional needle highlighting. */
export function LineContent({ line, needle }: { line: Line; needle?: string }) {
  return (
    <>
      {line.map((segment, index) => {
        if (segment.kind === "var") {
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
    const text = segment.kind === "text" ? segment.value : segment.label;
    if (text.length <= remaining) {
      result.push(segment);
      remaining -= text.length;
    } else {
      if (segment.kind === "text") {
        result.push({ kind: "text", value: `${segment.value.slice(0, remaining).replace(/\s+$/, "")} …` });
      }
      break;
    }
  }
  return result;
}
