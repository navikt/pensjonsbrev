import { describe, expect, it } from "vitest";

import {
  diffSegmentSignature,
  getEditableCharacterOffset,
  getEditableLiteralText,
  renderDiffSegments,
  renderPlainText,
} from "~/Brevredigering/LetterEditor/diff/DiffSegments";
import { type DiffSegment } from "~/Brevredigering/LetterEditor/diff/diffModel";

const segments: DiffSegment[] = [
  { type: "unchanged", text: "Før " },
  { type: "deleted", text: "gammel " },
  { type: "inserted", text: "ny " },
  { type: "unchanged", text: "tekst" },
];

describe("DiffSegments DOM behavior", () => {
  it("renders class-based markers and restores plain editable text", () => {
    const element = document.createElement("span");
    element.contentEditable = "true";

    renderDiffSegments(element, segments, `hash:key:${diffSegmentSignature(segments)}`);

    expect(element.querySelector("[data-diff-insertion]")?.className).toBe("attestant-diff-inserted");
    expect(element.querySelector("[data-diff-deletion]")?.className).toBe("attestant-diff-deleted");
    expect((element.querySelector("[data-diff-deletion]") as HTMLElement).contentEditable).toBe("false");
    expect(getEditableLiteralText(element)).toBe("Før ny tekst");

    renderPlainText(element, "Før ny tekst");
    expect(element.textContent).toBe("Før ny tekst");
    expect(element.querySelector("[data-diff-deletion],[data-diff-insertion]")).toBeNull();
  });

  it("calculates the editable caret offset without deleted text", () => {
    const element = document.createElement("span");
    element.contentEditable = "true";
    document.body.appendChild(element);
    renderDiffSegments(element, segments, `hash:key:${diffSegmentSignature(segments)}`);

    const finalTextNode = element.lastChild?.firstChild;
    if (!finalTextNode) throw new Error("Expected final text node");
    const range = document.createRange();
    range.setStart(finalTextNode, 2);
    range.collapse(true);
    const selection = globalThis.getSelection();
    selection?.removeAllRanges();
    selection?.addRange(range);

    expect(getEditableCharacterOffset(element)).toBe("Før ny ".length + 2);

    selection?.removeAllRanges();
    element.remove();
  });

  it("changes the render signature when segment content changes", () => {
    expect(diffSegmentSignature(segments)).not.toBe(
      diffSegmentSignature(
        segments.map((segment) => (segment.type === "inserted" ? { ...segment, text: "annen " } : segment)),
      ),
    );
  });
});
