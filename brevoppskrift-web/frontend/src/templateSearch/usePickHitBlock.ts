import { type RefObject, useEffect, useRef } from "react";

import { hitBlockOf } from "~/templateSearch/SearchHighlightContext";

/**
 * Calls `onPick` with the search hit block the user clicks in, or moves
 * keyboard focus into (e.g. tabbing to an expression inside it). The blocks
 * themselves are not made focusable, so the tab order stays as it is.
 *
 * Listens on `container` rather than on each block, since the blocks are
 * rendered by the search-agnostic documentation view.
 */
export function usePickHitBlock(container: RefObject<HTMLElement | null>, onPick: (block: number) => void): void {
  const latestOnPick = useRef(onPick);
  latestOnPick.current = onPick;

  useEffect(() => {
    const root = container.current;
    if (!root) {
      return;
    }
    const pick = (event: Event) => {
      const block = hitBlockOf(event.target);
      if (block !== undefined) {
        latestOnPick.current(block);
      }
    };
    root.addEventListener("click", pick);
    root.addEventListener("focusin", pick);
    return () => {
      root.removeEventListener("click", pick);
      root.removeEventListener("focusin", pick);
    };
  }, [container]);
}
