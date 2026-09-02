import { type DiffSegment } from "./diffModel";

// Diff segments are rendered directly into contentEditable elements so deleted text can remain visible
// without becoming part of the editable letter content.

// Standard parameters for the 32-bit FNV-1a hash algorithm.
const FNV_1A_OFFSET_BASIS = 0x811c9dc5;
const FNV_1A_PRIME = 0x01000193;

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

export const diffSegmentSignature = (segments: DiffSegment[]) => {
  let hash = FNV_1A_OFFSET_BASIS;

  for (const segment of segments) {
    const value = `${segment.type.length}:${segment.type}${segment.text.length}:${segment.text}`;
    for (let index = 0; index < value.length; index++) {
      hash ^= value.charCodeAt(index);
      hash = Math.imul(hash, FNV_1A_PRIME);
    }
  }

  return (hash >>> 0).toString(16).padStart(8, "0");
};

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
