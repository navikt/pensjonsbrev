import { type Caret, type ContainerPath, parsePathKey, pathKey } from "../model/paths";

export const ZERO_WIDTH_SPACE = "\u200B";

const PATH_ATTRIBUTE = "data-path";
const CONTENT_ATTRIBUTE = "data-content";
const CONTAINER_ATTRIBUTE = "data-container";

export function containerAttributes(path: ContainerPath) {
  return { [CONTAINER_ATTRIBUTE]: pathKey(path) };
}

export function containerSelector(path: ContainerPath): string {
  return `[${CONTAINER_ATTRIBUTE}="${pathKey(path)}"]`;
}

export function textAttributes(path: ContainerPath, content: number) {
  return { [PATH_ATTRIBUTE]: pathKey(path), [CONTENT_ATTRIBUTE]: content };
}

export function textSelector(at: Pick<Caret, "path" | "content">): string {
  return `[${PATH_ATTRIBUTE}="${pathKey(at.path)}"][${CONTENT_ATTRIBUTE}="${at.content}"]`;
}

function closestTextElement(node: Node | null): HTMLElement | null {
  const element = node instanceof Element ? node : node?.parentElement;
  return element?.closest<HTMLElement>(`[${PATH_ATTRIBUTE}]`) ?? null;
}

function caretOfElement(element: HTMLElement, offset: number): Caret | undefined {
  const path = parsePathKey(element.getAttribute(PATH_ATTRIBUTE) ?? "");
  const content = Number(element.getAttribute(CONTENT_ATTRIBUTE));
  if (!path || Number.isNaN(content)) return undefined;
  return { path, content, offset };
}

export function visibleLength(element: HTMLElement): number {
  const text = element.textContent ?? "";
  return text.startsWith(ZERO_WIDTH_SPACE) ? text.length - 1 : text.length;
}

function offsetWithin(element: HTMLElement, node: Node, offset: number): number {
  const range = document.createRange();
  range.setStart(element, 0);
  range.setEnd(node, offset);
  const raw = range.toString().length - ((element.textContent ?? "").startsWith(ZERO_WIDTH_SPACE) ? 1 : 0);
  return Math.min(Math.max(raw, 0), visibleLength(element));
}

function intersects(range: Range, node: Node): boolean {
  try {
    return range.intersectsNode(node);
  } catch {
    return false;
  }
}

export function caretAtDomSelection(): Caret | undefined {
  const selection = globalThis.getSelection();
  if (!selection || selection.rangeCount === 0) return undefined;
  const range = selection.getRangeAt(0);
  const element = closestTextElement(range.startContainer);
  return element ? caretOfElement(element, offsetWithin(element, range.startContainer, range.startOffset)) : undefined;
}

export function domSelection(root: HTMLElement): { anchor: Caret; focus: Caret } | undefined {
  const selection = globalThis.getSelection();
  if (!selection || selection.rangeCount === 0) return undefined;
  const range = selection.getRangeAt(0);
  if (range.collapsed) return undefined;

  const elements = [...root.querySelectorAll<HTMLElement>(`[${PATH_ATTRIBUTE}]`)].filter((el) => intersects(range, el));
  const directStart = closestTextElement(range.startContainer);
  const directEnd = closestTextElement(range.endContainer);
  const startElement = directStart && root.contains(directStart) ? directStart : elements.at(0);
  const endElement = directEnd && root.contains(directEnd) ? directEnd : elements.at(-1);
  if (!startElement || !endElement) return undefined;

  const startOffset =
    startElement === directStart ? offsetWithin(startElement, range.startContainer, range.startOffset) : 0;
  const endOffset =
    endElement === directEnd
      ? offsetWithin(endElement, range.endContainer, range.endOffset)
      : visibleLength(endElement);
  const anchor = caretOfElement(startElement, startOffset);
  const focus = caretOfElement(endElement, endOffset);
  if (!anchor || !focus || (startElement === endElement && startOffset === endOffset)) return undefined;
  return { anchor, focus };
}

export function placeCaret(element: HTMLElement, offset: number): void {
  const node = element.firstChild;
  if (!node) return;
  const shift = (element.textContent ?? "").startsWith(ZERO_WIDTH_SPACE) ? 1 : 0;
  const range = document.createRange();
  range.setStart(node, Math.min(offset + shift, node.textContent?.length ?? 0));
  range.collapse(true);
  const selection = globalThis.getSelection();
  selection?.removeAllRanges();
  selection?.addRange(range);
  element.focus();
}

export function selectAllIn(element: HTMLElement): void {
  const node = element.firstChild;
  if (!node) return;
  const range = document.createRange();
  range.selectNodeContents(node);
  const selection = globalThis.getSelection();
  selection?.removeAllRanges();
  selection?.addRange(range);
}

export function hasRangeSelection(): boolean {
  const selection = globalThis.getSelection();
  return !!selection && selection.rangeCount > 0 && !selection.getRangeAt(0).collapsed;
}
