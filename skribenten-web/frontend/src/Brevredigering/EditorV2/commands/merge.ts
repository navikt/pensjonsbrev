import { type Draft } from "immer";

import { type DocumentV2 } from "~/types/brevV2";

import { removeAt } from "../model/collection";
import { type Caret, isListBlock, isTable, isTextBlock, resolve, type TextContainer } from "../model/paths";
import {
  appendContent,
  breakOutOfList,
  endOf,
  isEmptyContainer,
  linearOf,
  locate,
  normalize,
  removeContainer,
} from "../model/structure";
import { contentCollection, isEmptyText, isLiteral, isNewLine } from "../model/text";
import { type Command, type EditorStateV2 } from "../state/editorState";
import { focusEnd, focusIn } from "./focus";

export type Direction = "backward" | "forward";

export const merge: Command<[at: Caret, direction: Direction]> = (draft, at, direction) => {
  const container = resolve(draft.redigertBrev, at.path);
  if (!container) return;
  const linear = linearOf(draft.redigertBrev, at);
  const atBoundary = direction === "backward" ? linear === 0 : linear === endOf(container);
  if (atBoundary) {
    mergeContainers(draft, at, direction);
  } else {
    mergeWithinContainer(draft, container, at, direction, linear);
  }
};

function mergeWithinContainer(
  draft: Draft<EditorStateV2>,
  container: TextContainer,
  at: Caret,
  direction: Direction,
  linear: number,
) {
  const current = container.content[at.content];
  const neighbourIndex = direction === "backward" ? at.content - 1 : at.content + 1;
  const neighbour = container.content[neighbourIndex];
  if (isNewLine(neighbour)) {
    removeAt(contentCollection(container), neighbourIndex);
    normalize(container);
    focusIn(draft, container, direction === "backward" ? linear - 1 : linear);
  } else if (isLiteral(neighbour) && isLiteral(current)) {
    if (isEmptyText(current)) removeAt(contentCollection(container), at.content);
    normalize(container);
    focusIn(draft, container, linear);
  }
}

function mergeContainers(draft: Draft<EditorStateV2>, at: Caret, direction: Direction) {
  const doc = draft.redigertBrev;
  const path = at.path;
  const container = resolve(doc, path);
  if (!container) return;

  switch (path.kind) {
    case "title":
    case "header":
    case "cell":
      return;
    case "item": {
      if (direction === "backward" && isEmptyContainer(container)) {
        focusIn(draft, breakOutOfList(doc, path));
        return;
      }
      const neighbour = resolve(doc, { ...path, item: path.item + (direction === "backward" ? -1 : 1) });
      if (neighbour) join(draft, direction === "backward" ? [neighbour, container] : [container, neighbour]);
      return;
    }
    case "block": {
      if (direction === "backward") {
        const previous = lastContainerOfBlock(doc, path.block - 1);
        if (previous) {
          join(draft, [previous, container]);
        } else if (isTable(doc.blocks[path.block - 1]) && isEmptyContainer(container)) {
          removeContainer(doc, path);
          focusEnd(draft, lastContainerOfBlock(doc, path.block - 1, true));
        }
      } else {
        const next = doc.blocks[path.block + 1];
        if (isTextBlock(next)) {
          join(draft, [container, next]);
        } else if (next && isEmptyContainer(container)) {
          removeContainer(doc, path);
          focusIn(draft, resolve(doc, { kind: "block", block: path.block }) ?? firstContainerOfBlock(doc, path.block));
        }
      }
      return;
    }
  }
}

function lastContainerOfBlock(doc: DocumentV2, index: number, includeTables = false): TextContainer | undefined {
  const block = doc.blocks[index];
  if (isTextBlock(block)) return block;
  if (isListBlock(block)) return block.items.at(-1);
  if (includeTables && isTable(block)) return block.rows.at(-1)?.cells.at(-1) ?? block.header.colSpec.at(-1);
  return undefined;
}

function firstContainerOfBlock(doc: DocumentV2, index: number): TextContainer | undefined {
  const block = doc.blocks[index];
  if (isTextBlock(block)) return block;
  if (isListBlock(block)) return block.items[0];
  if (isTable(block)) return block.header.colSpec[0] ?? block.rows[0]?.cells[0];
  return undefined;
}

export function join(draft: Draft<EditorStateV2>, [first, second]: [TextContainer, TextContainer]) {
  const doc = draft.redigertBrev;
  const remove = (container: TextContainer) => {
    const path = locate(doc, container);
    if (path) removeContainer(doc, path);
  };
  if (isEmptyContainer(second)) {
    remove(second);
    normalize(first);
    focusEnd(draft, first);
  } else if (isEmptyContainer(first)) {
    remove(first);
    focusIn(draft, second, 0);
  } else {
    const linear = endOf(first);
    appendContent(first, second);
    remove(second);
    focusIn(draft, first, linear);
  }
}
