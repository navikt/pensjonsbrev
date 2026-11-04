import { type RefObject, useEffect } from "react";

/**
 * A block the user picked on screen, which is already in view: the next
 * scroll to it is skipped. Cleared when used. */
export type SkipScroll = { current: number | undefined };

/* Scrolls block `index` into view inside `container`. A block can render as
 * several elements (one per branch of a conditional), so this scrolls to the
 * first branch containing a marked literal, or the first element if none has
 * one. Runs again when `trigger` or `scrollKey` changes, e.g. a new template,
 * language or search. A block in `skip` is not scrolled to.
 */
export function useScrollToBlock(
  container: RefObject<HTMLElement | null>,
  index: number | undefined,
  trigger?: unknown,
  skip?: SkipScroll,
  scrollKey?: unknown,
): void {
  // biome-ignore lint/correctness/useExhaustiveDependencies: `trigger` and `scrollKey` re-run scrolling when its target changes.
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
      const blocks = root.querySelectorAll(`[data-block-index="${CSS.escape(String(index))}"]`);
      const block = [...blocks].find((candidate) => candidate.querySelector("mark")) ?? blocks[0];
      block?.scrollIntoView?.({ behavior: "smooth", block: "center" });
    });
    return () => cancelAnimationFrame(frame);
  }, [container, index, trigger, skip, scrollKey]);
}
