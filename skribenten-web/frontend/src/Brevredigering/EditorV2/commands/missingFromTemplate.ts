import { blockPaths, caret, TITLE } from "../model/paths";
import { removeBlock } from "../model/structure";
import { type Command } from "../state/editorState";

export const keepMissingFromTemplate: Command<[block: number]> = (draft, index) => {
  const block = draft.redigertBrev.blocks[index];
  if (!block) return;
  block.id = null;
  block.missingFromTemplate = false;
};

export const removeMissingFromTemplate: Command<[block: number]> = (draft, index) => {
  const doc = draft.redigertBrev;
  if (!doc.blocks[index]) return;
  removeBlock(doc, index);
  const next = Math.min(index, doc.blocks.length - 1);
  const block = doc.blocks[next];
  draft.focus = caret((block && blockPaths(block, next)[0]) ?? TITLE);
};
