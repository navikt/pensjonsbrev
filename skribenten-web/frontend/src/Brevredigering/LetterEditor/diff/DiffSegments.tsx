import { css } from "@emotion/react";

import { type DiffSegment } from "./diffModel";

const insertedStyle = css({
  color: "var(--ax-text-meta-lime)",
  fontWeight: 600,
  borderRadius: "2px",
  background: "var(--ax-bg-meta-lime-moderateA)",
});

const deletedStyle = css({
  color: "var(--ax-text-danger)",
  fontWeight: 600,
  borderRadius: "2px",
  background: "var(--ax-bg-danger-moderate-pressedA)",
  textDecorationLine: "line-through",
  userSelect: "none",
});

export function DiffSegments({ segments }: { segments: DiffSegment[] }) {
  return (
    <>
      {segments.map((segment, i) => {
        switch (segment.type) {
          case "unchanged":
            return <span key={i}>{segment.text}</span>;
          case "inserted":
            return (
              <span css={insertedStyle} data-diff-insertion key={i}>
                {segment.text}
              </span>
            );
          case "deleted":
            return (
              <span contentEditable={false} css={deletedStyle} data-diff-deletion key={i}>
                {segment.text}
              </span>
            );
        }
      })}
    </>
  );
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
