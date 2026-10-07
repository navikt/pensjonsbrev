import { css, keyframes } from "@emotion/react";

// Each cycle starts on three dots and lingers there, then counts up from
// nothing: three (linger), none, one, two, back to three.
const STEP_MS = 300;
const LINGER_MS = 800;
const CYCLE_MS = LINGER_MS + 3 * STEP_MS;

const percentAt = (ms: number) => `${(ms / CYCLE_MS) * 100}%`;

/** The nth dot (1-3) disappears when the lingering ends and comes back once
 *  the count reaches it. With `step-end`, opacity holds until the next
 *  keyframe; the third dot's return is the start of the next cycle. */
function dotKeyframes(n: number) {
  return keyframes`
    0% { opacity: 1; }
    ${percentAt(LINGER_MS)} { opacity: 0; }
    ${percentAt(LINGER_MS + n * STEP_MS)}, 100% { opacity: ${n < 3 ? 1 : 0}; }
  `;
}

// Hidden dots keep their space, so the text around them never shifts.
const dots = css`
  span:nth-of-type(1) {
    animation: ${dotKeyframes(1)} ${CYCLE_MS}ms step-end infinite;
  }

  span:nth-of-type(2) {
    animation: ${dotKeyframes(2)} ${CYCLE_MS}ms step-end infinite;
  }

  span:nth-of-type(3) {
    animation: ${dotKeyframes(3)} ${CYCLE_MS}ms step-end infinite;
  }

  @media (prefers-reduced-motion: reduce) {
    span {
      animation: none;
    }
  }
`;

/** Trailing dots that show three and linger, then count 0-1-2-3, on repeat, to
 *  show that work is ongoing. Decorative only: assistive technology reads the
 *  text without them. Under reduced motion all three dots stand still. */
export function AnimatedEllipsis() {
  return (
    <span aria-hidden css={dots}>
      <span>.</span>
      <span>.</span>
      <span>.</span>
    </span>
  );
}
