import { useEffect, useRef } from "react";

/**
 * Scrolls the selected result row (`[data-selected-result]`) into view, once
 * per selection: on coming back to the list, after a refresh, or from a shared
 * link. Later re-renders of the same selection leave the scroll position to
 * the user. Waits until the row has rendered, so it is fine to call while the
 * search is still running.
 */
export function useScrollToSelectedResult(selected: string | undefined, rendered: unknown): void {
  const scrolledTo = useRef<string | undefined>(undefined);
  // biome-ignore lint/correctness/useExhaustiveDependencies: `rendered` only signals that the rows may have changed.
  useEffect(() => {
    if (!selected || scrolledTo.current === selected) {
      return;
    }
    const row = document.querySelector("[data-selected-result]");
    if (row) {
      scrolledTo.current = selected;
      row.scrollIntoView?.({ block: "center" });
    }
  }, [selected, rendered]);
}
