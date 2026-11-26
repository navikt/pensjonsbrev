import type React from "react";

// Numbered like the editor's "Overskrift 1–3", which is also how pasting maps h1–h3 back to title blocks.
const CLIPBOARD_HEADING_TAGS = { TITLE1: "h1", TITLE2: "h2", TITLE3: "h3" } as const;
const FONT_TAGS: Partial<Record<string, "b" | "i">> = { BOLD: "b", ITALIC: "i" };
const INLINE_TAGS = new Set(["SPAN", "B", "I", "BR"]);
const KEPT_ATTRIBUTES = new Set(["rowspan", "colspan"]);
const ANCESTOR_WRAPPERS: Partial<Record<string, string>> = {
  UL: ":scope > li",
  OL: ":scope > li",
  TR: ":scope > :is(td, th)",
  THEAD: ":scope > tr",
  TBODY: ":scope > tr",
  TFOOT: ":scope > tr",
  TABLE: ":scope > :is(thead, tbody, tfoot, tr)",
};
// Zero-width space (empty literals) and word joiner (between adjacent literals) are editor-only characters.
const EDITOR_ONLY_CHARACTERS = /[\u200B\u2060]/g;

/**
 * The editor only carries block types and bold/italic as classes and styles, so the browser's own copy loses them.
 * Writes semantic html instead, so pasting into Skribenten or other applications keeps headings, paragraphs and
 * formatting. Part of a heading copies as plain text, like in Word.
 */
export function copyAsSemanticHtml(event: React.ClipboardEvent<HTMLElement>) {
  const selection = globalThis.getSelection();
  if (!selection || selection.rangeCount === 0 || selection.isCollapsed) return;

  const root = event.currentTarget;
  const range = selection.getRangeAt(0);
  if (!root.contains(range.commonAncestorContainer)) return;

  const clipboard = root.ownerDocument.createElement("div");
  clipboard.append(range.cloneContents());
  const includesActions = clipboard.querySelector(".missing-in-rendered-actions") !== null;
  for (const control of clipboard.querySelectorAll(".missing-in-rendered-actions, button")) control.remove();
  wrapInSelectedAncestors(clipboard, range, root);
  toSemanticHtml(clipboard);

  event.preventDefault();
  event.clipboardData.setData("text/html", clipboard.innerHTML);
  if (includesActions) {
    for (const node of clipboard.querySelectorAll("br")) node.replaceWith("\n");
    for (const node of clipboard.querySelectorAll("p,h1,h2,h3,li,tr,ul,ol,table")) {
      if (node.nextSibling) node.after("\n");
    }
    for (const node of clipboard.querySelectorAll(":is(td,th) + :is(td,th)")) node.before("\t");
    event.clipboardData.setData("text/plain", clipboard.textContent);
  } else {
    event.clipboardData.setData("text/plain", withoutEditorCharacters(selection.toString()));
  }
}

/** A selection within a container is cloned without its outer ancestors, so add back those that carry semantics. */
function wrapInSelectedAncestors(clipboard: HTMLElement, range: Range, root: HTMLElement) {
  for (let node: Node | null = range.commonAncestorContainer; node && node !== root; node = node.parentNode) {
    if (!(node instanceof HTMLElement)) continue;

    const requiredChildren = ANCESTOR_WRAPPERS[node.tagName];
    const isFormattedLiteral = node.dataset.fontType !== undefined;
    const isListOrTableAncestor = requiredChildren !== undefined && clipboard.querySelector(requiredChildren) !== null;
    const isMixedParagraph =
      node.parentNode === root &&
      node.classList.contains("PARAGRAPH") &&
      clipboard.querySelector(":scope > :is(ul, ol, table)") !== null;
    const isWholeHeading =
      node.parentNode === root && headingTagOf(node) !== undefined && isFullySelected(clipboard, node);
    if (isFormattedLiteral || isListOrTableAncestor || isMixedParagraph || isWholeHeading) {
      const wrapper = node.cloneNode(false) as HTMLElement;
      wrapper.append(...clipboard.childNodes);
      clipboard.append(wrapper);
    }
  }
}

function toSemanticHtml(clipboard: HTMLElement) {
  const document = clipboard.ownerDocument;

  for (const literal of clipboard.querySelectorAll<HTMLElement>("[data-font-type]")) {
    const tag = FONT_TAGS[literal.dataset.fontType ?? ""];
    if (tag) {
      const formatted = document.createElement(tag);
      formatted.append(...literal.childNodes);
      literal.append(formatted);
    }
  }

  for (const block of clipboard.querySelectorAll<HTMLElement>(":scope > div")) {
    const headingTag = headingTagOf(block);
    if (headingTag) {
      const heading = document.createElement(headingTag);
      heading.append(...(block.querySelector(":scope > :is(h2, h3, h4)") ?? block).childNodes);
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
    textNodes.currentNode.textContent = withoutEditorCharacters(textNodes.currentNode.textContent ?? "");
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

function headingTagOf(block: HTMLElement) {
  return Object.entries(CLIPBOARD_HEADING_TAGS).find(([type]) => block.classList.contains(type))?.[1];
}

function isFullySelected(clipboard: HTMLElement, block: HTMLElement) {
  // Compare against the letter content only, since the block can also hold action controls (e.g. "Behold"/"Slett").
  const literals = Array.from(block.querySelectorAll("[data-literal-index]"), (literal) => literal.textContent ?? "");
  const selected = withoutEditorCharacters(clipboard.textContent ?? "").trim();
  return selected.length > 0 && selected === withoutEditorCharacters(literals.join("")).trim();
}

function withoutEditorCharacters(text: string) {
  return text.replaceAll(EDITOR_ONLY_CHARACTERS, "");
}
