import { type BlockV2, type DocumentV2, type TextBlockV2, type TextV2 } from "~/types/brevV2";

import { type Collection, collection, insertAt, removeAt, removeFrom, transfer } from "./collection";
import { newItem, newList, newParagraph } from "./factories";
import {
  type Caret,
  type ContainerPath,
  caret,
  containers,
  isListBlock,
  listAt,
  resolve,
  type TextContainer,
} from "./paths";
import {
  contentCollection,
  ensureEditable,
  isEmptyContent,
  isLiteral,
  linearLength,
  linearOffset,
  mergeAdjacentLiterals,
  positionAt,
  setText,
  span,
  splitLiteral,
  textOf,
} from "./text";

export function blockCollection(doc: DocumentV2): Collection<BlockV2> {
  return collection(doc.blocks, doc.deletedBlocks, null);
}

export function locate(doc: DocumentV2, container: TextContainer): ContainerPath | undefined {
  return containers(doc).find((path) => resolve(doc, path) === container);
}

export function caretIn(doc: DocumentV2, container: TextContainer, linear: number): Caret | undefined {
  const path = locate(doc, container);
  if (!path) return undefined;
  const position = positionAt(container.content, linear);
  return caret(path, position.content, position.offset);
}

export function linearOf(doc: DocumentV2, at: Caret): number {
  const container = resolve(doc, at.path);
  return container ? linearOffset(container.content, at) : 0;
}

export function normalize(container: TextContainer): void {
  mergeAdjacentLiterals(container);
  ensureEditable(container);
}

export function joinAdjacentLists(doc: DocumentV2, index: number): void {
  const first = doc.blocks[index - 1];
  const second = doc.blocks[index];
  if (isListBlock(first) && isListBlock(second) && first.type === second.type) {
    const items = collection(first.items, first.deletedItems, first);
    const from = collection(second.items, second.deletedItems, second);
    transfer(from, 0, from.items.length, items, items.items.length);
    removeAt(blockCollection(doc), index);
  }
}

export function removeBlock(doc: DocumentV2, index: number): void {
  removeAt(blockCollection(doc), index);
  joinAdjacentLists(doc, index);
}

export function removeContainer(doc: DocumentV2, path: ContainerPath): void {
  switch (path.kind) {
    case "block":
      removeBlock(doc, path.block);
      return;
    case "item": {
      const list = listAt(doc, path.block);
      if (!list) return;
      removeAt(collection(list.items, list.deletedItems, list), path.item);
      if (list.items.length === 0) removeBlock(doc, path.block);
      return;
    }
    case "title":
    case "header":
    case "cell":
      return;
  }
}

export function splitContent(container: TextContainer, at: Caret): TextV2[] {
  const source = contentCollection(container);
  const position = positionAt(container.content, linearOffset(container.content, at));
  const text = container.content[position.content];
  const tail: TextContainer = { content: [], deletedContent: [], id: null };
  const destination = contentCollection(tail);
  if (isLiteral(text) && position.offset > 0) {
    const remainder = splitLiteral(text, position.offset);
    transfer(source, position.content + 1, source.items.length - position.content - 1, destination, 0);
    insertAt(destination, 0, remainder);
  } else {
    transfer(source, position.content, source.items.length - position.content, destination, 0);
  }
  normalize(container);
  normalize(tail);
  return tail.content;
}

export function appendContent(target: TextContainer, source: TextContainer): void {
  const from = contentCollection(source);
  transfer(from, 0, from.items.length, contentCollection(target), target.content.length);
  normalize(target);
}

export function insertSibling(
  doc: DocumentV2,
  path: ContainerPath,
  at: "before" | "after",
  content?: TextV2[],
): TextContainer | undefined {
  const shift = at === "after" ? 1 : 0;
  switch (path.kind) {
    case "block": {
      const paragraph = newParagraph(content);
      insertAt(blockCollection(doc), path.block + shift, paragraph);
      return paragraph;
    }
    case "item": {
      const list = listAt(doc, path.block);
      if (!list) return undefined;
      const item = newItem(content);
      insertAt(collection(list.items, list.deletedItems, list), path.item + shift, item);
      return item;
    }
    case "title":
    case "header":
    case "cell":
      return undefined;
  }
}

export function breakOutOfList(
  doc: DocumentV2,
  path: Extract<ContainerPath, { kind: "item" }>,
): TextBlockV2 | undefined {
  const list = listAt(doc, path.block);
  if (!list) return undefined;
  const items = collection(list.items, list.deletedItems, list);
  const [item] = removeAt(items, path.item);
  const paragraph = newParagraph(item?.content);
  const blocks = blockCollection(doc);
  if (path.item === 0) {
    insertAt(blocks, path.block, paragraph);
    if (list.items.length === 0) removeAt(blocks, path.block + 1);
  } else {
    const tail = removeFrom(items, path.item);
    insertAt(blocks, path.block + 1, paragraph);
    if (tail.length > 0) insertAt(blocks, path.block + 2, newList(list.type, tail));
  }
  return paragraph;
}

export function isEmptyContainer(container: TextContainer | undefined): boolean {
  return container !== undefined && isEmptyContent(container.content);
}

export function endOf(container: TextContainer): number {
  return linearLength(container.content);
}

export function deleteRange(container: TextContainer, from: number, to: number): void {
  const target = contentCollection(container);
  let start = 0;
  for (let index = 0; index < target.items.length; ) {
    const text = target.items[index];
    const end = start + span(text);
    const overlaps = end > start ? from < end && to > start : from < start && start < to;
    if (overlaps && isLiteral(text) && (from > start || to < end)) {
      const value = textOf(text);
      setText(text, value.slice(0, Math.max(0, from - start)) + value.slice(Math.max(0, to - start)));
      index++;
    } else if (overlaps) {
      removeAt(target, index);
    } else {
      index++;
    }
    start = end;
  }
  normalize(container);
}

export function countMissingFromTemplate(doc: DocumentV2): number {
  return doc.blocks.filter((block) => block.missingFromTemplate).length;
}
