import { css, keyframes } from "@emotion/react";
import { type CSSProperties, type ReactNode } from "react";

// Dash lengths are in units of the rect's `pathLength` (100 = one full lap).
const LAP_MS = 1600;
const HEAD_LENGTH = 4;
const TAIL_SEGMENT_LENGTH = 3;
const TAIL_OPACITIES = [0.8, 0.6, 0.45, 0.3, 0.18, 0.08];
const TAIL_LENGTH = TAIL_OPACITIES.length * TAIL_SEGMENT_LENGTH;

function segmentStyle(start: number, length: number, opacity: number): CSSProperties {
  return {
    opacity,
    strokeDasharray: `${length} ${100 - length}`,
    // A negative delay starts the dash part-way through the lap, placing it
    // `start` units along the path.
    animationDelay: `${-(start / 100) * LAP_MS}ms`,
  };
}

// SVG strokes can't carry a gradient along their path, so the comet's tail is
// drawn as adjacent dashes of falling opacity behind a solid head.
const SEGMENT_STYLES: CSSProperties[] = [
  segmentStyle(TAIL_LENGTH, HEAD_LENGTH, 1),
  ...TAIL_OPACITIES.map((opacity, i) =>
    segmentStyle(TAIL_LENGTH - (i + 1) * TAIL_SEGMENT_LENGTH, TAIL_SEGMENT_LENGTH, opacity),
  ),
];

// A rect's path starts at its top-left corner and runs clockwise, and a
// falling dash offset moves the dashes forward along it.
const travel = keyframes`
  from { stroke-dashoffset: 0; }
  to { stroke-dashoffset: -100; }
`;

const fadeIn = keyframes`
  from { opacity: 0; }
  to { opacity: 1; }
`;

const wrapper = css`
  position: relative;
  isolation: isolate;
`;

const overlay = css`
  position: absolute;
  inset: 0;
  z-index: 2;
  width: 100%;
  height: 100%;
  overflow: visible;
  pointer-events: none;

  /* The delay keeps searches that finish almost instantly from flashing. */
  animation: ${fadeIn} 150ms ease-out 50ms both;

  rect {
    /* Centred on the input's 1px border, matching its 8px corner radius. */
    width: calc(100% - 2px);
    height: calc(100% - 2px);
    rx: 7px;
    fill: none;
    stroke: var(--ax-border-accent);
    stroke-width: 2px;
  }

  .segment {
    animation: ${travel} ${LAP_MS}ms linear infinite;
  }

  .still {
    display: none;
  }

  @media (prefers-reduced-motion: reduce) {
    .segment {
      display: none;
    }

    .still {
      display: inline;
    }
  }
`;

/**
 * Draws a comet travelling clockwise around the search field while `active`.
 *
 * Assumes `children` is an Aksel `Search` whose box is exactly the input: a
 * hidden label, and no description or error message. Anything else rendered in
 * the field's box would push the outline off the input's border.
 *
 * Nothing is rendered while inactive, so an idle page runs no animation.
 */
export function SearchActivityOutline({ active, children }: { active: boolean; children: ReactNode }) {
  return (
    <div css={wrapper}>
      {children}
      {active ? (
        <svg aria-hidden css={overlay} data-testid="search-activity" focusable="false">
          {SEGMENT_STYLES.map((style, i) => (
            <rect className="segment" key={i} pathLength={100} style={style} x={1} y={1} />
          ))}
          <rect className="still" x={1} y={1} />
        </svg>
      ) : null}
    </div>
  );
}
