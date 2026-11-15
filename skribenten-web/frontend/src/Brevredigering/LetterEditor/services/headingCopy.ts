import type React from "react";

const HEADING_TAGS = { TITLE1: "h1", TITLE2: "h2", TITLE3: "h3" } as const;

// Zero-width space (empty literals) and word joiner (between adjacent literals) are editor-only characters.
const visibleText = (text: string) => text.replaceAll(/[\u200B\u2060]/g, "").trim();

/**
 * Copying text inside a single literal only puts a styled span on the clipboard, so a copied heading would paste as
 * plain text. When the selection covers a whole heading, write it as a semantic heading instead. Partial selections
 * keep the native copy and paste as plain text, like in Word.
 */
export function copyWholeHeading(event: React.ClipboardEvent<HTMLElement>) {
  const selection = globalThis.getSelection();
  if (!selection || selection.rangeCount === 0 || selection.isCollapsed) return;

  const range = selection.getRangeAt(0);
  const block = blockOf(range.startContainer, event.currentTarget);
  if (!block || block !== blockOf(range.endContainer, event.currentTarget)) return;

  const tag = Object.entries(HEADING_TAGS).find(([type]) => block.classList.contains(type))?.[1];
  const text = visibleText(range.toString());
  if (!tag || text.length === 0 || text !== visibleText(block.textContent ?? "")) return;

  const heading = block.ownerDocument.createElement(tag);
  heading.textContent = text;
  event.preventDefault();
  event.clipboardData.setData("text/html", heading.outerHTML);
  event.clipboardData.setData("text/plain", text);
}

function blockOf(node: Node, root: HTMLElement): HTMLElement | undefined {
  let current: Node | null = node;
  while (current && current.parentNode !== root) current = current.parentNode;
  return current instanceof HTMLElement ? current : undefined;
}
