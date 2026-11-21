import type React from "react";

// Numbered like the editor's "Overskrift 1–3", which is also how pasting maps h1–h3 back to title blocks.
const CLIPBOARD_HEADING_TAGS = { TITLE1: "h1", TITLE2: "h2", TITLE3: "h3" } as const;
const FONT_TAGS: Partial<Record<string, "b" | "i">> = { BOLD: "b", ITALIC: "i" };
const INLINE_TAGS = new Set(["SPAN", "B", "I", "BR"]);
const CELL_OR_ITEM_TAGS = new Set(["TD", "TH", "LI"]);
const TABLE_AND_LIST_TAGS = new Set(["TABLE", "THEAD", "TBODY", "TR", "UL", "OL"]);
const KEPT_ATTRIBUTES = new Set(["rowspan", "colspan"]);
// Editor-only markup: action buttons, word joiners and attestant diff decorations. Variables stay, as plain text.
const NOT_LETTER_CONTENT = '[contenteditable="false"]:not([data-variable]), [data-diff-deletion]';
// Zero-width space (empty literals) and word joiner (between adjacent literals) are editor-only characters.
const EDITOR_ONLY_CHARACTERS = /[\u200B\u2060]/g;

/**
 * The editor only carries block types and bold/italic as classes and attributes, so the browser's own copy loses
 * them. Writes semantic html instead, so pasting into Skribenten or other applications keeps headings, paragraphs,
 * lists, tables and formatting. Part of a heading copies as plain text, like in Word.
 *
 * Returns false, leaving the browser's own copy, when the selection isn't inside `root`.
 */
export function writeSemanticClipboard(event: React.ClipboardEvent<HTMLElement>, root: HTMLElement): boolean {
  const selection = globalThis.getSelection();
  if (!selection || selection.rangeCount === 0 || selection.isCollapsed) return false;

  const range = selection.getRangeAt(0);
  if (!root.contains(range.commonAncestorContainer)) return false;

  const clipboard = root.ownerDocument.createElement("div");
  clipboard.append(range.cloneContents());
  wrapInSelectedAncestors(clipboard, range, root);
  toSemanticHtml(clipboard);

  event.clipboardData.setData("text/html", clipboard.innerHTML);
  event.clipboardData.setData("text/plain", toPlainText(clipboard).trim());
  return true;
}

/**
 * The browser clones a selection without the elements around it, so add back those that matter: formatting, a whole
 * heading, and the table or list around several selected cells or items. Text within one cell or item stays text.
 */
function wrapInSelectedAncestors(clipboard: HTMLElement, range: Range, root: HTMLElement) {
  let isWithinOneCellOrItem = false;
  for (let node: Node | null = range.commonAncestorContainer; node && node !== root; node = node.parentNode) {
    if (!(node instanceof HTMLElement)) continue;
    if (CELL_OR_ITEM_TAGS.has(node.tagName)) isWithinOneCellOrItem = true;

    const isFormattedText = node.dataset.fontType !== undefined;
    const isWholeHeading = node.parentNode === root && headingTagOf(node) !== undefined && isFullySelected(range, node);
    const isTableOrListAroundSelection = !isWithinOneCellOrItem && TABLE_AND_LIST_TAGS.has(node.tagName);
    if (isFormattedText || isWholeHeading || isTableOrListAroundSelection) {
      const wrapper = node.cloneNode(false) as HTMLElement;
      wrapper.append(...clipboard.childNodes);
      clipboard.append(wrapper);
    }
  }
}

function toSemanticHtml(clipboard: HTMLElement) {
  const document = clipboard.ownerDocument;

  for (const element of clipboard.querySelectorAll(NOT_LETTER_CONTENT)) element.remove();

  for (const text of clipboard.querySelectorAll<HTMLElement>("[data-font-type]")) {
    const tag = FONT_TAGS[text.dataset.fontType ?? ""];
    if (tag) {
      const formatted = document.createElement(tag);
      formatted.append(...text.childNodes);
      text.append(formatted);
    }
  }

  for (const block of clipboard.querySelectorAll<HTMLElement>(":scope > div")) {
    const headingTag = headingTagOf(block);
    if (headingTag) {
      const heading = document.createElement(headingTag);
      heading.append(...block.childNodes);
      block.replaceWith(heading);
    } else if (block.classList.contains("PARAGRAPH")) {
      block.replaceWith(...splitIntoParagraphs(block));
    }
  }

  for (const element of clipboard.querySelectorAll("*")) {
    for (const { name } of [...element.attributes]) {
      if (!KEPT_ATTRIBUTES.has(name)) element.removeAttribute(name);
    }
  }
  for (const span of clipboard.querySelectorAll("span")) span.replaceWith(...span.childNodes);

  const textNodes = document.createTreeWalker(clipboard, NodeFilter.SHOW_TEXT);
  while (textNodes.nextNode()) {
    textNodes.currentNode.textContent = (textNodes.currentNode.textContent ?? "").replaceAll(
      EDITOR_ONLY_CHARACTERS,
      "",
    );
  }
}

/** A paragraph block can also hold lists and tables, which can't be nested in <p>. */
function splitIntoParagraphs(block: HTMLElement): Node[] {
  const pieces: Node[] = [];
  let paragraph: HTMLParagraphElement | undefined;
  for (const child of [...block.childNodes]) {
    if (child.nodeType === Node.TEXT_NODE || INLINE_TAGS.has((child as Element).tagName)) {
      if (!paragraph) {
        paragraph = block.ownerDocument.createElement("p");
        pieces.push(paragraph);
      }
      paragraph.append(child);
    } else {
      paragraph = undefined;
      pieces.push(child);
    }
  }
  return pieces;
}

/** Plain text with a line per block, list item, table row and line break, and tabs between table cells. */
function toPlainText(node: Node): string {
  if (node.nodeType === Node.TEXT_NODE) return node.textContent ?? "";
  if (!(node instanceof Element)) return "";
  if (node.tagName === "BR") return "\n";

  const inner = Array.from(node.childNodes, toPlainText).join("");
  switch (node.tagName) {
    case "TD":
    case "TH": {
      return node.nextElementSibling ? `${inner}\t` : inner;
    }
    case "P":
    case "H1":
    case "H2":
    case "H3":
    case "LI":
    case "TR":
    case "UL":
    case "OL":
    case "TABLE": {
      return inner.endsWith("\n") ? inner : `${inner}\n`;
    }
    default: {
      return inner;
    }
  }
}

function headingTagOf(block: HTMLElement) {
  return Object.entries(CLIPBOARD_HEADING_TAGS).find(([type]) => block.classList.contains(type))?.[1];
}

function isFullySelected(range: Range, block: HTMLElement) {
  // Compare against the letter content only, since the block can also hold action controls (e.g. "Behold"/"Slett").
  const texts = Array.from(block.querySelectorAll("[data-literal], [data-variable]"), (text) => text.textContent ?? "");
  const selected = range.toString().replaceAll(EDITOR_ONLY_CHARACTERS, "").trim();
  return selected.length > 0 && selected === texts.join("").replaceAll(EDITOR_ONLY_CHARACTERS, "").trim();
}
