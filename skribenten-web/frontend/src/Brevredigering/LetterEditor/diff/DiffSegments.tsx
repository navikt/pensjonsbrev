import { type DiffSegment } from "./diffModel";

function createSegmentNode(segment: DiffSegment): HTMLSpanElement {
  const span = document.createElement("span");
  span.textContent = segment.text;

  if (segment.type === "inserted") {
    span.dataset.diffInsertion = "";
    span.style.color = "var(--ax-text-meta-lime)";
    span.style.fontWeight = "600";
    span.style.borderRadius = "2px";
    span.style.background = "var(--ax-bg-meta-lime-moderateA)";
  }

  if (segment.type === "deleted") {
    span.dataset.diffDeletion = "";
    span.contentEditable = "false";
    span.style.color = "var(--ax-text-danger)";
    span.style.fontWeight = "600";
    span.style.borderRadius = "2px";
    span.style.background = "var(--ax-bg-danger-moderate-pressedA)";
    span.style.textDecorationLine = "line-through";
    span.style.userSelect = "none";
  }

  return span;
}

export function renderDiffSegments(element: HTMLElement, segments: DiffSegment[], diffVersion: string) {
  if (element.dataset.diffVersion === diffVersion) return;
  element.replaceChildren(...segments.map(createSegmentNode));
  element.dataset.diffVersion = diffVersion;
}

export function renderPlainText(element: HTMLElement, text: string) {
  if (element.dataset.diffVersion !== undefined) {
    delete element.dataset.diffVersion;
  }
  if (element.textContent !== text || element.querySelector("[data-diff-deletion],[data-diff-insertion]")) {
    element.textContent = text;
  }
}

export function getEditableLiteralText(element: HTMLElement): string {
  const clone = element.cloneNode(true) as HTMLElement;
  clone.querySelectorAll("[data-diff-deletion]").forEach((node) => node.remove());
  return clone.textContent ?? "";
}

export function getEditableCharacterOffset(element: HTMLElement): number {
  const selection = globalThis.getSelection();
  if (!selection || selection.rangeCount === 0) return 0;

  const range = selection.getRangeAt(0);
  if (!range.startContainer || !element.contains(range.startContainer)) return 0;

  const preCaretRange = range.cloneRange();
  preCaretRange.selectNodeContents(element);
  preCaretRange.setEnd(range.startContainer, range.startOffset);

  const tempDiv = document.createElement("div");
  tempDiv.appendChild(preCaretRange.cloneContents());
  tempDiv.querySelectorAll("[data-diff-deletion]").forEach((node) => node.remove());

  return tempDiv.textContent?.length ?? 0;
}
