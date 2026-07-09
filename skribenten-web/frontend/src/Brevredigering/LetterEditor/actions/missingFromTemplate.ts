import { TITLE_INDEX } from "~/types/brevbakerTypes";

import { type Action, withPatches } from "../lib/actions";
import { type LetterEditorState } from "../model/state";
import { removeElements } from "./common";


export const removeMissingFromTemplateBlock: Action<LetterEditorState, [blockIndex: number]> = withPatches(
  (draft, blockIndex) => {
    const blocks = draft.redigertBrev.blocks;
    if (blocks[blockIndex] == null) return;

    removeElements(blockIndex, 1, { content: blocks, deletedContent: draft.redigertBrev.deletedBlocks, id: null });
    draft.saveStatus = "DIRTY";

    const nextBlockIndex = Math.min(blockIndex, blocks.length - 1);
    draft.focus =
      nextBlockIndex >= 0
        ? { blockIndex: nextBlockIndex, contentIndex: 0, cursorPosition: 0 }
        : { blockIndex: TITLE_INDEX, contentIndex: 0, cursorPosition: 0 };
  },
);


export const keepMissingFromTemplateBlock: Action<LetterEditorState, [blockIndex: number]> = withPatches(
  (draft, blockIndex) => {
    const block = draft.redigertBrev.blocks[blockIndex];
    if (block == null) return;

    block.id = null;
    block.missingFromTemplate = false;
    draft.saveStatus = "DIRTY";
  },
);
