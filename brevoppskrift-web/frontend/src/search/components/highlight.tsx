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

/** Renders a {@link Line}, drawing variable segments as chips and wrapping matched
 * spans in `<mark>`. Pass either `ranges` (per-segment offsets, e.g. from a snippet
 * match) or `metaNeedle` (a literal substring highlighted on every text segment, used
 * for name/brevkode/title matches). */
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

/** Visible length of one line, counting variable chips by their label length. */
function lineLength(line: Line): number {
  return line.reduce(
    (sum, segment) => sum + (segment.kind === "text" ? segment.value.length : segment.label.length),
    0,
  );
}

/** Lowercased plain text of a line; variable chips contribute their label so that
 * indices line up with {@link clampLine}'s segment-based slicing. */
function linePlain(line: Line): string {
  return line
    .map((segment) => (segment.kind === "text" ? segment.value : segment.label))
    .join("")
    .toLowerCase();
}

/** Character index to center truncation on: the middle of the first occurrence of
 * `needle` in the line, or null when it is not found. */
export function indexOfNeedleCenter(line: Line, needle: string): number | null {
  if (!needle) {
    return null;
  }
  const idx = linePlain(line).indexOf(needle.toLowerCase());
  return idx < 0 ? null : idx + Math.floor(needle.length / 2);
}

/** Global character offset (counting variable labels) at the centre of the matched
 * span described by `ranges`, used to centre {@link clampLine} on the match. */
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

/** Trim a line to about `maxChars`, centered on `center` (or the head when null),
 * keeping variable chips atomic and adding "…" sentinels where text was removed.
 * `highlight` ranges (in the original line's coordinates) are remapped onto the
 * trimmed output so the full match — even when it spans several segments — stays
 * highlighted. */
export function clampLine(
  line: Line,
  center: number | null,
  maxChars: number,
  highlight: HighlightRange[],
): ClampedLine {
  if (lineLength(line) <= maxChars) {
    return { line, ranges: highlight };
  }
  const half = Math.floor(maxChars / 2);
  const windowStart = center == null ? 0 : Math.max(0, center - half);
  const windowEnd = windowStart + maxChars;

  type Piece = { seg: Line[number]; srcIndex: number; srcFrom: number; srcTo: number };
  const pieces: Piece[] = [];
  let pos = 0;
  let trimmedEnd = false;
  for (let srcIndex = 0; srcIndex < line.length; srcIndex++) {
    const segment = line[srcIndex];
    const segLen = segment.kind === "text" ? segment.value.length : segment.label.length;
    const segStart = pos;
    const segEnd = pos + segLen;
    pos = segEnd;
    if (segEnd <= windowStart || segStart >= windowEnd) {
      if (segStart >= windowEnd) {
        trimmedEnd = true;
      }
      continue;
    }
    if (segment.kind === "var") {
      pieces.push({ seg: segment, srcIndex, srcFrom: 0, srcTo: 0 });
      continue;
    }
    const from = Math.max(0, windowStart - segStart);
    const to = Math.min(segment.value.length, windowEnd - segStart);
    if (to < segment.value.length) {
      trimmedEnd = true;
    }
    pieces.push({ seg: { kind: "text", value: segment.value.slice(from, to) }, srcIndex, srcFrom: from, srcTo: to });
  }

  // Per-piece offset bookkeeping so highlight ranges can be remapped: `lead` is the
  // number of sentinel chars prepended to the piece's visible content; `strip` is the
  // number of leading whitespace chars removed.
  const leadByPiece = pieces.map(() => 0);
  const stripByPiece = pieces.map(() => 0);

  const trimmedStart = windowStart > 0;
  if (trimmedStart) {
    const first = pieces[0];
    if (first && first.seg.kind === "text") {
      const stripped = first.seg.value.replace(/^\s+/, "");
      stripByPiece[0] = first.seg.value.length - stripped.length;
      leadByPiece[0] = 2; // "… " prefix length
      first.seg = { kind: "text", value: `… ${stripped}` };
    } else {
      pieces.unshift({ seg: { kind: "text", value: "… " }, srcIndex: -1, srcFrom: 0, srcTo: 0 });
      leadByPiece.unshift(0);
      stripByPiece.unshift(0);
    }
  }
  if (trimmedEnd) {
    const last = pieces[pieces.length - 1];
    if (last && last.seg.kind === "text") {
      last.seg = { kind: "text", value: `${last.seg.value.replace(/\s+$/, "")} …` };
    } else {
      pieces.push({ seg: { kind: "text", value: " …" }, srcIndex: -1, srcFrom: 0, srcTo: 0 });
      leadByPiece.push(0);
      stripByPiece.push(0);
    }
  }

  const ranges: HighlightRange[] = [];
  for (let outIndex = 0; outIndex < pieces.length; outIndex++) {
    const piece = pieces[outIndex];
    if (piece.seg.kind !== "text" || piece.srcIndex < 0) {
      continue;
    }
    const strip = stripByPiece[outIndex];
    const lead = leadByPiece[outIndex];
    const contentStart = piece.srcFrom + strip;
    const contentEnd = piece.srcTo;
    for (const range of highlight) {
      if (range.segmentIndex !== piece.srcIndex) {
        continue;
      }
      const overlapStart = Math.max(range.start, contentStart);
      const overlapEnd = Math.min(range.end, contentEnd);
      if (overlapStart < overlapEnd) {
        ranges.push({
          segmentIndex: outIndex,
          start: lead + (overlapStart - piece.srcFrom - strip),
          end: lead + (overlapEnd - piece.srcFrom - strip),
        });
      }
    }
  }

  return { line: pieces.map((piece) => piece.seg), ranges };
}
