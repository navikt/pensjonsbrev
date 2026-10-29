import { type RefObject, useEffect } from "react";

/**
 * Scrolls block `index` into view inside `container`. A block can render as
 * several elements (one per branch of a conditional), so this scrolls to the
 * first. Runs again when `trigger` changes, e.g. a new template or language
 * renders the same index elsewhere.
 */
export function useScrollToBlock(
  container: RefObject<HTMLElement | null>,
  index: number | undefined,
  trigger?: unknown,
): void {
  // biome-ignore lint/correctness/useExhaustiveDependencies: `trigger` is only there to re-run when the content behind the same index changes.
  useEffect(() => {
    const root = container.current;
    if (!root || index === undefined) {
      return;
    }
    const frame = requestAnimationFrame(() => {
      root
        .querySelector(`[data-block-index="${CSS.escape(String(index))}"]`)
        ?.scrollIntoView?.({ behavior: "smooth", block: "center" });
    });
    return () => cancelAnimationFrame(frame);
  }, [container, index, trigger]);
}
