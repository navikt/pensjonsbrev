import { css } from "@emotion/react";
import { Fragment } from "react";

import { exactRanges, type HighlightRange, type Line, splitSegmentByRanges } from "~/search/textSearch";

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

export function HighlightedLine({
  line,
  ranges,
  metaNeedle,
}: {
  line: Line;
  ranges: HighlightRange[];
  metaNeedle?: string;
}) {
  return (
    <>
      {line.map((segment, index) => {
        if (segment.kind === "var") {
          return <VariableChip key={index} label={segment.label} />;
        }
        const segmentRanges = metaNeedle
          ? exactRanges(segment.value, metaNeedle)
          : ranges
              .filter((range) => range.segmentIndex === index)
              .map((range) => ({ start: range.start, end: range.end }));
        return splitSegmentByRanges(segment.value, segmentRanges).map((part, partIndex) =>
          part.match ? (
            <mark key={`${index}-${partIndex}`}>{part.text}</mark>
          ) : (
            <Fragment key={`${index}-${partIndex}`}>{part.text}</Fragment>
          ),
        );
      })}
    </>
  );
}

function lineLength(line: Line): number {
  return line.reduce(
    (sum, segment) => sum + (segment.kind === "text" ? segment.value.length : segment.label.length),
    0,
  );
}

function linePlainText(line: Line): string {
  return line
    .map((segment) => (segment.kind === "text" ? segment.value : segment.label))
    .join("")
    .toLowerCase();
}

export function indexOfNeedleCenter(line: Line, needle: string): number | null {
  if (!needle) {
    return null;
  }
  const idx = linePlainText(line).indexOf(needle.toLowerCase());
  return idx < 0 ? null : idx + Math.floor(needle.length / 2);
}

export function rangesCenter(line: Line, ranges: HighlightRange[]): number | null {
  if (ranges.length === 0) {
    return null;
  }
  const globalOffset = (segmentIndex: number, offset: number): number => {
    let global = 0;
    for (let i = 0; i < segmentIndex; i++) {
      const segment = line[i];
      global += segment.kind === "text" ? segment.value.length : segment.label.length;
    }
    return global + offset;
  };
  const first = ranges[0];
  const last = ranges[ranges.length - 1];
  return Math.floor((globalOffset(first.segmentIndex, first.start) + globalOffset(last.segmentIndex, last.end)) / 2);
}

type ClampedLine = { line: Line; ranges: HighlightRange[] };

export function clampLine(
  line: Line,
  _center: number | null,
  maxChars: number,
  highlight: HighlightRange[],
): ClampedLine {
  let lastHighlightEnd = 0;
  if (highlight.length > 0) {
    let pos = 0;
    for (let i = 0; i < line.length; i++) {
      const segment = line[i];
      const segLen = segment.kind === "text" ? segment.value.length : segment.label.length;
      for (const range of highlight) {
        if (range.segmentIndex === i) {
          lastHighlightEnd = Math.max(lastHighlightEnd, pos + range.end);
        }
      }
      pos += segLen;
    }
  }

  const effectiveMax = Math.max(maxChars, lastHighlightEnd);
  if (lineLength(line) <= effectiveMax) {
    return { line, ranges: highlight };
  }

  type Piece = { seg: Line[number]; srcIndex: number; srcFrom: number; srcTo: number };
  const pieces: Piece[] = [];
  let pos = 0;
  let trimmedEnd = false;
  for (let srcIndex = 0; srcIndex < line.length; srcIndex++) {
    const segment = line[srcIndex];
    const segLen = segment.kind === "text" ? segment.value.length : segment.label.length;
    const segStart = pos;
    pos += segLen;
    if (segStart >= effectiveMax) {
      trimmedEnd = true;
      continue;
    }
    if (segment.kind === "var") {
      pieces.push({ seg: segment, srcIndex, srcFrom: 0, srcTo: 0 });
      continue;
    }
    const to = Math.min(segment.value.length, effectiveMax - segStart);
    if (to < segment.value.length) {
      trimmedEnd = true;
    }
    pieces.push({ seg: { kind: "text", value: segment.value.slice(0, to) }, srcIndex, srcFrom: 0, srcTo: to });
  }

  if (trimmedEnd) {
    const last = pieces[pieces.length - 1];
    if (last && last.seg.kind === "text") {
      last.seg = { kind: "text", value: `${last.seg.value.replace(/\s+$/, "")} …` };
    } else {
      pieces.push({ seg: { kind: "text", value: " …" }, srcIndex: -1, srcFrom: 0, srcTo: 0 });
    }
  }

  const ranges: HighlightRange[] = [];
  for (let outIndex = 0; outIndex < pieces.length; outIndex++) {
    const piece = pieces[outIndex];
    if (piece.seg.kind !== "text" || piece.srcIndex < 0) {
      continue;
    }
    for (const range of highlight) {
      if (range.segmentIndex !== piece.srcIndex) {
        continue;
      }
      const overlapStart = Math.max(range.start, piece.srcFrom);
      const overlapEnd = Math.min(range.end, piece.srcTo);
      if (overlapStart < overlapEnd) {
        ranges.push({
          segmentIndex: outIndex,
          start: overlapStart - piece.srcFrom,
          end: overlapEnd - piece.srcFrom,
        });
      }
    }
  }

  return { line: pieces.map((piece) => piece.seg), ranges };
}
