import { type DiffSegment } from "./diffModel";

function createSegmentNode(segment: DiffSegment): HTMLSpanElement {
  const span = document.createElement("span");
  span.textContent = segment.text;

  if (segment.type === "inserted") {
    span.dataset.diffInsertion = "";
    span.className = "attestant-diff-inserted";
  }

  if (segment.type === "deleted") {
    span.dataset.diffDeletion = "";
    span.contentEditable = "false";
    span.className = "attestant-diff-deleted";
  }

  return span;
}

export const diffSegmentSignature = (segments: DiffSegment[]) => JSON.stringify(segments);

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
  for (const node of tempDiv.querySelectorAll("[data-diff-deletion]")) {
    node.remove();
  }

  return tempDiv.textContent?.length ?? 0;
}
