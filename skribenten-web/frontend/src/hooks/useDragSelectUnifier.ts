import { useEffect } from "react";

// Solves the browser's native limitation where a text selection
// cannot cross the boundary of separate contentEditable elements.
// It makes a group of individual editable spans behave like a single,
// continuous block of text, but only during a drag-selection.

export function useDragSelectUnifier<T extends HTMLElement>(host: T | null, enabled = true) {
  useEffect(() => {
    if (!host || !enabled) return;

    const isInside = (n: Node | null) => !!n && host.contains(n);

    // Mark the host(div) as "unified" and remove contentEditable from all children
    const unify = () => {
      if (!host.isConnected) return;
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
      if (!host.isConnected) return;
      if (host.dataset.unified !== "1") return;
      host.classList.remove("is-drag-selecting");
      delete host.dataset.unified;
      host.querySelectorAll<HTMLElement>("[data-ce-orig]").forEach((el) => {
        const orig = el.getAttribute("data-ce-orig");
        if (orig != null) el.setAttribute("contenteditable", orig);
        el.removeAttribute("data-ce-orig");
      });
    };

    let pointerDownInside = false;
    let dragStarted = false;
    let selectingByKeys = false;
    let pointerDownX = 0;
    let pointerDownY = 0;

    const maybeStartDrag = (clientX: number, clientY: number) => {
      if (!pointerDownInside || dragStarted) return;
      const dx = Math.abs(clientX - pointerDownX);
      const dy = Math.abs(clientY - pointerDownY);
      if (dx + dy < 3) return; // only unify after an actual drag
      dragStarted = true;
      unify();
    };

    // selection by keyboard?
    const onKeyDown = (event: KeyboardEvent) => {
      if (event.key === "Shift") {
        selectingByKeys = true;
      }
    };

    const onKeyUp = (event: KeyboardEvent) => {
      if (
        selectingByKeys &&
        !["ArrowRight", "ArrowDown", "ArrowLeft", "ArrowUp", "Control", "Alt", "Meta", "CapsLock"].includes(event.key)
      ) {
        selectingByKeys = false;
        restore();
      }
    };

    // selection by mouse?
    const onPointerDown = (event: PointerEvent) => {
      pointerDownInside = isInside(event.target as Node);
      dragStarted = false;
      if (!pointerDownInside) return;
      pointerDownX = event.clientX;
      pointerDownY = event.clientY;
    };

    const onPointerMove = (event: PointerEvent) => {
      if (!pointerDownInside) return;
      maybeStartDrag(event.clientX, event.clientY);
    };

    // Fallback: if a non-collapsed selection appears inside host (Safari/touch),
    // ensure we're unified.
    const onSelectionChange = () => {
      const selection = document.getSelection?.();
      if (!selection || selection.rangeCount === 0) return;
      if (selection.isCollapsed) {
        restore();
        return;
      }
      if (isInside(selection.anchorNode) || isInside(selection.focusNode)) {
        unify();
      }
    };

    const onPointerUp = () => {
      pointerDownInside = false;
      dragStarted = false;
      restore();
    };

    const onPointerCancel = () => restore();
    const onWindowBlur = () => restore();
    const onVisibilityChange = () => {
      if (document.visibilityState === "hidden") restore();
    };

    // use capture to beat other listeners that might interfere
    document.addEventListener("selectionchange", onSelectionChange);
    host.addEventListener("pointerdown", onPointerDown, true);
    host.addEventListener("pointermove", onPointerMove, true);
    host.addEventListener("keydown", onKeyDown, true);
    host.addEventListener("keyup", onKeyUp, true);
    document.addEventListener("pointerup", onPointerUp, true);
    document.addEventListener("pointercancel", onPointerCancel, true);
    document.addEventListener("visibilitychange", onVisibilityChange);
    window.addEventListener("blur", onWindowBlur);

    return () => {
      restore();
      document.removeEventListener("selectionchange", onSelectionChange);
      host.removeEventListener("pointerdown", onPointerDown, true);
      host.removeEventListener("pointermove", onPointerMove, true);
      host.removeEventListener("keydown", onKeyDown, true);
      host.removeEventListener("keyup", onKeyUp, true);
      document.removeEventListener("pointerup", onPointerUp, true);
      document.removeEventListener("pointercancel", onPointerCancel, true);
      document.removeEventListener("visibilitychange", onVisibilityChange);
      window.removeEventListener("blur", onWindowBlur);
    };
  }, [enabled, host]);
}
