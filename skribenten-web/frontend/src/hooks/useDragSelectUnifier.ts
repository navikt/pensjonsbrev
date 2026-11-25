import { useEffect } from "react";

// Solves the browser's native limitation where a text selection
// cannot cross the boundary of separate contentEditable elements.
// It makes a group of individual editable spans behave like a single,
// continuous block of text during a drag-selection, and for as long as the
// resulting selection spans several of them.

export function useDragSelectUnifier<T extends HTMLElement>(host: T | null, enabled = true) {
  useEffect(() => {
    if (!host || !enabled) return;

    const isInsideHost = (n: Node | null) => !!n && host.contains(n);
    const literalOf = (n: Node) => (n instanceof Element ? n : n.parentElement)?.closest("[data-literal-index]");

    // Restoring contentEditable makes Chromium shrink such a selection to the literal it starts in.
    const hasSelectionAcrossLiterals = () => {
      const selection = document.getSelection();
      if (!selection || selection.rangeCount === 0 || selection.isCollapsed) return false;
      const range = selection.getRangeAt(0);
      return (
        (isInsideHost(range.startContainer) || isInsideHost(range.endContainer)) &&
        literalOf(range.startContainer) !== literalOf(range.endContainer)
      );
    };

    const isContextClick = (event: MouseEvent) =>
      event.button === 2 || (event.button === 0 && event.ctrlKey && /Mac/.test(navigator.platform));

    // Mark the host(div) as "unified" and remove contentEditable from all children
    const unify = () => {
      if (!host.isConnected) return;
      if (host.dataset.unified === "1") return;
      host.dataset.unified = "1";
      host.classList.add("is-drag-selecting");
      for (const el of host.querySelectorAll<HTMLElement>("[contenteditable]")) {
        const orig = el.getAttribute("contenteditable") ?? "true";
        el.setAttribute("data-ce-orig", orig);
        el.removeAttribute("contenteditable");
      }
    };

    // Restore contentEditable attributes to original values
    const restore = (force = false) => {
      if (!host.isConnected) return;
      if (host.dataset.unified !== "1") return;
      if (!force && hasSelectionAcrossLiterals()) return;
      host.classList.remove("is-drag-selecting");
      delete host.dataset.unified;
      for (const el of host.querySelectorAll<HTMLElement>("[data-ce-orig]")) {
        const orig = el.getAttribute("data-ce-orig");
        if (orig != null) el.setAttribute("contenteditable", orig);
        el.removeAttribute("data-ce-orig");
      }
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
      // Copying keeps the whole selection, while editing and navigating need editable literals.
      const isModifier = ["Shift", "Control", "Alt", "Meta", "CapsLock"].includes(event.key);
      const isCopy = (event.ctrlKey || event.metaKey) && event.key.toLowerCase() === "c";
      const extendsSelection = event.shiftKey && /^(Arrow|Home$|End$|Page)/.test(event.key);
      if (!isModifier && !isCopy && !extendsSelection) restore(true);
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
      pointerDownInside = isInsideHost(event.target as Node);
      dragStarted = false;
      if (!pointerDownInside) return;
      // A new click (but not a shift-click extending the selection) must land in editable text.
      if (event.button === 0 && !event.shiftKey && !isContextClick(event)) restore(true);
      pointerDownX = event.clientX;
      pointerDownY = event.clientY;
    };

    // A context click would focus the clicked literal, which places the caret there and drops the selection.
    const onMouseDown = (event: MouseEvent) => {
      if (isContextClick(event) && hasSelectionAcrossLiterals()) event.preventDefault();
    };

    const onPointerMove = (event: PointerEvent) => {
      // pointerup can be lost (e.g. macOS' native context menu swallows it), so only drag while the primary button is held.
      if ((event.buttons & 1) === 0) pointerDownInside = false;
      if (!pointerDownInside) return;
      maybeStartDrag(event.clientX, event.clientY);
    };

    // Fallback: if a non-collapsed selection appears inside host (Safari/touch),
    // ensure we're unified.
    const onSelectionChange = () => {
      const selection = document.getSelection?.();
      if (!selection || selection.rangeCount === 0 || selection.isCollapsed) {
        restore();
        return;
      }
      // Skip unify if selection changed to cover a full fritekst by tab/focus
      if (
        selection.anchorNode === selection.focusNode &&
        selection.anchorOffset === 0 &&
        selection.focusOffset === selection.focusNode?.textContent?.length
      ) {
        return;
      }
      // Ensures unification when selection starts outside host
      if (isInsideHost(selection.anchorNode) || isInsideHost(selection.focusNode)) {
        unify();
      } else {
        restore();
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
    host.addEventListener("mousedown", onMouseDown, true);
    host.addEventListener("pointermove", onPointerMove, true);
    host.addEventListener("keydown", onKeyDown, true);
    host.addEventListener("keyup", onKeyUp, true);
    document.addEventListener("pointerup", onPointerUp, true);
    document.addEventListener("pointercancel", onPointerCancel, true);
    document.addEventListener("visibilitychange", onVisibilityChange);
    window.addEventListener("blur", onWindowBlur);

    return () => {
      restore(true);
      document.removeEventListener("selectionchange", onSelectionChange);
      host.removeEventListener("pointerdown", onPointerDown, true);
      host.removeEventListener("mousedown", onMouseDown, true);
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
