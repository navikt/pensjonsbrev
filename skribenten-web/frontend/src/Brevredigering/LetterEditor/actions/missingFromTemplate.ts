import { TITLE_INDEX } from "~/types/brevbakerTypes";

import { type Action, withPatches } from "../lib/actions";
import { type LetterEditorState } from "../model/state";
import { removeElements } from "./common";

/**
 * "Slett": removes the whole block using the same mechanism as other whole-block removals
 * (e.g. mergeBlocks) - splices it out of `blocks` and records its id in `deletedBlocks`.
 */
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

/**
 * "Behold": keeps the block's content but detaches it from the old template id, so it's
 * treated as a brand new block (isNew()) on the next merge and stops being flagged.
 */
export const keepMissingFromTemplateBlock: Action<LetterEditorState, [blockIndex: number]> = withPatches(
  (draft, blockIndex) => {
    const block = draft.redigertBrev.blocks[blockIndex];
    if (block == null) return;

    block.id = null;
    block.missingFromTemplate = false;
    draft.saveStatus = "DIRTY";
  },
);
