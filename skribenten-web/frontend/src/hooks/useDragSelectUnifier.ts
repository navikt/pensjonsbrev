import { useEffect } from "react";

// Solves the browser's native limitation where a text selection
// cannot cross the boundary of separate contentEditable elements.
// It makes a group of individual editable spans behave like a single,
// continuous block of text, but only during a drag-selection.

export function useDragSelectUnifier<T extends HTMLElement>(hostRef: React.RefObject<T | null>, enabled = true) {
  const host = hostRef.current;

  useEffect(() => {
    if (!host || !enabled) return;

    const isInside = (n: Node | null) => !!n && host.contains(n);

    // Mark the host(div) as "unified" and remove contentEditable from all children
    const unify = () => {
      if (host.dataset.unified === "1") return;
      host.dataset.unified = "1";
      host.classList.add("is-drag-selecting");
      host.querySelectorAll<HTMLElement>("[contenteditable]").forEach((el) => {
        const orig = el.getAttribute("contenteditable") ?? "true";
        el.setAttribute("data-ce-orig", orig);
        el.removeAttribute("contenteditable");
      });
    };

    // Restore contentEditable attributes to original values
    const restore = () => {
      if (host.dataset.unified !== "1") return;
      host.classList.remove("is-drag-selecting");
      delete host.dataset.unified;
      host.querySelectorAll<HTMLElement>("[data-ce-orig]").forEach((el) => {
        const orig = el.getAttribute("data-ce-orig");
        if (orig != null) el.setAttribute("contenteditable", orig);
        el.removeAttribute("data-ce-orig");
      });
    };

    // Start unifying when selection actually begins inside the host(div)
    const onSelectStart = (e: Event) => {
      if (isInside(e.target as Node)) unify();
    };

    // Fallback: if a non-collapsed selection appears inside host (Safari/touch),
    // ensure we're unified.
    const onSelectionChange = () => {
      const sel = document.getSelection?.();
      if (!sel || sel.rangeCount === 0) return;
      const collapsed = sel.isCollapsed || sel.type === "Caret";
      if (collapsed) return;
      if (isInside(sel.anchorNode) || isInside(sel.focusNode)) unify();
    };

    const onPointerUp = () => restore();
    const onPointerCancel = () => restore();
    const onWindowBlur = () => restore();

    // use capture to beat other listeners that might interfere
    host.addEventListener("selectstart", onSelectStart, true);
    document.addEventListener("selectionchange", onSelectionChange);
    document.addEventListener("pointerup", onPointerUp, true);
    document.addEventListener("pointercancel", onPointerCancel, true);
    window.addEventListener("blur", onWindowBlur);

    return () => {
      host.removeEventListener("selectstart", onSelectStart, true);
      document.removeEventListener("selectionchange", onSelectionChange);
      document.removeEventListener("pointerup", onPointerUp, true);
      document.removeEventListener("pointercancel", onPointerCancel, true);
      window.removeEventListener("blur", onWindowBlur);
    };
  }, [enabled, host]);
}
