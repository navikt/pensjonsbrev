import { type RefObject, useEffect } from "react";

/** A block the user picked on screen, which is already in view: the next
 *  scroll to it is skipped. Cleared when used. */
export type SkipScroll = { current: number | undefined };

/**
 * Scrolls block `index` into view inside `container`. A block can render as
 * several elements (one per branch of a conditional), so this scrolls to the
 * first. Runs again when `trigger` changes, e.g. a new template or language
 * renders the same index elsewhere. A block in `skip` is not scrolled to.
 */
export function useScrollToBlock(
  container: RefObject<HTMLElement | null>,
  index: number | undefined,
  trigger?: unknown,
  skip?: SkipScroll,
): void {
  // biome-ignore lint/correctness/useExhaustiveDependencies: `trigger` is only there to re-run when the content behind the same index changes.
  useEffect(() => {
    const root = container.current;
    if (!root || index === undefined) {
      return;
    }
    if (skip && skip.current === index) {
      skip.current = undefined;
      return;
    }
    const frame = requestAnimationFrame(() => {
      root
        .querySelector(`[data-block-index="${CSS.escape(String(index))}"]`)
        ?.scrollIntoView?.({ behavior: "smooth", block: "center" });
    });
    return () => cancelAnimationFrame(frame);
  }, [container, index, trigger, skip]);
}
