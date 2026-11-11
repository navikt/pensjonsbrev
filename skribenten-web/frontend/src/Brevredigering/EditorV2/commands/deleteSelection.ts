import { type Draft } from "immer";

import { type BlockV2, type DocumentV2 } from "~/types/brevV2";

import { insertAt } from "../model/collection";
import { newParagraph } from "../model/factories";
import {
  blockIndexOf,
  blockPaths,
  type Caret,
  type ContainerPath,
  compareCarets,
  comparePaths,
  containers,
  isTextBlock,
  resolve,
  type Selection,
  samePath,
  type TextContainer,
} from "../model/paths";
import {
  blockCollection,
  deleteRange,
  endOf,
  linearOf,
  locate,
  removeBlock,
  removeContainer,
} from "../model/structure";
import { type Command, type EditorStateV2 } from "../state/editorState";
import { focusEnd, focusIn } from "./focus";
import { join } from "./merge";

type Boundary = { at: Caret; block: number; container: TextContainer; linear: number };

export const deleteSelection: Command<[selection: Selection]> = (draft, { anchor, focus }) => {
  const doc = draft.redigertBrev;
  const [start, end] = compareCarets(anchor, focus) <= 0 ? [anchor, focus] : [focus, anchor];
  const first = resolve(doc, start.path);
  const last = resolve(doc, end.path);
  if (!first || !last) return;

  if (samePath(start.path, end.path)) {
    const from = linearOf(doc, start);
    const to = linearOf(doc, end);
    if (from === to) return;
    deleteRange(first, from, to);
    focusIn(draft, first, from);
    return;
  }

  const startBlock = blockIndexOf(start.path);
  const endBlock = blockIndexOf(end.path);
  if (startBlock === undefined || endBlock === undefined) return;
  const from: Boundary = { at: start, block: startBlock, container: first, linear: linearOf(doc, start) };
  const to: Boundary = { at: end, block: endBlock, container: last, linear: linearOf(doc, end) };

  const covered = coveredBlocks(doc, from, to);
  deleteRange(first, from.linear, endOf(first));
  deleteRange(last, 0, to.linear);
  removeBetween(doc, from, to);
  for (const block of covered) {
    const index = doc.blocks.indexOf(block);
    if (index !== -1) removeBlock(doc, index);
  }

  settle(draft, from, to);
};

function coveredBlocks(doc: DocumentV2, from: Boundary, to: Boundary) {
  const startsBlock = isBlockBoundary(doc, from, "start");
  const endsBlock = isBlockBoundary(doc, to, "end");
  const blocks: BlockV2[] = [];
  if (startsBlock && (to.block > from.block || endsBlock)) blocks.push(doc.blocks[from.block]);
  if (endsBlock && to.block > from.block) blocks.push(doc.blocks[to.block]);
  return blocks;
}

function isBlockBoundary(doc: DocumentV2, boundary: Boundary, side: "start" | "end"): boolean {
  const block = doc.blocks[boundary.block];
  if (!block || isTextBlock(block)) return false;
  const paths = blockPaths(block, boundary.block);
  const edge = side === "start" ? paths[0] : paths.at(-1);
  const linear = side === "start" ? 0 : endOf(boundary.container);
  return edge !== undefined && samePath(edge, boundary.at.path) && boundary.linear === linear;
}

function removeBetween(doc: DocumentV2, from: Boundary, to: Boundary) {
  const between = containers(doc)
    .filter((path) => comparePaths(from.at.path, path) < 0 && comparePaths(path, to.at.path) < 0)
    .reverse();
  for (const path of between) {
    const block = blockIndexOf(path) ?? from.block;
    if (block > from.block && block < to.block) {
      if (isFirstOfBlock(doc, path, block)) removeBlock(doc, block);
    } else if (path.kind === "item") {
      removeContainer(doc, path);
    } else {
      const container = resolve(doc, path);
      if (container) deleteRange(container, 0, endOf(container));
    }
  }
}

function isFirstOfBlock(doc: DocumentV2, path: ContainerPath, index: number): boolean {
  const block = doc.blocks[index];
  const first = block && blockPaths(block, index)[0];
  return first !== undefined && samePath(first, path);
}

function isJoinable(path: ContainerPath | undefined): boolean {
  return path?.kind === "block" || path?.kind === "item";
}

function settle(draft: Draft<EditorStateV2>, from: Boundary, to: Boundary) {
  const doc = draft.redigertBrev;
  const first = locate(doc, from.container);
  const last = locate(doc, to.container);
  if (isJoinable(first) && isJoinable(last)) {
    join(draft, [from.container, to.container]);
  } else if (first) {
    focusIn(draft, from.container, from.linear);
  } else if (last) {
    focusIn(draft, to.container, 0);
  } else {
    if (doc.blocks.length === 0) insertAt(blockCollection(doc), 0, newParagraph());
    focusAround(draft, from.block);
  }
}

function focusAround(draft: Draft<EditorStateV2>, index: number) {
  const doc = draft.redigertBrev;
  const following = doc.blocks[index];
  const preceding = doc.blocks.at(-1);
  if (following) {
    const path = blockPaths(following, index)[0];
    focusIn(draft, path && resolve(doc, path));
  } else if (preceding) {
    const path = blockPaths(preceding, doc.blocks.length - 1).at(-1);
    focusEnd(draft, path && resolve(doc, path));
  }
}
